package org.biz.employeesVS.UI;

import java.util.List;

import org.biz.employeesVS.LoggedInUser;
import org.biz.employeesVS.MainApp;
import org.biz.employeesVS.UI.screens.Departments;
import org.biz.employeesVS.UI.screens.Employees;
import org.biz.employeesVS.UI.screens.Roles;
import org.biz.employeesVS.UI.screens.Users;
import org.biz.employeesVS.data.DataModel;

import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class Navigator extends CustomComponent {

	private VerticalLayout layout;
	private Panel panel = new Panel();
	private DataModel dataModel;
	private LoggedInUser user;
	private MainApp app;
	private Navigator navigator = this;

	private Embedded btnDepartments = new Embedded("", new ThemeResource("images/Departments.png"));
	private Embedded btnEmployees = new Embedded("", new ThemeResource("images/Employees.png"));
	private Embedded btnUsers = new Embedded("", new ThemeResource("images/Users.png"));
	private Embedded btnRoles = new Embedded("", new ThemeResource("images/Roles.png"));
	
    private Departments scrDepartments;
    private Employees scrEmployees;
    private Users scrUsers;
    private Roles scrRoles;
    
	public Navigator(final MainApp app) {
		System.out.println("Navigator created");
		this.dataModel = app.getDataModel();
		this.user = app.getLoggedInUser();
		this.app = app;
		
		btnDepartments.setVisible(false);
		btnEmployees.setVisible(false);
		btnUsers.setVisible(false);
		btnRoles.setVisible(false);
		
		layout = new VerticalLayout();
		panel.setContent(layout);
        panel.addStyleName("navigator");
		
		layout.addComponent(btnDepartments);
		layout.addComponent(btnEmployees);
		layout.addComponent(btnUsers);
		layout.addComponent(btnRoles);
		
		layout.setMargin(true);
		layout.setSpacing(true);
		
	    scrDepartments = null;
	    scrEmployees = null;
	    scrRoles = null;
	    scrUsers = null;

		btnDepartments.addListener(new ClickListener() {
			@Override
			public void click(ClickEvent event) {
			    if (scrDepartments == null) {
			    	scrDepartments = new Departments(dataModel, navigator);
			    	scrEmployees = new Employees(dataModel, navigator);
			    	//pre-loaded Employees, otherwise Departments will get error when navigating to Employees
			    }
				dataModel.getDepartmentContainer().removeAllContainerFilters();
				app.getCenter().setScreen(scrDepartments);
			}
		});
		
		btnEmployees.addListener(new ClickListener() {
			@Override
			public void click(ClickEvent event) {
				if (scrEmployees == null) scrEmployees = new Employees(dataModel, navigator);
				dataModel.getEmployeeContainer().removeAllContainerFilters();
				app.getCenter().setScreen(scrEmployees);
			}
		});
		
		btnUsers.addListener(new ClickListener() {
			@Override
			public void click(ClickEvent event) {
				if (scrUsers == null) scrUsers = new Users(dataModel, navigator);
				dataModel.getEmployeeContainer().removeAllContainerFilters();
				app.getCenter().setScreen(scrUsers);
			}
		});

		btnRoles.addListener(new ClickListener() {
			@Override
			public void click(ClickEvent event) {
				if (scrRoles == null) scrRoles = new Roles(dataModel, navigator);
				dataModel.getEmployeeContainer().removeAllContainerFilters();
				app.getCenter().setScreen(scrRoles);
			}
		});
		setCompositionRoot(panel);
	}
	
	public void setScreen(Component scrn) {
		app.getCenter().setScreen(scrn);
	}
	
	public void addDialog(Window win, int x, int y) {
		app.getCenter().addDialog(win,x,y);
	}
	
	public void removeDialog(Window win) {
		app.getCenter().removeDialog(win);
	}
	
	public Component getScreenDepartments() {
		return scrDepartments;
	}
	
	public Component getScreenEmployees() {
		return scrEmployees;
	}
	
	public void showNotification(String message) {
		app.getCenter().showNotification(message);
	}

	public void setAccessRights() {
		btnDepartments.setVisible(false);
		btnEmployees.setVisible(false);
		btnUsers.setVisible(false);
		btnRoles.setVisible(false);

		user = app.getLoggedInUser();
		if (user != null) {
			List<String> roles = user.getRoles();
			if (roles.contains("User")) {
				btnDepartments.setVisible(true);
				btnEmployees.setVisible(true);
			}
			if (roles.contains("Administrator")) {
				btnUsers.setVisible(true);
				btnRoles.setVisible(true);
			}
		} else {
			System.out.println("Strange, user should not be null!!!");
		}
	}
}
