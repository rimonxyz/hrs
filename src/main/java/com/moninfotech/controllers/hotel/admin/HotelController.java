package com.moninfotech.controllers.hotel.admin;

import com.moninfotech.domain.Hotel;
import com.moninfotech.service.HotelService;
import com.utils.ImageValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

    // Get All Hotels paginated
    @RequestMapping(value = "", method = RequestMethod.GET)
    private String all(@RequestParam(value = "page", required = false) Integer page, Model model) {
        if (page == null || page < 0) page = 0;
        model.addAttribute(hotelService.findAll(page, 10));
        return "hotel/all";
    }


    // Create a hotel by Admin
    // @GET
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    private String createPage() {
        return "hotel/create";
    }

    //@POST
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    private String create(@ModelAttribute Hotel hotel, BindingResult bindingResult,
                          @RequestParam("image") MultipartFile multipartFile) throws IOException {
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

    // Edit hotel informations by admin
    //@GET
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    private String editPage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("hotel", hotelService.findOne(id));
        return "hotel/edit";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    private String editPage(@ModelAttribute Hotel hotel, BindingResult bindingResult,
                            @PathVariable("id") Long id,
                            @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (bindingResult.hasErrors())
            System.out.println(bindingResult.toString());
        // set existing id to update
        hotel.setId(id);

        Hotel existingHotel = this.hotelService.findOne(id);
        if (existingHotel != null){
            // set created date from previous entity
            hotel.setCreated(existingHotel.getCreated());
            // if no image is uploaded the set previous image if available
            if (multipartFile == null) hotel.setImage(existingHotel.getImage());
        }
        // check if image is valid
        if (ImageValidator.isImageValid(multipartFile))
            hotel.setImage(multipartFile.getBytes());
            // else return with error message
        else return "redirect:/admin/hotels/edit/" + id + "?message=Image is invalid!";
        hotel = this.hotelService.save(hotel);
        return "redirect:/admin/hotels?message=" + hotel.getName() + " informations are updated!";
    }

    // Delete entity
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.POST)
    private String delete(@PathVariable Long id){
        this.hotelService.delete(id);
        return "redirect:/admin/hotels?message=Successfully deleted!";
    }

    // returns image with that entity id
    @RequestMapping(value = "/images/{id}", method = RequestMethod.GET)
    private ResponseEntity<byte[]> getImage(@PathVariable("id") Long id) {
        return new ResponseEntity<byte[]>(this.hotelService.findOne(id).getImage(), HttpStatus.OK);
    }
}
