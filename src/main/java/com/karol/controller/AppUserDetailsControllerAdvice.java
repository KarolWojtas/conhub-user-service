package com.karol.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.karol.model.exceptions.UserNotFoundException;
import com.karol.model.exceptions.UsernameNotUniqueException;

@RestControllerAdvice
public class AppUserDetailsControllerAdvice {
	@ExceptionHandler(value= {UserNotFoundException.class})
	public ResponseEntity handleUserNotFound() {
		return ResponseEntity.notFound().build();
	}
	@ExceptionHandler(value= {UsernameNotUniqueException.class})
	public ResponseEntity handleUsernamenotUnique() {
		return ResponseEntity.badRequest().build();
	}
}
