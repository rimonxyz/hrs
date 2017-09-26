package com.moninfotech.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Package extends BaseEntity {
    @Column(length = 1000000)
    @JsonIgnore
    private byte[] image;
    @Column(nullable = false)
    @NotNull
    private String title;
    private String shortTag;

    private Date date;
    private String duration;
    private int price;
    private String link;
    private String timeDistribution;
    private String include;
    private String exclude;

    @Column(nullable = false)
    @NotNull
    private String location;
    @Column(length = 10000)
    private String description;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortTag() {
        return shortTag;
    }

    public void setShortTag(String shortTag) {
        this.shortTag = shortTag;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTimeDistribution() {
        return timeDistribution;
    }

    public void setTimeDistribution(String timeDistribution) {
        this.timeDistribution = timeDistribution;
    }

    public String getInclude() {
        return include;
    }

    public void setInclude(String include) {
        this.include = include;
    }

    public String getExclude() {
        return exclude;
    }

    public void setExclude(String exclude) {
        this.exclude = exclude;
    }

    @Override
    public String toString() {
        return "Package{" +
                ", title='" + title + '\'' +
                ", shortTag='" + shortTag + '\'' +
                ", date=" + date +
                ", duration='" + duration + '\'' +
                ", price=" + price +
                ", link='" + link + '\'' +
                ", timeDistribution='" + timeDistribution + '\'' +
                ", include='" + include + '\'' +
                ", exclude='" + exclude + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                "} " + super.toString();
    }
}
