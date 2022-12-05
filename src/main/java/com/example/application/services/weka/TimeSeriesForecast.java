package com.example.application.services.weka;


import weka.classifiers.evaluation.NumericPrediction;
import weka.classifiers.functions.GaussianProcesses;
import weka.classifiers.timeseries.WekaForecaster;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.gui.beans.DataSource;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;


public class TimeSeriesForecast {

    DataSource dataSource;

    WekaForecaster wekaForecaster = new WekaForecaster();

    String dateColName = "date";
    String salesColName = "sales";

    int futureForecastStep = 8;


    public TimeSeriesForecast(String datename,String salesName,int step){
        dateColName = datename;
        salesColName = salesName;
        futureForecastStep=step;
    }
    public List<Double> makeForecast(File file) throws Exception {
        CSVLoader loader = new CSVLoader();
        loader.setSource(file);

        Instances dataset = loader.getDataSet();


        System.out.println("arrayData:"+dataset.get(0));
        System.out.println("Date Format in data:"+dataset.attribute(dateColName).value(0));
        System.out.println("Sales value in sales data:"+dataset.attribute(salesColName).value(0));

        File pfile = new File("forecast.txt");
        pfile.createNewFile();
        PrintStream stream = new PrintStream("forecast.txt");
        wekaForecaster.setBaseForecaster(new GaussianProcesses());
        wekaForecaster.setFieldsToForecast(salesColName);
        wekaForecaster.getTSLagMaker().setTimeStampField(dateColName);
        wekaForecaster.getTSLagMaker().setMinLag(1);
        wekaForecaster.getTSLagMaker().setMaxLag(20);
        wekaForecaster.getTSLagMaker().setAddMonthOfYear(true);
        wekaForecaster.getTSLagMaker().setAddDayOfWeek(true);
        wekaForecaster.getTSLagMaker().setAddQuarterOfYear(true);
        wekaForecaster.getTSLagMaker().setAddDayOfMonth(true);
        wekaForecaster.buildForecaster(dataset,stream);
        wekaForecaster.primeForecaster(dataset);
        List<List<NumericPrediction>> prediction = new ArrayList<>();
        List<Double> forecastValues = new ArrayList<>();
        prediction = wekaForecaster.forecast(futureForecastStep,stream);
        for (int i = 0; i < futureForecastStep; i++) {
            List<NumericPrediction> predsAtStep = prediction.get(i);
            NumericPrediction predForTarget = predsAtStep.get(0);
            forecastValues.add(predForTarget.predicted());
//            System.out.println("" + predForTarget.predicted() + " "+predsAtStep.size());

    }
        return forecastValues;

}


}