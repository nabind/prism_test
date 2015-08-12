package com.ctb.mergeutility.merge;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ctb.mergeutility.common.ApplicationConfig;
import com.ctb.mergeutility.common.ApplicationConstants;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.exceptions.InvalidPdfException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopyFields;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class FileMerger {
	private static final Logger logger = Logger.getLogger(FileMerger.class);
	private static Properties applicationProperies = ApplicationConfig
			.loadApplicationConfig().getConfigProp();

	public FileMerger() {

	}

	public int findOperation(Map<String, String> valueMap) {
		int RESULT = 0;
		String corpId = valueMap.get(ApplicationConstants.CORP_NO);
		String ppImageId = valueMap.get(ApplicationConstants.PP_IMAGE_ID);
		String oasImageId = valueMap.get(ApplicationConstants.OAS_IMAGE_ID);
		if (StringUtils.isBlank(corpId))
			return ApplicationConstants.CORP_NO_NOT_FOUND;
		if (StringUtils.isBlank(ppImageId) && StringUtils.isBlank(oasImageId))
			RESULT = ApplicationConstants.PP_AND_OAS_FILE_NOT_FOUND;
		else if (!StringUtils.isBlank(ppImageId) && !StringUtils.isBlank(oasImageId)) {
			if (ppImageId.equalsIgnoreCase(oasImageId)) {
				RESULT = ApplicationConstants.PP_AND_OAS_IDS_ARE_SAME;
			} else {
				RESULT = ApplicationConstants.PP_AND_OAS_FILE_FOUND;
			}
		} else if (!StringUtils.isBlank(ppImageId) && StringUtils.isBlank(oasImageId))
			RESULT = ApplicationConstants.ONLY_PP_FILE_FOUND;
		else if (StringUtils.isBlank(ppImageId) && !StringUtils.isBlank(oasImageId))
			RESULT = ApplicationConstants.ONLY_OAS_FILE_FOUND;

		return RESULT;
	}

	public boolean copyFile(String src, String dest, String corpNo) {
		boolean flag = false;
		try {
			File sourceFile = new File(src);
			if (!sourceFile.exists()) {
				logger.info("Source file not present at location " + src);
				return false;
			}
			File directory = new File(dest + corpNo);
			if (!directory.isDirectory())
				directory.mkdirs();
			FileUtils.copyFileToDirectory(sourceFile, directory);
			String oldFileName = sourceFile.getName();
			sourceFile = new File(dest + corpNo + File.separator + oldFileName);
			if (oldFileName.contains("oas")) {
				StringBuilder buildr = new StringBuilder(oldFileName);
				oldFileName = buildr.substring(3);
				File destFile = new File(dest + corpNo + File.separator + oldFileName.toUpperCase());
				if (destFile.isFile() && destFile.exists())
					try {
						String path1 = destFile.getCanonicalPath();
						File filePath = new File(path1);
						filePath.delete();
					} catch (IOException e) {
						logger.info(e);
					}
				sourceFile.renameTo(destFile);
			}

		} catch (IOException e) {

			logger.info(e);
		}
		flag = true;
		return flag;
	}

	public int mergePaperPancilAndOas(Map<String, String> valueMap,String destination) {
		int flag = 0;
		boolean success = false;
		String corpId = valueMap.get(ApplicationConstants.CORP_NO);
		String pdfInputExtPP=applicationProperies.getProperty(ApplicationConstants.PDF_INPUT_EXT_PP);
		String pdfInputExtOas=applicationProperies.getProperty(ApplicationConstants.PDF_INPUT_EXT_OAS);
		String pdfOutputExt=applicationProperies.getProperty(ApplicationConstants.PDF_OUTPUT_EXT);
		String fileNamePp = valueMap.get(ApplicationConstants.PP_IMAGE_ID)
				+ "."+pdfInputExtPP;
		String fileNameOas = valueMap.get(ApplicationConstants.OAS_IMAGE_ID) + "."+pdfInputExtOas;
		
		String finalFileName=valueMap.get(ApplicationConstants.PP_IMAGE_ID)
				+ "."+pdfOutputExt;
		String ppFile = applicationProperies
				.getProperty(ApplicationConstants.PP_ROOT)
				+ corpId
				+ File.separator
				+ fileNamePp;
		String oasFile = applicationProperies
				.getProperty(ApplicationConstants.OAS_ROOT)
				+ corpId
				+ File.separator
				+ fileNameOas;
		
		// added new *******************************
		File sourceFile = new File(ppFile);
		if (!sourceFile.exists()) {
			logger.info("PP file not present" + ppFile);
			flag = 11;
		}
		
		sourceFile = new File(oasFile);
		if (!sourceFile.exists()) {
			logger.info("OAS file not present" + oasFile);
			flag = (flag == 11)? 33 : 22;
		}
		if(flag > 0) return flag;
		// end : added new *******************************
		
		File dir = new File(destination);
		if (!dir.isDirectory())
			dir.mkdirs();
		destination +=  File.separator + finalFileName;
		try {
			success = doMerge(ppFile, oasFile, destination);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (success)? 1 : 0;
	}

	public boolean doMerge(String ppfile, String oasFile, String destination) throws DocumentException, IOException {
		boolean flag = false;
		logger.info("Start combine PDF files");
		PdfReader ppPdf = null;
		PdfReader oasPdf = null;
		FileInputStream ppFileInputStream = null;
		FileInputStream oasFileInputStream = null;

		try {
			ppFileInputStream = new FileInputStream(ppfile);
			ppPdf = new PdfReader(ppFileInputStream);
		} catch (Exception pdfException) {
			logger.info("Invalid Paper Pencil PDF " + ppfile);
		}
		try {
			oasFileInputStream = new FileInputStream(oasFile);
			oasPdf = new PdfReader(oasFileInputStream);

		} catch (Exception pdfException) {
			logger.info("Invalid oas  PDF " + oasFile);
		}
		System.out.println("pp pdf:" + ppPdf.toString());
		System.out.println("oas pdf:" + oasPdf.toString());

		if (ppPdf != null && oasPdf != null) {
			FileOutputStream fos = new FileOutputStream(destination);
			PdfCopyFields copy = new PdfCopyFields(fos);
			copy.addDocument(ppPdf);
			copy.addDocument(oasPdf);
			copy.close();
			fos.close();
			ppFileInputStream.close();
			oasFileInputStream.close();
		}

		flag = true;
		return flag;
	}

	public boolean createCsvReport(int ppOnly, int oasOnly, int ppAndOas,
			int notPpNorOas, String grtfileName,String dest, ArrayList<String> missingImagesList) {
		boolean flag = false;
		
		String csvFileLocation = applicationProperies
				.getProperty(ApplicationConstants.WORK_DIR);
		System.out.println("pring missing images list" + missingImagesList);
		//grtfileName = grtfileName.substring(0, grtfileName.toLowerCase()
		//		.indexOf(".DAT".toLowerCase()));
		File csvFileStoreDir = new File(dest);
		if (!csvFileStoreDir.exists())
			csvFileStoreDir.mkdirs();
		String fullyQualifiedCsvFile = dest + "/" + "SUMMARY_FILE" + ".csv";
		
		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(
					fullyQualifiedCsvFile));
			// writing header of report
			String header = ApplicationConstants.TYPE_OF_MATCH
					+ ApplicationConstants.SEPERATOR
					+ ApplicationConstants.MATCH_COUNT;
			bufferedWriter.write(header);
			// adding line to the report file
			String lineOne = ApplicationConstants.COUNT_PP_AND_OAS_FILE
					+ ApplicationConstants.SEPERATOR + ppAndOas;
			String lineTwo = ApplicationConstants.COUNT_PP_FILE_ONLY
					+ ApplicationConstants.SEPERATOR + ppOnly;
			String lineThree = ApplicationConstants.COUNT_OAS_FILE_ONLY
					+ ApplicationConstants.SEPERATOR + oasOnly;
			String lineFour = ApplicationConstants.COUNT_NITHER_PP_NOR_OAS_FILE
					+ ApplicationConstants.SEPERATOR + notPpNorOas;
			
			bufferedWriter.newLine();
			bufferedWriter.write(lineOne);
			bufferedWriter.newLine();
			bufferedWriter.write(lineTwo);
			bufferedWriter.newLine();
			bufferedWriter.write(lineThree);
			bufferedWriter.newLine();
			bufferedWriter.write(lineFour);
			bufferedWriter.newLine();
			bufferedWriter.newLine();
			bufferedWriter.newLine();
			bufferedWriter.newLine();
			
			if (missingImagesList.size() > 0)
			{
				String titleLine = " The list for missing images";
				String headLine = ApplicationConstants.ORGTP_NO
						+ ApplicationConstants.SEPERATOR + ApplicationConstants.CORP_NO 
						+ ApplicationConstants.SEPERATOR + ApplicationConstants.OAS_IMAGE_ID 
						+ ApplicationConstants.SEPERATOR + ApplicationConstants.PP_IMAGE_ID
						+ ApplicationConstants.SEPERATOR + "OAS Image Missing"
						+ ApplicationConstants.SEPERATOR + "PP Image Missing";
				System.out.println("missinglist size:" + missingImagesList.size());
				bufferedWriter.write(titleLine);
				bufferedWriter.newLine();
				bufferedWriter.write(headLine);
				
				for(int i = 0; i < missingImagesList.size(); i=i+6)
				{
			
					String dataLine = missingImagesList.get(i + 0) 
						+ ApplicationConstants.SEPERATOR + missingImagesList.get(i+1)
						+ ApplicationConstants.SEPERATOR + missingImagesList.get(i+2)
						+ ApplicationConstants.SEPERATOR + missingImagesList.get(i+3)
						+ ApplicationConstants.SEPERATOR + missingImagesList.get(i+4)
						+ ApplicationConstants.SEPERATOR + missingImagesList.get(i+5);
					
					bufferedWriter.newLine();
					bufferedWriter.write(dataLine);
			}}
			bufferedWriter.close();
			flag = true;
		} catch (Exception e) {
			logger.info(e);
		}

		return flag;
	}

}// end of class
