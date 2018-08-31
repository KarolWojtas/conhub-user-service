package com.karol.services;

import static org.junit.Assert.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import com.karol.model.domain.AppUserDetails;
import com.karol.model.domain.Roles;
import com.karol.model.dto.AppUserDetailsDto;
import com.karol.model.exceptions.UserNotFoundException;
import com.karol.model.mappers.AppUserDetailsMapper;
import com.karol.services.interfaces.AppUserDetailsService;
import com.karol.services.repositories.AppUserDetailsRepository;

import org.junit.Before;
import org.junit.Test;

public class AppUserDetailsServiceImplTest {
	@Mock
	AppUserDetailsRepository userRepository;
	AppUserDetailsService service;
	AppUserDetails userDetails;
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		service = new AppUserDetailsServiceImpl(userRepository, AppUserDetailsMapper.INSTANCE, new BCryptPasswordEncoder());
		userDetails = AppUserDetails.builder().id(2l).username("username").githubUsername("githubUsername").password("password").roles(Roles.USER.getValue()).build();
	}

	@Test
	public void testLoadUserByUsername() {
		given(userRepository.findByUsername(anyString())).willReturn(userDetails);
		
		UserDetails savedUser = service.loadUserByUsername("username");
		assertEquals(userDetails.getUsername(), savedUser.getUsername());
		assertEquals(userDetails.getPassword(), savedUser.getPassword());
		assertEquals(userDetails.getAuthorities(), savedUser.getAuthorities());
		
	}

	@Test
	public void testCount() {
		given(userRepository.count()).willReturn(3l);
		
		long count = service.count();
		assertEquals(3l, count);
	}

	@Test
	public void testGetUserbyUsername() throws UserNotFoundException {
		given(userRepository.findByUsername(anyString())).willReturn(userDetails);
		
		AppUserDetailsDto userDto = service.getUserByUsername("username");
		assertEquals(userDetails.getUsername(), userDto.getUsername());
		assertEquals(userDetails.getId(), userDto.getId());
		assertEquals(userDetails.getRoles(), userDto.getRoles());
		assertNull(userDto.getPassword());
	}
	
	@Test
	public void testGetByGithubUsername() throws UserNotFoundException {
		given(userRepository.findByGithubUsername(anyString())).willReturn(userDetails);
		
		AppUserDetails savedUser = service.getUserByGithubUsername("githubusername");
		
		assertEquals(userDetails.getGithubUsername(), savedUser.getGithubUsername());
	}

}
