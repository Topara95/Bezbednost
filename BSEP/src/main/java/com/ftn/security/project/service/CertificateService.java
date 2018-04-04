package com.ftn.security.project.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.security.KeyPair;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.asn1.x500.X500Name;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.security.project.certificate.CertificateDTO;
import com.ftn.security.project.certificate.CertificateGenerator;
import com.ftn.security.project.certificate.CertificateInfo;
import com.ftn.security.project.certificate.IssuerData;
import com.ftn.security.project.certificate.SubjectData;
import com.ftn.security.project.exception.BadRequestException;
import com.ftn.security.project.exception.NotFoundException;
import com.ftn.security.project.repository.KeyStoreRepository;
import com.ftn.security.project.utils.CryptoUtil;

@Service
public class CertificateService {

	private final CertificateGenerator certificateGenerator;
	private final KeyStoreRepository repository;
	
	@Autowired
	public CertificateService(CertificateGenerator certificateGenerator, KeyStoreRepository repository) {
		this.certificateGenerator = certificateGenerator;
		this.repository = repository;
	}

	public List<CertificateDTO> getAll() {
		return repository.getCertificates()
			.stream()
			.map(CertificateDTO::new)
			.collect(Collectors.toList());
	}
	
	public List<CertificateDTO> getAllCa() {
		return repository.getCertificates()
			.stream()
			.filter(c -> c.getBasicConstraints() != -1)
			.map(CertificateDTO::new)
			.collect(Collectors.toList());
	}

	public X509Certificate getOne(String serialNumber) {
		return repository.getCertificate(serialNumber)
				.orElseThrow(NotFoundException::new);
	}
	
	public String download(String serialNumber) {
		X509Certificate cert = getOne(serialNumber);
		StringWriter streamWritter = new StringWriter();
		
		try {
			streamWritter.write("-----BEGIN CERTIFICATE-----\n");
			streamWritter.write(DatatypeConverter.printBase64Binary(cert.getEncoded()).replaceAll("(.{64})", "$1\n"));
			streamWritter.write("\n-----END CERTIFICATE-----\n");
		} catch (CertificateEncodingException e) {
			e.printStackTrace();
		}
	   
		return streamWritter.toString();
	}
	
	public CertificateDTO add(CertificateInfo certInfo) {		
		try {
			X500Name x500name = CryptoUtil.generateX500Name(certInfo);
			KeyPair keyPair = CryptoUtil.generateKeyPair();

			SubjectData subjectData = new SubjectData(keyPair.getPublic(), x500name, new Date(), certInfo.getExpirationDate());
			IssuerData issuerData = new IssuerData(keyPair.getPrivate(), x500name);

			X509Certificate certificate = certificateGenerator.generateCertificate(subjectData, issuerData, true);	

			repository.saveCertificate(certificate.getSerialNumber().toString(), keyPair.getPrivate(), certificate);
		
			return new CertificateDTO(certificate);
		} catch(Exception e) {
			throw new BadRequestException();
		}
	}
	
	public X509Certificate issueCertificate(String issuerSerialNumber, CertificateInfo certInfo, boolean isCa) {
		IssuerData issuerData = repository.getIssuerData(issuerSerialNumber);
		
		if (issuerData == null || getOne(issuerSerialNumber).getBasicConstraints() == -1) {
			throw new BadRequestException();
		}

		try {
			KeyPair keyPair = CryptoUtil.generateKeyPair();
			X500Name x500name = CryptoUtil.generateX500Name(certInfo);
			SubjectData subjectData = new SubjectData(keyPair.getPublic(), x500name, new Date(), certInfo.getExpirationDate());
			
			X509Certificate certificate = certificateGenerator.generateCertificate(subjectData, issuerData, isCa);
			
			repository.saveCertificate(certificate.getSerialNumber().toString(), keyPair.getPrivate(), certificate);
			
			return certificate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void revokeCertificate(String serialNumber) {
		X509Certificate certificate = getOne(serialNumber);
		List<X509Certificate> certificates = new ArrayList<>();
		
		try {
			File file = new File("./crl.crl");
			
			if (!file.exists()) {		
				saveCRL(certificates, file);
			} else {
				ObjectInputStream iis = new ObjectInputStream(new FileInputStream(file));
				certificates = (List<X509Certificate>) iis.readObject();
				iis.close();
			}
			
			for (X509Certificate cert : certificates) {
				if (cert.getSerialNumber().equals(certificate.getSerialNumber())) {
					return;
				}
			}
			
			String issuer = certificate.getSubjectX500Principal().getName();
			
			List<X509Certificate> allCertificates = repository.getCertificates();
			
			List<X509Certificate> revokeList = allCertificates
				.stream()
				.filter(c -> c.getIssuerX500Principal().getName().equals(issuer))
				.collect(Collectors.toList());
			
			allCertificates.removeAll(revokeList);	
			recursion(certificates, revokeList, allCertificates);
			
			certificates.add(certificate);
			certificates.addAll(revokeList);
			saveCRL(certificates, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void recursion(List<X509Certificate> certificates, List<X509Certificate> revokeList, List<X509Certificate> allCertificates) {		
		revokeList.forEach(rc -> {
			List<X509Certificate> childRevokeList = allCertificates
				.stream()
				.filter(c -> c.getIssuerX500Principal().getName().equals(rc.getSubjectX500Principal().getName()))
				.collect(Collectors.toList());
			
			certificates.addAll(childRevokeList);
			allCertificates.removeAll(childRevokeList);
			recursion(certificates, childRevokeList, allCertificates);
		});
	}
	
	@SuppressWarnings("unchecked")
	public boolean checkValidity(String serialNumber) {
		X509Certificate certificate = getOne(serialNumber);
		List<X509Certificate> certificates = new ArrayList<>();
		
		File file = new File("./crl.crl");
		
		if (!file.exists()) {
			return true;
		}
		
		try {
			ObjectInputStream iis = new ObjectInputStream(new FileInputStream(file));
			certificates = (List<X509Certificate>) iis.readObject();
			iis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			certificate.checkValidity();
		} catch (CertificateExpiredException | CertificateNotYetValidException  e) {
			System.out.println("NE VALJDA OVDE?");
			return false;
		} 
		
		for (X509Certificate cert : certificates) {
			if (cert.getSerialNumber().equals(certificate.getSerialNumber())) {
				System.out.println("ILI OVDE?");
				return false;
			}
		} 
		
		return true;
	}
	
	private void saveCRL(List<X509Certificate> certificates, File file) throws Exception {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
		oos.writeObject(certificates);
		oos.flush();
		oos.close();
	}
	
}
