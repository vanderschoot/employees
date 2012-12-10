package org.biz.employeesVS.UI.screens;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class Home extends CustomComponent {

	private Panel panel = new Panel();
	private VerticalLayout layout;
      
	public Home() {
		System.out.println("Home created");

		layout = new VerticalLayout();
		layout.setHeight(100, Sizeable.UNITS_PERCENTAGE);
		layout.setMargin(true);

		panel.setContent(layout);
	    panel.addStyleName("screen");

		createScreenHeader();
		createText();		
		
		setCompositionRoot(panel);
	}
	
	
	private void createScreenHeader() {
		HorizontalLayout screenTitle = new HorizontalLayout();
		screenTitle.setSizeFull();
		Label label = new Label("employeesVS : a Vaadin Rich Client Demo App for a SOAP Webservice Server App");		
		label.setSizeUndefined();
		label.addStyleName("screenheader");
		screenTitle.addComponent(label);
		screenTitle.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
		screenTitle.setMargin(true);
		layout.addComponent(screenTitle);
	}
	
	private void createText() {	

		Label text = new Label(
				"This application is a Vaadin Client application demo wich is connected via SOAP Webservices to a SOAP Server application. <BR/>" +
				"A simple login dialog checks the user/password against the database.<BR/>" +
				"The Screen buttons will be visible after login. <BR/>" +
				"The Departments en Employees screens require a user with the role 'User'.<BR/>" +
				"The User and Role screens require a user with the role 'Administrator'.<BR/>" +
				"<BR/>" +
				"This Client app should be used with one of the Server apps : <BR/>" +
				"<UL>" +
				"<LI><B>employeesSOAP</B>  : a SOAP Server with MySQL Database</LI>" +
				"<LI><B>employeesSOAPH</B> : a SOAP Server with HyperSQL Database (HSQLDB)</LI>" +
				"</UL>" +
				"");

		text.addStyleName("labeltext");
		text.setSizeFull();
		text.setContentMode(Label.CONTENT_XHTML);
		layout.addComponent(text);
	}
}
