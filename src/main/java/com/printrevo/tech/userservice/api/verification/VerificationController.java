package com.printrevo.tech.userservice.api.verification;

import com.printrevo.tech.platform.api.Response;
import com.printrevo.tech.platform.api.ResponseConverter;
import com.printrevo.tech.userservice.api.verification.body.ApiVerifyUserBody;
import com.printrevo.tech.userservice.api.verification.body.VerificationValidationBody;
import com.printrevo.tech.userservice.services.verification.command.flowservices.VerificationRequestFlowService;
import com.printrevo.tech.userservice.services.verification.command.flowservices.VerificationRequestValidationFlowService;
import com.printrevo.tech.userservice.services.verification.command.flowservices.instructions.VerificationRequestFlowCommand;
import com.printrevo.tech.userservice.services.verification.command.instructions.VerificationRequestValidationCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "v1/verification", produces = "application/json")
public record VerificationController(
        VerificationRequestValidationFlowService verificationRequestValidationFlowService,
        VerificationRequestFlowService verificationRequestFlowService
) {

    @PostMapping
    @Operation(summary = "Initiate a verification request")
    @ApiResponse(content = {
            @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<Response<String>> initiateVerificationRequest(
            @RequestBody ApiVerifyUserBody verifyUserDto) {
        return new ResponseConverter().convert(
                verificationRequestFlowService.decorate(new VerificationRequestFlowCommand(verifyUserDto))
        );
    }

    @PutMapping
    @Operation(summary = "Verify otp sent to a user's email")
    @ApiResponse(content = {
            @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<Response<String>> verifyEmailOtp(
            @RequestBody VerificationValidationBody verificationValidation) {
        return new ResponseConverter().convert(
                verificationRequestValidationFlowService.decorate(
                        new VerificationRequestValidationCommand(verificationValidation))
        );
    }
}
