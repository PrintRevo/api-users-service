package com.printrevo.tech.userservice.services.verification.command.flowservices.instructions;

import com.printrevo.tech.platform.services.Command;
import com.printrevo.tech.platform.validators.Validate;
import com.printrevo.tech.userservice.api.verification.body.ApiVerifyUserBody;
import com.printrevo.tech.userservice.entities.core.dto.DataEntry;
import com.printrevo.tech.userservice.entities.core.verification.constansts.OriginatingSource;
import com.printrevo.tech.userservice.entities.core.verification.constansts.VerificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Data
@Validate
@NoArgsConstructor
@AllArgsConstructor
public class VerificationRequestFlowCommand implements Command {

    @NotNull(message = "Originating source is required")
    private OriginatingSource originatingSource;

    @NotNull(message = "Verification type is required")
    private VerificationType verificationType;

    @NotBlank(message = "User reference id is required")
    @NotEmpty(message = "User reference id is required")
    private String userRefId;

    private Map<String, DataEntry> details = new HashMap<>();

    public VerificationRequestFlowCommand(ApiVerifyUserBody verifyUserBody) {
        setOriginatingSource(verifyUserBody.getOriginatingSource());
        setVerificationType(verifyUserBody.getVerificationType());
        setUserRefId(verifyUserBody.getUserRefId());
    }

    public VerificationRequestFlowCommand(OriginatingSource originatingSource
            , VerificationType verificationType, String userRefId) {
        setOriginatingSource(originatingSource);
        setVerificationType(verificationType);
        setUserRefId(userRefId);
    }
}
