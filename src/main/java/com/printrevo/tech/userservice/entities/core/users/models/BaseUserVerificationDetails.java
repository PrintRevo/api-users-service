package com.printrevo.tech.userservice.entities.core.users.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseUserVerificationDetails extends BaseUserDetails {

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "phone_number_verified")
    private Boolean phoneNumberVerified = false;

    @Column(name = "phone_number_verified_at")
    private LocalDateTime phoneNumberVerifiedAt;


    @Column(name = "email")
    private String email;

    @Column(name = "email_verified")
    private Boolean emailVerified = false;

    @Column(name = "email_verified_at")
    private LocalDateTime emailVerifiedAt;

    protected static void copyFrom(BaseUserVerificationDetails from, BaseUserVerificationDetails to) {

        to.setPhoneNumber(from.getPhoneNumber());
        to.setPhoneNumberVerified(from.getPhoneNumberVerified());
        to.setPhoneNumberVerifiedAt(from.getPhoneNumberVerifiedAt());

        to.setEmail(from.getEmail());
        to.setEmailVerified(from.getEmailVerified());
        to.setEmailVerifiedAt(from.getEmailVerifiedAt());

        BaseUserDetails.copyFrom(from, to);
    }
}
