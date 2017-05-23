package com.moninfotech.service.impl;

import com.moninfotech.commons.SortAttributes;
import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.Review;
import com.moninfotech.domain.User;
import com.moninfotech.repository.ReviewRepository;
import com.moninfotech.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sayemkcn on 5/23/17.
 */
@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepo;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepo) {
        this.reviewRepo = reviewRepo;
    }

    @Override
    public Review save(Review review) {
        return this.reviewRepo.save(review);
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
}
