package com.moninfotech.service;

import com.moninfotech.domain.Booking;
import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.Room;
import com.moninfotech.domain.User;
import com.moninfotech.exceptions.invalid.InvalidException;
import com.moninfotech.exceptions.notfound.SessionBookingNotFoundException;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by sayemkcn on 4/18/17.
 */
public interface BookingService {

    List<Booking> findByUser(User user,boolean isCanceled, boolean isConfirmed, Integer page, Integer size);

    List<Booking> findByHotel(Hotel hotel,boolean isCanceled, boolean isConfirmed, Integer page, Integer size);

    Booking findOne(Long id);

    List<Booking> findAll(boolean isCanceled,boolean isConfirmed, boolean isApproved, Integer page, Integer size);

    boolean belongsTo(Booking booking, User user);

    Booking save(Booking booking);

    Long[] convertToIds(String jsonArray);

    Date[] getDates(String jsonArray);

    List<Booking> findAll(Long[] id);

    // Filter booking list by user on specific hotel
    Collection<Booking> getBookingList(Hotel hotel, User user);

    boolean isDuplicateAttempt(Booking booking, Room room, Date date);

    List<Date> removeBookingDate(Booking booking, Long roomId);

    boolean isBookingInvalid(Booking booking, Room room);

    /**
     * Returns booking list according to logged in user. For user, returns their booking list, for hotel, returns booking list that was placed
     * on that hotel, for super admin returns all booking items
     *
     * @param currentUser
     * @param page
     * @param size
     * @return List<Booking>
     */
    List<Booking> findBookings(User currentUser,boolean isCanceled, boolean isConfirmed, boolean isApproved, Integer page, Integer size);

    // returns filtered booking list by booking creation type
    List<Booking> findFiltered(User currentUser, boolean isManual, String hotelType);

    // returns all of the booking list which order is placed on provided day
    List<Room> findFilteredRoomListByPlacementDate(User currentUser, Date date, String analFlagRole);

    // returns all of the booking list (Distinct) which order is placed on provided day
    List<Room> findFilteredRoomListByPlacementDateDistinct(User currentUser, Date date, String analFlagRole);

    // returns all rooms that are booked on provided day
    List<Room> findFilteredRoomList(User currentUser, Date date, String analFlagRole);

    List<Booking> filterBookingList(List<Booking> bookingList, String filterType, String filterValue) throws ParseException;

    Booking addToCart(HttpSession session,Long roomId,Date checkInDate,Date checkoutDate) throws InvalidException;

    Booking getBookingFromSession(HttpSession session) throws SessionBookingNotFoundException;

    Booking checkout(User currentUser, HttpSession session) throws SessionBookingNotFoundException;
}
