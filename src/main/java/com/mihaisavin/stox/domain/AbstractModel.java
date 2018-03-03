package com.mihaisavin.stox.domain;

public abstract class AbstractModel {
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}