package com.moninfotech.controllers.users;

import com.moninfotech.domain.User;
import com.moninfotech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Created by sayemkcn on 4/2/17.
 */
@Controller
@RequestMapping("/admin/users")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    private String allUsers(@RequestParam(value = "page", required = false) Integer page, Model model) {
        if (page == null || page < 0) page = 0;
        model.addAttribute("userList", this.userService.findAll(page, 10));
        return "users/admin/all";
    }

    // Edit
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    private String editPage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", this.userService.findOne(id));
        return "/users/admin/edit";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    private String edit(@ModelAttribute User user, BindingResult bindingResult,
                        @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) System.out.println(bindingResult.toString());
        user.setId(id);
        // set created date from previous entity for preventing from being null
        user.setCreated(this.userService.findOne(id).getCreated());
        user = this.userService.save(user);

        return "redirect:/admin/users?message=" + user.getName() + " is updated!";
    }

    // Delete
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    private String delete(@PathVariable("id") Long id) {
        this.userService.delete(id);
        return "redirect:/admin/users?message=User is deleted.";
    }

}
