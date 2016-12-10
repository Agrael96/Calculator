package com.example.agrael.calculator;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.operator.Operator;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by agrael on 11/29/16.
 */
public class ProgrammerEvaluator implements Evaluator {

    private ArrayList<Operator> operators;

    public ProgrammerEvaluator(){

        operators = new ArrayList<>();

        Operator andOperator = new Operator("&", 2, false, Operator.PRECEDENCE_SUBTRACTION-1) {
            @Override
            public double apply(double... doubles) {
                if(doubles.length<2) {
                    throw new IllegalArgumentException("Operands for AND mist be two");
                }
                final int leftArg = (int) doubles[0];
                final int rightArg = (int) doubles[1];
                return leftArg & rightArg;
            }
        };

        Operator orOperator = new Operator("|", 2, false, Operator.PRECEDENCE_SUBTRACTION-3) {
            @Override
            public double apply(double... doubles) {
                if(doubles.length<2) {
                    throw new IllegalArgumentException("Operands for OR mist be two");
                }
                final int leftArg = (int) doubles[0];
                final int rightArg = (int) doubles[1];
                return leftArg | rightArg;
            }
        };

        Operator inversionOperator = new Operator("~", 1, false, Operator.PRECEDENCE_MULTIPLICATION+1) {
            @Override
            public double apply(double... doubles) {
                if(doubles.length!=1) {
                    throw new IllegalArgumentException("Operands for INV mist be one");
                }
                final int rightArg = (int) doubles[0];
                return ~rightArg;
            }
        };

        Operator xorOperator = new Operator("^", 2, false, Operator.PRECEDENCE_SUBTRACTION-2) {
            @Override
            public double apply(double... doubles) {
                if(doubles.length<2) {
                    throw new IllegalArgumentException("Operands for XOR mist be two");
                }
                final int leftArg = (int) doubles[0];
                final int rightArg = (int) doubles[1];
                return leftArg ^ rightArg;
            }
        };

        operators.add(andOperator);
        operators.add(orOperator);
        operators.add(inversionOperator);
        operators.add(xorOperator);
    }

    @Override
    public String evaluate(String expression) {

        Expression exp;
        exp = new ExpressionBuilder(expression).operator(operators).build();
        BigDecimal res = BigDecimal.ZERO;
        try {
            String result = String.valueOf(exp.evaluate());
            res = new BigDecimal(result);
        } catch (Exception e) {
            throw e;
        }
        return res.toPlainString();
    }
}
