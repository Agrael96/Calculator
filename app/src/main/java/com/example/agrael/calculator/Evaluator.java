package com.example.agrael.calculator;


/**
 * Created by agrael on 11/22/16.
 */
public interface Evaluator {

   public String evaluate(String expression) throws EvaluationException, ValidationException;

}
