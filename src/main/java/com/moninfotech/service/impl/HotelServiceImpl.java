package com.moninfotech.service.impl;

import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.User;
import com.moninfotech.repository.HotelRepository;
import com.moninfotech.service.HotelService;
import com.moninfotech.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sayemkcn on 3/26/17.
 */
@Service
public class HotelServiceImpl implements HotelService {
    @Autowired
    private HotelRepository hotelRepo;

    // returns all hotels paginated
    @Override
    public List<Hotel> findAll(int page, int size, String sortBy, boolean isDesc) {
        if (sortBy == null || sortBy.isEmpty()) sortBy = Constants.FIELD_ID;
        if (isDesc)
            return this.hotelRepo.findAll(new PageRequest(page, size, Sort.Direction.DESC, sortBy)).getContent();
        return this.hotelRepo.findAll(new PageRequest(page, size, Sort.Direction.ASC, sortBy)).getContent();
    }

    // save a hotel
    @Override
    public Hotel save(Hotel hotel) {
        return this.hotelRepo.save(hotel);
    }

    // find a hotel by id
    @Override
    public Hotel findOne(Long id) {
        return this.hotelRepo.findOne(id);
    }

    @Override
    public void delete(Long id) {
        this.hotelRepo.delete(id);
    }

    @Override
    public Hotel findByUser(User user) {
        return this.hotelRepo.findByUser(user);
    }

    @Override
    public List<Hotel> findByAddressDistrict(String district) {
        return this.hotelRepo.findByAddressDistrict(district);
    }

    @Override
    public List<Hotel> filterUnbookedHotelsByDate(List<Hotel> hotels, Date startDate, Date endDate) {
        List<Hotel> hotelList = new ArrayList<>();
        for (Hotel hotel : hotels) {
            if (hotel.hasUnbookedRoom(startDate, endDate))
                hotelList.add(hotel);
        }
        return hotelList;
    }

    @Override
    public List<String> getAddressAreaList() {
        List<String> areaList = new ArrayList<>();
        this.hotelRepo.findAll().forEach(hotel -> {
            areaList.add(hotel.getAddress().getArea());
        });
        return areaList;
    }
}
