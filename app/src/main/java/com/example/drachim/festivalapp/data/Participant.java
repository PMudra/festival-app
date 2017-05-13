package com.example.drachim.festivalapp.data;

import android.graphics.Bitmap;

/**
 * Created by Dr. Achim on 09.05.2017.
 */

public class Participant {
    private String name;
    private Bitmap photo;
    private boolean isInterested;

    public Participant(String name, boolean isInterested) {
        this.name = name;
        this.isInterested = isInterested;
        this.photo = null;
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

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}
