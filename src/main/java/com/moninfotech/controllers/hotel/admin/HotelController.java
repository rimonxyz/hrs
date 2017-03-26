package com.moninfotech.controllers.hotel.admin;

import com.moninfotech.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by sayemkcn on 3/26/17.
 */
@Controller
@RequestMapping("/admin/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    private String all(@RequestParam(value = "page", required = false) Integer page, Model model) {
        if (page == null || page < 0) page = 0;
        model.addAttribute(hotelService.findAll(page, 10));
        return "hotel/all";
    }

}
