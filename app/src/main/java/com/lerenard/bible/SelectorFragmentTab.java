package com.lerenard.bible;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by mc on 25-Dec-16.
 */

public class SelectorFragmentTab extends Fragment implements ReferenceSelectorItemSelectedListener {
    protected Reference reference;
    private ReferenceSelectorItemSelectedListener listener;

    @Override
    public void onChapterSelected(int chapterIndex) {
        if (listener != null) {
            listener.onChapterSelected(chapterIndex);
        }
    }

    @Override
    public void onBookSelected(int bookIndex) {
        if (listener != null) {
            listener.onBookSelected(bookIndex);
        }
    }

    public void setListener(ReferenceSelectorItemSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        reference = args.getParcelable(SelectorFragment.REFERENCE_KEY);
    }
}
