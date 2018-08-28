package com.karol.services.interfaces;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.karol.model.domain.AppUserDetails;

public interface AppUserDetailsService extends UserDetailsService{
	public AppUserDetails saveUser(AppUserDetails userDetails);
	public long count();
}
