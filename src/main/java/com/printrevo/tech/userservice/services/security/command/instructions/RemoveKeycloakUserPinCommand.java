package com.printrevo.tech.userservice.services.security.command.instructions;

import com.printrevo.tech.commonsecurity.dto.keycloak.SecurityUser;
import com.printrevo.tech.platform.services.Command;
import com.printrevo.tech.platform.validators.Validate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Validate
@NoArgsConstructor
@AllArgsConstructor
public class RemoveKeycloakUserPinCommand implements Command {

    @NotNull(message = "Security user is required")
    private SecurityUser securityUser;
}
