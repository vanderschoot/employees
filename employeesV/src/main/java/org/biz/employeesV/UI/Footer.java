package org.biz.employeesV.UI;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

@SuppressWarnings("serial")
public class Footer extends CustomComponent {

	Panel panel = new Panel();
	private HorizontalLayout layout;
       
	public Footer() {
		System.out.println("Footer created");
		layout = new HorizontalLayout();		
		panel.setContent(layout);
        panel.addStyleName("footer");

		Label label = new Label("Employees & Departments ©");
		label.setWidth(300, UNITS_PIXELS);
		layout.addComponent(label);
		layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
		layout.setExpandRatio(label, 1);
		
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setWidth("100%");
		
		setCompositionRoot(panel);
	}
}
