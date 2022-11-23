package com.example.application.views.Dashboard;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.*;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.ZoomBuilder;
import com.github.appreciated.apexcharts.config.legend.HorizontalAlign;
import com.github.appreciated.apexcharts.config.stroke.Curve;
import com.github.appreciated.apexcharts.config.subtitle.Align;
import com.github.appreciated.apexcharts.config.xaxis.XAxisType;
import com.github.appreciated.apexcharts.config.yaxis.builder.TitleBuilder;
//import com.github.appreciated.apexcharts.config.xaxis.builder.TitleBuilder;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.html.Div;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class AreaChartExample extends Div {
    public AreaChartExample() {

    }
    public ApexCharts chart(List<Series> inData, List<LocalDate> dateLables){
        ApexCharts areaChart = ApexChartsBuilder.get()
                .withChart(ChartBuilder.get()
                        .withType(Type.AREA)
                        .withZoom(ZoomBuilder.get()
                                .withEnabled(false)
                                .build())
                        .build())
                .withDataLabels(DataLabelsBuilder.get()
                        .withEnabled(false)
                        .build())
                .withStroke(StrokeBuilder.get().withCurve(Curve.STRAIGHT).build())
                .withSeries(inData.toArray(Series[]::new))
                .withTitle(TitleSubtitleBuilder.get()
                        .withText("Sales Of Different Products over the years")
                        .withAlign(Align.LEFT).build())
                .withSubtitle(TitleSubtitleBuilder.get()
                        .withText("Price Movements")
                        .withAlign(Align.LEFT).build())
                .withLabels(IntStream.range(0, dateLables.size()).boxed().map(day -> dateLables.get(day).toString()).toArray(String[]::new))

                .withXaxis(XAxisBuilder.get()
                        .withTitle(com.github.appreciated.apexcharts.config.xaxis.builder.TitleBuilder.get()
                                .withText("Time")
                                .build())
                        .withType(XAxisType.DATETIME).build())
                .withYaxis(YAxisBuilder.get()
                        .withTitle(TitleBuilder.get()
                                .withText("$ Sales")
                                .build())
                        .withOpposite(true)
                        .build())
                .withLegend(LegendBuilder.get().withHorizontalAlign(HorizontalAlign.LEFT).build())
                .build();
        return areaChart;
    }
}
