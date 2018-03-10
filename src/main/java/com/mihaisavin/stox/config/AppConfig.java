package com.mihaisavin.stox.config;

import com.mihaisavin.stox.domain.AbstractModel;

import javax.persistence.Entity;

@Entity
public class AppConfig extends AbstractModel {

    public AppConfig() {
        super();
    }

    private String name;
    private boolean startEngine;
    private int pollingInterval;
    private boolean sendEmails;

    public boolean isStartEngine() {
        return startEngine;
    }

    public void setStartEngine(boolean startEngine) {
        this.startEngine = startEngine;
    }

    public int getPollingInterval() {
        return pollingInterval;
    }

    public void setPollingInterval(int seconds) {
        this.pollingInterval = seconds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSendEmails() {
        return sendEmails;
    }

    public void setSendEmails(boolean sendEmails) {
        this.sendEmails = sendEmails;
    }

    @Override
    public String toString() {
        return super.toString() + name + " & " +
                "shouldStartEngine: " + startEngine + " & " +
                "polling interval (seconds): " + pollingInterval + " & " +
                "isSendEmails: " + sendEmails;
    }
}