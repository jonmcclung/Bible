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
            DATABASE_VERSION = 2;
    private static final String
            DATABASE_NAME = "bible.db",
            TABLE_RIBBONS = "RIBBONS",
            _ID = BaseColumns._ID,
            RIBBONS_NAME = "name",
            RIBBONS_REF_POSITION = "position",
            RIBBONS_REF_VERSE = "verse",
            RIBBONS_REF_TRANSLATION_NAME = "translation_name",
            RIBBONS_LAST_VISITED = "last_visited",
            RIBBONS_POSITION_IN_LIST = "position_in_list", // no longer in use

    CREATE_TABLE_RIBBONS =
            "CREATE TABLE " + TABLE_RIBBONS + "("
            + _ID + " INTEGER PRIMARY KEY, "
            + RIBBONS_NAME + " TEXT, "
            + RIBBONS_REF_POSITION + " INTEGER, "
            + RIBBONS_REF_VERSE + " INTEGER, "
            + RIBBONS_REF_TRANSLATION_NAME + " TEXT, "
            + RIBBONS_LAST_VISITED + " INTEGER)";

    private static final String[] star = {
            _ID, RIBBONS_NAME, RIBBONS_REF_POSITION, RIBBONS_REF_VERSE,
            RIBBONS_REF_TRANSLATION_NAME, RIBBONS_LAST_VISITED};

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RIBBONS);

        Ribbon firstRibbon = new Ribbon(
                Reference.getDefault(),
                "Personal Reading");
        ContentValues values = getValues(firstRibbon);
        db.insert(TABLE_RIBBONS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "upgrading from " + oldVersion + " to " + newVersion);
        if (oldVersion < 2) {
            String TABLE_RIBBONS_BACKUP = TABLE_RIBBONS + "_BACKUP";
            db.execSQL("BEGIN TRANSACTION");
            db.execSQL("CREATE TEMPORARY TABLE " + TABLE_RIBBONS_BACKUP + "("
                       + _ID + " INTEGER PRIMARY KEY, "
                       + RIBBONS_NAME + " TEXT, "
                       + RIBBONS_REF_POSITION + " INTEGER, "
                       + RIBBONS_REF_VERSE + " INTEGER, "
                       + RIBBONS_REF_TRANSLATION_NAME + " TEXT, "
                       + RIBBONS_LAST_VISITED + " INTEGER)");
            db.execSQL("INSERT INTO " + TABLE_RIBBONS_BACKUP +
                       " SELECT " + _ID + ", " + RIBBONS_NAME + ", " + RIBBONS_REF_POSITION +
                       ", " + RIBBONS_REF_VERSE + ", " + RIBBONS_REF_TRANSLATION_NAME + ", " +
                       RIBBONS_LAST_VISITED + " FROM " + TABLE_RIBBONS);
            db.execSQL("DROP TABLE " + TABLE_RIBBONS);
            db.execSQL(CREATE_TABLE_RIBBONS);
            db.execSQL("INSERT INTO " + TABLE_RIBBONS +
                       " SELECT " + _ID + ", " + RIBBONS_NAME + ", " + RIBBONS_REF_POSITION +
                       ", " + RIBBONS_REF_VERSE + ", " + RIBBONS_REF_TRANSLATION_NAME + ", " +
                       RIBBONS_LAST_VISITED + " FROM " + TABLE_RIBBONS_BACKUP);
            db.execSQL("DROP TABLE " + TABLE_RIBBONS_BACKUP);
            db.execSQL("COMMIT");
        }
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

    public void addRibbon(Ribbon ribbon, int position) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = getValues(ribbon);
        ribbon.setId(db.insert(TABLE_RIBBONS, null, values));
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
                + RIBBONS_LAST_VISITED + " FROM "
                + TABLE_RIBBONS
                + " ORDER BY " + RIBBONS_LAST_VISITED, null);
        return res;
    }

    public void updateRibbon(Ribbon ribbon) {
        SQLiteDatabase db = getWritableDatabase();

        db.update(
                TABLE_RIBBONS,
                getValues(ribbon),
                _ID + " = ?",
                new String[]{Long.toString(ribbon.getId())});
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
                RIBBONS_LAST_VISITED);
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
        return stringBuilder.toString();
    }

    public void deleteRibbon(Ribbon ribbon) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(
                TABLE_RIBBONS,
                _ID + " = ?",
                new String[]{Long.toString(ribbon.getId())});
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
        return res;
    }
}
