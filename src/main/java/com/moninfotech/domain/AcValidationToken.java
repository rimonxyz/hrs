package com.moninfotech.domain;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class AcValidationToken extends ValidationToken{
    @OneToOne
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "AcValidationToken{" +
                "user=" + user +
                "} " + super.toString();
    }
}
