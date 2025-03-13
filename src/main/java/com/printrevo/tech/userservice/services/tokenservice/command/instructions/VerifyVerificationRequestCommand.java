package com.printrevo.tech.userservice.services.tokenservice.command.instructions;

import com.printrevo.tech.platform.services.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyVerificationRequestCommand implements Command {
    private String otp;
    private String userId;
}
