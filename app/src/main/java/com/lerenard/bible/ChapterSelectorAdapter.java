package com.lerenard.bible;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by mc on 26-Dec-16.
 */

public class ChapterSelectorAdapter
        extends RecyclerView.Adapter<ChapterSelectorAdapter.ViewHolder> {

    private final int chapterCount;
    private final Context context;
    private View activeView;
    private final ReferenceSelectorItemSelectedListener listener;
    private final int initialPosition;
    private final Drawable selected, unselected;

    public ChapterSelectorAdapter(
            int chapterCount, int initialPosition, Context context,
            ReferenceSelectorItemSelectedListener listener) {
        this.chapterCount = chapterCount;
        this.context = context;
        this.listener = listener;
        this.initialPosition = initialPosition;
        unselected = ContextCompat.getDrawable(context,
                                               R.drawable
                                                       .chapter_selector_button_unselected);
        selected = ContextCompat.getDrawable(context,
                                             R.drawable
                                                     .chapter_selector_button_selected);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private int position;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (activeView != null) {
                        activeView.setBackground(unselected);
                    }
                    view.setBackground(selected);
                    activeView = view;
                    listener.onChapterSelected(position);
                }
            });
        }

        public void setPosition(int position) {
            this.position = position + 1;
            if (this.position == initialPosition) {
                activeView = itemView;
                itemView.setBackground(selected);
            }
            ((TextView) itemView).setText(String.format(Locale.getDefault(), "%d", this.position));
        }
    }
}
