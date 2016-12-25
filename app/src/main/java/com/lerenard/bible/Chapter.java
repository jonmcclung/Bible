package com.lerenard.bible;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mc on 18-Dec-16.
 */
public class Chapter implements Parcelable {

    public Chapter() {}

    @Override
    public String toString() {
        return "<Chapter(verses: " + getCount() + ")>";
    }

    public Chapter(ArrayList<Verse> verses) {
        this.verses = verses;
    }

    private ArrayList<Verse> verses;

    protected Chapter(Parcel in) {
        verses = in.createTypedArrayList(Verse.CREATOR);
    }

    public static final Creator<Chapter> CREATOR = new Creator<Chapter>() {
        @Override
        public Chapter createFromParcel(Parcel in) {
            return new Chapter(in);
        }

        @Override
        public Chapter[] newArray(int size) {
            return new Chapter[size];
        }
    };

    public void setVerses(ArrayList<Verse> verses) {
        this.verses = verses;
    }

    /**
     * 1-based, or null if out of bounds.
     */
    public Verse getVerse(int index) {
        if (index > 0 && index < verses.size()) {
            return verses.get(index - 1);
        }
        else {
            return null;
        }
    }

    public int getCount() {
        return verses.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(verses);
    }

    public ArrayList<Verse> getVerses() {
        return verses;
    }
}
