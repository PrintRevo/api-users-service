package com.printrevo.tech.userservice.services.users.query.instructions;

import com.printrevo.tech.platform.services.Query;
import com.printrevo.tech.platform.validators.Validate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
@Validate
@NoArgsConstructor
@AllArgsConstructor
public class GetUserByAuthenticationServerIdQuery implements Query {
    @NotNull(message = "Authentication server id is required")
    @NotEmpty(message = "Authentication server id is required")
    private String authenticationServerId;
}
