package com.moninfotech.service.impl;

import com.moninfotech.domain.Booking;
import com.moninfotech.domain.Invoice;
import com.moninfotech.domain.PaymentInfo;
import com.moninfotech.domain.User;
import com.moninfotech.repository.InvoiceRepository;
import com.moninfotech.service.InvoiceService;
import com.moninfotech.service.PaymentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sayemkcn on 6/7/17.
 */
@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepo;
    private final PaymentInfoService paymentInfoService;

    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepo, PaymentInfoService paymentInfoService) {
        this.invoiceRepo = invoiceRepo;
        this.paymentInfoService = paymentInfoService;
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
    public List<Invoice> findByUser(User user, boolean isPaid) {
        return this.invoiceRepo.findByUserAndPaid(user, isPaid);
    }

    @Override
    public Invoice findByBooking(Booking booking) {
        return this.invoiceRepo.findByBooking(booking);
    }

    @Override
    public Invoice setInvoicePaid(Invoice invoice, PaymentInfo paymentInfo) {
        if (invoice == null) throw new IllegalArgumentException("invoice can not be null");
        if (paymentInfo == null) paymentInfo = new PaymentInfo();

        invoice.getBooking().getTransaction().setSuccess(true);
        invoice.setPaid(invoice.getBooking().getTransaction().isSuccess());
        invoice = this.save(invoice);

        // save payment info
        paymentInfo.setTransaction(invoice.getBooking().getTransaction());
        this.paymentInfoService.save(paymentInfo);
        return invoice;
    }
}
