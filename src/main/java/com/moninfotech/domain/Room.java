package com.moninfotech.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moninfotech.commons.DateUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by sayemkcn on 3/21/17.
 */
@Entity
public class Room extends BaseEntity {
    private String roomNumber;
    @OneToOne
    private Category category;
    private int price;
    @ElementCollection(fetch = FetchType.LAZY)
    @Column(length = 1000000)
    @JsonIgnore
    private List<byte[]> images;
    private int discount;
    private boolean discounted;
    private int floorNumber;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roomList")
    @JsonBackReference
    private List<Booking> bookingList;
    @ManyToOne
    @JsonBackReference
    private Hotel hotel;

    @PrePersist
    @PreUpdate
    public void setDiscounted() {
        if (this.discount > 0)
            this.setDiscounted(true);
        else this.setDiscounted(false);
    }

    public boolean isBooked(Date date1, Date date2) {
        for (Booking booking : this.bookingList) {
            if (DateUtils.isInBetween(booking.getStartDate(), booking.getEndDate(), date1, date2) || DateUtils.isInBetween(date1, date2, booking.getStartDate(), booking.getEndDate()))
                return true;
        }
        return false;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<byte[]> getImages() {
        return images;
    }

    public void setImages(List<byte[]> images) {
        this.images = images;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public boolean isDiscounted() {
        return discounted;
    }

    public void setDiscounted(boolean discounted) {
        this.discounted = discounted;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber='" + roomNumber + '\'' +
                ", category=" + category +
                ", price=" + price +
                ", images=" + images +
                ", discount=" + discount +
                ", discounted=" + discounted +
                ", floorNumber=" + floorNumber +
                ", bookingList=" + bookingList +
                ", hotel=" + hotel +
                "} " + super.toString();
    }
}
