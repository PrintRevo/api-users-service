package com.printrevo.tech.userservice.services.security.command.instructions;

import com.printrevo.tech.platform.services.Command;
import com.printrevo.tech.userservice.entities.core.users.dto.SysUserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserKeycloakCommand implements Command {
    private SysUserDto userDto;
}
