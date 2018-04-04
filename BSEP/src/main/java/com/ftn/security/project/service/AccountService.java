package com.ftn.security.project.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.security.project.exception.BadRequestException;
import com.ftn.security.project.model.ApplicationUser;
import com.ftn.security.project.model.ApplicationUserDTO;
import com.ftn.security.project.model.ApplicationUserType;
import com.ftn.security.project.repository.AccountReposity;
import com.ftn.security.project.utils.CryptoUtil;

@Service
public class AccountService {

	private final AccountReposity accountReposity;
	
	@Autowired
	public AccountService(AccountReposity accountReposity) {
		this.accountReposity = accountReposity;
	}
	
	public void registerAccount(ApplicationUserDTO applicationUserDTO) {
		byte[] salt = CryptoUtil.generateSalt();
		byte[] passwordHash = CryptoUtil.hashPassword(applicationUserDTO.getPassword(), salt);
		
		ApplicationUser user = new ApplicationUser(applicationUserDTO.getUsername(), passwordHash, salt, ApplicationUserType.REGULAR);
		
		try {			
			accountReposity.save(user);
		} catch(Exception e) {
			throw new BadRequestException("Username already exists!");
		}
	}
	
	public ApplicationUser getAccount(ApplicationUserDTO userDTO) {
		ApplicationUser user = accountReposity.findByUsername(userDTO.getUsername());
		
		if (user == null) {
			throw new BadRequestException("Username or password not valid!");
		}
		
		byte[] pwdHash = CryptoUtil.hashPassword(userDTO.getPassword(), user.getPasswordSalt());
		
	    if (!Arrays.equals(pwdHash, user.getPasswordHash())) {
	    	throw new BadRequestException("Username or password not valid!");
	    }
		
		return user;
	}
	
}
