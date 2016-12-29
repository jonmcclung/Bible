package com.lerenard.bible;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mc on 18-Dec-16.
 */
public class Verse implements Parcelable {

    public static final Creator<Verse> CREATOR = new Creator<Verse>() {
        @Override
        public Verse createFromParcel(Parcel in) {
            return new Verse(in);
        }

        @Override
        public Verse[] newArray(int size) {
            return new Verse[size];
        }
    };
    private String text;

    public Verse() {}

    public Verse(String text) {
        this.text = text;
    }

    protected Verse(Parcel in) {
        text = in.readString();
    }

    @Override
    public String toString() {
        return text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(text);
    }
}
