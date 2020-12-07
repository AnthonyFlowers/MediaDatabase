package com.anthony.mediadatabase.service;

/**
 * Adapted from
 * https://github.com/hellokoding/hellokoding-courses/tree/master/springboot-examples/springboot-registration-login/src/main/java/com/hellokoding/auth/service
 *
 */
public interface SecurityService {
	String findLoggedInUsername();

	void autoLogin(String username, String password);
}
