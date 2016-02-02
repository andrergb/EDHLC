package com.android.argb.edhlc.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.argb.edhlc.R;
import com.android.argb.edhlc.database.deck.DecksDataAccessObject;
import com.android.argb.edhlc.database.record.RecordsDataAccessObject;
import com.android.argb.edhlc.objects.Deck;
import com.android.argb.edhlc.objects.Record;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

public class DeckActivity extends AppCompatActivity {

    private LinearLayout doughnutChartLinearLayout;
    private ImageView imageViewShieldColor;
    private TextView textViewDeckName;
    private List<ImageView> listIdentityHolder;
    private ImageView imageViewMana1;
    private ImageView imageViewMana2;
    private ImageView imageViewMana3;
    private ImageView imageViewMana4;
    private ImageView imageViewMana5;
    private ImageView imageViewMana6;
    private TextView textViewTotalGame;

    private int[] COLORS;
    private MultipleCategorySeries mMultipleCategorySeriesDataSet;
    private DefaultRenderer mDoughnutRender;
    private GraphicalView mDoughnutChartView;

    private Deck currentDeck;
    private String mPlayerName;
    private String mDeckName;
    private String mDeckIdentity;

    private DecksDataAccessObject decksDB;
    private RecordsDataAccessObject recordsDB;
    private TextView textViewFirst;
    private TextView textViewSecond;
    private TextView textViewThird;
    private TextView textViewFourth;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(DeckActivity.this, PlayerListActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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

    public void updateDonutChart(int first, int second, int third, int fourth) {
        textViewFirst.setText("1st: " + first);
        textViewSecond.setText("2nd: " + second);
        textViewThird.setText("3rd: " + third);
        textViewFourth.setText("4th: " + fourth);

        List<double[]> values = new ArrayList<>();
        values.add(new double[]{first, second, third, fourth});
        values.add(new double[]{0, 0, 0, 0});
        values.add(new double[]{0, 0, 0, 0});

        List<String[]> titles = new ArrayList<>();
        titles.add(new String[]{String.valueOf(first), String.valueOf(second), String.valueOf(third), String.valueOf(fourth)});
        titles.add(new String[]{"", "", "", ""});
        titles.add(new String[]{"", "", "", ""});

        mMultipleCategorySeriesDataSet = new MultipleCategorySeries("");
        mMultipleCategorySeriesDataSet.clear();
        for (int i = 0; i < values.size(); i++)
            mMultipleCategorySeriesDataSet.add(titles.get(i), values.get(i));

        mDoughnutRender = new DefaultRenderer();
        for (int color : COLORS) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            mDoughnutRender.addSeriesRenderer(r);
        }

        mDoughnutRender.setZoomEnabled(false);
        mDoughnutRender.setZoomButtonsVisible(false);
        mDoughnutRender.setPanEnabled(false);
        mDoughnutRender.setShowLegend(false);
        mDoughnutRender.setClickEnabled(false);
        mDoughnutRender.setScale((float) 1.2);
        mDoughnutRender.setShowLabels(false);
        mDoughnutRender.setDisplayValues(true);
        mDoughnutRender.setStartAngle(225);
        mDoughnutRender.setBackgroundColor(Color.TRANSPARENT);

        mDoughnutChartView = ChartFactory.getDoughnutChartView(this, mMultipleCategorySeriesDataSet, mDoughnutRender);

        doughnutChartLinearLayout.removeAllViews();
        doughnutChartLinearLayout.addView(mDoughnutChartView);

        mDoughnutChartView.repaint();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.edh_default_secondary));
        }

        Intent intent = getIntent();
        mPlayerName = intent.getStringExtra("PLAYERNAME");
        mDeckName = intent.getStringExtra("DECKNAME");
        mDeckIdentity = intent.getStringExtra("DECKIDENTITY");

        currentDeck = new Deck(mPlayerName, mDeckName);

        decksDB = new DecksDataAccessObject(this);
        recordsDB = new RecordsDataAccessObject(this);
        decksDB.open();
        recordsDB.open();

        COLORS = new int[]{this.getResources().getColor(R.color.first),
                this.getResources().getColor(R.color.second),
                this.getResources().getColor(R.color.third),
                this.getResources().getColor(R.color.fourth)};

        createLayout(this.findViewById(android.R.id.content));
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
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        mMultipleCategorySeriesDataSet = (MultipleCategorySeries) savedState.getSerializable("current_series");
        mDoughnutRender = (DefaultRenderer) savedState.getSerializable("current_renderer");
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateLayout();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("current_series", mMultipleCategorySeriesDataSet);
        outState.putSerializable("current_renderer", mDoughnutRender);
    }

    private void createLayout(View view) {
        if (view != null) {
            //Deck name
            textViewDeckName = (TextView) view.findViewById(R.id.textviewDeckName);

            //Shield color
            imageViewShieldColor = (ImageView) view.findViewById(R.id.imageViewShieldColor);

            //Deck identity
            imageViewMana1 = (ImageView) view.findViewById(R.id.imageViewMana1);
            imageViewMana2 = (ImageView) view.findViewById(R.id.imageViewMana2);
            imageViewMana3 = (ImageView) view.findViewById(R.id.imageViewMana3);
            imageViewMana4 = (ImageView) view.findViewById(R.id.imageViewMana4);
            imageViewMana5 = (ImageView) view.findViewById(R.id.imageViewMana5);
            imageViewMana6 = (ImageView) view.findViewById(R.id.imageViewMana6);

            imageViewMana1.setVisibility(View.GONE);
            imageViewMana2.setVisibility(View.GONE);
            imageViewMana3.setVisibility(View.GONE);
            imageViewMana4.setVisibility(View.GONE);
            imageViewMana5.setVisibility(View.GONE);
            imageViewMana6.setVisibility(View.GONE);

            listIdentityHolder = new ArrayList<>();
            listIdentityHolder.add(imageViewMana6);
            listIdentityHolder.add(imageViewMana5);
            listIdentityHolder.add(imageViewMana4);
            listIdentityHolder.add(imageViewMana3);
            listIdentityHolder.add(imageViewMana2);
            listIdentityHolder.add(imageViewMana1);

            //Total games
            textViewTotalGame = (TextView) view.findViewById(R.id.textViewTotalGame);

            //Chart game history
            //LinearLayout layoutChart = (LinearLayout) findViewById(R.id.chart);
            //layoutChart.addView(mChartView);

            //TODO
            doughnutChartLinearLayout = (LinearLayout) findViewById(R.id.chart);

            textViewFirst = (TextView) view.findViewById(R.id.textViewFirst);
            textViewSecond = (TextView) view.findViewById(R.id.textViewSecond);
            textViewThird = (TextView) view.findViewById(R.id.textViewThird);
            textViewFourth = (TextView) view.findViewById(R.id.textViewFourth);
        }
    }

    private void updateLayout() {
        //Deck name
        textViewDeckName.setText(mDeckName);

        //Shield color
        imageViewShieldColor.setColorFilter(decksDB.getDeck(mPlayerName, mDeckName).getDeckColor()[0]);

        //Deck Identity
        int index = 0;
        if (mDeckIdentity.charAt(0) == '1') {
            listIdentityHolder.get(index).setBackground(this.getResources().getDrawable(R.drawable.mana_white));
            listIdentityHolder.get(index).setVisibility(View.VISIBLE);
            index++;
        }
        if (mDeckIdentity.charAt(1) == '1') {
            listIdentityHolder.get(index).setBackground(this.getResources().getDrawable(R.drawable.mana_blue));
            listIdentityHolder.get(index).setVisibility(View.VISIBLE);
            index++;
        }
        if (mDeckIdentity.charAt(2) == '1') {
            listIdentityHolder.get(index).setBackground(this.getResources().getDrawable(R.drawable.mana_black));
            listIdentityHolder.get(index).setVisibility(View.VISIBLE);
            index++;
        }
        if (mDeckIdentity.charAt(3) == '1') {
            listIdentityHolder.get(index).setBackground(this.getResources().getDrawable(R.drawable.mana_red));
            listIdentityHolder.get(index).setVisibility(View.VISIBLE);
            index++;
        }
        if (mDeckIdentity.charAt(4) == '1') {
            listIdentityHolder.get(index).setBackground(this.getResources().getDrawable(R.drawable.mana_green));
            listIdentityHolder.get(index).setVisibility(View.VISIBLE);
            index++;
        }
        if (mDeckIdentity.charAt(5) == '1') {
            listIdentityHolder.get(index).setBackground(this.getResources().getDrawable(R.drawable.mana_colorless));
            listIdentityHolder.get(index).setVisibility(View.VISIBLE);
        }

        //Total games
        List<Record> records = recordsDB.getAllRecordsByDeck(currentDeck);
        textViewTotalGame.setText("" + records.size());

        //Game history (0/0/0/0)
        int first = recordsDB.getAllFirstPlaceRecordsByDeck(currentDeck).size();
        int second = recordsDB.getAllSecondPlaceRecordsByDeck(currentDeck).size();
        int third = recordsDB.getAllThirdPlaceRecordsByDeck(currentDeck).size();
        int fourth = recordsDB.getAllFourthPlaceRecordsByDeck(currentDeck).size();

        //TODO
        updateDonutChart(5, 4, 3, 2);
    }
}
