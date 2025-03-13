package com.printrevo.tech.userservice.entities.core.verification.models;

import com.printrevo.tech.platform.db.BaseAuditable;
import com.printrevo.tech.userservice.entities.core.dto.DataEntry;
import com.printrevo.tech.userservice.entities.core.users.models.SysUser;
import com.printrevo.tech.userservice.entities.core.verification.constansts.OriginatingSource;
import com.printrevo.tech.userservice.entities.core.verification.constansts.VerificationType;
import com.printrevo.tech.userservice.platform.db.PgJsonbToMapConverter;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "verification_requests")
public class VerificationRequest extends BaseAuditable {

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_type")
    private VerificationType verificationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "originating_source")
    private OriginatingSource originatingSource;

    @Column(name = "identity_value")
    private String identityValue;

    @Column(name = "correlation_id")
    private String correlationId;

    @Column(name = "verified")
    private Boolean verified = Boolean.FALSE;

    @Column(name = "verified_at", columnDefinition = "timestamp with time zone")
    private LocalDateTime verifiedAt;

    @Column(name = "expires_at", columnDefinition = "timestamp with time zone")
    private LocalDateTime expiresAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private SysUser user;

    @JsonValue
    @Column(name = "details", columnDefinition = "TEXT")
    @Convert(converter = PgJsonbToMapConverter.class)
    private Map<String, DataEntry> details = new HashMap<>();
}
