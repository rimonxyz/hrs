package com.moninfotech.controllers.subscribers;

import com.moninfotech.domain.Subscriber;
import com.moninfotech.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/subscribers")
public class SubscriberController {
    private final SubscriberService subscriberService;

    @Autowired
    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @GetMapping("")
    private String getAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                          Model model) {
        if (page == null || page < 0) page = 0;
        List<Subscriber> subscriberList = this.subscriberService.findAll(page).getContent();
        model.addAttribute(subscriberList);
        model.addAttribute("page",page);
        return "adminlte/fragments/subscriber/all";
    }

}
