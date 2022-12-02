package com.example.application.views.Registration;

import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

import java.util.stream.Stream;

public class RegistrationForm extends FormLayout {

   private H3 title;

   private TextField firstName;
   private TextField lastName;

   private EmailField email;

   private PasswordField password;
   private PasswordField passwordConfirm;

   private TextField role;

   private TextField userId;



   private Span errorMessageField;

   private Button submitButton;

   private Button goLogin;


   public RegistrationForm() {
       title = new H3("Signup form");
       firstName = new TextField("First name");
       lastName = new TextField("Last name");
       email = new EmailField("Email");
       role = new TextField("Role");
       userId = new TextField("User ID");
       goLogin = new Button("Go to Login");




       password = new PasswordField("Password");
       passwordConfirm = new PasswordField("Confirm password");

       setRequiredIndicatorVisible(firstName, lastName, email,role,userId, password,
               passwordConfirm);

       errorMessageField = new Span();

       submitButton = new Button("Register For Getting Started");
       submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
       goLogin.setVisible(false);

       add(title, firstName, lastName, email,role,userId, password,
               passwordConfirm, errorMessageField,
               submitButton,goLogin);

       // Max width of the Form
       setMaxWidth("500px");

       // Allow the form layout to be responsive.
       // On device widths 0-490px we have one column.
       // Otherwise, we have two columns.
       setResponsiveSteps(
               new ResponsiveStep("0", 1, ResponsiveStep.LabelsPosition.TOP),
               new ResponsiveStep("490px", 2, ResponsiveStep.LabelsPosition.TOP));

       // These components always take full width
       setColspan(title, 2);
       setColspan(email, 2);
       setColspan(errorMessageField, 2);
       setColspan(submitButton, 2);
   }


    public TextField getUserId() {
        return userId;
    }

    public void setUserId(TextField userId) {
        this.userId = userId;
    }

    public EmailField getEmail() {
        return email;
    }

    public void setEmail(EmailField email) {
        this.email = email;
    }

    public PasswordField getPasswordField() { return password; }

   public PasswordField getPasswordConfirmField() { return passwordConfirm; }

   public Span getErrorMessageField() { return errorMessageField; }

   public Button getSubmitButton() { return submitButton; }

    public Button getGoLogin() {
        return goLogin;
    }

    public TextField getFirstName() {
        return firstName;
    }

    public void setFirstName(TextField firstName) {
        this.firstName = firstName;
    }

    public TextField getLastName() {
        return lastName;
    }

    public void setLastName(TextField lastName) {
        this.lastName = lastName;
    }

    private void setRequiredIndicatorVisible(HasValueAndElement<?, ?>... components) {
       Stream.of(components).forEach(comp -> comp.setRequiredIndicatorVisible(true));
   }

}