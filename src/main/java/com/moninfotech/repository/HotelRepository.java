package com.moninfotech.repository;

import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sayemkcn on 3/26/17.
 */
@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Hotel findByUser(User user);

    List<Hotel> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<Hotel> findByAddressAreaContainingIgnoreCase(String area,Pageable pageable);

    List<Hotel> findByAddressUpazilaContainingIgnoreCase(String location,Pageable pageable);
}
