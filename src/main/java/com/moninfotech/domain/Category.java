package com.moninfotech.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * Created by sayemkcn on 3/21/17.
 */
@Entity
public class Category extends BaseEntity {
    private String name;
    private int maxChildNumber;
    private int maxAdultNumber;

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(length = 1000000)
    @JsonIgnore
    private List<byte[]> images;

    @Embedded
    @JsonBackReference
    private Facilities facilities;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private List<Room> roomList;

    @PreRemove
    private void onRemove() {
        this.roomList.clear();
    }

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


    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
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

    public List<byte[]> getImages() {
        return images;
    }

    public void setImages(List<byte[]> images) {
        this.images = images;
    }
}
