package com.printrevo.tech.userservice.api.verification.body;

import com.printrevo.tech.userservice.entities.core.verification.constansts.OriginatingSource;
import com.printrevo.tech.userservice.entities.core.verification.constansts.VerificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Initiate a verification request")
public class ApiVerifyUserBody {

    @Schema(required = true, description = "The logic to execute after verification has been complete")
    private OriginatingSource originatingSource;

    @Schema(required = true, description = "The type of verification to perform")
    private VerificationType verificationType;

    @Schema(required = true, description = "The user reference id")
    private String userRefId;
}
