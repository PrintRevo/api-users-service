package com.printrevo.tech.userservice.services.security.query.instructions;

import com.printrevo.tech.platform.services.Query;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindKeycloakUsersQuery implements Query {
    Map<String, Object> queryFields = new HashMap<>();
}
