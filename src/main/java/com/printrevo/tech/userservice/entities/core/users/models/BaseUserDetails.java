package com.printrevo.tech.userservice.entities.core.users.models;

import com.printrevo.tech.common.domain.CountryCode;
import com.printrevo.tech.platform.db.BaseAuditable;
import com.printrevo.tech.userservice.entities.core.users.constants.UserStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseUserDetails extends BaseAuditable {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "country")
    @Enumerated(EnumType.STRING)
    private CountryCode country;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "user_status")
    private UserStatus userStatus;

    protected static void copyFrom(BaseUserDetails from, BaseUserVerificationDetails to) {

        to.setFirstName(from.getFirstName());
        to.setMiddleName(from.getMiddleName());
        to.setLastName(from.getLastName());
        to.setCountry(from.getCountry());
        to.setUserStatus(from.getUserStatus());

        BaseAuditable.copyFromAuditable(to, from);
    }
}
