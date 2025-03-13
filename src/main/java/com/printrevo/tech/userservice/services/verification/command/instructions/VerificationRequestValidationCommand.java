package com.printrevo.tech.userservice.services.verification.command.instructions;

import com.printrevo.tech.platform.services.Command;
import com.printrevo.tech.platform.validators.Validate;
import com.printrevo.tech.userservice.api.verification.body.VerificationValidationBody;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Validate
@NoArgsConstructor
@AllArgsConstructor
public class VerificationRequestValidationCommand implements Command {

    @NotBlank(message = "Verification request reference id is required")
    @NotEmpty(message = "Verification request reference id is required")
    private String verificationRequestRefId;

    @NotBlank(message = "otp is required")
    @NotEmpty(message = "otp is required")
    private String otp;

    public VerificationRequestValidationCommand(VerificationValidationBody verificationValidationBody) {
        setVerificationRequestRefId(verificationValidationBody.getVerificationRequestRefId());
        setOtp(verificationValidationBody.getOtp());
    }
}
