package com.printrevo.tech.userservice.api.users.body;

import com.printrevo.tech.common.domain.CountryCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Create user with basic details")
public class ApiCreateUser {
    @Schema(required = true, description = "user email")
    private String email;
    @Schema(required = true, description = "user country")
    private CountryCode country;
}
