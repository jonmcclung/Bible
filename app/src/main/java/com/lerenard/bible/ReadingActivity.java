package com.lerenard.bible;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Locale;

public class ReadingActivity extends AppCompatActivity {
    public static final String RIBBON_KEY = "RIBBON_KEY", CURRENT_POSITION_KEY =
            "CURRENT_POSITION_KEY";
    private static final String TAG = "ReadingActivity_";
    private static final int SELECT_REFERENCE_CODE = 1;
    private int currentPosition = -1;
    private TextView bookNameView;
    private TextView chapterNameView;
    private ViewPager pager;
    private ScrollView scrollView;
    private Ribbon ribbon;
    private TextView translationNameView;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_REFERENCE_CODE:
                    Reference reference = data.getExtras()
                                              .getParcelable(SelectorFragment.REFERENCE_KEY);
                    ribbon.updateIndices(reference);
                    pager.setCurrentItem(ribbon.getPosition());
                    updateInfoToolbar();
                    break;
                default:
                    throw new IllegalStateException("unexpected requestCode: " + requestCode);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        Bundle savedState =
                (savedInstanceState == null ? getIntent().getExtras() : savedInstanceState);
        ribbon = savedState.getParcelable(RIBBON_KEY);
        currentPosition = ribbon.getPosition();

        scrollView = (ScrollView) findViewById(R.id.scrollView);

        bookNameView = (TextView) findViewById(R.id.book_name_view);
        bookNameView.setOnClickListener(
                new StartSelectorFragmentListener(SelectorPosition.BOOK_POSITION));

        chapterNameView = (TextView) findViewById(R.id.chapter_name_view);
        chapterNameView.setOnClickListener(
                new StartSelectorFragmentListener(SelectorPosition.CHAPTER_POSITION));

        translationNameView = (TextView) findViewById(R.id.translation_name_view);
        translationNameView.setText(ribbon.getTranslation().getName());

        ChapterPagerAdapter adapter = new ChapterPagerAdapter(ribbon, getSupportFragmentManager());

        pager = (ViewPager) findViewById(R.id.chapter_pager);
        pager.setAdapter(adapter);
        pager.setCurrentItem(currentPosition);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(
                    int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (currentPosition != pager.getCurrentItem()) {
                    scrollView.smoothScrollTo(0, 0);
                    updateInfoToolbar();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        updateInfoToolbar();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RIBBON_KEY, ribbon);
    }

    private void updateInfoToolbar() {
        currentPosition = pager.getCurrentItem();
        ribbon.setPosition(currentPosition);

        bookNameView.setText(ribbon.getBookName());
        chapterNameView.setText(
                String.format(Locale.getDefault(), "%d", ribbon.getChapterIndex()));
        translationNameView.setText(ribbon.getTranslation().getName());
    }

    class StartSelectorFragmentListener implements View.OnClickListener {
        private SelectorPosition selectorPosition;

        public StartSelectorFragmentListener(SelectorPosition selectorPosition) {
            this.selectorPosition = selectorPosition;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), SelectorActivity.class);
            intent.putExtra(SelectorFragment.SELECTOR_POS_KEY, selectorPosition);
            intent.putExtra(SelectorFragment.REFERENCE_KEY, ribbon.getReference());
            startActivityForResult(intent, SELECT_REFERENCE_CODE);
        }
    }
}
