package com.ctb.mergeutility.common;

/**/

public final class ApplicationConstants {

	public static final String READ_DIRECTORY = "READ_DIRECTORY";
	public static final String WORK_DIR = "WORK_DIR";
	public static final String FILE_PATH = "FILE_PATH";
	public static final String CORP_NO = "CORP_NO";
	public static final String ORGTP_NO = "ORGTP_NO";
	public static final String PP_IMAGE_ID = "PP_IMAGE_ID";
	public static final String OAS_IMAGE_ID = "OAS_IMAGE_ID";
	public static final String PDF_INPUT_EXT_PP = "PDF_INPUT_EXT_PP";
	public static final String PDF_INPUT_EXT_OAS = "PDF_INPUT_EXT_OAS";
	public static final String PDF_OUTPUT_EXT = "PDF_OUTPUT_EXT";

	/*Used to switch case statement...*/
	public static final int CORP_NO_NOT_FOUND = 0;
	public static final int ONLY_PP_FILE_FOUND = 1;
	public static final int ONLY_OAS_FILE_FOUND = 2;
	public static final int PP_AND_OAS_FILE_NOT_FOUND = 3;
	public static final int PP_AND_OAS_FILE_FOUND = 4;
	public static final int PP_AND_OAS_IDS_ARE_SAME = 5;
	
	public static final String PP_ROOT = "PP_ROOT";
	public static final String OAS_ROOT = "OAS_ROOT";
	public static final String FINAL_DESINATION = "FINAL_DESINATION";
	public static final String STOPER = "stopper";
	public static final String STOPPER_SGL = "SGL";
	public static final String CSV_REPORT_DIRECTORY = "CSV_REPORT_DIRECTORY";
	
	/* index value mapping */
	
	public static final String CORPID_START_INDEX = "CORPID_START_INDEX";
	public static final String CORPID_END_INDEX = "CORPID_END_INDEX";
	public static final String ORGTP_START_INDEX = "ORGTP_START_INDEX";
	public static final String ORGTP_END_INDEX = "ORGTP_END_INDEX";
	public static final String PP_IMAGE_ID_START_INDEX = "PP_IMAGE_ID_START_INDEX";
	public static final String PP_IMAGE_ID_END_INDEX = "PP_IMAGE_ID_END_INDEX";
	public static final String OAS_IMAGE_ID_START_INDEX = "OAS_IMAGE_ID_START_INDEX";
	public static final String OAS_IMAGE_ID_END_INDEX = "OAS_IMAGE_ID_END_INDEX";

	/* CSV File Header... */

	public static final String COUNT_PP_FILE_ONLY = "Only PP Id Present";
	public static final String COUNT_OAS_FILE_ONLY = "Only OAS Id Present";
	public static final String COUNT_PP_AND_OAS_FILE = "Both PP and OAS ID Present";
	public static final String COUNT_NITHER_PP_NOR_OAS_FILE = "Both PP and OAS ID Absent";
	public static final String TYPE_OF_MATCH = "TYPE OF MATCH";
	public static final String MATCH_COUNT = "COUNT";
	public static final String SEPERATOR = ",";

}// end of class
