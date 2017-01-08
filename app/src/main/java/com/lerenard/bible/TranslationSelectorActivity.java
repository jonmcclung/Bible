package com.lerenard.bible;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class TranslationSelectorActivity extends AppCompatActivity
        implements DataListener<Translation> {

    public static final String TRANSLATION_KEY = "TRANSLATION_KEY";
    private static final String TAG = "TSA_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation_selector);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.translation_recycler_view);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        TranslationAdapter adapter = new TranslationAdapter(this);

        adapter.setTranslations(Translation.getAll(this));
        recyclerView.setAdapter(adapter);

        DividerItemDecoration spacer =
                new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        spacer.setDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.ribbon_spacer));
        recyclerView.addItemDecoration(spacer);
    }

    @Override
    public void onDataReceived(Translation translation) {
        onTranslationSelected(translation);
    }

    public void onTranslationSelected(Translation translation) {
        Intent data = new Intent().putExtra(TRANSLATION_KEY, translation);
        setResult(RESULT_OK, data);
        finish();
    }
}
