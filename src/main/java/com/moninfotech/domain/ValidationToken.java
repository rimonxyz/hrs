package com.moninfotech.domain;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class ValidationToken extends BaseEntity{
    private String token;
    private boolean tokenValid;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isTokenValid() {
        return tokenValid;
    }

    public void setTokenValid(boolean tokenValid) {
        this.tokenValid = tokenValid;
    }

    @Override
    public String toString() {
        return "ValidationToken{" +
                "token='" + token + '\'' +
                ", tokenValid=" + tokenValid +
                "} " + super.toString();
    }
}
