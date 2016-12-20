package com.lerenard.bible;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by mc on 18-Dec-16.
 */
public class Translation {

    private static List<Translation> allTranslations;
    public static Translation defaultTranslation = null;
    private static final String TAG = "Translation_";

    public Translation(String name, InputStream stream) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<JsonNode> typeReference =
                new TypeReference<JsonNode>() {};
        JsonNode map = mapper.readValue(stream, typeReference);

        Log.d(TAG, "this is the map: " + map.toString());

        List<String> bookNames = Reference.allBooks;


        books = new ArrayList<>(bookNames.size());
        for (int i = 0; i < bookNames.size(); ++i) {
            books.add(null);
        }

        Log.d(TAG, "books should be full of null: " + books);

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
            books.set(bookNames.indexOf(bookName), book);
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
                    verses.set(Integer.parseInt(verseField.getKey()) - 1, new Verse(verseField.getValue().asText()));
                }
            }
        }

        Log.d(TAG, "books ended up as this: " + books.toString());
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
}
