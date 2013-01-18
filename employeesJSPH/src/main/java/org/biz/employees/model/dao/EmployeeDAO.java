package org.biz.employees.model.dao;


import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.biz.employees.model.entities.Employee;

@Stateless
@LocalBean
@Named
public class EmployeeDAO extends AbstractDAO<Employee> {
	
	@PersistenceContext()
	private EntityManager em;

	public EmployeeDAO(){
		setClassT(Employee.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Employee> get4Department(int departmentId) {
		Query query = em.createQuery("select e from Employee e where e.department.departmentId = " + departmentId);
		return (List<Employee>) query.getResultList(); 
	}

}