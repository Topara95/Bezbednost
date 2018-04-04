package com.ftn.security.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5261038338291402260L;
	
	public BadRequestException() {
		
	}
	
	public BadRequestException(String message) {
		super(message);
	}

}
