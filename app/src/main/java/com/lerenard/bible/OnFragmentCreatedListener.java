package com.lerenard.bible;

import android.support.v4.app.Fragment;

/**
 * Created by mc on 10-Jan-17.
 */

public interface OnFragmentCreatedListener<F extends Fragment> {
    void onFragmentCreated(F fragment);
}
