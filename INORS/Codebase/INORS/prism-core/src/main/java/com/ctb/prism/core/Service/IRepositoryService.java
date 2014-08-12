package com.ctb.prism.core.Service;

import java.io.File;
import java.io.IOException;
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
	 * @param file
	 */
	public void uploadAsset(File file);
}
