package com.kirchnersolutions.PiCenter.services;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

import java.util.ArrayList;
import java.util.List;

public class PolynomialHelper {

    private PolynomialHelper(){}

    public static double[] fitPoly(WeightedObservedPoints obs, int degree, int trys, double[] guessCoef){

        final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(degree);
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
        return ((y - ny) / ny) * ((y - ny) / ny);
    }

    /*private double[] fitPoly(WeightedObservedPoints obs, int degree){
        final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(degree);
        double[] coeff = fitter.fit(obs.toList());
        for(int i = 0; i <= trys; i++){
            coeff = fitPoly(obs, degree, coeff);
        }
        return coeff;
    }*/

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

}
