package com.moninfotech.repository;

import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.Review;
import com.moninfotech.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by sayemkcn on 5/23/17.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    Page<Review> findByUser(User user, Pageable pageable);
    Page<Review> findByHotel(Hotel hotel, Pageable pageable);
    Page<Review> findByRating(float rating,Pageable pageable);
}
