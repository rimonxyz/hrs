package com.moninfotech.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.moninfotech.commons.DateUtils;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sayemkcn on 3/23/17.
 */
@Entity
public class Booking extends BaseEntity {

    @OneToOne
    private Transaction transaction;
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Room> roomList;

    private boolean manualBooking;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Date> bookingDateList;

    @ManyToOne
    private User user;

    @ManyToOne
    private Hotel hotel;

    @OneToOne(mappedBy = "booking")
    private Invoice invoice;

    public boolean isValid() {
        // TODO: 6/7/17 TIME VALIDATION | MAKE SURE BOOKING TIME ISN'T PAST
        if (roomList == null || bookingDateList == null || roomList.size() != bookingDateList.size()) return false;
        for (int i = 0; i < roomList.size() && i < bookingDateList.size(); i++) {
            Room room = roomList.get(i);
            Date bookingDate = bookingDateList.get(i);
            if (room.isBooked(bookingDate)) return false;
        }
        return true;
    }

    public int getTotalCost() {
        return this.roomList.stream()
                .mapToInt(Room::getPrice)
                .sum();
    }

    public synchronized int getTotalDiscount() {
        int totalDiscount = 0;
        for (int i = 0; i < roomList.size() && i < bookingDateList.size(); i++)
            totalDiscount += roomList.get(i).getDiscount(bookingDateList.get(i));
        return totalDiscount;
    }

    public synchronized int getTotalPayableCost() {
        int totalPayableCost = 0;
        for (int i=0;i<roomList.size()&&i<bookingDateList.size();i++)
            totalPayableCost += roomList.get(i).getDiscountedPrice(bookingDateList.get(i));
        return totalPayableCost;
    }

    public String getTotalDiscountPercentage() {
        if (this.getTotalCost() == 0) return this.getTotalCost() + "%";
        return (this.getTotalDiscount() * 100) / this.getTotalCost() + "%";
    }

    public int getTotalDiscountPercentageNumber() {
        if (this.getTotalCost() == 0) return 0;
        return (this.getTotalDiscount() * 100) / this.getTotalCost();
    }

    public String getReadableDate(Date date) {
        return DateUtils.getReadableDateFormat().format(date);
    }

    public String getAllBookingDates() {
        Set<String> dates = new HashSet<>();
        SimpleDateFormat dateFormat = DateUtils.getReadableDateFormat();
        this.bookingDateList.forEach(date -> dates.add(dateFormat.format(date)));
        return String.join(", ", dates);
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public List<Date> getBookingDateList() {
        return bookingDateList;
    }

    public void setBookingDateList(List<Date> bookingDateList) {
        this.bookingDateList = bookingDateList;
    }


    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public boolean isManualBooking() {
        return manualBooking;
    }

    public void setManualBooking(boolean manualBooking) {
        this.manualBooking = manualBooking;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "transaction=" + transaction +
                ", roomList=" + roomList +
                ", manualBooking=" + manualBooking +
                ", bookingDateList=" + bookingDateList +
                ", user=" + user +
                ", hotel=" + hotel +
                ", invoice=" + invoice +
                '}';
    }
}
