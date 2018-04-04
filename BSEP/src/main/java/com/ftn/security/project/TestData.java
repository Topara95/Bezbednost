package com.ftn.security.project;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ftn.security.project.model.ApplicationUser;
import com.ftn.security.project.model.ApplicationUserDTO;
import com.ftn.security.project.model.ApplicationUserType;
import com.ftn.security.project.repository.AccountReposity;
import com.ftn.security.project.service.AccountService;
import com.ftn.security.project.utils.CryptoUtil;

@Component
public class TestData {
	
	@Autowired
	private AccountService accservice;
	
	@Autowired
	private AccountReposity accrepository;
	
	@PostConstruct
	private void init(){
		
		ApplicationUserDTO au1 = new ApplicationUserDTO("jova","jova");
		accservice.registerAccount(au1);
		
		ApplicationUserDTO au2 = new ApplicationUserDTO("boki","boki");
		accservice.registerAccount(au2);
		
		//moze se lepse napisati
		ApplicationUserDTO au3 = new ApplicationUserDTO("dino","dino");
		byte[] salt = CryptoUtil.generateSalt();
		byte[] passwordHash = CryptoUtil.hashPassword(au3.getPassword(), salt);
		
		ApplicationUser user = new ApplicationUser(au3.getUsername(), passwordHash, salt, ApplicationUserType.ADMIN);
		accrepository.save(user);
		
	}
}
