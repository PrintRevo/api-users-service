package com.printrevo.tech.userservice.services.users.command.flowservices.instructions;

import com.printrevo.tech.common.domain.CountryCode;
import com.printrevo.tech.commonsecurity.constants.PinStatus;
import com.printrevo.tech.platform.services.Command;
import com.printrevo.tech.platform.validators.Validate;
import com.printrevo.tech.platform.validators.ValidateAuthorization;
import com.printrevo.tech.userservice.api.users.body.EditBaseUserDetailsBody;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.util.Objects.nonNull;

@Data
@Validate
@NoArgsConstructor
@AllArgsConstructor
@ValidateAuthorization(
        permissions = {"UPDATE_USER_BASIC"},
        fallBackExpressions = {"@usersUtils.isSecurityContextUser(userRefId)"})
public class UpdateUserFlowCommand implements Command {

    @NotBlank(message = "User ref id is required")
    @NotEmpty(message = "User ref id is required")
    private String userRefId;

    private String authServerId;

    private String firstName;
    private String middleName;
    private String lastName;

    private CountryCode country;

    private String phoneNumber;
    private Boolean phoneNumberVerified;

    private String email;
    private Boolean emailVerified;

    private Boolean enabled;

    private PinStatus pinStatus;

    private LocalDateTime dateOfBirth;
    private String city;
    private String address;
    private String postalCode;

    public UpdateUserFlowCommand(String userRefId, EditBaseUserDetailsBody editUserBody) {

        setUserRefId(userRefId);
        setFirstName(editUserBody.getFirstName());
        setMiddleName(editUserBody.getMiddleName());
        setLastName(editUserBody.getLastName());
        setCountry(editUserBody.getCountryCode());
        if (nonNull(editUserBody.getDateOfBirth())) {
            setDateOfBirth(LocalDate.parse(editUserBody.getDateOfBirth()).atStartOfDay());
        }

        setCity(editUserBody.getCity());
        setAddress(editUserBody.getAddress());
        setPostalCode(editUserBody.getPostalCode());
    }
}
