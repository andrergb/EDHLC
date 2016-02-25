package com.android.argb.edhlc.chart;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.argb.edhlc.R;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * -Created by agbarros on 25/02/2016.
 */
public class DonutChart {
    private Activity context;
    private DefaultRenderer mDoughnutRender;
    private MultipleCategorySeries mMultipleCategorySeriesDataSet;

    public DonutChart(Activity activity, DefaultRenderer mDoughnutRender, MultipleCategorySeries mMultipleCategorySeriesDataSet) {
        this.context = activity;
        this.mDoughnutRender = mDoughnutRender;
        this.mMultipleCategorySeriesDataSet = mMultipleCategorySeriesDataSet;
    }

    public void updateDonutChart(int[] data) {
        switch (data.length) {
            case 2:
                updateDonutChart2(data);
                break;
            case 3:
                updateDonutChart3(data);
                break;
            case 4:
                updateDonutChart4(data);
                break;
        }
    }

    public void updateDonutChart2(int[] data) {
        TextView textTotalGames1Chart2Slots = (TextView) context.findViewById(R.id.textTotalGames1Chart2Slots);
        TextView textTotalGames2Chart2Slots = (TextView) context.findViewById(R.id.textTotalGames2Chart2Slots);
        TextView textFirstChart2Slots = (TextView) context.findViewById(R.id.textFirstChart2Slots);
        TextView textSecondChart2Slots = (TextView) context.findViewById(R.id.textSecondChart2Slots);
        LinearLayout linearChart2Slots = (LinearLayout) context.findViewById(R.id.linearChart2Slots);
        int[] COLORS = new int[]{ContextCompat.getColor(context, R.color.first),
                ContextCompat.getColor(context, android.R.color.transparent),
                ContextCompat.getColor(context, R.color.second),
                ContextCompat.getColor(context, android.R.color.transparent)};
        int first = data[0];
        int second = data[1];
        double total = (double) (first + second);

        textTotalGames1Chart2Slots.setText("" + (int) total);
        textTotalGames2Chart2Slots.setText(total == 1 ? "game played" : "games played");
        textFirstChart2Slots.setText("First: " + first);
        textSecondChart2Slots.setText("Second: " + second);

        List<double[]> values = new ArrayList<>();
        double emptySpace = (total == first || total == second) ? 0 : total * 0.005;
        values.add(new double[]{first, first > 0 ? emptySpace : 0, second, second > 0 ? emptySpace : 0});
        values.add(new double[]{0, 0, 0, 0});
        values.add(new double[]{0, 0, 0, 0});

        List<String[]> titles = new ArrayList<>();
        titles.add(new String[]{String.valueOf(first), "", String.valueOf(second), ""});
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

        GraphicalView mDoughnutChartView2 = ChartFactory.getDoughnutChartView(context, mMultipleCategorySeriesDataSet, mDoughnutRender);

        linearChart2Slots.removeAllViews();
        linearChart2Slots.addView(mDoughnutChartView2);

        mDoughnutChartView2.repaint();
    }

    public void updateDonutChart3(int[] data) {
        TextView textTotalGames1Chart3Slots = (TextView) context.findViewById(R.id.textTotalGames1Chart3Slots);
        TextView textTotalGames2Chart3Slots = (TextView) context.findViewById(R.id.textTotalGames2Chart3Slots);
        TextView textFirstChart3Slots = (TextView) context.findViewById(R.id.textFirstChart3Slots);
        TextView textSecondChart3Slots = (TextView) context.findViewById(R.id.textSecondChart3Slots);
        TextView textThirdChart3Slots = (TextView) context.findViewById(R.id.textThirdChart3Slots);
        LinearLayout linearChart3Slots = (LinearLayout) context.findViewById(R.id.linearChart3Slots);
        int[] COLORS = new int[]{ContextCompat.getColor(context, R.color.first),
                ContextCompat.getColor(context, android.R.color.transparent),
                ContextCompat.getColor(context, R.color.second),
                ContextCompat.getColor(context, android.R.color.transparent),
                ContextCompat.getColor(context, R.color.third),
                ContextCompat.getColor(context, android.R.color.transparent)};
        int first = data[0];
        int second = data[1];
        int third = data[2];
        double total = (double) (first + second + third);

        textTotalGames1Chart3Slots.setText("" + (int) total);
        textTotalGames2Chart3Slots.setText(total == 1 ? "game played" : "games played");
        textFirstChart3Slots.setText("First: " + first);
        textSecondChart3Slots.setText("Second: " + second);
        textThirdChart3Slots.setText("Third: " + third);

        List<double[]> values = new ArrayList<>();
        double emptySpace = (total == first || total == second || total == third) ? 0 : total * 0.005;
        values.add(new double[]{first, first > 0 ? emptySpace : 0,
                second, second > 0 ? emptySpace : 0,
                third, third > 0 ? emptySpace : 0});
        values.add(new double[]{0, 0, 0, 0, 0, 0});
        values.add(new double[]{0, 0, 0, 0, 0, 0});

        List<String[]> titles = new ArrayList<>();
        titles.add(new String[]{String.valueOf(first), "", String.valueOf(second), "", String.valueOf(third), ""});
        titles.add(new String[]{"", "", "", "", "", ""});
        titles.add(new String[]{"", "", "", "", "", ""});

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

        GraphicalView mDoughnutChartView3 = ChartFactory.getDoughnutChartView(context, mMultipleCategorySeriesDataSet, mDoughnutRender);

        linearChart3Slots.removeAllViews();
        linearChart3Slots.addView(mDoughnutChartView3);

        mDoughnutChartView3.repaint();
    }

    public void updateDonutChart4(int[] data) {
        TextView textTotalGames1Chart4Slots = (TextView) context.findViewById(R.id.textTotalGames1Chart4Slots);
        TextView textTotalGames2Chart4Slots = (TextView) context.findViewById(R.id.textTotalGames2Chart4Slots);
        TextView textFirstChart4Slots = (TextView) context.findViewById(R.id.textFirstChart4Slots);
        TextView textSecondChart4Slots = (TextView) context.findViewById(R.id.textSecondChart4Slots);
        TextView textThirdChart4Slots = (TextView) context.findViewById(R.id.textThirdChart4Slots);
        TextView textFourthChart4Slots = (TextView) context.findViewById(R.id.textFourthChart4Slots);
        LinearLayout linearChart4Slots = (LinearLayout) context.findViewById(R.id.linearChart4Slots);
        int[] COLORS = new int[]{ContextCompat.getColor(context, R.color.first),
                ContextCompat.getColor(context, android.R.color.transparent),
                ContextCompat.getColor(context, R.color.second),
                ContextCompat.getColor(context, android.R.color.transparent),
                ContextCompat.getColor(context, R.color.third),
                ContextCompat.getColor(context, android.R.color.transparent),
                ContextCompat.getColor(context, R.color.fourth),
                ContextCompat.getColor(context, android.R.color.transparent)};
        int first = data[0];
        int second = data[1];
        int third = data[2];
        int fourth = data[3];
        double total = (double) (first + second + third + fourth);

        textTotalGames1Chart4Slots.setText("" + (int) total);
        textTotalGames2Chart4Slots.setText(total == 1 ? "game played" : "games played");
        textFirstChart4Slots.setText("First: " + first);
        textSecondChart4Slots.setText("Second: " + second);
        textThirdChart4Slots.setText("Third: " + third);
        textFourthChart4Slots.setText("Fourth: " + fourth);

        List<double[]> values = new ArrayList<>();
        double emptySpace = (total == first || total == second || total == third || total == fourth) ? 0 : total * 0.005;
        values.add(new double[]{first, first > 0 ? emptySpace : 0,
                second, second > 0 ? emptySpace : 0,
                third, third > 0 ? emptySpace : 0,
                fourth, fourth > 0 ? emptySpace : 0});
        values.add(new double[]{0, 0, 0, 0, 0, 0, 0, 0});
        values.add(new double[]{0, 0, 0, 0, 0, 0, 0, 0});

        List<String[]> titles = new ArrayList<>();
        titles.add(new String[]{String.valueOf(first), "", String.valueOf(second), "", String.valueOf(third), "", String.valueOf(fourth), ""});
        titles.add(new String[]{"", "", "", "", "", "", "", ""});
        titles.add(new String[]{"", "", "", "", "", "", "", ""});

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

        GraphicalView mDoughnutChartView4 = ChartFactory.getDoughnutChartView(context, mMultipleCategorySeriesDataSet, mDoughnutRender);

        linearChart4Slots.removeAllViews();
        linearChart4Slots.addView(mDoughnutChartView4);

        mDoughnutChartView4.repaint();
    }
}
