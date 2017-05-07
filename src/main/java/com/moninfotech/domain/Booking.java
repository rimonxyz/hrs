package com.moninfotech.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.moninfotech.commons.DateUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by sayemkcn on 3/23/17.
 */
@Entity
public class Booking extends BaseEntity {
    private Date startDate;
    private Date endDate;
    @OneToOne
    private Transaction transaction;
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Room> roomList;
    @ManyToOne
    private User user;

    @ManyToOne
    private Hotel hotel;

    public boolean isValid() {
        if (roomList == null) return false;
        for (Room room : roomList) {
            if (room.isBooked(this.startDate, this.endDate)) return false;
        }
        return true;
    }

    public String getReadableDate(Date date){
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

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", transaction=" + transaction +
                ", user=" + user +
                "} " + super.toString();
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
}
