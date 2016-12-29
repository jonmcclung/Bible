package com.lerenard.bible;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by mc on 25-Dec-16.
 */

public class SelectorFragmentTab extends Fragment implements ReferenceSelectorItemSelectedListener {
    protected Reference reference;
    private ReferenceSelectorItemSelectedListener listener;

    @Override
    public void onChapterSelected(int chapterIndex) {
        reference.setChapterIndex(chapterIndex);
        if (listener != null) {
            listener.onChapterSelected(chapterIndex);
        }
    }

    @Override
    public void onBookSelected(int bookIndex) {
        reference.setBookIndex(bookIndex);
        if (listener != null) {
            listener.onBookSelected(bookIndex);
        }
    }

    @Override
    public void submit() {
        if (listener != null) {
            listener.submit();
        }
    }

    public void setListener(ReferenceSelectorItemSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        restoreArguments(args);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SelectorFragment.REFERENCE_KEY, reference);
    }

    protected void restoreArguments(Bundle args) {
        reference = args.getParcelable(SelectorFragment.REFERENCE_KEY);
    }
}
