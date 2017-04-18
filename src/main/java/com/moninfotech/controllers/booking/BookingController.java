package com.moninfotech.controllers.booking;

import com.moninfotech.domain.Booking;
import com.moninfotech.domain.Room;
import com.moninfotech.domain.User;
import com.moninfotech.domain.annotations.CurrentUser;
import com.moninfotech.service.BookingService;
import com.moninfotech.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;
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

    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<Booking> createBooking(@RequestBody String idsJson, @CurrentUser User currentUser) throws UnsupportedEncodingException {
        idsJson = URLDecoder.decode(idsJson, "UTF-8");
        Long ids[] = null;
        try {
            ids = this.bookingService.convertToIds(idsJson);
            List<Room> roomList = this.roomService.findAll(ids);
            if (roomList.isEmpty())
                return new ResponseEntity<Booking>(HttpStatus.NO_CONTENT);
            Booking booking = new Booking();
            booking.setRoomList(roomList);
            booking.setUser(currentUser);
            booking.setStartDate(new Date());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, 2);
            booking.setEndDate(calendar.getTime());
            booking = this.bookingService.save(booking);
            return new ResponseEntity<Booking>(booking, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Booking>(HttpStatus.BAD_REQUEST);
        }
    }

}
