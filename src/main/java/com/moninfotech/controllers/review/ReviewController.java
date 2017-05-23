package com.moninfotech.controllers.review;

import com.moninfotech.commons.SortAttributes;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @RequestMapping(value = "", method = RequestMethod.GET)
    private String allReviews(@RequestParam(value = "page", required = false) Integer page,
                              @RequestParam(value = "hotelId", required = false) Long hotelId,
                              @CurrentUser User currentUser, Model model) {
        if (page == null || page < 0) page = 0;
        List<Review> myReviewList;
        if (hotelId == null)
            myReviewList = this.reviewService.findByUser(currentUser, page, SortAttributes.Page.SIZE);
        else {
            Hotel hotel = this.hotelService.findOne(hotelId);
            if (hotel == null)
                myReviewList = this.reviewService.findByUser(currentUser, page, SortAttributes.Page.SIZE);
            else
                myReviewList = this.reviewService.findByUserAndHotel(currentUser, hotel);
        }
        model.addAttribute("hotelList", this.reviewService.findReviewedHotels(currentUser));
        model.addAttribute("reviewList", myReviewList);
        return "review/all";
    }


    //CREATE REVIEW

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
        return "redirect:/reviews";
    }

}
