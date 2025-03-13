package com.printrevo.tech.userservice.services.security.command.flowservices;

import com.printrevo.tech.platform.db.TransactionFilter;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.dto.DataEntry;
import com.printrevo.tech.userservice.entities.core.users.constants.UserStatus;
import com.printrevo.tech.userservice.entities.core.users.dto.SysUserDto;
import com.printrevo.tech.userservice.entities.core.verification.constansts.OriginatingSource;
import com.printrevo.tech.userservice.entities.core.verification.constansts.VerificationType;
import com.printrevo.tech.userservice.services.security.command.flowservices.instructions.InitiateResetUserPasswordFlowCommand;
import com.printrevo.tech.userservice.services.users.command.CreateSysUserService;
import com.printrevo.tech.userservice.services.users.command.instructions.CreateSysUserCommand;
import com.printrevo.tech.userservice.services.users.query.GetUserPageByIdentifierQueryService;
import com.printrevo.tech.userservice.services.users.query.instructions.GetUserByIdentifierQuery;
import com.printrevo.tech.userservice.services.verification.command.flowservices.VerificationRequestFlowService;
import com.printrevo.tech.userservice.services.verification.command.flowservices.instructions.VerificationRequestFlowCommand;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class InitiateResetUserPasswordFlowService
        extends CommandBaseService<InitiateResetUserPasswordFlowCommand, String> {

    private final GetUserPageByIdentifierQueryService getUserPageByIdentifierQueryService;
    private final VerificationRequestFlowService verificationRequestFlowService;
    private final CreateSysUserService createSysUserService;

    @Override
    public Result<String> execute(InitiateResetUserPasswordFlowCommand command) {

        var userResult = getUserPageByIdentifierQueryService.decorate(
                new GetUserByIdentifierQuery(new TransactionFilter(
                        Map.of("email", command.getEmail(), "userStatus", UserStatus.ACTIVE.name()))));

        if (userResult.isFailure())
            return new Result.ResultBuilder<String>()
                    .received(userResult)
                    .build();

        var userDto = getUser(userResult);

        if (userDto.isEmpty()) {

            var createdUser = createSysUserService.decorate(
                    new CreateSysUserCommand(command.getEmail(), command.getCountry()));

            if (createdUser.isFailure())
                return new Result.ResultBuilder<String>()
                        .received(createdUser)
                        .build();

            userDto = Optional.of(new SysUserDto(createdUser.getData()));
        }

        var verificationRequestResult = verificationRequestFlowService.decorate(
                new VerificationRequestFlowCommand(
                        OriginatingSource.RESET_USER_PASSWORD,
                        VerificationType.EMAIL,
                        userDto.get().getId(),
                        Map.of("countryCode", new DataEntry(String.valueOf(command.getCountry()), "countryCode"))
                ));

        if (verificationRequestResult.isFailure())
            return new Result.ResultBuilder<String>()
                    .received(verificationRequestResult)
                    .build();

        return new Result.ResultBuilder<String>()
                .withData(verificationRequestResult.getData())
                .withMessage("Reset OTP sent successfully")
                .build();
    }

    private Optional<SysUserDto> getUser(Result<Page<SysUserDto>> userResult) {
        return userResult.getData().stream()
                .filter(userDto -> nonNull(userDto.getUserStatus()))
                .filter(userDto -> userDto.getUserStatus().equals(UserStatus.ACTIVE))
                .findFirst();
    }
}
