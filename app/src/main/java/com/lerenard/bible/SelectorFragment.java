package com.lerenard.bible;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;

/**
 * Created by mc on 25-Dec-16.
 */

public class SelectorFragment extends Fragment {

    private Reference reference;
    private ViewPager pager;


    private SelectorPosition position;

    public static final String
            SELECTOR_POS_KEY = "SELECTOR_POS_KEY",
            REFERENCE_KEY = "REFERENCE_KEY";

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

        reference = args.getParcelable(REFERENCE_KEY);
        position = (SelectorPosition) args.getSerializable(SELECTOR_POS_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reference_selector, container, false);
        SelectorAdapter adapter = new SelectorAdapter(getFragmentManager(), reference);
        pager = (ViewPager) rootView.findViewById(R.id.reference_selector_pager);
        pager.setAdapter(adapter);
        final TabLayout tab = (TabLayout) rootView.findViewById(R.id.selector_tab);
        tab.setupWithViewPager(pager, true);
        pager.setCurrentItem(position.ordinal());
        /*pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(
                    int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });*/
        return rootView;
    }
}


