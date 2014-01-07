package com.ctb.prism.core.constant;

/**
 * Contains application level constants 
 *
 */
public interface IApplicationConstants {
	
	// different kind of users
	public static enum USER_TYPE { USER_CTB, USER_SCHOOL, USER_CLASS, USER_PARENT };
	public static enum ROLE_TYPE { ROLE_ACSI, ROLE_SCHOOL, ROLE_CLASS, ROLE_PARENT, ROLE_CTB, ROLE_USER, ROLE_ADMIN, ROLE_OAS };
	
	public static enum PATCH_FOR_SUBTEST { p_PTCS_Subtest_MultiSelect,p_Subtest_Multiselect_School, p_Inview_Subtest_MultiSelect,p_Subtest_MultiSelect,p_Longitudinal_Roster_Subtest_MultiSelect,p_Subtest_MultiSelect_School_Longitudinal_Summary,p_Subtest_Class_MultiSelect,p_Roster_Subtest_MultiSelect, p_Roster_Score_List, p_Score_Type_List, p_PTCS_Roster_Score_School, p_Inview_Roster_Score_School, p_Inview_Comb_Score, p_PTCS_Comb_Score, p_PTCS_Comb_Subtest_MultiSelect, p_Inview_Comb_Subtest_Multiselect,p_Ethnicities,p_Local_Use_Field1,p_Local_Use_Field2,p_Local_Use_Field3,p_Local_Use_Field4,p_Local_Use_Field5,p_Subtest };
	
	public static enum EXTRACT_CATEGORY { TTD, PD, AE };
	public static enum EXTRACT_FILETYPE { DAT, XML, ICL, ISR, IPR, BOTH, CR };
	public static enum REQUEST_TYPE { SDF, SBE, GDF };
	public static enum JOB_STATUS { SU, IP, CO, ER, DE, AR, FT };
	
	// input control default value ALL
	public static final String VALUE_ALL = "ALL";
	
	// Gender default value
	//public static final String VALUE_DEFAULT_GENDER = VALUE_ALL;
	
	// Gifted and Talented default value
	//public static final String VALUE_DEFAULT_GIFTED_TALENTED = VALUE_ALL;
	
	// score type default value
	//public static final String VALUE_DEFAULT_SCORE_TYPE = "NCE";
	
	// Special program default value
	//public static final String VALUE_DEFAULT_SPECIAL_PROGRAM = VALUE_ALL;
	
	// Years in christian schools default value
	//public static final String VALUE_DEFAULT_YEARS_IN_CHRISTIAN_SCHOOLS = VALUE_ALL;
	
	public static final String LOGGED_IN_USER_JASPER_ORG_ID ="\\$[P][{][LoggedInUserJasperOrgId]+[}]";
	public static final String LOGGED_IN_USERNAME ="\\$[P][{][LoggedInUserName]+[}]";
	
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String TOTAL_PAGES = "TOTAL_PAGES";
	
	public static final String JASPER_ORG_PARAM = "LoggedInUserJasperOrgId";
	public static final String JASPER_USER_PARAM = "LoggedInUserName";
	public static final String DATA_TYPE_COLLECTION = "7";
	public static final String DATA_TYPE_TESTBOX = "2";
	public static final String FIELD_TYPE_DATE = "3";
	public static final String EXTENDED_YEAR = "p_ExtendedAdminYear";
	public static final String ETHNICITY_ID = "p_Ethnicities";
	
	public static final String JASPER_PARAM_INITIAL = "P{";
	
	public static final String CURRUSER = "CURRUSER";
	public static final String CURRUSERID = "CURRUSERID";
	public static final String CURR_USER_DISPLAY = "CURRUSERDISPLAY";
	public static final String CURRORG = "CURRORG";
	public static final String CURRORGLVL = "CURRORGLVL";
	public static final String AUTHORITIES = "AUTHORITIES";
	public static final String PREV_ADMIN="PREV_ADMIN";
	public static final String SSO_PREV_ADMIN="SSO_PREV_ADMIN";
	public static final String EMAIL="EMAIL";
	
	public static final String REPORT_HEIGHT = "height";
	public static final String REPORT_WIDTH = "width";
	
	public static final String PARENT_LOGIN = "parent";
	
	public static final String ACTIVE_FLAG = "AC";
	public static final String SS_FLAG = "SS";
	public static final String INACTIVE_FLAG = "IN";
	public static final String DELETED_FLAG = "DE";
	public static final String INPROGRESS_FLAG = "IP";
	public static final String COMPLETED_FLAG = "CP";
	public static final String ERROR_FLAG = "ER";
	
	public static final String DOWNLOAD_TYPE_MERGED = "M";
	public static final String DOWNLOAD_TYPE_SINGLE = "S";
	public static final String DOWNLOAD_MODE_IC = "IC";
	public static final String DOWNLOAD_MODE_GD = "GD";
	
	public static final String FLAG_Y = "Y";
	public static final String FLAG_N = "N";
	
	public static final String HOME_URL_OBJECT = "HOME_URL_OBJECT";
	public static final String FIRST_TIME_LOGIN = "FIRST_TIME_LOGIN";
	
	public static final String REPORT_IMAGE = "REPORT_IMAGE_";
	public static final String CURR_ASSESSMENT = "CURR_ASSESSMENT";
	public static final String IS_INVITATION_PDF = "105_InvLetter";
	
	
	public static final String CHECK_DEFAULT = "checkDefault";
	public static final String CHECK_DEFAULT_NAME = "checkDefaultName";
	public static final String CHECK_SELECTED = "checkSelect";
	public static final String CHECK_SELECTED_NAME = "checkSelectName";
	
	public static final String EMPTY_REPORT = "The report is empty.";
	public static final String EXPORT_AS_XLS = "xls";
	public static final String DEFAULT_USER_ROLE = "ROLE_USER";
	public static final String WILD_CHAR = "%";
	
	public static final String PARENT_REPORT = "PARENT_REPORT";
	public static final String STUDENT_BIO_ID = "STUDENT_BIO";
	
	public static final String ADMIN_YEAR = "AdminYear";
	public static final String ORG_MODE = "ORG_MODE";
	
	public static final String APP_LDAP = "LDAP";
	public static final String APP_DAO = "DAO";
	public static final String RELOAD_USER = "RELOAD_USER";
	
	public static final String INPUT_REMEMBER = "sessionParam_";
	public static final String CLIENT_TYPE = "clientType";
	public static final String SOAP = "SOAP";
	
	public static final String CUSTOMER = "CUSTOMER";
	public static final String REPORT_TYPE_API = "API";
	public static final String REPORT_TYPE_TABLE = "TABLE";
	public static final String REPORT_TYPE_CUSTOM = "CUSTOM";
	public static final String REPORT_TYPE_CUSTOM_NO_FILTER = "NFCUSTOM";
	public static final String REPORT_TYPE_XML = "XML";
	public static final String REPORT_API_URL = "openReportHtml";
	public static final String REPORT_TABLE_URL = "openReportHtmlApi";
	public static final String EMPTY_MESSAGE = "Click here to add text";
	public static final String CHECKED_CHECKBOX_VALUE = "on";
	public static final String DEFAULT_VALUE_DRM_CHECKBOX = "IN";
	public static final String CHECKED_VALUE_DRM_CHECKBOX = "AC";
	public static final int DEFAULT_BATCH_UPDATE_VALUE = -2;
	
	public static final String PRODUCT_NAME = "PRODUCT_NAME";
	public static final String TESTING_SITE = "Testing Site";
	public static final String SCHOOL = "School";
	public static final String TASC_PRODUCT="TASC";
	
	public static final String ROLE_INIT="ROLE_";
	public static final String SSO_ORG="SSO_ORG";
	public static final String SSO_ORG_LEVEL="SSO_ORG_LEVEL";
	public static final String SSO_ADMIN="SSO_ADMIN";
	public static final long DEFAULT_LEVELID_VALUE = -99;
	public static final long DEFAULT_ROLEID_VALUE = -99;
	public static final String LOGGEDIN_USER_DETAILS="LOGGEDIN_USER_DETAILS";
	public static final String PURPOSE="eduCenterUsers";
	public static final long ROLE_USER_ID = 1;
	public static final long ROLE_ADMIN_ID = 3;
	public static final long RECORD_PER_PAGE = 15;
	public static final String LOGIN_AS = "LOGIN_AS";
	public static final String ORG_USER_FLAG = "O";
	public static final String EDU_USER_FLAG = "E";
	
	//System Configuration Message
	public static final String COMMON_LOG_IN = "Common Log In";
	public static final String CHILDREN_OVERVIEW = "Children Overview";
	public static final String INORS_HOME_PAGE = "Inors Home Page";
	public static final String TEACHER_LOG_IN = "Teacher Log In";
	public static final String PARENT_LOG_IN = "Parent Log In";
	public static final String GENERIC_REPORT_NAME = "Generic System Configuration";
	public static final String PRODUCT_SPECIFIC_REPORT_NAME = "Product Specific System Configuration";
	public static final String GENERIC_MESSAGE_TYPE = "GSCM";
	public static final String PRODUCT_SPECIFIC_MESSAGE_TYPE = "PSCM";
	public static final String MORE_INFO_MESSAGE_TYPE = "MI";
	public static final String MORE_INFO = "More Info";
	public static final String GROUP_DOWNLOAD_INSTRUCTION = "Group Download Instruction";
	//Article/Content type
	public static final String CONTENT_TYPE_ACT = "ACT";
	public static final String CONTENT_TYPE_IND = "IND";
	public static final String CONTENT_TYPE_STD = "STD";
	public static final String CHILD_MAP = "CHILD_MAP";
	
}