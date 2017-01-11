package com.lerenard.bible;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

/**
 * Created by mc on 10-Jan-17.
 */

public abstract class ReferenceSelectorAdapter<T extends ReferenceSelectorAdapter.ViewHolder> extends RecyclerView.Adapter<T> {

    private static final String TAG = "ChapterSelectorAdapter_";
    private final Context context;
    private final Drawable selected, pressed, unselected;
    protected ReferenceSelectorItemSelectedListener listener;
    private int count;
    private View activeView;
    protected int currentSelection;

    public ReferenceSelectorAdapter(
            int count, int initialPosition, Context context, int unselectedResource,
            int pressedResource, int selectedResource) {
        this.count = count;
        this.context = context;
        this.currentSelection = initialPosition;
        unselected = ContextCompat.getDrawable(
                context,
                unselectedResource);
        selected = ContextCompat.getDrawable(
                context,
                selectedResource);
        pressed = ContextCompat.getDrawable(
                context,
                pressedResource);
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return count;
    }

    private void unselect(View itemView) {
        itemView.setBackground(unselected);
    }

    protected void setCount(int count, int currentSelection) {
        int oldCount = this.count;
        int oldSelection = this.currentSelection;


        this.count = count;
        this.currentSelection = currentSelection;

        if (count > oldCount) {
            notifyItemRangeInserted(oldCount, count - oldCount);
        }
        else {
            notifyItemRangeRemoved(count, oldCount - count);
            notifyItemChanged(count - 1); // should make the last item colored
        }
    }

    private void select(View view, int position) {
        if (activeView != null) {
            activeView.setBackground(unselected);
        }
        activeView = view;
        activeView.setBackground(selected);
        currentSelection = position;
    }

    public void setListener(ReferenceSelectorItemSelectedListener listener) {
        this.listener = listener;
    }

    abstract class ViewHolder extends RecyclerView.ViewHolder {

        protected int position;

        abstract void onSelected();

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnTouchListener(new View.OnTouchListener() {
                private Drawable old;

                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            old = view.getBackground();
                            view.setBackground(pressed);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            view.setBackground(old);
                            break;
                        case MotionEvent.ACTION_UP:
                            select(view, position);
                            old = view.getBackground();
                            onSelected();
                            break;
                    }

                    return false;
                }
            });
        }

        void setPosition(int position) {
            this.position = position;
            if (this.position == currentSelection) {
                select(itemView, this.position);
            }
            else {
                unselect(itemView);
            }
        }
    }
}