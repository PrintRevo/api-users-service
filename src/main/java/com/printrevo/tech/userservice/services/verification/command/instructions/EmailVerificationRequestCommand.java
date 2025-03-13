package com.printrevo.tech.userservice.services.verification.command.instructions;

import com.printrevo.tech.platform.services.Command;
import com.printrevo.tech.platform.validators.Validate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Validate
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationRequestCommand implements Command {

    @NotEmpty(message = "Email address is required")
    private String emailAddress;

    @NotEmpty(message = "User ID is required")
    private String userId;
}
