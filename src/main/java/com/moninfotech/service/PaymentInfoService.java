package com.moninfotech.service;

import com.moninfotech.domain.PaymentInfo;

import java.util.List;

public interface PaymentInfoService {
    PaymentInfo save(PaymentInfo paymentInfo);

    PaymentInfo findOne(Long id);

    List<PaymentInfo> findAll(int page, int size);
}
