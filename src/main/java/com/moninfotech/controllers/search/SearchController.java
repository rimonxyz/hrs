package com.moninfotech.controllers.search;

import com.moninfotech.domain.Hotel;
import com.moninfotech.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by sayemkcn on 4/21/17.
 */
@Controller
@RequestMapping(value = "/search")
public class SearchController {

    @Autowired
    private HotelService hotelService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    private String search(@RequestParam("location") String location,
                               @RequestParam("startDate") Date startDate,
                               @RequestParam("endDate") Date endDate,
                               Model model) {

        List<Hotel> hotels = this.hotelService.findByAddressDistrict(location);
        hotels = this.hotelService.filterUnbookedHotelsByDate(hotels, startDate, endDate);
        model.addAttribute("hotelList", hotels);
        return "index";
    }

}
