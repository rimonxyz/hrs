package com.moninfotech.service;

import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.Review;
import com.moninfotech.domain.User;

import java.util.List;

/**
 * Created by sayemkcn on 5/23/17.
 */
public interface ReviewService {
    Review save(Review review);
    Review findOne(Long id);
    List<Review> findAll();
    List<Review> findByUser(User user,int page,int size);

    List<Review> findByHotel(Hotel hotel,int page,int size);
    List<Review> findByRating(float rating,int page,int size);

    List<Review> findByUserAndHotel(User currentUser, Hotel hotel);
    List<Hotel> findReviewedHotels(User user);

    public List<Review> findReviews(User user, Hotel hotel, int page, int size);
}
