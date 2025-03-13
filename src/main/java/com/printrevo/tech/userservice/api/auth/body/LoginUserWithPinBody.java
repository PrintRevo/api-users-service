package com.printrevo.tech.userservice.api.auth.body;

import com.printrevo.tech.commonsecurity.constants.PinIdentifier;
import com.printrevo.tech.starter.logbook.annotation.Sensitive;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserWithPinBody {

    @Schema(required = true, description = "user login identifier")
    private String identifier;

    @Schema(required = true, description = "user pin identifier type")
    private PinIdentifier identifierType;

    @Sensitive
    @Schema(required = true, description = "user pin")
    private String pin;
}
