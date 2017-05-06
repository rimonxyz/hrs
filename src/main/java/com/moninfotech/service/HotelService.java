package com.moninfotech.service;

import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.User;

import java.util.Date;
import java.util.List;

/**
 * Created by sayemkcn on 4/2/17.
 */
public interface HotelService {
    // returns all hotels paginated
    List<Hotel> findAll(int page, int size, String sortBy, boolean isDesc);

    // save a hotel
    Hotel save(Hotel hotel);

    // find a hotel by id
    Hotel findOne(Long id);

    void delete(Long id);

    Hotel findByUser(User user);

    List<Hotel> findByAddressArea(String district);

    List<Hotel> filterUnbookedHotelsByDate(List<Hotel> hotels, Date startDate, Date endDate);

    List<String> getAddressAreaList();
}
