package com.printrevo.tech.userservice.services.verification.command;

import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.verification.models.EmailVerificationRequest;
import com.printrevo.tech.userservice.entities.core.verification.repositories.EmailVerificationRequestRepository;
import com.printrevo.tech.userservice.services.tokenservice.command.SendEmailVerificationRequestService;
import com.printrevo.tech.userservice.services.tokenservice.command.instructions.SendEmailVerificationRequestCommand;
import com.printrevo.tech.userservice.services.verification.command.instructions.EmailVerificationRequestCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationRequestFlowService
        extends CommandBaseService<EmailVerificationRequestCommand, String> {

    private final SendEmailVerificationRequestService sendEmailVerificationRequestService;
    private final EmailVerificationRequestRepository emailVerificationRequestRepository;

    @Override
    public Result<String> execute(EmailVerificationRequestCommand command) {

        var emailRequest =
                SendEmailVerificationRequestCommand.builder()
                        .emailAddress(command.getEmailAddress())
                        .userId(command.getUserId())
                        .build();

        var result = sendEmailVerificationRequestService.decorate(emailRequest);

        if (result.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(result)
                    .build();

        var emailVerificationRequest = new EmailVerificationRequest();
        emailVerificationRequest.setEmailAddress(command.getEmailAddress());
        emailVerificationRequest.setTokenServiceId(result.getId());
        emailVerificationRequest.setUserId(command.getUserId());
        emailVerificationRequest.setTimeToLive(5);
        emailVerificationRequest.setSentDate(LocalDate.now());

        emailVerificationRequest = emailVerificationRequestRepository.save(emailVerificationRequest);

        return new Result.ResultBuilder<String>()
                .withData(emailVerificationRequest.getId())
                .build();
    }
}
