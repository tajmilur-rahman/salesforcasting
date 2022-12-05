package com.example.application.views.FileUpload;

import com.example.application.services.weka.SampleService;
import com.example.application.views.Calendar.CalendarView;
import com.example.application.views.NewMainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;

import javax.annotation.security.PermitAll;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Uses(Icon.class)
@PermitAll
@Route(value = "file-upload-form",layout = NewMainLayout.class)
public class FileUploadForm extends Div {
    MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
    Upload upload = new Upload(buffer);
    Button finalizeButton = new Button("Finalize");
    H3 invalidFile = new H3("Invalid File");

    // Fields
    ComboBox<String> DateColumnName = new ComboBox<>("Date Column");

    TextField dateFormatDetails = new TextField("Date Format");
    TextField firstDate = new TextField("First Entry From Date Column");

    ComboBox<String> ProductCategoryColumnName = new ComboBox<>("Product Category Column");
    ComboBox<String> SalesColumnName = new ComboBox<>("Sales Column");

    ComboBox<String> dateFormats = new ComboBox<>("Date Format");

    TextArea first5EntrySampleData = new TextArea("First 5 Entries");

        Grid<RowS> sampleDataShow = new Grid<>();
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
        first5EntrySampleData.setVisible(false);
        invalidFile.setVisible(false);
        first5EntrySampleData.setEnabled(false);
        dateFormats.setVisible(false);


        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());
        upload.setAcceptedFileTypes(".csv");

        finalizeButton.addClickListener(event -> {

            String dateColumnName = DateColumnName.getValue();
            String categoryColName = ProductCategoryColumnName.getValue();
            String salesColName = SalesColumnName.getValue();
            boolean flagTmp = false;

            if(data.column(categoryColName).type() == ColumnType.INTEGER){
                // convert to string
                List<Integer> cateData  = (List<Integer>) data.column(categoryColName).asList();
                data = data.removeColumns(categoryColName);
                List<String> cateString = new ArrayList<>();
                cateData.forEach(c->cateString.add(c.toString()));
                data = data.addColumns(StringColumn.create(categoryColName,cateString));
            }

            if(data.column(dateColumnName).type()== ColumnType.LOCAL_DATE ||data.column(dateColumnName).type()== ColumnType.LOCAL_DATE_TIME ){
                flagTmp = true;
            }

            if(data.column(dateColumnName).type()==ColumnType.STRING){
                String df = dateFormats.getValue();
                System.out.println("Date Format:"+df);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern(df);
                List<String> allDates = (List<String>) data.column(dateColumnName).asList();
                System.out.println("Entries in Data:"+allDates.size());
                List<LocalDate> localDateListmew = new ArrayList<>();
                LocalDate prevLd = LocalDate.of(2010,10,10);

                    for(String sDate:allDates){
                        try{
                            if(sDate == null){
                                localDateListmew.add(prevLd);
                            }
                            else {
                                localDateListmew.add(LocalDate.parse(sDate,dtf));
                                prevLd = LocalDate.parse(sDate,dtf);
                            }
                        }
                        catch (Exception e){
                            flagTmp = false;
                            invalidFile.setText("Either Date column is not selected correct or date format!");
                        }


                    }

                if(localDateListmew.size()==allDates.size()){
                    data = data.removeColumns(dateColumnName);
                    data = data.addColumns(DateColumn.create(dateColumnName,localDateListmew));
                    System.out.println("Date Conversion Completed!");
                    System.out.println(data.first(5));
                    flagTmp = true;
                }else{
                    invalidFile.setText("There is some issue in file please check and re-upload!");
                    flagTmp = false;
                }

            }


            if(data.column(dateColumnName).type() == ColumnType.INTEGER){
                // Try to make year
                if(data.column(dateColumnName).getString(0).toCharArray().length>3){
                    System.out.println("The Column is in Year anc can be converted to years"+data.column(dateColumnName).getString(0).toCharArray().length);
                    List<Integer> years = (ArrayList<Integer>) data.column(dateColumnName).asList();
                    List<LocalDate> lDates = new ArrayList<>();
                    int preYear = 2000;
                    for(Integer y:years){
                        if(y==null){
                            lDates.add(LocalDate.of(preYear,12,31));
                        }else{
                            lDates.add(LocalDate.of(y,12,31));
                            preYear = y;
                        }
                    }
                    data = data.removeColumns(dateColumnName);
                    data = data.addColumns(DateColumn.create(dateColumnName,lDates));
                    flagTmp = true;
                }
                else {
                    invalidFile.setText("There is some issue in file please check and re-upload!");
                    invalidFile.setVisible(true);
                }

            }

            if(flagTmp){
                data = data.retainColumns(dateColumnName,categoryColName,salesColName);
                for(String col : data.columnNames()){
                    System.out.println(data.column(col).type());
                }
                Table soreted = data.sortOn(dateColumnName);
                System.out.println(soreted);

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

            }



                });

        upload.addSucceededListener(e->{
            sampleService.resetAllVariables();
            invalidFile.setVisible(false);
            String fileName = e.getFileName();
            InputStream inputStream = buffer.getInputStream(fileName);

            List<String> dFormat = new ArrayList<>();
            dFormat.add("dd-MM-yyyy");
            dFormat.add("dd/MM/yyyy");
            dFormat.add("MM-dd-yyyy");
            dFormat.add("MM/dd/yyyy");
            dFormat.add("yyyy-MM-dd");
            dFormat.add("yyyy-dd-MM");
            dFormat.add("yyyy/MM/dd");
            dFormat.add("yyyy/dd/MM");
            dateFormats.setItems(dFormat);
            dateFormats.setAllowCustomValue(true);
            dateFormats.setVisible(true);

            data = Table.read().csv(inputStream);
            List<String> colNames = data.columnNames();
            List<RowS> tempData = new ArrayList<>();
            sampleDataShow.removeAllColumns();
//            int k = 5;
//            if(30<data.rowCount()){
//                k = 30;
//            }
            for(int i=0;i<data.rowCount();i++) {
                RowS row = new RowS();
                for(String col:colNames){
                    if(i==0){
                        sampleDataShow.addColumn(rrow -> rrow.getValue(col)).setHeader(col);
                    }
                    row.setValue(col, data.column(col).getString(i));

                }
                tempData.add(row);

            }
            sampleDataShow.setItems(tempData);

            DateColumnName.setVisible(true);
            DateColumnName.setItems(colNames);

            ProductCategoryColumnName.setVisible(true);
            ProductCategoryColumnName.setItems(colNames);
            SalesColumnName.setVisible(true);
            SalesColumnName.setItems(colNames);
            dateFormatDetails.setVisible(true);
            sampleDataShow.setVisible(true);
//            first5EntrySampleData.setVisible(true);
//            first5EntrySampleData.setEnabled(true);
//            first5EntrySampleData.setValue(data.first(5).print());
            System.out.println(data.first(5).toString());

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
        first5EntrySampleData.setWidthFull();
        VerticalLayout sd =new VerticalLayout(sampleDataShow);

        HorizontalLayout hs = new HorizontalLayout(DateColumnName,dateFormats,ProductCategoryColumnName, SalesColumnName,finalizeButton);
        VerticalLayout hh2 = new VerticalLayout(invalidFile,upload,sd,hs);
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
