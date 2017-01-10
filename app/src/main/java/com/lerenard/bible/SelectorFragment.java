package com.lerenard.bible;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by mc on 25-Dec-16.
 */

public class SelectorFragment extends Fragment implements ReferenceSelectorItemSelectedListener {

    public static final String
            SELECTOR_POS_KEY = "SELECTOR_POS_KEY",
            REFERENCE_KEY = "REFERENCE_KEY";
    private static final String TAG = "SelectorFragment";

    private Reference reference;
    private SelectorPosition selectorPosition;
    private ReferenceSelectorItemSelectedListener listener;
    private SelectorAdapter adapter;
    private ViewPager pager;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        restoreArguments(args);
    }

    private void scrollTo(SelectorPosition selectorPosition) {
        this.selectorPosition = selectorPosition;
        pager.setCurrentItem(selectorPosition.ordinal());
    }

    private void restoreArguments(Bundle args) {
        reference = args.getParcelable(REFERENCE_KEY);
        selectorPosition = (SelectorPosition) args.getSerializable(SELECTOR_POS_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            restoreArguments(savedInstanceState);
        }


        View rootView = inflater.inflate(R.layout.fragment_reference_selector, container, false);

        TextView goButton = (TextView) rootView.findViewById(R.id.go_button);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });

        pager = (ViewPager) rootView.findViewById(R.id.reference_selector_pager);

        adapter = new SelectorAdapter(getFragmentManager(), reference);
        adapter.setListener(this);
        pager.setAdapter(adapter);
        pager.setCurrentItem(selectorPosition.ordinal());

        final TabLayout tab = (TabLayout) rootView.findViewById(R.id.selector_tab);
        tab.setupWithViewPager(pager, true);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SELECTOR_POS_KEY, selectorPosition);
        outState.putParcelable(REFERENCE_KEY, reference);
    }

    public void setListener(ReferenceSelectorItemSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onVerseSelected(int verseIndex) {
        reference.setVerse(verseIndex);
        if (listener != null) {
            listener.onVerseSelected(verseIndex);
            listener.submit();
        }
    }

    @Override
    public void onChapterSelected(int chapterIndex) {
        reference.setChapterIndex(chapterIndex);
        if (listener != null) {
            listener.onChapterSelected(chapterIndex);
        }
        scrollTo(SelectorPosition.VERSE_POSITION);
    }

    @Override
    public void onBookSelected(int bookIndex) {
        reference.setBookIndex(bookIndex);
        adapter.updateReference(reference);
        if (listener != null) {
            listener.onBookSelected(bookIndex);
        }
        scrollTo(SelectorPosition.CHAPTER_POSITION);
    }

    @Override
    public void submit() {
        if (listener != null) {
            listener.submit();
        }
    }

    public Reference getReference() {
        return reference;
    }
}


