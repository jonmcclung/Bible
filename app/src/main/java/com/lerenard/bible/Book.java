package com.lerenard.bible;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mc on 18-Dec-16.
 */
public class Book implements Parcelable {

    private static int[] chapterCounts = {
            50, 40, 27, 36, 34, 24, 21, 4, 31, 24, 22, 25, 29, 36, 10, 13, 10, 42, 150, 31, 12, 8,
            66, 52, 5, 48, 12, 14, 3, 9, 1, 4, 7, 3, 3, 3, 2, 14, 4, 28, 16, 24, 21, 28, 16, 16, 13,
            6, 6, 4, 4, 5, 3, 6, 4, 3, 1, 13, 5, 5, 3, 5, 1, 1, 1, 22};

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
        return "<Book(name: " + name + " chapters: " + chapters.size() + ")>";
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

    public static int getChapterCount(int bookIndex) {
        return chapterCounts[bookIndex];
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
