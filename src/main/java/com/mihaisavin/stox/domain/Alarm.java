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

    public Alarm() {
    }

    public Alarm(String symbol, double originalValue, int variance){
        active = true;
        this.symbol = symbol;
        this.originalValue = originalValue;
        this.variance = variance;
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

    public void setOriginalValue(long originalValue) {
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

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }
}
