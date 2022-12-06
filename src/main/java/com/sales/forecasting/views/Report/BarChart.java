package com.sales.forecasting.views.Report;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.PlotOptions;
import com.github.appreciated.apexcharts.config.builder.*;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.ZoomBuilder;
import com.github.appreciated.apexcharts.config.legend.HorizontalAlign;
import com.github.appreciated.apexcharts.config.plotoptions.builder.BarBuilder;
import com.github.appreciated.apexcharts.config.yaxis.builder.TitleBuilder;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.html.Div;

import java.util.List;

public class BarChart extends Div {

    public ApexCharts createChart(String name,List<Double> salesval,List<String> categories){

        ApexCharts chart = ApexChartsBuilder.get()
                .withChart(ChartBuilder.get()
                        .withType(Type.BAR)
                        .withZoom(ZoomBuilder.get()
                                .withAutoScaleYaxis(true)
                                .withEnabled(true)
                                .build())
                        .build())
                .withPlotOptions(PlotOptionsBuilder.get()
                        .withBar(BarBuilder.get()
                                .withHorizontal(false)
                                .withColumnWidth("55%")
                                .build())
                        .build())
                .withDataLabels(DataLabelsBuilder.get()
                        .withEnabled(false).build())
                .withStroke(StrokeBuilder.get()
                        .withShow(true)
                        .withWidth(2.0)
                        .withColors("transparent")
                        .build())
                .withSeries(new Series<>(name,salesval.toArray(Double[]::new)))
                .withYaxis(YAxisBuilder.get()
                        .withTitle(TitleBuilder.get()
                                .withText("In $")
                                .build())
                        .build())
                .withXaxis(XAxisBuilder.get().withCategories(categories).build())
                .withFill(FillBuilder.get()
                        .withOpacity(1.0).build())
                .withLegend(LegendBuilder.get().withHorizontalAlign(HorizontalAlign.LEFT).build())
                .build();
        return chart;

    }
}
