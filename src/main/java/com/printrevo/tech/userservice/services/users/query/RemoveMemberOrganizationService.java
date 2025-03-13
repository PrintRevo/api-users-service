package com.printrevo.tech.userservice.services.users.query;

import com.printrevo.tech.commonsecurity.clients.KeycloakOrganizationsAPIClient;
import com.printrevo.tech.commonsecurity.config.SecurityProperties;
import com.printrevo.tech.commonsecurity.constants.AuthSource;
import com.printrevo.tech.commonsecurity.constants.AuthSourceContextHolder;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.services.users.query.instructions.RemoveMemberFromOrganizationCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RemoveMemberOrganizationService extends CommandBaseService<RemoveMemberFromOrganizationCommand, Void> {

    private final SecurityProperties securityProperties;
    private final KeycloakOrganizationsAPIClient keycloakOrganizationsAPIClient;

    public RemoveMemberOrganizationService(SecurityProperties securityProperties,
                                           KeycloakOrganizationsAPIClient keycloakOrganizationsAPIClient) {
        this.securityProperties = securityProperties;
        this.keycloakOrganizationsAPIClient = keycloakOrganizationsAPIClient;
    }

    @Override
    public Result<Void> execute(RemoveMemberFromOrganizationCommand command) {

        try {

            AuthSourceContextHolder.setValue(AuthSource.CLIENT);

            keycloakOrganizationsAPIClient.removeOrganizationMember(
                    securityProperties.getClient().getRealm(),
                    command.getOrganizationId(),
                    command.getUserAuthServerId()
            );

            return new Result.ResultBuilder<Void>()
                    .withMessage("Member removed from organization successfully")
                    .build();

        } catch (Exception e) {
            log.error("Error removing member from organization: ", e);
            return new Result.ResultBuilder<Void>()
                    .withIsFailed(true)
                    .withMessage(e.getLocalizedMessage())
                    .build();

        } finally {

            AuthSourceContextHolder.clearValue();

        }
    }
}
