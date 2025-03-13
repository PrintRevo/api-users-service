package com.printrevo.tech.userservice.services.security.command;

import com.printrevo.tech.commonsecurity.clients.KeycloakAPIClient;
import com.printrevo.tech.commonsecurity.config.SecurityProperties;
import com.printrevo.tech.commonsecurity.constants.AuthSource;
import com.printrevo.tech.commonsecurity.constants.AuthSourceContextHolder;
import com.printrevo.tech.commonsecurity.dto.keycloak.CreateSecurityUserDto;
import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.users.dto.SysUserDto;
import com.printrevo.tech.userservice.services.security.command.instructions.CreateUserKeycloakCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateUserKeycloakService extends CommandBaseService<CreateUserKeycloakCommand, Void> {

    private final SecurityProperties securityProperties;
    private final KeycloakAPIClient keycloakAPIClient;

    @Override
    public Result<Void> execute(CreateUserKeycloakCommand command) {

        try {
            var createUserDTO = getCreateUserDTO(command.getUserDto());
            AuthSourceContextHolder.setValue(AuthSource.CLIENT);
            keycloakAPIClient.createUser(securityProperties.getClient().getRealm(), createUserDTO);
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

    public CreateSecurityUserDto getCreateUserDTO(SysUserDto userDto) {
        var dto = new CreateSecurityUserDto();
        dto.setCreatedTimestamp(LocalDate.now().toEpochDay());
        dto.setEmail(userDto.getEmail());
        dto.setEnabled(true);
        dto.setEmailVerified(userDto.getEmailVerified());
        dto.setUsername(userDto.getId());
        if (nonNull(userDto.getPhoneNumber()))
            dto.addAttribute("phoneNumber", List.of(userDto.getPhoneNumber()));
        dto.addAttribute("users-service-id", List.of(userDto.getId()));
        dto.setGroups(List.of("da-remit"));
        return dto;
    }
}
