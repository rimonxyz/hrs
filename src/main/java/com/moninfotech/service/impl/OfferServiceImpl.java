package com.moninfotech.service.impl;

import com.moninfotech.commons.SortAttributes;
import com.moninfotech.domain.Offer;
import com.moninfotech.domain.Package;
import com.moninfotech.repository.OfferRepository;
import com.moninfotech.repository.PackageRepository;
import com.moninfotech.service.OfferService;
import com.moninfotech.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepo;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository) {
        this.offerRepo = offerRepository;
    }

    @Override
    public List<Offer> findAll(Integer page, Integer size) {
        if (page == null || size == null)
            return this.offerRepo.findAll();
        return this.offerRepo.findAll(new PageRequest(page, size, Sort.Direction.DESC, SortAttributes.FIELD_ID)).getContent();
    }

    @Override
    public Offer save(Offer offer) {
        return this.offerRepo.save(offer);
    }

    @Override
    public Offer findOne(Long id) {
        return this.offerRepo.findOne(id);
    }

    @Override
    public void delete(Long id) {
        this.offerRepo.delete(id);
    }
}
