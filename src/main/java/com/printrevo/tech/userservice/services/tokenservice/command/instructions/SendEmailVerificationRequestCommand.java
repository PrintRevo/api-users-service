package com.printrevo.tech.userservice.services.tokenservice.command.instructions;


import com.printrevo.tech.platform.services.Command;
import com.printrevo.tech.platform.validators.Validate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Validate
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailVerificationRequestCommand implements Command {

    @Email(message = "Enter valid email address")
    @NotNull(message = "Email address is required")
    @NotEmpty(message = "Email address is required")
    private String emailAddress;

    @NotNull(message = "User ID is required")
    @NotEmpty(message = "User ID is required")
    private String userId;
}
