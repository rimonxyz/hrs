package com.moninfotech.controllers.review;

import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.Review;
import com.moninfotech.domain.User;
import com.moninfotech.domain.annotations.CurrentUser;
import com.moninfotech.service.BookingService;
import com.moninfotech.service.HotelService;
import com.moninfotech.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by sayemkcn on 5/23/17.
 */
@Controller
@RequestMapping(value = "/reviews")
public class ReviewController {

    private final HotelService hotelService;
    private final ReviewService reviewService;
    private final BookingService bookingService;

    @Autowired
    public ReviewController(HotelService hotelService, ReviewService reviewService, BookingService bookingService) {
        this.hotelService = hotelService;
        this.reviewService = reviewService;
        this.bookingService = bookingService;
    }

    @RequestMapping(value = "/hotel/{hotelId}/create", method = RequestMethod.GET)
    private String createPage(@PathVariable("hotelId") Long hotelId,
                              @CurrentUser User currentUser, Model model) {
        Hotel hotel = this.hotelService.findOne(hotelId);
        if (hotel == null) return "redirect:/reviews?message=Hotel not found!";
        if (!hotel.hasBookingUser(currentUser) || hotel.hasUserReview(currentUser))
            return "redirect:/reviews?message=You can\'t review this hotel!";
        // extra - booking informations in this hotel
        model.addAttribute(this.bookingService.getBookingList(hotel, currentUser));
        model.addAttribute("hotel", hotel);
        return "review/create";
    }

    @RequestMapping(value = "/hotel/{hotelId}/create", method = RequestMethod.POST)
    private String create(@ModelAttribute Review review, BindingResult bindingResult,
                          @PathVariable("hotelId") Long hotelId,
                          @CurrentUser User currentUser, Model model) {
        if (bindingResult.hasErrors())
            System.out.println(bindingResult.toString());
        Hotel hotel = this.hotelService.findOne(hotelId);
        if (hotel == null) return "redirect:/reviews?message=Hotel not found!";
        if (!hotel.hasBookingUser(currentUser) || hotel.hasUserReview(currentUser))
            return "redirect:/reviews?message=You can\'t review this hotel!";
        if (review.getComment() == null || review.getComment().isEmpty() || review.getRating() == 0)
            return "redirect:/reviews/hotel/" + hotelId + "/create?message=Comment or Rating can not be empty!";
        review.setUser(currentUser);
        review.setHotel(hotel);
        review = this.reviewService.save(review);
        return "redirect:/reviews/" + review.getId();
    }

}
