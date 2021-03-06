package com.moninfotech.controllers.booking;

import com.moninfotech.commons.Constants;
import com.moninfotech.commons.SessionAttr;
import com.moninfotech.commons.pojo.BookingHelper;

import com.moninfotech.commons.utils.DateUtils;

import com.moninfotech.commons.utils.PasswordUtil;
import com.moninfotech.config.security.SecurityConfig;
import com.moninfotech.domain.*;
import com.moninfotech.domain.annotations.CurrentUser;
import com.moninfotech.exceptions.invalid.InvalidException;
import com.moninfotech.exceptions.notfound.SessionBookingNotFoundException;
import com.moninfotech.exceptions.nullexceptions.NullPasswordException;
import com.moninfotech.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    private final InvoiceService invoiceService;
    private final TransactionService transactionService;

    @Autowired
    public BookingController(BookingService bookingService,
                             RoomService roomService,
                             UserService userService,
                             HotelService hotelService, InvoiceService invoiceService, TransactionService transactionService) {
        this.bookingService = bookingService;
        this.roomService = roomService;
        this.userService = userService;
        this.hotelService = hotelService;
        this.invoiceService = invoiceService;
        this.transactionService = transactionService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    private String myBookings(@CurrentUser User currentUser,
                              @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                              @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                              @RequestParam(value = "filterType", required = false, defaultValue = "") String filterType,
                              @RequestParam(value = "filterValue", required = false, defaultValue = "") String filterValue,
                              @RequestParam(value = "analDate", required = false, defaultValue = "") String analDate,
                              @RequestParam(value = "analFlagRole", required = false, defaultValue = Constants.Roles.ROLE_USER) String analFlagRole,
                              Model model) throws ParseException {

        // find booking list by role
        List<Booking> bookingList = this.bookingService.findBookings(currentUser, false,true, true, page, size);

        Date date = null;
        if (analDate == null || analDate.isEmpty()) date = new Date();
        else date = DateUtils.getParsableDateFormat().parse(analDate);

        List<Room> todaysBookedRoomList = this.bookingService.findFilteredRoomList(currentUser, date, analFlagRole);
        List<Room> todaysPlacedRoomList = this.bookingService.findFilteredRoomListByPlacementDateDistinct(currentUser, date, analFlagRole);

        if (!filterType.isEmpty() && !filterValue.isEmpty()) {
            bookingList = this.bookingService.filterBookingList(bookingList, filterType, filterValue);
            todaysBookedRoomList = this.roomService.filterRoomList(todaysBookedRoomList, filterType, filterValue);
            todaysPlacedRoomList = this.roomService.filterRoomList(todaysPlacedRoomList, filterType, filterValue);
        }
        // find total placement price and count

        model.addAttribute("bookingHelper", new BookingHelper());
        model.addAttribute("analDate", DateUtils.getParsableDateFormat().format(date));
        model.addAttribute("analFlagRole", analFlagRole);
        model.addAttribute("bookingList", bookingList);
        model.addAttribute("todaysPlacedRoomList", todaysPlacedRoomList);
        model.addAttribute("todaysPlacedRoomListSize", this.bookingService.findFilteredRoomListByPlacementDate(currentUser, new Date(), analFlagRole).size());
        model.addAttribute("bookedRoomList", todaysBookedRoomList);
        model.addAttribute("hotelList", this.hotelService.findAll());
        model.addAttribute("invoiceList", this.invoiceService.findByUser(currentUser, false));
        model.addAttribute("filterValue", filterValue);

        model.addAttribute("page", page);
        return "adminlte/fragments/booking/all";
    }

    @GetMapping("/canceled")
    private String canceledBookings(@CurrentUser User currentUser,
                                    @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                    @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                    Model model) {
        // find booking list by role
        List<Booking> canceledBookingList = this.bookingService.findBookings(currentUser, true, true,true, page, size);
        model.addAttribute("bookingList", canceledBookingList);
//        model.addAttribute("template", "fragments/booking/allCanceled");
        return "adminlte/fragments/booking/allCanceled";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    private String details(@PathVariable("id") Long id,
                           @CurrentUser User currentUser,
                           Model model) {
        Booking booking = this.bookingService.findOne(id);
        if (booking == null) return "redirect:/bookings?message=Booking not found!";
        // If this booking doesn't belong to this logged in user
        if (!this.bookingService.belongsTo(booking, currentUser))
            return "redirect:/bookings?message=You\'re not authorised to access this resource.";

        model.addAttribute("booking", booking);
        model.addAttribute("myBookingList", this.bookingService.getBookingList(booking.getHotel(), currentUser));
//        model.addAttribute("template", "fragments/booking/details");
        return "adminlte/fragments/booking/details";
    }

    @GetMapping("/cart/add/{roomId}")
    private String addToCart(@PathVariable("roomId") Long roomId,
                             @RequestParam(value = "checkInDate", required = false) Date checkInDate,
                             @RequestParam(value = "checkoutDate", required = false) Date checkoutDate,
                             HttpSession session) throws InvalidException {
        Room room = this.roomService.findOne(roomId);
        this.bookingService.addToCart(session, roomId, checkInDate, checkoutDate);

        return "redirect:/hotels/" + room.getHotel().getId() + "?messageinfo=Room " + room.getRoomNumber() + " Added to the cart!";
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
        if (room == null) return "redirect:/?message=Can\'t find room!";
        return "redirect:/hotels/" + room.getHotel().getId();
    }

    @GetMapping("/cart/clear")
    private String clearCart(HttpSession session) {
        // remove item from session
        session.removeAttribute(SessionAttr.SESSION_BOOKING);
        return "redirect:/hotels?type=Hotel&messageinfo=All items removed from cart!";
    }

    @GetMapping("/checkout")
    private String checkout(@CurrentUser User currentUser, HttpSession session) throws SessionBookingNotFoundException {
        if (currentUser == null) return "redirect:/bookings/tempRegister";
        Booking booking = this.bookingService.checkout(currentUser, session);
        if (!booking.isApproved())
            return "redirect:/hotels?messagesuccess=Thank you for your request. We'll send you an invoice by email after your booking is approved!";
        return "redirect:/invoices/generate/" + booking.getId();
    }

    @GetMapping("/tempRegister")
    private String tempRegisterPage(HttpSession session) {
        session.setAttribute(SessionAttr.SESSION_BOOKING_PROCESSING, true);
        return "adminlte/pages/tempRegister";
    }

    @PostMapping("/tempRegister")
    private String tempRegister(@ModelAttribute User user, BindingResult bindingResult, HttpSession session) throws Exception, NullPasswordException {
        session.removeAttribute(SessionAttr.SESSION_BOOKING_PROCESSING); // remove booking processing

        if (bindingResult.hasErrors()) System.out.println(bindingResult.toString());
        // check if user already exists
        if (this.userService.findByEmail(user.getEmail()) != null)
            return "redirect:/bookings/tempRegister?message=User already registered!";
        // set default user role
        user.grantRole(Constants.Roles.ROLE_USER);
        user.setAddress(new Address());
        user.setPassword(PasswordUtil.encryptPassword(user.getPassword(), PasswordUtil.EncType.BCRYPT_ENCODER, null));
        user = this.userService.save(user);
        SecurityConfig.setAuthentication(user);
        return "redirect:/bookings/checkout";
    }


//    @GetMapping("/checkout/assignUser")
//    private String assignUserToBookingPage(@RequestParam(value = "searchQuery", required = false, defaultValue = "") String searchQuery, Model model) {
//        model.addAttribute("userList", this.userService.findByEmailOrPhoneNumber(searchQuery, searchQuery));
////        model.addAttribute("template", "fragments/booking/assignUser");
//        return "adminlte/fragments/booking/assignUser";
//    }
//
//    @PostMapping("/checkout/assignUser")
//    private String assignUserToBooking(@RequestParam("userId") Long userId, HttpSession session, Model model) {
//        User user = this.userService.findOne(userId);
//        if (user == null) return "redirect:/bookings/checkout/assignUser?message=User not found!";
//        Booking booking = (Booking) session.getAttribute(SessionAttr.SESSION_BOOKING);
//        if (booking == null) return "redirect:/checkout/assignUser?message=User not found!";
//        booking.setUser(user);
//        booking.setManualBooking(true);
//        // HANDLE PAYMENT PROCEEDURE
//
//        booking = this.bookingService.save(booking);
//        session.removeAttribute(SessionAttr.SESSION_BOOKING);
//        return "redirect:/invoices/generate/" + booking.getId() + "?messagesuccess=Booking Successful!";
//    }
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
        if (currentUser.hasAssignedRole(Constants.Roles.ROLE_HOTEL_ADMIN)) {
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

    // Cancel Booking
    @PostMapping("/cancel/{id}")
    private String cancelBooking(@PathVariable("id") Long id,
                                 @CurrentUser User user) {
        Booking booking = this.bookingService.findOne(id);
        if (booking == null || !booking.belongsTo(user))
            return "redirect:/bookings?message=You are unauthorized to cancel this booking!";
        if (!booking.isCancelable())
            return "redirect:/bookings?message=You can\'t cancel booking after 24 hours!";
        booking.setCancelled(true);
        booking = this.bookingService.save(booking);
        return "redirect:/bookings?messageinfo=Booking Canceled!";
    }

}
