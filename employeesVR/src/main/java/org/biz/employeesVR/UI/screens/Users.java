package org.biz.employeesVR.UI.screens;

import java.util.Arrays;
import java.util.List;

import org.biz.employeesVR.UI.Navigator;
import org.biz.employeesVR.data.DataModel;
import org.biz.employeesVR.data.ReturnStatus;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
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
public class Users extends CustomComponent {

	private Panel panel = new Panel();

	private Navigator navigator;
	private DataModel dataModel;
	private final Table userTable = new Table();
	private final Table roleTable = new Table();
	private IndexedContainer userContainer;
	private IndexedContainer roles4User;
	private IndexedContainer userroleContainer;
	private IndexedContainer roleContainer;
	private PropertysetItem itemUserRole;
	private Window winUserForm = new Window("User");
	private Window winRoleForm = new Window("Role");
	private Window winRolesTable = new Window("Roles");
	private Form userForm = new Form();
	private Form roleForm = new Form();
	
	final Button btnUserEdit     	= new Button("Edit");
	final Button btnUserAdd     	= new Button("Add");
	final Button btnUserDel     	= new Button("Delete");

	final Button btnRoles     		= new Button("Roles");
	final Button btnRoleAdd     	= new Button("Add");
	final Button btnRoleDel     	= new Button("Delete");
	
	private String tableAction = "";
	private Object tempItemId = null;
	
	private String userId = ""; 	//contains clicked userId in userTable
	private String userName = "";	//contains clicked userName in userTable
	private String roleId = ""; 	//contains clicked roleId in roleTable
	private String userRoleId = ""; //contains clicked userRoleId in roleTable
	
	private boolean deleted = false;

	public Users(DataModel dataModel, Navigator navigator) {
		System.out.println("Users created");
		this.navigator = navigator;
		this.dataModel = dataModel;
		
		HorizontalLayout layout = new HorizontalLayout();		
		panel.setContent(layout);
        panel.addStyleName("screen");

		layout.setHeight(100, Sizeable.UNITS_PERCENTAGE);
		layout.setWidth(60, Sizeable.UNITS_PERCENTAGE);
		layout.setMargin(true);
		
		Component usersTable = createUsersTable();
		layout.addComponent(usersTable);
		
		//layout.addComponent(rolesTable);
		
		Component rolesTable = createRolesTable();
		winRolesTable.addComponent(rolesTable);
		winRolesTable.setWidth(300, UNITS_PIXELS);
		winRolesTable.setHeight(400, UNITS_PIXELS);

		layout.setComponentAlignment(usersTable, Alignment.TOP_LEFT);
		layout.setComponentAlignment(usersTable, Alignment.TOP_RIGHT);
		
		setCompositionRoot(panel);
	}
	
	private Component createUsersTable() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);

		createScreenHeader(layout, "Users", true);
		createUserTable(layout);
		createUserForm();
		return layout;
	}
	
	private Component createRolesTable() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		createScreenHeader(layout, "Roles", false);
		createRoleTable(layout);
		createRoleForm();	
		return layout;
	}
	
	private void createScreenHeader(VerticalLayout layout, String title, boolean addSearchFilter) {
		HorizontalLayout screenTitle = new HorizontalLayout();
		screenTitle.setSizeFull();
		Label label = new Label(title);		
		label.setSizeUndefined();
		label.addStyleName("screenheader");
		screenTitle.addComponent(label);
		screenTitle.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
		if (addSearchFilter) {
			Component filter = addSearchFilter();
			screenTitle.addComponent(filter);
			screenTitle.setComponentAlignment(filter, Alignment.MIDDLE_RIGHT);
		}
		screenTitle.setMargin(true);
		layout.addComponent(screenTitle);
	}
	
	private void createUserTable(VerticalLayout layout) {	
		userTable.setPageLength(7);
		
		userContainer = dataModel.getUserContainer();
		
		userTable.setContainerDataSource(userContainer);
		userContainer.removeAllContainerFilters();

		userTable.setColumnCollapsingAllowed(true);
		userTable.setColumnReorderingAllowed(true);

		userTable.setSelectable(true);
		userTable.setImmediate(true);
		userTable.setNullSelectionAllowed(false);
		userTable.setVisibleColumns(DataModel.USER_COLS);
		userTable.setColumnHeaders(DataModel.USER_LABELS);
       
        /* bind form to clicked table item, and set fields in the right order */
        userTable.addListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
		        List<Object> orderedProperties = Arrays.asList(DataModel.USER_COLS);
		        Item item = userTable.getItem(userTable.getValue());
				System.out.println("userTable ChangeEvent, Item = " + item);	
		        if ((item != userForm.getItemDataSource()) && (item != null)){
		        	userForm.setItemDataSource(item, orderedProperties);
		        	userId = item.getItemProperty("ID").getValue().toString();
		        	userName = item.getItemProperty("userName").getValue().toString();
					System.out.println("userTable ChangeEvent, userId = " + userId + " / userName = " + userName);		 
					winRolesTable.setCaption("Roles for user " + userName);
		    		roles4User = dataModel.getRoles4User(userId);
		    		roleTable.setContainerDataSource(roles4User);
		        }
				btnUserEdit.setEnabled(true);
				btnUserDel.setEnabled(true);
				btnRoles.setEnabled(true);
			}
        });
		layout.addComponent(userTable);
		createUserTableControls(layout);        
	}
	
	private void createUserTableControls(VerticalLayout layout) {
		HorizontalLayout buttons = new HorizontalLayout();		

        btnUserEdit.setIcon(new ThemeResource("icons/16/pencil.png"));
		btnUserAdd.setIcon(new ThemeResource("icons/16/edit_add.png"));
		btnUserDel.setIcon(new ThemeResource("icons/16/edit_remove.png"));

		buttons.addComponent(btnUserAdd);
		buttons.addComponent(btnUserDel);
		buttons.addComponent(btnUserEdit);		
		buttons.addComponent(btnRoles);		
		
		if (userTable.getItem(userTable.getValue())  == null ) {
			btnUserEdit.setEnabled(false);
			btnUserDel.setEnabled(false);
			btnRoles.setEnabled(false);
		}
		
		btnUserAdd.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
		        /* Create a new item and set it as the data source for this form */
		        tempItemId = userContainer.addItem();
		        Item tempItem = userContainer.getItem(tempItemId);
		        if (tempItem != null) {
		            List<Object> orderedProperties = Arrays.asList(DataModel.USER_COLS);
		            userForm.setItemDataSource(tempItem, orderedProperties);
		            tableAction = "add";
		        } else {
		        	userForm.setItemDataSource(null);
		            tableAction = "";
		        }
				winUserForm.addComponent(userForm);
				navigator.addDialog(winUserForm, 100, 0);
			}
		});
		
		btnUserEdit.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
		        /* bind form to data source of selected table item */
		        Item item = userTable.getItem(userTable.getValue());
		        if (item != userForm.getItemDataSource()) {
		        	userForm.setItemDataSource(item);
		        }
	            tableAction = "edit";
				winUserForm.addComponent(userForm);
				navigator.addDialog(winUserForm, 100, 0);
			}
		});
				
		btnUserDel.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
		    	try {
		    		Object itemId = userTable.getValue();
		    		ReturnStatus sts = dataModel.deleteUser(itemId);
		    		if (sts.isSucces()) {
		    			if (userContainer.removeItem(itemId)) {
		    				navigator.showNotification("User has been removed");
		    			} else {		    				
		    				navigator.showNotification("User could NOT be removed (from table)");
		    			}
		    		} else {
	    				navigator.showNotification("User could NOT be removed (from database), " + sts.getMessage());		    			
		    		}
				} catch (UnsupportedOperationException e) {
					e.printStackTrace();
				}
		    	userTable.select(null); 
			}
		});
		
		btnRoles.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				navigator.addDialog(winRolesTable, 700, 200);
			}
		});

		layout.addComponent(buttons);
	}
	
	private Component addSearchFilter() {
		HorizontalLayout searchbar = new HorizontalLayout(); 
		final VerticalLayout searchfields = new VerticalLayout(); 
		Button btnSearch = new Button("Search");
		btnSearch.setIcon(new ThemeResource("icons/16/search.png"));
		searchbar.addComponent(btnSearch);
		searchbar.addComponent(searchfields);
		searchfields.setVisible(false);
		int i = 0;
		for (final Object pn : DataModel.USER_COLS) {
			final TextField sf = new TextField();
			searchfields.addComponent(sf);
			sf.setWidth("100%"); 
			sf.setInputPrompt(DataModel.USER_LABELS[i]); 
			sf.setImmediate(true); 
			searchfields.setExpandRatio(sf, 1);
			sf.addListener(new Property.ValueChangeListener() {
				public void valueChange(ValueChangeEvent event) {
					userContainer.removeContainerFilters(pn);
					if (sf.toString().length() > 0 && !pn.equals(sf.toString())) {
						userContainer.addContainerFilter(pn, sf.toString(), true, false); 
					} 
					navigator.showNotification( "" + userContainer.size() + " matches found");
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

	private void createRoleTable(VerticalLayout layout) {	
		roleTable.setPageLength(7);
		
		roles4User = dataModel.getRoles4User("");		
		roleTable.setContainerDataSource(roles4User);

		roleTable.setColumnCollapsingAllowed(false);
		roleTable.setColumnReorderingAllowed(true);

		roleTable.setSelectable(true);
		roleTable.setImmediate(true);
		roleTable.setNullSelectionAllowed(false);
		roleTable.setVisibleColumns(DataModel.ROLES4USER_COLS);
		roleTable.setColumnHeaders(DataModel.ROLES4USER_LABELS);
       
        roleTable.addListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (!deleted) { // dont do this code if the change is caused by delete action
			        if (roleTable.getValue() != null) { // change is from roletable click.
				        List<Object> orderedProperties = Arrays.asList(DataModel.USERROLE_COLS);
				        Item item = roleTable.getItem(roleTable.getValue());
			        	userRoleId = item.getItemProperty("ID").getValue().toString();
			        	roleId = item.getItemProperty("RoleId").getValue().toString();
				        itemUserRole = new PropertysetItem();
				        itemUserRole.addItemProperty("RoleId", new ObjectProperty<Object>(roleId));
				        itemUserRole.addItemProperty("UserId", new ObjectProperty<Object>(userId));
						System.out.println("roleTable ChangeEvent, Item = " + item + " / userId = " + userId +  " / roleId = " + roleId +  " /  userRoleId = " + userRoleId);	
				        if (itemUserRole != roleForm.getItemDataSource()) {
				        	System.out.println("roleTable ChangeEvent, set roleForm");
				        	roleForm.setItemDataSource(itemUserRole, orderedProperties);
				        }
						btnRoleDel.setEnabled(true);
			        }
				} else { // change was from role delete action
					deleted = false;
				}
			}
        });
		layout.addComponent(roleTable);
		createRoleTableControls(layout);        
	}
	
	private void createRoleTableControls(VerticalLayout layout) {
		HorizontalLayout buttons = new HorizontalLayout();		
		
		userroleContainer = dataModel.getUserRoleContainer();

		btnRoleAdd.setIcon(new ThemeResource("icons/16/edit_add.png"));
		btnRoleDel.setIcon(new ThemeResource("icons/16/edit_remove.png"));

		buttons.addComponent(btnRoleAdd);
		buttons.addComponent(btnRoleDel);
		
		if (roleTable.getItem(roleTable.getValue())  == null ) {
			btnRoleDel.setEnabled(false);
		}
		
		btnRoleAdd.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
		        /* Create a new item and set it as the data source for this form */
		        tempItemId = userroleContainer.addItem();
		        Item tempItem = userroleContainer.getItem(tempItemId);
		        if (tempItem != null) {
			        tempItem.getItemProperty("UserId").setValue(userId);
			        System.out.println("roleTable, btnRoleAdd, created tempitem, tempItemId = " + tempItemId);
		            List<Object> orderedProperties = Arrays.asList(DataModel.USERROLE_COLS);
		            roleForm.setItemDataSource(tempItem, orderedProperties);
		            winRoleForm.setCaption("Role for user : " + userName);
		            tableAction = "add";
		        } else {
		        	roleForm.setItemDataSource(null);
		            tableAction = "";
			        System.out.println("roleTable, btnRoleAdd, tempItem not created ????");
		        }
				winRoleForm.addComponent(roleForm);
				navigator.addDialog(winRoleForm, 100, 0);
			}
		});
				
		btnRoleDel.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
		    	try {
		    		String UserId = roleForm.getItemDataSource().getItemProperty("UserId").toString();
		    		String RoleId = roleForm.getItemDataSource().getItemProperty("RoleId").toString();
		    		IndexedContainer userrole = dataModel.getUserRoleContainer();

		    		System.out.println("btnRoleDel, userId = " + userId + " / roleId = " + RoleId);
					System.out.println("btnRoleDel, Item Ids = " + userrole.getItemIds());
					
	    			Object itemId = dataModel.getUserRole(UserId, RoleId);
	    				    			
		    		ReturnStatus sts = dataModel.deleteUserRole(itemId);
		    		if (sts.isSucces()) {
		    			if (userrole.removeItem(itemId)) {
		            		// Refresh roleTable.
				    		roles4User  = dataModel.getRoles4User(UserId);		
				    		roleTable.setContainerDataSource(roles4User);
			    			roleTable.select(roles4User.firstItemId()); 
		    				navigator.showNotification("Role has been removed from user");
		    			} else {		    				
		    				navigator.showNotification("Role could NOT be removed (from table)");
		    			}
		    		} else {
	    				navigator.showNotification("Role could NOT be removed (from database), " + sts.getMessage());		    			
		    		}
				} catch (UnsupportedOperationException e) {
					e.printStackTrace();
				}
			}
		});
			
		layout.addComponent(buttons);
	}	
	
	private void createUserForm() {
        /* No immediate updates of containter. Updates to container need commit of the form.*/
		userForm.setWriteThrough(false);
        
        /* Init form footer with buttons */
		Button btnOK     = new Button("OK");
		Button btnCancel = new Button("Cancel");
		
        btnOK.setIcon(new ThemeResource("icons/16/ok.png"));
        btnCancel.setIcon(new ThemeResource("icons/16/cancel.png"));

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.addComponent(btnOK);
        buttons.addComponent(btnCancel);
        userForm.setFooter(buttons);
        
        userForm.setReadOnly(false);
        winUserForm.setWidth(400, Sizeable.UNITS_PIXELS);
        winUserForm.setHeight(250, Sizeable.UNITS_PIXELS);

        /* Field factory for overriding how the fields are created. */
        userForm.setFormFieldFactory(new DefaultFieldFactory() {
            @Override
            public Field createField(Item item, Object propertyId,Component uiContext) {
                Field field = super.createField(item, propertyId, uiContext);
                                                
                /* Set null representation of all text fields to empty */
                if (field instanceof TextField) {
                    ((TextField) field).setNullRepresentation("");
                }
                field.setWidth("200px");

                /* Set the correct caption to each field */
                for (int i = 0; i < DataModel.USER_COLS.length; i++) {
                    if (DataModel.USER_COLS[i].equals(propertyId)) {
                        field.setCaption(DataModel.USER_LABELS[i]);
                    }
                }
                return field;
            }
        });

        btnOK.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
                if (userForm.isValid()) {
                	userForm.commit();
                	try {
                		if (tableAction.equals("add")) {
                			ReturnStatus sts = dataModel.addUser(
                							userForm.getField("email").getValue().toString(), 
                							userForm.getField("userName").getValue().toString(), 
                							userForm.getField("password").getValue().toString());
                			if (sts.getMessage() != null) {
                				Item item = userContainer.getItem(tempItemId);
                				item.getItemProperty("ID").setValue(sts.getMessage());
                			}
                		}
                		if (tableAction.equals("edit")) {
                			dataModel.updateUser(userTable.getValue(),
					                			 userForm.getField("email").getValue().toString(), 
					                			 userForm.getField("userName").getValue().toString(), 
	                							 userForm.getField("password").getValue().toString());
                		}
    		            tableAction = "";
					} catch (UnsupportedOperationException e) {
						e.printStackTrace();
					}
                }
				navigator.removeDialog(winUserForm);
			}
		});
        
        btnCancel.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				userForm.discard();
				if (tableAction.equals("add")) {
	               	try {
	               		userContainer.removeItem(tempItemId);
					} catch (UnsupportedOperationException e) {
						e.printStackTrace();
					}
				}
				navigator.removeDialog(winUserForm);
			}
		});

    }
	
	private void createRoleForm() {
        /* No immediate updates of container. Updates to container need commit of the form. */
		//roleForm.setWriteThrough(false);
        
		roleContainer = dataModel.getRoleContainer();
		
        /* Init form footer with buttons */
		Button btnOK     = new Button("OK");
		Button btnCancel = new Button("Cancel");
		
        btnOK.setIcon(new ThemeResource("icons/16/ok.png"));
        btnCancel.setIcon(new ThemeResource("icons/16/cancel.png"));

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.addComponent(btnOK);
        buttons.addComponent(btnCancel);
        roleForm.setFooter(buttons);
        
        roleForm.setReadOnly(false);
        winRoleForm.setWidth(400, Sizeable.UNITS_PIXELS);
        winRoleForm.setHeight(250, Sizeable.UNITS_PIXELS);

        /* Field factory for overriding how the fields are created. */
        roleForm.setFormFieldFactory(new DefaultFieldFactory() {
            @Override
            public Field createField(Item item, Object propertyId,Component uiContext) {
                Field field = super.createField(item, propertyId, uiContext);
                                                                
                if (propertyId.equals("RoleId")) {
                	Select select = new Select("Role");
                	for (int i=0; i < roleContainer.size();i++) {
                		Item depitm = roleContainer.getItem(roleContainer.getIdByIndex(i));
                		Integer depid = (Integer) depitm.getItemProperty("ID").getValue();
                		String depname = depitm.getItemProperty("name").getValue().toString();
                		select.addItem(depid);
                		select.setItemCaption(depid, depname);
                	}
                	select.setItemCaptionMode(Select.ITEM_CAPTION_MODE_EXPLICIT);                   	
                	select.setNullSelectionAllowed(false);
                 	return select;
                } else if (propertyId.equals("UserId")) {
                	// UserId is part of container, but is made invisible since it is not relevant on the form
                	// UserId will be set to the selected value in : btnOK.addListener
                	field = super.createField(item, propertyId, uiContext);
                	field.setVisible(false);
                } else {
                	field = super.createField(item, propertyId, uiContext);                	
                    /* Set null representation of all text fields to empty */
                    if (field instanceof TextField) {
                        ((TextField) field).setNullRepresentation("");
                    }
                }

                field.setWidth("200px");               
                
                /* Set the correct caption to each field */
                for (int i = 0; i < DataModel.USERROLE_COLS.length; i++) {
                    if (DataModel.USERROLE_COLS[i].equals(propertyId)) {
                        field.setCaption(DataModel.USERROLE_LABELS[i]);
                    }
                }
                return field;
            }
        });

        btnOK.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
                if (roleForm.isValid()) {
                	// roleForm.commit();
                	try {
    					Integer RoleId = (Integer) roleForm.getField("RoleId").getValue();
            			ReturnStatus sts = dataModel.addUserRole(RoleId.toString(), userId);
            			if (sts.getMessage() != null) {
            				System.out.println("set ID to added item for tempItemId = " + tempItemId +  " , in roleTable with ID = " + sts.getMessage());
            				Item item = userroleContainer.getItem(tempItemId);
            				item.getItemProperty("ID").setValue(sts.getMessage());
            				item.getItemProperty("RoleId").setValue(RoleId);
            				System.out.println("Tempitem = " + item);
            				System.out.println("Tempitem property ids = " + item.getItemPropertyIds());
            				System.out.println("Tempitem  UserId = " + item.getItemProperty("UserId").getValue() + " / RoleId = " + item.getItemProperty("RoleId").getValue());
            			} else {            				
            				System.out.println("Did not receive ID for added item in roleTabe?");
            			}
    		            tableAction = "";
	            		// Refresh roleTable.
			    		roles4User  = dataModel.getRoles4User(userId);		
			    		roleTable.setContainerDataSource(roles4User);
		    			roleTable.select(roles4User.firstItemId()); 
					} catch (UnsupportedOperationException e) {
						e.printStackTrace();
					}
                }
				navigator.removeDialog(winRoleForm);
			}
		});
                
        btnCancel.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				roleForm.discard();
               	try {
               		System.out.println("Remove tempItemId " + tempItemId +  " from roleTabe");
               		userroleContainer.removeItem(tempItemId);
				} catch (UnsupportedOperationException e) {
					e.printStackTrace();
				}
				navigator.removeDialog(winRoleForm);
			}
		});

    }
}
