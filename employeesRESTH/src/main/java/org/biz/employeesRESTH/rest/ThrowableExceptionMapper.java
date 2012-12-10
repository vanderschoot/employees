package org.biz.employeesRESTH.rest;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;
  
@Provider
public class ThrowableExceptionMapper implements ExceptionMapper<Throwable>{
 
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(Throwable ex) {
        System.out.println("ThrowableExceptionMapper: "+ex.getClass());
        ex.printStackTrace();
        return Response.status(500).entity(new Gson().toJson(new ReturnStatus(false,ex.getMessage()))).build();
    }
 
}