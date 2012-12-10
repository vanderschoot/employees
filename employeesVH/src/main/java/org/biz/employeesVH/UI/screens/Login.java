package org.biz.employeesVH.UI.screens;

import org.biz.employeesVH.MainApp;
import org.biz.employeesVH.data.DataModel;
import org.biz.employeesVH.LoggedInUser;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.LoginForm.LoginEvent;
import com.vaadin.ui.LoginForm.LoginListener;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class Login extends Window {

	private DataModel dataModel;
	
    public Login(final MainApp app) {
		System.out.println("Login created");
        setCaption("Login");
        LoginForm login = new LoginForm();
        addComponent(login);
        setWidth(300, Sizeable.UNITS_PIXELS);
        setHeight(250, Sizeable.UNITS_PIXELS);
        this.dataModel = app.getDataModel();
        
        login.addListener(new LoginListener() {
			@Override
			public void onLogin(LoginEvent event) {
				String username = event.getLoginParameter("username");
				String password = event.getLoginParameter("password");
				if (dataModel.Login(username, password)) {
					
					LoggedInUser usr = app.getLoggedInUser();
					usr.setUsername(username);
					usr.setPassword(password);
					
				    usr.setRoles(dataModel.getroles(username));					
					
					app.getHeader().LoggedIn();
					app.showNotification("Hi " + username +  " You are logged in.");
				} else {
					app.showNotification("Login is not successfull.");					
				}
			}
        });
    }
}