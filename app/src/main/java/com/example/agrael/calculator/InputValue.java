package com.example.agrael.calculator;

import android.media.MediaCodec;
import android.os.Bundle;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by agrael on 12/7/16.
 */
public class InputValue {

    private boolean isFloatDigit;
    private boolean isNegativeDigit;
    private boolean isResult;
    private String value;

    private final String RESULT_KEY = "result";
    private final String IS_FLOAT_DIGIT_KEY = "isFloatDigit";
    private final String IS_NEG_DIGIT_KEY = "isNegativeDigit";
    private final String IS_RESULT_KEY = "isResult";

    public InputValue(){
        isFloatDigit = false;
        isNegativeDigit = false;
        value = "";
    }


    public boolean isNegativeValue(){
        return isNegativeDigit;
    }

    public String toString(){
        return value;
    }

    public boolean setValue(String value){
        if(validate(value)){
            this.value = value;
            return true;
        }
        return false;

    }

    public void setResult(boolean isResult){
        this.isResult = isResult;
        if(isResult){
            isFloatDigit = false;
        }
    }

    public void clear(){
        value = "";
        isFloatDigit = false;
        isNegativeDigit = false;
        isResult = false;
    }

    public boolean isResult(){
        return isResult;
    }

    public String addStringToValue(String str){
        if(!str.isEmpty()) {
            if (str.equals("0") && value.equals("0")) {
                isResult = false;
            }
            if (str.equals(".") && !value.isEmpty()) {
                if(!isResult) {
                    if (!isFloatDigit) {
                        isFloatDigit = true;
                        value += str;
                    }
                }
            }
            else {
                if (isResult) {
                    isResult = false;
                    value = str;
                } else {
                    value += str;
                }
            }
            return value;
        }
        return "";
    }

    public String removeLastInput(){
        if(!value.isEmpty()) {
            if(isResult){
                return "";
            }
            if (value.endsWith(".")) {
                isFloatDigit = false;
            }
            value = value.substring(0, value.length() - 1);
            return value;
        }
        return "";
    }

    public String makeNumberNegative(){
        if(!value.isEmpty()) {
            if(!isNegativeDigit) {
                isNegativeDigit = true;
                value = "-"+value;
            }
            else{
                value = value.substring(1, value.length());
                isNegativeDigit = false;
            }
        }
        return value;
    }

    public String makeInversion(){
        if(!value.isEmpty()) {
            if(!isNegativeDigit) {
                isNegativeDigit = true;
                value = "~"+value;
            }
            else{
                value = value.substring(1, value.length());
                isNegativeDigit = false;
            }
        }
        return value;
    }

    private boolean validate(String value){
        Pattern regexp = Pattern.compile("^[0-9]+\\\\.?[0-9]+");
        Matcher matcher = regexp.matcher(value);
        if(matcher.matches()){
            return true;
        }
        return true;
    }

    public Bundle getData(){
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_RESULT_KEY, isResult);
        bundle.putBoolean(IS_FLOAT_DIGIT_KEY, isFloatDigit);
        bundle.putBoolean(IS_NEG_DIGIT_KEY, isNegativeDigit);
        bundle.putString(RESULT_KEY, value);
        return bundle;
    }

    public void restoreData(Bundle bundle){
        if(bundle != null) {
            isResult = bundle.getBoolean(IS_RESULT_KEY, false);
            isNegativeDigit = bundle.getBoolean(IS_NEG_DIGIT_KEY, false);
            isFloatDigit = bundle.getBoolean(IS_FLOAT_DIGIT_KEY, false);
            value = bundle.getString(RESULT_KEY, "");
        }
    }
}
