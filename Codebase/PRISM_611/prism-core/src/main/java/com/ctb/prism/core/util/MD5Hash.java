package com.ctb.prism.core.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.util.DigestUtils;



import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;

public class MD5Hash {
	
	private static final IAppLogger logger = LogFactory.getLoggerInstance(MD5Hash.class.getName());
	
	public static String md5Hashencode(String message){
		String digest =null;
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hash = md.digest(message.getBytes("UTF-8"));
			
			//Converting byte array to hexadecimal string
			StringBuilder sb = new StringBuilder(2*hash.length);
			for(byte b: hash){
				sb.append(String.format("%02x", b&0xff));
			}
			digest = sb.toString();
			
		} catch(UnsupportedEncodingException ex) {
			logger.log(IAppLogger.ERROR, "Error occuered md5Hashencode: " + message);
		} catch(NoSuchAlgorithmException ex) {
			logger.log(IAppLogger.ERROR, "Error occuered md5Hashencode: " + message);
		}
		return digest;
	}
	
	public static String md5Spring(String text) {
		//return DigestUtils.md5Hex(text);
		return DigestUtils.md5DigestAsHex(text.getBytes());
	}
	
	
	 public static void main(String args[]) {
		 String password = "password";
		 System.out.println("MD5 hash generated using Java : " + md5Hashencode(password));
		 System.out.println("MD5 hash generated using Java : " + md5Spring(password));
		 
	 }
	
}
