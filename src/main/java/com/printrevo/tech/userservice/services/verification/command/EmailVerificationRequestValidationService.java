package com.printrevo.tech.userservice.services.verification.command;

import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.verification.models.EmailVerificationRequest;
import com.printrevo.tech.userservice.entities.core.verification.repositories.EmailVerificationRequestRepository;
import com.printrevo.tech.userservice.services.tokenservice.command.VerifyVerificationRequestService;
import com.printrevo.tech.userservice.services.tokenservice.command.instructions.VerifyVerificationRequestCommand;
import com.printrevo.tech.userservice.services.verification.command.instructions.EmailVerificationRequestValidationCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class EmailVerificationRequestValidationService
        extends CommandBaseService<EmailVerificationRequestValidationCommand, String> {

    private final EmailVerificationRequestRepository emailVerificationRequestRepository;
    private final VerifyVerificationRequestService verifyVerificationRequestService;

    @Override
    public Result<String> execute(EmailVerificationRequestValidationCommand command) {

        var verificationRequest = command.getVerificationRequest();

        var emailVerificationRequest = emailVerificationRequestRepository
                .findById(verificationRequest.getCorrelationId());

        if (emailVerificationRequest.isEmpty())
            return new Result.ResultBuilder<String>()
                    .withStatus(new ResultStatus(HttpStatus.INTERNAL_SERVER_ERROR
                            , "Email verification request not found"))
                    .build();

        if (validateOTPExpired(emailVerificationRequest.get()))
            return new Result.ResultBuilder<String>()
                    .withStatus(new ResultStatus(HttpStatus.FORBIDDEN, "OTP Expired!"))
                    .build();

        var validationResults = verifyVerificationRequestService.decorate(
                new VerifyVerificationRequestCommand(command.getOtp(), emailVerificationRequest.get().getUserId()));

        if (validationResults.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(validationResults)
                    .build();

        return new Result.ResultBuilder<String>()
                .withData(emailVerificationRequest.get().getId())
                .withStatus(new ResultStatus(HttpStatus.OK, "Otp validated"))
                .build();
    }

    public boolean validateOTPExpired(EmailVerificationRequest phoneReq) {
        var minutes = Duration.between(LocalDateTime.now()
                , ZonedDateTime.of(phoneReq.getCreated(), ZoneId.of("UTC"))).toMinutes();
        return Math.abs(minutes) > phoneReq.getTimeToLive();
    }
}
