package com.lerenard.bible;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import co.paulburke.android.itemtouchhelperdemo.helper.SimpleItemTouchHelperCallback;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class HomeActivity extends AppCompatActivity implements DataSetListener<Ribbon> {

    public static final int REQUEST_WRITE_STORAGE = 1;
    private static final String TAG = "HomeActivity_";
    private static boolean justAsked = false;
    private static Context context;

    public static Context getContext() {
        return context;
    }

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
        Intent intent = new Intent(this, ReadingActivity.class)
                .putExtra(ReadingActivity.RIBBON_KEY, ribbon);
        startActivity(intent);
    }

    @Override
    public void onDrag(Ribbon ribbon, int start, int end) {

    }

    @Override
    public void onLongPress(Ribbon ribbon, int position) {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_home);

        Translation NIV = Translation.get(this, "NIV");
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
        Log.d(TAG, "translation: " + ribbons.get(0).getTranslation() + ", default translation: " + Translation.getDefault());
        ribbons.add(new Ribbon(new Reference("1 John", 1, 1, NIV), "something else"));
        ribbons.add(new Ribbon(new Reference("Mark", 1, 1, NIV), "personal reading"));

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
