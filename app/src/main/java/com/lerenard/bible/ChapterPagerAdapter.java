package com.lerenard.bible;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

/**
 * Created by mc on 23-Dec-16.
 */

public class ChapterPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "ChapterPageAdapter_";
    private final Ribbon ribbon;
    private SparseArray<ChapterFragment> map;

    public ChapterPagerAdapter(Ribbon ribbon, FragmentManager fm) {
        super(fm);
        map = new SparseArray<>();
        this.ribbon = ribbon;
    }

    public Ribbon getRibbon() {
        return ribbon;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ChapterFragment fragment = (ChapterFragment) super.instantiateItem(container, position);
        map.put(position, fragment);
        return fragment;
    }

    @Override
    public ChapterFragment getItem(int position) {
        ChapterFragment res = map.get(position);
        if (res != null) {
            Log.d(TAG, "recycling " + position + ", " + res);
            return res;
        }
        else {
            Reference newReference = new Reference(ribbon.getReference());
            if (position != ribbon.getPosition()) {
                newReference.setPosition(position);
                // we want to see the top of new chapters when we scroll to them
                newReference.setVerseIndex(1);
            }
            res = new ChapterFragment();
            Log.d(TAG, "need a new " + position + ", " + res);
            map.put(position, res);
            Bundle args = new Bundle();
            Ribbon toPass = new Ribbon(ribbon);
            toPass.setReference(newReference);
            args.putParcelable(ReadingActivity.RIBBON_KEY, toPass);
            res.setArguments(args);
            return res;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        Log.d(TAG, "destroying " + position);
        map.remove(position);
    }

    @Override
    public int getCount() {
        return Reference.getTotalChapterCount();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    /**
     * will have no effect if setting to the same translation
     */
    public void setTranslation(Translation translation) {
        if (ribbon.getTranslation() != translation) {
            ribbon.setTranslation(translation);
            for (int i = 0; i < map.size(); ++i) {
                map.get(map.keyAt(i)).setTranslation(translation);
            }
            notifyDataSetChanged();
        }
    }
}
