package com.limi.andorid.vocabularyassistant.acti;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.limi.andorid.vocabularyassistant.R;
import com.limi.andorid.vocabularyassistant.helper.MySQLiteHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;


public class LearningTraceFragment extends Fragment {

    private View parentView;
    private CombinedChart mCombinedChart;
    private PieChart mPieChart;
    private ArrayList<String> weekArrayList = new ArrayList<>();
    private ArrayList<String> dateArrayList = new ArrayList<>();
    private MySQLiteHandler mySQLiteHandler;
    private Typeface tf;
    int[] MATERIAL_COLORS = {
            rgb("#2E7D32"), rgb("#43A047"), rgb("#66BB6A"), rgb("#A5D6A7")
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_learning_trace, container, false);
        MainActivity parentActivity = (MainActivity) getActivity();
        mySQLiteHandler = new MySQLiteHandler(parentView.getContext());

        mCombinedChart = (CombinedChart) parentView.findViewById(R.id.combined_chart);
        mCombinedChart.setDescription("");
        mCombinedChart.setBackgroundColor(Color.WHITE);
        mCombinedChart.setDrawGridBackground(false);
        mCombinedChart.setDrawBarShadow(false);

        // draw bars behind lines
        mCombinedChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
        });

        YAxis rightAxis = mCombinedChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = mCombinedChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        XAxis xAxis = mCombinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);


        for (int i = 0; i < 7; i++) {
            String sWeekFormat = "EEE";
            String sDateFormat = "yyyy-MM-dd";
            DateFormat weekFormat = new SimpleDateFormat(sWeekFormat, Locale.ENGLISH);
            DateFormat dateFormat = new SimpleDateFormat(sDateFormat, Locale.ENGLISH);
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, -i);
            date = calendar.getTime();

            String sWeek = weekFormat.format(date);
            String sDate = dateFormat.format(date);

            weekArrayList.add(sWeek);
            dateArrayList.add(sDate);


        }
        Collections.reverse(weekArrayList);
        Collections.reverse(dateArrayList);
        String[] mWeek = weekArrayList.toArray(new String[weekArrayList.size()]);
        CombinedData data = new CombinedData(mWeek);

        data.setData(generateLineData());
        data.setData(generateBarData());
//        data.setData(generateBubbleData());
//         data.setData(generateScatterData());
//         data.setData(generateCandleData());

        mCombinedChart.setData(data);
        mCombinedChart.invalidate();


        mPieChart = (PieChart) parentView.findViewById(R.id.pie_chart);
        mPieChart.setDescription("");


        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");

        mPieChart.setCenterTextTypeface(tf);
        mPieChart.setCenterText(generateCenterText());
        mPieChart.setCenterTextSize(10f);
        mPieChart.setCenterTextTypeface(tf);

        // radius of the center hole in percent of maximum radius
        mPieChart.setHoleRadius(45f);
        mPieChart.setTransparentCircleRadius(50f);

        Legend l = mPieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);

        mPieChart.setData(generatePieData());

        mPieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mPieChart.setRotationEnabled(true);
        mPieChart.setHighlightPerTapEnabled(true);


        return parentView;
    }

    private LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index < 7; index++) {
            int count = mySQLiteHandler.getUserWrongDateCount(MainActivity.currentUserID, dateArrayList.get(index));
            entries.add(new Entry(count, index));
        }

        LineDataSet set = new LineDataSet(entries, "Line DataSet");
        set.setColor(Color.rgb(153, 102, 0));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(153, 102, 0));
        set.setCircleRadius(5f);
        set.setFillColor(Color.rgb(153, 102, 0));
        set.setDrawCubic(true);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(153, 102, 0));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        d.addDataSet(set);

        return d;
    }

    private BarData generateBarData() {

        BarData d = new BarData();

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        for (int index = 0; index < 7; index++) {
            int count = mySQLiteHandler.getUserWordDateCount(MainActivity.currentUserID, dateArrayList.get(index));
            entries.add(new BarEntry(count, index));
        }

        BarDataSet set = new BarDataSet(entries, "Bar DataSet");
        set.setColors(MATERIAL_COLORS);
        set.setValueTextColor(Color.rgb(0, 153, 51));
        set.setValueTextSize(10f);
        d.addDataSet(set);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        return d;
    }


    private PieData generatePieData() {

        int count = 3;

        ArrayList<Entry> entries1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("Recited Words ");
        xVals.add("Wrong Words");
        xVals.add("Remaining Words");

        for (int i = 0; i < count; i++) {
            xVals.add("entry" + (i + 1));
            int totalWords = MainActivity.wordEndID - MainActivity.wordStartID + 1;
            int wordRecited = mySQLiteHandler.getUserWordCount(MainActivity.currentUserID);
            int wordRemained = totalWords - wordRecited;
            int wordWrong = mySQLiteHandler.getUserWrongCount(MainActivity.currentUserID);
            switch (i) {

                case 0:
                    entries1.add(new Entry(wordRecited, i));
                    break;
                case 1:
                    entries1.add(new Entry(wordWrong, i));
                    break;
                case 2:
                    entries1.add(new Entry(wordRemained, i));
                    break;

            }

        }

        PieDataSet ds1 = new PieDataSet(entries1, "Correct Rate");
        ds1.setColors(ColorTemplate.PASTEL_COLORS);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.BLACK);
        ds1.setValueTextSize(12f);

        com.github.mikephil.charting.data.PieData d = new PieData(xVals, ds1);
        d.setValueTypeface(tf);

        return d;
    }

    private SpannableString generateCenterText() {
        SpannableString s = new SpannableString("Correct Words Rate");
        s.setSpan(new RelativeSizeSpan(2f), 0, 8, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 8, s.length(), 0);
        return s;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public int rgb(String hex) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return Color.rgb(r, g, b);
    }
}
