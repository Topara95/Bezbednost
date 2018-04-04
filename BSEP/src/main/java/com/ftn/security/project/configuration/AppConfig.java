package com.ftn.security.project.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ftn.security.project.certificate.CertificateGenerator;
import com.ftn.security.project.repository.KeyStoreRepository;

@Configuration
public class AppConfig {

	@Bean
	public CertificateGenerator certificateGenerator() {
		return new CertificateGenerator();
	}
	
	@Bean
	public KeyStoreRepository keyStoreRepository() {
		return new KeyStoreRepository();
	}
	
}
