package com.example.application.views.Login;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("Login | Sales Forecasting")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver  {

    private final LoginForm login = new LoginForm();
    public LoginView(){
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        Button signUpButton = new Button("Sign Up");
        signUpButton.addClickListener(e -> signUpButton.getUI().ifPresent(ui -> ui.navigate("usrreg")));


        login.setAction("login");
        add(new H1("Sales Forecast"),login,signUpButton);
    }
    @Override
    public void beforeEnter(BeforeEnterEvent event) {

        // This is to show the authentication requirement
        if(event.getLocation().getQueryParameters().getParameters().containsKey("error")){
            login.setError(true);
        }

    }
}
