package com.lerenard.bible;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import static com.lerenard.bible.SelectorFragment.REFERENCE_KEY;

/**
 * Created by mc on 25-Dec-16.
 */

public class SelectorAdapter extends FragmentPagerAdapter {
    private final BookSelectorFragment bookSelectorFragment;
    private final ChapterSelectorFragment chapterSelectorFragment;


    public SelectorAdapter(FragmentManager fm, Reference reference) {
        super(fm);
        Bundle args = new Bundle();
        args.putParcelable(REFERENCE_KEY, reference);
        bookSelectorFragment = new BookSelectorFragment();
        chapterSelectorFragment = new ChapterSelectorFragment();
        bookSelectorFragment.setArguments(args);
        chapterSelectorFragment.setArguments(args);
    }

    @Override
    public Fragment getItem(int position) {
        switch (SelectorPosition.values()[position]) {
            case BOOK_POSITION:
                return bookSelectorFragment;
            case CHAPTER_POSITION:
                return chapterSelectorFragment;
            default:
                throw new IllegalStateException("unexpected position: " + position);
        }
    }

    @Override
    public int getCount() {
        return SelectorPosition.values().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (SelectorPosition.values()[position]) {
            case BOOK_POSITION:
                return "Book";
            case CHAPTER_POSITION:
                return "Chapter";
            default:
                throw new IllegalStateException("unexpected position: " + position);
        }
    }

    public void setListener(ReferenceSelectorItemSelectedListener listener) {
        bookSelectorFragment.setListener(listener);
        chapterSelectorFragment.setListener(listener);
    }
}
