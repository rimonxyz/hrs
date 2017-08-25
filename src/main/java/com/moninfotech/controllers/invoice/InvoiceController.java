package com.moninfotech.controllers.invoice;

import com.moninfotech.commons.Constants;
import com.moninfotech.domain.*;
import com.moninfotech.domain.annotations.CurrentUser;
import com.moninfotech.service.BookingService;
import com.moninfotech.service.HotelService;
import com.moninfotech.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    @Autowired
    public InvoiceController(BookingService bookingService, InvoiceService invoiceService, HotelService hotelService) {
        this.bookingService = bookingService;
        this.invoiceService = invoiceService;
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
    private String paymentSuccess(@CurrentUser User currentUser,
                                  @PathVariable("invoiceId") Long invoiceId,
                                  @ModelAttribute PaymentInfo paymentInfo, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            System.out.println("PaymentInfoBindingError: " + bindingResult);
        Invoice invoice = this.invoiceService.findOne(invoiceId);

        // NEEDS PAYMENT VALIDATION
        if (paymentInfo==null || !paymentInfo.isValid())
            return "redirect:/invoices/generate/" + invoice.getBooking().getId() + "?message=Payment Invalid!";

        if (!invoice.getBooking().getUser().getId().equals(currentUser.getId()))
            return "redirect:/invoices/generate/" + invoice.getBooking().getId() + "?messageinfo=You can't pay for others lol!";
        invoice.getBooking().getTransaction().setSuccess(true);
        invoice.setPaid(invoice.getBooking().getTransaction().isSuccess());
        invoice = this.invoiceService.save(invoice);
        return "redirect:/invoices/generate/" + invoice.getBooking().getId() + "?messagesuccess=Payment Successful!";
    }

    @PostMapping("/{invoiceId}/payment/failed")
    private String paymentFailed(@PathVariable("invoiceId") Long invoiceId) {
        Invoice invoice = this.invoiceService.findOne(invoiceId);
        return "redirect:/invoices/generate/" + invoice.getBooking().getId() + "?message=Payment Failed!";
    }

    private Date nextDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }
}
