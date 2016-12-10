package com.example.agrael.calculator;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.math.BigDecimal;
import java.util.ArrayList;

public class SimpleCalcFragment extends Fragment implements View.OnClickListener{


    private final String MEMORY_KEY = "memoryKey";
    private final String EXPRESSION_KEY = "expressionKey";
    private final String VALUE_KEY = "valueKey";

    private BigDecimal memory;
    private TextView resultText;
    private TextView expressionText;
    private InputExpression expression;
    private InputValue value;

    private boolean isScrollMoveSet;

    private EngineeringEvaluator evaluator;


    public SimpleCalcFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_simple_calc, container, false);

        memory           = BigDecimal.ZERO;
        value = new InputValue();
        expression = new InputExpression();
        isScrollMoveSet  = false;

        resultText     = (TextView) v.findViewById(R.id.resultText);
        expressionText = (TextView) v.findViewById(R.id.expressionText);
        evaluator = new EngineeringEvaluator();

        ArrayList<View> views = Utils.getAllChildren(v.getRootView());

        for(View view: views){
            if(view instanceof Button){
                view.setOnClickListener(this);
            }
        }

        restoreData(getArguments());

        return v;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
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
                try {
                    String result = evaluator.evaluate(expression.getEvaluableString());
                    resultText.setText(result);
                    value.setValue(result);
                }catch(EvaluationException e){
                   Utils.showToast(getContext(), getString(R.string.evaluationError));
                }
                expression.clear();
                value.setResult(true);
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
                if(!value.toString().isEmpty()) {
                    memory = memory.add(new BigDecimal(value.toString()));
                }
                break;
            }
            case R.id.memoryReadButton: {
                resultText.setText(memory.toString());
                value.setValue(memory.toString());
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

            case R.id.deleteButton : {
                resultText.setText(value.removeLastInput());
                break;
            }
        }

        if(!isScrollMoveSet){
            isScrollMoveSet = true;
            resultText.setMovementMethod(new ScrollingMovementMethod());
            expressionText.setMovementMethod(new ScrollingMovementMethod());
        }
    }

    private void setResult(){
        try{
            String exp = expression.getEvaluableString();
            String result = evaluator.evaluate(exp);
            value.setValue(result);
            value.setResult(true);
            resultText.setText(value.toString());
        }catch (Exception e){
            Utils.showToast(getContext(), getString(R.string.evaluationError));
        }
    }

    private void addOperatorToExpression(String operator){
        expressionText.setText(expression.addTokenToExpression(operator, value));
        if(!expression.toString().isEmpty()){
            setResult();
        }
    }

    public Bundle getData(){

        Bundle bundle = new Bundle();

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
