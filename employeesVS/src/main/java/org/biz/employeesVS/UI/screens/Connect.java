package org.biz.employeesVS.UI.screens;

import org.biz.employeesVS.MainApp;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class Connect extends Window {

    private Form form = new Form();
    private MainApp app;
	private PropertysetItem serverConfig;
	private ProgressIndicator pi;
	private DataLoader dataLoader;
	private Button btnOK     = new Button("OK");
	private Button btnCancel = new Button("Cancel");
	   

    public Connect(final MainApp app) {
		System.out.println("Connect created");
		this.app = app;
        setCaption("Connect");
        addComponent(form);
        setWidth(300, Sizeable.UNITS_PIXELS);
        setHeight(350, Sizeable.UNITS_PIXELS);
        
		
        btnOK.setIcon(new ThemeResource("icons/16/ok.png"));
        btnCancel.setIcon(new ThemeResource("icons/16/cancel.png"));
        pi = new ProgressIndicator();
        pi.setIndeterminate(true);
        pi.setPollingInterval(500);
        pi.setEnabled(false);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.addComponent(btnOK);
        buttons.addComponent(btnCancel);
        buttons.addComponent(pi);

        form.setFooter(buttons);
        
        form.setReadOnly(false);
        
        serverConfig = new PropertysetItem();
        serverConfig.addItemProperty("serverhost", new ObjectProperty<Object>(""));
        serverConfig.addItemProperty("serverport", new ObjectProperty<Object>(""));
        serverConfig.addItemProperty("serverproject", new ObjectProperty<Object>(""));
        
        serverConfig.getItemProperty("serverhost").setValue("localhost");
        serverConfig.getItemProperty("serverport").setValue("8080");
        serverConfig.getItemProperty("serverproject").setValue("employeesSOAPH");

        form.setItemDataSource(serverConfig);
                
        btnOK.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
                if (form.isValid()) {	
                    // Add an indeterminate progress indicator
                	pi.setEnabled(true);
                    pi.setVisible(true);
                    btnOK.setEnabled(false);
                    dataLoader = new DataLoader();
                    dataLoader.start();
               }
			}
		});
       
        btnCancel.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				form.discard();
                app.removeDialog(getWindow());
			}
		});
    }        

    public void Connected(boolean ok) {
    	pi.setEnabled(false);
    	btnOK.setEnabled(true);
    	
        btnOK.setEnabled(true);
    	pi.setEnabled(false);
        pi.setVisible(false);
        if (ok) {
        	app.getHeader().Connected();
        	app.removeDialog(getWindow());
        }
    }

    public class DataLoader extends Thread {
        @Override
        public void run() {
        	synchronized (getApplication()) {
            	boolean ok = app.getDataModel().getData();
            	Connected(ok);
            }
        }
    }

    public String getServerHost() {
    	return serverConfig.getItemProperty("serverhost").getValue().toString();
    }
    
    public String getServerPort() {
    	return serverConfig.getItemProperty("serverport").getValue().toString();
    }

    public String getServerProject() {
    	return serverConfig.getItemProperty("serverproject").getValue().toString();
    }
    
}