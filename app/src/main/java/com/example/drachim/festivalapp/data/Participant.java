package com.example.drachim.festivalapp.data;

/**
 * Created by Dr. Achim on 09.05.2017.
 */

public class Participant {
    private String name;
    private boolean isInterested;

    public Participant(String name, boolean isInterested) {
        this.name = name;
        this.isInterested = isInterested;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInterested() {
        return isInterested;
    }

    public void setInterested(boolean interested) {
        isInterested = interested;
    }
}
