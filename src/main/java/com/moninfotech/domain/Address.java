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
    private String upazila;
    private String area;
    private String mapDirectionFrom;

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

    public String getCommaSeperatedString() {
        return String.join(", ", new String[]{this.area, this.upazila, this.district, this.city, this.country});
    }

    public String getUpazila() {
        return upazila;
    }

    public void setUpazila(String upazila) {
        this.upazila = upazila;
    }

    public String getMapDirectionFrom() {
        return mapDirectionFrom;
    }

    public void setMapDirectionFrom(String mapDirectionFrom) {
        this.mapDirectionFrom = mapDirectionFrom;
    }

    @Override
    public String toString() {
        return "Address{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", upazila='" + upazila + '\'' +
                ", area='" + area + '\'' +
                ", mapDirectionFrom='" + mapDirectionFrom + '\'' +
                '}';
    }
}
