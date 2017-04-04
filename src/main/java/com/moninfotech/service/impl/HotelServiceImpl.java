package com.moninfotech.service.impl;

import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.User;
import com.moninfotech.repository.HotelRepository;
import com.moninfotech.service.HotelService;
import com.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sayemkcn on 3/26/17.
 */
@Service
public class HotelServiceImpl implements HotelService{
    @Autowired
    private HotelRepository hotelRepo;

    // returns all hotels paginated
    @Override
    public List<Hotel> findAll(int page, int size) {
        return this.hotelRepo.findAll(new PageRequest(page, size, Sort.Direction.DESC, Constants.FIELD_ID)).getContent();
    }

    // save a hotel
    @Override
    public Hotel save(Hotel hotel){
        return this.hotelRepo.save(hotel);
    }

    // find a hotel by id
    @Override
    public Hotel findOne(Long id){
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
}
