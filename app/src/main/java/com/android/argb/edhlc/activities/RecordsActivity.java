package com.android.argb.edhlc.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.argb.edhlc.Constants;
import com.android.argb.edhlc.R;
import com.android.argb.edhlc.adapters.RecordListAdapter;
import com.android.argb.edhlc.Utils;
import com.android.argb.edhlc.database.record.RecordsDataAccessObject;
import com.android.argb.edhlc.objects.Deck;
import com.android.argb.edhlc.objects.Record;

import java.util.ArrayList;

/* Created by ARGB */
public class RecordsActivity extends AppCompatActivity {
    private RecordsDataAccessObject recordsDB;

    //Card - Records2
    private CardView cardViewRecords2List;
    private int mCardViewFullHeightRecords2;
    private RelativeLayout relativeCardTitleRecords2List;
    private TextView textTitleRecords2List;
    private ImageView iconIndicatorRecords2List;
    private ArrayList<Record> listRecords2Content;
    private ListView listViewRecords2;

    //Card - Records3
    private CardView cardViewRecords3List;
    private int mCardViewFullHeightRecords3;
    private RelativeLayout relativeCardTitleRecords3List;
    private TextView textTitleRecords3List;
    private ImageView iconIndicatorRecords3List;
    private ArrayList<Record> listRecords3Content;
    private ListView listViewRecords3;

    //Card - Records4
    private CardView cardViewRecords4List;
    private int mCardViewFullHeightRecords4;
    private RelativeLayout relativeCardTitleRecords4List;
    private TextView textTitleRecords4List;
    private ImageView iconIndicatorRecords4List;
    private ArrayList<Record> listRecords4Content;
    private ListView listViewRecords4;

    ///Drawer
    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Switch switchScreen;

    private String mRecordHighlightName;
    private String mRecordHighlightDeck;
    private int mRecordSize;

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mDrawer)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    public void onClickCardExpansion(View v) {
        switch (v.getId()) {
            case R.id.relativeCardTitleRecords2List:
                Utils.toggleCardExpansion(this, cardViewRecords2List, textTitleRecords2List, iconIndicatorRecords2List, relativeCardTitleRecords2List.getHeight(), mCardViewFullHeightRecords2);
                break;
            case R.id.relativeCardTitleRecords3List:
                Utils.toggleCardExpansion(this, cardViewRecords3List, textTitleRecords3List, iconIndicatorRecords3List, relativeCardTitleRecords3List.getHeight(), mCardViewFullHeightRecords3);
                break;
            case R.id.relativeCardTitleRecords4List:
                Utils.toggleCardExpansion(this, cardViewRecords4List, textTitleRecords4List, iconIndicatorRecords4List, relativeCardTitleRecords4List.getHeight(), mCardViewFullHeightRecords4);
                break;
        }
    }

    public void onClickDrawerItem(View view) {
        switch (view.getId()) {
            case R.id.drawerItemHome:
                mDrawerLayout.closeDrawers();
                Intent intentHome = new Intent(this, MainActivity.class);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
                this.finish();
                break;

            case R.id.drawerItemPlayers:
                mDrawerLayout.closeDrawers();
                Intent intentPlayers = new Intent(this, PlayerListActivity.class);
                intentPlayers.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentPlayers);
                this.finish();
                break;

            case R.id.drawerItemRecords:
                mDrawerLayout.closeDrawers();
                if (mRecordHighlightName != null || mRecordHighlightDeck != null) {
                    Intent intentRecords = new Intent(this, RecordsActivity.class);
                    intentRecords.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentRecords);
                    this.finish();
                }
                break;

            case R.id.drawerItemScreen:
                switchScreen.setChecked(!switchScreen.isChecked());
                if (!switchScreen.isChecked()) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.SCREEN_ON, 0).apply();
                } else {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.SCREEN_ON, 1).apply();
                }
                break;

            case R.id.drawerItemSettings:
                mDrawerLayout.closeDrawers();
                startActivity(new Intent(this, SettingsActivity.class));
                this.finish();
                break;

            case R.id.drawerItemAbout:
                mDrawerLayout.closeDrawers();
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        Intent intent = getIntent();
        mRecordHighlightName = intent.getStringExtra("RECORD_HIGHLIGHT_NAME");
        mRecordHighlightDeck = intent.getStringExtra("RECORD_HIGHLIGHT_DECK");
        mRecordSize = intent.getIntExtra("RECORD_SIZE", 0);

        recordsDB = new RecordsDataAccessObject(this);
        recordsDB.open();

        createStatusBar();
        createToolbar();
        createDrawer();
        createLayout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        recordsDB.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getInt(Constants.SCREEN_ON, 0) == 1) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            switchScreen.setChecked(true);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            switchScreen.setChecked(false);
        }

        updateLayout();
    }

    private void createDrawer() {
        mDrawer = (LinearLayout) findViewById(R.id.records_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.records_drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        LinearLayout drawerItemPlayers = (LinearLayout) findViewById(R.id.drawerItemRecords);
        ImageView drawerItemIconPlayers = (ImageView) findViewById(R.id.drawerItemIconRecords);
        TextView drawerItemTextPlayers = (TextView) findViewById(R.id.drawerItemTextRecords);
        if (drawerItemPlayers != null) {
            drawerItemPlayers.setBackgroundColor(ContextCompat.getColor(this, R.color.gray200));
        }
        if (drawerItemIconPlayers != null) {
            drawerItemIconPlayers.setColorFilter(ContextCompat.getColor(this, R.color.accent_color));
        }
        if (drawerItemTextPlayers != null) {
            drawerItemTextPlayers.setTextColor(ContextCompat.getColor(this, R.color.accent_color));
        }
    }

    private void createLayout() {
        NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        if (nestedScrollView != null) {
            nestedScrollView.smoothScrollBy(0, 0);
        }

        //Drawer
        switchScreen = (Switch) findViewById(R.id.switchScreen);
        switchScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!switchScreen.isChecked()) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.SCREEN_ON, 0).apply();
                } else {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.SCREEN_ON, 1).apply();
                }
            }
        });

        //Card 2 players
        cardViewRecords2List = (CardView) findViewById(R.id.cardViewRecords2List);
        relativeCardTitleRecords2List = (RelativeLayout) findViewById(R.id.relativeCardTitleRecords2List);
        textTitleRecords2List = (TextView) findViewById(R.id.textTitleRecords2List);
        iconIndicatorRecords2List = (ImageView) findViewById(R.id.iconIndicatorRecords2List);

        listViewRecords2 = (ListView) findViewById(R.id.listRecords2);
        if (mRecordHighlightName != null && mRecordHighlightDeck != null)
            listRecords2Content = new ArrayList<>(recordsDB.getAllRecordsByDeck(new Deck(mRecordHighlightName, mRecordHighlightDeck), 2));
        else if (mRecordHighlightName != null)
            listRecords2Content = new ArrayList<>(recordsDB.getAllRecordsByPlayerName(mRecordHighlightName, 2));
        else
            listRecords2Content = new ArrayList<>(recordsDB.getAllRecords(2));
        RecordListAdapter mRecord2ListAdapter = new RecordListAdapter(this, listRecords2Content, 2, mRecordHighlightName);
        listViewRecords2.setAdapter(mRecord2ListAdapter);

        //Card 3 players
        cardViewRecords3List = (CardView) findViewById(R.id.cardViewRecords3List);
        relativeCardTitleRecords3List = (RelativeLayout) findViewById(R.id.relativeCardTitleRecords3List);
        textTitleRecords3List = (TextView) findViewById(R.id.textTitleRecords3List);
        iconIndicatorRecords3List = (ImageView) findViewById(R.id.iconIndicatorRecords3List);

        listViewRecords3 = (ListView) findViewById(R.id.listRecords3);
        if (mRecordHighlightName != null && mRecordHighlightDeck != null)
            listRecords3Content = new ArrayList<>(recordsDB.getAllRecordsByDeck(new Deck(mRecordHighlightName, mRecordHighlightDeck), 3));
        else if (mRecordHighlightName != null)
            listRecords3Content = new ArrayList<>(recordsDB.getAllRecordsByPlayerName(mRecordHighlightName, 3));
        else
            listRecords3Content = new ArrayList<>(recordsDB.getAllRecords(3));
        RecordListAdapter mRecord3ListAdapter = new RecordListAdapter(this, listRecords3Content, 3, mRecordHighlightName);
        listViewRecords3.setAdapter(mRecord3ListAdapter);

        //Card 4 players
        cardViewRecords4List = (CardView) findViewById(R.id.cardViewRecords4List);
        relativeCardTitleRecords4List = (RelativeLayout) findViewById(R.id.relativeCardTitleRecords4List);
        textTitleRecords4List = (TextView) findViewById(R.id.textTitleRecords4List);
        iconIndicatorRecords4List = (ImageView) findViewById(R.id.iconIndicatorRecords4List);

        listViewRecords4 = (ListView) findViewById(R.id.listRecords4);
        if (mRecordHighlightName != null && mRecordHighlightDeck != null)
            listRecords4Content = new ArrayList<>(recordsDB.getAllRecordsByDeck(new Deck(mRecordHighlightName, mRecordHighlightDeck), 4));
        else if (mRecordHighlightName != null)
            listRecords4Content = new ArrayList<>(recordsDB.getAllRecordsByPlayerName(mRecordHighlightName, 4));
        else
            listRecords4Content = new ArrayList<>(recordsDB.getAllRecords(4));
        RecordListAdapter mRecord4ListAdapter = new RecordListAdapter(this, listRecords4Content, 4, mRecordHighlightName);
        listViewRecords4.setAdapter(mRecord4ListAdapter);
    }

    private void createStatusBar() {
        View statusBarBackground = findViewById(R.id.statusBarBackground);
        if (statusBarBackground != null) {
            ViewGroup.LayoutParams params = statusBarBackground.getLayoutParams();
            params.height = Utils.getStatusBarHeight(this);
            statusBarBackground.setLayoutParams(params);
            statusBarBackground.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_color));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black_transparent));
        }
    }

    private void createToolbar() {
        assert getSupportActionBar() != null;
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.primary_color)));
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setTitle("Records" +
                (mRecordHighlightDeck != null ? " - " + mRecordHighlightDeck :
                        (mRecordHighlightName != null ? " - " + mRecordHighlightName : "")));
    }


    private void updateLayout() {
        //Card 2 players
        cardViewRecords2List.setVisibility(View.GONE);
        Utils.justifyListViewHeightBasedOnChildren(listViewRecords2);
        if (listRecords2Content.size() > 0) {
            cardViewRecords2List.setVisibility(View.VISIBLE);
            if (mCardViewFullHeightRecords2 == 0) {
                cardViewRecords2List.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        cardViewRecords2List.getViewTreeObserver().removeOnPreDrawListener(this);
                        mCardViewFullHeightRecords2 = cardViewRecords2List.getHeight();

                        if (mRecordSize != 0 && mRecordSize != 2) {
                            ViewGroup.LayoutParams layoutParams = cardViewRecords2List.getLayoutParams();
                            layoutParams.height = relativeCardTitleRecords2List.getHeight();
                            cardViewRecords2List.setLayoutParams(layoutParams);
                        } else {
                            textTitleRecords2List.setTextColor(ContextCompat.getColor(RecordsActivity.this, R.color.secondary_color));
                            iconIndicatorRecords2List.setImageDrawable(ContextCompat.getDrawable(RecordsActivity.this, R.drawable.arrow_up));
                            iconIndicatorRecords2List.setRotation(0);
                            iconIndicatorRecords2List.startAnimation(AnimationUtils.loadAnimation(RecordsActivity.this, R.anim.rotate_180_anticlockwise_instant));
                            iconIndicatorRecords2List.setColorFilter(ContextCompat.getColor(RecordsActivity.this, R.color.secondary_color));
                        }
                        return true;
                    }
                });
            }
        }

        //Card 3 players
        cardViewRecords3List.setVisibility(View.GONE);
        Utils.justifyListViewHeightBasedOnChildren(listViewRecords3);
        if (listRecords3Content.size() > 0) {
            cardViewRecords3List.setVisibility(View.VISIBLE);
            if (mCardViewFullHeightRecords3 == 0) {
                cardViewRecords3List.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        cardViewRecords3List.getViewTreeObserver().removeOnPreDrawListener(this);
                        mCardViewFullHeightRecords3 = cardViewRecords3List.getHeight();

                        if (mRecordSize != 0 && mRecordSize != 3) {
                            ViewGroup.LayoutParams layoutParams = cardViewRecords3List.getLayoutParams();
                            layoutParams.height = relativeCardTitleRecords3List.getHeight();
                            cardViewRecords3List.setLayoutParams(layoutParams);
                        } else {
                            textTitleRecords3List.setTextColor(ContextCompat.getColor(RecordsActivity.this, R.color.secondary_color));
                            iconIndicatorRecords3List.setImageDrawable(ContextCompat.getDrawable(RecordsActivity.this, R.drawable.arrow_up));
                            iconIndicatorRecords3List.setRotation(0);
                            iconIndicatorRecords3List.startAnimation(AnimationUtils.loadAnimation(RecordsActivity.this, R.anim.rotate_180_anticlockwise_instant));
                            iconIndicatorRecords3List.setColorFilter(ContextCompat.getColor(RecordsActivity.this, R.color.secondary_color));
                        }
                        return true;
                    }
                });
            }
        }

        //Card 4 players
        cardViewRecords4List.setVisibility(View.GONE);
        Utils.justifyListViewHeightBasedOnChildren(listViewRecords4);
        if (listRecords4Content.size() > 0) {
            cardViewRecords4List.setVisibility(View.VISIBLE);
            if (mCardViewFullHeightRecords4 == 0) {
                cardViewRecords4List.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        cardViewRecords4List.getViewTreeObserver().removeOnPreDrawListener(this);
                        mCardViewFullHeightRecords4 = cardViewRecords4List.getHeight();

                        if (mRecordSize != 0 && mRecordSize != 4) {
                            ViewGroup.LayoutParams layoutParams = cardViewRecords4List.getLayoutParams();
                            layoutParams.height = relativeCardTitleRecords4List.getHeight();
                            cardViewRecords4List.setLayoutParams(layoutParams);
                        } else {
                            textTitleRecords4List.setTextColor(ContextCompat.getColor(RecordsActivity.this, R.color.secondary_color));
                            iconIndicatorRecords4List.setImageDrawable(ContextCompat.getDrawable(RecordsActivity.this, R.drawable.arrow_up));
                            iconIndicatorRecords4List.setRotation(0);
                            iconIndicatorRecords4List.startAnimation(AnimationUtils.loadAnimation(RecordsActivity.this, R.anim.rotate_180_anticlockwise_instant));
                            iconIndicatorRecords4List.setColorFilter(ContextCompat.getColor(RecordsActivity.this, R.color.secondary_color));
                        }
                        return true;
                    }
                });
            }

        }
    }
}
