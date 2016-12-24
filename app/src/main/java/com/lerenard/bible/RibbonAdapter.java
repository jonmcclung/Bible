package com.lerenard.bible;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.paulburke.android.itemtouchhelperdemo.helper.ItemTouchHelperAdapter;
import co.paulburke.android.itemtouchhelperdemo.helper.ItemTouchHelperViewHolder;
import co.paulburke.android.itemtouchhelperdemo.helper.OnStartDragListener;

/**
 * Created by mc on 18-Dec-16.
 */

public class RibbonAdapter extends RecyclerAdapter<Ribbon, RibbonViewHolder> {

    private static String TAG = "CountRecyclerViewAdapter";

    public RibbonAdapter(
            Context context,
            ArrayList<Ribbon> items,
            DataSetListener<Ribbon> listener, int unselectedColor,
            int selectedColor) throws NoSuchMethodException {
        super(items, listener, R.layout.ribbon_view,
              new Supplier<>(
                      RibbonViewHolder.class.getConstructor(
                              View.class,
                              RibbonAdapter.class)),
              unselectedColor, selectedColor);
    }

    @Override
    public RibbonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);
        return supplier.get(view, this);
    }
}