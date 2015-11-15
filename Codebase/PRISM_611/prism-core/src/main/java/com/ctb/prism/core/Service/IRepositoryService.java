package com.ctb.prism.core.Service;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.ctb.prism.core.transferobject.ObjectValueTO;

public interface IRepositoryService {
	/**
	 * @param path
	 * @return
	 */
	public List<String> getAssetList(String path);
	
	/**
	 * @param path
	 * @return
	 */
	public List<ObjectValueTO> getAssetListWithPath(String path); 

	/**
	 * @param assetPath
	 * @return
	 * @throws IOException
	 */
	public byte[] getAssetBytes(String assetPath) throws IOException;
	
	/**
	 * @param assetPath
	 * @return
	 * @throws Exception
	 */
	public byte[] getAssetBytesByS3Key(String assetPath) throws Exception;

	/**
	 * @param key 
	 * @param file
	 */
	public void uploadAsset(String key, File file);
	
	public void uploadMapAsset(String key, File file);
	
	/**
	 * @param fullyQualifiedS3KeyPrefix
	 * @param file
	 * @throws Exception
	 */
	public void uploadAssetByS3Key(String fullyQualifiedS3KeyPrefix, File file) throws Exception;

	/**
	 * @param key
	 *            full qualified file path
	 * @param is
	 *            an InputStream object
	 */
	public void uploadAsset(String key, InputStream is);
	
	public void removeAsset(String key);
	
	public InputStream getAssetInputStream(String assetPath) throws IOException; 
	
	/**
	 * @author Joykumar Pal
	 * @param key 
	 * @param file
	 */
	public void uploadAssetAsync(String key, File file);
}
