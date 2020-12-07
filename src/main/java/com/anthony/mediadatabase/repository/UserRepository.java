package com.anthony.mediadatabase.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anthony.mediadatabase.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	User findByUsername(String username);
}
