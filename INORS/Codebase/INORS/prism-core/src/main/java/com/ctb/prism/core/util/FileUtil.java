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
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.FileCopyUtils;

import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
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
	private static final IAppLogger logger = LogFactory.getLoggerInstance(FileUtil.class.getName());

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
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			FileCopyUtils.copy(data, response.getOutputStream());
			logger.log(IAppLogger.INFO, fileName + "[" + data.length + "] written to output stream");
		} catch (IOException e) {
			logger.log(IAppLogger.ERROR, fileName + " - ", e);
			e.printStackTrace();
		}
	}

	/**
	 * Creates a zip byte array from a byte array. The zip byte array can be used to create a zip file.
	 * 
	 * @param filename
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static byte[] zipBytes(String filename, byte[] input) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		ZipEntry entry = new ZipEntry(filename);
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
	 */
	public static byte[] getBytes(String filePath) {
		logger.log(IAppLogger.INFO, "filePath = " + filePath);
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
	 * Merges multiple Pdf files into a single Pdf file.
	 * 
	 * @param files
	 *            List of Pdf file paths
	 * @return A merged Pdf bytes
	 */
	public static byte[] getMergedPdfBytes(List<String> files) {
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
					reader = new PdfReader(file);
					// loop over the pages in that document
					n = reader.getNumberOfPages();
					for (int page = 0; page < n;) {
						copy.addPage(copy.getImportedPage(reader, ++page));
					}
					copy.freeReader(reader);
					reader.close();
				}
				document.close();
				logger.log(IAppLogger.INFO, "merged pdf bytes [" + baos.size() + "] created");
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
	 */
	public static void createZipFile(String zipFileName, List<String> filePaths) {
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			fos = new FileOutputStream(zipFileName);
			zos = new ZipOutputStream(fos);
			for (String filePath : filePaths) {
				ZipEntry entry = new ZipEntry(filePath);
				byte[] input = getBytes(filePath);
				entry.setSize(input.length);
				zos.putNextEntry(entry);
				zos.write(input);
				zos.closeEntry();
			}
			zos.flush();
			logger.log(IAppLogger.INFO, "Zip file [" + zipFileName + "] created");
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
	}

	/**
	 * Returns the length of the file.
	 * 
	 * @param filePath
	 * @return
	 */
	public static long fileSize(String filePath) {
		long size = 0;
		File file = new File(filePath);
		size = file.length();
		return size;
	}
}
