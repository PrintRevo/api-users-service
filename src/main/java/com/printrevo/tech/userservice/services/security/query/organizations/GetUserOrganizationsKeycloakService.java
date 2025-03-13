package com.printrevo.tech.userservice.services.security.query.organizations;

import com.printrevo.tech.commonsecurity.clients.KeycloakOrganizationsAPIClient;
import com.printrevo.tech.commonsecurity.config.SecurityProperties;
import com.printrevo.tech.commonsecurity.constants.AuthSource;
import com.printrevo.tech.commonsecurity.constants.AuthSourceContextHolder;
import com.printrevo.tech.commonsecurity.dto.keycloak.organizations.CurrentOrgUser;
import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.QueryBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.services.security.query.organizations.instructions.GetUserOrganizationsKeycloakQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetUserOrganizationsKeycloakService
        extends QueryBaseService<GetUserOrganizationsKeycloakQuery, Map<String, CurrentOrgUser>> {

    private final KeycloakOrganizationsAPIClient keycloakOrganizationsAPIClient;
    private final SecurityProperties securityProperties;

    @Override
    public Result<Map<String, CurrentOrgUser>> execute(GetUserOrganizationsKeycloakQuery command) {

        try {

            AuthSourceContextHolder.setValue(AuthSource.SESSION);

            var organizations = keycloakOrganizationsAPIClient
                    .getCurrentUserOrganizations(securityProperties.getClient().getRealm());

            return new Result.ResultBuilder<Map<String, CurrentOrgUser>>()
                    .withData(organizations)
                    .withMessage("Organizations retrieved successfully")
                    .build();

        } catch (Exception e) {
            log.error("Error fetching user organizations ", e);
            return new Result.ResultBuilder<Map<String, CurrentOrgUser>>()
                    .withStatus(new ResultStatus(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Unexpected error occurred"))
                    .build();
        } finally {
            AuthSourceContextHolder.clearValue();
        }
    }
}
