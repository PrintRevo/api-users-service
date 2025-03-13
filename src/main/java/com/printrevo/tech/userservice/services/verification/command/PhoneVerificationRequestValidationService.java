package com.printrevo.tech.userservice.services.verification.command;

import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.verification.models.PhoneNumberVerificationRequest;
import com.printrevo.tech.userservice.entities.core.verification.repositories.PhoneNumberVerificationRequestRepository;
import com.printrevo.tech.userservice.services.verification.command.instructions.PhoneVerificationRequestValidationCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@Service
public class PhoneVerificationRequestValidationService
        extends CommandBaseService<PhoneVerificationRequestValidationCommand, String> {

    private final PhoneNumberVerificationRequestRepository phoneNumberVerificationRequestRepository;
    private final Integer timeToLiveInMinutes;

    public PhoneVerificationRequestValidationService(
            PhoneNumberVerificationRequestRepository phoneNumberVerificationRequestRepository
            , @Value("${verification.phone.otp-expiry}") String timeToLiveInMinutes) {
        this.phoneNumberVerificationRequestRepository = phoneNumberVerificationRequestRepository;
        this.timeToLiveInMinutes = Integer.parseInt(timeToLiveInMinutes);
    }

    @Override
    public Result<String> execute(PhoneVerificationRequestValidationCommand command) {

        var verificationRequest = command.getVerificationRequest();

        var phoneReq = phoneNumberVerificationRequestRepository
                .findById(verificationRequest.getCorrelationId());

        if (phoneReq.isEmpty())
            return new Result.ResultBuilder<String>()
                    .withStatus(new ResultStatus(HttpStatus.INTERNAL_SERVER_ERROR
                            , "Phone verification request not found"))
                    .build();

        if (validateOTPExpired(phoneReq.get()))
            return new Result.ResultBuilder<String>()
                    .withStatus(new ResultStatus(HttpStatus.FORBIDDEN, "OTP Expired!"))
                    .build();

        if (!phoneReq.get().getOtp().equals(command.getOtp()))
            return new Result.ResultBuilder<String>()
                    .withStatus(new ResultStatus(HttpStatus.FORBIDDEN, "Invalid OTP!"))
                    .build();

        return new Result.ResultBuilder<String>()
                .withData(phoneReq.get().getId())
                .withStatus(new ResultStatus(HttpStatus.OK, "Otp validated"))
                .build();
    }

    public boolean validateOTPExpired(PhoneNumberVerificationRequest phoneReq) {
        var minutes = Duration.between(LocalDateTime.now()
                , ZonedDateTime.of(phoneReq.getCreated(), ZoneId.of("UTC"))).toMinutes();
        return Math.abs(minutes) > timeToLiveInMinutes;
    }
}
