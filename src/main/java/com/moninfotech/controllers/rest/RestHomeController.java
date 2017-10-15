package com.moninfotech.controllers.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class RestHomeController {

    @GetMapping("/home")
    private String getHome() {
        return "Hello Home!";
    }

}
