package com.printrevo.tech.userservice.services.security.command.instructions;

import com.printrevo.tech.commonsecurity.constants.PinIdentifier;
import com.printrevo.tech.commonsecurity.constants.PinStatus;
import com.printrevo.tech.commonsecurity.dto.keycloak.SecurityUser;
import com.printrevo.tech.platform.services.Command;
import com.printrevo.tech.platform.validators.Validate;
import com.printrevo.tech.starter.logbook.annotation.Sensitive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Validate
@NoArgsConstructor
@AllArgsConstructor
public class UpdateKeycloakUserPinCommand implements Command {

    @NotNull(message = "Security user is required")
    private SecurityUser securityUser;

    @Sensitive
    @NotEmpty(message = "Pin is required")
    @Size(min = 6, max = 6, message = "Pin must be 6 digits")
    private String pin;

    @NotNull(message = "Pin identifier is required")
    private PinIdentifier pinIdentifier;

    @NotNull(message = "Pin status is required")
    private PinStatus pinStatus;

    private String email;
    private String phoneNumber;
}
