package com.prism.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.util.FileCopyUtils;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BadPdfFormatException;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfReader;

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
				logger.info("Adding " + filePath);
				String fileName = getFileNameFromFilePath(filePath);
				if (fileName == null) {
					logger.warn("Skipping " + filePath);
					continue;
				}
				ZipEntry entry = new ZipEntry(fileName);
				byte[] input = getBytes(filePath);
				logger.info(humanReadableByteCount(input.length) + " Data Read");
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

	public static String createZipFile(String zipFileName, String filePath) {
		logger.info("Adding " + filePath + " file in " + zipFileName);
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		String zipFilePath = null;
		try {
			File zipFile = new File(zipFileName);
			fos = new FileOutputStream(zipFile);
			zos = new ZipOutputStream(fos);
			// for (String filePath : filePaths) {
			logger.info("Adding " + filePath);
			String fileName = getFileNameFromFilePath(filePath);
			ZipEntry entry = new ZipEntry(fileName);
			byte[] input = getBytes(filePath);
			logger.info(humanReadableByteCount(input.length) + " Data Read");
			entry.setSize(input.length);
			zos.putNextEntry(entry);
			zos.write(input);
			zos.closeEntry();
			// }
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
	 * Creates a PDF file from the list of file paths.
	 * 
	 * @param zipFileName
	 * @param filePaths
	 */
	public static String createPDFFile(String PDFFileName, List<String> filePaths) {
		logger.info("Adding " + filePaths.size() + " files in " + PDFFileName);
		FileOutputStream fos = null;
		String PDFFilePath = null;
		try {
			File PDFFile = new File(PDFFileName);
			fos = new FileOutputStream(PDFFile);
			int count = 0;
			for (String filePath : filePaths) {
				logger.info("Adding " + ++count + " of " + filePaths.size() + ": " + filePath);
				String fileName = getFileNameFromFilePath(filePath);
				if (fileName == null) {
					logger.warn("Skipping " + filePath);
					continue;
				}
				byte[] input = getBytes(filePath);
				logger.info(humanReadableByteCount(input.length) + " Data Read");
				fos.write(input);
			}
			fos.close();
			logger.info("PDF file [" + PDFFileName + "] created");
			PDFFilePath = PDFFile.getAbsolutePath();
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
		return PDFFilePath;
	}

	/**
	 * Returns the file name from the file path.
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileNameFromFilePath(String filePath) {
		String fileName = filePath;
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

	/**
	 * Cleans up temp files.
	 * 
	 * @param file
	 */
	public static void removeFile(String file) {
		try {
			File pdf = new File(file);
			if (pdf.exists())
				pdf.delete();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Cleans up temp files.
	 * 
	 * @param file
	 */
	public static void removeFile(List<String> files) {
		for (String file : files) {
			try {
				if (file != null) {
					File pdf = new File(file);
					if (pdf.exists())
						pdf.delete();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * @param bytes
	 * @return
	 */
	public static String humanReadableByteCount(long bytes) {
		int unit = 1024;
		if (bytes < unit) {
			return bytes + " B";
		}
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = "KMGTPE".charAt(exp - 1) + "";
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

	/**
	 * @param files
	 * @param rootPath
	 * @return
	 */
	public static byte[] getMergedPdfBytes(List<String> files, String rootPath) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (files != null && !files.isEmpty()) {
			try {
				Document document = new Document();
				PdfCopy copy = new PdfCopy(document, baos);
				document.open();
				PdfReader reader;
				int n;
				// loop over the documents you want to concatenate
				for (String file : files) {
					file = CustomStringUtil.appendString(rootPath, file);
					try {
						reader = new PdfReader(file);
						// loop over the pages in that document
						n = reader.getNumberOfPages();
						for (int page = 0; page < n;) {
							copy.addPage(copy.getImportedPage(reader, ++page));
						}
						if (isOdd(n)) {
							// TODO: This page intentionally left blank.
							copy.addPage(new Rectangle(PageSize.A4), 0);
							copy.add(new Paragraph("This page intentionally left blank."));
						}
						copy.freeReader(reader);
						reader.close();
					} catch (IOException e) {
						logger.info("Skipping " + file);
						logger.warn(file + ": " + e.getMessage());
					} catch (BadPdfFormatException e) {
						logger.info("Skipping " + file);
						logger.error(file + ": " + e.getMessage());
					}
				}
				document.close();
			} catch (DocumentException e) {
				logger.error(e.getMessage());
			}
			logger.info("merged pdf bytes [" + baos.size() + "] created");
		}
		return baos.toByteArray();
	}

	/**
	 * Creates a file in disk.
	 * 
	 * @param fileName
	 * @param input
	 */
	public static void createFile(String fileName, byte[] input) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileName);
			fos.write(input);
			FileCopyUtils.copy(input, new FileOutputStream(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Checks whether an integer is odd or even.
	 * 
	 * @param n
	 * @return
	 */
	public static boolean isOdd(int n) {
		if (n % 2 == 0)
			return false;
		else
			return true;
	}

	public static void moveFiles(String toDir, Set<String> pdfPaths) {
		if (toDir == null || toDir.isEmpty()) {
			logger.error("Error in moving files. Invalid toDir.");
		} else {
			int count = 0;
			for (String pdf : pdfPaths) {
				logger.info("Moving " + ++count + " of " + pdfPaths.size() + " + files: " + pdf);
				try {
					FileUtils.moveFileToDirectory(new File(pdf), new File(toDir), true);
				} catch (IOException e) {
					logger.warn(e.getMessage());
				}
			}
		}
	}

	public static void copyFiles(String toDir, Set<String> pdfPaths) {
		if (toDir == null || toDir.isEmpty()) {
			logger.error("Error in copying files. Invalid toDir.");
		} else {
			int count = 0;
			for (String pdf : pdfPaths) {
				logger.info("Copying " + ++count + " of " + pdfPaths.size() + " + files: " + pdf);
				try {
					FileUtils.copyFileToDirectory(new File(pdf), new File(toDir), true);
				} catch (IOException e) {
					logger.warn(e.getMessage());
				}
			}
		}
	}

	public static String replaceWithRootPath(String rootPath, String filePath) {
		String fileName = getFileNameFromFilePath(filePath);
		return rootPath + fileName;
	}

	public static void copyFile(String toDir, String filePath) {
		if (toDir == null || toDir.isEmpty()) {
			logger.error("Error in copying files. Invalid toDir.");
		} else {
			try {
				FileUtils.copyFileToDirectory(new File(filePath), new File(toDir), true);
			} catch (IOException e) {
				logger.warn(e.getMessage());
			}
		}
	}

	/**
	 * Archive files
	 * 
	 * @param docLocation
	 * @param prop
	 */
	public static void archiveFiles(String docLocation, String arcFilePath) {
		ZipOutputStream zos = null;
		FileOutputStream fos = null;
		FileInputStream in = null;
		try {
			fos = new FileOutputStream(arcFilePath);
			zos = new ZipOutputStream(fos);
			File folder = new File(docLocation);
			File[] listOfFiles = folder.listFiles();
			logger.info("Adding Pdf files into the zip file: " + arcFilePath);
			for (File f : listOfFiles) {
				if (f.isFile()) {
					addToZipFile(f, zos);
				}
			}
			for (File f : listOfFiles) {
				if (f.isFile()) {
					logger.debug(CustomStringUtil.appendString("Deleting temp file : ", f.getAbsolutePath()));
					removeFile(f.getAbsolutePath());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(zos);
			IOUtils.closeQuietly(fos);
			IOUtils.closeQuietly(in);
		}
	}

	/**
	 * Add entries into zip
	 * 
	 * @param f
	 * @param zos
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void addToZipFile(File f, ZipOutputStream zos) throws FileNotFoundException, IOException {
		logger.debug("Writing '" + f.getName() + "' to zip file");
		FileInputStream fis = null;
		try {
			File file = new File(f.getAbsolutePath());
			fis = new FileInputStream(file);
			ZipEntry zipEntry = new ZipEntry(f.getName());
			zos.putNextEntry(zipEntry);
			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zos.write(bytes, 0, length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			zos.closeEntry();
			IOUtils.closeQuietly(fis);
		}
	}

}
