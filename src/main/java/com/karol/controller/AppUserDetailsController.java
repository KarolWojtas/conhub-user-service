package com.karol.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;
import com.karol.model.domain.AppUserDetails;
import com.karol.model.dto.AppUserDetailsDto;
import com.karol.model.exceptions.UserNotFoundException;
import com.karol.model.exceptions.UsernameNotUniqueException;
import com.karol.model.views.Views;
import com.karol.services.interfaces.AppUserDetailsService;

@RestController
@RequestMapping(produces="application/json")
public class AppUserDetailsController {
	private AppUserDetailsService userService;
	@Autowired
	public AppUserDetailsController(AppUserDetailsService userService) {
		super();
		this.userService = userService;
	}
	
	@GetMapping("/auth/{username}")
	public AppUserDetails getUserByUsernameAuth(@PathVariable String username) {
		return (AppUserDetails) userService.loadUserByUsername(username);
	}
	@JsonView(Views.Public.class)
	@GetMapping("/{username}")
	public AppUserDetailsDto getUserByUsername(@PathVariable String username) throws UserNotFoundException {
		
		return userService.getUserByUsername(username);
	}
	@PostMapping("")
	@JsonView(Views.Public.class)
	public ResponseEntity<AppUserDetailsDto> saveUser(@Valid @RequestBody(required=true) AppUserDetailsDto userDto) throws URISyntaxException, UsernameNotUniqueException{
		AppUserDetailsDto savedUser = userService.saveUser(userDto);
		return ResponseEntity.created(new URI("/"+userDto.getUsername())).body(savedUser);
	}
	@DeleteMapping("/{username}")
	public ResponseEntity deleteUser(@PathVariable String username){
		userService.deleteUserByUsername(username);
		return ResponseEntity.accepted().build();
	}
	@PatchMapping("/{username}")
	@JsonView(Views.Public.class)
	public ResponseEntity<AppUserDetailsDto> patchUser(@PathVariable String username, @RequestBody(required=true) AppUserDetailsDto userDto) throws UserNotFoundException{
		AppUserDetailsDto patchedDto = userService.patchUser(userDto, username);
		return ResponseEntity.accepted().body(patchedDto);
	}
	@GetMapping("/checkusername/{username}")
	public ResponseEntity<Boolean> isUsernameUnique(@PathVariable(required=true) String username){
		boolean isUsernameUnique = userService.isUsernameUnique(username);
		System.out.println(isUsernameUnique);
		if(isUsernameUnique) {
			return ResponseEntity.ok(true);
		} else {
			return ResponseEntity.ok(false);
		}
	}
	
	@GetMapping("/auth/githubusername/{githubUsername}")
	public AppUserDetails getUserByGithubUsername(@PathVariable String githubUsername) {
		AppUserDetails user = userService.getUserByGithubUsername(githubUsername);
		return user;
	}
	@GetMapping("/{username}/changeusername/{newusername}")
	@JsonView(Views.Public.class)
	public AppUserDetailsDto changeUsername(@PathVariable String username, @PathVariable String newusername) throws UserNotFoundException, UsernameNotUniqueException {
		return userService.changeUsername(username, newusername);
	}
	@GetMapping(value="/{username}/avatar",produces= {"image/jpeg"})
	public ResponseEntity<byte[]> getAvatarByUsername(@PathVariable String username) throws UserNotFoundException{
		return ResponseEntity.ok(userService.getAvatarbyUsername(username));
	}
	@PostMapping("/{username}/avatar")
	public ResponseEntity<Boolean> saveAvatar(@PathVariable String username, @RequestParam("image") MultipartFile imageFile) throws UserNotFoundException, IOException{
		AppUserDetailsDto dto = userService.saveAvatar(imageFile, username);
		return ResponseEntity.ok(dto!=null);
	}
}
