package com.moninfotech.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.moninfotech.commons.DateUtils;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Date> bookingDateList;

    @ManyToOne
    private User user;

    @ManyToOne
    private Hotel hotel;

    public boolean isValid() {
        if (roomList == null || bookingDateList == null || roomList.size() != bookingDateList.size()) return false;
        for (int i=0;i<roomList.size()&&i<bookingDateList.size();i++) {
            Room room = roomList.get(i);
            Date bookingDate = bookingDateList.get(i);
            if (room.isBooked(bookingDate)) return false;
        }
        return true;
    }

    public int getTotalCost() {
        int totalCost = 0;
        for (Room room : this.roomList)
            totalCost += room.getPrice();
        return totalCost;
    }

    public int getTotalDiscount() {
        int totalDiscount = 0;
        for (Room room : this.roomList)
            totalDiscount += room.getDiscount();
        return totalDiscount;
    }

    public int getTotalPayableCost() {
        int totalPayableCost = 0;
        for (Room room : this.roomList)
            totalPayableCost += room.getDiscountedPrice();
        return totalPayableCost;
    }

    public String getTotalDiscountPercentage() {
        return (this.getTotalDiscount() * 100) / this.getTotalCost() + "%";
    }
    public String getTotalDiscountPercentageNumber() {
        return (this.getTotalDiscount() * 100) / this.getTotalCost() + "%";
    }

    public String getReadableDate(Date date) {
        return DateUtils.getReadableDateFormat().format(date);
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

    @Override
    public String toString() {
        return "Booking{" +
                "transaction=" + transaction +
                ", user=" + user +
                ", hotel=" + hotel +
                "} " + super.toString();
    }
}
