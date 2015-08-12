package com.ctb.mergeutility.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.MultiObjectDeleteException;
import com.amazonaws.services.s3.model.MultiObjectDeleteException.DeleteError;
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
			Properties properties = ApplicationConfig.loadApplicationConfig().getConfigProp();
			ClientConfiguration clientConfig = new ClientConfiguration();
			clientConfig.setProtocol(Protocol.HTTPS);
			//clientConfig.setProxyHost(properties.getProperty("ip_address"));
			//clientConfig.setProxyPort(Integer.valueOf(properties.getProperty("port_number")));
			//clientConfig.setProxyUsername(properties.getProperty("user_id_on_network"));
			//clientConfig.setProxyPassword(properties.getProperty("password_on_network"));
			this.credentials = new BasicAWSCredentials(properties.getProperty("aws.accessKey"), properties.getProperty("aws.secretKey"));
			this.bucketName = properties.getProperty("aws.s3bucket");
			this.s3 = new AmazonS3Client(this.credentials);
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
	
	/**
	 * Remove assets from s3
	 */
	public void removeAsset(List<KeyVersion> keys) {
		logger.info("keys = " + keys);
		DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(this.bucketName);
		multiObjectDeleteRequest.setKeys(keys);
		
		try {
		    DeleteObjectsResult delObjRes = s3.deleteObjects(multiObjectDeleteRequest);
		    logger.info("Successfully deleted all the %s items.\n" + delObjRes.getDeletedObjects().size());
		    			
		} catch (MultiObjectDeleteException mode) {
		    // Process exception.
			printDeleteResults(mode);
		}
		logger.info("Asset Removed from S3: ");
	}
	
	private void printDeleteResults(MultiObjectDeleteException mode) {
        System.out.format("%s \n", mode.getMessage());
        System.out.format("No. of objects successfully deleted = %s\n", mode.getDeletedObjects().size());
        System.out.format("No. of objects failed to delete = %s\n", mode.getErrors().size());
        System.out.format("Printing error data...\n");
        for (DeleteError deleteError : mode.getErrors()){
            System.out.format("Object Key: %s\t%s\t%s\n", 
                    deleteError.getKey(), deleteError.getCode(), deleteError.getMessage());
        }
	}

}