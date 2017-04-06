package com.lerenard.bible;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SPLASH_ACTIVITY_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // does the heavy work of loading translation with splash screen
        long now = System.currentTimeMillis();
        final Ribbon lastUsed = MainApplication.getDatabase().getLastUsed();
        Log.d(TAG, "finding out last ribbon took " + (System.currentTimeMillis() - now) + " ms");
        now = System.currentTimeMillis();
        Intent readingIntent = new Intent(getApplicationContext(), ReadingActivity.class)
                .putExtra(ReadingActivity.RIBBON_KEY, lastUsed);
        Log.d(TAG, "getting the intent took " + (System.currentTimeMillis() - now) + " ms");
        now = System.currentTimeMillis();
        startActivity(readingIntent);
        Log.d(TAG, "starting the intent took " + (System.currentTimeMillis() - now) + " ms");
        now = System.currentTimeMillis();
        Translation.loadAll(getApplicationContext());
        Log.d(TAG, "getting all the translations took " + (System.currentTimeMillis() - now) + " ms");
        now = System.currentTimeMillis();

        finish();
        Log.d(TAG, "finishing took " + (System.currentTimeMillis() - now) + " ms");
    }
}
