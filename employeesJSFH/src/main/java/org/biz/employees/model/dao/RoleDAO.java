package org.biz.employees.model.dao;


import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import org.biz.employees.model.entities.Role;

@Stateless
@LocalBean
@Named
public class RoleDAO extends AbstractDAO<Role> {
	
	public RoleDAO(){
		setClassT(Role.class);
	}
}