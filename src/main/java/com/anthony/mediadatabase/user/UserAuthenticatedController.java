package com.anthony.mediadatabase.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
