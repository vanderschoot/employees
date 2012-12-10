package org.biz.employeesVS.UI.screens;

import org.biz.employeesVS.MainApp;
import org.biz.employeesVS.LoggedInUser;
import org.biz.employeesVS.data.DataModel;
import org.biz.employeesVS.data.SoapServiceFactory;
import org.biz.employeesVS.ws.ReturnStatus;

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
   	private SoapServiceFactory ssf;
	
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
		        ssf = dataModel.getSoapServiceFactory();
				String username = event.getLoginParameter("username");
				String password = event.getLoginParameter("password");
				ReturnStatus sts = ssf.login(username, password);
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