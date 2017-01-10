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
 * Created by mc on 10-Jan-17.
 */
public class VerseSelectorAdapter extends NumberSelectorAdapter<VerseSelectorAdapter.ViewHolder> {

    private static final String TAG = "VerseSelectorAdapter_";

    public VerseSelectorAdapter(int count, int initialPosition, Context context) {
        super(count, initialPosition, context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getView(parent));
    }

    class ViewHolder extends ReferenceSelectorAdapter.ViewHolder {

        @Override
        void onSelected() {
            if (listener != null) {
                listener.onVerseSelected(position);
            }
        }

        ViewHolder(View itemView) {
            super(itemView);
        }

        public void setPosition(int position) {
            super.setPosition(position + 1);
            ((TextView) itemView).setText(String.format(Locale.getDefault(), "%d", this.position));
        }
    }
}

