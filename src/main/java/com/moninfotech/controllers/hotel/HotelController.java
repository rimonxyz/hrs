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

import java.util.List;

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
    private String all(@RequestParam(value = "page", required = false) Integer page,
                       @RequestParam(value = "sortBy", required = false) String soryBy,
                       @RequestParam(value = "isDesc", required = false) boolean isDesc,
                       @RequestParam(value = "filterType", required = false) String filterType,
                       @RequestParam(value = "filterValue", required = false) String filterValue,
                       Model model) {
        if (page == null || page < 0) page = 0;
        List<Hotel> hotelList = hotelService.findAll(page, 10, soryBy, isDesc);
        if (filterType != null && !filterType.isEmpty() && filterValue != null && !filterValue.isEmpty())
            hotelList = this.hotelService.filterHotels(hotelList, filterType, filterValue);
        model.addAttribute("isDesc", !isDesc);
        if (filterValue != null)
            model.addAttribute("filterValue", filterValue);
        model.addAttribute(hotelList);
        return "hotel/all";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    private String details(@PathVariable("id") Long id, Model model) {
        Hotel hotel = this.hotelService.findOne(id);
        if (hotel == null) return "redirect:/hotels?message=Hotel not found!";
        model.addAttribute(hotel);
        return "hotel/details";
    }

    // returns image with that entity id
    @RequestMapping(value = "/images/{id}", method = RequestMethod.GET)
    private ResponseEntity<byte[]> getImage(@PathVariable("id") Long id) {
        return new ResponseEntity<byte[]>(this.hotelService.findOne(id).getImage(), HttpStatus.OK);
    }
}
