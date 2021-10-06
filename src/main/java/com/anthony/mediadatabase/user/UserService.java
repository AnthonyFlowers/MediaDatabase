package com.anthony.mediadatabase.user;

public interface UserService {
	void save(User user);

	User findByUsername(String username);
}
