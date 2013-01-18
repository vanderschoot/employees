package org.biz.employees.model.dao;


import javax.inject.Named;
import org.biz.employees.model.entities.Role;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Named("roleDAO")
@Repository
@Transactional
public class RoleDAO extends AbstractDAO<Role> {
	
	public RoleDAO(){
		setClassT(Role.class);
	}
}