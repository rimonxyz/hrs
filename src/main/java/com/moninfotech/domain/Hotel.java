package com.moninfotech.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
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
    @Column(length = 1000000)
    @JsonIgnore
    private byte[] image;
    @Embedded
    private Address address;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hotel")
    @JsonBackReference
    private List<Room> roomList;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hotel")
    @JsonBackReference
    private List<Review> reviewList;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    public boolean hasUnbookedRoom(Date startDate, Date endDate) {
        for (Room room : this.roomList) {
            if (!room.isBooked(startDate, endDate))
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

    @Override
    public String toString() {
        return "Hotel{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                "} " + super.toString();
    }
}
