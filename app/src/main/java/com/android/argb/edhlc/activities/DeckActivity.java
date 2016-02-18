package com.android.argb.edhlc.activities;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.argb.edhlc.Constants;
import com.android.argb.edhlc.FABScrollBehavior;
import com.android.argb.edhlc.R;
import com.android.argb.edhlc.database.deck.DecksDataAccessObject;
import com.android.argb.edhlc.database.record.RecordsDataAccessObject;
import com.android.argb.edhlc.objects.Deck;
import com.android.argb.edhlc.objects.Record;
import com.android.camera.CropImageIntentBuilder;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//Crop: https://github.com/lvillani/android-cropimage
public class DeckActivity extends AppCompatActivity {

    private static int REQUEST_PICTURE_PICKER = 1;

    private Deck mCurrentDeck;
    private String mPlayerName;
    private String mDeckName;
    private String mDeckIdentity;

    private DecksDataAccessObject decksDB;
    private RecordsDataAccessObject recordsDB;

    //Main card - Deck info
    private CardView cardViewDeckInfo;
    private int mCardViewFullHeightDeckInfo = 0;
    private RelativeLayout relativeTitleDeckInfo;
    private TextView textTitleDeckInfo;
    private ImageView iconIndicatorDeckInfo;
    private TextView textViewCommander;
    private List<ImageView> listIdentityHolder;
    private TextView textViewWins;
    private TextView textViewTotalGames;
    private TextView textViewOwnerName;
    private TextView textViewCreationDate;
    private ImageView imageViewShieldColor;
    private int[] COLORS;

    //Card - Chart deck history 2 - 1v1
    private CardView cardViewDeckHistory2;
    private int mCardViewFullHeightDeckHistory2 = 0;
    private RelativeLayout relativeTitleDeckHistory2;
    private TextView textTitleDeckHistory2;
    private ImageView iconIndicatorDeckHistory2;
    private DefaultRenderer mDoughnutRender2;
    private LinearLayout doughnutChartLinearLayout2;
    private MultipleCategorySeries mMultipleCategorySeriesDataSet2;
    private TextView textViewTotalGameNumber2;
    private TextView textViewTotalGameText2;
    private TextView textViewFirst2;
    private TextView textViewSecond2;

    //Card - Chart deck history 3 - 1v1v1
    private CardView cardViewDeckHistory3;
    private int mCardViewFullHeightDeckHistory3 = 0;
    private RelativeLayout relativeTitleDeckHistory3;
    private TextView textTitleDeckHistory3;
    private ImageView iconIndicatorDeckHistory3;
    private DefaultRenderer mDoughnutRender3;
    private LinearLayout doughnutChartLinearLayout3;
    private MultipleCategorySeries mMultipleCategorySeriesDataSet3;
    private TextView textViewTotalGameNumber3;
    private TextView textViewTotalGameText3;
    private TextView textViewFirst3;
    private TextView textViewSecond3;
    private TextView textViewThird3;

    //Card - Chart deck history 4 - 1v1v1v1
    private CardView cardViewDeckHistory4;
    private int mCardViewFullHeightDeckHistory4 = 0;
    private RelativeLayout relativeTitleDeckHistory4;
    private TextView textTitleDeckHistory4;
    private ImageView iconIndicatorDeckHistory4;
    private DefaultRenderer mDoughnutRender4;
    private LinearLayout doughnutChartLinearLayout4;
    private MultipleCategorySeries mMultipleCategorySeriesDataSet4;
    private TextView textViewTotalGameNumber4;
    private TextView textViewTotalGameText4;
    private TextView textViewFirst4;
    private TextView textViewSecond4;
    private TextView textViewThird4;
    private TextView textViewFourth4;

    //Card - Last game played
    private CardView cardViewLastGamePlayed;
    private int mCardViewFullHeightLastGamePlayed = 0;
    private RelativeLayout relativeTitleLastGamePlayed;
    private TextView textTitleLastGamePlayed;
    private ImageView iconIndicatorLastGamePlayed;
    private LinearLayout linearLastGame1;
    private TextView textViewRecordDate;
    private TextView textViewPlayer1;
    private TextView textViewDeck1;
    private LinearLayout linearLastGame2;
    private TextView textViewPlayer2;
    private TextView textViewDeck2;
    private LinearLayout linearLastGame3;
    private TextView textViewPlayer3;
    private TextView textViewDeck3;
    private LinearLayout linearLastGame4;
    private TextView textViewPlayer4;
    private TextView textViewDeck4;

    //Toolbar
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView imageViewBanner;

    ///Drawer
    private DrawerLayout mDeckDrawerLayout;
    private LinearLayout mDeckDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Switch switchScreen;

    //Fab
    private boolean isFabOpen;
    private FloatingActionButton fabMain;
    private FloatingActionButton fabEdit;
    private FloatingActionButton fabDelete;

    //Edit dialog
    private CheckBox checkBoxManaWhite;
    private CheckBox checkBoxManaBlue;
    private CheckBox checkBoxManaBlack;
    private CheckBox checkBoxManaRed;
    private CheckBox checkBoxManaGreen;
    private CheckBox checkBoxManaColorless;

    public static Intent getPickImageIntent() {
        final Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return Intent.createChooser(intent, "Select Picture");
    }

    public void animateFAB() {
        Animation fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        Animation fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        Animation rotate45Clockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_45_clockwise);
        Animation rotate45Anticlockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_45_anticlockwise);
        if (isFabOpen) {
            fabMain.startAnimation(rotate45Anticlockwise);
            fabMain.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.accent_color)));

            fabEdit.setClickable(false);
            fabEdit.setVisibility(View.GONE);
            fabEdit.startAnimation(fab_close);

            fabDelete.setClickable(false);
            fabDelete.setVisibility(View.GONE);
            fabDelete.startAnimation(fab_close);

            isFabOpen = false;
        } else {
            fabMain.startAnimation(rotate45Clockwise);
            fabMain.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.accent_secondary_color)));

            fabEdit.setClickable(true);
            fabEdit.setVisibility(View.VISIBLE);
            fabEdit.startAnimation(fab_open);
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fabEdit.getLayoutParams();
            p.setBehavior(new FABScrollBehavior());
            fabEdit.setLayoutParams(p);

            fabDelete.setClickable(true);
            fabDelete.setVisibility(View.VISIBLE);
            fabDelete.startAnimation(fab_open);
            p = (CoordinatorLayout.LayoutParams) fabDelete.getLayoutParams();
            p.setBehavior(new FABScrollBehavior());
            fabDelete.setLayoutParams(p);

            isFabOpen = true;
        }
    }

    @Override
    public void onBackPressed() {
        if (mDeckDrawerLayout.isDrawerOpen(mDeckDrawer))
            mDeckDrawerLayout.closeDrawers();
        else {
            super.onBackPressed();
            Intent intent = new Intent(DeckActivity.this, PlayerListActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    public void onClickCardExpansion(View v) {
        switch (v.getId()) {

            case R.id.relativeTitleDeckInfo:
                toggleCardExpansion(cardViewDeckInfo, textTitleDeckInfo, iconIndicatorDeckInfo, relativeTitleDeckInfo.getHeight(), mCardViewFullHeightDeckInfo);
                break;

            case R.id.relativeTitleLastGamePlayed:
                toggleCardExpansion(cardViewLastGamePlayed, textTitleLastGamePlayed, iconIndicatorLastGamePlayed, relativeTitleLastGamePlayed.getHeight(), mCardViewFullHeightLastGamePlayed);
                break;

            case R.id.relativeTitleDeckHistory2:
                toggleCardExpansion(cardViewDeckHistory2, textTitleDeckHistory2, iconIndicatorDeckHistory2, relativeTitleDeckHistory2.getHeight(), mCardViewFullHeightDeckHistory2);
                break;

            case R.id.relativeTitleDeckHistory3:
                toggleCardExpansion(cardViewDeckHistory3, textTitleDeckHistory3, iconIndicatorDeckHistory3, relativeTitleDeckHistory3.getHeight(), mCardViewFullHeightDeckHistory3);
                break;

            case R.id.relativeTitleDeckHistory4:
                toggleCardExpansion(cardViewDeckHistory4, textTitleDeckHistory4, iconIndicatorDeckHistory4, relativeTitleDeckHistory4.getHeight(), mCardViewFullHeightDeckHistory4);
                break;
        }
    }

    //TODO to be used in all activities
    public void onClickDrawerItem(View view) {
        switch (view.getId()) {
            case R.id.drawerItemHome:
                mDeckDrawerLayout.closeDrawers();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;

            case R.id.drawerItemPlayers:
                mDeckDrawerLayout.closeDrawers();
                startActivity(new Intent(this, PlayerListActivity.class));
                finish();
                break;

            case R.id.drawerItemRecords:
                mDeckDrawerLayout.closeDrawers();
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
                mDeckDrawerLayout.closeDrawers();
                startActivity(new Intent(this, SettingsActivity.class));
                finish();
                break;

            case R.id.drawerItemAbout:
                mDeckDrawerLayout.closeDrawers();
                //TODO
                break;
        }
    }

    public void onClickFabButton(View view) {
        switch (view.getId()) {
            case R.id.fabMain:
                animateFAB();
                break;
            case R.id.fabEdit:
                createEditDeckDialog();
                break;
            case R.id.fabDelete:
                createRemoveDeckDialog();
                break;
        }
    }

    public void onClickImageBanner(View view) {
        startActivityForResult(getPickImageIntent(), REQUEST_PICTURE_PICKER);
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
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public void updateDonutChart2(int first, int second) {
        textViewFirst2.setText("First: " + first);
        textViewSecond2.setText("Second: " + second);

        COLORS = new int[]{ContextCompat.getColor(this, R.color.first),
                ContextCompat.getColor(this, android.R.color.transparent),
                ContextCompat.getColor(this, R.color.second),
                ContextCompat.getColor(this, android.R.color.transparent)};

        List<double[]> values = new ArrayList<>();
        double total = (double) (first + second);
        double emptySpace = (total == first || total == second) ? 0 : total * 0.01;
        values.add(new double[]{first, first > 0 ? emptySpace : 0,
                second, second > 0 ? emptySpace : 0});
        values.add(new double[]{0, 0, 0, 0});
        values.add(new double[]{0, 0, 0, 0});

        List<String[]> titles = new ArrayList<>();
        titles.add(new String[]{String.valueOf(first), "", String.valueOf(second), ""});
        titles.add(new String[]{"", "", "", ""});
        titles.add(new String[]{"", "", "", ""});

        mMultipleCategorySeriesDataSet2 = new MultipleCategorySeries("");
        mMultipleCategorySeriesDataSet2.clear();
        for (int i = 0; i < values.size(); i++)
            mMultipleCategorySeriesDataSet2.add(titles.get(i), values.get(i));

        mDoughnutRender2 = new DefaultRenderer();
        for (int color : COLORS) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            mDoughnutRender2.addSeriesRenderer(r);
        }

        mDoughnutRender2.setZoomEnabled(false);
        mDoughnutRender2.setZoomButtonsVisible(false);
        mDoughnutRender2.setPanEnabled(false);
        mDoughnutRender2.setShowLegend(false);
        mDoughnutRender2.setClickEnabled(false);
        mDoughnutRender2.setScale((float) 1.2);
        mDoughnutRender2.setShowLabels(false);
        mDoughnutRender2.setDisplayValues(true);
        mDoughnutRender2.setStartAngle(225);
        mDoughnutRender2.setBackgroundColor(Color.TRANSPARENT);

        GraphicalView mDoughnutChartView2 = ChartFactory.getDoughnutChartView(this, mMultipleCategorySeriesDataSet2, mDoughnutRender2);

        doughnutChartLinearLayout2.removeAllViews();
        doughnutChartLinearLayout2.addView(mDoughnutChartView2);

        mDoughnutChartView2.repaint();
    }

    public void updateDonutChart3(int first, int second, int third) {
        textViewFirst3.setText("First: " + first);
        textViewSecond3.setText("Second: " + second);
        textViewThird3.setText("Third: " + third);

        COLORS = new int[]{ContextCompat.getColor(this, R.color.first),
                ContextCompat.getColor(this, android.R.color.transparent),
                ContextCompat.getColor(this, R.color.second),
                ContextCompat.getColor(this, android.R.color.transparent),
                ContextCompat.getColor(this, R.color.third),
                ContextCompat.getColor(this, android.R.color.transparent)};

        List<double[]> values = new ArrayList<>();
        double total = (double) (first + second + third);
        double emptySpace = (total == first || total == second || total == third) ? 0 : total * 0.01;
        values.add(new double[]{first, first > 0 ? emptySpace : 0,
                second, second > 0 ? emptySpace : 0,
                third, third > 0 ? emptySpace : 0});
        values.add(new double[]{0, 0, 0, 0, 0, 0});
        values.add(new double[]{0, 0, 0, 0, 0, 0});

        List<String[]> titles = new ArrayList<>();
        titles.add(new String[]{String.valueOf(first), "", String.valueOf(second), "", String.valueOf(third), ""});
        titles.add(new String[]{"", "", "", "", "", ""});
        titles.add(new String[]{"", "", "", "", "", ""});

        mMultipleCategorySeriesDataSet3 = new MultipleCategorySeries("");
        mMultipleCategorySeriesDataSet3.clear();
        for (int i = 0; i < values.size(); i++)
            mMultipleCategorySeriesDataSet3.add(titles.get(i), values.get(i));

        mDoughnutRender3 = new DefaultRenderer();
        for (int color : COLORS) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            mDoughnutRender3.addSeriesRenderer(r);
        }

        mDoughnutRender3.setZoomEnabled(false);
        mDoughnutRender3.setZoomButtonsVisible(false);
        mDoughnutRender3.setPanEnabled(false);
        mDoughnutRender3.setShowLegend(false);
        mDoughnutRender3.setClickEnabled(false);
        mDoughnutRender3.setScale((float) 1.2);
        mDoughnutRender3.setShowLabels(false);
        mDoughnutRender3.setDisplayValues(true);
        mDoughnutRender3.setStartAngle(225);
        mDoughnutRender3.setBackgroundColor(Color.TRANSPARENT);

        GraphicalView mDoughnutChartView3 = ChartFactory.getDoughnutChartView(this, mMultipleCategorySeriesDataSet3, mDoughnutRender3);

        doughnutChartLinearLayout3.removeAllViews();
        doughnutChartLinearLayout3.addView(mDoughnutChartView3);

        mDoughnutChartView3.repaint();
    }

    public void updateDonutChart4(int first, int second, int third, int fourth) {
        textViewFirst4.setText("First: " + first);
        textViewSecond4.setText("Second: " + second);
        textViewThird4.setText("Third: " + third);
        textViewFourth4.setText("Fourth: " + fourth);

        COLORS = new int[]{ContextCompat.getColor(this, R.color.first),
                ContextCompat.getColor(this, android.R.color.transparent),
                ContextCompat.getColor(this, R.color.second),
                ContextCompat.getColor(this, android.R.color.transparent),
                ContextCompat.getColor(this, R.color.third),
                ContextCompat.getColor(this, android.R.color.transparent),
                ContextCompat.getColor(this, R.color.fourth),
                ContextCompat.getColor(this, android.R.color.transparent)};

        List<double[]> values = new ArrayList<>();
        double total = (double) (first + second + third + fourth);
        double emptySpace = (total == first || total == second || total == third || total == fourth) ? 0 : total * 0.01;
        values.add(new double[]{first, first > 0 ? emptySpace : 0,
                second, second > 0 ? emptySpace : 0,
                third, third > 0 ? emptySpace : 0,
                fourth, fourth > 0 ? emptySpace : 0});
        values.add(new double[]{0, 0, 0, 0, 0, 0, 0, 0});
        values.add(new double[]{0, 0, 0, 0, 0, 0, 0, 0});

        List<String[]> titles = new ArrayList<>();
        titles.add(new String[]{String.valueOf(first), "", String.valueOf(second), "", String.valueOf(third), "", String.valueOf(fourth), ""});
        titles.add(new String[]{"", "", "", "", "", "", "", ""});
        titles.add(new String[]{"", "", "", "", "", "", "", ""});

        mMultipleCategorySeriesDataSet4 = new MultipleCategorySeries("");
        mMultipleCategorySeriesDataSet4.clear();
        for (int i = 0; i < values.size(); i++)
            mMultipleCategorySeriesDataSet4.add(titles.get(i), values.get(i));

        mDoughnutRender4 = new DefaultRenderer();
        for (int color : COLORS) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            mDoughnutRender4.addSeriesRenderer(r);
        }

        mDoughnutRender4.setZoomEnabled(false);
        mDoughnutRender4.setZoomButtonsVisible(false);
        mDoughnutRender4.setPanEnabled(false);
        mDoughnutRender4.setShowLegend(false);
        mDoughnutRender4.setClickEnabled(false);
        mDoughnutRender4.setScale((float) 1.2);
        mDoughnutRender4.setShowLabels(false);
        mDoughnutRender4.setDisplayValues(true);
        mDoughnutRender4.setStartAngle(225);
        mDoughnutRender4.setBackgroundColor(Color.TRANSPARENT);

        GraphicalView mDoughnutChartView4 = ChartFactory.getDoughnutChartView(this, mMultipleCategorySeriesDataSet4, mDoughnutRender4);

        doughnutChartLinearLayout4.removeAllViews();
        doughnutChartLinearLayout4.addView(mDoughnutChartView4);

        mDoughnutChartView4.repaint();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int REQUEST_CROP_PICTURE = 0;
        if (requestCode == REQUEST_PICTURE_PICKER) {
            if (resultCode == RESULT_OK) {
                File croppedImageFile = new File(getFilesDir(), "image_" + mPlayerName + "_" + mDeckName + ".png");
                Uri croppedImageUri = Uri.fromFile(croppedImageFile);

                ViewGroup.LayoutParams layoutParams = imageViewBanner.getLayoutParams();
                int auxWidth = imageViewBanner.getWidth();
                int auxHeight = (int) (layoutParams.width * (float) 9 / 16);

                CropImageIntentBuilder cropImage = new CropImageIntentBuilder(16, 9, auxWidth, auxHeight, croppedImageUri);
                cropImage.setSourceImage(data.getData());

                startActivityForResult(cropImage.getIntent(this), REQUEST_CROP_PICTURE);
            }
        }
        if (requestCode == REQUEST_CROP_PICTURE) {
            if (resultCode == RESULT_OK) {
                File croppedImageFile = new File(getFilesDir(), "image_" + mPlayerName + "_" + mDeckName + ".png");
                imageViewBanner.setImageBitmap(BitmapFactory.decodeFile(croppedImageFile.getAbsolutePath()));
                imageViewBanner.setAdjustViewBounds(true);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        }

        assert getSupportActionBar() != null;
        final Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDeckDrawer = (LinearLayout) findViewById(R.id.deck_drawer);
        mDeckDrawerLayout = (DrawerLayout) findViewById(R.id.deck_drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDeckDrawerLayout, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDeckDrawerLayout.setDrawerListener(mDrawerToggle);

        Intent intent = getIntent();
        mPlayerName = intent.getStringExtra("PLAYERNAME");
        mDeckName = intent.getStringExtra("DECKNAME");
        mDeckIdentity = intent.getStringExtra("DECKIDENTITY");

        decksDB = new DecksDataAccessObject(this);
        recordsDB = new RecordsDataAccessObject(this);
        decksDB.open();
        recordsDB.open();

        mCurrentDeck = decksDB.getDeck(mPlayerName, mDeckName);

        createLayout(this.findViewById(android.R.id.content));

        //TODO selected item on drawer - temp local
        LinearLayout drawerItemPlayers = (LinearLayout) findViewById(R.id.drawerItemPlayers);
        ImageView drawerItemIconPlayers = (ImageView) findViewById(R.id.drawerItemIconPlayers);
        TextView drawerItemTextPlayers = (TextView) findViewById(R.id.drawerItemTextPlayers);
        drawerItemPlayers.setBackgroundColor(ContextCompat.getColor(this, R.color.gray200));
        drawerItemIconPlayers.setColorFilter(ContextCompat.getColor(this, R.color.accent_color));
        drawerItemTextPlayers.setTextColor(ContextCompat.getColor(this, R.color.accent_color));
//        switchScreen.getThumbDrawable().setColorFilter(ContextCompat.getColor(this, R.color.accent_color), PorterDuff.Mode.MULTIPLY);
//        switchScreen.getTrackDrawable().setColorFilter(ContextCompat.getColor(this, R.color.accent_secondary_color), PorterDuff.Mode.MULTIPLY);
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

    private void createEditDeckDialog() {
        View dialogDeckView = LayoutInflater.from(DeckActivity.this).inflate(R.layout.dialog_deck, null);
        final EditText userInput = (EditText) dialogDeckView.findViewById(R.id.editTextDeckName);
        checkBoxManaWhite = (CheckBox) dialogDeckView.findViewById(R.id.checkbox_mana_white);
        checkBoxManaBlue = (CheckBox) dialogDeckView.findViewById(R.id.checkbox_mana_blue);
        checkBoxManaBlack = (CheckBox) dialogDeckView.findViewById(R.id.checkbox_mana_black);
        checkBoxManaRed = (CheckBox) dialogDeckView.findViewById(R.id.checkbox_mana_red);
        checkBoxManaGreen = (CheckBox) dialogDeckView.findViewById(R.id.checkbox_mana_green);
        checkBoxManaColorless = (CheckBox) dialogDeckView.findViewById(R.id.checkbox_mana_colorless);

        userInput.setText(mDeckName);
        final String currentColorIdentity = mCurrentDeck.getDeckIdentity();
        checkBoxManaWhite.setChecked(currentColorIdentity.length() >= 0 && currentColorIdentity.charAt(0) == '1');
        checkBoxManaBlue.setChecked(currentColorIdentity.length() >= 1 && currentColorIdentity.charAt(1) == '1');
        checkBoxManaBlack.setChecked(currentColorIdentity.length() >= 2 && currentColorIdentity.charAt(2) == '1');
        checkBoxManaRed.setChecked(currentColorIdentity.length() >= 3 && currentColorIdentity.charAt(3) == '1');
        checkBoxManaGreen.setChecked(currentColorIdentity.length() >= 4 && currentColorIdentity.charAt(4) == '1');
        checkBoxManaColorless.setChecked(currentColorIdentity.length() >= 5 && currentColorIdentity.charAt(5) == '1');

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DeckActivity.this);
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
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
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

                    if (!colorIdentity.equalsIgnoreCase(currentColorIdentity)) {
                        decksDB.updateDeckIdentity(mCurrentDeck, colorIdentity);
                        mDeckIdentity = colorIdentity;
                        mCurrentDeck = decksDB.getDeck(mPlayerName, mDeckName);
                        updateLayout();
                    }

                    if (!newDeckName.equalsIgnoreCase(mDeckName)) {
                        if (decksDB.updateDeckName(mCurrentDeck, newDeckName) != -1) {
                            recordsDB.updateDeckNameRecord(mCurrentDeck, newDeckName);

                            File oldFile = new File(getFilesDir(), "image_" + mPlayerName + "_" + mDeckName + ".png");
                            File newFile = new File(getFilesDir(), "image_" + mPlayerName + "_" + newDeckName + ".png");
                            if (oldFile.renameTo(newFile))
                                Toast.makeText(DeckActivity.this, "Successfully edited ", Toast.LENGTH_SHORT).show();

                            mDeckName = newDeckName;
                            mCurrentDeck = decksDB.getDeck(mPlayerName, mDeckName);

                            updateLayout();
                        } else {
                            Toast.makeText(DeckActivity.this, "Fail: Deck " + newDeckName + " already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(DeckActivity.this, "Insert a name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createLayout(View view) {
        if (view != null) {

            mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
            mCollapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.white));
            mCollapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white));

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
            fabMain = (FloatingActionButton) findViewById(R.id.fabMain);
            fabEdit = (FloatingActionButton) findViewById(R.id.fabEdit);
            fabDelete = (FloatingActionButton) findViewById(R.id.fabDelete);

            //Deck banner image
            imageViewBanner = (ImageView) findViewById(R.id.imageViewBanner);

            //Card Deck info
            cardViewDeckInfo = (CardView) view.findViewById(R.id.cardViewDeckInfo);
            relativeTitleDeckInfo = (RelativeLayout) view.findViewById(R.id.relativeTitleDeckInfo);
            textTitleDeckInfo = (TextView) view.findViewById(R.id.textTitleDeckInfo);
            iconIndicatorDeckInfo = (ImageView) view.findViewById(R.id.iconIndicatorDeckInfo);
            //Deck name
            textViewCommander = (TextView) view.findViewById(R.id.textViewCommander);
            //Deck Owner
            textViewOwnerName = (TextView) view.findViewById(R.id.textViewOwnerName);
            //Creation Date
            textViewCreationDate = (TextView) view.findViewById(R.id.textViewCreationDate);
            //Shield color
            imageViewShieldColor = (ImageView) view.findViewById(R.id.imageViewShieldColor);
            //Total games
            textViewTotalGames = (TextView) view.findViewById(R.id.textViewTotalGames);
            //Wins
            textViewWins = (TextView) view.findViewById(R.id.textViewWins);
            //Deck identity
            listIdentityHolder = new ArrayList<>();
            listIdentityHolder.add((ImageView) view.findViewById(R.id.imageViewMana6));
            listIdentityHolder.add((ImageView) view.findViewById(R.id.imageViewMana5));
            listIdentityHolder.add((ImageView) view.findViewById(R.id.imageViewMana4));
            listIdentityHolder.add((ImageView) view.findViewById(R.id.imageViewMana3));
            listIdentityHolder.add((ImageView) view.findViewById(R.id.imageViewMana2));
            listIdentityHolder.add((ImageView) view.findViewById(R.id.imageViewMana1));

            //Chart 1v1
            cardViewDeckHistory2 = (CardView) findViewById(R.id.cardViewDeckHistory2);
            relativeTitleDeckHistory2 = (RelativeLayout) findViewById(R.id.relativeTitleDeckHistory2);
            textTitleDeckHistory2 = (TextView) findViewById(R.id.textTitleDeckHistory2);
            iconIndicatorDeckHistory2 = (ImageView) findViewById(R.id.iconIndicatorDeckHistory2);
            doughnutChartLinearLayout2 = (LinearLayout) findViewById(R.id.chart2);
            textViewTotalGameNumber2 = (TextView) view.findViewById(R.id.textViewTotalGameNumber2);
            textViewTotalGameText2 = (TextView) view.findViewById(R.id.textViewTotalGameText2);
            textViewFirst2 = (TextView) view.findViewById(R.id.textViewFirst2);
            textViewSecond2 = (TextView) view.findViewById(R.id.textViewSecond2);

            //Chart 1v1v1
            cardViewDeckHistory3 = (CardView) findViewById(R.id.cardViewDeckHistory3);
            relativeTitleDeckHistory3 = (RelativeLayout) findViewById(R.id.relativeTitleDeckHistory3);
            textTitleDeckHistory3 = (TextView) findViewById(R.id.textTitleDeckHistory3);
            iconIndicatorDeckHistory3 = (ImageView) findViewById(R.id.iconIndicatorDeckHistory3);
            doughnutChartLinearLayout3 = (LinearLayout) findViewById(R.id.chart3);
            textViewTotalGameNumber3 = (TextView) view.findViewById(R.id.textViewTotalGameNumber3);
            textViewTotalGameText3 = (TextView) view.findViewById(R.id.textViewTotalGameText3);
            textViewFirst3 = (TextView) view.findViewById(R.id.textViewFirst3);
            textViewSecond3 = (TextView) view.findViewById(R.id.textViewSecond3);
            textViewThird3 = (TextView) view.findViewById(R.id.textViewThird3);

            //Chart 1v1v1v1
            cardViewDeckHistory4 = (CardView) findViewById(R.id.cardViewDeckHistory4);
            relativeTitleDeckHistory4 = (RelativeLayout) findViewById(R.id.relativeTitleDeckHistory4);
            textTitleDeckHistory4 = (TextView) findViewById(R.id.textTitleDeckHistory4);
            iconIndicatorDeckHistory4 = (ImageView) findViewById(R.id.iconIndicatorDeckHistory4);
            doughnutChartLinearLayout4 = (LinearLayout) findViewById(R.id.chart4);
            textViewTotalGameNumber4 = (TextView) view.findViewById(R.id.textViewTotalGameNumber4);
            textViewTotalGameText4 = (TextView) view.findViewById(R.id.textViewTotalGameText4);
            textViewFirst4 = (TextView) view.findViewById(R.id.textViewFirst4);
            textViewSecond4 = (TextView) view.findViewById(R.id.textViewSecond4);
            textViewThird4 = (TextView) view.findViewById(R.id.textViewThird4);
            textViewFourth4 = (TextView) view.findViewById(R.id.textViewFourth4);

            //Chart lastGamePlayed
            cardViewLastGamePlayed = (CardView) findViewById(R.id.cardViewLastGamePlayed);
            relativeTitleLastGamePlayed = (RelativeLayout) findViewById(R.id.relativeTitleLastGamePlayed);
            iconIndicatorLastGamePlayed = (ImageView) findViewById(R.id.iconIndicatorLastGamePlayed);
            textTitleLastGamePlayed = (TextView) findViewById(R.id.textTitleLastGamePlayed);
            textViewRecordDate = (TextView) findViewById(R.id.textViewRecordDate);

            linearLastGame1 = (LinearLayout) findViewById(R.id.linearLastGame1);
            textViewPlayer1 = (TextView) view.findViewById(R.id.textViewPlayer1);
            textViewDeck1 = (TextView) view.findViewById(R.id.textViewDeck1);

            linearLastGame2 = (LinearLayout) findViewById(R.id.linearLastGame2);
            textViewPlayer2 = (TextView) view.findViewById(R.id.textViewPlayer2);
            textViewDeck2 = (TextView) view.findViewById(R.id.textViewDeck2);

            linearLastGame3 = (LinearLayout) findViewById(R.id.linearLastGame3);
            textViewPlayer3 = (TextView) view.findViewById(R.id.textViewPlayer3);
            textViewDeck3 = (TextView) view.findViewById(R.id.textViewDeck3);

            linearLastGame4 = (LinearLayout) findViewById(R.id.linearLastGame4);
            textViewPlayer4 = (TextView) view.findViewById(R.id.textViewPlayer4);
            textViewDeck4 = (TextView) view.findViewById(R.id.textViewDeck4);
        }
    }

    private void createRemoveDeckDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DeckActivity.this);
        alertDialogBuilder.setTitle("Delete deck");
        alertDialogBuilder.setMessage("Are you sure to delete this deck?");
        alertDialogBuilder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        long result = decksDB.removeDeck(mCurrentDeck);
                        if (result != 0) {
                            dialog.cancel();
                            //TODO confirm flux
                            DeckActivity.this.finish();
                        } else {
                            Toast.makeText(DeckActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        alertDialogBuilder.setNeutralButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void makeViewVisible(View view) {
        int viewTop = view.getTop();
        int viewBottom = view.getBottom();

        for (; ; ) {
            ViewParent viewParent = view.getParent();
            if (viewParent == null || !(viewParent instanceof ViewGroup))
                break;

            ViewGroup viewGroupParent = (ViewGroup) viewParent;
            if (viewGroupParent instanceof NestedScrollView) {

                NestedScrollView nestedScrollView = (NestedScrollView) viewGroupParent;
                int height = nestedScrollView.getHeight();
                int screenTop = nestedScrollView.getScrollY();
                int screenBottom = screenTop + height;
                int fadingEdge = nestedScrollView.getVerticalFadingEdgeLength();

                // leave room for top fading edge as long as rect isn't at very top
                if (viewTop > 0)
                    screenTop += fadingEdge;

                // leave room for bottom fading edge as long as rect isn't at very bottom
                if (viewBottom < nestedScrollView.getChildAt(0).getHeight())
                    screenBottom -= fadingEdge;

                int scrollYDelta = 0;

                if (viewBottom > screenBottom && viewTop > screenTop) {
                    // need to move down to get it in view: move down just enough so
                    // that the entire rectangle is in view (or at least the first
                    // screen size chunk).

                    if (viewBottom - viewTop > height) // just enough to get screen size chunk on
                        scrollYDelta += (viewTop - screenTop);
                    else              // get entire rect at bottom of screen
                        scrollYDelta += (viewBottom - screenBottom);

                    // make sure we aren't scrolling beyond the end of our content
                    int bottom = nestedScrollView.getChildAt(0).getBottom();
                    int distanceToBottom = bottom - screenBottom;
                    scrollYDelta = Math.min(scrollYDelta, distanceToBottom);

                } else if (viewTop < screenTop && viewBottom < screenBottom) {
                    // need to move up to get it in view: move up just enough so that
                    // entire rectangle is in view (or at least the first screen
                    // size chunk of it).

                    if (viewBottom - viewTop > height)    // screen size chunk
                        scrollYDelta -= (screenBottom - viewBottom);
                    else                  // entire rect at top
                        scrollYDelta -= (screenTop - viewTop);

                    // make sure we aren't scrolling any further than the top our content
                    scrollYDelta = Math.max(scrollYDelta, -nestedScrollView.getScrollY());
                }
                nestedScrollView.smoothScrollBy(0, scrollYDelta);
                break;
            }
            // Transform coordinates to parent:
            int dy = viewGroupParent.getTop() - viewGroupParent.getScrollY();
            viewTop += dy;
            viewBottom += dy;

            view = viewGroupParent;
        }
    }

    private void toggleCardExpansion(final CardView card, TextView title, ImageView selector, int minHeight, int maxHeight) {
        // expand
        if (card.getHeight() == minHeight) {
            title.setTextColor(ContextCompat.getColor(this, R.color.secondary_color));

            Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate_180_anticlockwise);
            selector.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.arrow_up));
            selector.setRotation(0);
            selector.startAnimation(rotation);
            selector.setColorFilter(ContextCompat.getColor(this, R.color.secondary_color));

            ValueAnimator anim = ValueAnimator.ofInt(card.getMeasuredHeightAndState(), maxHeight);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = card.getLayoutParams();
                    layoutParams.height = val;
                    card.setLayoutParams(layoutParams);
                    makeViewVisible(card);
                }
            });
            anim.start();
        } else {
            // collapse
            title.setTextColor(ContextCompat.getColor(this, R.color.secondary_text));

            Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate_180_clockwise);
            selector.startAnimation(rotation);
            selector.setColorFilter(ContextCompat.getColor(this, R.color.secondary_text));

            ValueAnimator anim = ValueAnimator.ofInt(card.getMeasuredHeightAndState(), minHeight);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = card.getLayoutParams();
                    layoutParams.height = val;
                    card.setLayoutParams(layoutParams);
                }
            });
            anim.start();
        }
    }

    //TODO rename updateList
    private void updateLayout() {
        //ToolBar - DeckName
        mCollapsingToolbarLayout.setTitle(mDeckName);

        //ToolBar - Deck Image
        File croppedImageFile = new File(getFilesDir(), "image_" + mPlayerName + "_" + mDeckName + ".png");
        if (croppedImageFile.isFile()) {
            imageViewBanner.setImageBitmap(BitmapFactory.decodeFile(croppedImageFile.getAbsolutePath()));
            imageViewBanner.setAdjustViewBounds(true);
        } else {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            ViewGroup.LayoutParams layoutParams = imageViewBanner.getLayoutParams();
            layoutParams.height = (int) (displaymetrics.widthPixels * (float) 9 / 16);
            imageViewBanner.setLayoutParams(layoutParams);
        }

        //Deck info - Card
        if (mCardViewFullHeightDeckInfo == 0) {
            cardViewDeckInfo.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    cardViewDeckInfo.getViewTreeObserver().removeOnPreDrawListener(this);
                    mCardViewFullHeightDeckInfo = cardViewDeckInfo.getHeight();
                    return true;
                }
            });
        }
        //Deck info - title
        if (cardViewDeckInfo.getHeight() == mCardViewFullHeightDeckInfo) {
            textTitleDeckInfo.setTextColor(ContextCompat.getColor(this, R.color.secondary_color));
            iconIndicatorDeckInfo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.arrow_up));
            iconIndicatorDeckInfo.setColorFilter(ContextCompat.getColor(this, R.color.secondary_color));
        } else {
            textTitleDeckInfo.setTextColor(ContextCompat.getColor(this, R.color.secondary_text));
            iconIndicatorDeckInfo.setColorFilter(ContextCompat.getColor(this, R.color.secondary_text));
        }

        //Deck info - Deck name
        textViewCommander.setText(mDeckName);

        //Deck info - Deck owner
        textViewOwnerName.setText(mPlayerName);

        //Deck info - Creation Date
        textViewCreationDate.setText(mCurrentDeck.getDeckCreationDate());

        //Deck info - Shield color
        imageViewShieldColor.setColorFilter(decksDB.getDeck(mPlayerName, mDeckName).getDeckShieldColor()[0]);

        //Deck info - Deck Identity
        listIdentityHolder.get(0).setVisibility(View.GONE);
        listIdentityHolder.get(1).setVisibility(View.GONE);
        listIdentityHolder.get(2).setVisibility(View.GONE);
        listIdentityHolder.get(3).setVisibility(View.GONE);
        listIdentityHolder.get(4).setVisibility(View.GONE);
        listIdentityHolder.get(5).setVisibility(View.GONE);
        int index = 0;
        if (mDeckIdentity.charAt(0) == '1') {
            listIdentityHolder.get(index).setBackground(ContextCompat.getDrawable(this, R.drawable.mana_white));
            listIdentityHolder.get(index).setVisibility(View.VISIBLE);
            index++;
        }
        if (mDeckIdentity.charAt(1) == '1') {
            listIdentityHolder.get(index).setBackground(ContextCompat.getDrawable(this, R.drawable.mana_blue));
            listIdentityHolder.get(index).setVisibility(View.VISIBLE);
            index++;
        }
        if (mDeckIdentity.charAt(2) == '1') {
            listIdentityHolder.get(index).setBackground(ContextCompat.getDrawable(this, R.drawable.mana_black));
            listIdentityHolder.get(index).setVisibility(View.VISIBLE);
            index++;
        }
        if (mDeckIdentity.charAt(3) == '1') {
            listIdentityHolder.get(index).setBackground(ContextCompat.getDrawable(this, R.drawable.mana_red));
            listIdentityHolder.get(index).setVisibility(View.VISIBLE);
            index++;
        }
        if (mDeckIdentity.charAt(4) == '1') {
            listIdentityHolder.get(index).setBackground(ContextCompat.getDrawable(this, R.drawable.mana_green));
            listIdentityHolder.get(index).setVisibility(View.VISIBLE);
            index++;
        }
        if (mDeckIdentity.charAt(5) == '1') {
            listIdentityHolder.get(index).setBackground(ContextCompat.getDrawable(this, R.drawable.mana_colorless));
            listIdentityHolder.get(index).setVisibility(View.VISIBLE);
        }

        //Record card
        List<Record> allRecords = recordsDB.getAllRecordsByDeck(mCurrentDeck);
        cardViewLastGamePlayed.setVisibility(View.GONE);
        if (allRecords.size() != 0) {
            cardViewLastGamePlayed.setVisibility(View.VISIBLE);
            if (mCardViewFullHeightLastGamePlayed == 0) {
                cardViewLastGamePlayed.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        cardViewLastGamePlayed.getViewTreeObserver().removeOnPreDrawListener(this);
                        mCardViewFullHeightLastGamePlayed = cardViewLastGamePlayed.getHeight();
                        ViewGroup.LayoutParams layoutParams = cardViewLastGamePlayed.getLayoutParams();
                        layoutParams.height = relativeTitleLastGamePlayed.getHeight();
                        cardViewLastGamePlayed.setLayoutParams(layoutParams);
                        return true;
                    }
                });
            }

            Record lastRecord = allRecords.get(allRecords.size() - 1);
            textViewRecordDate.setText(lastRecord.getDate());
            switch (lastRecord.getTotalPlayers()) {
                case 2:
                    linearLastGame1.setVisibility(View.VISIBLE);
                    textViewPlayer1.setText(lastRecord.getFirstPlace().getDeckOwnerName());
                    textViewDeck1.setText(lastRecord.getFirstPlace().getDeckName());
                    textViewPlayer1.setTypeface(null, mCurrentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? Typeface.BOLD : Typeface.NORMAL);
                    textViewDeck1.setTypeface(null, mCurrentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? Typeface.BOLD : Typeface.NORMAL);

                    linearLastGame2.setVisibility(View.VISIBLE);
                    textViewPlayer2.setText(lastRecord.getSecondPlace().getDeckOwnerName());
                    textViewDeck2.setText(lastRecord.getSecondPlace().getDeckName());
                    textViewPlayer2.setTypeface(null, mCurrentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? Typeface.BOLD : Typeface.NORMAL);
                    textViewDeck2.setTypeface(null, mCurrentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? Typeface.BOLD : Typeface.NORMAL);

                    linearLastGame3.setVisibility(View.GONE);
                    linearLastGame4.setVisibility(View.GONE);
                    break;

                case 3:
                    linearLastGame1.setVisibility(View.VISIBLE);
                    textViewPlayer1.setText(lastRecord.getFirstPlace().getDeckOwnerName());
                    textViewDeck1.setText(lastRecord.getFirstPlace().getDeckName());
                    textViewPlayer1.setTypeface(null, mCurrentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? Typeface.BOLD : Typeface.NORMAL);
                    textViewDeck1.setTypeface(null, mCurrentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? Typeface.BOLD : Typeface.NORMAL);

                    linearLastGame2.setVisibility(View.VISIBLE);
                    textViewPlayer2.setText(lastRecord.getSecondPlace().getDeckOwnerName());
                    textViewDeck2.setText(lastRecord.getSecondPlace().getDeckName());
                    textViewPlayer2.setTypeface(null, mCurrentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? Typeface.BOLD : Typeface.NORMAL);
                    textViewDeck2.setTypeface(null, mCurrentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? Typeface.BOLD : Typeface.NORMAL);

                    linearLastGame3.setVisibility(View.VISIBLE);
                    textViewPlayer3.setText(lastRecord.getThirdPlace().getDeckOwnerName());
                    textViewDeck3.setText(lastRecord.getThirdPlace().getDeckName());
                    textViewPlayer3.setTypeface(null, mCurrentDeck.isEqualDeck(lastRecord.getThirdPlace()) ? Typeface.BOLD : Typeface.NORMAL);
                    textViewDeck3.setTypeface(null, mCurrentDeck.isEqualDeck(lastRecord.getThirdPlace()) ? Typeface.BOLD : Typeface.NORMAL);

                    linearLastGame4.setVisibility(View.GONE);
                    break;

                case 4:
                    linearLastGame1.setVisibility(View.VISIBLE);
                    textViewPlayer1.setText(lastRecord.getFirstPlace().getDeckOwnerName());
                    textViewDeck1.setText(lastRecord.getFirstPlace().getDeckName());
                    textViewPlayer1.setTypeface(null, mCurrentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? Typeface.BOLD : Typeface.NORMAL);
                    textViewDeck1.setTypeface(null, mCurrentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? Typeface.BOLD : Typeface.NORMAL);

                    linearLastGame2.setVisibility(View.VISIBLE);
                    textViewPlayer2.setText(lastRecord.getSecondPlace().getDeckOwnerName());
                    textViewDeck2.setText(lastRecord.getSecondPlace().getDeckName());
                    textViewPlayer2.setTypeface(null, mCurrentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? Typeface.BOLD : Typeface.NORMAL);
                    textViewDeck2.setTypeface(null, mCurrentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? Typeface.BOLD : Typeface.NORMAL);

                    linearLastGame3.setVisibility(View.VISIBLE);
                    textViewPlayer3.setText(lastRecord.getThirdPlace().getDeckOwnerName());
                    textViewDeck3.setText(lastRecord.getThirdPlace().getDeckName());
                    textViewPlayer3.setTypeface(null, mCurrentDeck.isEqualDeck(lastRecord.getThirdPlace()) ? Typeface.BOLD : Typeface.NORMAL);
                    textViewDeck3.setTypeface(null, mCurrentDeck.isEqualDeck(lastRecord.getThirdPlace()) ? Typeface.BOLD : Typeface.NORMAL);

                    linearLastGame4.setVisibility(View.VISIBLE);
                    textViewPlayer4.setText(lastRecord.getFourthPlace().getDeckOwnerName());
                    textViewDeck4.setText(lastRecord.getFourthPlace().getDeckName());
                    textViewPlayer4.setTypeface(null, mCurrentDeck.isEqualDeck(lastRecord.getFourthPlace()) ? Typeface.BOLD : Typeface.NORMAL);
                    textViewDeck4.setTypeface(null, mCurrentDeck.isEqualDeck(lastRecord.getFourthPlace()) ? Typeface.BOLD : Typeface.NORMAL);
                    break;
            }
        }

        //Total games and Chart2 1v1
        int firstIn2 = recordsDB.getRecordsByPosition(mCurrentDeck, 1, 2).size();
        int secondIn2 = recordsDB.getRecordsByPosition(mCurrentDeck, 2, 2).size();
        int total2 = firstIn2 + secondIn2;
        cardViewDeckHistory2.setVisibility(View.GONE);
        if (total2 != 0) {
            cardViewDeckHistory2.setVisibility(View.VISIBLE);
            if (mCardViewFullHeightDeckHistory2 == 0) {
                cardViewDeckHistory2.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        cardViewDeckHistory2.getViewTreeObserver().removeOnPreDrawListener(this);
                        mCardViewFullHeightDeckHistory2 = cardViewDeckHistory2.getHeight();
                        ViewGroup.LayoutParams layoutParams = cardViewDeckHistory2.getLayoutParams();
                        layoutParams.height = relativeTitleDeckHistory2.getHeight();
                        cardViewDeckHistory2.setLayoutParams(layoutParams);
                        return true;
                    }
                });
            }
            textViewTotalGameNumber2.setText("" + total2);
            textViewTotalGameText2.setText(total2 == 1 ? "game played" : "games played");
            updateDonutChart2(firstIn2, secondIn2);
        }

        //Total games and Chart3 1v1v1
        int firstIn3 = recordsDB.getRecordsByPosition(mCurrentDeck, 1, 3).size();
        int secondIn3 = recordsDB.getRecordsByPosition(mCurrentDeck, 2, 3).size();
        int thirdIn3 = recordsDB.getRecordsByPosition(mCurrentDeck, 3, 3).size();
        int total3 = firstIn3 + secondIn3 + thirdIn3;
        cardViewDeckHistory3.setVisibility(View.GONE);
        if (total3 != 0) {
            cardViewDeckHistory3.setVisibility(View.VISIBLE);
            if (mCardViewFullHeightDeckHistory3 == 0) {
                cardViewDeckHistory3.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        cardViewDeckHistory3.getViewTreeObserver().removeOnPreDrawListener(this);
                        mCardViewFullHeightDeckHistory3 = cardViewDeckHistory3.getHeight();
                        ViewGroup.LayoutParams layoutParams = cardViewDeckHistory3.getLayoutParams();
                        layoutParams.height = relativeTitleDeckHistory3.getHeight();
                        cardViewDeckHistory3.setLayoutParams(layoutParams);
                        return true;
                    }
                });
            }
            textViewTotalGameNumber3.setText("" + total3);
            textViewTotalGameText3.setText(total3 == 1 ? "game played" : "games played");
            updateDonutChart3(firstIn3, secondIn3, thirdIn3);
        }

        //Total games and Chart4 1v1v1v1
        int firstIn4 = recordsDB.getRecordsByPosition(mCurrentDeck, 1, 4).size();
        int secondIn4 = recordsDB.getRecordsByPosition(mCurrentDeck, 2, 4).size();
        int thirdIn4 = recordsDB.getRecordsByPosition(mCurrentDeck, 3, 4).size();
        int fourthIn4 = recordsDB.getRecordsByPosition(mCurrentDeck, 4, 4).size();
        int total4 = firstIn4 + secondIn4 + thirdIn4 + fourthIn4;
        cardViewDeckHistory4.setVisibility(View.GONE);
        if (total4 != 0) {
            cardViewDeckHistory4.setVisibility(View.VISIBLE);
            if (mCardViewFullHeightDeckHistory4 == 0) {
                cardViewDeckHistory4.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        cardViewDeckHistory4.getViewTreeObserver().removeOnPreDrawListener(this);
                        mCardViewFullHeightDeckHistory4 = cardViewDeckHistory4.getHeight();
                        ViewGroup.LayoutParams layoutParams = cardViewDeckHistory4.getLayoutParams();
                        layoutParams.height = relativeTitleDeckHistory4.getHeight();
                        cardViewDeckHistory4.setLayoutParams(layoutParams);

                        return true;
                    }
                });
            }
            textViewTotalGameNumber4.setText("" + total4);
            textViewTotalGameText4.setText(total4 == 1 ? "game played" : "games played");
            updateDonutChart4(firstIn4, secondIn4, thirdIn4, fourthIn4);
        }

        //Deck info - Total Games
        textViewTotalGames.setText("" + (total2 + total3 + total4));
        //Deck info - Wins
        textViewWins.setText("" + (firstIn2 + firstIn3 + firstIn4));
    }
}
