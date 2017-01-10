package com.lerenard.bible;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mc on 10-Jan-17.
 */

public abstract class NumberSelectorAdapter<T extends ReferenceSelectorAdapter.ViewHolder> extends ReferenceSelectorAdapter<T> {
    public NumberSelectorAdapter(
            int count, int initialPosition, Context context) {
        super(
                count,
                initialPosition,
                context,
                R.drawable.number_selector_button_unselected,
                R.drawable.number_selector_button_pressed,
                R.drawable.number_selector_button_selected);
    }

    protected View getView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext())
                      .inflate(
                              R.layout.number_selector_item_view,
                              parent,
                              false);
    }
}