package com.lerenard.bible;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mc on 18-Dec-16.
 */
public class Reference {
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
                throw new IllegalArgumentException("I can't find a bookName with the name " + bookName);
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
                        "The bookName " + bookName + " does not match the given bookIndex of " + bookIndex +
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


}
