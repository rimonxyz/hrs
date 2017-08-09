package com.moninfotech.controllers.feedback.admin;

import com.moninfotech.domain.Feedback;
import com.moninfotech.service.FeedbackService;
import com.moninfotech.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by sayemkcn on 2/14/17.
 */
@Controller
@RequestMapping("/admin/feedback")
public class FeedbackAdminController {

    private final FeedbackService feedbackService;
    private final MailService mailService;

    @Autowired
    public FeedbackAdminController(FeedbackService feedbackService, MailService mailService) {
        this.feedbackService = feedbackService;
        this.mailService = mailService;
    }

    @GetMapping("")
    private String allFeedback(@RequestParam(value = "page", required = false) Integer page, Model model) {
        if (page == null || page < 0) page = 0;
        model.addAttribute("feedbackList", this.feedbackService.findAll(page, 20));
        model.addAttribute("page",page);
        model.addAttribute("template", "fragments/feedback/all");
        return "adminlte/index";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    private String singleFeedback(@PathVariable("id") Long id, Model model) {
        model.addAttribute("feedback", this.feedbackService.getOne(id));
        model.addAttribute("template", "fragments/feedback/details");
        return "adminlte/index";
    }

    @RequestMapping(value = "/{id}/response", method = RequestMethod.POST)
    private String sendResponse(@PathVariable("id") Long id,
                                @RequestParam("response") String responseMessage) {
        Feedback feedback = this.feedbackService.getOne(id);
        feedback.setResponse(responseMessage);
        feedback.setResponded(true);
        feedback = this.feedbackService.save(feedback);
        try {
//             send email
            this.mailService.sendEmail(feedback.getEmail(),"Feedback response from HotelsWave", feedback.getResponse());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "redirect:/admin/feedback/" + feedback.getId();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    private String deleteFeedback(@PathVariable("id") Long id) {
        this.feedbackService.delete(id);
        return "redirect:/admin/feedback";
    }

}