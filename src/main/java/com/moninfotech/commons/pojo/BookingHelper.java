package com.moninfotech.commons.pojo;

import com.moninfotech.commons.DateUtils;
import com.moninfotech.domain.Booking;
import com.moninfotech.domain.Room;

import java.text.ParseException;
import java.util.Date;
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

    public Long getTotalDiscountedPrice(List<Room> roomList, String dateStr, boolean supportMultipleBookingDates) throws ParseException {
        Date date = DateUtils.getParsableDateFormat().parse(dateStr);
        if (supportMultipleBookingDates)
            return roomList.stream()
                    .map(room -> room.getDiscountedPrice(date) * room.getBookingDates(date).size())
                    .mapToLong(Integer::longValue)
                    .sum();
        return roomList.stream()
                .map(room -> room.getDiscountedPrice(date))
                .mapToLong(Integer::longValue)
                .sum();
    }
}
