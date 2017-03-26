package com.moninfotech.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by sayemkcn on 3/26/17.
 */
@Controller
public class HomeController {

    @RequestMapping(value = "",method = RequestMethod.GET)
    private String home(){
        return "index";
    }

}
