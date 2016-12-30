package com.lerenard.bible;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.Log;

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
    private long _id;
    private String name;
    private CharSequence lastVisitedText;
    private Date lastVisited;

    public Ribbon(Ribbon ribbon) {
        this(ribbon._id, new Reference(ribbon.reference), ribbon.name, ribbon.getLastVisited());
    }

    public Ribbon(long id, Reference reference, String name, Date lastVisited) {
        this._id = id;
        this.reference = reference;
        this.name = name;
        setLastVisited(lastVisited);
    }

    public Date getLastVisited() {
        return lastVisited;
    }

    private void setLastVisited(Date date) {
        lastVisited = date;
        if (date.getTime() == 0) {
            lastVisitedText = null;
        }
        else {
            lastVisitedText = DateFormat.format(dateFormatString, lastVisited);
        }
    }

    protected Ribbon(Parcel in) {
        _id = in.readLong();
        reference = in.readParcelable(Reference.class.getClassLoader());
        name = in.readString();
        setLastVisited(new Date(in.readLong()));
    }

    public Ribbon() {
        this(-1, new Reference(Reference.getDefault()), null, new Date(0));
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        _id = id;
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
        parcel.writeLong(_id);
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

    public CharSequence getBookName() {
        return reference.getBookName();
    }

    public int getChapterIndex() {
        return reference.getChapterIndex();
    }

    public void setChapterIndex(int chapterIndex) {
        reference.setChapterIndex(chapterIndex);
    }

    public int getPosition() {
        return reference.getPosition();
    }

    public void setPosition(int position) {
        reference.setPosition(position);
    }

    public void updateIndices(Reference reference) {
        this.reference.setIndices(reference.getBookIndex(), reference.getChapterIndex());
    }

    public void setLastVisitedToNow() {
        setLastVisited(new Date(System.currentTimeMillis()));
    }
}
