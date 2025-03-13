package com.printrevo.tech.userservice.services.security.command;

import com.printrevo.tech.common.exception.DaExternalServiceException;
import com.printrevo.tech.commonsecurity.dto.AccessToken;
import com.printrevo.tech.commonsecurity.helpers.session.SessionHelper;
import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.services.security.command.flowservices.instructions.LoginUserFlowCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginUserKeycloakService
        extends CommandBaseService<LoginUserFlowCommand, AccessToken> {

    private final SessionHelper sessionHelper;

    @Override
    public Result<AccessToken> execute(LoginUserFlowCommand command) {
        try {
            var token = sessionHelper
                    .loginUserWithEmailAndPassword(command.getEmail(), command.getPassword());
            return new Result.ResultBuilder<AccessToken>()
                    .withData(token)
                    .build();
        } catch (DaExternalServiceException e) {
            log.error("Error while logging in user: {}", e.getLocalizedMessage());
            return new Result.ResultBuilder<AccessToken>()
                    .withStatus(new ResultStatus(HttpStatus.resolve(Integer.parseInt(e.getCode()))
                            , e.getLocalizedMessage()))
                    .build();
        } catch (Exception e) {
            return new Result.ResultBuilder<AccessToken>()
                    .withStatus(new ResultStatus(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Error while logging in user. Try again later."))
                    .build();
        }
    }
}
