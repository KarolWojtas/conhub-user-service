package com.karol.services;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import com.karol.controller.AppUserDetailsController;
import com.karol.model.exceptions.UserNotFoundException;

@Component
public class LinkService {
	public Link linkToAvatar(String username) throws UserNotFoundException {
		return linkTo(methodOn(AppUserDetailsController.class).getAvatarByUsername(username)).withRel("avatar");
	}
}
