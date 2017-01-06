package com.lerenard.bible;

/**
 * Created by mc on 06-Jan-17.
 */

public interface DataListener<T> {
    void onDataReceived(T t);
}
