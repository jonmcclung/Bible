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
    public Fragment getItem(int position) {
        Reference newReference = new Reference(ribbon.getReference());
        newReference.setPosition(position);
        /*Log.d(
                TAG, "position: " + position + ", newReference: " + newReference + ", ribbon: " +
                     ribbon);*/
        ChapterFragment res = new ChapterFragment();
        map.put(position, res);
        Bundle args = new Bundle();
        Ribbon toPass = new Ribbon(ribbon);
        toPass.setReference(newReference);
//        Log.d(TAG, "modified ribbon is " + toPass);
        args.putParcelable(ReadingActivity.RIBBON_KEY, toPass);
        res.setArguments(args);
        return res;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
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
