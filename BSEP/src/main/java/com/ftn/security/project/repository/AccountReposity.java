package com.ftn.security.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ftn.security.project.model.ApplicationUser;

@Repository
public interface AccountReposity extends JpaRepository<ApplicationUser, Integer> {

	public ApplicationUser findByUsername(String username);
	
}
