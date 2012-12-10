package org.biz.employeesVS.ws;

import javax.jws.WebService;

import org.biz.employeesVS.ws.ReturnStatus;

@WebService(targetNamespace = "http://biz.org/wsdl")
public interface SecurityService {
	
    public ReturnStatus logout();

    public boolean checkPassword(String userName, String password);
 
    public boolean checkUser(String userName) ;
    
    public ReturnStatus login(String Auth);
    
    public ReturnStatus register(String email,String userName,String password);

    public ReturnStatus askpassword(String email,String userName);

	public ReturnStatus checkrole(String userName,String roleName);

}
