package com.printrevo.tech.userservice.services.security.command.flowservices;

import com.printrevo.tech.commonsecurity.dto.keycloak.SecurityUser;
import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.users.constants.UserStatus;
import com.printrevo.tech.userservice.entities.core.users.models.SysUser;
import com.printrevo.tech.userservice.entities.core.users.repositories.SysUserRepository;
import com.printrevo.tech.userservice.services.security.command.AddUserToSecurityGroupService;
import com.printrevo.tech.userservice.services.security.command.CreateUserKeycloakService;
import com.printrevo.tech.userservice.services.security.command.UserPasswordValidationService;
import com.printrevo.tech.userservice.services.security.command.flowservices.instructions.UpdateUserPasswordFlowCommand;
import com.printrevo.tech.userservice.services.security.command.instructions.AddUserToSecurityGroupCommand;
import com.printrevo.tech.userservice.services.security.command.instructions.CreateSecurityUserCommand;
import com.printrevo.tech.userservice.services.security.command.instructions.CreateUserKeycloakCommand;
import com.printrevo.tech.userservice.services.security.command.instructions.UserPasswordValidationCommand;
import com.printrevo.tech.userservice.services.security.query.FindKeycloakUsersQueryService;
import com.printrevo.tech.userservice.services.security.query.instructions.FindKeycloakUsersQuery;
import com.printrevo.tech.userservice.services.users.query.GetUserByRefIdService;
import com.printrevo.tech.userservice.services.users.query.instructions.GetUserByRefIdQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CreateSecurityUserFlowService extends CommandBaseService<CreateSecurityUserCommand, String> {

    private static final String BASE_USERS_GROUP = "printrevo-normal-users";

    private final UpdateUserPasswordFlowService updateUserPasswordFlowService;
    private final AddUserToSecurityGroupService addUserToSecurityGroupService;
    private final FindKeycloakUsersQueryService findKeycloakUsersQueryService;
    private final UserPasswordValidationService userPasswordValidationService;
    private final CreateUserKeycloakService createUserKeycloakService;
    private final GetUserByRefIdService getUserByRefIdService;
    private final SysUserRepository userRepository;

    @Override
    public Result<String> execute(CreateSecurityUserCommand command) {

        var userResult = getUserByRefIdService.decorate(
                new GetUserByRefIdQuery(command.getUserRefId()));

        if (userResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(userResult)
                    .build();

        var passwordValidationResult = userPasswordValidationService.decorate(
                new UserPasswordValidationCommand(command.getPassword()));

        if (passwordValidationResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(passwordValidationResult)
                    .build();

        if (Boolean.FALSE.equals(userResult.getData().getEmailVerified()))
            return new Result.ResultBuilder<String>()
                    .withStatus(new ResultStatus(HttpStatus.FORBIDDEN, "User not verified!"))
                    .build();

        var userByEmailResult = findKeycloakUsersQueryService.decorate(
                new FindKeycloakUsersQuery(Map.of("email", userResult.getData().getEmail())));

        if (userByEmailResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(userByEmailResult)
                    .build();

        SecurityUser securityUser;

        if (Boolean.FALSE.equals(userByEmailResult.getData().isEmpty())) {
            securityUser = userByEmailResult.getData().get(0);
        } else {
            var createUserKeycloakResult = createUserKeycloakService
                    .decorate(new CreateUserKeycloakCommand(userResult.getData().toDTO()));

            if (createUserKeycloakResult.isFailed())
                return new Result.ResultBuilder<String>()
                        .received(createUserKeycloakResult)
                        .build();

            var userByUsernameResult = findKeycloakUsersQueryService.decorate(
                    new FindKeycloakUsersQuery(Map.of("username", userResult.getData().getId())));

            if (userByUsernameResult.isFailed())
                return new Result.ResultBuilder<String>()
                        .received(userByUsernameResult)
                        .build();

            if (userByUsernameResult.getData().size() != 1)
                return new Result.ResultBuilder<String>()
                        .withStatus(new ResultStatus(HttpStatus.INTERNAL_SERVER_ERROR
                                , "Failed to query created user"))
                        .build();
            securityUser = userByUsernameResult.getData().get(0);
        }

        var activatedUser = activateUser(userResult.getData(), securityUser.getId());

        /*
            We are bypassing the authorization validation here because the processing can be in an
            unauthenticated context.
         */
        var passwordSetResult = updateUserPasswordFlowService
                .decorate(new UpdateUserPasswordFlowCommand(
                        activatedUser.getId(), command.getPassword(), false
                        , true));

        if (passwordSetResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(passwordSetResult)
                    .build();

        var addUserToSecurityGroupResult = addUserToSecurityGroupService
                .decorate(new AddUserToSecurityGroupCommand(BASE_USERS_GROUP,
                        activatedUser.getAuthServerId()));

        if (addUserToSecurityGroupResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(addUserToSecurityGroupResult)
                    .build();

        return new Result.ResultBuilder<String>()
                .withData(activatedUser.getId())
                .withStatus(new ResultStatus(HttpStatus.CREATED, "User successfully created"))
                .build();
    }

    private SysUser activateUser(SysUser existingUser, String userAuthServerId) {
        existingUser.setAuthServerId(userAuthServerId);
        existingUser.setUserStatus(UserStatus.ACTIVE);
        return userRepository.save(existingUser);
    }

}
