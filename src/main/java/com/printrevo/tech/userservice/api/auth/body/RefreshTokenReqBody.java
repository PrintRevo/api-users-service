package com.printrevo.tech.userservice.api.auth.body;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenReqBody {
    @Schema(description = "The refresh token")
    private String refreshToken;
}
