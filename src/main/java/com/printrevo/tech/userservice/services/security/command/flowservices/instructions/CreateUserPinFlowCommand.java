package com.printrevo.tech.userservice.services.security.command.flowservices.instructions;

import com.printrevo.tech.commonsecurity.constants.PinIdentifier;
import com.printrevo.tech.platform.services.Command;
import com.printrevo.tech.platform.validators.Validate;
import com.printrevo.tech.platform.validators.ValidateAuthorization;
import com.printrevo.tech.starter.logbook.annotation.Sensitive;
import com.printrevo.tech.userservice.api.auth.body.CreateUserPinBody;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Validate
@NoArgsConstructor
@AllArgsConstructor
@ValidateAuthorization(
        permissions = {"UPDATE_USER_PASSWORD"},
        fallBackExpressions = {"@usersUtils.isSecurityContextUser(userRefId)"},
        message = "Access denied: Cannot set this user's pin.")
public class CreateUserPinFlowCommand implements Command {

    @NotBlank(message = "User reference id is required")
    @NotEmpty(message = "User reference id is required")
    private String userRefId;

    @Sensitive
    @NotBlank(message = "Pin is required")
    @NotEmpty(message = "Pin is required")
    @Size(min = 6, max = 6, message = "Pin must be 6 digits")
    private String pin;

    @NotNull(message = "Pin identifier is required")
    private PinIdentifier pinIdentifier;

    public CreateUserPinFlowCommand(CreateUserPinBody createUserPinBody) {
        this.userRefId = createUserPinBody.getUserRefId();
        this.pin = createUserPinBody.getPin();
        this.pinIdentifier = createUserPinBody.getPinIdentifier();
    }
}
