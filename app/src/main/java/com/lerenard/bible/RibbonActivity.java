package com.lerenard.bible;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import co.paulburke.android.itemtouchhelperdemo.helper.SimpleItemTouchHelperCallback;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class RibbonActivity extends AppCompatActivity implements DataSetListener<Ribbon>,
                                                                 RibbonNameListener {

    public static final int
            REQUEST_UPDATE_RIBBON = 2;
    private static final String TAG = "HomeActivity_";
    private static final String NEW_RIBBON_DIALOG_TAG = "NEW_RIBBON_DIALOG_TAG";
    private static boolean justAsked = false;
    private static Context context;
    private RibbonAdapter adapter;
    private int oldRibbonId;

    private boolean canGoBack() {
        return oldRibbonId < 0;
    }

    private void setCanGoBack(boolean canGoBack) {
        int goodRibbonId = Math.abs(oldRibbonId);
        if (canGoBack) {
            oldRibbonId = goodRibbonId;
        }
        else {
            oldRibbonId = -goodRibbonId;
        }
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onAdd(final Ribbon ribbon, final int index) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                MainApplication.getDatabase().addRibbon(ribbon, index);
                return null;
            }
        }.execute();
    }

    @Override
    public void onDelete(final Ribbon ribbon, final int position) {
        if (ribbon.getId() == oldRibbonId) {
            setCanGoBack(false);
        }
        Snackbar.make(
                findViewById(R.id.layout_home),
                R.string.count_deleted,
                Snackbar.LENGTH_LONG)
                .setAction(R.string.undo_count_deleted, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.insert(position, ribbon, true);
                        setCanGoBack(true);
                    }
                }).show();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                MainApplication.getDatabase().deleteRibbon(ribbon);
                return null;
            }
        }.execute();
    }

    @Override
    public void onUpdate(final Ribbon ribbon) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MainApplication.getDatabase().updateRibbon(ribbon);
                return null;
            }
        }.execute();
    }

    @Override
    public void onClick(final Ribbon ribbon, int position) {
        ribbon.setLastVisitedToNow();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MainApplication.getDatabase().updateRibbon(ribbon);
                return null;
            }
        }.execute();
        selectRibbon(ribbon);
    }

    @Override
    public void onDrag(final Ribbon ribbon, final int start, final int end) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MainApplication.getDatabase().moveRibbon(ribbon.getId(), start, end);
                return null;
            }
        }.execute();
    }

    @Override
    public void onLongPress(Ribbon ribbon, int position) {}

    @Override
    public void onBackPressed() {
        if (canGoBack()) {
            super.onBackPressed();
        }
        else {
            Snackbar.make(
                    findViewById(R.id.layout_home),
                    getString(R.string.refuse_back_to_deleted_ribbon),
                    Snackbar.LENGTH_SHORT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_home);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newRibbon();
            }
        });

        Translation defaultTranslation = Translation.getDefault(this);
        if (defaultTranslation == null) {
            Snackbar.make(
                    findViewById(R.id.layout_home),
                    String.format(getResources().getString(R.string.unable_to_load_bible), "NIV"),
                    Snackbar.LENGTH_LONG).show();
        }

        adapter = null;
        try {
            adapter = new RibbonAdapter(
                    getApplicationContext(),
                    MainApplication.getDatabase().getAllRibbons(), this,
                    ContextCompat.getColor(getApplicationContext(), R.color.defaultItemColor),
                    ContextCompat.getColor(getApplicationContext(), R.color.highlightColor));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
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

    private void newRibbon() {
        FragmentManager fm = getSupportFragmentManager();
        NewRibbonDialog dialog = new NewRibbonDialog();
        dialog.setListener(this);
        dialog.show(fm, NEW_RIBBON_DIALOG_TAG);
    }

    private void selectRibbon(Ribbon ribbon) {
        Intent intent = new Intent()
                .putExtra(ReadingActivity.RIBBON_KEY, ribbon);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onRibbonNameReceived(String name) {
        Ribbon addMe = new Ribbon(Reference.getDefault(), name);
        adapter.add(addMe, true);
        selectRibbon(addMe);
    }
}
