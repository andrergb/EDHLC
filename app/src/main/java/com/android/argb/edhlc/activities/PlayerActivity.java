package com.android.argb.edhlc.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Typeface;
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

//Crop: https://github.com/lvillani/android-cropimage
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
    private TextView textViewTimePlayedDeckPlayerInfo;
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

    //Card - Chart player history 2 - 1v1
    private CardView cardViewChart2Slots;
    private int mCardViewFullHeightDeckHistory2 = 0;
    private RelativeLayout relativeTitleChart2Slots;
    private TextView textTitleChart2Slots;
    private ImageView indicatorChart2Slots;
    private DefaultRenderer mDoughnutRender2;
    private MultipleCategorySeries mMultipleCategorySeriesDataSet2;
    private DonutChart donutChart2;

    //Card - Chart player history 3 - 1v1v1
    private CardView cardViewChart3Slots;
    private int mCardViewFullHeightDeckHistory3 = 0;
    private RelativeLayout relativeTitleChart3Slots;
    private TextView textTitleChart3Slots;
    private ImageView indicatorChart3Slots;
    private DefaultRenderer mDoughnutRender3;
    private MultipleCategorySeries mMultipleCategorySeriesDataSet3;
    private DonutChart donutChart3;

    //Card - Chart player history 4 - 1v1v1v1
    private CardView cardViewChart4Slots;
    private int mCardViewFullHeightDeckHistory4 = 0;
    private RelativeLayout relativeTitleChart4Slots;
    private TextView textTitleChart4Slots;
    private ImageView indicatorChart4Slots;
    private DefaultRenderer mDoughnutRender4;
    private MultipleCategorySeries mMultipleCategorySeriesDataSet4;
    private DonutChart donutChart4;

    //Card - Last game played
    private CardView cardViewRecordCard;
    private int mCardViewFullHeightLastGamePlayed = 0;
    private RelativeLayout relativeTitleRecordCard;
    private TextView textTitleRecordCard;
    private ImageView indicatorRecordCard;
    private TextView textDateRecordCard;
    private LinearLayout linearFirstLineRecordCard;
    private TextView textFirstIndicatorRecordCard;
    private TextView textFirstPlayerRecordCard;
    private TextView textFirstDeckRecordCard;
    private View divider1RecordCard;
    private LinearLayout linearSecondLineRecordCard;
    private TextView textSecondIndicatorRecordCard;
    private TextView textSecondPlayerRecordCard;
    private TextView textSecondDeckRecordCard;
    private View divider2RecordCard;
    private LinearLayout linearThirdLineRecordCard;
    private TextView textThirdIndicatorRecordCard;
    private TextView textThirdPlayerRecordCard;
    private TextView textThirdDeckRecordCard;
    private View divider3RecordCard;
    private LinearLayout linearFourthLineRecordCard;
    private TextView textFourthIndicatorRecordCard;
    private TextView textFourthPlayerRecordCard;
    private TextView textFourthDeckRecordCard;

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

            case R.id.relativeCardTitleChart2Slots:
                Utils.toggleCardExpansion(this, cardViewChart2Slots, textTitleChart2Slots, indicatorChart2Slots, relativeTitleChart2Slots.getHeight(), mCardViewFullHeightDeckHistory2);
                break;

            case R.id.relativeCardTitleChart3Slots:
                Utils.toggleCardExpansion(this, cardViewChart3Slots, textTitleChart3Slots, indicatorChart3Slots, relativeTitleChart3Slots.getHeight(), mCardViewFullHeightDeckHistory3);
                break;

            case R.id.relativeCardTitleChart4Slots:
                Utils.toggleCardExpansion(this, cardViewChart4Slots, textTitleChart4Slots, indicatorChart4Slots, relativeTitleChart4Slots.getHeight(), mCardViewFullHeightDeckHistory4);
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
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;

            case R.id.drawerItemPlayers:
                mPlayerDrawerLayout.closeDrawers();
                startActivity(new Intent(this, PlayerListActivity.class));
                finish();
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
        mPlayerName = intent.getStringExtra("PLAYERNAME");

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

        //TODO selected item on drawer - temp local
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
        //Most played general - tines
        textViewTimePlayedDeckPlayerInfo = (TextView) findViewById(R.id.textViewTimePlayedDeckPlayerInfo);
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

        //Chart 1v1
        cardViewChart2Slots = (CardView) findViewById(R.id.cardViewChart2Slots);
        relativeTitleChart2Slots = (RelativeLayout) findViewById(R.id.relativeCardTitleChart2Slots);
        textTitleChart2Slots = (TextView) findViewById(R.id.textTitleChart2Slots);
        textTitleChart2Slots.setText("Player History - 2 players");
        indicatorChart2Slots = (ImageView) findViewById(R.id.indicatorChart2Slots);
        donutChart2 = new DonutChart(this, mDoughnutRender2, mMultipleCategorySeriesDataSet2);

        //Chart 1v1v1
        cardViewChart3Slots = (CardView) findViewById(R.id.cardViewChart3Slots);
        relativeTitleChart3Slots = (RelativeLayout) findViewById(R.id.relativeCardTitleChart3Slots);
        textTitleChart3Slots = (TextView) findViewById(R.id.textTitleChart3Slots);
        textTitleChart3Slots.setText("Player History - 2 players");
        indicatorChart3Slots = (ImageView) findViewById(R.id.indicatorChart3Slots);
        donutChart3 = new DonutChart(this, mDoughnutRender3, mMultipleCategorySeriesDataSet3);

        //Chart 1v1v1v1
        cardViewChart4Slots = (CardView) findViewById(R.id.cardViewChart4Slots);
        relativeTitleChart4Slots = (RelativeLayout) findViewById(R.id.relativeCardTitleChart4Slots);
        textTitleChart4Slots = (TextView) findViewById(R.id.textTitleChart4Slots);
        textTitleChart4Slots.setText("Player History - 2 players");
        indicatorChart4Slots = (ImageView) findViewById(R.id.indicatorChart4Slots);
        donutChart4 = new DonutChart(this, mDoughnutRender4, mMultipleCategorySeriesDataSet4);

        //Chart lastGamePlayed
        cardViewRecordCard = (CardView) findViewById(R.id.cardViewRecordCard);
        relativeTitleRecordCard = (RelativeLayout) findViewById(R.id.relativeCardTitleRecord);
        indicatorRecordCard = (ImageView) findViewById(R.id.indicatorRecordCard);
        textTitleRecordCard = (TextView) findViewById(R.id.textTitleRecordCard);
        textDateRecordCard = (TextView) findViewById(R.id.textDateRecordCard);

        linearFirstLineRecordCard = (LinearLayout) findViewById(R.id.linearFirstLineRecordCard);
        textFirstIndicatorRecordCard = (TextView) findViewById(R.id.textFirstIndicatorRecordCard);
        textFirstPlayerRecordCard = (TextView) findViewById(R.id.textFirstPlayerRecordCard);
        textFirstDeckRecordCard = (TextView) findViewById(R.id.textFirstDeckRecordCard);
        divider1RecordCard = findViewById(R.id.divider1RecordCard);

        linearSecondLineRecordCard = (LinearLayout) findViewById(R.id.linearSecondLineRecordCard);
        textSecondIndicatorRecordCard = (TextView) findViewById(R.id.textSecondIndicatorRecordCard);
        textSecondPlayerRecordCard = (TextView) findViewById(R.id.textSecondPlayerRecordCard);
        textSecondDeckRecordCard = (TextView) findViewById(R.id.textSecondDeckRecordCard);
        divider2RecordCard = findViewById(R.id.divider2RecordCard);

        linearThirdLineRecordCard = (LinearLayout) findViewById(R.id.linearThirdLineRecordCard);
        textThirdIndicatorRecordCard = (TextView) findViewById(R.id.textThirdIndicatorRecordCard);
        textThirdPlayerRecordCard = (TextView) findViewById(R.id.textThirdPlayerRecordCard);
        textThirdDeckRecordCard = (TextView) findViewById(R.id.textThirdDeckRecordCard);
        divider3RecordCard = findViewById(R.id.divider3RecordCard);

        linearFourthLineRecordCard = (LinearLayout) findViewById(R.id.linearFourthLineRecordCard);
        textFourthIndicatorRecordCard = (TextView) findViewById(R.id.textFourthIndicatorRecordCard);
        textFourthPlayerRecordCard = (TextView) findViewById(R.id.textFourthPlayerRecordCard);
        textFourthDeckRecordCard = (TextView) findViewById(R.id.textFourthDeckRecordCard);
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
        alertDialogBuilder.setMessage("Are you sure to delete these deck?");
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

    private void updateEditMode() {
        int color = mIsInEditMode ? ContextCompat.getColor(this, R.color.divider) : ContextCompat.getColor(this, R.color.primary_color);

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
        List<Deck> mostUsedDecks = recordsDB.getMostUsedDecks(allDecks);
        int timesUsedMostPlayedDeck = recordsDB.getAllRecordsByDeck(mostUsedDecks.get(0)).size();

        Player currentPlayer = playersDB.getPlayer(mPlayerName);

        String auxMostPlayedDeck = mostUsedDecks.get(0).getDeckName();
        int totalMostUsedDecks = mostUsedDecks.size() - 1;
        if (totalMostUsedDecks == 1)
            auxMostPlayedDeck = auxMostPlayedDeck + " and " + totalMostUsedDecks + " other";
        else if (totalMostUsedDecks > 1)
            auxMostPlayedDeck = auxMostPlayedDeck + " and " + totalMostUsedDecks + " others";


        textViewMostPlayedDeckPlayerInfo.setText(auxMostPlayedDeck);
        textViewTimePlayedDeckPlayerInfo.setText("" + timesUsedMostPlayedDeck);

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

        if (mCardViewFullHeightDeckList == 0) {
            cardViewDeckList.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    cardViewDeckList.getViewTreeObserver().removeOnPreDrawListener(this);
                    mCardViewFullHeightDeckList = cardViewDeckList.getHeight();

                    ViewGroup.LayoutParams layoutParams = cardViewDeckList.getLayoutParams();
                    layoutParams.height = relativeTitleDeckListCard.getHeight();
                    cardViewDeckList.setLayoutParams(layoutParams);
                    return true;
                }
            });
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

            Typeface typefaceMedium = Typeface.create("sans-serif-medium", Typeface.NORMAL);
            Typeface typefaceNormal = Typeface.create("sans-serif", Typeface.NORMAL);
            int colorSelected = ContextCompat.getColor(this, R.color.accent_color);
            int colorPrimary = ContextCompat.getColor(this, R.color.primary_text);
            int colorSecondary = ContextCompat.getColor(this, R.color.secondary_text);

            Record lastRecord = allRecords.get(allRecords.size() - 1);
            textTitleRecordCard.setText("Last game played");
            textDateRecordCard.setText("Played on " + lastRecord.getDate());
            switch (lastRecord.getTotalPlayers()) {
                case 2:
                    linearFirstLineRecordCard.setVisibility(View.VISIBLE);
                    linearSecondLineRecordCard.setVisibility(View.VISIBLE);
                    linearThirdLineRecordCard.setVisibility(View.GONE);
                    linearFourthLineRecordCard.setVisibility(View.GONE);

                    divider1RecordCard.setVisibility(View.VISIBLE);
                    divider2RecordCard.setVisibility(View.GONE);
                    divider3RecordCard.setVisibility(View.GONE);

                    textFirstDeckRecordCard.setText(lastRecord.getFirstPlace().getDeckName());
                    textFirstDeckRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getFirstPlace().getDeckOwnerName()) ? colorSelected : colorPrimary);
                    textFirstDeckRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getFirstPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textFirstPlayerRecordCard.setText(lastRecord.getFirstPlace().getDeckOwnerName());
                    textFirstPlayerRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getFirstPlace().getDeckOwnerName()) ? colorSelected : colorSecondary);
                    textFirstPlayerRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getFirstPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textFirstIndicatorRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getFirstPlace().getDeckOwnerName()) ? colorSelected : colorSecondary);
                    textFirstIndicatorRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getFirstPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);

                    textSecondDeckRecordCard.setText(lastRecord.getSecondPlace().getDeckName());
                    textSecondDeckRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getSecondPlace().getDeckOwnerName()) ? colorSelected : colorPrimary);
                    textSecondDeckRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getSecondPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textSecondPlayerRecordCard.setText(lastRecord.getSecondPlace().getDeckOwnerName());
                    textSecondPlayerRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getSecondPlace().getDeckOwnerName()) ? colorSelected : colorSecondary);
                    textSecondPlayerRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getSecondPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textSecondIndicatorRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getSecondPlace().getDeckOwnerName()) ? colorSelected : colorSecondary);
                    textSecondIndicatorRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getSecondPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);

                    break;

                case 3:
                    linearFirstLineRecordCard.setVisibility(View.VISIBLE);
                    linearSecondLineRecordCard.setVisibility(View.VISIBLE);
                    linearThirdLineRecordCard.setVisibility(View.VISIBLE);
                    linearFourthLineRecordCard.setVisibility(View.GONE);

                    divider1RecordCard.setVisibility(View.VISIBLE);
                    divider2RecordCard.setVisibility(View.VISIBLE);
                    divider3RecordCard.setVisibility(View.GONE);

                    textFirstDeckRecordCard.setText(lastRecord.getFirstPlace().getDeckName());
                    textFirstDeckRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getFirstPlace().getDeckOwnerName()) ? colorSelected : colorPrimary);
                    textFirstDeckRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getFirstPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textFirstPlayerRecordCard.setText(lastRecord.getFirstPlace().getDeckOwnerName());
                    textFirstPlayerRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getFirstPlace().getDeckOwnerName()) ? colorSelected : colorSecondary);
                    textFirstPlayerRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getFirstPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textFirstIndicatorRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getFirstPlace().getDeckOwnerName()) ? colorSelected : colorSecondary);
                    textFirstIndicatorRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getFirstPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);

                    textSecondDeckRecordCard.setText(lastRecord.getSecondPlace().getDeckName());
                    textSecondDeckRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getSecondPlace().getDeckOwnerName()) ? colorSelected : colorPrimary);
                    textSecondDeckRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getSecondPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textSecondPlayerRecordCard.setText(lastRecord.getSecondPlace().getDeckOwnerName());
                    textSecondPlayerRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getSecondPlace().getDeckOwnerName()) ? colorSelected : colorSecondary);
                    textSecondPlayerRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getSecondPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textSecondIndicatorRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getSecondPlace().getDeckOwnerName()) ? colorSelected : colorSecondary);
                    textSecondIndicatorRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getSecondPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);

                    textThirdDeckRecordCard.setText(lastRecord.getThirdPlace().getDeckName());
                    textThirdDeckRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getThirdPlace().getDeckOwnerName()) ? colorSelected : colorPrimary);
                    textThirdDeckRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getThirdPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textThirdPlayerRecordCard.setText(lastRecord.getThirdPlace().getDeckOwnerName());
                    textThirdPlayerRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getThirdPlace().getDeckOwnerName()) ? colorSelected : colorSecondary);
                    textThirdPlayerRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getThirdPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textThirdIndicatorRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getThirdPlace().getDeckOwnerName()) ? colorSelected : colorSecondary);
                    textThirdIndicatorRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getThirdPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    break;

                case 4:
                    linearFirstLineRecordCard.setVisibility(View.VISIBLE);
                    linearSecondLineRecordCard.setVisibility(View.VISIBLE);
                    linearThirdLineRecordCard.setVisibility(View.VISIBLE);
                    linearFourthLineRecordCard.setVisibility(View.VISIBLE);

                    divider1RecordCard.setVisibility(View.VISIBLE);
                    divider2RecordCard.setVisibility(View.VISIBLE);
                    divider3RecordCard.setVisibility(View.VISIBLE);

                    textFirstDeckRecordCard.setText(lastRecord.getFirstPlace().getDeckName());
                    textFirstDeckRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getFirstPlace().getDeckOwnerName()) ? colorSelected : colorPrimary);
                    textFirstDeckRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getFirstPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textFirstPlayerRecordCard.setText(lastRecord.getFirstPlace().getDeckOwnerName());
                    textFirstPlayerRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getFirstPlace().getDeckOwnerName()) ? colorSelected : colorSecondary);
                    textFirstPlayerRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getFirstPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textFirstIndicatorRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getFirstPlace().getDeckOwnerName()) ? colorSelected : colorSecondary);
                    textFirstIndicatorRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getFirstPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);

                    textSecondDeckRecordCard.setText(lastRecord.getSecondPlace().getDeckName());
                    textSecondDeckRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getSecondPlace().getDeckOwnerName()) ? colorSelected : colorPrimary);
                    textSecondDeckRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getSecondPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textSecondPlayerRecordCard.setText(lastRecord.getSecondPlace().getDeckOwnerName());
                    textSecondPlayerRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getSecondPlace().getDeckOwnerName()) ? colorSelected : colorSecondary);
                    textSecondPlayerRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getSecondPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textSecondIndicatorRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getSecondPlace().getDeckOwnerName()) ? colorSelected : colorSecondary);
                    textSecondIndicatorRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getSecondPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);

                    textThirdDeckRecordCard.setText(lastRecord.getThirdPlace().getDeckName());
                    textThirdDeckRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getThirdPlace().getDeckOwnerName()) ? colorSelected : colorPrimary);
                    textThirdDeckRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getThirdPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textThirdPlayerRecordCard.setText(lastRecord.getThirdPlace().getDeckOwnerName());
                    textThirdPlayerRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getThirdPlace().getDeckOwnerName()) ? colorSelected : colorSecondary);
                    textThirdPlayerRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getThirdPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textThirdIndicatorRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getThirdPlace().getDeckOwnerName()) ? colorSelected : colorSecondary);
                    textThirdIndicatorRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getThirdPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);

                    textFourthDeckRecordCard.setText(lastRecord.getFourthPlace().getDeckName());
                    textFourthDeckRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getFourthPlace().getDeckOwnerName()) ? colorSelected : colorPrimary);
                    textFourthDeckRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getFourthPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textFourthPlayerRecordCard.setText(lastRecord.getFourthPlace().getDeckOwnerName());
                    textFourthPlayerRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getFourthPlace().getDeckOwnerName()) ? colorSelected : colorSecondary);
                    textFourthPlayerRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getFourthPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    textFourthIndicatorRecordCard.setTextColor(mPlayerName.equalsIgnoreCase(lastRecord.getFourthPlace().getDeckOwnerName()) ? colorSelected : colorSecondary);
                    textFourthIndicatorRecordCard.setTypeface(mPlayerName.equalsIgnoreCase(lastRecord.getFourthPlace().getDeckOwnerName()) ? typefaceMedium : typefaceNormal);
                    break;
            }
        }

        //Total games and Chart2 1v1
        int firstIn2 = recordsDB.getRecordsByPosition(mPlayerName, 1, 2).size();
        int secondIn2 = recordsDB.getRecordsByPosition(mPlayerName, 2, 2).size();
        int total2 = firstIn2 + secondIn2;
        cardViewChart2Slots.setVisibility(View.GONE);
        if (total2 != 0) {
            cardViewChart2Slots.setVisibility(View.VISIBLE);
            if (mCardViewFullHeightDeckHistory2 == 0) {
                cardViewChart2Slots.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        cardViewChart2Slots.getViewTreeObserver().removeOnPreDrawListener(this);
                        mCardViewFullHeightDeckHistory2 = cardViewChart2Slots.getHeight();
                        ViewGroup.LayoutParams layoutParams = cardViewChart2Slots.getLayoutParams();
                        layoutParams.height = relativeTitleChart2Slots.getHeight();
                        cardViewChart2Slots.setLayoutParams(layoutParams);
                        return true;
                    }
                });
            }
            donutChart2.updateDonutChart(new int[]{firstIn2, secondIn2});
        }

        //Total games and Chart3 1v1v1
        int firstIn3 = recordsDB.getRecordsByPosition(mPlayerName, 1, 3).size();
        int secondIn3 = recordsDB.getRecordsByPosition(mPlayerName, 2, 3).size();
        int thirdIn3 = recordsDB.getRecordsByPosition(mPlayerName, 3, 3).size();
        int total3 = firstIn3 + secondIn3 + thirdIn3;
        cardViewChart3Slots.setVisibility(View.GONE);
        if (total3 != 0) {
            cardViewChart3Slots.setVisibility(View.VISIBLE);
            if (mCardViewFullHeightDeckHistory3 == 0) {
                cardViewChart3Slots.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        cardViewChart3Slots.getViewTreeObserver().removeOnPreDrawListener(this);
                        mCardViewFullHeightDeckHistory3 = cardViewChart3Slots.getHeight();
                        ViewGroup.LayoutParams layoutParams = cardViewChart3Slots.getLayoutParams();
                        layoutParams.height = relativeTitleChart3Slots.getHeight();
                        cardViewChart3Slots.setLayoutParams(layoutParams);
                        return true;
                    }
                });
            }
            donutChart3.updateDonutChart(new int[]{firstIn3, secondIn3, thirdIn3});
        }

        //Total games and Chart4 1v1v1v1
        int firstIn4 = recordsDB.getRecordsByPosition(mPlayerName, 1, 4).size();
        int secondIn4 = recordsDB.getRecordsByPosition(mPlayerName, 2, 4).size();
        int thirdIn4 = recordsDB.getRecordsByPosition(mPlayerName, 3, 4).size();
        int fourthIn4 = recordsDB.getRecordsByPosition(mPlayerName, 4, 4).size();
        int total4 = firstIn4 + secondIn4 + thirdIn4 + fourthIn4;
        cardViewChart4Slots.setVisibility(View.GONE);
        if (total4 != 0) {
            cardViewChart4Slots.setVisibility(View.VISIBLE);
            if (mCardViewFullHeightDeckHistory4 == 0) {
                cardViewChart4Slots.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        cardViewChart4Slots.getViewTreeObserver().removeOnPreDrawListener(this);
                        mCardViewFullHeightDeckHistory4 = cardViewChart4Slots.getHeight();
                        ViewGroup.LayoutParams layoutParams = cardViewChart4Slots.getLayoutParams();
                        layoutParams.height = relativeTitleChart4Slots.getHeight();
                        cardViewChart4Slots.setLayoutParams(layoutParams);

                        return true;
                    }
                });
            }
            donutChart4.updateDonutChart(new int[]{firstIn4, secondIn4, thirdIn4, fourthIn4});
        }
    }
}
