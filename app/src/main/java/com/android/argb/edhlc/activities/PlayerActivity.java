package com.android.argb.edhlc.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.argb.edhlc.Constants;
import com.android.argb.edhlc.DeckListAdapter;
import com.android.argb.edhlc.R;
import com.android.argb.edhlc.Utils;
import com.android.argb.edhlc.chart.DonutChart;
import com.android.argb.edhlc.colorpicker.ColorPickerDialog;
import com.android.argb.edhlc.colorpicker.ColorPickerSwatch;
import com.android.argb.edhlc.database.deck.DecksDataAccessObject;
import com.android.argb.edhlc.database.player.PlayersDataAccessObject;
import com.android.argb.edhlc.database.record.RecordsDataAccessObject;
import com.android.argb.edhlc.objects.Deck;
import com.android.argb.edhlc.objects.Player;
import com.android.argb.edhlc.objects.Record;

import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.renderer.DefaultRenderer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayerActivity extends AppCompatActivity {

    private String mPlayerName;

    private DecksDataAccessObject decksDB;
    private RecordsDataAccessObject recordsDB;
    private PlayersDataAccessObject playersDB;

    //Main card - Deck info
    private CardView cardViewPlayerInfo;
    private int mCardViewFullHeightPlayerInfo = 0;
    private RelativeLayout relativeTitlePlayerInfo;
    private TextView textTitlePlayerInfo;
    private ImageView iconIndicatorPlayerInfo;
    private TextView textViewMostPlayedDeckPlayerInfo;
    private TextView textViewOthersMostPlayedDeckPlayerInfo;
    private TextView textViewMostTimePlayedDeckPlayerInfo;
    private TextView textViewLeastPlayedDeckPlayerInfo;
    private TextView textViewOthersLeastPlayedDeckPlayerInfo;
    private TextView textViewLeastTimePlayedDeckPlayerInfo;
    private TextView textViewWins;
    private TextView textViewTotalGames;
    private TextView textViewCreationDate;
    private TextView textViewTotalDecksPlayerInfo;

    //Card - Deck list
    private CardView cardViewDeckList;
    private int mCardViewFullHeightDeckList = 0;
    private RelativeLayout relativeTitleDeckListCard;
    private TextView textTitleDeckListCard;
    private ImageView indicatorDeckListCard;
    private ListView listDeckListCard;
    private List<String[]> deckList;  // 0 imagePath - 1 title - 2 subTitle - 3 identity - 4 selection
    private DeckListAdapter mDeckListAdapter;

    //Card - History charts
    private CardView cardViewCharts;
    private int mCardViewFullHeightDeckHistory = 0;
    private RelativeLayout relativeTitleHistoryCharts;
    private TextView textTitleHistoryCharts;
    private ImageView indicatorHistoryCharts;
    private LinearLayout linearFullChart2;
    private LinearLayout linearFullChart3;
    private LinearLayout linearFullChart4;
    private DefaultRenderer mDoughnutRender2;
    private MultipleCategorySeries mMultipleCategorySeriesDataSet2;
    private DonutChart donutChart2;

    private DefaultRenderer mDoughnutRender3;
    private MultipleCategorySeries mMultipleCategorySeriesDataSet3;
    private DonutChart donutChart3;

    private DefaultRenderer mDoughnutRender4;
    private MultipleCategorySeries mMultipleCategorySeriesDataSet4;
    private DonutChart donutChart4;

    //Card - Last game played
    private CardView cardViewRecordCard;
    private int mCardViewFullHeightLastGamePlayed = 0;
    private RelativeLayout relativeTitleRecordCard;
    private TextView textTitleRecordCard;
    private ImageView indicatorRecordCard;

    //Add deck dialog
    private CheckBox checkBoxManaWhite;
    private CheckBox checkBoxManaBlue;
    private CheckBox checkBoxManaBlack;
    private CheckBox checkBoxManaRed;
    private CheckBox checkBoxManaGreen;
    private CheckBox checkBoxManaColorless;

    ///Drawer
    private DrawerLayout mPlayerDrawerLayout;
    private LinearLayout mPlayerDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Switch switchScreen;

    //Fab
    private boolean isFabOpen;
    private FloatingActionButton fabAdd;
    private LinearLayout fabContentAdd;
    private FloatingActionButton fabMain;
    private FloatingActionButton fabEdit;
    private LinearLayout fabContentEdit;
    private FloatingActionButton fabDelete;
    private LinearLayout fabContentDelete;
    private View fabDismissView;

    private boolean mIsInEditMode;
    private ActionBar mActionBar;
    private Menu optionMenu;
    private View statusBarBackground;

    public void animateFAB() {
        if (isFabOpen) {
            Animation fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
            Animation rotate45Anticlockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_45_anticlockwise);

            fabDismissView.setClickable(false);
            fabDismissView.setVisibility(View.GONE);

            fabMain.startAnimation(rotate45Anticlockwise);
            fabMain.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.accent_color)));

            fabAdd.setClickable(false);
            fabContentAdd.startAnimation(fab_close);

            fabEdit.setClickable(false);
            fabContentEdit.startAnimation(fab_close);

            fabDelete.setClickable(false);
            fabContentDelete.startAnimation(fab_close);

            isFabOpen = false;
        } else {
            Animation fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
            Animation rotate45Clockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_45_clockwise);

            fabDismissView.setClickable(true);
            fabDismissView.setVisibility(View.VISIBLE);

            fabMain.startAnimation(rotate45Clockwise);
            fabMain.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.accent_secondary_black_color)));

            fabAdd.setClickable(true);
            fabContentAdd.setVisibility(View.VISIBLE);
            fabContentAdd.startAnimation(fab_open);

            fabEdit.setClickable(true);
            fabContentEdit.setVisibility(View.VISIBLE);
            fabContentEdit.startAnimation(fab_open);

            fabDelete.setClickable(true);
            fabContentDelete.setVisibility(View.VISIBLE);
            fabContentDelete.startAnimation(fab_open);

            isFabOpen = true;
        }
    }

    @Override
    public void onBackPressed() {
        if (mPlayerDrawerLayout.isDrawerOpen(mPlayerDrawer))
            mPlayerDrawerLayout.closeDrawers();
        else if (mIsInEditMode) {
            mIsInEditMode = false;
            updateEditMode();
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    public void onClickCardExpansion(View v) {
        switch (v.getId()) {

            case R.id.relativeCardTitlePlayerInfo:
                Utils.toggleCardExpansion(this, cardViewPlayerInfo, textTitlePlayerInfo, iconIndicatorPlayerInfo, relativeTitlePlayerInfo.getHeight(), mCardViewFullHeightPlayerInfo);
                break;

            case R.id.relativeCardTitleDeckList:
                if (mIsInEditMode) {
                    mIsInEditMode = false;
                    updateEditMode();
                }

                Utils.toggleCardExpansion(this, cardViewDeckList, textTitleDeckListCard, indicatorDeckListCard, relativeTitleDeckListCard.getHeight(), mCardViewFullHeightDeckList);
                break;

            case R.id.relativeCardTitleRecord:
                Utils.toggleCardExpansion(this, cardViewRecordCard, textTitleRecordCard, indicatorRecordCard, relativeTitleRecordCard.getHeight(), mCardViewFullHeightLastGamePlayed);
                break;

            case R.id.relativeTitleHistoryCharts:
                Utils.toggleCardExpansion(this, cardViewCharts, textTitleHistoryCharts, indicatorHistoryCharts, relativeTitleHistoryCharts.getHeight(), mCardViewFullHeightDeckHistory);
                break;
        }
    }

    public void onClickCheckBoxEditDeckList(View view) {
        optionMenu.findItem(R.id.action_edit_deck).setVisible(mDeckListAdapter.getTotalDataChecked() < 2);
        mDeckListAdapter.notifyDataSetChanged();
    }

    public void onClickDrawerItem(View view) {
        switch (view.getId()) {
            case R.id.drawerItemHome:
                mPlayerDrawerLayout.closeDrawers();
                Intent intentHome = new Intent(this, MainActivity.class);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
                this.finish();
                break;

            case R.id.drawerItemPlayers:
                mPlayerDrawerLayout.closeDrawers();
                Intent intentPlayerList = new Intent(this, PlayerListActivity.class);
                intentPlayerList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentPlayerList);
                this.finish();
                break;

            case R.id.drawerItemRecords:
                mPlayerDrawerLayout.closeDrawers();
                startActivity(new Intent(this, RecordsActivity.class));
                finish();
                break;

            case R.id.drawerItemScreen:
                switchScreen.setChecked(!switchScreen.isChecked());
                if (!switchScreen.isChecked()) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.SCREEN_ON, 0).commit();
                } else {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.SCREEN_ON, 1).commit();
                }
                break;

            case R.id.drawerItemSettings:
                mPlayerDrawerLayout.closeDrawers();
                startActivity(new Intent(this, SettingsActivity.class));
                finish();
                break;

            case R.id.drawerItemAbout:
                mPlayerDrawerLayout.closeDrawers();
                //TODO
                break;
        }
    }

    public void onClickFabButton(View view) {
        mIsInEditMode = false;
        updateEditMode();

        switch (view.getId()) {
            case R.id.fabAdd:
                dialogAddDeck(view);
                animateFAB();
                break;
            case R.id.fabMain:
                animateFAB();
                break;
            case R.id.fabEdit:
                dialogEditPlayer(view);
                animateFAB();
                break;
            case R.id.fabDelete:
                dialogRemovePlayer(view);
                animateFAB();
                break;
        }
    }

    public void onClickFullChart(View view) {
        int size = 0;
        switch (view.getId()) {
            case R.id.showAllRecords:
                size = 0;
                break;
            case R.id.linearFullChart2:
                size = 2;
                break;
            case R.id.linearFullChart3:
                size = 3;
                break;
            case R.id.linearFullChart4:
                size = 4;
                break;
        }

        Intent intent = new Intent(this, RecordsActivity.class);
        intent.putExtra("RECORD_HIGHLIGHT_NAME", mPlayerName);
        intent.putExtra("RECORD_SIZE", size);
        startActivity(intent);
    }

    public void onClickManaCheckBox(View view) {
        if (checkBoxManaWhite.isChecked() ||
                checkBoxManaBlue.isChecked() ||
                checkBoxManaBlack.isChecked() ||
                checkBoxManaRed.isChecked() ||
                checkBoxManaGreen.isChecked())
            checkBoxManaColorless.setChecked(false);
        else
            checkBoxManaColorless.setChecked(true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionMenu = menu;
        getMenuInflater().inflate(R.menu.menu_player_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //Option menu
        switch (item.getItemId()) {
            case R.id.action_edit_deck:
                for (int i = 0; i < deckList.size(); i++)
                    if (deckList.get(i)[4].equalsIgnoreCase("true"))
                        dialogEditDeck(decksDB.getDeck(mPlayerName, deckList.get(i)[1]));
                return true;
            case R.id.action_delete_deck:
                dialogRemoveDeck();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent intent = getIntent();
        mPlayerName = intent.getStringExtra("PLAYER_NAME");

        decksDB = new DecksDataAccessObject(this);
        decksDB.open();

        recordsDB = new RecordsDataAccessObject(this);
        recordsDB.open();

        playersDB = new PlayersDataAccessObject(this);
        playersDB.open();

        createStatusBar();
        createToolbar();
        createDrawer();
        createLayout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        decksDB.close();
        recordsDB.close();
        playersDB.close();
    }

    @Override
    protected void onPause() {
        if (mIsInEditMode) {
            mIsInEditMode = false;
            updateEditMode();
        }

        super.onPause();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        mMultipleCategorySeriesDataSet2 = (MultipleCategorySeries) savedState.getSerializable("current_series2");
        mDoughnutRender2 = (DefaultRenderer) savedState.getSerializable("current_renderer2");

        mMultipleCategorySeriesDataSet3 = (MultipleCategorySeries) savedState.getSerializable("current_series3");
        mDoughnutRender3 = (DefaultRenderer) savedState.getSerializable("current_renderer3");

        mMultipleCategorySeriesDataSet4 = (MultipleCategorySeries) savedState.getSerializable("current_series4");
        mDoughnutRender4 = (DefaultRenderer) savedState.getSerializable("current_renderer4");
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("current_series2", mMultipleCategorySeriesDataSet2);
        outState.putSerializable("current_renderer2", mDoughnutRender2);

        outState.putSerializable("current_series3", mMultipleCategorySeriesDataSet3);
        outState.putSerializable("current_renderer3", mDoughnutRender3);

        outState.putSerializable("current_series4", mMultipleCategorySeriesDataSet4);
        outState.putSerializable("current_renderer4", mDoughnutRender4);
    }

    private void createDrawer() {
        mPlayerDrawer = (LinearLayout) findViewById(R.id.player_drawer);
        mPlayerDrawerLayout = (DrawerLayout) findViewById(R.id.player_drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mPlayerDrawerLayout, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mPlayerDrawerLayout.setDrawerListener(mDrawerToggle);

        LinearLayout drawerItemPlayers = (LinearLayout) findViewById(R.id.drawerItemPlayers);
        ImageView drawerItemIconPlayers = (ImageView) findViewById(R.id.drawerItemIconPlayers);
        TextView drawerItemTextPlayers = (TextView) findViewById(R.id.drawerItemTextPlayers);
        drawerItemPlayers.setBackgroundColor(ContextCompat.getColor(this, R.color.gray200));
        drawerItemIconPlayers.setColorFilter(ContextCompat.getColor(this, R.color.accent_color));
        drawerItemTextPlayers.setTextColor(ContextCompat.getColor(this, R.color.accent_color));
    }

    private void createLayout() {
        NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        nestedScrollView.smoothScrollBy(0, 0);

        fabDismissView = findViewById(R.id.fabDismissView);
        fabDismissView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (fabMain.getVisibility() == View.VISIBLE && isFabOpen) {
                        animateFAB();
                    }
                }
                return true;
            }
        });

        //Drawer
        switchScreen = (Switch) findViewById(R.id.switchScreen);
        switchScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!switchScreen.isChecked()) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.SCREEN_ON, 0).commit();
                } else {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.SCREEN_ON, 1).commit();
                }
            }
        });

        //FloatingActionButton
        fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        fabContentAdd = (LinearLayout) findViewById(R.id.fabContentAdd);
        fabMain = (FloatingActionButton) findViewById(R.id.fabMain);
        fabContentEdit = (LinearLayout) findViewById(R.id.fabContentEdit);
        fabEdit = (FloatingActionButton) findViewById(R.id.fabEdit);
        fabContentDelete = (LinearLayout) findViewById(R.id.fabContentDelete);
        fabDelete = (FloatingActionButton) findViewById(R.id.fabDelete);

        //Card Deck info
        cardViewPlayerInfo = (CardView) findViewById(R.id.cardViewPlayerInfo);
        relativeTitlePlayerInfo = (RelativeLayout) findViewById(R.id.relativeCardTitlePlayerInfo);
        textTitlePlayerInfo = (TextView) findViewById(R.id.textTitlePlayerInfo);
        iconIndicatorPlayerInfo = (ImageView) findViewById(R.id.iconIndicatorPlayerInfo);
        //Most played general
        textViewMostPlayedDeckPlayerInfo = (TextView) findViewById(R.id.textViewMostPlayedDeckPlayerInfo);
        //Most played general "view more"
        textViewOthersMostPlayedDeckPlayerInfo = (TextView) findViewById(R.id.textViewOthersMostPlayedDeckPlayerInfo);
        //Most played general - tines
        textViewMostTimePlayedDeckPlayerInfo = (TextView) findViewById(R.id.textViewMostTimePlayedDeckPlayerInfo);
        //Least played general
        textViewLeastPlayedDeckPlayerInfo = (TextView) findViewById(R.id.textViewLeastPlayedDeckPlayerInfo);
        //Least played general "view more"
        textViewOthersLeastPlayedDeckPlayerInfo = (TextView) findViewById(R.id.textViewOthersLeastPlayedDeckPlayerInfo);
        //Least played general - tines
        textViewLeastTimePlayedDeckPlayerInfo = (TextView) findViewById(R.id.textViewLeastTimePlayedDeckPlayerInfo);
        //Creation Date
        textViewCreationDate = (TextView) findViewById(R.id.textViewCreationDatePlayerInfo);
        //Total games
        textViewTotalGames = (TextView) findViewById(R.id.textViewTotalGamesPlayerInfo);
        //Wins
        textViewWins = (TextView) findViewById(R.id.textViewWinsPlayerInfo);
        //Total decks
        textViewTotalDecksPlayerInfo = (TextView) findViewById(R.id.textViewTotalDecksPlayerInfo);

        //Card deckList
        cardViewDeckList = (CardView) findViewById(R.id.cardViewDeckList);
        relativeTitleDeckListCard = (RelativeLayout) findViewById(R.id.relativeCardTitleDeckList);
        textTitleDeckListCard = (TextView) findViewById(R.id.textTitleDeckListCard);
        indicatorDeckListCard = (ImageView) findViewById(R.id.iconIndicatorDeckListCard);
        listDeckListCard = (ListView) findViewById(R.id.listDeckListCard);
        listDeckListCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!mIsInEditMode) {
                    Intent intent = new Intent(PlayerActivity.this, DeckActivity.class);
                    intent.putExtra("PLAYER_NAME", mPlayerName);
                    intent.putExtra("DECK_NAME", deckList.get(position)[1]);
                    intent.putExtra("DECK_IDENTITY", deckList.get(position)[3]);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    mDeckListAdapter.checkBoxSetSelection(position, mDeckListAdapter.checkBoxGetSelection(position) ? "false" : "true");
                    optionMenu.findItem(R.id.action_edit_deck).setVisible(mDeckListAdapter.getTotalDataChecked() < 2);
                    mDeckListAdapter.notifyDataSetChanged();
                }
            }
        });
        listDeckListCard.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mDeckListAdapter.checkBoxSetSelection(position, "true");
                mIsInEditMode = true;
                updateEditMode();
                return true;
            }
        });

        deckList = new ArrayList<>();
        mDeckListAdapter = new DeckListAdapter(this, deckList);
        listDeckListCard.setAdapter(mDeckListAdapter);

        //Chart History
        cardViewCharts = (CardView) findViewById(R.id.cardViewHistoryCharts);
        relativeTitleHistoryCharts = (RelativeLayout) findViewById(R.id.relativeTitleHistoryCharts);
        textTitleHistoryCharts = (TextView) findViewById(R.id.textTitleHistoryCharts);
        indicatorHistoryCharts = (ImageView) findViewById(R.id.indicatorHistoryCharts);
        linearFullChart2 = (LinearLayout) findViewById(R.id.linearFullChart2);
        linearFullChart3 = (LinearLayout) findViewById(R.id.linearFullChart3);
        linearFullChart4 = (LinearLayout) findViewById(R.id.linearFullChart4);
        donutChart2 = new DonutChart(this, mDoughnutRender2, mMultipleCategorySeriesDataSet2);
        donutChart3 = new DonutChart(this, mDoughnutRender3, mMultipleCategorySeriesDataSet3);
        donutChart4 = new DonutChart(this, mDoughnutRender4, mMultipleCategorySeriesDataSet4);

        //Chart lastGamePlayed
        cardViewRecordCard = (CardView) findViewById(R.id.cardViewRecordCard);
        relativeTitleRecordCard = (RelativeLayout) findViewById(R.id.relativeCardTitleRecord);
        indicatorRecordCard = (ImageView) findViewById(R.id.indicatorRecordCard);
        textTitleRecordCard = (TextView) findViewById(R.id.textTitleRecordCard);

    }

    private void createStatusBar() {
        statusBarBackground = findViewById(R.id.statusBarBackground);
        ViewGroup.LayoutParams params = statusBarBackground.getLayoutParams();
        params.height = Utils.getStatusBarHeight(this);
        statusBarBackground.setLayoutParams(params);
        statusBarBackground.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_color));

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
        mActionBar = getSupportActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.primary_color)));
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setTitle(mPlayerName);
    }

    private void dialogAddDeck(final View view) {
        View playerNameView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_add_deck, null);
        final EditText userInput = (EditText) playerNameView.findViewById(R.id.editTextEditDeckName);
        checkBoxManaWhite = (CheckBox) playerNameView.findViewById(R.id.checkbox_mana_white);
        checkBoxManaBlue = (CheckBox) playerNameView.findViewById(R.id.checkbox_mana_blue);
        checkBoxManaBlack = (CheckBox) playerNameView.findViewById(R.id.checkbox_mana_black);
        checkBoxManaRed = (CheckBox) playerNameView.findViewById(R.id.checkbox_mana_red);
        checkBoxManaGreen = (CheckBox) playerNameView.findViewById(R.id.checkbox_mana_green);
        checkBoxManaColorless = (CheckBox) playerNameView.findViewById(R.id.checkbox_mana_colorless);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(playerNameView);
        alertDialogBuilder.setTitle("Add new Deck for " + mPlayerName);
        alertDialogBuilder.setPositiveButton("ADD",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        alertDialogBuilder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertDialog.show();

        //Override POSITIVE button
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempName = userInput.getText().toString();
                if (!tempName.equalsIgnoreCase("")) {

                    String colorIdentity = "";
                    colorIdentity = checkBoxManaWhite.isChecked() ? colorIdentity.concat("1") : colorIdentity.concat("0");
                    colorIdentity = checkBoxManaBlue.isChecked() ? colorIdentity.concat("1") : colorIdentity.concat("0");
                    colorIdentity = checkBoxManaBlack.isChecked() ? colorIdentity.concat("1") : colorIdentity.concat("0");
                    colorIdentity = checkBoxManaRed.isChecked() ? colorIdentity.concat("1") : colorIdentity.concat("0");
                    colorIdentity = checkBoxManaGreen.isChecked() ? colorIdentity.concat("1") : colorIdentity.concat("0");
                    colorIdentity = checkBoxManaColorless.isChecked() ? colorIdentity.concat("1") : colorIdentity.concat("0");

                    if (decksDB.addDeck(new Deck(mPlayerName, tempName, new int[]{ContextCompat.getColor(PlayerActivity.this, R.color.primary_color), ContextCompat.getColor(PlayerActivity.this, R.color.secondary_color)}, colorIdentity, Utils.getCurrentDate())) != -1) {
                        Toast.makeText(view.getContext(), tempName + " added", Toast.LENGTH_SHORT).show();

                        //Set card to wrap_content
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        int pixel6dp = (int) Utils.convertDpToPixel((float) 6, getApplicationContext());
                        layoutParams.setMargins(pixel6dp, pixel6dp, pixel6dp, pixel6dp);
                        cardViewDeckList.setLayoutParams(layoutParams);

                        updateLayout();

                        //Update mCardViewFullHeightDeckList to expansion
                        cardViewDeckList.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                            @Override
                            public boolean onPreDraw() {
                                cardViewDeckList.getViewTreeObserver().removeOnPreDrawListener(this);
                                mCardViewFullHeightDeckList = cardViewDeckList.getHeight();

                                Utils.expand(PlayerActivity.this, cardViewDeckList, textTitleDeckListCard, indicatorDeckListCard, relativeTitleDeckListCard.getHeight(), mCardViewFullHeightDeckList);
                                return true;
                            }
                        });

                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(view.getContext(), "Deck " + tempName + " already exists", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(view.getContext(), "Insert a name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void dialogEditDeck(final Deck mCurrentDeck) {
        View dialogDeckView = LayoutInflater.from(PlayerActivity.this).inflate(R.layout.dialog_deck, null);
        final EditText userInput = (EditText) dialogDeckView.findViewById(R.id.editTextEditDeckName);
        final ImageView shieldColor = (ImageView) dialogDeckView.findViewById(R.id.imageViewEditShieldColor);
        final LinearLayout linearEditShieldColor = (LinearLayout) dialogDeckView.findViewById(R.id.linearEditShieldColor);
        checkBoxManaWhite = (CheckBox) dialogDeckView.findViewById(R.id.checkbox_mana_white);
        checkBoxManaBlue = (CheckBox) dialogDeckView.findViewById(R.id.checkbox_mana_blue);
        checkBoxManaBlack = (CheckBox) dialogDeckView.findViewById(R.id.checkbox_mana_black);
        checkBoxManaRed = (CheckBox) dialogDeckView.findViewById(R.id.checkbox_mana_red);
        checkBoxManaGreen = (CheckBox) dialogDeckView.findViewById(R.id.checkbox_mana_green);
        checkBoxManaColorless = (CheckBox) dialogDeckView.findViewById(R.id.checkbox_mana_colorless);

        final int[] editShieldColor = new int[]{mCurrentDeck.getDeckShieldColor()[0], mCurrentDeck.getDeckShieldColor()[1]};

        userInput.setText(mCurrentDeck.getDeckName());
        shieldColor.setColorFilter(mCurrentDeck.getDeckShieldColor()[0]);
        linearEditShieldColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int[] mColor = getResources().getIntArray(R.array.edh_shield_colors);
                final int[] mColor_dark = getResources().getIntArray(R.array.edh_shield_colors_dark);
                ColorPickerDialog colorCalendar = ColorPickerDialog.newInstance(R.string.color_picker_default_title, mColor, editShieldColor[0], 5, ColorPickerDialog.SIZE_SMALL);
                colorCalendar.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int color) {
                        int color_dark = color;
                        for (int i = 0; i < mColor.length; i++)
                            if (mColor[i] == color)
                                color_dark = mColor_dark[i];

                        editShieldColor[0] = color;
                        editShieldColor[1] = color_dark;

                        shieldColor.setColorFilter(color);
                    }
                });
                colorCalendar.show(getFragmentManager(), "cal");
            }
        });

        final String currentColorIdentity = mCurrentDeck.getDeckIdentity();
        checkBoxManaWhite.setChecked(currentColorIdentity.length() >= 0 && currentColorIdentity.charAt(0) == '1');
        checkBoxManaBlue.setChecked(currentColorIdentity.length() >= 1 && currentColorIdentity.charAt(1) == '1');
        checkBoxManaBlack.setChecked(currentColorIdentity.length() >= 2 && currentColorIdentity.charAt(2) == '1');
        checkBoxManaRed.setChecked(currentColorIdentity.length() >= 3 && currentColorIdentity.charAt(3) == '1');
        checkBoxManaGreen.setChecked(currentColorIdentity.length() >= 4 && currentColorIdentity.charAt(4) == '1');
        checkBoxManaColorless.setChecked(currentColorIdentity.length() >= 5 && currentColorIdentity.charAt(5) == '1');

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PlayerActivity.this);
        alertDialogBuilder.setView(dialogDeckView);
        alertDialogBuilder.setTitle("Edit deck");
        alertDialogBuilder.setPositiveButton("EDIT",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        alertDialogBuilder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        alertDialog.show();

        //Override POSITIVE button
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newDeckName = userInput.getText().toString();
                if (!newDeckName.equalsIgnoreCase("")) {
                    String colorIdentity = "";
                    colorIdentity = checkBoxManaWhite.isChecked() ? colorIdentity.concat("1") : colorIdentity.concat("0");
                    colorIdentity = checkBoxManaBlue.isChecked() ? colorIdentity.concat("1") : colorIdentity.concat("0");
                    colorIdentity = checkBoxManaBlack.isChecked() ? colorIdentity.concat("1") : colorIdentity.concat("0");
                    colorIdentity = checkBoxManaRed.isChecked() ? colorIdentity.concat("1") : colorIdentity.concat("0");
                    colorIdentity = checkBoxManaGreen.isChecked() ? colorIdentity.concat("1") : colorIdentity.concat("0");
                    colorIdentity = checkBoxManaColorless.isChecked() ? colorIdentity.concat("1") : colorIdentity.concat("0");

                    //Deck identity
                    if (!colorIdentity.equalsIgnoreCase(currentColorIdentity)) {
                        decksDB.updateDeckIdentity(mCurrentDeck, colorIdentity);
                        updateLayout();
                    }

                    //Shield color
                    if (editShieldColor[0] != mCurrentDeck.getDeckShieldColor()[0] && editShieldColor[1] != mCurrentDeck.getDeckShieldColor()[1]) {
                        decksDB.updateDeckShieldColor(mCurrentDeck, editShieldColor);
                    }

                    //Deck name
                    if (!newDeckName.equalsIgnoreCase(mCurrentDeck.getDeckName())) {
                        if (decksDB.updateDeckName(mCurrentDeck, newDeckName) != -1) {
                            //Records
                            recordsDB.updateDeckNameRecord(mCurrentDeck, newDeckName);

                            //Banner image
                            File oldFile = new File(getFilesDir(), "image_" + mPlayerName + "_" + mCurrentDeck.getDeckName() + ".png");
                            File newFile = new File(getFilesDir(), "image_" + mPlayerName + "_" + newDeckName + ".png");
                            if (oldFile.renameTo(newFile))
                                Toast.makeText(PlayerActivity.this, "Successfully edited ", Toast.LENGTH_SHORT).show();

                            updateLayout();
                        } else {
                            Toast.makeText(PlayerActivity.this, "Fail: Deck " + newDeckName + " already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                    mIsInEditMode = false;
                    updateEditMode();

                    alertDialog.dismiss();
                } else {
                    Toast.makeText(PlayerActivity.this, "Insert a name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void dialogEditPlayer(final View view) {
        View playerNameView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_player_name, null);
        final EditText userInput = (EditText) playerNameView.findViewById(R.id.editTextPlayerName);
        userInput.setText(mPlayerName);
        userInput.setSelection(userInput.getText().length());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(playerNameView);
        alertDialogBuilder.setTitle("Edit " + mPlayerName);
        alertDialogBuilder.setPositiveButton("EDIT",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Overridden at 'alertDialog.getButton' to avoid dismiss every time
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertDialog.show();

        //Override POSITIVE button
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = userInput.getText().toString();
                if (!newName.equalsIgnoreCase("")) {
                    long result = playersDB.updatePlayer(mPlayerName, newName);
                    if (result != -1) {
                        decksDB.updateDeckOwner(mPlayerName, newName);
                        recordsDB.updateRecord(mPlayerName, newName);
                        mPlayerName = newName;
                        updateLayout();
                        Toast.makeText(view.getContext(), mPlayerName + " edited", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(view.getContext(), "Player " + newName + " already exists", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(view.getContext(), "Insert a name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void dialogRemoveDeck() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PlayerActivity.this);
        alertDialogBuilder.setTitle("Delete deck");
        alertDialogBuilder.setMessage("Are you sure to delete these decks?");
        alertDialogBuilder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        for (int i = 0; i < deckList.size(); i++)
                            if (deckList.get(i)[4].equalsIgnoreCase("true"))
                                decksDB.removeDeck(new Deck(mPlayerName, deckList.get(i)[1]));

                        //Set card to wrap_content
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        int pixel6dp = (int) Utils.convertDpToPixel((float) 6, getApplicationContext());
                        layoutParams.setMargins(pixel6dp, pixel6dp, pixel6dp, pixel6dp);
                        cardViewDeckList.setLayoutParams(layoutParams);

                        updateLayout();

                        //Update mCardViewFullHeightDeckList to expansion
                        cardViewDeckList.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                            @Override
                            public boolean onPreDraw() {
                                cardViewDeckList.getViewTreeObserver().removeOnPreDrawListener(this);
                                mCardViewFullHeightDeckList = cardViewDeckList.getHeight();
                                return true;
                            }
                        });

                        mIsInEditMode = false;
                        updateEditMode();

                        dialog.cancel();
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void dialogRemovePlayer(final View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PlayerActivity.this);
        alertDialogBuilder.setTitle("Delete deck");
        alertDialogBuilder.setMessage("Are you sure to delete this player?");
        alertDialogBuilder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (playersDB.deletePlayer(mPlayerName) != 0) {
                            Toast.makeText(view.getContext(), mPlayerName + " removed", Toast.LENGTH_SHORT).show();
                            PlayerActivity.this.finish();
                        } else {
                            Toast.makeText(view.getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void handleTiedLeastPlayedDecks(final List<Deck> tiedDecks) {
        textViewOthersLeastPlayedDeckPlayerInfo.setText("more decks");
        textViewOthersLeastPlayedDeckPlayerInfo.setTextColor(ContextCompat.getColor(this, R.color.primary_color));
        textViewOthersLeastPlayedDeckPlayerInfo.setClickable(true);
        textViewOthersLeastPlayedDeckPlayerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textViewOthersLeastPlayedDeckPlayerInfo.isActivated()) {
                    textViewOthersLeastPlayedDeckPlayerInfo.setText("more decks");
                    textViewLeastPlayedDeckPlayerInfo.setText(tiedDecks.get(0).getDeckName());

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    int pixel6dp = (int) Utils.convertDpToPixel((float) 6, getApplicationContext());
                    layoutParams.setMargins(pixel6dp, pixel6dp, pixel6dp, pixel6dp);
                    cardViewPlayerInfo.setLayoutParams(layoutParams);
                    cardViewPlayerInfo.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            cardViewPlayerInfo.getViewTreeObserver().removeOnPreDrawListener(this);
                            mCardViewFullHeightPlayerInfo = cardViewPlayerInfo.getHeight();
                            return true;
                        }
                    });
                } else {
                    textViewOthersLeastPlayedDeckPlayerInfo.setText("hide decks");
                    String decks = "";
                    for (int i = 0; i < tiedDecks.size(); i++) {
                        decks = decks + tiedDecks.get(i).getDeckName();
                        if (i < tiedDecks.size() - 1)
                            decks = decks + System.getProperty("line.separator");
                    }
                    textViewLeastPlayedDeckPlayerInfo.setText(decks);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    int pixel6dp = (int) Utils.convertDpToPixel((float) 6, getApplicationContext());
                    layoutParams.setMargins(pixel6dp, pixel6dp, pixel6dp, pixel6dp);
                    cardViewPlayerInfo.setLayoutParams(layoutParams);
                    cardViewPlayerInfo.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            cardViewPlayerInfo.getViewTreeObserver().removeOnPreDrawListener(this);
                            mCardViewFullHeightPlayerInfo = cardViewPlayerInfo.getHeight();
                            return true;
                        }
                    });
                }
                textViewOthersLeastPlayedDeckPlayerInfo.setActivated(!textViewOthersLeastPlayedDeckPlayerInfo.isActivated());
            }
        });
    }

    private void handleTiedMostPlayedDecks(final List<Deck> tiedDecks) {
        textViewOthersMostPlayedDeckPlayerInfo.setText("more decks");
        textViewOthersMostPlayedDeckPlayerInfo.setTextColor(ContextCompat.getColor(this, R.color.primary_color));
        textViewOthersMostPlayedDeckPlayerInfo.setClickable(true);
        textViewOthersMostPlayedDeckPlayerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textViewOthersMostPlayedDeckPlayerInfo.isActivated()) {
                    textViewOthersMostPlayedDeckPlayerInfo.setText("more decks");
                    textViewMostPlayedDeckPlayerInfo.setText(tiedDecks.get(0).getDeckName());

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    int pixel6dp = (int) Utils.convertDpToPixel((float) 6, getApplicationContext());
                    layoutParams.setMargins(pixel6dp, pixel6dp, pixel6dp, pixel6dp);
                    cardViewPlayerInfo.setLayoutParams(layoutParams);
                    cardViewPlayerInfo.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            cardViewPlayerInfo.getViewTreeObserver().removeOnPreDrawListener(this);
                            mCardViewFullHeightPlayerInfo = cardViewPlayerInfo.getHeight();
                            return true;
                        }
                    });
                } else {
                    textViewOthersMostPlayedDeckPlayerInfo.setText("hide decks");
                    String decks = "";
                    for (int i = 0; i < tiedDecks.size(); i++) {
                        decks = decks + tiedDecks.get(i).getDeckName();
                        if (i < tiedDecks.size() - 1)
                            decks = decks + System.getProperty("line.separator");
                    }
                    textViewMostPlayedDeckPlayerInfo.setText(decks);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    int pixel6dp = (int) Utils.convertDpToPixel((float) 6, getApplicationContext());
                    layoutParams.setMargins(pixel6dp, pixel6dp, pixel6dp, pixel6dp);
                    cardViewPlayerInfo.setLayoutParams(layoutParams);
                    cardViewPlayerInfo.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            cardViewPlayerInfo.getViewTreeObserver().removeOnPreDrawListener(this);
                            mCardViewFullHeightPlayerInfo = cardViewPlayerInfo.getHeight();
                            return true;
                        }
                    });
                }
                textViewOthersMostPlayedDeckPlayerInfo.setActivated(!textViewOthersMostPlayedDeckPlayerInfo.isActivated());
            }
        });
    }

    private void updateEditMode() {
        int color = mIsInEditMode ? ContextCompat.getColor(this, R.color.secondary_text) : ContextCompat.getColor(this, R.color.primary_color);

        statusBarBackground.setBackgroundColor(color);
        mActionBar.setBackgroundDrawable(new ColorDrawable(color));

        optionMenu.findItem(R.id.action_edit_deck).setVisible(mIsInEditMode);
        optionMenu.findItem(R.id.action_delete_deck).setVisible(mIsInEditMode);

        if (!mIsInEditMode)
            mDeckListAdapter.checkBoxClearAllSelections();
        mDeckListAdapter.setIsInEditMode(mIsInEditMode);
        mDeckListAdapter.notifyDataSetChanged();
    }

    private void updateLayout() {
        mActionBar.setTitle(mPlayerName);

        //Deck info - Card
        if (mCardViewFullHeightPlayerInfo == 0) {
            cardViewPlayerInfo.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    cardViewPlayerInfo.getViewTreeObserver().removeOnPreDrawListener(this);
                    mCardViewFullHeightPlayerInfo = cardViewPlayerInfo.getHeight();
                    return true;
                }
            });
        }
        //Deck info - title
        if (cardViewPlayerInfo.getHeight() == mCardViewFullHeightPlayerInfo) {
            textTitlePlayerInfo.setTextColor(ContextCompat.getColor(this, R.color.secondary_color));
            iconIndicatorPlayerInfo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.arrow_up));
            iconIndicatorPlayerInfo.setColorFilter(ContextCompat.getColor(this, R.color.secondary_color));
        } else {
            textTitlePlayerInfo.setTextColor(ContextCompat.getColor(this, R.color.secondary_text));
            iconIndicatorPlayerInfo.setColorFilter(ContextCompat.getColor(this, R.color.secondary_text));
        }

        List<Deck> allDecks = decksDB.getAllDeckByPlayerName(mPlayerName);
        List<Record> allRecords = recordsDB.getAllRecordsByPlayerName(mPlayerName);
        List<Record> allFirstRecords = recordsDB.getRecordsByPosition(mPlayerName, 1);
        final List<Deck> mostUsedDecks = recordsDB.getMostUsedDecks(allDecks);
        final List<Deck> leastUsedDecks = recordsDB.getLeastUsedDecks(allDecks);

        LinearLayout mostUsedDeck = (LinearLayout) findViewById(R.id.mostUsedDeck);
        View dividerMostUsedDeck = findViewById(R.id.dividerMostUsedDeck);
        if (mostUsedDecks.size() == 0) {
            mostUsedDeck.setVisibility(View.GONE);
            dividerMostUsedDeck.setVisibility(View.GONE);
        } else {
            mostUsedDeck.setVisibility(View.VISIBLE);
            dividerMostUsedDeck.setVisibility(View.VISIBLE);
            //Most played deck
            textViewMostPlayedDeckPlayerInfo.setText(mostUsedDecks.get(0).getDeckName());
            //Handle tied decks
            if (mostUsedDecks.size() == 1)
                textViewOthersMostPlayedDeckPlayerInfo.setText("");
            else
                handleTiedMostPlayedDecks(mostUsedDecks);
            //TIMES most played deck
            textViewMostTimePlayedDeckPlayerInfo.setText("" + recordsDB.getAllRecordsByDeck(mostUsedDecks.get(0)).size());
        }

        LinearLayout leastUsedDeck = (LinearLayout) findViewById(R.id.leastUsedDeck);
        View divideLeastUsedDeck = findViewById(R.id.dividerLeastUsedDeck);
        if (leastUsedDecks.size() == 0) {
            leastUsedDeck.setVisibility(View.GONE);
            divideLeastUsedDeck.setVisibility(View.GONE);
        } else {
            leastUsedDeck.setVisibility(View.VISIBLE);
            divideLeastUsedDeck.setVisibility(View.VISIBLE);
            //Least played deck
            textViewLeastPlayedDeckPlayerInfo.setText(leastUsedDecks.get(0).getDeckName());
            //Handle Least tied decks
            if (leastUsedDecks.size() == 1)
                textViewOthersLeastPlayedDeckPlayerInfo.setText("");
            else
                handleTiedLeastPlayedDecks(leastUsedDecks);
            //TIMES Least played deck
            textViewLeastTimePlayedDeckPlayerInfo.setText("" + recordsDB.getAllRecordsByDeck(leastUsedDecks.get(0)).size());
        }

        Player currentPlayer = playersDB.getPlayer(mPlayerName);
        textViewCreationDate.setText(currentPlayer.getPlayerDate());
        textViewTotalGames.setText("" + allRecords.size());
        textViewWins.setText("" + allFirstRecords.size());
        textViewTotalDecksPlayerInfo.setText("" + allDecks.size());

        //Card deckList
        if (deckList == null)
            deckList = new ArrayList<>();
        else
            deckList.clear();

        for (int i = 0; i < allDecks.size(); i++) {
            Deck auxDeck = new Deck(mPlayerName, allDecks.get(i).getDeckName());

            String imagePath = "image_" + mPlayerName + "_" + allDecks.get(i).getDeckName() + ".png";

            String title = auxDeck.getDeckName();

            int auxTotalVictories = 0;
            auxTotalVictories = auxTotalVictories + recordsDB.getRecordsByPosition(auxDeck, 1, 2).size();
            auxTotalVictories = auxTotalVictories + recordsDB.getRecordsByPosition(auxDeck, 1, 3).size();
            auxTotalVictories = auxTotalVictories + recordsDB.getRecordsByPosition(auxDeck, 1, 4).size();
            String subTitle = recordsDB.getAllRecordsByDeck(auxDeck).size() + " games and " + auxTotalVictories + (auxTotalVictories <= 1 ? " victory" : " victories");

            String identity = allDecks.get(i).getDeckIdentity();

            deckList.add(new String[]{imagePath, title, subTitle, identity, "false"});
        }
        mDeckListAdapter.notifyDataSetChanged();
        Utils.justifyListViewHeightBasedOnChildren(listDeckListCard);

        cardViewDeckList.setVisibility(View.GONE);
        if (deckList.size() > 0) {
            cardViewDeckList.setVisibility(View.VISIBLE);
            if (mCardViewFullHeightDeckList == 0) {
                cardViewDeckList.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        cardViewDeckList.getViewTreeObserver().removeOnPreDrawListener(this);
                        mCardViewFullHeightDeckList = cardViewDeckList.getHeight();

                        textTitleDeckListCard.setTextColor(ContextCompat.getColor(PlayerActivity.this, R.color.secondary_color));
                        indicatorDeckListCard.setImageDrawable(ContextCompat.getDrawable(PlayerActivity.this, R.drawable.arrow_up));
                        indicatorDeckListCard.setRotation(0);
                        indicatorDeckListCard.startAnimation(AnimationUtils.loadAnimation(PlayerActivity.this, R.anim.rotate_180_anticlockwise_instant));
                        indicatorDeckListCard.setColorFilter(ContextCompat.getColor(PlayerActivity.this, R.color.secondary_color));
                        return true;
                    }
                });
            }
        }

        //Card last game player
        cardViewRecordCard.setVisibility(View.GONE);
        if (allRecords.size() != 0) {
            cardViewRecordCard.setVisibility(View.VISIBLE);
            if (mCardViewFullHeightLastGamePlayed == 0) {
                cardViewRecordCard.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        cardViewRecordCard.getViewTreeObserver().removeOnPreDrawListener(this);
                        mCardViewFullHeightLastGamePlayed = cardViewRecordCard.getHeight();
                        ViewGroup.LayoutParams layoutParams = cardViewRecordCard.getLayoutParams();
                        layoutParams.height = relativeTitleRecordCard.getHeight();
                        cardViewRecordCard.setLayoutParams(layoutParams);
                        return true;
                    }
                });
            }

            Record lastRecord = allRecords.get(allRecords.size() - 1);
            textTitleRecordCard.setText("Last game played");
            Utils.createRecordListElement(this, lastRecord, mPlayerName);
        }

        //Total games and Chart2 1v1
        int firstIn2 = recordsDB.getRecordsByPosition(mPlayerName, 1, 2).size();
        int secondIn2 = recordsDB.getRecordsByPosition(mPlayerName, 2, 2).size();
        int total2 = firstIn2 + secondIn2;
        linearFullChart2.setVisibility(View.GONE);
        if (total2 != 0) {
            linearFullChart2.setVisibility(View.VISIBLE);
            donutChart2.updateDonutChart(new int[]{firstIn2, secondIn2});
        }

        //Total games and Chart3 1v1v1
        int firstIn3 = recordsDB.getRecordsByPosition(mPlayerName, 1, 3).size();
        int secondIn3 = recordsDB.getRecordsByPosition(mPlayerName, 2, 3).size();
        int thirdIn3 = recordsDB.getRecordsByPosition(mPlayerName, 3, 3).size();
        int total3 = firstIn3 + secondIn3 + thirdIn3;
        linearFullChart3.setVisibility(View.GONE);
        if (total3 != 0) {
            linearFullChart3.setVisibility(View.VISIBLE);
            donutChart3.updateDonutChart(new int[]{firstIn3, secondIn3, thirdIn3});
        }

        //Total games and Chart4 1v1v1v1
        int firstIn4 = recordsDB.getRecordsByPosition(mPlayerName, 1, 4).size();
        int secondIn4 = recordsDB.getRecordsByPosition(mPlayerName, 2, 4).size();
        int thirdIn4 = recordsDB.getRecordsByPosition(mPlayerName, 3, 4).size();
        int fourthIn4 = recordsDB.getRecordsByPosition(mPlayerName, 4, 4).size();
        int total4 = firstIn4 + secondIn4 + thirdIn4 + fourthIn4;
        linearFullChart4.setVisibility(View.GONE);
        if (total4 != 0) {
            linearFullChart4.setVisibility(View.VISIBLE);
            donutChart4.updateDonutChart(new int[]{firstIn4, secondIn4, thirdIn4, fourthIn4});
        }

        //Card history
        cardViewCharts.setVisibility(View.GONE);
        if (total2 + total3 + total4 > 0) {
            cardViewCharts.setVisibility(View.VISIBLE);
            if (mCardViewFullHeightDeckHistory == 0) {
                cardViewDeckList.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        cardViewCharts.getViewTreeObserver().removeOnPreDrawListener(this);
                        mCardViewFullHeightDeckHistory = cardViewCharts.getHeight();
                        ViewGroup.LayoutParams layoutParams = cardViewCharts.getLayoutParams();
                        layoutParams.height = relativeTitleHistoryCharts.getHeight();
                        cardViewCharts.setLayoutParams(layoutParams);
                        return true;
                    }
                });
            }
        }
    }
}
