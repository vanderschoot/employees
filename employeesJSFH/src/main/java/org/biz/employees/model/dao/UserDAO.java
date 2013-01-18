package org.biz.employees.model.dao;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.biz.employees.model.entities.Role;
import org.biz.employees.model.entities.User;
import org.biz.employees.model.entities.UserRole;

@Stateless
@LocalBean
@Named
public class UserDAO extends AbstractDAO<User> {
	
	@PersistenceContext
	private EntityManager em;

	public UserDAO(){
		setClassT(User.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Role> getRoles(final User user) {
		
		String qry = "SELECT ur FROM UserRole ur " + 
					 "JOIN FETCH ur.role " +
					 "WHERE " + 
					 "ur.user.userName = '" + user.getUserName() + "'";
		
		List<Role> roles = new ArrayList<Role>();
		List<UserRole> userroles =  em.createQuery(qry).getResultList();
		System.out.println("number of userroles for " + user.getUserName() +  " = " + userroles.size());
		
		for (UserRole ur : userroles) {
			roles.add(ur.getRole());
		}
		
		return roles;
	}
	
	@SuppressWarnings("unchecked")
	public List<User> findUsers(final String userName) {
		
		String qry = "SELECT u FROM User u " + 
					 "WHERE " + 
					 "u.userName = '" + userName + "'";
		
		List<User> users =  em.createQuery(qry).getResultList();
		System.out.println("number of users for " + userName +  " = " + users.size());
		
		return users;
	}

}