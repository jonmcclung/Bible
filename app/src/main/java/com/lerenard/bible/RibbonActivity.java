package com.lerenard.bible;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import co.paulburke.android.itemtouchhelperdemo.helper.SimpleItemTouchHelperCallback;

public class RibbonActivity extends AppCompatActivity implements DataSetListener<Ribbon>,
                                                                 RibbonNameListener {

    public static final int
            REQUEST_UPDATE_RIBBON = 2,
            REQUEST_SELECT_RIBBON_REFERENCE = 3;
    public static final String RIBBON_ID_KEY = "RIBBON_ID_KEY";
    private static final String TAG = "HomeActivity_";
    private static final String NEW_RIBBON_DIALOG_TAG = "NEW_RIBBON_DIALOG_TAG";
    private static boolean justAsked = false;
    private RibbonAdapter adapter;
    private long oldRibbonId;
    private RecyclerView ribbonList;

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
        final boolean undoSetsCanGoBack = ribbon.getId() == oldRibbonId;
        if (undoSetsCanGoBack) {
            setCanGoBack(false);
        }
        Snackbar.make(
                findViewById(R.id.layout_ribbon),
                R.string.count_deleted,
                Snackbar.LENGTH_LONG)
                .setAction(R.string.undo_count_deleted, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.insert(position, ribbon, true);
                        setCanGoBack(undoSetsCanGoBack);
                        int firstVisiblePosition =
                                ((LinearLayoutManager) ribbonList.getLayoutManager())
                                        .findFirstVisibleItemPosition();
                        int lastVisiblePosition =
                                ((LinearLayoutManager) ribbonList.getLayoutManager())
                                        .findLastVisibleItemPosition();
                        if (firstVisiblePosition > position || lastVisiblePosition < position) {
                            ribbonList.smoothScrollToPosition(position);
                        }
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

    private void setCanGoBack(boolean canGoBack) {
        long goodRibbonId = Math.abs(oldRibbonId);
        if (canGoBack) {
            oldRibbonId = goodRibbonId;
        }
        else {
            oldRibbonId = -goodRibbonId;
        }
        Log.d(TAG, "set can go back to " + canGoBack + " which is " + canGoBack());
    }

    private boolean canGoBack() {
        Log.d(TAG, "oldRibbonId is " + oldRibbonId);
        return oldRibbonId > 0;
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
    public void onDrag(Ribbon ribbon, int start, int end) {}

    @Override
    public boolean onLongPress(Ribbon ribbon, int position) {
        return true;
    }

    private void selectRibbon(Ribbon ribbon) {
        Intent intent = new Intent()
                .putExtra(ReadingActivity.RIBBON_KEY, ribbon);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ribbon);

        Bundle savedState =
                savedInstanceState == null ? getIntent().getExtras() : savedInstanceState;

        oldRibbonId = savedState.getLong(RIBBON_ID_KEY);
        Log.d(TAG, "restoring oldRibbonId: " + oldRibbonId);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newRibbon();
            }
        });

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

        ribbonList = (RecyclerView) findViewById(R.id.ribbonList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                true);
        layoutManager.setStackFromEnd(true);
        ribbonList.setLayoutManager(layoutManager);
        ribbonList.setAdapter(adapter);
        new ItemTouchHelper(
                new SimpleItemTouchHelperCallback(adapter) {
                    @Override
                    public boolean isLongPressDragEnabled() {
                        return false;
                    }
                })
                .attachToRecyclerView(ribbonList);

        DividerItemDecoration spacer =
                new DividerItemDecoration(ribbonList.getContext(), layoutManager.getOrientation());
        spacer.setDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.ribbon_spacer));
        ribbonList.addItemDecoration(spacer);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "saving oldRibbonId: " + oldRibbonId);
        outState.putLong(RIBBON_ID_KEY, oldRibbonId);
    }

    private void newRibbon() {
        FragmentManager fm = getSupportFragmentManager();
        NewRibbonDialog dialog = new NewRibbonDialog();
        dialog.setListener(this);
        dialog.show(fm, NEW_RIBBON_DIALOG_TAG);
    }

    @Override
    public void onRibbonNameReceived(String name) {
        Ribbon addMe = new Ribbon(Reference.getDefault(), name);
        adapter.add(addMe, true);
        Intent selectorIntent = new Intent(getApplicationContext(), SelectorActivity.class)
                .putExtra(SelectorFragment.SELECTOR_POS_KEY, SelectorPosition.BOOK_POSITION)
                .putExtra(SelectorFragment.REFERENCE_KEY, addMe.getReference());
        startActivityForResult(selectorIntent, REQUEST_SELECT_RIBBON_REFERENCE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT_RIBBON_REFERENCE:
                    Ribbon added = adapter.getLastVisited();
                    added.setReference(
                            (Reference) data.getExtras()
                                            .getParcelable(SelectorFragment.REFERENCE_KEY));
                    Intent result = new Intent().putExtra(ReadingActivity.RIBBON_KEY, added);
                    setResult(RESULT_OK, result);
                    finish();
                    return;
                default:
                    throw new IllegalStateException("unexpected requestCode: " + requestCode);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (canGoBack()) {
            super.onBackPressed();
        }
        else {
            Snackbar.make(
                    findViewById(R.id.layout_ribbon),
                    getString(R.string.refuse_back_to_deleted_ribbon),
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
