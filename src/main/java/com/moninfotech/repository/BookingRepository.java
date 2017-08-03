package com.moninfotech.repository;

import com.moninfotech.domain.Booking;
import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Created by sayemkcn on 4/18/17.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
    List<Booking> findByHotel(Hotel hotel);
    Page<Booking> findByUser(User user, Pageable pageable);
    Page<Booking> findByHotel(Hotel hotel, Pageable pageable);
}
