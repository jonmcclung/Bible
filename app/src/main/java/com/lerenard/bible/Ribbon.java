package com.lerenard.bible;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;

import java.sql.Date;

/**
 * Created by mc on 18-Dec-16.
 */
public class Ribbon implements Parcelable {

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
    private static final String TAG = "Ribbon_";
    private static final String dateFormatString = "dd MMM yyyy";
    private Reference reference;
    private String name;
    private CharSequence lastVisitedText;
    private Date lastVisited;

    protected Ribbon(Parcel in) {
        reference = in.readParcelable(Reference.class.getClassLoader());
        name = in.readString();
        lastVisited = new Date(in.readLong());
        lastVisitedText = DateFormat.format(dateFormatString, lastVisited);
    }

    public Ribbon() {
        this.reference = new Reference(Reference.getDefault());
        this.name = null;
        lastVisited = new Date(0);
        lastVisitedText = HomeActivity.getContext().getString(R.string.neverVisited);
    }

    public Ribbon(Reference reference, String name) {
        this.reference = reference;
        this.name = name;
        setLastVisitedToNow();
    }

    public void setLastVisitedToNow() {
        lastVisited = new Date(System.currentTimeMillis());
        lastVisitedText = DateFormat.format(dateFormatString, lastVisited);
    }

    public Date getLastVisited() {
        return lastVisited;
    }

    public CharSequence getLastVisitedText() {
        return lastVisitedText;
    }

    @NonNull
    public Translation getTranslation() {
        return reference.getTranslation();
    }

    public void setTranslation(@NonNull Translation translation) {
        reference.setTranslation(translation);
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

    @Override
    public String toString() {
        return "<Ribbon(name: " + name + ", reference: " + reference.toString() + ")>";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(reference, 0);
        parcel.writeString(name);
        parcel.writeLong(lastVisited.getTime());
    }

    public Book getBook() {
        return reference.getBook();
    }

    public Chapter getChapter() {
        return reference.getChapter();
    }

    public void setPosition(int position) {
        reference.setPosition(position);
    }

    public CharSequence getBookName() {
        return reference.getBookName();
    }

    public int getChapterIndex() {
        return reference.getChapterIndex();
    }

    public int getPosition() {
        return reference.getPosition();
    }

    public void setChapterIndex(int chapterIndex) {
        reference.setChapterIndex(chapterIndex);
    }

    public void updateIndices(Reference reference) {
        this.reference.setIndices(reference.getBookIndex(), reference.getChapterIndex());
    }
}
