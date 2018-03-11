package com.mihaisavin.stox.domain;

import javax.persistence.*;

@Entity
public class Alarm extends AbstractModel {
    private long ownerId;
    private boolean active;
    private String symbol;
    private double originalValue;
    /**
     * percent of increase(+value) or decrease(-value)
     * relative to the original value
     */
    private int variance;
    private String state;
    @Transient
    private double nowValue;

    public Alarm() {
        super();
        active = false;
        state = "created";
        variance = 1; //percent
    }

    public Alarm(String symbol, double originalValue, int variance){
        this();
        this.symbol = symbol;
        this.originalValue = originalValue;
        this.variance = variance;
        this.nowValue = originalValue;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(double originalValue) {
        this.originalValue = originalValue;
    }

    public int getVariance() {
        return variance;
    }

    public void setVariance(int variance) {
        this.variance = variance;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive() {
        this.active = true;
    }
    public void setInactive() {
        this.active = false;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getNowValue() {
        return nowValue;
    }

    public void setNowValue(double nowValue) {
        this.nowValue = nowValue;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
