package com.lerenard.bible.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.lerenard.bible.Reference;
import com.lerenard.bible.Ribbon;
import com.lerenard.bible.Translation;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

/**
 * Created by mc on 06-Dec-16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHandler";
    private static final int
            DATABASE_VERSION = 1;
    private static final String
            DATABASE_NAME = "bible.db",
            TABLE_RIBBONS = "RIBBONS",
            _ID = BaseColumns._ID,
            RIBBONS_NAME = "name",
            RIBBONS_REF_POSITION = "position",
            RIBBONS_REF_VERSE = "verse",
            RIBBONS_REF_TRANSLATION_NAME = "translation_name",
            RIBBONS_LAST_VISITED = "last_visited",
            RIBBONS_POSITION_IN_LIST = "position_in_list",

    CREATE_TABLE_RIBBONS =
            "CREATE TABLE " + TABLE_RIBBONS + "("
            + _ID + " INTEGER PRIMARY KEY, "
            + RIBBONS_NAME + " TEXT, "
            + RIBBONS_REF_POSITION + " INTEGER, "
            + RIBBONS_REF_VERSE + " INTEGER, "
            + RIBBONS_REF_TRANSLATION_NAME + " TEXT, "
            + RIBBONS_LAST_VISITED + " INTEGER, "
            + RIBBONS_POSITION_IN_LIST + " INTEGER)";

    private static final String[] star = {
            _ID, RIBBONS_NAME, RIBBONS_REF_POSITION, RIBBONS_REF_VERSE,
            RIBBONS_REF_TRANSLATION_NAME, RIBBONS_LAST_VISITED, RIBBONS_POSITION_IN_LIST};

    private int itemCount = -1;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RIBBONS);

        Ribbon firstRibbon = new Ribbon(
                new Reference("John", 0, 1, Translation.getDefault()),
                "Personal Reading");
        ContentValues values = getValues(firstRibbon);
        values.put(RIBBONS_POSITION_IN_LIST, 0);
        db.insert(TABLE_RIBBONS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "upgrading from " + oldVersion + " to " + newVersion);
    }

    public void addRibbon(Ribbon ribbon) {
        addRibbon(ribbon, itemCount);
    }

    public void addRibbon(Ribbon ribbon, int position) {
        SQLiteDatabase db = getWritableDatabase();

        if (position != itemCount++) {
            db.execSQL(
                    "UPDATE " + TABLE_RIBBONS + " SET " + RIBBONS_POSITION_IN_LIST + " = " +
                    RIBBONS_POSITION_IN_LIST +
                    " + 1 WHERE " + RIBBONS_POSITION_IN_LIST + " >= " + position);
        }

        ContentValues values = getValues(ribbon);
        values.put(RIBBONS_POSITION_IN_LIST, position);
        ribbon.setId(db.insert(TABLE_RIBBONS, null, values));
        db.close();
    }

    /**
     * Retrieve the values to be inserted for the given ribbon.
     * Doesn't include the ribbon id as it's assumed that you will
     * use a where clause with that or that it's otherwise
     * not useful.
     */
    private ContentValues getValues(Ribbon ribbon) {
        ContentValues values = new ContentValues();
        values.put(RIBBONS_NAME, ribbon.getName());
        values.put(RIBBONS_REF_POSITION, ribbon.getReference().getPosition());
        values.put(RIBBONS_REF_VERSE, ribbon.getReference().getVerse());
        values.put(RIBBONS_REF_TRANSLATION_NAME, ribbon.getTranslation().getName());
        values.put(RIBBONS_LAST_VISITED, ribbon.getLastVisited().getTime());
        return values;
    }

    public void moveRibbon(long fromId, int fromPosition, int toPosition) {
        if (Math.abs(fromPosition - toPosition) != 1) {
            Log.e(TAG, "fromPosition: " + fromPosition + ", toPosition: " + toPosition +
                       ". But they should differ by exactly one");
        }

        ContentValues newValuesForTo = new ContentValues();
        newValuesForTo.put(RIBBONS_POSITION_IN_LIST, fromPosition);

        ContentValues newValuesForFrom = new ContentValues();
        newValuesForFrom.put(RIBBONS_POSITION_IN_LIST, toPosition);

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {

            db.update(
                    TABLE_RIBBONS,
                    newValuesForTo,
                    RIBBONS_POSITION_IN_LIST + " = ?",
                    new String[]{String.valueOf(toPosition)});

            db.update(
                    TABLE_RIBBONS,
                    newValuesForFrom,
                    _ID + " = ?",
                    new String[]{String.valueOf(fromId)});

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    public Ribbon getRibbon(int id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RIBBONS,
                star,
                _ID + " = ?",
                new String[]{Integer.toString(id)},
                null, null, null);
        cursor.moveToFirst();

        Ribbon ribbon = getRibbon(cursor);

        cursor.close();
        db.close();
        return ribbon;
    }

    public Ribbon getRibbon(Cursor cursor) {
        String translationName =
                cursor.getString(cursor.getColumnIndex(RIBBONS_REF_TRANSLATION_NAME));
        Translation translation = Translation.get(translationName);
        return new Ribbon(
                cursor.getInt(cursor.getColumnIndex(_ID)),
                new Reference(
                        cursor.getInt(cursor.getColumnIndex(RIBBONS_REF_POSITION)),
                        cursor.getInt(cursor.getColumnIndex(RIBBONS_REF_VERSE)),
                        translation),
                cursor.getString(cursor.getColumnIndex(RIBBONS_NAME)),
                new Date(cursor.getLong(cursor.getColumnIndex(RIBBONS_LAST_VISITED))));
    }

    public ArrayList<Ribbon> getAllRibbons() {
        ArrayList<Ribbon> res = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = getCursor(db);

        if (cursor.moveToFirst()) {
            do {
                res.add(getRibbon(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return res;
    }

    private Cursor getCursor(SQLiteDatabase db) {
        Cursor res = db.rawQuery(
                "SELECT "
                + _ID + ", "
                + RIBBONS_NAME + ", "
                + RIBBONS_REF_POSITION + ", "
                + RIBBONS_REF_VERSE + ", "
                + RIBBONS_REF_TRANSLATION_NAME + ", "
                + RIBBONS_LAST_VISITED + ", "
                + RIBBONS_POSITION_IN_LIST + " FROM "
                + TABLE_RIBBONS
                + " ORDER BY " + RIBBONS_LAST_VISITED, null);
        itemCount = res.getCount();
        return res;
    }

    public void updateRibbon(Ribbon ribbon) {
        SQLiteDatabase db = getWritableDatabase();

        db.update(
                TABLE_RIBBONS,
                getValues(ribbon),
                _ID + " = ?",
                new String[]{Long.toString(ribbon.getId())});

        db.close();
    }

    public void batchDeleteRibbon(Collection<Ribbon> ribbons) {
        // TODO
        throw new UnsupportedOperationException("not implemented");
    }

    public String toString() {
        SQLiteDatabase db = getReadableDatabase();

        StringBuilder stringBuilder = new StringBuilder(), line = new StringBuilder();
        int width = 15;
        String formatString = "%1$-" + width + "s";
        for (String column : star) {
            stringBuilder.append(String.format(Locale.US, formatString, column))
                         .append('|');
            line.append(new String(new char[width]).replace('\0', '-'))
                .append('+');
        }
        stringBuilder.append('\n')
                     .append(line.toString())
                     .append('\n');
        Cursor cursor = db.query(
                TABLE_RIBBONS,
                star,
                null, null, null, null,
                RIBBONS_POSITION_IN_LIST + " DESC");
        if (cursor.moveToFirst()) {
            do {
                for (String column : star) {
                    stringBuilder.append(String.format(Locale.US, formatString,
                                                       StringUtils.trim(cursor.getString(
                                                               cursor.getColumnIndexOrThrow(
                                                                       column)), width)))
                                 .append('|');
                }
                stringBuilder.append('\n');
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return stringBuilder.toString();
    }

    public void deleteRibbon(Ribbon ribbon) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            Cursor cursor = db.query(
                    TABLE_RIBBONS,
                    new String[]{RIBBONS_POSITION_IN_LIST},
                    _ID + " = ?",
                    new String[]{String.valueOf(ribbon.getId())},
                    null, null, null);
            int positionColumn = cursor.getColumnIndexOrThrow(RIBBONS_POSITION_IN_LIST);
            if (cursor.moveToFirst()) {
                int oldPosition = cursor.getInt(positionColumn);
                cursor.close();

                db.delete(
                        TABLE_RIBBONS,
                        _ID + " = ?",
                        new String[]{Long.toString(ribbon.getId())});
                --itemCount;

                db.execSQL("UPDATE " + TABLE_RIBBONS +
                           " SET " + RIBBONS_POSITION_IN_LIST + " = " + RIBBONS_POSITION_IN_LIST +
                           " - 1" +
                           " WHERE " + RIBBONS_POSITION_IN_LIST + " > " +
                           Integer.toString(oldPosition));
                db.setTransactionSuccessful();
            }
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    public int getCount() {
        return itemCount;
    }

    public Cursor getCursor() {
        return getCursor(getWritableDatabase());
    }

    public Ribbon getLastUsed() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_RIBBONS,
                star,
                RIBBONS_LAST_VISITED + " = (SELECT MAX(" + RIBBONS_LAST_VISITED + ") FROM " +
                TABLE_RIBBONS + ")", null, null, null, null);
        cursor.moveToFirst();
        Ribbon res = getRibbon(cursor);
        cursor.close();
        db.close();
        return res;
    }
}
