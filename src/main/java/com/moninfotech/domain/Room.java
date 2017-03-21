package com.moninfotech.domain;

import javax.persistence.*;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by sayemkcn on 3/21/17.
 */
@Entity
public class Room extends BaseEntity {
    private String roomNumber;
    @OneToOne
    private Category type;
    private int price;
    @ElementCollection(fetch = FetchType.LAZY)
    private List<Date> bookedDateList;
    @ElementCollection(fetch = FetchType.LAZY)
    private List<File> images;
    private int discount;
    private boolean discounted;
    private int floorNumber;

    @ManyToOne
    private Hotel hotel;

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Category getType() {
        return type;
    }

    public void setType(Category type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<Date> getBookedDateList() {
        return bookedDateList;
    }

    public void setBookedDateList(List<Date> bookedDateList) {
        this.bookedDateList = bookedDateList;
    }

    public List<File> getImages() {
        return images;
    }

    public void setImages(List<File> images) {
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

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber='" + roomNumber + '\'' +
                ", type=" + type +
                ", price=" + price +
                ", bookedDateList=" + bookedDateList +
                ", images=" + images +
                ", discount=" + discount +
                ", discounted=" + discounted +
                ", floorNumber=" + floorNumber +
                ", hotel=" + hotel +
                "} " + super.toString();
    }
}
