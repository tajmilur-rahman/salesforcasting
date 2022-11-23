package com.example.application.views.Calendar;

import com.example.application.services.weka.SampleService;
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
import com.vaadin.flow.router.Route;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import tech.tablesaw.api.Table;

import javax.annotation.security.PermitAll;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static tech.tablesaw.aggregate.AggregateFunctions.*;
import static tech.tablesaw.api.QuerySupport.and;

@PermitAll
@Route(value = "calendar-form",layout = NewMainLayout.class)
@Uses(Icon.class)
public class CalendarView extends Div {

    Button submitButton = new Button("Submit");
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
        top5 = top5.sortDescendingOn(top5.columnNames().get(1)).first(5);
//        System.out.println(top5);
        List<String> colvas = (List<String>) top5.column(0).asList();
        Table top5new = filterData.where(t->t.stringColumn(sampleService.productCategoryName).isIn(colvas));

         // Group by the Category and find the most poular category and plot those in the dashboard
        Table suData = top5new.summarize(sampleService.salesColName,sum).by(sampleService.productCategoryName,
                sampleService.dateColName);
//        System.out.println(suData);
        TreeMap<String,Table> allTables = new TreeMap<>();
        suData = suData.sortOn(sampleService.dateColName);
        for(String col:colvas){
            allTables.put(col,suData.where(t->t.stringColumn(sampleService.productCategoryName).isEqualTo(col)));
        }
        sampleService.allTables = allTables;
//
//        System.out.println(suData);
//


    }
}
