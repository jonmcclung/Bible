package com.lerenard.bible;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by mc on 26-Dec-16.
 */

public class ChapterSelectorAdapter
        extends NumberSelectorAdapter<ChapterSelectorAdapter.ViewHolder> {

    private static final String TAG = "ChapterSelectorAdapter_";

    public ChapterSelectorAdapter(int count, int initialPosition, Context context) {
        super(count, initialPosition, context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getView(parent));
    }

    class ViewHolder extends ReferenceSelectorAdapter.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void onSelected() {
            if (listener != null) {
                listener.onChapterSelected(position);
            }
        }

        public void setPosition(int position) {
            super.setPosition(position + 1);
            ((TextView) itemView).setText(String.format(Locale.getDefault(), "%d", this.position));
        }
    }
}
