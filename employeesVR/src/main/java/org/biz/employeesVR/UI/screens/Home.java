package org.biz.employeesVR.UI.screens;

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
		Label label = new Label("employeesVR : a Vaadin Rich Client Demo App for a REST Webservice Server App");		
		label.setSizeUndefined();
		label.addStyleName("screenheader");
		screenTitle.addComponent(label);
		screenTitle.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
		screenTitle.setMargin(true);
		layout.addComponent(screenTitle);
	}
	
	private void createText() {	

		Label text = new Label(
				"This application is a Vaadin Client application demo wich is connected via REST Webservices to a REST Server application. <BR/>" +
				"A simple login dialog checks the user/password against the database.<BR/>" +
				"The Screen buttons will be visible after login. <BR/>" +
				"The Departments en Employees screens require a user with the role 'User'.<BR/>" +
				"The User and Role screens require a user with the role 'Administrator'.<BR/>" +
				"<BR/><BR/>" +
				"In addition to the user/password login dialog, the server application is secured with standard JAAS security for authentication and authorisation. <BR/>" +
				"When the client logs in on the server application, a programmatic basic authentication servlet login takes place. <BR/>" +
				"The REST services require authorisation based on the roles “User” and “Administrator” as defined in the database.<BR/>" +
				"The JAAS security is configured to make use of a SQLLoginModule for authentication.<BR/><BR/>" +
				"This Client app should be used with one of the Server apps : <BR/>" +
				"<UL>" +
				"<LI><B>employeesREST</B> : a REST Server with MySQL Database </LI>" +
				"<LI><B>employeesRESTH</B> : a REST Server with HyperSQL Database (HSQLDB)</LI>" +
				"</UL>" +
				"");

		text.addStyleName("labeltext");
		text.setSizeFull();
		text.setContentMode(Label.CONTENT_XHTML);
		layout.addComponent(text);
	}
}
