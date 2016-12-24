package com.lerenard.bible;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mc on 18-Dec-16.
 */
public class Book implements Parcelable {

    public Book() {}

    protected Book(Parcel in) {
        chapters = in.createTypedArrayList(Chapter.CREATOR);
        name = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public String toString() {
        return "<Book(name: " + name + " chapters: " + chapters.toString() + ")>";
    }

    public Book(String name, ArrayList<Chapter> chapters) {
        this.name = name;
        this.chapters = chapters;
    }

    private ArrayList<Chapter> chapters;

    public String getName() {
        return name;
    }

    private String name;

    /**
     * 1-based, or null if out of bounds.
     */
    public Chapter getChapter(int index) {
        if (index > 0 && index <= chapters.size()) {
            return chapters.get(index - 1);
        }
        else {
            return null;
        }
    }

    public void setChapters(ArrayList<Chapter> chapters) {
        this.chapters = chapters;
    }

    public int getChapterCount() {
        return chapters.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(chapters);
        parcel.writeString(name);
    }
}
