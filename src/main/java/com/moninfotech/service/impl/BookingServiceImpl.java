package com.moninfotech.service.impl;

import com.moninfotech.commons.DateUtils;
import com.moninfotech.domain.Booking;
import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.Room;
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
import java.util.*;

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
        if (booking!=null && booking.getBookingDateList() != null && booking.getBookingDateList() != null) {
            for (int i = 0; i < booking.getRoomList().size() && i < booking.getBookingDateList().size(); i++) {
                if (booking.getRoomList().get(i).getId().equals(room.getId()) && DateUtils.isSameDay(booking.getBookingDateList().get(i),date))
                    return true;
            }
        }
        return false;
    }


}
