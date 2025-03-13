package com.printrevo.tech.userservice.services.security.command.instructions;

import com.printrevo.tech.commonsecurity.dto.keycloak.KeycloakUser;
import com.printrevo.tech.platform.services.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateKeycloakUserCommand implements Command {
    private KeycloakUser keycloakUser;
    private String authServerId;
}
