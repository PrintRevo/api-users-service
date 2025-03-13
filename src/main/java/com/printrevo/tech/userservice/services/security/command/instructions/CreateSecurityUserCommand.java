package com.printrevo.tech.userservice.services.security.command.instructions;

import com.printrevo.tech.platform.services.Command;
import com.printrevo.tech.platform.validators.Validate;
import com.printrevo.tech.starter.logbook.annotation.Sensitive;
import com.printrevo.tech.userservice.api.auth.body.CreateSecurityUserBody;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Validate
@NoArgsConstructor
@AllArgsConstructor
public class CreateSecurityUserCommand implements Command {

    @NotBlank(message = "User reference id is required")
    @NotEmpty(message = "User reference id is required")
    private String userRefId;

    @Sensitive
    @NotBlank(message = "User password is required")
    @NotEmpty(message = "User password is required")
    private String password;

    public CreateSecurityUserCommand(CreateSecurityUserBody createSecurityUserBody) {
        this.userRefId = createSecurityUserBody.getUserRefId();
        this.password = createSecurityUserBody.getUserPassword();
    }
}
