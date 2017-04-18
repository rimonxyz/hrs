package com.moninfotech.controllers.hotel;

import com.moninfotech.domain.Hotel;
import com.moninfotech.service.HotelService;
import com.moninfotech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by sayemkcn on 4/16/17.
 */
@Controller
@RequestMapping(value = "/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;
    @Autowired
    private UserService userService;

    // Get All Hotels paginated
    @RequestMapping(value = "", method = RequestMethod.GET)
    private String all(@RequestParam(value = "page", required = false) Integer page, Model model) {
        if (page == null || page < 0) page = 0;
        model.addAttribute(hotelService.findAll(page, 10));
        return "hotel/all";
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    private String details(@PathVariable("id") Long id,Model model){
        Hotel hotel = this.hotelService.findOne(id);
        if (hotel==null) return "redirect:/hotels?message=Hotel not found!";
        model.addAttribute(hotel);
        return "hotel/details";
    }

    // returns image with that entity id
    @RequestMapping(value = "/images/{id}", method = RequestMethod.GET)
    private ResponseEntity<byte[]> getImage(@PathVariable("id") Long id) {
        return new ResponseEntity<byte[]>(this.hotelService.findOne(id).getImage(), HttpStatus.OK);
    }
}
