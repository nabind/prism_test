package com.ctb.prism.login.security.encoder;

import groovy.util.logging.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.safehaus.uuid.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.login.Service.ILoginService;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@Slf4j
public class DigitalMeasuresHMACQueryStringBuilder {
	private String URL_ENCODING;//UTF-8
	private String ENCODING_ALGORITHM;//HmacSHA1

	private SecretKey secretKey;
	
	private String encryptionKey;//WPZguVF49hXaRuZfe9L29ItsC2I
	private int signatureValiditySeconds;//60
	private String timeZone;//PST
	
	private static final String CUSTOMER_ID_PARAM = "customer_id=";
	private static final String ORG_NODE_PARAM = "&org_node_code=";
	private static final String HIERARCHY_LAVEL_PARAM = "&hierarchy_level=";
	private static final String APPLICATION_NAME_PARAM = "&application_name=";
    private static final String EXPIRY_DATE_PARAM = "&time_stamp=";
    private static final String USER_ROLE_PARAM = "&user_role=";
    private static final String USER_NAME_PARAM = "&user_name=";
    private static final String SIGNATURE_PARAM = "&signature=";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    /* ## new for eR candidate report */
    private static final String STUDENT_ID_PARAM = "ctbstudentid=";
    private static final String UUID_PARAM = "&uuid=";
    /* ## end : new for eR candidate report */
    
    @Autowired ILoginService loginService;
    
	public DigitalMeasuresHMACQueryStringBuilder() {
	}

	/**
	 * @param encryptionKey	encryption key supplied to you by Digital Measures
	 * @param signatureValiditySeconds	number of seconds that generated signature is valid for
	 *	10 seconds is recommended, unless your server's clock
	 *	tends to drift significantly. Consider using NTP before
	 *	adjusting this too far.
	 */
	public DigitalMeasuresHMACQueryStringBuilder(String encryptionKey, int signatureValiditySeconds, String encodingAlgo)
	{
		this(encryptionKey.getBytes(), signatureValiditySeconds, encodingAlgo);
	}

	/**
	 * @param encryptionKey	encryption key supplied to you by Digital Measures
	 * @param signatureValiditySeconds	number of seconds that generated signature is valid for
	 *	10 seconds is recommended, unless your server's clock
	 *	tends to drift significantly. Consider using NTP before
	 *	adjusting this too far.
	 */
	public DigitalMeasuresHMACQueryStringBuilder(byte[] encryptionKey, int signatureValiditySeconds, String encodingAlgo)
	{
		this.ENCODING_ALGORITHM = encodingAlgo;
		this.secretKey = new SecretKeySpec(encryptionKey, ENCODING_ALGORITHM);
		this.signatureValiditySeconds = signatureValiditySeconds;
	}

	/**
	 * Construct a query string including HMAC signature
	 * @param username	username string that has been authenticated by your systems
	 * @return the authenticated query string
	 */
	/*public String buildAuthenticatedQueryString(String username) throws Exception
	{
		return buildAuthenticatedQueryString(username, null);
	}*/
	
	/**
	 * Construct a query string including HMAC signature
	 * @param username	username string that has been authenticated by your systems
	 * @param ipAddress	user's publicly accessible IP address, or Null. Reasons to omit:
	 *	IP addresses are not available due to network or server configuration
	 *	Users access the public internet through NAT, but your server
	 *	is inside the NAT, and can see internal network IP addresses.
	 * @return the authenticated query string
	 */
	/*public String buildAuthenticatedQueryString(String username, String ipAddress) throws Exception
	{
		String validUntilDate = getISO8601UTCDate();
		Appendable queryString = buildUnauthenticatedQueryString(username, ipAddress, validUntilDate);
		String signature = getAuthenticationCode(queryString.toString());

		appendAuthenticationCode(queryString, signature);

		return queryString.toString();
	}*/
	
	public String buildAuthenticatedQueryString(String customerId, String orgNode, 
			String orgLevel, String applicationName, String role, String userName, String theme) throws Exception
	{
		String validUntilDate = getISO8601UTCDate();
		Appendable queryString = buildUnauthenticatedQueryString(customerId, orgNode, orgLevel, applicationName, role, userName, validUntilDate);
		String signature = getAuthenticationCode(queryString.toString(), applicationName, theme);

		appendAuthenticationCode(queryString, signature);

		return queryString.toString();
	}
	
	/**
	 * Check if the client provided parameters is valid
	 * @param inputParam
	 * @param ipAddress
	 * @param validUntilDate
	 * @param secretValue
	 * @return
	 * @throws Exception
	 */
	public boolean isValidRequest(String customerId, String orgNode, 
			String orgLevel, String applicationName, String role, String userName,
			String validUntilDate, String secretValue, String theme) throws Exception
	{
		
		TimeZone timeZone = TimeZone.getTimeZone(getTimeZone());
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
		format.setTimeZone(timeZone);
		format.setLenient(false);
		Calendar calendar = Calendar.getInstance(timeZone);
		format.format(calendar.getTime());
		
		Date date = format.parse(URLDecoder.decode(validUntilDate, URL_ENCODING));
		Calendar expiryTime = Calendar.getInstance(timeZone);
		expiryTime.setTime(date);
		
		if(expiryTime.compareTo(calendar) > 0) {
			Appendable queryString = buildUnauthenticatedQueryString(
					URLDecoder.decode(customerId, URL_ENCODING), 
					URLDecoder.decode(orgNode, URL_ENCODING), 
					URLDecoder.decode(orgLevel, URL_ENCODING), 
					URLDecoder.decode(applicationName, URL_ENCODING), 
					URLDecoder.decode(role, URL_ENCODING), 
					URLDecoder.decode(userName, URL_ENCODING),
					URLDecoder.decode(validUntilDate, URL_ENCODING));
			String signature = urlEncode(getAuthenticationCode(queryString.toString(), applicationName, theme));
			
			if(secretValue != null && secretValue.equals(URLDecoder.decode(signature, URL_ENCODING))) {
				// encoding needed before comparing
				return equals(urlEncode(secretValue), signature);
			}
	
			return equals(secretValue, signature);
		} else {
			Logger.logInfo("Request key is expired .... ");
			return false;
		}

	}
	
	/* ## new for eR candidate report */
	public boolean isValidRequest(String applicationName, String validUntilDate, String studentId, String secretValue,
			String uuid, String orgNode, String theme) throws Exception
	{
		
		TimeZone timeZone = TimeZone.getTimeZone(getTimeZone());
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
		format.setTimeZone(timeZone);
		format.setLenient(false);
		Calendar calendar = Calendar.getInstance(timeZone);
		format.format(calendar.getTime());
		
		Date date = format.parse(URLDecoder.decode(validUntilDate, URL_ENCODING));
		Calendar expiryTime = Calendar.getInstance(timeZone);
		expiryTime.setTime(date);
		
		if(expiryTime.compareTo(calendar) > 0) {
			Appendable queryString = buildUnauthenticatedQueryString(
					URLDecoder.decode(studentId, URL_ENCODING),
					URLDecoder.decode(applicationName, URL_ENCODING), 
					URLDecoder.decode(validUntilDate, URL_ENCODING),
					URLDecoder.decode(uuid, URL_ENCODING),
					URLDecoder.decode(orgNode, URL_ENCODING));
			String signature = urlEncode(getAuthenticationCode(queryString.toString(), applicationName, theme));
			
			if(secretValue != null && secretValue.equals(URLDecoder.decode(signature, URL_ENCODING))) {
				// encoding needed before comparing
				return equals(urlEncode(secretValue), signature);
			}
	
			return equals(secretValue, signature);
		} else {
			Logger.logInfo("Request key is expired .... ");
			return false;
		}

	}
	/* end ## new for eR candidate report */
	
	/**
	 * Overloaded method for clearCache utility
	 * Check if the client provided parameters is valid
	 * @param inputParam
	 * @param ipAddress
	 * @param validUntilDate
	 * @param secretValue
	 * @return
	 * @throws Exception
	 */
	public boolean isValidRequest(String validUntilDate, String secretValue, String theme) throws Exception
	{
		
		TimeZone timeZone = TimeZone.getTimeZone(getTimeZone());
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
		format.setTimeZone(timeZone);
		format.setLenient(false);
		Calendar calendar = Calendar.getInstance(timeZone);
		format.format(calendar.getTime());
		
		Date date = format.parse(URLDecoder.decode(validUntilDate, URL_ENCODING));
		Calendar expiryTime = Calendar.getInstance(timeZone);
		expiryTime.setTime(date);
		
		if(expiryTime.compareTo(calendar) > 0) {
			Appendable queryString = buildUnauthenticatedQueryString(
						URLDecoder.decode(validUntilDate, URL_ENCODING));
			String signature = urlEncode(getAuthenticationCode(queryString.toString(), theme));
			
			if(secretValue != null && secretValue.equals(URLDecoder.decode(signature, URL_ENCODING))) {
				// encoding needed before comparing
				return equals(urlEncode(secretValue), signature);
			}
	
			return equals(secretValue, signature);
		} else {
			System.out.println("Request key is expired .... ");
			return false;
		}

	}
	
	
	
	private boolean equals(String expected, String actual) {
        byte[] expectedBytes = null;
        byte[] actualBytes = null;
        try {
            expectedBytes = expected.getBytes(URL_ENCODING);
            actualBytes = actual.getBytes(URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding while encrypting.",e);
        }

        int expectedLength = expectedBytes == null ? -1 : expectedBytes.length;
        int actualLength = actualBytes == null ? -1 : actualBytes.length;
        if (expectedLength != actualLength) {
            return false;
        }

        int result = 0;
        for (int i = 0; i < expectedLength; i++) {
            result |= expectedBytes[i] ^ actualBytes[i];
        }
        return result == 0;
    }

	/**
	 * Build a timestamp in the UTC time zone, format date according to
	 * ISO8601: yyyy-MM-ddThh:mm:ssZ, with the specified number of seconds added
	 * @return timestamp
	 */
	private String getISO8601UTCDate()
	{
		TimeZone timeZone = TimeZone.getTimeZone(getTimeZone());
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
		format.setTimeZone(timeZone);
		format.setLenient(false);

		Calendar calendar = Calendar.getInstance(timeZone);
		calendar.add(Calendar.SECOND, signatureValiditySeconds);

		return format.format(calendar.getTime());
	}

	/**
	 * Construct query string without HMAC signature
	 * @param username	username string that has been authenticated by your systems
	 * @param ipAddress	user's publicly accessible IP address, or Null
	 * @param validUntilDate	iso date in the format yyyy-MM-ddThh:mm:ssZ
	 * @return query string
	 */
	private Appendable buildUnauthenticatedQueryString(String customerId, String orgNode, 
			String orgLevel, String applicationName, String role, String userName,
			String validUntilDate) throws Exception
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append(CUSTOMER_ID_PARAM).append(urlEncode(customerId));
		builder.append(ORG_NODE_PARAM).append(urlEncode(orgNode));
		builder.append(HIERARCHY_LAVEL_PARAM).append(urlEncode(orgLevel));
		builder.append(APPLICATION_NAME_PARAM).append(urlEncode(applicationName));
		builder.append(EXPIRY_DATE_PARAM).append(urlEncode(validUntilDate));
		builder.append(USER_ROLE_PARAM).append(urlEncode(role));
		builder.append(USER_NAME_PARAM).append(urlEncode(userName));
		
		/*builder.append(API_KEY_PARAMETER_NAME).append(urlEncode(username));
		builder.append(EXPIRY_DATE_PARAMETER_NAME).append(urlEncode(validUntilDate));

		if(null != ipAddress && ipAddress.trim().length() > 0)
			builder.append(IP_ADDRESS_PARAMETER_NAME).append(urlEncode(ipAddress));*/

		return builder;
	}
	
	/* ## new for eR candidate report */
	private Appendable buildUnauthenticatedQueryString(String studentId, String applicationName, 
			String validUntilDate, String uuid, String orgNode) throws Exception
	{
		StringBuilder builder = new StringBuilder();
		builder.append(STUDENT_ID_PARAM).append(urlEncode(studentId));
		builder.append(APPLICATION_NAME_PARAM).append(urlEncode(applicationName));
		builder.append(EXPIRY_DATE_PARAM).append(urlEncode(validUntilDate));
		builder.append(UUID_PARAM).append(urlEncode(uuid));
		builder.append(ORG_NODE_PARAM).append(urlEncode(orgNode));
		return builder;
	}
	/* ## end new for eR candidate report */
	
	
	/**
	 * Overloaded method for clearContractCache
	 * Construct query string without HMAC signature
	 * @param username	username string that has been authenticated by your systems
	 * @param ipAddress	user's publicly accessible IP address, or Null
	 * @param validUntilDate	iso date in the format yyyy-MM-ddThh:mm:ssZ
	 * @return query string
	 */
	private Appendable buildUnauthenticatedQueryString(
			String validUntilDate) throws Exception
	{
		StringBuilder builder = new StringBuilder();		
		builder.append(EXPIRY_DATE_PARAM).append(urlEncode(validUntilDate));

		return builder;
	}
	

	/**
	 * Append the authentication code to an unauthenticated query string
	 * @param queryString	unauthenticated query string
	 * @param signature	authentication signature
	 * @return authenticated query string
	 */
	private void appendAuthenticationCode(Appendable queryString, String signature) throws Exception
	{
		queryString.append(SIGNATURE_PARAM).append(urlEncode(signature));
	}

	/**
	 * Perform HMAC SHA-1 hash on the supplied message
	 * @param message	message to hash
	 * @return hashed message
	 */
	private String getAuthenticationCode(String message, String applicationName, String theme) throws Exception
	{
		Mac messageAuthenticationCode = Mac.getInstance(ENCODING_ALGORITHM);

		// get encryption key from configuration DB
		if(theme != null) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			//paramMap.put("property", "hmac.secret.key");
			//paramMap.put("source", CustomStringUtil.appendString(applicationName, "|", theme));
			paramMap.put("contractName", theme);
			Map<String, Object> propertyMap = loginService.getContractProerty(paramMap);
			encryptionKey = (String)propertyMap.get(IApplicationConstants.HMAC_SECRET_KEY+"~"+CustomStringUtil.appendString(applicationName, "|", theme));
			secretKey = new SecretKeySpec((encryptionKey == null)? "".getBytes() : encryptionKey.getBytes(), ENCODING_ALGORITHM);
		}
		// end: 
		
		messageAuthenticationCode.init(secretKey);
		messageAuthenticationCode.update(message.getBytes());

		byte[] digest = messageAuthenticationCode.doFinal();

		return Base64.encode(digest);
	}
	
	
	/**
	 * Overloaded method for clearContractCache
	 * Perform HMAC SHA-1 hash on the supplied message
	 * @param message	message to hash
	 * @return hashed message
	 */
	private String getAuthenticationCode(String message, String theme) throws Exception
	{
		Mac messageAuthenticationCode = Mac.getInstance(ENCODING_ALGORITHM);

		// get encryption key from configuration DB
		if(theme != null) {
			encryptionKey = "BTCguSF49hYaPmAfe9Q29LtsQ2X" +"~"+ theme;
			secretKey = new SecretKeySpec((encryptionKey == null)? "".getBytes() : encryptionKey.getBytes(), ENCODING_ALGORITHM);
		}
		// end: 
		
		messageAuthenticationCode.init(secretKey);
		messageAuthenticationCode.update(message.getBytes());

		byte[] digest = messageAuthenticationCode.doFinal();

		return Base64.encode(digest);
	}
	

	/**
	 * Percent-escape any characters that are not valid in a URL
	 * @param value	raw value to encode
	 * @return encoded value
	 */
	private String urlEncode(String value) throws Exception
	{
		return URLEncoder.encode(value, URL_ENCODING);
	}
	
	public String getEncryptionKey() {
		return encryptionKey;
	}

	public void setEncryptionKey(String encryptionKey) {
		this.encryptionKey = encryptionKey;
	}

	public int getSignatureValiditySeconds() {
		return signatureValiditySeconds;
	}

	public void setSignatureValiditySeconds(int signatureValiditySeconds) {
		this.signatureValiditySeconds = signatureValiditySeconds;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getURL_ENCODING() {
		return URL_ENCODING;
	}

	public void setURL_ENCODING(String uRL_ENCODING) {
		URL_ENCODING = uRL_ENCODING;
	}

	public String getENCODING_ALGORITHM() {
		return ENCODING_ALGORITHM;
	}

	public void setENCODING_ALGORITHM(String eNCODING_ALGORITHM) {
		ENCODING_ALGORITHM = eNCODING_ALGORITHM;
	}
	
	public static void main(String[] args) {
		try {
			DigitalMeasuresHMACQueryStringBuilder hmc = 
				new DigitalMeasuresHMACQueryStringBuilder("BTCguSF49hYaPmAfe9Q29LtsQ2X", 30, "HmacSHA1");
			hmc.setURL_ENCODING("UTF-8");
			/*Appendable queryString = hmc.buildUnauthenticatedQueryString(
					"12160", //URLDecoder.decode(customerId, URL_ENCODING), 
					"", //URLDecoder.decode(orgNode, URL_ENCODING), 
					"1", //URLDecoder.decode(orgLevel, URL_ENCODING), 
					"OAS", //URLDecoder.decode(applicationName, URL_ENCODING), 
					"Admin", //URLDecoder.decode(role, URL_ENCODING), 
					"2013-10-18T18:39:59Z" //URLDecoder.decode(validUntilDate, URL_ENCODING)
					);
			System.out.println(queryString.toString());
			*/
			String param = "customer_id=457598&org_node_code=D1%7E8130%7E8793&hierarchy_level=3&application_name=CTB.com&time_stamp=2014-10-12T22%3A54%3A42Z&user_role=Admin&user_name=aredmon5023";
			String signature = hmc.getAuthenticationCode(param.toString(), "CTB.COM", null);
			System.out.println( URLEncoder.encode(signature, "UTF-8"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
