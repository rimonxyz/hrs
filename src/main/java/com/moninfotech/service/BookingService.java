package com.moninfotech.service;

import com.moninfotech.domain.Booking;
import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.Room;
import com.moninfotech.domain.User;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by sayemkcn on 4/18/17.
 */
public interface BookingService {

    List<Booking> findByUser(User user, int page, int size);

    Booking findOne(Long id);

    Booking save(Booking booking);

    Long[] convertToIds(String jsonArray);

    Date[] getDates(String jsonArray);

    List<Booking> findAll(Long[] id);

    Collection<Booking> getBookingList(Hotel hotel, User user);

    boolean isDuplicateAttempt(Booking booking, Room room, Date date);

    List<Date> removeBookingDate(Booking booking, Long roomId);

    boolean isBookingInvalid(Booking booking, Room room);
}
