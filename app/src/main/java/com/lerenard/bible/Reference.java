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

    private static Reference defaultReference = new Reference("Genesis", 1, 1);

    private static final int[] cumulativeChapterCount = {
            50, 90, 117, 153, 187, 211, 232, 236, 267, 291, 313, 338, 367, 403, 413, 426, 436, 478,
            628, 659, 671, 679, 745, 797, 802, 850, 862, 876, 879, 888, 889, 893, 900, 903, 906,
            909, 911, 925, 929, 957, 973, 997, 1018, 1046, 1062, 1078, 1091, 1097, 1103, 1107, 1111,
            1116, 1119, 1125, 1129, 1132, 1133, 1146, 1151, 1156, 1159, 1164, 1165, 1166, 1167,
            1189};

    public static Reference fromPosition(int position) {
        int index = Arrays.binarySearch(cumulativeChapterCount, position);
        if (index < 0) {  // the exact chapter count was not found
            /* converting to insertion point: the first index that held a value greater than
            position, also the index of the book it belongs in*/
            index = -(index + 1);
            if (index == cumulativeChapterCount.length) {
                return null;  // invalid position
            }
        }
        if (index == 0) {
            return new Reference(0, position, 1);
        }
        else {
            return new Reference(index, position - cumulativeChapterCount[index - 1], 1);
        }
    }

    protected Reference(Parcel in) {
        bookIndex = in.readInt();
        chapter = in.readInt();
        verse = in.readInt();
        bookName = allBooks.get(bookIndex);
        bookAbbreviation = abbreviations.get(bookIndex);
    }

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

    public Reference(Reference reference) {
        this(reference.bookIndex, reference.chapter, reference.verse);
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public int getVerse() {
        return verse;
    }

    public void setVerse(int verse) {
        this.verse = verse;
    }

    public int getBookIndex() {
        return bookIndex;
    }

    public void setBookIndex(int bookIndex) {
        this.bookIndex = bookIndex;
    }

    private String bookName, bookAbbreviation;
    private int bookIndex;
    private int chapter, verse;

    public Reference(String bookName, int chapter, int verse) {
        this(chapter, verse);
        this.bookName = bookName;
        bookIndex = allBooks.indexOf(bookName);
        if (bookIndex == -1) {
            bookIndex = abbreviations.indexOf(bookName);
            if (bookIndex == -1) {
                throw new IllegalArgumentException(
                        "I can't find a bookName with the name " + bookName);
            }
            else {
                bookAbbreviation = bookName;
                this.bookName = allBooks.get(bookIndex);
            }
        }
    }

    private Reference(int chapter, int verse) {
        this.chapter = chapter;
        this.verse = verse;
    }

    public Reference(String bookName, int bookIndex, int chapter, int verse) {
        this(bookIndex, chapter, verse);
        if (!bookName.equals(this.bookName)) {
            if (!bookName.equals(this.bookAbbreviation)) {
                throw new IllegalArgumentException(
                        "The bookName " + bookName + " does not match the given bookIndex of " +
                        bookIndex +
                        ". Expected value was one of " + this.bookName + " and " +
                        abbreviations.get(bookIndex));
            }
        }
    }

    public Reference(int bookIndex, int chapter, int verse) {
        this(chapter, verse);
        this.bookIndex = bookIndex;
        this.bookName = allBooks.get(bookIndex);
        this.bookAbbreviation = abbreviations.get(bookIndex);
    }

    public Reference() {}

    @Override
    public String toString() {
        return bookName + " " + String.valueOf(chapter) + ":" + String.valueOf(verse);
    }

    public static Reference parseString(String reference) {
        return null;
        // TODO
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(bookIndex);
        parcel.writeInt(chapter);
        parcel.writeInt(verse);
    }

    public static Reference getDefault() {
        return defaultReference;
    }

    public static int getTotalChapterCount() {
        return cumulativeChapterCount[cumulativeChapterCount.length - 1];
    }

    /**
     * @return the index of this reference's chapter in an ordered list of all chapters from
     * Genesis 1 to the last chapter of Revelation.
     */
    public int position() {
        if (bookIndex == 0) {
            return chapter;
        }
        return cumulativeChapterCount[bookIndex - 1] + chapter;
    }
}
