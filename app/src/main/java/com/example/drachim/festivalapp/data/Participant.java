package com.example.drachim.festivalapp.data;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Participant implements Parcelable{

    private int id;
    private int festivalId;
    private String name;
    private boolean isInterested;
    private Uri pictureUri;

    public Participant() {
    }

    private Participant(Parcel data) {
        id = data.readInt();
        festivalId = data.readInt();
        name = data.readString();
        isInterested = data.readInt() != 0;
        pictureUri = data.readParcelable(Uri.class.getClassLoader());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFestivalId() {
        return festivalId;
    }

    public void setFestivalId(int festivalId) {
        this.festivalId = festivalId;
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

    public Uri getPictureUri() {
        return pictureUri;
    }

    public void setPictureUri(Uri pictureUri) {
        this.pictureUri = pictureUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(festivalId);
        dest.writeString(name);
        dest.writeInt(isInterested ? 1 : 0);
        dest.writeParcelable(pictureUri, flags);
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
