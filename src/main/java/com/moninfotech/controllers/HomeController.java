package com.moninfotech.controllers;

import com.moninfotech.domain.User;
import com.moninfotech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by sayemkcn on 3/26/17.
 */
@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    private String home() {
        return "index";
    }


    // Register
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerPage() {
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@ModelAttribute User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) System.out.println(bindingResult.toString());
        user = this.userService.save(user);
        return "redirect:/login?message=We have sent you an email. Please confirm your identity by clicking on the confirmation link.";
    }

    // login
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String loginPage(){
        return "login";
    }


}
