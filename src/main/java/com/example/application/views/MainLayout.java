package com.example.application.views;

import com.example.application.views.list.DashboardView;
import com.example.application.views.list.ListView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.Theme;
import com.example.application.security.SecurityService;
import com.example.application.views.charts.*;

@Theme()
public class MainLayout extends AppLayout {
	private final SecurityService securityService;
	
	public MainLayout(SecurityService securityService) {
		 this.securityService = securityService;
		createHead();
		createDrw();
	}
	
	public void createHead() {
		H1 logo = new H1("Sales Forecaster");
		logo.addClassNames("text-l", "m-m"); // is it medatory
		
		
		
		Button logout = new Button("Log out", e -> securityService.logout()); 
		// Following will be added as the header of the page
				HorizontalLayout hl = new HorizontalLayout(
						new DrawerToggle(), // that 3 bar icon
						logo,logout
						);

		
		hl.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
		hl.setWidth("100%"); // nice to have relative values 
		hl.addClassNames("py-0", "px-m"); // is class names are mendatoruy???
		 hl.expand(logo); 
		addToNavbar(hl); // Here header is added to the navigation bar component of the vaadin
	}
	
	public void createDrw() {
		//
		RouterLink link = new RouterLink("list",ListView.class);
		 link.setHighlightCondition(HighlightConditions.sameLocation());
		 addToDrawer(new VerticalLayout( 
				 link,
				 new RouterLink("Dashboard", DashboardView.class)
		        ));
	}
}
