package com.printrevo.tech.userservice.services.verification.query.instructions;

import com.printrevo.tech.platform.services.Query;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetVerificationRequestByIdQuery implements Query {
    private String refId;
}
