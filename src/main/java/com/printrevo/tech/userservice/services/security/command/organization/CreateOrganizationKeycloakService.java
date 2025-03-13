package com.printrevo.tech.userservice.services.security.command.organization;

import com.printrevo.tech.commonsecurity.clients.KeycloakOrganizationsAPIClient;
import com.printrevo.tech.commonsecurity.config.SecurityProperties;
import com.printrevo.tech.commonsecurity.constants.AuthSource;
import com.printrevo.tech.commonsecurity.constants.AuthSourceContextHolder;
import com.printrevo.tech.commonsecurity.dto.keycloak.organizations.CreateOrganizationDto;
import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.services.security.command.organization.flowservices.instructions.CreateOrganizationKeycloakFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class CreateOrganizationKeycloakService
        extends CommandBaseService<CreateOrganizationKeycloakFlowCommand, Void> {

    private final KeycloakOrganizationsAPIClient keycloakOrganizationsAPIClient;
    private final SecurityProperties securityProperties;

    public CreateOrganizationKeycloakService(KeycloakOrganizationsAPIClient keycloakOrganizationsAPIClient
            , SecurityProperties securityProperties) {
        this.keycloakOrganizationsAPIClient = keycloakOrganizationsAPIClient;
        this.securityProperties = securityProperties;
    }

    @Override
    public Result<Void> execute(CreateOrganizationKeycloakFlowCommand command) {

        try {

            AuthSourceContextHolder.setValue(AuthSource.CLIENT);

            var realm = securityProperties.getClient().getRealm();

            var organization = createOrganization(command, realm);

            keycloakOrganizationsAPIClient.createOrganization(realm, organization, command.getUserAuthServerId());

            return new Result.ResultBuilder<Void>()
                    .withMessage("Organization created successfully")
                    .build();

        } catch (Exception e) {
            log.error("Error creating organization", e);
            return new Result.ResultBuilder<Void>()
                    .withStatus(new ResultStatus(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage()))
                    .build();
        } finally {
            AuthSourceContextHolder.clearValue();
        }
    }

    private CreateOrganizationDto createOrganization(
            CreateOrganizationKeycloakFlowCommand command, String realm) {
        var org = new CreateOrganizationDto();
        org.setName(command.getName());
        org.setRealm(realm);
        org.setOrganizationNumber(command.getOrganizationNumber());
        org.setAttributes(command.getAttributes());
        return org;
    }
}

