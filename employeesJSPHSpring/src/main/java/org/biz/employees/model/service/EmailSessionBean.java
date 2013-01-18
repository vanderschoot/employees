package org.biz.employees.model.service;

import javax.inject.Named;
import org.springframework.stereotype.Service;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

@Named("emailBean")
@Service
public class EmailSessionBean {
	    
	private MailSender mailSender;
	private SimpleMailMessage templateMessage;
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public void setTemplateMessage(SimpleMailMessage templateMessage) {
		this.templateMessage = templateMessage;
	}
	
	public String sendEmail(String to, String subject, String body) {

		SimpleMailMessage message = new SimpleMailMessage();

		message.setFrom("maritevdschoot@gmail.com");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
        System.out.println("Send email : to=" + to + " / subject=" + subject + " / message=" + body);
        try {
        	mailSender.send(message);	
        } catch(MailAuthenticationException e) {
            System.out.println("Error in Send email. " + e.getMessage());
        	return e.getMessage();
        }
        System.out.println("email sent");	            
        return "OK";
	}	
}
