package com.ftn.security.project.controller;

import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.security.project.certificate.CertificateDTO;
import com.ftn.security.project.certificate.CertificateInfo;
import com.ftn.security.project.exception.BadRequestException;
import com.ftn.security.project.exception.UnauthorizedException;
import com.ftn.security.project.model.ApplicationUser;
import com.ftn.security.project.model.ApplicationUserType;
import com.ftn.security.project.service.CertificateService;

@RestController
@RequestMapping("api/certificates")
public class CertificateController {

	private final CertificateService certificateService;
	private final HttpSession httpSession;
	
	@Autowired
	public CertificateController(CertificateService certificateService, HttpSession httpSession) {
		this.certificateService = certificateService;
		this.httpSession = httpSession;
		System.out.println("Kreiran kontroller");
	}
	
	@GetMapping
	public List<CertificateDTO> getCertificates(@RequestParam(value = "type", required = false, defaultValue = "all") String filter) {		
		if (filter.equals("all")) {
			return certificateService.getAll();
		} else if (filter.equals("ca")) {
			return certificateService.getAllCa();
		} else {
			throw new BadRequestException();
		}
	}
	
	@GetMapping("{serialNumber}")
	public CertificateDTO getCertificate(@PathVariable String serialNumber) {
		return new CertificateDTO(certificateService.getOne(serialNumber));
	}

	@GetMapping("download/{serialNumber}")
	public ResponseEntity<?> downloadCertificate(@PathVariable String serialNumber) {
		String certFile = certificateService.download(serialNumber);

		return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType("application/pkix-cert"))
					.contentLength(certFile.length())
					.body(certFile);
	}
	
	@PostMapping
	public CertificateDTO postCertificate(@RequestBody @Valid CertificateInfo certInfo) {
		ApplicationUser user = (ApplicationUser) httpSession.getAttribute("loggedUser");
		
		if (user == null || user.getUserType() == ApplicationUserType.REGULAR) {
			throw new UnauthorizedException("Only administrators allowed!");
		}
		
		if (certInfo.getExpirationDate().before(new Date())) {
			throw new BadRequestException();
		}

		return certificateService.add(certInfo);
	}
	
	@PostMapping("{serialNumber}/signable")
	public CertificateDTO postCertificateSignable(@PathVariable String serialNumber, @RequestBody CertificateInfo certInfo) {
		if (certInfo.getExpirationDate().before(new Date())) {
			throw new BadRequestException();
		}
		
		ApplicationUser user = (ApplicationUser) httpSession.getAttribute("loggedUser");
		
		if (user == null || user.getUserType() == ApplicationUserType.REGULAR) {
			throw new UnauthorizedException("Only administrators allowed!");
		}
		
		X509Certificate certificate = certificateService.issueCertificate(serialNumber, certInfo, true);
		
		if (certificate == null) {
			throw new BadRequestException();
		}
		
		return new CertificateDTO(certificate);
	}
	
	@PostMapping("{serialNumber}/end")
	public CertificateDTO postCertificateEnd(@PathVariable String serialNumber, @RequestBody CertificateInfo certInfo) {
		if (certInfo.getExpirationDate().before(new Date())) {
			throw new BadRequestException();
		}
		
		X509Certificate certificate = certificateService.issueCertificate(serialNumber, certInfo, false);
		
		if (certificate == null) {
			throw new BadRequestException();
		}
		
		return new CertificateDTO(certificate);
	}
	
	@PutMapping("/revoke/{serialNumber}")
	public void revokeCertificate(@PathVariable String serialNumber) {
		ApplicationUser user = (ApplicationUser) httpSession.getAttribute("loggedUser");
		System.out.println(user);
		if (user == null || user.getUserType() == ApplicationUserType.REGULAR) {
			throw new UnauthorizedException("Only administrators allowed!");
		}
		
		certificateService.revokeCertificate(serialNumber);
	}
	
	@GetMapping("/check/{serialNumber}")
	public void checkCertificate(@PathVariable String serialNumber) {
		boolean check = certificateService.checkValidity(serialNumber);
		
		if (!check) {
			throw new BadRequestException("Certificate not valid!");
		}
	}
}
