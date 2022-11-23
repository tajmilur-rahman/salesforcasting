package com.example.application.services.weka;

import org.springframework.stereotype.Component;
import tech.tablesaw.api.Table;

import java.time.LocalDate;
import java.util.TreeMap;

/**
 * This i will be using as the service to share common data across views,
 * For example one can jump to the Dashboard from the calendar to dashboard with selected dates
 * */
@Component
public class SampleService {

    public String sampleData = "Sample Customs";
    public String startDate = "";
    public String endDate = "";


    public LocalDate endDateLocal = LocalDate.now();
    public LocalDate startDateLocal = endDateLocal.minusMonths(1);

    public TreeMap<String,Table> allTables = new TreeMap<>();

    public String dateColName = "date";
    public String productCategoryName = "category";
    public String salesColName = "sales";
    public Table tableData;

}
