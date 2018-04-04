package com.ftn.security.project.certificate;

import java.math.BigInteger;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import org.bouncycastle.asn1.x500.AttributeTypeAndValue;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

public class CertificateDTO {

	private String commonName;
	private String givenname;
	private String surname;
	private String organization;
	private String organizationalUnitName;
	private String countryCode;
	private String email;
	private BigInteger serialNumber;
	private CertificateStatus status;
	
	public CertificateDTO(X509Certificate certificate) {
		try {
			X500Name name = new JcaX509CertificateHolder(certificate).getSubject();
			RDN[] rnds = name.getRDNs();

			for (RDN rdn: rnds) {
				AttributeTypeAndValue[] values = rdn.getTypesAndValues();
				for (AttributeTypeAndValue val : values) {
					if (val.getType().equals(BCStyle.CN)) {
						commonName = val.getValue().toString();
					} else if (val.getType().equals(BCStyle.GIVENNAME)) {
						givenname = val.getValue().toString();
					} else if (val.getType().equals(BCStyle.SURNAME)) {
						surname = val.getValue().toString();
					} else if (val.getType().equals(BCStyle.O)) {
						organization = val.getValue().toString();
					} else if (val.getType().equals(BCStyle.OU)) {
						organizationalUnitName = val.getValue().toString();
					} else if (val.getType().equals(BCStyle.C)) {
						countryCode = val.getValue().toString();
					} else if (val.getType().equals(BCStyle.E)) {
						email = val.getValue().toString();
					}
				}
			}

			serialNumber = certificate.getSerialNumber();
		} catch (CertificateEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public BigInteger getSerialNumber() {
		return serialNumber;
	}

	public String getCommonName() {
		return commonName;
	}

	public String getGivenname() {
		return givenname;
	}

	public String getSurname() {
		return surname;
	}

	public String getOrganization() {
		return organization;
	}

	public String getOrganizationalUnitName() {
		return organizationalUnitName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public String getEmail() {
		return email;
	}
	
	public CertificateStatus getStatus() {
		return status;
	}
}
