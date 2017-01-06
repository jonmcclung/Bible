package com.lerenard.bible;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

/**
 * Created by mc on 25-Dec-16.
 */

public class SelectorFragmentTab extends Fragment {
    protected Reference reference;
    protected ReferenceSelectorItemSelectedListener listener;

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
