package com.lerenard.bible;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by mc on 25-Dec-16.
 */

public class SelectorActivity extends AppCompatActivity implements ReferenceSelectorItemSelectedListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reference_selector);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        SelectorPosition position =
                (SelectorPosition) extras.getSerializable(SelectorFragment.SELECTOR_POS_KEY);
        Reference reference = extras.getParcelable(SelectorFragment.REFERENCE_KEY);
        SelectorFragment selectorFragment = new SelectorFragment();
        selectorFragment.setArguments(extras);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.reference_selector_container, selectorFragment)
                .commit();
    }

    @Override
    public void onChapterSelected(int chapterIndex) {

    }

    @Override
    public void onBookSelected(int bookIndex) {

    }
}
