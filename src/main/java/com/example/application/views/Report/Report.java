package com.example.application.views.Report;

import com.example.application.views.NewMainLayout;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@PermitAll
@Route(value = "report-form",layout = NewMainLayout.class)
@Uses(Icon.class)
public class Report extends Div {
}
