package com.printrevo.tech.userservice.services.security.command;

import com.printrevo.tech.commonsecurity.config.SecurityProperties;
import com.printrevo.tech.commonsecurity.constants.AuthSource;
import com.printrevo.tech.commonsecurity.constants.PinIdentifier;
import com.printrevo.tech.commonsecurity.security.providers.pin.PinHelper;
import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.services.security.command.instructions.UpdateKeycloakUserPinCommand;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UpdateKeycloakUserPinService
        extends CommandBaseService<UpdateKeycloakUserPinCommand, Void> {

    private final SecurityProperties securityProperties;
    private final PinHelper pinClient;

    public UpdateKeycloakUserPinService(SecurityProperties securityProperties, PinHelper pinClient) {
        this.securityProperties = securityProperties;
        this.pinClient = pinClient;
    }

    @Override
    public Result<Void> execute(UpdateKeycloakUserPinCommand command) {

        try {

            pinClient.setPin(
                    securityProperties.getClient().getRealm(),
                    AuthSource.CLIENT,
                    command.getSecurityUser(),
                    getIdentifier(command),
                    command.getPin(),
                    command.getPinStatus()
            );

        } catch (IllegalArgumentException illegalArgumentException) {

            return new Result.ResultBuilder<Void>()
                    .withStatus(new ResultStatus(HttpStatus.BAD_REQUEST, illegalArgumentException.getMessage()))
                    .build();

        } catch (Exception exception) {

            return new Result.ResultBuilder<Void>()
                    .withStatus(new ResultStatus(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage()))
                    .build();
        }

        return new Result.ResultBuilder<Void>()
                .withMessage("Pin updated successfully!")
                .build();
    }

    private Map.Entry<PinIdentifier, String> getIdentifier(UpdateKeycloakUserPinCommand command) {
        if (command.getPinIdentifier().equals(PinIdentifier.PHONE_NUMBER))
            return Map.entry(PinIdentifier.PHONE_NUMBER, command.getPhoneNumber());
        else
            return Map.entry(PinIdentifier.EMAIL, command.getEmail());
    }
}
