package com.moninfotech.service.impl;

import com.moninfotech.commons.SortAttributes;
import com.moninfotech.domain.PaymentInfo;
import com.moninfotech.repository.PaymentInfoRepository;
import com.moninfotech.service.PaymentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PaymentInfoServiceImpl implements PaymentInfoService {

    private final PaymentInfoRepository paymentInfoRepo;

    @Autowired
    public PaymentInfoServiceImpl(PaymentInfoRepository paymentInfoRepo) {
        this.paymentInfoRepo = paymentInfoRepo;
    }

    @Override
    @Transactional
    public PaymentInfo save(PaymentInfo paymentInfo) {
        PaymentInfo ex = this.paymentInfoRepo.findByTransaction(paymentInfo.getTransaction());
        if (ex != null)
            return ex;
        return this.paymentInfoRepo.save(paymentInfo);
    }

    @Override
    public PaymentInfo findOne(Long id) {
        return this.paymentInfoRepo.findOne(id);
    }

    @Override
    public List<PaymentInfo> findAll(int page, int size) {
        return this.paymentInfoRepo.findAll(new PageRequest(page, size, Sort.Direction.DESC, SortAttributes.FIELD_ID)).getContent();
    }
}
