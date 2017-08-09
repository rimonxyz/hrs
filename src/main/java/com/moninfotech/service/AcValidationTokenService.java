package com.moninfotech.service;

import com.moninfotech.domain.AcValidationToken;

public interface AcValidationTokenService {
    AcValidationToken save(AcValidationToken acValidationToken);
    AcValidationToken findOne(Long id);
    AcValidationToken findByToken(String token);
    void delete(Long id);
}
