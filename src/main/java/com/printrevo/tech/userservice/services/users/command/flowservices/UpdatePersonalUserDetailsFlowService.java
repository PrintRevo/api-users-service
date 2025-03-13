package com.printrevo.tech.userservice.services.users.command.flowservices;

import com.printrevo.tech.common.domain.CountryCode;
import com.printrevo.tech.commonsecurity.constants.PinStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.users.dto.SysUserDto;
import com.printrevo.tech.userservice.entities.core.users.models.SysUser;
import com.printrevo.tech.userservice.entities.core.users.repositories.SysUserRepository;
import com.printrevo.tech.userservice.services.security.command.flowservices.UpdateKeycloakUserFlowService;
import com.printrevo.tech.userservice.services.security.command.flowservices.instructions.UpdateKeycloakUserFlowCommand;
import com.printrevo.tech.userservice.services.users.command.flowservices.instructions.UpdateUserFlowCommand;
import com.printrevo.tech.userservice.services.users.query.GetUserByRefIdService;
import com.printrevo.tech.userservice.services.users.query.instructions.GetUserByRefIdQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.printrevo.tech.platform.util.StringUtil.coalesce;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class UpdatePersonalUserDetailsFlowService
        extends CommandBaseService<UpdateUserFlowCommand, SysUserDto> {

    private final UpdateKeycloakUserFlowService updateKeycloakUserService;
    private final GetUserByRefIdService getUserByRefIdService;
    private final SysUserRepository userRepository;

    @Override
    public Result<SysUserDto> execute(UpdateUserFlowCommand command) {

        var userResult = getUserByRefIdService.decorate
                (new GetUserByRefIdQuery(command.getUserRefId()));

        if (userResult.isFailed())
            return new Result.ResultBuilder<SysUserDto>()
                    .received(userResult)
                    .build();

        var updatedUser = getUpdatedUser(userResult.getData(), command);

        if (nonNull(updatedUser.getAuthServerId())) {
            var updateKeycloakUserResult = updateKeycloakUserService
                    .decorate(new UpdateKeycloakUserFlowCommand(updatedUser.toDTO()));

            if (updateKeycloakUserResult.isFailed())
                return new Result.ResultBuilder<SysUserDto>()
                        .received(updateKeycloakUserResult)
                        .build();
        }

        return new Result.ResultBuilder<SysUserDto>()
                .withId(updatedUser.toDTO())
                .withMessage("User successfully updated")
                .build();
    }

    public SysUser getUpdatedUser(SysUser existingUser, UpdateUserFlowCommand command) {

        existingUser.setFirstName((String) coalesce(existingUser.getFirstName(), command.getFirstName()));
        existingUser.setMiddleName((String) coalesce(existingUser.getMiddleName(), command.getMiddleName()));
        existingUser.setLastName((String) coalesce(existingUser.getLastName(), command.getLastName()));
        existingUser.setCountry((CountryCode) coalesce(existingUser.getCountry(), command.getCountry()));

        if (nonNull(command.getPhoneNumber())) {
            existingUser.setPhoneNumber((String) coalesce(existingUser.getPhoneNumber(), command.getPhoneNumber()));
            if (isNull(command.getPhoneNumberVerified())) existingUser.setPhoneNumberVerified(false);
        }

        if (nonNull(command.getEmail())) {
            existingUser.setEmail((String) coalesce(existingUser.getEmail(), command.getEmail()));
            if (isNull(command.getEmailVerified())) existingUser.setEmailVerified(false);
        }

        existingUser.setEmailVerified((Boolean) coalesce(existingUser.getEmailVerified()
                , command.getEmailVerified()));

        existingUser.setPhoneNumberVerified((Boolean) coalesce(existingUser.getPhoneNumberVerified()
                , command.getPhoneNumberVerified()));

        existingUser.setCountry((CountryCode) coalesce(existingUser.getCountry(), command.getCountry()));

        existingUser.setAuthServerId((String) coalesce(existingUser.getAuthServerId(), command.getAuthServerId()));

        existingUser.setPinStatus((PinStatus) coalesce(existingUser.getPinStatus(), command.getPinStatus()));

        existingUser.setCountry((CountryCode) coalesce(existingUser.getCountry(), command.getCountry()));
        existingUser.setCity((String) coalesce(existingUser.getCity(), command.getCity()));
        existingUser.setAddress((String) coalesce(existingUser.getAddress(), command.getAddress()));
        existingUser.setPostalCode((String) coalesce(existingUser.getPostalCode(), command.getPostalCode()));
        existingUser.setDateOfBirth((LocalDateTime) coalesce(existingUser.getDateOfBirth(), command.getDateOfBirth()));

        return userRepository.save(existingUser);
    }
}
