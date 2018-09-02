package com.karol.services.interfaces;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.karol.model.domain.AppUserDetails;
import com.karol.model.dto.AppUserDetailsDto;
import com.karol.model.exceptions.UserNotFoundException;
import com.karol.model.exceptions.UsernameNotUniqueException;

public interface AppUserDetailsService extends UserDetailsService{
	public AppUserDetailsDto saveUser(AppUserDetailsDto userDetails) throws UsernameNotUniqueException;
	public AppUserDetailsDto getUserByUsername(String username) throws UserNotFoundException;
	public void deleteUserByUsername(String username);
	public AppUserDetailsDto patchUser(AppUserDetailsDto userDetailsDto, String username) throws UserNotFoundException;
	public boolean isUsernameUnique(String username);
	public AppUserDetails getUserByGithubUsername(String githubUsername) throws UserNotFoundException;
	public AppUserDetailsDto changeUsername(String oldUsername, String newUsername) throws UserNotFoundException, UsernameNotUniqueException;
	public long count();
}
