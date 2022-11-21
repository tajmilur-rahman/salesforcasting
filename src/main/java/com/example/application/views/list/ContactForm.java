package com.example.application.views.list;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

import java.util.List;

import com.example.application.data.entity.*;
//@Route(value = "") // this flag is used to navigate from the web
public class ContactForm extends FormLayout {
	
	// Add the user inputs here,
	private Contact contact;
	TextField firstName = new TextField("First name"); // In order to work for the binder name in the data entity should be same as the field that it corresponds
	TextField lastName = new TextField("Last name");
	EmailField email = new EmailField("Email");
	ComboBox<Status> status = new ComboBox<>("Status");
	ComboBox<Company> company = new ComboBox<>("Company");
	

	Button save = new Button("Save");
	Button delete = new Button("Delete");
	Button close = new Button("Cancel");
	Binder<Contact> binder = new BeanValidationBinder<>(Contact.class);
	
	  
	  
	
	// These is how the seperate components are created.
	  public ContactForm(List<Company> companies, List<Status> statuses) {
		  this.addClassName("contact-form");
		  binder.bindInstanceFields(this);
		  company.setItems(companies);
		  company.setItemLabelGenerator(Company::getName);
		  status.setItems(statuses);
		  status.setItemLabelGenerator(Status::getName);
		  
		  // following will add all the things to the UI
		  this.add(firstName	,lastName,email,company,status,buttonLayout());
	  }
	  
	  // buttons should be packeged in given stype here we are going to add those in horizontal
	  private HorizontalLayout buttonLayout() {
		  save.addThemeVariants(ButtonVariant.LUMO_PRIMARY); // may be the color of the button
		  delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		  close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		  
		  // Other functions this may be similar to the java script 
		  save.addClickShortcut(Key.ENTER); 
		  close.addClickShortcut(Key.ESCAPE);
		  
		  // now add the actions to perform when button are pressed
		  save.addClickListener(event -> validateAndSave());
		  delete.addClickListener(event -> fireEvent(new DeleteEvent(this, contact))); 
		  close.addClickListener(event -> fireEvent(new CloseEvent(this))); 
		  
		  binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid())); 
		  
		  // add all these components int he horizontal layout and return it
		  return new HorizontalLayout(save,delete,close);
	  }
	  private void validateAndSave() {
		  try {
		    binder.writeBean(contact); 
		    fireEvent(new SaveEvent(this, contact)); 
		  } catch (ValidationException e) {
		    e.printStackTrace();
		  }
		}

	

	public void setContact(Contact contact) {
		this.contact = contact;
		binder.readBean(contact);
	}
	  
	public static abstract class ContactFormEvent extends ComponentEvent<ContactForm> {
		private Contact contact;

		protected ContactFormEvent(ContactForm source, Contact contact) { 
		 super(source, false);
		 this.contact = contact;
		}

		public Contact getContact() {
		 return contact;
		}
		}

		public static class SaveEvent extends ContactFormEvent {
		SaveEvent(ContactForm source, Contact contact) {
		 super(source, contact);
		}
		}

		public static class DeleteEvent extends ContactFormEvent {
		DeleteEvent(ContactForm source, Contact contact) {
		 super(source, contact);
		}

		}

		public static class CloseEvent extends ContactFormEvent {
		CloseEvent(ContactForm source) {
		 super(source, null);
		}
		}

		public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
		 ComponentEventListener<T> listener) { 
		return getEventBus().addListener(eventType, listener);
		}

}