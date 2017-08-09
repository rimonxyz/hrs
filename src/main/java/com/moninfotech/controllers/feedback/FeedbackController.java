package com.moninfotech.controllers.feedback;

import com.moninfotech.domain.Feedback;
import com.moninfotech.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("/feedback")
@PropertySource("classpath:messages.properties")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @Value("${message.feedbackReceivedMessage}")
    private String feedbackReceivedMessage;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("")
    private String feedbackPage(Model model) {
        model.addAttribute("template", "fragments/feedback/create");
        return "adminlte/index";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    private String saveFeedback(@Valid @ModelAttribute Feedback feedback, BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) System.out.println(bindingResult.toString());
        System.out.println(feedback.toString());
        feedback = this.feedbackService.save(feedback);
        if (feedback != null)
            return "redirect:/?message=" + this.feedbackReceivedMessage;
        model.addAttribute("template", "fragments/dashboard/dashboard");
        return "adminlte/index";
    }

}