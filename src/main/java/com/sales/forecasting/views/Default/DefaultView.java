package com.sales.forecasting.views.Default;

import com.sales.forecasting.views.NewMainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@PermitAll
@Route(value = "",layout = NewMainLayout.class)
@Uses(Icon.class)
public class DefaultView extends Div {

    Button fileUpload = new Button("Upload Sales Files");
    Button dashboardButton = new Button("Go to Dashboard");

    DefaultView(){
        addButtons();
    }

    private void  addButtons(){
        HorizontalLayout hl = new HorizontalLayout();
        VerticalLayout vl = new VerticalLayout();
        hl.add(fileUpload,dashboardButton);
        vl.add(hl);

        hl.setAlignItems(FlexComponent.Alignment.CENTER);
        vl.setAlignItems(FlexComponent.Alignment.CENTER);


        add(vl);

        fileUpload.addClickListener(e -> fileUpload.getUI().ifPresent(ui -> ui.navigate("file-upload-form")));
        dashboardButton.addClickListener(e -> dashboardButton.getUI().ifPresent(ui -> ui.navigate("dashboard-view")));
    }
}
