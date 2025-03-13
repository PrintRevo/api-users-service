package com.printrevo.tech.userservice.entities.core.verification.repositories;

import com.printrevo.tech.userservice.entities.core.verification.models.PhoneNumberVerificationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneNumberVerificationRequestRepository
        extends JpaRepository<PhoneNumberVerificationRequest, String> {
}
