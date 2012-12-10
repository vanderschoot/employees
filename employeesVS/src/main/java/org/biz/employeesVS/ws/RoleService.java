package org.biz.employeesVS.ws;


import java.util.List;
import javax.jws.WebService;
import org.biz.employeesVS.data.DTO.Role;
import org.biz.employeesVS.ws.ReturnStatus;

@WebService(targetNamespace = "http://biz.org/wsdl")
public interface RoleService {

	public ReturnStatus create(String name);
 	
    public List<Role> list(int first,int max);
 	
    public Role find(int id);
    
    public ReturnStatus delete(int id);
       
    public ReturnStatus update(long id,String name);
    
}
