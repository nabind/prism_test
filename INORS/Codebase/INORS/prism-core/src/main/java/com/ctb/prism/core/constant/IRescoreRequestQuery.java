package com.ctb.prism.core.constant;

import com.ctb.prism.core.util.CustomStringUtil;

/**
 * This interface contains the constants used in the application
 * Please write Stored Procedure instead of Query
 * @author Joy
 * @version 1.0
 */
public interface IRescoreRequestQuery {

	public static final String GET_DNP_STUDENT_DETAILS = "PKG_RESCORE_REQUEST.SP_GET_DNP_STUDENT_DETAILS(?,?,?,?,?,?)";
	
	public static final String SUBMIT_RESCORE_REQUEST = "PKG_RESCORE_REQUEST.SP_SUBMIT_RESCORE_REQUEST(?,?,?,?,?,?,?,?,?,?)";
	
	public static final String RESET_ITEM_STATE = "PKG_RESCORE_REQUEST.SP_RESET_ITEM_STATE(?,?,?,?,?,?)";
	
	public static final String RESET_ITEM_DATE = "PKG_RESCORE_REQUEST.SP_RESET_ITEM_DATE(?,?,?,?,?)";
	
	public static final String GET_NOT_DNP_STUDENT = "PKG_RESCORE_REQUEST.SP_GET_NOT_DNP_STUDENT(?,?,?,?,?,?)";
	
	public static final String GET_NOT_DNP_STUDENT_DETAILS = "PKG_RESCORE_REQUEST.SP_GET_NOT_DNP_STUDENT_DETAILS(?,?,?,?,?,?,?)";

}