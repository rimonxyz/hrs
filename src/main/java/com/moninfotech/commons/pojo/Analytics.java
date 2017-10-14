package com.moninfotech.commons.pojo;

public class Analytics {
    private String roomNumber;
    private String category;
    private String bookingDates;
    private int totalRent;

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBookingDates() {
        return bookingDates;
    }

    public void setBookingDates(String bookingDates) {
        this.bookingDates = bookingDates;
    }

    public int getTotalRent() {
        return totalRent;
    }

    public void setTotalRent(int totalRent) {
        this.totalRent = totalRent;
    }
}
