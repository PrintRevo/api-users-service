package com.printrevo.tech.userservice.services.tokenservice.command;

import com.printrevo.tech.commonsecurity.constants.AuthSource;
import com.printrevo.tech.commonsecurity.constants.AuthSourceContextHolder;
import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.clients.tokenservice.TokenServiceFeignClient;
import com.printrevo.tech.userservice.data.dto.tokenservice.VerificationValidationRequest;
import com.printrevo.tech.userservice.services.tokenservice.command.instructions.VerifyVerificationRequestCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VerifyVerificationRequestService extends CommandBaseService<VerifyVerificationRequestCommand, String> {

    private final TokenServiceFeignClient tokenServiceFeignClient;

    public VerifyVerificationRequestService(TokenServiceFeignClient tokenServiceFeignClient) {
        this.tokenServiceFeignClient = tokenServiceFeignClient;
    }

    @Override
    public Result<String> execute(VerifyVerificationRequestCommand command) {

        try {

            var validationRequest = new VerificationValidationRequest();
            validationRequest.setOtpCode(command.getOtp());
            validationRequest.setUserId(command.getUserId());

            AuthSourceContextHolder.setValue(AuthSource.CLIENT);

            var result = tokenServiceFeignClient.validateVerificationRequest(validationRequest);

            return new Result.ResultBuilder<String>()
                    .withStatus(new ResultStatus(HttpStatus.OK, result.getMessage()))
                    .withData(String.valueOf(result.getData().getId()))
                    .build();

        } catch (Exception e) {
            log.error("Error occurred while validating OTP: {}", e.getLocalizedMessage());
            return new Result.ResultBuilder<String>()
                    .withStatus(new ResultStatus(HttpStatus.BAD_REQUEST, "Invalid OTP"))
                    .build();
        } finally {
            AuthSourceContextHolder.clearValue();
        }
    }
}
