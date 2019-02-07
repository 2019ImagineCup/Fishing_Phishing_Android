package ensharp.imagincup2019.fishingphishing.UI.Graph;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class LineGraph {

    LineChart lineChart;
    List<Entry> entries;

    public LineGraph(LineChart _lineChart){
        this.lineChart = _lineChart;
    }

    public void setLineChart(List<Entry> _entries){
        entries = _entries;

        LineDataSet lineDataSet = new LineDataSet(entries, "Accuracy");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setCircleColorHole(Color.BLUE);
        lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(1);
        xAxis.setAxisMaximum(5);
        xAxis.mAxisRange = 5;
        xAxis.setSpaceMin(1);
        xAxis.setSpaceMax(1);
        xAxis.setGranularity(1);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);
        yLAxis.setDrawLabels(true);
        yLAxis.setDrawAxisLine(false);
        yLAxis.setDrawGridLines(false);
        yLAxis.setAxisMinimum(0);
        yLAxis.setAxisMaximum(1);
        yLAxis.setGranularity(Float.parseFloat("0.25"));

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);
        yRAxis.setDrawLabels(false);

        Description description = new Description();
        description.setText("");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
        lineChart.invalidate();
    }
}
