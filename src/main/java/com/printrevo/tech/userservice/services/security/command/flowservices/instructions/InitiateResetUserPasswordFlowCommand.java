package com.printrevo.tech.userservice.services.security.command.flowservices.instructions;

import com.printrevo.tech.common.domain.CountryCode;
import com.printrevo.tech.platform.services.Command;
import com.printrevo.tech.platform.validators.Validate;
import com.printrevo.tech.userservice.api.auth.body.ResetUserPasswordReqDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
@Validate
@NoArgsConstructor
@AllArgsConstructor
public class InitiateResetUserPasswordFlowCommand implements Command {

    @NotBlank(message = "Email is required")
    @NotEmpty(message = "Email is required")
    @Email(regexp = ".+[@].+[\\.].+", message = "Email is invalid")
    private String email;

    @NotNull(message = "Country is required")
    private CountryCode country;

    public InitiateResetUserPasswordFlowCommand(ResetUserPasswordReqDto resetUserPasswordReqDto) {
        this.email = resetUserPasswordReqDto.getEmail();
        this.country = resetUserPasswordReqDto.getCountry();
    }
}
