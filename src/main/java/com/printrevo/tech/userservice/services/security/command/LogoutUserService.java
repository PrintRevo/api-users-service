package com.printrevo.tech.userservice.services.security.command;

import com.printrevo.tech.commonsecurity.helpers.session.SessionHelper;
import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.services.security.command.instructions.LogoutUserCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LogoutUserService extends CommandBaseService<LogoutUserCommand, Void> {

    private final SessionHelper sessionHelper;
    private final String publicClientId;

    public LogoutUserService(
            SessionHelper sessionHelper, @Value("${security.pkce.client-id}") String publicClientId) {
        this.sessionHelper = sessionHelper;
        this.publicClientId = publicClientId;
    }

    @Override
    public Result<Void> execute(LogoutUserCommand command) {
        try {
            if (command.getPublicClient())
                sessionHelper.logoutUser(command.getRefreshToken(), publicClientId);
            else sessionHelper.logoutUser(command.getRefreshToken());
            return new Result.ResultBuilder<Void>()
                    .withMessage("User logged out successfully")
                    .build();
        } catch (Exception e) {
            return new Result.ResultBuilder<Void>()
                    .withStatus(new ResultStatus(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage()))
                    .build();
        }
    }
}
