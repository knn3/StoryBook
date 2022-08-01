package com.example.storbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class CaretakerStat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caretaker_stat);

        //Initialize
        BarChart chart = (BarChart) findViewById(R.id.BarChart);

        //1. make data
        BarDataSet barDataSet1 = new BarDataSet(data1(), "Data1");

        //2. create bar data
        BarData barData = new BarData();

        //3. add dataset to bar data
        barData.addDataSet(barDataSet1);

        //4. register data to bar chart
        chart.setData(barData);

    }

    //To do: insert x y values accordingly

    //data set making
    private ArrayList<BarEntry> data1(){

        ArrayList<BarEntry> dataList = new ArrayList<>();

        dataList.add(new BarEntry(0, 3));
        dataList.add(new BarEntry(1, 6));
        dataList.add(new BarEntry(2, 2));


        return dataList;
    }



}