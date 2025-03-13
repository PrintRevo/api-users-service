package com.printrevo.tech.userservice.services.products.query;

import com.printrevo.tech.commonsecurity.clients.KeycloakAPIClient;
import com.printrevo.tech.commonsecurity.config.SecurityProperties;
import com.printrevo.tech.commonsecurity.constants.AuthSource;
import com.printrevo.tech.commonsecurity.constants.AuthSourceContextHolder;
import com.printrevo.tech.commonsecurity.dto.keycloak.SecurityGroup;
import com.printrevo.tech.commonsecurity.helpers.session.CurrentUser;
import com.printrevo.tech.commonsecurity.helpers.session.SessionHelper;
import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.QueryBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.services.products.query.instructions.GetKeyCloakUserGroups;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class GetKeyCloakUserGroupQueryService
        extends QueryBaseService<GetKeyCloakUserGroups, List<SecurityGroup>> {


    private final SessionHelper sessionHelper;

    private final SecurityProperties securityProperties;

    private final KeycloakAPIClient keycloakAPIClient;

    @Override
    public Result<List<SecurityGroup>> execute(GetKeyCloakUserGroups query) {
        Optional<CurrentUser> currentUser = sessionHelper.getCurrentUser();

        if (currentUser.isEmpty()) {
            return new Result.ResultBuilder<List<SecurityGroup>>()
                    .withStatus(new ResultStatus(HttpStatus.UNAUTHORIZED, "No signed-in user found. Please sign in to continue"))
                    .withIsFailed(true)
                    .build();
        }
        try {
            AuthSourceContextHolder.setValue(AuthSource.CLIENT);
            List<SecurityGroup> userGroup = keycloakAPIClient.getUserGroups(securityProperties.getClient().getRealm(), currentUser.get().getUserAuthenticatorId());
            log.debug("currentUser: {}", currentUser.get());
            log.debug("userGroup: {}", userGroup);
            return new Result.ResultBuilder<List<SecurityGroup>>()
                    .withData(userGroup)
                    .withStatus(new ResultStatus(HttpStatus.OK, ""))
                    .build();
        } catch (Exception e) {
            return new Result.ResultBuilder<List<SecurityGroup>>()
                    .withStatus(new ResultStatus(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage()))
                    .build();
        } finally {
            AuthSourceContextHolder.clearValue();
        }


    }
}
