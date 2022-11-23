package com.example.application.views;

import com.example.application.components.appnav.AppNavItem;
import com.example.application.views.AdvanceSetting.AdvanceSetting;
import com.example.application.views.Calendar.CalendarView;
import com.example.application.views.Dashboard.Dashboard;
import com.example.application.views.Report.Report;
import com.example.application.views.Settings.SettingsView;
import com.example.application.views.UserDetails.UserDetailsView;
import com.example.application.security.SecurityService;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;

import javax.annotation.security.PermitAll;

/**
 * This is the main Layout which will open after login
 * */

@PermitAll

public class NewMainLayout extends AppLayout {

    private H2 viewTitle;
    private final SecurityService securityService;

    // Just Adding 2 Simple Button which will lead to the File upload or dashboard



    public NewMainLayout(SecurityService securityService){
        this.securityService = securityService;
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();


    }



    private void addHeaderContent(){
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("Sales Forecaster");
        Button button = new Button("Log Out",e -> securityService.logout());
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);
        HorizontalLayout headerBar = new HorizontalLayout(new DrawerToggle(),appName,button);
        headerBar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        headerBar.expand(appName);
        headerBar.setWidth("100%");
        headerBar.addClassNames("py-0", "px-m");


        addToNavbar(headerBar);
        addToDrawer(createNavigation());
    }

    private VerticalLayout createNavigation() {
        // AppNav is not yet an official component.
        // For documentation, visit https://github.com/vaadin/vcf-nav#readme
        VerticalLayout nav = new VerticalLayout();

        // Modify Following
        nav.add(new AppNavItem("Dashboard", Dashboard.class, "la la-dashboard"));
        nav.add(new AppNavItem("Report", Report.class, "la la-file"));
        nav.add(new AppNavItem("Calendar", CalendarView.class, "la la-calendar"));
        nav.add(new AppNavItem("User Details", UserDetailsView.class, "la la-user"));
        nav.add(new AppNavItem("Settings", SettingsView.class, "la la-tools"));
        nav.add(new AppNavItem("Advance Settings", AdvanceSetting.class, "la la-toolbox"));
        return nav;
    }
    private Footer createFooter() {
        Footer layout = new Footer();

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
