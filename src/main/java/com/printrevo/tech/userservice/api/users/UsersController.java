package com.printrevo.tech.userservice.api.users;

import com.printrevo.tech.platform.api.Response;
import com.printrevo.tech.platform.api.ResponseConverter;
import com.printrevo.tech.platform.db.TransactionFilter;
import com.printrevo.tech.userservice.api.users.body.ApiCreateUser;
import com.printrevo.tech.userservice.api.users.body.EditBaseUserDetailsBody;
import com.printrevo.tech.userservice.entities.core.users.dto.SysUserDto;
import com.printrevo.tech.userservice.services.users.command.CreateSysUserService;
import com.printrevo.tech.userservice.services.users.command.flowservices.CurrentUserResolverFlowService;
import com.printrevo.tech.userservice.services.users.command.flowservices.UpdatePersonalUserDetailsFlowService;
import com.printrevo.tech.userservice.services.users.command.flowservices.instructions.CurrentUserResolverCommand;
import com.printrevo.tech.userservice.services.users.command.flowservices.instructions.UpdateUserFlowCommand;
import com.printrevo.tech.userservice.services.users.command.instructions.CreateSysUserCommand;
import com.printrevo.tech.userservice.services.users.query.GetUserPageByIdentifierQueryService;
import com.printrevo.tech.userservice.services.users.query.instructions.GetUserByIdentifierQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "v1/users", produces = "application/json")
public record UsersController(
        UpdatePersonalUserDetailsFlowService updatePersonalUserDetailsFlowService,
        GetUserPageByIdentifierQueryService getUserPageByIdentifierQueryService,
        CurrentUserResolverFlowService currentUserResolverFlowService,
        CreateSysUserService createSysUserService
) {

    @PostMapping
    @Operation(summary = "Create a new user wih status on-boarding, always returns 201")
    @ApiResponse(content = {
            @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<Response<String>> createUser(@RequestBody ApiCreateUser createUserDto) {
        return new ResponseConverter().convert(
                createSysUserService.decorate(new CreateSysUserCommand(createUserDto))
        );
    }

    @PutMapping("{userRefId}")
    @Operation(summary = "Update basic user details. Note! cannot update user unique identifiers")
    @ApiResponse(content = {
            @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SysUserDto.class)))
    })
    public ResponseEntity<Response<SysUserDto>> editBaseUserDetails(@PathVariable String userRefId
            , @RequestBody EditBaseUserDetailsBody editUserBody) {
        return new ResponseConverter().convert(
                updatePersonalUserDetailsFlowService.decorate(new UpdateUserFlowCommand(userRefId, editUserBody))
        );
    }

    @GetMapping
    @Operation(summary = "Query users by identifier, optional user field names to search from eg username")
    @ApiResponse(content = {
            @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Page.class)))
    })
    @PreAuthorize("hasAuthority('QUERY_ALL_USERS')")
    public ResponseEntity<Response<Page<SysUserDto>>> queryUsers(
            @RequestParam Map<String, String> fieldSearchQuery) {
        var params = new HashMap<String, Object>(fieldSearchQuery);
        return new ResponseConverter().convert(
                getUserPageByIdentifierQueryService.decorate(
                        new GetUserByIdentifierQuery(new TransactionFilter(params)))
        );
    }

    @GetMapping("current-user")
    @Operation(summary = "Get current user details from an authenticated request")
    @ApiResponse(content = {
            @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SysUserDto.class)))
    })
    public ResponseEntity<Response<SysUserDto>> getCurrentUser() {
        return new ResponseConverter().convert(
                currentUserResolverFlowService.decorate(new CurrentUserResolverCommand())
        );
    }
}
