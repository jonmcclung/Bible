package com.lerenard.bible;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * Created by mc on 10-Jan-17.
 */

public class VerseSelectorFragment extends SelectorFragmentTab {

    private static final String TAG = "VerseSelectorFrag_";
    private VerseSelectorAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            restoreArguments(savedInstanceState);
        }
        final View rootView =
                inflater.inflate(R.layout.fragment_verse_selector, container, false);

        final RecyclerView recyclerView =
                (RecyclerView) rootView.findViewById(R.id.verse_recycler_view);

        final float chapterSelectorDiameter =
                getActivity().getResources().getDimension(R.dimen.chapter_selector_diameter);
        final float chapterSelectorMargin =
                getActivity().getResources().getDimension(R.dimen.chapter_selector_margin);

        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new VerseSelectorAdapter(
                reference.getChapter().getVerseCount(),
                reference.getVerseIndex(),
                getContext());
        adapter.setListener(listener);
        recyclerView.setAdapter(adapter);
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        int usableWidth = rootView.getWidth();
                        int bw = recyclerView.getWidth() / layoutManager.getSpanCount();
                        int buttonWidth =
                                (int) (chapterSelectorDiameter + (chapterSelectorMargin * 2));
                        int spanCount = usableWidth / buttonWidth;
                        int extra = usableWidth % buttonWidth;
                        layoutManager.setSpanCount(spanCount);
                        final ViewGroup.MarginLayoutParams params =
                                (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();
                        params.leftMargin = extra / 2;

                        layoutManager.scrollToPositionWithOffset(
                                reference.getVerseIndex() - 1,
                                layoutManager.getHeight() / 2);

                        recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        recyclerView.requestLayout();
                    }
                });
        return rootView;
    }

    @Override
    public void setListener(ReferenceSelectorItemSelectedListener listener) {
        super.setListener(listener);
        if (adapter != null) {
            adapter.setListener(listener);
        }
    }

    public void updateReference(Reference reference) {
        adapter.setCount(reference.getChapter().getVerseCount(), reference.getVerseIndex());
    }

}
