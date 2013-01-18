package org.biz.employees.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

public class CustomAuthenticationProvider implements AuthenticationProvider  {
	
	@Autowired 
	@Qualifier("sec")
	private SecurityService sec;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		System.out.println("CustomAuthenticationProvider");
		if(authentication != null) {
			String userName = authentication.getName();
			Object credentials = authentication.getCredentials();
			if ((userName!=null) && (!userName.isEmpty()) && (credentials!=null)) {
				String password = authentication.getCredentials().toString();
				
				if (sec.checkUser(userName)) {
					if (sec.checkPassword(userName, password)) {
						List<GrantedAuthority> authorities = sec.getGrantedAuthorities(authentication.getName());
						return new UsernamePasswordAuthenticationToken(authentication.getName(), authentication.getCredentials(), authorities);					
					} else {
						return null;
					}
				} else {
					return null;				
				}
			} else {
				return null;
			}
		} else {
			return null;								
		}
	}

	@Override
	public boolean supports(Class<? extends Object> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
