package com.moninfotech.repository;

import com.moninfotech.domain.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by sayemkcn on 3/26/17.
 */
@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
