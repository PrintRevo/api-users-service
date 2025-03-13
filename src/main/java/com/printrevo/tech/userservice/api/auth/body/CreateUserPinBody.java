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
public class CreateUserPinBody {

    @Schema(required = true, description = "The user reference id")
    private String userRefId;

    @Sensitive
    @Schema(required = true, description = "The user pin")
    private String pin;

    @Schema(required = true, description = "The user pin identifier")
    private PinIdentifier pinIdentifier;
}
