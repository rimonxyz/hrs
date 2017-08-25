package com.moninfotech.service;

import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.Transaction;
import com.moninfotech.domain.User;

public interface TransactionService {
    Transaction save(Transaction transaction);
    Transaction findOne(Long id);
    Transaction findByHotel(Hotel hotel);
    Transaction findByUser(User user);
}
