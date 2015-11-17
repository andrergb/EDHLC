package com.android.argb.edhlc.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.android.argb.edhlc.R;
import com.android.argb.edhlc.database.player.PlayersDataAccessObject;
import com.android.argb.edhlc.objects.ActivePlayer;
import com.android.argb.edhlc.objects.DrawerPlayerList;

import java.util.ArrayList;
import java.util.List;

public class PlayerListActivity extends ActionBarActivity {

    private static CheckBox checkBox;
    private ListView listViewPlayers;
    private PlayersDataAccessObject playersDB;

    private DrawerPlayerList mDrawerPlayerList;
    BroadcastReceiver mPlayerAddedOrRemovedBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playerlist);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.edh_default_secondary));
        }

        //DrawerMain menu
        List<String[]> drawerLists = new ArrayList<>();
        drawerLists.add(getResources().getStringArray(R.array.string_menu_player_list_1));
        drawerLists.add(getResources().getStringArray(R.array.string_menu_player_list_2));

        assert getSupportActionBar() != null;
        mDrawerPlayerList = new DrawerPlayerList(this, drawerLists);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mPlayerAddedOrRemovedBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateLayout();
            }
        };

        playersDB = new PlayersDataAccessObject(this);

        createLayout(this.findViewById(android.R.id.content));
    }


    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mPlayerAddedOrRemovedBroadcastReceiver,
                new IntentFilter(DrawerPlayerList.BROADCAST_INTENT_FILTER_PLAYER_ADDED_OR_REMOVED)
        );

        if (getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).getInt("SCREEN_ON", 0) == 1)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        else
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        playersDB.open();
        updateLayout();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPlayerAddedOrRemovedBroadcastReceiver);
        playersDB.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //DrawerMain menu
        if (mDrawerPlayerList.getDrawerToggle().onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerPlayerList.getDrawerToggle().syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerPlayerList.getDrawerToggle().onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerPlayerList.isDrawerOpen())
            mDrawerPlayerList.dismiss();
        else
            super.onBackPressed();
    }

    public void createLayout(View view) {
        if (view != null) {
            checkBox = (CheckBox) findViewById(R.id.checkBoxKeepScreenOn);
            listViewPlayers = (ListView) findViewById(R.id.listViewPlayers);
        }
    }

    private void updateLayout() {
        if (getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).getInt("SCREEN_ON", 0) == 1) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
            getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        listViewPlayers.setAdapter(new CustomDeckListViewAdapter(this.getBaseContext(), playersDB.getAllPlayers()));
        listViewPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PlayerListActivity.this, PlayerActivity.class);
                intent.putExtra("PLAYERNAME", listViewPlayers.getItemAtPosition(position).toString());
                startActivity(intent);
            }
        });
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

    class CustomDeckListViewAdapter extends BaseAdapter {
        Context context;
        List<String> dataPlayersList;
        private LayoutInflater inflater = null;

        public CustomDeckListViewAdapter(Context context, List<String> dataPlayersList) {
            this.context = context;
            this.dataPlayersList = dataPlayersList;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return dataPlayersList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataPlayersList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (vi == null)
                vi = inflater.inflate(R.layout.row_playerlist, null);

            TextView textViewPlayerListName = (TextView) vi.findViewById(R.id.textViewPlayerListName);
            textViewPlayerListName.setText(dataPlayersList.get(position));

            return vi;
        }
    }
}
