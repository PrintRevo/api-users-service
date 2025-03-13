package com.printrevo.tech.userservice.services.security.command.flowservices.instructions;

import com.printrevo.tech.platform.services.Command;
import com.printrevo.tech.platform.validators.Validate;
import com.printrevo.tech.platform.validators.ValidateAuthorization;
import com.printrevo.tech.starter.logbook.annotation.Sensitive;
import com.printrevo.tech.userservice.api.auth.body.ChangeUserPasswordReqDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Validate
@NoArgsConstructor
@AllArgsConstructor
@ValidateAuthorization(
        permissions = {"UPDATE_USER_PASSWORD"},
        fallBackExpressions = {
                "@usersUtils.isSecurityContextUser(userRefId)",
                "bypassAuthValidation == true"},
        message = "Access denied: Cannot change this user's password.")
public class UpdateUserPasswordFlowCommand implements Command {

    @NotBlank(message = "User ref id is required")
    @NotEmpty(message = "User ref id is required")
    private String userRefId;

    @Sensitive
    @NotBlank(message = "Password is required")
    @NotEmpty(message = "Password is required")
    private String password;

    private Boolean isTemporary;

    private Boolean bypassAuthValidation;

    public UpdateUserPasswordFlowCommand(ChangeUserPasswordReqDto changeUserPasswordReqDto) {
        this.userRefId = changeUserPasswordReqDto.getUserRefId();
        this.password = changeUserPasswordReqDto.getPassword();
        this.isTemporary = false;
    }
}
