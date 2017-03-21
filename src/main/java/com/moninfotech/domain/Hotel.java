package com.moninfotech.domain;

import javax.persistence.*;
import java.util.List;

/**
 * Created by sayemkcn on 3/21/17.
 */
@Entity
public class Hotel extends BaseEntity{
    private String name;
    @Embedded
    private Address address;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hotel")
    private List<Room> roomList;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hotel")
    private List<Review> reviewList;

    @OneToOne
    private User user;

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

    @Override
    public String toString() {
        return "Hotel{" +
                "name='" + name + '\'' +
                ", address=" + address +
                ", roomList=" + roomList +
                ", reviewList=" + reviewList +
                ", user=" + user +
                '}';
    }
}
