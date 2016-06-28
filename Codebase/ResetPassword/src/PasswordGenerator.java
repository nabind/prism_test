



/**
 * This class is used to generate random string baased on business rule
 * The string can be used for password or salt generation 
 * 
 * @author Amit Dhara
 *
 */
public class PasswordGenerator {
	/** Minimum length for a decent password */
	private static int MIN_LENGTH = 8;

	/** The random number generator. */
	protected static java.util.Random random = new java.util.Random();
	
	private static final char[] symbols = new char[62];
	
	private static char[] buf;

	static {
		for (int idx = 0; idx < 10; ++idx)
			symbols[idx] = (char) ('0' + idx);
		for (int idx = 10; idx < 36; ++idx)
			symbols[idx] = (char) ('a' + idx - 10);
		for (int idx = 36; idx < 62; ++idx)
			symbols[idx] = (char) ('A' + idx - 36);
	}

	/*
	 * Set of characters that is valid. Must be printable, memorable, and "won't
	 * break HTML" (i.e., not ' <', '>', '&', '=', ...). or break shell commands
	 * (i.e., not ' <', '>', '$', '!', ...). I, L and O are good to leave out,
	 * as are numeric zero and one.
	 */
	protected static char[] goodSmallChar = { 'a', 'b', 'c', 'd', 'e', 'f',
			'h', 'j', 'k', 'm', 'n', 'p', 'r', 's', 't', 
			'w', 'x', 'y', 'z', };
	protected static char[] goodCapChar = { 'A', 'B', 'C', 'D', 'E', 'F', 'G',
			'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'W',
			'X', 'Y', 'Z', };
	protected static char[] goodSpecialChar = { '#', '$', '@', };
	protected static char[] goodDigit = { '2', '3', '4', '5', '6', '7', '8',
			'9', };

	/**
	 *  Generate a Password object with a random string
	 */
	public static String getNext() {
		MIN_LENGTH = 8;
		return generateRandomText();
	}
	
	/**
	 * Generate a random string of user provided length
	 * @param length
	 * @return
	 */
	public static String getNext(int length) {
		if(length > 2) MIN_LENGTH = length;
		return generateRandomText();
	}
	
	/**
	 * Generate random text with capital letter at starting and ends with a digit
	 * @return
	 */
	public static String generateRandomText() {
		StringBuffer sb = new StringBuffer();
		sb.append(goodCapChar[random.nextInt(goodCapChar.length)]);
		for (int i = 0; i < MIN_LENGTH - 2; i++) {
			sb.append(goodSmallChar[random.nextInt(goodSmallChar.length)]);
		}
		//sb.append(goodSpecialChar[random.nextInt(goodSpecialChar.length)]);
		sb.append(goodDigit[random.nextInt(goodDigit.length)]);
		return sb.toString();
	}
	
	/**
	 * Generate random text without any business validation
	 * This text can be used as a salt
	 * @return
	 */
	public static String getNextSalt() {
		return getNextSalt(20);
	}
	public static String getNextSalt(int length) {
		if (length < 1)	buf = new char[8];
		else buf = new char[length];
		
		for (int idx = 0; idx < buf.length; ++idx)
			buf[idx] = symbols[random.nextInt(symbols.length)];
		return new String(buf);
	}
	
	
}
