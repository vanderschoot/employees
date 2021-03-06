package org.biz.employees.model.dao;


import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.biz.employees.model.entities.Department;
import org.biz.employees.model.entities.Employee;

@Stateless
@LocalBean
@Named
public class DepartmentDAO extends AbstractDAO<Department> {

	@PersistenceContext
	private EntityManager em;
	
	public DepartmentDAO(){
		setClassT(Department.class);
	}

	@SuppressWarnings("unchecked")
	public List<Employee> getEmployees(final Department department) {
		return  em.createQuery(
				"SELECT e FROM Employee e WHERE e.department = :Department ORDER BY e.lastName")
				.setParameter("Department", department).getResultList();
	}
}