package com.moninfotech.service;

import com.moninfotech.domain.Booking;
import com.moninfotech.domain.Invoice;
import com.moninfotech.domain.User;

import java.util.List;

/**
 * Created by sayemkcn on 6/7/17.
 */
public interface InvoiceService {
    Invoice save(Invoice invoice);

    Invoice findOne(Long id);

    List<Invoice> findByUser(User user,boolean isPaid);

    Invoice findByBooking(Booking booking);

}
