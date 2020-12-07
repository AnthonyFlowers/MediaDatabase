package com.anthony.mediadatabase.service;

import com.anthony.mediadatabase.model.User;

public interface UserService {
	void save(User user);
	
	User findByUsername(String username);
}
