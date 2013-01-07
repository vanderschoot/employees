package org.biz.employeesRESTH.email;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@Stateless
@LocalBean
public class EmailSessionBean {
	
    @Resource(mappedName = "java:jboss/mail/GMailSSL")
    private Session sessionSSL;

	public boolean sendEmailSSL(String to, String subject, String body) {	
		
		System.out.println("sendEmailSSL, to:" + to + " / subject: " + subject + "body: " + body);

	    try {
            MimeMessage m = new MimeMessage(sessionSSL);
            Address from = new InternetAddress("joostvdschoot@gmail.com");
            Address[] tolist = new InternetAddress[] {new InternetAddress(to) };

            m.setFrom(from);
            m.setRecipients(Message.RecipientType.TO, tolist);
            m.setSubject(subject);
            m.setSentDate(new java.util.Date());
            m.setContent(body,"text/plain");
            System.out.println("Send email");
            Transport.send(m);
	        System.out.println("email sent");	            
	        return true;
	      } catch (MessagingException ex) {
	        ex.printStackTrace();
	        return false;
	      }
    }

	
	public boolean sendEmailSSL_HTML(String to, String subject, String body) {	
		
		System.out.println("sendEmailSSL_HTML, to:" + to + " / subject: " + subject + "body: " + body);
		
	    try {
	            String fromAddress = "joostvdschoot@gmail.com";
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
