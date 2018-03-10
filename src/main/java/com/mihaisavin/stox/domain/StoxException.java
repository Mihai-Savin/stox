package com.mihaisavin.stox.domain;

import java.util.Arrays;

public class StoxException extends Exception {
    String[] causes;

    public StoxException(String... causes) {
        super();
        this.causes = causes;
    }

    @Override
    public String getMessage() {
        return causes != null ? Arrays.toString(causes) : "No CAUSE!";
    }

}