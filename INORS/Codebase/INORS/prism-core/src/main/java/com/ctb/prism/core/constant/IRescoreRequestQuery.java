package com.ctb.prism.core.constant;

import com.ctb.prism.core.util.CustomStringUtil;

/**
 * This interface contains the constants used in the application
 * Please write Stored Procedure instead of Query
 * @author Joy
 * @version 1.0
 */
public interface IRescoreRequestQuery {

	//public static final String GET_TEST_ADMINISTRATION = "PKG_MANAGE_CONTENT.SP_GET_CUST_PROD_DETAILS(?,?,?,?)";
	public static final String GET_RESCORE_STUDENT_LIST = CustomStringUtil.appendString(
			"SELECT * FROM RESULTS_GRT_FACT",
			" WHERE CUST_PROD_ID = ?",
			" and ORG_NODEID IN (SELECT DISTINCT ORG_NODEID FROM ORG_NODE_DIM WHERE ORG_MODE = ? AND PARENT_ORG_NODEID = ?)",
			" AND GRADEID = ?"
			);

}