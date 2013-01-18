package org.biz.employees.model.service;

import java.util.Iterator;
//import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
//import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


public class CustomExceptionHandler extends ExceptionHandlerWrapper {
	protected static Logger log = Logger.getLogger("CustomExceptionHandler");

    private ExceptionHandler wrapped;

    CustomExceptionHandler(ExceptionHandler exception) {
        this.wrapped = exception;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    @Override
    public void handle() throws FacesException {

        final Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();
        while (i.hasNext()) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context =
                    (ExceptionQueuedEventContext) event.getSource();

            // get the exception from context
            Throwable t = context.getException();

            //final FacesContext fc = FacesContext.getCurrentInstance();
            //final Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
            //final NavigationHandler nav = fc.getApplication().getNavigationHandler();

            //here you do what ever you want with exception
            try {

                //log error ?
                log.log(Level.ERROR, "Critical Exception!", t);
                FacesContext.getCurrentInstance().addMessage("newPassword", new FacesMessage("Error: " + t.getLocalizedMessage()));
                //redirect error page
                //requestMap.put("exceptionMessage", t.getMessage());
                //nav.handleNavigation(fc, null, "error");
                //fc.renderResponse();
            } finally {
                //remove it from queue
                i.remove();
            }
        }
        //parent handle
        getWrapped().handle();
    }
}