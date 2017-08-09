package com.moninfotech.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Offer extends BaseEntity {
    private String title;
    private String shortTag;
    @Column(length = 10000)
    private String description;
    @Column(length = 1000000)
    @JsonIgnore
    private byte[] image;
    private String linkToOpen;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortTag() {
        return shortTag;
    }

    public void setShortTag(String shortTag) {
        this.shortTag = shortTag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getLinkToOpen() {
        return linkToOpen;
    }

    public void setLinkToOpen(String linkToOpen) {
        this.linkToOpen = linkToOpen;
    }
}
