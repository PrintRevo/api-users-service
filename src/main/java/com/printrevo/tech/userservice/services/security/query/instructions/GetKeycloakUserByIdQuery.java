package com.printrevo.tech.userservice.services.security.query.instructions;

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
public class GetKeycloakUserByIdQuery implements Query {

    @NotNull(message = "Auth id is required")
    @NotEmpty(message = "Auth id is required")
    private String authServerId;

}
