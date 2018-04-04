package com.ftn.security.project.certificate;

import java.security.PrivateKey;

import org.bouncycastle.asn1.x500.X500Name;

public class IssuerData {

	private X500Name x500name;
	private PrivateKey privateKey;

	public IssuerData(PrivateKey privateKey, X500Name x500name) {
		this.privateKey = privateKey;
		this.x500name = x500name;
	}

	public X500Name getX500name() {
		return x500name;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

}
