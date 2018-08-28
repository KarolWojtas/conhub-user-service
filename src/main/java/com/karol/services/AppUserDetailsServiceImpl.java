package com.karol.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.karol.model.domain.AppUserDetails;
import com.karol.services.interfaces.AppUserDetailsService;
import com.karol.services.repositories.AppUserDetailsRepository;
@Service
public class AppUserDetailsServiceImpl implements AppUserDetailsService{
	private AppUserDetailsRepository userRepository;
	@Autowired
	public AppUserDetailsServiceImpl(AppUserDetailsRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Override
	public AppUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return userRepository.findByUsername(username);
	}

	@Override
	public AppUserDetails saveUser(AppUserDetails userDetails) {
		// TODO Auto-generated method stub
		return userRepository.save(userDetails);
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return userRepository.count();
	}

}
