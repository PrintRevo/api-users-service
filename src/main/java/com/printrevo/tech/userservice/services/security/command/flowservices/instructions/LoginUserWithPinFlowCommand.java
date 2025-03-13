package com.printrevo.tech.userservice.services.security.command.flowservices.instructions;

import com.printrevo.tech.commonsecurity.constants.PinIdentifier;
import com.printrevo.tech.platform.services.Command;
import com.printrevo.tech.platform.validators.Validate;
import com.printrevo.tech.starter.logbook.annotation.Sensitive;
import com.printrevo.tech.userservice.api.auth.body.LoginUserWithPinBody;
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
public class LoginUserWithPinFlowCommand implements Command {

    @NotEmpty(message = "Identifier is required")
    private String identifier;

    @NotNull(message = "Identifier type is required")
    private PinIdentifier identifierType;

    @Sensitive
    @Size(min = 6, max = 6, message = "Pin must be 6 digits")
    @NotNull(message = "Pin is required")
    private String pin;

    public LoginUserWithPinFlowCommand(LoginUserWithPinBody body) {
        setIdentifier(body.getIdentifier());
        setIdentifierType(body.getIdentifierType());
        setPin(body.getPin());
    }
}
