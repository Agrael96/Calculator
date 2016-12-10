package com.example.agrael.calculator;

/**
 * Created by agrael on 12/3/16.
 */
public class EvaluationException extends Exception {

    public EvaluationException() {}

    public String what(){
        return "Evaluation error";
    }
}
