package com.printrevo.tech.userservice.api.auth.body;

import com.printrevo.tech.starter.logbook.annotation.Sensitive;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Create a security user with password")
public class CreateSecurityUserBody {
    @Schema(required = true, description = "The user reference id")
    private String userRefId;

    @Sensitive
    @Schema(required = true, description = "The user password")
    private String userPassword;
}
