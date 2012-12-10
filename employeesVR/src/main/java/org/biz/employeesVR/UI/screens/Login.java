package org.biz.employeesVR.UI.screens;

import org.biz.employeesVR.MainApp;
import org.biz.employeesVR.LoggedInUser;
import org.biz.employeesVR.data.DataModel;
import org.biz.employeesVR.data.RestServiceFactory;
import org.biz.employeesVR.data.ReturnStatus;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Label;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.LoginForm.LoginEvent;
import com.vaadin.ui.LoginForm.LoginListener;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class Login extends Window {

	private DataModel dataModel;
   	private RestServiceFactory rsf;
	
    public Login(final MainApp app, final Label lblUser) {
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
		        rsf = dataModel.getRestServiceFactory();
				String username = event.getLoginParameter("username");
				String password = event.getLoginParameter("password");
				ReturnStatus sts = rsf.login(username, password);
				if (sts.isSucces()) {					
					LoggedInUser usr = app.getLoggedInUser();
					usr.setUsername(username);
					usr.setPassword(password);
					
					List<String> rolelist = new ArrayList<String>();
					String[] roles = sts.getMessage().split(",");

				    for (int x=0; x<roles.length; x++) {
				    	rolelist.add(roles[x]);
				        System.out.println("role: " +  roles[x]);
				    }
				    usr.setRoles(rolelist);
				    
					lblUser.setValue(username);
				    
					app.getHeader().LoggedIn();
					app.showNotification("Hi " + username +  " You are logged in.");
				} else {
					app.showNotification("Login is not successfull : " + sts.getMessage());										
				}
			}
        });
    }
    
    

    
    
}