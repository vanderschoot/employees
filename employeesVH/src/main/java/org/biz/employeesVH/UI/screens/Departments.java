package org.biz.employeesVH.UI.screens;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import org.biz.employeesVH.UI.Navigator;
import org.biz.employeesVH.data.DataModel;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class Departments extends CustomComponent {

	private Panel panel = new Panel();
	private VerticalLayout layout;
	private Navigator navigator;
	private final Table table = new Table();
	private SQLContainer departmentContainer;
	private SQLContainer employeeContainer;
	private Window win = new Window();
	private Form form = new Form();
	
	final Button btnTableEdit 	= new Button("Table Edit");
	final Button btnTableOK     = new Button("OK");
	final Button btnTableCancel = new Button("Cancel");
	final Button btnEdit     	= new Button("Edit");
	final Button btnAdd     	= new Button("Add");
	final Button btnDel     	= new Button("Delete");
       
	public Departments(DataModel dataModel, Navigator navigator) {
		System.out.println("Departments created");
		
		this.navigator = navigator;

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
		Label label = new Label("Departments");		
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
		table.setSizeFull();
		table.setPageLength(5);
		
		departmentContainer = dataModel.getDepartmentContainer();
		employeeContainer = dataModel.getEmployeeContainer();
		
		table.setContainerDataSource(departmentContainer);
		departmentContainer.removeAllContainerFilters();

		table.setColumnCollapsingAllowed(true);
		table.setColumnReorderingAllowed(true);

		table.setSelectable(true);
		table.setImmediate(true);
		table.setNullSelectionAllowed(false);
        table.setVisibleColumns(DataModel.DEPARTMENT_COLS);
        table.setColumnHeaders(DataModel.DEPARTMENT_LABELS);
        
        table.addGeneratedColumn("Employees", new ColumnGenerator() {
        	public Component generateCell(Table source, final Object itemId, Object columnId) {
	        	final Button btnEmployees = new Button(">");
	        	//btnEmployees.setIcon(new ThemeResource("icons/32/arrow-right.png"));
	        	btnEmployees.setDescription("show Employees for Department");
	        	btnEmployees.setData(itemId);
	        	btnEmployees.addListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						navigator.setScreen(navigator.getScreenEmployees());
						employeeContainer.removeAllContainerFilters();
						employeeContainer.addContainerFilter(new Equal("DEPARTMENTID", Integer.parseInt(btnEmployees.getData().toString())));
					}
				});
	        	return btnEmployees;
        	}
        });
       
        /* bind form to clicked table item, and set fields in the right order */
        final List<Object> orderedProperties = Arrays.asList(DataModel.DEPARTMENT_COLS);
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
		btnTableOK.setIcon(new ThemeResource("icons/16/filesave.png"));
		btnTableCancel.setIcon(new ThemeResource("icons/16/undo.png"));

        btnEdit.setIcon(new ThemeResource("icons/16/pencil.png"));
		btnAdd.setIcon(new ThemeResource("icons/16/edit_add.png"));
		btnDel.setIcon(new ThemeResource("icons/16/edit_remove.png"));

		buttons.addComponent(btnAdd);
		buttons.addComponent(btnDel);
		buttons.addComponent(btnEdit);		
		buttons.addComponent(btnTableEdit);		
		buttons.addComponent(btnTableOK);
		buttons.addComponent(btnTableCancel);
		
		if (table.getItem(table.getValue())  == null ) {
			btnEdit.setEnabled(false);
			btnDel.setEnabled(false);
		}
		
		btnTableOK.setVisible(false);
		btnTableCancel.setVisible(false);

		btnAdd.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
		        try {
		        	departmentContainer.rollback();
		        } catch (SQLException ignored) {
		        }
		        /* Create a new item and set it as the data source for this form */
		        Object tempItemId = departmentContainer.addItem();
		        Item tempItem = departmentContainer.getItem(tempItemId);
		        if (tempItem != null) {
		            List<Object> orderedProperties = Arrays.asList(DataModel.DEPARTMENT_COLS);
		            form.setItemDataSource(tempItem, orderedProperties);
		        } else {
		        	form.setItemDataSource(null);
		        }
				win.addComponent(form);
				navigator.addDialog(win, 100, 0);
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
				win.addComponent(form);
				navigator.addDialog(win, 100, 0);
			}
		});
		
		btnDel.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
		    	try {
		    		departmentContainer.removeItem(table.getValue());
		    		departmentContainer.commit();
				} catch (UnsupportedOperationException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				table.select(null); 
			}
		});
		
		btnTableEdit.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
			    table.setEditable(true);
				btnTableOK.setVisible(true);
				btnTableCancel.setVisible(true);
				btnTableEdit.setVisible(false);
				btnAdd.setVisible(false);
				btnDel.setVisible(false);
				btnEdit.setVisible(false);
			}
		});

		btnTableOK.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				table.commit();
				try {
					departmentContainer.commit();
				} catch (UnsupportedOperationException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			    table.setEditable(false);
				btnTableOK.setVisible(false);
				btnTableCancel.setVisible(false);
				btnTableEdit.setVisible(true);
				btnAdd.setVisible(true);
				btnDel.setVisible(true);
				btnEdit.setVisible(true);
			}
		});

		btnTableCancel.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				table.discard();
				try {
					departmentContainer.rollback();
				} catch (UnsupportedOperationException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			    table.setEditable(false);
				btnTableOK.setVisible(false);
				btnTableCancel.setVisible(false);
				btnTableEdit.setVisible(true);
				btnAdd.setVisible(true);
				btnDel.setVisible(true);
				btnEdit.setVisible(true);
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
		for (final Object pn : DataModel.DEPARTMENT_COLS) {
			final TextField sf = new TextField();
			searchfields.addComponent(sf);
			sf.setWidth("100%"); 
			sf.setInputPrompt(DataModel.DEPARTMENT_LABELS[i]); 
			sf.setImmediate(true); 
			searchfields.setExpandRatio(sf, 1);
			sf.addListener(new Property.ValueChangeListener() {
				public void valueChange(ValueChangeEvent event) {
					departmentContainer.removeContainerFilters(pn);
					if (sf.toString().length() > 0 && !pn.equals(sf.toString())) {
						departmentContainer.addContainerFilter(pn, sf.toString(), true, false); 
					} 
					navigator.showNotification( "" + departmentContainer.size() + " matches found");
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
        /*
         * Enable buffering so that commit() must be called for the form before
         * input is written to the data source. (Form input is not written
         * immediately through to the underlying object.)
         */
        form.setWriteThrough(false);
        
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
        win.setHeight(250, Sizeable.UNITS_PIXELS);

        /* Field factory for overriding how the fields are created. */
        form.setFormFieldFactory(new DefaultFieldFactory() {
            @Override
            public Field createField(Item item, Object propertyId,Component uiContext) {
                Field field;

                field = super.createField(item, propertyId, uiContext);
                                
                /* Set null representation of all text fields to empty */
                if (field instanceof TextField) {
                    ((TextField) field).setNullRepresentation("");
                }

                field.setWidth("200px");

                /* Set the correct caption to each field */
                for (int i = 0; i < DataModel.DEPARTMENT_COLS.length; i++) {
                    if (DataModel.DEPARTMENT_COLS[i].equals(propertyId)) {
                        field.setCaption(DataModel.DEPARTMENT_LABELS[i]);
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
						departmentContainer.commit();
					} catch (UnsupportedOperationException e) {
						e.printStackTrace();
					} catch (SQLException e) {
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
               	try {
					departmentContainer.rollback();
				} catch (UnsupportedOperationException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				navigator.removeDialog(win);
			}
		});
    }
}
