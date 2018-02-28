package com.mihaisavin.stox.domain;

public class Alarm {
    private boolean active;
    private String symbol;
    private double originalValue;
    /**
     * percent of increase(+value) or decrease(-value)
     * relative to the original value
     */
    private int variance;

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

    public void setActive(boolean active) {
        this.active = active;
    }
}
