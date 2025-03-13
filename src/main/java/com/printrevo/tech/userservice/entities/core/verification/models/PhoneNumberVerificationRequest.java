package com.printrevo.tech.userservice.entities.core.verification.models;

import com.printrevo.tech.platform.db.BaseAuditable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "phone_number_verification_requests")
public class PhoneNumberVerificationRequest extends BaseAuditable {

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "time_to_live")
    private Integer timeToLive;

    @Column(name = "otp")
    private String otp;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @Column(name = "sent_date")
    private LocalDate sentDate;
}
