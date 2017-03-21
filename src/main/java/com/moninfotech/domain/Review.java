package com.moninfotech.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by sayemkcn on 3/21/17.
 */
@Entity
public class Review extends BaseEntity {
    private float rating;
    private String comment;
    @ManyToOne
    private Hotel hotel;

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

    @Override
    public String toString() {
        return "Review{" +
                "rating=" + rating +
                ", comment='" + comment + '\'' +
                ", hotel=" + hotel +
                "} " + super.toString();
    }
}
