package com.printrevo.tech.userservice.services.security.command.flowservices;

import com.printrevo.tech.commonsecurity.constants.PinIdentifier;
import com.printrevo.tech.commonsecurity.constants.PinStatus;
import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.services.security.command.UpdateKeycloakUserPinService;
import com.printrevo.tech.userservice.services.security.command.flowservices.instructions.CreateUserPinFlowCommand;
import com.printrevo.tech.userservice.services.security.command.instructions.UpdateKeycloakUserPinCommand;
import com.printrevo.tech.userservice.services.security.query.GetKeycloakUserByIdService;
import com.printrevo.tech.userservice.services.security.query.instructions.GetKeycloakUserByIdQuery;
import com.printrevo.tech.userservice.services.users.command.flowservices.UpdatePersonalUserDetailsFlowService;
import com.printrevo.tech.userservice.services.users.command.flowservices.instructions.UpdateUserFlowCommand;
import com.printrevo.tech.userservice.services.users.query.GetUserByRefIdService;
import com.printrevo.tech.userservice.services.users.query.instructions.GetUserByRefIdQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class CreateUserPinFlowService extends CommandBaseService<CreateUserPinFlowCommand, String> {

    private final UpdatePersonalUserDetailsFlowService updatePersonalUserDetailsFlowService;
    private final UpdateKeycloakUserPinService updateKeycloakUserPinService;
    private final GetKeycloakUserByIdService getKeycloakUserByIdService;
    private final GetUserByRefIdService getUserByRefIdService;

    @Override
    public Result<String> execute(CreateUserPinFlowCommand command) {

        var userResult = getUserByRefIdService.decorate(
                new GetUserByRefIdQuery(command.getUserRefId()));

        if (userResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(userResult)
                    .build();

        var updateUserPinCommand = new UpdateKeycloakUserPinCommand();

        if (command.getPinIdentifier().equals(PinIdentifier.EMAIL)) {

            var emailVerified = userResult.getData().getEmailVerified();
            if (emailVerified == null || !emailVerified)
                return new Result.ResultBuilder<String>()
                        .withStatus(new ResultStatus(HttpStatus.FORBIDDEN, "Email not verified!"))
                        .withData("Email not verified!")
                        .build();

            updateUserPinCommand.setPinIdentifier(PinIdentifier.EMAIL);
            updateUserPinCommand.setEmail(userResult.getData().getEmail());

        } else if (command.getPinIdentifier().equals(PinIdentifier.PHONE_NUMBER)) {

            var phoneNumberVerified = userResult.getData().getPhoneNumberVerified();
            if (phoneNumberVerified == null || !phoneNumberVerified)
                return new Result.ResultBuilder<String>()
                        .withStatus(new ResultStatus(HttpStatus.FORBIDDEN, "Phone number not verified!"))
                        .withData("Phone number not verified!")
                        .build();

            updateUserPinCommand.setPinIdentifier(PinIdentifier.PHONE_NUMBER);
            updateUserPinCommand.setPhoneNumber(userResult.getData().getPhoneNumber());
        }

        if (isNull(userResult.getData().getAuthServerId()) || userResult.getData().getAuthServerId().isEmpty())
            return new Result.ResultBuilder<String>()
                    .withStatus(new ResultStatus(HttpStatus.FORBIDDEN, "User not yet activated!"))
                    .build();

        var securityUserByIdResult = getKeycloakUserByIdService.decorate(
                new GetKeycloakUserByIdQuery(userResult.getData().getAuthServerId()));

        if (securityUserByIdResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(securityUserByIdResult)
                    .build();

        updateUserPinCommand.setSecurityUser(securityUserByIdResult.getData());
        updateUserPinCommand.setPin(command.getPin());
        updateUserPinCommand.setPinStatus(PinStatus.ACTIVE);

        var updatePinResult = updateKeycloakUserPinService.decorate(updateUserPinCommand);

        if (updatePinResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(updatePinResult)
                    .build();

        var updateUserCommand = new UpdateUserFlowCommand();
        updateUserCommand.setUserRefId(userResult.getData().getId());
        updateUserCommand.setPinStatus(PinStatus.ACTIVE);

        var updatePersonalUserDetailsResult =
                updatePersonalUserDetailsFlowService.decorate(updateUserCommand);

        if (updatePersonalUserDetailsResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(updatePersonalUserDetailsResult)
                    .build();

        return new Result.ResultBuilder<String>()
                .withData(userResult.getData().getId())
                .withMessage("Pin created successfully!")
                .build();

    }
}
