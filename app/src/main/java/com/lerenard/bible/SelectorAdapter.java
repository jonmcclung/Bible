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
    private final Reference reference;

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

    public SelectorAdapter(FragmentManager fm, Reference reference) {
        super(fm);
        this.reference = reference;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putParcelable(REFERENCE_KEY, reference);
        Fragment res;
        switch (SelectorPosition.values()[position]) {
            case BOOK_POSITION:
                res = new BookSelectorFragment();
                break;
            case CHAPTER_POSITION:
                res = new ChapterSelectorFragment();
                break;
            default:
                throw new IllegalStateException("unexpected position: " + position);
        }
        res.setArguments(args);
        return res;
    }

    @Override
    public int getCount() {
        return SelectorPosition.values().length;
    }
}
