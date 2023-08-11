package com.smartcontact.Config;


import java.nio.file.attribute.UserPrincipalNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smartcontact.Entities.User;
import com.smartcontact.Repository.UserRepository;



public class UserDetailsServiceImpl  implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username /* email */) throws UsernameNotFoundException {
                                                     
		User user = userRepository.getUserByUserName(username);
           
		if(user==null) {
			throw new UsernameNotFoundException("Count not found user!!");
		}
		
		UserDetailsImpl userDetailsImpl=new UserDetailsImpl(user);
		
		return userDetailsImpl;
	}

}
