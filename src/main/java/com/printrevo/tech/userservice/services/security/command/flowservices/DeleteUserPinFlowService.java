package com.printrevo.tech.userservice.services.security.command.flowservices;

import com.printrevo.tech.commonsecurity.constants.PinStatus;
import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.services.security.command.RemoveKeycloakUserPinService;
import com.printrevo.tech.userservice.services.security.command.flowservices.instructions.DeleteUserPinFlowCommand;
import com.printrevo.tech.userservice.services.security.command.instructions.RemoveKeycloakUserPinCommand;
import com.printrevo.tech.userservice.services.security.query.GetKeycloakUserByIdService;
import com.printrevo.tech.userservice.services.security.query.instructions.GetKeycloakUserByIdQuery;
import com.printrevo.tech.userservice.services.users.command.flowservices.UpdatePersonalUserDetailsFlowService;
import com.printrevo.tech.userservice.services.users.command.flowservices.instructions.UpdateUserFlowCommand;
import com.printrevo.tech.userservice.services.users.query.GetUserByRefIdService;
import com.printrevo.tech.userservice.services.users.query.instructions.GetUserByRefIdQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DeleteUserPinFlowService extends CommandBaseService<DeleteUserPinFlowCommand, String> {

    private final UpdatePersonalUserDetailsFlowService updatePersonalUserDetailsFlowService;
    private final RemoveKeycloakUserPinService removeKeycloakUserPinService;
    private final GetKeycloakUserByIdService getKeycloakUserByIdService;
    private final GetUserByRefIdService getUserByRefIdService;

    @Override
    public Result<String> execute(DeleteUserPinFlowCommand command) {

        var userResult = getUserByRefIdService.decorate(
                new GetUserByRefIdQuery(command.getUserRefId()));

        if (userResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(userResult)
                    .build();

        if (!Objects.equals(PinStatus.ACTIVE, userResult.getData().getPinStatus()))
            return new Result.ResultBuilder<String>()
                    .withStatus(new ResultStatus(HttpStatus.FORBIDDEN, "Pin not active"))
                    .build();

        var securityUserByIdResult = getKeycloakUserByIdService.decorate(
                new GetKeycloakUserByIdQuery(userResult.getData().getAuthServerId()));

        if (securityUserByIdResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(securityUserByIdResult)
                    .build();

        var removeUserPinResult = removeKeycloakUserPinService.decorate(
                new RemoveKeycloakUserPinCommand(securityUserByIdResult.getData()));

        if (removeUserPinResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(removeUserPinResult)
                    .build();

        var updateUserCommand = new UpdateUserFlowCommand();
        updateUserCommand.setUserRefId(userResult.getData().getId());
        updateUserCommand.setPinStatus(PinStatus.INACTIVE);

        var updatePersonalUserDetailsResult =
                updatePersonalUserDetailsFlowService.decorate(updateUserCommand);

        if (updatePersonalUserDetailsResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(updatePersonalUserDetailsResult)
                    .build();

        return new Result.ResultBuilder<String>()
                .withData(userResult.getData().getId())
                .withMessage("Pin removed successfully!")
                .build();
    }
}
