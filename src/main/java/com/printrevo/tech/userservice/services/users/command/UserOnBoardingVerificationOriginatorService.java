package com.printrevo.tech.userservice.services.users.command;

import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.db.TransactionFilter;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.users.constants.UserStatus;
import com.printrevo.tech.userservice.entities.core.users.models.SysUser;
import com.printrevo.tech.userservice.entities.core.users.repositories.SysUserRepository;
import com.printrevo.tech.userservice.entities.core.verification.constansts.OriginatingSource;
import com.printrevo.tech.userservice.entities.core.verification.constansts.VerificationType;
import com.printrevo.tech.userservice.entities.core.verification.models.VerificationRequest;
import com.printrevo.tech.userservice.services.users.query.GetUserPageByIdentifierQueryService;
import com.printrevo.tech.userservice.services.users.query.instructions.GetUserByIdentifierQuery;
import com.printrevo.tech.userservice.services.verification.command.VerificationOriginatorService;
import com.printrevo.tech.userservice.services.verification.command.instructions.VerificationOriginatorCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserOnBoardingVerificationOriginatorService extends VerificationOriginatorService {

    private final GetUserPageByIdentifierQueryService getUserPageByIdentifierQueryService;
    private final SysUserRepository userRepository;

    @Override
    public Result<String> execute(VerificationOriginatorCommand command) {

        var verificationRequest = command.getVerificationRequest();

        if (Boolean.FALSE.equals(verificationRequest.getVerified()))
            return new Result.ResultBuilder<String>()
                    .withStatus(new ResultStatus(HttpStatus.FORBIDDEN, getForbiddenMessage(verificationRequest)))
                    .build();

        var updatedUser = verifyUser(verificationRequest);

        var activeUsersQueryResult = getUserPageByIdentifierQueryService.decorate(
                new GetUserByIdentifierQuery(new TransactionFilter(
                        getIdentifiers(verificationRequest, updatedUser))));

        if (activeUsersQueryResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(activeUsersQueryResult)
                    .build();

        if (!activeUsersQueryResult.getData().isEmpty())
            return new Result.ResultBuilder<String>()
                    .withStatus(new ResultStatus(HttpStatus.CONFLICT, "User already exists"))
                    .build();

        updateUserStatus(updatedUser);

        return new Result.ResultBuilder<String>()
                .withId(updatedUser.getId())
                .withMessage("User successfully verified")
                .build();
    }

    private void updateUserStatus(SysUser user) {
        if (user.getUserStatus().equals(UserStatus.ON_BOARDING))
            user.setUserStatus(UserStatus.ORIGINATING);
        userRepository.save(user);
    }

    private Map<String, Object> getIdentifiers(VerificationRequest verificationRequest, SysUser updatedUser) {
        var identifiers = new HashMap<String, Object>();
        identifiers.put("userStatus", UserStatus.ACTIVE);

        var verificationType = verificationRequest.getVerificationType();

        if (verificationType.equals(VerificationType.PHONE_NUMBER))
            identifiers.put("phoneNumber", updatedUser.getPhoneNumber());

        if (verificationType.equals(VerificationType.EMAIL))
            identifiers.put("email", updatedUser.getEmail());

        return identifiers;
    }

    private SysUser verifyUser(VerificationRequest verificationRequest) {
        var user = verificationRequest.getUser();
        var verificationType = verificationRequest.getVerificationType();

        if (verificationType.equals(VerificationType.EMAIL)) {
            user.setEmail(verificationRequest.getIdentityValue());
            user.setEmailVerified(true);
        } else if (verificationType.equals(VerificationType.PHONE_NUMBER)) {
            user.setPhoneNumber(verificationRequest.getIdentityValue());
            user.setPhoneNumberVerified(true);
        }

        return userRepository.save(user);
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
        return OriginatingSource.ON_BOARDING;
    }
}
