package com.lerenard.bible;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by mc on 29-Dec-16.
 */

public class BookSelectorAdapter extends RecyclerView.Adapter<BookSelectorAdapter.ViewHolder> {

    private static final String TAG = "ChapterSelectorAdapter_";
    private final Context context;
    private final Drawable selected, pressed, unselected;
    private final Translation translation;
    private ReferenceSelectorItemSelectedListener listener;
    private View activeView;
    private int currentSelection;

    public BookSelectorAdapter(
            Translation translation, int initialPosition, Context context) {
        this.translation = translation;
        this.context = context;
        this.currentSelection = initialPosition;
        unselected = ContextCompat.getDrawable(
                context,
                R.drawable
                        .book_selector_button_unselected);
        selected = ContextCompat.getDrawable(
                context,
                R.drawable
                        .book_selector_button_selected);
        pressed = ContextCompat.getDrawable(
                context,
                R.drawable
                        .book_selector_button_pressed);
    }

    @Override
    public ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                                        .inflate(
                                                R.layout.book_selector_item_view,
                                                parent,
                                                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            ViewHolder holder, int position) {
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return translation.getBooks().size();
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
                                listener.onBookSelected(position);
                            }
                            break;
                    }

                    return false;
                }
            });
        }

        public void setPosition(int position) {
            this.position = position;
            if (this.position == currentSelection) {
                select(itemView, this.position);
            }
            else {
                unselect(itemView);
            }
            ((TextView) itemView).setText(Reference.abbreviations.get(position));
        }

    }
}
