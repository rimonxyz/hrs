package com.moninfotech.service.impl;

import com.moninfotech.domain.Booking;
import com.moninfotech.repository.BookingRepository;
import com.moninfotech.service.BookingService;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
            // data json
            JSONArray jsonArray = new JSONArray(json);
            //id array json
            jsonArray = jsonArray.getJSONArray(0);
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
    public Date[] getDates(String json) {
        try {
            // data json
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() < 3)
                throw new IndexOutOfBoundsException("JSON INDEX OUT OF BOUND on getDates(String jsonArray) method.");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date[] dates = new Date[2];
            dates[0] = sdf.parse(jsonArray.getString(1));
            dates[1] = sdf.parse(jsonArray.getString(2));
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
}
