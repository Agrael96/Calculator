package com.example.agrael.calculator;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.PopupMenuCompat;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartEditorFragment extends Fragment implements View.OnClickListener{

    double leftEnd;
    double rightEnd;



    LineChart chart;
    EditText function;
    ImageButton drawButton;
    ImageButton helpButton;
    float divider;
    FunctionEvaluator evaluator;
    String functionString;

    public ChartEditorFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chart, container, false);

        leftEnd = -2;
        rightEnd = 10;

        chart = (LineChart) v.findViewById(R.id.chartContainer);
        chart.setNoDataText("");
        function = (EditText) v.findViewById(R.id.function);
        drawButton = (ImageButton) v.findViewById(R.id.drawButton);
        helpButton = (ImageButton) v.findViewById(R.id.helpButton);

        Button btn = (Button) v.findViewById(R.id.menuButton);
        btn.setOnClickListener(this);

        evaluator = new FunctionEvaluator();

        drawButton.setOnClickListener(this);
        helpButton.setOnClickListener(this);

        functionString = "";

        chart.setOnChartGestureListener(new OnChartGestureListener() {
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
                    divider = (-1)*getView().getWidth()/(float)1.1;
                    dX/=divider;
                    rightEnd+=dX;
                    leftEnd+=dX;
                    drawChart(functionString);
                }
            }
        });


        return v;
    }


    private void drawChart(String function){
        if(!function.isEmpty()) {
            ArrayList<Entry> entries = new ArrayList<>();
            for (double x = leftEnd; x < rightEnd; x += 0.05) {
                Double result = Double.NaN;
                try{
                    result = evaluator.evaluate(function, (float) x);
                }catch (ValidationException e) {
                    Utils.showToast(getContext(), e.what());
                    return;
                }catch (EvaluationException e){}
                if(!result.isNaN()) {
                    entries.add(new Entry((float) x, result.floatValue()));
                }
            }
            if (!entries.isEmpty()) {
                LineDataSet set = new LineDataSet(entries, function);
                set.setDrawCircles(false);
                set.setDrawValues(false);
                set.setLineWidth(2);
                LineData data = new LineData(set);
                if (chart != null) {
                    chart.setScaleEnabled(true);
                    chart.getAxisLeft().setTextColor(Color.WHITE);
                    chart.getAxisRight().setTextColor(Color.WHITE);
                    chart.getXAxis().setTextColor(Color.WHITE);
                    chart.getLegend().setTextColor(Color.WHITE);
                    chart.getLegend().setTextSize(20);
                    Description description = new Description();
                    description.setText("");
                    chart.setDescription(description);
                    chart.setDoubleTapToZoomEnabled(true);
                    chart.setPinchZoom(true);
                    chart.setData(data);
                    chart.invalidate();

                    functionString = function;
                }
            }
            else{
                Utils.showToast(getContext(), getString(R.string.evaluationError));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.drawButton: {
                String expression = function.getText().toString();
                leftEnd = -2;
                rightEnd = 10;
                drawChart(expression);
                break;
            }
            case R.id.helpButton: {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View view = inflater.inflate(R.layout.fragment_help_dialog, null);
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("Help");
                dialog.setView(view);
                dialog.setCancelable(false);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
                break;
            }
            case R.id.menuButton: {
                PopupMenu popupMenu = new PopupMenu(getContext(), v);
                popupMenu.inflate(R.menu.popupmenu);
                popupMenu
                        .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                // Toast.makeText(PopupMenuDemoActivity.this,
                                // item.toString(), Toast.LENGTH_LONG).show();
                                // return true;
                                switch (item.getItemId()) {

                                    case R.id.drawMenu:
                                        Utils.showToast(getContext(), "1");
                                        return true;
                                    case R.id.helpMenu:
                                        Utils.showToast(getContext(), "2");
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                popupMenu.show();
                break;
            }
        }
    }
}
