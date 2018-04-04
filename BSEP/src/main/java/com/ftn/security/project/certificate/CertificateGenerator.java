package com.ftn.security.project.certificate;

import java.math.BigInteger;
import java.security.cert.X509Certificate;

import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import com.ftn.security.project.repository.KeyStoreRepository;

public class CertificateGenerator {

	private static long serialNumber = 0;
	
	public CertificateGenerator() {
		serialNumber = new KeyStoreRepository().getCertificates().size();
	}
	
	public X509Certificate generateCertificate(SubjectData subject, IssuerData issuer, boolean isCa) throws Exception {
		ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256WithRSAEncryption")
			.setProvider("BC")
			.build(issuer.getPrivateKey());
		
		++serialNumber;
		
		GeneralName location = new GeneralName(GeneralName.uniformResourceIdentifier, new DERIA5String("http://localhost:8080/api/certificate/" + serialNumber));
		
		X509CertificateHolder certHolder = new JcaX509v3CertificateBuilder(
			issuer.getX500name(),
			new BigInteger(Long.toString(serialNumber)),
			subject.getStartDate(),
			subject.getEndDate(),
			subject.getX500name(),
			subject.getPublicKey())
			.addExtension(Extension.basicConstraints, true, new BasicConstraints(isCa))
			.addExtension(Extension.authorityInfoAccess, false, location)
			.build(contentSigner);
		
		return new JcaX509CertificateConverter()
			.setProvider("BC")
			.getCertificate(certHolder);
	}
}
