package com.ctb.prism.report.dao;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
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
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.transferobject.ObjectValueTOMapper;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.core.util.FileUtil;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.report.transferobject.AssessmentTO;
import com.ctb.prism.report.transferobject.GroupDownloadStudentTO;
import com.ctb.prism.report.transferobject.GroupDownloadTO;
import com.ctb.prism.report.transferobject.InputControlTO;
import com.ctb.prism.report.transferobject.JobTrackingTO;
import com.ctb.prism.report.transferobject.ManageMessageTO;
import com.ctb.prism.report.transferobject.ManageMessageTOMapper;
import com.ctb.prism.report.transferobject.ObjectValueTO;
import com.ctb.prism.report.transferobject.QuerySheetTO;
import com.ctb.prism.report.transferobject.ReportMessageTO;
import com.ctb.prism.report.transferobject.ReportParameterTO;
import com.ctb.prism.report.transferobject.ReportTO;
//import com.googlecode.ehcache.annotations.Cacheable;
//import com.googlecode.ehcache.annotations.TriggersRemove;

/**
 * This class is responsible for reading and writing to database. The transactions through this class should be related to report only.
 */
@Repository("reportDAO")
public class ReportDAOImpl extends BaseDAO implements IReportDAO {
	private static final IAppLogger logger = LogFactory.getLoggerInstance(ReportDAOImpl.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#getFilledReport(net.sf.jasperreports.engine.JasperReport, java.util.Map)
	 */
	@Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).string(#jasperReport.name)).concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#parameters)) )")
	public JasperPrint getFilledReport(JasperReport jasperReport, Map<String, Object> parameters) throws Exception {
		Connection conn = null;
		logger.log(IAppLogger.INFO, CustomStringUtil.appendString("####----------------------------JASPER--PRINT-----------------------------", jasperReport.getName()));
		try {
			conn = getPrismConnection();
			return JasperFillManager.fillReport(jasperReport, parameters, conn);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
				}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#getFilledReportNoCache(net.sf.jasperreports.engine.JasperReport, java.util.Map)
	 */
	@Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).string(#jasperReport.name)).concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#parameters)) )")
	public JasperPrint getFilledReportNoCache(JasperReport jasperReport, Map<String, Object> parameters) throws Exception {
		Connection conn = null;
		logger.log(IAppLogger.INFO, CustomStringUtil.appendString("####----------------------------JASPER--PRINT-----------------------------", jasperReport.getName()));
		try {
			conn = getPrismConnection();
			return JasperFillManager.fillReport(jasperReport, parameters, conn);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
				}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#getReportJasperObject(java.lang.String)
	 */
	public JasperReport getReportJasperObject(final String reportPath) {
		logger.log(IAppLogger.INFO, "Enter: ReportDAOImpl - getReportJasperObject");

		Object[] param = new String[] { reportPath };
		return getJdbcTemplate().queryForObject(IQueryConstants.GET_JASPER_REPORT_OBJECT, param, new RowMapper<JasperReport>() {
			public JasperReport mapRow(ResultSet rs, int col) throws SQLException {
				JasperReport jasperReport = null;
				try {
					jasperReport = JasperCompileManager.compileReport(rs.getBinaryStream(1));
				} catch (JRException e) {
					logger.log(IAppLogger.WARN, "Could not compile report jrxml retrieved from database for report " + reportPath, e);
				}
				logger.log(IAppLogger.INFO, "Exit: ReportDAOImpl - getReportJasperObject");
				return jasperReport;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#getReportJasperObjectForName(java.lang.String)
	 */
	public JasperReport getReportJasperObjectForName(final String reportname) {
		logger.log(IAppLogger.INFO, "Enter: ReportDAOImpl - getReportJasperObject");

		Object[] param = new String[] { reportname };
		return getJdbcTemplate().queryForObject(IQueryConstants.GET_JASPER_REPORT_OBJECT_FOR_NAME, param, new RowMapper<JasperReport>() {
			public JasperReport mapRow(ResultSet rs, int col) throws SQLException {
				JasperReport jasperReport = null;
				try {
					jasperReport = JasperCompileManager.compileReport(rs.getBinaryStream(1));
				} catch (JRException e) {
					logger.log(IAppLogger.WARN, "Could not compile report jrxml retrieved from database for report " + reportname, e);
				}
				logger.log(IAppLogger.INFO, "Exit: ReportDAOImpl - getReportJasperObject");
				return jasperReport;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#removeReportCache()
	 */
	//@CacheEvict(value ={ "compiledJrxml"}, allEntries = true)
	@com.googlecode.ehcache.annotations.TriggersRemove(cacheName="compiledJrxml", removeAll=true)
	public void removeReportCache() {
		logger.log(IAppLogger.INFO, "Removed report cache");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#removeCache()
	 */
	@CacheEvict(value = { "defaultCache", "adminCache" }, allEntries = true)
	public void removeCache() {
		logger.log(IAppLogger.INFO, "Removed all cache");
	}
	
	@CacheEvict(value = {"configCache"}, allEntries = true)
	public void removeConfigurationCache() {
		logger.log(IAppLogger.INFO, "Removed config cache");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#getReportJasperObjectList(java.lang.String)
	 */
	//@Cacheable(value = "compiledJrxml", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).string(#reportPath)).concat(#root.method.name) )")
	@com.googlecode.ehcache.annotations.Cacheable(cacheName = "compiledJrxml")
	public List<ReportTO> getReportJasperObjectList(final String reportPath) throws JRException {
		logger.log(IAppLogger.INFO, "Enter: ReportDAOImpl - getReportJasperObject");

		Object[] param = new String[] { reportPath };
		
		return getJdbcTemplate().query(IQueryConstants.GET_JASPER_REPORT_OBJECT, param, new RowMapper<ReportTO>() {
			public ReportTO mapRow(ResultSet rs, int col) throws SQLException {
				long start = System.currentTimeMillis();
				ReportTO reportTo = null;
				JasperReport jasperReport = null;
				reportTo = new ReportTO();
				try {
					String objectType = rs.getString(1);
					if ("jrxml".equals(objectType)) {
						jasperReport = JasperCompileManager.compileReport(rs.getBinaryStream(2));
						reportTo.setCompiledReport(jasperReport);
						reportTo.setJrxml(true);
					} else {
						OracleLobHandler lobHandler = new OracleLobHandler();
						InputStream inputStream = lobHandler.getBlobAsBinaryStream(rs, "DATA");
						reportTo.setImage(inputStream);
						reportTo.setJrxml(false);
					}
					String reportLabel = rs.getString(3);

					if ("Main jrxml".equalsIgnoreCase(reportLabel)) {
						reportTo.setMainReport(true);
					} else {
						reportTo.setMainReport(false);
					}
				} catch (JRException e) {
					try {
						throw new JRException(e.getMessage());
					} catch (JRException e1) {
						e1.printStackTrace();
					}
					logger.log(IAppLogger.WARN, "Could not compile report jrxml retrieved from database for report " + reportPath, e);
				}
				long end = System.currentTimeMillis();
				//System.out.println("<<<< Time Taken: ReportDAOImpl : getReportJasperObjectList >>>>" + CustomStringUtil.getHMSTimeFormat(end - start));
				
				logger.log(IAppLogger.INFO, "<<<< Time Taken: ReportDAOImpl : getReportJasperObjectList >>>>" + CustomStringUtil.getHMSTimeFormat(end - start));
				logger.log(IAppLogger.INFO, "Exit: ReportDAOImpl - getReportJasperObject");
				return reportTo;
			}
		});
	}

	/*
	 * public List<ReportTO> getReportJasperObjectList(final String reportPath) throws JRException { logger.log(IAppLogger.INFO, "Enter: ReportDAOImpl - getReportJasperObject"); List<ReportTO>
	 * reportList = null; Object[] param = new String[]{ reportPath }; List<Map<String, Object>> lstData = getJdbcTemplate().queryForList(IQueryConstants.GET_JASPER_REPORT_OBJECT, param); if (
	 * lstData.size() > 0 ) { reportList = new ArrayList<ReportTO>(); JasperReport jasperReport = null; for (Map<String, Object> fieldDetails : lstData) { Blob data = (Blob) fieldDetails.get("DATA");
	 * java.io.InputStream in = null; try { in = data.getBinaryStream(); } catch (SQLException e) { e.printStackTrace(); } if(in != null) jasperReport = JasperCompileManager.compileReport(in); String
	 * reportLabel = (String) fieldDetails.get("LABEL"); ReportTO reportTo = new ReportTO(); reportTo.setCompiledReport(jasperReport); if("Main jrxml".equalsIgnoreCase(reportLabel)) {
	 * reportTo.setMainReport(true); } else { reportTo.setMainReport(false); } reportList.add(reportTo); } } return reportList; }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#getInputControlDetails(java.lang.String)
	 */
	@Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).string(#reportPath)).concat(#root.method.name) )")
	public List<InputControlTO> getInputControlDetails(String reportPath) {
		logger.log(IAppLogger.INFO, "Enter: ReportDAOImpl - getInputControlDetails");

		List<InputControlTO> inputControlTOs = null;
		Object[] param = new String[] { reportPath };
		int[] types = new int[] { Types.VARCHAR };
		List<Map<String, Object>> lstData = getJdbcTemplate().queryForList(IQueryConstants.GET_INPUT_CONTROL_DETAILS, param, types);
		if (lstData.size() > 0) {
			inputControlTOs = new ArrayList<InputControlTO>();
			for (Map<String, Object> fieldDetails : lstData) {
				InputControlTO to = new InputControlTO();
				to.setControlId(((BigDecimal) fieldDetails.get("CONTROLID")).longValue());
				to.setDatasource((BigDecimal) fieldDetails.get("DATASOURCE") != null ? ((BigDecimal) fieldDetails.get("DATASOURCE")).toString() : null);
				to.setDataType((BigDecimal) fieldDetails.get("DATATYPE") != null ? ((BigDecimal) fieldDetails.get("DATATYPE")).toString() : null);
				to.setLabel((String) fieldDetails.get("LBL"));
				to.setLabelId((String) fieldDetails.get("LABELID"));
				to.setMandatory(((BigDecimal) fieldDetails.get("MANDATORY")).intValue() == 1 ? true : false);
				to.setQueryValueColumn((String) fieldDetails.get("QUERY_VALUE_COLUMN"));
				to.setReadonly(((BigDecimal) fieldDetails.get("READ_ONLY")).intValue() == 1 ? true : false);
				to.setSequence(((BigDecimal) fieldDetails.get("SEQ")).intValue());
				to.setType(((BigDecimal) fieldDetails.get("TYPE")).toString());
				to.setVisible(((BigDecimal) fieldDetails.get("VISIBLE")).intValue() == 1 ? true : false);

				String strListOfValues = (String) fieldDetails.get("LIST_OF_VALUES");
				if (strListOfValues != null) {
					String[] values = strListOfValues.split(",");
					to.setListOfValues(Arrays.asList(values));
				} else {
					to.setQuery((String) fieldDetails.get("SQL_QUERY"));
				}
				to.setFieldType((fieldDetails.get("FIELD_TYPE") != null) ? ((BigDecimal) fieldDetails.get("FIELD_TYPE")).toString() : null);
				to.setMinLength((fieldDetails.get("MIN_LENGTH") != null) ? fieldDetails.get("MIN_LENGTH").toString() : null);
				to.setMaxLength((fieldDetails.get("MAX_LENGTH") != null) ? fieldDetails.get("MAX_LENGTH").toString() : null);
				inputControlTOs.add(to);
			}
		}

		logger.log(IAppLogger.INFO, "Exit: ReportDAOImpl - getInputControlDetails");
		return inputControlTOs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#getAllInputControls()
	 */
	public List<InputControlTO> getAllInputControls() {
		logger.log(IAppLogger.INFO, "Enter: ReportDAOImpl - getAllInputControls");

		List<InputControlTO> inputControlTOs = null;
		// Object[] param = new String[]{ reportPath };
		// int[] types = new int[]{Types.VARCHAR};
		List<Map<String, Object>> lstData = getJdbcTemplate().queryForList(IQueryConstants.GET_ALL_INPUT_CONTROLS);
		if (lstData.size() > 0) {
			inputControlTOs = new ArrayList<InputControlTO>();
			for (Map<String, Object> fieldDetails : lstData) {
				InputControlTO to = new InputControlTO();
				to.setLabelId((String) fieldDetails.get("LABELID"));
				inputControlTOs.add(to);
			}
		}
		logger.log(IAppLogger.INFO, "Exit: ReportDAOImpl - getAllInputControls");
		return inputControlTOs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#getValuesOfSingleInput(java.lang.String)
	 */
	@Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).string(#query)).concat(#root.method.name) )")
	public List<ObjectValueTO> getValuesOfSingleInput(String query) {
		if (query == null)
			return null;
		logger.log(IAppLogger.DEBUG, query);
		List<ObjectValueTO> list = getJdbcTemplatePrism().query(query, new RowMapper<ObjectValueTO>() {
			public ObjectValueTO mapRow(ResultSet rs, int col) throws SQLException {
				ObjectValueTO to = new ObjectValueTO();
				to.setValue(rs.getString(1));
				to.setName(rs.getString(2));
				return to;
			}
		});
		return list;
	}

	/**
	 * Returns E if the username is an Education Center User. Returns O if the username is an Org User Else returns null.
	 * 
	 * @param username
	 * @return
	 */
	private String getUserType(String username) {
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_USER_TYPE, username);
		if (!lstData.isEmpty()) {
			for (Map<String, Object> fieldDetails : lstData) {
				return fieldDetails.get("USER_TYPE").toString();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#getTenantId(java.lang.String)
	 */
	@Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).string(#userName)).concat('getTenantId') )")
	public String getTenantId(String userName) {
		String userType = getUserType(userName);
		logger.log(IAppLogger.INFO, "Enter: ReportDAOImpl - userType = " + userType);

		if (IApplicationConstants.EDU_USER_FLAG.equals(userType)) {
			return getJdbcTemplatePrism().queryForObject(IQueryConstants.GET_EDU_TENANT_ID, new Object[] { /* userParameter[0] */userName }, new RowMapper<String>() {
				public String mapRow(ResultSet rs, int col) throws SQLException {
					return ((BigDecimal) rs.getObject(1)).toString();
				}
			});
		} else {
			return getJdbcTemplatePrism().queryForObject(IQueryConstants.GET_TENANT_ID, new Object[] { userName }, new RowMapper<String>() {
				public String mapRow(ResultSet rs, int col) throws SQLException {
					return ((BigDecimal) rs.getObject(1)).toString();
				}
			});
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#getAllReportList(java.util.Map)
	 * 
	 * @Cacheable(cacheName = "allReports")
	 */
	@Cacheable(value = "configCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getAllReportList') )")
	public List<ReportTO> getAllReportList(Map<String, Object> paramMap) {
		logger.log(IAppLogger.INFO, "Enter: ReportDAOImpl - getAllReportList");
		UserTO loggedinUserTO = (UserTO) paramMap.get("loggedinUserTO");
		List<ReportTO> reports = null;
		List<Map<String, Object>> dataList = null;
		if (paramMap.get("editReport") != null && paramMap.get("editReport").equals("editReport")) {
			dataList = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_DASHBOARD_DETAILS, loggedinUserTO.getCustomerId(), paramMap.get("reportId"));
		} else {
			dataList = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ALL_REPORT_LIST, loggedinUserTO.getCustomerId());
		}
		if (dataList != null && dataList.size() > 0) {
			reports = new ArrayList<ReportTO>();
			for (Map<String, Object> data : dataList) {
				ReportTO to = new ReportTO();
				to.setReportId(((BigDecimal) data.get("ID")).longValue());
				to.setReportName((String) data.get("REPORT_NAME"));
				to.setReportDescription((String) data.get("REPORT_DESC"));
				to.setReportUrl((String) data.get("REPORT_FOLDER_URI"));
				to.setReportOriginalUrl((String) data.get("REPORT_FOLDER_URI"));
				to.setReportType((String) data.get("REPORT_TYPE"));
				to.setEnabled(((String) data.get("STATUS")).equals(IApplicationConstants.ACTIVE_FLAG) ? true : false);
				String orgLevels = (String) data.get("ORG_NODE_LEVEL");
				orgLevels = orgLevels.replace("Corporation,Diocese", "Corporation/Diocese");
				to.setAllOrgNode(orgLevels);
				to.setLinkName(((BigDecimal) data.get("CUST_PROD_ID")).longValue());
				to.setProducttName((String) data.get("product_name"));
				to.setProductId(((BigDecimal) data.get("productid")).longValue());

				String strRoles = (String) data.get("ROLES");
				if (strRoles != null && strRoles.length() > 0) {
					String[] roles = strRoles.split(",");
					for (String role : roles) {
						ROLE_TYPE role_type = Utils.getRoles(role);
						if (role_type != null) {
							to.addRole(role_type);
						}
					}
				}
				String strOrgLevl = (String) data.get("ORG_NODE_LEVEL");
				strOrgLevl = strOrgLevl.replace("Corporation,Diocese", "Corporation/Diocese");
				if (strOrgLevl != null && strOrgLevl.length() > 0) {
					String[] orgLevel = strOrgLevl.split(",");
					to.setOrgNodeLevelArr(orgLevel);
				}
				to.setAssessmentName((String) data.get("ASSESSMENT_NAME"));
				to.setMenuId(((BigDecimal) data.get("MENUID")).toString());
				to.setMenuName((String) data.get("MENUNAME"));
				reports.add(to);
			}
		}
		logger.log(IAppLogger.INFO, "Exit: ReportDAOImpl - getAllReportList");
		return reports;
	}

	/**
	 * Updates the particular report information in database
	 * 
	 * @param reportId
	 * @param reportName
	 * @param reportUrl
	 * @param isEnabled
	 *            1 if enabled, 0 otherwise
	 * @param roles
	 *            The user roles who can access this particular report.
	 * 
	 * @CacheEvict(value = "allReports", allEntries=true)
	 */
	//@TriggersRemove(cacheName = "allReports", removeAll = true)
	@CacheEvict(value = "configCache", allEntries = true)
	public boolean updateReport(String reportId, String reportName, String reportUrl, String isEnabled, String[] roles) {
		logger.log(IAppLogger.INFO, "Enter: ReportDAOImpl - updateReport");
		try {
			// update report table
			getJdbcTemplatePrism().update(IQueryConstants.UPDATE_REPORT, reportName, reportUrl, isEnabled, reportId);
			// delete from report_role table
			getJdbcTemplatePrism().update(IQueryConstants.DELETE_REPORT_ROLE, reportId);

			if (roles != null) {
				for (String role : roles) {
					if (Utils.isValidRoles(role)) {
						// insert into report_role table
						getJdbcTemplatePrism().update(IQueryConstants.INSERT_REPORT_ROLE, reportId, role, IApplicationConstants.ACTIVE_FLAG);
					}
				}
				// if(strRole.length() >= 3) strRole = strRole.substring(0, strRole.length() - 3);
			}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while updating report details.", e);
			return false;
		}
		logger.log(IAppLogger.INFO, "Exit: ReportDAOImpl - updateReport");
		return true;
	}

	/**
	 * Retrieves assessments details from database.
	 * 
	 * @return List of all available assessments {@link AssessmentTO} along with corresponding report details {@link ReportTO}
	 */
	//Fix for TD 77939 - implement customerId, caching param change - By Joy
	//@Cacheable(value = "defaultCache", key="(T(com.ctb.prism.core.util.CacheKeyUtils).string(#p0)).concat(#root.method.name)")
	@Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getAssessments') )")
	public List<AssessmentTO> getAssessments(Map<String, Object> paramMap) {
		logger.log(IAppLogger.INFO, "Enter: ReportDAOImpl - getAssessments");

		UserTO loggedinUserTO = (UserTO) paramMap.get("loggedinUserTO");
		boolean parentReports = ((Boolean) paramMap.get("parentReports")).booleanValue();
		
		List<AssessmentTO> assessments = null;
		List<Map<String, Object>> dataList = null;
		if (parentReports) {
			dataList = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ALL_ASSESSMENT_LIST, "PN%",loggedinUserTO.getCustomerId());
		} else {
			dataList = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ALL_ASSESSMENT_LIST, "API%",loggedinUserTO.getCustomerId());
		}
		if (dataList != null && dataList.size() > 0) {
			assessments = new ArrayList<AssessmentTO>();
			long oldAssessmentId = -1;
			AssessmentTO assessmentTO = null;

			for (Map<String, Object> data : dataList) {
				long assessmentId = ((BigDecimal) data.get("MENU_ID")).longValue();
				if (oldAssessmentId != assessmentId) {
					oldAssessmentId = assessmentId;
					assessmentTO = new AssessmentTO();
					assessmentTO.setAssessmentId(assessmentId);
					assessmentTO.setAssessmentName((String) data.get("MENU_NAME"));
					assessments.add(assessmentTO);
				}

				ReportTO reportTO = new ReportTO();
				reportTO.setReportId(((BigDecimal) data.get("REPORT_ID")).longValue());
				reportTO.setReportName((String) data.get("REPORT_NAME"));
				reportTO.setReportUrl((String) data.get("REPORT_FOLDER_URI"));
				reportTO.setEnabled(data.get("STATUS").equals(IApplicationConstants.ACTIVE_FLAG) ? true : false);
				//Changed to get this from cache
				//String strRoles = (String) data.get("ROLES");
				String strRoles = getListOfRoles(reportTO.getReportId());
				reportTO.setAllRoles(strRoles);
				if (strRoles != null && strRoles.length() > 0) {
					String[] roles = strRoles.split(",");
					for (String role : roles) {
						ROLE_TYPE user_TYPE = Utils.getRoles(role);// Utils.getRole(role);
						if (user_TYPE != null) {
							reportTO.addRole(user_TYPE);
						}
					}
				}
				reportTO.setReportType((String) data.get("TYPE"));
				reportTO.setOrgLevel((((BigDecimal) data.get("ORGLEVEL")) != null) ? ((BigDecimal) data.get("ORGLEVEL")).toString() : "");
				assessmentTO.addReport(reportTO);
			}
		}

		logger.log(IAppLogger.INFO, "Exit: ReportDAOImpl - getAssessments");
		return assessments;
	}

	@CacheEvict(value = "configCache", allEntries=true )
	public boolean deleteReport(String reportId) throws SystemException {
		logger.log(IAppLogger.INFO, "Enter: ReportDAOImpl - deleteReport");
		try {
			// delete from report_role table
			getJdbcTemplatePrism().update(IQueryConstants.DELETE_REPORT_ROLE, reportId);
			
			// delete from report table DASH_MESSAGES table - Added by Joy
			getJdbcTemplatePrism().update(IQueryConstants.DELETE_REPORT_MSG, reportId);
			
			// delete from report table
			getJdbcTemplatePrism().update(IQueryConstants.DELETE_REPORT, reportId);
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while updating report details.", e);
			return false;
		}
		logger.log(IAppLogger.INFO, "Exit: ReportDAOImpl - deleteReport");
		return true;
	}

	/**
	 * Returns the reportTO on add.
	 * 
	 * @param nodeid
	 * @return
	 */
	private ReportTO getDashboardData(String reportid, String customerid) {

		ReportTO to = null;
		List<Map<String, Object>> dataList = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_DASHBOARD_DETAILS, customerid, reportid);
		if (dataList != null && dataList.size() > 0) {
			to = new ReportTO();
			for (Map<String, Object> data : dataList) {

				to.setReportId(((BigDecimal) data.get("ID")).longValue());
				to.setReportName((String) data.get("REPORT_NAME"));
				to.setReportDescription((String) data.get("REPORT_DESC"));
				to.setReportUrl((String) data.get("REPORT_FOLDER_URI"));
				to.setReportType((String) data.get("REPORT_TYPE"));
				to.setEnabled(((String) data.get("STATUS")).equals(IApplicationConstants.ACTIVE_FLAG) ? true : false);
				to.setAllOrgNode((String) data.get("ORG_NODE_LEVEL"));
				to.setLinkName(((BigDecimal) data.get("CUST_PROD_ID")).longValue());
				to.setProducttName((String) data.get("product_name"));
				to.setProductId(((BigDecimal) data.get("productid")).longValue());
				to.setMenuId(String.valueOf(data.get("MENUID")));
				to.setMenuName((String) data.get("MENUNAME"));
				String strRoles = (String) data.get("ROLES");
				if (strRoles != null && strRoles.length() > 0) {
					String[] roles = strRoles.split(",");
					for (String role : roles) {
						ROLE_TYPE role_type = Utils.getRoles(role);
						if (role_type != null) {
							to.addRole(role_type);
						}
					}
				}
			}
		}

		return to;

	}

	@Cacheable(value = "defaultCache", key="'getAllRoles'.concat(#root.method.name)")
	public List<ObjectValueTO> getAllRoles() {
		List<ObjectValueTO> roles = null;
		List<Map<String, Object>> dataList = getJdbcTemplatePrism().queryForList(IQueryConstants.ALL_ROLE);
		if (dataList != null && dataList.size() > 0) {
			roles = new ArrayList<ObjectValueTO>();
			for (Map<String, Object> data : dataList) {
				ObjectValueTO to = new ObjectValueTO();
				to.setValue(((BigDecimal) data.get("ROLEID")).toString());
				to.setName((String) data.get("ROLE_NAME"));
				roles.add(to);
			}
		}
		return roles;
	}

	public List<ObjectValueTO> getAllAssessments() {
		List<ObjectValueTO> assessments = null;
		List<Map<String, Object>> dataList = getJdbcTemplatePrism().queryForList(IQueryConstants.ALL_ASSESSMENTS);
		if (dataList != null && dataList.size() > 0) {
			assessments = new ArrayList<ObjectValueTO>();
			for (Map<String, Object> data : dataList) {
				ObjectValueTO to = new ObjectValueTO();
				to.setValue(((BigDecimal) data.get("ID")).toString());
				to.setName((String) data.get("NAME"));
				assessments.add(to);
			}
		}
		return assessments;
	}

	/**
	 * 
	 * @param reportIdentifier
	 *            - report id OR report URL
	 * @return
	 */
	public ReportTO getReport(String reportIdentifier) {
		ReportTO to = new ReportTO();
		List<Map<String, Object>> dataList = getJdbcTemplatePrism().queryForList(IQueryConstants.REPORT_TYPE, reportIdentifier, reportIdentifier);
		if (dataList != null && dataList.size() > 0) {
			for (Map<String, Object> data : dataList) {
				to.setReportType((String) data.get("TYPE"));
				to.setReportId(((BigDecimal) data.get("ID")).longValue());
				to.setReportUrl((String) data.get("URI"));
				break;
			}
		}
		return to;
	}

	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getAdminYear(final Map<String, Object> paramMap) throws SystemException {
		logger.log(IAppLogger.INFO, "Enter: ReportDAOImpl - getAdminYear()");
		List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOList = null;
		try {
			objectValueTOList = getJdbcTemplatePrism().query(IQueryConstants.ALL_ADMIN_YEAR, new ObjectValueTOMapper());
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred in getAdminYear():", e);
			throw new SystemException(e);
		}
		logger.log(IAppLogger.INFO, "Exit: ReportDAOImpl - getAdminYear()");
		return objectValueTOList;
	}

	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getOrgNodeLevel(final Map<String, Object> paramMap) throws SystemException {
		logger.log(IAppLogger.INFO, "Enter: ReportDAOImpl - getOrgNodeLevel()");
		List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOList = null;
		try {
			objectValueTOList = getJdbcTemplatePrism().query(IQueryConstants.ALL_ORG_NODE_LEVEL, new ObjectValueTOMapper());
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred in getOrgNodeLevel():", e);
			throw new SystemException(e);
		}
		logger.log(IAppLogger.INFO, "Exit: ReportDAOImpl - getOrgNodeLevel()");
		return objectValueTOList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#loadManageMessage(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public List<ManageMessageTO> loadManageMessage(final Map<String, Object> paramMap) throws SystemException {

		long reportId = ((Long) paramMap.get("reportId")).longValue();
		long custProdId = ((Long) paramMap.get("custProdId")).longValue();
		String reportName = ((String) paramMap.get("reportName"));
		@SuppressWarnings("rawtypes")
		List placeHolderValueList = new ArrayList();
		List<ManageMessageTO> manageMessageTOList = null;
		try {
			if (null != reportName && (reportName.equalsIgnoreCase(IApplicationConstants.GENERIC_REPORT_NAME))) {
				placeHolderValueList.add(reportId);
				custProdId = 5001L;
				placeHolderValueList.add(custProdId);
				placeHolderValueList.add(reportId);
				placeHolderValueList.add(custProdId);
				manageMessageTOList = getJdbcTemplatePrism().query(IQueryConstants.GET_MANAGE_MESSAGE_LIST_SCM_GENERIC, placeHolderValueList.toArray(), new ManageMessageTOMapper());
			} else if (null != reportName && (reportName.equalsIgnoreCase(IApplicationConstants.PRODUCT_SPECIFIC_REPORT_NAME))) {
				placeHolderValueList.add(reportId);
				placeHolderValueList.add(custProdId);
				placeHolderValueList.add(reportId);
				placeHolderValueList.add(custProdId);
				manageMessageTOList = getJdbcTemplatePrism().query(IQueryConstants.GET_MANAGE_MESSAGE_LIST_SCM_PRODUCT_SPECIFIC, placeHolderValueList.toArray(), new ManageMessageTOMapper());
			} else {
				placeHolderValueList.add(reportId);
				placeHolderValueList.add(custProdId);
				placeHolderValueList.add(reportId);
				placeHolderValueList.add(custProdId);
				manageMessageTOList = getJdbcTemplatePrism().query(IQueryConstants.GET_MANAGE_MESSAGE_LIST, placeHolderValueList.toArray(), new ManageMessageTOMapper());
			}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred in loadManageMessage():", e);
			return null;
		}
		return manageMessageTOList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public int saveManageMessage(final List<ManageMessageTO> manageMessageTOList) throws SystemException {
		logger.log(IAppLogger.INFO, "Enter: ReportDAOImpl - saveManageMessage()");
		int insertDMFlag = 1;
		int successFlag = 0;

		try {
			// Delete DASH_MESSAGES - Start
			// Delete all records from DASH_MESSAGES depending upon Search Criteria - No dependency for deleted record count
			List placeHolderValueList = new ArrayList();
			placeHolderValueList.add(manageMessageTOList.get(0).getReportId());
			placeHolderValueList.add(manageMessageTOList.get(0).getCustProdIdHidden());
			int deleteCountDbMessage = getJdbcTemplatePrism().update(IQueryConstants.DELETE_DASH_MESSAGE, placeHolderValueList.toArray());
			// Delete DASH_MESSAGES - End

			// Insert DASH_MESSAGES - Start
			int[] insertDMCountArr = getJdbcTemplatePrism().batchUpdate(IQueryConstants.INSERT_DASH_MESSAGES, new BatchPreparedStatementSetter() {

				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ManageMessageTO manageMessageTO = manageMessageTOList.get(i);
					ps.setLong(1, manageMessageTO.getReportId());
					ps.setLong(2, manageMessageTO.getMessageTypeId());
					ps.setString(3, manageMessageTO.getMessage().replaceAll("<strong>", "<b>").replaceAll("</strong>", "</b>"));
					ps.setLong(4, manageMessageTO.getCustProdIdHidden());
					ps.setString(5, IApplicationConstants.CHECKED_CHECKBOX_VALUE.equalsIgnoreCase(manageMessageTO.getActivationStatus()) ? IApplicationConstants.CHECKED_VALUE_DRM_CHECKBOX
							: IApplicationConstants.DEFAULT_VALUE_DRM_CHECKBOX);
				}

				public int getBatchSize() {
					return manageMessageTOList.size();
				}

			});
			// Insert DASH_MESSAGES - End
			insertDMFlag = Utils.batchUpdateCheck(insertDMCountArr);
			if (insertDMFlag == 1) {
				successFlag = 1;
			}

		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred in saveManageMessage():", e);
			e.printStackTrace();
			successFlag = 0;
			throw new SystemException(e);
		} finally {
			if (successFlag == 0) {
				throw new SystemException("Exception: ReportDAOImpl - saveManageMessage()");
			}
		}
		logger.log(IAppLogger.INFO, "Exit: ReportDAOImpl - saveManageMessage()");
		return successFlag;
	}

	// 365348
	@CacheEvict(value = "configCache", allEntries = true)
	public boolean updateReportNew(ReportTO reportTO) {
		logger.log(IAppLogger.INFO, "Enter: ReportDAOImpl - updateReport");
		try {
			int userRoleLoop = 0, orgLevelLoop = 0;
			getJdbcTemplatePrism().update(IQueryConstants.UPDATE_REPORT_NEW, reportTO.getReportName(), reportTO.getReportDescription(), reportTO.getReportOriginalUrl(), reportTO.getReportStatus(),
					reportTO.getReportType(), reportTO.getReportId());
			getJdbcTemplatePrism().update(IQueryConstants.DELETE_REPORT_ROLE, reportTO.getReportId());
			for (userRoleLoop = 0; userRoleLoop < reportTO.getUserRoles().length; userRoleLoop++) {

				for (orgLevelLoop = 0; orgLevelLoop < reportTO.getOrgNodeLevelArr().length - 1; orgLevelLoop++) {
					getJdbcTemplatePrism().update(IQueryConstants.INSERT_REPORT_ROLE, reportTO.getMenuId(), reportTO.getReportId(), reportTO.getUserRoles()[userRoleLoop],
							reportTO.getOrgNodeLevelArr()[orgLevelLoop], reportTO.getCustomerLinks(), reportTO.getReportId(), IApplicationConstants.ACTIVE_FLAG);
				}
			}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while updating report details.", e);
			return false;
		}
		logger.log(IAppLogger.INFO, "Exit: ReportDAOImpl - updateReport");
		return true;
	}

	/**
	 * add new dashboard
	 * 
	 * @param String
	 *            reportName, String reportDescription, String userName, String reportType, String password, String assessmentType,String reportStatus, String[] userRoles
	 * @return ReportTO
	 */

	// Arunava Datta
	@CacheEvict(value = "configCache", allEntries = true)
	public ReportTO addNewDashboard(ReportParameterTO reportParameterTO) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: ReportDAOImpl - addNewDashboard");

		ReportTO reportTo = null;
		String reportName = reportParameterTO.getReportName();
		String reportDescription = reportParameterTO.getReportDescription();
		String reportType = reportParameterTO.getReportType();
		String reportUri = reportParameterTO.getReportUrl();
		String assessmentType = reportParameterTO.getAssessmentName();
		String reportStatus = reportParameterTO.getReportStatus();
		Long customerLinks = reportParameterTO.getLinkName();
		String[] userRoles = reportParameterTO.getUserRoles();
		String[] orgNodeLevel = reportParameterTO.getOrgNodeLevel();
		String customerId = reportParameterTO.getCustomerId();
		try {

			List<Map<String, Object>> reportMap = null;
			reportMap = getJdbcTemplatePrism().queryForList(IQueryConstants.CHECK_EXISTING_REPORT, reportName, reportUri);
			if (reportMap == null || reportMap.isEmpty()) {
				int userRoleLoop = 0, orgNodeLevelLoop = 0;
				long report_seq_id = getJdbcTemplatePrism().queryForLong(IQueryConstants.DB_REPORT_SEQ_ID);

				getJdbcTemplatePrism().update(IQueryConstants.INSERT_REPORT, report_seq_id, reportName, reportDescription, reportType, reportUri, reportStatus);

				for (userRoleLoop = 0; userRoleLoop < userRoles.length; userRoleLoop++) {
					for (orgNodeLevelLoop = 0; orgNodeLevelLoop < orgNodeLevel.length; orgNodeLevelLoop++) {
						getJdbcTemplatePrism().update(IQueryConstants.INSERT_REPORT_ROLE, reportParameterTO.getMenuId(), report_seq_id, userRoles[userRoleLoop], orgNodeLevel[orgNodeLevelLoop],
								customerLinks, report_seq_id, IApplicationConstants.ACTIVE_FLAG);
					}
				}

				reportTo = getDashboardData(String.valueOf(report_seq_id), customerId);
			}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while adding dashboard details.", e);
			return null;
		}
		logger.log(IAppLogger.INFO, "Exit: ReportDAOImpl - addNewDashboard");
		return reportTo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#getAllGroupDownloadFiles(java.util.Map)
	 */
	public List<JobTrackingTO> getAllGroupDownloadFiles(Map<String, Object> paramMap) {
		logger.log(IAppLogger.INFO, "Enter: getAllGroupDownloadFiles()");
		UserTO loggedinUserTO = (UserTO) paramMap.get("loggedinUserTO");
		List<JobTrackingTO> allGroupFiles = null;
		List<Map<String, Object>> dataList = getJdbcTemplatePrism().queryForList(IReportQuery.GET_GROUP_DOWNLOAD_LIST, loggedinUserTO.getUserId());
		if (dataList != null && dataList.size() > 0) {
			allGroupFiles = new ArrayList<JobTrackingTO>();
			for (Map<String, Object> data : dataList) {
				JobTrackingTO to = new JobTrackingTO();
				String createdDate = getFormattedDate((Timestamp) data.get("created_date_time"), "MM/dd/yyyy HH:mm:ss");
				to.setJobId(((BigDecimal) data.get("job_id")).longValue());
				Timestamp ts = (Timestamp) data.get("updated_date_time");
				if (ts == null) {
					ts = (Timestamp) data.get("created_date_time");
				}
				Calendar cal = Calendar.getInstance();
				cal.setTime(ts);
				cal.add(Calendar.DAY_OF_WEEK, (Integer.parseInt((String) paramMap.get("gdfExpiryTime"))));
				ts.setTime(cal.getTime().getTime());
				ts = new Timestamp(cal.getTime().getTime());
				String updatedDate = getFormattedDate(ts, "MM/dd/yyyy HH:mm:ss");
				if (null != ((String) data.get("file_size")) || ((String) data.get("file_size")) != "") {
					to.setFileSize((String) data.get("file_size"));
				} else {
					to.setFileSize(" ");
				}
				String filePath = (String) data.get("request_filename");
				String fileName = "";
				if (filePath == null || "null".equalsIgnoreCase(filePath)) {
					filePath = "";
				} else {
					if (filePath.lastIndexOf("\\") >= 0) {
						fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
					} else {
						fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
					}
				}
				// fileName = filePath.substring(filePath.lastIndexOf(File.separator)+1);
				if ("".equals(fileName)) {
					fileName = getFileNameFromRequestDetails((String) data.get("REQUEST_TYPE"), (String) data.get("REQUEST_DETAILS"));
				}
				to.setRequestFilename(fileName);
				to.setFilePath(filePath);
				to.setCreatedDateTime(createdDate);
				to.setUpdatedDateTime(updatedDate);
				to.setRequestType((String) data.get("REQUEST_TYPE"));
				to.setJobStatus((String) data.get("JOB_STATUS"));
				to.setQuerySheetTO(getQuerySheetTO((String) data.get("REQUEST_TYPE"), (String) data.get("REQUEST_DETAILS")));
				allGroupFiles.add(to);
			}
		}
		logger.log(IAppLogger.INFO, "Exit: getAllGroupDownloadFiles()");
		return allGroupFiles;
	}

	private String getFileNameFromRequestDetails(String requestType, String requestDetails) {
		String fileName = "";
		if ((requestType != null) && (requestType.equals(IApplicationConstants.REQUEST_TYPE.GDF.toString()))) {
			GroupDownloadTO to = Utils.jsonToObject(requestDetails, GroupDownloadTO.class);
			if (to != null) {
				if (to.getFileName() != null) {
					fileName = to.getFileName();
				}
			}
		}
		// logger.log(IAppLogger.INFO, "Return: getFileNameFromRequestDetails() = " + fileName);
		return fileName;
	}

	private QuerySheetTO getQuerySheetTO(String requestType, String requestDetails) {
		QuerySheetTO querySheetTO = new QuerySheetTO();
		if (IApplicationConstants.REQUEST_TYPE.GDF.toString().equals(requestType)) {
			GroupDownloadTO to = Utils.jsonToObject(requestDetails, GroupDownloadTO.class);
			String jobId = to.getJobId();
			String fileName = to.getFileName();
			String dateOfFileGenerationRequest = to.getExtractStartDate();
			String testAdministration = to.getTestAdministrationVal();
			String testProgram = to.getTestProgram();
			if ("0".equals(testProgram)) {
				testProgram = "Non Public Schools";
			} else if ("1".equals(testProgram)) {
				testProgram = "Public Schools";
			}
			//String corpDiocese = to.getDistrict();
			//String schoolNames = to.getSchool();
			String classNames = to.getKlass();
			String gradeNames = to.getGrade();
			String fileType = to.getGroupFile();
			String customerId = to.getCustomerId();
			String students = to.getStudents();
			int studentCount = students.split(",").length;
			int classCount = classNames.split(",").length;
			int schoolCount = gradeNames.split(",").length;
			String button = to.getButton();

			// logger.log(IAppLogger.INFO, "fileName: " + fileName);
			// logger.log(IAppLogger.INFO, "students: " + students);
			// logger.log(IAppLogger.INFO, "studentCount: " + studentCount);

			querySheetTO.setJobId(jobId);
			querySheetTO.setFileName(fileName);
			querySheetTO.setDateOfFileGenerationRequest(dateOfFileGenerationRequest);
			querySheetTO.setTestAdministration(testAdministration);
			querySheetTO.setTestProgram(testProgram);
			querySheetTO.setCorpDiocese(to.getDistrict());
			querySheetTO.setSchoolNames(to.getSchool());
			querySheetTO.setClassNames(classNames);
			querySheetTO.setGradeNames(gradeNames);
			querySheetTO.setFileType(fileType);
			querySheetTO.setRequestType(requestType);
			querySheetTO.setStudentCount(studentCount);
			querySheetTO.setClassCount(classCount);
			querySheetTO.setSchoolCount(schoolCount);
			querySheetTO.setSelectedStudents(students);
			querySheetTO.setCustomerId(customerId);
			querySheetTO.setButton(button);
		}
		return querySheetTO;
	}

	/**
	 * Query Sheet.
	 * 
	 * @param requestDetails
	 * @return
	 */
	public String getRequestSummary(String requestDetails) {
		QuerySheetTO querySheetTO = getQuerySheetTO(IApplicationConstants.REQUEST_TYPE.GDF.toString(), requestDetails);
		String jobId = querySheetTO.getJobId();
		String productId = querySheetTO.getTestAdministration();
		String gradeId = querySheetTO.getGradeNames();
		String corpDiocese = querySheetTO.getCorpDiocese();
		String schools = querySheetTO.getSchoolNames();
		String klass = querySheetTO.getClassNames();
		String orgNodeIds = getOrgNodeIds(corpDiocese, schools, klass);
		String selectedStudents = querySheetTO.getSelectedStudents();
		String customerId = querySheetTO.getCustomerId();
		List<Map<String, Object>> dataList = getJdbcTemplatePrism().queryForList(CustomStringUtil.replaceCharacterInString('~', orgNodeIds, IReportQuery.GET_REQUEST_SUMMARY), jobId, productId, gradeId);
		Map<String, String> valueMap = new HashMap<String, String>();
		if ((dataList != null) && (!dataList.isEmpty())) {
			for (Map<String, Object> data : dataList) {
				valueMap.put((String) data.get("KEY"), (String) data.get("VALUE"));
			}
		}
		// querySheetTO.setDateOfFileGenerationRequest(valueMap.get("EXTRACT_STARTDATE"));
		querySheetTO.setTestAdministration(valueMap.get("PRODUCT_NAME"));
		querySheetTO.setCorpDiocese(valueMap.get(querySheetTO.getCorpDiocese()));
		querySheetTO.setSchoolNames(valueMap.get(querySheetTO.getSchoolNames()));
		querySheetTO.setGradeNames(valueMap.get("GRADE_NAME"));
		
		List<Map<String, Object>> selectionDataList = getJdbcTemplatePrism().queryForList(CustomStringUtil.replaceCharacterInString('~', selectedStudents, IReportQuery.GET_STUDENT_SELECTION_STATISTICS));
		String schoolCount = "";
		String classCount = "";
		if ((selectionDataList != null) && (!selectionDataList.isEmpty())) {
			for (Map<String, Object> data : selectionDataList) {
				schoolCount = ((BigDecimal) data.get("SCHOOLS")).toString();
				classCount = ((BigDecimal) data.get("CLASSES")).toString();
			}
		}
		String selectionString = querySheetTO.getStudentCount() + " Student(s) in " + classCount + " Class(es) in " + schoolCount + " School(s) have been selected.";

		GroupDownloadTO gdTo = null;
		String stateOrgNodeId = null;
		if (("-1".equals(klass)) || ("-1".equals(gradeId))) {
			gdTo = Utils.jsonToObject(requestDetails, GroupDownloadTO.class);
			stateOrgNodeId = getAncestorOrgNodeId(gdTo.getSchool(), 1);
		}

		List<String> classNameList = new ArrayList<String>();
		if ("-1".equals(klass)) {
			classNameList = getSelectedClassNames(stateOrgNodeId, selectedStudents, customerId);
		} else {
			classNameList.add(valueMap.get(querySheetTO.getClassNames()));
		}

		List<String> gradeNameList = new ArrayList<String>();
		if ("-1".equals(gradeId)) {
			gradeNameList = getAllGradeNames(stateOrgNodeId, gdTo.getTestAdministrationVal(), corpDiocese, schools, gdTo.getGroupFile(), gdTo.getTestProgram(),customerId);
		} else {
			gradeNameList.add(getResult(CustomStringUtil.replaceCharacterInString('?', gradeId, IQueryConstants.GET_GRADE_NAME_BY_ID)));
		}
		
		String fileTypeDescription = querySheetTO.getFileType();
		if (fileTypeDescription == null) {
			fileTypeDescription = "";
		} else if (fileTypeDescription.equals("ISR")) {
			fileTypeDescription = "Individual Student Report (ISR)";
		} else if (fileTypeDescription.equals("IPR")) {
			fileTypeDescription = "Image Print";
		} else if (fileTypeDescription.equals("BOTH")) {
			fileTypeDescription = "Both (ISR and Image Print)";
		} else if (fileTypeDescription.equals("ICL")) {
			fileTypeDescription = "Invitation Letter";
		}
		
		String requestTypeDescription = querySheetTO.getButton();
		if (requestTypeDescription == null) {
			requestTypeDescription = "";
		} else if (requestTypeDescription.equals("CP")) {
			requestTypeDescription = "Combined PDF";
		} else if (requestTypeDescription.equals("SP")) {
			requestTypeDescription = "Separate PDF";
		}

		StringBuilder requestSummary = new StringBuilder();
		requestSummary.append("Generated File Name: " + FileUtil.getFileNameFromFilePath(querySheetTO.getFileName()) + "\n");
		requestSummary.append("Date of File Generation Request: " + querySheetTO.getDateOfFileGenerationRequest() + "\n");
		requestSummary.append("Test Administration: " + querySheetTO.getTestAdministration() + "\n");
		requestSummary.append("Test Program: " + querySheetTO.getTestProgram() + "\n");
		requestSummary.append("Corp/Diocese: " + querySheetTO.getCorpDiocese() + "\n");
		requestSummary.append("School: " + querySheetTO.getSchoolNames() + "\n");
		requestSummary.append("Grade: " + Utils.convertListToCommaString(gradeNameList) + "\n");
		requestSummary.append("Class: " + Utils.convertListToCommaString(classNameList) + "\n\n\n");
		requestSummary.append(selectionString + "\n\n\n");
		requestSummary.append("File Type: " + fileTypeDescription + "\n");
		requestSummary.append("Request Type: " + /*querySheetTO.getRequestType()*/requestTypeDescription);
		return requestSummary.toString();
	}

	private String getOrgNodeIds(String corpDiocese, String schools, String klass) {
		StringBuilder sb = new StringBuilder();
		if (corpDiocese != null && !corpDiocese.isEmpty()) {
			sb.append(corpDiocese);
			sb.append(",");
		}
		if (schools != null && !schools.isEmpty()) {
			sb.append(schools);
			sb.append(",");
		}
		if (klass != null && !klass.isEmpty() && (!"-1".equals(klass))) {
			sb.append(klass);
		} else {
			sb.append("0");
		}
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#getRequestDetail(java.util.Map)
	 */
	public List<JobTrackingTO> getRequestDetail(Map<String, Object> paramMap) {
		logger.log(IAppLogger.INFO, "Enter: getRequestDetail()");
		List<JobTrackingTO> allGroupFiles = null;
		String jobId = (String) paramMap.get("jobId");
		List<Map<String, Object>> dataList = getJdbcTemplatePrism().queryForList(IReportQuery.GET_GROUP_DOWNLOAD_REQUEST_VIEW, jobId);
		if (dataList != null && dataList.size() > 0) {
			allGroupFiles = new ArrayList<JobTrackingTO>();
			for (Map<String, Object> data : dataList) {
				JobTrackingTO to = new JobTrackingTO();
				String filePath = (String) data.get("request_filename");
				// String extractStartdate = getFormattedDate((Timestamp) data.get("extract_startdate"),"MM/dd/yyyy HH:mm:ss");
				// String extractEnddate = getFormattedDate((Timestamp) data.get("extract_enddate"),"MM/dd/yyyy HH:mm:ss");
				String fileName = "";
				if (filePath != null) {
					if (filePath.lastIndexOf("\\") >= 0) {
						fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
					} else {
						fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
					}
				}
				if ("".equals(fileName)) {
					fileName = getFileNameFromRequestDetails((String) data.get("REQUEST_TYPE"), (String) data.get("REQUEST_DETAILS"));
				}
				to.setRequestFilename(fileName);
				to.setRequestType((String) data.get("REQUEST_TYPE"));
				to.setRequestDetails((String) data.get("REQUEST_DETAILS"));
				to.setRequestSummary((String) data.get("REQUEST_SUMMARY"));
				to.setExtractCategory((String) data.get("EXTRACT_CATEGORY"));
				// to.setExtractStartdate(extractStartdate);
				// to.setExtractEnddate(extractEnddate);
				to.setJobName((String) data.get("JOB_NAME"));
				allGroupFiles.add(to);
			}
		}
		logger.log(IAppLogger.INFO, "Exit: getRequestDetail()");
		return allGroupFiles;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#deleteGroupFiles(java.lang.String)
	 */
	public boolean deleteGroupFiles(String Id) throws Exception {
		try {
			getJdbcTemplatePrism().update(IReportQuery.DELETE_GROUP_FILES, Id);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * @author Arunava
	 * @param gdfExpiryTime
	 * @return
	 * @throws Exception
	 *             For GDF deleting by scheduler
	 */

	public void deleteScheduledGroupFiles(String gdfExpiryTime) throws Exception {
		try {
			int gdfExpiryTimeRange = Integer.parseInt(gdfExpiryTime);
			logger.log(IAppLogger.INFO, "Entering Scheduled method for GROUP DOWNLOAD FILES--------------- ");
			List<Map<String, Object>> dataList = getJdbcTemplatePrism().queryForList(IReportQuery.GET_DELETE_SCHEDULED_GROUP_DOWNLOAD_LIST, gdfExpiryTimeRange);
			String appendLog = " ... File is deleted by cron job as the file is expired : " + Utils.getDateTime();
			if ( dataList != null && dataList.size() > 0 )
			{
				for (Map<String, Object> data : dataList)
				{
					String filePath = (String) data.get("request_filename");
					if(filePath != null && filePath.length() > 0) {
					long jobId = ((BigDecimal) data.get("job_id")).longValue();
					logger.log(IAppLogger.INFO, "File Path/Job Id--------------" + filePath + "/" + jobId);
					File file = new File(filePath);
						if(file.exists())
						{
						file.delete();
					}
					getJdbcTemplatePrism().update(IReportQuery.DELETE_SCHEDULED_GROUP_FILES, appendLog, jobId);
				}
				}
				logger.log(IAppLogger.INFO, "Exiting Scheduled method for GROUP DOWNLOAD FILES--------------- ");
			}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Exception Message from  Scheduled method for GROUP DOWNLOAD FILES--------------- ");
		}
	}

	/**
	 * Returns the date in a defined format
	 * 
	 * @author Arunava Datta
	 * @param pTimestamp
	 * @param pFormat
	 *            examples: dd-MM-yyyy
	 * @return
	 * @see http://java.sun.com/j2se/1.4.2/docs/api/java/text/SimpleDateFormat.html
	 */
	public static String getFormattedDate(Timestamp pTimestamp, String pFormat) {
		DateFormat dF = new java.text.SimpleDateFormat(pFormat);
		return dF.format(pTimestamp.getTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#getTestAdministrations()
	 */
	@Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( 'getTestAdministrations' )")
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getTestAdministrations() {
		return getJdbcTemplatePrism().query(IQueryConstants.GET_TEST_ADMINISTRATIONS_GD, new ObjectValueTOMapper());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#populateStudentTableGD(com.ctb.prism.report.transferobject.GroupDownloadTO)
	 */
	//@Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#to.string).concat('populateStudentTableGD') )")
	public List<GroupDownloadStudentTO> populateStudentTableGD(GroupDownloadTO to) {
		logger.log(IAppLogger.INFO, "Exit: populateStudentTableGD()");
		List<GroupDownloadStudentTO> studentList = new ArrayList<GroupDownloadStudentTO>();

		String schoolId = to.getSchool();
		String classId = to.getKlass();
		String gradeId = to.getGrade();
		String testProgram = to.getTestProgram();
		String collationHierarchy = to.getCollationHierarchy();
		String groupFile = to.getGroupFile();
		String customerId = to.getCustomerId();
		String orgNodeLevel = to.getOrgNodeLevel();
		String prodId = to.getTestAdministrationVal();
		String custProdId = getCustProdId(customerId, prodId);
		if (testProgram == null || testProgram.isEmpty()) {
			logger.log(IAppLogger.ERROR, "testProgram cannot be null or empty: " + testProgram);
			logger.log(IAppLogger.INFO, "Exit: populateStudentTableGD()");
			return studentList;
		}
		//String orgMode = IApplicationConstants.ORG_MODE_DESC[Integer.parseInt(testProgram)];

		logger.log(IAppLogger.INFO, "schoolId = " + schoolId);
		logger.log(IAppLogger.INFO, "classId = " + classId);
		logger.log(IAppLogger.INFO, "gradeId = " + gradeId);
		logger.log(IAppLogger.INFO, "testProgram = " + testProgram);
		logger.log(IAppLogger.INFO, "collationHierarchy = " + collationHierarchy);
		logger.log(IAppLogger.INFO, "customerId = " + customerId);
		//logger.log(IAppLogger.INFO, "orgMode = " + orgMode);

		String stateOrgNodeId = getAncestorOrgNodeId(schoolId, 1);
		String testAdministrationVal = to.getTestAdministrationVal();
		String districtId = to.getDistrict();

		logger.log(IAppLogger.INFO, "stateOrgNodeId = " + stateOrgNodeId);
		logger.log(IAppLogger.INFO, "testAdministrationVal = " + testAdministrationVal);
		logger.log(IAppLogger.INFO, "districtId = " + districtId);

		String orderBy = "";
		if (collationHierarchy != null) {
			if ("11".equals(collationHierarchy)) {
				//orderBy = "ORDER BY CLASS.ORG_NODE_NAME, SBD.LAST_NAME, SBD.FIRST_NAME, SBD.MIDDLE_NAME";
				orderBy = "ORDER BY 2, SBD.STUDENT_LAST_NAME, SBD.STUDENT_FIRST_NAME, SBD.STUDENT_MIDDLE_INITIAL";
			} else if ("12".equals(collationHierarchy)) {
				orderBy = "ORDER BY SBD.STUDENT_LAST_NAME, SBD.STUDENT_FIRST_NAME, SBD.STUDENT_MIDDLE_INITIAL";
			}
		}

		List<Map<String, Object>> dataList = null;
		if ("-1".equals(classId)) {
			if ((schoolId != null) && (!"undefined".equalsIgnoreCase(schoolId))) {
				logger.log(IAppLogger.INFO, "ALL Classes");
				if ("-1".equals(gradeId)) {
					logger.log(IAppLogger.INFO, "ALL Grades");
					String query = CustomStringUtil.replaceCharacterInString('#', orderBy, IQueryConstants.GET_ALL_STUDENT_TABLE_GD_ALL_GRADES); 
					List<String> gradeList = getAllGrades(stateOrgNodeId, testAdministrationVal, districtId, schoolId, groupFile, testProgram,customerId);
					String grades = Utils.convertListToCommaString(gradeList);
					query = CustomStringUtil.replaceCharacterInString('$', grades, query);
					logger.log(IAppLogger.INFO, query);
					dataList = getJdbcTemplatePrism().queryForList(query, new Object[] { custProdId, custProdId, /*SF_GET_CLASS_START*/stateOrgNodeId, testAdministrationVal, districtId, schoolId, -99, testProgram, customerId/*SF_GET_CLASS_END*/, custProdId, testProgram });//
				} else {
					String query = CustomStringUtil.replaceCharacterInString('#', orderBy, IQueryConstants.GET_ALL_STUDENT_TABLE_GD); 
					logger.log(IAppLogger.INFO, query);
					dataList = getJdbcTemplatePrism().queryForList(query, new Object[] { custProdId, custProdId, /*SF_GET_CLASS_START*/stateOrgNodeId, testAdministrationVal, districtId, schoolId, gradeId, testProgram, customerId/*SF_GET_CLASS_END*/, gradeId, custProdId, testProgram});
				}
			}
		} else {
			if ((classId != null) && (!"undefined".equalsIgnoreCase(classId))) {
				if ("-1".equals(gradeId)) {
					String query = CustomStringUtil.replaceCharacterInString('#', orderBy, IQueryConstants.GET_STUDENT_TABLE_GD_ALL_GRADES); 
					List<String> gradeList = getAllGrades(stateOrgNodeId, testAdministrationVal, districtId, schoolId, groupFile, testProgram, customerId);
					String grades = Utils.convertListToCommaString(gradeList);
					query = CustomStringUtil.replaceCharacterInString('$', grades, query);
					logger.log(IAppLogger.INFO, query);
					dataList = getJdbcTemplatePrism().queryForList(query, new Object[] { custProdId, custProdId, classId, custProdId, testProgram});
				} else {
					String query = CustomStringUtil.replaceCharacterInString('#', orderBy, IQueryConstants.GET_STUDENT_TABLE_GD); 
					logger.log(IAppLogger.INFO, query);
				//	System.out.println("<<query>> "+ query);
					dataList = getJdbcTemplatePrism().queryForList(query, new Object[] { custProdId, custProdId, classId, gradeId, custProdId, testProgram });
				//	System.out.println("<<datlist>> custProdId: "+custProdId + " classId:" + classId+ " gradeId:"+gradeId+ " testProgram:" + testProgram );
				}
			}
		}
		if ((dataList != null) && (!dataList.isEmpty())) {
			for (Map<String, Object> data : dataList) {
				String id = data.get("ID").toString();
				String name = (String) data.get("NAME");
				String klass = (String) data.get("CLASS");
				String grade = (String) data.get("GRADE");
				String ic = (String) data.get("IC");
				String isr = (String) data.get("ISR");
				String ip = (String) data.get("IP");
				String icFlag = (String) data.get("IC_FLAG");
				String isrFlag = (String) data.get("ISR_FLAG");
				String ipFlag = (String) data.get("IP_FLAG");

				logger.log(IAppLogger.DEBUG, "id = " + id);
				logger.log(IAppLogger.DEBUG, "name = " + name);
				logger.log(IAppLogger.DEBUG, "klass = " + klass);
				logger.log(IAppLogger.DEBUG, "grade = " + grade);
				logger.log(IAppLogger.DEBUG, "ic = " + ic);
				logger.log(IAppLogger.DEBUG, "isr = " + isr);
				logger.log(IAppLogger.DEBUG, "ip = " + ip);

				GroupDownloadStudentTO student = new GroupDownloadStudentTO();
				student.setId(id);
				student.setName(name);
				student.setKlass(klass);
				student.setGrade(grade);
				student.setIc(ic);
				student.setIsr(isr);
				student.setIp(ip);
				student.setIcFlag(icFlag);
				student.setIsrFlag(isrFlag);
				student.setIpFlag(ipFlag);
				studentList.add(student);
			}
		}
		logger.log(IAppLogger.INFO, "studentList.size() = " + studentList.size());
		logger.log(IAppLogger.INFO, "Exit: populateStudentTableGD()");
		return studentList;
	}

	@Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #p4, #p5, #p6, 'getAllGrades' )")
	private List<String> getAllGrades(String stateOrgNodeId, String testAdministrationVal, String corpDiocese, String school, String groupFile, String testProgram, String customerId) {
		List<String> grades = new ArrayList<String>();
		grades.add("-1");
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ALL_GRADES, stateOrgNodeId, testAdministrationVal, corpDiocese, school, groupFile, testProgram, customerId,"SCHOOL_OTH");
		if ((lstData != null) && (!lstData.isEmpty())) {
			for (Map<String, Object> fieldDetails : lstData) {
				grades.add((String) fieldDetails.get("GRADEID"));
			}
		}
		logger.log(IAppLogger.INFO, "Grades Size: " + grades.size());
		return grades;
	}
	
	@Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #p4, 'getAllClassesForAllGrades' )")
	private List<String> getAllClassesForAllGrades(String productId, String orgMode, String schoolId, String customerId, String orgNodeLevel) {
		List<String> classes = new ArrayList<String>();
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ALL_CLASSES_FOR_ALL_GRADES, productId, orgMode, schoolId, customerId, productId, orgNodeLevel, schoolId, orgNodeLevel, schoolId);
		if ((lstData != null) && (!lstData.isEmpty())) {
			for (Map<String, Object> fieldDetails : lstData) {
				classes.add((String) fieldDetails.get("ORG_NODEID"));
			}
		}
		logger.log(IAppLogger.INFO, "Classes Size: " + classes.size());
		return classes;
	}

	@Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #p4, #p5, #p6, 'getAllGradeNames' )")
	private List<String> getAllGradeNames(String stateOrgNodeId, String testAdministrationVal, String corpDiocese, String school, String groupFile, String testProgram, String customerId) {
		List<String> grades = new ArrayList<String>();
		List<Map<String, Object>> lstData = getJdbcTemplatePrism()
				.queryForList(IQueryConstants.GET_ALL_GRADE_NAMES, stateOrgNodeId, testAdministrationVal, corpDiocese, school, groupFile, testProgram, customerId, "SCHOOL_OTH");
		if ((lstData != null) && (!lstData.isEmpty())) {
			for (Map<String, Object> fieldDetails : lstData) {
				grades.add((String) fieldDetails.get("GRADE_NAME"));
			}
		}
		logger.log(IAppLogger.INFO, "Grades Size: " + grades.size());
		logger.log(IAppLogger.INFO, "Grades: " + grades);
		return grades;
	}

	@Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, 'getSelectedClassNames' )")
	private List<String> getSelectedClassNames(String stateOrgNodeId, String studentIds, String customerId) {
		List<String> classes = new ArrayList<String>();
		// List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(CustomStringUtil.replaceCharacterInString('~', studentIds, IQueryConstants.GET_SELECTED_CLASS_NAMES), stateOrgNodeId, customerId);
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(CustomStringUtil.replaceCharacterInString('~', studentIds, IQueryConstants.GET_SELECTED_CLASS_NAMES));
		if ((lstData != null) && (!lstData.isEmpty())) {
			for (Map<String, Object> fieldDetails : lstData) {
				classes.add((String) fieldDetails.get("ORG_NODE_NAME"));
			}
		}
		logger.log(IAppLogger.INFO, "Classes Size: " + classes.size());
		logger.log(IAppLogger.INFO, "Classes: " + classes);
		return classes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#createJobTracking(com.ctb.prism.report.transferobject.GroupDownloadTO)
	 */
	public String createJobTracking(GroupDownloadTO to) {
		logger.log(IAppLogger.INFO, "Enter: createJobTracking()");
		String button = to.getButton();
		String testAdministrationVal = to.getTestAdministrationVal();
		String fileName = to.getFileName();
		String groupFile = to.getGroupFile();
		String students = to.getStudents();
		String orgNodeId = to.getSchool();
		String currUserId = to.getUserId();
		String currUserName = to.getUserName();
		String currAdminId = to.getAdminId();
		String currCustomerId = to.getCustomerId();

		logger.log(IAppLogger.INFO, "button = " + button);
		logger.log(IAppLogger.INFO, "testAdministrationVal = " + testAdministrationVal);
		logger.log(IAppLogger.INFO, "fileName = " + fileName);
		logger.log(IAppLogger.INFO, "groupFile = " + groupFile);
		logger.log(IAppLogger.INFO, "students = " + students);
		logger.log(IAppLogger.INFO, "orgNodeId = " + orgNodeId);
		logger.log(IAppLogger.INFO, "currUserId = " + currUserId);
		logger.log(IAppLogger.INFO, "currUserName = " + currUserName);
		logger.log(IAppLogger.INFO, "currAdminId = " + currAdminId);
		logger.log(IAppLogger.INFO, "currCustomerId = " + currCustomerId);

		Long job_id = getJdbcTemplatePrism().queryForLong(IQueryConstants.GET_PROCESS_SEQ);
		Long userId = (currUserId != null) ? Long.valueOf(currUserId) : 0;
		String job_name = groupFile;
		String extract_category = IApplicationConstants.EXTRACT_CATEGORY.AE.toString(); // As per requirement email
		String extract_filetype = groupFile;
		String request_type = IApplicationConstants.REQUEST_TYPE.GDF.toString(); // As per requirement email
		String request_details_str = Utils.objectToJson(to);
		String request_summary = "Group Download - " + groupFile + ": In Progress";
		InputStream is = null;
		is = new ByteArrayInputStream(request_details_str.getBytes());
		LobHandler lobHandler = new DefaultLobHandler();
		String request_filename = null;
		String request_email = to.getEmail();
		String job_log = null;
		String job_status = IApplicationConstants.JOB_STATUS.IP.toString();
		Long customerid = (currCustomerId != null) ? Long.valueOf(currCustomerId) : 0;
		Long productId = (testAdministrationVal != null) ? Long.valueOf(testAdministrationVal) : 0;

		logger.log(IAppLogger.INFO, "job_id = " + job_id);
		logger.log(IAppLogger.INFO, "userId = " + userId);
		logger.log(IAppLogger.INFO, "job_name = " + job_name);
		logger.log(IAppLogger.INFO, "extract_category = " + extract_category);
		logger.log(IAppLogger.INFO, "extract_filetype = " + extract_filetype);
		logger.log(IAppLogger.INFO, "request_type = " + request_type);
		logger.log(IAppLogger.INFO, "request_details_str = " + request_details_str);
		logger.log(IAppLogger.INFO, "request_summary = " + request_summary);
		logger.log(IAppLogger.INFO, "request_filename = " + request_filename);
		logger.log(IAppLogger.INFO, "request_email = " + request_email);
		logger.log(IAppLogger.INFO, "job_log = " + job_log);
		logger.log(IAppLogger.INFO, "job_status = " + job_status);
		logger.log(IAppLogger.INFO, "customerid = " + customerid);
		logger.log(IAppLogger.INFO, "productId = " + productId);

		int count = getJdbcTemplatePrism().update(
				IQueryConstants.INSERT_JOB_TRACKING,
				new Object[] { job_id, userId, job_name, extract_category, extract_filetype, request_type, request_summary, new SqlLobValue(is, request_details_str.length(), lobHandler),
						request_filename, request_email, job_log, job_status, customerid, productId, customerid },
				new int[] { Types.NUMERIC, Types.NUMERIC, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.CLOB, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
						Types.VARCHAR, Types.NUMERIC, Types.NUMERIC, Types.NUMERIC });
		logger.log(IAppLogger.INFO, "count = " + count);
		logger.log(IAppLogger.INFO, "Exit: createJobTracking()");
		return job_id.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#updateJobTracking(com.ctb.prism.report.transferobject.GroupDownloadTO)
	 */
	public int updateJobTracking(GroupDownloadTO to) {
		try {
			logger.log(IAppLogger.INFO, "Enter: updateJobTracking()");
			int updateCount = 0;

			String request_filename = to.getFileName();
			String gdfExpiryTime = to.getGdfExpiryTime();
			String job_log = to.getJobLog();
			String job_status = to.getJobStatus();
			String file_size = to.getFileSize();
			String job_id = to.getJobId();
			String request_details = to.getRequestDetails();

			logger.log(IAppLogger.INFO, "request_filename: " + request_filename);
			logger.log(IAppLogger.INFO, "gdfExpiryTime: " + gdfExpiryTime);
			logger.log(IAppLogger.INFO, "job_log: " + job_log);
			logger.log(IAppLogger.INFO, "job_status: " + job_status);
			logger.log(IAppLogger.INFO, "file_size: " + file_size);
			logger.log(IAppLogger.INFO, "job_id: " + job_id);
			logger.log(IAppLogger.INFO, "request_details: " + request_details);

			String request_summary = getRequestSummary(Utils.objectToJson(to));
			logger.log(IAppLogger.INFO, "request_summary: " + request_summary);

			updateCount = getJdbcTemplatePrism().update(CustomStringUtil.replaceCharacterInString('#', gdfExpiryTime, IQueryConstants.UPDATE_JOB_TRACKING), request_filename, request_summary, job_log, job_status, file_size, job_id);
			logger.log(IAppLogger.INFO, "updateCount: " + updateCount);

			logger.log(IAppLogger.INFO, "Exit: updateJobTracking()");
			return updateCount;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#getGDFilePaths(com.ctb.prism.report.transferobject.GroupDownloadTO)
	 */
	public Map<String, String> getGDFilePaths(GroupDownloadTO to) {
		logger.log(IAppLogger.INFO, "Enter: getGDFilePaths()");
		Map<String, String> filePaths = new LinkedHashMap<String, String>();
		String students = to.getStudents();
		String groupFile = to.getGroupFile();
		logger.log(IAppLogger.INFO, "students: " + students);
		logger.log(IAppLogger.INFO, "groupFile: " + groupFile);
		if (IApplicationConstants.EXTRACT_FILETYPE.ICL.toString().equals(groupFile)) {
			// Invitation Code Letter
			filePaths = getICLetterPaths(students);
		} else if (IApplicationConstants.EXTRACT_FILETYPE.BOTH.toString().equals(groupFile)) {
			// Both (IP and ISR)
			filePaths = getBothPaths(students);
		} else if (IApplicationConstants.EXTRACT_FILETYPE.IPR.toString().equals(groupFile)) {
			// Image Prints
			filePaths = getIPPaths(students);
		} else if (IApplicationConstants.EXTRACT_FILETYPE.ISR.toString().equals(groupFile)) {
			// Individual Student Report
			filePaths = getISRPaths(students);
		}
		logger.log(IAppLogger.INFO, "filePaths.size(): " + filePaths.size());
		logger.log(IAppLogger.INFO, "Exit: getGDFilePaths()");
		return filePaths;
	}
	
	/**
	 * Gets the list of IC letter paths.
	 * 
	 * @param students
	 *            Comma separated STUDENT_BIO_IDs
	 * @return
	 */
	@Cacheable(value = "configCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, 'getICLetterPaths' )")
	private Map<String, String> getICLetterPaths(String students) {
		Map<String, String> icPaths = new LinkedHashMap<String, String>();
		String[] studentIds = students.split(",");
		// Loop used to ensure collation hierarchy
		int icPrefixSequence = 0;
		for (String studentId : studentIds) { // TODO : Don't use loop
			String query = IQueryConstants.GET_IC_FILE_PATH;
			query = CustomStringUtil.replaceAll(query, "?", studentId);
			List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(query);
			if ((lstData != null) && (!lstData.isEmpty())) {
				for (Map<String, Object> fieldDetails : lstData) {
					String key = (String) fieldDetails.get("IC_FILE_LOC"); // Actual File Location
					String value = (String) fieldDetails.get("IC_FILE_NAME"); // New Name for the File
					icPrefixSequence = icPrefixSequence + 1;
					value = icPrefixSequence + "-" + value;
					icPaths.put(key, value);
				}
			}
		}
		logger.log(IAppLogger.INFO, "IC Size: " + icPaths.size());
		return icPaths;
	}

	/**
	 * Gets the list of Individual Student Report paths.
	 * 
	 * @param students
	 *            Comma separated STUDENT_BIO_IDs
	 * @return
	 */
	@Cacheable(value = "configCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, 'getISRPaths' )")
	private Map<String, String> getISRPaths(String students) {
		Map<String, String> isrPaths = new LinkedHashMap<String, String>();
		String[] studentIds = students.split(",");
		// Loop used to ensure collation hierarchy
		int isrPrefixSequence = 0;
		for (String studentId : studentIds) { // TODO : Don't use loop
			String query = IQueryConstants.GET_STUDENTS_PDF_FILE_PATH_ISR_ONLY;
			query = CustomStringUtil.replaceAll(query, "?", studentId);
			List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(query);
			if ((lstData != null) && (!lstData.isEmpty())) {
				for (Map<String, Object> fieldDetails : lstData) {
					String key = (String) fieldDetails.get("FILENAME"); // Actual File Location
					String value = (String) fieldDetails.get("FILE_NAME"); // New Name for the File
					isrPrefixSequence = isrPrefixSequence + 1;
					value = isrPrefixSequence + "-" + value;
					isrPaths.put(key, value);
				}
			}
		}
		logger.log(IAppLogger.INFO, "ISR Size: " + isrPaths.size());
		return isrPaths;
	}

	/**
	 * Gets the list of Image Print paths.
	 * 
	 * @param students
	 *            Comma separated STUDENT_BIO_IDs
	 * @return
	 */
	@Cacheable(value = "configCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, 'getIPPaths' )")
	private Map<String, String> getIPPaths(String students) {
		Map<String, String> iprPaths = new LinkedHashMap<String, String>();
		String[] studentIds = students.split(",");
		// Loop used to ensure collation hierarchy
		int iprPrefixSequence = 0;
		for (String studentId : studentIds) { // TODO : Don't use loop
			String query = IQueryConstants.GET_STUDENTS_PDF_FILE_PATH_IPR_ONLY;
			query = CustomStringUtil.replaceAll(query, "?", studentId);
			List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(query);
			if ((lstData != null) && (!lstData.isEmpty())) {
				for (Map<String, Object> fieldDetails : lstData) {
					String key = (String) fieldDetails.get("FILENAME"); // Actual File Location
					String value = (String) fieldDetails.get("FILE_NAME"); // New Name for the File
					iprPrefixSequence = iprPrefixSequence + 1;
					value = iprPrefixSequence + "-" + value;
					iprPaths.put(key, value);
				}
			}
		}
		logger.log(IAppLogger.INFO, "IPR Size: " + iprPaths.size());
		return iprPaths;
	}
	
	/**
	 * Gets the list of both Image Print and Individual Student Report paths.
	 * 
	 * @param students
	 * @return
	 */
	@Cacheable(value = "configCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, 'getBothPaths' )")
	private Map<String, String> getBothPaths(String students) {
		Map<String, String> bothPaths = new LinkedHashMap<String, String>();
		String[] studentIds = students.split(",");
		// Loop used to ensure collation hierarchy
		int isrPrefixSequence = 0;
		int iprPrefixSequence = 0;
		for (String studentId : studentIds) { // TODO : Don't use loop
			String query = IQueryConstants.GET_STUDENTS_PDF_FILE_PATH_BOTH;
			query = CustomStringUtil.replaceAll(query, "?", studentId);
			List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(query);
			if ((lstData != null) && (!lstData.isEmpty())) {
				for (Map<String, Object> fieldDetails : lstData) {
					String key = (String) fieldDetails.get("FILENAME"); // Actual File Location
					String value = (String) fieldDetails.get("FILE_NAME"); // New Name for the File
					if (value.startsWith("a-")) {
						isrPrefixSequence = isrPrefixSequence + 1;
						value = isrPrefixSequence + value;
					} else if (value.startsWith("b-")) {
						iprPrefixSequence = iprPrefixSequence + 1;
						value = iprPrefixSequence + value;
					}
					bothPaths.put(key, value);
				}
			}
		}
		logger.log(IAppLogger.INFO, "Both Size: " + bothPaths.size());
		return bothPaths;
	}

	/**
	 * Returns a scalar result.
	 * 
	 * @param query
	 * @return
	 */
	@Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, 'getResult' )")
	private String getResult(String query) {
		logger.log(IAppLogger.DEBUG, "Enter: getResult()");
		logger.log(IAppLogger.DEBUG, query);
		String result = null;
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(query);
		if ((lstData != null) && (!lstData.isEmpty())) {
			for (Map<String, Object> fieldDetails : lstData) {
				result = (String) fieldDetails.get("RESULT");
				break;
			}
		}
		logger.log(IAppLogger.DEBUG, "Exit: getResult() = " + result);
		return result;
	}

	
	//Fix for TD 77743 - By Joy
	/**
	 * @author Arunavo Retrieves and returns message corresponding configured in database
	 * @param msgtype
	 *            ,reportname and infoname
	 * @return message
	 */
	@Cacheable(value = "configCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getSystemConfigurationMessage') )")
	public String getSystemConfigurationMessage(Map<String, Object> paramMap) {
		logger.log(IAppLogger.INFO, "Enter: getSystemConfigurationMessage()");
		long t1 = System.currentTimeMillis();
		String MESSAGE_NAME = (String) paramMap.get("MESSAGE_NAME");
		String REPORT_ID = (String) paramMap.get("REPORT_ID");
		String MESSAGE_TYPE = (String) paramMap.get("MESSAGE_TYPE");

		List<Map<String, Object>> lstData = null;
		String systemConfig = "";
		
		//Fix for TD 77743 - By Joy
		if(IApplicationConstants.MORE_INFO.equals(MESSAGE_NAME) 
				&& IApplicationConstants.REPORT_SPECIFIC_MESSAGE_TYPE.equals(MESSAGE_TYPE)){
			
			String productId = (String) paramMap.get("productId");
			String customerId = (String) paramMap.get("customerId");
			lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_REPORT_MESSAGE_MORE_INFO
					,REPORT_ID, MESSAGE_TYPE, MESSAGE_NAME,productId,customerId);
			
		}else{
			lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_SYSTEM_CONFIGURATION_MESSAGE_REPORT_SPECIFIC
					,REPORT_ID, MESSAGE_TYPE, MESSAGE_NAME);
		}
		
		if (!lstData.isEmpty()) {
			for (Map<String, Object> fieldDetails : lstData) {
				if (fieldDetails.get("REPORT_MSG") != null || fieldDetails.get("REPORT_MSG") != "") {
					if (null != fieldDetails.get("REPORT_MSG")) {
						systemConfig = fieldDetails.get("REPORT_MSG").toString().trim();
					}
				}
			}
		}
		
		long t2 = System.currentTimeMillis();
		logger.log(IAppLogger.INFO, "Exit: getSystemConfigurationMessage(): " + String.valueOf(t2 - t1) + "ms");
		return systemConfig;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#getReportMessage(java.util.Map)
	 */
	@Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getReportMessage') )")
	public String getReportMessage(Map<String, Object> paramMap) {
		logger.log(IAppLogger.INFO, "Enter: getReportMessage()");
		String reportId = (String) paramMap.get("REPORT_ID");
		String messageType = (String) paramMap.get("MESSAGE_TYPE");
		String messageName = (String) paramMap.get("MESSAGE_NAME");
		String productId = (String) paramMap.get("PRODUCT_ID");
		String customerId = (String) paramMap.get("CUSTOMER_ID");
		String orgNodeLevel = (String) paramMap.get("ORG_NODE_LEVEL");
		String message = null;
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_REPORT_MESSAGE, reportId, messageType, messageName, productId, customerId, orgNodeLevel);
		if (!lstData.isEmpty()) {
			for (Map<String, Object> fieldDetails : lstData) {
				message = (String) fieldDetails.get("REPORT_MSG");
			}
		}
		logger.log(IAppLogger.INFO, "Exit: getReportMessage()");
		return message;
	}
	
	@Cacheable(value = "configCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getAllReportMessages') )")
	public List<ReportMessageTO> getAllReportMessages(Map<String, Object> paramMap) {
		logger.log(IAppLogger.INFO, "Enter: getReportMessage()");
		List<ReportMessageTO> reportMessageList = new ArrayList<ReportMessageTO>();
		String reportId = (String) paramMap.get("REPORT_ID");
		String productId = (String) paramMap.get("PRODUCT_ID");
		String customerId = (String) paramMap.get("CUSTOMER_ID");
		String orgNodeLevel = (String) paramMap.get("ORG_NODE_LEVEL");
		String userId = (String) paramMap.get("USER_ID");
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ALL_REPORT_MESSAGES, reportId, productId, customerId, orgNodeLevel);
		if (!lstData.isEmpty()) {
			for (Map<String, Object> fieldDetails : lstData) {
				ReportMessageTO to = new ReportMessageTO();
				to.setMessageType((String) fieldDetails.get("MESSAGE_TYPE"));
				to.setMessageName((String) fieldDetails.get("MESSAGE_NAME"));
				to.setMessage((String) fieldDetails.get("REPORT_MSG"));
				to.setDisplayFlag(IApplicationConstants.FLAG_Y);
				reportMessageList.add(to);
			}
		}
		logger.log(IAppLogger.INFO, "reportMessageList.size()=" + reportMessageList.size());
		logger.log(IAppLogger.INFO, "Exit: getReportMessage()");
		return reportMessageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#getProcessDataGD(java.lang.String)
	 */
	//@Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, 'getProcessDataGD' )")
	public JobTrackingTO getProcessDataGD(String processId) {
		logger.log(IAppLogger.INFO, "Enter: getProcessDataGD()");
		JobTrackingTO to = new JobTrackingTO();
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList("SELECT * FROM JOB_TRACKING WHERE JOB_ID = ?", processId);
		if (!lstData.isEmpty()) {
			for (Map<String, Object> data : lstData) {
				String createdDate = getFormattedDate((Timestamp) data.get("CREATED_DATE_TIME"), "MM/dd/yyyy HH:mm:ss aa");
				String extractStartdate = getFormattedDate((Timestamp) data.get("EXTRACT_STARTDATE"), "MM/dd/yyyy HH:mm:ss aa");
				to.setJobId(((BigDecimal) data.get("job_id")).longValue());
				to.setExtractStartdate(extractStartdate);
				to.setCreatedDateTime(createdDate);
				to.setExtractFiletype((String) data.get("extract_filetype"));
				to.setRequestType((String) data.get("request_type"));
				to.setJobStatus((String) data.get("job_status"));
				to.setRequestDetails((String) data.get("REQUEST_DETAILS"));
				to.setQuerySheetTO(getQuerySheetTO((String) data.get("request_type"), (String) data.get("request_details")));
			}
		}
		logger.log(IAppLogger.INFO, "Exit: getProcessDataGD()");
		return to;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#getConventionalFileNameGD(java.lang.Long)
	 */
	@Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, 'getConventionalFileNameGD' )")
	public String getConventionalFileNameGD(Long orgNodeId) {
		logger.log(IAppLogger.INFO, "Enter: getConventionalFileNameGD()");
		String conventionalFileName = null;
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_FILENAME_GD, orgNodeId);
		if (!lstData.isEmpty()) {
			for (Map<String, Object> fieldDetails : lstData) {
				conventionalFileName = (String) fieldDetails.get("FILE_NAME");
			}
		}
		logger.log(IAppLogger.INFO, "Exit: getConventionalFileNameGD()");
		return conventionalFileName;
	}

	/**
	 * Returns an Ancestor OrgNodeId for the mentioned OrgNodeLevel.
	 * 
	 * @param orgNodeId
	 * @param ancestorLevel
	 * @return
	 */
	@Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, 'getAncestorOrgNodeId' )")
	public String getAncestorOrgNodeId(String orgNodeId, int ancestorLevel) {
		String ancestorOrgNodeId = "-1";
		if (orgNodeId == null) {
			return ancestorOrgNodeId;
		}
		String orgNodeIdPath = getOrgNodeIdPath(orgNodeId);
		logger.log(IAppLogger.INFO, "orgNodeIdPath = " + orgNodeIdPath);
		String[] ancestorIds = orgNodeIdPath.split("~");
		if (ancestorIds.length >= ancestorLevel) {
			ancestorOrgNodeId = ancestorIds[ancestorLevel];
		}
		return ancestorOrgNodeId;
	}

	/**
	 * Returns the OrgNodeIdPath.
	 * 
	 * @param orgNodeId
	 * @return
	 */
	@Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, 'getOrgNodeIdPath' )")
	public String getOrgNodeIdPath(String orgNodeId) {
		if (orgNodeId == null) {
			return "0";
		}
		StringBuilder orgNodeIdPath = new StringBuilder();
		orgNodeIdPath.insert(0, orgNodeId);
		String parentOrgNodeId = getParentOrgNodeId(orgNodeId);
		while ((parentOrgNodeId != null) && (!"0".equals(parentOrgNodeId))) {
			orgNodeIdPath.insert(0, "~");
			orgNodeIdPath.insert(0, parentOrgNodeId);
			parentOrgNodeId = getParentOrgNodeId(parentOrgNodeId);
		}
		orgNodeIdPath.insert(0, "0~");
		return orgNodeIdPath.toString();
	}

	/**
	 * Returns the Parent OrgNodeId.
	 * 
	 * @param orgNodeId
	 * @return
	 */
	@Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, 'getParentOrgNodeId' )")
	public String getParentOrgNodeId(String orgNodeId) {
		String parentOrgNodeId = null;
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList("SELECT PARENT_ORG_NODEID FROM ORG_NODE_DIM WHERE ORG_NODEID = ?", orgNodeId);
		if (!lstData.isEmpty()) {
			for (Map<String, Object> fieldDetails : lstData) {
				parentOrgNodeId = fieldDetails.get("PARENT_ORG_NODEID").toString();
			}
		}
		return parentOrgNodeId;
	}

	@Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, 'getCustProdId' )")
	public String getCustProdId(String custId, String prodId) {
		String custProdId = "-1";
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList("SELECT CUST_PROD_ID  FROM CUST_PRODUCT_LINK WHERE CUSTOMERID = ? AND PRODUCTID = ?", custId, prodId);
		if (!lstData.isEmpty()) {
			for (Map<String, Object> fieldDetails : lstData) {
				custProdId = fieldDetails.get("CUST_PROD_ID").toString();
			}
		}
		return custProdId;
	}

	public String getStudentFileName(String type, String studentBioId, String custProdId) {
		logger.log(IAppLogger.INFO, "Enter: getStudentFileName()");
		logger.log(IAppLogger.INFO, "type = " + type);
		logger.log(IAppLogger.INFO, "studentBioId = " + studentBioId);
		String fileName = "";
		List<Map<String, Object>> lstData = null;
		if (IApplicationConstants.EXTRACT_FILETYPE.ISR.toString().equals(type)) {
			lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_STUDENT_FILE_NAME, studentBioId, IApplicationConstants.EXTRACT_FILETYPE.ISR.toString(), custProdId);
		} else if (IApplicationConstants.EXTRACT_FILETYPE.IPR.toString().equals(type)) {
			lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_STUDENT_FILE_NAME, studentBioId, "IP", custProdId);
		}
		if (lstData != null && !lstData.isEmpty()) {
			for (Map<String, Object> fieldDetails : lstData) {
				fileName = (String) fieldDetails.get("FILENAME");
			}
		}
		logger.log(IAppLogger.INFO, "fileName = " + fileName);
		logger.log(IAppLogger.INFO, "Exit: getStudentFileName()");
		return fileName;
	}
	
	@Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, 'getListOfRoles' )")
	private String getListOfRoles(Long reportId) {
		logger.log(IAppLogger.INFO, "Enter: getListOfRoles()"); 
		String roles = null;
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_LIST_OF_ROLES,reportId);
		if (!lstData.isEmpty()) {
			for (Map<String, Object> fieldDetails : lstData) {
				roles = fieldDetails.get("ROLES").toString();
			}
		}
		return roles;
	}
	
}
