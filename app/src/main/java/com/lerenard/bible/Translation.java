package com.lerenard.bible;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by mc on 18-Dec-16.
 */
public class Translation implements Parcelable {
    public static final Creator<Translation> CREATOR = new Creator<Translation>() {
        @Override
        public Translation createFromParcel(Parcel in) {
            return new Translation(in);
        }

        @Override
        public Translation[] newArray(int size) {
            return new Translation[size];
        }
    };
    private static final String TAG = "Translation_";
    private static final String DEFAULT_NAME_KEY = "DEFAULT_TRANSLATION_NAME_KEY";
    private static Translation defaultTranslation = null;
    private static HashMap<String, Translation> allTranslations = new HashMap<>();
    private static ArrayList<String> allTranslationNames;

    static {
        allTranslationNames = new ArrayList<>();
        allTranslationNames.addAll(Arrays.asList("NIV", "ESV", "NLT", "MSG"));
    }

    private final String name;
    private List<Book> books;

    protected Translation(Parcel in) {
        this(in.readString());

    }

    public Translation(String name) {
        this.name = name;
        if (allTranslations.containsKey(name)) {
            this.books = allTranslations.get(name).getBooks();
        }
        else {
            add(this);
            new LoadTranslationTask().execute();
        }
    }

    public List<Book> getBooks() {
        while (books == null) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return books;
    }

    public static void add(Translation translation) {
        allTranslations.put(translation.name, translation);
    }

    public void setBooks(@NonNull List<Book> books) {
        this.books = books;
    }

    public static ArrayList<Translation> getAll(Context context) {
        loadAll(context);
        return getAll();
    }

    public static void loadAll(Context context) {
        for (String translationName : allTranslationNames) {
            get(translationName);
        }
    }

    public static ArrayList<Translation> getAll() {
        ArrayList<Translation> res = new ArrayList<>(allTranslationNames.size());
        for (String translationName : allTranslationNames) {
            res.add(get(translationName));
        }
        return res;
    }

    public static Translation get(String name) {
        if (!allTranslations.containsKey(name) || allTranslations.get(name) == null) {
            final Translation res = new Translation(name);
            allTranslations.put(name, res);
            return res;
        }
        else {
            return allTranslations.get(name);
        }
    }

    public static Translation getDefault() {
        return getDefault(MainApplication.getContext());
    }

    public static Translation getDefault(Context context) {
        if (defaultTranslation == null) {
            setDefault(get(getDefaultName()));
        }
        int i = 0;
        while (defaultTranslation == null) {
            setDefault(get(allTranslationNames.get(i++)));
        }
        return defaultTranslation;
    }

    public static String getDefaultName() {
        return MainApplication
                .getContext()
                .getSharedPreferences(
                        MainApplication.SHARED_PREFERENCES_FILENAME,
                        Context.MODE_PRIVATE)
                .getString(DEFAULT_NAME_KEY, "NIV");
    }

    public static void setDefaultName(String defaultName) {
        MainApplication
                .getContext()
                .getSharedPreferences(
                        MainApplication.SHARED_PREFERENCES_FILENAME,
                        Context.MODE_PRIVATE).edit()
                .putString(DEFAULT_NAME_KEY, defaultName).commit();
    }

    public static void setDefault(Translation defaultTranslation) {
        Translation.defaultTranslation = defaultTranslation;
    }

    public static ArrayList<Book> loadBooks(String name) {
        try {
            InputStream file = MainApplication.getContext().getAssets().open(pathFromName(name));
            ArrayList<Book> res = fromJson(name, file);
            file.close();
            return res;
        } catch (IOException e) {
            Log.d(TAG, e.toString());
            return null;
        }
    }

    private static String pathFromName(String name) {
        return "bibles/" + name + "/" + name + ".json";
    }

    public static ArrayList<Book> fromJson(final String name, final InputStream stream) throws
            IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<JsonNode> typeReference =
                new TypeReference<JsonNode>() {
                };
        final JsonNode map = mapper.readValue(stream, typeReference);
        long start = System.currentTimeMillis();
        List<String> bookNames = Reference.allBooks;

        ArrayList<Book> books = new ArrayList<>(bookNames.size());
        for (int i = 0; i < bookNames.size(); ++i) {
            books.add(null);
        }

        Iterator<Map.Entry<String, JsonNode>> bookFields = map.fields();
        while (bookFields.hasNext()) {
            Map.Entry<String, JsonNode> bookField = bookFields.next();
            JsonNode chapterList = bookField.getValue();

            int numChapters = chapterList.size();
            ArrayList<Chapter> chapters = new ArrayList<>(numChapters);
            for (int i = 0; i < numChapters; ++i) {
                chapters.add(new Chapter(i + 1));
            }

            String bookName = bookField.getKey();
            Book book = new Book(bookName, chapters);
            books.set(bookNames.indexOf(bookName), book);

            final Iterator<Map.Entry<String, JsonNode>> chapterFields =
                    chapterList.fields();
            while (chapterFields.hasNext()) {
                Map.Entry<String, JsonNode> chapterField = chapterFields.next();
                JsonNode verseList = chapterField.getValue();
                int numVerses = verseList.size();
                ArrayList<Verse> verses = new ArrayList<>(numVerses);
                for (int i = 0; i < numVerses; ++i) {
                    verses.add(new Verse(""));
                }
                int chapterIndex = Integer.parseInt(chapterField.getKey()) - 1;
                chapters.get(chapterIndex).setVerses(verses);
                final Iterator<Map.Entry<String, JsonNode>> verseFields =
                        verseList.fields();
                while (verseFields.hasNext()) {
                    Map.Entry<String, JsonNode> verseField = verseFields.next();
                    int verseNumber = Integer.parseInt(verseField.getKey()) - 1;
                    while (verses.size() <= verseNumber) {

                        verses.add(new Verse(""));
                    }
                    verses.set(
                            verseNumber,
                            new Verse(verseField.getValue().asText()));
                }
            }
        }
        return books;
    }

    public static ArrayList<String> getAllNames() {
        return allTranslationNames;
    }

    @Override
    public String toString() {
        return super.toString() + ", name: " + name;
    }

    public String getName() {
        return name;
    }

    public Book getBook(int index) {
        return getBooks().get(index);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (!allTranslations.containsKey(name)) {
            allTranslations.put(name, this);
        }
        parcel.writeString(name);
    }

    public class LoadTranslationTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            books = Translation.loadBooks(name);
            return null;
        }
    }
}
