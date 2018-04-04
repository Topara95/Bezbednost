package com.ftn.security.project.model;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ApplicationUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(unique = true)
	private String username;
	
	@Column(nullable = false)
	private byte[] passwordHash;
	
	@Column(nullable = false)
	private byte[] passwordSalt;
	
	@Column(nullable = false)
	private ApplicationUserType userType;
	
	public ApplicationUser() {
		
	}
	
	public ApplicationUser(String username, byte[] passwordHash, byte[] passwordSalt, ApplicationUserType userType) {
		this.username = username;
		this.passwordHash = passwordHash;
		this.passwordSalt = passwordSalt;
		this.userType = userType;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public byte[] getPasswordHash() {
		return passwordHash;
	}
	
	public void setPasswordHash(byte[] passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	public byte[] getPasswordSalt() {
		return passwordSalt;
	}
	
	public void setPasswordSalt(byte[] passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	public ApplicationUserType getUserType() {
		return userType;
	}

	public void setUserType(ApplicationUserType userType) {
		this.userType = userType;
	}

	@Override
	public String toString() {
		return "ApplicationUser [id=" + id + ", username=" + username + ", passwordHash="
				+ Arrays.toString(passwordHash) + ", passwordSalt=" + Arrays.toString(passwordSalt) + ", userType="
				+ userType + "]";
	}
	
}
