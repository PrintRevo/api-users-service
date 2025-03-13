package com.printrevo.tech.userservice.services.security.query.organizations.instructions;

import com.printrevo.tech.platform.services.Query;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetOrganizationsKeycloakQuery implements Query {
    private String searchQuery;
}
