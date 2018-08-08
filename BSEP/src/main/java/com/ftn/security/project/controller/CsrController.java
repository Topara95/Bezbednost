package com.ftn.security.project.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.validation.Valid;

import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.security.project.certificate.CsrInfo;
import com.ftn.security.project.service.CsrService;

@RestController
@RequestMapping("api/csr")
public class CsrController {

	@Autowired
	private CsrService csrservice;
	
	@PostMapping
	public void makeCsr(@RequestBody @Valid CsrInfo csrInfo) {
		try {
			csrservice.makeCsrRequest(csrInfo);
		} catch (NoSuchAlgorithmException | NoSuchProviderException | OperatorCreationException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
