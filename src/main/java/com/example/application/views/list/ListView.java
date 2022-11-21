package com.example.application.views.list;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.security.PermitAll;

import org.springframework.stereotype.Service;

import com.example.application.data.entity.Contact;
import com.example.application.data.service.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.example.application.views.*;

@PermitAll
@Route(value="", layout = MainLayout.class)
@PageTitle("Contacts | Vaadin CRM")
public class ListView extends VerticalLayout { 
    // here defining all the components of the page
	Grid<Contact> grid = new Grid<>(Contact.class); 
    TextField filterText = new TextField();
    TextField newfield = new TextField();
    
    ContactForm cform;
    
    CrmService service;
    
    

    public ListView(CrmService service) {
    	
    	// THis part makes every thing in loop
    	this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configForm();
        add(getSoimething(),  getComp()); 
        
        updateList();
        closeEditor(); 
    }
    private Component getComp() {
    	 HorizontalLayout content = new HorizontalLayout(grid, cform);
    	 content.setFlexGrow(2, grid); 
         content.setFlexGrow(1, cform);
         content.addClassNames("content");
         content.setSizeFull();
         return content;
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "email"); 
        grid.addColumn(contact -> contact.getStatus().getName()).setHeader("Status"); 
        grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company");
        grid.getColumns().forEach(col -> col.setAutoWidth(true)); 
        
        grid.asSingleSelect().addValueChangeListener(event ->editContact(event.getValue()));
    }

    private void editContact(Contact contact) {
		// TODO Auto-generated method stub
    	if (contact == null) {
            closeEditor();
        } else {
            cform.setContact(contact);
            cform.setVisible(true);
            addClassName("editing");
        }
	}
    
    private void addContact() { 
        grid.asSingleSelect().clear();
        editContact(new Contact());
    }
    private void closeEditor() {
        cform.setContact(null);
        cform.setVisible(false);
        removeClassName("editing");
    }
	private HorizontalLayout getSoimething() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY); 
        filterText.addValueChangeListener(e -> updateList());
        newfield.setPlaceholder("Somemthing");

        Button addContactButton = new Button("Add contact");
        addContactButton.addClickListener(click -> addContact()); 

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton,newfield);  // So you can added the components as you like one by one.
        toolbar.addClassName("toolbar");
        return toolbar;
    }
    
    private void configForm() {
    	cform = new ContactForm(service.findAllCompanies(), service.findAllStatuses());
    	cform.setWidth("25em");
    	cform.addListener(ContactForm.SaveEvent.class, this::saveContact); 
        cform.addListener(ContactForm.DeleteEvent.class, this::deleteContact); 
        cform.addListener(ContactForm.CloseEvent.class, e -> closeEditor()); 
    }
    private void saveContact(ContactForm.SaveEvent event) {
        service.saveContact(event.getContact());
        updateList();
        closeEditor();
    }

    private void deleteContact(ContactForm.DeleteEvent event) {
        service.deleteContact(event.getContact());
        updateList();
        closeEditor();
    }
    private void updateList() { 
        grid.setItems(service.findAllContacts(filterText.getValue()));
    }
}