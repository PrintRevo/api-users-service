package com.printrevo.tech.userservice.services.security.command.instructions;

import com.printrevo.tech.platform.services.Command;
import com.printrevo.tech.platform.validators.Validate;
import com.printrevo.tech.starter.logbook.annotation.Sensitive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Validate
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordValidationCommand implements Command {

    @Sensitive
    @NotBlank(message = "Password is required")
    @NotEmpty(message = "Password is required")
    private String password;
}
