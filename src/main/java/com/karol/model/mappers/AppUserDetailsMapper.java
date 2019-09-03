package com.karol.model.mappers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.GrantedAuthority;

import com.karol.model.domain.AppUserDetails;
import com.karol.model.dto.AppUserDetailsDto;

@Mapper(componentModel="spring")
public interface AppUserDetailsMapper {
	AppUserDetailsMapper INSTANCE =  Mappers.getMapper(AppUserDetailsMapper.class);
	@Mappings({
		@Mapping(source="id", target="id"),
		@Mapping(source="username", target="username"),
		@Mapping(source="roles", target="roles"),
		@Mapping(source="password", target="password"),
		@Mapping(source="githubUsername", target="githubUsername")
	})
	AppUserDetails appUserDetailsDtoToAppUserDetails(AppUserDetailsDto appUserDetailsDto);
	@Mappings({
		@Mapping(source="id", target="id"),
		@Mapping(source="username", target="username"),
		@Mapping(source="roles", target="roles"),
		@Mapping(source="password", target="password",ignore=true),
		@Mapping(source="githubUsername", target="githubUsername")
	})
	AppUserDetailsDto appUserDetailsToAppUserDetailsDto(AppUserDetails appUserDetails);
	
	default List<? extends GrantedAuthority> authoritiesToAuthorities(Collection<? extends GrantedAuthority> collection){
		return collection.stream().collect(Collectors.toList());
	}
}
