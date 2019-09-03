package com.karol.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.karol.controller.AppUserDetailsController;
import com.karol.model.domain.AppUserDetails;
import com.karol.model.domain.Roles;
import com.karol.model.dto.AppUserDetailsDto;
import com.karol.model.exceptions.UserNotFoundException;
import com.karol.model.exceptions.UsernameNotUniqueException;
import com.karol.model.mappers.AppUserDetailsMapper;
import com.karol.services.interfaces.AppUserDetailsService;
import com.karol.services.repositories.AppUserDetailsRepository;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
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
	
	public AppUserDetailsDto saveUser(AppUserDetailsDto userDetails) throws UsernameNotUniqueException {
		userDetails.setId(null);
		userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));
		if(userDetails.getAccountCreated()==null) {
			userDetails.setAccountCreated(Instant.now());
		}
		AppUserDetails savedUser =  userRepository.save(userDetailsMapper.appUserDetailsDtoToAppUserDetails(userDetails));
		if(savedUser == null) {
		  throw new UsernameNotUniqueException();
		}
		return userDetailsMapper.appUserDetailsToAppUserDetailsDto(savedUser);
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
	@Transactional
	public void deleteUserByUsername(String username) {
		userRepository.deleteByUsername(username);
		
	}

	@Override
	
	public AppUserDetailsDto patchUser(AppUserDetailsDto userDetailsDto, String username) throws UserNotFoundException {
		AppUserDetails savedUser = userRepository.findByUsername(username);
		if(savedUser == null) {
			throw new UserNotFoundException("User: "+userDetailsDto.getUsername()+" not found");
		}
		if(userDetailsDto.getPassword()!=null) {
			savedUser.setPassword(passwordEncoder.encode(userDetailsDto.getPassword()));
			
		} 
		if(userDetailsDto.getRoles()!=null) {
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
	public AppUserDetails getUserByGithubUsername(String githubUsername) {
		AppUserDetails user = userRepository.findByGithubUsername(githubUsername);
		if(user == null) {
			return null;
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

	@Override
	public AppUserDetailsDto saveAvatar(MultipartFile imageFile, String username) throws UserNotFoundException, IOException {
		AppUserDetails savedUser = userOrThrowError(username);
		//savedUser.setAvatar(imageFile.getBytes());
		savedUser.setAvatar(compressImage(imageFile, 0.5f));
		AppUserDetailsDto dtoWithAvatar = userDetailsMapper.appUserDetailsToAppUserDetailsDto(userRepository.save(savedUser));
		return dtoWithAvatar;
	}
	private byte[] compressImage(MultipartFile file, float compressionQuality) {
		byte[] imageCompressed = null;
		try (ByteArrayOutputStream os = new ByteArrayOutputStream() ; ImageOutputStream ios = ImageIO.createImageOutputStream(os)){
			BufferedImage image = ImageIO.read(file.getInputStream());
			System.out.println(image == null);
			ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("jpg").next();
			imageWriter.setOutput(ios);
			ImageWriteParam param = imageWriter.getDefaultWriteParam();
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(compressionQuality);
			imageWriter.write(null, new IIOImage(image, null, null), param);
			imageCompressed = os.toByteArray();
			
			
			imageWriter.dispose();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return imageCompressed;
	}

	@Override
	public byte[] getAvatarbyUsername(String username) throws UserNotFoundException {
		return userOrThrowError(username).getAvatar();
	}
	private AppUserDetails userOrThrowError(String username) throws UserNotFoundException {
		AppUserDetails savedUser = userRepository.findByUsername(username);
		if(savedUser == null) {
			throw new UserNotFoundException();
		} else {
			return savedUser;
		}
	}

}
