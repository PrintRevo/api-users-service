package com.printrevo.tech.userservice.entities.core.verification.models;


import com.printrevo.tech.platform.db.BaseAuditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "email_verification_requests")
public class EmailVerificationRequest extends BaseAuditable {

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "time_to_live")
    private Integer timeToLive;

    @Column(name = "token_service_id")
    private String tokenServiceId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "sent_date")
    private LocalDate sentDate;
}
