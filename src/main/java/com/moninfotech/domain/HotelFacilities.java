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

    private boolean frontDesk;
    private boolean roomService;
    private boolean electricity;
    private boolean security;
    private boolean complementaryBreakfast;
    private boolean coffeeShop;
    private boolean freeParking;
    private boolean airportTransportation;
    private boolean businessCenter;
    private boolean meetingRoom;
    private boolean doctorOnCall;
    private boolean wakeupService;
    private boolean wheelChairAccessible;
    private boolean safeDepositBox;
    private boolean nonSmokingRoom;
    private boolean vipFloor;

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

    public boolean isFrontDesk() {
        return frontDesk;
    }

    public void setFrontDesk(boolean frontDesk) {
        this.frontDesk = frontDesk;
    }

    public boolean isRoomService() {
        return roomService;
    }

    public void setRoomService(boolean roomService) {
        this.roomService = roomService;
    }

    public boolean isElectricity() {
        return electricity;
    }

    public void setElectricity(boolean electricity) {
        this.electricity = electricity;
    }

    public boolean isSecurity() {
        return security;
    }

    public void setSecurity(boolean security) {
        this.security = security;
    }

    public boolean isComplementaryBreakfast() {
        return complementaryBreakfast;
    }

    public void setComplementaryBreakfast(boolean complementaryBreakfast) {
        this.complementaryBreakfast = complementaryBreakfast;
    }

    public boolean isCoffeeShop() {
        return coffeeShop;
    }

    public void setCoffeeShop(boolean coffeeShop) {
        this.coffeeShop = coffeeShop;
    }

    public boolean isFreeParking() {
        return freeParking;
    }

    public void setFreeParking(boolean freeParking) {
        this.freeParking = freeParking;
    }

    public boolean isAirportTransportation() {
        return airportTransportation;
    }

    public void setAirportTransportation(boolean airportTransportation) {
        this.airportTransportation = airportTransportation;
    }

    public boolean isBusinessCenter() {
        return businessCenter;
    }

    public void setBusinessCenter(boolean businessCenter) {
        this.businessCenter = businessCenter;
    }

    public boolean isMeetingRoom() {
        return meetingRoom;
    }

    public void setMeetingRoom(boolean meetingRoom) {
        this.meetingRoom = meetingRoom;
    }

    public boolean isDoctorOnCall() {
        return doctorOnCall;
    }

    public void setDoctorOnCall(boolean doctorOnCall) {
        this.doctorOnCall = doctorOnCall;
    }

    public boolean isWakeupService() {
        return wakeupService;
    }

    public void setWakeupService(boolean wakeupService) {
        this.wakeupService = wakeupService;
    }

    public boolean isWheelChairAccessible() {
        return wheelChairAccessible;
    }

    public void setWheelChairAccessible(boolean wheelChairAccessible) {
        this.wheelChairAccessible = wheelChairAccessible;
    }

    public boolean isSafeDepositBox() {
        return safeDepositBox;
    }

    public void setSafeDepositBox(boolean safeDepositBox) {
        this.safeDepositBox = safeDepositBox;
    }

    public boolean isNonSmokingRoom() {
        return nonSmokingRoom;
    }

    public void setNonSmokingRoom(boolean nonSmokingRoom) {
        this.nonSmokingRoom = nonSmokingRoom;
    }

    public boolean isVipFloor() {
        return vipFloor;
    }

    public void setVipFloor(boolean vipFloor) {
        this.vipFloor = vipFloor;
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
