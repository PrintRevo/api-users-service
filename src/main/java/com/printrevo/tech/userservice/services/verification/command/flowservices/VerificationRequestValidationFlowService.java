package com.printrevo.tech.userservice.services.verification.command.flowservices;

import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.verification.models.VerificationRequest;
import com.printrevo.tech.userservice.entities.core.verification.repositories.VerificationRequestRepository;
import com.printrevo.tech.userservice.services.verification.command.EmailVerificationRequestValidationService;
import com.printrevo.tech.userservice.services.verification.command.PhoneVerificationRequestValidationService;
import com.printrevo.tech.userservice.services.verification.command.instructions.EmailVerificationRequestValidationCommand;
import com.printrevo.tech.userservice.services.verification.command.instructions.PhoneVerificationRequestValidationCommand;
import com.printrevo.tech.userservice.services.verification.command.instructions.VerificationOriginatorCommand;
import com.printrevo.tech.userservice.services.verification.command.instructions.VerificationRequestValidationCommand;
import com.printrevo.tech.userservice.services.verification.query.GetVerificationRequestByIdService;
import com.printrevo.tech.userservice.services.verification.query.VerificationOriginatorResolverService;
import com.printrevo.tech.userservice.services.verification.query.instructions.GetVerificationOriginatorResolverQuery;
import com.printrevo.tech.userservice.services.verification.query.instructions.GetVerificationRequestByIdQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class VerificationRequestValidationFlowService
        extends CommandBaseService<VerificationRequestValidationCommand, String> {

    private final EmailVerificationRequestValidationService emailVerificationRequestValidationService;
    private final PhoneVerificationRequestValidationService phoneVerificationRequestValidationService;
    private final VerificationOriginatorResolverService verificationOriginatorResolverService;
    private final GetVerificationRequestByIdService getVerificationRequestByIdService;
    private final VerificationRequestRepository verificationRequestRepository;

    @Override
    public Result<String> execute(VerificationRequestValidationCommand command) {

        var verificationRequestQueryResult =
                getVerificationRequestByIdService.decorate(
                        new GetVerificationRequestByIdQuery(command.getVerificationRequestRefId()));

        if (verificationRequestQueryResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(verificationRequestQueryResult)
                    .build();

        var verificationValidationResult = getVerificationRequestValidationResult(
                verificationRequestQueryResult.getData(), command.getOtp());

        if (verificationValidationResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(verificationValidationResult)
                    .build();

        var verificationRequest = getVerifiedRequest(verificationRequestQueryResult.getData());

        var verificationOriginatorResolverQueryResult =
                verificationOriginatorResolverService.decorate(new GetVerificationOriginatorResolverQuery(
                        verificationRequest.getOriginatingSource()));

        if (verificationOriginatorResolverQueryResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(verificationOriginatorResolverQueryResult)
                    .build();

        return verificationOriginatorResolverQueryResult.getData()
                .decorate(new VerificationOriginatorCommand(verificationRequest));
    }

    private VerificationRequest getVerifiedRequest(VerificationRequest verificationRequest) {
        verificationRequest.setVerified(true);
        verificationRequest.setVerifiedAt(LocalDateTime.now());
        verificationRequest.setExpiresAt(LocalDateTime.now().plus(2,
                TimeUnit.MINUTES.toChronoUnit()));
        return verificationRequestRepository.save(verificationRequest);
    }

    private Result<String> getVerificationRequestValidationResult(
            VerificationRequest verificationRequest, String otp) {
        return switch (verificationRequest.getVerificationType()) {
            case EMAIL -> emailVerificationRequestValidationService.decorate(
                    new EmailVerificationRequestValidationCommand(verificationRequest, otp));
            case PHONE_NUMBER -> phoneVerificationRequestValidationService
                    .decorate(new PhoneVerificationRequestValidationCommand(verificationRequest, otp));
            default -> new Result.ResultBuilder<String>().withIsFailed(true)
                    .withStatus(new ResultStatus(HttpStatus.NOT_IMPLEMENTED
                            , "The verification type is not yet implemented!")).build();
        };
    }
}
