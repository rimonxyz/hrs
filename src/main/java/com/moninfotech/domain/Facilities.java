package com.moninfotech.domain;

import javax.persistence.Embeddable;

/**
 * Created by sayemkcn on 3/21/17.
 */
@Embeddable
public class Facilities{
    private boolean breakfast;
    private boolean internet;
    private boolean ac;
    private boolean tv;
    private boolean geyser;

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

    public boolean isAc() {
        return ac;
    }

    public void setAc(boolean ac) {
        this.ac = ac;
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

    @Override
    public String toString() {
        return "Facilities{" +
                "breakfast=" + breakfast +
                ", internet=" + internet +
                ", ac=" + ac +
                ", tv=" + tv +
                ", geyser=" + geyser +
                "} " + super.toString();
    }
}
