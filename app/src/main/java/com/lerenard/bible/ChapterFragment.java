package com.lerenard.bible;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 * Created by mc on 23-Dec-16.
 */

public class ChapterFragment extends Fragment {
    private static final String TAG = "ChapterFragment_";
    private static final String VERSE_OFFSETS_KEY = "VERSE_OFFSETS_KEY";
    private static final String TEXT_KEY = "TEXT_KEY";
    private static final String CREATED_KEY = "CREATED_KEY";
    private Ribbon ribbon;
    private ArrayList<Integer> verseOffsets;
    private OnFragmentCreatedListener<ChapterFragment> listener;
    private boolean created;
    private TextView text;
    private CharSequence chapterText;

    @Override
    public String toString() {
        return super.toString() + ", " + ribbon;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        if (args != null) {
            ribbon = args.getParcelable(ReadingActivity.RIBBON_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
//        setRetainInstance(true);
        if (savedInstanceState != null) {
            ribbon = savedInstanceState.getParcelable(ReadingActivity.RIBBON_KEY);
            verseOffsets = savedInstanceState.getIntegerArrayList(VERSE_OFFSETS_KEY);
            chapterText = savedInstanceState.getCharSequence(TEXT_KEY);
            created = savedInstanceState.getBoolean(CREATED_KEY);
        }
        else {
            verseOffsets = new ArrayList<>();
            chapterText = null;
            created = false;
        }

        final View root = inflater.inflate(R.layout.fragment_reading, container, false);
        root.setTag(ribbon.getReference().getPosition());
        text = ((TextView) root.findViewById(R.id.chapterText));
        if (chapterText != null) {
            text.setText(chapterText);
        }
        else {
            initializeText();
        }
        if (listener != null) {
            Log.d(TAG, this + "notifying inside onCreateView");
            listener.onFragmentCreated(this);
        }
        else {
            Log.d(TAG, this + "no listener yet, will notify when I get one.");
        }
        created = true;
        int padding = (int) getResources().getDimension(R.dimen.activity_reading_padding);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Log.d(TAG, "setting height to " + displayMetrics.heightPixels);
        root.setPadding(padding, padding, padding, displayMetrics.heightPixels);
        return root;
    }

    private void initializeText() {
        text.setText(getChapterText(ribbon.getChapter()));
    }

    private SpannableStringBuilder getChapterText(Chapter chapter) {
        Log.d(TAG, "getChapterText");
        verseOffsets.clear();
        ArrayList<Verse> verses = chapter.getVerses();
        SpannableStringBuilder builder = new SpannableStringBuilder();
        int verseIndex = 0;
        while (verseIndex < verses.size()) {
            verseOffsets.add(builder.length());
            Verse verse = verses.get(verseIndex);
            addSubscript(builder, ++verseIndex);
            builder.append(getString(R.string.spacing_between_number_and_verse));
            builder.append(verse.getText());
            builder.append(getString(R.string.spacing_between_verse_and_number));
        }
        return builder;
    }

    private void addSubscript(SpannableStringBuilder builder, int number) {
        int oldLength = builder.length();
        builder.append(String.format(
                Locale.getDefault(),
                "%d",
                number));

        builder.setSpan(
                new TextAppearanceSpan(getContext(), R.style.verseNumberStyle),
                oldLength,
                builder.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ReadingActivity.RIBBON_KEY, ribbon);
        outState.putIntegerArrayList(VERSE_OFFSETS_KEY, verseOffsets);
        outState.putCharSequence(TEXT_KEY, text.getText());
        outState.putBoolean(CREATED_KEY, created);
    }

    public void setOnCreatedListener(OnFragmentCreatedListener<ChapterFragment> listener) {
        this.listener = listener;
        if (created) {
            Log.d(
                    TAG, this +
                         "didn't have a chance to notify when I was created, but better late than" +
                         " never.");
            listener.onFragmentCreated(this);
        }
        else {
            if (text != null && text.getLayout() != null) {
                Log.d(TAG, "wtf");
            }
            Log.d(
                    TAG,
                    this + "I haven't been created yet, so I'll wait until that happens to notify");
        }
    }

    public int setAndGetVerseIndex(int y) {
        if (text == null || text.getLayout() == null) {
            return -1;
        }
        int line = text.getLayout().getLineForVertical(y);
        int offset = text.getLayout().getLineStart(line);
        int verseIndex = Collections.binarySearch(verseOffsets, offset);
        if (verseIndex < 0) {
            verseIndex = -verseIndex;
        }
        else {
            ++verseIndex;
        }
        if (verseIndex > verseOffsets.size()) {
            verseIndex = verseOffsets.size();
        }
        // verseIndex represents the smallest i such that verseOffsets[i] >= offset
        ribbon.setVerseIndex(verseIndex);
        return verseIndex;
    }

    public TextView getText() {
        return text;
    }

    public Ribbon getRibbon() {
        return ribbon;
    }

    public int getPreferredOffset() {
        if (text == null || text.getLayout() == null) {
            Log.d(TAG, "I thought this stuff wouldn't be null, but it is... " + this);
            return -1;
        }
        else {
            return text.getLayout().getLineTop(text.getLayout().getLineForOffset(
                    verseOffsets.get(ribbon.getVerseIndex() - 1)));
        }
    }

    public void setTranslation(Translation translation) {
        ribbon.setTranslation(translation);
    }
}
