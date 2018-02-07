package com.moninfotech.service.impl;

import com.moninfotech.commons.SortAttributes;
import com.moninfotech.domain.Subscriber;
import com.moninfotech.repository.SubscriberRepository;
import com.moninfotech.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SubscriberServiceImpl implements SubscriberService {

    private final SubscriberRepository subscriberRepo;

    @Autowired
    public SubscriberServiceImpl(SubscriberRepository subscriberRepo) {
        this.subscriberRepo = subscriberRepo;
    }

    @Override
    public Page<Subscriber> findAll(int page) {
        return this.subscriberRepo.findAll(new PageRequest(page, SortAttributes.Page.SIZE, Sort.Direction.DESC, SortAttributes.FIELD_ID));
    }
}
