package com.android.argb.edhlc.activities;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.argb.edhlc.R;
import com.android.argb.edhlc.database.deck.DecksDataAccessObject;
import com.android.argb.edhlc.database.record.RecordsDataAccessObject;
import com.android.argb.edhlc.objects.Deck;
import com.android.argb.edhlc.objects.Record;
import com.android.camera.CropImageIntentBuilder;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//Crop: https://github.com/lvillani/android-cropimage
public class DeckActivity extends AppCompatActivity {

    private static int REQUEST_PICTURE = 1;
    private static int REQUEST_CROP_PICTURE = 2;
    private Deck currentDeck;
    private String mPlayerName;
    private String mDeckName;
    private String mDeckIdentity;
    private DecksDataAccessObject decksDB;
    private RecordsDataAccessObject recordsDB;
    //Main card - Deck info
    private ImageView imageViewShieldColor;
    private TextView textViewDeckName;
    private List<ImageView> listIdentityHolder;
    private ImageView imageViewMana1;
    private ImageView imageViewMana2;
    private ImageView imageViewMana3;
    private ImageView imageViewMana4;
    private ImageView imageViewMana5;
    private ImageView imageViewMana6;
    private int[] COLORS;
    //Card - Chart deck history 2 - 1v1
    private CardView cardViewDeckHistory2;
    private ImageView iconIndicatorDeckHistory2;
    private TextView textTitleDeckHistory2;
    private TextView textViewFirst2;
    private TextView textViewSecond2;
    private LinearLayout doughnutChartLinearLayout2;
    private TextView textViewTotalGameNumber2;
    private TextView textViewTotalGameText2;
    private DefaultRenderer mDoughnutRender2;
    private MultipleCategorySeries mMultipleCategorySeriesDataSet2;
    private GraphicalView mDoughnutChartView2;
    private RelativeLayout relativeTitleDeckHistory2;
    private int mCardViewFullHeightDeckHistory2;
    //Card - Chart deck history 3 - 1v1v1
    private CardView cardViewDeckHistory3;
    private ImageView iconIndicatorDeckHistory3;
    private TextView textTitleDeckHistory3;
    private TextView textViewFirst3;
    private TextView textViewSecond3;
    private TextView textViewThird3;
    private LinearLayout doughnutChartLinearLayout3;
    private TextView textViewTotalGameNumber3;
    private TextView textViewTotalGameText3;
    private DefaultRenderer mDoughnutRender3;
    private MultipleCategorySeries mMultipleCategorySeriesDataSet3;
    private GraphicalView mDoughnutChartView3;
    private RelativeLayout relativeTitleDeckHistory3;
    private int mCardViewFullHeightDeckHistory3;
    //Card - Chart deck history 4 - 1v1v1v1
    private CardView cardViewDeckHistory4;
    private ImageView iconIndicatorDeckHistory4;
    private TextView textTitleDeckHistory4;
    private TextView textViewFirst4;
    private TextView textViewSecond4;
    private TextView textViewThird4;
    private TextView textViewFourth4;
    private LinearLayout doughnutChartLinearLayout4;
    private TextView textViewTotalGameNumber4;
    private TextView textViewTotalGameText4;
    private MultipleCategorySeries mMultipleCategorySeriesDataSet4;
    private DefaultRenderer mDoughnutRender4;
    private GraphicalView mDoughnutChartView4;
    private RelativeLayout relativeTitleDeckHistory4;
    private int mCardViewFullHeightDeckHistory4;
    //Card - Last game played
    private CardView cardViewLastGamePlayed;
    private RelativeLayout relativeTitleLastGamePlayed;
    private TextView textTitleLastGamePlayed;
    private ImageView iconIndicatorLastGamePlayed;
    private LinearLayout linearLastGame1;
    private TextView textViewPlayer1;
    private TextView textViewDeck1;
    private LinearLayout linearLastGame2;
    private TextView textViewPlayer2;
    private TextView textViewDeck2;
    private LinearLayout linearLastGame3;
    private TextView textViewPlayer3;
    private TextView textViewDeck3;
    private LinearLayout linearLastGame4;
    private TextView textViewPlayer4;
    private TextView textViewDeck4;
    private int mCardViewFullHeightLastGamePlayed;
    private ImageView imageViewBanner;

    //TODO mediaUtils
    public static Intent getPickImageIntent(final Context context) {
        final Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return Intent.createChooser(intent, "Select picture");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(DeckActivity.this, PlayerListActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void onClickCardExpansion(View v) {
        switch (v.getId()) {
            case R.id.relativeTitleLastGamePlayed:
                toggleCardExpansion(cardViewLastGamePlayed, textTitleLastGamePlayed, iconIndicatorLastGamePlayed, relativeTitleLastGamePlayed.getHeight(), mCardViewFullHeightLastGamePlayed);
                break;

            case R.id.relativeTitleDeckHistory2:
                toggleCardExpansion(cardViewDeckHistory2, textTitleDeckHistory2, iconIndicatorDeckHistory2, relativeTitleDeckHistory2.getHeight(), mCardViewFullHeightDeckHistory2);
                break;

            case R.id.relativeTitleDeckHistory3:
                toggleCardExpansion(cardViewDeckHistory3, textTitleDeckHistory3, iconIndicatorDeckHistory3, relativeTitleDeckHistory3.getHeight(), mCardViewFullHeightDeckHistory3);
                break;

            case R.id.relativeTitleDeckHistory4:
                toggleCardExpansion(cardViewDeckHistory4, textTitleDeckHistory4, iconIndicatorDeckHistory4, relativeTitleDeckHistory4.getHeight(), mCardViewFullHeightDeckHistory4);
                break;
        }
    }

    public void onClickImageBanner(View view) {
        //TODO opcao de camera ou gallery
        startActivityForResult(getPickImageIntent(this), REQUEST_PICTURE);
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

    public void updateDonutChart2(int first, int second) {
        textViewFirst2.setText("First: " + first);
        textViewSecond2.setText("Second: " + second);

        List<double[]> values = new ArrayList<>();
        values.add(new double[]{first, second});
        values.add(new double[]{0, 0});
        values.add(new double[]{0, 0});

        List<String[]> titles = new ArrayList<>();
        titles.add(new String[]{String.valueOf(first), String.valueOf(second)});
        titles.add(new String[]{"", ""});
        titles.add(new String[]{"", ""});

        mMultipleCategorySeriesDataSet2 = new MultipleCategorySeries("");
        mMultipleCategorySeriesDataSet2.clear();
        for (int i = 0; i < values.size(); i++)
            mMultipleCategorySeriesDataSet2.add(titles.get(i), values.get(i));

        mDoughnutRender2 = new DefaultRenderer();
        for (int color : COLORS) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            mDoughnutRender2.addSeriesRenderer(r);
        }

        mDoughnutRender2.setZoomEnabled(false);
        mDoughnutRender2.setZoomButtonsVisible(false);
        mDoughnutRender2.setPanEnabled(false);
        mDoughnutRender2.setShowLegend(false);
        mDoughnutRender2.setClickEnabled(false);
        mDoughnutRender2.setScale((float) 1.2);
        mDoughnutRender2.setShowLabels(false);
        mDoughnutRender2.setDisplayValues(true);
        mDoughnutRender2.setStartAngle(225);
        mDoughnutRender2.setBackgroundColor(Color.TRANSPARENT);

        mDoughnutChartView2 = ChartFactory.getDoughnutChartView(this, mMultipleCategorySeriesDataSet2, mDoughnutRender2);

        doughnutChartLinearLayout2.removeAllViews();
        doughnutChartLinearLayout2.addView(mDoughnutChartView2);

        mDoughnutChartView2.repaint();
    }

    public void updateDonutChart3(int first, int second, int third) {
        textViewFirst3.setText("First: " + first);
        textViewSecond3.setText("Second: " + second);
        textViewThird3.setText("Third: " + third);

        List<double[]> values = new ArrayList<>();
        values.add(new double[]{first, second, third});
        values.add(new double[]{0, 0, 0});
        values.add(new double[]{0, 0, 0});

        List<String[]> titles = new ArrayList<>();
        titles.add(new String[]{String.valueOf(first), String.valueOf(second), String.valueOf(third)});
        titles.add(new String[]{"", "", ""});
        titles.add(new String[]{"", "", ""});

        mMultipleCategorySeriesDataSet3 = new MultipleCategorySeries("");
        mMultipleCategorySeriesDataSet3.clear();
        for (int i = 0; i < values.size(); i++)
            mMultipleCategorySeriesDataSet3.add(titles.get(i), values.get(i));

        mDoughnutRender3 = new DefaultRenderer();
        for (int color : COLORS) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            mDoughnutRender3.addSeriesRenderer(r);
        }

        mDoughnutRender3.setZoomEnabled(false);
        mDoughnutRender3.setZoomButtonsVisible(false);
        mDoughnutRender3.setPanEnabled(false);
        mDoughnutRender3.setShowLegend(false);
        mDoughnutRender3.setClickEnabled(false);
        mDoughnutRender3.setScale((float) 1.2);
        mDoughnutRender3.setShowLabels(false);
        mDoughnutRender3.setDisplayValues(true);
        mDoughnutRender3.setStartAngle(225);
        mDoughnutRender3.setBackgroundColor(Color.TRANSPARENT);

        mDoughnutChartView3 = ChartFactory.getDoughnutChartView(this, mMultipleCategorySeriesDataSet3, mDoughnutRender3);

        doughnutChartLinearLayout3.removeAllViews();
        doughnutChartLinearLayout3.addView(mDoughnutChartView3);

        mDoughnutChartView3.repaint();
    }

    public void updateDonutChart4(int first, int second, int third, int fourth) {
        textViewFirst4.setText("First: " + first);
        textViewSecond4.setText("Second: " + second);
        textViewThird4.setText("Third: " + third);
        textViewFourth4.setText("Fourth: " + fourth);

        List<double[]> values = new ArrayList<>();
        values.add(new double[]{first, second, third, fourth});
        values.add(new double[]{0, 0, 0, 0});
        values.add(new double[]{0, 0, 0, 0});

        List<String[]> titles = new ArrayList<>();
        titles.add(new String[]{String.valueOf(first), String.valueOf(second), String.valueOf(third), String.valueOf(fourth)});
        titles.add(new String[]{"", "", "", ""});
        titles.add(new String[]{"", "", "", ""});

        mMultipleCategorySeriesDataSet4 = new MultipleCategorySeries("");
        mMultipleCategorySeriesDataSet4.clear();
        for (int i = 0; i < values.size(); i++)
            mMultipleCategorySeriesDataSet4.add(titles.get(i), values.get(i));

        mDoughnutRender4 = new DefaultRenderer();
        for (int color : COLORS) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            mDoughnutRender4.addSeriesRenderer(r);
        }

        mDoughnutRender4.setZoomEnabled(false);
        mDoughnutRender4.setZoomButtonsVisible(false);
        mDoughnutRender4.setPanEnabled(false);
        mDoughnutRender4.setShowLegend(false);
        mDoughnutRender4.setClickEnabled(false);
        mDoughnutRender4.setScale((float) 1.2);
        mDoughnutRender4.setShowLabels(false);
        mDoughnutRender4.setDisplayValues(true);
        mDoughnutRender4.setStartAngle(225);
        mDoughnutRender4.setBackgroundColor(Color.TRANSPARENT);

        mDoughnutChartView4 = ChartFactory.getDoughnutChartView(this, mMultipleCategorySeriesDataSet4, mDoughnutRender4);

        doughnutChartLinearLayout4.removeAllViews();
        doughnutChartLinearLayout4.addView(mDoughnutChartView4);

        mDoughnutChartView4.repaint();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICTURE) {
            if (resultCode == RESULT_OK) {
                File croppedImageFile = new File(getFilesDir(), "image_" + mPlayerName + "_" + mDeckName + ".png");
                Uri croppedImageUri = Uri.fromFile(croppedImageFile);

                CropImageIntentBuilder cropImage = new CropImageIntentBuilder(16, 9, 800, 450, croppedImageUri);
                cropImage.setSourceImage(data.getData());

                startActivityForResult(cropImage.getIntent(this), REQUEST_CROP_PICTURE);
            }
        }
        if (requestCode == REQUEST_CROP_PICTURE) {
            if (resultCode == RESULT_OK) {
                File croppedImageFile = new File(getFilesDir(), "image_" + mPlayerName + "_" + mDeckName + ".png");
                imageViewBanner.setImageBitmap(BitmapFactory.decodeFile(croppedImageFile.getAbsolutePath()));
                imageViewBanner.setAdjustViewBounds(true);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.dark_primary_color));
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
        mMultipleCategorySeriesDataSet2 = (MultipleCategorySeries) savedState.getSerializable("current_series2");
        mDoughnutRender2 = (DefaultRenderer) savedState.getSerializable("current_renderer2");

        mMultipleCategorySeriesDataSet3 = (MultipleCategorySeries) savedState.getSerializable("current_series3");
        mDoughnutRender3 = (DefaultRenderer) savedState.getSerializable("current_renderer3");

        mMultipleCategorySeriesDataSet4 = (MultipleCategorySeries) savedState.getSerializable("current_series4");
        mDoughnutRender4 = (DefaultRenderer) savedState.getSerializable("current_renderer4");
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateLayout();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("current_series2", mMultipleCategorySeriesDataSet2);
        outState.putSerializable("current_renderer2", mDoughnutRender2);

        outState.putSerializable("current_series3", mMultipleCategorySeriesDataSet3);
        outState.putSerializable("current_renderer3", mDoughnutRender3);

        outState.putSerializable("current_series4", mMultipleCategorySeriesDataSet4);
        outState.putSerializable("current_renderer4", mDoughnutRender4);
    }

    private void createLayout(View view) {
        if (view != null) {
            //Deck banner image
            imageViewBanner = (ImageView) findViewById(R.id.imageViewBanner);

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

            //Chart 1v1
            cardViewDeckHistory2 = (CardView) findViewById(R.id.cardViewDeckHistory2);
            relativeTitleDeckHistory2 = (RelativeLayout) findViewById(R.id.relativeTitleDeckHistory2);
            textTitleDeckHistory2 = (TextView) findViewById(R.id.textTitleDeckHistory2);
            iconIndicatorDeckHistory2 = (ImageView) findViewById(R.id.iconIndicatorDeckHistory2);
            doughnutChartLinearLayout2 = (LinearLayout) findViewById(R.id.chart2);
            textViewTotalGameNumber2 = (TextView) view.findViewById(R.id.textViewTotalGameNumber2);
            textViewTotalGameText2 = (TextView) view.findViewById(R.id.textViewTotalGameText2);
            textViewFirst2 = (TextView) view.findViewById(R.id.textViewFirst2);
            textViewSecond2 = (TextView) view.findViewById(R.id.textViewSecond2);
            cardViewDeckHistory2.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    cardViewDeckHistory2.getViewTreeObserver().removeOnPreDrawListener(this);
                    mCardViewFullHeightDeckHistory2 = cardViewDeckHistory2.getHeight();
                    ViewGroup.LayoutParams layoutParams = cardViewDeckHistory2.getLayoutParams();
                    layoutParams.height = relativeTitleDeckHistory2.getHeight();
                    cardViewDeckHistory2.setLayoutParams(layoutParams);
                    return true;
                }
            });

            //Chart 1v1v1
            cardViewDeckHistory3 = (CardView) findViewById(R.id.cardViewDeckHistory3);
            relativeTitleDeckHistory3 = (RelativeLayout) findViewById(R.id.relativeTitleDeckHistory3);
            textTitleDeckHistory3 = (TextView) findViewById(R.id.textTitleDeckHistory3);
            iconIndicatorDeckHistory3 = (ImageView) findViewById(R.id.iconIndicatorDeckHistory3);
            doughnutChartLinearLayout3 = (LinearLayout) findViewById(R.id.chart3);
            textViewTotalGameNumber3 = (TextView) view.findViewById(R.id.textViewTotalGameNumber3);
            textViewTotalGameText3 = (TextView) view.findViewById(R.id.textViewTotalGameText3);
            textViewFirst3 = (TextView) view.findViewById(R.id.textViewFirst3);
            textViewSecond3 = (TextView) view.findViewById(R.id.textViewSecond3);
            textViewThird3 = (TextView) view.findViewById(R.id.textViewThird3);
            cardViewDeckHistory3.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    cardViewDeckHistory3.getViewTreeObserver().removeOnPreDrawListener(this);
                    mCardViewFullHeightDeckHistory3 = cardViewDeckHistory3.getHeight();
                    ViewGroup.LayoutParams layoutParams = cardViewDeckHistory3.getLayoutParams();
                    layoutParams.height = relativeTitleDeckHistory3.getHeight();
                    cardViewDeckHistory3.setLayoutParams(layoutParams);
                    return true;
                }
            });

            //Chart 1v1v1v1
            cardViewDeckHistory4 = (CardView) findViewById(R.id.cardViewDeckHistory4);
            relativeTitleDeckHistory4 = (RelativeLayout) findViewById(R.id.relativeTitleDeckHistory4);
            textTitleDeckHistory4 = (TextView) findViewById(R.id.textTitleDeckHistory4);
            iconIndicatorDeckHistory4 = (ImageView) findViewById(R.id.iconIndicatorDeckHistory4);
            doughnutChartLinearLayout4 = (LinearLayout) findViewById(R.id.chart4);
            textViewTotalGameNumber4 = (TextView) view.findViewById(R.id.textViewTotalGameNumber4);
            textViewTotalGameText4 = (TextView) view.findViewById(R.id.textViewTotalGameText4);
            textViewFirst4 = (TextView) view.findViewById(R.id.textViewFirst4);
            textViewSecond4 = (TextView) view.findViewById(R.id.textViewSecond4);
            textViewThird4 = (TextView) view.findViewById(R.id.textViewThird4);
            textViewFourth4 = (TextView) view.findViewById(R.id.textViewFourth4);

            cardViewDeckHistory4.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    cardViewDeckHistory4.getViewTreeObserver().removeOnPreDrawListener(this);
                    mCardViewFullHeightDeckHistory4 = cardViewDeckHistory4.getHeight();
                    ViewGroup.LayoutParams layoutParams = cardViewDeckHistory4.getLayoutParams();
                    layoutParams.height = relativeTitleDeckHistory4.getHeight();
                    cardViewDeckHistory4.setLayoutParams(layoutParams);
                    return true;
                }
            });

            //Chart lastGamePlayed
            cardViewLastGamePlayed = (CardView) findViewById(R.id.cardViewLastGamePlayed);
            relativeTitleLastGamePlayed = (RelativeLayout) findViewById(R.id.relativeTitleLastGamePlayed);
            iconIndicatorLastGamePlayed = (ImageView) findViewById(R.id.iconIndicatorLastGamePlayed);
            textTitleLastGamePlayed = (TextView) findViewById(R.id.textTitleLastGamePlayed);

            linearLastGame1 = (LinearLayout) findViewById(R.id.linearLastGame1);
            textViewPlayer1 = (TextView) view.findViewById(R.id.textViewPlayer1);
            textViewDeck1 = (TextView) view.findViewById(R.id.textViewDeck1);

            linearLastGame2 = (LinearLayout) findViewById(R.id.linearLastGame2);
            textViewPlayer2 = (TextView) view.findViewById(R.id.textViewPlayer2);
            textViewDeck2 = (TextView) view.findViewById(R.id.textViewDeck2);

            linearLastGame3 = (LinearLayout) findViewById(R.id.linearLastGame3);
            textViewPlayer3 = (TextView) view.findViewById(R.id.textViewPlayer3);
            textViewDeck3 = (TextView) view.findViewById(R.id.textViewDeck3);

            linearLastGame4 = (LinearLayout) findViewById(R.id.linearLastGame4);
            textViewPlayer4 = (TextView) view.findViewById(R.id.textViewPlayer4);
            textViewDeck4 = (TextView) view.findViewById(R.id.textViewDeck4);

            cardViewLastGamePlayed.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    cardViewLastGamePlayed.getViewTreeObserver().removeOnPreDrawListener(this);
                    mCardViewFullHeightLastGamePlayed = cardViewLastGamePlayed.getHeight();
                    ViewGroup.LayoutParams layoutParams = cardViewLastGamePlayed.getLayoutParams();
                    layoutParams.height = relativeTitleLastGamePlayed.getHeight();
                    cardViewLastGamePlayed.setLayoutParams(layoutParams);
                    return true;
                }
            });

        }
    }

    private void toggleCardExpansion(final CardView card, TextView title, ImageView selector, int minHeight, int maxHeight) {
        // expand
        if (card.getHeight() == minHeight) {
            title.setTextColor(this.getResources().getColor(R.color.dark_primary_color));

            Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate_90_clockwise);
            selector.startAnimation(rotation);
            selector.setColorFilter(this.getResources().getColor(R.color.dark_primary_color));

            ValueAnimator anim = ValueAnimator.ofInt(card.getMeasuredHeightAndState(), maxHeight);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = card.getLayoutParams();
                    layoutParams.height = val;
                    card.setLayoutParams(layoutParams);
                }
            });

            anim.start();

        } else {
            // collapse
            title.setTextColor(this.getResources().getColor(R.color.secondary_text));

            Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate_90_anticlockwise);
            selector.startAnimation(rotation);
            selector.setColorFilter(this.getResources().getColor(R.color.secondary_text));

            ValueAnimator anim = ValueAnimator.ofInt(card.getMeasuredHeightAndState(), minHeight);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = card.getLayoutParams();
                    layoutParams.height = val;
                    card.setLayoutParams(layoutParams);
                }
            });
            anim.start();
        }
    }

    private void updateLayout() {
        //Deck banner image
        File croppedImageFile = new File(getFilesDir(), "image_" + mPlayerName + "_" + mDeckName + ".png");
        if (croppedImageFile.isFile()) {
            imageViewBanner.setImageBitmap(BitmapFactory.decodeFile(croppedImageFile.getAbsolutePath()));
            imageViewBanner.setAdjustViewBounds(true);
        }

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

        //Record card
        List<Record> allRecords = recordsDB.getAllRecordsByDeck(currentDeck);
        cardViewLastGamePlayed.setVisibility(View.GONE);
        if (allRecords.size() != 0) {
            cardViewLastGamePlayed.setVisibility(View.VISIBLE);
            Record lastRecord = allRecords.get(allRecords.size() - 1);
            switch (lastRecord.getTotalPlayers()) {
                case 2:
                    linearLastGame1.setVisibility(View.VISIBLE);
                    textViewPlayer1.setText(lastRecord.getFirstPlace().getPlayerName());
                    textViewDeck1.setText(lastRecord.getFirstPlace().getDeckName());
                    textViewPlayer1.setTypeface(null, currentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? Typeface.BOLD : Typeface.NORMAL);
                    textViewDeck1.setTypeface(null, currentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? Typeface.BOLD : Typeface.NORMAL);

                    linearLastGame2.setVisibility(View.VISIBLE);
                    textViewPlayer2.setText(lastRecord.getSecondPlace().getPlayerName());
                    textViewDeck2.setText(lastRecord.getSecondPlace().getDeckName());
                    textViewPlayer2.setTypeface(null, currentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? Typeface.BOLD : Typeface.NORMAL);
                    textViewDeck2.setTypeface(null, currentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? Typeface.BOLD : Typeface.NORMAL);

                    linearLastGame3.setVisibility(View.GONE);
                    linearLastGame4.setVisibility(View.GONE);
                    break;

                case 3:
                    linearLastGame1.setVisibility(View.VISIBLE);
                    textViewPlayer1.setText(lastRecord.getFirstPlace().getPlayerName());
                    textViewDeck1.setText(lastRecord.getFirstPlace().getDeckName());
                    textViewPlayer1.setTypeface(null, currentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? Typeface.BOLD : Typeface.NORMAL);
                    textViewDeck1.setTypeface(null, currentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? Typeface.BOLD : Typeface.NORMAL);

                    linearLastGame2.setVisibility(View.VISIBLE);
                    textViewPlayer2.setText(lastRecord.getSecondPlace().getPlayerName());
                    textViewDeck2.setText(lastRecord.getSecondPlace().getDeckName());
                    textViewPlayer2.setTypeface(null, currentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? Typeface.BOLD : Typeface.NORMAL);
                    textViewDeck2.setTypeface(null, currentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? Typeface.BOLD : Typeface.NORMAL);

                    linearLastGame3.setVisibility(View.VISIBLE);
                    textViewPlayer3.setText(lastRecord.getThirdPlace().getPlayerName());
                    textViewDeck3.setText(lastRecord.getThirdPlace().getDeckName());
                    textViewPlayer3.setTypeface(null, currentDeck.isEqualDeck(lastRecord.getThirdPlace()) ? Typeface.BOLD : Typeface.NORMAL);
                    textViewDeck3.setTypeface(null, currentDeck.isEqualDeck(lastRecord.getThirdPlace()) ? Typeface.BOLD : Typeface.NORMAL);

                    linearLastGame4.setVisibility(View.GONE);
                    break;

                case 4:
                    linearLastGame1.setVisibility(View.VISIBLE);
                    textViewPlayer1.setText(lastRecord.getFirstPlace().getPlayerName());
                    textViewDeck1.setText(lastRecord.getFirstPlace().getDeckName());
                    textViewPlayer1.setTypeface(null, currentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? Typeface.BOLD : Typeface.NORMAL);
                    textViewDeck1.setTypeface(null, currentDeck.isEqualDeck(lastRecord.getFirstPlace()) ? Typeface.BOLD : Typeface.NORMAL);

                    linearLastGame2.setVisibility(View.VISIBLE);
                    textViewPlayer2.setText(lastRecord.getSecondPlace().getPlayerName());
                    textViewDeck2.setText(lastRecord.getSecondPlace().getDeckName());
                    textViewPlayer2.setTypeface(null, currentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? Typeface.BOLD : Typeface.NORMAL);
                    textViewDeck2.setTypeface(null, currentDeck.isEqualDeck(lastRecord.getSecondPlace()) ? Typeface.BOLD : Typeface.NORMAL);

                    linearLastGame3.setVisibility(View.VISIBLE);
                    textViewPlayer3.setText(lastRecord.getThirdPlace().getPlayerName());
                    textViewDeck3.setText(lastRecord.getThirdPlace().getDeckName());
                    textViewPlayer3.setTypeface(null, currentDeck.isEqualDeck(lastRecord.getThirdPlace()) ? Typeface.BOLD : Typeface.NORMAL);
                    textViewDeck3.setTypeface(null, currentDeck.isEqualDeck(lastRecord.getThirdPlace()) ? Typeface.BOLD : Typeface.NORMAL);

                    linearLastGame4.setVisibility(View.VISIBLE);
                    textViewPlayer4.setText(lastRecord.getFourthPlace().getPlayerName());
                    textViewDeck4.setText(lastRecord.getFourthPlace().getDeckName());
                    textViewPlayer4.setTypeface(null, currentDeck.isEqualDeck(lastRecord.getFourthPlace()) ? Typeface.BOLD : Typeface.NORMAL);
                    textViewDeck4.setTypeface(null, currentDeck.isEqualDeck(lastRecord.getFourthPlace()) ? Typeface.BOLD : Typeface.NORMAL);
                    break;
            }
        }

        //Total games and Chart2 1v1
        int firstIn2 = recordsDB.getRecordsByPosition(currentDeck, 1, 2).size();
        int secondIn2 = recordsDB.getRecordsByPosition(currentDeck, 2, 2).size();
        int total2 = firstIn2 + secondIn2;

        cardViewDeckHistory2.setVisibility(View.GONE);
        if (total2 != 0) {
            cardViewDeckHistory2.setVisibility(View.VISIBLE);
            textViewTotalGameNumber2.setText("" + total2);
            textViewTotalGameText2.setText(total2 == 1 ? "game played" : "games played");
            updateDonutChart2(firstIn2, secondIn2);
        }

        //Total games and Chart3 1v1v1
        int firstIn3 = recordsDB.getRecordsByPosition(currentDeck, 1, 3).size();
        int secondIn3 = recordsDB.getRecordsByPosition(currentDeck, 2, 3).size();
        int thirdIn3 = recordsDB.getRecordsByPosition(currentDeck, 3, 3).size();
        int total3 = firstIn3 + secondIn3 + thirdIn3;

        cardViewDeckHistory3.setVisibility(View.GONE);
        if (total3 != 0) {
            cardViewDeckHistory3.setVisibility(View.VISIBLE);
            textViewTotalGameNumber3.setText("" + total3);
            textViewTotalGameText3.setText(total3 == 1 ? "game played" : "games played");
            updateDonutChart3(firstIn3, secondIn3, thirdIn3);
        }

        //Total games and Chart4 1v1v1v1
        int firstIn4 = recordsDB.getRecordsByPosition(currentDeck, 1, 4).size();
        int secondIn4 = recordsDB.getRecordsByPosition(currentDeck, 2, 4).size();
        int thirdIn4 = recordsDB.getRecordsByPosition(currentDeck, 3, 4).size();
        int fourthIn4 = recordsDB.getRecordsByPosition(currentDeck, 4, 4).size();
        int total4 = firstIn4 + secondIn4 + thirdIn4 + fourthIn4;

        cardViewDeckHistory4.setVisibility(View.GONE);
        if (total4 != 0) {
            cardViewDeckHistory4.setVisibility(View.VISIBLE);
            textViewTotalGameNumber4.setText("" + total4);
            textViewTotalGameText4.setText(total4 == 1 ? "game played" : "games played");
            updateDonutChart4(firstIn4, secondIn4, thirdIn4, fourthIn4);
        }
    }
}
