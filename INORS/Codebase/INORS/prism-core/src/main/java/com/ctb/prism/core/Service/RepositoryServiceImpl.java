package com.ctb.prism.core.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.ctb.prism.core.transferobject.ObjectValueTO;

@Service("repositoryService")
public class RepositoryServiceImpl implements IRepositoryService {
	
	private static final String FOLDER_SUFFIX = "/";

	private AmazonS3 s3client;
	private String bucket;	
	
	public void setS3Client(AmazonS3 client) {
		this.s3client = client;
	}
	
	public void setBucket(String bucket){
		this.bucket = bucket;
	}
	
	
	/*public RepositoryServiceImpl() {
	}

	public RepositoryServiceImpl(AmazonS3 client) {
		s3client = client;
	}*/
	
	public List<String> getAssetList(String path) {
		List<String> result = new ArrayList<String>();		
		ObjectListing objList = s3client.listObjects(bucket, getS3Path(path));
		for (S3ObjectSummary summary:objList.getObjectSummaries()) {
			//ignore folders
			if(! summary.getKey().endsWith(FOLDER_SUFFIX)){
				result.add(summary.getKey().substring(path.length()));
			}
		}

		return result;
	}
	
	public List<ObjectValueTO> getAssetListWithPath(String path) {
		List<ObjectValueTO> result = new ArrayList<ObjectValueTO>();		
		ObjectListing objList = s3client.listObjects(bucket, getS3Path(path));
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

		return path + FOLDER_SUFFIX;
	}
}
