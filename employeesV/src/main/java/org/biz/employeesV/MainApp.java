/*
 * Employees demo application
 */
package org.biz.employeesV;

import org.biz.employeesV.UI.Center;
import org.biz.employeesV.UI.Footer;
import org.biz.employeesV.UI.Header;
import org.biz.employeesV.data.DataModel;

import com.vaadin.Application;
import com.vaadin.ui.Window;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class MainApp extends Application
{
    private Window window;
    private DataModel dataModel = new DataModel();
    private Center center = new Center(this);
    private Header header = new Header(this);
    private Footer footer = new Footer();

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

    
    public DataModel getDataModel() {
    	return dataModel;
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

}
