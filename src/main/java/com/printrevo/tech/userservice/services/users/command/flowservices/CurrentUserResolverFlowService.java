package com.printrevo.tech.userservice.services.users.command.flowservices;

import com.printrevo.tech.commonsecurity.dto.keycloak.SecurityUser;
import com.printrevo.tech.commonsecurity.helpers.session.SessionHelper;
import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.users.constants.UserStatus;
import com.printrevo.tech.userservice.entities.core.users.dto.SysUserDto;
import com.printrevo.tech.userservice.entities.core.users.models.SysUser;
import com.printrevo.tech.userservice.entities.core.users.repositories.SysUserRepository;
import com.printrevo.tech.userservice.services.security.query.GetKeycloakUserByIdService;
import com.printrevo.tech.userservice.services.security.query.instructions.GetKeycloakUserByIdQuery;
import com.printrevo.tech.userservice.services.users.command.flowservices.instructions.CurrentUserResolverCommand;
import com.printrevo.tech.userservice.services.users.query.GetUserByAuthenticationServerIdService;
import com.printrevo.tech.userservice.services.users.query.instructions.GetUserByAuthenticationServerIdQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CurrentUserResolverFlowService extends CommandBaseService<CurrentUserResolverCommand, SysUserDto> {

    private final GetUserByAuthenticationServerIdService getUserByAuthenticationServerIdService;
    private final GetKeycloakUserByIdService getKeycloakUserByIdService;
    private final SysUserRepository sysUserRepository;
    private final SessionHelper sessionHelper;

    @Override
    public Result<SysUserDto> execute(CurrentUserResolverCommand command) {
        var currentUser = sessionHelper.getCurrentUser();

        if (currentUser.isEmpty())
            return new Result.ResultBuilder<SysUserDto>()
                    .withStatus(new ResultStatus(HttpStatus.NOT_FOUND, "User not found"))
                    .build();

        var userAuthenticationServerId = currentUser.get().getUserAuthenticatorId();

        var userResults = getUserByAuthenticationServerIdService.decorate(
                new GetUserByAuthenticationServerIdQuery(userAuthenticationServerId));

        var sysUser = userResults.getData();

        if (userResults.isFailed()) {

            if (!Objects.equals(HttpStatus.NOT_FOUND, userResults.getStatus().getHttpStatus()))
                return new Result.ResultBuilder<SysUserDto>()
                        .received(userResults)
                        .build();

            var keycloakUserResult = getKeycloakUserByIdService.decorate(
                    new GetKeycloakUserByIdQuery(userAuthenticationServerId));

            if (keycloakUserResult.isFailed()) {
                return new Result.ResultBuilder<SysUserDto>()
                        .received(keycloakUserResult)
                        .build();
            }

            var user = getSysUser(keycloakUserResult);

            var existingUser = sysUserRepository.findByEmailAndUserStatus(user.getEmail(), UserStatus.ACTIVE);

            if (existingUser.isPresent()) {
                user = existingUser.get();
                user.setAuthServerId(keycloakUserResult.getData().getId());
            }

            sysUser = sysUserRepository.save(user);
        }

        return new Result.ResultBuilder<SysUserDto>()
                .withData(sysUser.toDTO())
                .withMessage("User found")
                .build();
    }

    private SysUser getSysUser(Result<SecurityUser> keycloakUserResult) {
        var keycloakUser = keycloakUserResult.getData();

        var user = new SysUser();
        user.setAuthServerId(keycloakUser.getId());
        user.setEmail(keycloakUser.getEmail());
        user.setFirstName(keycloakUser.getFirstName());
        user.setLastName(keycloakUser.getLastName());
        user.setEmail(keycloakUser.getEmail());
        user.setEmailVerified(keycloakUser.isEmailVerified());
        user.setIsActive(keycloakUser.isEnabled());
        if (keycloakUser.isEmailVerified())
            user.setUserStatus(UserStatus.ACTIVE);
        else user.setUserStatus(UserStatus.ORIGINATING);
        return user;
    }
}
