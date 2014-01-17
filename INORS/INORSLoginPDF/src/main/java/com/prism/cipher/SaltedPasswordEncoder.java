package com.prism.cipher;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

/**
 * This wrapper class used to return encrypted password using SHA
 * A salt is used to encrypt the password hash
 * Same encryption algorithm should be used in spring authentication and java utility that generates user password
 * 
 * @author Amit Dhara
 *
 */
public class SaltedPasswordEncoder extends ShaPasswordEncoder {
	public SaltedPasswordEncoder() {
		super();
	}

	public SaltedPasswordEncoder(int strength) {
		super(strength);
	}

	/**
	 * Return encrypted salted password with SHA-256
	 * 
	 * @param rowPass
	 * @param salt
	 * @return
	 */
	public static String encryptPassword(String rawPass, String salt) {
		SaltedPasswordEncoder ex = new SaltedPasswordEncoder(256);
		return ex.encodePassword(rawPass, salt);
	}
	
	/**
	 * Return encoded password (SHA-x)with user provided encryption bit 
	 * 
	 * @param rowPass
	 * @param salt
	 * @param encoding
	 * @return
	 */
	public static String encryptPassword(String rawPass, String salt, int encoding) {
		SaltedPasswordEncoder ex = new SaltedPasswordEncoder(encoding);
		return ex.encodePassword(rawPass, salt);
	}


}