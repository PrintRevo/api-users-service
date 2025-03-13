package com.printrevo.tech.userservice.services.verification.query;

import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.QueryBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.verification.models.VerificationRequest;
import com.printrevo.tech.userservice.entities.core.verification.repositories.VerificationRequestRepository;
import com.printrevo.tech.userservice.services.verification.query.instructions.GetVerificationRequestByIdQuery;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class GetVerificationRequestByIdService
        extends QueryBaseService<GetVerificationRequestByIdQuery, VerificationRequest> {

    private final VerificationRequestRepository verificationRequestRepository;

    public GetVerificationRequestByIdService(VerificationRequestRepository verificationRequestRepository) {
        this.verificationRequestRepository = verificationRequestRepository;
    }

    @Override
    public Result<VerificationRequest> execute(GetVerificationRequestByIdQuery command) {
        return verificationRequestRepository.findById(command.getRefId())
                .map(verificationRequest -> new Result.ResultBuilder<VerificationRequest>()
                        .withData(verificationRequest).build())
                .orElse(new Result.ResultBuilder<VerificationRequest>()
                        .withIsFailed(true)
                        .withStatus(new ResultStatus(HttpStatus.NOT_FOUND
                                , String.format("Verification request with id %s not found", command.getRefId())))
                        .build());
    }
}
