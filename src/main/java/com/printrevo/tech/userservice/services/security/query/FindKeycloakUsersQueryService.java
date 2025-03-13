package com.printrevo.tech.userservice.services.security.query;

import com.printrevo.tech.commonsecurity.clients.KeycloakAPIClient;
import com.printrevo.tech.commonsecurity.config.SecurityProperties;
import com.printrevo.tech.commonsecurity.constants.AuthSource;
import com.printrevo.tech.commonsecurity.constants.AuthSourceContextHolder;
import com.printrevo.tech.commonsecurity.dto.keycloak.SecurityUser;
import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.QueryBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.services.security.query.instructions.FindKeycloakUsersQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindKeycloakUsersQueryService
        extends QueryBaseService<FindKeycloakUsersQuery, List<SecurityUser>> {

    private final SecurityProperties securityProperties;
    private final KeycloakAPIClient keycloakAPIClient;

    @Override
    public Result<List<SecurityUser>> execute(FindKeycloakUsersQuery command) {

        try {
            AuthSourceContextHolder.setValue(AuthSource.CLIENT);
            var users = keycloakAPIClient.getAllUsers(securityProperties.getClient().getRealm(),
                    getQueryParams(command.getQueryFields()));
            return new Result.ResultBuilder<List<SecurityUser>>()
                    .withData(users)
                    .build();
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return new Result.ResultBuilder<List<SecurityUser>>()
                    .withStatus(new ResultStatus(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred"))
                    .build();
        } finally {
            AuthSourceContextHolder.clearValue();
        }
    }

    private Map<String, String> getQueryParams(Map<String, Object> queryFields) {
        var query = new HashMap<String, String>();
        for (var field : queryFields.entrySet()) {
            query.put(field.getKey(), String.valueOf(field.getValue()));
        }
        return query;
    }
}
