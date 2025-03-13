package com.printrevo.tech.userservice.entities.core.users.dto;

import com.printrevo.tech.commonsecurity.constants.PinStatus;
import com.printrevo.tech.userservice.entities.core.users.models.BaseUserVerificationDetails;
import com.printrevo.tech.userservice.entities.core.users.models.SysUser;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserDto extends BaseUserVerificationDetails {

    private String authServerId;

    private PinStatus pinStatus;

    private LocalDateTime dateOfBirth;

    private String city;
    private String address;
    private String postalCode;

    public SysUserDto(String id) {
        setId(id);
    }

    public static SysUserDto copyFrom(SysUser sysUser) {

        var dto = new SysUserDto();

        dto.setAuthServerId(sysUser.getAuthServerId());
        dto.setPinStatus(sysUser.getPinStatus());

        dto.setDateOfBirth(sysUser.getDateOfBirth());

        dto.setCity(sysUser.getCity());
        dto.setAddress(sysUser.getAddress());
        dto.setPostalCode(sysUser.getPostalCode());

        BaseUserVerificationDetails.copyFrom(sysUser, dto);

        return dto;
    }
}
