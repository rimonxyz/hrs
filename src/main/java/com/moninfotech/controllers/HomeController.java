package com.moninfotech.controllers;

import com.moninfotech.commons.Config;
import com.moninfotech.commons.Constants;
import com.moninfotech.commons.SessionAttr;
import com.moninfotech.commons.pojo.BookingHelper;
import com.moninfotech.commons.utils.PasswordUtil;
import com.moninfotech.commons.validators.Validator;
import com.moninfotech.domain.*;
import com.moninfotech.domain.annotations.CurrentUser;
import com.moninfotech.exceptions.NotFoundException;
import com.moninfotech.exceptions.nullexceptions.NullPasswordException;
import com.moninfotech.repository.SubscriberRepository;
import com.moninfotech.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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

    private final Validator emailValidator;
    private final Validator phoneValidator;

    private final SubscriberRepository subscriberRepo;

    private final MailService mailService;

    @Value("${shutdown.username}")
    private String sdUsername;
    @Value("${shutdown.password}")
    private String sdPassword;

    @Autowired
    public HomeController(UserService userService, HotelService hotelService, BookingService bookingService, PackageService packageService, OfferService offerService, AcValidationTokenService acValidationTokenService, ActivityService activityService, @Qualifier("phoneValidator") Validator phoneValidator, @Qualifier("emailValidator") Validator emailValidator, SubscriberRepository subscriberRepo, MailService mailService) {
        this.userService = userService;
        this.hotelService = hotelService;
        this.bookingService = bookingService;
        this.packageService = packageService;
        this.offerService = offerService;
        this.acValidationTokenService = acValidationTokenService;
        this.activityService = activityService;
        this.phoneValidator = phoneValidator;
        this.emailValidator = emailValidator;
        this.subscriberRepo = subscriberRepo;
        this.mailService = mailService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    private String home(Model model) {
        model.addAttribute("areaList", this.hotelService.getAddressAreaAndUpazilaList());
        model.addAttribute("packageList", this.packageService.findAll(0, 3));
        model.addAttribute("offerList", this.offerService.findAll(0, 3));
        return "index";
    }
    @GetMapping("/test")
    private String testPage(Model model){
        model.addAttribute("areaList", this.hotelService.getAddressAreaAndUpazilaList());
        model.addAttribute("packageList", this.packageService.findAll(0, 3));
        model.addAttribute("offerList", this.offerService.findAll(0, 3));
        return "test";
    }
    @GetMapping("/whyUs")
    private String whyUs(Model model) {
        return "whyUs";
    }


    @GetMapping("/dashboard")
    private String dashboard(@CurrentUser User currentUser,
                             @RequestParam(value = "hotelType", required = false, defaultValue = Hotel.Type.BOTH) String hotelType,
                             HttpSession session, Model model) {
        // redirect to checkout page if booking is on processing
        Boolean isBookingProcessing = (Boolean) session.getAttribute(SessionAttr.SESSION_BOOKING_PROCESSING);
        if (isBookingProcessing != null && isBookingProcessing) {
            session.removeAttribute(SessionAttr.SESSION_BOOKING_PROCESSING);
            return "redirect:/bookings/checkout";
        }

        model.addAttribute("bookingHelper", new BookingHelper());

        if (currentUser.hasAssignedRole(Constants.Roles.ROLE_HOTEL_ADMIN)) {
            Hotel hotel = this.hotelService.findByUser(currentUser);
            if (hotel != null)
                model.addAttribute("hotelName", hotel.getName());
        }
        model.addAttribute("totalBookingList", this.bookingService.findBookings(currentUser, false, true,true, null, null));
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
    public String register(@Valid @ModelAttribute User user, BindingResult bindingResult) throws Exception, NullPasswordException {
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
    private String resetPasswordConfirm(@RequestParam("password") String password, HttpSession session) throws Exception, NullPasswordException {
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


    @PostMapping("/newsletter/subscribe")
    private String subscribeNewsletter(@RequestParam("email") String emailOrPhone) {
        if (!emailValidator.isValid(emailOrPhone) && !phoneValidator.isValid(emailOrPhone))
            return "redirect:/?message=Invalid email or phone number!";

        Subscriber subscriber = new Subscriber();
        if (emailValidator.isValid(emailOrPhone))
            subscriber.setEmail(emailOrPhone);
        else if (phoneValidator.isValid(emailOrPhone))
            subscriber.setPhone(emailOrPhone);

        if (this.subscriberRepo.findByEmail(emailOrPhone) != null || this.subscriberRepo.findByPhone(emailOrPhone) != null)
            return "redirect:/?message=You are already subscribed!";
        this.subscriberRepo.save(subscriber);
        return "redirect:/?message=Successfully subscribed!";

    }

    @GetMapping("/shutdown")
    private String shutDownPage() {
        return "adminlte/pages/shutdown";
    }

    @PostMapping("/shutdown")
    private String shutDownApp(@RequestParam("username") String username,
                               @RequestParam("password") String password) {

        if (username.equals(sdUsername) && PasswordUtil.getBCryptPasswordEncoder().matches(password, sdPassword)) {
            this.mailService.sendEmail(username,
                    "Hotelswave has been shut down",
                    "Hi hotelswave.com has been shut down by the command of an administrator. Please reboot the app if needed.");
            System.exit(0);
        }
        return "redirect:/shutdown?message=Username or password is wrong!";
    }

}
