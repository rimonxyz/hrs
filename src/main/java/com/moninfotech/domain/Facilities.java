package com.moninfotech.domain;

import javax.persistence.Embeddable;

/**
 * Created by sayemkcn on 3/21/17.
 */
@Embeddable
public class Facilities {
    private boolean frontFace;
    private boolean withCorridor;
    private boolean breakfast;
    private boolean internet;
    private boolean airConditioned;
    private boolean tv;
    private boolean geyser;
    private boolean hillView;
    private boolean seaView;

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

    public boolean isBreakfast() {
        return breakfast;
    }

    public void setBreakfast(boolean breakfast) {
        this.breakfast = breakfast;
    }

    public boolean isInternet() {
        return internet;
    }

    public void setInternet(boolean internet) {
        this.internet = internet;
    }

    public boolean isAirConditioned() {
        return airConditioned;
    }

    public void setAirConditioned(boolean airConditioned) {
        this.airConditioned = airConditioned;
    }

    public boolean isTv() {
        return tv;
    }

    public void setTv(boolean tv) {
        this.tv = tv;
    }

    public boolean isGeyser() {
        return geyser;
    }

    public void setGeyser(boolean geyser) {
        this.geyser = geyser;
    }


    public boolean isHillView() {
        return hillView;
    }

    public void setHillView(boolean hillView) {
        this.hillView = hillView;
    }

    public boolean isSeaView() {
        return seaView;
    }

    public void setSeaView(boolean seaView) {
        this.seaView = seaView;
    }

    @Override
    public String toString() {
        return "Facilities{" +
                "frontFace=" + frontFace +
                ", withCorridor=" + withCorridor +
                ", breakfast=" + breakfast +
                ", internet=" + internet +
                ", airConditioned=" + airConditioned +
                ", tv=" + tv +
                ", geyser=" + geyser +
                ", hillView=" + hillView +
                ", seaView=" + seaView +
                '}';
    }
}
