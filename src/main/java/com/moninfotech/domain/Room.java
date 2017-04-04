package com.moninfotech.domain;

import javax.persistence.*;
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
    private List<byte[]> images;
    private int discount;
    private boolean discounted;
    private int floorNumber;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roomList")
    private List<Booking> bookingList;
    @ManyToOne
    private Hotel hotel;

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
