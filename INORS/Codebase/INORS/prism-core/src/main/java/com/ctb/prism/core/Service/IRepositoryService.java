package com.ctb.prism.core.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IRepositoryService {
	/**
	 * @param path
	 * @return
	 */
	public List<String> getAssetList(String path);

	/**
	 * @param assetPath
	 * @return
	 * @throws IOException
	 */
	public byte[] getAssetBytes(String assetPath) throws IOException;

	/**
	 * @param key 
	 * @param file
	 */
	public void uploadAsset(String key, File file);
	
	public void removeAsset(String key);
	
	public InputStream getAssetInputStream(String assetPath) throws IOException; 
}
