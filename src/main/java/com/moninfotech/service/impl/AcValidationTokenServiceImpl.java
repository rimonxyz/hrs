package com.moninfotech.service.impl;

import com.moninfotech.domain.AcValidationToken;
import com.moninfotech.repository.AcValidationTokenRepository;
import com.moninfotech.service.AcValidationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcValidationTokenServiceImpl implements AcValidationTokenService {
    private final AcValidationTokenRepository tokenRepo;

    @Autowired
    public AcValidationTokenServiceImpl(AcValidationTokenRepository tokenRepo) {
        this.tokenRepo = tokenRepo;
    }

    @Override
    public AcValidationToken save(AcValidationToken acValidationToken) {
        return this.tokenRepo.save(acValidationToken);
    }

    @Override
    public AcValidationToken findOne(Long id) {
        return this.tokenRepo.findOne(id);
    }

    @Override
    public AcValidationToken findByToken(String token) {
        if (token==null) return null;
        return this.tokenRepo.findByToken(token);
    }

    @Override
    public void delete(Long id) {
        this.tokenRepo.delete(id);
    }
}
