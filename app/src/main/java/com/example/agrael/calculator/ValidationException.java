package com.example.agrael.calculator;

/**
 * Created by agrael on 12/3/16.
 */
public class ValidationException extends Exception {

    public ValidationException(){};

    public String what(){
        return "Expression not valid";
    }
}
