package com.moninfotech.controllers.hotel.admin;

import com.moninfotech.domain.Hotel;
import com.moninfotech.service.HotelService;
import com.utils.ImageValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    private String createPage() {
        return "hotel/create";
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    private String create(@ModelAttribute Hotel hotel, BindingResult bindingResult,
                          @RequestParam("image")MultipartFile multipartFile) throws IOException {
        if (bindingResult.hasErrors())
            System.out.print("Binding ERROR: " + bindingResult.toString());
        // set image to the hotel entity if it's valid.
        if (ImageValidator.isImageValid(multipartFile))
            hotel.setImage(multipartFile.getBytes());
        /*
         * SET USER INFORMATIONS WITH HOTEL ENTITY BEFORE SAVING
         */
        hotel = this.hotelService.save(hotel);
        return "redirect:/admin/hotels?message=" + hotel.getName() + " is saved.";
    }

}
