package com.karol.model.dto;

import java.time.Instant;
import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import com.fasterxml.jackson.annotation.JsonView;
import com.karol.model.views.Views;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDetailsDto{
	@JsonView(Views.Public.class)
	private Long id;
	@JsonView(Views.Public.class)
	private String username;
	@JsonView(Views.Private.class)
	private String password;
	@JsonView(Views.Private.class)
	private String roles;
	@JsonView(Views.Private.class)
	private String githubUsername;
	@JsonView(Views.Private.class)
	private Instant accountCreated;
	
}
