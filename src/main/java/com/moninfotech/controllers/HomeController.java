package com.moninfotech.controllers;

import com.moninfotech.domain.User;
import com.moninfotech.service.HotelService;
import com.moninfotech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    @Autowired
    public HomeController(UserService userService, HotelService hotelService) {
        this.userService = userService;
        this.hotelService = hotelService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    private String home(Model model) {
        model.addAttribute("areaList", this.hotelService.getAddressAreaList());
        return "index";
    }


    // Register
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerPage() {
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@Valid @ModelAttribute User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) System.out.println(bindingResult.toString());
        // check if user already exists
        if (this.userService.findByEmail(user.getEmail()) != null)
            return "redirect:/register?message=User already registered!";
        // set default user
        List<String> defaultRoles = new ArrayList<>();
        defaultRoles.add("ROLE_USER");
        user.setRoles(defaultRoles);
        user = this.userService.save(user);
        return "redirect:/login?message=We have sent you an email. Please confirm your identity by clicking on the confirmation link.";
    }

    // login
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "login";
    }


}
