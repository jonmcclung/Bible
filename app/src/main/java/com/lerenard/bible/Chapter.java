package com.lerenard.bible;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by mc on 18-Dec-16.
 */
public class Chapter implements Parcelable {

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
    private ArrayList<Verse> verses;
    private int index;

    public Chapter() {}

    public Chapter(int index) {
        this.index = index;
    }

    public Chapter(ArrayList<Verse> verses, int index) {
        this(index);
        this.verses = verses;
    }

    protected Chapter(Parcel in) {
        verses = in.createTypedArrayList(Verse.CREATOR);
        index = in.readInt();
    }

    @Override
    public String toString() {
        return "<Chapter(verses: " + getVerseCount() + ")>";
    }

    public int getVerseCount() {
        return verses.size();
    }

    public int getIndex() {
        return index;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(verses);
        parcel.writeInt(index);
    }

    public ArrayList<Verse> getVerses() {
        return verses;
    }

    public void setVerses(ArrayList<Verse> verses) {
        this.verses = verses;
    }
}
