package com.moninfotech.service;

import com.moninfotech.commons.pojo.ParamFacilities;
import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.User;
import com.moninfotech.exceptions.NotFoundException;
import com.moninfotech.exceptions.invalid.InvalidException;

import java.util.Date;
import java.util.List;

/**
 * Created by sayemkcn on 4/2/17.
 */
public interface HotelService {
    // returns all hotels paginated
    List<Hotel> findAll();
    List<Hotel> findAll(int page, int size, String sortBy, boolean isDesc);

    // save a hotel
    Hotel save(Hotel hotel) throws InvalidException;

    // find a hotel by id
    Hotel findOne(Long id);

    Hotel getOne(Long id) throws NotFoundException;

    void delete(Long id);

    Hotel findByUser(User user);

    List<Hotel> searchHotel(String query,int page);

    List<Hotel> findByAddressArea(String district,int page);

    List<Hotel> findByAddressUpazila(String location,int page);

    List<Hotel> filterUnbookedHotelsByDate(List<Hotel> hotels, Date date);

    List<String> getAddressAreaAndUpazilaList();

    List<Hotel> filterHotels(List<Hotel> hotelList, String filterType, String filterValue);

    List<Hotel> filterHotels(List<Hotel> hotelList, String type);

    List<Hotel> findByNameContaining(String query,int page);

    List<Hotel> filter(String query, String star, String price, int rating, ParamFacilities facilities);

    void softDelete(Long hotelId) throws NotFoundException, InvalidException;
}
