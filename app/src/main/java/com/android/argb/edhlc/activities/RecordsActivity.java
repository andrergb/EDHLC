package com.android.argb.edhlc.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.argb.edhlc.Constants;
import com.android.argb.edhlc.R;
import com.android.argb.edhlc.database.record.RecordsDataAccessObject;
import com.android.argb.edhlc.objects.Deck;
import com.android.argb.edhlc.objects.Drawer.DrawerRecords;
import com.android.argb.edhlc.objects.Record;

import java.util.ArrayList;
import java.util.List;

public class RecordsActivity extends ActionBarActivity {

    private static CheckBox mCheckBoxKeepScreenOn;
    private ListView mListViewRecords;

    private DrawerRecords mDrawerRecords;
    private String mPlayerName;
    private String mDeckName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.edh_default_secondary));
        }

        //DrawerMain menu
        List<String[]> drawerLists = new ArrayList<>();
        drawerLists.add(getResources().getStringArray(R.array.string_menu_records_1));
        drawerLists.add(getResources().getStringArray(R.array.string_menu_records_2));

        assert getSupportActionBar() != null;
        mDrawerRecords = new DrawerRecords(this, drawerLists);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        mPlayerName = intent.getStringExtra("RECORDS_PLAYER_NAME");
        mDeckName = intent.getStringExtra("RECORDS_DECK_NAME");

        createLayout(this.findViewById(android.R.id.content));
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getInt(Constants.SCREEN_ON, 0) == 1)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        else
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        updateLayout();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //DrawerMain menu
        if (mDrawerRecords.getDrawerToggle().onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerRecords.getDrawerToggle().syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerRecords.getDrawerToggle().onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerRecords.isDrawerOpen())
            mDrawerRecords.dismiss();
        else
            super.onBackPressed();
    }

    public void createLayout(View view) {
        if (view != null) {
            mCheckBoxKeepScreenOn = (CheckBox) findViewById(R.id.checkBoxKeepScreenOn);
            mListViewRecords = (ListView) findViewById(R.id.listViewRecords);
        }
    }

    private void updateLayout() {
        if (getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getInt(Constants.SCREEN_ON, 0) == 1) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            mCheckBoxKeepScreenOn.setChecked(true);
        } else {
            mCheckBoxKeepScreenOn.setChecked(false);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        RecordsDataAccessObject recordsDb = new RecordsDataAccessObject(this);
        recordsDb.open();
        List<Record> records;
        if (mPlayerName != null && mDeckName != null)
            records = recordsDb.getAllRecordsByDeck(new Deck(mPlayerName, mDeckName));
        else
            records = recordsDb.getAllRecords();
        recordsDb.close();

        mListViewRecords.setAdapter(new CustomDeckListViewAdapter(this.getBaseContext(), records));
    }

    public void onClickKeepScreenOn(View view) {
        if (!mCheckBoxKeepScreenOn.isChecked()) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.SCREEN_ON, 0).commit();
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.SCREEN_ON, 1).commit();
        }
    }

    class CustomDeckListViewAdapter extends BaseAdapter {
        Context context;
        List<Record> dataRecords;
        private LayoutInflater inflater = null;

        public CustomDeckListViewAdapter(Context context, List<Record> dataRecords) {
            this.context = context;
            this.dataRecords = dataRecords;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return dataRecords.size();
        }

        @Override
        public Object getItem(int position) {
            return dataRecords.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (vi == null)
                vi = inflater.inflate(R.layout.row_records, null);

            LinearLayout linearLayoutFirstPlace = (LinearLayout) vi.findViewById(R.id.linearLayoutFirstPlace);
            LinearLayout linearLayoutSecondPlace = (LinearLayout) vi.findViewById(R.id.linearLayoutSecondPlace);
            LinearLayout linearLayoutThirdPlace = (LinearLayout) vi.findViewById(R.id.linearLayoutThirdPlace);
            LinearLayout linearLayoutFourthPlace = (LinearLayout) vi.findViewById(R.id.linearLayoutFourthPlace);
            linearLayoutFirstPlace.setVisibility(View.VISIBLE);
            linearLayoutSecondPlace.setVisibility(View.VISIBLE);
            switch (dataRecords.get(position).size()) {
                case 3:
                    linearLayoutThirdPlace.setVisibility(View.VISIBLE);
                    linearLayoutFourthPlace.setVisibility(View.GONE);
                    break;
                case 4:
                    linearLayoutThirdPlace.setVisibility(View.VISIBLE);
                    linearLayoutFourthPlace.setVisibility(View.VISIBLE);
                    break;
            }

            TextView textViewFirstPlayerName = (TextView) vi.findViewById(R.id.textViewFirstPlayerName);
            textViewFirstPlayerName.setText(dataRecords.get(position).getFirstPlace().getPlayerName());
            TextView textViewFirstPlayerDeck = (TextView) vi.findViewById(R.id.textViewFirstPlayerDeck);
            textViewFirstPlayerDeck.setText(dataRecords.get(position).getFirstPlace().getDeckName());
            textViewFirstPlayerName.setTypeface(null, Typeface.NORMAL);
            textViewFirstPlayerDeck.setTypeface(null, Typeface.NORMAL);
            if (dataRecords.get(position).getFirstPlace().getPlayerName().equalsIgnoreCase(mPlayerName) &&
                    dataRecords.get(position).getFirstPlace().getDeckName().equalsIgnoreCase(mDeckName)) {
                textViewFirstPlayerName.setTypeface(null, Typeface.BOLD);
                textViewFirstPlayerDeck.setTypeface(null, Typeface.BOLD);
            } else {
                textViewFirstPlayerName.setTypeface(null, Typeface.NORMAL);
                textViewFirstPlayerDeck.setTypeface(null, Typeface.NORMAL);
            }

            TextView textViewSecondPlayerName = (TextView) vi.findViewById(R.id.textViewSecondPlayerName);
            textViewSecondPlayerName.setText(dataRecords.get(position).getSecondPlace().getPlayerName());
            TextView textViewSecondPlayerDeck = (TextView) vi.findViewById(R.id.textViewSecondPlayerDeck);
            textViewSecondPlayerDeck.setText(dataRecords.get(position).getSecondPlace().getDeckName());
            textViewSecondPlayerName.setTypeface(null, Typeface.NORMAL);
            textViewSecondPlayerDeck.setTypeface(null, Typeface.NORMAL);
            if (dataRecords.get(position).getSecondPlace().getPlayerName().equalsIgnoreCase(mPlayerName) &&
                    dataRecords.get(position).getSecondPlace().getDeckName().equalsIgnoreCase(mDeckName)) {
                textViewSecondPlayerName.setTypeface(null, Typeface.BOLD);
                textViewSecondPlayerDeck.setTypeface(null, Typeface.BOLD);
            } else {
                textViewSecondPlayerName.setTypeface(null, Typeface.NORMAL);
                textViewSecondPlayerDeck.setTypeface(null, Typeface.NORMAL);
            }

            TextView textViewThirdPlayerName = (TextView) vi.findViewById(R.id.textViewThirdPlayerName);
            textViewThirdPlayerName.setText(dataRecords.get(position).getThirdPlace().getPlayerName());
            TextView textViewThirdPlayerDeck = (TextView) vi.findViewById(R.id.textViewThirdPlayerDeck);
            textViewThirdPlayerDeck.setText(dataRecords.get(position).getThirdPlace().getDeckName());
            textViewThirdPlayerName.setTypeface(null, Typeface.NORMAL);
            textViewThirdPlayerDeck.setTypeface(null, Typeface.NORMAL);
            if (dataRecords.get(position).getThirdPlace().getPlayerName().equalsIgnoreCase(mPlayerName) &&
                    dataRecords.get(position).getThirdPlace().getDeckName().equalsIgnoreCase(mDeckName)) {
                textViewThirdPlayerName.setTypeface(null, Typeface.BOLD);
                textViewThirdPlayerDeck.setTypeface(null, Typeface.BOLD);
            } else {
                textViewThirdPlayerName.setTypeface(null, Typeface.NORMAL);
                textViewThirdPlayerDeck.setTypeface(null, Typeface.NORMAL);
            }

            TextView textViewFourthPlayerName = (TextView) vi.findViewById(R.id.textViewFourthPlayerName);
            textViewFourthPlayerName.setText(dataRecords.get(position).getFourthPlace().getPlayerName());
            TextView textViewFourthPlayerDeck = (TextView) vi.findViewById(R.id.textViewFourthPlayerDeck);
            textViewFourthPlayerDeck.setText(dataRecords.get(position).getFourthPlace().getDeckName());
            textViewFourthPlayerName.setTypeface(null, Typeface.NORMAL);
            textViewFourthPlayerDeck.setTypeface(null, Typeface.NORMAL);
            if (dataRecords.get(position).getFourthPlace().getPlayerName().equalsIgnoreCase(mPlayerName) &&
                    dataRecords.get(position).getFourthPlace().getDeckName().equalsIgnoreCase(mDeckName)) {
                textViewFourthPlayerName.setTypeface(null, Typeface.BOLD);
                textViewFourthPlayerDeck.setTypeface(null, Typeface.BOLD);
            } else {
                textViewFourthPlayerName.setTypeface(null, Typeface.NORMAL);
                textViewFourthPlayerDeck.setTypeface(null, Typeface.NORMAL);
            }

            return vi;
        }
    }
}
