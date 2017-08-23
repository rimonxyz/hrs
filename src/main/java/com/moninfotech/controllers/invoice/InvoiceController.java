package com.moninfotech.controllers.invoice;

import com.moninfotech.commons.Constants;
import com.moninfotech.domain.Booking;
import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.Invoice;
import com.moninfotech.domain.User;
import com.moninfotech.domain.annotations.CurrentUser;
import com.moninfotech.service.BookingService;
import com.moninfotech.service.HotelService;
import com.moninfotech.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    private final HotelService hotelService;

    @Autowired
    public InvoiceController(BookingService bookingService, InvoiceService invoiceService, HotelService hotelService) {
        this.bookingService = bookingService;
        this.invoiceService = invoiceService;
        this.hotelService = hotelService;
    }

    @GetMapping("/generate/{bookingId}")
    private String gererateInvoice(@PathVariable("bookingId") Long bookingId,
                                   @CurrentUser User currentUser,
                                   Model model) {
        Booking booking = this.bookingService.findOne(bookingId);
        if (booking == null) return "redirect:/bookings?message=You\'re not authorized to do this action!";
        // If this booking doesn't belong to this logged in user
        if (!this.bookingService.belongsTo(booking, currentUser))
            return "redirect:/bookings?message=You\'re not authorised to access this resource.";
        Invoice invoice;
        if (booking.getInvoice() != null) invoice = booking.getInvoice();
        else invoice = new Invoice(nextDay(), currentUser, booking);
        invoice = this.invoiceService.save(invoice);

        model.addAttribute("invoice", invoice);
        model.addAttribute("template", "fragments/booking/invoice");
        return "adminlte/index";
    }

    @PostMapping("/{invoiceId}/payment/success")
    private String paymentSuccess(@PathVariable("invoiceId")Long invoiceId){
        Invoice invoice = this.invoiceService.findOne(invoiceId);
        invoice.setPaid(true);
        invoice = this.invoiceService.save(invoice);
        return "redirect:/invoices/generate/"+invoice.getBooking().getId()+"?messagesuccess=Payment Successful!";
    }

    private Date nextDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }
}
