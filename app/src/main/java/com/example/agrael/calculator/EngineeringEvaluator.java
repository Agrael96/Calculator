package com.example.agrael.calculator;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;
import net.objecthunter.exp4j.operator.Operator;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by agrael on 11/29/16.
 */
public class EngineeringEvaluator implements Evaluator {

    protected ArrayList<Function> functions;
    protected ArrayList<Operator> operators;

    public EngineeringEvaluator(){

        operators = new ArrayList<>();
        functions = new ArrayList<>();


        Function sinr = new Function("sinr", 1) {
            @Override
            public double apply(double... doubles) {
                return Math.sin(doubles[0]);
            }
        };

        Function sind = new Function("sind", 1) {
            @Override
            public double apply(double... doubles) {
                return Math.sin(doubles[0]*Math.PI/180);
            }
        };

        Function cosr = new Function("cosr", 1) {
            @Override
            public double apply(double... doubles) {
                return Math.cos(doubles[0]);
            }
        };

        Function cosd = new Function("cosd", 1) {
            @Override
            public double apply(double... doubles) {
                return Math.cos(doubles[0]*Math.PI/180);
            }
        };

        Function tanr = new Function("tanr", 1) {
            @Override
            public double apply(double... doubles) {
                return Math.tan(doubles[0]);
            }
        };

        Function tand = new Function("tand", 1) {
            @Override
            public double apply(double... doubles) {
                return Math.tan(doubles[0]*Math.PI/180);
            }
        };

        Function sqr = new Function("sqr", 1) {
            @Override
            public double apply(double... doubles) {
                return Math.pow(doubles[0], 2);
            }
        };

        Function factorial = new Function("fac", 1) {
            @Override
            public double apply(double... doubles) {
                return factorial(doubles[0]+1);
            }
        };

        functions.add(sind);
        functions.add(sinr);
        functions.add(cosr);
        functions.add(cosd);
        functions.add(tanr);
        functions.add(tand);
        functions.add(sqr);
        functions.add(factorial);
    }

    @Override
    public String evaluate(String expression) throws EvaluationException {

        Expression exp;
        exp = new ExpressionBuilder(expression).functions(functions).operator(operators).build();
        BigDecimal res = BigDecimal.ZERO;
        try {
            String result = String.valueOf(exp.evaluate());
            res = new BigDecimal(result);
        } catch (Exception e) {
            throw new EvaluationException();
        }catch (StackOverflowError e){
            throw new EvaluationException();
        }
        return res.toPlainString();
    }

    public double factorial(double value){
        if((value>0.0)&&(value<1.0))  {
            return factorial(value+1.0)/value;
        }
        if(value>2){
            return (value-1)*factorial(value-1);
        }
        if(value<=0){
            return Math.PI/(Math.sin(Math.PI*value)*factorial(1-value));
        }
        return gammaApproximation(value);
    }

    private double gammaApproximation(double x){
        double [] p={ 6.64561438202405440627855e+4,
                -3.61444134186911729807069e+4,
                -3.14512729688483675254357e+4,
                8.66966202790413211295064e+2,
                6.29331155312818442661052e+2,
                -3.79804256470945635097577e+2,
                2.47656508055759199108314e+1,
                -1.71618513886549492533811e+0,};

        double [] q={ -1.15132259675553483497211e+5,
                -1.34659959864969306392456e+5,
                4.75584627752788110767815e+3,
                2.25381184209801510330112e+4,
                -3.10777167157231109440444e+3,
                -1.01515636749021914166146e+3,
                3.15350626979604161529144e+2,
                -3.08402300119738975254353e+1,};

        double res = x - 1.0;
        double a = 0.0;
        double b = 0.0;
        for(int i = 0; i<8; i++){
            a+=p[i]*Math.pow(res, i+1);
            b+=q[i]*Math.pow(res, i);
        }
        b+=Math.pow(res, 8);
        return (a/b+1.0);
    }
}
