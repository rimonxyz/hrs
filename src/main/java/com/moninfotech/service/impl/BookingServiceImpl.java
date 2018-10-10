package com.moninfotech.service.impl;

import com.moninfotech.commons.Constants;
import com.moninfotech.commons.SessionAttr;
import com.moninfotech.commons.Utils;
import com.moninfotech.commons.pojo.FilterType;
import com.moninfotech.commons.utils.DateUtils;
import com.moninfotech.domain.*;
import com.moninfotech.exceptions.invalid.InvalidException;
import com.moninfotech.exceptions.notfound.SessionBookingNotFoundException;
import com.moninfotech.repository.BookingRepository;
import com.moninfotech.service.BookingService;
import com.moninfotech.service.HotelService;
import com.moninfotech.service.RoomService;
import com.moninfotech.service.TransactionService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by sayemkcn on 4/18/17.
 */
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepo;
    private final HotelService hotelService;
    private final RoomService roomService;
    private final TransactionService transactionService;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepo, HotelService hotelService, RoomService roomService, TransactionService transactionService) {
        this.bookingRepo = bookingRepo;
        this.hotelService = hotelService;
        this.roomService = roomService;
        this.transactionService = transactionService;
    }

    @Override
    public List<Booking> findByUser(User user, boolean isCanceled, boolean isConfirmed, Integer page, Integer size) {
        if (page == null || size == null)
            return this.bookingRepo.findByUserAndCancelledAndConfirmed(user, isCanceled, isConfirmed);
        return this.bookingRepo.findByUserAndCancelledAndConfirmed(user, isCanceled, isConfirmed, new PageRequest(page, size, Sort.Direction.DESC, Constants.FIELD_ID)).getContent();
    }

    @Override
    public List<Booking> findByHotel(Hotel hotel, boolean isCanceled, boolean isConfirmed, Integer page, Integer size) {
        if (page == null || size == null)
            return this.bookingRepo.findByHotelAndCancelledAndConfirmed(hotel, isCanceled, isConfirmed);
        return this.bookingRepo.findByHotelAndCancelledAndConfirmed(hotel, isCanceled, isConfirmed, new PageRequest(page, size, Sort.Direction.DESC, Constants.FIELD_ID)).getContent();
    }

    @Override
    public Booking findOne(Long id) {
        return this.bookingRepo.findOne(id);
    }

    @Override
    public List<Booking> findAll(boolean isCanceled, boolean isConfirmed, boolean isApproved, Integer page, Integer size) {
        if (page == null || size == null)
            return this.bookingRepo.findByCancelledAndConfirmedAndApproved(isCanceled, isConfirmed, isApproved);
        return this.bookingRepo.findByCancelledAndConfirmed(isCanceled, isConfirmed, new PageRequest(page, size, Sort.Direction.DESC, Constants.FIELD_ID)).getContent();
    }

    @Override
    public boolean belongsTo(Booking booking, User user) {
        // If this booking doesn't belong to this logged in user
        if (!booking.getUser().getId().equals(user.getId())) {
            // check if logged in user is hotel admin.
            if (user.hasAssignedRole(Constants.Roles.ROLE_HOTEL_ADMIN)) {
                // check if this booking isn't belongs to his/her hotel, then restrict and send redirect with message
                Hotel hotel = this.hotelService.findByUser(user);
                if (!booking.getHotel().getId().equals(hotel.getId()))
                    return false;
            }
        }
        return true;
    }

    @Override
    public Booking save(Booking booking) {
        return this.bookingRepo.save(booking);
    }

    @Override
    public synchronized Long[] convertToIds(String json) {
        try {
            Long[] ids = null;
            // data json
            JSONObject jsonObject = new JSONObject(json);
            //id array json
            String idsString = jsonObject.getString("ids");
            if (!idsString.isEmpty()) {
                String[] splittedIds = idsString.split(",");
                ids = new Long[splittedIds.length];
                for (int i = 0; i < splittedIds.length; i++) {
                    ids[i] = Long.parseLong(splittedIds[i]);
                }

            }
            return ids;
        } catch (Exception e) {
            throw new JSONException(e);
        }
    }

    @Override
    public Date[] getDates(String json) {
        try {
            // data json
            JSONObject jsonObject = new JSONObject(json);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date[] dates = new Date[2];
            dates[0] = sdf.parse(jsonObject.getString("startDate"));
            dates[1] = sdf.parse(jsonObject.getString("endDate"));
            return dates;
        } catch (JSONException e) {
            throw new JSONException(e);
        } catch (IndexOutOfBoundsException e) {
            throw e;
        } catch (ParseException e) {
            System.out.println("getDates(String json) :" + e.toString());
        }
        return new Date[2];
    }

    @Override
    public List<Booking> findAll(Long[] ids) {
        return this.bookingRepo.findAll(Arrays.asList(ids));
    }

    @Override
    public Collection<Booking> getBookingList(Hotel hotel, User user) {
        List<Booking> myBookingList = new ArrayList<>();
        hotel.getBookingList().forEach(booking -> {
            if (booking.getUser().getId().equals(user.getId()))
                myBookingList.add(booking);
        });
        myBookingList.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
        return myBookingList;
    }

    @Override
    public boolean isDuplicateAttempt(Booking booking, Room room, Date date) {
        if (booking != null && booking.getBookingDateList() != null && booking.getBookingDateList() != null) {
            for (int i = 0; i < booking.getRoomList().size() && i < booking.getBookingDateList().size(); i++) {
                if (booking.getRoomList().get(i).getId().equals(room.getId()) && DateUtils.isSameDay(booking.getBookingDateList().get(i), date))
                    return true;
            }
        }
        return false;
    }

    @Override
    public List<Date> removeBookingDate(Booking booking, Long roomId) {
        List<Date> newBookingDateList = new ArrayList<>();
        for (int i = 0; i < booking.getRoomList().size(); i++) {
            if (!booking.getRoomList().get(i).getId().equals(roomId))
                newBookingDateList.add(booking.getBookingDateList().get(i));
        }
        return newBookingDateList;
    }

    @Override
    public boolean isBookingInvalid(Booking booking, Room room) {
        // if booking has any room from different hotel
        return booking.getRoomList().stream()
                .filter(r -> !r.getHotel().getId().equals(room.getHotel().getId()))
                .count() > 0;
    }

    @Override
    public List<Booking> findBookings(User currentUser, boolean isCanceled, boolean isConfirmed, boolean isApproved, Integer page, Integer size) {
        List<Booking> bookingList = new ArrayList<>();
        if (currentUser.hasAssignedRole(Constants.Roles.ROLE_USER))
            bookingList = this.findByUser(currentUser, isCanceled, isConfirmed, page, size);
        else if (currentUser.hasAssignedRole(Constants.Roles.ROLE_HOTEL_ADMIN)) {
            Hotel hotel = this.hotelService.findByUser(currentUser);
            bookingList = this.findByHotel(hotel, isCanceled, isConfirmed, page, size);
        } else if (currentUser.hasAssignedRole(Constants.Roles.ROLE_ADMIN)) {
            bookingList = this.findAll(isCanceled, isConfirmed, isApproved, page, size);
        }
        return bookingList;
    }


    @Override
    public List<Booking> findFiltered(User currentUser, boolean isManual, String hotelType) {
        List<Booking> bookingList = this.findBookings(currentUser, false, true,true, null, null);
        // filter
        if (hotelType == null || hotelType.equals(Hotel.Type.BOTH))
            return bookingList
                    .stream()
                    .filter(booking -> booking.isManualBooking() == isManual)
                    .collect(Collectors.toList());
        else
            return bookingList
                    .stream()
                    .filter(booking -> booking.getHotel().getType().equals(hotelType))
                    .filter(booking -> booking.isManualBooking() == isManual)
                    .collect(Collectors.toList());

    }

    @Override
    public List<Room> findFilteredRoomListByPlacementDate(User currentUser, Date date, String analFlagRole) {
        List<Booking> bookingList = this.findBookings(currentUser, false, true,true, null, null);
        List<Room> roomList = new ArrayList<>();
        for (Booking booking : bookingList) {
            // filter booking by role
            if (booking.getUser().hasAssignedRole(analFlagRole)) {
                // check if only user
                // skip for role_user if user has more than one role
                if (analFlagRole.equals(Constants.Roles.ROLE_USER) && !booking.getUser().isOnlyUser())
                    continue;
                if (DateUtils.isSameDay(booking.getCreated(), date))
                    roomList.addAll(booking.getRoomList());
            }
        }
        return roomList;
    }


    // returns all of the booking list (Distinct) which order is placed on provided day
    @Override
    public List<Room> findFilteredRoomListByPlacementDateDistinct(User currentUser, Date date, String analFlagRole) {
        return this.findFilteredRoomListByPlacementDate(currentUser, date, analFlagRole).stream()
                .filter(Utils.distinctByKey(BaseEntity::getId))
                .collect(Collectors.toList());
    }

    // returns all rooms that are booked on provided day
    @Override
    public List<Room> findFilteredRoomList(User currentUser, Date date, String analFlagRole) {
        List<Booking> bookingList = this.findBookings(currentUser, false, true,true, null, null);

        List<Room> bookedRooms = new ArrayList<>();
        for (Booking booking : bookingList) {
            // filter booking by role
            if (booking.getUser().hasAssignedRole(analFlagRole)) {
                // check if only user
                // skip for role_user if user has more than one role
                if (analFlagRole.equals(Constants.Roles.ROLE_USER) && !booking.getUser().isOnlyUser())
                    continue;
                List<Room> roomList = booking.getRoomList();
                List<Date> bookingDateList = booking.getBookingDateList();
                for (int i = 0; i < roomList.size() && i < bookingDateList.size(); i++) {
                    if (DateUtils.isSameDay(bookingDateList.get(i), date))
                        bookedRooms.add(roomList.get(i));
                }
            }
        }
        return bookedRooms;
    }

    @Override
    public List<Booking> filterBookingList(List<Booking> bookingList, String filterType, String filterValue) throws ParseException {
        if (bookingList == null || bookingList.isEmpty() || filterType == null || filterType.isEmpty() || filterValue == null || filterValue.isEmpty())
            return bookingList;

        switch (filterType) {
            case FilterType.HOTEL_NAME:
                return bookingList.stream()
                        .filter(booking -> booking.getHotel().getName().toLowerCase().contains(filterValue.toLowerCase()))
                        .collect(Collectors.toList());
            case FilterType.DATE:
                Date filterDate = DateUtils.getParsableDateFormat().parse(filterValue);
                return bookingList.stream()
                        .filter(booking -> DateUtils.isSameDay(booking.getCreated(), filterDate))
                        .collect(Collectors.toList());
            case FilterType.HOTEL_TYPE:
                return bookingList.stream()
                        .filter(booking -> booking.getHotel().getType().toLowerCase().equals(filterValue.toLowerCase()))
                        .collect(Collectors.toList());
        }
        return bookingList;
    }

    @Override
    public Booking addToCart(HttpSession session, Long roomId, Date checkInDate, Date checkoutDate) throws InvalidException {
//        Date date = null;
//        try {
//            date = DateUtils.getParsableDateFormat().parse(dateStr);
//        } catch (ParseException e) {
//            date = new Date();
//        }

        Room room = this.roomService.findOne(roomId);
        if (room == null) throw new InvalidException("/rooms/" + roomId, "");
        Booking booking = (Booking) session.getAttribute(SessionAttr.SESSION_BOOKING);
        if (booking == null || booking.getRoomList() == null || booking.getBookingDateList() == null) {
            booking = new Booking();
            booking.setRoomList(new ArrayList<>());
            booking.setBookingDateList(new ArrayList<>());
        }
        if (checkInDate == null) checkInDate = new Date();
        if (checkoutDate == null) checkoutDate = new Date();
        LocalDate start = checkInDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = checkoutDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        for (LocalDate localDate = start; localDate.isBefore(end); localDate = localDate.plusDays(1)) {
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            // validate booking // if user tries to book rooms from different hotels in a same booking restrict him from doing that shit.
            if (this.isBookingInvalid(booking, room))
                throw new InvalidException("/hotels/" + room.getHotel().getId(), "You can\'t book rooms from different hotels at the same time!");
            if (this.isDuplicateAttempt(booking, room, date))
                continue;

            booking.getRoomList().add(room);
            booking.getBookingDateList().add(date);
        }

        session.setAttribute(SessionAttr.SESSION_BOOKING, booking);
        return booking;
    }

    @Override
    public Booking getBookingFromSession(HttpSession session) throws SessionBookingNotFoundException {
        Booking booking = (Booking) session.getAttribute(SessionAttr.SESSION_BOOKING);
        if (booking == null
                || booking.getRoomList() == null
                || booking.getRoomList().isEmpty()
                || booking.getBookingDateList() == null
                || booking.getBookingDateList().isEmpty())
            throw new SessionBookingNotFoundException("/hotels", "No items to book!");
        return booking;
    }

    @Override
    public Booking checkout(User currentUser, HttpSession session) throws SessionBookingNotFoundException {
        Booking booking = this.getBookingFromSession(session);
        // set hotel for booking
        Hotel hotel = booking.findOutHotel();
        booking.setHotel(hotel);
        // check if hotel list and booking date list are incompatible
        if (booking.getRoomList().size() != booking.getBookingDateList().size()) {
            session.removeAttribute(SessionAttr.SESSION_BOOKING);
            throw new SessionBookingNotFoundException("/hotels/" + booking.getHotel().getId(), "There\'s something wrong. Please try again later!");
        }
        if (!booking.isValid()) {
            session.removeAttribute(SessionAttr.SESSION_BOOKING);
            throw new SessionBookingNotFoundException("/hotels/" + booking.getHotel().getId(), "One or more room isn't available during this time. Please try again!");
        }
        // set user of booking
//        if (currentUser.hasAssignedRole(Constants.Roles.ROLE_HOTEL_ADMIN)) {
//            session.setAttribute(SessionAttr.SESSION_BOOKING, booking);
//            return "redirect:/bookings/checkout/assignUser";
//        }
        booking.setUser(currentUser);
        // Transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(booking.getTotalPayableCost());
        transaction.setDebit(true);
        transaction = this.transactionService.save(transaction);
        booking.setTransaction(transaction);
        booking.setApproved(hotel.isEnabled());

        booking = this.save(booking);
        session.removeAttribute(SessionAttr.SESSION_BOOKING);
        return booking;
    }


}
