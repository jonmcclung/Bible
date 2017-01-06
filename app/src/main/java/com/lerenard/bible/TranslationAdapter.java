package com.lerenard.bible;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mc on 06-Jan-17.
 */

public class TranslationAdapter extends RecyclerView.Adapter<TranslationAdapter.ViewHolder> {

    private DataListener<Translation> listener;
    private ArrayList<Translation> translations;

    public TranslationAdapter(DataListener<Translation> listener) {
        this.listener = listener;
    }

    public TranslationAdapter() {}

    public void setListener(DataListener<Translation> listener) {
        this.listener = listener;
    }

    public void setTranslations(ArrayList<Translation> translations) {
        this.translations = translations;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.translation_view, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setTranslation(translations.get(position));
    }

    @Override
    public int getItemCount() {
        return translations.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameView;
        private Translation translation;

        public ViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.translation_view_name_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onDataReceived(translation);
                }
            });
        }

        public void setTranslation(Translation translation) {
            this.translation = translation;
            nameView.setText(translation.getName());
        }
    }
}
