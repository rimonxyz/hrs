package com.moninfotech.service;

import com.moninfotech.domain.Offer;
import com.moninfotech.domain.Package;

import java.util.List;

public interface OfferService {

    List<Offer> findAll(Integer page, Integer size);

    Offer save(Offer pckg);

    Offer findOne(Long id);

    void delete(Long id);

}
