package com.moninfotech.repository;

import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.User;
import org.springframework.data.domain.Page;
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

    List<Hotel> findByDeletedFalse();
    Page<Hotel> findByDeletedFalse(Pageable pageable);

    @Query(value = "SELECT * FROM hotel LEFT JOIN room ON hotel.id=room.hotel_id WHERE area LIKE %:q% AND star LIKE %:star% AND accomodation_type LIKE %:accomodationType% AND ROUND(rating)=:rating" +
            " AND  (:restaurant is null or restaurant=:restaurant) " +
            " AND  (:lift is null or lift=:lift) " +
            " AND  (:wifi is null or wifi=:wifi) " +
            " AND  (:conferenceRoom is null or conference_room=:conferenceRoom) " +
            " AND  (:swimingPool is null or swiming_pool=:swimingPool) " +
            " AND  (:sportsZone is null or sports_zone=:sportsZone) " +
            " AND  (:gym is null or gym=:gym) " +
            " AND  (:spa is null or spa=:spa) " +
            " AND  (:bar is null or bar=:bar) " +
            " AND  (:helipad is null or helipad=:helipad) " +
            " AND  (:ac is null or ac=:ac) " +
            " AND  (:frontDesk is null or front_desk=:frontDesk) " +
            " AND  (:roomService is null or room_service=:roomService) " +
            " AND  (:electricity is null or electricity=:electricity) " +
            " AND  (:security is null or security=:security) " +
            " AND  (:complementaryBreakfast is null or complementary_breakfast=:complementaryBreakfast) " +
            " AND  (:coffeeShop is null or coffee_shop=:coffeeShop) " +
            " AND  (:freeParking is null or free_parking=:freeParking) " +
            " AND  (:airportTransportation is null or airport_transportation=:airportTransportation) " +
            " AND  (:businessCenter is null or business_center=:businessCenter) " +
            " AND  (:meetingRoom is null or meeting_room=:meetingRoom) " +
            " AND  (:doctorOnCall is null or doctor_on_call=:doctorOnCall) " +
            " AND  (:wakeupService is null or wakeup_service=:wakeupService) " +
            " AND  (:wheelChairAccessible is null or wheel_chair_accessible=:wheelChairAccessible) " +
            " AND  (:safeDepositBox is null or safe_deposit_box=:safeDepositBox) " +
            " AND  (:nonSmokingRoom is null or non_smoking_room=:nonSmokingRoom) " +
            " AND  (:vipFloor is null or vip_floor=:vipFloor) " +
            " AND  (:acceptCreditCard is null or accept_credit_card=:acceptCreditCard) " +
            " AND  (:newspaper is null or newspaper=:newspaper) " +
            " AND  (:laundry is null or laundry=:laundry) " +
            " AND  (:lobby is null or lobby=:lobby) " +
            " AND  (:foreignExchange is null or foreign_exchange=:foreignExchange) " +
            " AND  (:executiveLaunge is null or executive_launge=:executiveLaunge) " +
            " AND  (:babySitting is null or baby_sitting=:babySitting) " +
            "AND room.price BETWEEN :priceFrom AND :priceTo GROUP BY hotel.id",nativeQuery = true)
    List<Hotel> filterHotels(@Param("q") String query,
                             @Param("star") String star,
                             @Param("accomodationType") String accomodationType,
                             @Param("priceFrom") int priceFrom,
                             @Param("priceTo") int priceTo,
                             @Param("rating") int rating,

                             @Param("restaurant")  Boolean restaurant,
                             @Param("lift")  Boolean lift,
                             @Param("wifi")  Boolean wifi,
                             @Param("conferenceRoom")  Boolean conferenceRoom,
                             @Param("swimingPool")  Boolean swimingPool,
                             @Param("sportsZone")  Boolean sportsZone,
                             @Param("gym")  Boolean gym,
                             @Param("spa")  Boolean spa,
                             @Param("bar")  Boolean bar,
                             @Param("helipad")  Boolean helipad,
                             @Param("ac")  Boolean ac,
                             @Param("frontDesk")  Boolean frontDesk,
                             @Param("roomService")  Boolean roomService,
                             @Param("electricity")  Boolean electricity,
                             @Param("security")  Boolean security,
                             @Param("complementaryBreakfast")  Boolean complementaryBreakfast,
                             @Param("coffeeShop")  Boolean coffeeShop,
                             @Param("freeParking")  Boolean freeParking,
                             @Param("airportTransportation")  Boolean airportTransportation,
                             @Param("businessCenter")  Boolean businessCenter,
                             @Param("meetingRoom")  Boolean meetingRoom,
                             @Param("doctorOnCall")  Boolean doctorOnCall,
                             @Param("wakeupService")  Boolean wakeupService,
                             @Param("wheelChairAccessible")  Boolean wheelChairAccessible,
                             @Param("safeDepositBox")  Boolean safeDepositBox,
                             @Param("nonSmokingRoom")  Boolean nonSmokingRoom,
                             @Param("vipFloor")  Boolean vipFloor,
                             @Param("acceptCreditCard")  Boolean acceptCreditCard,
                             @Param("newspaper")  Boolean newspaper,
                             @Param("laundry")  Boolean laundry,
                             @Param("lobby")  Boolean lobby,
                             @Param("foreignExchange")  Boolean foreignExchange,
                             @Param("executiveLaunge")  Boolean executiveLaunge,
                             @Param("babySitting")  Boolean babySitting
    );

    List<Hotel> findDistinctByNameContainingIgnoreCaseOrAddressAreaContainingIgnoreCaseOrAddressUpazilaContainingIgnoreCaseAndDeletedFalse(String name,String area,String upazilla, Pageable pageable);

    List<Hotel> findByNameContainingIgnoreCaseAndDeletedFalse(String name, Pageable pageable);

    List<Hotel> findByAddressAreaContainingIgnoreCaseAndDeletedFalse(String area,Pageable pageable);

    List<Hotel> findByAddressUpazilaContainingIgnoreCaseAndDeletedFalse(String location,Pageable pageable);
}
