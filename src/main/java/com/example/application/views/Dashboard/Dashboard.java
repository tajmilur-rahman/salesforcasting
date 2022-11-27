package com.example.application.views.Dashboard;

import com.example.application.services.weka.SampleService;
import com.example.application.views.NewMainLayout;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@PermitAll
@Route(value = "dashboard-view",layout = NewMainLayout.class)
@Uses(Icon.class)
public class Dashboard extends Div {


    @Autowired
    private SampleService sampleService;

    public String sampleVariable = "No Variable Arrived";

    TextField sampleTextField = new TextField();

    MultiSelectListBox<String> listBox = new MultiSelectListBox<>();
    TextField topProdicts = new TextField("Top Categories to show");
    Button update = new Button("Update");

    public Dashboard(SampleService sampleService){
        this.sampleService = sampleService;
        topProdicts.setValue("5");
        addButtons();

        sampleTextField.setValue(sampleService.sampleData);
        writeTextButton();

//        ArrayList<Double> d = new ArrayList<>();
//        Series s = new Series("Key",d);
        update.addClickListener(e->{
            writeTextButton();
        });


    }
    public void updateValueOfText(){
        sampleTextField.setValue(sampleVariable);
    }

    public void addButtons(){
        FormLayout vl = new FormLayout(topProdicts,update);
        add(vl);
    }

    public void writeTextButton(){
        VerticalLayout vl = new VerticalLayout();
        vl.add(sampleTextField);

        List<Series> seriess = new ArrayList<>();
        List<List<LocalDate>> dateSeries = new ArrayList<>();
        String value = topProdicts.getValue();
         int listN = Integer.valueOf(value);

        // Check if the new files is loaded or not,
        if(sampleService.allTables.size()>0){
//            listBox.setValue(sampleService.allTables.keySet());

            // update the chart
            List<String> keysShort = sampleService.productCategories.subList(0,listN);



            for(String key:keysShort){
                List<String> colname = sampleService.allTables.get(key).columnNames();
                ArrayList<Double> data = (ArrayList<Double>) sampleService.allTables.get(key).column(colname.get(colname.size()-1)).asList();
                Series s = new Series<>(key,data.toArray(Double[]::new));
                List<LocalDate> date = (List<LocalDate>) sampleService.allTables.get(key).column(colname.get(colname.size()-2)).asList();
                dateSeries.add(date);


                seriess.add(s);
            }

        }else{
            Series s = new Series<>("STOCK ABC", 10.0, 41.0, 35.0, 51.0, 49.0, 62.0, 69.0, 91.0, 148.0);
            seriess.add(s);
            dateSeries.add(IntStream.range(1, 10).boxed().map(day -> LocalDate.of(2000, 1, day)).collect(Collectors.toList()));
        }


        AreaChartExample areaChartExample = new AreaChartExample();
        VerticalLayout chartVl = new VerticalLayout();

        chartVl.add(areaChartExample.chart(seriess,dateSeries.get(0)));
        chartVl.setWidth("70%");
        vl.add(chartVl);
        vl.setAlignItems(FlexComponent.Alignment.CENTER);
        add(vl);


    }
}
