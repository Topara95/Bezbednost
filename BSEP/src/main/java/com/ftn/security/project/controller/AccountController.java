package com.ftn.security.project.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.security.project.exception.BadRequestException;
import com.ftn.security.project.model.ApplicationUser;
import com.ftn.security.project.model.ApplicationUserDTO;
import com.ftn.security.project.service.AccountService;

@RestController
@RequestMapping("api/account")
public class AccountController {

	private final AccountService accountService;
	private final HttpSession httpSession;
	
	@Autowired
	public AccountController(AccountService accountService, HttpSession httpSession) {
		this.accountService = accountService;
		this.httpSession = httpSession;
	}
	
	@PostMapping("register")
	public void register(@Valid @RequestBody ApplicationUserDTO applicationUser) {
		accountService.registerAccount(applicationUser);
	}
	
	@PostMapping("login")
	public void login(@Valid @RequestBody ApplicationUserDTO applicationUser, HttpServletRequest request) {
		ApplicationUser user = accountService.getAccount(applicationUser);
		
		if (user != null) {
			httpSession.setAttribute("loggedUser", user);
		} else {
			throw new BadRequestException();
		}
	}
	
	@PostMapping("logut")
	public void logout(HttpServletRequest request) {
		httpSession.invalidate();
	}
	
	@GetMapping("/isLogged")
	public ApplicationUser isLoggedIn() {
		ApplicationUser user = (ApplicationUser) httpSession.getAttribute("loggedUser");
		if(user != null) {
			return user;
		}else {
			return null;	
		}
	}
	
}
