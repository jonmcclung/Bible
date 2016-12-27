package com.lerenard.bible;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.Log;

import java.sql.Date;

/**
 * Created by mc on 18-Dec-16.
 */
public class Ribbon implements Parcelable {

    private static final String TAG = "Ribbon_";
    private @NonNull
    Translation translation;
    private Reference reference;
    private String name;
    private static final String dateFormatString = "dd MMM yyyy";
    private CharSequence lastVisitedText;
    private Date lastVisited;

    protected Ribbon(Parcel in) {
        translation = in.readParcelable(Translation.class.getClassLoader());
        reference = in.readParcelable(Reference.class.getClassLoader());
        name = in.readString();
        lastVisited = new Date(in.readLong());
        lastVisitedText = DateFormat.format(dateFormatString, lastVisited);
    }

    public static final Creator<Ribbon> CREATOR = new Creator<Ribbon>() {
        @Override
        public Ribbon createFromParcel(Parcel in) {
            return new Ribbon(in);
        }

        @Override
        public Ribbon[] newArray(int size) {
            return new Ribbon[size];
        }
    };

    public Date getLastVisited() {
        return lastVisited;
    }

    public CharSequence getLastVisitedText() {
        return lastVisitedText;
    }

    public void setLastVisitedToNow() {
        lastVisited = new Date(System.currentTimeMillis());
        lastVisitedText = DateFormat.format(dateFormatString, lastVisited);
    }

    @NonNull
    public Translation getTranslation() {
        return translation;
    }

    public void setTranslation(@NonNull Translation translation) {
        this.translation = translation;
    }

    public Reference getReference() {
        return reference;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Ribbon() {
        this.translation = Translation.getDefault();
        this.reference = Reference.getDefault();
        this.name = null;
        lastVisited = new Date(0);
        lastVisitedText = HomeActivity.getContext().getString(R.string.neverVisited);
    }

    @Override
    public String toString() {
        return "<Ribbon(name: " + name + ", translation: " + translation.toString() + ", reference: " + reference.toString() + ")>";
    }

    public Ribbon(@NonNull Translation translation, Reference reference, String name) {
        this.translation = translation;
        this.reference = reference;
        this.name = name;
        setLastVisitedToNow();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(translation, 0);
        parcel.writeParcelable(reference, 0);
        parcel.writeString(name);
        parcel.writeLong(lastVisited.getTime());
    }

    public Book getBook() {
        return translation.getBook(reference.getBookIndex());
    }

    public Chapter getChapter() {
        int bookIndex = reference.getBookIndex();
        Log.d(TAG, "bookIndex: " + bookIndex);
        Book book = translation.getBook(bookIndex);
        Log.d(TAG, "book: " + book);
        int chapterIndex = reference.getChapter();
        Log.d(TAG, "chapterIndex: " + chapterIndex);
        Chapter chapter = book.getChapter(chapterIndex);
        Log.d(TAG, "chapter: " + chapter);
        return chapter;
    }
}
