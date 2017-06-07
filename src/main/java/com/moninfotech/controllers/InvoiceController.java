package com.moninfotech.controllers;

import com.moninfotech.domain.Booking;
import com.moninfotech.domain.Invoice;
import com.moninfotech.domain.User;
import com.moninfotech.domain.annotations.CurrentUser;
import com.moninfotech.service.BookingService;
import com.moninfotech.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by sayemkcn on 6/7/17.
 */
@Controller
@RequestMapping("/invoices")
public class InvoiceController {

    private final BookingService bookingService;
    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(BookingService bookingService,InvoiceService invoiceService) {
        this.bookingService = bookingService;
        this.invoiceService = invoiceService;
    }

    @GetMapping("/generate/{bookingId}")
    private String gererateInvoice(@PathVariable("bookingId") Long bookingId,
                                   @CurrentUser User currentUser,
                                   Model model) {
        Booking booking = this.bookingService.findOne(bookingId);
        if (!booking.getUser().getId().equals(currentUser.getId()))
            return "/bookings?message=You\'re not authorized to do this action!";
        Invoice invoice;
        if (booking.getInvoice() != null) invoice = booking.getInvoice();
        else invoice = new Invoice(nextDay(), currentUser, booking);
        invoice = this.invoiceService.save(invoice);

        model.addAttribute("invoice",invoice);
        model.addAttribute("template","fragments/booking/invoice");
        return "adminlte/index";
    }

    private Date nextDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }
}
