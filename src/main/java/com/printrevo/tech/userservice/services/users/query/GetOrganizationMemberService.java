package com.printrevo.tech.userservice.services.users.query;

import com.printrevo.tech.commonsecurity.clients.KeycloakOrganizationsAPIClient;
import com.printrevo.tech.commonsecurity.config.SecurityProperties;
import com.printrevo.tech.commonsecurity.constants.AuthSource;
import com.printrevo.tech.commonsecurity.constants.AuthSourceContextHolder;
import com.printrevo.tech.commonsecurity.dto.keycloak.SecurityUser;
import com.printrevo.tech.platform.decorator.QueryBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.services.users.query.instructions.GetOrganizationMemberQuery;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetOrganizationMemberService extends QueryBaseService<GetOrganizationMemberQuery, Optional<SecurityUser>> {

    private final SecurityProperties securityProperties;
    private final KeycloakOrganizationsAPIClient keycloakOrganizationsAPIClient;

    public GetOrganizationMemberService(SecurityProperties securityProperties,
                                        KeycloakOrganizationsAPIClient keycloakOrganizationsAPIClient) {
        this.securityProperties = securityProperties;
        this.keycloakOrganizationsAPIClient = keycloakOrganizationsAPIClient;
    }

    @Override
    public Result<Optional<SecurityUser>> execute(GetOrganizationMemberQuery query) {
        try {
            AuthSourceContextHolder.setValue(AuthSource.CLIENT);

            var organizationMember = Optional.ofNullable(
                    keycloakOrganizationsAPIClient.getOrganizationMember(
                            securityProperties.getClient().getRealm(),
                            query.getOrganizationId(),
                            query.getUserAuthServerId()
                    )
            );

            return new Result.ResultBuilder<Optional<SecurityUser>>()
                    .withMessage("Member retrieved successfully")
                    .withData(organizationMember)
                    .build();

        } catch (Exception e) {
            return new Result.ResultBuilder<Optional<SecurityUser>>()
                    .withData(Optional.empty())
                    .withMessage(e.getLocalizedMessage())
                    .build();
        } finally {
            AuthSourceContextHolder.clearValue();
        }
    }
}
