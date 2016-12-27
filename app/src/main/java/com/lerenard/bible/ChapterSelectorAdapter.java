package com.lerenard.bible;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by mc on 26-Dec-16.
 */

public class ChapterSelectorAdapter
        extends RecyclerView.Adapter<ChapterSelectorAdapter.ViewHolder> {

    private final int chapterCount;
    private final ReferenceSelectorItemSelectedListener listener;

    public ChapterSelectorAdapter(int chapterCount, int initialPosition, ReferenceSelectorItemSelectedListener listener) {
        this.chapterCount = chapterCount;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                              .inflate(
                                      R.layout.chapter_selector_item_view,
                                      parent,
                                      false));
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
                    listener.onChapterSelected(position);
                }
            });
        }

        public void setPosition(int position) {
            this.position = position + 1;
            ((TextView) itemView).setText(String.format(Locale.getDefault(), "%d", this.position));
        }
    }
}
