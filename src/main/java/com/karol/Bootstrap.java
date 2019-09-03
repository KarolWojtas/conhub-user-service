package com.karol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.karol.model.dto.AppUserDetailsDto;
import com.karol.model.exceptions.UsernameNotUniqueException;
import com.karol.services.interfaces.AppUserDetailsService;
@Component
public class Bootstrap implements CommandLineRunner{
	@Autowired
	private AppUserDetailsService userDetailsService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Override
	public void run(String... args) throws Exception {
		if(userDetailsService.count()==0) {
			this.saveTestUser();
		}
		
	}
	private void saveTestUser() throws UsernameNotUniqueException {
		AppUserDetailsDto user = new AppUserDetailsDto();
		user.setUsername("username");
		user.setPassword("password");
		user.setRoles("ROLE_USER,ROLE_ADMIN");
		userDetailsService.saveUser(user);
	}
}
