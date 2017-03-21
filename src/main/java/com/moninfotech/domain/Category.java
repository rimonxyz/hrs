package com.moninfotech.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;

/**
 * Created by sayemkcn on 3/21/17.
 */
@Entity
public class Category extends BaseEntity {
    private int maxChildNumber;
    private int maxAdultNumber;
    @Embedded
    private Facilities facilities;
    private boolean frontFace;
    private boolean withCorridor;

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

    public boolean isFrontFace() {
        return frontFace;
    }

    public void setFrontFace(boolean frontFace) {
        this.frontFace = frontFace;
    }

    public boolean isWithCorridor() {
        return withCorridor;
    }

    public void setWithCorridor(boolean withCorridor) {
        this.withCorridor = withCorridor;
    }

    @Override
    public String toString() {
        return "Category{" +
                "maxChildNumber=" + maxChildNumber +
                ", maxAdultNumber=" + maxAdultNumber +
                ", facilities=" + facilities +
                ", frontFace=" + frontFace +
                ", withCorridor=" + withCorridor +
                "} " + super.toString();
    }
}
