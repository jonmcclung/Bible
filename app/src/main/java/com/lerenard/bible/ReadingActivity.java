package com.lerenard.bible;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.SubscriptSpan;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReadingActivity extends AppCompatActivity {

    public static final String
            TRANSLATION_KEY = "READING_ACTIVITY_TRANSLATION_KEY",
            REFERENCE_KEY = "READING_ACTIVITY_REFERENCE_KEY";
    private LinearLayout chapterText;
    private LayoutInflater inflater;

    class VerseView {

        //        private ArrayList<TextView> views = new ArrayList<>(3);
        private int endOffset;
        private int number;

        public VerseView(LinearLayout currentRow, int number, String rawText) {
            this.number = number;
            char[] text = rawText.toCharArray();

            // start represents the part that continues on the last line we left off at.
            TextView middle, end,
                    start = (TextView) inflater
                            .inflate(R.layout.verse_text_view, currentRow, false);
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
                middle = (TextView) inflater.inflate(R.layout.verse_text_view, currentRow, false);
                middle.setText(text, lastSpaceIndex, text.length - lastSpaceIndex);
            }

            currentRow = (LinearLayout) inflater.inflate(R.layout.verse_row, chapterText, false);
            chapterText.addView(currentRow);
            currentRow.addView(middle);

            int lineCount = middle.getLineCount();
            if (lineCount != 1) {
                int endOfMiddle = middle.getLayout().getLineEnd(lineCount - 2);
                middle.setText(text, lastSpaceIndex, endOfMiddle - lastSpaceIndex);
//                views.add(middle);

                currentRow =
                        (LinearLayout) inflater.inflate(R.layout.verse_row, chapterText, false);
                chapterText.addView(currentRow);
                end = (TextView) inflater.inflate(R.layout.verse_text_view, currentRow, false);
                currentRow.addView(end);
                end.setText(text, endOfMiddle, text.length - endOfMiddle);
//                views.add(end);
            }

        }

        private String addSubscript(TextView view) {

            SpannableStringBuilder builder =
                    new SpannableStringBuilder(String.format(Locale.getDefault(), "%d", number));
            builder.setSpan(
                    new SubscriptSpan(), 0, builder.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(view.getText());
            return builder.toString();
        }
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

    }

    private void initializeText(Chapter chapter) {
        ArrayList<Verse> verses = chapter.getVerses();
        for (int i = 0; i < verses.size(); ++i) {
            VerseView verseView = new VerseView(
                    (LinearLayout) chapterText.getChildAt(chapterText.getChildCount() - 1), i + 1,
                    verses.get(i).getText());

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        inflater = LayoutInflater
                .from(getApplicationContext());

        chapterText = (LinearLayout) findViewById(R.id.chapterText);

        Bundle extras = getIntent().getExtras();

        Translation translation = extras.getParcelable(TRANSLATION_KEY);
        Reference reference = extras.getParcelable(REFERENCE_KEY);


        Chapter chapter =
                translation.getBook(reference.getBookIndex()).getChapter(reference.getChapter());

        initializeText(chapter);
    }
}
