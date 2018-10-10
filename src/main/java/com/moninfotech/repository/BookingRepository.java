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
    List<Booking> findByCancelledAndConfirmedAndApproved(boolean calceled, boolean isConfirmed, boolean isApproved);

    List<Booking> findByUserAndCancelledAndConfirmedAndApproved(User user, boolean calceled, boolean isConfirmed, boolean isApproved);

    List<Booking> findByHotelAndCancelledAndConfirmedAndApproved(Hotel hotel, boolean calceled, boolean isConfirmed, boolean isApproved);

    Page<Booking> findByUserAndCancelledAndConfirmedAndApproved(User user, boolean calceled, boolean isConfirmed, boolean isApproved, Pageable pageable);

    Page<Booking> findByHotelAndCancelledAndConfirmedAndApproved(Hotel hotel, boolean calceled, boolean isConfirmed, boolean isApproved, Pageable pageable);

    Page<Booking> findByCancelledAndConfirmedAndApproved(boolean canceled, boolean confirmed, boolean isApproved, Pageable pageable);
}
