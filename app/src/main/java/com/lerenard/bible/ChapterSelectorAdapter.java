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

import java.util.Locale;

/**
 * Created by mc on 26-Dec-16.
 */

public class ChapterSelectorAdapter
        extends RecyclerView.Adapter<ChapterSelectorAdapter.ViewHolder> {

    private static final String TAG = "ChapterSelectorAdapter_";
    private final Context context;
    private final Drawable selected, pressed, unselected;
    private ReferenceSelectorItemSelectedListener listener;
    private int chapterCount;
    private View activeView;
    private int currentSelection;

    public ChapterSelectorAdapter(
            int chapterCount, int initialPosition, Context context) {
        this.chapterCount = chapterCount;
        this.context = context;
        this.currentSelection = initialPosition;
        unselected = ContextCompat.getDrawable(
                context,
                R.drawable
                        .chapter_selector_button_unselected);
        selected = ContextCompat.getDrawable(
                context,
                R.drawable
                        .chapter_selector_button_selected);
        pressed = ContextCompat.getDrawable(
                context,
                R.drawable
                        .chapter_selector_button_pressed);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                                        .inflate(
                                                R.layout.chapter_selector_item_view,
                                                parent,
                                                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return chapterCount;
    }

    private void unselect(View itemView) {
        itemView.setBackground(unselected);
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

    public void setChapters(int chapterCount, int currentChapter) {
        int oldChapterCount = this.chapterCount;
        int oldSelection = currentSelection;

        this.chapterCount = chapterCount;
        currentSelection = currentChapter;

        if (chapterCount > oldChapterCount) {
            if (BuildConfig.DEBUG) {
                if (currentChapter != oldSelection) {
                    Log.e(
                            TAG,
                            "They want to set it to a different chapter even though there's room " +
                            "here.");
                }
            }
            notifyItemRangeInserted(oldChapterCount, chapterCount - oldChapterCount);
        }
        else {
            notifyItemRangeRemoved(chapterCount, oldChapterCount - chapterCount);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private int position;

        public ViewHolder(View itemView) {
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
                            if (listener != null) {
                                listener.onChapterSelected(position);
                            }
                            break;
                    }

                    return false;
                }
            });
        }

        public void setPosition(int position) {
            this.position = position + 1;
            if (this.position == currentSelection) {
                select(itemView, this.position);
            }
            else {
                unselect(itemView);
            }
            ((TextView) itemView).setText(String.format(Locale.getDefault(), "%d", this.position));
        }

    }
}
