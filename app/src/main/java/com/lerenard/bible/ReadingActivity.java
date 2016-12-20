package com.lerenard.bible;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class ReadingActivity extends AppCompatActivity {
    public static final String RIBBON_KEY = "RIBBON_KEY";
    private static final String TAG = "ReadingActivity_";
    private LinearLayout chapterText;
    private LayoutInflater inflater;

    private LinearLayout getRow() {
        return (LinearLayout) inflater.inflate(R.layout.verse_row, chapterText, false);
    }
/*
    class VerseView {

        private int number;

        public VerseView(TextView textView, int number, String rawText) {
            this.number = number;
            addSubscript(textView);
            textView.append(rawText);
        }

        public VerseView(LinearLayout currentRow, int number, String rawText) {
            this.number = number;
            char[] text = rawText.toCharArray();

            // start represents the part that continues on the last line we left off at.
            TextView middle, end,
                    start = getViewInRow(currentRow);
            currentRow.addView(start);

            start.setMaxLines(1);
            addSubscript(start);

            int length = text.length;
            start.setText(text, 0, length);
            length = start.getLayout().getEllipsisStart(0);
            // lastIndexOf is inclusive
            int lastSpaceIndex = rawText.lastIndexOf(' ', length);

            if (lastSpaceIndex == -1) {
                currentRow.removeView(start);
                lastSpaceIndex = 0;
                middle = start; // puts subscript on middle
                middle.setMaxLines(Integer.MAX_VALUE); // restores max lines
            }
            else {
                start.setText(text, 0, lastSpaceIndex);
//                views.add(start);
                middle = getViewInRow(currentRow);
                middle.setText(text, lastSpaceIndex, text.length - lastSpaceIndex);
            }

            currentRow = getRow();
            chapterText.addView(currentRow);
            currentRow.addView(middle);

            int lineCount = middle.getLineCount();
            if (lineCount != 1) {
                int endOfMiddle = middle.getLayout().getLineEnd(lineCount - 2);
                middle.setText(text, lastSpaceIndex, endOfMiddle - lastSpaceIndex);
//                views.add(middle);

                currentRow = getRow();
                chapterText.addView(currentRow);
                end = getViewInRow(currentRow);
                currentRow.addView(end);
                end.setText(text, endOfMiddle, text.length - endOfMiddle);
//                views.add(end);
            }

        }*/

      /*  private TextView getViewInRow(LinearLayout currentRow) {
            return (TextView) inflater.inflate(
                    R.layout.verse_text_view, currentRow, false);
        }*/
/*

        public ArrayList<TextView> getViews() {
            return views;
        }

        public TextView getStart() {
            return views.get(0);
        }

        public TextView getMiddle() {
            if (views.size() == 3) {
                return views.get(1);
            }
            return null;
        }

        public TextView getEnd() {
            switch (views.size()) {
                case 3:
                    return views.get(2);
                case 2:
                    return views.get(1);
                default:
                    return null;
            }
        }
*/

//    }

    private void addSubscript(TextView view, int number) {
        SpannableStringBuilder builder = new SpannableStringBuilder(
                String.format(
                        Locale.getDefault(),
                        "%d",
                        number));
        builder.setSpan(
                new TextAppearanceSpan(getApplicationContext(), R.style.verseNumberStyle),
                0,
                builder.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.append(builder);
    }

    private void initializeText(Chapter chapter) {
        ArrayList<Verse> verses = chapter.getVerses();
        chapterText.removeAllViews();
        final LinearLayout currentRow = getRow();
        chapterText.addView(currentRow);
        TextView textView = (TextView) inflater.inflate(
                R.layout.verse_text_view, currentRow, false);
        currentRow.addView(textView);

        for (int i = 0; i < verses.size(); ++i) {
            Verse verse = verses.get(i);
            addSubscript(textView, i + 1);
            textView.append(getString(R.string.spacing_between_number_and_verse));
            textView.append(verse.getText());
            textView.append(getString(R.string.spacing_between_verse_and_number));
        }


        /*for (int i = 0; i < verses.size(); ++i) {
            VerseView verseView = new VerseView(
                    (LinearLayout) chapterText.getChildAt(chapterText.getChildCount() - 1), i + 1,
                    verses.get(i).getText());

        }*/

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        inflater = LayoutInflater
                .from(getApplicationContext());

        chapterText = (LinearLayout) findViewById(R.id.chapterText);

        Bundle extras = getIntent().getExtras();

        Ribbon ribbon = extras.getParcelable(RIBBON_KEY);
        Translation translation = ribbon.getTranslation();
        Reference reference = ribbon.getReference();

        Chapter chapter =
                translation.getBook(reference.getBookIndex()).getChapter(reference.getChapter());

        initializeText(chapter);
    }
}
