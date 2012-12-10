/*
 * Employees demo application
 */
package org.biz.employeesVR;

import org.biz.employeesVR.UI.Center;
import org.biz.employeesVR.UI.Footer;
import org.biz.employeesVR.UI.Header;
import org.biz.employeesVR.UI.Navigator;
import org.biz.employeesVR.data.DataModel;

import com.vaadin.Application;
import com.vaadin.ui.Window;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class MainApp extends Application
{
    private Window window;
    private DataModel dataModel = new DataModel(this);
    private Center center = new Center(this);
    private Header header = new Header(this);
    private Footer footer = new Footer();
    private LoggedInUser loggedInUser = new LoggedInUser();

    @Override
    public void init()
    {
        window = new Window("Employees");
        setMainWindow(window);
		setTheme("employees");
				
		window.addComponent(header);
		window.addComponent(center);
		window.addComponent(footer);		
    }
    
    public Center getCenter() {
    	return center;
    }

    public Header getHeader() {
    	return header;
    }
    
    public Navigator getNavigator() {
    	return center.getNavigator();
    }
    
    public DataModel getDataModel() {
    	return dataModel;
    }
    
    public LoggedInUser getLoggedInUser() {
    	return loggedInUser;
    }
    
	public void addDialog(Window win, int x, int y) {
		window.addWindow(win);
		win.setPositionX(x);
		win.setPositionY(y);
	}
	
	public void removeDialog(Window win) {
		window.removeWindow(win);
	}
	
	public void showNotification(String message) {
		window.showNotification(message);
	}	
	
	public void showError(String message) {
		window.showNotification(message,Window.Notification.TYPE_ERROR_MESSAGE);
	}	

}
