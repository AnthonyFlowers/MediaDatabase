package com.anthony.mediadatabase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.anthony.mediadatabase.model.User;
import com.anthony.mediadatabase.service.UserService;

public class UserAuthenticatedController {

	@Autowired
	private UserService userService;
	
	/**
	 * Get the currently authenticated user
	 * 
	 * @return User that is currently authenticated
	 */
	protected User getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByUsername(auth.getName());
		return user;
	}
}
