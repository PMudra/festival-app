package com.example.drachim.festivalapp.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Participant implements Parcelable{
    private String name;
    private String photoUri;
    private boolean isInterested = true;

    public Participant(String name) {
        this.name = name;
    }

    public Participant(String name, String photoUri) {
        this.name = name;
        this.photoUri = photoUri;
    }

    private Participant(Parcel data) {
        this.name = data.readString();
        this.photoUri = data.readString();

        boolean[] myBooleanArr = new boolean[1];
        data.readBooleanArray(myBooleanArr);
        this.isInterested = myBooleanArr[0];
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

    public String getPhoto() {
        return photoUri;
    }

    public void setPhoto(String photoUri) {
        this.photoUri = photoUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(photoUri);
        dest.writeBooleanArray(new boolean[]{isInterested});
    }

    public static final Parcelable.Creator<Participant> CREATOR = new Parcelable.Creator<Participant>() {
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
