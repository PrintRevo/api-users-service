package com.printrevo.tech.userservice.data.dto.tokenservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationValidationRequest {

    @JsonProperty("otp_code")
    private String otpCode;

    @JsonProperty("user_id")
    private String userId;
}
