package com.printrevo.tech.userservice.services.security.command.instructions;

import com.printrevo.tech.platform.services.Command;
import com.printrevo.tech.platform.validators.Validate;
import com.printrevo.tech.starter.logbook.annotation.Sensitive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Validate
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserWithPinKeycloakCommand implements Command {

    @NotNull(message = "Identifier is required")
    @NotEmpty(message = "Identifier is required")
    private String identifier;

    @Sensitive
    @Size(min = 6, max = 6, message = "Pin must be 6 digits")
    @NotNull(message = "Pin is required")
    private String pin;
}
