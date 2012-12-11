package org.biz.employeesJH.model.ejb;

import java.util.Iterator;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.model.LazyDataModel;  
import org.primefaces.model.SortOrder;
import java.util.Map;  
  
import org.biz.employeesJH.data.Database;
import org.biz.employeesJH.entities.Department;
import org.biz.employeesJH.utils.JsfUtil;

// NB: To use this in JSF .xhtml you need an empty beans.xml deployment descriptor with the following content: <beans></beans>. 
// The existence of this configuration file in the WEB-INF folder activates CDI capabilities.
// The @Named annotation is also needed to make it visible.

@Stateless
@LocalBean
@Named
public class DepartmentBean {
    
	private List<Department> departments;
	private Department selectedDepartment = new Department();
    private LazyDataModel<Department> departmentsLazy;  
    
    int rowcount = 0;
	
    @PersistenceContext
    EntityManager em;
    
	public DepartmentBean()  {
		System.out.println("DepartmentBean Created");
		Database database = new Database();
		database.createDatabase();
	}
	
    @SuppressWarnings("unchecked")
	public List<Department> getDepartments() {
		System.out.println("DepartmentBean.getDepartments()");
    	departments = em.createQuery("SELECT x FROM Department x").getResultList();
		System.out.println("DepartmentBean.getDepartments()");
		rowcount = em.createQuery("SELECT COUNT(x) FROM Department x", Long.class).getSingleResult().intValue();
		return departments;
    }
    
    public Department find(Object id) {
        return em.find(Department.class, id);
    }

    
    // Wordt gebruikt om een array te maken van een list t.b.v. een listbox (MenuSelectOne)
    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(this.getDepartments(), true);
    }

    
    public LazyDataModel<Department> getDepartmentsLazy() {
		System.out.println("getDepartmentsLazy");
    	if (departmentsLazy == null) {
    		departmentsLazy = new LazyDataModel<Department>() {
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<Department> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {  
					int start = first;
                	int end = first + pageSize;
                	String order = "ASC";
                	String query = "";
                	
                	if (sortField != null) {
                        if (sortOrder == SortOrder.DESCENDING) {
                			order = "DESC";     
                		} 
                		query = "SELECT x FROM Department x ORDER BY x." + sortField + " " + order;
                	} else
                	{
                		query = "SELECT x FROM Department x";                		
                	}

                	System.out.println("query = " + query);

                	departments = em.createQuery(query).setFirstResult(start).setMaxResults(end).getResultList();
            		
            		this.setRowCount(em.createQuery("SELECT COUNT(x) FROM Department x", Long.class).getSingleResult().intValue());
            		
            		return departments;
				}

			};
    	}
    	
    	return departmentsLazy;
    }
    
    public Department getSelectedDepartment() {  
		System.out.println("getSelectedDepartment : " + selectedDepartment.getName());
        return selectedDepartment;  
    }  
  
    public void setSelectedDepartment(Department selectedDepartment) {  
		System.out.println("setSelectedDepartment : " + selectedDepartment.getName());
        this.selectedDepartment = selectedDepartment;  
    }  

    public void createOrUpdateDepartment(ActionEvent event) {
    	System.out.println("createOrUpdateDepartment");

    	System.out.println("Id = " + selectedDepartment.getDepartmentId());

    	Department dpt = new Department();
		dpt.setDepartmentId(selectedDepartment.getDepartmentId());
		dpt.setAddress(selectedDepartment.getAddress());
		dpt.setBudget(selectedDepartment.getBudget());
		dpt.setName(selectedDepartment.getName());
		
		if (selectedDepartment.getBudget() <= 0) {
		    FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Budget must be > 0" , "extra info???")); 			
		} else
		{
			try {
				if (selectedDepartment.getDepartmentId() == 0) {
					System.out.println("persist");
					em.persist(dpt);
				    FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO,"Added " + selectedDepartment.getName() , "extra info???")); 
				    //FacesMessage.SEVERITY_WARN
				    //FacesMessage.SEVERITY_ERROR
				    //FacesMessage.SEVERITY_FATAL			    
				} else {
					System.out.println("merge");
					//em.clear();
					em.merge(dpt);
				    FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO,"Updated " + selectedDepartment.getName(), "extra info???")); 
				}
				selectedDepartment = new Department();
			}
			catch (java.lang.Exception e)
			{
				System.out.println("Error : " + e.getMessage());
			}
		}
    }

    public void clearDepartment(ActionEvent event) {
    	System.out.println("clearDepartment");
    	selectedDepartment = new Department();
    	UIViewRoot viewRoot =  FacesContext.getCurrentInstance().getViewRoot();
	    UIComponent component1 = viewRoot.findComponent("form-createupdate");  
	    this.clearComponentHierarchy(component1);
	    UIComponent component2 = viewRoot.findComponent("center");  
    	System.out.println("center=" + component2.getContainerClientId(FacesContext.getCurrentInstance()) + " / " + component2.getId());
    }
        
    @SuppressWarnings("unused")
	public void deleteDepartment(ActionEvent event) {
    	System.out.println("deleteDepartment");
    	String resultstatus;
    	String msg;
		try {
			Department dpt = em.find(Department.class, selectedDepartment.getDepartmentId());
			if (dpt != null) {				
				em.remove(em.merge(dpt));
			    FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage("Deleted " + selectedDepartment.getName())); 
				selectedDepartment = new Department();
			} else {
				resultstatus = "ERROR"; msg = "record not found";
			}			
		} catch (IllegalStateException e) {
			e.printStackTrace(); resultstatus = "ERROR"; msg = e.getMessage();
		} catch (SecurityException e) {
			e.printStackTrace(); resultstatus = "ERROR"; msg = e.getMessage();
		}
    }

    public String showEmployees(){
    	System.out.println("showEmployees");
	    return "/employee/employeelistpf"; //outcome
	}
    
	public void clearComponentHierarchy(UIComponent pComponent) {

		if (pComponent.isRendered()) {

			if (pComponent instanceof EditableValueHolder) {
				EditableValueHolder editableValueHolder = (EditableValueHolder) pComponent;
				editableValueHolder.setSubmittedValue(null);
				editableValueHolder.setValue(null);
				editableValueHolder.setLocalValueSet(false);
				editableValueHolder.setValid(true);
			}

			for (Iterator<UIComponent> iterator = pComponent.getFacetsAndChildren(); iterator.hasNext();) {
				clearComponentHierarchy(iterator.next());
			}
		}
	}
	
    @FacesConverter(forClass=Department.class)
    public static class ConsultantControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DepartmentBean controller = (DepartmentBean)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "departmentBean");
            return controller.find(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Department) {
            	Department o = (Department) object;
                return getStringKey(o.getDepartmentId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+DepartmentBean.class.getName());
            }
        }

    }


    
}
