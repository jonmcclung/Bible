package com.lerenard.bible;

import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ReadingActivity extends AppCompatActivity {
    public static final String RIBBON_KEY = "RIBBON_KEY", CURRENT_POSITION_KEY = "CURRENT_POSITION_KEY";
    private static final String TAG = "ReadingActivity_";
    private TextView bookNameView;
    private TextView chapterNameView;
    private TextView translationNameView;
    private ChapterPagerAdapter adapter;
    private ViewPager pager;
    private ScrollView scrollView;
    int currentPosition = -1;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_POSITION_KEY, pager.getCurrentItem());
    }

    private void updateInfoToolbar(Ribbon ribbon) {

    }

    private void updateInfoToolbar() {
        Reference reference = Reference.fromPosition(pager.getCurrentItem());

        bookNameView.setText(reference.getBookName());
        chapterNameView.setText(
                String.format(Locale.getDefault(), "%d", reference.getChapter()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "OnCreate, savedInstanceState: " + savedInstanceState);
        setContentView(R.layout.activity_reading);

        bookNameView = (TextView) findViewById(R.id.book_name_view);
        chapterNameView = (TextView) findViewById(R.id.chapter_name_view);
        translationNameView = (TextView) findViewById(R.id.translation_name_view);
        scrollView = (ScrollView) findViewById(R.id.scrollView);


        final Bundle extras = getIntent().getExtras();
        Ribbon ribbon = extras.getParcelable(RIBBON_KEY);
        assert ribbon != null : Log.e(TAG, "somehow ribbon is null.");

//        LinearLayout toolBar = (LinearLayout) findViewById(R.id.activity_reading_toolbar);
        translationNameView.setText(ribbon.getTranslation().getName());

        pager = (ViewPager) findViewById(R.id.chapter_pager);
        adapter = new ChapterPagerAdapter(ribbon, getSupportFragmentManager());
        pager.setAdapter(adapter);
        if (savedInstanceState == null) {
            currentPosition = ribbon.getReference().getPosition();
        }
        else {
            currentPosition = savedInstanceState.getInt(CURRENT_POSITION_KEY);
        }
        pager.setCurrentItem(currentPosition);
        Log.d(TAG, "set current item to " + ribbon.getReference());
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(
                    int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                Log.d(
                        TAG,
                        "now at " + position + " (" + Reference.fromPosition(position) + ")");
                updateInfoToolbar();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                // TODO
                if (state == ViewPager.SCROLL_STATE_IDLE && currentPosition != pager.getCurrentItem()) {
                    scrollView.smoothScrollTo(0, 0);
                    currentPosition = pager.getCurrentItem();
                    Log.d(TAG, "current item is now " + currentPosition);
                }
            }
        });
        updateInfoToolbar();
        /*pager.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Log.d(TAG, "onGlobalLayout");
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        final View view = pager.findViewWithTag(pager.getCurrentItem());
                        params.width = view.getWidth();

                        params.height = view.getScreenB();
                        Log.d(TAG, "width: " + params.width + ", height: " + params.height);
                        pager.setLayoutParams(params);
                        pager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });*/
    }
}
