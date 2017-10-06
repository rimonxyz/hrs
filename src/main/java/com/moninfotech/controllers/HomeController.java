package com.moninfotech.controllers;

import com.moninfotech.commons.Config;
import com.moninfotech.commons.Constants;
import com.moninfotech.commons.SortAttributes;
import com.moninfotech.commons.pojo.BookingHelper;
import com.moninfotech.commons.utils.PasswordUtil;
import com.moninfotech.domain.*;
import com.moninfotech.domain.annotations.CurrentUser;
import com.moninfotech.exceptions.NotFoundException;
import com.moninfotech.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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
        model.addAttribute("areaList", this.hotelService.getAddressAreaAndUpazilaList());
        model.addAttribute("packageList", this.packageService.findAll(null, null));
        model.addAttribute("offerList", this.offerService.findAll(null, null));
        return "index";
    }

    @GetMapping("/dashboard")
    private String dashboard(@CurrentUser User currentUser,
                             @RequestParam(value = "hotelType", required = false, defaultValue = Hotel.Type.BOTH) String hotelType, Model model) {
        model.addAttribute("bookingHelper", new BookingHelper());

        if (currentUser.hasAssignedRole(Constants.Roles.ROLE_HOTEL_ADMIN)) {
            Hotel hotel = this.hotelService.findByUser(currentUser);
            if (hotel != null)
                model.addAttribute("hotelName", hotel.getName());
        }
        model.addAttribute("totalBookingList", this.bookingService.findBookings(currentUser, false, true, null, null));
        model.addAttribute("manualBookingList", this.bookingService.findFiltered(currentUser, true, hotelType));
        model.addAttribute("autoBookingList", this.bookingService.findFiltered(currentUser, false, hotelType));

        // Total Visitors
        Activity firstActivity = this.activityService.findFirst();
        model.addAttribute("totalVisitors", firstActivity != null ? firstActivity.getTotalVisitors() : 0L);

        return "adminlte/fragments/dashboard/dashboard";
    }

    // Register
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerPage() {
        return "adminlte/pages/register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@Valid @ModelAttribute User user, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) System.out.println(bindingResult.toString());
        // check if user already exists
        if (this.userService.findByEmail(user.getEmail()) != null)
            return "redirect:/register?message=User already registered!";
        // set default user
        if (user.getEmail().equals(Config.ADMIN_EMAIL1)
                || user.getEmail().equals(Config.ADMIN_EMAIL2)
                || user.getEmail().equals(Config.ADMIN_EMAIL3))
            user.grantRole(Constants.Roles.ROLE_ADMIN);
        else
            user.grantRole(Constants.Roles.ROLE_USER);
        user.setPassword(PasswordUtil.encryptPassword(user.getPassword(), PasswordUtil.EncType.BCRYPT_ENCODER, null));
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
        acToken.setReason("User Registration");
        this.userService.save(user, acToken);
        return "redirect:/login?messageinfo=Account \"" + user.getName() + "\" is Activated. Please logged in to continue.";
    }

    @GetMapping("/resetPassword")
    private String resetPassword(@RequestParam(value = "token", required = false) String token, HttpSession session) {
        if (token == null || token.isEmpty()) // return email page to send password reset link
            return "adminlte/pages/resetPassword";
        AcValidationToken acValidationToken = this.acValidationTokenService.findByToken(token);
        if (acValidationToken == null || !acValidationToken.isTokenValid())
            return "redirect:/login?message=Invalid request!";
        session.setAttribute("passwordResetToken", token);
        return "adminlte/pages/newPassword";
    }

    @PostMapping("/resetPassword")
    private String resetPasswordConfirm(@RequestParam("password") String password, HttpSession session) throws Exception {
        String token = (String) session.getAttribute("passwordResetToken");
        AcValidationToken acValidationToken = this.acValidationTokenService.findByToken(token);
        if (acValidationToken == null || !acValidationToken.isTokenValid())
            return "redirect:/login?message=Invalid Request!";
        if (password.length() < 6)
            return "redirect:/resetPassword?token=" + token + "&message=Password length must be at least 6 characters!";
        User user = acValidationToken.getUser();
        user.setPassword(PasswordUtil.encryptPassword(password, PasswordUtil.EncType.BCRYPT_ENCODER, null));
        acValidationToken.setTokenValid(false);
        acValidationToken.setReason("Password Reset");
        this.userService.save(user, acValidationToken);
        return "redirect:/login?messagesuccess=Password reset successful!";
    }

    @PostMapping("/resetPassword/verifyEmail")
    private String verifyEmail(@RequestParam("email") String email) {
        User user = this.userService.findByEmail(email);
        if (user == null)
            return "redirect:/resetPassword?message=User is not registered with this email. Please register to continue!";
        this.userService.requireAccountValidationByEmail(user, "/resetPassword");
        return "redirect:/resetPassword?messageinfo=A verification link has been sent to your email. Please click that link to confirm your identification.";
    }

    @GetMapping("/extest")
    private String testEx() throws NotFoundException {
        throw new NotFoundException();
    }

}
