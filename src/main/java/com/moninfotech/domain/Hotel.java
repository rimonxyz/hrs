package com.moninfotech.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sayemkcn on 3/21/17.
 */
@Entity
public class Hotel extends BaseEntity {
    private String name;
    @Column(length = 10000)
    private String description;
    private String phoneNumber;
    private String type;
    private String star;
    private boolean enabled;
    private boolean deleted;

    private float rating;
    private String accomodationType;
    private String popularPlacesToVisit;
    private String bdcCompareLink;
    private String hotelsDotComCompareLink;
    private String agodaCompareLink;
    private String expediaCompareLink;
    private String tourPackagesLink;

    private Integer discount;

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(length = 83886080)
    @JsonIgnore
    private List<byte[]> images;
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

    public String getRatingDesc() {
        if (this.rating >= 7)
            return "Excellent";
        else if (this.rating > 5 && this.rating < 7)
            return "Great";
        else if (this.rating > 3 && this.rating <= 5)
            return "Good";
        else if (this.rating > 0)
            return "Low";
        else if (this.reviewList.size() == 0)
            return "Unrated";
        return "Very Low";
    }

    public List<Category> getEffectiveCategories(){
        if (this.roomList==null) return Collections.emptyList();
        return this.roomList.stream()
                .filter(room -> !room.isArchived())
                .map(Room::getCategory)
                .collect(Collectors.toList());
    }

    public int getMinimumPrice() {
        if (this.roomList.size() == 0) return 0;

        int price = Integer.MAX_VALUE;
        for (Room room : roomList) {
            if (room.getPrice() < price)
                price = room.getPrice();
        }
        return price;

    }
    
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }


    public String getAccomodationType() {
        return accomodationType;
    }

    public void setAccomodationType(String accomodationType) {
        this.accomodationType = accomodationType;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getTourPackagesLink() {
        return tourPackagesLink;
    }

    public void setTourPackagesLink(String tourPackagesLink) {
        this.tourPackagesLink = tourPackagesLink;
    }

    public static final class Type {
        private Type() {
        }

        public static final String HOTEL = "Hotel";
        public static final String SHIP = "Ship";
        public static final String BOTH = "Both";
    }

    public enum AccomodationType {
        HOTEL("Hotel"),
        RESORT("Resort"),
        APARTMENT("Apartment");

        private String value;

        AccomodationType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
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

    public List<byte[]> getImages() {
        return images;
    }

    public void setImages(List<byte[]> images) {
        this.images = images;
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

    public HotelFacilities getFacilities() {
        return facilities;
    }

    public void setFacilities(HotelFacilities facilities) {
        this.facilities = facilities;
    }

    public String getPopularPlacesToVisit() {
        return popularPlacesToVisit;
    }

    public String getPopularPlacesToVisitHtml() {
        if (this.popularPlacesToVisit==null) return "";
        String newString =popularPlacesToVisit.replace(",,,","<br/>");
        newString = newString.replace("-b","<b>");
        newString = newString.replace("b-","</b>");
        return newString;
    }

    public void setPopularPlacesToVisit(String popularPlacesToVisit) {
        this.popularPlacesToVisit = popularPlacesToVisit;
    }

    public String getBdcCompareLink() {
        return bdcCompareLink;
    }

    public void setBdcCompareLink(String bdcCompareLink) {
        this.bdcCompareLink = bdcCompareLink;
    }

    public String getHotelsDotComCompareLink() {
        return hotelsDotComCompareLink;
    }

    public void setHotelsDotComCompareLink(String hotelsDotComCompareLink) {
        this.hotelsDotComCompareLink = hotelsDotComCompareLink;
    }

    public String getAgodaCompareLink() {
        return agodaCompareLink;
    }

    public void setAgodaCompareLink(String agodaCompareLink) {
        this.agodaCompareLink = agodaCompareLink;
    }

    public String getExpediaCompareLink() {
        return expediaCompareLink;
    }

    public void setExpediaCompareLink(String expediaCompareLink) {
        this.expediaCompareLink = expediaCompareLink;
    }
}
