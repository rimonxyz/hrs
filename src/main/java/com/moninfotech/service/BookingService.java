package com.moninfotech.service;

import com.moninfotech.domain.Booking;

import java.util.List;

/**
 * Created by sayemkcn on 4/18/17.
 */
public interface BookingService {

    Booking save(Booking booking);

    Long[] convertToIds(String jsonArray);

    List<Booking> findAll(Long[] id);
}
