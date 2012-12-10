package org.biz.employeesV.UI;

import org.biz.employeesV.UI.screens.Departments;
import org.biz.employeesV.UI.screens.Employees;
import org.biz.employeesV.UI.screens.Roles;
import org.biz.employeesV.UI.screens.Users;
import org.biz.employeesV.data.DataModel;

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
	private Center center;

	private Embedded btnDepartments = new Embedded("", new ThemeResource("images/Departments.png"));
	private Embedded btnEmployees = new Embedded("", new ThemeResource("images/Employees.png"));
	private Embedded btnUsers = new Embedded("", new ThemeResource("images/Users.png"));
	private Embedded btnRoles = new Embedded("", new ThemeResource("images/Roles.png"));
	
    private Departments scrDepartments;
    private Employees scrEmployees;
    private Users scrUsers;
    private Roles scrRoles;
    
	public Navigator(final Center center, final DataModel dataModel) {
		System.out.println("Navigator created");
		this.center = center;
		layout = new VerticalLayout();
		panel.setContent(layout);
        panel.addStyleName("navigator");
		
		layout.addComponent(btnDepartments);
		layout.addComponent(btnEmployees);
		layout.addComponent(btnUsers);
		layout.addComponent(btnRoles);
		
		layout.setMargin(true);
		layout.setSpacing(true);
		
	    scrDepartments = new Departments(dataModel, this);
	    scrEmployees = new Employees(dataModel, this);
	    scrUsers = new Users(dataModel, this);
	    scrRoles = new Roles(dataModel, this);

		btnDepartments.addListener(new ClickListener() {
			@Override
			public void click(ClickEvent event) {
				dataModel.getDepartmentContainer().removeAllContainerFilters();
				center.setScreen(scrDepartments);
			}
		});
		
		btnEmployees.addListener(new ClickListener() {
			@Override
			public void click(ClickEvent event) {
				dataModel.getEmployeeContainer().removeAllContainerFilters();
				center.setScreen(scrEmployees);
			}
		});
		
		btnUsers.addListener(new ClickListener() {
			@Override
			public void click(ClickEvent event) {
				dataModel.getEmployeeContainer().removeAllContainerFilters();
				center.setScreen(scrUsers);
			}
		});

		btnRoles.addListener(new ClickListener() {
			@Override
			public void click(ClickEvent event) {
				dataModel.getEmployeeContainer().removeAllContainerFilters();
				center.setScreen(scrRoles);
			}
		});

		
		setCompositionRoot(panel);
	}
	
	public void setScreen(Component scrn) {
		center.setScreen(scrn);
	}
	
	public void addDialog(Window win, int x, int y) {
		center.addDialog(win,x,y);
	}
	
	public void removeDialog(Window win) {
		center.removeDialog(win);
	}
	
	public Component getScreenDepartments() {
		return scrDepartments;
	}
	
	public Component getScreenEmployees() {
		return scrEmployees;
	}
	
	public void showNotification(String message) {
		center.showNotification(message);
	}


}
