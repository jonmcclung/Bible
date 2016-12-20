package com.lerenard.bible;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by mc on 18-Dec-16.
 */
public class Translation implements Parcelable {

    protected Translation(Parcel in) {
        name = in.readString();
        Translation cached = get(name);
        if (cached == null) {
            throw new IllegalStateException(
                    "Tried to read translation from parcel, but books were not cached. name: " +
                    name);
        }
        else {
            books = cached.books;
        }
    }

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

    public static Translation getDefault() {
        return defaultTranslation;
    }

    public static void setDefault(Translation defaultTranslation) {
        Translation.defaultTranslation = defaultTranslation;
    }

    public static Translation defaultTranslation = null;
    private static final String TAG = "Translation_";
    private static HashMap<String, Translation> allTranslations = new HashMap<>();

    public static void add(Translation translation) {
        allTranslations.put(translation.name, translation);
    }

    public static Translation get(String name) {
        if (allTranslations.containsKey(name)) {
            return allTranslations.get(name);
        }
        return null;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    private List<Book> books;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Translation(String name) {
        this.name = name;
    }

    private String name;

    public Book getBook(int index) {
        return books.get(index);
    }

    public static Translation fromJson(String name, InputStream stream) throws IOException {
        Translation res = new Translation(name);
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<JsonNode> typeReference =
                new TypeReference<JsonNode>() {
                };
        JsonNode map = mapper.readValue(stream, typeReference);

        List<String> bookNames = Reference.allBooks;

        res.books = new ArrayList<>(bookNames.size());
        for (int i = 0; i < bookNames.size(); ++i) {
            res.books.add(null);
        }

//        Log.d(TAG, "res.books should be full of null: " + res.books);

        Iterator<Map.Entry<String, JsonNode>> bookFields = map.fields();
        while (bookFields.hasNext()) {
            // entries are Books, values are lists of chapters
            Map.Entry<String, JsonNode> bookField = bookFields.next();
            JsonNode chapterList = bookField.getValue();
            int numChapters = chapterList.size();
            ArrayList<Chapter> chapters = new ArrayList<>(numChapters);
            for (int i = 0; i < numChapters; ++i) {
                chapters.add(null);
            }
            String bookName = bookField.getKey();
            Book book = new Book(bookName, chapters);
            res.books.set(bookNames.indexOf(bookName), book);
            final Iterator<Map.Entry<String, JsonNode>> chapterFields = chapterList.fields();
            while (chapterFields.hasNext()) {
                Map.Entry<String, JsonNode> chapterField = chapterFields.next();
                JsonNode verseList = chapterField.getValue();
                int numVerses = verseList.size();
                ArrayList<Verse> verses = new ArrayList<>(numVerses);
                for (int i = 0; i < numVerses; ++i) {
                    verses.add(null);
                }
                Chapter chapter = new Chapter(verses);
                chapters.set(Integer.parseInt(chapterField.getKey()) - 1, chapter);
                final Iterator<Map.Entry<String, JsonNode>> verseFields = verseList.fields();
                while (verseFields.hasNext()) {
                    Map.Entry<String, JsonNode> verseField = verseFields.next();
                    verses.set(
                            Integer.parseInt(verseField.getKey()) - 1,
                            new Verse(verseField.getValue().asText()));
                }
            }
        }

//        Log.d(TAG, "res.books ended up as this: " + res.books.toString());
        return res;
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
}
