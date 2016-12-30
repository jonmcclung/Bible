package com.lerenard.bible;

import android.app.Application;
import android.content.Context;

import com.lerenard.bible.helper.DatabaseHandler;

/**
 * Created by mc on 29-Dec-16.
 */

public class MainApplication extends Application {
    private static DatabaseHandler database;

    public static DatabaseHandler getDatabase() {
        return database;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        database = new DatabaseHandler(getApplicationContext());
    }
}
