package com.printrevo.tech.userservice.services.security.command;

import com.printrevo.tech.commonsecurity.clients.KeycloakAPIClient;
import com.printrevo.tech.commonsecurity.config.SecurityProperties;
import com.printrevo.tech.commonsecurity.constants.AuthSource;
import com.printrevo.tech.commonsecurity.constants.AuthSourceContextHolder;
import com.printrevo.tech.commonsecurity.dto.keycloak.SecurityGroup;
import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.services.security.command.instructions.AddUserToSecurityGroupCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;


@Slf4j
@Service
public class AddUserToSecurityGroupService
        extends CommandBaseService<AddUserToSecurityGroupCommand, Void> {

    private final SecurityProperties securityProperties;
    private final KeycloakAPIClient keycloakAPIClient;

    public AddUserToSecurityGroupService(SecurityProperties securityProperties, KeycloakAPIClient keycloakAPIClient) {
        this.securityProperties = securityProperties;
        this.keycloakAPIClient = keycloakAPIClient;
    }

    @Override
    public Result<Void> execute(AddUserToSecurityGroupCommand command) {

        try {
            AuthSourceContextHolder.setValue(AuthSource.CLIENT);

            var theGroup = getAllGroups().stream().filter(securityGroup ->
                    securityGroup.getName().equals(command.getGroupName())).findFirst();

            if (theGroup.isEmpty()) return new Result.ResultBuilder<Void>()
                    .withStatus(new ResultStatus(HttpStatus.NOT_FOUND, "Group not found"))
                    .build();

            keycloakAPIClient.addUserToGroup(securityProperties.getClient().getRealm(), command.getUserAuthServerId(),
                    theGroup.get().getId());
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return new Result.ResultBuilder<Void>()
                    .withStatus(new ResultStatus(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage()))
                    .build();
        } finally {
            AuthSourceContextHolder.clearValue();
        }

        return new Result.ResultBuilder<Void>()
                .build();
    }

    private List<SecurityGroup> getAllGroups() {
        List<SecurityGroup> groupsList = new ArrayList<>();
        var groups = keycloakAPIClient.getAllGroups(securityProperties.getClient().getRealm());
        for (var group : groups) {
            addGroup(groupsList, group);
        }
        return groupsList;
    }

    private void addGroup(List<SecurityGroup> securityGroups, SecurityGroup securityGroup) {
        securityGroups.add(securityGroup);

        if (nonNull(securityGroup.getSubGroups()) && Boolean.FALSE.equals(securityGroup.getSubGroups().isEmpty())) {
            for (var group : securityGroup.getSubGroups()) {
                addGroup(securityGroups, group);
            }
        }
    }
}
