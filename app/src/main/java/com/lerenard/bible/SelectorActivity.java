package com.lerenard.bible;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by mc on 25-Dec-16.
 */

public class SelectorActivity extends AppCompatActivity
        implements ReferenceSelectorItemSelectedListener {

    private static final String
            TAG = "SelectorActivity_",
            SELECTOR_FRAGMENT_TAG = "SELECTOR_FRAGMENT_TAG";

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "restoring");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_reference_selector);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            SelectorPosition selectorPosition =
                    (SelectorPosition) extras.getSerializable(SelectorFragment.SELECTOR_POS_KEY);
            Ribbon ribbon = extras.getParcelable(ReadingActivity.RIBBON_KEY);
            SelectorFragment selectorFragment = new SelectorFragment();
            selectorFragment.setArguments(extras);
            selectorFragment.setListener(this);
            Log.d(TAG, "set fragment listener for " + selectorFragment + " I am " + this);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.reference_selector_container, selectorFragment, SELECTOR_FRAGMENT_TAG)
                    .commit();
        }
        else {
            ((SelectorFragment) getSupportFragmentManager().findFragmentByTag(SELECTOR_FRAGMENT_TAG)).setListener(this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "saving");
    }

    @Override
    public void onChapterSelected(int chapterIndex) {}

    @Override
    public void onBookSelected(int bookIndex) {}

    @Override
    public void submit() {
        Reference reference = ((SelectorFragment) getSupportFragmentManager().findFragmentByTag(SELECTOR_FRAGMENT_TAG)).getReference();
        Intent data = new Intent();
        data.putExtra(SelectorFragment.REFERENCE_KEY, reference);
        setResult(RESULT_OK, data);
        finish();
    }
}
