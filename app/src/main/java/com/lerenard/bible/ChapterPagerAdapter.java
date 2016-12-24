package com.lerenard.bible;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;

/**
 * Created by mc on 23-Dec-16.
 */

public class ChapterPagerAdapter extends FragmentStatePagerAdapter {
    private final Ribbon ribbon;
    private static final String TAG = "ChapterPageAdapter_";
    public ChapterPagerAdapter(Ribbon ribbon, FragmentManager fm) {
        super(fm);
        this.ribbon = ribbon;
    }

    @Override
    public Fragment getItem(int position) {
        Reference newReference = Reference.fromPosition(position);
        Log.d(TAG, "position: " + position + ", newReference: " + newReference);
        if (newReference == null) {
            return null;
        }
        Fragment res = new ChapterFragment();
        Bundle args = new Bundle();
        args.putParcelable(ReadingActivity.RIBBON_KEY, new Ribbon(ribbon.getTranslation(), newReference, ribbon.getName()));
        res.setArguments(args);
        return res;
    }

    @Override
    public int getCount() {
        return Reference.getTotalChapterCount();
    }
}
