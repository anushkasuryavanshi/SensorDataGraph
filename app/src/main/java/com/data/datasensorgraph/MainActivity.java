
package com.data.datasensorgraph;
import android.graphics.Color;
import android.os.Bundle;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;


import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;


import java.io.*;
import java.util.*;

    public class MainActivity extends AppCompatActivity {

        LineChart lineChart;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            lineChart = findViewById(R.id.lineChart);

            writeSampleData(); // only during development

            FileProcessor processor = new FileProcessor();
            File file = new File(getFilesDir(), "sensor_data.txt");

            new Thread(() -> {
                try {
                    List<DataPoint> dataPoints = processor.readAndProcess(file);
                    runOnUiThread(() -> plotGraph(dataPoints));
                } catch (Exception e) {
                    runOnUiThread(() -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }).start();
        }

        private void plotGraph(List<DataPoint> dataPoints) {
            List<Entry> entries = new ArrayList<>();
            for (DataPoint dp : dataPoints) {
                entries.add(new Entry(dp.timestamp, (float) dp.value));
            }

            LineDataSet dataSet = new LineDataSet(entries, "Sensor A");

            // Style the line
            dataSet.setColor(Color.BLUE);
            dataSet.setLineWidth(2f);
            dataSet.setValueTextSize(10f);
            dataSet.setCircleRadius(4f);
            dataSet.setCircleColor(Color.BLUE);
            dataSet.setDrawValues(true); // Show value labels on points
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // Smooth curves

            // Customize chart
            LineChart chart = lineChart;
            chart.setData(new LineData(dataSet));
            chart.setDrawGridBackground(false);
            chart.setTouchEnabled(true);
            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);
            chart.setPinchZoom(true);
            chart.getDescription().setEnabled(false);

            chart.getAxisRight().setEnabled(false);


        }


//
        private void writeSampleData() {
            String data = "1624550000,sensorA,34.5\n" +
                    "1624550002,sensorB,22.1\n" +
                    "1624550003,sensorA,34.8\n" +
                    "1624550005,sensorC,12.5\n" +
                    "1624550006,sensorA,39.2\n";

            try {
                FileOutputStream fos = openFileOutput("sensor_data.txt", MODE_PRIVATE);
                fos.write(data.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }