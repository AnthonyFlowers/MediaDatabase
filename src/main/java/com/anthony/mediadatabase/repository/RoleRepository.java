package com.anthony.mediadatabase.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anthony.mediadatabase.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
}
