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
public class GetOrganizationMemberQuery implements Query {
    @NotNull(message = "organizationNumber is required")
    @NotEmpty(message = "organizationNumber is required")
    private String organizationId;

    @NotNull(message = "userAuthServerId is required")
    @NotEmpty(message = "userAuthServerId is required")
    private String userAuthServerId;
}
