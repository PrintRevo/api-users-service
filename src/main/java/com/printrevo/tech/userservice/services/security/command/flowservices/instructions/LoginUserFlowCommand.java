package com.printrevo.tech.userservice.services.security.command.flowservices.instructions;

import com.printrevo.tech.platform.services.Command;
import com.printrevo.tech.platform.validators.Validate;
import com.printrevo.tech.starter.logbook.annotation.Sensitive;
import com.printrevo.tech.userservice.api.auth.body.LoginUserBody;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;


@Data
@Validate
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserFlowCommand implements Command {

    @NotBlank(message = "Email is required")
    @NotEmpty(message = "Email is required")
    @Email(regexp = ".+[@].+[\\.].+", message = "Email is invalid")
    private String email;

    @Sensitive
    @NotBlank(message = "Password is required")
    @NotEmpty(message = "Password is required")
    private String password;

    public LoginUserFlowCommand(LoginUserBody body) {
        setEmail(body.getEmail());
        setPassword(body.getPassword());
    }
}
