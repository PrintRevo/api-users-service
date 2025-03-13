package com.printrevo.tech.userservice.services.security.query;

import com.printrevo.tech.commonsecurity.clients.KeycloakAPIClient;
import com.printrevo.tech.commonsecurity.config.SecurityProperties;
import com.printrevo.tech.commonsecurity.constants.AuthSource;
import com.printrevo.tech.commonsecurity.constants.AuthSourceContextHolder;
import com.printrevo.tech.commonsecurity.dto.keycloak.SecurityUser;
import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.QueryBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.services.security.query.instructions.GetKeycloakUserByIdQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetKeycloakUserByIdService extends QueryBaseService<GetKeycloakUserByIdQuery, SecurityUser> {

    private final SecurityProperties securityProperties;
    private final KeycloakAPIClient keycloakAPIClient;

    @Override
    public Result<SecurityUser> execute(GetKeycloakUserByIdQuery command) {

        try {
            AuthSourceContextHolder.setValue(AuthSource.CLIENT);
            var user = keycloakAPIClient.getUser(securityProperties.getClient().getRealm(), command.getAuthServerId());

            if (isNull(user)) {
                return new Result.ResultBuilder<SecurityUser>()
                        .withStatus(new ResultStatus(HttpStatus.BAD_REQUEST, "User not found"))
                        .build();
            }

            return new Result.ResultBuilder<SecurityUser>()
                    .withData(user)
                    .build();

        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return new Result.ResultBuilder<SecurityUser>()
                    .withStatus(new ResultStatus(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred"))
                    .build();
        } finally {
            AuthSourceContextHolder.clearValue();
        }
    }
}
