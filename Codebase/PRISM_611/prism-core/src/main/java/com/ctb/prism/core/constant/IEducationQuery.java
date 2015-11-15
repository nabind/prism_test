package com.ctb.prism.core.constant;

import com.ctb.prism.core.util.CustomStringUtil;

/**
 * This interface contains the constants used in the application
 * 
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public interface IEducationQuery {
	
	public static final String GET_EDU_CENTER_DETAILS = CustomStringUtil
	.appendString(" SELECT C.EDU_CENTERID,C.EDU_CENTER_CODE,C.EDU_CENTER_NAME",
			" FROM EDU_CENTER_DETAILS C WHERE C.CUSTOMERID = ?");
	

	public static final String GET_EDUCENTER_USER_ROLE = CustomStringUtil.appendString(
			" SELECT DISTINCT RLE.ROLEID ROLEID,RLE.ROLE_NAME ROLENAME,'Education Center' ORG_LABEL,RLE.DESCRIPTION ",
			" FROM USER_ROLE URLE, ROLE RLE, USERS USR",
			" WHERE USR.USERID= ? AND USR.USERID = URLE.USERID  AND URLE.ROLEID = RLE.ROLEID");  

}
