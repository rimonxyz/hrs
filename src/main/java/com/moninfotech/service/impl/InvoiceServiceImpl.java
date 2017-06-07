package com.moninfotech.service.impl;

import com.moninfotech.domain.Booking;
import com.moninfotech.domain.Invoice;
import com.moninfotech.domain.User;
import com.moninfotech.repository.InvoiceRepository;
import com.moninfotech.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sayemkcn on 6/7/17.
 */
@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepo;

    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepo) {
        this.invoiceRepo = invoiceRepo;
    }

    @Override
    public Invoice save(Invoice invoice) {
        return this.invoiceRepo.save(invoice);
    }

    @Override
    public Invoice findOne(Long id) {
        return this.invoiceRepo.findOne(id);
    }

    @Override
    public List<Invoice> findByUser(User user) {
        return this.invoiceRepo.findByUser(user);
    }

    @Override
    public Invoice findByBooking(Booking booking) {
        return this.invoiceRepo.findByBooking(booking);
    }
}
