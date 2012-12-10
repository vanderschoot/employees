package org.biz.employeesV.UI;

import org.biz.employeesV.MainApp;
import org.biz.employeesV.UI.screens.Help;
import org.biz.employeesV.UI.screens.Home;
import org.biz.employeesV.UI.screens.Login;

import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;

@SuppressWarnings("serial")
public class Header extends CustomComponent {

	Panel panel = new Panel();
	private GridLayout layout = new GridLayout(3,1);
	private HorizontalLayout menuButtons = new HorizontalLayout();
	private AbsoluteLayout loginButtons = new AbsoluteLayout();
	
	private MainApp app;

	private Embedded btnHome = new Embedded("", new ThemeResource("images/Home.png"));
	private Embedded btnHelp = new Embedded("", new ThemeResource("images/Help.png"));
	private Embedded btnLogin = new Embedded("", new ThemeResource("images/Login.png"));
	private Embedded btnLogout = new Embedded("", new ThemeResource("images/Logout.png"));
	private Embedded logo = new Embedded("", new ThemeResource("images/LogoEmployees.png"));
    
    private Home scrHome;
    private Help scrHelp;
    private Login scrLogin;
    
	public Header(final MainApp app) {
		System.out.println("Header created");
		
		this.app = app;
		
		panel.setContent(layout);
        panel.addStyleName("header");
		
        layout.setColumnExpandRatio(0, 3);
        layout.setColumnExpandRatio(1, 2);
        layout.setColumnExpandRatio(2, 2);

		
        /*
         * Menu unit
         */
        menuButtons.addComponent(btnHome);
        menuButtons.addComponent(btnHelp);
		layout.addComponent(menuButtons,0,0);

		/*
		 * Login unit
		 */
		btnLogin.setWidth(80, UNITS_PIXELS);
		btnLogout.setWidth(80, UNITS_PIXELS);
		loginButtons.addComponent(btnLogin,"left: 0px; top: 0px;");
		loginButtons.addComponent(btnLogout,"left: 0px; top: 0px;");
		layout.addComponent(loginButtons,1,0);

		btnLogout.setVisible(false);
		
        layout.setComponentAlignment(btnLogin, Alignment.MIDDLE_LEFT);
        layout.setComponentAlignment(btnLogout, Alignment.MIDDLE_LEFT);
        
        /*
         * Logo Unit
         */
        layout.addComponent(logo,2,0);
        layout.setComponentAlignment(logo, Alignment.TOP_RIGHT);
		
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setWidth("100%");
		
	    scrLogin = new Login(app, btnLogin, btnLogout);
	    scrHome = new Home();
	    scrHelp = new Help();

		app.getCenter().setScreen(scrHome);
		
		btnHome.addListener(new ClickListener() {
			@Override
			public void click(ClickEvent event) {
				app.getCenter().setScreen(scrHome);	
			}
		});
		
		btnHelp.addListener(new ClickListener() {
			@Override
			public void click(ClickEvent event) {
				app.addDialog(scrHelp,100,100);
			}
		});

		btnLogin.addListener(new ClickListener() {
			@Override
			public void click(ClickEvent event) {
				app.addDialog(scrLogin,0,0);
			}
		});

		btnLogout.addListener(new ClickListener() {
			@Override
			public void click(ClickEvent event) {
				LoggedOut();
			}
		});
		
		setCompositionRoot(panel);
	}
	
	public void LoggedIn() {
		app.removeDialog(scrLogin);
		btnLogout.setVisible(true);
		btnLogin.setVisible(false);
	}
	
	public void LoggedOut() {
		btnLogout.setVisible(false);
		btnLogin.setVisible(true);
	}
	
}
