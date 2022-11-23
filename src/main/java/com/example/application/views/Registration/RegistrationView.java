package com.example.application.views.Registration;

import com.example.application.data.service.UserDetailsServiceNew;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("usrreg")
@AnonymousAllowed
public class RegistrationView extends VerticalLayout {

   public RegistrationView(UserDetailsServiceNew personService) {
       RegistrationForm registrationForm = new RegistrationForm();
       // Center the RegistrationForm
       setHorizontalComponentAlignment(Alignment.CENTER, registrationForm);

       add(registrationForm);


       RegistrationFormBinder registrationFormBinder = new RegistrationFormBinder(registrationForm,personService);
       registrationFormBinder.addBindingAndValidation();
   }
}