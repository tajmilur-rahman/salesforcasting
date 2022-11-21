package com.example.application.views.fileupload;

import com.example.application.data.entity.SalesData;
import com.example.application.data.entity.UsersDetails;
import com.example.application.data.service.UserDetailsServiceNew;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tech.tablesaw.api.Table;

import java.io.InputStream;
import java.util.List;

@AnonymousAllowed
@PageTitle("File Upload Form")
@Route(value = "file-upload-form", layout = MainLayout.class)
@Uses(Icon.class)
public class FileUploadForm extends Div {

    MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
    Upload upload = new Upload(buffer);

    // Fields
    ComboBox<String> DateColumnName = new ComboBox<>("Date Column");


    ComboBox<String> ProductCategoryColumnName = new ComboBox<>("Product Category Column");
    ComboBox<String> SalesColumnName = new ComboBox<>("Sales Column");
    Table data;

    // Buttons


    public FileUploadForm(UserDetailsServiceNew personService) {
        addClassName("file-upload-form-view");
        DateColumnName.setVisible(false);
        DateColumnName.setAllowCustomValue(false);
        ProductCategoryColumnName.setVisible(false);
        ProductCategoryColumnName.setAllowCustomValue(false);
        SalesColumnName.setVisible(false);
        SalesColumnName.setAllowCustomValue(false);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        clearForm();

        upload.setAcceptedFileTypes(".csv");

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
        });

//        cancel.addClickListener(e -> clearForm());
//        UsersDetails ud = binder.getBean();
//        ud.setPassword(bCryptPasswordEncoder.encode(ud.getPassword()));
//        save.addClickListener(e -> {
//
//            personService.update(ud);
//            Notification.show(binder.getBean().getClass().getSimpleName() + " details stored.");
//            clearForm();
//        });
    }

    private void clearForm() {
//        binder.setBean(new UsersDetails());
    }

    private Component createTitle() {
        return new H3("File Details");
    }

    private Component createFormLayout() {
        HorizontalLayout hh = new HorizontalLayout(upload);
        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("50%");
        HorizontalLayout hh2 = new HorizontalLayout(DateColumnName,ProductCategoryColumnName, SalesColumnName);
        formLayout.add(hh,hh2);


//        formLayout.add(firstName, lastName, userId, password,passwordConfirm, email, role);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
//        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        buttonLayout.add(save);
//        buttonLayout.add(cancel);
        return buttonLayout;
    }

    private static class PhoneNumberField extends CustomField<String> {
        private ComboBox<String> countryCode = new ComboBox<>();
        private TextField number = new TextField();

        public PhoneNumberField(String label) {
            setLabel(label);
            countryCode.setWidth("120px");
            countryCode.setPlaceholder("Country");
            countryCode.setPattern("\\+\\d*");
            countryCode.setPreventInvalidInput(true);
            countryCode.setItems("+354", "+91", "+62", "+98", "+964", "+353", "+44", "+972", "+39", "+225");
            countryCode.addCustomValueSetListener(e -> countryCode.setValue(e.getDetail()));
            number.setPattern("\\d*");
            number.setPreventInvalidInput(true);
            HorizontalLayout layout = new HorizontalLayout(countryCode, number);
            layout.setFlexGrow(1.0, number);
            add(layout);
        }

        @Override
        protected String generateModelValue() {
            if (countryCode.getValue() != null && number.getValue() != null) {
                String s = countryCode.getValue() + " " + number.getValue();
                return s;
            }
            return "";
        }

        @Override
        protected void setPresentationValue(String phoneNumber) {
            String[] parts = phoneNumber != null ? phoneNumber.split(" ", 2) : new String[0];
            if (parts.length == 1) {
                countryCode.clear();
                number.setValue(parts[0]);
            } else if (parts.length == 2) {
                countryCode.setValue(parts[0]);
                number.setValue(parts[1]);
            } else {
                countryCode.clear();
                number.clear();
            }
        }
    }

}
