package com.printrevo.tech.userservice.services.users.command;

import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.verification.constansts.OriginatingSource;
import com.printrevo.tech.userservice.entities.core.verification.constansts.VerificationType;
import com.printrevo.tech.userservice.entities.core.verification.models.VerificationRequest;
import com.printrevo.tech.userservice.services.users.command.flowservices.UpdatePersonalUserDetailsFlowService;
import com.printrevo.tech.userservice.services.users.command.flowservices.instructions.UpdateUserFlowCommand;
import com.printrevo.tech.userservice.services.verification.command.VerificationOriginatorService;
import com.printrevo.tech.userservice.services.verification.command.instructions.VerificationOriginatorCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class UserVerificationOriginatorService extends VerificationOriginatorService {

    private final UpdatePersonalUserDetailsFlowService updatePersonalUserDetailsFlowService;

    private static UpdateUserFlowCommand getUpdateUserFlowCommand(VerificationRequest verificationRequest) {
        var user = verificationRequest.getUser();

        var updateCommand = new UpdateUserFlowCommand();
        updateCommand.setUserRefId(user.getId());

        if (verificationRequest.getVerificationType().equals(VerificationType.EMAIL)) {
            updateCommand.setEmail(verificationRequest.getIdentityValue());
            updateCommand.setEmailVerified(true);
        } else if (verificationRequest.getVerificationType().equals(VerificationType.PHONE_NUMBER)) {
            updateCommand.setPhoneNumber(verificationRequest.getIdentityValue());
            updateCommand.setPhoneNumberVerified(true);
        }
        return updateCommand;
    }

    @Override
    public Result<String> execute(VerificationOriginatorCommand command) {

        var verificationRequest = command.getVerificationRequest();

        if (Boolean.FALSE.equals(verificationRequest.getVerified()))
            return new Result.ResultBuilder<String>()
                    .withStatus(new ResultStatus(HttpStatus.FORBIDDEN, getForbiddenMessage(verificationRequest)))
                    .build();

        var details = verificationRequest.getDetails();

        if (isNull(details) || details.isEmpty())
            return new Result.ResultBuilder<String>()
                    .withStatus(new ResultStatus(HttpStatus.BAD_REQUEST
                            , "Verification object not present"))
                    .build();

        var updateCommand = getUpdateUserFlowCommand(verificationRequest);

        var updatePersonalUserDetailsResult = updatePersonalUserDetailsFlowService.decorate(updateCommand);

        if (updatePersonalUserDetailsResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(updatePersonalUserDetailsResult)
                    .build();

        return new Result.ResultBuilder<String>()
                .withId(updatePersonalUserDetailsResult.getData().getId())
                .withMessage("User successfully verified")
                .build();
    }

    public String getForbiddenMessage(VerificationRequest verificationRequest) {
        var verificationType = verificationRequest.getVerificationType();
        var identifier = "";
        if (verificationType == VerificationType.PHONE_NUMBER) identifier = "Phone number";
        else if (verificationType == VerificationType.EMAIL) identifier = "Email";
        return String.format("%s not verified!", identifier);
    }

    @Override
    public OriginatingSource originatingSource() {
        return OriginatingSource.USER_VERIFICATION;
    }
}
