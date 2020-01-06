package com.kirchnersolutions.PiCenter.services;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;
import java.util.List;

public class PolynomialHelper {

    private PolynomialHelper(){}

    public static double[] fitPoly(WeightedObservedPoints obs, int degree, int trys, double[] guessCoef){
        final PolynomialCurveFitter fitter;
        if(guessCoef.length == degree + 1){
            fitter = PolynomialCurveFitter.create(degree)
                    .withStartPoint(guessCoef);
        }else {
            fitter = PolynomialCurveFitter.create(degree);
        }
        double[] coeff = fitter.fit(obs.toList());
        PolynomialFunction function = new PolynomialFunction(coeff);
        List<double[]> weighted = new ArrayList<>();
        for(int i = 0; i < obs.toList().size(); i++){
            double ny = function.value(obs.toList().get(i).getX());
            double[] t = {getWeight(ny, obs.toList().get(i).getY()), obs.toList().get(i).getY(), obs.toList().get(i).getX()};
            weighted.add(t);
        }
        if(trys > 1){
            fitPoly(generatePoints(weighted), degree, trys - 1, coeff);
        }
        return coeff;
    }

    public static WeightedObservedPoints generatePoints(List<double[]> weighted){
        final WeightedObservedPoints obs = new WeightedObservedPoints();
        for (double[] doubles : weighted){
            obs.add(doubles[0], doubles[1], doubles[2]);
        }
        return obs;
    }

    public static double getWeight(double ny, double y){
        if(ny == 0){
            ny = .0001;
        }
        return ((y - ny) / ny) * ((y - ny) / ny);
    }

    public static double[] fitPoly(WeightedObservedPoints obs, int degree, double[] guessCoef){
        final PolynomialCurveFitter fitter = PolynomialCurveFitter
                .create(degree)
                .withStartPoint(guessCoef);
        final double[] coeff = fitter.fit(obs.toList());
        return coeff;
    }

    public static double[] fitPoly(WeightedObservedPoints obs, int degree){
        final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(degree);
        final double[] coeff = fitter.fit(obs.toList());
        return coeff;
    }

    public static List<double[]> dummyCoef(){
        List<double[]> dummy = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            dummy.add(new double[0]);
        }
        return dummy;
    }

     double getRSquare(WeightedObservedPoints obs, double[] coef) {
        final double[] coefficients = coef;
        double[] predictedValues = new double[obs.toList().size()];
        double residualSumOfSquares = 0;
        final DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
        PolynomialFunction function = new PolynomialFunction(coefficients);
        for (int i=0; i< predictedValues.length; i++) {
            predictedValues[i] = function.value( obs.toList().get(i).getX());

            double actualVal = obs.toList().get(i).getY();
            double t = Math.pow((predictedValues[i] - actualVal), 2);
            residualSumOfSquares  += t;
            descriptiveStatistics.addValue(actualVal);
        }
        final double avgActualValues = descriptiveStatistics.getMean();
        double totalSumOfSquares = 0;
        for (int i=0; i<predictedValues.length; i++) {
            totalSumOfSquares += Math.pow( (predictedValues[i] - avgActualValues),2);

        }
        return 1.0 - (residualSumOfSquares/totalSumOfSquares);
    }

    private double getRSquare(WeightedObservedPoints obs, int degree) {
        final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(degree);
        final double[] coefficients = fitter.fit(obs.toList());
        double[] predictedValues = new double[obs.toList().size()];
        double residualSumOfSquares = 0;
        final DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
        PolynomialFunction function = new PolynomialFunction(coefficients);
        for (int i=0; i< predictedValues.length; i++) {
            predictedValues[i] = function.value( obs.toList().get(i).getX());

            double actualVal = obs.toList().get(i).getY();
            double t = Math.pow((predictedValues[i] - actualVal), 2);
            residualSumOfSquares  += t;
            descriptiveStatistics.addValue(actualVal);
        }
        final double avgActualValues = descriptiveStatistics.getMean();
        double totalSumOfSquares = 0;
        for (int i=0; i<predictedValues.length; i++) {
            totalSumOfSquares += Math.pow( (predictedValues[i] - avgActualValues),2);

        }
        return 1.0 - (residualSumOfSquares/totalSumOfSquares);
    }

}
