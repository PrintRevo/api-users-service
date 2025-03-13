package com.printrevo.tech.userservice.services.verification.query;

import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.QueryBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.verification.constansts.OriginatingSource;
import com.printrevo.tech.userservice.services.verification.command.VerificationOriginatorService;
import com.printrevo.tech.userservice.services.verification.query.instructions.GetVerificationOriginatorResolverQuery;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class VerificationOriginatorResolverService
        extends QueryBaseService<GetVerificationOriginatorResolverQuery, VerificationOriginatorService> {

    private final Map<OriginatingSource, VerificationOriginatorService> verificationOriginatorServiceMap;

    public VerificationOriginatorResolverService(List<VerificationOriginatorService> verificationOriginatorServices) {
        verificationOriginatorServiceMap = verificationOriginatorServices.stream().collect(
                Collectors.toMap(VerificationOriginatorService::originatingSource, Function.identity()));
    }

    @Override
    public Result<VerificationOriginatorService> execute(GetVerificationOriginatorResolverQuery command) {
        return Optional.ofNullable(verificationOriginatorServiceMap.get(command.getOriginatingSource()))
                .map(otpExecutionService -> new Result.ResultBuilder<VerificationOriginatorService>()
                        .withData(otpExecutionService).build())
                .orElse(new Result.ResultBuilder<VerificationOriginatorService>()
                        .withStatus(new ResultStatus(HttpStatus.NOT_FOUND,
                                "verification execution service not found"))
                        .build());
    }
}
