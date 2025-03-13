package com.printrevo.tech.userservice.services.security.command.organization.flowservices.instructions;

import com.printrevo.tech.platform.services.Command;
import com.printrevo.tech.platform.validators.Validate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Validate
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrganizationKeycloakFlowCommand implements Command {

    @NotEmpty(message = "user auth server id is required")
    private String userAuthServerId;

    @NotEmpty(message = "organization name is required")
    private String name;

    @NotEmpty(message = "organization realm is required")
    private String organizationNumber;


    private Map<String, List<String>> attributes = new HashMap<>();
}
