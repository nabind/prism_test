package com.drc.aes;

import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.drc.util.ApplicationConstants;


public class AESEncryptionDecryption {	
	
	private byte[] AES_Encrypt(byte[] bytesToBeEncrypted,byte[] passwordBytes) throws Exception{

		byte[] saltBytes = new byte[] { 13,25,73,14,115,-30,97,8 };	
		
		RFC2898DeriveBytes secretKey = new RFC2898DeriveBytes(passwordBytes, saltBytes, 1000);
		byte[] key = secretKey.getBytes(32);
		byte[] iv = secretKey.getBytes(16);
		
		SecretKeySpec secret = new SecretKeySpec(key, "AES");
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
		
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secret, ivSpec);
		byte[] result = cipher.doFinal(bytesToBeEncrypted);
		
		return result;
	}
	
	private byte[] AES_Decrypt(byte[] bytesToBeDecrypted,byte[] passwordBytes) throws Exception{
				
		byte[] saltBytes = new byte[] { 13,25,73,14,115,-30,97,8 };		   
		
		RFC2898DeriveBytes secretKey = new RFC2898DeriveBytes(passwordBytes, saltBytes, 1000);
		byte[] key = secretKey.getBytes(32);
		byte[] iv = secretKey.getBytes(16);		

		SecretKeySpec secret = new SecretKeySpec(key, "AES");
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
		
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secret, ivSpec);
		byte[] result = cipher.doFinal(bytesToBeDecrypted);
		
		return result;
	}

	public  String encryptText(String input, String password) throws Exception{

		byte[] bytesToBeEncrypted=input.getBytes("UTF-8");
		byte[] passwordBytes=password.getBytes("UTF-8");
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(passwordBytes);
		passwordBytes = md.digest();
		byte[] bytesEncrypted=AES_Encrypt(bytesToBeEncrypted,passwordBytes);
		return new String(Base64.encodeBase64(bytesEncrypted));
	}
	
	public String decryptText(String input, String password) throws Exception{

		byte[] bytesToBeDecrypted=Base64.decodeBase64(input);
		byte[] passwordBytes=password.getBytes("UTF-8");
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(passwordBytes);
		passwordBytes = md.digest();
		byte[] bytesDecrypted=AES_Decrypt(bytesToBeDecrypted,passwordBytes);
		return new String(bytesDecrypted);
	}	
	
	public static void main(String args[]){
		AESEncryptionDecryption c = new AESEncryptionDecryption();
		try {
			System.out.println(c.encryptText("123456789", "tasc-dev-test-encrypt"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
