package com.lerenard.bible;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.icu.text.DateFormat;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lerenard.bible.helper.FileAccess;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import co.paulburke.android.itemtouchhelperdemo.helper.SimpleItemTouchHelperCallback;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class HomeActivity extends AppCompatActivity implements DataSetListener<Ribbon> {

    public static final int REQUEST_WRITE_STORAGE = 1;
    private static boolean justAsked = false;
    private static final String TAG = "HomeActivity_";
    private static Context context;

    @Override
    public void onAdd(Ribbon ribbon, int index) {

    }

    @Override
    public void onDelete(Ribbon ribbon, int position) {

    }

    @Override
    public void onUpdate(Ribbon ribbon) {

    }

    @Override
    public void onClick(Ribbon ribbon, int position) {
        Intent intent = new Intent(this, ReadingActivity.class).putExtra(ReadingActivity.RIBBON_KEY, ribbon);
        startActivity(intent);
    }

    @Override
    public void onDrag(Ribbon ribbon, int start, int end) {

    }

    @Override
    public void onLongPress(Ribbon ribbon, int position) {

    }

    public static Context getContext() {
        return context;
    }

    static class Person {
        public String name;
        public int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public Person() {}
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(
                TAG,
                "acquired result of permission request. Code: " + requestCode + ", permissions: " +
                Arrays
                        .toString(permissions) + ", grantResults: " +
                Arrays.toString(grantResults));
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    Log.d(TAG, "permission granted, recreating");
                }
                else {
                    Log.d(TAG, "permission denied");
                    Snackbar snackbar = Snackbar.make(
                            findViewById(R.id.activity_home),
                            getString(R.string.grant_permission),
                            Snackbar.LENGTH_LONG);
                    ((TextView) (snackbar.getView())
                            .findViewById(android.support.design.R.id.snackbar_text))
                            .setMaxLines(10);
                    snackbar.setAction(R.string.grant_permissions, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(
                                    HomeActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_WRITE_STORAGE);
                        }
                    });
                    snackbar.show();
                    justAsked = true;
                }
                break;
        }
    }

    private Translation loadTranslation(String path, String name) {
        try {
            InputStream file = getAssets().open(path);
            Translation translation = Translation.fromJson(name, file);
            Translation.add(translation);
            file.close();
            return translation;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "resuming");
        context = getApplicationContext();

        /*String s = "{\"name\": \"Jon\", \"age\": 21}";

        ObjectMapper mapper = new ObjectMapper();
        Person jon;
        try {
            jon = mapper.readValue(s, Person.class);
            Log.d(TAG, jon.name + ", " + jon.age);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return;
        }

        TypeReference<JsonNode> typeReference =
                new TypeReference<JsonNode>() {
                };
        try {
            JsonNode map = mapper.readValue(
                    "{\"hello\": {\"1\": \"one\", \"2\": \"two\"}, \"goodbye\": {\"2\": \"one\", " +
                    "\"1\": \"two\"}}",
                    typeReference);
            Log.d(TAG, "this is the map: " + map.toString());

            List<String> keys = Arrays.asList("hello", "goodbye");


            ArrayList<ArrayList<String>> values = new ArrayList<>(keys.size());
            for (int i = 0; i < keys.size(); ++i) {
                values.add(null);
            }

            Log.d(TAG, "values should be full of null: " + values);
            Iterator<Map.Entry<String, JsonNode>> fields = map.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                ArrayList<String> list = new ArrayList<>();
                values.set(keys.indexOf(entry.getKey()), list);
                JsonNode node = entry.getValue();
                int numFieldNames = node.size();
                for (int i = 0; i < numFieldNames; ++i) {
                    list.add(null);
                }
                Iterator<String> fieldNames = node.fieldNames();
                while (fieldNames.hasNext()) {
                    String fieldName = fieldNames.next();
                    list.set(Integer.parseInt(fieldName) - 1, node.get(fieldName).asText());
                }
            }

            Log.d(TAG, "this is values: " + values.toString());
        } catch (IOException e) {
            Log.d(TAG, "failed to load map ):");
            e.printStackTrace();
        }*/

        /*if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (justAsked) {
                justAsked = false;
            }
            else {
                Log.d(TAG, "attempting to acquire permissions");
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
            }
        }
        else {
            List<Person> people = new ArrayList<>();
            people.add(jon);
            people.add(new Person("Bob", 24));
            people.add(new Person("Sam", 9999));
            people.add(new Person("José", 4));
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File directory = FileAccess.getDocumentsDirectory(this);
                if (directory == null) {
                    Log.e(TAG, "Could not make directory.");
                }
                else {
                    File file = new File(directory, "people.json");
                    Log.d(TAG, "successfully created " + file.toString());
                    try {
                        mapper.writeValue(file, people);
                        Log.d(TAG, "Wrote it!");
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d(TAG, "failed");
                    }
                }
            }
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_home);

        Translation NIV = loadTranslation("bibles/NIV/NIV.json", "NIV");
        if (NIV != null) {
            Translation.setDefault(NIV);
        }
        else {
            Snackbar.make(
                    findViewById(R.id.activity_home),
                    String.format(getResources().getString(R.string.unable_to_load_bible), "NIV"),
                    Snackbar.LENGTH_LONG).show();
        }


        ArrayList<Ribbon> ribbons = new ArrayList<>();
        ribbons.add(new Ribbon());

        RibbonAdapter adapter = null;

        try {
            adapter = new RibbonAdapter(
                    getApplicationContext(),
                    ribbons, this,
                    ContextCompat.getColor(getApplicationContext(), R.color.defaultItemColor),
                    ContextCompat.getColor(getApplicationContext(), R.color.highlightColor));
            Log.d(TAG, "Successfully created adapter");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        RecyclerView ribbonList = (RecyclerView) findViewById(R.id.ribbonList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                true);
        layoutManager.setStackFromEnd(true);
        ribbonList.setLayoutManager(layoutManager);
        ribbonList.setAdapter(adapter);
        new ItemTouchHelper(
                new SimpleItemTouchHelperCallback(adapter))
                .attachToRecyclerView(ribbonList);

        DividerItemDecoration spacer =
                new DividerItemDecoration(ribbonList.getContext(), layoutManager.getOrientation());
        spacer.setDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.ribbon_spacer));
        ribbonList.addItemDecoration(spacer);
    }
}
