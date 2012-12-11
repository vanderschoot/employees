package org.biz.employeesJ.model.ejb;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.model.LazyDataModel;  
import org.primefaces.model.SortOrder;

import java.util.Map;  
  
import org.biz.employeesJ.entities.Department;
import org.biz.employeesJ.entities.Employee;

// NB: To use this in JSF .xhtml you need an empty beans.xml deployment descriptor with the following content: <beans></beans>. 
// The existence of this configuration file in the WEB-INF folder activates CDI capabilities.
// The @Named annotation is also needed to make it visible.

@Stateless
@LocalBean
@Named


 public class EmployeeBean {
    
	private List<Employee> employees;
	private Department department;
	private Employee selectedEmployee = new Employee();
	private Department selectedDepartment = new Department();
    private LazyDataModel<Employee> employeesLazy;  
    private boolean showAll=false;
    
    int rowcount = 0;
    
    @EJB
    DepartmentBean departmentBean;
	
    @PersistenceContext
    EntityManager em;
    
	public EmployeeBean()  {
	}
	
	@PostConstruct
	public void init() throws Exception {
		selectedDepartment = departmentBean.getSelectedDepartment();
		System.out.println("EmployeeBean init : selectedDepartment = " + selectedDepartment.getName());
	}

	
    @SuppressWarnings("unchecked")
	public List<Employee> getEmployees() {
     	employees = em.createQuery("SELECT x FROM Employee x").getResultList();
		rowcount = em.createQuery("SELECT COUNT(x) FROM Employee x", Long.class).getSingleResult().intValue();
		return employees;
    }
    
    public LazyDataModel<Employee> getEmployeesLazy() {
    	if (employeesLazy == null) {
    		employeesLazy = new LazyDataModel<Employee>() {
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<Employee> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {  
                	String order = "ASC";
                	String query = "";
                	String countquery = "";
                	//Department department = departmentBean.getSelectedDepartment();
                	selectedDepartment = departmentBean.getSelectedDepartment();
                	
            		System.out.println("EmployeeBean LDM,  SelectedDepartment = " + selectedDepartment.getName());
                	
            		query = "SELECT x FROM Employee x";
            		countquery = "SELECT COUNT(x) FROM Employee x";

            		System.out.println("EmployeeBean LDM,  ShowAll = " + getShowAll());
            		
            		if ((selectedDepartment != null) && (!getShowAll()))  {
            			if (selectedDepartment.getDepartmentId() >= 0) {
            				query = query + " WHERE x.department = :department";
            				countquery = countquery + " WHERE x.department = :department";
            			}
            		}
            		
            		if (sortField != null) {
                        if (sortOrder == SortOrder.DESCENDING) {
                			order = "DESC";     
                		} 
                		query = query + " ORDER BY x." + sortField + " " + order;
                	} 

                	System.out.println("query = " + query);

 
                	if (!getShowAll()) {
                       	employees = em.createQuery(query).setFirstResult(first).setMaxResults(first + pageSize).setParameter("department", selectedDepartment).getResultList();
	            		this.setRowCount(em.createQuery(countquery, Long.class).setParameter("department", selectedDepartment).getSingleResult().intValue());
	                	System.out.println("rowcount = " + this.getRowCount());
                	} else {
                       	employees = em.createQuery(query).setFirstResult(first).setMaxResults(first + pageSize).getResultList();
	            		this.setRowCount(em.createQuery(countquery, Long.class).getSingleResult().intValue());
	                	System.out.println("rowcount = " + this.getRowCount());
                	}

            		return employees;
				}
			};
    	}
    	
    	return employeesLazy;
    }
    
    public Employee getSelectedEmployee() {  
		System.out.println("getSelectedEmployee : " + selectedEmployee.getLastName());
        return selectedEmployee;  
    }  
  
    public void setSelectedEmployee(Employee selectedEmployee) {  
		System.out.println("setSelectedEmployee : " + selectedEmployee.getLastName());
        this.selectedEmployee = selectedEmployee;  
    }  

    public Department getSelectedDepartment() {  
        return selectedDepartment;  
    }  
  
    public void setSelectedDepartment(Department selectedDepartment) {  
        this.selectedDepartment = selectedDepartment;  
    }  
    
    public boolean getShowAll() {
    	return showAll;
   }
    
    public void setShowAll(boolean showAll) {
    	this.showAll = showAll;
   }
    public void toggleShowAll(ActionEvent event) {
    	this.showAll = !this.showAll;
    	System.out.println("toggleShowAll : " + this.showAll);
    }

    public Department getDepartment() {  
        return department;  
    }  
  
    public void setDepartment(Department department) {  
        this.department= department;  
    }  

    public void createOrUpdateEmployee(ActionEvent event) {
    	System.out.println("createOrUpdateEmployee");

    	System.out.println("EmployeeId = " + selectedEmployee.getEmployeeId());
    	System.out.println("FirstName  = " + selectedEmployee.getFirstName());
    	System.out.println("LastName   = " + selectedEmployee.getLastName());
    	System.out.println("BirthDate  = " + selectedEmployee.getBirthDate());
    	System.out.println("Department name = " + selectedEmployee.getDepartment().getName());
    	System.out.println("Department Id   = " + selectedEmployee.getDepartment().getDepartmentId());

    	Employee dpt = new Employee(); 
		dpt.setEmployeeId(selectedEmployee.getEmployeeId());
		dpt.setBirthDate(selectedEmployee.getBirthDate());
		dpt.setDepartment(selectedEmployee.getDepartment());
		dpt.setFirstName(selectedEmployee.getFirstName());
		dpt.setLastName(selectedEmployee.getLastName());
		
		boolean err = false;
		
		if (selectedEmployee.getBirthDate() != null) {
			if (selectedEmployee.getBirthDate().after(new Date())) {
				System.out.println("BirthDate must not be in the future");
				FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Birthdate must not be in the future" , "extra info???"));
				err = true;
			}
		}
		
		if (!err) {
			try {
				if (selectedEmployee.getEmployeeId() == 0) {
					System.out.println("persist");
					em.persist(dpt);
				    FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO,"Added " + selectedEmployee.getFirstName()  + " " + selectedEmployee.getLastName() , "extra info???")); 
				    //FacesMessage.SEVERITY_WARN
				    //FacesMessage.SEVERITY_ERROR
				    //FacesMessage.SEVERITY_FATAL			    
				} else {
					System.out.println("merge");
					//em.clear();
					em.merge(dpt);
				    FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO,"Updated " + selectedEmployee.getFirstName()  + " " + selectedEmployee.getLastName(), "extra info???")); 
				}
				selectedEmployee = new Employee();
			}
			catch (java.lang.Exception e)
			{
				System.out.println("Error : " + e.getMessage());
			    FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO,"Error :  " + e.getMessage() , "extra info???")); 
			}
		}
    }

    public void clearEmployee(ActionEvent event) {
    	System.out.println("clearEmployee");
    	selectedEmployee = new Employee();
    	UIViewRoot viewRoot =  FacesContext.getCurrentInstance().getViewRoot();
	    UIComponent component1 = viewRoot.findComponent("form-createupdate");  
	    this.clearComponentHierarchy(component1);
    }
        
    @SuppressWarnings("unused")
	public void deleteEmployee(ActionEvent event) {
    	System.out.println("deleteEmployee");
    	String resultstatus;
    	String msg;
		try {
			Employee dpt = em.find(Employee.class, selectedEmployee.getEmployeeId());
			if (dpt != null) {				
				em.remove(em.merge(dpt));
			    FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage("Deleted " + selectedEmployee.getFirstName()  + " " + selectedEmployee.getLastName())); 
				selectedEmployee = new Employee();
			} else {
				resultstatus = "ERROR"; msg = "record not found";
			}			
		} catch (IllegalStateException e) {
			e.printStackTrace(); resultstatus = "ERROR"; msg = e.getMessage();
		} catch (SecurityException e) {
			e.printStackTrace(); resultstatus = "ERROR"; msg = e.getMessage();
		}
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

    
}
