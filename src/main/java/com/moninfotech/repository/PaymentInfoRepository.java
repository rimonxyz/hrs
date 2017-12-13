package com.moninfotech.repository;

import com.moninfotech.domain.PaymentInfo;
import com.moninfotech.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentInfoRepository extends JpaRepository<PaymentInfo,Long>{
    PaymentInfo findByTransaction(Transaction transaction);
}
