package com.example.agrael.calculator;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

import java.math.BigDecimal;

/**
 * Created by agrael on 12/3/16.
 */
public class FunctionEvaluator extends EngineeringEvaluator {

    public FunctionEvaluator(){}


    public double evaluate(String function, float xValue) throws ValidationException, EvaluationException{
        if(!function.isEmpty()) {
            Expression exp;
            try {
                exp = new ExpressionBuilder(function).functions(functions)
                        .operator(operators)
                        .variable("x").build();
            }catch (Exception e){
                throw new ValidationException();
            }
            BigDecimal res = BigDecimal.ZERO;
            try {
                exp.setVariable("x", xValue);
                String result = String.valueOf(exp.evaluate());
                res = new BigDecimal(result);
            } catch (Exception e) {
                throw new EvaluationException();
            }
            return res.floatValue();
        }
        return Double.NaN;
    }

    public boolean validate(String function){
        Expression exp;
        if(!function.isEmpty()) {
            try {
                exp = new ExpressionBuilder(function).functions(functions)
                        .operator(operators)
                        .variable("x").build();
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

}
