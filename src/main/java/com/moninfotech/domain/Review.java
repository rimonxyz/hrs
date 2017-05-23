package com.moninfotech.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by sayemkcn on 3/21/17.
 */
@Entity
public class Review extends BaseEntity {
    private float rating;
    private String title;
    private String comment;

    @ManyToOne
    private Hotel hotel;

    @ManyToOne
    @JsonBackReference
    private User user;

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Review{" +
                "rating=" + rating +
                ", title='" + title + '\'' +
                ", comment='" + comment + '\'' +
                "} " + super.toString();
    }
}
