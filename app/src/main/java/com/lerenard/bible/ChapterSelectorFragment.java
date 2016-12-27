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
 * Created by mc on 25-Dec-16.
 */

public class ChapterSelectorFragment extends SelectorFragmentTab {

    private static final String TAG = "ChapterSelectorFrag_";

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        final View rootView =
                inflater.inflate(R.layout.fragment_chapter_selector, container, false);
        final RecyclerView recyclerView =
                (RecyclerView) rootView.findViewById(R.id.chapter_recycler_view);
        final float chapterSelectorDiameter =
                getActivity().getResources().getDimension(R.dimen.chapter_selector_diameter);
        final float chapterSelectorMargin =
                getActivity().getResources().getDimension(R.dimen.chapter_selector_margin);
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerViewItemDecorator(0));
        ChapterSelectorAdapter adapter =
                new ChapterSelectorAdapter(Book.getChapterCount(this.reference.getBookIndex()),
                                           this.reference.getChapter(), getContext(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        int usableWidth = rootView.getWidth();
                        int buttonWidth = (int) (chapterSelectorDiameter + (chapterSelectorMargin * 2));
                        int spanCount = usableWidth / buttonWidth;
                        int extra = usableWidth % buttonWidth;
                        layoutManager.setSpanCount(spanCount);
                        // hello
                        final ViewGroup.MarginLayoutParams params =
                                (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();
                        params.leftMargin = extra / 2;
                        recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        recyclerView.requestLayout();
                    }
                });
        return rootView;
    }
}
