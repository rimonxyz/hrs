package com.moninfotech.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moninfotech.commons.DateUtils;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by sayemkcn on 3/21/17.
 */
@Entity
public class Room extends BaseEntity {
    private String roomNumber;
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
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Category category;

    @PrePersist
    @PreUpdate
    public void setDiscounted() {
        if (this.discount > 0)
            this.setDiscounted(true);
        else this.setDiscounted(false);
    }

    public int getDiscountedPrice() {
        int x = this.price - this.discount;
        if (x < 0)
            return 0;
        return x;
    }

    public String getDiscountPercentage() {
        return String.valueOf((discount * 100) / price) + "% Off";
    }

    // check if this room is booked for a given date of current month
    public boolean isBooked(int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DATE, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        for (Booking booking : this.bookingList) {
            if (DateUtils.isInBetween(booking.getStartDate(), booking.getEndDate(), cal.getTime()))
                return true;
        }
        return false;
    }


    // gives status for two dates - checks if this room is booked on this date range
    public boolean isBooked(Date date1, Date date2) {
        for (Booking booking : this.bookingList) {
            if (DateUtils.isInBetween(booking.getStartDate(), booking.getEndDate(), date1, date2) || DateUtils.isInBetween(date1, date2, booking.getStartDate(), booking.getEndDate()))
                return true;
        }
        return false;
    }

    public List<Integer> getDaysCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int myMonth = cal.get(Calendar.MONTH);

        List<Integer> dateList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
        while (myMonth == cal.get(Calendar.MONTH)) {
            System.out.print(cal.getTime());
            dateList.add(Integer.parseInt(simpleDateFormat.format(cal.getTime())));
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dateList;
    }

    public String getBookingDatesAsString() {
        // get all booked dates of this room
        List<LocalDate> dates = new ArrayList<>();
        if (this.bookingList != null) {
            for (Booking booking : this.bookingList) {
                dates.addAll(DateUtils.getDates(booking.getStartDate(), booking.getEndDate()));
            }
        }
        // convert all dates into a string
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < dates.size(); i++) {
            LocalDate localDate = dates.get(i);
            if (localDate.isBefore(LocalDate.now()))
                continue;
            DateTimeFormatter formatter;
            if (localDate.getMonth().equals(LocalDate.now().getMonth()))
                formatter = DateTimeFormatter.ofPattern("dd");
            else
                formatter = DateTimeFormatter.ofPattern("dd MMM");
            if (i == dates.size() - 1)
                stringBuilder.append(localDate.format(formatter));
            else
                stringBuilder.append(localDate.format(formatter) + ",\n");
        }
        return stringBuilder.toString();
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
