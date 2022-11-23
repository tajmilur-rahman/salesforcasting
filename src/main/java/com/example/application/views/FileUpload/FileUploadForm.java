package com.example.application.views.FileUpload;

import com.example.application.services.weka.SampleService;
import com.example.application.views.Calendar.CalendarView;
import com.example.application.views.NewMainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import tech.tablesaw.api.Table;

import javax.annotation.security.PermitAll;
import java.io.InputStream;
import java.util.List;


@Uses(Icon.class)
@PermitAll
@Route(value = "file-upload-form",layout = NewMainLayout.class)
public class FileUploadForm extends Div {
    MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
    Upload upload = new Upload(buffer);
    Button finalizeButton = new Button("Finalize");

    // Fields
    ComboBox<String> DateColumnName = new ComboBox<>("Date Column");

    TextField dateFormatDetails = new TextField("Date Format");
    TextField firstDate = new TextField("First Entry From Date Column");

    ComboBox<String> ProductCategoryColumnName = new ComboBox<>("Product Category Column");
    ComboBox<String> SalesColumnName = new ComboBox<>("Sales Column");
    Table data;
    @Autowired
    SampleService sampleService;

    public FileUploadForm(SampleService sampleService){
        this.sampleService = sampleService;
        addClassName("file-upload-form-view");
        DateColumnName.setVisible(false);
        DateColumnName.setAllowCustomValue(false);
        ProductCategoryColumnName.setVisible(false);
        ProductCategoryColumnName.setAllowCustomValue(false);
        SalesColumnName.setVisible(false);
        SalesColumnName.setAllowCustomValue(false);
        dateFormatDetails.setVisible(false);
        firstDate.setVisible(false);
        firstDate.setEnabled(false);
        finalizeButton.setVisible(false);


        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());
        upload.setAcceptedFileTypes(".csv");

        finalizeButton.addClickListener(event -> {

            String dateColumnName = DateColumnName.getValue();
            String categoryColName = ProductCategoryColumnName.getValue();
            String salesColName = SalesColumnName.getValue();
            data.retainColumns(dateColumnName,categoryColName,salesColName);
            Table soreted = data.sortOn(dateColumnName);

            sampleService.endDateLocal = soreted.last(1).row(0).getDate(dateColumnName);
            sampleService.startDateLocal = soreted.first(1).row(0).getDate(dateColumnName);


//            System.out.println("First Entery:\n"+soreted.first(3));
//            System.out.println("Date Column:"+soreted.row(1).getDate(dateColumnName));
//            System.out.println("First Entery:\n"+soreted.last(3));

            sampleService.dateColName = dateColumnName;
            sampleService.productCategoryName = categoryColName;
            sampleService.salesColName = salesColName;
            System.out.println(data.structure());
            sampleService.tableData = data;
            finalizeButton.getUI().ifPresent(ui -> ui.navigate(CalendarView.class));

                });

        upload.addSucceededListener(e->{
            String fileName = e.getFileName();
            InputStream inputStream = buffer.getInputStream(fileName);

            data = Table.read().csv(inputStream);
            List<String> colNames = data.columnNames();
            DateColumnName.setVisible(true);
            DateColumnName.setItems(colNames);

            ProductCategoryColumnName.setVisible(true);
            ProductCategoryColumnName.setItems(colNames);
            SalesColumnName.setVisible(true);
            SalesColumnName.setItems(colNames);
            dateFormatDetails.setVisible(true);
            System.out.println(data.structure());
            sampleService.tableData = data;
            finalizeButton.setVisible(true);
        });
    }

    private Component createTitle() {
        return new H3("File Details");
    }

    private Component createFormLayout() {

        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("50%");
        VerticalLayout hh2 = new VerticalLayout(upload,DateColumnName,ProductCategoryColumnName, SalesColumnName,finalizeButton);
        hh2.setAlignItems(FlexComponent.Alignment.CENTER);

//        HorizontalLayout hh3 = new HorizontalLayout(firstDate,dateFormatDetails);
        formLayout.add(hh2);


//        formLayout.add(firstName, lastName, userId, password,passwordConfirm, email, role);
        return hh2;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        return buttonLayout;
    }
}
