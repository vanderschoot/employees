package org.biz.employeesSOAPH.ws.impl;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.biz.employeesSOAPH.entities.Role;
import org.biz.employeesSOAPH.ws.ReturnStatus;
import org.biz.employeesSOAPH.ws.RoleService;

@Stateless
@WebService(
        portName = "RolePort",
        serviceName = "RoleServiceWS",
        targetNamespace = "http://biz.org/wsdl",
        endpointInterface = "org.biz.employeesSOAPH.ws.RoleService")
public class RoleServiceWS implements RoleService {
    @PersistenceContext
    private EntityManager em;
   
    public RoleServiceWS() {
    	System.out.println("SOAP Webservice RoleServiceWS created");
    }
    
    @WebMethod
    public ReturnStatus create(String name) {
    	Role rol = new Role();
    	rol.setName(name);
        System.out.println("create : " + name);
    	
        em.persist(rol);
        System.out.println("create, id = " + rol.getRoleId());
        ReturnStatus sts = new ReturnStatus(true);
        sts.setMessage(new Integer(rol.getRoleId()).toString());
        return sts;        
    }
 	
    @WebMethod
    public List<Role> list(int first,int max) {
 		
 		System.out.println("role list!!!!");
        List<Role> roles = new ArrayList<Role>();
        List<Role> found = em.createQuery("select r from Role r",Role.class)
        		.setFirstResult(first).setMaxResults(max).getResultList();
       
        System.out.println("role list2!!!!");
     	
        for (Role r : found) {
        	System.out.println("r.copy().name" + r.copy().getName());
        	roles.add(r.copy());
        }

        return roles;
    }
 	
 	
    @WebMethod
    public Role find(int id) {
        System.out.println("Find, param = " + id);
        Role rol = em.find(Role.class, id);
        System.out.println("Find Finished");
        if (rol == null) {
        	System.out.println("Find, role = null");
            return null;
        }
        System.out.println("Find role not null");
        System.out.println("Find, role = " + rol.copy().getName());
        return rol.copy();        
    }
    
    @WebMethod
    public ReturnStatus delete(int id) {
        System.out.println("delete, id = " + id);
        Role  rol = em.find(Role.class, id);
        if (rol != null) {
            em.remove(rol);
            System.out.println("Delete OK");
            return new ReturnStatus(true);
        }
        return new ReturnStatus(false);
    }
       
    @WebMethod
    public ReturnStatus update(long id,String name) {

    	System.out.println("update : " + name);
        
        Role rol = em.find(Role.class, id);
        if (rol == null) {
            ReturnStatus sts = new ReturnStatus(false);
            sts.setMessage("Role id " + id + " not found");
            return sts;
        }

        rol.setName(name);
        em.merge(rol);
        
        return new ReturnStatus(true);
    }

    
}
