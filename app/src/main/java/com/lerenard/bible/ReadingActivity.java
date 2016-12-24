package com.lerenard.bible;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReadingActivity extends AppCompatActivity {
    public static final String RIBBON_KEY = "RIBBON_KEY";
    private static final String TAG = "ReadingActivity_";
    private Reference reference;
    private Translation translation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        Bundle extras = getIntent().getExtras();
        Ribbon ribbon = extras.getParcelable(RIBBON_KEY);

        ViewPager pager = (ViewPager) findViewById(R.id.chapter_pager);
        pager.setAdapter(new ChapterPagerAdapter(ribbon, getSupportFragmentManager()));
        pager.setCurrentItem(ribbon.getReference().position());
    }
}
