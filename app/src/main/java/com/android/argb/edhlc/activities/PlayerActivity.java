package com.android.argb.edhlc.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.argb.edhlc.Constants;
import com.android.argb.edhlc.DeckListAdapter;
import com.android.argb.edhlc.R;
import com.android.argb.edhlc.Utils;
import com.android.argb.edhlc.chart.DonutChart;
import com.android.argb.edhlc.database.deck.DecksDataAccessObject;
import com.android.argb.edhlc.database.record.RecordsDataAccessObject;
import com.android.argb.edhlc.objects.Deck;
import com.android.argb.edhlc.objects.Record;

import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.renderer.DefaultRenderer;

import java.util.ArrayList;
import java.util.List;

//Crop: https://github.com/lvillani/android-cropimage
public class PlayerActivity extends AppCompatActivity {

    private String mPlayerName;

    private DecksDataAccessObject decksDB;
    private RecordsDataAccessObject recordsDB;

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
    private int[] COLORS;

    //Card - Deck list
    private CardView cardViewDeckList;
    private int mCardViewFullHeightDeckList = 0;
    private RelativeLayout relativeTitleDeckListCard;
    private TextView textTitleDeckListCard;
    private ImageView indicatorDeckListCard;
    private ListView listDeckListCard;
    private List<String[]> deckList;

    //Card - Chart deck history 2 - 1v1
    private CardView cardViewChart2Slots;
    private int mCardViewFullHeightDeckHistory2 = 0;
    private RelativeLayout relativeTitleChart2Slots;
    private TextView textTitleChart2Slots;
    private ImageView indicatorChart2Slots;
    private DefaultRenderer mDoughnutRender2;
    private MultipleCategorySeries mMultipleCategorySeriesDataSet2;
    private DonutChart donutChart2;

    //Card - Chart deck history 3 - 1v1v1
    private CardView cardViewChart3Slots;
    private int mCardViewFullHeightDeckHistory3 = 0;
    private RelativeLayout relativeTitleChart3Slots;
    private TextView textTitleChart3Slots;
    private ImageView indicatorChart3Slots;
    private DefaultRenderer mDoughnutRender3;
    private MultipleCategorySeries mMultipleCategorySeriesDataSet3;
    private DonutChart donutChart3;

    //Card - Chart deck history 4 - 1v1v1v1
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
        else {
            super.onBackPressed();
            //TODO FLUX
//            Intent intent = new Intent(PlayerActivity.this, PlayerListActivity.class);
//            startActivity(intent);
//            this.finish();
        }
    }

    public void onClickCardExpansion(View v) {
        switch (v.getId()) {

            case R.id.relativeCardTitlePlayerInfo:
                Utils.toggleCardExpansion(this, cardViewPlayerInfo, textTitlePlayerInfo, iconIndicatorPlayerInfo, relativeTitlePlayerInfo.getHeight(), mCardViewFullHeightPlayerInfo);
                break;

            case R.id.relativeCardTitleDeckList:
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

    //TODO to be used in all activities
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
                //TODO
                createAddDeckDialog();
                animateFAB();
                break;
            case R.id.fabMain:
                animateFAB();
                break;
            case R.id.fabEdit:
                createEditPlayerDialog();
                animateFAB();
                break;
            case R.id.fabDelete:
                createRemovePlayerDialog();
                animateFAB();
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
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
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

    private void createAddDeckDialog() {
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

    private void createEditPlayerDialog() {
        //TODO
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


        //Chart 1v1
        cardViewChart2Slots = (CardView) findViewById(R.id.cardViewChart2Slots);
        relativeTitleChart2Slots = (RelativeLayout) findViewById(R.id.relativeCardTitleChart2Slots);
        textTitleChart2Slots = (TextView) findViewById(R.id.textTitleChart2Slots);
        indicatorChart2Slots = (ImageView) findViewById(R.id.indicatorChart2Slots);
        donutChart2 = new DonutChart(this, mDoughnutRender2, mMultipleCategorySeriesDataSet2);

        //Chart 1v1v1
        cardViewChart3Slots = (CardView) findViewById(R.id.cardViewChart3Slots);
        relativeTitleChart3Slots = (RelativeLayout) findViewById(R.id.relativeCardTitleChart3Slots);
        textTitleChart3Slots = (TextView) findViewById(R.id.textTitleChart3Slots);
        indicatorChart3Slots = (ImageView) findViewById(R.id.indicatorChart3Slots);
        donutChart3 = new DonutChart(this, mDoughnutRender3, mMultipleCategorySeriesDataSet3);

        //Chart 1v1v1v1
        cardViewChart4Slots = (CardView) findViewById(R.id.cardViewChart4Slots);
        relativeTitleChart4Slots = (RelativeLayout) findViewById(R.id.relativeCardTitleChart4Slots);
        textTitleChart4Slots = (TextView) findViewById(R.id.textTitleChart4Slots);
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

    private void createRemovePlayerDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PlayerActivity.this);
        alertDialogBuilder.setTitle("Delete deck");
        alertDialogBuilder.setMessage("Are you sure to delete this player?");
        alertDialogBuilder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO
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

    private void createStatusBar() {
        View statusBarBackground = findViewById(R.id.statusBarBackground);
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
        final Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(mPlayerName);
    }

    private void updateLayout() {
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

        //TODO Most played general
        textViewMostPlayedDeckPlayerInfo.setText("TODO");
        //TODO Most played general - tines
        textViewTimePlayedDeckPlayerInfo.setText("TODO");
        //TODO Creation Date
        textViewCreationDate.setText("TODO");
        //TODO Total games
        textViewTotalGames.setText("TODO");
        //TODO Wins
        textViewWins.setText("TODO");
        //TODO Total decks
        textViewTotalDecksPlayerInfo.setText("TODO");

        //Card deckList
        deckList = new ArrayList<>();
        List<Deck> aux = decksDB.getAllDeckByPlayerName(mPlayerName);
        for (int i = 0; i < aux.size(); i++) {
            String imagePath = "image_" + mPlayerName + "_" + aux.get(i).getDeckName() + ".png";
            String title = aux.get(i).getDeckName();
            String subTitle = "TODO";

            deckList.add(new String[]{imagePath, title, subTitle});
        }
        listDeckListCard.setAdapter(new DeckListAdapter(this, deckList));
        Utils.justifyListViewHeightBasedOnChildren(listDeckListCard);

        if (mCardViewFullHeightDeckList == 0) {
            cardViewDeckList.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    cardViewDeckList.getViewTreeObserver().removeOnPreDrawListener(this);
                    mCardViewFullHeightDeckList = cardViewDeckList.getHeight();
                    ViewGroup.LayoutParams layoutParams = cardViewDeckList.getLayoutParams();
                    layoutParams.height = relativeTitleRecordCard.getHeight();
                    cardViewDeckList.setLayoutParams(layoutParams);
                    return true;
                }
            });
        }

        //Card last game player
        List<Record> allRecords = recordsDB.getAllRecordsByPlayerName(mPlayerName);
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

        //Deck info - Total Games
        textViewTotalGames.setText("" + (total2 + total3 + total4));
        //Deck info - Wins
        textViewWins.setText("" + (firstIn2 + firstIn3 + firstIn4));
    }
}
