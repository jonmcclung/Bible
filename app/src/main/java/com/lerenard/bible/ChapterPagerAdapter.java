package com.lerenard.bible;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

/**
 * Created by mc on 23-Dec-16.
 */

public class ChapterPagerAdapter extends FragmentStatePagerAdapter {
    private final Ribbon ribbon;
    private SparseArray<ChapterFragment> map;
    private static final String TAG = "ChapterPageAdapter_";

    public ChapterPagerAdapter(Ribbon ribbon, FragmentManager fm) {
        super(fm);
        map = new SparseArray<>();
        this.ribbon = ribbon;
    }

    public Ribbon getRibbon() {
        return ribbon;
    }

    @Override
    public Fragment getItem(int position) {
        Reference newReference = Reference.fromPosition(position);
        Log.d(TAG, "position: " + position + ", newReference: " + newReference + ", ribbon: " + ribbon);
        ChapterFragment res = new ChapterFragment();
        map.put(position, res);
        Bundle args = new Bundle();
        Ribbon toPass = new Ribbon(ribbon.getTranslation(), newReference, ribbon.getName());
        Log.d(TAG, "modified ribbon is " + toPass);
        args.putParcelable(ReadingActivity.RIBBON_KEY, toPass);
        res.setArguments(args);
        return res;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        map.remove(position);
    }

    public ChapterFragment getFragment(int position) {
        return map.get(position);
    }

    @Override
    public int getCount() {
        return Reference.getTotalChapterCount();
    }
}
