package com.moninfotech.controllers.booking;

import com.moninfotech.commons.DateUtils;
import com.moninfotech.commons.SessionAttr;
import com.moninfotech.commons.pojo.BookingHelper;
import com.moninfotech.commons.pojo.Roles;
import com.moninfotech.domain.Booking;
import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.Room;
import com.moninfotech.domain.User;
import com.moninfotech.domain.annotations.CurrentUser;
import com.moninfotech.service.BookingService;
import com.moninfotech.service.HotelService;
import com.moninfotech.service.RoomService;
import com.moninfotech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sayemkcn on 4/18/17.
 */
@Controller
@RequestMapping(value = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final RoomService roomService;
    private final UserService userService;
    private final HotelService hotelService;

    @Autowired
    public BookingController(BookingService bookingService,
                             RoomService roomService,
                             UserService userService,
                             HotelService hotelService) {
        this.bookingService = bookingService;
        this.roomService = roomService;
        this.userService = userService;
        this.hotelService = hotelService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    private String myBookings(@CurrentUser User user,
                              @RequestParam(value = "page", required = false) Integer page,
                              @RequestParam(value = "size", required = false) Integer size,
                              Model model) {
        if (page == null || page < 0) page = 0;
        if (size == null || !(size > 0)) size = 10;
        if (user == null) return "redirect:/login";

        model.addAttribute("bookingHelper", new BookingHelper());
        model.addAttribute("bookingList", this.bookingService.findByUser(user, page, size));
        model.addAttribute("template", "fragments/booking/all");
        return "adminlte/index";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    private String details(@PathVariable("id") Long id,
                           @CurrentUser User currentUser,
                           Model model) {
        Booking booking = this.bookingService.findOne(id);
        if (booking == null) return "redirect:/bookings?message=Booking not found!";
        if (!booking.getUser().getId().equals(currentUser.getId()))
            return "redirect:/bookings?message=You\'re not authorised to access this resource.";
        model.addAttribute("booking", booking);

        model.addAttribute("template", "fragments/booking/details");
        return "adminlte/index";
    }

    @GetMapping("/cart/add/{roomId}")
    private String addToCart(@PathVariable("roomId") Long roomId,
                             @RequestParam(value = "date", required = false) String dateStr,
                             HttpSession session) {
        Date date = null;
        try {
            date = DateUtils.getParsableDateFormat().parse(dateStr);
        } catch (ParseException e) {
            date = new Date();
        }
        Room room = this.roomService.findOne(roomId);
        if (room == null) return "redirect:/rooms/" + roomId;
        Booking booking = (Booking) session.getAttribute(SessionAttr.SESSION_BOOKING);
        if (booking == null || booking.getRoomList() == null || booking.getBookingDateList() == null) {
            booking = new Booking();
            booking.setRoomList(new ArrayList<>());
            booking.setBookingDateList(new ArrayList<>());
        }
        // validate booking // if user tries to book rooms from different hotels in a same booking restrict him from doing that shit.
        if (this.bookingService.isBookingInvalid(booking,room))
            return "redirect:/hotels/"+room.getHotel().getId()+"?message=You can\'t book rooms from different hotels at the same time!";
        if (this.bookingService.isDuplicateAttempt(booking, room, date))
            return "redirect:/rooms/" + roomId + "/" + dateStr + "?message=You can't book same room twice at same day!";
        booking.getRoomList().add(room);
        booking.getBookingDateList().add(date);
        session.setAttribute(SessionAttr.SESSION_BOOKING, booking);

        return "redirect:/hotels/" + room.getHotel().getId()+"?messageinfo=Room "+room.getRoomNumber()+" Added to the cart!";
    }

    @GetMapping("/cart/remove/{roomId}")
    private String removeFromCart(@PathVariable("roomId") Long roomId,
                                  HttpSession session) {
        // remove item from session
        Booking booking = (Booking) session.getAttribute(SessionAttr.SESSION_BOOKING);
        if (booking != null) {
            List<Room> sBookingRooms = this.roomService.removeRoom(booking.getRoomList(), roomId);
            List<Date> sBookingDates = this.bookingService.removeBookingDate(booking, roomId);
            booking.setRoomList(sBookingRooms);
            booking.setBookingDateList(sBookingDates);
            session.setAttribute(SessionAttr.SESSION_BOOKING, booking);
        }
        Room room = this.roomService.findOne(roomId);
        if (room==null) return "redirect:/?message=Can\'t find room!";
        return "redirect:/hotels/" + room.getHotel().getId();
    }

    @GetMapping("/checkout")
    private String checkout(@CurrentUser User currentUser, HttpSession session) {
        if (currentUser == null) return "redirect:/login?message=Please login to continue.";
        Booking booking = (Booking) session.getAttribute(SessionAttr.SESSION_BOOKING);
        if (booking == null
                || booking.getRoomList() == null
                || booking.getRoomList().isEmpty()
                || booking.getBookingDateList() == null
                || booking.getBookingDateList().isEmpty()) return "redirect:/hotels?message=No items to book!";
        // set hotel for booking
        Hotel hotel = booking.getRoomList().get(0).getHotel();
        booking.setHotel(hotel);
        // set user of booking
        booking.setUser(currentUser);
        // check if hotel list and booking date list are incompatible
        if (booking.getRoomList().size() != booking.getBookingDateList().size()) {
            session.removeAttribute(SessionAttr.SESSION_BOOKING);
            return "redirect:/hotels/" + booking.getHotel() + "?message=There\'s something wrong. Please try again later!";
        }
        if (!booking.isValid()) {
            session.removeAttribute(SessionAttr.SESSION_BOOKING);
            return "redirect:/hotels/" + booking.getHotel().getId() + "?message=One or more room isn't available during this time. Please try again!";
        }
        booking = this.bookingService.save(booking);
        session.removeAttribute(SessionAttr.SESSION_BOOKING);
        return "redirect:/bookings/" + booking.getId() + "?message=Booking Successful!";
    }

//    @ResponseBody
//    @CrossOrigin
//    @RequestMapping(value = "/order", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//    private ResponseEntity<Booking> orderBooking(@RequestBody String data,
//                                                 @CurrentUser User currentUser,
//                                                 HttpSession session) throws UnsupportedEncodingException {
//        System.out.println(data);
//        data = URLDecoder.decode(data, "UTF-8");
//        System.out.println(data);
//        Long ids[] = null;
//        try {
//            ids = this.bookingService.convertToIds(data);
//            Date[] bookingDates = this.bookingService.getDates(data);
//            if (bookingDates == null || bookingDates.length < 2) // user hasn't selected date range
//                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//            List<Room> roomList = this.roomService.findAll(ids); // find rooms with that ids
//            if (roomList.isEmpty())
//                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//            Booking booking = new Booking();
//            booking.setRoomList(roomList); // associate rooms with the booking object
//            booking.setHotel(roomList.get(0).getHotel());
//            User user = this.userService.findOne(currentUser.getId());
//            if (user == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN); // if user not logged in
//            booking.setUser(user);
//            booking.setStartDate(bookingDates[0]);
//            booking.setEndDate(bookingDates[1]);
//            // check if any of these rooms are already booked during this period
//            if (!booking.isValid()) return new ResponseEntity<>(HttpStatus.IM_USED);
//            session.setAttribute(SessionAttr.SESSION_BOOKING, booking);
////            System.out.println(booking.toString());
//            return new ResponseEntity<>(booking, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }

    // Review
    @RequestMapping(value = "/review", method = RequestMethod.GET)
    private String reviewPage(@CurrentUser User currentUser,
                              @RequestParam(value = "query", required = false) String query,
                              HttpSession session, Model model) {
        // Check if logged in user is Hotel or not
        // if hotel then make sure that he can't book for hotels rather than his own
        if (currentUser.hasAssignedRole(Roles.ROLE_HOTEL)) {
            Hotel hotel = this.hotelService.findByUser(currentUser);
            Booking booking = (Booking) session.getAttribute(SessionAttr.SESSION_BOOKING);
            if (booking == null)
                return "redirect:/hotels/" + hotel.getId() + "?message=You haven't selected any item yet!";
            // if it's someone else hotel
            if (!hotel.getId().equals(booking.getHotel().getId()))
                return "redirect:/hotels/" + hotel.getId() + "?message=Sorry you can\'t book for other hotels!";
            // search user by query and add to model
            if (query != null) {
                List<User> userList = new ArrayList<>();
                User user = this.userService.findByEmail(query);
                if (user != null) userList.add(user);
                List<User> uList = this.userService.findByName(query);
                if (uList != null)
                    userList.addAll(uList);
                model.addAttribute("userList", userList);
            }
        }
        return "booking/review";
    }

    @RequestMapping(value = "/review/remove/{roomId}", method = RequestMethod.GET)
    private String removeRoom(@PathVariable("roomId") Long roomId,
                              HttpSession session) {
        Booking booking = (Booking) session.getAttribute(SessionAttr.SESSION_BOOKING);
        if (booking != null) {
            List<Room> updatedRoomList = this.roomService.removeRoom(booking.getRoomList(), roomId);
            booking.setRoomList(updatedRoomList);
            session.setAttribute(SessionAttr.SESSION_BOOKING, booking);
        }
        return "redirect:/bookings/review";
    }

    @RequestMapping(value = "/review/confirm", method = RequestMethod.POST)
    private String confirmBooking(@RequestParam(value = "userId", required = false) Long userId, HttpSession session) {
        Booking booking = (Booking) session.getAttribute(SessionAttr.SESSION_BOOKING);
        // Set user from user id // offline booking for hotel admin for offline user
        if (userId != null)
            booking.setUser(this.userService.findOne(userId));
        if (booking != null && booking.isValid())
            booking = this.bookingService.save(booking);
        return "redirect:/bookings";
    }

}
