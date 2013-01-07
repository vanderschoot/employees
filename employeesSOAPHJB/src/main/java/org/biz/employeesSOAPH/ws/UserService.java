package org.biz.employeesSOAPH.ws;


import java.util.List;

import javax.jws.WebService;

import org.biz.employeesSOAPH.entities.User;
import org.biz.employeesSOAPH.ws.ReturnStatus;

@WebService(targetNamespace = "http://biz.org/wsdl")
public interface UserService {
    public ReturnStatus create(String email,String userName,String password);
 	
    public List<User> list(int first,int max);
 		
    public User find(int id);
  
    public ReturnStatus delete(int id);

    public ReturnStatus update(long id,String email,String userName,String password);
}
