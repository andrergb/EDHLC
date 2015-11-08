package com.android.argb.edhlc.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.argb.edhlc.PlaceholderFragment;
import com.android.argb.edhlc.R;
import com.android.argb.edhlc.SectionsPagerAdapter;
import com.android.argb.edhlc.colorpicker.ColorPickerDialog;
import com.android.argb.edhlc.colorpicker.ColorPickerSwatch;
import com.android.argb.edhlc.objects.ActivePlayer;
import com.android.argb.edhlc.objects.DrawerMain;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    public static ActivePlayer mActivePlayer1;
    public static ActivePlayer mActivePlayer2;
    public static ActivePlayer mActivePlayer3;
    public static ActivePlayer mActivePlayer4;

    private static ImageView mImageViewThrone;
    private static TextView mTextViewName;
    private static TextView mTextViewLife;
    private static TextView mTextViewEDH1;
    private static TextView mTextViewEDH2;
    private static TextView mTextViewEDH3;
    private static TextView mTextViewEDH4;
    private static TextView mTextViewP1Name;
    private static TextView mTextViewP2Name;
    private static TextView mTextViewP3Name;
    private static TextView mTextViewP4Name;
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
    private static CheckBox checkBox;
    private static Thread threadLife1;
    private static Thread threadLife2;
    private static Thread threadLife3;
    private static Thread threadLife4;

    private PlaceholderFragment placeholderFragment;
    private int currentFragment = 0;

    private DrawerMain drawerMain;
    BroadcastReceiver mNewGameBroadcastReceiver;

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
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.setCount(getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).getInt("NUM_PLAYERS", 4));
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentFragment = position;
                getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).edit().putInt("CURRENT_PAGE", currentFragment).commit();
                placeholderFragment = (PlaceholderFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, position);
                createLayout(placeholderFragment.getView());
                updateLayout(getCurrentActivePlayer());
                highlightActivePlayer(getCurrentActivePlayer());
                setActivePlayerBarColor(getCurrentActivePlayer().getPlayerColor());
            }
        });

        mNewGameBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                newGame(intent.getStringExtra(DrawerMain.BROADCAST_MESSAGE_NEWGAME_OPTION));
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentFragment = getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).getInt("CURRENT_PAGE", 0);
        if (currentFragment + 1 > mSectionsPagerAdapter.getCount())
            currentFragment = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("TAG"))
            currentFragment = Integer.valueOf(extras.getString("TAG")) - 1;

        mActivePlayer1 = ActivePlayer.loadPlayerSharedPreferences(this, 1);
        mActivePlayer2 = ActivePlayer.loadPlayerSharedPreferences(this, 2);
        mActivePlayer3 = ActivePlayer.loadPlayerSharedPreferences(this, 3);
        mActivePlayer4 = ActivePlayer.loadPlayerSharedPreferences(this, 4);

        LocalBroadcastManager.getInstance(this).registerReceiver(mNewGameBroadcastReceiver, new IntentFilter(DrawerMain.BROADCAST_INTENTFILTER_NEWGAME));
    }

    @Override
    protected void onPause() {
        super.onPause();
        getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).edit().putInt("CURRENT_PAGE", currentFragment).commit();
        ActivePlayer.savePlayerSharedPreferences(this, mActivePlayer1);
        ActivePlayer.savePlayerSharedPreferences(this, mActivePlayer2);
        ActivePlayer.savePlayerSharedPreferences(this, mActivePlayer3);
        ActivePlayer.savePlayerSharedPreferences(this, mActivePlayer4);

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mNewGameBroadcastReceiver);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            placeholderFragment = (PlaceholderFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, currentFragment);
            createLayout(placeholderFragment.getView());
            updateLayout(getCurrentActivePlayer());
            highlightActivePlayer(getCurrentActivePlayer());
            setActivePlayerBarColor(getCurrentActivePlayer().getPlayerColor());
            mViewPager.setCurrentItem(currentFragment, false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //menu.findItem(R.id.action_overview).setVisible(!drawerMain.isDrawerOpen());
        //menu.findItem(R.id.action_history).setVisible(!drawerMain.isDrawerOpen());
        return super.onPrepareOptionsMenu(menu);
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
            this.finish();

//            PlayersDataAccessObject playerDB = new PlayersDataAccessObject(this);
//            playerDB.open();
//            playerDB.createPlayer("Dezao");
//            playerDB.createPlayer("Macaco");
//            playerDB.createPlayer("Anthony");
//            playerDB.createPlayer("Marcos");
//            playerDB.close();
//
//            DecksDataAccessObject decksDb = new DecksDataAccessObject(this);
//            decksDb.open();
//            Deck deck1 = new Deck("Dezao", "Animar");
//            Deck deck2 = new Deck("Dezao", "Marchesa");
//            Deck deck3 = new Deck("Dezao", "Rato");
//            Deck deck4 = new Deck("Dezao", "Zur");
//            decksDb.createDeck(deck1);
//            decksDb.createDeck(deck2);
//            decksDb.createDeck(deck3);
//            decksDb.createDeck(deck4);
//
//            Deck deck5 = new Deck("Macaco", "Sidisi");
//            Deck deck6 = new Deck("Macaco", "Kaceto");
//            Deck deck7 = new Deck("Macaco", "Ezuri");
//            Deck deck8 = new Deck("Macaco", "Sidry");
//            decksDb.createDeck(deck5);
//            decksDb.createDeck(deck6);
//            decksDb.createDeck(deck7);
//            decksDb.createDeck(deck8);
//
//            Deck deck9 = new Deck("Anthony", "Good Stuff 1");
//            Deck deck10 = new Deck("Anthony", "Good Stuff 2");
//            Deck deck11 = new Deck("Anthony", "Good Stuff 3");
//            Deck deck12 = new Deck("Anthony", "Good Stuff 4");
//            decksDb.createDeck(deck9);
//            decksDb.createDeck(deck10);
//            decksDb.createDeck(deck11);
//            decksDb.createDeck(deck12);
//
//            Deck deck13 = new Deck("Marcos", "Milhoes1");
//            Deck deck14 = new Deck("Marcos", "Milhoes2");
//            Deck deck15 = new Deck("Marcos", "Milhoes4");
//            Deck deck16 = new Deck("Marcos", "Milhoes5");
//            decksDb.createDeck(deck13);
//            decksDb.createDeck(deck14);
//            decksDb.createDeck(deck15);
//            decksDb.createDeck(deck16);
//            decksDb.close();
        } else if (id == R.id.action_history) {
            startActivity(new Intent(this, HistoryActivity.class));
            this.finish();
//            RecordsDataAccessObject r = new RecordsDataAccessObject(this);
//            r.open();
//            List<Record> a = r.getAllRecords();
//            for (int i = 0; i < a.size(); i++) {
//                Log.d("dezao", "1: " + a.get(i).getFistPlace().getPlayerName() + " - " + a.get(i).getFistPlace().getDeckName());
//                Log.d("dezao", "2: " + a.get(i).getSecondPlace().getPlayerName() + " - " + a.get(i).getSecondPlace().getDeckName());
//                Log.d("dezao", "3: " + a.get(i).getThirdPlace().getPlayerName() + " - " + a.get(i).getThirdPlace().getDeckName());
//                Log.d("dezao", "4: " + a.get(i).getFourthPlace().getPlayerName() + " - " + a.get(i).getFourthPlace().getDeckName());
//                Log.d("dezao", "---------------------------------");
//
//            }
//            r.close();
        }/*else  if (id == R.id.action_add_player) {
            if (mSectionsPagerAdapter.getCount() < 4) {
                mSectionsPagerAdapter.setCount(mSectionsPagerAdapter.getCount() + 1);
                mSectionsPagerAdapter.notifyDataSetChanged();

                mViewPager.setCurrentItem(mSectionsPagerAdapter.getCount());

                getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).edit().putInt("NUM_PLAYERS", mSectionsPagerAdapter.getCount()).commit();
                Toast.makeText(this, "ActivePlayer " + mSectionsPagerAdapter.getCount() + " added!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Max == 4 Players", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.action_remove_player) {
            if (mSectionsPagerAdapter.getCount() > 2) {
                mSectionsPagerAdapter.setCount(mSectionsPagerAdapter.getCount() - 1);
                mSectionsPagerAdapter.notifyDataSetChanged();

                if (currentFragment + 1 > mSectionsPagerAdapter.getCount())
                    mViewPager.setCurrentItem(mSectionsPagerAdapter.getCount() - 1);

                getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).edit().putInt("NUM_PLAYERS", mSectionsPagerAdapter.getCount()).commit();
                Toast.makeText(this, "ActivePlayer " + (mSectionsPagerAdapter.getCount() + 1) + " removed!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Min == 2 Players", Toast.LENGTH_SHORT).show();
            }
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerMain.getDrawerToggle().syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerMain.getDrawerToggle().onConfigurationChanged(newConfig);
    }

    private void createLayout(View view) {
        if (view != null) {
            checkBox = (CheckBox) findViewById(R.id.checkBoxKeepScreenOn);
            mImageViewThrone = (ImageView) view.findViewById(R.id.imageViewThrone);

            mTextViewName = (TextView) view.findViewById(R.id.textViewName);
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

        }
    }

    private void updateLayout(ActivePlayer currentActivePlayer) {
        if (getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).getInt("SCREEN_ON", 0) == 1) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
            getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        if (currentActivePlayer.getPlayerColor()[0] == 0 || currentActivePlayer.getPlayerColor()[1] == 0)
            currentActivePlayer.setPlayerColor(getResources().getIntArray(R.array.edh_default));

        mImageViewThrone.setVisibility(View.INVISIBLE);
        if (getNumOfActivePlayers() == 4) {
            if (currentActivePlayer.getPlayerLife() >= mActivePlayer1.getPlayerLife() && currentActivePlayer.getPlayerLife() >= mActivePlayer2.getPlayerLife() && currentActivePlayer.getPlayerLife() >= mActivePlayer3.getPlayerLife() && currentActivePlayer.getPlayerLife() >= mActivePlayer4.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
        } else if (getNumOfActivePlayers() == 3) {
            if (currentActivePlayer.getPlayerLife() >= mActivePlayer1.getPlayerLife() && currentActivePlayer.getPlayerLife() >= mActivePlayer2.getPlayerLife() && currentActivePlayer.getPlayerLife() >= mActivePlayer3.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
        } else if (getNumOfActivePlayers() == 2) {
            if (currentActivePlayer.getPlayerLife() >= mActivePlayer1.getPlayerLife() && currentActivePlayer.getPlayerLife() >= mActivePlayer2.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
        }

        mTextViewName.setText(currentActivePlayer.getPlayerName());
        mTextViewLife.setText(currentActivePlayer.getPlayerLife() + "");
        mTextViewEDH1.setText(currentActivePlayer.getPlayerEDH1() + "");
        mTextViewEDH2.setText(currentActivePlayer.getPlayerEDH2() + "");
        mTextViewEDH3.setText(currentActivePlayer.getPlayerEDH3() + "");
        mTextViewEDH4.setText(currentActivePlayer.getPlayerEDH4() + "");
        mTextViewP1Name.setText(mActivePlayer1.getPlayerName());
        mTextViewP2Name.setText(mActivePlayer2.getPlayerName());
        mTextViewP3Name.setText(mActivePlayer3.getPlayerName());
        mTextViewP4Name.setText(mActivePlayer4.getPlayerName());

        mButtonLifePositive.getBackground().setColorFilter(getCurrentActivePlayer().getPlayerColor()[0], PorterDuff.Mode.SRC_IN);
        mButtonLifeNegative.getBackground().setColorFilter(getCurrentActivePlayer().getPlayerColor()[0], PorterDuff.Mode.SRC_IN);
        mButtonEDH1Positive.getBackground().setColorFilter(getCurrentActivePlayer().getPlayerColor()[1], PorterDuff.Mode.SRC_IN);
        mButtonEDH1Negative.getBackground().setColorFilter(getCurrentActivePlayer().getPlayerColor()[1], PorterDuff.Mode.SRC_IN);
        mButtonEDH2Positive.getBackground().setColorFilter(getCurrentActivePlayer().getPlayerColor()[1], PorterDuff.Mode.SRC_IN);
        mButtonEDH2Negative.getBackground().setColorFilter(getCurrentActivePlayer().getPlayerColor()[1], PorterDuff.Mode.SRC_IN);
        mButtonEDH3Positive.getBackground().setColorFilter(getCurrentActivePlayer().getPlayerColor()[1], PorterDuff.Mode.SRC_IN);
        mButtonEDH3Negative.getBackground().setColorFilter(getCurrentActivePlayer().getPlayerColor()[1], PorterDuff.Mode.SRC_IN);
        mButtonEDH4Positive.getBackground().setColorFilter(getCurrentActivePlayer().getPlayerColor()[1], PorterDuff.Mode.SRC_IN);
        mButtonEDH4Negative.getBackground().setColorFilter(getCurrentActivePlayer().getPlayerColor()[1], PorterDuff.Mode.SRC_IN);
    }

    private void highlightActivePlayer(ActivePlayer currentActivePlayer) {
        if (currentActivePlayer.getPlayerTag() == 1) {
            mTextViewP1Name.setTextColor(getCurrentActivePlayer().getPlayerColor()[1]);
            mTextViewP1Name.setTypeface(null, Typeface.BOLD);
        } else if (currentActivePlayer.getPlayerTag() == 2) {
            mTextViewP2Name.setTextColor(getCurrentActivePlayer().getPlayerColor()[1]);
            mTextViewP2Name.setTypeface(null, Typeface.BOLD);
        } else if (currentActivePlayer.getPlayerTag() == 3) {
            mTextViewP3Name.setTextColor(getCurrentActivePlayer().getPlayerColor()[1]);
            mTextViewP3Name.setTypeface(null, Typeface.BOLD);
        } else if (currentActivePlayer.getPlayerTag() == 4) {
            mTextViewP4Name.setTextColor(getCurrentActivePlayer().getPlayerColor()[1]);
            mTextViewP4Name.setTypeface(null, Typeface.BOLD);
        }
    }


    private void activePlayerLifeHistoryHandler() {
        Thread threadLife;
        final int playerTag;

        if (getCurrentActivePlayer().getPlayerTag() == 1) {
            threadLife = threadLife1;
            playerTag = 1;
        } else if (getCurrentActivePlayer().getPlayerTag() == 2) {
            threadLife = threadLife2;
            playerTag = 2;
        } else if (getCurrentActivePlayer().getPlayerTag() == 3) {
            threadLife = threadLife3;
            playerTag = 3;
        } else {
            threadLife = threadLife4;
            playerTag = 4;
        }

        if (threadLife != null)
            threadLife.interrupt();

        threadLife = new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            int latestSavedLife;
                            String latestSavedLifePreferences = getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).getString("PHL" + playerTag, "40");
                            if (latestSavedLifePreferences != null) {
                                String[] latestSavedLifeArray = latestSavedLifePreferences.split("_");
                                latestSavedLife = Integer.valueOf(latestSavedLifeArray[latestSavedLifeArray.length - 1]);
                            } else
                                latestSavedLife = getActivePlayerByTag(playerTag).getPlayerLife();

                            Thread.sleep(2000);

                            int currentLife = getActivePlayerByTag(playerTag).getPlayerLife();

                            if ((currentLife - latestSavedLife) != 0) {
                                String lifeToBeSaved = getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).getString("PHL" + playerTag, "40");
                                lifeToBeSaved = lifeToBeSaved + "_" + currentLife;
                                getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).edit().putString("PHL" + playerTag, lifeToBeSaved).commit();


                                // SAVE EDH DAMAGE
                                String latestSavedEDH;
                                String latestSavedEDHPreferences = getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).getString("PHEDH" + playerTag, "0@0@0@0");
                                if (latestSavedEDHPreferences != null) {
                                    String[] latestSavedLifeArray = latestSavedEDHPreferences.split("_");
                                    latestSavedEDH = latestSavedLifeArray[latestSavedLifeArray.length - 1];
                                } else
                                    latestSavedEDH = getActivePlayerByTag(playerTag).getPlayerEDH1() + "@" + getActivePlayerByTag(playerTag).getPlayerEDH2() + "@"
                                            + getActivePlayerByTag(playerTag).getPlayerEDH3() + "@" + getActivePlayerByTag(playerTag).getPlayerEDH4();

                                String currentEdh = getActivePlayerByTag(playerTag).getPlayerEDH1() + "@" + getActivePlayerByTag(playerTag).getPlayerEDH2() + "@"
                                        + getActivePlayerByTag(playerTag).getPlayerEDH3() + "@" + getActivePlayerByTag(playerTag).getPlayerEDH4();

                                if (!currentEdh.equalsIgnoreCase(latestSavedEDH)) {
                                    String edhToBeSaved = latestSavedEDHPreferences + "_" + currentEdh;
                                    getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).edit().putString("PHEDH" + playerTag, edhToBeSaved).commit();
                                    Log.d("dezao", "saved: " + edhToBeSaved);
                                } else {
                                    String edhToBeSaved = latestSavedEDHPreferences + "_" + currentEdh;
                                    getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).edit().putString("PHEDH" + playerTag, edhToBeSaved).commit();
                                    Log.d("dezao", "saved: " + edhToBeSaved);
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
            threadLife1 = threadLife;
        else if (getCurrentActivePlayer().getPlayerTag() == 2)
            threadLife2 = threadLife;
        else if (getCurrentActivePlayer().getPlayerTag() == 3)
            threadLife3 = threadLife;
        else
            threadLife4 = threadLife;
    }

    private void resetHistoryLife() {
        getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).edit().putString("PHL" + 1, "40").commit();
        getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).edit().putString("PHL" + 2, "40").commit();
        getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).edit().putString("PHL" + 3, "40").commit();
        getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).edit().putString("PHL" + 4, "40").commit();

        getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).edit().putString("PHEDH" + 1, "0@0@0@0").commit();
        getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).edit().putString("PHEDH" + 2, "0@0@0@0").commit();
        getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).edit().putString("PHEDH" + 3, "0@0@0@0").commit();
        getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).edit().putString("PHEDH" + 4, "0@0@0@0").commit();
    }

    public ActivePlayer getCurrentActivePlayer() {
        if (currentFragment == 0) {
            return mActivePlayer1;
        } else if (currentFragment == 1) {
            return mActivePlayer2;
        } else if (currentFragment == 2) {
            return mActivePlayer3;
        } else if (currentFragment == 3) {
            return mActivePlayer4;
        }
        return new ActivePlayer();
    }

    public void onClickActivePlayerName(View v) {
        if (!drawerMain.isDrawerOpen())
            createPlayerNameDialog(v);
    }

    public void onClickActivePlayerLife(View v) {
        if (!drawerMain.isDrawerOpen())
            createActivePlayerLifeDialog(v);
    }

    public void onClickActivePlayerLifePlusButton(View view) {
        if (getCurrentActivePlayer().getPlayerLife() < 99) {
            getCurrentActivePlayer().setPlayerLife(getCurrentActivePlayer().getPlayerLife() + 1);
            setActivePlayerLife(String.valueOf(getCurrentActivePlayer().getPlayerLife()));

            activePlayerLifeHistoryHandler();
            updateLayout(getCurrentActivePlayer());
        }
    }

    public void onClickActivePlayerLifeMinusButton(View view) {
        if (getCurrentActivePlayer().getPlayerLife() > -99) {
            getCurrentActivePlayer().setPlayerLife(getCurrentActivePlayer().getPlayerLife() - 1);
            setActivePlayerLife(String.valueOf(getCurrentActivePlayer().getPlayerLife()));

            activePlayerLifeHistoryHandler();
            updateLayout(getCurrentActivePlayer());
        }
    }

    public void onClickActivePlayerEDH1PlusButton(View view) {
        if (getCurrentActivePlayer().getPlayerEDH1() < 21) {
            getCurrentActivePlayer().setPlayerEDH1(getCurrentActivePlayer().getPlayerEDH1() + 1);
            setActivePlayerEDH1(String.valueOf(getCurrentActivePlayer().getPlayerEDH1()));

            getCurrentActivePlayer().setPlayerLife(getCurrentActivePlayer().getPlayerLife() - 1);
            setActivePlayerLife(String.valueOf(getCurrentActivePlayer().getPlayerLife()));

            activePlayerLifeHistoryHandler();
        }
    }

    public void onClickActivePlayerEDH1MinusButton(View view) {
        if (getCurrentActivePlayer().getPlayerEDH1() > 0) {
            getCurrentActivePlayer().setPlayerEDH1(getCurrentActivePlayer().getPlayerEDH1() - 1);
            setActivePlayerEDH1(String.valueOf(getCurrentActivePlayer().getPlayerEDH1()));

            getCurrentActivePlayer().setPlayerLife(getCurrentActivePlayer().getPlayerLife() + 1);
            setActivePlayerLife(String.valueOf(getCurrentActivePlayer().getPlayerLife()));

            activePlayerLifeHistoryHandler();
        }
    }

    public void onClickActivePlayerEDH2PlusButton(View view) {
        if (getCurrentActivePlayer().getPlayerEDH2() < 21) {
            getCurrentActivePlayer().setPlayerEDH2(getCurrentActivePlayer().getPlayerEDH2() + 1);
            setActivePlayerEDH2(String.valueOf(getCurrentActivePlayer().getPlayerEDH2()));

            getCurrentActivePlayer().setPlayerLife(getCurrentActivePlayer().getPlayerLife() - 1);
            setActivePlayerLife(String.valueOf(getCurrentActivePlayer().getPlayerLife()));

            activePlayerLifeHistoryHandler();
        }
    }

    public void onClickActivePlayerEDH2MinusButton(View view) {
        if (getCurrentActivePlayer().getPlayerEDH2() > 0) {
            getCurrentActivePlayer().setPlayerEDH2(getCurrentActivePlayer().getPlayerEDH2() - 1);
            setActivePlayerEDH2(String.valueOf(getCurrentActivePlayer().getPlayerEDH2()));

            getCurrentActivePlayer().setPlayerLife(getCurrentActivePlayer().getPlayerLife() + 1);
            setActivePlayerLife(String.valueOf(getCurrentActivePlayer().getPlayerLife()));

            activePlayerLifeHistoryHandler();
        }
    }

    public void onClickActivePlayerEDH3PlusButton(View view) {
        if (getCurrentActivePlayer().getPlayerEDH3() < 21) {
            getCurrentActivePlayer().setPlayerEDH3(getCurrentActivePlayer().getPlayerEDH3() + 1);
            setActivePlayerEDH3(String.valueOf(getCurrentActivePlayer().getPlayerEDH3()));

            getCurrentActivePlayer().setPlayerLife(getCurrentActivePlayer().getPlayerLife() - 1);
            setActivePlayerLife(String.valueOf(getCurrentActivePlayer().getPlayerLife()));

            activePlayerLifeHistoryHandler();
        }
    }

    public void onClickActivePlayerEDH3MinusButton(View view) {
        if (getCurrentActivePlayer().getPlayerEDH3() > 0) {
            getCurrentActivePlayer().setPlayerEDH3(getCurrentActivePlayer().getPlayerEDH3() - 1);
            setActivePlayerEDH3(String.valueOf(getCurrentActivePlayer().getPlayerEDH3()));

            getCurrentActivePlayer().setPlayerLife(getCurrentActivePlayer().getPlayerLife() + 1);
            setActivePlayerLife(String.valueOf(getCurrentActivePlayer().getPlayerLife()));

            activePlayerLifeHistoryHandler();
        }
    }

    public void onClickActivePlayerEDH4PlusButton(View view) {
        if (getCurrentActivePlayer().getPlayerEDH4() < 21) {
            getCurrentActivePlayer().setPlayerEDH4(getCurrentActivePlayer().getPlayerEDH4() + 1);
            setActivePlayerEDH4(String.valueOf(getCurrentActivePlayer().getPlayerEDH4()));

            getCurrentActivePlayer().setPlayerLife(getCurrentActivePlayer().getPlayerLife() - 1);
            setActivePlayerLife(String.valueOf(getCurrentActivePlayer().getPlayerLife()));

            activePlayerLifeHistoryHandler();
        }
    }

    public void onClickActivePlayerEDH4MinusButton(View view) {
        if (getCurrentActivePlayer().getPlayerEDH4() > 0) {
            getCurrentActivePlayer().setPlayerEDH4(getCurrentActivePlayer().getPlayerEDH4() - 1);
            setActivePlayerEDH4(String.valueOf(getCurrentActivePlayer().getPlayerEDH4()));

            getCurrentActivePlayer().setPlayerLife(getCurrentActivePlayer().getPlayerLife() + 1);
            setActivePlayerLife(String.valueOf(getCurrentActivePlayer().getPlayerLife()));

            activePlayerLifeHistoryHandler();
        }
    }

    private void newGame(String option) {
        if (option.equalsIgnoreCase(DrawerMain.BROADCAST_MESSAGE_NEWGAME_OPTION_YES)) {
            resetHistoryLife();

            mActivePlayer1 = new ActivePlayer(mActivePlayer1.getPlayerName(), 40, 0, 0, 0, 0, mActivePlayer1.getPlayerColor(), 1);
            mActivePlayer2 = new ActivePlayer(mActivePlayer2.getPlayerName(), 40, 0, 0, 0, 0, mActivePlayer2.getPlayerColor(), 2);
            mActivePlayer3 = new ActivePlayer(mActivePlayer3.getPlayerName(), 40, 0, 0, 0, 0, mActivePlayer3.getPlayerColor(), 3);
            mActivePlayer4 = new ActivePlayer(mActivePlayer4.getPlayerName(), 40, 0, 0, 0, 0, mActivePlayer4.getPlayerColor(), 4);

            updateLayout(getCurrentActivePlayer());
        } else if (option.equalsIgnoreCase(DrawerMain.BROADCAST_MESSAGE_NEWGAME_OPTION_NO)) {
            resetHistoryLife();

            int[] defaultColor = getResources().getIntArray(R.array.edh_default);
            mActivePlayer1 = new ActivePlayer("ActivePlayer 1", 40, 0, 0, 0, 0, defaultColor, 1);
            mActivePlayer2 = new ActivePlayer("ActivePlayer 2", 40, 0, 0, 0, 0, defaultColor, 2);
            mActivePlayer3 = new ActivePlayer("ActivePlayer 3", 40, 0, 0, 0, 0, defaultColor, 3);
            mActivePlayer4 = new ActivePlayer("ActivePlayer 4", 40, 0, 0, 0, 0, defaultColor, 4);

            mSectionsPagerAdapter.setCount(4);
            mSectionsPagerAdapter.notifyDataSetChanged();
            getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).edit().putInt("NUM_PLAYERS", mSectionsPagerAdapter.getCount()).commit();

            updateLayout(getCurrentActivePlayer());
        }
    }

    public void onClickKeepScreenOn(View view) {

        if (!checkBox.isChecked()) {
            getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).edit().putInt("SCREEN_ON", 0).commit();
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).edit().putInt("SCREEN_ON", 1).commit();
        }
    }

    public void createActivePlayerLifeDialog(View view) {
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
                                if (Integer.valueOf(tempLife) < -99)
                                    tempLife = "-99";
                                if (Integer.valueOf(tempLife) > 99)
                                    tempLife = "99";
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
                new DialogInterface.OnClickListener()

                {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }

        );
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        alertDialog.show();
    }

    public void createPlayerNameDialog(View view) {
        View playerNameView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_player_name, null);
        final EditText userInput = (EditText) playerNameView.findViewById(R.id.editTextPlayerName);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(playerNameView);
        alertDialogBuilder.setTitle(mTextViewName.getText() + "");
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String tempName = userInput.getText().toString();
                        if (!tempName.equalsIgnoreCase("")) {
                            getCurrentActivePlayer().setPlayerName(tempName);
                            setActivePlayerName(getCurrentActivePlayer().getPlayerName());
                        }
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
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
            }
        });
        colorCalendar.show(getFragmentManager(), "cal");
    }

    public void setActivePlayerName(String playerName) {
        mTextViewName.setText(playerName);

        mTextViewP1Name.setText(mActivePlayer1.getPlayerName());
        mTextViewP2Name.setText(mActivePlayer2.getPlayerName());
        mTextViewP3Name.setText(mActivePlayer3.getPlayerName());
        mTextViewP4Name.setText(mActivePlayer4.getPlayerName());
    }

    public void setActivePlayerLife(String playerLife) {
        mTextViewLife.setText(playerLife);
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

    private ActivePlayer getActivePlayerByTag(int playerTag) {
        if (mActivePlayer1.getPlayerTag() == playerTag)
            return mActivePlayer1;
        else if (mActivePlayer2.getPlayerTag() == playerTag)
            return mActivePlayer2;
        else if (mActivePlayer3.getPlayerTag() == playerTag)
            return mActivePlayer3;
        else
            return mActivePlayer4;
    }

    private int getNumOfActivePlayers() {
        return mSectionsPagerAdapter.getCount();
    }
}
