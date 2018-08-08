package com.ftn.security.project.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.ContentVerifierProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.PKCSException;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.springframework.stereotype.Service;

import com.ftn.security.project.certificate.CsrInfo;
import com.ftn.security.project.utils.CryptoUtil;


@Service
public class CsrService {

	
	public void makeCsrRequest(CsrInfo csrInfo) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, OperatorCreationException {
		X500Name x500name = CryptoUtil.generateX500NameCsr(csrInfo);
		
		KeyPair pair = CryptoUtil.generateKeyPair();
		PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(
				x500name, pair.getPublic());
		JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256withRSA");
		ContentSigner signer = csBuilder.build(pair.getPrivate());
		PKCS10CertificationRequest csr = p10Builder.build(signer);
		
		String rootPath = System.getProperty("user.dir");
		File f = new File(rootPath + "/src/main/resources/csr/" + csrInfo.getGivenname() + csrInfo.getSurname() + ".csr");
		//f.createNewFile();
		
		BufferedWriter w = new BufferedWriter(new FileWriter(f.getPath()));
		StringWriter sw = new StringWriter();
		JcaPEMWriter pemWriter = new JcaPEMWriter(sw); 
		pemWriter.writeObject(csr);
		pemWriter.close();
			
		sw.close();
		w.write(sw.toString());
		w.flush();
		w.close();

	}
	
	public boolean readcsr(File f) throws IOException, InvalidKeyException, NoSuchAlgorithmException, OperatorCreationException, PKCSException {
		byte[] bFile = new byte[(int) f.length()];
		FileInputStream fis;
		boolean valid = false;
		fis = new FileInputStream(f);
		fis.read(bFile);
		fis.close();
		String temp = new String(bFile).replace("-----BEGIN NEW CERTIFICATE REQUEST-----", "");
	      String temp2 = temp.replace("-----END NEW CERTIFICATE REQUEST-----", "").trim();
	      StringReader sr = new StringReader(temp2);
	      PEMParser pp = new PEMParser(sr);
	      Object parsedObj = pp.readObject();
	      if (parsedObj instanceof PKCS10CertificationRequest){
		    JcaPKCS10CertificationRequest jcaPKCS10CertificationRequest = new JcaPKCS10CertificationRequest((PKCS10CertificationRequest)parsedObj);	          
		    X500Name x500name = jcaPKCS10CertificationRequest.getSubject();
			PublicKey publicKey = jcaPKCS10CertificationRequest.getPublicKey();
			ContentVerifierProvider contentVerifierProvider = new JcaContentVerifierProviderBuilder().setProvider("BC").build(publicKey);
			valid = jcaPKCS10CertificationRequest.isSignatureValid(contentVerifierProvider);
			}
	     return valid; 
	}

	
}
