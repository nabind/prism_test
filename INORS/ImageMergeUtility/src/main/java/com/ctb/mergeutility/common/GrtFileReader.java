package com.ctb.mergeutility.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class GrtFileReader {

	private static final Logger logger = Logger.getLogger(GrtFileReader.class);

	private static Properties properties = ApplicationConfig
			.loadApplicationConfig().getConfigProp();

	/* Constructor */
	public GrtFileReader() {
	}

	public List<Map<String, String>> read(File grtFile) {
		List<Map<String, String>> valueList = new ArrayList<Map<String, String>>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(grtFile));
			String line = null;
			while ((line = reader.readLine()) != null && (line.length()>1)) {			
				
				valueList.add(getMap(line));
			}
			reader.close();
		} catch (IOException e) {
			logger.info(e);
		}
		return valueList;
	}

	private Map<String, String> getMap(String line) {
		Map<String, String> map = new HashMap<String, String>();		
		int corpid_start_index = Integer.parseInt(properties.getProperty(ApplicationConstants.CORPID_START_INDEX));
		int corpid_end_index = Integer.parseInt(properties.getProperty(ApplicationConstants.CORPID_END_INDEX));
		int pp_image_id_start_index = Integer.parseInt(properties.getProperty(ApplicationConstants.PP_IMAGE_ID_START_INDEX));
		int pp_image_id_end_index = Integer.parseInt(properties.getProperty(ApplicationConstants.PP_IMAGE_ID_END_INDEX));
		int oas_image_id_start_index = Integer.parseInt(properties.getProperty(ApplicationConstants.OAS_IMAGE_ID_START_INDEX));
		int oas_image_id_end_index = Integer.parseInt(properties.getProperty(ApplicationConstants.OAS_IMAGE_ID_END_INDEX));
		int orgtp_start_index = Integer.parseInt(properties.getProperty(ApplicationConstants.ORGTP_START_INDEX));
		int orgtp_end_index = Integer.parseInt(properties.getProperty(ApplicationConstants.ORGTP_END_INDEX));
		
		
		map.put(ApplicationConstants.CORP_NO, line.substring(corpid_start_index, corpid_end_index));
		map.put(ApplicationConstants.ORGTP_NO, line.substring(orgtp_start_index, orgtp_end_index));
		String ppid = line.substring(pp_image_id_start_index, pp_image_id_end_index).trim();
		String oasid = line.substring(oas_image_id_start_index, oas_image_id_end_index).trim();
		if (StringUtils.isBlank(ppid))
			ppid = null;
		if (StringUtils.isBlank(oasid))
			oasid = null;
		map.put(ApplicationConstants.PP_IMAGE_ID, ppid);
		map.put(ApplicationConstants.OAS_IMAGE_ID, oasid);
		return map;
	}

	public Vector<File> findGrtFiles(String readPath) {

		File dir = new File(readPath);
		Vector<File> grtFilesForRread = new Vector<File>();
		if (dir.isDirectory() == false) {
			logger.info("Invalid read directory...");
			return grtFilesForRread;
		}
		File[] fileList = dir.listFiles();
		if (fileList != null) {
			for (File file : fileList) {
				if (file.isFile()
						&& file.getName().toUpperCase().contains(".DAT")) {
					grtFilesForRread.add(file);
				}
			}
		}
		if (grtFilesForRread.size() > 0)
			logger.info("Files for processing found...");
		else
			logger.info("No Print Trigger file found for processing...");

		return grtFilesForRread;
	}

}// end of class
