package com.printrevo.tech.userservice.api.auth.body;

import com.printrevo.tech.starter.logbook.annotation.Sensitive;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login user with email and password")
public class LoginUserBody {
    @Schema(required = true, description = "user email")
    private String email;

    @Sensitive
    @Schema(required = true, description = "user password")
    private String password;
}
