package com.moninfotech.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by sayemkcn on 6/7/17.
 */
@Entity
public class Invoice extends BaseEntity {

    private Date dueDate;

    @ManyToOne
    private User user;

    @OneToOne
    private Booking booking;

    private boolean paid;

    public Invoice() {
    }

//    @PrePersist
//    @PreUpdate
//    void confirmBooking(){
//        if (this.isPaid()) {
//            this.booking.setConfirmed(true);
//        }
//    }

    public Invoice(Date dueDate, User user, Booking booking) {
        this.dueDate = dueDate;
        this.user = user;
        this.booking = booking;
    }

    public String getInvoiceId(){
        return "#"+String.format("%08d",this.getId());
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.booking.setConfirmed(paid);
        this.paid = paid;
    }
}
