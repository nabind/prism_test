package com.ctb.prism.core.constant;

/**
 * Contains application level constants 
 *
 */
public interface IApplicationConstants {
	
	// different kind of users
	public static enum USER_TYPE { USER_CTB, USER_SCHOOL, USER_CLASS, USER_PARENT };
	public static enum ROLE_TYPE { ROLE_ACSI, ROLE_SCHOOL, ROLE_CLASS, ROLE_PARENT, ROLE_CTB, ROLE_USER, ROLE_ADMIN };
	
	public static enum PATCH_FOR_SUBTEST { p_PTCS_Subtest_MultiSelect,p_Subtest_Multiselect_School, p_Inview_Subtest_MultiSelect,p_Subtest_MultiSelect,p_Longitudinal_Roster_Subtest_MultiSelect,p_Subtest_MultiSelect_School_Longitudinal_Summary,p_Subtest_Class_MultiSelect,p_Roster_Subtest_MultiSelect, p_Roster_Score_List, p_Score_Type_List, p_PTCS_Roster_Score_School, p_Inview_Roster_Score_School, p_Inview_Comb_Score, p_PTCS_Comb_Score, p_PTCS_Comb_Subtest_MultiSelect, p_Inview_Comb_Subtest_Multiselect };
	
	// input control default value ALL
	//public static final String VALUE_ALL = "ALL";
	
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
	public static final String DATA_TYPE_COLLECTION = "7";
	public static final String DATA_TYPE_TESTBOX = "2";
	public static final String EXTENDED_YEAR = "p_ExtendedAdminYear";
	public static final String ETHNICITY_ID = "p_Ethnicity";
	
	public static final String JASPER_PARAM_INITIAL = "P{";
	
	public static final String CURRUSER = "CURRUSER";
	public static final String CURRUSERID = "CURRUSERID";
	public static final String CURR_USER_DISPLAY = "CURRUSERDISPLAY";
	public static final String CURRORG = "CURRORG";
	public static final String AUTHORITIES = "AUTHORITIES";
	public static final String PREV_ADMIN="PREV_ADMIN";
	
	public static final String REPORT_HEIGHT = "height";
	public static final String REPORT_WIDTH = "width";
	
	public static final String PARENT_LOGIN = "parent";
	
	public static final String ACTIVE_FLAG = "AC";
	public static final String INACTIVE_FLAG = "IN";
	public static final String DELETED_FLAG = "DE";
	
	public static final String FLAG_Y = "Y";
	public static final String FLAG_N = "N";
	
	public static final String HOME_URL_OBJECT = "HOME_URL_OBJECT";
	public static final String FIRST_TIME_LOGIN = "FIRST_TIME_LOGIN";
	
	public static final String REPORT_IMAGE = "REPORT_IMAGE_";
	public static final String CURR_ASSESSMENT = "CURR_ASSESSMENT";
	public static final String IS_INVITATION_PDF = "105_InvLetter";
	
	
	public static final String CHECK_DEFAULT = "checkDefault";
	public static final String CHECK_DEFAULT_NAME = "checkDefaultName";
	
	public static final String EMPTY_REPORT = "The report is empty.";
	public static final String EXPORT_AS_XLS = "xls";
	public static final String DEFAULT_USER_ROLE = "ROLE_USER";
	
	public static final String PARENT_REPORT = "PARENT_REPORT";
	public static final String STUDENT_BIO_ID = "STUDENT_BIO";
	
	public static final String ADMIN_YEAR = "AdminYear";
}
