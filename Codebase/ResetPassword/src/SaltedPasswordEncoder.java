import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

public class SaltedPasswordEncoder extends ShaPasswordEncoder {

	public SaltedPasswordEncoder(int strength) {
		super(strength);
	}

	public static String encryptPassword(String rowPass, String salt) {
		SaltedPasswordEncoder ex = new SaltedPasswordEncoder(256);
		return ex.encodePassword(rowPass, salt);
	}

}
