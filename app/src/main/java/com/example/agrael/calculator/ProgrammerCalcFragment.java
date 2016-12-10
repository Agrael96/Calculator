package com.example.agrael.calculator;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;


public class ProgrammerCalcFragment extends Fragment implements View.OnClickListener {


    private final String MEMORY_KEY = "memoryKey";
    private final String EXPRESSION_KEY = "expressionKey";
    private final String VALUE_KEY = "valueKey";



    private BigDecimal memory;
    private TextView resultText;
    private TextView expressionText;
    private boolean isScrollMoveSet;
    private int oldRadix;

    private InputValue value;
    private InputExpression expression;

    private ProgrammerEvaluator evaluator;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_programmer_calc, container, false);

        memory = BigDecimal.ZERO;
        isScrollMoveSet = false;
        oldRadix = 10;

        value = new InputValue();
        expression = new InputExpression();


        resultText = (TextView) v.findViewById(R.id.resultText);
        expressionText = (TextView) v.findViewById(R.id.expressionText);

        evaluator = new ProgrammerEvaluator();

        ArrayList<View> views = Utils.getAllChildren(v.getRootView());

        for (View view : views) {
            if (view instanceof Button || view instanceof RadioButton) {
                view.setOnClickListener(this);
            }
        }

        restoreData(getArguments());

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.binButton: {
                setBinSystem();
                updateInputData(value, expression, 2);
                break;
            }
            case R.id.decButton: {
                setDecSystem();
                updateInputData(value, expression, 10);
                break;
            }
            case R.id.octButton: {
                setOctSystem();
                updateInputData(value, expression, 8);
                break;
            }
            case R.id.hexButton: {
                setHexSystem();
                updateInputData(value, expression, 16);
                break;
            }
            case R.id.zeroButton: {
                resultText.setText(value.addStringToValue("0"));
                break;
            }
            case R.id.oneButton: {
                resultText.setText(value.addStringToValue("1"));
                break;
            }
            case R.id.twoButton: {
                resultText.setText(value.addStringToValue("2"));
                break;
            }
            case R.id.threeButton: {
                resultText.setText(value.addStringToValue("3"));
                break;
            }
            case R.id.fourButton: {
                resultText.setText(value.addStringToValue("4"));
                break;
            }
            case R.id.fiveButton: {
                resultText.setText(value.addStringToValue("5"));
                break;
            }
            case R.id.sixButton: {
                resultText.setText(value.addStringToValue("6"));
                break;
            }
            case R.id.sevenButton: {
                resultText.setText(value.addStringToValue("7"));
                break;
            }
            case R.id.eightButton: {
                resultText.setText(value.addStringToValue("8"));
                break;
            }
            case R.id.nineButton: {
                resultText.setText(value.addStringToValue("9"));
                break;
            }
            case R.id.aButton: {
                resultText.setText(value.addStringToValue("A"));
                break;
            }
            case R.id.bButton: {
                resultText.setText(value.addStringToValue("B"));
                break;
            }
            case R.id.cButton: {
                resultText.setText(value.addStringToValue("C"));
                break;
            }
            case R.id.dButton: {
                resultText.setText(value.addStringToValue("D"));
                break;
            }
            case R.id.eButton: {
                resultText.setText(value.addStringToValue("E"));
                break;
            }
            case R.id.fButton: {
                resultText.setText(value.addStringToValue("F"));
                break;
            }
            case R.id.dotButton: {
                resultText.setText(value.addStringToValue("."));
                break;
            }
            case R.id.resultButton: {
                if(expression.toString().isEmpty()){
                    value.setResult(true);
                    break;
                }
                if(!value.toString().isEmpty() && !value.isResult()){
                    expression.addStringToExpression(value.toString());
                }
                expressionText.setText("");

                String result = "";
                try {
                    String exp = expression.getEvaluableString();
                    exp = Converter.convertExpression(exp, oldRadix, 10);
                    result = evaluator.evaluate(exp);
                    result = Converter.convert(result, 10, oldRadix);
                    resultText.setText(result);
                    value.setValue(result);
                } catch (Exception e) {
                    Utils.showToast(getContext(), getString(R.string.evaluationError));
                }


                value.setResult(true);
                expression.clear();
                break;
            }
            case R.id.plusButton: {
                addOperatorToExpression("+");
                break;
            }
            case R.id.minusButton: {
                addOperatorToExpression("-");
                break;
            }
            case R.id.multiplyButton: {
                addOperatorToExpression("*");
                break;
            }
            case R.id.divisionButton: {
                addOperatorToExpression("/");
                break;
            }
            case R.id.negativeButton: {
                resultText.setText(value.makeNumberNegative());
                break;
            }
            case R.id.clearButton: {
                resultText.setText("");
                expressionText.setText("");
                value.clear();
                expression.clear();
                break;
            }
            case R.id.memoryClearButton: {
                memory = BigDecimal.ZERO;
                break;
            }
            case R.id.memoryPlusButton: {
                if (!value.toString().isEmpty()) {
                    memory = memory.add(new BigDecimal(Converter.convert(value.toString(), oldRadix, 10)));
                }
                break;
            }
            case R.id.memoryReadButton: {
                String memoryString = Converter.convert(memory.toString(), 10, oldRadix);
                resultText.setText(memoryString);
                value.setValue(memoryString);
                value.setResult(true);
                break;
            }
            case R.id.openBracketButton: {
                if(!expression.toString().isEmpty()) {
                    if (expression.toString().charAt(expression.toString().length() - 1) == ')') {
                        return;
                    }
                }
                expressionText.setText(expression.addStringToExpression("("));
                break;
            }
            case R.id.closeBracketButton: {
                expressionText.setText(expression.addTokenToExpression(")", value));
                setResult();
                break;
            }
            case R.id.andButton: {
                addOperatorToExpression("&");
                break;
            }
            case R.id.orButton: {
                addOperatorToExpression("|");
                break;
            }
            case R.id.xorButton: {
                addOperatorToExpression("^");
                break;
            }
            case R.id.modButton: {
                addOperatorToExpression("%");
                break;
            }
            case R.id.inversionButton: {
                resultText.setText(value.makeInversion());
                break;
            }

            case R.id.deleteButton: {
                resultText.setText(value.removeLastInput());
                break;
            }
        }
        if (!isScrollMoveSet) {
            isScrollMoveSet = true;
            resultText.setMovementMethod(new ScrollingMovementMethod());
            expressionText.setMovementMethod(new ScrollingMovementMethod());
        }
    }

    private void setHexSystem() {
        setAllButtonEnabled();
    }

    private void setOctSystem() {
        setAllButtonEnabled();
        this.getView().findViewById(R.id.aButton).setEnabled(false);
        this.getView().findViewById(R.id.bButton).setEnabled(false);
        this.getView().findViewById(R.id.cButton).setEnabled(false);
        this.getView().findViewById(R.id.dButton).setEnabled(false);
        this.getView().findViewById(R.id.eButton).setEnabled(false);
        this.getView().findViewById(R.id.fButton).setEnabled(false);
        this.getView().findViewById(R.id.nineButton).setEnabled(false);
        this.getView().findViewById(R.id.eightButton).setEnabled(false);
    }

    private void setDecSystem() {
        setAllButtonEnabled();
        this.getView().findViewById(R.id.aButton).setEnabled(false);
        this.getView().findViewById(R.id.bButton).setEnabled(false);
        this.getView().findViewById(R.id.cButton).setEnabled(false);
        this.getView().findViewById(R.id.dButton).setEnabled(false);
        this.getView().findViewById(R.id.eButton).setEnabled(false);
        this.getView().findViewById(R.id.fButton).setEnabled(false);
    }

    private void setBinSystem() {
        setAllButtonEnabled();
        this.getView().findViewById(R.id.aButton).setEnabled(false);
        this.getView().findViewById(R.id.bButton).setEnabled(false);
        this.getView().findViewById(R.id.cButton).setEnabled(false);
        this.getView().findViewById(R.id.dButton).setEnabled(false);
        this.getView().findViewById(R.id.eButton).setEnabled(false);
        this.getView().findViewById(R.id.fButton).setEnabled(false);
        this.getView().findViewById(R.id.nineButton).setEnabled(false);
        this.getView().findViewById(R.id.eightButton).setEnabled(false);
        this.getView().findViewById(R.id.sevenButton).setEnabled(false);
        this.getView().findViewById(R.id.sixButton).setEnabled(false);
        this.getView().findViewById(R.id.fiveButton).setEnabled(false);
        this.getView().findViewById(R.id.fourButton).setEnabled(false);
        this.getView().findViewById(R.id.threeButton).setEnabled(false);
        this.getView().findViewById(R.id.twoButton).setEnabled(false);
    }

    private void setAllButtonEnabled() {
        ArrayList<View> views = Utils.getAllChildren(this.getView());
        for (View v : views) {
            if (v instanceof Button) {
                v.setEnabled(true);
            }
        }
    }


    private void setResult() {
        try{
            String exp = expression.getEvaluableString();
            exp = Converter.convertExpression(exp, oldRadix, 10);
            String result = evaluator.evaluate(exp);
            result = Converter.convert(result,10, oldRadix);
            value.setValue(result);
            value.setResult(true);
            resultText.setText(value.toString());
        }catch (Exception e){
            Utils.showToast(getContext(), getString(R.string.evaluationError));
        }
    }

    private void updateInputData(InputValue result, InputExpression expression, int radix) {
        String resultString = Converter.convert(result.toString(), oldRadix, radix);
        resultText.setText(resultString);
        value.setValue(resultString);

        String expressionString = Converter.convertExpression(expression.toString(), oldRadix, radix);
        expressionText.setText(expressionString);
        expression.setExpression(expressionString);
        oldRadix = radix;
    }



    private void addOperatorToExpression(String operator) {
        expressionText.setText(expression.addTokenToExpression(operator, value));
        if(!expression.toString().isEmpty()){
            setResult();
        }
    }

    public Bundle getData(){

        Bundle bundle = new Bundle();

        expression.setExpression(Converter.convertExpression(expression.toString(), oldRadix, 10));
        value.setValue(Converter.convert(value.toString(), oldRadix, 10));

        bundle.putBundle(EXPRESSION_KEY, expression.getData());
        bundle.putBundle(VALUE_KEY, value.getData());
        bundle.putString(MEMORY_KEY, memory.toString());

        return bundle;
    }

    private void restoreData(@Nullable Bundle bundle){
        if(bundle != null && !bundle.isEmpty()){
            memory = memory.add(new BigDecimal(bundle.getString(MEMORY_KEY)));
            expression.restoreData(bundle.getBundle(EXPRESSION_KEY));
            value.restoreData(bundle.getBundle(VALUE_KEY));
            resultText.setText(value.toString());
            expressionText.setText(expression.toString());
        }
    }
}
