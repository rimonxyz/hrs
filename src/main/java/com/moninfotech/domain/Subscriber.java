package com.moninfotech.domain;

import javax.persistence.Entity;

@Entity
public class Subscriber extends BaseEntity{
    private String email;
    private String phone;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                "} " + super.toString();
    }
}
