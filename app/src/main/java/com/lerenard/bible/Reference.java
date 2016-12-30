package com.lerenard.bible;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mc on 18-Dec-16.
 */
public class Reference implements Parcelable {
    public static final List<String> allBooks = Arrays.asList(
            "Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy", "Joshua", "Judges",
            "Ruth", "1 Samuel", "2 Samuel", "1 Kings", "2 Kings", "1 Chronicles",
            "2 Chronicles", "Ezra", "Nehemiah", "Esther", "Job", "Psalms", "Proverbs",
            "Ecclesiastes", "Song of Solomon", "Isaiah", "Jeremiah", "Lamentations", "Ezekiel",
            "Daniel", "Hosea", "Joel", "Amos", "Obadiah", "Jonah", "Micah", "Nahum", "Habakkuk",
            "Zephaniah", "Haggai", "Zechariah", "Malachi", "Matthew", "Mark", "Luke", "John",
            "Acts", "Romans", "1 Corinthians", "2 Corinthians", "Galatians", "Ephesians",
            "Philippians", "Colossians", "1 Thessalonians", "2 Thessalonians", "1 Timothy",
            "2 Timothy", "Titus", "Philemon", "Hebrews", "James", "1 Peter", "2 Peter",
            "1 John", "2 John", "3 John", "Jude", "Revelation"
    );

    public static final List<String> abbreviations = Arrays.asList(
            "Gen", "Exod", "Lev", "Num", "Deut", "Josh", "Judg", "Ruth", "1 Sam",
            "2 Sam", "1 Kgs", "2 Kgs", "1 Chr", "2 Chr", "Ezra", "Neh",
            "Esth", "Job", "Ps", "Prov", "Ecc", "Song", "Is", "Jer",
            "Lam", "Ezk", "Dan", "Hos", "Joel", "Amos", "Obad", "Jonah", "Micah",
            "Nah", "Hab", "Zeph", "Hag", "Zech", "Mal", "Matt", "Mark", "Luke",
            "John", "Acts", "Rom", "1 Cor", "2 Cor", "Gal", "Eph", "Phlp",
            "Col", "1 Th", "2 Th", "1 Tim", "2 Tim", "Titus", "Phlm",
            "Heb", "James", "1 Pet", "2 Pet", "1 Jn", "2 Jn", "3 Jn", "Jude", "Rev");

    public static final Creator<Reference> CREATOR = new Creator<Reference>() {
        @Override
        public Reference createFromParcel(Parcel in) {
            return new Reference(in);
        }

        @Override
        public Reference[] newArray(int size) {
            return new Reference[size];
        }
    };

    private static final int[] cumulativeChapterCount = {
            50, 90, 117, 153, 187, 211, 232, 236, 267, 291, 313, 338, 367, 403, 413, 426, 436, 478,
            628, 659, 671, 679, 745, 797, 802, 850, 862, 876, 879, 888, 889, 893, 900, 903, 906,
            909, 911, 925, 929, 957, 973, 997, 1018, 1046, 1062, 1078, 1091, 1097, 1103, 1107, 1111,
            1116, 1119, 1125, 1129, 1132, 1133, 1146, 1151, 1156, 1159, 1164, 1165, 1166, 1167,
            1189};


    private Translation translation;
    private int position;
    private int bookIndex;
    private int chapterIndex, verse;

    protected Reference(Parcel in) {
        bookIndex = in.readInt();
        chapterIndex = in.readInt();
        verse = in.readInt();
        translation = in.readParcelable(Translation.class.getClassLoader());
        updatePosition();
    }

    /**
     * updates position from indices
     */
    private void updatePosition() {
        if (bookIndex == 0) {
            position = chapterIndex - 1;
        }
        else {
            position = cumulativeChapterCount[bookIndex - 1] + chapterIndex - 1;
        }
    }

    public Reference(Reference reference) {
        this(reference.bookIndex, reference.chapterIndex, reference.verse, reference.translation);
    }

    public Reference(int bookIndex, int chapterIndex, int verse, Translation translation) {
        this(chapterIndex, verse, translation);
        this.bookIndex = bookIndex;
        updatePosition();
    }

    private Reference(
            int chapterIndex, int verse, Translation translation) {
        this.chapterIndex = chapterIndex;
        this.verse = verse;
        this.translation = translation;
    }

    public Reference(String bookName, int chapterIndex, int verse, Translation translation) {
        this(chapterIndex, verse, translation);
        bookIndex = Book.indexFromName(bookName);
        updatePosition();
    }
/*
    public static Reference fromPosition(int position) {
        return fromPosition(position, null);
    }

    public static Reference fromPosition(int position, Translation translation) {
    }*/

    public static Reference parseString(String reference) {
        return null;
        // TODO
    }

    public static Reference getDefault() {
        return new Reference("Genesis", 1, 1, Translation.getDefault());
    }

    public static int getTotalChapterCount() {
        return cumulativeChapterCount[cumulativeChapterCount.length - 1];
    }

    public Translation getTranslation() {
        return translation;
    }

    public void setTranslation(Translation translation) {
        this.translation = translation;
    }

    public String getBookName() {
        return allBooks.get(bookIndex);
    }

    public int getChapterIndex() {
        return chapterIndex;
    }

    public void setChapterIndex(int chapter) {
        this.chapterIndex = chapter;
        setVerse(Math.min(verse, getChapter().getVerseCount()));
        updatePosition();
    }

    public Chapter getChapter() {
        return getBook().getChapter(chapterIndex);
    }

    public void setChapter(Chapter chapter) {
        setChapterIndex(chapter.getIndex());
    }

    public int getVerse() {
        return verse;
    }

    public void setVerse(int verse) {
        if (verse < 1 || verse > getChapter().getVerseCount()) {
            throw new IllegalArgumentException(
                    "verse must be between 1 and the number of verses in the chapter. Book: " +
                    getBookName() + ", Chapter: " + chapterIndex + ":" + verse);
        }
        this.verse = verse;
    }

    public int getBookIndex() {
        return bookIndex;
    }

    public void setBookIndex(int bookIndex) {
        this.bookIndex = bookIndex;
        setChapterIndex(Math.min(chapterIndex, translation.getBook(bookIndex).getChapterCount()));
        updatePosition();
    }

    @Override
    public String toString() {
        return getUIString() + ", translation: " + translation + ", position: " + position + "";
    }

    public String getUIString() {
        return getBookName() + " " + String.valueOf(chapterIndex) + ":" + String.valueOf(verse);
    }

    public Book getBook() {
        return translation.getBook(bookIndex);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(bookIndex);
        parcel.writeInt(chapterIndex);
        parcel.writeInt(verse);
        parcel.writeParcelable(translation, 0);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        updateIndices();
    }

    public void setIndices(int bookIndex, int chapterIndex) {
        setBookIndex(bookIndex);
        this.chapterIndex = chapterIndex;
        updatePosition();
    }

    /**
     * updates indices from position
     */
    private void updateIndices() {
        bookIndex = Arrays.binarySearch(cumulativeChapterCount, position);
        if (bookIndex >=
            0) {  // it is the first chapterIndex of the next book, so increment bookIndex
            ++bookIndex;
        }
        else {  // the exact chapterIndex count was not found
            /* converting to insertion point: the first bookIndex that held a value greater than
            calculatePosition, also the bookIndex of the book it belongs in*/
            bookIndex = -(bookIndex + 1);
            if (bookIndex == cumulativeChapterCount.length) {
                throw new IllegalStateException(
                        "bookIndex must not be equal to the last bookIndex. Index: " + bookIndex +
                        ", max: " + cumulativeChapterCount.length);  // invalid position
            }
        }
        chapterIndex = position + 1 - (bookIndex == 0 ? 0 : cumulativeChapterCount[bookIndex - 1]);
    }
}
