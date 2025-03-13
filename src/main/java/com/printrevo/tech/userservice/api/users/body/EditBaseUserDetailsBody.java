package com.printrevo.tech.userservice.api.users.body;

import com.printrevo.tech.common.domain.CountryCode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Edit user details")
public class EditBaseUserDetailsBody {
    @Schema(description = "user first name")
    private String firstName;
    @Schema(description = "user middle name")
    private String middleName;
    @Schema(description = "user last name")
    private String lastName;
    @Schema(description = "user country")
    private CountryCode countryCode;
    private String dateOfBirth;
    private String city;
    private String address;
    private String postalCode;
}
