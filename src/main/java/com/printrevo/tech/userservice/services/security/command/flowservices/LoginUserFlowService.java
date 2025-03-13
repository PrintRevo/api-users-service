package com.printrevo.tech.userservice.services.security.command.flowservices;

import com.printrevo.tech.commonsecurity.dto.AccessToken;
import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.db.TransactionFilter;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.users.constants.UserStatus;
import com.printrevo.tech.userservice.entities.core.users.dto.SysUserDto;
import com.printrevo.tech.userservice.services.security.command.LoginUserKeycloakService;
import com.printrevo.tech.userservice.services.security.command.flowservices.instructions.LoginUserFlowCommand;
import com.printrevo.tech.userservice.services.users.query.GetUserPageByIdentifierQueryService;
import com.printrevo.tech.userservice.services.users.query.instructions.GetUserByIdentifierQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class LoginUserFlowService extends CommandBaseService<LoginUserFlowCommand, AccessToken> {

    private final GetUserPageByIdentifierQueryService getUserPageByIdentifierQueryService;
    private final LoginUserKeycloakService loginUserKeycloakService;

    @Override
    public Result<AccessToken> execute(LoginUserFlowCommand command) {

        var presentUsers = getUserPageByIdentifierQueryService.decorate(
                new GetUserByIdentifierQuery(new TransactionFilter(Map.of("email", command.getEmail()))));

        if (presentUsers.isFailed())
            return new Result.ResultBuilder<AccessToken>()
                    .withIsFailed(true)
                    .withStatus(new ResultStatus(HttpStatus.INTERNAL_SERVER_ERROR
                            , "Contact admin for assistance"))
                    .build();

        var activeUser = getActiveUser(presentUsers.getData());

        if (activeUser.isEmpty())
            return new Result.ResultBuilder<AccessToken>()
                    .withIsFailed(true)
                    .withStatus(new ResultStatus(HttpStatus.UNAUTHORIZED
                            , "Unauthorized!"))
                    .build();

        var loginUserKeycloakResult = loginUserKeycloakService.decorate(command);

        if (loginUserKeycloakResult.isFailed())
            return new Result.ResultBuilder<AccessToken>()
                    .withIsFailed(true)
                    .received(loginUserKeycloakResult)
                    .build();

        return new Result.ResultBuilder<AccessToken>()
                .withData(loginUserKeycloakResult.getData())
                .build();
    }

    private Optional<SysUserDto> getActiveUser(Page<SysUserDto> presentUsers) {
        return presentUsers.stream()
                .filter(userDto -> nonNull(userDto.getEmailVerified()))
                .filter(SysUserDto::getEmailVerified)
                .filter(userDto -> userDto.getUserStatus().equals(UserStatus.ACTIVE))
                .findFirst();

    }
}
