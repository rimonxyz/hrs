package com.moninfotech.service.impl;

import com.moninfotech.domain.Booking;
import com.moninfotech.repository.BookingRepository;
import com.moninfotech.service.BookingService;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sayemkcn on 4/18/17.
 */
@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingRepository bookingRepo;

    @Override
    public Booking save(Booking booking) {
        return this.bookingRepo.save(booking);
    }

    @Override
    public synchronized Long[] convertToIds(String json) {
        try {
            JSONArray jsonArray = new JSONArray(json);
            Long[] ids = new Long[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                ids[i] = jsonArray.getLong(i);
            }
            return ids;
        } catch (Exception e) {
            throw new JSONException(e);
        }
    }

    @Override
    public List<Booking> findAll(Long[] ids) {
        return this.bookingRepo.findAll(Arrays.asList(ids));
    }
}
