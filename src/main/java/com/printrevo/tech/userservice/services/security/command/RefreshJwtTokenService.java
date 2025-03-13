package com.printrevo.tech.userservice.services.security.command;

import com.printrevo.tech.commonsecurity.constants.AuthSource;
import com.printrevo.tech.commonsecurity.constants.AuthSourceContextHolder;
import com.printrevo.tech.commonsecurity.dto.AccessToken;
import com.printrevo.tech.commonsecurity.helpers.session.SessionHelper;
import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.services.security.command.instructions.RefreshJwtTokenCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RefreshJwtTokenService
        extends CommandBaseService<RefreshJwtTokenCommand, AccessToken> {

    private final SessionHelper sessionHelper;
    private final String publicClientId;

    public RefreshJwtTokenService(SessionHelper sessionHelper, @Value("${security.pkce.client-id}") String publicClientId) {
        this.sessionHelper = sessionHelper;
        this.publicClientId = publicClientId;
    }

    @Override
    public Result<AccessToken> execute(RefreshJwtTokenCommand command) {
        try {
            AuthSourceContextHolder.setValue(AuthSource.SESSION);
            AccessToken refreshedToken;
            if (command.getPublicClient())
                refreshedToken = sessionHelper.refreshSessionToken(command.getRefreshToken(), publicClientId);
            else refreshedToken = sessionHelper.refreshSessionToken(command.getRefreshToken());
            return new Result.ResultBuilder<AccessToken>()
                    .withMessage("Token refreshed successfully")
                    .withData(refreshedToken)
                    .build();
        } catch (Exception e) {
            return new Result.ResultBuilder<AccessToken>()
                    .withStatus(new ResultStatus(HttpStatus.UNAUTHORIZED, e.getLocalizedMessage()))
                    .build();
        }
    }
}
