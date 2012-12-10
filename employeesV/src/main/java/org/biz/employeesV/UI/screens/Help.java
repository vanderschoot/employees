package org.biz.employeesV.UI.screens;

import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class Help extends Window {
    private static final String HELPTEXT = 
    		"This application is "
            + "a demonstration of <strong><a href=\""
            + "http://dev.vaadin.com/\">Vaadin</a></strong> "
            + "application development."
            + "<br/><br/>"
            + "Functionality:"
            + "<UL>"
            + "<LI>Login with the Login Button in the Header.</LI>"
            + "<LI>Logout with the Logout Button in the Header.</LI>"
            + "<LI>Select the Screens with the buttons on the left.</LI>"
            + "<LI>return to the Home Page with the HOME Button in the Header.</LI>"
            + "<LI>Show Help with the Help Button in the Header.</LI>"
            + "</UL>"
            + "<br/><br/>"
            + "You can add new users and roles with the User and Role Screens <BR/>"
            + "The 'Administrator' role is required for the User and Role Screens <BR/>"
            + "The 'User' role is required for the Department and Employee Screens <BR/>"
            ;

    public Help() {
		System.out.println("Help created");
        setCaption("Employees help");
        addComponent(new Label(HELPTEXT, Label.CONTENT_XHTML));
        setWidth(300, UNITS_PIXELS);
        setHeight(300, UNITS_PIXELS);
    }
}