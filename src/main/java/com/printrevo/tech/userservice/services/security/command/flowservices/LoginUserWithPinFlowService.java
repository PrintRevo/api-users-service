package com.printrevo.tech.userservice.services.security.command.flowservices;

import com.printrevo.tech.commonsecurity.constants.PinIdentifier;
import com.printrevo.tech.commonsecurity.constants.PinStatus;
import com.printrevo.tech.commonsecurity.dto.AccessToken;
import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.db.TransactionFilter;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.users.constants.UserStatus;
import com.printrevo.tech.userservice.entities.core.users.dto.SysUserDto;
import com.printrevo.tech.userservice.services.security.command.LoginUserWithPinKeycloakService;
import com.printrevo.tech.userservice.services.security.command.flowservices.instructions.LoginUserWithPinFlowCommand;
import com.printrevo.tech.userservice.services.security.command.instructions.LoginUserWithPinKeycloakCommand;
import com.printrevo.tech.userservice.services.users.query.GetUserPageByIdentifierQueryService;
import com.printrevo.tech.userservice.services.users.query.instructions.GetUserByIdentifierQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginUserWithPinFlowService
        extends CommandBaseService<LoginUserWithPinFlowCommand, AccessToken> {

    private final GetUserPageByIdentifierQueryService getUserPageByIdentifierQueryService;
    private final LoginUserWithPinKeycloakService loginUserKeycloakService;

    @Override
    public Result<AccessToken> execute(LoginUserWithPinFlowCommand command) {

        var presentUsers = getUserPageByIdentifierQueryService.decorate(
                new GetUserByIdentifierQuery(new TransactionFilter(Map.of(
                        getIdentifier(command), command.getIdentifier()))));

        if (presentUsers.isFailed())
            return new Result.ResultBuilder<AccessToken>()
                    .withIsFailed(true)
                    .withStatus(new ResultStatus(HttpStatus.INTERNAL_SERVER_ERROR
                            , "Contact admin for assistance"))
                    .build();

        var activeUser = getActiveUser(
                presentUsers.getData(), command.getIdentifierType());

        if (activeUser.isEmpty())
            return new Result.ResultBuilder<AccessToken>()
                    .withIsFailed(true)
                    .withStatus(new ResultStatus(HttpStatus.UNAUTHORIZED
                            , "Unauthorized!"))
                    .build();

        var loginResult = loginUserKeycloakService.decorate(
                new LoginUserWithPinKeycloakCommand(command.getIdentifier(), command.getPin()));

        if (loginResult.isFailed())
            return new Result.ResultBuilder<AccessToken>()
                    .received(loginResult)
                    .build();

        return new Result.ResultBuilder<AccessToken>()
                .withData(loginResult.getData())
                .build();
    }

    private Optional<SysUserDto> getActiveUser(Page<SysUserDto> data, PinIdentifier identifierType) {
        return data.stream()
                .filter(userDto -> userDto.getUserStatus().equals(UserStatus.ACTIVE))
                .filter(userDto -> isActive(userDto, identifierType))
                .filter(userDto -> userDto.getPinStatus() != null)
                .filter(userDto -> !userDto.getPinStatus().equals(PinStatus.INACTIVE))
                .findFirst();
    }

    private boolean isActive(SysUserDto userDto, PinIdentifier identifierType) {
        if (identifierType.equals(PinIdentifier.PHONE_NUMBER))
            return userDto.getPhoneNumberVerified() != null && userDto.getPhoneNumberVerified();
        else
            return userDto.getEmailVerified() != null && userDto.getEmailVerified();
    }


    private String getIdentifier(LoginUserWithPinFlowCommand command) {
        return command.getIdentifierType().equals(PinIdentifier.PHONE_NUMBER) ? "phoneNumber" : "email";
    }

}
