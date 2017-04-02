package com.moninfotech.service;

import com.moninfotech.domain.Hotel;
import com.moninfotech.repository.HotelRepository;
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
public class HotelService {
    @Autowired
    private HotelRepository hotelRepo;

    // returns all hotels paginated
    public List<Hotel> findAll(int page, int size) {
        return this.hotelRepo.findAll(new PageRequest(page, size, Sort.Direction.DESC, Constants.FIELD_ID)).getContent();
    }

    // save a hotel
    public Hotel save(Hotel hotel){
        return this.hotelRepo.save(hotel);
    }

    // find a hotel by id
    public Hotel findOne(Long id){
        return this.hotelRepo.findOne(id);
    }

    public void delete(Long id) {
        this.hotelRepo.delete(id);
    }
}
