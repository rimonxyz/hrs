package com.moninfotech.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Embedded;
import javax.persistence.Entity;

/**
 * Created by sayemkcn on 3/21/17.
 */
@Entity
public class Category extends BaseEntity {
    private String name;
    private int maxChildNumber;
    private int maxAdultNumber;
    @Embedded
    @JsonBackReference
    private Facilities facilities;

    public int getMaxChildNumber() {
        return maxChildNumber;
    }

    public void setMaxChildNumber(int maxChildNumber) {
        this.maxChildNumber = maxChildNumber;
    }

    public int getMaxAdultNumber() {
        return maxAdultNumber;
    }

    public void setMaxAdultNumber(int maxAdultNumber) {
        this.maxAdultNumber = maxAdultNumber;
    }

    public Facilities getFacilities() {
        return facilities;
    }

    public void setFacilities(Facilities facilities) {
        this.facilities = facilities;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", maxChildNumber=" + maxChildNumber +
                ", maxAdultNumber=" + maxAdultNumber +
                ", facilities=" + facilities +
                "} " + super.toString();
    }
}
