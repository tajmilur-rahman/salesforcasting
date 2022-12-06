package com.sales.forecasting.views.Report;

import com.sales.forecasting.services.weka.SampleService;
import com.sales.forecasting.views.NewMainLayout;
import com.github.appreciated.apexcharts.ApexCharts;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import tech.tablesaw.api.Table;

import javax.annotation.security.PermitAll;

import java.util.List;
import java.util.Set;

import static tech.tablesaw.aggregate.AggregateFunctions.*;

@PermitAll
@Route(value = "report-form",layout = NewMainLayout.class)
@Uses(Icon.class)
public class Report extends Div {

    // Average Sales
    // Max Sales
    // Min Sales
    MultiSelectListBox<String> listBox = new MultiSelectListBox<>();
    // layout Filter button
    Button loadChart = new Button("Update Chart");

    Button selectAll = new Button("Select All");
    Button deselect = new Button("Deselect");

    SampleService sampleService;
    public Report(SampleService sampleService){
        this.sampleService = sampleService;
        addButtons();
        loadChart.addClickListener(e->{
            updateChart();
        });
        selectAll.addClickListener(e->{
            listBox.select(sampleService.productCategories);
        });
        deselect.addClickListener(e->{
            listBox.deselect(sampleService.productCategories);
        });

    }

    public void addButtons(){

        if(sampleService.productCategories.size()>0){
            listBox.setItems(sampleService.productCategories);
        }
        listBox.setMaxHeight(100, Unit.PIXELS);

        HorizontalLayout fl = new HorizontalLayout(listBox,selectAll,deselect,loadChart);
        VerticalLayout vl = new VerticalLayout(fl);

        vl.setHeight("20%");
        vl.setWidth("50%");
        vl.setAlignItems(FlexComponent.Alignment.CENTER);
        add(vl);
    }

    public void updateChart(){

        BarChart bc = new BarChart();
        if(sampleService.tableData !=null){
            Set<String> selectedValues = listBox.getSelectedItems();

            Table data = sampleService.tableData;
            Table averageData = data.summarize(sampleService.salesColName,mean).by(sampleService.productCategoryName);
            averageData = averageData.where(t->t.stringColumn(sampleService.productCategoryName).isIn(selectedValues));
            Table SumData = data.summarize(sampleService.salesColName,sum).by(sampleService.productCategoryName);
            SumData = SumData.where(t->t.stringColumn(sampleService.productCategoryName).isIn(selectedValues));
            Table MaxeData = data.summarize(sampleService.salesColName,max).by(sampleService.productCategoryName);
            MaxeData = MaxeData.where(t->t.stringColumn(sampleService.productCategoryName).isIn(selectedValues));

            System.out.println("Number of components Before VL in report:"+getComponentCount());
            if(getComponentCount()>1){
                remove(getComponentAt(1));
            }

            List<String> aColname = averageData.columnNames();
            List<String> sColname = SumData.columnNames();
            List<String> mColname = MaxeData.columnNames();



            ApexCharts averageChart = bc.createChart("Average Sale",
                    (List<Double>) averageData.column(aColname.get(1)).asList(),
                    (List<String>) averageData.column(aColname.get(0)).asList()
                    );
            ApexCharts totalChart = bc.createChart("Total Sale",
                    (List<Double>) SumData.column(sColname.get(1)).asList(),
                    (List<String>) SumData.column(sColname.get(0)).asList()
            );
            ApexCharts maxChart = bc.createChart("Max Sale",
                    (List<Double>) MaxeData.column(mColname.get(1)).asList(),
                    (List<String>) MaxeData.column(mColname.get(0)).asList()
            );
            VerticalLayout vl = new VerticalLayout(averageChart,totalChart,maxChart);
            this.add(vl);
        }


    }

}
