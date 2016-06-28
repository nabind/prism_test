
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.Key;
import java.util.Arrays;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.IOUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;



public class AESEncryption {

	private static final String ALGO = "AES";
	private static final byte[] keyValue = "ETCguRF49hEaRuZg".getBytes();

	public static String encrypt(String Data) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(Data.getBytes());
		String encryptedValue = new BASE64Encoder().encode(encVal);
		return encryptedValue;
	}

	public static String decrypt(String encryptedData) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
		byte[] decValue = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}
	
	public static byte[] encrypt(InputStream Data) throws Exception {
		return encrypt(Data, keyValue);
	}
	public static byte[] encrypt(InputStream Data, byte[] keyval) throws Exception {
		Key key = generateKey(keyval);
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(IOUtils.toByteArray(Data));
		//String encryptedValue = new BASE64Encoder().encode(encVal);
		//return encryptedValue;
		
		return encVal;
	}

	public static byte[] decrypt(byte[] encryptedData) throws Exception {
		return decrypt(encryptedData, keyValue);
	}
	public static byte[] decrypt(byte[] encryptedData, byte[] keyVal) throws Exception {
		Key key = generateKey(keyVal);
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.DECRYPT_MODE, key);
		//byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
		byte[] decValue = c.doFinal(encryptedData);
		//String decryptedValue = new String(decValue);
		//return decryptedValue;
		return decValue;
	}

	private static Key generateKey() throws Exception {
		return generateKey(keyValue);
	}
	
	private static Key generateKey(byte[] keyVal) throws Exception {
		byte [] subArray;
		if(keyVal.length >= 16) subArray = Arrays.copyOfRange(keyVal, 0, 16);
		else subArray = Arrays.copyOfRange(keyValue, 0, 16);
		Key key = new SecretKeySpec(subArray, ALGO);
		return key;
	}

	public static void main(String[] args) {
		InputStream encFileStream = null;
		FileOutputStream fos = null;
		try {
			Scanner reader = new Scanner(System.in);
			System.out.print("Enter encrypted file name with full path: ");
			String encFileName = reader.nextLine();
			
		    /* get enc file*/
			File encFile = new File(encFileName);
			if(encFile.isFile()) {
			    encFileStream = new FileInputStream(encFile);
			    
			    /* decrypt file */
			    fos = new FileOutputStream(encFile.getAbsolutePath()+".decrypted."+encFile.getName());
			    fos.write(AESEncryption.decrypt(IOUtils.toByteArray(encFileStream)));
			    
			} else {
				System.out.println("Invalid file name or path.");
			}
		} catch (Exception ex) {
			System.out.println("Invalid input file ... may be wrong encryption");
			ex.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fos);
		    IOUtils.closeQuietly(encFileStream);
		}
	}
}