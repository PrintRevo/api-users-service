package com.printrevo.tech.userservice.services.security.command.flowservices.instructions;

import com.printrevo.tech.platform.services.Command;
import com.printrevo.tech.userservice.entities.core.users.dto.SysUserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateKeycloakUserFlowCommand implements Command {
    private SysUserDto userDto;
}
