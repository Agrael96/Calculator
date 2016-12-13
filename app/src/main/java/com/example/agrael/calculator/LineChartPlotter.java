package com.example.agrael.calculator;

import android.graphics.Color;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

import java.util.ArrayList;

/**
 * Created by agrael on 12/13/16.
 */
public class LineChartPlotter {

    private LineChart chartView;
    private double leftEnd;
    private double rightEnd;
    private ArrayList<String> functions;
    private FunctionEvaluator evaluator;
    private double divider;
    private int[] colors;

    public LineChartPlotter(LineChart v){
        chartView = v;

        evaluator = new FunctionEvaluator();

        leftEnd = -10;
        rightEnd = 10;
        functions = new ArrayList<>(3);

        functions.add("");
        functions.add("");
        functions.add("");

        divider = 1;
        colors = new int[3];
        colors[0] = Color.CYAN;
        colors[1] = Color.MAGENTA;
        colors[2] = Color.GREEN;

        chartView.setNoDataText("");
        chartView.setScaleEnabled(true);
        chartView.getAxisLeft().setTextColor(Color.WHITE);

        chartView.getAxisLeft().setTextSize(12);
        chartView.getAxisRight().setTextSize(1);
        chartView.getXAxis().setTextSize(12);

        chartView.getAxisRight().setTextColor(Color.WHITE);
        chartView.getXAxis().setTextColor(Color.WHITE);
        chartView.getLegend().setTextColor(Color.WHITE);
        chartView.getLegend().setTextSize(20);
        Description description = new Description();
        description.setText("");
        chartView.setDescription(description);
        chartView.setDoubleTapToZoomEnabled(true);
        chartView.setPinchZoom(true);

        chartView.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
                if(dX != 0){
                    dX/=divider;
                    rightEnd+=dX;
                    leftEnd+=dX;
                    try {
                        draw();
                    }catch (ValidationException e){
                        int i = 0;
                    }catch (EvaluationException e){
                        int i = 0;
                    }
                }
            }
        });
    }

    public void clearLimits(){
        leftEnd = -10;
        rightEnd = 10;
    }

    public boolean addFunction(String function, int index){
        if(index >=0 && index < 3){
            if(evaluator.validate(function)){
                functions.set(index, function);
                return true;
            }

        }
        return false;
    }

    public void draw() throws ValidationException, EvaluationException{
        if(!functions.isEmpty()) {
            ArrayList<ArrayList<Entry>> entries = new ArrayList<>();
            ArrayList<LineDataSet> sets = new ArrayList<>();
            for(int i = 0; i<functions.size(); ++i) {
                if(!functions.get(i).isEmpty()) {
                    entries.add(new ArrayList<Entry>());
                    for (double x = leftEnd; x < rightEnd; x += 0.05) {
                        Double result = Double.NaN;
                        try {
                            result = evaluator.evaluate(functions.get(i), (float) x);
                        } catch (ValidationException e) {
                            throw new ValidationException();
                        } catch (EvaluationException e) {}
                        if (!result.isNaN()) {
                            entries.get(i).add(new Entry((float) x, result.floatValue()));
                        }
                    }
                    if (!entries.get(i).isEmpty()) {
                        LineDataSet set = new LineDataSet(entries.get(i), functions.get(i));
                        set.setDrawCircles(false);
                        set.setDrawValues(false);
                        set.setLineWidth(2);
                        set.setColor(colors[i]);
                        sets.add(set);
                    }
                }
            }
            if (chartView != null) {
                if(!sets.isEmpty()){
                    LineData data = new LineData(sets.get(0));
                    for(int i = 1; i<sets.size(); ++i){
                        data.addDataSet(sets.get(i));
                    }
                    chartView.setData(data);
                    chartView.invalidate();
                }
                else{
                    throw new EvaluationException();
                }
            }
        }
    }

    public void setMotionDivider(double divider){
        this.divider = divider;
    }
}
