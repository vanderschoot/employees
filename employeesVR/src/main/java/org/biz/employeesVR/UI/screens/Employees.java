package org.biz.employeesVR.UI.screens;

import java.util.Arrays;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.biz.employeesVR.UI.Navigator;
import org.biz.employeesVR.data.DataModel;
import org.biz.employeesVR.data.ReturnStatus;
import org.biz.employeesVR.data.DTO.Department;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class Employees extends CustomComponent {

	private Panel panel = new Panel();
	private VerticalLayout layout;
	private Navigator navigator;
	private DataModel dataModel;

	private Table table = null;
	private IndexedContainer employeeContainer;
	private IndexedContainer departmentContainer;
	private Window win = new Window("Employee");
	private Form form = new Form();
	
	final Button btnEdit     = new Button("Edit");
	final Button btnAdd     = new Button("Add");
	final Button btnDel     = new Button("Delete");
	
	private String tableAction = "";
	private Object tempItemId = null;
       
	public Employees(DataModel dataModel, Navigator navigator) {
		System.out.println("Employees created");
		
		this.navigator = navigator;
		this.dataModel = dataModel;

		layout = new VerticalLayout();
		layout.setHeight(100, Sizeable.UNITS_PERCENTAGE);
		panel.setContent(layout);
        panel.addStyleName("screen");
		layout.setMargin(true);

		createScreenHeader();
		createTable(dataModel, navigator);
		createTableControls();
		createForm();

		setCompositionRoot(panel);
	}
	
	private void createScreenHeader() {
		HorizontalLayout screenTitle = new HorizontalLayout();
		screenTitle.setSizeFull();
		Label label = new Label("Employees");		
		label.setSizeUndefined();
		label.addStyleName("screenheader");
		Component filter = addSearchFilter();
		screenTitle.addComponent(label);
		screenTitle.addComponent(filter);
		screenTitle.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
		screenTitle.setComponentAlignment(filter, Alignment.MIDDLE_RIGHT);
		screenTitle.setMargin(true);
		layout.addComponent(screenTitle);
	}
	
	private void createTable(DataModel dataModel, final Navigator navigator) {	
		
		table = new Table() {
			@Override
			protected String formatPropertyValue(Object itemId,Object propId,Property property) {
				Object value = property.getValue();
				if (value instanceof Date) {
					Date date = (Date) value;
					return new SimpleDateFormat("dd-MM-yyyy").format(date);
				}
				return super.formatPropertyValue(itemId, propId, property);
			}
		};
		
		table.setSizeFull();
		table.setPageLength(10);
		
		employeeContainer = dataModel.getEmployeeContainer();
		departmentContainer = dataModel.getDepartmentContainer();
		
		table.setContainerDataSource(employeeContainer);
		employeeContainer.removeAllContainerFilters();

		table.setColumnCollapsingAllowed(true);
		table.setColumnReorderingAllowed(true);

		table.setSelectable(true);
		table.setImmediate(true);
		table.setNullSelectionAllowed(false);
        table.setVisibleColumns(DataModel.EMPLOYEE_COLS);
        table.setColumnHeaders(DataModel.EMPLOYEE_LABELS);        
        
        table.addGeneratedColumn("DepartmentId", new ColumnGenerator() {
        	public Component generateCell(Table source, Object itemId, Object columnId) {
	        	Label label = new Label();
	        	if (itemId.toString().equals("Temporary row id")) {
	        		// I.g.v toevoegen nieuw record: Nodig om exception te voorkomen als foreign key nog niet bestaat
	        		// in dat geval is itemId instantie van class com.vaadin.data.util.sqlcontainer.TemporaryRowId
	        		// normaal is itemId instantie van class com.vaadin.data.util.sqlcontainer.RowId
	        	} else {
		        	Item employee = employeeContainer.getItem(itemId);
		        	Object DepartmentId = employee.getItemProperty("DepartmentId").getValue();
		        	Collection<?> coll = departmentContainer.getItemIds();
		        	Iterator<?> it = coll.iterator();
		        	while(it.hasNext()){
		        		Object ItemId = it.next();
		        		Item departm = departmentContainer.getItem(ItemId);
		        		Object obj = departm.getItemProperty("ID").getValue();
		        		if (departm.getItemProperty("ID").getValue().equals(DepartmentId)) {
		        			label.setValue(departm.getItemProperty("name"));
		        			System.out.println("Set label, emp" + employee.getItemProperty("firstName")+ " / label = " + label.getValue().toString());
		        		}
		        	}		        	
	        	}
	        	return label;
        	}
       });
        
        /* bind form to clicked table item, and set fields in the right order */
        final List<Object> orderedProperties = Arrays.asList(DataModel.EMPLOYEE_COLS);
        table.addListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
		        Item item = table.getItem(table.getValue());
		        if (item != form.getItemDataSource()) {
		        	form.setItemDataSource(item, orderedProperties);
		        }
				btnEdit.setEnabled(true);
				btnDel.setEnabled(true);
			}
        });
                
		layout.addComponent(table);
	}
	
	private void createTableControls() {
		HorizontalLayout buttons = new HorizontalLayout();		

        btnEdit.setIcon(new ThemeResource("icons/16/pencil.png"));
		btnAdd.setIcon(new ThemeResource("icons/16/edit_add.png"));
		btnDel.setIcon(new ThemeResource("icons/16/edit_remove.png"));

		buttons.addComponent(btnAdd);
		buttons.addComponent(btnDel);
		buttons.addComponent(btnEdit);		
		
		if (table.getItem(table.getValue())  == null ) {
			btnEdit.setEnabled(false);
			btnDel.setEnabled(false);
		}
		
		btnAdd.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
		        /* Create a new item and set employeeContainter as the data source for this form */
		        tempItemId = employeeContainer.addItem();
		        Item tempItem = employeeContainer.getItem(tempItemId);
		        if (tempItem != null) {
		            List<Object> orderedProperties = Arrays.asList(DataModel.EMPLOYEE_COLS);
		            form.setItemDataSource(tempItem, orderedProperties);
		            tableAction = "add";
		        } else {
		        	form.setItemDataSource(null);
		            tableAction = "";
		        }
				win.addComponent(form);
				navigator.addDialog(win,200,0);
			}
		});
				
		btnEdit.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
		        /* bind form to data source of selected table item */
		        Item item = table.getItem(table.getValue());
		        if (item != form.getItemDataSource()) {
		        	form.setItemDataSource(item);
		        }
	            tableAction = "edit";
				win.addComponent(form);
				navigator.addDialog(win,200,0);
			}
		});
				
		btnDel.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
		    	try {
		    		Object itemId = table.getValue();
		    		ReturnStatus sts = dataModel.deleteEmployee(itemId);
		    		if (sts.isSucces()) {
		    			if (employeeContainer.removeItem(itemId)) {
		    				navigator.showNotification("Employee has been removed");
		    			} else {		    				
		    				navigator.showNotification("Employee could NOT be removed (from table)");
		    			}
		    		} else {
	    				navigator.showNotification("Employee could NOT be removed (from database), " + sts.getMessage());		    			
		    		}
				} catch (UnsupportedOperationException e) {
					e.printStackTrace();
				}
				table.select(null); 
			}
		});

		layout.addComponent(buttons);
	}
	
	private Component addSearchFilter() {
		HorizontalLayout searchbar = new HorizontalLayout(); 
		final HorizontalLayout searchfields = new HorizontalLayout(); 
		Button btnSearch = new Button("Search");
		btnSearch.setIcon(new ThemeResource("icons/16/search.png"));
		searchbar.addComponent(btnSearch);
		searchbar.addComponent(searchfields);
		searchfields.setVisible(false);
		int i = 0;
		for (final Object pn : DataModel.EMPLOYEE_COLS) {
			final TextField sf = new TextField();
			searchfields.addComponent(sf);
			sf.setWidth("100%"); 
			sf.setInputPrompt(DataModel.EMPLOYEE_LABELS[i]); 
			sf.setImmediate(true); 
			searchfields.setExpandRatio(sf, 1);
			sf.addListener(new Property.ValueChangeListener() {
				public void valueChange(ValueChangeEvent event) {
					employeeContainer.removeContainerFilters(pn);
					if (sf.toString().length() > 0 && !pn.equals(sf.toString())) {
						employeeContainer.addContainerFilter(pn, sf.toString(), true, false); 
					} 
					navigator.showNotification( "" + employeeContainer.size() + " matches found");
				} 
			}); 
			i++;
		} 
		
		btnSearch.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (searchfields.isVisible()) 
					searchfields.setVisible(false);
				else
					searchfields.setVisible(true);
			}
		});
		
		return searchbar;
	} 

	private void createForm() {
        /* Init form footer with buttons */
		Button btnOK     = new Button("OK");
		Button btnCancel = new Button("Cancel");
		
        btnOK.setIcon(new ThemeResource("icons/16/ok.png"));
        btnCancel.setIcon(new ThemeResource("icons/16/cancel.png"));

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.addComponent(btnOK);
        buttons.addComponent(btnCancel);
        form.setFooter(buttons);
        
        form.setReadOnly(false);
        win.setWidth(400, Sizeable.UNITS_PIXELS);
        win.setHeight(350, Sizeable.UNITS_PIXELS);

        /* Field factory for overriding how the fields are created. */
        form.setFormFieldFactory(new DefaultFieldFactory() {
            @Override
            public Field createField(Item item, Object propertyId,Component uiContext) {
                Field field = super.createField(item, propertyId, uiContext);

                if (propertyId.equals("DepartmentId")) {
                	Select select = new Select("Department");
                	for (int i=0; i < departmentContainer.size();i++) {
                		Item depitm = departmentContainer.getItem(departmentContainer.getIdByIndex(i));
                		Integer depid = (Integer) depitm.getItemProperty("ID").getValue();
                		String depname = depitm.getItemProperty("name").getValue().toString();
                		select.addItem(depid);
                		select.setItemCaption(depid, depname);
                	}
                	select.setItemCaptionMode(Select.ITEM_CAPTION_MODE_EXPLICIT);   
                	select.setNullSelectionAllowed(false);
                 	return select;
                } else if (propertyId.equals("birthDate")) {
                	PopupDateField date = new PopupDateField("BirthDate") {
                		@Override
                		protected java.util.Date handleUnparsableDateString(String dateString) throws Property.ConversionException {
	                		// Try custom parsing
	                		String fields[] = dateString.split("-");
	                		if (fields.length >= 3) {
		                		try {
			                		int year = Integer.parseInt(fields[0]);
			                		int month = Integer.parseInt(fields[1])-1;
			                		int day = Integer.parseInt(fields[2]);
			                		GregorianCalendar c =new GregorianCalendar(year, month, day);
			                		return c.getTime();
		                		} catch (NumberFormatException e) {
			                		throw new Property.
			                		ConversionException("Not a number");
		                		}
	                		}
	                		// Bad date
	                		throw new Property.
	                		ConversionException("Your date needs two '-' separators");
                		}
                	};
                    //date.setCaption("BirthDate");
                    date.setDateFormat("dd-MM-yyyy");
                 	return date;
                } else {
                	field = super.createField(item, propertyId, uiContext);                	
                    /* Set null representation of all text fields to empty */
                    if (field instanceof TextField) {
                        ((TextField) field).setNullRepresentation("");
                    }
                }

                field.setWidth("200px");

                /* Set the correct caption to each field */
                for (int i = 0; i < DataModel.EMPLOYEE_COLS.length; i++) {
                    if (DataModel.EMPLOYEE_COLS[i].equals(propertyId)) {
                        field.setCaption(DataModel.EMPLOYEE_LABELS[i]);
                    }
                }
                return field;
            }
        });
        
        btnOK.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
                if (form.isValid()) {
                	form.commit();
                	try {
            			DateField fld = (DateField) form.getField("birthDate");
            			Date utilDate = (java.util.Date) fld.getValue();
            			
                		if (tableAction.equals("add")) {
                			ReturnStatus sts = dataModel.addEmployee(  
                									form.getField("firstName").getValue().toString(), 
                									form.getField("lastName").getValue().toString(),
                									utilDate,
                									form.getField("DepartmentId").getValue().toString()
                									);
                			if (sts.getMessage() != null) {
                				Item item = employeeContainer.getItem(tempItemId);
                				item.getItemProperty("ID").setValue(sts.getMessage());
                			}
                		}
                		if (tableAction.equals("edit")) {
                			dataModel.updateEmployee(	table.getValue(),
                										form.getField("firstName").getValue().toString(), 
                										form.getField("lastName").getValue().toString(),
                										utilDate,
                										form.getField("DepartmentId").getValue().toString()
                										);
                		}
                		table.setContainerDataSource(employeeContainer);
    		            tableAction = "";
					} catch (UnsupportedOperationException e) {
						e.printStackTrace();
					}
                }
				navigator.removeDialog(win);
			}
		});
        
        btnCancel.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				form.discard();
				if (tableAction.equals("add")) {
	               	try {
	               		employeeContainer.removeItem(tempItemId);
					} catch (UnsupportedOperationException e) {
						e.printStackTrace();
					}
				}
				navigator.removeDialog(win);
			}
		});
                
    }
}
