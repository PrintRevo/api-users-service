package com.printrevo.tech.userservice.api.verification.body;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationValidationBody {
    @Schema(required = true, description = "The verification request reference id")
    private String verificationRequestRefId;
    @Schema(required = true, description = "The verification request received otp")
    private String otp;
}
