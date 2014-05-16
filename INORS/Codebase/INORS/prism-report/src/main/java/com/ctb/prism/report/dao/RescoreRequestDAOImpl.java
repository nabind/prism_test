package com.ctb.prism.report.dao;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.jdbc.support.lob.OracleLobHandler;
import org.springframework.stereotype.Repository;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.constant.IApplicationConstants.ROLE_TYPE;
import com.ctb.prism.core.constant.IQueryConstants;
import com.ctb.prism.core.constant.IReportQuery;
import com.ctb.prism.core.dao.BaseDAO;
import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.transferobject.ObjectValueTOMapper;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.core.util.FileUtil;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.report.transferobject.RescoreRequestTO;

@Repository("rescoreRequestDAO")
public class RescoreRequestDAOImpl extends BaseDAO implements IRescoreRequestDAO {
	
	private static final IAppLogger logger = LogFactory.getLoggerInstance(RescoreRequestDAOImpl.class.getName());

	@SuppressWarnings("unchecked")
	public List<RescoreRequestTO> getDnpStudentList(Map<String, Object> paramMap) throws BusinessException {
		
		logger.log(IAppLogger.INFO, "Enter: RescoreRequestDAOImpl - getDnpStudentList()");
		long t1 = System.currentTimeMillis();
		
		List<RescoreRequestTO> dnpStudentList = null;
		final String testAdministrationVal = (String) paramMap.get("testAdministrationVal");
		final String testProgram = (String) paramMap.get("testProgram");
		final String corpDiocese = (String) paramMap.get("corpDiocese");
		final String school = (String) paramMap.get("school");
		final String grade = (String) paramMap.get("grade");
		final UserTO loggedinUserTO = (UserTO) paramMap.get("loggedinUserTO");
		
		try {
			dnpStudentList = (List<RescoreRequestTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = null;
					cs = con.prepareCall("{call " + IQueryConstants.GET_DNP_STUDENT_DETAILS + "}");
					cs.setLong(1, Long.valueOf(loggedinUserTO.getCustomerId()));
					cs.setLong(2, Long.valueOf(testAdministrationVal));
					cs.setLong(3, Long.valueOf(school));
					cs.setLong(4, Long.valueOf(grade));
					cs.registerOutParameter(5, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(6, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					List<RescoreRequestTO> dnpStudentResult = new ArrayList<RescoreRequestTO>();
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(5);
						RescoreRequestTO rescoreRequestTO = null;
						while (rs.next()) {
							rescoreRequestTO = new RescoreRequestTO();
							rescoreRequestTO.setRrfId(rs.getLong("RRF_ID"));
							rescoreRequestTO.setStudentBioId(rs.getLong("STUDENT_BIO_ID"));
							rescoreRequestTO.setStudentFullName(rs.getString("STUDENT_FULL_NAME"));
							rescoreRequestTO.setRequestedDate(rs.getString("REQUESTED_DATE"));
							rescoreRequestTO.setSubtestId(rs.getLong("SUBTESTID"));
							rescoreRequestTO.setSubtestCode(rs.getString("SUBTEST_CODE"));
							rescoreRequestTO.setSessionId(rs.getLong("SESSION_ID"));
							rescoreRequestTO.setPerformanceLevel(rs.getString("PERFORMANCE_LEVEL"));
							rescoreRequestTO.setOriginalScore(rs.getString("ORIGINAL_SCORE"));
							rescoreRequestTO.setItemsetId(rs.getLong("ITEMSETID"));
							rescoreRequestTO.setIsRequested(rs.getString("IS_REQUESTED"));
							rescoreRequestTO.setUserId(rs.getLong("USERID"));
							rescoreRequestTO.setUserName(rs.getString("USERNAME"));
							dnpStudentResult.add(rescoreRequestTO);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return dnpStudentResult;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: RescoreRequestDAOImpl - getDnpStudentList() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return dnpStudentList;
	}
	
}
