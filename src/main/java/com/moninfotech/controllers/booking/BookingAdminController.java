package com.moninfotech.controllers.booking;

import com.moninfotech.domain.Booking;
import com.moninfotech.domain.User;
import com.moninfotech.domain.annotations.CurrentUser;
import com.moninfotech.service.BookingService;
import com.moninfotech.service.MailService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/bookings")
public class BookingAdminController {

    private final BookingService bookingService;
    private final MailService mailService;

    @Autowired
    public BookingAdminController(BookingService bookingService, MailService mailService) {
        this.bookingService = bookingService;
        this.mailService = mailService;
    }

    @GetMapping("/pending")
    private String getPendingBookingPage(@CurrentUser User currentUser,
                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                         Model model) {
        List<Booking> bookingList = this.bookingService.findBookings(currentUser, false, false, false, page, 20);

        model.addAttribute("bookingList", bookingList);
        model.addAttribute("page", page);

        return "adminlte/fragments/booking/pending";
    }

    @PostMapping("/{id}/approve")
    private String approveBooking(@PathVariable("id") Long bookingId,
                                  @CurrentUser User currentUser,
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  Model model) throws NotFoundException {
        Booking booking = this.bookingService.findOne(bookingId);
        if (booking == null) throw new NotFoundException("Could not find booking!");

        booking.setApproved(booking.isValid());
        this.bookingService.save(booking);

        return "redirect:/admin/bookings/pending?messagesuccess=Booking "+booking.getId()+" is approved!";
    }

}
