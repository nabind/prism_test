package com.ctb.prism.core.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.transferobject.ObjectValueTO;

@Service("repositoryService")
public class RepositoryServiceImpl implements IRepositoryService {
	
	private static final IAppLogger logger = LogFactory.getLoggerInstance(RepositoryServiceImpl.class.getName());
	
	private static final String FOLDER_SUFFIX = "/";

	private AmazonS3 s3client;
	private String bucket;	
	
	public void setS3Client(AmazonS3 client) {
		this.s3client = client;
	}
	
	public void setBucket(String bucket){
		this.bucket = bucket;
	}
	
	@Autowired
	private IPropertyLookup propertyLookup;
	
	/*public RepositoryServiceImpl() {
	}

	public RepositoryServiceImpl(AmazonS3 client) {
		s3client = client;
	}*/
	
	public List<String> getAssetList(String path) {
		logger.log(IAppLogger.INFO, "path = " + path);
		List<String> result = new ArrayList<String>();
		if (path == null || path.isEmpty()) {
			return result;
		}
		ObjectListing objList = s3client.listObjects(bucket, getS3Path(path));
		logger.log(IAppLogger.INFO, "size = " + objList.getObjectSummaries().size());
		for (S3ObjectSummary summary : objList.getObjectSummaries()) {
			// ignore folders
			if (!summary.getKey().endsWith(FOLDER_SUFFIX)) {
				result.add(summary.getKey().substring(path.length()));
			}
		}
		logger.log(IAppLogger.INFO, "result = " + result);
		return result;
	}
	
	public List<ObjectValueTO> getAssetListWithPath(String path) {
		List<ObjectValueTO> result = new ArrayList<ObjectValueTO>();		
		// ObjectListing objList = s3client.listObjects(bucket, getS3Path(path));
		ObjectListing objList = s3client.listObjects(new ListObjectsRequest().withBucketName(bucket));
		for (S3ObjectSummary summary:objList.getObjectSummaries()) {
			//ignore folders
			if(! summary.getKey().endsWith(FOLDER_SUFFIX)){
				java.util.Calendar c = java.util.Calendar.getInstance();
				c.add(java.util.Calendar.MINUTE, 1);
				URL objPath = s3client.generatePresignedUrl(bucket, summary.getKey(), c.getTime());
				
				ObjectValueTO obj = new ObjectValueTO();
				obj.setName(summary.getKey().substring(path.length()));
				obj.setValue(objPath.toString());
			}
		}

		return result;
	}
	
	private String getS3Path(String path) {
		//remove root path: /
		if (path.startsWith(FOLDER_SUFFIX)) {
			path = path.substring(1);
		}
		return propertyLookup.get("environment.postfix").toUpperCase() + FOLDER_SUFFIX + path + FOLDER_SUFFIX;
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.core.Service.IRepositoryService#getAssetBytes(java.lang.String)
	 */
	public byte[] getAssetBytes(String assetPath) throws IOException {
		assetPath = propertyLookup.get("environment.postfix").toUpperCase() + FOLDER_SUFFIX + assetPath;
		System.out.println("Downloading an object from "+assetPath);
		S3Object object = s3client.getObject(new GetObjectRequest(bucket, assetPath));
		S3ObjectInputStream inputStream = object.getObjectContent();
		byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);
		inputStream.close(); // Must be closed as it is directly opened from Amazon
		return bytes;
	}
	
	/* (non-Javadoc)
	 * @see com.ctb.prism.core.Service.IRepositoryService#uploadAsset(java.io.File)
	 */
	public void uploadAsset(String key, File file) {
		key = key + file.getName();
		logger.log(IAppLogger.INFO, "key = " + key);
		s3client.putObject(bucket, key, file);
	}
	
	/**
	 * @author Joy
	 * "key" is full qualified file path
	 * "is" is an InputStream object
	 */
	public void uploadAsset(String key, InputStream is) {
		logger.log(IAppLogger.INFO, "key = " + key);
		byte[] contentBytes = null;
		try {
			contentBytes = IOUtils.toByteArray(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Long contentLength = Long.valueOf(contentBytes.length);
	    ObjectMetadata metadata = new ObjectMetadata();
	    metadata.setContentLength(contentLength);
		//s3client.putObject(bucket, key, is, metadata);
	    s3client.putObject(new PutObjectRequest(bucket, key, is, metadata));
	}
	
	/* (non-Javadoc)
	 * @see com.ctb.prism.core.Service.IRepositoryService#removeAsset(java.io.File)
	 */
	public void removeAsset(String key){
		logger.log(IAppLogger.INFO, "key = " + key);
		s3client.deleteObject(bucket, key);
	}
	
	
	public InputStream getAssetInputStream(String assetPath) throws IOException {
		logger.log(IAppLogger.INFO, "downloading object into input strem");
		S3Object object = s3client.getObject(new GetObjectRequest(bucket, assetPath));
		return  object.getObjectContent();		
	}
}
