/**
 * 
 */
package com.ctb.prism.core.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;

/**
 * @author 165505
 * 
 */
public class FileUtil {
	private static final IAppLogger logger = LogFactory
			.getLoggerInstance(FileUtil.class.getName());

	public static byte[] zipBytes(String filename, byte[] input)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		ZipEntry entry = new ZipEntry(filename);
		entry.setSize(input.length);
		zos.putNextEntry(entry);
		zos.write(input);
		zos.closeEntry();
		zos.close();
		logger.log(IAppLogger.INFO, "zip bytes created");
		return baos.toByteArray();
	}

	public static byte[] xlsxBytes(byte[] input, String delimiter) {
		InputStream is = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedReader bfReader = null;
		try {
			is = new ByteArrayInputStream(input);
			bfReader = new BufferedReader(new InputStreamReader(is));
			XSSFWorkbook workBook = new XSSFWorkbook();
			XSSFSheet sheet = workBook.createSheet("sheet1");
			String currentLine = null;
			int rowNum = 0;
			while ((currentLine = bfReader.readLine()) != null) {
				String str[] = currentLine.split(delimiter);
				XSSFRow currentRow = sheet.createRow(rowNum++);
				for (int i = 0; i < str.length; i++) {
					currentRow.createCell(i).setCellValue(str[i]);
				}
			}
			workBook.write(baos);
			logger.log(IAppLogger.INFO, "xlsx bytes created");
		} catch (IOException e) {
			logger.log(IAppLogger.ERROR, e.getMessage());
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
}
