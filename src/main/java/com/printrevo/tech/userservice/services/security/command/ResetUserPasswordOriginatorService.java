package com.printrevo.tech.userservice.services.security.command;

import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.users.constants.UserStatus;
import com.printrevo.tech.userservice.entities.core.users.repositories.SysUserRepository;
import com.printrevo.tech.userservice.entities.core.verification.constansts.OriginatingSource;
import com.printrevo.tech.userservice.services.security.command.flowservices.CreateSecurityUserFlowService;
import com.printrevo.tech.userservice.services.security.command.instructions.CreateSecurityUserCommand;
import com.printrevo.tech.userservice.services.verification.command.VerificationOriginatorService;
import com.printrevo.tech.userservice.services.verification.command.instructions.VerificationOriginatorCommand;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@AllArgsConstructor
public class ResetUserPasswordOriginatorService extends VerificationOriginatorService {

    private final CreateSecurityUserFlowService createSecurityUserFlowService;
    private final SysUserRepository userRepository;

    @Override
    public Result<String> execute(VerificationOriginatorCommand command) {

        var request = command.getVerificationRequest();
        var user = request.getUser();

        if (Objects.equals(user.getUserStatus(), UserStatus.ACTIVE)) {
            return new Result.ResultBuilder<String>()
                    .withData(request.getId())
                    .withMessage("Verification successful")
                    .build();
        }

        user.setEmail(request.getIdentityValue());
        user.setEmailVerified(true);
        userRepository.save(user);

        var createUserResult = createSecurityUserFlowService.decorate(
                new CreateSecurityUserCommand(user.getId(), "@TaarifaK2020"));

        if (createUserResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(createUserResult)
                    .build();

        return new Result.ResultBuilder<String>()
                .withData(request.getId())
                .withMessage("User created successfully")
                .build();
    }

    @Override
    public OriginatingSource originatingSource() {
        return OriginatingSource.RESET_USER_PASSWORD;
    }
}
