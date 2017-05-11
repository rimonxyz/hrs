package com.moninfotech.controllers.search;

import com.moninfotech.domain.Hotel;
import com.moninfotech.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
                          @RequestParam(value = "isDesc", required = false) boolean isDesc,
                          Model model) {

        List<Hotel> hotels = this.hotelService.findByAddressArea(location);
        hotels.addAll(this.hotelService.findByAddressUpazila(location));
//        // filter hotels out that are already booked
//        hotels = this.hotelService.filterUnbookedHotelsByDate(hotels, startDate, endDate);
        model.addAttribute("hotelList", hotels);
        model.addAttribute("areaList", this.hotelService.getAddressAreaAndUpazilaList());
        model.addAttribute("isDesc", !isDesc);
        return "index";
    }

}
