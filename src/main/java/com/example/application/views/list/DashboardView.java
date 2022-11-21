package com.example.application.views.list;

import java.util.ArrayList;

import javax.annotation.security.PermitAll;

import com.example.application.data.service.CrmService;
import com.example.application.views.MainLayout;
import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.builder.ResponsiveBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.config.responsive.builder.OptionsBuilder;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PermitAll
@Route(value = "dashboard", layout = MainLayout.class) 
@PageTitle("Dashboard | Sales Forecasting")
public class DashboardView extends VerticalLayout{
	private final CrmService service;
	 
	public DashboardView(CrmService service) { 
        this.service = service;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER); 
        add(getContactStats(), getCompaniesChart());
    }
	
	private Component getContactStats() {
        Span stats = new Span(service.countContacts() + " contacts"); 
        stats.addClassNames("text-xl", "mt-m");
        return stats;
    }
	
	private ApexCharts getCompaniesChart() {
//		ApexCharts apexCharts = ApexChartsBuilder.get().build();
//		apexCharts.setChart(ChartBuilder.get().withType(Type.pie).build());
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
                .withChart(ChartBuilder.get().withType(Type.pie).build())
                
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
		
		       
		return donutChart;
	}

}
