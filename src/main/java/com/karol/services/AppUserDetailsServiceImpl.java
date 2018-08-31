package com.karol.services;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bouncycastle.crypto.tls.UserMappingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.karol.model.domain.AppUserDetails;
import com.karol.model.domain.Roles;
import com.karol.model.dto.AppUserDetailsDto;
import com.karol.model.exceptions.UserNotFoundException;
import com.karol.model.exceptions.UsernameNotUniqueException;
import com.karol.model.mappers.AppUserDetailsMapper;
import com.karol.services.interfaces.AppUserDetailsService;
import com.karol.services.repositories.AppUserDetailsRepository;
@Service
public class AppUserDetailsServiceImpl implements AppUserDetailsService{
	private AppUserDetailsRepository userRepository;
	private AppUserDetailsMapper userDetailsMapper;
	private PasswordEncoder passwordEncoder;
	@Autowired
	public AppUserDetailsServiceImpl(AppUserDetailsRepository userRepository, AppUserDetailsMapper userDetailsMapper,
			PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.userDetailsMapper = userDetailsMapper;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public AppUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return userRepository.findByUsername(username);
	}

	

	@Override
	public AppUserDetails saveUser(AppUserDetailsDto userDetails) {
		userDetails.setId(null);
		return userRepository.save(userDetailsMapper.appUserDetailsDtoToAppUserDetails(userDetails));
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return userRepository.count();
	}

	@Override
	public AppUserDetailsDto getUserByUsername(String username) throws UserNotFoundException {
		AppUserDetails user = userRepository.findByUsername(username);
		if(user == null) {
			throw new UserNotFoundException("User: "+username+" not found");
		}
		return userDetailsMapper.appUserDetailsToAppUserDetailsDto(user);
	}

	@Override
	public void deleteUserByUsername(String username) {
		userRepository.deleteByUsername(username);
		
	}

	@Override
	public AppUserDetailsDto patchUser(AppUserDetailsDto userDetailsDto) throws UserNotFoundException {
		AppUserDetails savedUser = userRepository.findByUsername(userDetailsDto.getUsername());
		if(savedUser == null) {
			throw new UserNotFoundException("User: "+userDetailsDto.getUsername()+" not found");
		}
		if(!userDetailsDto.getPassword().equals(null)) {
			savedUser.setPassword(passwordEncoder.encode(userDetailsDto.getPassword()));
			
		} 
		if(!userDetailsDto.getRoles().equals(null)) {
			savedUser.setRoles(this.legalRolesFromString(userDetailsDto.getRoles()));
			
		}
		return userDetailsMapper.appUserDetailsToAppUserDetailsDto(userRepository.save(savedUser));
	}
	private String legalRolesFromString(String roles) {
		StringBuilder verifiedRoles = new StringBuilder();
		int index = 1;
		String[] rolesArray = roles.split(",");
		for (String roleString : roles.split(",")) {
			
			verifiedRoles.append(Stream.of(Roles.values()).map(role->role.getValue()).filter(roleValue -> roleValue.equals(roleString))
					.collect(Collectors.joining()));
			if(index<rolesArray.length) {
				verifiedRoles.append(" ");
				index++;
			}
			
		}
		String rolesFromBuilder = verifiedRoles.toString().trim();
		rolesFromBuilder = rolesFromBuilder.replaceAll("\\s+", ",");
		return rolesFromBuilder;
	}

	@Override
	public boolean isUsernameUnique(String username) {
		
		return userRepository.findByUsername(username) == null;
	}

	@Override
	public AppUserDetails getUserByGithubUsername(String githubUsername) throws UserNotFoundException {
		AppUserDetails user = userRepository.findByGithubUsername(githubUsername);
		if(user == null) {
			throw new UserNotFoundException("User: "+githubUsername+" not found");
		}
		return user;
	}

	@Override
	public AppUserDetailsDto changeUsername(String oldUsername, String newUsername) throws UserNotFoundException, UsernameNotUniqueException {
		AppUserDetails savedUser = userRepository.findByUsername(oldUsername);
		if(savedUser == null ) {
			throw new UserNotFoundException();
		}
		if(userRepository.findByUsername(newUsername) != null) {
			throw new UsernameNotUniqueException();
		}
		savedUser.setUsername(newUsername);
		
		return userDetailsMapper.appUserDetailsToAppUserDetailsDto(userRepository.save(savedUser));
	}

}
