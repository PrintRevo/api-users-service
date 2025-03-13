package com.printrevo.tech.userservice.services.security.command.flowservices;

import com.printrevo.tech.commonsecurity.constants.PinIdentifier;
import com.printrevo.tech.commonsecurity.constants.PinStatus;
import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.services.security.command.UpdateKeycloakUserPinService;
import com.printrevo.tech.userservice.services.security.command.flowservices.instructions.ResetUserPinFlowCommand;
import com.printrevo.tech.userservice.services.security.command.instructions.UpdateKeycloakUserPinCommand;
import com.printrevo.tech.userservice.services.security.query.GetKeycloakUserByIdService;
import com.printrevo.tech.userservice.services.security.query.instructions.GetKeycloakUserByIdQuery;
import com.printrevo.tech.userservice.services.users.query.GetUserByRefIdService;
import com.printrevo.tech.userservice.services.users.query.instructions.GetUserByRefIdQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class ResetUserPinFlowService extends CommandBaseService<ResetUserPinFlowCommand, String> {

    private final UpdateKeycloakUserPinService updateKeycloakUserPinService;
    private final GetKeycloakUserByIdService getKeycloakUserByIdService;
    private final GetUserByRefIdService getUserByRefIdService;

    @Override
    public Result<String> execute(ResetUserPinFlowCommand command) {

        var userResult = getUserByRefIdService.decorate(
                new GetUserByRefIdQuery(command.getUserRefId()));

        if (userResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(userResult)
                    .build();

        if (userResult.getData().getAuthServerId() == null)
            return new Result.ResultBuilder<String>()
                    .withStatus(new ResultStatus(HttpStatus.FORBIDDEN, "User does not have a pin"))
                    .build();

        if (isNull(userResult.getData().getPinStatus()) ||
                userResult.getData().getPinStatus().equals(PinStatus.INACTIVE))
            return new Result.ResultBuilder<String>()
                    .withStatus(new ResultStatus(HttpStatus.FORBIDDEN, "User does not have a pin"))
                    .build();

        var securityUserByIdResult = getKeycloakUserByIdService.decorate(
                new GetKeycloakUserByIdQuery(userResult.getData().getAuthServerId()));

        if (securityUserByIdResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(securityUserByIdResult)
                    .build();

        UpdateKeycloakUserPinCommand updateCommand = new UpdateKeycloakUserPinCommand();
        updateCommand.setSecurityUser(securityUserByIdResult.getData());
        updateCommand.setPin(command.getPin());
        updateCommand.setPinIdentifier(PinIdentifier.EMAIL);
        updateCommand.setPinStatus(command.getStatus());
        updateCommand.setEmail(userResult.getData().getEmail());

        var updatePinResult = updateKeycloakUserPinService.decorate(updateCommand);

        if (updatePinResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(updatePinResult)
                    .build();

        return new Result.ResultBuilder<String>()
                .withData(userResult.getData().getId())
                .withMessage("Pin reset successfully")
                .build();
    }
}
