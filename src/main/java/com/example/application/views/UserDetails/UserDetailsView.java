package com.example.application.views.UserDetails;


import com.example.application.data.entity.UsersDetails;
import com.example.application.data.service.UserDetailsServiceNew;
import com.example.application.views.NewMainLayout;
import com.example.application.security.CustomUserDetails;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.security.PermitAll;

@PermitAll
@Route(value = "user-details-form",layout = NewMainLayout.class)
@Uses(Icon.class)
public class UserDetailsView extends Div {

    private TextField firstName = new TextField("First Name");
    private TextField lastName = new TextField("Last Name");

    private EmailField email = new EmailField("Email");

    private PasswordField password = new PasswordField("Password");
    private PasswordField passwordConfirm = new PasswordField("Confirm Password");

    private TextField role = new TextField("Role");

    private TextField userId = new TextField("User Id");

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private Binder<UsersDetails> binder = new Binder<>(UsersDetails.class);

    public UserDetailsView(UserDetailsServiceNew personService){

        addClassName("person-form-view");
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        String userid = user.getUsername();

        UsersDetails udd = personService.findByID(userid);




        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        binder.bindInstanceFields(this);

        fillForm();


        userId.setValue(udd.getUserId());
        firstName.setValue(udd.getFirstName());
        lastName.setValue(udd.getLastName());
        email.setValue(udd.getEmail());

        cancel.addClickListener(e -> fillForm());
        UsersDetails ud = binder.getBean();
        ud.setPassword(bCryptPasswordEncoder.encode(ud.getPassword()));
        save.addClickListener(e -> {



            personService.update(ud);
            Notification.show(binder.getBean().getClass().getSimpleName() + " details stored.");
            fillForm();
        });


    }

    private void fillForm() {
        binder.setBean(new UsersDetails());
    }

    private Component createTitle() {
        return new H3("Personal information");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        email.setErrorMessage("Please enter a valid email address");

        formLayout.add(firstName, lastName, userId, password,passwordConfirm, email, role);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save);
        buttonLayout.add(cancel);
        return buttonLayout;
    }
}
