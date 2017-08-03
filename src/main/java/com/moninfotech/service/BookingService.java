package com.moninfotech.service;

import com.moninfotech.domain.Booking;
import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.Room;
import com.moninfotech.domain.User;
import com.moninfotech.domain.annotations.CurrentUser;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by sayemkcn on 4/18/17.
 */
public interface BookingService {

    List<Booking> findByUser(User user, Integer page, Integer size);

    List<Booking> findByHotel(Hotel hotel, Integer page, Integer size);

    Booking findOne(Long id);

    List<Booking> findAll(Integer page, Integer size);

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
    List<Booking> findBookings(User currentUser, Integer page, Integer size);

    List<Booking> findFiltered(User currentUser, boolean isManual);
}
