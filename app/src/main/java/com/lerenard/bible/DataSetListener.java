package com.lerenard.bible;

import android.view.View;

public interface DataSetListener<T> {
    void onAdd(final T t, int index);

    void onDelete(final T t, int position);

    void onUpdate(final T t);

    void onClick(final T t, int position);

    void onDrag(final T t, int start, int end);

    void onLongPress(final T t, int position);
}
