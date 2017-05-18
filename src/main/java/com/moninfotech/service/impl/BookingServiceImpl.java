package com.moninfotech.service.impl;

import com.moninfotech.domain.Booking;
import com.moninfotech.domain.User;
import com.moninfotech.repository.BookingRepository;
import com.moninfotech.service.BookingService;
import com.moninfotech.utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public List<Booking> findByUser(User user, int page, int size) {
        return this.bookingRepo.findByUser(user, new PageRequest(page, size, Sort.Direction.DESC, Constants.FIELD_ID)).getContent();
    }

    @Override
    public Booking findOne(Long id) {
        return this.bookingRepo.findOne(id);
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
                for (int i=0;i<splittedIds.length;i++){
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


}
