package com.ctb.prism.core.util;

import java.sql.SQLException;
import java.util.List;

public class PasswordGenerator {
	/** Minimum length for a decent password */
	public static final int MIN_LENGTH = 8;

	/** The random number generator. */
	protected static java.util.Random r = new java.util.Random();

	/*
	 * Set of characters that is valid. Must be printable, memorable, and "won't
	 * break HTML" (i.e., not ' <', '>', '&', '=', ...). or break shell commands
	 * (i.e., not ' <', '>', '$', '!', ...). I, L and O are good to leave out,
	 * as are numeric zero and one.
	 */
	protected static char[] goodSmallChar = { 'a', 'b', 'c', 'd', 'e', 'f',
			'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 
			'w', 'x', 'y', 'z', };
	protected static char[] goodCapChar = { 'A', 'B', 'C', 'D', 'E', 'F', 'G',
			'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'W',
			'X', 'Y', 'Z', };
	protected static char[] goodSpecialChar = { '#', '$', '@', };
	protected static char[] goodDigit = { '2', '3', '4', '5', '6', '7', '8',
			'9', };

	/* Generate a Password object with a random password. */
	public static String getNext() {
		StringBuffer sb = new StringBuffer();
		sb.append(goodCapChar[r.nextInt(goodCapChar.length)]);
		for (int i = 0; i < MIN_LENGTH - 2; i++) {
			sb.append(goodSmallChar[r.nextInt(goodSmallChar.length)]);
		}
		//sb.append(goodSpecialChar[r.nextInt(goodSpecialChar.length)]);
		sb.append(goodDigit[r.nextInt(goodDigit.length)]);
		return sb.toString();
	}

}
