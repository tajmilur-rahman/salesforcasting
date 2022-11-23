package com.example.application.views.Settings;

import com.example.application.views.Dashboard.Dashboard;
import com.example.application.views.FileUpload.FileUploadForm;
import com.example.application.views.NewMainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;


@PermitAll
@Route(value = "setting-form",layout = NewMainLayout.class)
@Uses(Icon.class)
public class SettingsView extends Div {

    Button fileUpload = new Button("File Upload");

    TextArea additionalFeatures = new TextArea();

    public SettingsView(){


        createForm();
        fileUpload.addClickListener(event -> {
            fileUpload.getUI().ifPresent(ui -> ui.navigate(FileUploadForm.class));
        });

    }

    public void createForm(){
        FormLayout fl = new FormLayout();
        fl.add(fileUpload);
        fl.add(additionalFeatures);
        fl.setWidth("60%");
        HorizontalLayout vl = new HorizontalLayout(fl);
        vl.setAlignItems(FlexComponent.Alignment.CENTER);
        additionalFeatures.setValue("Other Features Under Development!");

        additionalFeatures.setEnabled(false);

        add(vl);

    }



}
