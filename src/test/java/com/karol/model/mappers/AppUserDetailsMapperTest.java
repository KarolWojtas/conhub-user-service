package com.karol.model.mappers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.karol.model.domain.AppUserDetails;
import com.karol.model.dto.AppUserDetailsDto;

public class AppUserDetailsMapperTest {
	private AppUserDetailsMapper mapper = AppUserDetailsMapper.INSTANCE;
	private AppUserDetails userDetails;
	private AppUserDetailsDto userDetailsDto;
	@Before
	public void setUp() throws Exception {
		userDetails = AppUserDetails.builder().id(1l).username("username").password("pass").roles("ROLE_TEST").build();
		userDetailsDto = AppUserDetailsDto.builder().id(2l).username("username2").password("pass").roles("ROLE_DTO").build();
	}

	@Test
	public void testAppUserDetailsDtoToAppUserDetails() {
		AppUserDetails mappedUser = mapper.appUserDetailsDtoToAppUserDetails(userDetailsDto);
		
		assertEquals(userDetailsDto.getId(), mappedUser.getId());
		assertEquals(userDetailsDto.getUsername(), mappedUser.getUsername());
		assertEquals(userDetailsDto.getRoles(), mappedUser.getRoles());
		assertEquals(userDetails.getPassword(), mappedUser.getPassword());	}

	@Test
	public void testAppUserDetailsToAppUserDetailsDto() {
		AppUserDetailsDto dto = mapper.appUserDetailsToAppUserDetailsDto(userDetails);
		assertEquals(userDetails.getId(), dto.getId());
		assertEquals(userDetails.getUsername(), dto.getUsername());
		assertEquals(userDetails.getRoles(), dto.getRoles());
		assertNull(dto.getPassword());
	}

}
