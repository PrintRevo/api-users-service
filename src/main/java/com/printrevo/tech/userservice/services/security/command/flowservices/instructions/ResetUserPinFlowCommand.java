package com.printrevo.tech.userservice.services.security.command.flowservices.instructions;

import com.printrevo.tech.commonsecurity.constants.PinStatus;
import com.printrevo.tech.platform.services.Command;
import com.printrevo.tech.platform.validators.Validate;
import com.printrevo.tech.platform.validators.ValidateAuthorization;
import com.printrevo.tech.starter.logbook.annotation.Sensitive;
import com.printrevo.tech.userservice.api.auth.body.ResetUserPinBody;
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
        message = "Access denied: Cannot reset this user's pin.")
public class ResetUserPinFlowCommand implements Command {

    @NotBlank(message = "userRefId is required")
    @NotEmpty(message = "userRefId is required")
    private String userRefId;

    @Sensitive
    @NotBlank(message = "pin is required")
    @NotEmpty(message = "pin is required")
    @Size(min = 6, max = 6, message = "pin must be 6 digits")
    private String pin;

    @NotNull(message = "status is required")
    private PinStatus status;

    public ResetUserPinFlowCommand(ResetUserPinBody resetUserPinBody) {
        this.userRefId = resetUserPinBody.getUserRefId();
        this.pin = resetUserPinBody.getPin();
        this.status = resetUserPinBody.getStatus();
    }
}
