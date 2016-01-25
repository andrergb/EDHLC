package com.android.argb.edhlc.objects.Drawer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.argb.edhlc.R;
import com.android.argb.edhlc.activities.PlayerListActivity;
import com.android.argb.edhlc.activities.RecordsActivity;

import java.util.List;

/**
 * -Created by agbarros on 05/11/2015.
 */
public class DrawerHistory {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private LinearLayout linearLayoutDrawer;

    private Activity parentActivity;

    public DrawerHistory(final Activity parentActivity, List<String[]> options) {
        this.parentActivity = parentActivity;

        linearLayoutDrawer = (LinearLayout) parentActivity.findViewById(R.id.linearLayoutDrawer);
        mDrawerLayout = (DrawerLayout) parentActivity.findViewById(R.id.drawer_layout);

        ListView mDrawerList1 = (ListView) parentActivity.findViewById(R.id.listViewDrawer1);
        mDrawerList1.setAdapter(new ArrayAdapter<>(parentActivity, android.R.layout.simple_list_item_1, options.get(0)));
        mDrawerList1.setOnItemClickListener(new DrawerItemClickListener1());

        ListView mDrawerList2 = (ListView) parentActivity.findViewById(R.id.listViewDrawer2);
        mDrawerList2.setAdapter(new ArrayAdapter<>(parentActivity, android.R.layout.simple_list_item_1, options.get(1)));
        mDrawerList2.setOnItemClickListener(new DrawerItemClickListener2());

        mDrawerToggle = new ActionBarDrawerToggle(parentActivity, mDrawerLayout, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                parentActivity.invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                parentActivity.invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return mDrawerToggle;
    }

    public boolean isDrawerOpen() {
        return (mDrawerLayout.isDrawerOpen(linearLayoutDrawer));
    }

    public void dismiss() {
        mDrawerLayout.closeDrawers();
    }

    private class DrawerItemClickListener1 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        }
    }

    private class DrawerItemClickListener2 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0: //Home
                    mDrawerLayout.closeDrawers();
                    parentActivity.finish();
                    break;
                case 1: //Players
                    mDrawerLayout.closeDrawers();
                    parentActivity.startActivity(new Intent(parentActivity, PlayerListActivity.class));
                    parentActivity.finish();
                    break;
                case 2: //All Records
                    mDrawerLayout.closeDrawers();
                    parentActivity.startActivity(new Intent(parentActivity, RecordsActivity.class));
                    parentActivity.finish();
                    break;
                case 3: //Settings
                    mDrawerLayout.closeDrawers();
                    // parentActivity.startActivity(new Intent(parentActivity, SettingsActivity.class));
                    break;
                case 4: //About
                    mDrawerLayout.closeDrawers();
                    createAboutDialog(view);
                    break;
            }
        }
    }

    private void createAboutDialog(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setTitle("About " + parentActivity.getResources().getString(R.string.app_name));
        String message;
        try {
            message = "\nVersion: " + parentActivity.getPackageManager().getPackageInfo(parentActivity.getPackageName(), 0).versionName;
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
        alertDialog.show();
    }

}
