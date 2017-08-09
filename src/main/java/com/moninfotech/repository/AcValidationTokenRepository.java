package com.moninfotech.repository;

import com.moninfotech.domain.AcValidationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcValidationTokenRepository extends JpaRepository<AcValidationToken, Long> {
    AcValidationToken findByToken(String token);
}
