package com.printrevo.tech.userservice.services.security.command.flowservices;

import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.services.security.command.UserPasswordValidationService;
import com.printrevo.tech.userservice.services.security.command.flowservices.instructions.ResetUserPasswordFlowCommand;
import com.printrevo.tech.userservice.services.security.command.flowservices.instructions.UpdateUserPasswordFlowCommand;
import com.printrevo.tech.userservice.services.security.command.instructions.UserPasswordValidationCommand;
import com.printrevo.tech.userservice.services.verification.query.GetVerificationRequestByIdService;
import com.printrevo.tech.userservice.services.verification.query.instructions.GetVerificationRequestByIdQuery;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class ResetUserPasswordFlowService
        extends CommandBaseService<ResetUserPasswordFlowCommand, String> {

    private final GetVerificationRequestByIdService getVerificationRequestByIdService;
    private final UpdateUserPasswordFlowService updateUserPasswordFlowService;
    private final UserPasswordValidationService userPasswordValidationService;

    @Override
    public Result<String> execute(ResetUserPasswordFlowCommand command) {

        var passwordValidationResult = userPasswordValidationService.decorate(
                new UserPasswordValidationCommand(command.getPassword()));

        if (passwordValidationResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(passwordValidationResult)
                    .build();

        var verificationRequestResult =
                getVerificationRequestByIdService.decorate(
                        new GetVerificationRequestByIdQuery(command.getVerificationId()));

        if (verificationRequestResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(verificationRequestResult)
                    .build();

        var verificationRequest = verificationRequestResult.getData();

        if (Boolean.FALSE.equals(verificationRequest.getVerified()))
            return new Result.ResultBuilder<String>()
                    .withStatus(new ResultStatus(HttpStatus.FORBIDDEN, "Verification request not verified"))
                    .build();

        if (verificationRequest.getExpiresAt().isBefore(LocalDateTime.now()))
            return new Result.ResultBuilder<String>()
                    .withStatus(new ResultStatus(HttpStatus.FORBIDDEN, "Verification request expired"))
                    .build();

        var updateUserPasswordResult = updateUserPasswordFlowService.decorate(
                new UpdateUserPasswordFlowCommand(verificationRequest.getUser().getId(),
                        command.getPassword(), false, true));

        if (updateUserPasswordResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(updateUserPasswordResult)
                    .build();

        return new Result.ResultBuilder<String>()
                .withData(verificationRequest.getId())
                .withMessage("User password reset successfully")
                .build();
    }
}
