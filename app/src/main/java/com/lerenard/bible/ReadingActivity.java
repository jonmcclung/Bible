package com.lerenard.bible;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.lerenard.bible.helper.DatabaseHandler;

import java.util.Locale;

public class ReadingActivity extends AppCompatActivity implements RibbonNameListener {
    public static final String RIBBON_KEY = "RIBBON_KEY", CURRENT_POSITION_KEY =
            "CURRENT_POSITION_KEY";
    private static final String TAG = "ReadingActivity_";
    private static final int
            SELECT_REFERENCE_CODE = 1,
            SELECT_TRANSLATION_CODE = 2,
            SELECT_RIBBON_CODE = 3;
    private static final String NEW_RIBBON_DIALOG_TAG = "NEW_RIBBON_DIALOG_TAG";
    private int currentPosition = -1;
    private TextView bookNameView;
    private TextView chapterNameView;
    private ViewPager pager;
    private NestedScrollView scrollView;
    private Ribbon ribbon;
    private TextView translationNameView;
    private ChapterPagerAdapter adapter;
    private TextView verseNameView;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int verseIndex = ribbon.getVerseIndex();
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_REFERENCE_CODE:
                    Reference reference = data.getExtras()
                                              .getParcelable(SelectorFragment.REFERENCE_KEY);
//                    Log.d(TAG, "reference is " + reference);
                    ribbon.setReference(reference);
                    verseIndex = reference.getVerseIndex();
//                    Log.d(TAG, "ribbon is " + ribbon);
                    break;
                case SELECT_TRANSLATION_CODE:
                    Translation translation = data.getExtras().getParcelable(
                            TranslationSelectorActivity.TRANSLATION_KEY);
                    adapter.setTranslation(translation);
                    break;
                case SELECT_RIBBON_CODE:
                    ribbon = data.getExtras().getParcelable(RIBBON_KEY);
                    verseIndex = ribbon.getVerseIndex();
                    makeRibbonToast();
                    break;
                default:
                    throw new IllegalStateException("unexpected requestCode: " + requestCode);
            }

            pager.setCurrentItem(ribbon.getPosition());
            adapter.getItem(ribbon.getPosition())
                   .getRibbon()
                   .setVerseIndex(verseIndex);
            /*Log.d(TAG, "set verseIndex to " + ribbon.getVerseIndex());
            Log.d(TAG, "verseIndex before updateInfoToolbar is " +
                       adapter.getItem(ribbon.getPosition()).getRibbon().getVerseIndex());
            if (fragment != adapter.getItem(ribbon.getPosition())) {
                Log.d(TAG, "They are different!");
            }
            if (fragment.getRibbon().getVerseIndex() != adapter.getItem(ribbon.getPosition())
            .getRibbon().getVerseIndex()) {
                Log.d(TAG, "They have different verses!");
            }*/
            updateInfoToolbar();
            scrollToPreferred();
        }
        else {
            makeRibbonToast();
        }
    }

    private void makeRibbonToast() {
        makeRibbonToast(ribbon.getName());
    }

    private void updateInfoToolbar() {
        Log.d(TAG, "updateInfoToolbar");
        currentPosition = pager.getCurrentItem();
        ribbon.setPosition(currentPosition);
        ribbon.setVerseIndex(adapter.getItem(currentPosition).getRibbon().getVerseIndex());

        bookNameView.setText(ribbon.getBookName());
        chapterNameView.setText(
                String.format(Locale.getDefault(), "%d", ribbon.getChapterIndex()));
        verseNameView.setText(
                String.format(Locale.getDefault(), "%d", ribbon.getVerseIndex()));
        translationNameView.setText(ribbon.getTranslation().getName());
        adapter.setTranslation(ribbon.getTranslation());
        Log.d(TAG, "verseIndex inside updateInfoToolbar is " +
                   adapter.getItem(currentPosition).getRibbon().getVerseIndex());
    }

    private void scrollToPreferred() {
        scrollToPreferred(currentPosition);
    }

    private void makeRibbonToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    private void scrollToPreferred(int position) {
        final ChapterFragment oldFragment = adapter.getItem(position);
        oldFragment.setOnCreatedListener(new OnFragmentCreatedListener<ChapterFragment>() {
            @Override
            public void onFragmentCreated(final ChapterFragment fragment) {
                final TextView text = fragment.getText();
                final ViewTreeObserver observer = text.getViewTreeObserver();
                observer.addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                if (fragment == adapter.getItem(currentPosition)) {
                                    Log.d(TAG, "scrolling to " + fragment.getPreferredOffset() +
                                               " because verseIndex is " +
                                               fragment.getRibbon().getVerseIndex());
                                    scrollView.smoothScrollTo(0, fragment.getPreferredOffset());
                                }
                                else {
                                    Log.d(TAG, "fragment: " + fragment +
                                               " is different from current item: " +
                                               adapter.getItem(currentPosition));
                                }
                                text.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                        });
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            goImmersive();
        }
    }

    private void goImmersive() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_reading_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_reading_menu_go_to_ribbons:
                goToRibbons();
                return true;
            case R.id.activity_reading_new_ribbon_here:
                newRibbonHere();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToRibbons() {
        updateDatabaseWithRibbon();
        Intent ribbonIntent = new Intent(getApplicationContext(), RibbonActivity.class)
                .putExtra(RibbonActivity.RIBBON_ID_KEY, ribbon.getId());
        startActivityForResult(ribbonIntent, SELECT_RIBBON_CODE);
    }

    private void newRibbonHere() {
        FragmentManager fm = getSupportFragmentManager();
        NewRibbonDialog dialog = new NewRibbonDialog();
        dialog.setListener(this);
        dialog.show(fm, NEW_RIBBON_DIALOG_TAG);
    }

    private void updateDatabaseWithRibbon() {
        final DatabaseHandler db = MainApplication.getDatabase();
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                db.updateRibbon(ribbon);
                return null;
            }
        }.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_reading);
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(
                new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        goImmersive();
                    }
                });
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_reading_toolbar);
        setSupportActionBar(toolbar);

        Bundle savedState =
                (savedInstanceState == null ? getIntent().getExtras() : savedInstanceState);
        ribbon = savedState.getParcelable(RIBBON_KEY);

        if (savedInstanceState == null) {
            makeRibbonToast();
        }
        currentPosition = ribbon.getPosition();

        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(
                    NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                ChapterFragment fragment = adapter.getItem(currentPosition);
                int verseIndex = fragment.setAndGetVerseIndex(scrollY);
                if (verseIndex != -1) {
                    /* not using updateInfoToolbar for speed purposes
                    since this method will be called a lot.*/
                    verseNameView.setText(
                            String.format(Locale.getDefault(), "%d", verseIndex));
                    ribbon.setVerseIndex(verseIndex);
                }
            }
        });

        bookNameView = (TextView) findViewById(R.id.book_name_view);
        bookNameView.setOnClickListener(
                new StartSelectorFragmentListener(SelectorPosition.BOOK_POSITION));

        chapterNameView = (TextView) findViewById(R.id.chapter_name_view);
        chapterNameView.setOnClickListener(
                new StartSelectorFragmentListener(SelectorPosition.CHAPTER_POSITION));

        verseNameView = (TextView) findViewById(R.id.verse_name_view);
        verseNameView.setOnClickListener(
                new StartSelectorFragmentListener(SelectorPosition.VERSE_POSITION));

        translationNameView = (TextView) findViewById(R.id.translation_name_view);
        translationNameView.setText(ribbon.getTranslation().getName());
        translationNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =
                        new Intent(getApplicationContext(), TranslationSelectorActivity.class);
                startActivityForResult(intent, SELECT_TRANSLATION_CODE);
            }
        });

        adapter = new ChapterPagerAdapter(ribbon, getSupportFragmentManager());

        pager = (ViewPager) findViewById(R.id.chapter_pager);
        pager.setAdapter(adapter);
        pager.setCurrentItem(currentPosition);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(
                    int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (currentPosition != pager.getCurrentItem()) {
                    Log.d(TAG, "onPageSelected");
                    scrollToPreferred(position);
                    updateInfoToolbar();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        updateInfoToolbar();
        Log.d(TAG, "calling scrollToPreferred from onCreate. Ribbon: " + ribbon);
        scrollToPreferred();
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateDatabaseWithRibbon();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RIBBON_KEY, ribbon);
    }

    @Override
    public void onRibbonNameReceived(String name) {
        final Ribbon oldRibbon = new Ribbon(ribbon);
        ribbon.setName(name);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                MainApplication.getDatabase().updateRibbon(oldRibbon);
                MainApplication.getDatabase().addRibbon(ribbon);
                return null;
            }
        }.execute();
        makeRibbonToast();
    }

    class StartSelectorFragmentListener implements View.OnClickListener {
        private SelectorPosition selectorPosition;

        public StartSelectorFragmentListener(SelectorPosition selectorPosition) {
            this.selectorPosition = selectorPosition;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), SelectorActivity.class);
            intent.putExtra(SelectorFragment.SELECTOR_POS_KEY, selectorPosition);
            intent.putExtra(SelectorFragment.REFERENCE_KEY, ribbon.getReference());
            startActivityForResult(intent, SELECT_REFERENCE_CODE);
        }
    }
}
