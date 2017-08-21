package com.moninfotech.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by sayemkcn on 3/21/17.
 */
@Entity
public class Hotel extends BaseEntity {
    private String name;
    private String description;
    private String phoneNumber;
    private String type;
    private String star;
    @Column(length = 1000000)
    @JsonIgnore
    private byte[] image;
    @Embedded
    private Address address;
    @Embedded
    private HotelFacilities facilities;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hotel")
    @JsonBackReference
    private List<Room> roomList;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hotel")
    @JsonBackReference
    private List<Review> reviewList;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hotel")
    @JsonBackReference
    private List<Booking> bookingList;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    public HotelFacilities getFacilities() {
        return facilities;
    }

    public void setFacilities(HotelFacilities facilities) {
        this.facilities = facilities;
    }

    public static final class Type {
        private Type() {
        }

        public static final String HOTEL = "Hotel";
        public static final String SHIP = "Ship";
    }

    public float getAverageRating() {
        if (reviewList == null) return 0f;
        float sum = (float) this.reviewList.stream()
                .mapToDouble(Review::getRating)
                .sum();
        return sum / this.reviewList.size();
    }

    public int getNumberOfAcRooms() {
        return (int) this.roomList.stream()
                .filter(room -> room.getFacilities().isAirConditioned())
                .count();
    }

    public int getNumberOfNonAcRooms() {
        return this.roomList.size() - this.getNumberOfAcRooms();
    }

    public boolean hasUnbookedRoom(Date date) {
        for (Room room : this.roomList) {
            if (!room.isBooked(date))
                return true;
        }
        return false;
    }

    public boolean hasBookingUser(User user) {
        for (Booking booking : this.bookingList) {
            if (booking.getUser().getId().equals(user.getId()))
                return true;
        }
        return false;
    }

    public boolean hasUserReview(User user) {
        for (Review review : this.reviewList) {
            if (review.getUser().getId().equals(user.getId()))
                return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getType() {
        return type == null ? Type.HOTEL : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", type='" + type + '\'' +
                ", star=" + star +
                ", image=" + Arrays.toString(image) +
                ", address=" + address +
                ", roomList=" + roomList +
                ", reviewList=" + reviewList +
                ", bookingList=" + bookingList +
                ", user=" + user +
                '}';
    }
}
