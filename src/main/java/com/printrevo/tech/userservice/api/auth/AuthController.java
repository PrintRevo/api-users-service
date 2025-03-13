package com.printrevo.tech.userservice.api.auth;

import com.printrevo.tech.commonsecurity.dto.AccessToken;
import com.printrevo.tech.platform.api.Response;
import com.printrevo.tech.platform.api.ResponseConverter;
import com.printrevo.tech.userservice.api.auth.body.LogoutUserReqDto;
import com.printrevo.tech.userservice.api.auth.body.RefreshTokenReqBody;
import com.printrevo.tech.userservice.services.security.command.LogoutUserService;
import com.printrevo.tech.userservice.services.security.command.RefreshJwtTokenService;
import com.printrevo.tech.userservice.services.security.command.instructions.LogoutUserCommand;
import com.printrevo.tech.userservice.services.security.command.instructions.RefreshJwtTokenCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "v1/security", produces = "application/json")
public record AuthController(
        RefreshJwtTokenService refreshJwtTokenService,
        LogoutUserService logoutUserService) {

    @PostMapping("logout")
    @Operation(summary = "Terminate a user active session. This will both invalidate the jwt token and the refresh token")
    public ResponseEntity<Response<Void>> logout(
            @RequestBody LogoutUserReqDto logoutUserDto,
            @RequestParam(defaultValue = "false", required = false) String publicClient) {
        return new ResponseConverter().convert(
                logoutUserService.decorate(new LogoutUserCommand(
                        logoutUserDto.getRefreshToken(), Boolean.valueOf(publicClient)))
        );
    }

    @PostMapping("refresh")
    @Operation(summary = "Refreshes a user session")
    @ApiResponse(content = {
            @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = AccessToken.class)))
    })
    public ResponseEntity<Response<AccessToken>> refresh(
            @RequestBody RefreshTokenReqBody refreshTokenReqBody,
            @RequestParam(defaultValue = "false", required = false) String publicClient) {
        return new ResponseConverter().convert(
                refreshJwtTokenService.decorate(
                        new RefreshJwtTokenCommand(refreshTokenReqBody, Boolean.valueOf(publicClient)))
        );
    }
}
