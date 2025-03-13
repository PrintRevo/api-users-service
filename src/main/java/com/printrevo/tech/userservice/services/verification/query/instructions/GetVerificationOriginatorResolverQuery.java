package com.printrevo.tech.userservice.services.verification.query.instructions;

import com.printrevo.tech.platform.services.Query;
import com.printrevo.tech.userservice.entities.core.verification.constansts.OriginatingSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetVerificationOriginatorResolverQuery implements Query {
    private OriginatingSource originatingSource;
}
