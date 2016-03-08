package com.android.argb.edhlc.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.argb.edhlc.Constants;
import com.android.argb.edhlc.R;
import com.android.argb.edhlc.Utils;
import com.android.argb.edhlc.chart.DonutChart;
import com.android.argb.edhlc.colorpicker.ColorPickerDialog;
import com.android.argb.edhlc.colorpicker.ColorPickerSwatch;
import com.android.argb.edhlc.database.deck.DecksDataAccessObject;
import com.android.argb.edhlc.database.record.RecordsDataAccessObject;
import com.android.argb.edhlc.objects.Deck;
import com.android.argb.edhlc.objects.Record;
import com.android.camera.CropImageIntentBuilder;

import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.renderer.DefaultRenderer;

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
    private LinearLayout fabContentEdit;
    private FloatingActionButton fabDelete;
    private LinearLayout fabContentDelete;
    private View fabDismissView;

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
        if (isFabOpen) {
            Animation fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
            Animation rotate45Anticlockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_45_anticlockwise);

            fabDismissView.setClickable(false);
            fabDismissView.setVisibility(View.GONE);

            fabMain.startAnimation(rotate45Anticlockwise);
            fabMain.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.accent_color)));

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
        if (mDeckDrawerLayout.isDrawerOpen(mDeckDrawer))
            mDeckDrawerLayout.closeDrawers();
        else {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    public void onClickCardExpansion(View v) {
        switch (v.getId()) {

            case R.id.relativeTitleDeckInfo:
                Utils.toggleCardExpansion(this, cardViewDeckInfo, textTitleDeckInfo, iconIndicatorDeckInfo, relativeTitleDeckInfo.getHeight(), mCardViewFullHeightDeckInfo);
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

    public void onClickDrawerItem(View view) {
        switch (view.getId()) {
            case R.id.drawerItemHome:
                mDeckDrawerLayout.closeDrawers();
                Intent intentHome = new Intent(this, MainActivity.class);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
                this.finish();
                break;

            case R.id.drawerItemPlayers:
                mDeckDrawerLayout.closeDrawers();
                Intent intentPlayerList = new Intent(this, PlayerListActivity.class);
                intentPlayerList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentPlayerList);
                this.finish();
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
                animateFAB();
                break;
            case R.id.fabDelete:
                createRemoveDeckDialog();
                animateFAB();
                break;
        }
    }

    public void onClickImageBanner(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DeckActivity.this);
        alertDialogBuilder.setTitle("Pick image");
//        alertDialogBuilder.setMessage("");
        alertDialogBuilder.setPositiveButton("PICK IMAGE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivityForResult(getPickImageIntent(), REQUEST_PICTURE_PICKER);
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
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black_transparent));
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
        mPlayerName = intent.getStringExtra("PLAYER_NAME");
        mDeckName = intent.getStringExtra("DECK_NAME");
        mDeckIdentity = intent.getStringExtra("DECK_IDENTITY");

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

        userInput.setText(mDeckName);
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

                    if (!colorIdentity.equalsIgnoreCase(currentColorIdentity)) {
                        decksDB.updateDeckIdentity(mCurrentDeck, colorIdentity);
                        mDeckIdentity = colorIdentity;
                        mCurrentDeck = decksDB.getDeck(mPlayerName, mDeckName);
                        updateLayout();
                    }

                    if (editShieldColor[0] != mCurrentDeck.getDeckShieldColor()[0] && editShieldColor[1] != mCurrentDeck.getDeckShieldColor()[1]) {
                        decksDB.updateDeckShieldColor(mCurrentDeck, editShieldColor);
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
            fabMain = (FloatingActionButton) findViewById(R.id.fabMain);
            fabContentEdit = (LinearLayout) findViewById(R.id.fabContentEdit);
            fabEdit = (FloatingActionButton) findViewById(R.id.fabEdit);
            fabContentDelete = (LinearLayout) findViewById(R.id.fabContentDelete);
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
            textFirstPlayerRecordCard = (TextView) view.findViewById(R.id.textFirstPlayerRecordCard);
            textFirstDeckRecordCard = (TextView) view.findViewById(R.id.textFirstDeckRecordCard);
            divider1RecordCard = findViewById(R.id.divider1RecordCard);

            linearSecondLineRecordCard = (LinearLayout) findViewById(R.id.linearSecondLineRecordCard);
            textSecondIndicatorRecordCard = (TextView) findViewById(R.id.textSecondIndicatorRecordCard);
            textSecondPlayerRecordCard = (TextView) view.findViewById(R.id.textSecondPlayerRecordCard);
            textSecondDeckRecordCard = (TextView) view.findViewById(R.id.textSecondDeckRecordCard);
            divider2RecordCard = findViewById(R.id.divider2RecordCard);

            linearThirdLineRecordCard = (LinearLayout) findViewById(R.id.linearThirdLineRecordCard);
            textThirdIndicatorRecordCard = (TextView) findViewById(R.id.textThirdIndicatorRecordCard);
            textThirdPlayerRecordCard = (TextView) view.findViewById(R.id.textThirdPlayerRecordCard);
            textThirdDeckRecordCard = (TextView) view.findViewById(R.id.textThirdDeckRecordCard);
            divider3RecordCard = findViewById(R.id.divider3RecordCard);

            linearFourthLineRecordCard = (LinearLayout) findViewById(R.id.linearFourthLineRecordCard);
            textFourthIndicatorRecordCard = (TextView) findViewById(R.id.textFourthIndicatorRecordCard);
            textFourthPlayerRecordCard = (TextView) view.findViewById(R.id.textFourthPlayerRecordCard);
            textFourthDeckRecordCard = (TextView) view.findViewById(R.id.textFourthDeckRecordCard);

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
                            DeckActivity.this.finish();
                        } else {
                            Toast.makeText(DeckActivity.this, "Fail", Toast.LENGTH_SHORT).show();
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

    private void updateLayout() {
        List<Record> allRecords = recordsDB.getAllRecordsByDeck(mCurrentDeck);
        List<Record> allFirstRecords = recordsDB.getRecordsByPosition(mPlayerName, 1);

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
        imageViewShieldColor.setColorFilter(mCurrentDeck.getDeckShieldColor()[0]);

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


        //Deck info - Total Games
        textViewTotalGames.setText("" + allRecords.size());
        //Deck info - Wins
        textViewWins.setText("" + allFirstRecords.size());


        //Record card
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
                    textFirstDeckRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? colorSelected : colorPrimary);
                    textFirstDeckRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? typefaceMedium : typefaceNormal);
                    textFirstPlayerRecordCard.setText(lastRecord.getFirstPlace().getDeckOwnerName());
                    textFirstPlayerRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? colorSelected : colorSecondary);
                    textFirstPlayerRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? typefaceMedium : typefaceNormal);
                    textFirstIndicatorRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? colorSelected : colorSecondary);
                    textFirstIndicatorRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? typefaceMedium : typefaceNormal);

                    textSecondDeckRecordCard.setText(lastRecord.getSecondPlace().getDeckName());
                    textSecondDeckRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? colorSelected : colorPrimary);
                    textSecondDeckRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? typefaceMedium : typefaceNormal);
                    textSecondPlayerRecordCard.setText(lastRecord.getSecondPlace().getDeckOwnerName());
                    textSecondPlayerRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? colorSelected : colorSecondary);
                    textSecondPlayerRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? typefaceMedium : typefaceNormal);
                    textSecondIndicatorRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? colorSelected : colorSecondary);
                    textSecondIndicatorRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? typefaceMedium : typefaceNormal);

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
                    textFirstDeckRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? colorSelected : colorPrimary);
                    textFirstDeckRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? typefaceMedium : typefaceNormal);
                    textFirstPlayerRecordCard.setText(lastRecord.getFirstPlace().getDeckOwnerName());
                    textFirstPlayerRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? colorSelected : colorSecondary);
                    textFirstPlayerRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? typefaceMedium : typefaceNormal);
                    textFirstIndicatorRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? colorSelected : colorSecondary);
                    textFirstIndicatorRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? typefaceMedium : typefaceNormal);

                    textSecondDeckRecordCard.setText(lastRecord.getSecondPlace().getDeckName());
                    textSecondDeckRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? colorSelected : colorPrimary);
                    textSecondDeckRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? typefaceMedium : typefaceNormal);
                    textSecondPlayerRecordCard.setText(lastRecord.getSecondPlace().getDeckOwnerName());
                    textSecondPlayerRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? colorSelected : colorSecondary);
                    textSecondPlayerRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? typefaceMedium : typefaceNormal);
                    textSecondIndicatorRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? colorSelected : colorSecondary);
                    textSecondIndicatorRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? typefaceMedium : typefaceNormal);

                    textThirdDeckRecordCard.setText(lastRecord.getThirdPlace().getDeckName());
                    textThirdDeckRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getThirdPlace()) ? colorSelected : colorPrimary);
                    textThirdDeckRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getThirdPlace()) ? typefaceMedium : typefaceNormal);
                    textThirdPlayerRecordCard.setText(lastRecord.getThirdPlace().getDeckOwnerName());
                    textThirdPlayerRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getThirdPlace()) ? colorSelected : colorSecondary);
                    textThirdPlayerRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getThirdPlace()) ? typefaceMedium : typefaceNormal);
                    textThirdIndicatorRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getThirdPlace()) ? colorSelected : colorSecondary);
                    textThirdIndicatorRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getThirdPlace()) ? typefaceMedium : typefaceNormal);
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
                    textFirstDeckRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? colorSelected : colorPrimary);
                    textFirstDeckRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? typefaceMedium : typefaceNormal);
                    textFirstPlayerRecordCard.setText(lastRecord.getFirstPlace().getDeckOwnerName());
                    textFirstPlayerRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? colorSelected : colorSecondary);
                    textFirstPlayerRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? typefaceMedium : typefaceNormal);
                    textFirstIndicatorRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? colorSelected : colorSecondary);
                    textFirstIndicatorRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? typefaceMedium : typefaceNormal);

                    textSecondDeckRecordCard.setText(lastRecord.getSecondPlace().getDeckName());
                    textSecondDeckRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? colorSelected : colorPrimary);
                    textSecondDeckRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? typefaceMedium : typefaceNormal);
                    textSecondPlayerRecordCard.setText(lastRecord.getSecondPlace().getDeckOwnerName());
                    textSecondPlayerRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? colorSelected : colorSecondary);
                    textSecondPlayerRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? typefaceMedium : typefaceNormal);
                    textSecondIndicatorRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? colorSelected : colorSecondary);
                    textSecondIndicatorRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? typefaceMedium : typefaceNormal);

                    textThirdDeckRecordCard.setText(lastRecord.getThirdPlace().getDeckName());
                    textThirdDeckRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getThirdPlace()) ? colorSelected : colorPrimary);
                    textThirdDeckRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getThirdPlace()) ? typefaceMedium : typefaceNormal);
                    textThirdPlayerRecordCard.setText(lastRecord.getThirdPlace().getDeckOwnerName());
                    textThirdPlayerRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getThirdPlace()) ? colorSelected : colorSecondary);
                    textThirdPlayerRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getThirdPlace()) ? typefaceMedium : typefaceNormal);
                    textThirdIndicatorRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getThirdPlace()) ? colorSelected : colorSecondary);
                    textThirdIndicatorRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getThirdPlace()) ? typefaceMedium : typefaceNormal);

                    textFourthDeckRecordCard.setText(lastRecord.getFourthPlace().getDeckName());
                    textFourthDeckRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getFourthPlace()) ? colorSelected : colorPrimary);
                    textFourthDeckRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getFourthPlace()) ? typefaceMedium : typefaceNormal);
                    textFourthPlayerRecordCard.setText(lastRecord.getFourthPlace().getDeckOwnerName());
                    textFourthPlayerRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getFourthPlace()) ? colorSelected : colorSecondary);
                    textFourthPlayerRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getFourthPlace()) ? typefaceMedium : typefaceNormal);
                    textFourthIndicatorRecordCard.setTextColor(mCurrentDeck.isEqualDeck(lastRecord.getFourthPlace()) ? colorSelected : colorSecondary);
                    textFourthIndicatorRecordCard.setTypeface(mCurrentDeck.isEqualDeck(lastRecord.getFourthPlace()) ? typefaceMedium : typefaceNormal);
                    break;
            }
        }

        //Total games and Chart2 1v1
        int firstIn2 = recordsDB.getRecordsByPosition(mCurrentDeck, 1, 2).size();
        int secondIn2 = recordsDB.getRecordsByPosition(mCurrentDeck, 2, 2).size();
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
        int firstIn3 = recordsDB.getRecordsByPosition(mCurrentDeck, 1, 3).size();
        int secondIn3 = recordsDB.getRecordsByPosition(mCurrentDeck, 2, 3).size();
        int thirdIn3 = recordsDB.getRecordsByPosition(mCurrentDeck, 3, 3).size();
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
        int firstIn4 = recordsDB.getRecordsByPosition(mCurrentDeck, 1, 4).size();
        int secondIn4 = recordsDB.getRecordsByPosition(mCurrentDeck, 2, 4).size();
        int thirdIn4 = recordsDB.getRecordsByPosition(mCurrentDeck, 3, 4).size();
        int fourthIn4 = recordsDB.getRecordsByPosition(mCurrentDeck, 4, 4).size();
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
