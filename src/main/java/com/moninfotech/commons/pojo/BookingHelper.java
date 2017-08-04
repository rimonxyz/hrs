package com.moninfotech.commons.pojo;

import com.moninfotech.domain.Booking;
import com.moninfotech.domain.Room;

import java.util.List;

/**
 * Created by sayemkcn on 5/28/17.
 */
public class BookingHelper {

    public Long getTotalCost(List<Booking> bookingList) {
        return bookingList.stream()
                .map(Booking::getTotalCost)
                .mapToLong(Integer::longValue)
                .sum();
    }

    public Long getTotalDiscount(List<Booking> bookingList) {
        return bookingList.stream()
                .map(Booking::getTotalDiscount)
                .mapToLong(Integer::longValue)
                .sum();
    }

    public Long getTotalPaid(List<Booking> bookingList) {
        return bookingList.stream()
                .map(Booking::getTotalPayableCost)
                .mapToLong(Integer::longValue)
                .sum();
    }
    public Long getTotalDiscountedPrice(List<Room> roomList,String dateStr) {
        return roomList.stream()
                .map(room -> room.getDiscountedPrice(dateStr))
                .mapToLong(Integer::longValue)
                .sum();
    }
}
