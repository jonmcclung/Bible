package com.lerenard.bible;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by mc on 29-Dec-16.
 */

public class BookSelectorAdapter extends ReferenceSelectorAdapter<BookSelectorAdapter.ViewHolder> {

    private static final String TAG = "BookSelectorAdapter_";
    private final Translation translation;

    public BookSelectorAdapter(
            Translation translation, int initialPosition, Context context) {
        super(
                translation.getBooks().size(),
                initialPosition,
                context,
                R.drawable.book_selector_button_unselected,
                R.drawable.book_selector_button_pressed,
                R.drawable.book_selector_button_selected);
        this.translation = translation;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                                        .inflate(
                                                R.layout.book_selector_item_view,
                                                parent,
                                                false);
        return new ViewHolder(view);
    }

    class ViewHolder extends ReferenceSelectorAdapter.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void onSelected() {
            if (listener != null) {
                listener.onBookSelected(position);
            }
        }

        @Override
        void setPosition(int position) {
            super.setPosition(position);
            ((TextView) itemView).setText(Reference.abbreviations.get(position));
        }
    }
}
