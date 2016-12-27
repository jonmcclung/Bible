package com.lerenard.bible;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mc on 25-Dec-16.
 */

public class BookSelectorFragment extends SelectorFragmentTab {

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_selector, container, false);
        //TODO
        return rootView;
    }
}
