package com.prism.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class AWSStorageUtil {
	private final Logger logger = Logger.getLogger(AWSStorageUtil.class);
	private BasicAWSCredentials credentials;
	private AmazonS3 s3;
	private String bucketName;
	private static volatile AWSStorageUtil awsstorageUtil = new AWSStorageUtil();

	/**
	 * Constructor
	 */
	private AWSStorageUtil() {
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream("aws.properties"));
			ClientConfiguration clientConfig = new ClientConfiguration();
			clientConfig.setProtocol(Protocol.HTTPS);
			clientConfig.setProxyHost(properties.getProperty("ip_address"));
			clientConfig.setProxyPort(Integer.valueOf(properties.getProperty("port_number")));
			clientConfig.setProxyUsername(properties.getProperty("user_id_on_network"));
			clientConfig.setProxyPassword(properties.getProperty("password_on_network"));
			this.credentials = new BasicAWSCredentials(properties.getProperty("aws.accessKey"), properties.getProperty("aws.secretKey"));
			this.bucketName = properties.getProperty("aws.s3bucket");
			if("false".equals(properties.getProperty("bypass_proxy"))){
				this.s3 = new AmazonS3Client(this.credentials, clientConfig);
			} else {
				this.s3 = new AmazonS3Client(this.credentials);
			}
		} catch (Exception e) {
			logger.info("exception while creating awss3client : " + e);
			e.printStackTrace();
		}
	}

	public static AWSStorageUtil getInstance() {
		return awsstorageUtil;
	}

	public static AmazonS3 getAWSClient() {
		return awsstorageUtil.s3;
	}

	public static AmazonS3 getBucketName() {
		return awsstorageUtil.s3;
	}

	/**
	 * Upload an asset to s3
	 * 
	 * @param key
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public void uploadObject(String key, String fileName) throws FileNotFoundException {
		File file = new File(fileName);
		if (file.isFile()) {
			logger.debug("File fould");
		} else {
			throw new FileNotFoundException(fileName);
		}
		String s3key = key + "/" + file.getName();
		logger.debug( "File :" + file + " Asset(" + s3key + ") is getting uploaded  ");
		logger.info("S3 Key: " + s3key);
		s3.putObject(this.bucketName, s3key, file);
		logger.debug( "Asset(" + key + ") uploaded successfully");
	}

	/**
	 * List available assets on s3
	 */
	public void getObjectList() {
		logger.info("Listing objects");
		ObjectListing objectListing = s3.listObjects(new ListObjectsRequest().withBucketName(bucketName));
		for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
			logger.info(" - " + objectSummary.getKey() + " " + "(size = " + objectSummary.getSize() + ")");
		}
		logger.info("Exit: getObjectList()");
	}

}