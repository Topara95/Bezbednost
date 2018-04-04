package com.ftn.security.project.certificate;

import java.security.PublicKey;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;

public class SubjectData {

	private PublicKey publicKey;
	private X500Name x500name;
	private Date startDate;
	private Date endDate;

	public SubjectData(PublicKey publicKey, X500Name x500name, Date startDate, Date endDate) {
		this.publicKey = publicKey;
		this.x500name = x500name;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public X500Name getX500name() {
		return x500name;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}
	
}
