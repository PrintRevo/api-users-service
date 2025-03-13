package com.printrevo.tech.userservice.data.dto.tokenservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationRequest {

    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String name = "there";

    @JsonProperty("service_name")
    private String serviceName = "Da-Remit";

    @JsonProperty("user_id")
    private String userId;
}
