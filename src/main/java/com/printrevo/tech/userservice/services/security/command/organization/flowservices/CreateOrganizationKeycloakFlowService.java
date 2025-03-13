package com.printrevo.tech.userservice.services.security.command.organization.flowservices;

import com.printrevo.tech.commonsecurity.dto.keycloak.organizations.Organization;
import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.services.security.command.organization.CreateOrganizationKeycloakService;
import com.printrevo.tech.userservice.services.security.command.organization.flowservices.instructions.CreateOrganizationKeycloakFlowCommand;
import com.printrevo.tech.userservice.services.security.query.organizations.GetOrganizationsKeycloakService;
import com.printrevo.tech.userservice.services.security.query.organizations.instructions.GetOrganizationsKeycloakQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateOrganizationKeycloakFlowService
        extends CommandBaseService<CreateOrganizationKeycloakFlowCommand, String> {

    private final CreateOrganizationKeycloakService createOrganizationKeycloakService;
    private final GetOrganizationsKeycloakService getOrganizationsKeycloakService;

    @Override
    public Result<String> execute(CreateOrganizationKeycloakFlowCommand command) {

        var getOrganizationsResult = getOrganizations(command.getOrganizationNumber());

        if (getOrganizationsResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(getOrganizationsResult)
                    .build();

        if (!getOrganizationsResult.getData().isEmpty())
            return new Result.ResultBuilder<String>()
                    .withStatus(new ResultStatus(HttpStatus.FORBIDDEN, "Organization already exists"))
                    .build();

        var createOrganizationResult = createOrganizationKeycloakService.decorate(command);

        if (createOrganizationResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(createOrganizationResult)
                    .build();

        getOrganizationsResult = getOrganizations(command.getOrganizationNumber());

        if (getOrganizationsResult.isFailed())
            return new Result.ResultBuilder<String>()
                    .received(getOrganizationsResult)
                    .build();

        return new Result.ResultBuilder<String>()
                .withData(getOrganizationsResult.getData().get(0).getId())
                .withMessage("Organization created successfully")
                .build();
    }

    private Result<List<Organization>> getOrganizations(String organizationNumber) {
        return getOrganizationsKeycloakService.decorate(new GetOrganizationsKeycloakQuery(
                String.format("organizationNumber:%s", organizationNumber)));
    }
}
