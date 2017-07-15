package com.moninfotech.service;

import com.moninfotech.domain.Booking;
import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.Room;
import com.moninfotech.domain.User;
import com.moninfotech.domain.annotations.CurrentUser;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by sayemkcn on 4/18/17.
 */
public interface BookingService {

    List<Booking> findByUser(User user, int page, int size);

    List<Booking> findByHotel(Hotel hotel,int page,int size);

    Booking findOne(Long id);

    List<Booking> findAll(int page,int size);

    boolean belongsTo(Booking booking,User user);

    Booking save(Booking booking);

    Long[] convertToIds(String jsonArray);

    Date[] getDates(String jsonArray);

    List<Booking> findAll(Long[] id);

    // Filter booking list by user on specific hotel
    Collection<Booking> getBookingList(Hotel hotel, User user);

    boolean isDuplicateAttempt(Booking booking, Room room, Date date);

    List<Date> removeBookingDate(Booking booking, Long roomId);

    boolean isBookingInvalid(Booking booking, Room room);

    List<Booking> findBookings(User currentUser, Integer page, Integer size);
}
