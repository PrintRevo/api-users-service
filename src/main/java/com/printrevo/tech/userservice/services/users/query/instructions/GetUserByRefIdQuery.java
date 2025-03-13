package com.printrevo.tech.userservice.services.users.query.instructions;

import com.printrevo.tech.platform.services.Query;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserByRefIdQuery implements Query {
    @NotEmpty(message = "User reference id is required")
    @NotNull(message = "User reference id can not be null")
    private String userRefId;
}
