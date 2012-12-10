package org.biz.employeesSOAP.email;

import java.util.Date;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@Stateless
@LocalBean

public class EmailSessionBean {
	
    @Resource(name = "GMailSSL")
    private Session sessionSSL;

    private String usernameSSL = "";
    private String passwordSSL = "";

    @Resource(name = "GMailTLS")
    private Session sessionTLS;
    
    private String usernameTLS = "";
    private String passwordTLS = "";

	public boolean sendEmailTLS(String to, String subject, String body) {	
		
		System.out.println("sendEmailTLS, to:" + to + " / subject: " + subject + "body: " + body);
		
	    usernameTLS = sessionTLS.getProperty("mail.smtp.user");
	    passwordTLS = sessionTLS.getProperty("mail.smtp.password");

	    try {
	            Properties props = new Properties();
	            props.put("mail.smtp.host", sessionTLS.getProperty("mail.smtp.host"));
	            props.put("mail.smtp.starttls.enable", sessionTLS.getProperty("mail.smtp.starttls.enable"));
	            props.put("mail.smtp.auth", sessionTLS.getProperty("mail.smtp.auth"));
	            props.put("mail.smtp.port", sessionTLS.getProperty("mail.smtp.port"));
	            props.put("debug", sessionTLS.getProperty("debug"));
	            
	            String fromAddress = sessionTLS.getProperty("mail.smtp.from");
	            System.out.println("user/password: " + usernameTLS + "/" + passwordTLS);

	            sessionTLS  =   Session.getDefaultInstance(props,new Authenticator() {
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(usernameTLS,passwordTLS);
	                }
	            });

	            sessionTLS.setDebug(props.getProperty("debug") == "true");

	            Message message  =   new MimeMessage(sessionTLS);
	 
	            message.setFrom(new InternetAddress(fromAddress));
	            message.setRecipient(RecipientType.TO, new InternetAddress(to));
	            message.setSubject(subject);
	            message.setContent(body,"text/plain");
	            System.out.println("Send email");
	            Transport.send(message);
	            System.out.println("email sent");	            
	            return true;
	      } catch (MessagingException ex) {
	            ex.printStackTrace();
	            return false;
	      }
    }
	
	public boolean sendEmailSSL(String to, String subject, String body) {	
		
		System.out.println("sendEmailSSL, to:" + to + " / subject: " + subject + "body: " + body);

		usernameSSL = sessionSSL.getProperty("mail.smtp.user");
	    passwordSSL = sessionSSL.getProperty("mail.smtp.password");

	    try {
	            Properties props = new Properties();
	            props.put("mail.smtp.host", sessionSSL.getProperty("mail.smtp.host"));
	            props.put("mail.smtp.socketFactory.port", sessionSSL.getProperty("mail.smtp.socketFactory.port"));
	            props.put("mail.smtp.socketFactory.class", sessionSSL.getProperty("mail.smtp.socketFactory.class"));
	            props.put("mail.smtp.auth", sessionSSL.getProperty("mail.smtp.auth"));
	            props.put("mail.smtp.port", sessionSSL.getProperty("mail.smtp.port"));
	            props.put("debug", sessionSSL.getProperty("debug"));
	            
	            String fromAddress = sessionSSL.getProperty("mail.smtp.from");
	            System.out.println("user/password: " + usernameSSL + "/" + passwordSSL);

	            sessionSSL  =   Session.getDefaultInstance(props,new Authenticator() {
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(usernameSSL,passwordSSL);
	                }
	            });

	            sessionSSL.setDebug(props.getProperty("debug") == "true");

	            Message message  =   new MimeMessage(sessionSSL);
	 
	            message.setFrom(new InternetAddress(fromAddress));
	            message.setRecipient(RecipientType.TO, new InternetAddress(to));
	            message.setSubject(subject);
	            message.setContent(body,"text/plain");
	            System.out.println("Send email");
	            Transport.send(message);
	            System.out.println("email sent");	            
	            return true;
	      } catch (MessagingException ex) {
	            ex.printStackTrace();
	            return false;
	      }
    }

	
	public boolean sendEmailSSL_HTML(String to, String subject, String body) {	
		
		System.out.println("sendEmailSSL_HTML, to:" + to + " / subject: " + subject + "body: " + body);
		
		usernameSSL = sessionSSL.getProperty("mail.smtp.user");
	    passwordSSL = sessionSSL.getProperty("mail.smtp.password");
		
	    try {
	            Properties props = new Properties();
	            props.put("mail.smtp.host", sessionSSL.getProperty("mail.smtp.host"));
	            props.put("mail.smtp.socketFactory.port", sessionSSL.getProperty("mail.smtp.socketFactory.port"));
	            props.put("mail.smtp.socketFactory.class", sessionSSL.getProperty("mail.smtp.socketFactory.class"));
	            props.put("mail.smtp.auth", sessionSSL.getProperty("mail.smtp.auth"));
	            props.put("mail.smtp.port", sessionSSL.getProperty("mail.smtp.port"));
	            props.put("debug", sessionSSL.getProperty("debug"));
	            
	            String fromAddress = sessionSSL.getProperty("mail.smtp.from");
	            System.out.println("user/password: " + usernameSSL + "/" + passwordSSL);

	            sessionSSL  =   Session.getDefaultInstance(props,new Authenticator() {
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(usernameSSL,passwordSSL);
	                }
	            });

	            sessionSSL.setDebug(props.getProperty("debug") == "true");

	            Message message  =   new MimeMessage(sessionSSL);
	 
	            message.setFrom(new InternetAddress(fromAddress));
	            message.setRecipient(RecipientType.TO, new InternetAddress(to));
	            message.setSubject(subject);
	            
	            // Create a MimeMultipart instance with an alternative sub-type. 
	            // A multi-part message consists of multiple parts, in this case an HTML and a text message (other possible parts are file attachments). 
	            // The alternative sub-type indicates that the multiple message parts are alternative versions of the same content. 
	            
	            Multipart multipart = new MimeMultipart("alternative");	 
	            
	            // Create a MimeBodyPart instance to contain the text body part

				MimeBodyPart textPart = new MimeBodyPart();
				textPart.setText(body + "(textcontent)");

				// Create a MimeBodyPart instance to contain the HTML body part. 
				// Order is important, the preferred format of an alternative multi-part message should be added last.

				MimeBodyPart htmlPart = new MimeBodyPart();
				String htmlContent = "<html><h1>Hi</h1><p>" +  body + "(htmlcontent)" + "</p></html>";
				htmlPart.setContent(htmlContent, "text/html");

				// Add both MimeBodyPart instances to the MimeMultipart instance and set the MimeMultipart instance as the MimeMessage.

				multipart.addBodyPart(textPart);
				multipart.addBodyPart(htmlPart);
				message.setContent(multipart);	            
	            
	            System.out.println("Send email");
	            Transport.send(message);
	            System.out.println("email sent");	            
	            return true;
	      } catch (MessagingException ex) {
	            ex.printStackTrace();
	            return false;
	      }
    }
	
}
