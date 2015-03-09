package com.ctb.prism.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.IOUtils;


public class AESEncryption {

	private static final String aesEncryptionAlgorithm = "AES";
	//private static final byte[] keyValue = "ETCguRF49hEaRuZg".getBytes();
	private static final String characterEncoding = "UTF-8";
	private static final String cipherTransformation = "AES/CBC/PKCS5Padding";
	private static final String keyValue = "ETCguRF49hEaRuZg";

	
    private static byte[] getKeyBytes(String key) throws UnsupportedEncodingException{
        byte[] keyBytes= new byte[16];
        byte[] parameterKeyBytes= key.getBytes(characterEncoding);
        System.arraycopy(parameterKeyBytes, 0, keyBytes, 0, Math.min(parameterKeyBytes.length, keyBytes.length));
        return keyBytes;
    }
    
   //***********Encryption***************//
	
	public static byte[] encrypt(InputStream Data) throws Exception {
		return encrypt(Data, keyValue);
	}
	
    private static byte[] encrypt(InputStream Data, String key) throws Exception {
		byte[] inputStreamByte = IOUtils.toByteArray(Data);
		byte[] keyBytes = getKeyBytes(key);
		return encrypt(inputStreamByte,keyBytes, keyBytes);
   	}

    
    private static byte[] encrypt(byte[] plainText, byte[] key, byte [] initialVector) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
    {
        Cipher cipher = Cipher.getInstance(cipherTransformation);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, aesEncryptionAlgorithm);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        plainText = cipher.doFinal(plainText);
        return plainText;
    }
    
    //To test with plain text
	public static String encrypt(String plainStr) throws Exception {
		Cipher cipher = Cipher.getInstance(cipherTransformation);
		byte[] key = getKeyBytes(keyValue);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, aesEncryptionAlgorithm);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(key);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
    	byte[] encValue = cipher.doFinal(plainStr.getBytes());
        String encryptedValue = new String(encValue);
		return encryptedValue;
	}
    
	   //***********Decryption***************//
	
    public static byte[] decrypt(byte[] encryptedData) throws Exception {
       	return decrypt(encryptedData, keyValue);
	}
    
    private static byte[] decrypt(byte[] inputStreamByte, String key) throws Exception {
		byte[] keyBytes = getKeyBytes(key);
		return decrypt(inputStreamByte,keyBytes, keyBytes);
   	}
    
	private static byte[] decrypt(byte[] cipherText, byte[] key, byte [] initialVector) throws NoSuchAlgorithmException, 
			NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
    {
        Cipher cipher = Cipher.getInstance(cipherTransformation);
        SecretKeySpec secretKeySpecy = new SecretKeySpec(key, aesEncryptionAlgorithm);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpecy, ivParameterSpec);
        cipherText = cipher.doFinal(cipherText);
        return cipherText;
    }
	
	//To test with plain text
	public static String decrypt(String encryptedData) throws Exception {
		Cipher cipher = Cipher.getInstance(cipherTransformation);
		byte[] key = getKeyBytes(keyValue);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, aesEncryptionAlgorithm);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(key);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
       	byte[] decValue = cipher.doFinal(encryptedData.getBytes());
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}
	
	
	public static void main(String[] args) throws Exception {

		/*String password = "mypassword";
		String passwordEnc = AESEncryption.encrypt(password);
		String passwordDec = AESEncryption.decrypt(passwordEnc);

		System.out.println("Plain Text : " + password);
		System.out.println("Encrypted Text : " + passwordEnc);
		System.out.println("Decrypted Text : " + passwordDec);*/
		
		/* source file - clear text */ 
		File initialFile = new File("C:\\temp\\Candidate_Report_10.pdf");
	    InputStream targetStream = new FileInputStream(initialFile);
	    
	    /* encoded stream */
	    byte[] encFile = AESEncryption.encrypt(targetStream);
	    
	    /* create encrypted file*/
	    FileOutputStream enc = new FileOutputStream("C:\\temp\\Candidate_Report_10_enc.pdf");
	    enc.write(encFile);
	    
	    /* get enc file*/
	    InputStream encFileStream = new FileInputStream("C:\\temp\\Candidate_Report_10_enc.pdf");
	    
	    /* decrypt file */
	    FileOutputStream fos = new FileOutputStream("C:\\temp\\Candidate_Report_10_out.pdf");
	    fos.write(AESEncryption.decrypt(IOUtils.toByteArray(encFileStream)));
	    
	    IOUtils.closeQuietly(fos);
	    IOUtils.closeQuietly(enc);
	    IOUtils.closeQuietly(targetStream);
	    IOUtils.closeQuietly(encFileStream);
	}
}