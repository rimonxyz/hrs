package com.moninfotech.controllers.hotel.admin;

import com.moninfotech.commons.pojo.Roles;
import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.User;
import com.moninfotech.service.HotelService;
import com.moninfotech.service.UserService;
import com.moninfotech.utils.ImageValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sayemkcn on 3/26/17.
 */
@Controller
@RequestMapping("/admin/hotels")
public class HotelAdminController {

    @Autowired
    private HotelService hotelService;
    @Autowired
    private UserService userService;

    // Get All Hotels paginated
    @RequestMapping(value = "", method = RequestMethod.GET)
    private String all(@RequestParam(value = "page", required = false) Integer page,
                       @RequestParam(value = "sortBy",required = false) String soryBy,
                       @RequestParam(value = "desc",required = false) boolean isDesc,
                       Model model) {
        if (page == null || page < 0) page = 0;
        model.addAttribute(hotelService.findAll(page, 10,soryBy,isDesc));
        return "hotel/admin/all";
    }


    // Create a hotel by Admin
    // @GET
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    private String createPage() {
        return "hotel/admin/create";
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

        // first save user
        User user = this.userService.save(hotel.getUser());
        // save hotel address as user address by default
        user.setAddress(hotel.getAddress());
        // set default role for this user. in this case :ROLE_HOTEL
        List<String> roles = new ArrayList<>();
        roles.add(Roles.ROLE_HOTEL);
        user.setRoles(roles);
        // set saved user (with persisted id) to hotel
        hotel.setUser(user);
        // then save hotel
        hotel = this.hotelService.save(hotel);
        return "redirect:/admin/hotels?message=" + hotel.getName() + " is saved.";
    }

    // Edit hotel informations by admin
    //@GET
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    private String editPage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("hotel", hotelService.findOne(id));
        return "hotel/admin/edit";
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
        if (existingHotel != null) {
            // set created date from previous entity
            hotel.setCreated(existingHotel.getCreated());
            // set previous user to hotel entity
            hotel.setUser(existingHotel.getUser());
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
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    private String delete(@PathVariable Long id) {
        this.hotelService.delete(id);
        return "redirect:/admin/hotels?message=Successfully deleted!";
    }

    // returns image with that entity id
    @RequestMapping(value = "/images/{id}", method = RequestMethod.GET)
    private ResponseEntity<byte[]> getImage(@PathVariable("id") Long id) {
        return new ResponseEntity<byte[]>(this.hotelService.findOne(id).getImage(), HttpStatus.OK);
    }

    // disable user of a hotel
    @RequestMapping(value = "/{id}/action", method = RequestMethod.POST)
    private String disable(@PathVariable("id") Long id, @RequestParam("enabled") Boolean enabled) {
        Hotel hotel = this.hotelService.findOne(id);
        if (hotel == null || hotel.getUser() == null) return "redirect:/admin/hotels?message=User can not be found!";
        hotel.getUser().setEnabled(enabled);
        hotel = this.hotelService.save(hotel);
        return "redirect:/admin/hotels?message=" + hotel.getName() + " updated!";
    }

}
