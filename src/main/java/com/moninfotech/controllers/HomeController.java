package com.moninfotech.controllers;

import com.moninfotech.commons.Config;
import com.moninfotech.commons.Constants;
import com.moninfotech.commons.SortAttributes;
import com.moninfotech.commons.pojo.BookingHelper;
import com.moninfotech.domain.Booking;
import com.moninfotech.domain.User;
import com.moninfotech.domain.annotations.CurrentUser;
import com.moninfotech.service.BookingService;
import com.moninfotech.service.HotelService;
import com.moninfotech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sayemkcn on 3/26/17.
 */
@Controller
public class HomeController {

    private final UserService userService;

    private final HotelService hotelService;

    private final BookingService bookingService;

    @Autowired
    public HomeController(UserService userService, HotelService hotelService, BookingService bookingService) {
        this.userService = userService;
        this.hotelService = hotelService;
        this.bookingService = bookingService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    private String home(Model model) {
        model.addAttribute("areaList", this.hotelService.getAddressAreaAndUpazilaList());
        return "index";
    }

    @GetMapping("/dashboard")
    private String dashboard(@CurrentUser User currentUser, Model model) {
        model.addAttribute("bookingHelper", new BookingHelper());

        model.addAttribute("totalBookingList", this.bookingService.findBookings(currentUser,null,null));
        model.addAttribute("manualBookingList", this.bookingService.findFiltered(currentUser,true));
        model.addAttribute("autoBookingList", this.bookingService.findFiltered(currentUser,false));
        model.addAttribute("template", "fragments/dashboard/dashboard");
        return "adminlte/index";
    }

    // Register
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerPage() {
        return "adminlte/pages/register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@Valid @ModelAttribute User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) System.out.println(bindingResult.toString());
        // check if user already exists
        if (this.userService.findByEmail(user.getEmail()) != null)
            return "redirect:/register?message=User already registered!";
        // set default user
        List<String> defaultRoles = new ArrayList<>();
        if (user.getEmail().equals(Config.ADMIN_EMAIL))
            defaultRoles.add(Constants.Roles.ROLE_ADMIN);
        else
            defaultRoles.add(Constants.Roles.ROLE_USER);
        user.setRoles(defaultRoles);
        user = this.userService.save(user);
        return "redirect:/login?message=We have sent you an email. Please confirm your identity by clicking on the confirmation link.";
    }

    // login
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "adminlte/pages/login";
    }


}
