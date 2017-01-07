package com.lerenard.bible;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // does the heavy work of loading translation with splash screen
        Intent readingIntent = new Intent(getApplicationContext(), ReadingActivity.class)
                .putExtra(ReadingActivity.RIBBON_KEY, MainApplication.getDatabase().getLastUsed());
        startActivity(readingIntent);
    }
}
