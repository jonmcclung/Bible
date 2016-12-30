package com.lerenard.bible.helper;

import android.app.Activity;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;

/**
 * Created by mc on 17-Dec-16.
 */

public class FileAccess {
    private static final String TAG = "FileAccess_";

    @Nullable
    public static File getDocumentsDirectory(Activity activity) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.e(TAG, "sd card not mounted.");
            return null;
        }
        File res;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            res = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        }
        else {
            res = new File(Environment.getExternalStorageDirectory() + "/Documents");
        }
        boolean isPresent = true;
        if (!res.exists()) {
            isPresent = res.mkdirs();
        }
        if (isPresent) {
            return res;
        }
        else {
            Log.e(TAG, "Could not create " + res.toString());
            return null;
        }
    }
}

