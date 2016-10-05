import java.security.AlgorithmParameters;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.codec.binary.Base64;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class AESSaltEncryptDecrypt {
	
	private static String salt;
	
    public static String encrypt(String key, String initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            //SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            byte[] saltedKey = (salt + key).getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            saltedKey = sha.digest(saltedKey);
            saltedKey = Arrays.copyOf(saltedKey, 32); // use only first 256 bit

            SecretKeySpec secretKeySpec = new SecretKeySpec(saltedKey, "AES");
            
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            System.out.println("encrypted string: "
                    + Base64.encodeBase64String(encrypted));

            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String key, String initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            //SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            
            byte[] saltedKey = (salt + key).getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            saltedKey = sha.digest(saltedKey);
            saltedKey = Arrays.copyOf(saltedKey, 32); // use only first 256 bit

            SecretKeySpec secretKeySpec = new SecretKeySpec(saltedKey, "AES");
            
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        final String key = "Bar12345Bar12345"; // 128 bit key
        final String initVector = "RandomInitVector"; // 16 bytes IV
        salt = "SHA1PRNG";
       
        
        
        try{
    		//salt = "SHA1PRNG";// getSalt();

    	       /* char[] message = ("<Students>"+
    	        					"<Student>"+
    		        					"<FirstName>Bob</FirstName>"+
    		        					"<LastName>Miller</LastName>"+
    		        					"<SSN>/CjHjCVbiDBLPSfr8nkAFQ==</SSN>"+
    	        					"</Student>"+
    	        					"<Student>"+
    		        					"<FirstName>John</FirstName>"+
    		        					"<LastName>Smith</LastName>"+
    		        					"<SSN>WkoJknErEtJ90a4ErhTlWw==</SSN>"+
    	        					"</Student>"+
    	        				 "</Students>").toCharArray();*/
    	        
    	        
    	        SAXParserFactory factory = SAXParserFactory.newInstance();
    	    	SAXParser saxParser = factory.newSAXParser();
    	    	
    	    	DefaultHandler handler = new DefaultHandler() {
    	    		boolean bSSN = false;

    	    		public void startElement(String uri, String localName,String qName,
    	    				Attributes attributes) throws SAXException {

    	    			//System.out.println("Start Element :" + qName);

    	    			if (qName.equalsIgnoreCase("SSN")) {
    	    				bSSN = true;
    	    			}
    	    		}
    	    		
    	    		public void endElement(String uri, String localName,
    	    				String qName) throws SAXException {

    	    			//System.out.println("End Element :" + qName);

    	    		}

    	    		
    	    		public void characters(char ch[], int start, int length) throws SAXException {
    	        		if (bSSN) {
    	        			String encryptedSSN = new String(ch, start, length);
    	        			System.out.println("Now Decrypting SSN : " + encryptedSSN);
    	        			bSSN = false;
    	        			try {
    	        				String decryptedSSN = decrypt(key, initVector, encryptedSSN);
    	        				
    	        				System.out.println("Decrypted: " +decryptedSSN);
    						} catch (Exception e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
    	        			
    	        		}
    	        	}
    	    	};
    	        
    	    	saxParser.parse("./xml/sample.xml", handler);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
        
        
        
    }
	
}
