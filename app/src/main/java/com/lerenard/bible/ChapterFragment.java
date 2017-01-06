package com.lerenard.bible;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by mc on 23-Dec-16.
 */

public class ChapterFragment extends Fragment {
    private static final String TAG = "ChapterFragment_";
    private Ribbon ribbon;

    public Ribbon getRibbon() {
        return ribbon;
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
        if (savedInstanceState != null) {
            ribbon = savedInstanceState.getParcelable(ReadingActivity.RIBBON_KEY);
        }
        View root = inflater.inflate(R.layout.fragment_reading, container, false);
        root.setTag(ribbon.getReference().getPosition());
        TextView text = ((TextView) root.findViewById(R.id.chapterText));
        text.setText(getChapterText(ribbon.getChapter()));

        return root;
    }

    public void setTranslation(Translation translation) {
        ribbon.setTranslation(translation);
    }

    private SpannableStringBuilder getChapterText(Chapter chapter) {
        ArrayList<Verse> verses = chapter.getVerses();
        SpannableStringBuilder builder = new SpannableStringBuilder();
        for (int i = 0; i < verses.size(); ++i) {
            Verse verse = verses.get(i);
            addSubscript(builder, i + 1);
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
    }
}
