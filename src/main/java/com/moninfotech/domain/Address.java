package com.moninfotech.domain;

import javax.persistence.Embeddable;

/**
 * Created by sayemkcn on 3/21/17.
 */
@Embeddable
public class Address {
    private String country;
    private String city;
    private String district;
    private String area;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCommaSeperatedString(){
        return String.join(", ",new String[]{this.area,this.district,this.city,this.country});
    }

    @Override
    public String toString() {
        return "Address{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", area='" + area + '\'' +
                '}';
    }
}
