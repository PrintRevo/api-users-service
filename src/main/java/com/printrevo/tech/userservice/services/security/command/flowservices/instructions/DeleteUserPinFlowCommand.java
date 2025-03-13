package com.printrevo.tech.userservice.services.security.command.flowservices.instructions;

import com.printrevo.tech.platform.services.Command;
import com.printrevo.tech.platform.validators.Validate;
import com.printrevo.tech.platform.validators.ValidateAuthorization;
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
        fallBackExpressions = {"@usersUtils.isSecurityContextUser(userRefId)"},
        message = "Access denied: Cannot delete this user's pin.")
public class DeleteUserPinFlowCommand implements Command {

    @NotBlank(message = "User reference id is required")
    @NotEmpty(message = "User reference id is required")
    private String userRefId;
}
