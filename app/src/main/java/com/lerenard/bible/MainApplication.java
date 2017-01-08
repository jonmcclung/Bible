package com.lerenard.bible;

import android.app.Application;
import android.content.Context;

import com.lerenard.bible.helper.DatabaseHandler;

/**
 * Created by mc on 29-Dec-16.
 */

public class MainApplication extends Application {
    private static DatabaseHandler database;
    private static Context context; // not a memory leak because it will only hold application context.

    public static final String SHARED_PREFERENCES_FILENAME = "preferences";

    public static DatabaseHandler getDatabase() {
        return database;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        database = new DatabaseHandler(context);
    }

    public static Context getContext() {
        return context;
    }
}
