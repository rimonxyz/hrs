package com.moninfotech.domain;

import javax.persistence.Embeddable;

@Embeddable
public class HotelFacilities {
    private boolean restaurant;
    private boolean lift;
    private boolean wifi;
    private boolean conferenceRoom;
    private boolean swimingPool;
    private boolean sportsZone;
    private boolean gym;
    private boolean spa;
    private boolean bar;
    private boolean helipad;
    private boolean ac;

    public HotelFacilities() {
    }

    public boolean isRestaurant() {
        return restaurant;
    }

    public void setRestaurant(boolean restaurant) {
        this.restaurant = restaurant;
    }

    public boolean isLift() {
        return lift;
    }

    public void setLift(boolean lift) {
        this.lift = lift;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isConferenceRoom() {
        return conferenceRoom;
    }

    public void setConferenceRoom(boolean conferenceRoom) {
        this.conferenceRoom = conferenceRoom;
    }

    public boolean isSwimingPool() {
        return swimingPool;
    }

    public void setSwimingPool(boolean swimingPool) {
        this.swimingPool = swimingPool;
    }

    public boolean isSportsZone() {
        return sportsZone;
    }

    public void setSportsZone(boolean sportsZone) {
        this.sportsZone = sportsZone;
    }

    public boolean isGym() {
        return gym;
    }

    public void setGym(boolean gym) {
        this.gym = gym;
    }

    public boolean isSpa() {
        return spa;
    }

    public void setSpa(boolean spa) {
        this.spa = spa;
    }

    public boolean isBar() {
        return bar;
    }

    public void setBar(boolean bar) {
        this.bar = bar;
    }

    public boolean isHelipad() {
        return helipad;
    }

    public void setHelipad(boolean helipad) {
        this.helipad = helipad;
    }


    public boolean isAc() {
        return ac;
    }

    public void setAc(boolean ac) {
        this.ac = ac;
    }

    @Override
    public String toString() {
        return "HotelFacilities{" +
                "restaurant=" + restaurant +
                ", lift=" + lift +
                ", wifi=" + wifi +
                ", conferenceRoom=" + conferenceRoom +
                ", swimingPool=" + swimingPool +
                ", sportsZone=" + sportsZone +
                ", gym=" + gym +
                ", spa=" + spa +
                ", bar=" + bar +
                ", helipad=" + helipad +
                ", ac=" + ac +
                '}';
    }
}
