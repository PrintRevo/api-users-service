package com.printrevo.tech.userservice.services.security.command.instructions;

import com.printrevo.tech.platform.services.Command;
import com.printrevo.tech.platform.validators.Validate;
import com.printrevo.tech.userservice.api.auth.body.RefreshTokenReqBody;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Validate
@NoArgsConstructor
@AllArgsConstructor
public class RefreshJwtTokenCommand implements Command {

    @NotNull(message = "Refresh token is required")
    @NotEmpty(message = "Refresh token is required")
    private String refreshToken;

    private Boolean publicClient;

    public RefreshJwtTokenCommand(RefreshTokenReqBody refreshTokenReqBody, Boolean publicClient) {
        this.refreshToken = refreshTokenReqBody.getRefreshToken();
        this.publicClient = publicClient;
    }
}
