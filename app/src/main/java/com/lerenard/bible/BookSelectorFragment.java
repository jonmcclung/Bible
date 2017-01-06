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

public class BookSelectorFragment extends SelectorFragmentTab {


    public static final String TAG = "BookSelFrag_";
    private BookSelectorAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            restoreArguments(savedInstanceState);
        }

        final View rootView = inflater.inflate(R.layout.fragment_book_selector, container, false);
        final RecyclerView recyclerView =
                (RecyclerView) rootView.findViewById(R.id.book_recycler_view);

        final float bookSelectorWidth =
                getActivity().getResources().getDimension(R.dimen.book_selector_button_width);
        final float bookSelectorMargin =
                getActivity().getResources().getDimension(R.dimen.book_selector_margin);

        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new BookSelectorAdapter(reference.getTranslation(),
                                          reference.getBookIndex(), getContext());
        adapter.setListener(listener);
        recyclerView.setAdapter(adapter);
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        int usableWidth = rootView.getWidth();
                        int buttonWidth =
                                (int) (bookSelectorWidth + (bookSelectorMargin * 2));
                        int spanCount = usableWidth / buttonWidth;
                        int extra = usableWidth % buttonWidth;
                        layoutManager.setSpanCount(spanCount);
                        final ViewGroup.MarginLayoutParams params =
                                (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();
                        params.leftMargin = extra / 2;
                        /*Log.d(
                                TAG,
                                "usableWidth: " + usableWidth + ", buttonWidth: " + buttonWidth +
                                ", bookSelectorWidth: " + bookSelectorWidth +
                                ", bookSelectorMargin: " + bookSelectorMargin + ", extra: " +
                                extra + ", params.leftMargin: " + params.leftMargin);*/
                        recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        layoutManager.scrollToPositionWithOffset(
                                reference.getBookIndex(),
                                layoutManager.getHeight() / 2);
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
}
