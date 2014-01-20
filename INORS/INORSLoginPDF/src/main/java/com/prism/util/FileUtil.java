package com.prism.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.springframework.util.FileCopyUtils;

/**
 * This class contains various file handling utility methods:
 * <ul>
 * <li>Download a file through client browser</li>
 * <li>Prepare byte array to create a Zip file</li>
 * <li>Prepare byte array to create an Excell file</li>
 * <li>Prepare byte array to merge a list of Pdf files</li>
 * </ul>
 * 
 * @author Amitabha Roy
 * 
 */
public class FileUtil {
	private static final Logger logger = Logger.getLogger(FileUtil.class);

	public static String saveLetterFromFileServer(String docName, List<String> pdfPathList) {
		return FileUtil.createZipFile(docName, pdfPathList);
	}

	/**
	 * Gets the contents of the given input File into a new byte array.
	 * 
	 * @param filePath
	 *            Location of the input file
	 * @return byte array representation of the input file
	 */
	public static byte[] getBytes(String filePath) {
		if (filePath != null && !filePath.isEmpty()) {
			try {
				File in = new File(filePath);
				return FileCopyUtils.copyToByteArray(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "".getBytes();
	}

	/**
	 * Creates a Zip file from the list of file paths.
	 * 
	 * @param zipFileName
	 * @param filePaths
	 */
	public static String createZipFile(String zipFileName, List<String> filePaths) {
		logger.info("Adding " + filePaths.size() + " files in " + zipFileName);
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		String zipFilePath = null;
		try {
			File zipFile = new File(zipFileName);
			fos = new FileOutputStream(zipFile);
			zos = new ZipOutputStream(fos);
			for (String filePath : filePaths) {
				logger.info("filePath = " + filePath);
				String fileName = getFileNameFromFilePath(filePath);
				if (fileName == null) {
					logger.warn("Skipping " + filePath);
					continue;
				}
				ZipEntry entry = new ZipEntry(fileName);
				byte[] input = getBytes(filePath);
				logger.info(input.length + " bytes read");
				entry.setSize(input.length);
				zos.putNextEntry(entry);
				zos.write(input);
				zos.closeEntry();
			}
			zos.close();
			logger.info("Zip file [" + zipFileName + "] created");
			zipFilePath = zipFile.getAbsolutePath();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
				// zos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return zipFilePath;
	}

	/**
	 * Returns the file name from the file path.
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileNameFromFilePath(String filePath) {
		String fileName = null;
		int index = filePath.lastIndexOf('\\');
		if (index != -1) {
			fileName = filePath.substring(index + 1);
		}
		index = filePath.lastIndexOf('/');
		if (index != -1) {
			fileName = filePath.substring(index + 1);
		}
		return fileName;
	}
}
