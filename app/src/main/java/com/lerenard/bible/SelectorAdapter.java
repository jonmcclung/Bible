package com.lerenard.bible;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import static com.lerenard.bible.SelectorFragment.REFERENCE_KEY;

/**
 * Created by mc on 25-Dec-16.
 */

public class SelectorAdapter extends FragmentPagerAdapter {
    private static final String TAG = "SelectorAdapter_";
    private final Bundle args;
    private BookSelectorFragment bookSelectorFragment;
    private ChapterSelectorFragment chapterSelectorFragment;
    private VerseSelectorFragment verseSelectorFragment;
    private ReferenceSelectorItemSelectedListener listener;


    public SelectorAdapter(FragmentManager fm, Reference reference) {
        super(fm);
        args = new Bundle();
        args.putParcelable(REFERENCE_KEY, reference);
    }

    @Override
    public Fragment getItem(int position) {
        SelectorFragmentTab fragment;
        switch (SelectorPosition.values()[position]) {
            case BOOK_POSITION:
                fragment = new BookSelectorFragment();
                break;
            case CHAPTER_POSITION:
                fragment = new ChapterSelectorFragment();
                break;
            case VERSE_POSITION:
                fragment = new VerseSelectorFragment();
                break;
            default:
                throw new IllegalStateException("unexpected position: " + position);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        SelectorFragmentTab fragment =
                (SelectorFragmentTab) super.instantiateItem(container, position);
        switch (SelectorPosition.values()[position]) {
            case BOOK_POSITION:
                bookSelectorFragment = (BookSelectorFragment) fragment;
                break;
            case CHAPTER_POSITION:
                chapterSelectorFragment =
                        (ChapterSelectorFragment) fragment;
                break;
            case VERSE_POSITION:
                verseSelectorFragment = (VerseSelectorFragment) fragment;
                break;
            default:
                throw new IllegalStateException("unexpected position: " + position);
        }
        fragment.setListener(listener);
        return fragment;
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
            case VERSE_POSITION:
                return "Verse";
            default:
                throw new IllegalStateException("unexpected position: " + position);
        }
    }

    public void setListener(ReferenceSelectorItemSelectedListener listener) {
        this.listener = listener;
        if (bookSelectorFragment != null) {
            bookSelectorFragment.setListener(listener);
        }
        if (chapterSelectorFragment != null) {
            chapterSelectorFragment.setListener(listener);
        }
        if (verseSelectorFragment != null) {
            verseSelectorFragment.setListener(listener);
        }
    }

    public void updateReference(Reference reference) {
        if (chapterSelectorFragment != null) {
            chapterSelectorFragment.updateReference(reference);
        }
        if (verseSelectorFragment != null) {
            verseSelectorFragment.updateReference(reference);
        }
    }
}
