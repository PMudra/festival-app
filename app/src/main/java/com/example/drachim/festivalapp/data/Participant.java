package com.example.drachim.festivalapp.data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Participant implements Parcelable{
    private String name;
    private Bitmap photo = null;
    private boolean isInterested = true;

    public Participant(String name) {
        this.name = name;
    }

    public Participant(String name, Bitmap photo) {
        this.name = name;
        this.photo = photo;
    }

    private Participant(Parcel data) {
        this.name = data.readString();

        boolean[] myBooleanArr = new boolean[1];
        data.readBooleanArray(myBooleanArr);
        this.isInterested = myBooleanArr[0];

        this.photo = data.readParcelable(Bitmap.class.getClassLoader());
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeBooleanArray(new boolean[]{isInterested});
        dest.writeParcelable(photo, flags);
    }

    static final Parcelable.Creator<Participant> CREATOR = new Parcelable.Creator<Participant>() {
        @Override
        public Participant[] newArray(int size) {
            return new Participant[size];
        }

        @Override
        public Participant createFromParcel(Parcel source) {
            return new Participant(source);
        }
    };
}
