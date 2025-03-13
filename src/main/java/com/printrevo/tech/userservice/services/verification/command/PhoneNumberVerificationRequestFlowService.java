package com.printrevo.tech.userservice.services.verification.command;

import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.verification.models.PhoneNumberVerificationRequest;
import com.printrevo.tech.userservice.entities.core.verification.repositories.PhoneNumberVerificationRequestRepository;
import com.printrevo.tech.userservice.platform.notifications.DefaultNotificationProvider;
import com.printrevo.tech.userservice.services.verification.command.instructions.PhoneNumberVerificationRequestCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;

@Slf4j
@Service
public class PhoneNumberVerificationRequestFlowService
        extends CommandBaseService<PhoneNumberVerificationRequestCommand, String> {

    private static final String OTP_MESSAGE = "Enter the otp %s to verify your PrintRevo account.";

    private final PhoneNumberVerificationRequestRepository phoneNumberVerificationRequestRepository;
    private final DefaultNotificationProvider defaultNotificationProvider;
    private final Integer timeToLiveInMinutes;
    private final String phoneOTPLength;

    public PhoneNumberVerificationRequestFlowService(
            PhoneNumberVerificationRequestRepository phoneNumberVerificationRequestRepository
            , DefaultNotificationProvider defaultNotificationProvider
            , @Value("${verification.phone.otp-expiry}") String timeToLiveInMinutes
            , @Value("${verification.phone.otp-length}") String phoneOTPLength) {
        this.phoneNumberVerificationRequestRepository = phoneNumberVerificationRequestRepository;
        this.defaultNotificationProvider = defaultNotificationProvider;
        this.timeToLiveInMinutes = Integer.parseInt(timeToLiveInMinutes);
        this.phoneOTPLength = phoneOTPLength;
    }

    @Override
    public Result<String> execute(PhoneNumberVerificationRequestCommand command) {

        SecureRandom secureRandom = new SecureRandom();
        int otp = secureRandom.nextInt((int) Math.pow(10, Integer.parseInt(phoneOTPLength)));
        var message = String.format(OTP_MESSAGE, otp);

        try {
            defaultNotificationProvider.sendSMS(command.getPhoneNumber(), message);

            var phoneNumberVerificationRequest = new PhoneNumberVerificationRequest();
            phoneNumberVerificationRequest.setPhoneNumber(command.getPhoneNumber());
            phoneNumberVerificationRequest.setOtp(String.valueOf(otp));
            phoneNumberVerificationRequest.setTimeToLive(timeToLiveInMinutes);
            phoneNumberVerificationRequest.setSentDate(LocalDate.now());

            phoneNumberVerificationRequest = phoneNumberVerificationRequestRepository.save(phoneNumberVerificationRequest);

            return new Result.ResultBuilder<String>()
                    .withData(phoneNumberVerificationRequest.getId())
                    .build();

        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return new Result.ResultBuilder<String>()
                    .withStatus(new ResultStatus(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage()))
                    .build();
        }
    }
}
