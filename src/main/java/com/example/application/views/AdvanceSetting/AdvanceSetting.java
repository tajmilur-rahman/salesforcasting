package com.example.application.views.AdvanceSetting;

import com.example.application.views.NewMainLayout;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@PermitAll
@Route(value = "advance-setting-form",layout = NewMainLayout.class)
@Uses(Icon.class)
public class AdvanceSetting extends Div {

    private H2 info = new H2("Feature Will Be Developed In Future");

    public AdvanceSetting(){
        addThings();

    }
    public void addThings(){
        VerticalLayout vl = new VerticalLayout();

        vl.add(new H1("Caution WUP!"),new H2("Feature Will Be Developed In Future"));
        add(vl);
    }
}
