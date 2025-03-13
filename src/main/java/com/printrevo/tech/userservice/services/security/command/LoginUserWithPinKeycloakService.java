package com.printrevo.tech.userservice.services.security.command;

import com.printrevo.tech.common.exception.DaExternalServiceException;
import com.printrevo.tech.commonsecurity.clients.KeycloakAPIClient;
import com.printrevo.tech.commonsecurity.config.SecurityProperties;
import com.printrevo.tech.commonsecurity.dto.AccessToken;
import com.printrevo.tech.commonsecurity.dto.keycloak.LoginWithPinDto;
import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.services.security.command.instructions.LoginUserWithPinKeycloakCommand;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginUserWithPinKeycloakService
        extends CommandBaseService<LoginUserWithPinKeycloakCommand, AccessToken> {

    private final SecurityProperties securityProperties;
    private final KeycloakAPIClient keycloakAPIClient;

    @Override
    public Result<AccessToken> execute(LoginUserWithPinKeycloakCommand command) {
        try {
            var loginDto = new LoginWithPinDto();
            loginDto.setClientId(securityProperties.getPinAuthenticator().getClientId());
            loginDto.setClientSecret(securityProperties.getPinAuthenticator().getClientSecret());
            loginDto.setIdentifier(command.getIdentifier());
            loginDto.setPin(command.getPin());

            var token = keycloakAPIClient.loginWithPin(securityProperties.getPinAuthenticator().getRealm(), loginDto);

            return new Result.ResultBuilder<AccessToken>()
                    .withData(token)
                    .build();

        } catch (DaExternalServiceException e) {

            var status = HttpStatus.resolve(Integer.parseInt(e.getCode()));
            var message = e.getMessage();

            if (status == HttpStatus.UNAUTHORIZED) {
                try {
                    var errorMap = new ObjectMapper().readValue(message, Map.class);
                    message = String.format("%s: %s", errorMap.get("error"), errorMap.get("error_description"));
                } catch (JsonProcessingException ex) {
                    message = "Invalid credentials";
                }
            } else message = "An unknown error occurred";

            return new Result.ResultBuilder<AccessToken>()
                    .withStatus(new ResultStatus(status, message))
                    .build();
        }
    }
}
