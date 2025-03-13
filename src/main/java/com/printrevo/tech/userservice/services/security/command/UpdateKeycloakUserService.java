package com.printrevo.tech.userservice.services.security.command;

import com.printrevo.tech.commonsecurity.clients.KeycloakAPIClient;
import com.printrevo.tech.commonsecurity.config.SecurityProperties;
import com.printrevo.tech.commonsecurity.constants.AuthSource;
import com.printrevo.tech.commonsecurity.constants.AuthSourceContextHolder;
import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.services.security.command.instructions.UpdateKeycloakUserCommand;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UpdateKeycloakUserService extends CommandBaseService<UpdateKeycloakUserCommand, Void> {

    private final SecurityProperties securityProperties;
    private final KeycloakAPIClient keycloakAPIClient;

    public UpdateKeycloakUserService(SecurityProperties securityProperties, KeycloakAPIClient keycloakAPIClient) {
        this.securityProperties = securityProperties;
        this.keycloakAPIClient = keycloakAPIClient;
    }

    @Override
    public Result<Void> execute(UpdateKeycloakUserCommand command) {

        try {
            AuthSourceContextHolder.setValue(AuthSource.CLIENT);
            keycloakAPIClient.editUser(securityProperties.getClient().getRealm(), command.getKeycloakUser()
                    , command.getAuthServerId());
        } catch (Exception e) {
            return new Result.ResultBuilder<Void>()
                    .withStatus(new ResultStatus(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred"))
                    .build();
        } finally {
            AuthSourceContextHolder.clearValue();
        }

        return new Result.ResultBuilder<Void>()
                .build();
    }
}
