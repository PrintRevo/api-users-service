package com.printrevo.tech.userservice.entities.core.users.models;

import com.printrevo.tech.commonsecurity.constants.PinStatus;
import com.printrevo.tech.platform.db.Transferable;
import com.printrevo.tech.userservice.entities.core.users.dto.SysUserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "system_users")
public class SysUser extends BaseUserVerificationDetails implements Transferable<SysUserDto> {

    @Column(name = "auth_server_id")
    private String authServerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "pin_status")
    private PinStatus pinStatus = PinStatus.INACTIVE;


    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;

    private String city;

    private String address;

    @Column(name = "postal_code")
    private String postalCode;

    public SysUserDto toDTO() {
        return SysUserDto.copyFrom(this);
    }
}
