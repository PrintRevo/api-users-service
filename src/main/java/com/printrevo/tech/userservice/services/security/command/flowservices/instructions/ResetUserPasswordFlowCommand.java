package com.printrevo.tech.userservice.services.security.command.flowservices.instructions;

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
public class ResetUserPasswordFlowCommand implements Command {

    @NotBlank(message = "Verification id is required")
    @NotEmpty(message = "Verification id is required")
    private String verificationId;

    @Sensitive
    @NotBlank(message = "User password is required")
    @NotEmpty(message = "User password is required")
    private String password;
}
