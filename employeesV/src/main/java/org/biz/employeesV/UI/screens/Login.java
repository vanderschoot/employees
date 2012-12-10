package org.biz.employeesV.UI.screens;

import org.biz.employeesV.MainApp;
import org.biz.employeesV.data.DataModel;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.LoginForm.LoginEvent;
import com.vaadin.ui.LoginForm.LoginListener;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class Login extends Window {

	private DataModel dataModel;
	
    public Login(final MainApp app, Embedded btnLogin, Embedded btnLogout) {
		System.out.println("Login created");
        setCaption("Login");
        LoginForm login = new LoginForm();
        addComponent(login);
        setWidth(300, Sizeable.UNITS_PIXELS);
        setHeight(250, Sizeable.UNITS_PIXELS);
        dataModel = app.getDataModel();
        
        login.addListener(new LoginListener() {
			@Override
			public void onLogin(LoginEvent event) {
				String username = event.getLoginParameter("username");
				String password = event.getLoginParameter("password");
				if (dataModel.Login(username, password)) {
					app.getHeader().LoggedIn();
					app.showNotification("You are logged in.");
				} else {
					app.showNotification("Login is not successfull.");					
				}
			}
        });
    }
}