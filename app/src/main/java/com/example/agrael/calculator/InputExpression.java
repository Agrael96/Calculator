package com.example.agrael.calculator;

import android.os.Bundle;

/**
 * Created by agrael on 12/7/16.
 */
public class InputExpression {

    private int openBracketCount;
    private boolean isCloseBracketPresent;
    private boolean isLastOperator;
    private String expression;

    private final String OPEN_BRACKET_COUNT_KEY = "openBracketCount";
    private final String IS_CLOSE_BRACKET_PRESENT_KEY = "isCloseBracketPresent";
    private final String IS_LAST_OPERATOR_KEY = "isLastOperator";
    private final String EXPRESSION_KEY = "expressionKey";

    public InputExpression(){
        openBracketCount = 0;
        isCloseBracketPresent = false;
        isLastOperator = false;
        expression = "";
    }

    public String toString(){
        return expression;
    }

    private String removeLastCharacter(String source){
        if(source.charAt(source.length()-1) == '(') {
            openBracketCount--;
        }
        else if(source.charAt(source.length()-1) == ')'){
            openBracketCount++;
            isCloseBracketPresent = false;
        }
        return Utils.removeLastCharacter(source);
    }

    public String addValueToExpression(InputValue value){
        if(value.isNegativeValue()){
            expression+="(";
            expression+=value.toString();
            expression+=")";
        }
        else{
            expression+=value.toString();
        }
        return expression;
    }

    public boolean isLastCharacterOperator(String string){
        return Utils.isOperator(string.charAt(string.length()-1));
    }

    private String replaceOperator(String newOperator){
        boolean replaceOperator = false;
        if (!expression.isEmpty()) {
            if (isLastCharacterOperator(expression)) {
                if(expression.charAt(expression.length()-1) == '('){
                    expression+="0"+newOperator;
                }
                expression = removeLastCharacter(expression);
                replaceOperator = true;
            }
        }
        if (replaceOperator) {
            expression+=newOperator;
        }
        return expression;
    }

    public String addTokenToExpression(String operator, InputValue value){
        if(!operator.equals(")")) {
            if (value.isResult() && isLastCharacterOperator(expression)) {
                expression = replaceOperator(operator);
                return expression;
            }
            if (!value.toString().isEmpty()) {
                if (isCloseBracketPresent) {
                    if (value.isResult()) {
                        expression += operator;
                        isCloseBracketPresent = false;
                        isLastOperator = true;
                    } else {
                        expression += operator;
                        expression = addValueToExpression(value);

                    }
                } else {
                    expression = addValueToExpression(value);
                    expression += operator;
                    isLastOperator = true;
                }
            }
        }
        else{
            if(openBracketCount > 0) {
                if (!value.toString().isEmpty()) {
                    addValueToExpression(value);
                }
                if (expression.endsWith("(")) {
                    addStringToExpression("0)");
                } else {
                    addStringToExpression(")");
                }
                isCloseBracketPresent = true;
                openBracketCount--;
                isLastOperator = false;
            }
        }
        return expression;
    }

    public String addStringToExpression(String string){
        if(string.equals("(")){
            openBracketCount++;
        }
        if(!Utils.isOperator(string)){
            isLastOperator = false;
        }
        expression+=string;
        return expression;
    }

    public void clear(){
        expression = "";
        isCloseBracketPresent = false;
        isLastOperator = false;
        openBracketCount = 0;
    }

    public String getEvaluableString(){
        String newExpression = expression;
        if(!isCloseBracketPresent && isLastOperator){
            newExpression = newExpression.substring(0, newExpression.length()-1);
        }
        if(openBracketCount != 0) {
            int count = 0;
            for (int i = 0; i < newExpression.length(); ++i) {
                if (newExpression.charAt(i) == '(') {
                    count++;
                }
                if (count == openBracketCount) {
                    count = i;
                    break;
                }
            }
            newExpression = newExpression.substring(++count, newExpression.length());
        }
        return newExpression;
    }

    public String addFunctionToExpression(String func, InputValue value){
        if(!value.toString().isEmpty()){
            if(!expression.isEmpty()){
                if(expression.endsWith(")")){
                    if(value.isResult()) {
                        int count = 0;
                        for (int i = expression.length() - 1; i >= 0; --i) {
                            if (expression.charAt(i) == ')') {
                                count++;
                            }
                            if (expression.charAt(i) == '(') {
                                count--;
                            }
                            if (count == 0) {
                                String exp = expression.substring(0, i);
                                if(endWithFunction(exp)) {
                                    if(expression.endsWith("0")){
                                        i -= 5;
                                    }
                                    else if (exp.endsWith("r") || exp.endsWith("d") || exp.endsWith("t")) {
                                        i -= 4;
                                    } else{
                                        i -= 3;
                                    }
                                }
                                expression = expression.substring(0, i) + func + "("
                                        + expression.substring(i, expression.length())
                                        + ")";
                                isCloseBracketPresent = true;
                                return expression;
                            }
                        }
                    }
                    else{
                        expression+="*"+func+"("+value.toString()+")";
                        isCloseBracketPresent = true;
                        return expression;
                    }
                }
            }
            expression += func+"("+value.toString()+")";
            isCloseBracketPresent = true;
        }
        return expression;
    }

    public Bundle getData(){
        Bundle bundle = new Bundle();
        bundle.putString(EXPRESSION_KEY, expression);
        bundle.putBoolean(IS_LAST_OPERATOR_KEY, isLastOperator);
        bundle.putBoolean(IS_CLOSE_BRACKET_PRESENT_KEY, isCloseBracketPresent);
        bundle.putInt(OPEN_BRACKET_COUNT_KEY, openBracketCount);
        return bundle;
    }

    public void restoreData(Bundle bundle){
        if(bundle != null) {
            isCloseBracketPresent = bundle.getBoolean(IS_CLOSE_BRACKET_PRESENT_KEY, false);
            isLastOperator = bundle.getBoolean(IS_LAST_OPERATOR_KEY, false);
            openBracketCount = bundle.getInt(OPEN_BRACKET_COUNT_KEY, 0);
            expression = bundle.getString(EXPRESSION_KEY, "");
        }
    }

    public boolean endWithFunction(String str){
        if(str.endsWith("log10") || str.endsWith("log") || str.endsWith("d") || str.endsWith("fac")
                                 || str.endsWith("r") || str.endsWith("exp") || str.endsWith("sqrt")){
            return true;
        }
        return false;
    }

    public void setExpression(String exp){
        expression = exp;
    }
}
