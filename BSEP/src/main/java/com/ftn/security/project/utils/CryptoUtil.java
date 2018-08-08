package com.ftn.security.project.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;
import java.util.UUID;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;

import com.ftn.security.project.certificate.CertificateInfo;
import com.ftn.security.project.certificate.CsrInfo;

public class CryptoUtil {

	private static final Random RANDOM = new SecureRandom();
	private static final int ITERATIONS = 10000;
	private static final int KEY_LENGTH = 256;
	
    public static X500Name generateX500Name(CertificateInfo certInfo) {
        return new X500NameBuilder(BCStyle.INSTANCE)
			.addRDN(BCStyle.CN, certInfo.getCommonName())
			.addRDN(BCStyle.SURNAME, certInfo.getSurname())
			.addRDN(BCStyle.GIVENNAME, certInfo.getGivenname())
			.addRDN(BCStyle.O, certInfo.getOrganization())
			.addRDN(BCStyle.OU, certInfo.getOrganizationalUnitName())
			.addRDN(BCStyle.C, certInfo.getCounrtyCode())
			.addRDN(BCStyle.E, certInfo.getEmail())
			.addRDN(BCStyle.UID, UUID.randomUUID().toString())
			.build();
    }
    
    public static X500Name generateX500NameCsr(CsrInfo csrInfo) {
        return new X500NameBuilder(BCStyle.INSTANCE)
			.addRDN(BCStyle.CN, csrInfo.getCommonName())
			.addRDN(BCStyle.SURNAME, csrInfo.getSurname())
			.addRDN(BCStyle.GIVENNAME, csrInfo.getGivenname())
			.addRDN(BCStyle.O, csrInfo.getOrganization())
			.addRDN(BCStyle.OU, csrInfo.getOrganizationalUnitName())
			.addRDN(BCStyle.C, csrInfo.getCountryCode())
			.addRDN(BCStyle.E, csrInfo.getEmail())
			.addRDN(BCStyle.UID, UUID.randomUUID().toString())
			.build();
    }
    
    public static byte[] generateSalt() {
		byte[] salt = new byte[16];
	    RANDOM.nextBytes(salt);
	    return salt;
	}
    
    public static byte[] hashPassword(String password, byte[] salt) {
		PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
		
	    try {
	      SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	      return skf.generateSecret(spec).getEncoded();
	    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
	      throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
	    } finally {
	      spec.clearPassword();
	    }
	}
    
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA"); 
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
		keyGen.initialize(2048, random);
		return keyGen.generateKeyPair();
    }

}