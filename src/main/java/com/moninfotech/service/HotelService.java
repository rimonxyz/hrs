package com.moninfotech.service;

import com.moninfotech.domain.Hotel;

import java.util.List;

/**
 * Created by sayemkcn on 4/2/17.
 */
public interface HotelService {
    // returns all hotels paginated
    public List<Hotel> findAll(int page, int size);

    // save a hotel
    public Hotel save(Hotel hotel);

    // find a hotel by id
    public Hotel findOne(Long id);

    public void delete(Long id);
}
