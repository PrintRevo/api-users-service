package com.printrevo.tech.userservice.services.security.command.flowservices;

import com.printrevo.tech.commonsecurity.dto.keycloak.KeycloakUser;
import com.printrevo.tech.commonsecurity.dto.keycloak.SecurityUser;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.users.dto.SysUserDto;
import com.printrevo.tech.userservice.services.security.command.UpdateKeycloakUserService;
import com.printrevo.tech.userservice.services.security.command.flowservices.instructions.UpdateKeycloakUserFlowCommand;
import com.printrevo.tech.userservice.services.security.command.instructions.UpdateKeycloakUserCommand;
import com.printrevo.tech.userservice.services.security.query.GetKeycloakUserByIdService;
import com.printrevo.tech.userservice.services.security.query.instructions.GetKeycloakUserByIdQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.printrevo.tech.platform.util.StringUtil.coalesce;
import static java.util.Objects.nonNull;


@Service
@RequiredArgsConstructor
public class UpdateKeycloakUserFlowService
        extends CommandBaseService<UpdateKeycloakUserFlowCommand, Void> {

    private final GetKeycloakUserByIdService getKeycloakUserByIdService;
    private final UpdateKeycloakUserService updateKeycloakUserService;

    @Override
    public Result<Void> execute(UpdateKeycloakUserFlowCommand command) {

        var userDTO = command.getUserDto();

        var userByIdResult = getKeycloakUserByIdService.decorate(
                new GetKeycloakUserByIdQuery(userDTO.getAuthServerId()));

        if (userByIdResult.isFailed())
            return new Result.ResultBuilder<Void>()
                    .received(userByIdResult)
                    .build();

        var updatedKeycloakUser = getUpdatedKeycloakUser(userDTO, userByIdResult.getData());

        var updateKeycloakUserResult = updateKeycloakUserService.decorate(
                new UpdateKeycloakUserCommand(updatedKeycloakUser, userDTO.getAuthServerId()));

        if (updateKeycloakUserResult.isFailed())
            return new Result.ResultBuilder<Void>()
                    .received(updateKeycloakUserResult)
                    .build();

        return new Result.ResultBuilder<Void>()
                .withMessage("User successfully updated")
                .build();
    }

    public KeycloakUser getUpdatedKeycloakUser(SysUserDto userDTO, SecurityUser securityUser) {

        securityUser.setEmail((String) coalesce(securityUser.getEmail(), userDTO.getEmail()));
        securityUser.setEmailVerified((Boolean) coalesce(securityUser.isEmailVerified()
                , userDTO.getEmailVerified()));
        securityUser.setFirstName((String) coalesce(securityUser.getFirstName(), userDTO.getFirstName()));
        securityUser.setLastName((String) coalesce(securityUser.getLastName(), userDTO.getLastName()));

        if (nonNull(userDTO.getMiddleName()))
            securityUser.addAttribute("middleName", List.of(userDTO.getMiddleName()));

        if (nonNull(userDTO.getPhoneNumber()))
            securityUser.addAttribute("phoneNumber", List.of(userDTO.getPhoneNumber()));

        if (nonNull(userDTO.getPhoneNumberVerified()))
            securityUser.addAttribute("phoneNumberVerified", List.of(userDTO.getPhoneNumberVerified()
                    .toString()));

        if (nonNull(userDTO.getCountry()))
            securityUser.addAttribute("country", List.of(userDTO.getCountry().getName()));

        return securityUser;
    }
}
