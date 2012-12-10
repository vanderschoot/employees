package org.biz.employeesVS.ws;


import java.util.List;
import javax.jws.WebService;
import org.biz.employeesVS.data.DTO.Department;

@WebService(targetNamespace = "http://biz.org/wsdl")
public interface DepartmentService {

    public ReturnStatus create(String name,String address, int budget) ;
    
    public List<Department> list(int first, int max);
 	
    public Department find(int id);

    public ReturnStatus delete(int id);
        
    public ReturnStatus update(long id, String name,String address,int budget);

}
