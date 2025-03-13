package com.printrevo.tech.userservice.services.verification.command.flowservices;

import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.dto.DataEntry;
import com.printrevo.tech.userservice.entities.core.users.models.SysUser;
import com.printrevo.tech.userservice.entities.core.verification.constansts.VerificationType;
import com.printrevo.tech.userservice.entities.core.verification.models.VerificationRequest;
import com.printrevo.tech.userservice.entities.core.verification.repositories.VerificationRequestRepository;
import com.printrevo.tech.userservice.services.users.query.GetUserByRefIdService;
import com.printrevo.tech.userservice.services.users.query.instructions.GetUserByRefIdQuery;
import com.printrevo.tech.userservice.services.verification.command.EmailVerificationRequestFlowService;
import com.printrevo.tech.userservice.services.verification.command.PhoneNumberVerificationRequestFlowService;
import com.printrevo.tech.userservice.services.verification.command.flowservices.instructions.VerificationRequestFlowCommand;
import com.printrevo.tech.userservice.services.verification.command.instructions.EmailVerificationRequestCommand;
import com.printrevo.tech.userservice.services.verification.command.instructions.PhoneNumberVerificationRequestCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationRequestFlowService
        extends CommandBaseService<VerificationRequestFlowCommand, String> {

    private final PhoneNumberVerificationRequestFlowService phoneNumberVerificationRequestFlowService;
    private final EmailVerificationRequestFlowService emailVerificationRequestFlowService;
    private final VerificationRequestRepository verificationRequestRepository;
    private final GetUserByRefIdService getUserByRefIdService;

    @Override
    public Result<String> execute(VerificationRequestFlowCommand command) {

        var userResult = getUserByRefIdService.decorate(
                new GetUserByRefIdQuery(command.getUserRefId()));

        if (userResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(userResult)
                    .build();

        var user = userResult.getData();

        Result<String> verificationRequestResult = getVerificationRequestResult(command, user);

        if (verificationRequestResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(verificationRequestResult)
                    .build();

        var verificationRequest = getVerificationRequest(command, verificationRequestResult.getData(), user);

        return new Result.ResultBuilder<String>()
                .withData(verificationRequest.getId())
                .withMessage("Verification request received successfully")
                .build();
    }

    private Result<String> getVerificationRequestResult(
            VerificationRequestFlowCommand command, SysUser user) {
        return switch (command.getVerificationType()) {
            case EMAIL -> emailVerificationRequestFlowService.decorate(
                    new EmailVerificationRequestCommand(getValue(command, user), user.getId()));
            case PHONE_NUMBER -> phoneNumberVerificationRequestFlowService
                    .decorate(new PhoneNumberVerificationRequestCommand(getValue(command, user)));
            default -> new Result.ResultBuilder<String>().withIsFailed(true)
                    .withStatus(new ResultStatus(HttpStatus.NOT_IMPLEMENTED
                            , "The verification type is not yet implemented!")).build();
        };
    }

    public VerificationRequest getVerificationRequest(
            VerificationRequestFlowCommand command, String correlationId, SysUser user) {
        var verificationRequest = new VerificationRequest();
        verificationRequest.setVerificationType(command.getVerificationType());
        verificationRequest.setOriginatingSource(command.getOriginatingSource());
        verificationRequest.setIdentityValue(getValue(command, user));
        verificationRequest.setUser(user);
        verificationRequest.setCorrelationId(correlationId);
        verificationRequest.setDetails(command.getDetails());
        return verificationRequestRepository.save(verificationRequest);
    }

    public String getValue(VerificationRequestFlowCommand command, SysUser user) {
        var fromDetails = getFromDetails(command.getDetails(), command.getVerificationType());
        return switch (command.getVerificationType()) {
            case PHONE_NUMBER -> fromDetails.orElseGet(user::getPhoneNumber);
            case EMAIL -> fromDetails.orElseGet(user::getEmail);
            default -> null;
        };
    }

    private Optional<String> getFromDetails(Map<String, DataEntry> details, VerificationType verificationType) {
        return Optional.ofNullable(details)
                .filter(stringDataEntryMap -> stringDataEntryMap.containsKey(verificationType.name()))
                .map(stringDataEntryMap -> stringDataEntryMap.get(verificationType.name()))
                .map(DataEntry::getValue);
    }
}
