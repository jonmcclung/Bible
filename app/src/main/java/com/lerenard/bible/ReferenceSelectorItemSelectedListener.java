package com.lerenard.bible;

/**
 * Created by mc on 26-Dec-16.
 */

public interface ReferenceSelectorItemSelectedListener {
    void onVerseSelected(int verseIndex);

    void onChapterSelected(int chapterIndex);

    void onBookSelected(int bookIndex);

    void submit();
}
