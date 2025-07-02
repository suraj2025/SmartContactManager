package com.smart.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.entities.User;
import com.smart.repo.UserRepository;

public class CustomUserDetailService implements UserDetailsService {
	@Autowired
	UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User byEmail = userRepo.findByEmail(username);
		if(byEmail==null) {
			throw new UsernameNotFoundException("There is no user with this username");
		}
		UserDetails d=new CustomUserDetail(byEmail);		
		return d;
	}

}
