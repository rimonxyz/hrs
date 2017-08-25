package com.moninfotech.service.impl;

import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.Transaction;
import com.moninfotech.domain.User;
import com.moninfotech.repository.TransactionRepository;
import com.moninfotech.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService{
    private final TransactionRepository transactionRepo;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return this.transactionRepo.save(transaction);
    }

    @Override
    public Transaction findOne(Long id) {
        return this.transactionRepo.findOne(id);
    }

    @Override
    public Transaction findByHotel(Hotel hotel) {
        return null;
    }

    @Override
    public Transaction findByUser(User user) {
        return null;
    }
}
