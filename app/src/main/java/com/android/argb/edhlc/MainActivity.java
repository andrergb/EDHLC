package com.android.argb.edhlc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.argb.edhlc.colorpicker.ColorPickerDialog;
import com.android.argb.edhlc.colorpicker.ColorPickerSwatch;

public class MainActivity extends ActionBarActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    static Player mPlayer1;
    static Player mPlayer2;
    static Player mPlayer3;
    static Player mPlayer4;

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

    private static Thread threadLife1;
    private static Thread threadLife2;
    private static Thread threadLife3;
    private static Thread threadLife4;

    private PlaceholderFragment placeholderFragment;

    private int activeFragment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setPlayerBarColor(getResources().getIntArray(R.array.edh_default));

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.setCount(getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).getInt("NUM_PLAYERS", 4));
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                activeFragment = position;
                getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).edit().putInt("CURRENT_PAGE", activeFragment).commit();
                placeholderFragment = (PlaceholderFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, position);
                createLayout(placeholderFragment.getView());
                updateLayout(getActivePlayer());
                highlightActivePlayer(getActivePlayer());
                setPlayerBarColor(getActivePlayer().getPlayerColor());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        activeFragment = getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).getInt("CURRENT_PAGE", 0);
        if (activeFragment + 1 > mSectionsPagerAdapter.getCount())
            activeFragment = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("TAG"))
            activeFragment = Integer.valueOf(extras.getString("TAG")) - 1;

        mPlayer1 = Player.loadPlayerSharedPreferences(this, 1);
        mPlayer2 = Player.loadPlayerSharedPreferences(this, 2);
        mPlayer3 = Player.loadPlayerSharedPreferences(this, 3);
        mPlayer4 = Player.loadPlayerSharedPreferences(this, 4);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).edit().putInt("CURRENT_PAGE", activeFragment).commit();
        Player.savePlayerSharedPreferences(this, mPlayer1);
        Player.savePlayerSharedPreferences(this, mPlayer2);
        Player.savePlayerSharedPreferences(this, mPlayer3);
        Player.savePlayerSharedPreferences(this, mPlayer4);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            placeholderFragment = (PlaceholderFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, activeFragment);
            createLayout(placeholderFragment.getView());
            updateLayout(getActivePlayer());
            highlightActivePlayer(getActivePlayer());
            setPlayerBarColor(getActivePlayer().getPlayerColor());
            mViewPager.setCurrentItem(activeFragment, false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if (getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).getInt("SCREEN_ON", 0) == 1) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            menu.findItem(R.id.action_screen).setChecked(true);
        } else {
            menu.findItem(R.id.action_screen).setChecked(false);
            getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_add_player);
        menuItem.setTitle("Add Player " + (mSectionsPagerAdapter.getCount() + 1));
        if (mSectionsPagerAdapter.getCount() == 4) {
            menuItem.setEnabled(false);
        } else {
            menuItem.setEnabled(true);
        }

        menuItem = menu.findItem(R.id.action_remove_player);
        menuItem.setTitle("Remove Player " + (mSectionsPagerAdapter.getCount()));
        if (mSectionsPagerAdapter.getCount() < 3) {
            menuItem.setEnabled(false);
        } else {
            menuItem.setEnabled(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_newGame) {
            createNewGameDialog(placeholderFragment.getView());
        } else if (id == R.id.action_about) {
            createAboutDialog(placeholderFragment.getView());
        } else if (id == R.id.action_screen) {
            if (item.isChecked()) {
                item.setChecked(false);
                getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).edit().putInt("SCREEN_ON", 0).commit();
            } else {
                item.setChecked(true);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).edit().putInt("SCREEN_ON", 1).commit();
            }
        } else if (id == R.id.action_overview) {
            startActivity(new Intent(this, OverviewActivity.class));
            this.finish();
        } else if (id == R.id.action_playerColor) {
            createColorPickDialog();
        } else if (id == R.id.action_history) {
            startActivity(new Intent(this, HistoryActivity.class));
            this.finish();
        } else if (id == R.id.action_add_player) {
            if (mSectionsPagerAdapter.getCount() < 4) {
                mSectionsPagerAdapter.setCount(mSectionsPagerAdapter.getCount() + 1);
                mSectionsPagerAdapter.notifyDataSetChanged();

                mViewPager.setCurrentItem(mSectionsPagerAdapter.getCount());

                getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).edit().putInt("NUM_PLAYERS", mSectionsPagerAdapter.getCount()).commit();
                Toast.makeText(this, "Player " + mSectionsPagerAdapter.getCount() + " added!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Max == 4 Players", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.action_remove_player) {
            if (mSectionsPagerAdapter.getCount() > 2) {
                mSectionsPagerAdapter.setCount(mSectionsPagerAdapter.getCount() - 1);
                mSectionsPagerAdapter.notifyDataSetChanged();

                if (activeFragment + 1 > mSectionsPagerAdapter.getCount())
                    mViewPager.setCurrentItem(mSectionsPagerAdapter.getCount() - 1);

                getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).edit().putInt("NUM_PLAYERS", mSectionsPagerAdapter.getCount()).commit();
                Toast.makeText(this, "Player " + (mSectionsPagerAdapter.getCount() + 1) + " removed!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Min == 2 Players", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void createLayout(View view) {
        if (view != null) {
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

    private void updateLayout(Player activePlayer) {
        if (activePlayer.getPlayerColor()[0] == 0 || activePlayer.getPlayerColor()[1] == 0)
            activePlayer.setPlayerColor(getResources().getIntArray(R.array.edh_default));


        mImageViewThrone.setVisibility(View.INVISIBLE);
        if (getNumOfPlayerActive() == 4) {
            if (activePlayer.getPlayerLife() >= mPlayer1.getPlayerLife() && activePlayer.getPlayerLife() >= mPlayer2.getPlayerLife() && activePlayer.getPlayerLife() >= mPlayer3.getPlayerLife() && activePlayer.getPlayerLife() >= mPlayer4.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
        } else if (getNumOfPlayerActive() == 3) {
            if (activePlayer.getPlayerLife() >= mPlayer1.getPlayerLife() && activePlayer.getPlayerLife() >= mPlayer2.getPlayerLife() && activePlayer.getPlayerLife() >= mPlayer3.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
        } else if (getNumOfPlayerActive() == 2) {
            if (activePlayer.getPlayerLife() >= mPlayer1.getPlayerLife() && activePlayer.getPlayerLife() >= mPlayer2.getPlayerLife())
                mImageViewThrone.setVisibility(View.VISIBLE);
        }


        mTextViewName.setText(activePlayer.getPlayerName());
        mTextViewLife.setText(activePlayer.getPlayerLife() + "");
        mTextViewEDH1.setText(activePlayer.getPlayerEDH1() + "");
        mTextViewEDH2.setText(activePlayer.getPlayerEDH2() + "");
        mTextViewEDH3.setText(activePlayer.getPlayerEDH3() + "");
        mTextViewEDH4.setText(activePlayer.getPlayerEDH4() + "");
        mTextViewP1Name.setText(mPlayer1.getPlayerName());
        mTextViewP2Name.setText(mPlayer2.getPlayerName());
        mTextViewP3Name.setText(mPlayer3.getPlayerName());
        mTextViewP4Name.setText(mPlayer4.getPlayerName());

        mButtonLifePositive.getBackground().setColorFilter(getActivePlayer().getPlayerColor()[0], PorterDuff.Mode.SRC_IN);
        mButtonLifeNegative.getBackground().setColorFilter(getActivePlayer().getPlayerColor()[0], PorterDuff.Mode.SRC_IN);
        mButtonEDH1Positive.getBackground().setColorFilter(getActivePlayer().getPlayerColor()[1], PorterDuff.Mode.SRC_IN);
        mButtonEDH1Negative.getBackground().setColorFilter(getActivePlayer().getPlayerColor()[1], PorterDuff.Mode.SRC_IN);
        mButtonEDH2Positive.getBackground().setColorFilter(getActivePlayer().getPlayerColor()[1], PorterDuff.Mode.SRC_IN);
        mButtonEDH2Negative.getBackground().setColorFilter(getActivePlayer().getPlayerColor()[1], PorterDuff.Mode.SRC_IN);
        mButtonEDH3Positive.getBackground().setColorFilter(getActivePlayer().getPlayerColor()[1], PorterDuff.Mode.SRC_IN);
        mButtonEDH3Negative.getBackground().setColorFilter(getActivePlayer().getPlayerColor()[1], PorterDuff.Mode.SRC_IN);
        mButtonEDH4Positive.getBackground().setColorFilter(getActivePlayer().getPlayerColor()[1], PorterDuff.Mode.SRC_IN);
        mButtonEDH4Negative.getBackground().setColorFilter(getActivePlayer().getPlayerColor()[1], PorterDuff.Mode.SRC_IN);
    }

    private void highlightActivePlayer(Player activePlayer) {
        if (activePlayer.getPlayerTag() == 1) {
            mTextViewP1Name.setTextColor(getActivePlayer().getPlayerColor()[1]);
            mTextViewP1Name.setTypeface(null, Typeface.BOLD);
        } else if (activePlayer.getPlayerTag() == 2) {
            mTextViewP2Name.setTextColor(getActivePlayer().getPlayerColor()[1]);
            mTextViewP2Name.setTypeface(null, Typeface.BOLD);
        } else if (activePlayer.getPlayerTag() == 3) {
            mTextViewP3Name.setTextColor(getActivePlayer().getPlayerColor()[1]);
            mTextViewP3Name.setTypeface(null, Typeface.BOLD);
        } else if (activePlayer.getPlayerTag() == 4) {
            mTextViewP4Name.setTextColor(getActivePlayer().getPlayerColor()[1]);
            mTextViewP4Name.setTypeface(null, Typeface.BOLD);
        }
    }


    private void playerLifeHistoryHandler() {
        Thread threadLife;
        final int playerTag;

        if (getActivePlayer().getPlayerTag() == 1) {
            threadLife = threadLife1;
            playerTag = 1;
        } else if (getActivePlayer().getPlayerTag() == 2) {
            threadLife = threadLife2;
            playerTag = 2;
        } else if (getActivePlayer().getPlayerTag() == 3) {
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
                            String latestSavedLifePreferences = getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).getString("PHL" + playerTag, "40");
                            if (latestSavedLifePreferences != null) {
                                String[] latestSavedLifeArray = latestSavedLifePreferences.split("_");
                                latestSavedLife = Integer.valueOf(latestSavedLifeArray[latestSavedLifeArray.length - 1]);
                            } else
                                latestSavedLife = getPlayerByTag(playerTag).getPlayerLife();

                            Thread.sleep(2000);

                            int currentLife = getPlayerByTag(playerTag).getPlayerLife();

                            if ((currentLife - latestSavedLife) != 0) {
                                String lifeToBeSaved = getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).getString("PHL" + playerTag, "40");
                                lifeToBeSaved = lifeToBeSaved + "_" + currentLife;
                                getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).edit().putString("PHL" + playerTag, lifeToBeSaved).commit();


                                // SAVE EDH DAMAGE
                                String latestSavedEDH;
                                String latestSavedEDHPreferences = getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).getString("PHEDH" + playerTag, "0@0@0@0");
                                if (latestSavedEDHPreferences != null) {
                                    String[] latestSavedLifeArray = latestSavedEDHPreferences.split("_");
                                    latestSavedEDH = latestSavedLifeArray[latestSavedLifeArray.length - 1];
                                } else
                                    latestSavedEDH = getPlayerByTag(playerTag).getPlayerEDH1() + "@" + getPlayerByTag(playerTag).getPlayerEDH2() + "@"
                                            + getPlayerByTag(playerTag).getPlayerEDH3() + "@" + getPlayerByTag(playerTag).getPlayerEDH4();

                                String currentEdh = getPlayerByTag(playerTag).getPlayerEDH1() + "@" + getPlayerByTag(playerTag).getPlayerEDH2() + "@"
                                        + getPlayerByTag(playerTag).getPlayerEDH3() + "@" + getPlayerByTag(playerTag).getPlayerEDH4();

                                if (!currentEdh.equalsIgnoreCase(latestSavedEDH)) {
                                    String edhToBeSaved = latestSavedEDHPreferences + "_" + currentEdh;
                                    getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).edit().putString("PHEDH" + playerTag, edhToBeSaved).commit();
                                    Log.d("dezao", "saved: " + edhToBeSaved);
                                } else {
                                    String edhToBeSaved = latestSavedEDHPreferences + "_" + currentEdh;
                                    getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).edit().putString("PHEDH" + playerTag, edhToBeSaved).commit();
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

        if (getActivePlayer().getPlayerTag() == 1)
            threadLife1 = threadLife;
        else if (getActivePlayer().getPlayerTag() == 2)
            threadLife2 = threadLife;
        else if (getActivePlayer().getPlayerTag() == 3)
            threadLife3 = threadLife;
        else
            threadLife4 = threadLife;
    }

    private void resetHistoryLife() {
        getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).edit().putString("PHL" + 1, "40").commit();
        getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).edit().putString("PHL" + 2, "40").commit();
        getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).edit().putString("PHL" + 3, "40").commit();
        getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).edit().putString("PHL" + 4, "40").commit();

        getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).edit().putString("PHEDH" + 1, "0@0@0@0").commit();
        getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).edit().putString("PHEDH" + 2, "0@0@0@0").commit();
        getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).edit().putString("PHEDH" + 3, "0@0@0@0").commit();
        getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).edit().putString("PHEDH" + 4, "0@0@0@0").commit();
    }

    public Player getActivePlayer() {
        if (activeFragment == 0) {
            return mPlayer1;
        } else if (activeFragment == 1) {
            return mPlayer2;
        } else if (activeFragment == 2) {
            return mPlayer3;
        } else if (activeFragment == 3) {
            return mPlayer4;
        }
        return new Player();
    }

    public void onClickPlayerName(View v) {
        createPlayerNameDialog(v);
    }

    public void onClickPlayerLife(View v) {
        createPlayerLifeDialog(v);
    }


    public void onClickPlayerLifePlusButton(View view) {
        if (getActivePlayer().getPlayerLife() < 99) {
            getActivePlayer().setPlayerLife(getActivePlayer().getPlayerLife() + 1);
            setPlayerLife(String.valueOf(getActivePlayer().getPlayerLife()));

            playerLifeHistoryHandler();
            updateLayout(getActivePlayer());
        }
    }


    public void onClickPlayerLifeMinusButton(View view) {
        if (getActivePlayer().getPlayerLife() > -99) {
            getActivePlayer().setPlayerLife(getActivePlayer().getPlayerLife() - 1);
            setPlayerLife(String.valueOf(getActivePlayer().getPlayerLife()));

            playerLifeHistoryHandler();
            updateLayout(getActivePlayer());
        }
    }

    public void onClickPlayerEDH1PlusButton(View view) {
        if (getActivePlayer().getPlayerEDH1() < 21) {
            getActivePlayer().setPlayerEDH1(getActivePlayer().getPlayerEDH1() + 1);
            setPlayerEDH1(String.valueOf(getActivePlayer().getPlayerEDH1()));

            getActivePlayer().setPlayerLife(getActivePlayer().getPlayerLife() - 1);
            setPlayerLife(String.valueOf(getActivePlayer().getPlayerLife()));

            playerLifeHistoryHandler();
        }
    }

    public void onClickPlayerEDH1MinusButton(View view) {
        if (getActivePlayer().getPlayerEDH1() > 0) {
            getActivePlayer().setPlayerEDH1(getActivePlayer().getPlayerEDH1() - 1);
            setPlayerEDH1(String.valueOf(getActivePlayer().getPlayerEDH1()));

            getActivePlayer().setPlayerLife(getActivePlayer().getPlayerLife() + 1);
            setPlayerLife(String.valueOf(getActivePlayer().getPlayerLife()));

            playerLifeHistoryHandler();
        }
    }

    public void onClickPlayerEDH2PlusButton(View view) {
        if (getActivePlayer().getPlayerEDH2() < 21) {
            getActivePlayer().setPlayerEDH2(getActivePlayer().getPlayerEDH2() + 1);
            setPlayerEDH2(String.valueOf(getActivePlayer().getPlayerEDH2()));

            getActivePlayer().setPlayerLife(getActivePlayer().getPlayerLife() - 1);
            setPlayerLife(String.valueOf(getActivePlayer().getPlayerLife()));

            playerLifeHistoryHandler();
        }
    }

    public void onClickPlayerEDH2MinusButton(View view) {
        if (getActivePlayer().getPlayerEDH2() > 0) {
            getActivePlayer().setPlayerEDH2(getActivePlayer().getPlayerEDH2() - 1);
            setPlayerEDH2(String.valueOf(getActivePlayer().getPlayerEDH2()));

            getActivePlayer().setPlayerLife(getActivePlayer().getPlayerLife() + 1);
            setPlayerLife(String.valueOf(getActivePlayer().getPlayerLife()));

            playerLifeHistoryHandler();
        }
    }

    public void onClickPlayerEDH3PlusButton(View view) {
        if (getActivePlayer().getPlayerEDH3() < 21) {
            getActivePlayer().setPlayerEDH3(getActivePlayer().getPlayerEDH3() + 1);
            setPlayerEDH3(String.valueOf(getActivePlayer().getPlayerEDH3()));

            getActivePlayer().setPlayerLife(getActivePlayer().getPlayerLife() - 1);
            setPlayerLife(String.valueOf(getActivePlayer().getPlayerLife()));

            playerLifeHistoryHandler();
        }
    }

    public void onClickPlayerEDH3MinusButton(View view) {
        if (getActivePlayer().getPlayerEDH3() > 0) {
            getActivePlayer().setPlayerEDH3(getActivePlayer().getPlayerEDH3() - 1);
            setPlayerEDH3(String.valueOf(getActivePlayer().getPlayerEDH3()));

            getActivePlayer().setPlayerLife(getActivePlayer().getPlayerLife() + 1);
            setPlayerLife(String.valueOf(getActivePlayer().getPlayerLife()));

            playerLifeHistoryHandler();
        }
    }

    public void onClickPlayerEDH4PlusButton(View view) {
        if (getActivePlayer().getPlayerEDH4() < 21) {
            getActivePlayer().setPlayerEDH4(getActivePlayer().getPlayerEDH4() + 1);
            setPlayerEDH4(String.valueOf(getActivePlayer().getPlayerEDH4()));

            getActivePlayer().setPlayerLife(getActivePlayer().getPlayerLife() - 1);
            setPlayerLife(String.valueOf(getActivePlayer().getPlayerLife()));

            playerLifeHistoryHandler();
        }
    }

    public void onClickPlayerEDH4MinusButton(View view) {
        if (getActivePlayer().getPlayerEDH4() > 0) {
            getActivePlayer().setPlayerEDH4(getActivePlayer().getPlayerEDH4() - 1);
            setPlayerEDH4(String.valueOf(getActivePlayer().getPlayerEDH4()));

            getActivePlayer().setPlayerLife(getActivePlayer().getPlayerLife() + 1);
            setPlayerLife(String.valueOf(getActivePlayer().getPlayerLife()));

            playerLifeHistoryHandler();
        }
    }

    public void createNewGameDialog(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setTitle("New Game");
        alertDialogBuilder.setMessage("Do you want to keep the current names?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        resetHistoryLife();

                        mPlayer1 = new Player(mPlayer1.getPlayerName(), 40, 0, 0, 0, 0, mPlayer1.getPlayerColor(), 1);
                        mPlayer2 = new Player(mPlayer2.getPlayerName(), 40, 0, 0, 0, 0, mPlayer2.getPlayerColor(), 2);
                        mPlayer3 = new Player(mPlayer3.getPlayerName(), 40, 0, 0, 0, 0, mPlayer3.getPlayerColor(), 3);
                        mPlayer4 = new Player(mPlayer4.getPlayerName(), 40, 0, 0, 0, 0, mPlayer4.getPlayerColor(), 4);

                        updateLayout(getActivePlayer());
                    }
                });
        alertDialogBuilder.setNeutralButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        resetHistoryLife();

                        int[] defaultColor = getResources().getIntArray(R.array.edh_default);
                        mPlayer1 = new Player("Player 1", 40, 0, 0, 0, 0, defaultColor, 1);
                        mPlayer2 = new Player("Player 2", 40, 0, 0, 0, 0, defaultColor, 2);
                        mPlayer3 = new Player("Player 3", 40, 0, 0, 0, 0, defaultColor, 3);
                        mPlayer4 = new Player("Player 4", 40, 0, 0, 0, 0, defaultColor, 4);

                        mSectionsPagerAdapter.setCount(4);
                        mSectionsPagerAdapter.notifyDataSetChanged();
                        getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).edit().putInt("NUM_PLAYERS", mSectionsPagerAdapter.getCount()).commit();

                        updateLayout(getActivePlayer());
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void createPlayerLifeDialog(View view) {
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
                                getActivePlayer().setPlayerLife(Integer.valueOf(tempLife));
                                setPlayerLife(String.valueOf(getActivePlayer().getPlayerLife()));

                                playerLifeHistoryHandler();
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
        alertDialog.getWindow().

                setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

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
                            getActivePlayer().setPlayerName(tempName);
                            setPlayerName(getActivePlayer().getPlayerName());
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

    private void createAboutDialog(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setTitle("About " + getResources().getString(R.string.app_name));
        String message;
        try {
            message = "\nVersion: " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            message = "";
        }
        alertDialogBuilder.setMessage("Created by ARGB" + message);
        alertDialogBuilder.setPositiveButton("Ok",
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
        ColorPickerDialog colorCalendar = ColorPickerDialog.newInstance(R.string.color_picker_default_title, mColor, getActivePlayer().getPlayerColor()[0], 5, ColorPickerDialog.SIZE_SMALL);
        colorCalendar.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                int color_dark = color;
                for (int i = 0; i < mColor.length; i++)
                    if (mColor[i] == color)
                        color_dark = mColor_dark[i];
                getActivePlayer().setPlayerColor(new int[]{color, color_dark});
            }
        });
        colorCalendar.show(getFragmentManager(), "cal");
    }

    public void setPlayerName(String playerName) {
        mTextViewName.setText(playerName);

        mTextViewP1Name.setText(mPlayer1.getPlayerName());
        mTextViewP2Name.setText(mPlayer2.getPlayerName());
        mTextViewP3Name.setText(mPlayer3.getPlayerName());
        mTextViewP4Name.setText(mPlayer4.getPlayerName());
    }

    public void setPlayerLife(String playerLife) {
        mTextViewLife.setText(playerLife);
    }

    public void setPlayerEDH1(String edh) {
        mTextViewEDH1.setText(edh);
    }

    public void setPlayerEDH2(String edh) {
        mTextViewEDH2.setText(edh);
    }

    public void setPlayerEDH3(String edh) {
        mTextViewEDH3.setText(edh);
    }

    public void setPlayerEDH4(String edh) {
        mTextViewEDH4.setText(edh);
    }

    public void setPlayerBarColor(int[] colors) {
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

    private Player getPlayerByTag(int playerTag) {
        if (mPlayer1.getPlayerTag() == playerTag)
            return mPlayer1;
        else if (mPlayer2.getPlayerTag() == playerTag)
            return mPlayer2;
        else if (mPlayer3.getPlayerTag() == playerTag)
            return mPlayer3;
        else
            return mPlayer4;
    }

    private int getNumOfPlayerActive() {
        return mSectionsPagerAdapter.getCount();
    }

}
