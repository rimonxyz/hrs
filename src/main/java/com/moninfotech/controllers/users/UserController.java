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

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    private String allUsers(@RequestParam(value = "page", required = false) Integer page, Model model) {
        if (page == null || page < 0) page = 0;
        model.addAttribute("userList", this.userService.findAll(page, 10));
        model.addAttribute("template", "fragments/user/all");
        return "adminlte/index";
    }

    // Edit
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    private String editPage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", this.userService.findOne(id));
        model.addAttribute("template", "fragments/user/profile");
        return "adminlte/index";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    private String edit(@ModelAttribute User user, BindingResult bindingResult,
                        @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) System.out.println(bindingResult.toString());

        User existingUser = this.userService.findOne(user.getId());
        if (existingUser==null) return "redirect:/users?message=User not found!";
        existingUser.setName(user.getName());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setAddress(user.getAddress());

        user = this.userService.save(existingUser);

        return "redirect:/admin/users?messageinfo=" + user.getName() + " is updated!";
    }
//
//    // Delete
//    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
//    private String delete(@PathVariable("id") Long id) {
//        this.userService.delete(id);
//        return "redirect:/admin/users?message=User is deleted.";
//    }

    // Disable user
    @RequestMapping(value = "/{id}/action", method = RequestMethod.POST)
    private String disable(@PathVariable("id") Long id, @RequestParam("enabled") Boolean enabled) {
        User user = this.userService.findOne(id);
        if (user==null) return "redirect:/admin/users?message=User can not be found!";
        user.setEnabled(enabled);
        user = this.userService.save(user);
        return "redirect:/admin/users?message="+user.getName()+" updated!";
    }

}
