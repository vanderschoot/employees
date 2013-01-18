package org.biz.employees.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {
	@Autowired 
	@Qualifier("sec")
	private SecurityService sec;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        
		System.out.println("CustomUserDetailsService: " + userName);
		String password = sec.getPassword(userName);		
		
		if (password != null) {
			List<GrantedAuthority> authorities = sec.getGrantedAuthorities(userName);

			return new User(userName,
	                password,
	                authorities);        
		} else {
            throw new UsernameNotFoundException("User not found: " + userName);			
		}
    }
}
