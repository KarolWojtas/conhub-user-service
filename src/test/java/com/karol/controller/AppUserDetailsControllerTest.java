package com.karol.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.karol.model.domain.AppUserDetails;
import com.karol.model.domain.Roles;
import com.karol.model.dto.AppUserDetailsDto;
import com.karol.model.exceptions.UserNotFoundException;
import com.karol.model.exceptions.UsernameNotUniqueException;
import com.karol.model.mappers.AppUserDetailsMapper;
import com.karol.services.AppUserDetailsServiceImpl;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@RunWith(SpringRunner.class)
@WebMvcTest(AppUserDetailsController.class)
public class AppUserDetailsControllerTest {
	@Autowired
	MockMvc mockMvc;
	@MockBean
	private AppUserDetailsServiceImpl userService;
	@InjectMocks
	private AppUserDetailsController controller;
	private AppUserDetails userDetails;
	private AppUserDetailsMapper mapper = AppUserDetailsMapper.INSTANCE;
	private ObjectMapper jsonMapper;
	private AppUserDetailsDto userDto;
	@Before
	public void setUp() throws Exception {
		userDetails = AppUserDetails.builder().id(2l).username("username").password("password").githubUsername("gtusr").roles(Roles.ADMIN.getValue()).build();
		jsonMapper = new ObjectMapper();
		userDto = AppUserDetailsDto.builder().id(2l).username("username").password("password").githubUsername("github").roles(Roles.ADMIN.getValue()).build();
	}

	@Test
	public void testGetUserByUsernameAuth() throws Exception {
		given(userService.loadUserByUsername(anyString())).willReturn(userDetails);
		
		mockMvc.perform(get("/auth/username"))
			.andExpect(status().isOk());
	}

	@Test
	public void testGetUserByUsername() throws Exception {
		given(userService.getUserByUsername(anyString())).willReturn(mapper.appUserDetailsToAppUserDetailsDto(userDetails));
		
		mockMvc.perform(get("/username"))
			.andExpect(status().isOk());
	}
	@Test
	public void testSaveUser() throws JsonProcessingException, Exception {
		given(userService.saveUser(any())).willReturn(userDto);
		
		mockMvc.perform(post("").contentType(MediaType.APPLICATION_JSON).content(jsonMapper.writeValueAsString(userDto)))
			.andExpect(status().isCreated());
	}
	@Test
	public void patchUser() throws JsonProcessingException, Exception {
		given(userService.patchUser(any(AppUserDetailsDto.class), anyString())).willReturn(userDto);
		
		mockMvc.perform(patch("/username").contentType(MediaType.APPLICATION_JSON).content(jsonMapper.writeValueAsString(userDto)))
		 .andExpect(status().isAccepted());
	}
	@Test
	public void testCheckUsernameNotUnique() throws Exception {
		given(userService.isUsernameUnique(anyString())).willReturn(false);
		
		mockMvc.perform(get("/auth/checkusername/notunique"))
			.andExpect(status().isOk());
	}
	@Test
	public void testCheckUsernameUnique() throws Exception {
		given(userService.isUsernameUnique(anyString())).willReturn(true);
		
		mockMvc.perform(get("/auth/checkusername/notunique"))
			.andExpect(status().isOk());
	}
	@Test
	public void testFindGyGithubUsername() throws Exception {
		given(userService.getUserByGithubUsername(anyString())).willReturn(userDetails);
		
		mockMvc.perform(get("/auth/githubusername/githubusr"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.githubUsername", is("gtusr")));
	}
	@Test
	public void testFindByGithubNull() throws Exception {
		given(userService.getUserByGithubUsername(anyString())).willReturn(null);
		
		mockMvc.perform(get("/auth/githubusername/githubusr"))
		 .andExpect(status().isOk());
	}
	@Test
	public void testChangeUsername() throws Exception {
		given(userService.changeUsername(anyString(), anyString())).willReturn(userDto);
		
		mockMvc.perform(get("/username/changeusername/newusername"))
			.andExpect(status().isOk());
	}

}
