package com.printrevo.tech.userservice.services.security.command;

import com.printrevo.tech.common.exception.DaExternalServiceException;
import com.printrevo.tech.commonsecurity.clients.KeycloakAPIClient;
import com.printrevo.tech.commonsecurity.config.SecurityProperties;
import com.printrevo.tech.commonsecurity.constants.AuthSource;
import com.printrevo.tech.commonsecurity.constants.AuthSourceContextHolder;
import com.printrevo.tech.commonsecurity.dto.keycloak.ResetUserPasswordDto;
import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.services.security.command.instructions.SetUserKeycloakPasswordCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class SetUserKeycloakPasswordService
        extends CommandBaseService<SetUserKeycloakPasswordCommand, Void> {

    private final SecurityProperties securityProperties;
    private final KeycloakAPIClient keycloakAPIClient;

    @Override
    public Result<Void> execute(SetUserKeycloakPasswordCommand command) {

        try {
            var updateUserPasswordDTO = getUpdateUserPasswordDTO(command);
            AuthSourceContextHolder.setValue(AuthSource.CLIENT);
            keycloakAPIClient.resetUserPassword(securityProperties.getClient().getRealm(), command.getUserKeycloakId()
                    , updateUserPasswordDTO);
        } catch (DaExternalServiceException e) {
            var httpStatus = HttpStatus.resolve(Integer.parseInt(e.getCode()));
            if (isNull(httpStatus)) httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            if (httpStatus.is5xxServerError())
                log.error("Error occurred while updating user password", e);
            return new Result.ResultBuilder<Void>()
                    .withStatus(new ResultStatus(httpStatus, e.getLocalizedMessage()))
                    .build();
        } finally {
            AuthSourceContextHolder.clearValue();
        }

        return new Result.ResultBuilder<Void>()
                .build();
    }

    private ResetUserPasswordDto getUpdateUserPasswordDTO(
            SetUserKeycloakPasswordCommand command) {
        var dto = new ResetUserPasswordDto();
        dto.setTemporary(command.getIsTemporary());
        dto.setValue(command.getPassword());
        return dto;
    }
}
