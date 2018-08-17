package com.moninfotech.service.impl;

import com.moninfotech.commons.Constants;
import com.moninfotech.commons.SortAttributes;
import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.Review;
import com.moninfotech.domain.User;
import com.moninfotech.repository.HotelRepository;
import com.moninfotech.repository.ReviewRepository;
import com.moninfotech.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sayemkcn on 5/23/17.
 */
@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepo;
    private final HotelRepository hotelRepo;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepo, HotelRepository hotelRepo) {
        this.reviewRepo = reviewRepo;
        this.hotelRepo = hotelRepo;
    }

    @Override
    public Review save(Review review) {
        review = this.reviewRepo.save(review);
        Hotel hotel = review.getHotel();
        hotel.setRating(hotel.getAverageRating());
        this.hotelRepo.save(hotel);
        return review;
    }

    @Override
    public Review findOne(Long id) {
        return this.reviewRepo.findOne(id);
    }

    @Override
    public List<Review> findAll() {
        return this.reviewRepo.findAll();
    }

    @Override
    public List<Review> findByUser(User user, int page, int size) {
        return this.reviewRepo.findByUser(user, new PageRequest(page, size, Sort.Direction.DESC, SortAttributes.FIELD_RATING)).getContent();
    }

    @Override
    public List<Review> findByHotel(Hotel hotel, int page, int size) {
        return this.reviewRepo.findByHotel(hotel, new PageRequest(page, size, Sort.Direction.DESC, SortAttributes.FIELD_RATING)).getContent();
    }

    @Override
    public List<Review> findByRating(float rating, int page, int size) {
        return this.reviewRepo.findByRating(rating, new PageRequest(page, size, Sort.Direction.DESC, SortAttributes.FIELD_ID)).getContent();
    }

    @Override
    public List<Review> findByUserAndHotel(User currentUser, Hotel hotel) {
        return this.reviewRepo.findByUserAndHotel(currentUser, hotel);
    }

    @Override
    public List<Hotel> findReviewedHotels(User user) {
        return this.reviewRepo.findByUser(user)
                .stream()
                .map(Review::getHotel)
                .collect(Collectors.toList());
    }

    public List<Review> findAll(int page, int size) {
        return this.reviewRepo.findAll(new PageRequest(page, size, Sort.Direction.DESC, SortAttributes.FIELD_ID)).getContent();
    }

    @Override
    public List<Review> findReviews(User user, Hotel hotel, int page, int size) {
        if (user.hasAssignedRole(Constants.Roles.ROLE_USER)) {
            if (hotel != null)
                return this.findByUserAndHotel(user,hotel);
            return this.findByUser(user, page, size);
        } else if (user.hasAssignedRole(Constants.Roles.ROLE_HOTEL_ADMIN))
            return this.findByHotel(this.hotelRepo.findByUser(user), page, size);
        else {
            if (hotel != null)
                return this.findByHotel(hotel, page, size);
            return this.findAll(page, size);
        }
    }
}
