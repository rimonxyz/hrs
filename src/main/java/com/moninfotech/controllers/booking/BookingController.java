package com.moninfotech.controllers.booking;

import com.moninfotech.domain.Booking;
import com.moninfotech.domain.Room;
import com.moninfotech.domain.User;
import com.moninfotech.domain.annotations.CurrentUser;
import com.moninfotech.service.BookingService;
import com.moninfotech.service.RoomService;
import com.moninfotech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

/**
 * Created by sayemkcn on 4/18/17.
 */
@Controller
@RequestMapping(value = "/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    private String myBookings(@CurrentUser User user,
                              @RequestParam(value = "page", required = false) Integer page,
                              @RequestParam(value = "size", required = false) Integer size,
                              Model model) {
        if (page == null || page < 0) page = 0;
        if (size == null || !(size > 0)) size = 10;
        if (user == null) return "redirect:/login";
        model.addAttribute("bookingList", this.bookingService.findByUser(user, page, size));
        return "booking/all";
    }

    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<Booking> createBooking(@RequestBody String data, @CurrentUser User currentUser) throws UnsupportedEncodingException {
        System.out.println(data);
        data = URLDecoder.decode(data, "UTF-8");
        System.out.println(data);
        Long ids[] = null;
        try {
            ids = this.bookingService.convertToIds(data);
            Date[] bookingDates = this.bookingService.getDates(data);
            if (bookingDates == null || bookingDates.length < 2) // user hasn't selected date range
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            List<Room> roomList = this.roomService.findAll(ids); // find rooms with that ids
            if (roomList.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            Booking booking = new Booking();
            booking.setRoomList(roomList); // associate rooms with the booking object
            booking.setHotel(roomList.get(0).getHotel());
            User user = this.userService.findOne(currentUser.getId());
            if (user == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN); // if user not logged in
            booking.setUser(user);
            booking.setStartDate(bookingDates[0]);
            booking.setEndDate(bookingDates[1]);
            // check if any of these rooms are already booked during this period
            if (!booking.isValid()) return new ResponseEntity<>(HttpStatus.IM_USED);

            booking = this.bookingService.save(booking);
//            System.out.println(booking.toString());
            return new ResponseEntity<>(booking, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    // checkout page
    @RequestMapping(value = "/review",method = RequestMethod.GET)
    private String reviewPage(){
        return "booking/review";
    }

}
