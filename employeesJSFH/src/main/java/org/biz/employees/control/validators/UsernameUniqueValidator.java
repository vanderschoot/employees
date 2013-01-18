package org.biz.employees.control.validators;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.biz.employees.control.beans.UserBean;
import org.biz.employees.model.dao.UserDAO;
import org.biz.employees.model.entities.User;

@ManagedBean
@RequestScoped
public class UsernameUniqueValidator implements Validator {
	
	@EJB private UserDAO dao;

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		
	    UserBean userBean = (UserBean) component.getValueExpression("userBean").getValue(context.getELContext());
		
	    if (userBean.getUserId() < 0) {
			List<User> users = dao.findUsers(value.toString());
			if (users!= null) {
				if (users.size() >  0) {
					FacesMessage msg =  new FacesMessage("User Name ongeldig.", 
							 "Naam is al in gebruik. Kies een andere naam.");
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					throw new ValidatorException(msg);
				}
			}
	    }
	}

}
