package com.example.agrael.calculator;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by agrael on 11/10/16.
 */
public class Utils {

    static final public ArrayList<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup viewGroup = (ViewGroup) v;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            View child = viewGroup.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
    }

    static final public void pushStringFront(TextView textView, String str){

        textView.setText(str+textView.getText().toString());
    }

    static final public String removeFrontChar(String str){

        return str.substring(1, str.length());
    }

    static final public void pushStringBack(TextView textView, String str){
        textView.setText(textView.getText().toString() + str);
    }

    static final public boolean isOperator(char ch){
        if(ch == '+' || ch == '-' || ch == '*' || ch == '/'  || ch == '(' || ch == '%' || ch == '!'){
            return true;
        }
        return false;
    }

    static final public boolean isOperator(String str){
        if(str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/") ||
                str.equals("~") || str.equals("&") || str.equals("|") || str.equals("%") ||
                 str.equals("(") || str.equals(")") | str.equals("^") || str.equals("!")){
            return true;
        }
        return false;
    }

    static final public String removeLastCharacter(String source){
        return source.substring(0, source.length()-1);
    }

    static final public void showToast(Context context, String message){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        ViewGroup v = (ViewGroup) toast.getView();
        TextView tv = (TextView) v.getChildAt(0);
        tv.setTextSize(20);
        if (!toast.getView().isShown()){
            toast.show();
        }
    }
}
