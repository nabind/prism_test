package com.ctb.prism.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;




public class AESEncCompatible {

	private final String characterEncoding = "UTF-8";
    private final static String cipherTransformation = "AES/CBC/PKCS5Padding";
    private final static String aesEncryptionAlgorithm = "AES";
    private static String encryptionKey = "ETCguRF49hEaRuZg";

    public static void main(String args[]) throws IOException {
    	AESEncCompatible t = new AESEncCompatible();
        String encrypt = t.encrypt("mypassword");
        System.out.println("decrypted value:" + t.decrypt(encrypt));
        
        /* source file - clear text */ 
		File initialFile = new File("C:\\temp\\sample.txt");
	    InputStream targetStream = new FileInputStream(initialFile);
	    
	    /* encoded stream */
	    byte[] encFile = t.encrypt(targetStream);
	    
	    /* create encrypted file*/
	    FileOutputStream enc = new FileOutputStream("C:\\temp\\sample_enc.txt");
	    enc.write(encFile);
	    
	    /* get enc file*/
	    InputStream encFileStream = new FileInputStream("C:\\temp\\sample_enc.txt");
	    
	    /* decrypt file */
	    FileOutputStream fos = new FileOutputStream("C:\\temp\\sample_out.txt");
	    fos.write(t.decrypt(IOUtils.toByteArray(encFileStream)));
	    
	    IOUtils.closeQuietly(fos);
	    IOUtils.closeQuietly(enc);
	    IOUtils.closeQuietly(targetStream);
	    IOUtils.closeQuietly(encFileStream);
    }

    public byte[] encrypt(InputStream targetStream) throws IOException {
    	return encrypt(IOUtils.toByteArray(targetStream).toString()).getBytes();
    }
    public byte[] decrypt(byte[] bs) throws IOException {
    	return decrypt(bs.toString()).getBytes();
    }
    public String encrypt(String value) {
        try {
        	byte[] salt = encryptionKey.getBytes();
        	Key key = new SecretKeySpec(salt, 0, 16, "AES");

        	Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            String encrypt = (new Base64()).encodeAsString(cipher.doFinal(value.getBytes()));
            System.out.println("encrypted string:" + encrypt);
            return encrypt;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String decrypt(String encrypted) {
        try {
        	byte[] salt = encryptionKey.getBytes();
        	Key key = new SecretKeySpec(salt, 0, 16, "AES");

        	Cipher cipher = Cipher.getInstance("AES");
        	cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedValue = new Base64().decode(encrypted);
            byte[] decValue = cipher.doFinal(decodedValue);
            String decryptedValue = new String(decValue);
            return decryptedValue;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}