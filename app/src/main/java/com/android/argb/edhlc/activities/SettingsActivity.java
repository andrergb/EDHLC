package com.android.argb.edhlc.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.argb.edhlc.Constants;
import com.android.argb.edhlc.R;
import com.android.argb.edhlc.objects.Drawer.DrawerSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsActivity extends ActionBarActivity {

    private static CheckBox mCheckBoxKeepScreenOn;
    private ListView mListViewSettings;

    private DrawerSettings mDrawerSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.secondary_color));
        }

        //DrawerMain menu
        List<String[]> drawerLists = new ArrayList<>();
        drawerLists.add(getResources().getStringArray(R.array.string_menu_records_1));
        drawerLists.add(getResources().getStringArray(R.array.string_menu_records_2));

        assert getSupportActionBar() != null;
        mDrawerSettings = new DrawerSettings(this, drawerLists);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //DrawerMain menu
        if (mDrawerSettings.getDrawerToggle().onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerSettings.getDrawerToggle().syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerSettings.getDrawerToggle().onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerSettings.isDrawerOpen())
            mDrawerSettings.dismiss();
        else
            super.onBackPressed();
    }

    public void createLayout(View view) {
        if (view != null) {
            mCheckBoxKeepScreenOn = (CheckBox) findViewById(R.id.checkBoxKeepScreenOn);
            mListViewSettings = (ListView) findViewById(R.id.listViewSettings);
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

        final List<Map<String, String>> listMapDecks = new ArrayList<>();
        Map<String, String> auxMap = new HashMap<>(2);
        auxMap.put("title", "Life points");
        auxMap.put("subtitle", "Choose the starting life points");
        listMapDecks.add(auxMap);

        SimpleAdapter adapter = new SimpleAdapter(this,
                listMapDecks,
                R.layout.row_two_item_list,
                new String[]{"title", "subtitle"},
                new int[]{R.id.text1, R.id.text2});
        mListViewSettings.setAdapter(adapter);
        mListViewSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        createInitialLifeDialog(view);
                        break;
                }
            }
        });
        //TODO onClickListener
    }

    public void createInitialLifeDialog(View view) {
        View mView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_player_life, null);
        final EditText userInput = (EditText) mView.findViewById(R.id.editTextPlayerLife);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(mView);
        alertDialogBuilder.setTitle("Initial life");
        alertDialogBuilder.setMessage("Set the initial life of the players:");
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

    public void onClickKeepScreenOn(View view) {
        if (!mCheckBoxKeepScreenOn.isChecked()) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.SCREEN_ON, 0).commit();
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.SCREEN_ON, 1).commit();
        }
    }
}
