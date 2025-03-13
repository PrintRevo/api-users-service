package com.printrevo.tech.userservice.services.security.command.flowservices;

import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.users.constants.UserStatus;
import com.printrevo.tech.userservice.services.security.command.SetUserKeycloakPasswordService;
import com.printrevo.tech.userservice.services.security.command.UserPasswordValidationService;
import com.printrevo.tech.userservice.services.security.command.flowservices.instructions.UpdateUserPasswordFlowCommand;
import com.printrevo.tech.userservice.services.security.command.instructions.SetUserKeycloakPasswordCommand;
import com.printrevo.tech.userservice.services.security.command.instructions.UserPasswordValidationCommand;
import com.printrevo.tech.userservice.services.users.query.GetUserByRefIdService;
import com.printrevo.tech.userservice.services.users.query.instructions.GetUserByRefIdQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class UpdateUserPasswordFlowService
        extends CommandBaseService<UpdateUserPasswordFlowCommand, String> {

    private final SetUserKeycloakPasswordService setUserKeycloakPasswordService;
    private final UserPasswordValidationService userPasswordValidationService;
    private final GetUserByRefIdService getUserByRefIdService;

    @Override
    public Result<String> execute(UpdateUserPasswordFlowCommand command) {

        var passwordValidationResult =
                userPasswordValidationService.decorate(
                        new UserPasswordValidationCommand(command.getPassword()));

        if (passwordValidationResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(passwordValidationResult)
                    .build();

        var userResult = getUserByRefIdService.decorate(
                new GetUserByRefIdQuery(command.getUserRefId()));

        if (userResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(userResult)
                    .build();

        if (isNull(userResult.getData().getAuthServerId()) ||
                Boolean.FALSE.equals(userResult.getData().getUserStatus().equals(UserStatus.ACTIVE)))
            return new Result.ResultBuilder<String>()
                    .withStatus(new ResultStatus(HttpStatus.FORBIDDEN, "User not activated!"))
                    .build();

        var setUserKeycloakPasswordResult = setUserKeycloakPasswordService.decorate(
                new SetUserKeycloakPasswordCommand(userResult.getData().getAuthServerId(),
                        command.getPassword(), command.getIsTemporary()));

        if (setUserKeycloakPasswordResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(setUserKeycloakPasswordResult)
                    .build();

        return new Result.ResultBuilder<String>()
                .withData(command.getUserRefId())
                .withMessage("Password successfully set")
                .build();
    }
}
