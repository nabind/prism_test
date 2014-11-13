package com.prism.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.lowagie.text.pdf.PdfEncryptor;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

public class ReportPDF {
	private static final Logger logger = Logger.getLogger(ReportPDF.class);

	/**
	 * Fetches Invitation code letters from prism and save into local.
	 * 
	 * @deprecated use {@link new(savePdfFromPrismWeb())} instead.
	 * 
	 * @param prop
	 * @param schoolId
	 * @param elementName
	 * @param customerCode
	 * @param adminid
	 * @param studentBioId
	 * @return
	 * @throws IOException
	 * @Deprecated
	 */
	public static String saveLetterFromPrismWeb(Properties configProperties, Properties istepProperties, String schoolId, String elementName,
			String customerCode, String adminid, String studentBioId, boolean isExtTable, boolean encript) throws IOException {
		StringBuffer docBuff = new StringBuffer();
		docBuff.append(configProperties.getProperty("pdfGenPath")).append(File.separator).append("temp_IC_");
		docBuff.append(elementName).append("_").append(customerCode).append("_");
		docBuff.append(System.currentTimeMillis()).append(".pdf");

		StringBuffer URLStringBuf = new StringBuffer();
		URLStringBuf.append(istepProperties.getProperty("jasperURL"));
		URLStringBuf.append(istepProperties.getProperty("jasperURLParams"));
		URLStringBuf.append("&type=pdf&token=0&filter=true&p_L3_Jasper_Org_Id=").append(schoolId);
		URLStringBuf.append("&p_AdminYear=").append(adminid).append("&assessmentId=105_InvLetter").append("&p_Student_Bio_Id=").append(studentBioId);
		if (isExtTable) {
			URLStringBuf.append("&p_ExtTable=Y");
		} else {
			URLStringBuf.append("&p_ExtTable=N");
		}

		URL url1 = new URL(URLStringBuf.toString());
		FileOutputStream fos = new FileOutputStream(docBuff.toString());
		InputStream is = null;
		try {
			logger.info("Connecting to Report Server for " + studentBioId + " ... ");
			logger.info(URLStringBuf.toString());
			HttpURLConnection urlConn = (HttpURLConnection) url1.openConnection();

			// Checking whether the URL contains a PDF
			if (!urlConn.getContentType().equalsIgnoreCase("application/pdf")) {
				logger.error(schoolId + " : FAILED.\n[Sorry. This is not a PDF.]");
				return null;
			} else {
				try {
					// Read the PDF from the URL and save to a local file
					is = urlConn.getInputStream(); // is = url1.openStream();
					IOUtils.copy(is, fos);
				} catch (ConnectException ce) {
					logger.error(schoolId + " : FAILED.\n[" + ce.getMessage() + "]\n");
					return null;
				}
			}
		} catch (NullPointerException npe) {
			logger.error(schoolId + " : FAILED.\n[" + npe.getMessage() + "]\n");
			return null;
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(fos);
		}
		if (encript) {
			return encryptPdf(configProperties, istepProperties, docBuff.toString(), customerCode, elementName);
		} else {
			return docBuff.toString();
		}
	}

	public static String savePdfFromPrismWeb(String pdfPath, URL url) throws IOException {
		FileOutputStream fos = new FileOutputStream(pdfPath);
		InputStream is = null;
		try {
			logger.info(url);
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

			// Checking whether the URL contains a PDF
			if (!urlConn.getContentType().equalsIgnoreCase("application/pdf")) {
				logger.error("FAILED : This is not a PDF.");
				return null;
			} else {
				try {
					// Read the PDF from the URL and save to a local file
					is = urlConn.getInputStream();
					IOUtils.copy(is, fos);
				} catch (ConnectException e) {
					logger.error("FAILED : " + e.getMessage());
					return null;
				}
			}
		} catch (NullPointerException e) {
			logger.error("FAILED : " + e.getMessage());
			return null;
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(fos);
		}
		return pdfPath;
	}

	/**
	 * This method encrypt pdf with strength: 128bits
	 * 
	 * @param prop
	 * @param docLocation
	 * @return
	 */
	private static String encryptPdf(Properties configProperties, Properties istepProperties, String docLocation, String customerCode, String elementName) {
		logger.info("Encrypting pdf. Please wait... ");
		String docName = null;
		StringBuffer docBuff = new StringBuffer();
		try {
			docBuff.append(configProperties.getProperty("pdfGenPath")).append(File.separator).append(istepProperties.getProperty("pdfFileNamePrefixLetter"));
			docBuff.append(elementName).append("_").append(customerCode).append("_");
			docBuff.append(System.currentTimeMillis()).append(".pdf");
			docName = docBuff.toString();
			// Add the security object to the document
			PdfEncryptor.encrypt(new PdfReader(docLocation), new FileOutputStream(docName), istepProperties.getProperty("pdfPassword").getBytes(),
					istepProperties.getProperty("pdfOwnerPassword").getBytes(), PdfWriter.AllowDegradedPrinting, PdfWriter.STRENGTH128BITS);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			FileUtil.removeFile(docLocation);
		}
		return docName;
	}

}
