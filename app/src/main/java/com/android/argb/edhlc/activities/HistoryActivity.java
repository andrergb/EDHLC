package com.android.argb.edhlc.activities;

import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.argb.edhlc.R;
import com.android.argb.edhlc.objects.ActivePlayer;

public class HistoryActivity extends ActionBarActivity {

    static ActivePlayer mActivePlayer1;
    static ActivePlayer mActivePlayer2;
    static ActivePlayer mActivePlayer3;
    static ActivePlayer mActivePlayer4;

    private TextView textViewP1Name;
    private TextView textViewP2Name;
    private TextView textViewP3Name;
    private TextView textViewP4Name;

    private ListView listViewP1;
    private ListView listViewP2;
    private ListView listViewP3;
    private ListView listViewP4;

    private LinearLayout mLinearLayoutP3;
    private LinearLayout mLinearLayoutP4;

    private int numPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.edh_default_secondary));
        }

        numPlayers = getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).getInt("NUM_PLAYERS", 4);

        createLayout(this.findViewById(android.R.id.content));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActivePlayer1 = ActivePlayer.loadPlayerSharedPreferences(this, 1);
        mActivePlayer2 = ActivePlayer.loadPlayerSharedPreferences(this, 2);
        mActivePlayer3 = ActivePlayer.loadPlayerSharedPreferences(this, 3);
        mActivePlayer4 = ActivePlayer.loadPlayerSharedPreferences(this, 4);

        if (getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).getInt("SCREEN_ON", 0) == 1)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        else
            getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        updateLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overview, menu);
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

    public void createLayout(View view) {
        if (view != null) {
            textViewP1Name = (TextView) findViewById(R.id.textViewP1Name);
            textViewP1Name.setTypeface(null, Typeface.BOLD);
            listViewP1 = (ListView) findViewById(R.id.listViewP1);

            textViewP2Name = (TextView) findViewById(R.id.textViewP2Name);
            textViewP2Name.setTypeface(null, Typeface.BOLD);
            listViewP2 = (ListView) findViewById(R.id.listViewP2);

            mLinearLayoutP3 = (LinearLayout) findViewById(R.id.linearLayoutP3);
            textViewP3Name = (TextView) findViewById(R.id.textViewP3Name);
            textViewP3Name.setTypeface(null, Typeface.BOLD);
            listViewP3 = (ListView) findViewById(R.id.listViewP3);

            if (!isPlayerActive(3)) {
                mLinearLayoutP3.setVisibility(View.GONE);
            }

            mLinearLayoutP4 = (LinearLayout) findViewById(R.id.linearLayoutP4);
            textViewP4Name = (TextView) findViewById(R.id.textViewP4Name);
            textViewP4Name.setTypeface(null, Typeface.BOLD);
            listViewP4 = (ListView) findViewById(R.id.listViewP4);

            if (!isPlayerActive(4)) {
                mLinearLayoutP4.setVisibility(View.GONE);
            }
        }
    }

    private boolean isPlayerActive(int i) {
        if (numPlayers < i)
            return false;
        return true;
    }

    private void updateLayout() {
        textViewP1Name.setText(mActivePlayer1.getPlayerName());
        textViewP1Name.setTextColor(mActivePlayer1.getPlayerColor()[0]);
        String latestSavedLifePreferences = getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).getString("PHL" + mActivePlayer1.getPlayerTag(), "40");
        String latestSavedEDHPreferences = getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).getString("PHEDH" + mActivePlayer1.getPlayerTag(), "0@0@0@0");
        if (latestSavedLifePreferences != null && latestSavedEDHPreferences != null) {
            String[] latestSavedLifeArray = latestSavedLifePreferences.split("_");
            String[] latestSavedEDHArray = latestSavedEDHPreferences.split("_");
            listViewP1.setAdapter(new customHistoryListViewAdapter(this.getBaseContext(), latestSavedLifeArray, latestSavedEDHArray, mActivePlayer1.getPlayerColor()[1]));
        }

        textViewP2Name.setText(mActivePlayer2.getPlayerName());
        textViewP2Name.setTextColor(mActivePlayer2.getPlayerColor()[0]);
        latestSavedLifePreferences = getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).getString("PHL" + mActivePlayer2.getPlayerTag(), "0");
        latestSavedEDHPreferences = getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).getString("PHEDH" + mActivePlayer2.getPlayerTag(), "0@0@0@0");
        if (latestSavedLifePreferences != null && latestSavedEDHPreferences != null) {
            String[] latestSavedLifeArray = latestSavedLifePreferences.split("_");
            String[] latestSavedEDHArray = latestSavedEDHPreferences.split("_");
            listViewP2.setAdapter(new customHistoryListViewAdapter(this.getBaseContext(), latestSavedLifeArray, latestSavedEDHArray, mActivePlayer2.getPlayerColor()[1]));
        }

        textViewP3Name.setText(mActivePlayer3.getPlayerName());
        textViewP3Name.setTextColor(mActivePlayer3.getPlayerColor()[0]);
        latestSavedLifePreferences = getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).getString("PHL" + mActivePlayer3.getPlayerTag(), "0");
        latestSavedEDHPreferences = getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).getString("PHEDH" + mActivePlayer3.getPlayerTag(), "0@0@0@0");
        if (latestSavedLifePreferences != null && latestSavedEDHPreferences != null) {
            String[] latestSavedLifeArray = latestSavedLifePreferences.split("_");
            String[] latestSavedEDHArray = latestSavedEDHPreferences.split("_");
            listViewP3.setAdapter(new customHistoryListViewAdapter(this.getBaseContext(), latestSavedLifeArray, latestSavedEDHArray, mActivePlayer3.getPlayerColor()[1]));
        }

        textViewP4Name.setText(mActivePlayer4.getPlayerName());
        textViewP4Name.setTextColor(mActivePlayer4.getPlayerColor()[0]);
        latestSavedLifePreferences = getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).getString("PHL" + mActivePlayer4.getPlayerTag(), "0");
        latestSavedEDHPreferences = getSharedPreferences(ActivePlayer.PREFNAME, MODE_PRIVATE).getString("PHEDH" + mActivePlayer4.getPlayerTag(), "0@0@0@0");
        if (latestSavedLifePreferences != null && latestSavedEDHPreferences != null) {
            String[] latestSavedLifeArray = latestSavedLifePreferences.split("_");
            String[] latestSavedEDHArray = latestSavedEDHPreferences.split("_");
            listViewP4.setAdapter(new customHistoryListViewAdapter(this.getBaseContext(), latestSavedLifeArray, latestSavedEDHArray, mActivePlayer4.getPlayerColor()[1]));
        }
    }

    public void onClickP1(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("TAG", "1");
        startActivity(intent);
        this.finish();
    }

    public void onClickP2(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("TAG", "2");
        startActivity(intent);
        this.finish();
    }

    public void onClickP3(View view) {
        if (isPlayerActive(3)) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("TAG", "3");
            startActivity(intent);
            this.finish();
        }
    }

    public void onClickP4(View view) {
        if (isPlayerActive(4)) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("TAG", "4");
            startActivity(intent);
            this.finish();
        }
    }

    static class customHistoryListViewAdapter extends BaseAdapter {

        Context context;
        String[] dataLife;
        String[] dataEDH;
        int color;
        private static LayoutInflater inflater = null;

        public customHistoryListViewAdapter(Context context, String[] dataLife, String[] dataEDH, int color) {
            this.context = context;
            this.dataLife = dataLife;
            this.dataEDH = dataEDH;
            this.color = color;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return dataLife.length;
        }

        @Override
        public Object getItem(int position) {
            return dataLife[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (vi == null)
                vi = inflater.inflate(R.layout.row_history, null);

            TextView textViewLife = (TextView) vi.findViewById(R.id.textViewRowLife);
            textViewLife.setText(dataLife[position]);
            textViewLife.setTextColor(color);
            textViewLife.setTypeface(null, Typeface.BOLD);

            TextView textViewRowEDH1 = (TextView) vi.findViewById(R.id.textViewRowEDH1);
            textViewRowEDH1.setText(dataEDH[position].split("@")[0]);
            textViewRowEDH1.setTextColor(color);

            TextView textViewRowEDH2 = (TextView) vi.findViewById(R.id.textViewRowEDH2);
            textViewRowEDH2.setText(dataEDH[position].split("@")[1]);
            textViewRowEDH2.setTextColor(color);

            TextView textViewRowEDH3 = (TextView) vi.findViewById(R.id.textViewRowEDH3);
            textViewRowEDH3.setText(dataEDH[position].split("@")[2]);
            textViewRowEDH3.setTextColor(color);

            TextView textViewRowEDH4 = (TextView) vi.findViewById(R.id.textViewRowEDH4);
            textViewRowEDH4.setText(dataEDH[position].split("@")[3]);
            textViewRowEDH4.setTextColor(color);

            textViewRowEDH1.setAlpha((float) 1.0);
            textViewRowEDH2.setAlpha((float) 1.0);
            textViewRowEDH3.setAlpha((float) 1.0);
            textViewRowEDH4.setAlpha((float) 1.0);

            if (position == 0) {
                textViewRowEDH1.setAlpha((float) 0.4);
                textViewRowEDH2.setAlpha((float) 0.4);
                textViewRowEDH3.setAlpha((float) 0.4);
                textViewRowEDH4.setAlpha((float) 0.4);
            } else {
                if (dataEDH[position].split("@")[0].equalsIgnoreCase(dataEDH[position - 1].split("@")[0]))
                    textViewRowEDH1.setAlpha((float) 0.4);
                if (dataEDH[position].split("@")[1].equalsIgnoreCase(dataEDH[position - 1].split("@")[1]))
                    textViewRowEDH2.setAlpha((float) 0.4);
                if (dataEDH[position].split("@")[2].equalsIgnoreCase(dataEDH[position - 1].split("@")[2]))
                    textViewRowEDH3.setAlpha((float) 0.4);
                if (dataEDH[position].split("@")[3].equalsIgnoreCase(dataEDH[position - 1].split("@")[3]))
                    textViewRowEDH4.setAlpha((float) 0.4);
            }

            return vi;
        }
    }
}
