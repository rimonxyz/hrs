package com.moninfotech.service;

import com.moninfotech.domain.Subscriber;
import org.springframework.data.domain.Page;

public interface SubscriberService {

    Page<Subscriber> findAll(int page);

}
