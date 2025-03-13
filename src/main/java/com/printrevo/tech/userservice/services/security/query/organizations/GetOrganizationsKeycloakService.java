package com.printrevo.tech.userservice.services.security.query.organizations;

import com.printrevo.tech.commonsecurity.clients.KeycloakOrganizationsAPIClient;
import com.printrevo.tech.commonsecurity.config.SecurityProperties;
import com.printrevo.tech.commonsecurity.constants.AuthSource;
import com.printrevo.tech.commonsecurity.constants.AuthSourceContextHolder;
import com.printrevo.tech.commonsecurity.dto.keycloak.organizations.Organization;
import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.QueryBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.services.security.query.organizations.instructions.GetOrganizationsKeycloakQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetOrganizationsKeycloakService
        extends QueryBaseService<GetOrganizationsKeycloakQuery, List<Organization>> {

    private final KeycloakOrganizationsAPIClient keycloakOrganizationsAPIClient;
    private final SecurityProperties securityProperties;

    @Override
    public Result<List<Organization>> execute(GetOrganizationsKeycloakQuery command) {

        try {

            AuthSourceContextHolder.setValue(AuthSource.CLIENT);

            var organizations = keycloakOrganizationsAPIClient.getAllOrganizations(
                    securityProperties.getClient().getRealm(),
                    null, 0, 100, command.getSearchQuery());

            return new Result.ResultBuilder<List<Organization>>()
                    .withData(organizations)
                    .withMessage("Organizations retrieved successfully")
                    .build();

        } catch (Exception e) {
            return new Result.ResultBuilder<List<Organization>>()
                    .withStatus(new ResultStatus(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred"))
                    .build();
        } finally {
            AuthSourceContextHolder.clearValue();
        }
    }
}
