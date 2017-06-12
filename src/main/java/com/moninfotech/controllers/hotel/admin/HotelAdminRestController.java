package com.moninfotech.controllers.hotel.admin;

import com.moninfotech.domain.Hotel;
import com.moninfotech.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by sayemkcn on 3/26/17.
 */
@Controller
@RequestMapping("/rest/admin/hotels")
public class HotelAdminRestController {

    private final HotelService hotelService;

    @Autowired
    public HotelAdminRestController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/{id}")
    private ResponseEntity<Hotel> getHotel(@PathVariable("id") Long id) {
        if (id == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(this.hotelService.findOne(id), HttpStatus.OK);
    }
}
