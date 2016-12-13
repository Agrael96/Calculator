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
import android.widget.RadioButton;
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

    private EditText function;
    private ImageButton menuButton;
    private RadioButton firstFunc;
    private RadioButton secondFunc;
    private RadioButton thirdFunc;

    private FunctionEvaluator evaluator;
    private LineChartPlotter plotter;
    private ArrayList<String> functions;
    private int currentFunc;

    public ChartEditorFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chart, container, false);

        function = (EditText) v.findViewById(R.id.function);
        menuButton = (ImageButton) v.findViewById(R.id.menuButton);

        firstFunc  = (RadioButton) v.findViewById(R.id.firstFunc);
        secondFunc = (RadioButton) v.findViewById(R.id.secondFunc);
        thirdFunc  = (RadioButton) v.findViewById(R.id.thirdFunc);

        firstFunc.setOnClickListener(this);
        secondFunc.setOnClickListener(this);
        thirdFunc.setOnClickListener(this);
        menuButton.setOnClickListener(this);

        currentFunc = 0;
        evaluator = new FunctionEvaluator();
        plotter = new LineChartPlotter((LineChart) v.findViewById(R.id.chartContainer));

        functions = new ArrayList<>();
        functions.add("");
        functions.add("");
        functions.add("");

        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menuButton: {
                PopupMenu popupMenu = new PopupMenu(getContext(), v);
                popupMenu.inflate(R.menu.popupmenu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.drawMenu:
                                plotter.setMotionDivider((-1)*getView().getWidth()/1.1);
                                plotter.clearLimits();
                                functions.set(currentFunc, function.getText().toString());
                                for(int i = 0; i< functions.size(); ++i){
                                    plotter.addFunction(functions.get(i), i);
                                }
                                try {
                                    plotter.draw();
                                } catch (ValidationException e) {
                                    Utils.showToast(getContext(), e.what());
                                } catch (EvaluationException e) {
                                    Utils.showToast(getContext(), e.what());
                                }
                                return true;
                            case R.id.helpMenu:
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
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
                break;
            }
            case R.id.firstFunc: {
                functions.set(currentFunc, function.getText().toString());
                currentFunc = 0;
                function.setText(functions.get(currentFunc));
                break;
            }
            case R.id.secondFunc: {
                functions.set(currentFunc, function.getText().toString());
                currentFunc = 1;
                function.setText(functions.get(currentFunc));
                break;
            }
            case R.id.thirdFunc: {
                functions.set(currentFunc, function.getText().toString());
                currentFunc = 2;
                function.setText(functions.get(currentFunc));
                break;
            }
        }
    }
}
