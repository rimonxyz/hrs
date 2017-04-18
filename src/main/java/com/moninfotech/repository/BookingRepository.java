package com.moninfotech.repository;

import com.moninfotech.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by sayemkcn on 4/18/17.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
}
