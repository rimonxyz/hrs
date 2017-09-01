package com.moninfotech.controllers;

import com.moninfotech.commons.Config;
import com.moninfotech.commons.Constants;
import com.moninfotech.commons.SortAttributes;
import com.moninfotech.commons.pojo.BookingHelper;
import com.moninfotech.domain.*;
import com.moninfotech.domain.annotations.CurrentUser;
import com.moninfotech.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    private final PackageService packageService;

    private final OfferService offerService;

    private final AcValidationTokenService acValidationTokenService;

    private final ActivityService activityService;

    @Autowired
    public HomeController(UserService userService, HotelService hotelService, BookingService bookingService, PackageService packageService, OfferService offerService, AcValidationTokenService acValidationTokenService, ActivityService activityService) {
        this.userService = userService;
        this.hotelService = hotelService;
        this.bookingService = bookingService;
        this.packageService = packageService;
        this.offerService = offerService;
        this.acValidationTokenService = acValidationTokenService;
        this.activityService = activityService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    private String home(Model model) {
        Activity firstActivity = this.activityService.findFirst();

        model.addAttribute("areaList", this.hotelService.getAddressAreaAndUpazilaList());
        model.addAttribute("packageList", this.packageService.findAll(null, null));
        model.addAttribute("offerList", this.offerService.findAll(null, null));
        model.addAttribute("totalVisitors", firstActivity != null ? firstActivity.getTotalVisitors() : 0L);
        return "index";
    }

    @GetMapping("/dashboard")
    private String dashboard(@CurrentUser User currentUser,
                             @RequestParam(value = "hotelType", required = false, defaultValue = Hotel.Type.BOTH) String hotelType, Model model) {
        model.addAttribute("bookingHelper", new BookingHelper());

        model.addAttribute("totalBookingList", this.bookingService.findBookings(currentUser, false, true, null, null));
        model.addAttribute("manualBookingList", this.bookingService.findFiltered(currentUser, true, hotelType));
        model.addAttribute("autoBookingList", this.bookingService.findFiltered(currentUser, false, hotelType));

        return "adminlte/fragments/dashboard/dashboard";
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
        return "redirect:/login?messageinfo=We have sent you an email. Please confirm your identity by clicking on the confirmation link.";
    }

    // login
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "adminlte/pages/login";
    }

    @GetMapping("/user/validation")
    private String validateUser(@RequestParam("token") String validationToken,
                                @RequestParam("enabled") boolean enabled) {
        AcValidationToken acToken = this.acValidationTokenService.findByToken(validationToken);
        if (acToken == null || !acToken.isTokenValid()) return "redirect:/login?message=Confirmation link is invalid!";
        User user = acToken.getUser();
        user.setEnabled(enabled);
        acToken.setTokenValid(false);
        this.userService.save(user);
        this.acValidationTokenService.save(acToken);
        return "redirect:/login?messageinfo=Account \"" + user.getName() + "\" is Activated. Please logged in to continue.";
    }

}
