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
	

}
