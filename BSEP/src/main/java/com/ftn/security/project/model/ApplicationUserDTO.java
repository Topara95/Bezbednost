package com.ftn.security.project.model;

import javax.validation.constraints.NotNull;

public class ApplicationUserDTO {

	@NotNull
	private String username;
	@NotNull
	private String password;
	
	
	public ApplicationUserDTO() {
		
	}
	
	public ApplicationUserDTO(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
}
