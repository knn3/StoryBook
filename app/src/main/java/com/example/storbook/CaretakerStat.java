package com.example.storbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;


import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;




import java.util.ArrayList;
import java.util.List;

public class CaretakerStat extends AppCompatActivity {

    ArrayList<String> Titles;
    ArrayList<String> clickedtimes;
    ArrayList barArrayList;
    ArrayList<Integer> clickedTimeInt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caretaker_stat);

        Titles = ((global) this.getApplication()).picutreTitles;
        clickedtimes = ((global) this.getApplication()).pictureTimeClicked;
        clickedTimeInt = ((global) this.getApplication()).pictureTimeClickedInt;

        BarChart chart = findViewById(R.id.BarChart);

        getData();

        BarDataSet bardataset = new BarDataSet(barArrayList,"Time clicked");

        BarData barData = new BarData(bardataset);

        chart.setData(barData);
        //Set color bar
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

        bardataset.setValueTextColor(Color.BLACK);

        bardataset.setValueTextSize(16f);

        chart.getDescription().setEnabled(true);

//        chart.setOnChartValueSelectedListener(new barChartOnChartValueSelectedListener());

    }

    private void getData () {
        barArrayList = new ArrayList();
        //for (int i = 0; i < Titles.size(); i++){
        barArrayList.add(new BarEntry(2f, clickedTimeInt.get(0)));
        //}
    }

//    private void initBarChart(BarChart barChart){
//        //hiding the grey background of the chart, default false if not set
//        barChart.setDrawGridBackground(false);
//        //remove the bar shadow, default false if not set
//        barChart.setDrawBarShadow(false);
//        //remove border of the chart, default false if not set
//        barChart.setDrawBorders(false);
//
//        //remove the description label text located at the lower right corner
//        Description description = new Description();
//        description.setEnabled(false);
//        barChart.setDescription(description);
//
//        //setting animation for y-axis, the bar will pop up from 0 to its value within the time we set
//        barChart.animateY(1000);
//        //setting animation for x-axis, the bar will pop up separately within the time we set
//        barChart.animateX(1000);
//
//        XAxis xAxis = barChart.getXAxis();
//        //change the position of x-axis to the bottom
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        //set the horizontal distance of the grid line
//        xAxis.setGranularity(1f);
//        //hiding the x-axis line, default true if not set
//        xAxis.setDrawAxisLine(false);
//        //hiding the vertical grid lines, default true if not set
//        xAxis.setDrawGridLines(false);
//
//
//
//        //NOT SURE ABOUT THIS PART
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(Titles));
//
//
//
//
//
//        YAxis leftAxis = barChart.getAxisLeft();
//        //hiding the left y-axis line, default true if not set
//        leftAxis.setDrawAxisLine(false);
//
//
//
//        YAxis rightAxis = barChart.getAxisRight();
//        //hiding the right y-axis line, default true if not set
//        rightAxis.setDrawAxisLine(false);
//
//        Legend legend = barChart.getLegend();
//        //setting the shape of the legend form to line, default square shape
//        legend.setForm(Legend.LegendForm.LINE);
//        //setting the text size of the legend
//        legend.setTextSize(11f);
//        //setting the alignment of legend toward the chart
//        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//        //setting the stacking direction of legend
//        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        //setting the location of legend outside the chart, default false if not set
//        legend.setDrawInside(false);
//
//    }
//
//    private class barChartOnChartValueSelectedListener implements OnChartValueSelectedListener{
//
//        @Override
//        public void onValueSelected(Entry e, Highlight h) {
//            //trigger activity when the bar value is selected
//
//        }
//
//        @Override
//        public void onNothingSelected() {
//
//        }
//    }

}
