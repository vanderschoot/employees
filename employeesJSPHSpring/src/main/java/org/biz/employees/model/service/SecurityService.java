package org.biz.employees.model.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import sun.misc.*;

import org.biz.employees.model.service.EmailSessionBean;
import org.biz.employees.model.entities.User;
import org.biz.employees.model.entities.UserRole;
import org.biz.employees.model.service.ReturnStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;


@Named("sec")
@Service
public class SecurityService  {
    @PersistenceContext
    private EntityManager em;
    
    @Autowired
	private EmailSessionBean emailBean;
    
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    JpaTransactionManager transactionManager;

	public SecurityService() {		
		System.out.println("SecurityService created");
	}	
	
    public boolean checkPassword(String userName, String password) {
 
        System.out.println("checkPassword " + password);

    	User usr = null;
    	
        String qry = "select u from User u where u.userName = '" + userName + "'";
        System.out.println("Query = " + qry);
        

        if (em != null) {
	        List<User> users = em.createQuery(qry,User.class)
	    			.setFirstResult(0).setMaxResults(1).getResultList();
	
	        System.out.println("Query uitgevoerd");
	
	    	if (users != null) {
	            System.out.println("Er lijkt resultaat");
	            if (!users.isEmpty()) {
	                System.out.println("Proberen te lezen");
		    		usr = users.get(0);
			        System.out.println("User gevonden : " + usr.getUserName());
			    	System.out.println("User password = " + usr.getPassword());
					if (usr.getPassword().equals(password)) {
					    System.out.println("password ok");
					    return true;
					} else {
					    System.out.println("password niet ok");
						return false;
					}
	            } else {
	                System.out.println("Toch geen resultaat");
	            	return false;
	            }
	    	} else {
	    		return false;
	    	}
        } else {
	        System.out.println("EntityManager em = null");
        	return false;
        }
    }
 
    public boolean checkUser(String userName) {

        System.out.println("checkUser " + userName);

    	User usr = null;
    	
        String qry = "select u from User u where u.userName = '" + userName + "'";
        System.out.println("Query = " + qry);
        
    	List<User> users = em.createQuery(qry,User.class)
    			.setFirstResult(0).setMaxResults(1).getResultList();

        System.out.println("Query uitgevoerd");

    	if (users != null) {
            System.out.println("Er lijkt resultaat");
            if (!users.isEmpty()) {
                System.out.println("Proberen te lezen");
	    		usr = users.get(0);
		        System.out.println("User gevonden : " + usr.getUserName());
		        return true;
            } else {
                System.out.println("Toch geen resultaat");
            	return false;
            }
    	} else {
	        System.out.println("User niet gevonden");
            return false;
    	}
    }
    
    public String getPassword(String userName) {

        System.out.println("getPassword " + userName);

    	User usr = null;
    	
        String qry = "select u from User u where u.userName = '" + userName + "'";
        System.out.println("Query = " + qry);
        
    	List<User> users = em.createQuery(qry,User.class)
    			.setFirstResult(0).setMaxResults(1).getResultList();

        System.out.println("Query uitgevoerd");

    	if (users != null) {
            System.out.println("Er lijkt resultaat");
            if (!users.isEmpty()) {
                System.out.println("Proberen te lezen");
	    		usr = users.get(0);
		        System.out.println("User gevonden : " + usr.getUserName());
		        return usr.getPassword();
            } else {
                System.out.println("Toch geen resultaat");
            	return null;
            }
    	} else {
	        System.out.println("User niet gevonden");
            return null;
    	}
    }
    
    public ReturnStatus login(HttpServletRequest request, String Auth) {
   	
        System.out.println("login");
        
        String[] result = decodeToken(Auth);
        System.out.println("login, result = " + result[0]);
        
    	if (result[0].equals("OK")) {
    		
    		String username = result[1];
    		String password = result[2];
    		   	    
	        System.out.println("login : " + username + " / password = " + password);
        	if (checkPassword(username, password)) {
				
		        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
		        Authentication auth;
		        //token.setDetails(new WebAuthenticationDetails(request));
		        try {
		            auth = authenticationManager.authenticate(token);
		            System.out.println("Login succeeded!");
		            SecurityContext securityContext = SecurityContextHolder.getContext();
		            securityContext.setAuthentication(auth);

		            //HttpSession session = request.getSession(true);
		            //session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
		        } catch (BadCredentialsException e) {
		            System.out.println("login BadCredentialsException");
			        return new ReturnStatus(false,"login BadCredentialsException" + e.getMessage());
		        }

		        System.out.println("Login OK");
		        return new ReturnStatus(auth.isAuthenticated(),getroles(result[1]));
	        } else {
	            System.out.println("user niet gevonden (null)");
		        return new ReturnStatus(false,"user niet gevonden (null)");
	        }
    	} else {
            System.out.println("invalid");
	        return new ReturnStatus(false,result[1]);
    	}
    }    
    
    public ReturnStatus logout(String user) {
        System.out.println("logout, user = " + user);
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
	        String currentuser = auth.getName();
	        System.out.println("currentuser = " + currentuser);
	        if (user.equals(currentuser)) {	        
		        auth.setAuthenticated(false);
		    	return new ReturnStatus(true);        
	        } else {
		    	return new ReturnStatus(false, "tried to logout a wring user");                		        	
	        }
        } else {
	    	return new ReturnStatus(false, "no user was logged in");                	
        }
   }
    
    public ReturnStatus loggedin(String user) {
        System.out.println("loggedin: " + user);
         	
        if (user != null) {
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        if (auth != null) {
		        String currentuser = auth.getName();
		        System.out.println("currentuser = " + currentuser);
		        if (user.equals(currentuser)) {
			        if (auth.isAuthenticated()) {
			            System.out.println("user/currentuser : auth.isAuthenticated() = true");
				        return new ReturnStatus(true,getroles(user));
			        } else {
			            System.out.println("user/currentuser : auth.isAuthenticated() = false");
			        	return new ReturnStatus(false, null);
			        }
		        } else {
		            System.out.println("user != currentuser");
		        	return new ReturnStatus(false, null);		        	
		        }
	        } else {
	            System.out.println("user is NOT loggedin : auth = null");
	        	return new ReturnStatus(false, null);        	
	        }
        } else {
            System.out.println("user is null");
        	return new ReturnStatus(false, null);        	       	
        }
    }
   
    public ReturnStatus register(String email,String userName,String password) {
    	
        System.out.println("register");
        
        if (checkUser(userName)) {        	
    	    return new ReturnStatus(false,"Gebruiker bestaat al. Kies een andere naam.");
        }

        // Valideer correctheid email 
        try {
            javax.mail.internet.InternetAddress ia = new javax.mail.internet.InternetAddress(email);
            ia.validate();
        } catch (javax.mail.internet.AddressException e) {
    	    return new ReturnStatus(false,"email is niet juist formaat (server controlled).");
        }
                
    	User user = new User();
    	user.setEmail(email);
    	user.setUserName(userName);
    	user.setPassword(password);
        System.out.println("create : " + userName );
        
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus txstatus = transactionManager.getTransaction(def);
        
        em.persist(user);
        
        String subject = 	"Accountaanvraag";
        String message = 	"Uw aanvraag voor gebruikersnaam " + userName +  
        					" en wachtwoord " + password +  " is verwerkt.";
        
        String mailstatus = emailBean.sendEmail(email, subject,  message);
               
        if (mailstatus.equals("OK")) {
        	transactionManager.commit(txstatus);
    	    return new ReturnStatus(true, "Mail verzonden");
        } else {
        	transactionManager.rollback(txstatus);
    	    return new ReturnStatus(false, "Fout opgetreden, Mail niet verzonden. " + mailstatus);
        }
    }  

    public ReturnStatus askpassword(String email,String userName) {
    	
        System.out.println("askpassword");
        
    	User usr = null;
    	
        String qry = "select u from User u where u.userName = '" + userName + "'";
        System.out.println("Query = " + qry);
        
        // Valideer correctheid email 
        try {
            javax.mail.internet.InternetAddress ia = new javax.mail.internet.InternetAddress(email);
            ia.validate();
        } catch (javax.mail.internet.AddressException e) {
    	    return new ReturnStatus(false,"email is niet juist formaat (server controlled).");
        }
        
    	List<User> users = em.createQuery(qry,User.class)
    			.setFirstResult(0).setMaxResults(1).getResultList();

        System.out.println("Query uitgevoerd");

    	if (users != null) {
            System.out.println("Er lijkt resultaat");
            if (!users.isEmpty()) {
                System.out.println("Proberen te lezen");
	    		usr = users.get(0);
		        System.out.println("User gevonden : " + usr.getUserName());
		        if (usr.getEmail().equalsIgnoreCase(email)) {
			        System.out.println("email ok");
			        String message = "Uw password voor username " +  userName + " is : " + usr.getPassword();
			        String status = emailBean.sendEmail(email, "password aanvraag",  message);			        
			        if (status.equals("OK")) {        	
			    	    return new ReturnStatus(true, "Mail verzonden");
			        } else {
			    	    return new ReturnStatus(false, "Fout opgetreden in Mail : " + status);
			        }
		        } else {
			        System.out.println("email is niet hetzelfde : " + usr.getEmail() + " en " + email);
	           	    return new ReturnStatus(false,"email is niet hetzelfde : " + usr.getEmail() + " en " + email);
		        }
            } else {
                System.out.println("Toch geen resultaat");
           	    return new ReturnStatus(false,"gebruiker is niet bekend");
            }
    	} else {
	        System.out.println("User niet gevonden");
       	    return new ReturnStatus(false,"gebruiker is niet bekend");
    	}               
    }  

	public ReturnStatus checkrole(String userName,String roleName) {
	    System.out.println("checkrole");
	    boolean rolefound = false;
	    
        String qry = 	"SELECT ur FROM UserRole ur " + 
        				"JOIN FETCH ur.user " + 
        				"JOIN FETCH ur.role " +
        				"WHERE " + 
        				"ur.user.userName = '" + userName + "' AND " +
        				"ur.role.name = '" + roleName + "'";
        		
	    System.out.println("query = " + qry);
        
        List<UserRole> userroles = em.createQuery(qry, UserRole.class).getResultList();

        System.out.println("Query uitgevoerd");

        for (UserRole ur : userroles) {
        	String foundRole = ur.getRole().getName();
        	System.out.println(	" User = " + ur.getUser().getUserName() + 
        						" Role = " + foundRole +
        						" UserRoleId = " + ur.getUserRoleId());
        	if (foundRole.equalsIgnoreCase(roleName)) {
        		rolefound = true;
        	}
        }
        	    
        if (rolefound) {
	    	return new ReturnStatus(true);	        	
        } else {
	    	return new ReturnStatus(false);
        }
	}
	
	public String getroles(String userName) {
		System.out.println("getroles");

		String qry = 	"SELECT ur FROM UserRole ur " + 
						"JOIN FETCH ur.role " +
						"WHERE " + 
						"ur.user.userName = '" + userName + "'";

		System.out.println("query = " + qry);

		List<UserRole> userroles = em.createQuery(qry, UserRole.class).getResultList();

		System.out.println("Query uitgevoerd");
		
		String roles = ""; int i = 0;
		for (UserRole ur : userroles) {
			roles = roles +  ur.getRole().getName();i++;
			if (i < userroles.size()) { roles = roles + ",";}
		}
		System.out.println("roles = " + roles);
		return roles;
	}
	
	public List<GrantedAuthority> getGrantedAuthorities(String userName) {
		System.out.println("getGrantedAuthorities");

		String qry = 	"SELECT ur FROM UserRole ur " + 
						"JOIN FETCH ur.role " +
						"WHERE " + 
						"ur.user.userName = '" + userName + "'";

		System.out.println("query = " + qry);

		List<UserRole> userroles = em.createQuery(qry, UserRole.class).getResultList();

		System.out.println("Query uitgevoerd");

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		for (UserRole ur : userroles) {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + ur.getRole().getName().toUpperCase()));
		}
		return authorities;
	}

    public String[]  decodeToken(String authHeader) {
    	
        String result[] = new String[3];
        result[0] = "ERROR";

        if (authHeader != null) {
            System.out.println("Authorization Header gevonden");
            StringTokenizer st = new StringTokenizer(authHeader);
            if (st.hasMoreTokens()) {
                System.out.println("Tokens gevonden");

         	   String basic = st.nextToken();

               // We only handle HTTP Basic authentication

               if (basic.equalsIgnoreCase("Basic")) {
                  System.out.println("Basic Authenticatie gevonden");
                  String credentials = st.nextToken();
                  System.out.println("Credentials gevonden : " + credentials);
                  try {
 	                 //String userPass =
 	                 //   new String(DatatypeConverter.parseBase64Binary(credentials));
 	
 	                 @SuppressWarnings("restriction")
 					 BASE64Decoder decoder = new BASE64Decoder();
 	                 @SuppressWarnings("restriction")
 					 String userPass = new String(decoder.decodeBuffer(credentials));
 	                 System.out.println("credentials ontcijferd");
 	                 // The decoded string is in the form
 	                 // "userID:password".
 	
 	                 int p = userPass.indexOf(":");
 	                 if (p != -1) {
 	                    System.out.println("username/password correct met ':' gescheiden : " + userPass);            	                 	 
 	
 	                    String userName = userPass.substring(0, p);
 	                    String password = userPass.substring(p+1);
 	                    System.out.println("username = " + userName);            	                 	 
 	                    System.out.println("password = " + password);            	                 	 
 	 	
 	                    if ((!userName.trim().equals("")) &&
 	                        (!password.trim().equals(""))) {
 	                       System.out.println("Request valid"); 
 	                       result[0] = "OK";result[1] = userName;result[2] = password;
 	                       return result;
 	                    } else {
  	                       result[0] = "ERROR";result[1] = "invalid, username of password leeg";result[2]="";System.out.println(result[0] + " " + result[1]);
  	                       return result;
 	                    }
 	                 } else {
                         result[0] = "ERROR";result[1] = "username/password niet correct met : gescheiden";result[2]="";System.out.println(result[0] + " " + result[1]);
                         return result;
                 }
                  } catch(IOException e) {
	                  result[0] = "ERROR";result[1] = "Encyptie niet in het juiste format";result[2]="";System.out.println(result[0] + " " + result[1]);
	                  return result;
                  }
               } else {
                   result[0] = "ERROR";result[1] = "Geen Basic Authenticatie gevonden";result[2]="";System.out.println(result[0] + " " + result[1]);
                   return result;
               }
            } else {
                result[0] = "ERROR";result[1] = "invalid, username of password leeg";result[2]="";System.out.println(result[0] + " " + result[1]);
                return result;
            }
         } else {
              result[0] = "ERROR";result[1] = "Authorization Header niet gevonden";result[2]="";System.out.println(result[0] + " " + result[1]);
             return result;
         }
    }


}
