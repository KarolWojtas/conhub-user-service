package com.karol.services.interfaces;

import java.io.IOException;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

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
	public AppUserDetails getUserByGithubUsername(String githubUsername);
	public AppUserDetailsDto changeUsername(String oldUsername, String newUsername) throws UserNotFoundException, UsernameNotUniqueException;
	public long count();
	public AppUserDetailsDto saveAvatar(MultipartFile imageFile, String username) throws UserNotFoundException, IOException;
	public byte[] getAvatarbyUsername(String username) throws UserNotFoundException;
}
