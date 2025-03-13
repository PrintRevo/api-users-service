package com.printrevo.tech.userservice.services.security.command;

import com.printrevo.tech.commonsecurity.config.SecurityProperties;
import com.printrevo.tech.commonsecurity.constants.AuthSource;
import com.printrevo.tech.commonsecurity.security.providers.pin.PinHelper;
import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.services.security.command.instructions.RemoveKeycloakUserPinCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveKeycloakUserPinService
        extends CommandBaseService<RemoveKeycloakUserPinCommand, Void> {

    private final SecurityProperties securityProperties;
    private final PinHelper pinClient;

    @Override
    public Result<Void> execute(RemoveKeycloakUserPinCommand command) {

        try {
            pinClient.removePin(
                    securityProperties.getClient().getRealm(),
                    AuthSource.CLIENT,
                    command.getSecurityUser());
        } catch (Exception exception) {
            return new Result.ResultBuilder<Void>()
                    .withStatus(new ResultStatus(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage()))
                    .build();
        }

        return new Result.ResultBuilder<Void>()
                .withMessage("Pin removed successfully!")
                .build();
    }
}
