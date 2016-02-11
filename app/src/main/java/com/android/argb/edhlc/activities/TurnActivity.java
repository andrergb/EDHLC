package com.android.argb.edhlc.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.TextView;

import com.android.argb.edhlc.Constants;
import com.android.argb.edhlc.R;
import com.android.argb.edhlc.TurnSectionsPagerAdapter;
import com.android.argb.edhlc.objects.ActivePlayer;

public class TurnActivity extends ActionBarActivity {

    TurnSectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    private int currentFragment = 0;

    private TextView textViewPhase1;
    private TextView textViewPhase2;
    private TextView textViewPhase3;
    private TextView textViewPhase4;
    private TextView textViewPhase5;

    private long countUpTotal = 0;
    private long countUpPartial = 0;
    private Chronometer chronometerTotal;
    private Chronometer chronometerPartial;
    private TextView textViewTimerTotal;
    private TextView textViewTimerPartial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.secondary_color));
        }

        mSectionsPagerAdapter = new TurnSectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                saveTimer(currentFragment);

                currentFragment = position;
                getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.CURRENT_PAGE_TURN, currentFragment).commit();

                createLayout();
                startTimer();
                updateTimer();
                highlightCurrentTurn();
            }
        });
        getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putLong(Constants.PHASE1_TIMER, 0).commit();
        getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putLong(Constants.PHASE2_TIMER, 0).commit();
        getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putLong(Constants.PHASE3_TIMER, 0).commit();
        getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putLong(Constants.PHASE4_TIMER, 0).commit();
        getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putLong(Constants.PHASE5_TIMER, 0).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        currentFragment = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getInt(Constants.CURRENT_PAGE_TURN, 0);
        if (currentFragment + 1 > mSectionsPagerAdapter.getCount())
            currentFragment = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("TAG")) {
            currentFragment = Integer.valueOf(extras.getString("TAG")) - 1;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            createLayout();
            startTimer();
            highlightCurrentTurn();
            mViewPager.setCurrentItem(currentFragment, false);
        }
    }

    private void startTimer() {
        chronometerPartial.start();
        chronometerTotal.start();
    }

    private void saveTimer(int previousFragment) {
        switch (previousFragment) {
            case 0:
                getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putLong(Constants.PHASE1_TIMER, countUpPartial).commit();
                break;
            case 1:
                getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putLong(Constants.PHASE2_TIMER, countUpPartial).commit();
                break;
            case 2:
                getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putLong(Constants.PHASE3_TIMER, countUpPartial).commit();
                break;
            case 3:
                getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putLong(Constants.PHASE4_TIMER, countUpPartial).commit();
                break;
            case 4:
                getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putLong(Constants.PHASE5_TIMER, countUpPartial).commit();
                break;
        }
    }

    private void updateTimer() {
        switch (currentFragment) {
            case 0:
                countUpPartial = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getLong(Constants.PHASE1_TIMER, 0);
                break;
            case 1:
                countUpPartial = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getLong(Constants.PHASE2_TIMER, 0);
                break;
            case 2:
                countUpPartial = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getLong(Constants.PHASE3_TIMER, 0);
                break;
            case 3:
                countUpPartial = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getLong(Constants.PHASE4_TIMER, 0);
                break;
            case 4:
                countUpPartial = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getLong(Constants.PHASE5_TIMER, 0);
                break;
        }
    }

    private void createLayout() {
        textViewPhase1 = (TextView) this.findViewById(R.id.textViewPhase1);
        textViewPhase2 = (TextView) this.findViewById(R.id.textViewPhase2);
        textViewPhase3 = (TextView) this.findViewById(R.id.textViewPhase3);
        textViewPhase4 = (TextView) this.findViewById(R.id.textViewPhase4);
        textViewPhase5 = (TextView) this.findViewById(R.id.textViewPhase5);

        textViewTimerTotal = (TextView) this.findViewById(R.id.textViewTimerLeft);
        textViewTimerPartial = (TextView) this.findViewById(R.id.textViewTimerRight);

        chronometerTotal = (Chronometer) findViewById(R.id.chronometerTotal);
        chronometerTotal.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer arg0) {
                countUpTotal++;
                String hour = String.valueOf(countUpTotal / 60);
                if (hour.length() == 1)
                    hour = "0" + hour;
                String minute = String.valueOf(countUpTotal % 60);
                if (minute.length() == 1)
                    minute = "0" + minute;
                textViewTimerTotal.setText(hour + ":" + minute);
            }
        });

        chronometerPartial = (Chronometer) findViewById(R.id.chronometerPartial);
        chronometerPartial.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer arg0) {
                countUpPartial++;
                String hour = String.valueOf(countUpPartial / 60);
                if (hour.length() == 1)
                    hour = "0" + hour;
                String minute = String.valueOf(countUpPartial % 60);
                if (minute.length() == 1)
                    minute = "0" + minute;
                textViewTimerPartial.setText(hour + ":" + minute);
            }
        });
    }

    private void updateLayout(ActivePlayer currentActivePlayer) {
        //TODO checkbox
//        if (getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getInt(Constants.SCREEN_ON, 0) == 1) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//            checkBox.setChecked(true);
//        } else {
//            checkBox.setChecked(false);
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        }
    }

    private void onClickKeepScreenOn(View view) {
//        if (!checkBox.isChecked()) {
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//            getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.SCREEN_ON, 0).commit();
//        } else {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//            getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.SCREEN_ON, 1).commit();
//        }
    }

    private void highlightCurrentTurn() {
        textViewPhase1.setTextColor(getResources().getColor(R.color.primary_text_disabled_material_light));
        textViewPhase2.setTextColor(getResources().getColor(R.color.primary_text_disabled_material_light));
        textViewPhase3.setTextColor(getResources().getColor(R.color.primary_text_disabled_material_light));
        textViewPhase4.setTextColor(getResources().getColor(R.color.primary_text_disabled_material_light));
        textViewPhase5.setTextColor(getResources().getColor(R.color.primary_text_disabled_material_light));

        switch (mViewPager.getCurrentItem()) {
            case 0:
                textViewPhase1.setTextColor(getResources().getColor(android.R.color.black));
                break;
            case 1:
                textViewPhase2.setTextColor(getResources().getColor(android.R.color.black));
                break;
            case 2:
                textViewPhase3.setTextColor(getResources().getColor(android.R.color.black));
                break;
            case 3:
                textViewPhase4.setTextColor(getResources().getColor(android.R.color.black));
                break;
            case 4:
                textViewPhase5.setTextColor(getResources().getColor(android.R.color.black));
                break;
        }
    }
}
