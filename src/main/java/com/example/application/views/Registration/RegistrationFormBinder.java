package com.example.application.views.Registration;

import com.example.application.data.entity.UsersDetails;
import com.example.application.data.service.UserDetailsServiceNew;

import com.example.application.views.Login.LoginView;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.validator.EmailValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationFormBinder {

   private RegistrationForm registrationForm;

   /**
    * Flag for disabling first run for password validation
    */
   private boolean enablePasswordValidation;
   private UserDetailsServiceNew personService;
   public RegistrationFormBinder(RegistrationForm registrationForm, UserDetailsServiceNew personService) {
       this.registrationForm = registrationForm;
       this.personService = personService;
   }

   /**
    * Method to add the data binding and validation logics
    * to the registration form
    */
   public void addBindingAndValidation() {
       BeanValidationBinder<UsersDetails> binder = new BeanValidationBinder<>(UsersDetails.class);
       binder.bindInstanceFields(registrationForm);

       // A custom validator for password fields
       binder.forField(registrationForm.getPasswordField())
               .withValidator(this::passwordValidator).bind("password");

       binder.forField(registrationForm.getFirstName())
               .withValidator(this::firstNameLastNameValidator).bind("firstName");
       binder.forField(registrationForm.getLastName())
               .withValidator(this::firstNameLastNameValidator).bind("lastName");
       binder.forField(registrationForm.getEmail())
               .withValidator(new EmailValidator("Please Enter Valid Email: example@domain.com")).bind("email");
       // The second password field is not connected to the Binder, but we
       // want the binder to re-check the password validator when the field
       // value changes. The easiest way is just to do that manually.
       registrationForm.getPasswordConfirmField().addValueChangeListener(e -> {
           // The user has modified the second field, now we can validate and show errors.
           // See passwordValidator() for how this flag is used.
           enablePasswordValidation = true;

           binder.validate();
       });

       // Set the label where bean-level error messages go
       binder.setStatusLabel(registrationForm.getErrorMessageField());

       registrationForm.getGoLogin().addClickListener(event -> {
           registrationForm.getGoLogin().getUI().ifPresent(ui->ui.navigate(LoginView.class));
       });

       // And finally the submit button
       registrationForm.getSubmitButton().addClickListener(event -> {
           try {
               // Create empty bean to store the details into
               UsersDetails userBean = new UsersDetails();
               BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

               // Run validators and write the values to the bean
               binder.writeBean(userBean);

               userBean.setPassword(bCryptPasswordEncoder.encode(userBean.getPassword()));

               // Typically, you would here call backend to store the bean
               System.out.println("Data:"+userBean.getLastName());
               personService.update(userBean);
               // Show success message if everything went well
               showSuccess(userBean);
               registrationForm.getGoLogin().setVisible(true);
           } catch (ValidationException exception) {
               // validation errors are already visible for each field,
               // and bean-level errors are shown in the status label.
               // We could show additional messages here if we want, do logging, etc.
           }
       });
   }

   /**
    * Method to validate that:
    * <p>
    * 1) Password is at least 8 characters long
    * <p>
    * 2) Values in both fields match each other
    */

   private ValidationResult firstNameLastNameValidator(String flname, ValueContext ctx){

       Pattern p = Pattern.compile("[^a-z ]", Pattern.CASE_INSENSITIVE);
       Matcher m = p.matcher(flname);
       if(!m.find()){
        return ValidationResult.ok();
       }

       return ValidationResult.error("First Name and Last Name should not contain numbers of special characters");
   }

   private ValidationResult passwordValidator(String pass1, ValueContext ctx) {
       /*
        * Just a simple length check. A real version should check for password
        * complexity as well!
        */

       if (pass1 == null || pass1.length() < 8) {
           return ValidationResult.error("Password should be at least 8 characters long");
       }

       if (!enablePasswordValidation) {
           // user hasn't visited the field yet, so don't validate just yet, but next time.
           enablePasswordValidation = true;
           return ValidationResult.ok();
       }

       String pass2 = registrationForm.getPasswordConfirmField().getValue();

       if (pass1 != null && pass1.equals(pass2)) {
           return ValidationResult.ok();
       }

       return ValidationResult.error("Passwords do not match");
   }

   /**
    * We call this method when form submission has succeeded
    */
   private void showSuccess(UsersDetails userBean) {
       Notification notification =
               Notification.show("Data saved, welcome " + userBean.getFirstName());
       notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

       // Here you'd typically redirect the user to another view
   }

}