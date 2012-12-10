package org.biz.employeesVR.UI;

import org.biz.employeesVR.MainApp;
import org.biz.employeesVR.UI.screens.Help;
import org.biz.employeesVR.UI.screens.Home;
import org.biz.employeesVR.UI.screens.Login;
import org.biz.employeesVR.UI.screens.Register;
import org.biz.employeesVR.data.ReturnStatus;
import org.biz.employeesVR.UI.screens.Connect;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

@SuppressWarnings("serial")
public class Header extends CustomComponent {

	Panel panel = new Panel();
	private GridLayout layout = new GridLayout(3,2);
	private HorizontalLayout menuButtons = new HorizontalLayout();
	private AbsoluteLayout loginButtons = new AbsoluteLayout();
	private AbsoluteLayout loginLabels = new AbsoluteLayout();
	
	private MainApp app;

	private Embedded btnHome = new Embedded("", new ThemeResource("images/Home.png"));
	private Embedded btnHelp = new Embedded("", new ThemeResource("images/Help.png"));
	private Embedded btnConnect = new Embedded("", new ThemeResource("images/Connect.png"));
	private Embedded btnLogin = new Embedded("", new ThemeResource("images/Login.png"));
	private Embedded btnLogout = new Embedded("", new ThemeResource("images/Logout.png"));
	private Embedded logo = new Embedded("", new ThemeResource("images/LogoEmployees.png"));
	
	private Label lblUser = new Label();
	private Label lblRegister = new Label();
    
    private Home scrHome;
    private Help scrHelp;
    private Login scrLogin;
    private Register scrRegister;
    private Connect scrConnect;
    
	public Header(final MainApp app) {
		System.out.println("Header created");
		
		this.app = app;
		
		panel.setContent(layout);
        panel.addStyleName("header");
		
        layout.setColumnExpandRatio(0, 1);
        layout.setColumnExpandRatio(1, 4);
        layout.setColumnExpandRatio(2, 1);
		
        /*
         * Menu unit
         */
        menuButtons.addComponent(btnHome);
        menuButtons.addComponent(btnConnect);
        menuButtons.addComponent(btnHelp);
		layout.addComponent(menuButtons,0,1,2,1);

		/*
		 * Login unit
		 */
		btnLogin.setWidth(80, UNITS_PIXELS);
		btnLogout.setWidth(80, UNITS_PIXELS);
		loginButtons.addComponent(btnLogin,"right: 0px; top: 0px;");
		loginButtons.addComponent(btnLogout,"right: 0px; top: 0px;");
		layout.addComponent(loginButtons,2,0);
        layout.setComponentAlignment(btnLogin, Alignment.TOP_RIGHT);
        layout.setComponentAlignment(btnLogout, Alignment.TOP_RIGHT);

		lblUser.setSizeUndefined();
		lblRegister.setSizeUndefined();
		lblUser.addStyleName("whiteXlarge");
		lblRegister.setValue("Register");
		lblRegister.addStyleName("whitemedium");
		loginLabels.addComponent(lblUser,"right: 0px; top: 0px;");
		loginLabels.addComponent(lblRegister,"right: 0px; top: 0px;");
		layout.addComponent(loginLabels,1,0);
        layout.setComponentAlignment(lblUser, Alignment.TOP_RIGHT);
        layout.setComponentAlignment(lblRegister, Alignment.TOP_RIGHT);
		
        btnLogin.setVisible(false);
        lblRegister.setVisible(false);
        btnLogout.setVisible(false);
		lblUser.setVisible(false);
        
        /*
         * Logo Unit
         */
        layout.addComponent(logo,0,0);
        layout.setComponentAlignment(logo, Alignment.TOP_LEFT);
 		
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setWidth("100%");
		
	    scrLogin = new Login(app, lblUser);
	    scrRegister = new Register(app, lblRegister);
	    scrHome = new Home();
	    scrConnect = new Connect(app);
	    scrHelp = new Help();

		app.getCenter().setScreen(scrHome);		
		
		btnHome.addListener(new ClickListener() {
			@Override
			public void click(ClickEvent event) {
				app.getCenter().setScreen(scrHome);	
			}
		});
		
		btnConnect.addListener(new ClickListener() {
			@Override
			public void click(ClickEvent event) {
				app.addDialog(scrConnect,100,100);
			}
		});
		
		btnHelp.addListener(new ClickListener() {
			@Override
			public void click(ClickEvent event) {
				app.addDialog(scrHelp,100,100);
			}
		});
		
		loginLabels.addListener(new LayoutClickListener() {
            public void layoutClick(LayoutClickEvent event) {
            	Component comp = event.getClickedComponent();
                if (comp != null) {
                	if (comp.getClass().getSimpleName().equalsIgnoreCase("Label")) {
                		Label label = (Label)comp;
                		if (label.getValue().toString().equalsIgnoreCase("Register")) {
                            getWindow().showNotification("Clicked Label " + label.getValue());                			
            				app.addDialog(scrRegister,0,0);
                		}
                	}
                }            
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
				ReturnStatus sts = app.getDataModel().getRestServiceFactory().logout();
				if (sts != null) {
					if (sts.isSucces()) {
						app.showNotification("Logged Out!");						
						LoggedOut();
					} else {						
						app.showNotification("Logout not succesfull, " + sts.getMessage());
					}
				} else {
					app.showNotification("Logout not succesfull?  (no status feedback)");
				}
			}
		});
		
		setCompositionRoot(panel);
	}
	
	public void Connected() {
		btnLogin.setVisible(true);	
        lblRegister.setVisible(true);
	}
	
	public void LoggedIn() {
		app.removeDialog(scrLogin);
		btnLogout.setVisible(true);
		btnLogin.setVisible(false);
		lblUser.setVisible(true);
		lblRegister.setVisible(false);
		app.getNavigator().setAccessRights();
	}
	
	public void LoggedOut() {
		app.getLoggedInUser().clear();
		app.getCenter().setScreen(scrHome);
		app.getNavigator().setAccessRights();
		btnLogout.setVisible(false);
		btnLogin.setVisible(true);
		lblUser.setValue("");
		lblUser.setVisible(false);
		lblRegister.setVisible(true);
	}

	public Connect getConnection() {
		return scrConnect;
	}

}
