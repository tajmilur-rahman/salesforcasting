package com.example.application.views.Calendar;

import com.example.application.services.weka.SampleService;
import com.example.application.services.weka.TimeSeriesForecast;
import com.example.application.views.Dashboard.Dashboard;
import com.example.application.views.NewMainLayout;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import tech.tablesaw.api.*;

import javax.annotation.security.PermitAll;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static tech.tablesaw.aggregate.AggregateFunctions.*;
import static tech.tablesaw.api.QuerySupport.all;
import static tech.tablesaw.api.QuerySupport.and;

@PermitAll
@Route(value = "calendar-form",layout = NewMainLayout.class)
@Uses(Icon.class)
public class CalendarView extends Div {

    Button submitButton = new Button("Submit");

//    TextField topProducts = new
    DatePicker startDateTimePicker = new DatePicker(
            "Start date and time");


    DatePicker endDateTimePicker = new DatePicker(
            "End date and time");


    @Autowired
    SampleService sampleService;
    public CalendarView(SampleService sampleService){
        this.sampleService = sampleService;
        addDatePicker();

    }

    public void addDatePicker(){
        VerticalLayout vl = new VerticalLayout();

        endDateTimePicker.setValue(sampleService.endDateLocal);
        startDateTimePicker.setValue(sampleService.startDateLocal);


        startDateTimePicker.addValueChangeListener(
                e -> endDateTimePicker.setMin(e.getValue()));

        submitButton.setWidth(vl.getWidth());
        submitButton.addClickListener(event -> {
            sampleService.sampleData = "Changed in Dashboard";
            processData();
            submitButton.getUI().ifPresent(ui -> ui.navigate(Dashboard.class));

        });

        vl.add(startDateTimePicker,endDateTimePicker);
        vl.add(submitButton);
        vl.setAlignItems(FlexComponent.Alignment.CENTER);

        add(vl);
    }

    public void processData(){
        Table data = sampleService.tableData;


        LocalDate startDate = startDateTimePicker.getValue();
        LocalDate endDate = endDateTimePicker.getValue();
        // Filter Data based on the Date
        Table filterData = data.where(and(t->t.dateColumn(sampleService.dateColName).isBetweenExcluding(startDate,endDate)));

        // First find the Top 5 Categories
        Table top5 = filterData.summarize(sampleService.salesColName,sum).by(sampleService.productCategoryName);
        top5 = top5.sortDescendingOn(top5.columnNames().get(1)).first(top5.rowCount());
//        System.out.println(top5);
        List<String> colvas = (List<String>) top5.column(0).asList();
        Table top5new = filterData.where(t->t.stringColumn(sampleService.productCategoryName).isIn(colvas));

        // Group by the Category and find the most poular category and plot those in the dashboard
        Table suData = top5new.summarize(sampleService.salesColName,sum).by(sampleService.productCategoryName,
                sampleService.dateColName);
//        System.out.println(suData);
        TreeMap<String,Table> allTables = new TreeMap<>();
        suData = suData.sortOn(sampleService.dateColName);
        int jk = 0;
        for(String col:colvas){
            jk++;
            sampleService.productCategories.add(col);
            allTables.put(col,suData.where(t->t.stringColumn(sampleService.productCategoryName).isEqualTo(col)));
            // Make Forecast
            File fileData = new File("localCsvFile"+jk);
            Table subtable = data.where(t->t.stringColumn(sampleService.productCategoryName).isEqualTo(col));
            subtable = subtable.sortOn(sampleService.dateColName);
            subtable = subtable.summarize(sampleService.salesColName,sum).by(sampleService.dateColName);

            DateColumn dc =  subtable.dateColumn(sampleService.dateColName);
            List<Long> epochDate = new ArrayList<>();
            for(LocalDate ld:dc.asList()){
                long e = ld.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
                epochDate.add(e);
            }

            long[] edate = epochDate.stream().mapToLong(l -> l).toArray();

            subtable.addColumns(LongColumn.create("longDate",edate));

            subtable.write().csv(fileData);

            String salesCol = subtable.columnNames().get(subtable.columnNames().size()-2);

             // Calculate differance between the last date in the data nad the provided last date.
            long emaxd = sampleService.endDateLocal.toEpochDay();
            long usere = endDate.toEpochDay();

            long diffDays = usere - emaxd;
            System.out.println("User Differance:"+diffDays);

            TimeSeriesForecast tsf = new TimeSeriesForecast("longDate",salesCol,(int)diffDays);
            try{
                List<Double> newData =  tsf.makeForecast(fileData);
                List<String> coNames = allTables.get(col).columnNames();

               // family,date,Sum [sales],longDate
               Table lastEntry = allTables.get(col).last(1);
               System.out.println("Forecaster Result length:"+newData.size());
               System.out.println(lastEntry);
                String firstColval = (String) lastEntry.column(coNames.get(0)).get(0);
                LocalDate secondColval = (LocalDate) lastEntry.column(coNames.get(1)).get(0);
                double thirdColVal = (double)  lastEntry.column(coNames.get(2)).get(0);
//                long fourthColVal = (long)  lastEntry.column(coNames.get(3)).get(0);
                String[] firstColValCol = new String[newData.size()];
                LocalDate[] localDateColValCol = new LocalDate[newData.size()];
                double[] saleColValCol = new double[newData.size()];
                long[] longDatecolValCol = new long[newData.size()];
                secondColval = secondColval.plusDays(1);
                for(int i=0;i< newData.size();i++){
                    firstColValCol[i] = firstColval;
                    localDateColValCol[i] = secondColval;
                    secondColval = secondColval.plusDays(1);
                    saleColValCol[i] = newData.get(i);
                    longDatecolValCol[i] = localDateColValCol[i].atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
                }
                Table newVlaTable = Table.create("Forecasted results").addColumns(
                        StringColumn.create(coNames.get(0),firstColValCol),
                        DateColumn.create(coNames.get(1),localDateColValCol),
                        DoubleColumn.create(coNames.get(2),saleColValCol)
//                        ,LongColumn.create(coNames.get(3),longDatecolValCol)
                );
                Table currTabel =  allTables.get(col);
                for(int i=0;i<newData.size();i++){
                    currTabel.addRow(i,newVlaTable);
                }
                allTables.put(col,currTabel);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        }
        sampleService.allTables = allTables;
//
//        System.out.println(suData);
//


    }
}