package com.example.application.views;


import com.example.application.components.appnav.AppNav;
import com.example.application.components.appnav.AppNavItem;
import com.example.application.security.SecurityService;
import com.example.application.views.about.AboutView;
import com.example.application.views.checkoutform.CheckoutFormView;
import com.example.application.views.dashboard.DashboardView;
import com.example.application.views.fileupload.FileUploadForm;
import com.example.application.views.helloworld.HelloWorldView;
import com.example.application.views.helloworld2.HelloWorld2View;
import com.example.application.views.imagelist.ImageListView;
import com.example.application.views.masterdetail.MasterDetailView;
import com.example.application.views.personform.PersonFormView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;

import javax.annotation.security.PermitAll;

/**
 * The main view is a top-level placeholder for other views.
 */
@PermitAll
public class MainLayout extends AppLayout {

    private H2 viewTitle;
    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("My App");
        Button button = new Button("Log Out",e -> securityService.logout());
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);
        HorizontalLayout headerBar = new HorizontalLayout(new DrawerToggle(),button);

        Scroller scroller = new Scroller(createNavigation());
//        addToNavbar(headerBar);
        addToDrawer(header, scroller, createFooter(),button);
    }

    private AppNav createNavigation() {
        // AppNav is not yet an official component.
        // For documentation, visit https://github.com/vaadin/vcf-nav#readme
        AppNav nav = new AppNav();

        nav.addItem(new AppNavItem("Hello World", HelloWorldView.class, "lab la-accessible-icon"));
        nav.addItem(new AppNavItem("About", AboutView.class, "la la-file"));
        nav.addItem(new AppNavItem("Upload Data", FileUploadForm.class, "la la-file"));
        nav.addItem(new AppNavItem("Dashboard", DashboardView.class, "la la-chart-area"));
        nav.addItem(new AppNavItem("Person Form", PersonFormView.class, "la la-user"));
        nav.addItem(new AppNavItem("Master-Detail", MasterDetailView.class, "la la-columns"));
        nav.addItem(new AppNavItem("Image List", ImageListView.class, "la la-th-list"));
        nav.addItem(new AppNavItem("Hello World2", HelloWorld2View.class, "la la-globe"));
        nav.addItem(new AppNavItem("Checkout Form", CheckoutFormView.class, "la la-credit-card"));

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
