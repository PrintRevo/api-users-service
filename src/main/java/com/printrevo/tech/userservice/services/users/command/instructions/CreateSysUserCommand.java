package com.printrevo.tech.userservice.services.users.command.instructions;

import com.printrevo.tech.common.domain.CountryCode;
import com.printrevo.tech.platform.services.Command;
import com.printrevo.tech.platform.validators.Validate;
import com.printrevo.tech.userservice.api.users.body.ApiCreateUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Validate
@NoArgsConstructor
@AllArgsConstructor
public class CreateSysUserCommand implements Command {

    @NotEmpty(message = "Email is required")
    @Email(regexp = ".+[@].+[\\.].+", message = "Email is invalid")
    private String email;

    @NotNull(message = "Country is required")
    private CountryCode country;

    public CreateSysUserCommand(ApiCreateUser apiCreateUser) {
        setEmail(apiCreateUser.getEmail());
        setCountry(apiCreateUser.getCountry());
    }
}
