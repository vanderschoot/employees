package org.biz.employeesSOAPH.email;

import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;

public final class Emailer {

  public void sendEmail(
    String aFromEmailAddr, String aToEmailAddr,
    String aSubject, String aBody){
	  
    //Here, no Authenticator argument is used (it is null).
    //Authenticators are used to prompt the user for user
    //name and password.
    Session session = Session.getDefaultInstance( fMailServerConfig, null );
    MimeMessage message = new MimeMessage( session );
    
    try {
      //the "from" address may be set in code, or set in the
      //config file under "mail.from" ; here, the latter style is used
      //message.setFrom( new InternetAddress(aFromEmailAddr) );
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(aToEmailAddr));

      message.setSubject( aSubject );
      message.setText( aBody );
      Transport.send( message );
    } catch (MessagingException ex){
      System.out.println("Cannot send email. " + ex);
    }
  }

  /**
  * Allows the config to be refreshed at runtime, instead of
  * requiring a restart.
  */
  public static void refreshConfig() {
    fMailServerConfig.clear();
    fetchConfig();
  }

  // PRIVATE //

  private static Properties fMailServerConfig = new Properties();

  static {
    fetchConfig();
  }

  /**
  * Open a specific text file containing mail server
  * parameters, and populate a corresponding Properties object.
  */
  private static void fetchConfig() {
    InputStream input = null;
    try {
      //If possible, one should try to avoid hard-coding a path in this
      //manner; in a web application, one should place such a file in
      //WEB-INF, and access it using ServletContext.getResourceAsStream.
      //Another alternative is Class.getResourceAsStream.
      //This file contains the javax.mail config properties mentioned above.
      input = new FileInputStream( "C:\\Temp\\MyMailServer.txt" );
      fMailServerConfig.load( input );
    }
    catch ( IOException ex ){
      System.out.println("Cannot open and load mail server properties file.");
    }
    finally {
      try {
        if ( input != null ) input.close();
      }
      catch ( IOException ex ){
        System.out.println( "Cannot close mail server properties file." );
      }
    }
  }
} 