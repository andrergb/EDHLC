package com.android.argb.edhlc.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.argb.edhlc.Constants;
import com.android.argb.edhlc.MainPlaceholderFragment;
import com.android.argb.edhlc.MainSectionsPagerAdapter;
import com.android.argb.edhlc.R;
import com.android.argb.edhlc.colorpicker.ColorPickerDialog;
import com.android.argb.edhlc.colorpicker.ColorPickerSwatch;
import com.android.argb.edhlc.database.deck.DecksDataAccessObject;
import com.android.argb.edhlc.database.player.PlayersDataAccessObject;
import com.android.argb.edhlc.objects.ActivePlayer;
import com.android.argb.edhlc.objects.Deck;
import com.android.argb.edhlc.objects.Drawer.DrawerMain;
import com.android.argb.edhlc.objects.Record;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static ArrayList<Deck> mAliveDecksList;
    private static ArrayList<Deck> mDeadDecksList;
    private static ActivePlayer mActivePlayer1;
    private static ActivePlayer mActivePlayer2;
    private static ActivePlayer mActivePlayer3;
    private static ActivePlayer mActivePlayer4;
    private static ImageView mImageViewThrone;
    private static TextView mTextViewName;
    private static TextView mTextViewDeck;
    private static TextView mTextViewLife;
    private static TextView mTextViewEDH1;
    private static TextView mTextViewEDH2;
    private static TextView mTextViewEDH3;
    private static TextView mTextViewEDH4;
    private static TextView mTextViewP1Name;
    private static TextView mTextViewP2Name;
    private static TextView mTextViewP3Name;
    private static TextView mTextViewP4Name;
    private static LinearLayout mRelativeEDH1;
    private static LinearLayout mRelativeEDH2;
    private static LinearLayout mRelativeEDH3;
    private static LinearLayout mRelativeEDH4;
    private static Button mButtonLifePositive;
    private static Button mButtonLifeNegative;
    private static Button mButtonEDH1Positive;
    private static Button mButtonEDH1Negative;
    private static Button mButtonEDH2Positive;
    private static Button mButtonEDH2Negative;
    private static Button mButtonEDH3Positive;
    private static Button mButtonEDH3Negative;
    private static Button mButtonEDH4Positive;
    private static Button mButtonEDH4Negative;
    private static CheckBox mCheckBoxKeepScreenOn;
    private static Thread mThreadLife1;
    private static Thread mThreadLife2;
    private static Thread mThreadLife3;
    private static Thread mThreadLife4;
    private static MainSectionsPagerAdapter mMainSectionsPagerAdapter;
    private ViewPager mViewPager;
    private DrawerMain drawerMain;
    private int mCurrentFragment = 0;
    private BroadcastReceiver mBroadcastReceiver;
    private MainPlaceholderFragment mainPlaceholderFragment;
    private ArrayList<Deck> mActiveDecksList;
    private PlayersDataAccessObject mPlayersDB;
    private DecksDataAccessObject mDecksDB;

    public static ArrayList<Deck> getAliveDecksList() {
        mAliveDecksList = new ArrayList<>();

        Deck auxDeckPlayer1 = new Deck(mActivePlayer1.getPlayerName(), mActivePlayer1.getPlayerDeck(), mActivePlayer1.getPlayerColor());
        Deck auxDeckPlayer2 = new Deck(mActivePlayer2.getPlayerName(), mActivePlayer2.getPlayerDeck(), mActivePlayer2.getPlayerColor());
        Deck auxDeckPlayer3 = new Deck(mActivePlayer3.getPlayerName(), mActivePlayer3.getPlayerDeck(), mActivePlayer3.getPlayerColor());
        Deck auxDeckPlayer4 = new Deck(mActivePlayer4.getPlayerName(), mActivePlayer4.getPlayerDeck(), mActivePlayer4.getPlayerColor());

        for (int i = 0; i < mAliveDecksList.size(); i++)
            if (!mAliveDecksList.get(i).isEqualDeck(auxDeckPlayer1) && !mAliveDecksList.get(i).isEqualDeck(auxDeckPlayer2) && !mAliveDecksList.get(i).isEqualDeck(auxDeckPlayer3) && !mAliveDecksList.get(i).isEqualDeck(auxDeckPlayer4))
                mAliveDecksList.remove(i);

        boolean alreadyAdded = false;
        if (isPlayerActiveAndAlive(1)) {
            for (int i = 0; i < mAliveDecksList.size(); i++)
                if (mAliveDecksList.get(i).isEqualDeck(auxDeckPlayer1))
                    alreadyAdded = true;
            if (!alreadyAdded)
                mAliveDecksList.add(auxDeckPlayer1);
        }

        alreadyAdded = false;
        if (isPlayerActiveAndAlive(2)) {
            for (int i = 0; i < mAliveDecksList.size(); i++)
                if (mAliveDecksList.get(i).isEqualDeck(auxDeckPlayer2))
                    alreadyAdded = true;
            if (!alreadyAdded)
                mAliveDecksList.add(auxDeckPlayer2);
        }

        alreadyAdded = false;
        if (isPlayerActiveAndAlive(3)) {
            for (int i = 0; i < mAliveDecksList.size(); i++)
                if (mAliveDecksList.get(i).isEqualDeck(auxDeckPlayer3))
                    alreadyAdded = true;
            if (!alreadyAdded)
                mAliveDecksList.add(auxDeckPlayer3);
        }

        alreadyAdded = false;
        if (isPlayerActiveAndAlive(4)) {
            for (int i = 0; i < mAliveDecksList.size(); i++)
                if (mAliveDecksList.get(i).isEqualDeck(auxDeckPlayer4))
                    alreadyAdded = true;
            if (!alreadyAdded)
                mAliveDecksList.add(auxDeckPlayer4);
        }

        return mAliveDecksList;
    }

    public static ArrayList<Deck> getDeadDecksList() {
        if (mDeadDecksList != null) {
            Deck auxDeckPlayer1 = new Deck(mActivePlayer1.getPlayerName(), mActivePlayer1.getPlayerDeck(), mActivePlayer1.getPlayerColor());
            Deck auxDeckPlayer2 = new Deck(mActivePlayer2.getPlayerName(), mActivePlayer2.getPlayerDeck(), mActivePlayer2.getPlayerColor());
            Deck auxDeckPlayer3 = new Deck(mActivePlayer3.getPlayerName(), mActivePlayer3.getPlayerDeck(), mActivePlayer3.getPlayerColor());
            Deck auxDeckPlayer4 = new Deck(mActivePlayer4.getPlayerName(), mActivePlayer4.getPlayerDeck(), mActivePlayer4.getPlayerColor());

            for (int i = 0; i < mDeadDecksList.size(); i++)
                if (!mDeadDecksList.get(i).isEqualDeck(auxDeckPlayer1) && !mDeadDecksList.get(i).isEqualDeck(auxDeckPlayer2) && !mDeadDecksList.get(i).isEqualDeck(auxDeckPlayer3) && !mDeadDecksList.get(i).isEqualDeck(auxDeckPlayer4))
                    mDeadDecksList.remove(i);
        }
        return mDeadDecksList;
    }

    public static ArrayList<Deck> getActiveDecksList() {
        ArrayList<Deck> auxDeckList = new ArrayList<>();
        if (getNumOfActivePlayers() >= 1)
            auxDeckList.add(new Deck(mActivePlayer1.getPlayerName(), mActivePlayer1.getPlayerDeck(), mActivePlayer1.getPlayerColor()));
        if (getNumOfActivePlayers() >= 2)
            auxDeckList.add(new Deck(mActivePlayer2.getPlayerName(), mActivePlayer2.getPlayerDeck(), mActivePlayer2.getPlayerColor()));
        if (getNumOfActivePlayers() >= 3)
            auxDeckList.add(new Deck(mActivePlayer3.getPlayerName(), mActivePlayer3.getPlayerDeck(), mActivePlayer3.getPlayerColor()));
        if (getNumOfActivePlayers() >= 4)
            auxDeckList.add(new Deck(mActivePlayer4.getPlayerName(), mActivePlayer4.getPlayerDeck(), mActivePlayer4.getPlayerColor()));

        return auxDeckList;
    }

    private static int getNumOfActivePlayers() {
        return mMainSectionsPagerAdapter.getCount();
    }

    private static boolean isPlayerActiveAndAlive(int i) {
        if (getNumOfActivePlayers() < i)
            return false;
        else {
            switch (i) {
                case 1:
                    return mActivePlayer1.getPlayerIsAlive();
                case 2:
                    return mActivePlayer2.getPlayerIsAlive();
                case 3:
                    return mActivePlayer3.getPlayerIsAlive();
                case 4:
                    return mActivePlayer4.getPlayerIsAlive();
                default:
                    return true;
            }
        }
    }

    public void createActiveDeadPlayerLifeDialog(final View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setTitle("Do you want to bring " + mTextViewName.getText() + " back to life?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getCurrentActivePlayer().setPlayerIsAlive(true);
                        if (mDeadDecksList == null)
                            mDeadDecksList = new ArrayList<>();

                        for (int i = 0; i < MainActivity.mDeadDecksList.size(); i++) {
                            if (mDeadDecksList.get(i).isEqualDeck(new Deck(getCurrentActivePlayer().getPlayerName(), getCurrentActivePlayer().getPlayerDeck(), getCurrentActivePlayer().getPlayerColor()))) {
                                mDeadDecksList.remove(i);
                                break;
                            }
                        }
                        Toast.makeText(view.getContext(), getCurrentActivePlayer().getPlayerName() + " has awaken!", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }
        );
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        alertDialog.show();
    }

    public void createActivePlayerLifeDialog(final View view) {
        View playerLifeView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_player_life, null);
        final EditText userInput = (EditText) playerLifeView.findViewById(R.id.editTextPlayerLife);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(playerLifeView);
        alertDialogBuilder.setTitle(mTextViewName.getText() + ": " + mTextViewLife.getText());
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String tempLife = userInput.getText().toString();
                        try {
                            if (!tempLife.equalsIgnoreCase("")) {
                                if (Integer.valueOf(tempLife) < Constants.MIN_PLAYER_LIFE_INT)
                                    tempLife = Constants.MIN_PLAYER_LIFE_STRING;
                                if (Integer.valueOf(tempLife) > Constants.MAX_PLAYER_LIFE_INT)
                                    tempLife = Constants.MAX_PLAYER_LIFE_STRING;
                                getCurrentActivePlayer().setPlayerLife(Integer.valueOf(tempLife));
                                setActivePlayerLife(String.valueOf(getCurrentActivePlayer().getPlayerLife()));

                                activePlayerLifeHistoryHandler();
                            }
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
        );
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }
        );
        alertDialogBuilder.setNeutralButton("Lose Game",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getCurrentActivePlayer().setPlayerIsAlive(false);
                        if (mDeadDecksList == null)
                            mDeadDecksList = new ArrayList<>();
                        mDeadDecksList.add(new Deck(getCurrentActivePlayer().getPlayerName(), getCurrentActivePlayer().getPlayerDeck(), getCurrentActivePlayer().getPlayerColor()));
                        Toast.makeText(view.getContext(), getCurrentActivePlayer().getPlayerName() + " has been defeated!", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        alertDialog.show();
    }

    public void createColorPickDialog() {
        final int[] mColor = getResources().getIntArray(R.array.edh_shield_colors);
        final int[] mColor_dark = getResources().getIntArray(R.array.edh_shield_colors_dark);
        ColorPickerDialog colorCalendar = ColorPickerDialog.newInstance(R.string.color_picker_default_title, mColor, getCurrentActivePlayer().getPlayerColor()[0], 5, ColorPickerDialog.SIZE_SMALL);
        colorCalendar.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                int color_dark = color;
                for (int i = 0; i < mColor.length; i++)
                    if (mColor[i] == color)
                        color_dark = mColor_dark[i];
                getCurrentActivePlayer().setPlayerColor(new int[]{color, color_dark});
                mDecksDB.updateDeckColor(new Deck(getCurrentActivePlayer().getPlayerName(), getCurrentActivePlayer().getPlayerDeck(), getCurrentActivePlayer().getPlayerColor()));
            }
        });
        colorCalendar.show(getFragmentManager(), "cal");
    }

    public void createPlayerNameDialog(final View view) {
        if (mActiveDecksList == null) {
            mActiveDecksList = new ArrayList<>();
            mActiveDecksList.add(new Deck(mActivePlayer1.getPlayerName(), mActivePlayer1.getPlayerDeck(), mActivePlayer1.getPlayerColor()));
            mActiveDecksList.add(new Deck(mActivePlayer2.getPlayerName(), mActivePlayer2.getPlayerDeck(), mActivePlayer2.getPlayerColor()));
            mActiveDecksList.add(new Deck(mActivePlayer3.getPlayerName(), mActivePlayer3.getPlayerDeck(), mActivePlayer3.getPlayerColor()));
            mActiveDecksList.add(new Deck(mActivePlayer4.getPlayerName(), mActivePlayer4.getPlayerDeck(), mActivePlayer4.getPlayerColor()));
        }

        View NewGamePlayersChoiceView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_two_spinners, null);
        final Spinner spinnerPlayerName = (Spinner) NewGamePlayersChoiceView.findViewById(R.id.spinner1);
        final Spinner spinnerPlayerDeck = (Spinner) NewGamePlayersChoiceView.findViewById(R.id.spinner2);
        handleSpinners(spinnerPlayerName, spinnerPlayerDeck);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(NewGamePlayersChoiceView);
        alertDialogBuilder.setTitle("Change Player");
        alertDialogBuilder.setMessage("Choose a Player and a Deck:");
        alertDialogBuilder.setPositiveButton("Ok",
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
        alertDialog.show();

        //Override POSITIVE button
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempPlayerName = spinnerPlayerName.getSelectedItem().toString();
                String tempPlayerDeck = spinnerPlayerDeck.getSelectedItem().toString();

                Deck aux = mDecksDB.getDeck(tempPlayerName, tempPlayerDeck);
                int[] tempPlayerColor = aux.getDeckShieldColor();

                if (!tempPlayerName.equalsIgnoreCase(getResources().getString(R.string.edh_spinner_player_hint))
                        && !tempPlayerDeck.equalsIgnoreCase(getResources().getString(R.string.edh_spinner_deck_hint))) {
                    List<Deck> mDeckListAux = new ArrayList<>(mActiveDecksList);
                    if (Record.isValidRecord(mDeckListAux, new Deck(tempPlayerName, tempPlayerDeck))) {
                        for (int i = 0; i < mActiveDecksList.size(); i++) {
                            if (mActiveDecksList.get(i).getDeckOwnerName().equalsIgnoreCase(getCurrentActivePlayer().getPlayerName())
                                    && mActiveDecksList.get(i).getDeckName().equalsIgnoreCase(getCurrentActivePlayer().getPlayerDeck())) {
                                mActiveDecksList.remove(i);
                                mActiveDecksList.add(i, new Deck(tempPlayerName, tempPlayerDeck, tempPlayerColor));
                                break;
                            }
                        }
                        getCurrentActivePlayer().setPlayerName(tempPlayerName);
                        getCurrentActivePlayer().setPlayerDeck(tempPlayerDeck);
                        getCurrentActivePlayer().setPlayerColor(tempPlayerColor);
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(view.getContext(), "ERROR: Player/Deck already added!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(view.getContext(), "ERROR: invalid Player/Deck!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public ActivePlayer getCurrentActivePlayer() {
        if (mCurrentFragment == 0) {
            return mActivePlayer1;
        } else if (mCurrentFragment == 1) {
            return mActivePlayer2;
        } else if (mCurrentFragment == 2) {
            return mActivePlayer3;
        } else if (mCurrentFragment == 3) {
            return mActivePlayer4;
        }
        return new ActivePlayer();
    }

    @Override
    public void onBackPressed() {
        if (drawerMain.isDrawerOpen())
            drawerMain.dismiss();
        else
            super.onBackPressed();
    }

    public void onClickActivePlayerEDH1MinusButton(View view) {
        if (getCurrentActivePlayer().getPlayerEDH1() > Constants.MIN_EDH_DAMAGE_INT) {
            getCurrentActivePlayer().setPlayerEDH1(getCurrentActivePlayer().getPlayerEDH1() - 1);
            setActivePlayerEDH1(String.valueOf(getCurrentActivePlayer().getPlayerEDH1()));

            getCurrentActivePlayer().setPlayerLife(getCurrentActivePlayer().getPlayerLife() + 1);
            setActivePlayerLife(String.valueOf(getCurrentActivePlayer().getPlayerLife()));

            activePlayerLifeHistoryHandler();
            updateLayout(getCurrentActivePlayer());
        }
    }

    public void onClickActivePlayerEDH1PlusButton(View view) {
        if (getCurrentActivePlayer().getPlayerEDH1() < Constants.MAX_EDH_DAMAGE_INT) {
            getCurrentActivePlayer().setPlayerEDH1(getCurrentActivePlayer().getPlayerEDH1() + 1);
            setActivePlayerEDH1(String.valueOf(getCurrentActivePlayer().getPlayerEDH1()));

            getCurrentActivePlayer().setPlayerLife(getCurrentActivePlayer().getPlayerLife() - 1);
            setActivePlayerLife(String.valueOf(getCurrentActivePlayer().getPlayerLife()));

            activePlayerLifeHistoryHandler();
            updateLayout(getCurrentActivePlayer());
        }
    }

    public void onClickActivePlayerEDH2MinusButton(View view) {
        if (getCurrentActivePlayer().getPlayerEDH2() > Constants.MIN_EDH_DAMAGE_INT) {
            getCurrentActivePlayer().setPlayerEDH2(getCurrentActivePlayer().getPlayerEDH2() - 1);
            setActivePlayerEDH2(String.valueOf(getCurrentActivePlayer().getPlayerEDH2()));

            getCurrentActivePlayer().setPlayerLife(getCurrentActivePlayer().getPlayerLife() + 1);
            setActivePlayerLife(String.valueOf(getCurrentActivePlayer().getPlayerLife()));

            activePlayerLifeHistoryHandler();
            updateLayout(getCurrentActivePlayer());
        }
    }

    public void onClickActivePlayerEDH2PlusButton(View view) {
        if (getCurrentActivePlayer().getPlayerEDH2() < Constants.MAX_EDH_DAMAGE_INT) {
            getCurrentActivePlayer().setPlayerEDH2(getCurrentActivePlayer().getPlayerEDH2() + 1);
            setActivePlayerEDH2(String.valueOf(getCurrentActivePlayer().getPlayerEDH2()));

            getCurrentActivePlayer().setPlayerLife(getCurrentActivePlayer().getPlayerLife() - 1);
            setActivePlayerLife(String.valueOf(getCurrentActivePlayer().getPlayerLife()));

            activePlayerLifeHistoryHandler();
            updateLayout(getCurrentActivePlayer());
        }
    }

    public void onClickActivePlayerEDH3MinusButton(View view) {
        if (getCurrentActivePlayer().getPlayerEDH3() > Constants.MIN_EDH_DAMAGE_INT) {
            getCurrentActivePlayer().setPlayerEDH3(getCurrentActivePlayer().getPlayerEDH3() - 1);
            setActivePlayerEDH3(String.valueOf(getCurrentActivePlayer().getPlayerEDH3()));

            getCurrentActivePlayer().setPlayerLife(getCurrentActivePlayer().getPlayerLife() + 1);
            setActivePlayerLife(String.valueOf(getCurrentActivePlayer().getPlayerLife()));

            activePlayerLifeHistoryHandler();
            updateLayout(getCurrentActivePlayer());
        }
    }

    public void onClickActivePlayerEDH3PlusButton(View view) {
        if (getCurrentActivePlayer().getPlayerEDH3() < Constants.MAX_EDH_DAMAGE_INT) {
            getCurrentActivePlayer().setPlayerEDH3(getCurrentActivePlayer().getPlayerEDH3() + 1);
            setActivePlayerEDH3(String.valueOf(getCurrentActivePlayer().getPlayerEDH3()));

            getCurrentActivePlayer().setPlayerLife(getCurrentActivePlayer().getPlayerLife() - 1);
            setActivePlayerLife(String.valueOf(getCurrentActivePlayer().getPlayerLife()));

            activePlayerLifeHistoryHandler();
            updateLayout(getCurrentActivePlayer());
        }
    }

    public void onClickActivePlayerEDH4MinusButton(View view) {
        if (getCurrentActivePlayer().getPlayerEDH4() > Constants.MIN_EDH_DAMAGE_INT) {
            getCurrentActivePlayer().setPlayerEDH4(getCurrentActivePlayer().getPlayerEDH4() - 1);
            setActivePlayerEDH4(String.valueOf(getCurrentActivePlayer().getPlayerEDH4()));

            getCurrentActivePlayer().setPlayerLife(getCurrentActivePlayer().getPlayerLife() + 1);
            setActivePlayerLife(String.valueOf(getCurrentActivePlayer().getPlayerLife()));

            activePlayerLifeHistoryHandler();
            updateLayout(getCurrentActivePlayer());
        }
    }

    public void onClickActivePlayerEDH4PlusButton(View view) {
        if (getCurrentActivePlayer().getPlayerEDH4() < Constants.MAX_EDH_DAMAGE_INT) {
            getCurrentActivePlayer().setPlayerEDH4(getCurrentActivePlayer().getPlayerEDH4() + 1);
            setActivePlayerEDH4(String.valueOf(getCurrentActivePlayer().getPlayerEDH4()));

            getCurrentActivePlayer().setPlayerLife(getCurrentActivePlayer().getPlayerLife() - 1);
            setActivePlayerLife(String.valueOf(getCurrentActivePlayer().getPlayerLife()));

            activePlayerLifeHistoryHandler();
            updateLayout(getCurrentActivePlayer());
        }
    }

    public void onClickActivePlayerLifeMinusButton(View view) {
        if (getCurrentActivePlayer().getPlayerLife() > Constants.MIN_PLAYER_LIFE_INT) {
            getCurrentActivePlayer().setPlayerLife(getCurrentActivePlayer().getPlayerLife() - 1);
            setActivePlayerLife(String.valueOf(getCurrentActivePlayer().getPlayerLife()));

            activePlayerLifeHistoryHandler();
            updateLayout(getCurrentActivePlayer());
        }
    }

    public void onClickActivePlayerLifePlusButton(View view) {
        if (getCurrentActivePlayer().getPlayerLife() < Constants.MAX_PLAYER_LIFE_INT) {
            getCurrentActivePlayer().setPlayerLife(getCurrentActivePlayer().getPlayerLife() + 1);
            setActivePlayerLife(String.valueOf(getCurrentActivePlayer().getPlayerLife()));

            activePlayerLifeHistoryHandler();
            updateLayout(getCurrentActivePlayer());
        }
    }

    public void onClickActivePlayerName(View v) {
        if (!drawerMain.isDrawerOpen())
            createPlayerNameDialog(v);
    }

    public void onClickKeepScreenOn(View view) {

        if (!mCheckBoxKeepScreenOn.isChecked()) {
            getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.SCREEN_ON, 0).commit();
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.SCREEN_ON, 1).commit();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerMain.getDrawerToggle().onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onLongClickActivePlayerLife(View v) {
        if (!drawerMain.isDrawerOpen()) {
            if (getCurrentActivePlayer().getPlayerIsAlive())
                createActivePlayerLifeDialog(v);
            else
                createActiveDeadPlayerLifeDialog(v);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //DrawerMain menu
        if (drawerMain.getDrawerToggle().onOptionsItemSelected(item)) {
            return true;
        }

        //Option menu
        int id = item.getItemId();
        if (id == R.id.action_overview) {
            startActivity(new Intent(this, OverviewActivity.class));
        } else if (id == R.id.action_history) {
            startActivity(new Intent(this, HistoryActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            mainPlaceholderFragment = (MainPlaceholderFragment) mMainSectionsPagerAdapter.instantiateItem(mViewPager, mCurrentFragment);
            createLayout(mainPlaceholderFragment.getView());
            updateLayout(getCurrentActivePlayer());
            highlightActivePlayer(getCurrentActivePlayer());
            setActivePlayerBarColor(getCurrentActivePlayer().getPlayerColor());
            mViewPager.setCurrentItem(mCurrentFragment, false);
        }
    }

    public void setActivePlayerBarColor(int[] colors) {
        if (colors[0] == 0 || colors[1] == 0)
            colors = getResources().getIntArray(R.array.edh_default);

        ActionBar bar = getSupportActionBar();
        if (bar != null)
            bar.setBackgroundDrawable(new ColorDrawable(colors[0]));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(colors[1]);
        }
    }

    public void setActivePlayerEDH1(String edh) {
        mTextViewEDH1.setText(edh);
    }

    public void setActivePlayerEDH2(String edh) {
        mTextViewEDH2.setText(edh);
    }

    public void setActivePlayerEDH3(String edh) {
        mTextViewEDH3.setText(edh);
    }

    public void setActivePlayerEDH4(String edh) {
        mTextViewEDH4.setText(edh);
    }

    public void setActivePlayerLife(String playerLife) {
        mTextViewLife.setText(playerLife);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setActivePlayerBarColor(getResources().getIntArray(R.array.edh_default));

        //DrawerMain menu
        List<String[]> drawerLists = new ArrayList<>();
        drawerLists.add(getResources().getStringArray(R.array.string_menu_main_1));
        drawerLists.add(getResources().getStringArray(R.array.string_menu_main_2));

        assert getSupportActionBar() != null;
        drawerMain = new DrawerMain(this, drawerLists);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Fragments
        mMainSectionsPagerAdapter = new MainSectionsPagerAdapter(getSupportFragmentManager());
        mMainSectionsPagerAdapter.setCount(getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getInt(Constants.TOTAL_PLAYERS, 4));
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mMainSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mCurrentFragment = position;
                getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.CURRENT_PAGE, mCurrentFragment).commit();
                mainPlaceholderFragment = (MainPlaceholderFragment) mMainSectionsPagerAdapter.instantiateItem(mViewPager, position);
                createLayout(mainPlaceholderFragment.getView());
                updateLayout(getCurrentActivePlayer());
                highlightActivePlayer(getCurrentActivePlayer());
                setActivePlayerBarColor(getCurrentActivePlayer().getPlayerColor());
            }
        });

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getStringExtra(Constants.BROADCAST_MESSAGE_RANDOM_PLAYER_OPTION) != null) {
                    createRandomPlayerDialog(mainPlaceholderFragment.getView());
                } else if (intent.getStringExtra(Constants.BROADCAST_MESSAGE_NEW_GAME_OPTION) != null) {
                    newGame(intent.getStringExtra(Constants.BROADCAST_MESSAGE_NEW_GAME_OPTION), intent);
                }
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayersDB.close();
        mDecksDB.close();

        getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.CURRENT_PAGE, mCurrentFragment).commit();
        ActivePlayer.savePlayerSharedPreferences(this, mActivePlayer1);
        ActivePlayer.savePlayerSharedPreferences(this, mActivePlayer2);
        ActivePlayer.savePlayerSharedPreferences(this, mActivePlayer3);
        ActivePlayer.savePlayerSharedPreferences(this, mActivePlayer4);

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerMain.getDrawerToggle().syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayersDB = new PlayersDataAccessObject(this);
        mDecksDB = new DecksDataAccessObject(this);
        mPlayersDB.open();
        mDecksDB.open();

        mCurrentFragment = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getInt(Constants.CURRENT_PAGE, 0);
        if (mCurrentFragment + 1 > mMainSectionsPagerAdapter.getCount())
            mCurrentFragment = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("TAG")) {
            mCurrentFragment = Integer.valueOf(extras.getString("TAG")) - 1;
        }

        mActivePlayer1 = ActivePlayer.loadPlayerSharedPreferences(this, 1);
        mActivePlayer2 = ActivePlayer.loadPlayerSharedPreferences(this, 2);
        mActivePlayer3 = ActivePlayer.loadPlayerSharedPreferences(this, 3);
        mActivePlayer4 = ActivePlayer.loadPlayerSharedPreferences(this, 4);

        //Load the dead players
        if (mDeadDecksList == null) {
            mDeadDecksList = new ArrayList<>();
            if (!mActivePlayer1.getPlayerIsAlive())
                mDeadDecksList.add(new Deck(mActivePlayer1.getPlayerName(), mActivePlayer1.getPlayerDeck(), mActivePlayer1.getPlayerColor()));
            if (!mActivePlayer2.getPlayerIsAlive())
                mDeadDecksList.add(new Deck(mActivePlayer2.getPlayerName(), mActivePlayer2.getPlayerDeck(), mActivePlayer2.getPlayerColor()));
            if (!mActivePlayer3.getPlayerIsAlive())
                mDeadDecksList.add(new Deck(mActivePlayer3.getPlayerName(), mActivePlayer3.getPlayerDeck(), mActivePlayer3.getPlayerColor()));
            if (!mActivePlayer4.getPlayerIsAlive())
                mDeadDecksList.add(new Deck(mActivePlayer4.getPlayerName(), mActivePlayer4.getPlayerDeck(), mActivePlayer4.getPlayerColor()));
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter(Constants.BROADCAST_INTENT));
    }

    private void activePlayerLifeHistoryHandler() {
        Thread threadLife;
        final int playerTag;

        if (getCurrentActivePlayer().getPlayerTag() == 1) {
            threadLife = mThreadLife1;
            playerTag = 1;
        } else if (getCurrentActivePlayer().getPlayerTag() == 2) {
            threadLife = mThreadLife2;
            playerTag = 2;
        } else if (getCurrentActivePlayer().getPlayerTag() == 3) {
            threadLife = mThreadLife3;
            playerTag = 3;
        } else {
            threadLife = mThreadLife4;
            playerTag = 4;
        }

        if (threadLife != null)
            threadLife.interrupt();

        threadLife = new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            int latestSavedLife;
                            String latestSavedLifePreferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getString(Constants.PLAYER_HISTORY_LIFE + playerTag, Constants.INITIAL_PLAYER_LIFE_STRING);
                            if (!latestSavedLifePreferences.isEmpty()) {
                                String[] latestSavedLifeArray = latestSavedLifePreferences.split("_");
                                latestSavedLife = Integer.valueOf(latestSavedLifeArray[latestSavedLifeArray.length - 1]);
                            } else
                                latestSavedLife = getActivePlayerByTag(playerTag).getPlayerLife();

                            Thread.sleep(2000);

                            int currentLife = getActivePlayerByTag(playerTag).getPlayerLife();

                            if ((currentLife - latestSavedLife) != 0) {
                                String lifeToBeSaved = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getString(Constants.PLAYER_HISTORY_LIFE + playerTag, Constants.INITIAL_PLAYER_LIFE_STRING);
                                lifeToBeSaved = lifeToBeSaved + "_" + currentLife;
                                getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putString(Constants.PLAYER_HISTORY_LIFE + playerTag, lifeToBeSaved).commit();

                                // SAVE EDH DAMAGE
                                String latestSavedEDH;
                                String latestSavedEDHPreferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getString(Constants.PLAYER_EDH_PREFIX + playerTag, "0@0@0@0");
                                if (!latestSavedEDHPreferences.isEmpty()) {
                                    String[] latestSavedLifeArray = latestSavedEDHPreferences.split("_");
                                    latestSavedEDH = latestSavedLifeArray[latestSavedLifeArray.length - 1];
                                } else
                                    latestSavedEDH = getActivePlayerByTag(playerTag).getPlayerEDH1() + "@" + getActivePlayerByTag(playerTag).getPlayerEDH2() + "@"
                                            + getActivePlayerByTag(playerTag).getPlayerEDH3() + "@" + getActivePlayerByTag(playerTag).getPlayerEDH4();

                                String currentEdh = getActivePlayerByTag(playerTag).getPlayerEDH1() + "@" + getActivePlayerByTag(playerTag).getPlayerEDH2() + "@"
                                        + getActivePlayerByTag(playerTag).getPlayerEDH3() + "@" + getActivePlayerByTag(playerTag).getPlayerEDH4();

                                if (!currentEdh.equalsIgnoreCase(latestSavedEDH)) {
                                    String edhToBeSaved = latestSavedEDHPreferences + "_" + currentEdh;
                                    getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putString(Constants.PLAYER_EDH_PREFIX + playerTag, edhToBeSaved).commit();
                                } else {
                                    String edhToBeSaved = latestSavedEDHPreferences + "_" + currentEdh;
                                    getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putString(Constants.PLAYER_EDH_PREFIX + playerTag, edhToBeSaved).commit();
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        threadLife.start();

        if (getCurrentActivePlayer().getPlayerTag() == 1)
            mThreadLife1 = threadLife;
        else if (getCurrentActivePlayer().getPlayerTag() == 2)
            mThreadLife2 = threadLife;
        else if (getCurrentActivePlayer().getPlayerTag() == 3)
            mThreadLife3 = threadLife;
        else
            mThreadLife4 = threadLife;
    }

    private void createLayout(View view) {
        if (view != null) {
            mCheckBoxKeepScreenOn = (CheckBox) findViewById(R.id.checkBoxKeepScreenOn);
            mImageViewThrone = (ImageView) view.findViewById(R.id.imageViewThrone);

            mRelativeEDH1 = (LinearLayout) view.findViewById(R.id.relativeEDH1);
            mRelativeEDH2 = (LinearLayout) view.findViewById(R.id.relativeEDH2);
            mRelativeEDH3 = (LinearLayout) view.findViewById(R.id.relativeEDH3);
            mRelativeEDH4 = (LinearLayout) view.findViewById(R.id.relativeEDH4);

            mTextViewName = (TextView) view.findViewById(R.id.textViewName);
            mTextViewDeck = (TextView) view.findViewById(R.id.textViewDeck);
            mTextViewLife = (TextView) view.findViewById(R.id.textViewLife);
            mTextViewEDH1 = (TextView) view.findViewById(R.id.textViewEDH1);
            mTextViewEDH2 = (TextView) view.findViewById(R.id.textViewEDH2);
            mTextViewEDH3 = (TextView) view.findViewById(R.id.textViewEDH3);
            mTextViewEDH4 = (TextView) view.findViewById(R.id.textViewEDH4);
            mTextViewP1Name = (TextView) view.findViewById(R.id.textViewP1Name);
            mTextViewP2Name = (TextView) view.findViewById(R.id.textViewP2Name);
            mTextViewP3Name = (TextView) view.findViewById(R.id.textViewP3Name);
            mTextViewP4Name = (TextView) view.findViewById(R.id.textViewP4Name);

            mButtonLifePositive = (Button) view.findViewById(R.id.buttonLifePositive);
            mButtonLifeNegative = (Button) view.findViewById(R.id.buttonLifeNegative);

            mButtonEDH1Positive = (Button) view.findViewById(R.id.buttonEDH1Positive);
            mButtonEDH1Negative = (Button) view.findViewById(R.id.buttonEDH1Negative);
            mButtonEDH2Positive = (Button) view.findViewById(R.id.buttonEDH2Positive);
            mButtonEDH2Negative = (Button) view.findViewById(R.id.buttonEDH2Negative);
            mButtonEDH3Positive = (Button) view.findViewById(R.id.buttonEDH3Positive);
            mButtonEDH3Negative = (Button) view.findViewById(R.id.buttonEDH3Negative);
            mButtonEDH4Positive = (Button) view.findViewById(R.id.buttonEDH4Positive);
            mButtonEDH4Negative = (Button) view.findViewById(R.id.buttonEDH4Negative);

            mTextViewName.setOnLongClickListener(
                    new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            createColorPickDialog();
                            return true;
                        }
                    }
            );

            mTextViewLife.setOnLongClickListener(
                    new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            onLongClickActivePlayerLife(v);
                            return true;
                        }
                    }
            );
        }
    }

    private void createRandomPlayerDialog(final View view) {
        final int minValue = 1;
        final int maxValue = getNumOfActivePlayers();
        final Random r = new Random();
        final int[] randomResult = {r.nextInt(maxValue - minValue + 1) + minValue};

        View logView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_roll_a_dice_result, null);
        final TextView textViewResult = (TextView) logView.findViewById(R.id.textViewDiceResult);
        textViewResult.setTextSize(42);
        textViewResult.setText(MessageFormat.format("{0}", getActivePlayerByTag(randomResult[0]).getPlayerName()));

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(logView);
        alertDialogBuilder.setTitle("Random Player:");
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertDialogBuilder.setNeutralButton("Random",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Overridden at 'alertDialog.getButton' to avoid dismiss every time
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomResult[0] = r.nextInt(maxValue - minValue + 1) + minValue;
                textViewResult.setText(MessageFormat.format("{0}", getActivePlayerByTag(randomResult[0]).getPlayerName()));
            }
        });
    }

    private ActivePlayer getActivePlayerByTag(int playerTag) {
        if (mActivePlayer1.getPlayerTag() == playerTag)
            return mActivePlayer1;
        else if (mActivePlayer2.getPlayerTag() == playerTag)
            return mActivePlayer2;
        else if (mActivePlayer3.getPlayerTag() == playerTag)
            return mActivePlayer3;
        else if (mActivePlayer4.getPlayerTag() == playerTag)
            return mActivePlayer4;
        return new ActivePlayer();
    }

    private void handleSpinners(Spinner spinnerName, Spinner spinnerDeck) {
        final ArrayList<String> players = (ArrayList<String>) mPlayersDB.getAllPlayers();
        players.add(0, this.getString(R.string.edh_spinner_player_hint));

        final ArrayList<String> decks;
        decks = new ArrayList<>();

        final ArrayAdapter<String> playersNameAdapter;
        playersNameAdapter = new ArrayAdapter<>(this, R.layout.row_spinner_selected, players);
        playersNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final ArrayAdapter<String> playersDeckAdapter;
        playersDeckAdapter = new ArrayAdapter<>(this, R.layout.row_spinner_selected, decks);
        playersDeckAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerName.setAdapter(playersNameAdapter);
        spinnerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<Deck> aux = mDecksDB.getAllDeckByPlayerName(players.get(position));
                decks.clear();
                decks.add(0, getResources().getString(R.string.edh_spinner_deck_hint));
                for (int i = 0; i < aux.size(); i++) {
                    decks.add(aux.get(i).getDeckName());
                }
                playersDeckAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerDeck.setAdapter(playersDeckAdapter);
    }

    private void highlightActivePlayer(ActivePlayer currentActivePlayer) {
        if (currentActivePlayer.getPlayerTag() == 1) {
            mTextViewP1Name.setTypeface(null, Typeface.BOLD);
            mTextViewP1Name.setEnabled(currentActivePlayer.getPlayerIsAlive());
            mTextViewP1Name.setTextColor(currentActivePlayer.getPlayerIsAlive() ? getCurrentActivePlayer().getPlayerColor()[1] : Color.LTGRAY);
        } else if (currentActivePlayer.getPlayerTag() == 2) {
            mTextViewP2Name.setTypeface(null, Typeface.BOLD);
            mTextViewP2Name.setEnabled(currentActivePlayer.getPlayerIsAlive());
            mTextViewP2Name.setTextColor(currentActivePlayer.getPlayerIsAlive() ? getCurrentActivePlayer().getPlayerColor()[1] : Color.LTGRAY);
        } else if (currentActivePlayer.getPlayerTag() == 3) {
            mTextViewP3Name.setTypeface(null, Typeface.BOLD);
            mTextViewP3Name.setEnabled(currentActivePlayer.getPlayerIsAlive());
            mTextViewP3Name.setTextColor(currentActivePlayer.getPlayerIsAlive() ? getCurrentActivePlayer().getPlayerColor()[1] : Color.LTGRAY);
        } else if (currentActivePlayer.getPlayerTag() == 4) {
            mTextViewP4Name.setTypeface(null, Typeface.BOLD);
            mTextViewP4Name.setEnabled(currentActivePlayer.getPlayerIsAlive());
            mTextViewP4Name.setTextColor(currentActivePlayer.getPlayerIsAlive() ? getCurrentActivePlayer().getPlayerColor()[1] : Color.LTGRAY);
        }
    }

    private void newGame(String option, Intent intent) {
        resetHistoryLife();

        if (option.equalsIgnoreCase(Constants.BROADCAST_MESSAGE_NEW_GAME_OPTION)) {
            ArrayList<String> players = intent.getStringArrayListExtra(Constants.BROADCAST_MESSAGE_NEW_GAME_PLAYERS);
            ArrayList<String> decks = intent.getStringArrayListExtra(Constants.BROADCAST_MESSAGE_NEW_GAME_DECKS);
            ArrayList<String> color = intent.getStringArrayListExtra(Constants.BROADCAST_MESSAGE_NEW_GAME_COLOR);

            int[] defaultColor = getResources().getIntArray(R.array.edh_default);
            mActivePlayer1 = new ActivePlayer(this.getResources().getString(R.string.default_player_1), this.getResources().getString(R.string.default_deck_1), true, Constants.INITIAL_PLAYER_LIFE_INT, Constants.INITIAL_EDH_DAMAGE_INT, Constants.INITIAL_EDH_DAMAGE_INT, Constants.INITIAL_EDH_DAMAGE_INT, Constants.INITIAL_EDH_DAMAGE_INT, defaultColor, 1);
            mActivePlayer2 = new ActivePlayer(this.getResources().getString(R.string.default_player_2), this.getResources().getString(R.string.default_deck_2), true, Constants.INITIAL_PLAYER_LIFE_INT, Constants.INITIAL_EDH_DAMAGE_INT, Constants.INITIAL_EDH_DAMAGE_INT, Constants.INITIAL_EDH_DAMAGE_INT, Constants.INITIAL_EDH_DAMAGE_INT, defaultColor, 2);
            mActivePlayer3 = new ActivePlayer(this.getResources().getString(R.string.default_player_3), this.getResources().getString(R.string.default_deck_3), true, Constants.INITIAL_PLAYER_LIFE_INT, Constants.INITIAL_EDH_DAMAGE_INT, Constants.INITIAL_EDH_DAMAGE_INT, Constants.INITIAL_EDH_DAMAGE_INT, Constants.INITIAL_EDH_DAMAGE_INT, defaultColor, 3);
            mActivePlayer4 = new ActivePlayer(this.getResources().getString(R.string.default_player_4), this.getResources().getString(R.string.default_deck_4), true, Constants.INITIAL_PLAYER_LIFE_INT, Constants.INITIAL_EDH_DAMAGE_INT, Constants.INITIAL_EDH_DAMAGE_INT, Constants.INITIAL_EDH_DAMAGE_INT, Constants.INITIAL_EDH_DAMAGE_INT, defaultColor, 4);

            if (players.size() >= 1) {
                mActivePlayer1.setPlayerName(players.get(0));
                mActivePlayer1.setPlayerDeck(decks.get(0));
                mActivePlayer1.setPlayerColor(new int[]{Integer.valueOf(color.get(0).split(System.getProperty("path.separator"))[0]), Integer.valueOf(color.get(0).split(System.getProperty("path.separator"))[1])});
            }
            if (players.size() >= 2) {
                mActivePlayer2.setPlayerName(players.get(1));
                mActivePlayer2.setPlayerDeck(decks.get(1));
                mActivePlayer2.setPlayerColor(new int[]{Integer.valueOf(color.get(1).split(System.getProperty("path.separator"))[0]), Integer.valueOf(color.get(1).split(System.getProperty("path.separator"))[1])});
            }
            if (players.size() >= 3) {
                mActivePlayer3.setPlayerName(players.get(2));
                mActivePlayer3.setPlayerDeck(decks.get(2));
                mActivePlayer3.setPlayerColor(new int[]{Integer.valueOf(color.get(2).split(System.getProperty("path.separator"))[0]), Integer.valueOf(color.get(2).split(System.getProperty("path.separator"))[1])});
            }
            if (players.size() >= 4) {
                mActivePlayer4.setPlayerName(players.get(3));
                mActivePlayer4.setPlayerDeck(decks.get(3));
                mActivePlayer4.setPlayerColor(new int[]{Integer.valueOf(color.get(3).split(System.getProperty("path.separator"))[0]), Integer.valueOf(color.get(3).split(System.getProperty("path.separator"))[1])});
            }

            mMainSectionsPagerAdapter.setCount(players.size());
            mMainSectionsPagerAdapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(0);
            getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.TOTAL_PLAYERS, mMainSectionsPagerAdapter.getCount()).commit();
        }

        mActiveDecksList = new ArrayList<>();
        mActiveDecksList.add(new Deck(mActivePlayer1.getPlayerName(), mActivePlayer1.getPlayerDeck(), mActivePlayer1.getPlayerColor()));
        mActiveDecksList.add(new Deck(mActivePlayer2.getPlayerName(), mActivePlayer2.getPlayerDeck(), mActivePlayer2.getPlayerColor()));
        mActiveDecksList.add(new Deck(mActivePlayer3.getPlayerName(), mActivePlayer3.getPlayerDeck(), mActivePlayer3.getPlayerColor()));
        mActiveDecksList.add(new Deck(mActivePlayer4.getPlayerName(), mActivePlayer4.getPlayerDeck(), mActivePlayer4.getPlayerColor()));

        mDeadDecksList = new ArrayList<>();

        updateLayout(getCurrentActivePlayer());
    }

    private void resetHistoryLife() {
        getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putString(Constants.PLAYER_HISTORY_LIFE + 1, Constants.INITIAL_PLAYER_LIFE_STRING).commit();
        getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putString(Constants.PLAYER_HISTORY_LIFE + 2, Constants.INITIAL_PLAYER_LIFE_STRING).commit();
        getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putString(Constants.PLAYER_HISTORY_LIFE + 3, Constants.INITIAL_PLAYER_LIFE_STRING).commit();
        getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putString(Constants.PLAYER_HISTORY_LIFE + 4, Constants.INITIAL_PLAYER_LIFE_STRING).commit();

        getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putString(Constants.PLAYER_EDH_PREFIX + 1, "0@0@0@0").commit();
        getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putString(Constants.PLAYER_EDH_PREFIX + 2, "0@0@0@0").commit();
        getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putString(Constants.PLAYER_EDH_PREFIX + 3, "0@0@0@0").commit();
        getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putString(Constants.PLAYER_EDH_PREFIX + 4, "0@0@0@0").commit();
    }

    private void updateDethroneIcon() {
        mImageViewThrone.setVisibility(View.INVISIBLE);

        //P1
        if (isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3) && !isPlayerActiveAndAlive(4))
            if (getCurrentActivePlayer().getPlayerTag() == 1)
                mImageViewThrone.setVisibility(View.VISIBLE);

        //P2
        if (!isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3) && !isPlayerActiveAndAlive(4))
            if (getCurrentActivePlayer().getPlayerTag() == 2)
                mImageViewThrone.setVisibility(View.VISIBLE);

        //P3
        if (!isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3) && !isPlayerActiveAndAlive(4))
            if (getCurrentActivePlayer().getPlayerTag() == 3)
                mImageViewThrone.setVisibility(View.VISIBLE);

        //P4
        if (!isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3) && isPlayerActiveAndAlive(4))
            if (getCurrentActivePlayer().getPlayerTag() == 4)
                mImageViewThrone.setVisibility(View.VISIBLE);

        //P1 and P2
        if (isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3) && !isPlayerActiveAndAlive(4)) {
            if (getCurrentActivePlayer().getPlayerTag() == 1 && mActivePlayer1.getPlayerLife() >= mActivePlayer2.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
            if (getCurrentActivePlayer().getPlayerTag() == 2 && mActivePlayer2.getPlayerLife() >= mActivePlayer1.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
        }

        //P1 and P3
        if (isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3) && !isPlayerActiveAndAlive(4)) {
            if (getCurrentActivePlayer().getPlayerTag() == 1 && mActivePlayer1.getPlayerLife() >= mActivePlayer3.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
            if (getCurrentActivePlayer().getPlayerTag() == 3 && mActivePlayer3.getPlayerLife() >= mActivePlayer1.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
        }

        //P1 and P4
        if (isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3) && isPlayerActiveAndAlive(4)) {
            if (getCurrentActivePlayer().getPlayerTag() == 1 && mActivePlayer1.getPlayerLife() >= mActivePlayer4.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
            if (getCurrentActivePlayer().getPlayerTag() == 4 && mActivePlayer4.getPlayerLife() >= mActivePlayer1.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
        }

        //P2 and P3
        if (!isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3) && !isPlayerActiveAndAlive(4)) {
            if (getCurrentActivePlayer().getPlayerTag() == 2 && mActivePlayer2.getPlayerLife() >= mActivePlayer3.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
            if (getCurrentActivePlayer().getPlayerTag() == 3 && mActivePlayer3.getPlayerLife() >= mActivePlayer2.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
        }

        //P2 and P4
        if (!isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3) && isPlayerActiveAndAlive(4)) {
            if (getCurrentActivePlayer().getPlayerTag() == 2 && mActivePlayer2.getPlayerLife() >= mActivePlayer4.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
            if (getCurrentActivePlayer().getPlayerTag() == 4 && mActivePlayer4.getPlayerLife() >= mActivePlayer2.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
        }

        //P3 and P4
        if (!isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3) && isPlayerActiveAndAlive(4)) {
            if (getCurrentActivePlayer().getPlayerTag() == 3 && mActivePlayer3.getPlayerLife() >= mActivePlayer4.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
            if (getCurrentActivePlayer().getPlayerTag() == 4 && mActivePlayer4.getPlayerLife() >= mActivePlayer3.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
        }

        //P1, P2 and P3
        if (isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3) && !isPlayerActiveAndAlive(4)) {
            if (getCurrentActivePlayer().getPlayerTag() == 1 && mActivePlayer1.getPlayerLife() >= mActivePlayer2.getPlayerLife() && mActivePlayer1.getPlayerLife() >= mActivePlayer3.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
            if (getCurrentActivePlayer().getPlayerTag() == 2 && mActivePlayer2.getPlayerLife() >= mActivePlayer1.getPlayerLife() && mActivePlayer2.getPlayerLife() >= mActivePlayer3.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
            if (getCurrentActivePlayer().getPlayerTag() == 3 && mActivePlayer3.getPlayerLife() >= mActivePlayer1.getPlayerLife() && mActivePlayer3.getPlayerLife() >= mActivePlayer2.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
        }

        //P1, P2 and P4
        if (isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3) && isPlayerActiveAndAlive(4)) {
            if (getCurrentActivePlayer().getPlayerTag() == 1 && mActivePlayer1.getPlayerLife() >= mActivePlayer2.getPlayerLife() && mActivePlayer1.getPlayerLife() >= mActivePlayer4.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
            if (getCurrentActivePlayer().getPlayerTag() == 2 && mActivePlayer2.getPlayerLife() >= mActivePlayer1.getPlayerLife() && mActivePlayer2.getPlayerLife() >= mActivePlayer4.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
            if (getCurrentActivePlayer().getPlayerTag() == 4 && mActivePlayer4.getPlayerLife() >= mActivePlayer1.getPlayerLife() && mActivePlayer4.getPlayerLife() >= mActivePlayer2.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
        }

        //P1, P3 and P4
        if (isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3) && isPlayerActiveAndAlive(4)) {
            if (getCurrentActivePlayer().getPlayerTag() == 1 && mActivePlayer1.getPlayerLife() >= mActivePlayer3.getPlayerLife() && mActivePlayer1.getPlayerLife() >= mActivePlayer4.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
            if (getCurrentActivePlayer().getPlayerTag() == 3 && mActivePlayer3.getPlayerLife() >= mActivePlayer1.getPlayerLife() && mActivePlayer3.getPlayerLife() >= mActivePlayer4.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
            if (getCurrentActivePlayer().getPlayerTag() == 4 && mActivePlayer4.getPlayerLife() >= mActivePlayer1.getPlayerLife() && mActivePlayer4.getPlayerLife() >= mActivePlayer3.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
        }

        //P2, P3 and P4
        if (!isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3) && isPlayerActiveAndAlive(4)) {
            if (getCurrentActivePlayer().getPlayerTag() == 2 && mActivePlayer2.getPlayerLife() >= mActivePlayer3.getPlayerLife() && mActivePlayer2.getPlayerLife() >= mActivePlayer4.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
            if (getCurrentActivePlayer().getPlayerTag() == 3 && mActivePlayer3.getPlayerLife() >= mActivePlayer2.getPlayerLife() && mActivePlayer3.getPlayerLife() >= mActivePlayer4.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
            if (getCurrentActivePlayer().getPlayerTag() == 4 && mActivePlayer4.getPlayerLife() >= mActivePlayer3.getPlayerLife() && mActivePlayer4.getPlayerLife() >= mActivePlayer2.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
        }

        //P1, P2, P3 and P4
        if (isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3) && isPlayerActiveAndAlive(4)) {
            if (getCurrentActivePlayer().getPlayerTag() == 1 && mActivePlayer1.getPlayerLife() >= mActivePlayer2.getPlayerLife() && mActivePlayer1.getPlayerLife() >= mActivePlayer3.getPlayerLife() && mActivePlayer1.getPlayerLife() >= mActivePlayer4.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
            if (getCurrentActivePlayer().getPlayerTag() == 2 && mActivePlayer2.getPlayerLife() >= mActivePlayer1.getPlayerLife() && mActivePlayer2.getPlayerLife() >= mActivePlayer3.getPlayerLife() && mActivePlayer2.getPlayerLife() >= mActivePlayer4.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
            if (getCurrentActivePlayer().getPlayerTag() == 3 && mActivePlayer3.getPlayerLife() >= mActivePlayer1.getPlayerLife() && mActivePlayer3.getPlayerLife() >= mActivePlayer2.getPlayerLife() && mActivePlayer3.getPlayerLife() >= mActivePlayer4.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
            if (getCurrentActivePlayer().getPlayerTag() == 4 && mActivePlayer4.getPlayerLife() >= mActivePlayer1.getPlayerLife() && mActivePlayer4.getPlayerLife() >= mActivePlayer2.getPlayerLife() && mActivePlayer4.getPlayerLife() >= mActivePlayer3.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
        }
    }

    private void updateLayout(ActivePlayer currentActivePlayer) {
        if (getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getInt(Constants.SCREEN_ON, 0) == 1) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            mCheckBoxKeepScreenOn.setChecked(true);
        } else {
            mCheckBoxKeepScreenOn.setChecked(false);
            getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        if (currentActivePlayer.getPlayerColor()[0] == 0 || currentActivePlayer.getPlayerColor()[1] == 0)
            currentActivePlayer.setPlayerColor(getResources().getIntArray(R.array.edh_default));

        updateDethroneIcon();

        switch (getNumOfActivePlayers()) {
            case 2:
                mRelativeEDH1.setVisibility(View.VISIBLE);
                mRelativeEDH2.setVisibility(View.VISIBLE);
                mRelativeEDH3.setVisibility(View.INVISIBLE);
                mRelativeEDH4.setVisibility(View.INVISIBLE);
                break;
            case 3:
                mRelativeEDH1.setVisibility(View.VISIBLE);
                mRelativeEDH2.setVisibility(View.VISIBLE);
                mRelativeEDH3.setVisibility(View.VISIBLE);
                mRelativeEDH4.setVisibility(View.INVISIBLE);
                break;
            case 4:
                mRelativeEDH1.setVisibility(View.VISIBLE);
                mRelativeEDH2.setVisibility(View.VISIBLE);
                mRelativeEDH3.setVisibility(View.VISIBLE);
                mRelativeEDH4.setVisibility(View.VISIBLE);
                break;
        }

        mTextViewName.setText(currentActivePlayer.getPlayerName());
        mTextViewDeck.setText(currentActivePlayer.getPlayerDeck());
        mTextViewLife.setText(MessageFormat.format("{0}", String.valueOf(currentActivePlayer.getPlayerLife())));
        mTextViewEDH1.setText(MessageFormat.format("{0}", currentActivePlayer.getPlayerEDH1()));
        mTextViewEDH2.setText(MessageFormat.format("{0}", currentActivePlayer.getPlayerEDH2()));
        mTextViewEDH3.setText(MessageFormat.format("{0}", currentActivePlayer.getPlayerEDH3()));
        mTextViewEDH4.setText(MessageFormat.format("{0}", currentActivePlayer.getPlayerEDH4()));
        mTextViewP1Name.setText(mActivePlayer1.getPlayerName());
        mTextViewP2Name.setText(mActivePlayer2.getPlayerName());
        mTextViewP3Name.setText(mActivePlayer3.getPlayerName());
        mTextViewP4Name.setText(mActivePlayer4.getPlayerName());

        // Set enabled according to alive status
        mRelativeEDH1.setEnabled(getCurrentActivePlayer().getPlayerIsAlive());
        mRelativeEDH2.setEnabled(getCurrentActivePlayer().getPlayerIsAlive());
        mRelativeEDH2.setEnabled(getCurrentActivePlayer().getPlayerIsAlive());
        mRelativeEDH3.setEnabled(getCurrentActivePlayer().getPlayerIsAlive());
        mRelativeEDH4.setEnabled(getCurrentActivePlayer().getPlayerIsAlive());
        mTextViewName.setEnabled(getCurrentActivePlayer().getPlayerIsAlive());
        mTextViewDeck.setEnabled(getCurrentActivePlayer().getPlayerIsAlive());
        mTextViewLife.setEnabled(true); //mTextViewLife always true to onLongClick
        mTextViewLife.setTextColor(getCurrentActivePlayer().getPlayerIsAlive() ? Color.BLACK : Color.LTGRAY);
        mTextViewEDH1.setEnabled(getCurrentActivePlayer().getPlayerIsAlive());
        mTextViewEDH2.setEnabled(getCurrentActivePlayer().getPlayerIsAlive());
        mTextViewEDH3.setEnabled(getCurrentActivePlayer().getPlayerIsAlive());
        mTextViewEDH4.setEnabled(getCurrentActivePlayer().getPlayerIsAlive());
        mTextViewP1Name.setEnabled(getCurrentActivePlayer().getPlayerIsAlive());
        mTextViewP2Name.setEnabled(getCurrentActivePlayer().getPlayerIsAlive());
        mTextViewP3Name.setEnabled(getCurrentActivePlayer().getPlayerIsAlive());
        mTextViewP4Name.setEnabled(getCurrentActivePlayer().getPlayerIsAlive());
        mButtonLifePositive.setEnabled(getCurrentActivePlayer().getPlayerIsAlive());
        mButtonLifeNegative.setEnabled(getCurrentActivePlayer().getPlayerIsAlive());
        mButtonEDH1Positive.setEnabled(getCurrentActivePlayer().getPlayerIsAlive());
        mButtonEDH1Negative.setEnabled(getCurrentActivePlayer().getPlayerIsAlive());
        mButtonEDH2Positive.setEnabled(getCurrentActivePlayer().getPlayerIsAlive());
        mButtonEDH2Negative.setEnabled(getCurrentActivePlayer().getPlayerIsAlive());
        mButtonEDH3Positive.setEnabled(getCurrentActivePlayer().getPlayerIsAlive());
        mButtonEDH3Negative.setEnabled(getCurrentActivePlayer().getPlayerIsAlive());
        mButtonEDH4Positive.setEnabled(getCurrentActivePlayer().getPlayerIsAlive());
        mButtonEDH4Negative.setEnabled(getCurrentActivePlayer().getPlayerIsAlive());
        mButtonLifePositive.getBackground().setColorFilter(getCurrentActivePlayer().getPlayerIsAlive() ? getCurrentActivePlayer().getPlayerColor()[0] : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        mButtonLifeNegative.getBackground().setColorFilter(getCurrentActivePlayer().getPlayerIsAlive() ? getCurrentActivePlayer().getPlayerColor()[0] : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        mButtonEDH1Positive.getBackground().setColorFilter(getCurrentActivePlayer().getPlayerIsAlive() ? getCurrentActivePlayer().getPlayerColor()[0] : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        mButtonEDH1Negative.getBackground().setColorFilter(getCurrentActivePlayer().getPlayerIsAlive() ? getCurrentActivePlayer().getPlayerColor()[0] : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        mButtonEDH2Positive.getBackground().setColorFilter(getCurrentActivePlayer().getPlayerIsAlive() ? getCurrentActivePlayer().getPlayerColor()[0] : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        mButtonEDH2Negative.getBackground().setColorFilter(getCurrentActivePlayer().getPlayerIsAlive() ? getCurrentActivePlayer().getPlayerColor()[0] : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        mButtonEDH3Positive.getBackground().setColorFilter(getCurrentActivePlayer().getPlayerIsAlive() ? getCurrentActivePlayer().getPlayerColor()[0] : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        mButtonEDH3Negative.getBackground().setColorFilter(getCurrentActivePlayer().getPlayerIsAlive() ? getCurrentActivePlayer().getPlayerColor()[0] : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        mButtonEDH4Positive.getBackground().setColorFilter(getCurrentActivePlayer().getPlayerIsAlive() ? getCurrentActivePlayer().getPlayerColor()[0] : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        mButtonEDH4Negative.getBackground().setColorFilter(getCurrentActivePlayer().getPlayerIsAlive() ? getCurrentActivePlayer().getPlayerColor()[0] : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
    }
}
