package com.example.agrael.calculator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.HashMap;

/**
 * Created by agrael on 11/22/16.
 */
public class Converter {

    static public String convert(String value, int radixFrom, int radixTo) {
        if(!value.isEmpty()) {
            if (value.equals("0") || value.equals("0.0")) {
                return value;
            }
            value = convertToDec(value, radixFrom);
            String integerString = "";
            String floatString = "";

            if (value.lastIndexOf(".") > 0) {
                integerString = value.substring(0, value.lastIndexOf("."));
                floatString = "0." + value.subSequence(value.lastIndexOf(".") + 1, value.length()).toString();
            } else {
                floatString = "0";
                integerString = value;
            }

            String integerResult = new BigInteger(integerString).toString(radixTo).toUpperCase();
            String floatResult = "";

            double floatPart = Double.parseDouble(floatString);
            if (floatPart != 0) {
                for (int i = 0; i < 20; i++) {
                    floatPart *= radixTo;
                    String temp = String.valueOf((int) floatPart);
                    switch (temp) {
                        case "10": {
                            floatResult += "A";
                            break;
                        }
                        case "11": {
                            floatResult += "B";
                            break;
                        }
                        case "12": {
                            floatResult += "C";
                            break;
                        }
                        case "13": {
                            floatResult += "D";
                            break;
                        }
                        case "14": {
                            floatResult += "E";
                            break;
                        }
                        case "15": {
                            floatResult += "F";
                            break;
                        }
                        default: {
                            floatResult += temp;
                            break;
                        }
                    }
                    floatPart %= 1;
                    if (floatPart == 0) {
                        break;
                    }
                }
            } else {
                floatResult = "0";
            }
            int a = 0;
            return integerResult + "." + floatResult;
        }
        return value;
    }


    static public String convertToDec(String value, int radix) {
        String integerString = "";
        String floatString = "";
        if (value.lastIndexOf(".") > 0) {
            integerString = value.substring(0, value.lastIndexOf("."));
            floatString = value.subSequence(value.lastIndexOf(".") + 1, value.length()).toString();
        } else {
            floatString = "0";
            integerString = value;
        }
        String integerResult = "";
        //double floatPart = 0;
        BigDecimal floatPart = BigDecimal.ZERO;
        int divider = 1;
        switch (radix) {
            case 2: {
                divider = 2;
                break;
            }
            case 8: {
                divider = 8;
                break;
            }
            case 10: {
                return value;
            }
            case 16: {
                divider = 16;
                break;
            }
        }

        BigInteger integerPart = new BigInteger(integerString, radix);
        integerResult = integerPart.toString(10);
        for (int i = floatString.length() - 1; i >= 0; --i) {
            floatPart = getTempResult(floatPart, floatString.charAt(i));
            floatPart = floatPart.divide(new BigDecimal(String.valueOf(divider)));
        }
        floatPart = floatPart.round(MathContext.DECIMAL64);
        String temp = String.valueOf(floatPart);
        String floatResult = temp.substring(temp.lastIndexOf(".") + 1, temp.length());
        return integerResult + "." + floatResult;
    }

    static public String convertExpression(String expression, int oldRadix, int newRadix){
        String newString = "";
        if (!expression.isEmpty()) {
            HashMap<Integer, String> tokens = parseTokens(expression);
            for (int i = 0; i < tokens.size(); ++i) {
                if (!Utils.isOperator(tokens.get(i))) {
                    String temp = Converter.convert(tokens.get(i), oldRadix, newRadix);
                    newString += temp;
                } else {
                    newString += tokens.get(i);
                }
            }
        }
        return !newString.isEmpty() ? newString : convert(expression, oldRadix, newRadix);
    }

    static private HashMap<Integer, String> parseTokens(String expression){
        HashMap<Integer, String> tokens = new HashMap<>();
        int prevOp = -1;
        int index = 0;
        for(int i = 0; i<expression.length(); i++){
            if(Utils.isOperator(""+expression.charAt(i))){
                if(i!=0){
                    if(!Utils.isOperator(""+expression.charAt(i-1))){
                        tokens.put(index, expression.substring(prevOp+1, i));
                        index++;
                    }
                }
                tokens.put(index, expression.substring(i, i+1));
                prevOp = i;
                index++;
            }
        }
        if(prevOp != expression.length()-1){
            tokens.put(index, expression.substring(prevOp+1, expression.length()));
        }
        return tokens;
    }

    static private BigDecimal getTempResult(BigDecimal result, char part){
        switch(part){
            case 'A': {
                result = result.add(new BigDecimal("10"));
                break;
            }
            case 'B': {
                result = result.add(new BigDecimal("11"));
                break;
            }
            case 'C': {
                result = result.add(new BigDecimal("12"));
                break;
            }
            case 'D': {
                result = result.add(new BigDecimal("13"));
                break;
            }
            case 'E': {
                result = result.add(new BigDecimal("14"));
                break;
            }
            case 'F': {
                result = result.add(new BigDecimal("15"));
                break;
            }
            default: {
                result = result.add(new BigDecimal(String.valueOf(part-48)));
                break;
            }
        }
        return result;
    }
}
