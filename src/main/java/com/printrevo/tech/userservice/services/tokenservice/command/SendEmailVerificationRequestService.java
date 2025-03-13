package com.printrevo.tech.userservice.services.tokenservice.command;

import com.printrevo.tech.commonsecurity.constants.AuthSource;
import com.printrevo.tech.commonsecurity.constants.AuthSourceContextHolder;
import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.clients.tokenservice.TokenServiceFeignClient;
import com.printrevo.tech.userservice.data.dto.tokenservice.EmailVerificationRequest;
import com.printrevo.tech.userservice.services.tokenservice.command.instructions.SendEmailVerificationRequestCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SendEmailVerificationRequestService extends CommandBaseService<SendEmailVerificationRequestCommand, String> {

    private final TokenServiceFeignClient tokenServiceFeignClient;

    public SendEmailVerificationRequestService(TokenServiceFeignClient tokenServiceFeignClient) {
        this.tokenServiceFeignClient = tokenServiceFeignClient;
    }

    @Override
    public Result<String> execute(SendEmailVerificationRequestCommand command) {

        try {

            var emailRequest = new EmailVerificationRequest();
            emailRequest.setEmail(command.getEmailAddress());
            emailRequest.setUserId(command.getUserId());

            AuthSourceContextHolder.setValue(AuthSource.CLIENT);

            var result = tokenServiceFeignClient.sendEmailVerificationRequest(emailRequest);

            if (result.getStatus().equals("200"))
                return new Result.ResultBuilder<String>()
                        .withStatus(new ResultStatus(HttpStatus.OK, result.getMessage()))
                        .withData(String.valueOf(result.getData().getId()))
                        .build();

            return new Result.ResultBuilder<String>()
                    .withStatus(new ResultStatus(HttpStatus.valueOf(Integer.parseInt(result.getStatus()))
                            , result.getMessage()))
                    .build();

        } catch (Exception e) {
            log.error("Error sending email verification request", e);
            return new Result.ResultBuilder<String>()
                    .withStatus(new ResultStatus(HttpStatus.INTERNAL_SERVER_ERROR, "Error sending email verification request"))
                    .build();
        } finally {
            AuthSourceContextHolder.clearValue();
        }
    }
}
