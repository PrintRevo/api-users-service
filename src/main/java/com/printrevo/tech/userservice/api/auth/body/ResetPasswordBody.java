package com.printrevo.tech.userservice.api.auth.body;

import com.printrevo.tech.starter.logbook.annotation.Sensitive;
import lombok.Data;

@Data
public class ResetPasswordBody {

    @Sensitive
    private String password;
}
