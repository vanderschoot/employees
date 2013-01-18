package org.biz.employees.model.dao;


import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.biz.employees.model.entities.Department;
import org.biz.employees.model.entities.Employee;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Named("departmentDAO")
@Repository
@Transactional
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