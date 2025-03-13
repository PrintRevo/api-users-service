package com.printrevo.tech.userservice.api.auth.body;

import com.printrevo.tech.common.domain.CountryCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetUserPasswordReqDto {
    private CountryCode country;
    private String email;
}
