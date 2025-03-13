package com.printrevo.tech.userservice.services.security.command.instructions;

import com.printrevo.tech.platform.services.Command;
import com.printrevo.tech.platform.validators.Validate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Validate
@NoArgsConstructor
@AllArgsConstructor
public class LogoutUserCommand implements Command {

    @NotEmpty(message = "Refresh token is required")
    private String refreshToken;

    private Boolean publicClient;
}
