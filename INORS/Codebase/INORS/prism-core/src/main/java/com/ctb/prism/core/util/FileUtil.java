/**
 * 
 */
package com.ctb.prism.core.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.FileCopyUtils;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BadPdfFormatException;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

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
	private static final IAppLogger logger = LogFactory.getLoggerInstance(FileUtil.class.getName());
	// private static final String TEST_DIR = "C:\\Temp\\GroupDownload\\";

	/**
	 * Method to download a file through client browser.
	 * 
	 * @param response
	 * @param filePath
	 */
	public static void browserDownload(HttpServletResponse response, String filePath) {
		try {
			browserDownload(response, getBytes(filePath), getFileNameFromFilePath(filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to download a file through client browser.
	 * 
	 * @param response
	 * @param data
	 * @param fileName
	 */
	public static void browserDownload(HttpServletResponse response, byte[] data, String fileName) {
		browserDownload(response, data, fileName, "application/force-download");
	}

	/**
	 * Method to download a file through client browser.
	 * 
	 * @param response
	 * @param data
	 * @param fileName
	 * @param contentType
	 */
	public static void browserDownload(HttpServletResponse response, byte[] data, String fileName, String contentType) {
		response.setContentType(contentType);
		response.setContentLength(data.length);
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		try {
			FileCopyUtils.copy(data, response.getOutputStream());
			logger.log(IAppLogger.INFO, fileName + "[" + data.length + "] written to output stream");
		} catch (IOException e) {
			logger.log(IAppLogger.ERROR, fileName + " - ", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Displays the content of the byte[] on the browser.
	 * 
	 * @param response
	 * @param data
	 * @throws IOException
	 */
	public static void browserDisplay(HttpServletResponse response, byte[] data) throws IOException {
		FileCopyUtils.copy(data, response.getOutputStream());
	}
	
	/**
	 * Displays the content of the InputStream on the browser.
	 * 
	 * @param response
	 * @param in
	 * @throws IOException
	 */
	public static void browserDisplay(HttpServletResponse response, InputStream in) throws IOException  {
		FileCopyUtils.copy(in, response.getOutputStream());
	}

	/**
	 * Creates a zip byte array from a byte array. The zip byte array can be used to create a zip file.
	 * 
	 * @param filePath
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static byte[] zipBytes(String filePath, byte[] input) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		logger.log(IAppLogger.INFO, "filePath = " + filePath);
		String fileName = getFileNameFromFilePath(filePath);
		if (fileName == null) {
			logger.log(IAppLogger.WARN, "Skipping " + filePath);
			return "".getBytes();
		}
		ZipEntry entry = new ZipEntry(fileName);
		entry.setSize(input.length);
		zos.putNextEntry(entry);
		zos.write(input);
		zos.closeEntry();
		zos.close();
		logger.log(IAppLogger.INFO, "zip bytes [" + baos.size() + "] created");
		return baos.toByteArray();
	}

	/**
	 * Creates a xlsx byte array from a byte array. The xlsx byte array can be used to create an Excel Microsoft Office Open XML Format Spreadsheet file.
	 * 
	 * @param input
	 * @param delimiter
	 * @param headerStyle
	 * @return
	 */
	public static byte[] xlsxBytes(byte[] input, String delimiter, boolean headerStyle) {
		InputStream is = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedReader bfReader = null;
		try {
			is = new ByteArrayInputStream(input);
			bfReader = new BufferedReader(new InputStreamReader(is));
			XSSFWorkbook workBook = new XSSFWorkbook();
			XSSFSheet sheet = workBook.createSheet("sheet");
			String currentLine = null;
			int rowNum = 0;

			// write the excell sheet contents
			while ((currentLine = bfReader.readLine()) != null) {
				String str[] = currentLine.split(delimiter);
				XSSFRow currentRow = sheet.createRow(rowNum++);
				for (int i = 0; i < str.length; i++) {
					XSSFCell currentCell = currentRow.createCell(i);
					currentCell.setCellValue(str[i]);
					if (headerStyle && rowNum == 1) {
						currentCell.setCellStyle(getHeaderStyle(workBook));
					}
				}
			}

			// adjust column width to fit the content
			short minColIx = sheet.getRow(0).getFirstCellNum();
			short maxColIx = sheet.getRow(0).getLastCellNum();
			logger.log(IAppLogger.INFO, "minColIx=" + minColIx + ", maxColIx=" + maxColIx);
			for (short colIx = minColIx; colIx < maxColIx; colIx++) {
				sheet.autoSizeColumn(colIx);
			}

			workBook.write(baos);
			logger.log(IAppLogger.INFO, "xlsx bytes [" + baos.size() + "] created");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (Exception e) {
				logger.log(IAppLogger.WARN, e.getMessage());
			}
			try {
				if (baos != null) {
					baos.close();
				}
			} catch (Exception e) {
				logger.log(IAppLogger.WARN, e.getMessage());
			}
		}
		return baos.toByteArray();
	}

	/**
	 * This method is responsible for setting header font, bold, color, background.
	 * 
	 * @param workBook
	 * @return
	 */
	private static XSSFCellStyle getHeaderStyle(XSSFWorkbook workBook) {
		XSSFFont font = workBook.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setFontName("Arial");
		font.setColor(IndexedColors.WHITE.getIndex());
		font.setBold(true);
		font.setItalic(false);
		XSSFCellStyle hStyle = workBook.createCellStyle();
		hStyle.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		hStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		hStyle.setFont(font);
		return hStyle;
	}

	/**
	 * Gets the contents of the given input File into a new byte array.
	 * 
	 * @param filePath
	 *            Location of the input file
	 * @return byte array representation of the input file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static byte[] getBytes(String filePath) throws FileNotFoundException, IOException {
		logger.log(IAppLogger.INFO, "filePath = " + filePath);
		if (filePath != null && !filePath.isEmpty()) {
			File in = new File(filePath);
			return FileCopyUtils.copyToByteArray(in);
		}
		return "".getBytes();
	}

	/**
	 * Merges multiple Pdf files into a single Pdf file.
	 * 
	 * @param files
	 *            List of Pdf file paths
	 * @return A merged Pdf bytes
	 */
	public static byte[] getMergedPdfBytes(List<String> files) {
		return getMergedPdfBytes(files, "");
	}

	public static byte[] getMergedPdfBytes(List<String> files, String rootPath) {
		if (rootPath.startsWith("/") && !rootPath.endsWith("/")) {
			rootPath = rootPath + "/";
		}
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
						if (Utils.isOdd(n)) {
							copy.addPage(new Rectangle(PageSize.A4), 0); // TODO: This page intentionally left blank.
							copy.add(new Paragraph("This page intentionally left blank."));
						}
						copy.freeReader(reader);
						reader.close();
					} catch (IOException e) {
						logger.log(IAppLogger.INFO, "Skipping " + file);
						logger.log(IAppLogger.WARN, file + ": " + e.getMessage());
					} catch (BadPdfFormatException e) {
						logger.log(IAppLogger.INFO, "Skipping " + file);
						logger.log(IAppLogger.ERROR, file + ": " + e.getMessage());
					}
				}
				document.close();
			} catch (DocumentException e) {
				logger.log(IAppLogger.ERROR, e.getMessage());
			}
			logger.log(IAppLogger.INFO, "merged pdf bytes [" + baos.size() + "] created");
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
	 * Creates a Zip file from the list of file paths.
	 * 
	 * @param zipFileName
	 * @param filePaths
	 * @throws IOException
	 */
	public static void createZipFile(String zipFileName, List<String> filePaths) throws FileNotFoundException, IOException {
		LinkedHashSet<String> filePathSet = new LinkedHashSet<String>(filePaths); // List with no duplicates
		createZipFile(zipFileName, filePathSet);
	}

	/**
	 * Creates a Zip file and places all files from the List of filePaths into it.
	 * 
	 * @param zipFileName
	 *            Zip file name
	 * @param filePaths
	 *            List of file paths with no duplicates
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void createZipFile(String zipFileName, LinkedHashSet<String> filePaths) throws FileNotFoundException, IOException {
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			fos = new FileOutputStream(zipFileName);
			zos = new ZipOutputStream(fos);
			for (String filePath : filePaths) {
				logger.log(IAppLogger.INFO, "filePath = " + filePath);
				String fileName = getFileNameFromFilePath(filePath);
				if (fileName == null) {
					logger.log(IAppLogger.WARN, "Skipping " + filePath);
					continue;
				}
				ZipEntry entry = new ZipEntry(fileName);
				byte[] input = getBytes(filePath);
				logger.log(IAppLogger.INFO, input.length + " bytes read");
				entry.setSize(input.length);
				zos.putNextEntry(entry);
				zos.write(input);
				zos.closeEntry();
			}
			zos.close();
			logger.log(IAppLogger.INFO, "Zip file [" + zipFileName + "] created");
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				logger.log(IAppLogger.WARN, "Not able to close stream.");
			}
		}
	}

	/**
	 * Returns the length of the file.
	 * 
	 * @param filePath
	 * @return
	 */
	public static String fileSize(String filePath) {
		File file = new File(filePath);
		double size = 0;
		DecimalFormat f = new DecimalFormat("##.00");
		if (file.exists()) {
			size = file.length() / 1024 / 1024;
			if (size == 0) {
				size = file.length() / 1024;
				return (f.format(size) + " K");
			} else {
				return (f.format(size) + " M");
			}
		}
		return "0 M";
	}

	public static String fileSize(byte[] fileBytes) {
		double size = 0;
		DecimalFormat f = new DecimalFormat("##.00");
		size = fileBytes.length / 1024 / 1024;
		if (size == 0) {
			size = fileBytes.length / 1024;
			return (f.format(size) + " K");
		} else {
			return (f.format(size) + " M");
		}
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
	 * @param keyWithFileName
	 * @return
	 */
	public static String getDirFromFilePath(String filePath) {
		String dir = filePath;
		int index = filePath.lastIndexOf('\\');
		if (index != -1) {
			dir = filePath.substring(0, index + 1);
		}
		index = filePath.lastIndexOf('/');
		if (index != -1) {
			dir = filePath.substring(0, index + 1);
		}
		return dir.replace('\\', '/').replace("//", "/");
	}

	/**
	 * @param zipFileName
	 * @param filePaths
	 * @throws IOException
	 */
	public static void createDuplexZipFile(String zipFileName, List<String> filePaths) throws IOException {
		LinkedHashSet<String> filePathSet = new LinkedHashSet<String>(filePaths); // List with no duplicates
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			fos = new FileOutputStream(zipFileName);
			zos = new ZipOutputStream(fos);
			for (String filePath : filePathSet) {
				logger.log(IAppLogger.INFO, "filePath = " + filePath);
				String fileName = getFileNameFromFilePath(filePath);
				if (fileName == null) {
					logger.log(IAppLogger.WARN, "Skipping " + filePath);
					continue;
				}
				ZipEntry entry = new ZipEntry(fileName);
				byte[] input = getDuplexPdfBytes(filePath);
				logger.log(IAppLogger.INFO, input.length + " bytes read");
				entry.setSize(input.length);
				zos.putNextEntry(entry);
				zos.write(input);
				zos.closeEntry();
			}
			zos.close();
			logger.log(IAppLogger.INFO, "Zip file [" + zipFileName + "] created");
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				logger.log(IAppLogger.WARN, "Not able to close stream.");
			}
		}
	}

	/**
	 * @param zipFileName
	 *            A Zip file will be created with this name. This Zip file will contain all the Pdf files.
	 * @param filePaths
	 *            The Key of this Map is the Actual File Location and the Value is the system generated Pdf name to be used for that file.
	 * @throws IOException
	 */
	public static void createDuplexZipFile(String zipFileName, Map<String, String> filePaths) throws IOException {
		createDuplexZipFile(zipFileName, filePaths, "");
	}

	public static void createDuplexZipFile(String zipFileName, Map<String, String> filePaths, String rootPath) throws IOException {
		// test start
		// zipFileName = TEST_DIR + getFileNameFromFilePath(zipFileName);
		// test end
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			fos = new FileOutputStream(zipFileName);
			zos = new ZipOutputStream(fos);
			for (Entry<String, String> fileEntry : filePaths.entrySet()) {
				String filePath = CustomStringUtil.appendString(rootPath, fileEntry.getKey());
				String fileName = fileEntry.getValue();
				logger.log(IAppLogger.INFO, "filePath = " + filePath);
				if (fileName == null) {
					logger.log(IAppLogger.WARN, "Skipping " + filePath);
					continue;
				} else if ("".equals(fileName)) {
					fileName = getFileNameFromFilePath(filePath);
				} else if (fileName.endsWith(".pdf")) {
					// No need to append .pdf
				} else {
					fileName = fileName + ".pdf";
				}
				try {
					ZipEntry entry = new ZipEntry(fileName);
					byte[] input = getDuplexPdfBytes(filePath);
					logger.log(IAppLogger.INFO, input.length + " bytes read");
					entry.setSize(input.length);
					zos.putNextEntry(entry);
					zos.write(input);
					zos.closeEntry();
				} catch (Exception e) {
					logger.log(IAppLogger.WARN, "Skiping " + fileName + " in " + zipFileName);
				}
			}
			zos.close();
			logger.log(IAppLogger.INFO, "Zip file [" + zipFileName + "] created");
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				logger.log(IAppLogger.WARN, "Not able to close stream.");
			}
		}
	}

	/**
	 * Reads a Pdf file and creates a byte[]. If the Pdf has odd number of pages then adds a blank page at the end.
	 * 
	 * @param filePath
	 *            Pdf file path with file name
	 * @return
	 */
	private static byte[] getDuplexPdfBytes(String filePath) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			Document document = new Document();
			PdfCopy copy = new PdfCopy(document, baos);
			document.open();
			PdfReader reader = new PdfReader(filePath);
			int n = reader.getNumberOfPages();
			for (int page = 0; page < n;) {
				copy.addPage(copy.getImportedPage(reader, ++page));
			}
			if (Utils.isOdd(n)) {
				copy.addPage(new Rectangle(PageSize.A4), 0); // TODO: This page intentionally left blank.
				copy.add(new Paragraph("This page intentionally left blank."));
			}
			copy.freeReader(reader);
			reader.close();
			document.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	/**
	 * Creates a Pdf file with even number of page. If the directory doesn't exist then it will be created.
	 * 
	 * @param pdfFileName
	 * @param pdfContent
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void createDuplexPdf(String pdfFileName, String pdfContent) throws DocumentException, IOException {
		logger.log(IAppLogger.INFO, "Enter: createDuplexPdf(" + pdfFileName + ")");
		try {
			// test start
			// pdfFileName = TEST_DIR + getFileNameFromFilePath(pdfFileName);
			// test end
			String dir = getDirFromFilePath(pdfFileName);
			logger.log(IAppLogger.INFO, "dir: " + dir);
			File dirLocation = new File(dir);
			if (!dirLocation.exists()) {
				if (dirLocation.mkdirs()) {
					logger.log(IAppLogger.INFO, "Directories created: " + dirLocation);
				} else {
					logger.log(IAppLogger.ERROR, "Failed to create directories: " + dirLocation);
				}
			}
			OutputStream file = new FileOutputStream(new File(pdfFileName));
			Document document = new Document();
			PdfWriter.getInstance(document, file);
			document.open();
			document.add(new Paragraph(pdfContent));
			int n = document.getPageNumber();
			if (Utils.isOdd(n)) {
				document.add(new Rectangle(8.26F, 11.69F)); // TODO: This page intentionally left blank.
				document.add(new Paragraph("This page intentionally left blank."));
			}
			document.close();
			file.close();
		} finally {
			logger.log(IAppLogger.INFO, "Exit: createDuplexPdf()");
		}
	}

	/**
	 * Zip file name is provided by the user from user input text box. If user doesnot provide Zip file name then system will provide a default Zip file name. Pdf file name is always system generated.
	 * This method provides the default Zip file name.
	 * 
	 * @param currentUser
	 * @param groupFile
	 * @return
	 */
	public static String generateDefaultZipFileName(String currentUser, String groupFile) {
		String zipFileName = "";
		String userName = currentUser;
		if(userName.indexOf("9rc^w") != -1) userName = userName.substring(0, userName.indexOf("9rc^w"));
		if (IApplicationConstants.EXTRACT_FILETYPE.ICL.toString().equals(groupFile)) {
			zipFileName = CustomStringUtil.appendString(userName, " ", CustomStringUtil.replaceAll(Utils.getDateTime(), "-", "."), " ", groupFile);
		} else {
			zipFileName = CustomStringUtil.appendString(userName, " ", CustomStringUtil.replaceAll(Utils.getDateTime(), "-", "."));
		}
		return zipFileName;
	}
	
	public static String humanReadableByteCount(long bytes) {
		int unit = 1024;
		if (bytes < unit) {
			return bytes + " B";
		}
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = "KMGTPE".charAt(exp - 1) + "";
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

	public static String getFileSize(String filePath) throws FileNotFoundException, IOException {
		byte[] bytes = getBytes(filePath);
		return humanReadableByteCount(bytes.length);
	}
	
	/**
	 * Arunava
	 * This method creates a password protected zip file in disk.
	 * 
	 * @return
	 */
	public static ZipFile createPasswordProtectedZipFile(String filePath,String newFilePath,String pwd) {
		logger.log(IAppLogger.INFO, "Creating passwordprotected file");
		ZipFile zipFile = null;
		try {
			
			File file = new File(filePath);
			FileCopyUtils.copyToByteArray(file);
			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			parameters.setEncryptFiles(true);
			parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
			parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
			parameters.setPassword(pwd);
			zipFile = new ZipFile(newFilePath);
			zipFile.addFile(file,parameters);
			
			logger.log(IAppLogger.INFO, "SDF Zip file created");
		} catch (IOException e) {
			logger.log(IAppLogger.ERROR, "", e);
			e.printStackTrace();
		} catch (net.lingala.zip4j.exception.ZipException e) {
			logger.log(IAppLogger.ERROR, "", e);
			e.printStackTrace();
		}
		return zipFile;
	}

}
