package com.printrevo.tech.userservice.services.verification.command.instructions;

import com.printrevo.tech.platform.services.Command;
import com.printrevo.tech.userservice.entities.core.verification.models.VerificationRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationRequestValidationCommand implements Command {
    private VerificationRequest verificationRequest;
    private String otp;
}
