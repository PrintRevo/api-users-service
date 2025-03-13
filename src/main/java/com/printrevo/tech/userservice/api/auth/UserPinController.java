package com.printrevo.tech.userservice.api.auth;

import com.printrevo.tech.commonsecurity.dto.AccessToken;
import com.printrevo.tech.platform.api.Response;
import com.printrevo.tech.platform.api.ResponseConverter;
import com.printrevo.tech.userservice.api.auth.body.CreateUserPinBody;
import com.printrevo.tech.userservice.api.auth.body.LoginUserWithPinBody;
import com.printrevo.tech.userservice.api.auth.body.ResetUserPinBody;
import com.printrevo.tech.userservice.services.security.command.flowservices.CreateUserPinFlowService;
import com.printrevo.tech.userservice.services.security.command.flowservices.DeleteUserPinFlowService;
import com.printrevo.tech.userservice.services.security.command.flowservices.LoginUserWithPinFlowService;
import com.printrevo.tech.userservice.services.security.command.flowservices.ResetUserPinFlowService;
import com.printrevo.tech.userservice.services.security.command.flowservices.instructions.CreateUserPinFlowCommand;
import com.printrevo.tech.userservice.services.security.command.flowservices.instructions.DeleteUserPinFlowCommand;
import com.printrevo.tech.userservice.services.security.command.flowservices.instructions.LoginUserWithPinFlowCommand;
import com.printrevo.tech.userservice.services.security.command.flowservices.instructions.ResetUserPinFlowCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "v1/security/users/pin", produces = "application/json")
public record UserPinController(
        LoginUserWithPinFlowService loginUserWithPinFlowService,
        CreateUserPinFlowService createUserPinFlowService,
        DeleteUserPinFlowService deleteUserPinFlowService,
        ResetUserPinFlowService resetUserPinFlowService
) {

    @PostMapping
    @Operation(summary = "Create a security user pin")
    public ResponseEntity<Response<String>> createOrEditUserPin(
            @RequestBody CreateUserPinBody createUserPinBody) {
        return new ResponseConverter().convert(
                createUserPinFlowService.decorate(new CreateUserPinFlowCommand(createUserPinBody))
        );
    }

    @DeleteMapping
    @Operation(summary = "Delete a security user pin")
    public ResponseEntity<Response<String>> deleteUserPin(@RequestParam String userRefId) {
        return new ResponseConverter().convert(
                deleteUserPinFlowService.decorate(new DeleteUserPinFlowCommand(userRefId))
        );
    }

    @PostMapping("login")
    @Operation(summary = "Logs in a user using a pin")
    @ApiResponse(content = {
            @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = AccessToken.class)))
    })
    public ResponseEntity<Response<AccessToken>> loginUserWithPin(
            @RequestBody LoginUserWithPinBody body) {
        return new ResponseConverter().convert(
                loginUserWithPinFlowService.decorate(new LoginUserWithPinFlowCommand(body))
        );
    }

    @PutMapping("reset")
    @Operation(summary = "Reset a user pin")
    public ResponseEntity<Response<String>> resetUserPin(@RequestBody ResetUserPinBody resetUserPinBody) {
        return new ResponseConverter().convert(
                resetUserPinFlowService.decorate(new ResetUserPinFlowCommand(resetUserPinBody))
        );
    }
}
