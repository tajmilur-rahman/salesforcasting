package com.example.application.views.charts;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.example.application.data.service.CrmService;
import com.example.application.views.MainLayout;
import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.TitleSubtitle;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.DataLabelsBuilder;
import com.github.appreciated.apexcharts.config.builder.FillBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.builder.PlotOptionsBuilder;
import com.github.appreciated.apexcharts.config.builder.StrokeBuilder;
import com.github.appreciated.apexcharts.config.builder.TitleSubtitleBuilder;
import com.github.appreciated.apexcharts.config.builder.TooltipBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.builder.YAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.plotoptions.builder.BarBuilder;
import com.github.appreciated.apexcharts.config.subtitle.Align;
import com.github.appreciated.apexcharts.config.xaxis.XAxisType;
import com.github.appreciated.apexcharts.config.yaxis.builder.TitleBuilder;
import com.github.appreciated.apexcharts.helper.Coordinate;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.config.responsive.builder.OptionsBuilder;
import com.github.appreciated.apexcharts.config.builder.ResponsiveBuilder;

@PageTitle("Hello Barchart")
@Route(value = "bar", layout = MainLayout.class)
public class BarChartView extends VerticalLayout {
	 TextField newfield = new TextField();
	 private final CrmService service;
    public BarChartView(CrmService service) {
    	this.service = service;
    	addClassName("dashboard-view");
        add(new H1("Heading"));
        
        ArrayList<Double> data = new ArrayList<>();
        Series<Double> datas = new Series<>();
        ArrayList<String> dname = new ArrayList<>();
        service.findAllCompanies().forEach(company -> dname.add(company.getName()));
        String dname_s[] = new String[dname.size()];
        
        
        service.findAllCompanies().forEach(company -> data.add((double)company.getEmployeeCount()));
//        service.findAllCompanies().forEach(company -> datas.add((double)company.getEmployeeCount()));
        Double datan[] = new Double[data.size()];
        
        System.out.println("Data:"+data);
        for(int i=0;i<data.size();i++) {
        	datan[i] = data.get(i);
        	
        	dname_s[i] = dname.get(i);
        	System.out.println(datan[i]+" "+dname_s[i]);
        }
        datas.setData(datan);
        datas.setName("lol");
 
        
        
        ApexCharts donutChart = ApexChartsBuilder.get()
                .withChart(ChartBuilder.get().withType(Type.donut).build())
                
                .withLegend(LegendBuilder.get()
                        .withPosition(Position.right)
                        .build())
//                .with
                .withLabels(dname_s)
                .withSeries(datan) //.withSeries(44.0, 55.0, 41.0, 17.0, 15.0)
                .withResponsive(ResponsiveBuilder.get()
                        .withBreakpoint(480.0)
                        .withOptions(OptionsBuilder.get()
                                .withLegend(LegendBuilder.get()
                                        .withPosition(Position.bottom)
                                        .build())
                                .build())
                        .build())
                .build();
        
        donutChart.setWidth("600px");
        donutChart.setHeight("600px");
        
        donutChart.setVisible(true);
        add(donutChart);
//        setWidth("100%");
        
        add(new H1("Footer"));
        add(newfield);
        setSizeFull();
       
    }
   
}
