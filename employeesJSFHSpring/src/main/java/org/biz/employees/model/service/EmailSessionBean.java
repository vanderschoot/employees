package org.biz.employees.model.service;

import javax.inject.Named;
import org.springframework.stereotype.Service;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

@Named("emailBean")
@Service
public class EmailSessionBean {
	    
	private MailSender mailSender;
	@SuppressWarnings("unused")
	private SimpleMailMessage templateMessage;
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public void setTemplateMessage(SimpleMailMessage templateMessage) {
		this.templateMessage = templateMessage;
	}
	
	public boolean sendEmailSSL(String to, String subject, String body) {

		SimpleMailMessage message = new SimpleMailMessage();

		message.setFrom("joostvdschoot@gmail.com");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
        System.out.println("Send email");
		mailSender.send(message);	
        System.out.println("email sent");	            
        return true;
	}	
}
