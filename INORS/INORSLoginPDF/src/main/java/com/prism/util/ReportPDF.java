package com.prism.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import com.lowagie.text.pdf.PdfEncryptor;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

public class ReportPDF {

	/**
	 * Fetches Invitation code letters from priam and save into local
	 * @param nodeId
	 * @param contentId
	 * @param outFileName
	 * @return
	 * @throws IOException
	 */
	public static String saveLetterFromPrismWeb(Properties prop, String schoolId, 
			String elementName, String customerCode, String adminid) throws IOException {

		StringBuffer docBuff = new StringBuffer();
		docBuff.append(prop.getProperty("pdfGenPath")).append(File.separator).append("temp_IC_");
		docBuff.append(elementName).append("_").append(customerCode).append("_");
		docBuff.append(System.currentTimeMillis()).append(".pdf");
		
		StringBuffer URLStringBuf = new StringBuffer();
		URLStringBuf.append(prop.getProperty("jasperURL"));
		URLStringBuf.append(prop.getProperty("jasperURLParams"));
		URLStringBuf.append("&type=pdf&token=0&filter=true&p_L3_Jasper_Org_Id=").append(schoolId);
		URLStringBuf.append("&p_AdminYear=").append(adminid).append("&assessmentId=105_InvLetter");
		
		URL url1 = new URL(URLStringBuf.toString());

		FileOutputStream fos = new FileOutputStream(docBuff.toString());
		InputStream is = null;
		try {
			// Contacting the URL
			System.out.println("\nConnecting to " + url1.toString() + " ... ");
			URLConnection urlConn = url1.openConnection();

			// Checking whether the URL contains a PDF
			if (!urlConn.getContentType().equalsIgnoreCase("application/pdf")) {
				System.out.println(schoolId+" : FAILED.\n[Sorry. This is not a PDF.]");
				return null;
			} else {
				try {
					// Read the PDF from the URL and save to a local file
					is = url1.openStream();
					IOUtils.copy(is, fos);
				} catch (ConnectException ce) {
					System.out.println(schoolId+" : FAILED.\n[" + ce.getMessage() + "]\n");
					return null;
				}
			}

		} catch (NullPointerException npe) {
			System.out.println(schoolId+" : FAILED.\n[" + npe.getMessage() + "]\n");
			return null;
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(fos);
	    }
		return encryptPdf(prop, docBuff.toString(), customerCode, elementName);
	}
	
	/**
	 * This method encrypt pdf with strength: 128bits 
	 * @param prop
	 * @param docLocation
	 * @return
	 */
	private static String encryptPdf(Properties prop, String docLocation, 
			String customerCode, String elementName) {
		System.out.println("encrypting pdf... ");
		String docName = null;
		StringBuffer docBuff = new StringBuffer();
		try {
			docBuff.append(prop.getProperty("pdfGenPath")).append(File.separator).append(prop.getProperty("pdfFileNamePrefixLetter"));
			docBuff.append(elementName).append("_").append(customerCode).append("_");
			docBuff.append(System.currentTimeMillis()).append(".pdf");
			
			docName = docBuff.toString();
			
			// Add the security object to the document
			PdfEncryptor.encrypt(new PdfReader(docLocation), 
					new FileOutputStream(docName), 
					prop.getProperty("pdfPassword").getBytes(), 
					prop.getProperty("pdfOwnerPassword").getBytes(),
					PdfWriter.AllowDegradedPrinting, 
					PdfWriter.STRENGTH128BITS);
		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			removeFile(docLocation);
		}
		return docName;
	}
	
	private static void removeFile(String file) {
		try {
			File pdf = new File(file);
			if(pdf.exists()) pdf.delete();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
