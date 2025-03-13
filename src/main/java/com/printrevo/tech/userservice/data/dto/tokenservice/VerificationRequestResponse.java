package com.printrevo.tech.userservice.data.dto.tokenservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VerificationRequestResponse {
    @JsonProperty("id")
    private int id;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("otp")
    private String otp;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("status")
    private String status;

    @JsonProperty("validity")
    private String validity;

    @JsonProperty("created_at")
    private String createdAt;
}
