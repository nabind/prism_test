package com.vaannila.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import com.vaannila.DAO.CommonDAOImpl;
import com.vaannila.TO.ContentTO;
import com.vaannila.TO.OrgTO;
import com.vaannila.util.PropertyFile;


public class RosterPDF {

	/**
	 * Fetches roster reports from jasper
	 * @param nodeId
	 * @param contentId
	 * @param outFileName
	 * @return
	 * @throws IOException
	 */
	public static String saveRosterFromJasper(String nodeId, String contentId, String outFileName, boolean roster) throws IOException {

		StringBuffer URLStringBuf = new StringBuffer();
		Properties prop = PropertyFile.loadProperties("nd.properties");
		URLStringBuf.append(prop.getProperty("jasperURL"));
		if(roster) {
			URLStringBuf.append(prop.getProperty("jasperURLParams"));
		} else {
			URLStringBuf.append(prop.getProperty("growthURLParams"));
		}
		URLStringBuf.append("&j_username=").append(prop.getProperty("jasperUsername"));
		URLStringBuf.append("&j_password=").append(prop.getProperty("jasperPassword"));
		
		URLStringBuf.append("&p_org_nodeid=").append(nodeId);
		if(roster) URLStringBuf.append("&p_contentid=").append(contentId);
		
		URL url1 = new URL(URLStringBuf.toString());

		FileOutputStream fos = new FileOutputStream(outFileName);
		InputStream is = null;
		try {
			// Contacting the URL
			System.out.println("\nConnecting to " + url1.toString() + " ... ");
			URLConnection urlConn = url1.openConnection();

			// Checking whether the URL contains a PDF
			if (!urlConn.getContentType().equalsIgnoreCase("application/pdf")) {
				System.out.println(nodeId+" : FAILED.\n[Sorry. This is not a PDF.]");
				return "Error getting PDF from Jasper";
			} else {
				try {
					// Read the PDF from the URL and save to a local file
					is = url1.openStream();
					IOUtils.copy(is, fos);
				} catch (ConnectException ce) {
					System.out.println(nodeId+" : FAILED.\n[" + ce.getMessage() + "]\n");
					return "Error getting PDF from Jasper";
				}
			}

		} catch (NullPointerException npe) {
			System.out.println(nodeId+" : FAILED.\n[" + npe.getMessage() + "]\n");
			return "Error getting PDF from Jasper";
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(fos);
	    }
		return null;
	}
	
	public static void main(String[] args) {
		try {
			CommonDAOImpl commonDao = new CommonDAOImpl();
			List<OrgTO> allNodes = commonDao.getAllNodes();
			List<String> fileNameList = new LinkedList();
			Properties prop = PropertyFile.loadProperties("nd.properties");
			String exportLocation = prop.getProperty("imageSaveLoc");
			
			java.util.List<InputStream> pdfs = new ArrayList<InputStream>();
			
			for(OrgTO org : allNodes) {
				System.out.println("Fetching Org Node Id # "+org.getNodeId() + " : " + org.getSchoolName());
				String fileName1 = "1002"+ org.getNodeId() + "_" + 
									org.getDistrictNumber() + "_" + 
									org.getSchoolNumber() + "_" + System.currentTimeMillis() + ".pdf";
				
				String fileName2 = "1001"+ org.getNodeId() + "_" + 
				org.getDistrictNumber() + "_" + 
				org.getSchoolNumber() + "_" + System.currentTimeMillis() + ".pdf";
				
				String msg = saveRosterFromJasper(""+org.getNodeId(), "1002", fileName1, true);
				msg = saveRosterFromJasper(""+org.getNodeId(), "1001", fileName2, true);
				
				if(msg != null) {
					System.out.println("ERROR : "+msg);
				} else {
					fileNameList.add(fileName1);
					fileNameList.add(fileName2);
				}
				//break;
			}
			
			for(String file : fileNameList) {
				pdfs.add(new FileInputStream( file ));
			}
			System.out.println("merging all pdf");
			OutputStream output = output = new FileOutputStream(exportLocation + "Roster.pdf");
			ConcatPdf.concatPDFs(pdfs, output, false);
			//commonDao.updatePdfLocation(exportLocation + ".pdf", nodeId, true);
			
			System.out.println("Completed !!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
