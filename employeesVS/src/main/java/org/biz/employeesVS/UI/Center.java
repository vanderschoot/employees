package org.biz.employeesVS.UI;

import org.biz.employeesVS.MainApp;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class Center extends CustomComponent {

	private Panel panel = new Panel();
    private final HorizontalSplitPanel split = new HorizontalSplitPanel();
    private MainApp app;
    private Navigator navigator;
    
	public Center(MainApp app) {
		System.out.println("Center created");
		
		this.app = app;
		
		navigator = new Navigator(app);

		panel.setContent(split);
		panel.addStyleName("center");

		split.setHeight(500, Sizeable.UNITS_PIXELS);

		split.setSplitPosition(220, HorizontalSplitPanel.UNITS_PIXELS);
		split.setFirstComponent(navigator);

		setCompositionRoot(panel);
	}
	
	public void setScreen(Component scrn) {
		split.setSecondComponent(scrn);		
	}
	
	public void addDialog(Window win, int x, int y) {
		app.addDialog(win,x,y);
	}
	
	public void removeDialog(Window win) {
		app.removeDialog(win);
	}
	
	public void showNotification(String message) {
		app.showNotification(message);
	}	
	
	public Navigator getNavigator() {
		return navigator;
	}
}
