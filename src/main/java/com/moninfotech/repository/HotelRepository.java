package com.moninfotech.repository;

import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.HotelFacilities;
import com.moninfotech.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sayemkcn on 3/26/17.
 */
@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Hotel findByUser(User user);

    @Query(value = "SELECT * FROM hotel LEFT JOIN room ON hotel.id=room.hotel_id WHERE area LIKE %:q% AND star=:star  AND ROUND(rating)=:rating AND restaurant=:restaurant AND room.price BETWEEN :priceFrom AND :priceTo GROUP BY hotel.id",nativeQuery = true)
    List<Hotel> filterHotels(@Param("q") String query,@Param("star")  String star,@Param("priceFrom")  int priceFrom,@Param("priceTo")  int priceTo,@Param("rating")  int rating,@Param("restaurant")  boolean restaurant);

    List<Hotel> findDistinctByNameContainingIgnoreCaseOrAddressAreaContainingIgnoreCaseOrAddressUpazilaContainingIgnoreCase(String name,String area,String upazilla, Pageable pageable);

    List<Hotel> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<Hotel> findByAddressAreaContainingIgnoreCase(String area,Pageable pageable);

    List<Hotel> findByAddressUpazilaContainingIgnoreCase(String location,Pageable pageable);
}
