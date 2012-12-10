package org.biz.employeesSOAPH.ws;


import java.util.List;
import javax.jws.WebService;

import org.biz.employeesSOAPH.entities.UserRole;
import org.biz.employeesSOAPH.ws.ReturnStatus;


@WebService(targetNamespace = "http://biz.org/wsdl")
public interface UserRoleService {

	public ReturnStatus create(int roleId,int userId);
    
    public ReturnStatus createroles(int userId, String roleList);
 	
    public List<UserRole> list(int first,int max, int userId,int roleId);
 		
    public UserRole find(int id);
    
    public ReturnStatus delete(int id);

    public ReturnStatus delroles(int userId, String roleList);
    
    public ReturnStatus update(long id,int userId,int roleId) ;
    
}
