package com.lerenard.bible;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import co.paulburke.android.itemtouchhelperdemo.helper.ItemTouchHelperViewHolder;

/**
 * Created by mc on 18-Dec-16.
 */

public class RecyclerViewHolder<T> extends RecyclerView.ViewHolder
        implements View.OnClickListener, ItemTouchHelperViewHolder {
    private T item;
    private RecyclerAdapter<T, ? extends RecyclerViewHolder<T>> adapter;

    public RecyclerViewHolder(
            View itemView, RecyclerAdapter<T, ? extends RecyclerViewHolder<T>> adapter) {
        super(itemView);
        this.adapter = adapter;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        adapter.getListener().onClick(item, getAdapterPosition());
    }

    /**
     * use this method to update your views based on the value of item
     *
     * @param item the new item that should be represented by this ViewHolder
     */
    public void setItem(T item) {
        this.item = item;
    }

    @Override
    public void onItemSelected() {
        itemView.setBackgroundColor(adapter.getSelectedColor());
    }

    @Override
    public void onItemClear() {
        itemView.setBackgroundColor(adapter.getUnselectedColor());
    }
}
