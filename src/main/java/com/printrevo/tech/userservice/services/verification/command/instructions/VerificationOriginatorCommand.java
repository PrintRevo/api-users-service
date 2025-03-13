package com.printrevo.tech.userservice.services.verification.command.instructions;

import com.printrevo.tech.platform.services.Command;
import com.printrevo.tech.platform.validators.Validate;
import com.printrevo.tech.userservice.entities.core.verification.models.VerificationRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Validate
@NoArgsConstructor
@AllArgsConstructor
public class VerificationOriginatorCommand implements Command {

    @NotNull(message = "Verification request is required")
    private VerificationRequest verificationRequest;
}
