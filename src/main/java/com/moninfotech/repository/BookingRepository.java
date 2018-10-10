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
    List<Booking> findByCancelledAndConfirmedAndApproved(boolean calceled,boolean isConfirmed,boolean isApproved);

    List<Booking> findByUserAndCancelledAndConfirmed(User user,boolean calceled, boolean isConfirmed);

    List<Booking> findByHotelAndCancelledAndConfirmed(Hotel hotel,boolean calceled, boolean isConfirmed);

    Page<Booking> findByUserAndCancelledAndConfirmed(User user,boolean calceled, boolean isConfirmed, Pageable pageable);

    Page<Booking> findByHotelAndCancelledAndConfirmed(Hotel hotel,boolean calceled, boolean isConfirmed, Pageable pageable);

    Page<Booking> findByCancelledAndConfirmed(boolean isConfirmed,boolean calceled, Pageable pageable);
}
