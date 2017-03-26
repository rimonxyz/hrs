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
}
