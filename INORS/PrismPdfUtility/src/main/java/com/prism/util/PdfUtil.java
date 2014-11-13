package com.prism.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.lowagie.text.pdf.PdfEncryptor;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

public class PdfUtil {

	private static final Logger logger = Logger.getLogger(PdfUtil.class);

	/**
	 * This method encrypts pdf with strength: 128bits
	 * 
	 * @param prop
	 * @param docLocation
	 * @param orgid
	 * @param customerCode
	 * @param elementName
	 * @param state
	 * @param orgNodeLevel
	 * @return
	 */
	public static String encryptPdf(Properties prop, String docLocation, String orgid, String customerCode, String elementName, boolean state,
			final String orgNodeLevel) {
		logger.debug("Encrypting pdf... ");
		String docName = null;
		StringBuffer docBuff = new StringBuffer();
		try {
			String pdfPrefix = (state) ? prop.getProperty("pdfFileNamePrefixLoginState") : prop.getProperty("pdfFileNamePrefixLogin");
			docBuff.append(prop.getProperty("pdfGenPath")).append(File.separator).append(pdfPrefix);
			docBuff.append(elementName).append("_").append(customerCode).append("_").append(orgid).append(".pdf");
			docName = docBuff.toString();
			// Add the security object to the document
			PdfEncryptor.encrypt(new PdfReader(docLocation), new FileOutputStream(docName), prop.getProperty("pdfPasswordLevel" + orgNodeLevel).getBytes(),
					prop.getProperty("pdfOwnerPassword").getBytes(), PdfWriter.AllowDegradedPrinting, PdfWriter.STRENGTH128BITS);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return docName;
	}
}
