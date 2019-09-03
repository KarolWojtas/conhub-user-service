package com.karol.services;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Link;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.karol.model.domain.AppUserDetails;
import com.karol.model.domain.Roles;
import com.karol.model.dto.AppUserDetailsDto;
import com.karol.model.exceptions.UserNotFoundException;
import com.karol.model.exceptions.UsernameNotUniqueException;
import com.karol.model.mappers.AppUserDetailsMapper;
import com.karol.services.interfaces.AppUserDetailsService;
import com.karol.services.repositories.AppUserDetailsRepository;
@DataJpaTest
@RunWith(SpringRunner.class)
public class AppUserDetailsServiceImplITTest {
	@Autowired
	private AppUserDetailsRepository repository;
	@Autowired
	private TestEntityManager entityManager;
	AppUserDetailsDto userDto;
	AppUserDetailsService service;
	AppUserDetailsMapper mapper = AppUserDetailsMapper.INSTANCE;
	AppUserDetails userDetails; 
	PasswordEncoder encoder = new BCryptPasswordEncoder();
	Long savedUserDetailsId;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		service = new AppUserDetailsServiceImpl(repository, mapper, encoder);
		userDto = AppUserDetailsDto.builder().id(2l).username("username").password("password").roles(Roles.USER.getValue()).build();
		userDetails = AppUserDetails.builder().username("user").password("passwordUser").roles(Roles.ADMIN.getValue()).build();
		this.savedUserDetailsId = (Long) entityManager.persistAndGetId(userDetails);
	}

	@Test
	public void testSaveUser() throws UsernameNotUniqueException {
		AppUserDetailsDto savedUser = service.saveUser(userDto);
		
		assertEquals(userDto.getUsername(), savedUser.getUsername());
		assertNotNull(savedUser.getId());
		assertEquals(userDto.getRoles(), savedUser.getRoles());
	}

	@Test
	public void testDeleteUserByUsername() {
		
		
		service.deleteUserByUsername("user");
		
		assertThat(repository.count(), is(equalTo(0l)));
	}

	@Test
	public void testPatchUser() throws UserNotFoundException {
		
		userDto.setUsername("user");
		
		AppUserDetailsDto patchedDto = service.patchUser(userDto,"user");
		assertEquals(userDto.getRoles(), patchedDto.getRoles());
		
	}
	@Test
	public void testPatchUserCondition2() throws UserNotFoundException {
		
		userDto.setUsername("user");
		userDto.setRoles("ROLE_ADMIN,ROLE_TEST,KJASD,ROLE_USER");
		
		AppUserDetailsDto patchedDto = service.patchUser(userDto, "user");
		assertEquals("ROLE_ADMIN,ROLE_USER", patchedDto.getRoles());
		assertNotEquals(userDto.getId(), patchedDto.getId());
	}
	@Test
	public void testChangePasswordWithPatch() throws UserNotFoundException {
		String newPassword ="newpassword";
		
		AppUserDetailsDto dto = new AppUserDetailsDto();
		dto.setPassword(newPassword);
		service.patchUser(dto, "user");
		
		AppUserDetails changedUser = entityManager.find(AppUserDetails.class, this.savedUserDetailsId);
		
		assertTrue(encoder.matches("newpassword", changedUser.getPassword()));
	}
	@Test
	public void testIsUsernameUnique() {
		
		
		boolean shouldBeNotUnique = service.isUsernameUnique("user");
		boolean shouldBeUnique = service.isUsernameUnique("uniqueusername");
		
		assertFalse(shouldBeNotUnique);
		assertTrue(shouldBeUnique);
	}
	@Test
	public void testChangeUsername() throws UserNotFoundException, UsernameNotUniqueException {
		
		
		AppUserDetailsDto changedDto = service.changeUsername("user", "newusername");
		
		assertEquals(userDetails.getId(), changedDto.getId());
		assertEquals(userDetails.getRoles(), changedDto.getRoles());
		assertEquals("newusername", changedDto.getUsername());
	}
	@Test(expected=UserNotFoundException.class)
	public void testChangeUsernameNotFound() throws UserNotFoundException, UsernameNotUniqueException {
		entityManager.remove(userDetails);
		AppUserDetailsDto changedDto = service.changeUsername("user", "newusername");
	}
	@Test(expected=UsernameNotUniqueException.class)
	public void testChangeUsernameNotUnigue() throws UserNotFoundException, UsernameNotUniqueException {
		AppUserDetails user2 = AppUserDetails.builder().username("user2").password("password").roles("ROLE_TEST").build();
		
		entityManager.persist(user2);
		
		AppUserDetailsDto changedUser = service.changeUsername("user2", "user");
		
		
	}
	@Test
	public void testSaveAvatar() throws UserNotFoundException, IOException {
		try (FileInputStream fis = new FileInputStream(new File("image.jpg"))){
			MockMultipartFile imageFile = new MockMultipartFile("file1", "image.png", "image/png", fis);
			AppUserDetailsDto dtoWithAvatar = service.saveAvatar(imageFile, userDetails.getUsername());
		
			assertNotNull(dtoWithAvatar);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	

}
