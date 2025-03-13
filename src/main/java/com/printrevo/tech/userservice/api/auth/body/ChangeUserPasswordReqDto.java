package com.printrevo.tech.userservice.api.auth.body;

import com.printrevo.tech.starter.logbook.annotation.Sensitive;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeUserPasswordReqDto {
    @Schema(description = "The user reference id")
    private String userRefId;

    @Sensitive
    @Schema(description = "The new password")
    private String password;
}
