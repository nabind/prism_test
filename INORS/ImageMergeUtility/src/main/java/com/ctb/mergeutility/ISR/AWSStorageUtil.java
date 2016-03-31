package com.ctb.mergeutility.ISR;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Properties;

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
		} else {
			throw new FileNotFoundException(fileName);
		}
		String s3key = key + "/" + file.getName();
		s3.putObject(this.bucketName, s3key, file);
	}

	/**
	 * List available assets on s3
	 */
	public void getObjectList() {
		ObjectListing objectListing = s3.listObjects(new ListObjectsRequest().withBucketName(bucketName));
		for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
		}
	}
	
	/**
	 * Remove assets from s3
	 */
	public void removeAsset(List<KeyVersion> keys) {
		DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(this.bucketName);
		multiObjectDeleteRequest.setKeys(keys);
		
		try {
		    DeleteObjectsResult delObjRes = s3.deleteObjects(multiObjectDeleteRequest);
		    			
		} catch (MultiObjectDeleteException mode) {
		    // Process exception.
			printDeleteResults(mode);
		}
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