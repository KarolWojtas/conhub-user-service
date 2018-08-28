package com.karol.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.karol.model.domain.AppUserDetails;
import com.karol.services.interfaces.AppUserDetailsService;

@RestController
public class AppUserDetailsController {
	private AppUserDetailsService userService;
	@Autowired
	public AppUserDetailsController(AppUserDetailsService userService) {
		super();
		this.userService = userService;
	}
	@GetMapping("/{username}")
	public AppUserDetails getUserByUsername(@PathVariable String username) {
		return (AppUserDetails) userService.loadUserByUsername(username);
	}
	
	
}
