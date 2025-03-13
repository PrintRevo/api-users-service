package com.printrevo.tech.userservice.api.auth;

import com.printrevo.tech.commonsecurity.dto.AccessToken;
import com.printrevo.tech.platform.api.Response;
import com.printrevo.tech.platform.api.ResponseConverter;
import com.printrevo.tech.userservice.api.auth.body.*;
import com.printrevo.tech.userservice.services.security.command.flowservices.*;
import com.printrevo.tech.userservice.services.security.command.flowservices.instructions.InitiateResetUserPasswordFlowCommand;
import com.printrevo.tech.userservice.services.security.command.flowservices.instructions.LoginUserFlowCommand;
import com.printrevo.tech.userservice.services.security.command.flowservices.instructions.ResetUserPasswordFlowCommand;
import com.printrevo.tech.userservice.services.security.command.flowservices.instructions.UpdateUserPasswordFlowCommand;
import com.printrevo.tech.userservice.services.security.command.instructions.CreateSecurityUserCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "v1/security", produces = "application/json")
public record UserPasswordController(
        InitiateResetUserPasswordFlowService initiateResetUserPasswordFlowService,
        UpdateUserPasswordFlowService updateUserPasswordFlowService,
        ResetUserPasswordFlowService resetUserPasswordFlowService,
        CreateSecurityUserFlowService createSecurityUserFlowService,
        LoginUserFlowService loginFlowService
) {

    @PostMapping("users")
    @Operation(summary = "Creates a security user who can login to the system")
    public ResponseEntity<Response<String>> createUser(
            @RequestBody CreateSecurityUserBody createSecurityUserBody) {
        return new ResponseConverter().convert(
                createSecurityUserFlowService.decorate(
                        new CreateSecurityUserCommand(createSecurityUserBody))
        );
    }

    @PostMapping("users/login")
    @Operation(summary = "Logs in a user with password")
    @ApiResponse(content = {
            @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = AccessToken.class)))
    })
    public ResponseEntity<Response<AccessToken>> loginUser(@RequestBody LoginUserBody body) {
        return new ResponseConverter().convert(
                loginFlowService.decorate(new LoginUserFlowCommand(body)));
    }

    @PutMapping("users/password")
    @Operation(summary = "Change a user password")
    public ResponseEntity<Response<String>> updateUserPassword(
            @RequestBody ChangeUserPasswordReqDto changeUserPasswordReqDto) {
        return new ResponseConverter().convert(
                updateUserPasswordFlowService.decorate(
                        new UpdateUserPasswordFlowCommand(changeUserPasswordReqDto))
        );
    }

    @PostMapping("users/password/reset")
    @Operation(summary = "Request reset password OTP")
    public ResponseEntity<Response<String>> resetUserPassword(
            @RequestBody ResetUserPasswordReqDto resetUserPasswordReqDto) {
        return new ResponseConverter().convert(
                initiateResetUserPasswordFlowService.decorate(
                        new InitiateResetUserPasswordFlowCommand(resetUserPasswordReqDto))
        );
    }


    @PutMapping("users/password/reset")
    @Operation(summary = "Set user password after OTP verification")
    public ResponseEntity<Response<String>> resetUserPasswordAfterVerification(
            @RequestParam String verificationId, @RequestBody ResetPasswordBody resetPasswordBody) {
        return new ResponseConverter().convert(
                resetUserPasswordFlowService.decorate(new ResetUserPasswordFlowCommand(
                        verificationId, resetPasswordBody.getPassword()))
        );
    }
}
