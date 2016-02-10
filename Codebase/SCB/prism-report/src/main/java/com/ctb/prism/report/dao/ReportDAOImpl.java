package com.ctb.prism.report.dao;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Clob;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
import com.ctb.prism.report.transferobject.ObjectValueTO;
import com.ctb.prism.report.transferobject.QuerySheetTO;
import com.ctb.prism.report.transferobject.ReportMessageTO;
import com.ctb.prism.report.transferobject.ReportParameterTO;
import com.ctb.prism.report.transferobject.ReportTO;
import com.ctb.prism.webservice.transferobject.ReportActionTO;
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
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#parameters) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).string(#jasperReport.name)).concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#parameters)) )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#parameters) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).string(#jasperReport.name)).concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#parameters)) )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#parameters) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).string(#jasperReport.name)).concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#parameters)) )")
	} )
	public JasperPrint getFilledReport(JasperReport jasperReport, Map<String, Object> parameters) throws Exception {
		Connection conn = null;
		logger.log(IAppLogger.INFO, CustomStringUtil.appendString("####----------------------------JASPER--PRINT-----------------------------", jasperReport.getName()));
		String contractName = (String) parameters.get("contractName");
		Object obj = parameters.get("p_Ethnicities");
		if(obj != null && obj instanceof String) {
			logger.log(IAppLogger.INFO, obj.toString() + " ----------------------------------------------------- ethnicity is string !!! - should be List of String" + jasperReport.getName());
			String[] params = obj.toString().replace("[", "").replace("]", "").split(",");
			List<String> inputCollection = Arrays.asList(params);
			parameters.put("p_Ethnicities", inputCollection);
		}
		if(obj != null && obj instanceof List<?>) {
			String ethnicity0 = ((List<String>) obj).get(0);
			if(ethnicity0 != null && ethnicity0.contains(",")) {
				logger.log(IAppLogger.INFO, obj.toString() + " -----------xxxxxxxxxxxxxx------------------------ ethnicity is List of string -- but not splitted" + jasperReport.getName());
				String[] params = ethnicity0.replace("[", "").replace("]", "").split(",");
				List<String> inputCollection = Arrays.asList(params);
				parameters.put("p_Ethnicities", inputCollection);
			}
		}
		logger.log(IAppLogger.INFO, "contractName = " + contractName);
		try {
			if (contractName != null && !contractName.isEmpty()) {
				conn = getPrismConnection(contractName);
			} else {
				conn = getPrismConnection();
			}
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
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).string(#jasperReport.name)).concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#parameters)) )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).string(#jasperReport.name)).concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#parameters)) )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).string(#jasperReport.name)).concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#parameters)) )")
	} )
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
	
	public JasperPrint getFilledReportIC(JasperReport jasperReport, Map<String, Object> parameters) throws Exception {
		Connection conn = null;
		logger.log(IAppLogger.INFO, CustomStringUtil.appendString("####----------------------------IC PDF-----------------------------", jasperReport.getName()));
		String contractName = (String)parameters.get("contractName");
		try {
			try{
				conn = getPrismConnection();
			}catch(Exception e){
				conn = getPrismConnection(contractName);
			}
			
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
	@com.googlecode.ehcache.annotations.TriggersRemove(cacheName={"compiledJrxml", "allReports", "mainReport"}, removeAll=true)
	public void removeReportCache() {
		logger.log(IAppLogger.INFO, "Removed report cache");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#removeCache()
	 */
	@CacheEvict(value = { "inorsDefaultCache", "inorsAdminCache", "inorsConfigCache", "inorsUserCache",
			"tascDefaultCache", "tascAdminCache", "tascConfigCache", "tascUserCache",
			"usmoDefaultCache", "usmoAdminCache", "usmoConfigCache", "usmoUserCache"}, allEntries = true)
	public void removeCache() {
		logger.log(IAppLogger.INFO, "Removed all cache");
	}
	
	@Deprecated
	public void removeCache(String cacheKey) {
		// Deprecated
	}
	
	@CacheEvict(value = { "inorsDefaultCache", "inorsAdminCache", "inorsConfigCache", "inorsUserCache"}, allEntries = true)
	public boolean removeInorsCache(String contract) {
		logger.log(IAppLogger.INFO, "Removed all cache for " +contract );
		return true;
	}
	
	@CacheEvict(value = { "tascDefaultCache", "tascAdminCache", "tascConfigCache", "tascUserCache"}, allEntries = true)
	public boolean removeTascCache(String contract) {
		logger.log(IAppLogger.INFO, "Removed all cache for " +contract );
		return true;
	}
	
	@CacheEvict(value = { "usmoDefaultCache", "usmoAdminCache", "usmoConfigCache", "usmoUserCache"}, allEntries = true)
	public boolean removeUsmoCache(String contract) {
		logger.log(IAppLogger.INFO, "Removed all cache for " +contract );
		return true;
	}
	
	public void removeConfigurationCache(String contract) {
		if("inors".equals(contract)) removeInorsConfig(contract);
		if("tasc".equals(contract)) removeTascConfig(contract);
		if("usmo".equals(contract)) removeUsmoConfig(contract);
	}
	
	@CacheEvict(value = {"inorsConfigCache"}, allEntries = true)
	public void removeInorsConfig(String contract) {
		logger.log(IAppLogger.INFO, "Removed config cache for "+contract);
	}
	
	@CacheEvict(value = {"tascConfigCache"}, allEntries = true)
	public void removeTascConfig(String contract) {
		logger.log(IAppLogger.INFO, "Removed config cache for "+contract);
	}
	
	@CacheEvict(value = {"usmoConfigCache"}, allEntries = true)
	public void removeUsmoConfig(String contract) {
		logger.log(IAppLogger.INFO, "Removed config cache for "+contract);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#getReportJasperObjectList(java.lang.String)
	 */
	//@Cacheable(value = "compiledJrxml", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).string(#reportPath)).concat(#root.method.name) )")
	@com.googlecode.ehcache.annotations.Cacheable(cacheName = "compiledJrxml")
	public List<ReportTO> getReportJasperObjectList(final String reportPath) throws JRException {
		logger.log(IAppLogger.INFO, "Enter: getReportJasperObjectList()");

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
				logger.log(IAppLogger.INFO, "<<<< Time Taken: ReportDAOImpl : getReportJasperObjectList >>>>" + CustomStringUtil.getHMSTimeFormat(end - start));
				logger.log(IAppLogger.INFO, "Exit: getReportJasperObjectList()");
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
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key = "T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).string(#reportPath)).concat(#root.method.name) )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key = "T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).string(#reportPath)).concat(#root.method.name) )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key = "T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).string(#reportPath)).concat(#root.method.name) )")
	} )
	public List<InputControlTO> getInputControlDetails(String reportPath) {
		logger.log(IAppLogger.INFO, "Enter: getInputControlDetails()");
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
		logger.log(IAppLogger.INFO, "Exit: getInputControlDetails()");
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
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).string(#query)).concat(#root.method.name) )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).string(#query)).concat(#root.method.name) )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).string(#query)).concat(#root.method.name) )")
	} )
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
	 * Returns P if the user has Parent Role or returns E if the username is an Education Center User. Else returns O.
	 * 
	 * @param username
	 * @return
	 */
	private String getUserType(String username) {
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_USER_TYPE, username);
		if (!lstData.isEmpty()) {
			return IApplicationConstants.PARENT_USER_FLAG;
		} else {
			lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_EDU_USER_TYPE, username);
			if (!lstData.isEmpty()) {
				return IApplicationConstants.EDU_USER_FLAG;
			} else {
				return IApplicationConstants.ORG_USER_FLAG;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#getTenantId(java.lang.String)
	 */
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).string(#userName)).concat('getTenantId') )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).string(#userName)).concat('getTenantId') )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).string(#userName)).concat('getTenantId') )")
	} )
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
	
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).string(#reportUrl)).concat('getReportId') )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).string(#reportUrl)).concat('getReportId') )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).string(#reportUrl)).concat('getReportId') )")
	} )
	public String getReportId(String reportUrl) {
		return getJdbcTemplatePrism().queryForObject(IQueryConstants.GET_REPORT_ID, new Object[] { reportUrl + "%" }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int col) throws SQLException {
				return ((BigDecimal) rs.getObject(1)).toString();
			}
		});
	}

	/**@author Joy
	 * Move the queries to package
	 * (non-Javadoc)
	 * @see com.ctb.prism.report.dao.IReportDAO#getAllReportList(java.util.Map)
	 * @Cacheable(cacheName = "allReports")
	 */
	@SuppressWarnings("unchecked")
	@Caching( cacheable = {
			@Cacheable(value = "inorsConfigCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getAllReportList') )"),
			@Cacheable(value = "tascConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getAllReportList') )"),
			@Cacheable(value = "usmoConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getAllReportList') )")
	} )
	public List<ReportTO> getAllReportList(final Map<String, Object> paramMap) {

		logger.log(IAppLogger.INFO, "Enter: ReportDAOImpl - getAllReportList()");
		long t1 = System.currentTimeMillis();
		
		//final UserTO loggedinUserTO = (UserTO) paramMap.get("loggedinUserTO");
		final long loggedInCustomer = Long.valueOf( paramMap.get("loggedInCustomer").toString());
		
		List<ReportTO> reports = null;
		try {
			reports = (List<ReportTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						CallableStatement cs = null;
						cs = con.prepareCall("{call " + IQueryConstants.GET_DASHBOARD_DETAILS + "}");
						cs.setLong(1, loggedInCustomer);
						if (IApplicationConstants.PURPOSE_EDIT_REPORT.equals((String)paramMap.get("editReport"))) {
							cs.setLong(2, Long.parseLong((String)paramMap.get("reportId")));
						}else{
							cs.setLong(2, IApplicationConstants.DEFAULT_PRISM_VALUE);
						}
						cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR);
						return cs;
					}
				}, new CallableStatementCallback<Object>() {
					public Object doInCallableStatement(CallableStatement cs) {
	        			ResultSet rsReport = null;
	        			List<ReportTO> reportTOResult 
	        							= new ArrayList<ReportTO>();
	        			try {
							cs.execute();
							rsReport = (ResultSet) cs.getObject(3);
	
							ReportTO to = null;
							while(rsReport.next()){
								to = new ReportTO();
								to.setReportId(rsReport.getLong("ID"));
								to.setReportName(rsReport.getString("REPORT_NAME"));
								to.setReportDescription(rsReport.getString("REPORT_DESC"));
								to.setReportUrl(rsReport.getString("REPORT_FOLDER_URI"));
								to.setReportOriginalUrl(rsReport.getString("REPORT_FOLDER_URI"));
								to.setReportType(rsReport.getString("REPORT_TYPE"));
								to.setReportSequence(rsReport.getString("REPORT_SEQ") == null ? to.getReportId() : rsReport.getLong("REPORT_SEQ"));
								to.setEnabled(IApplicationConstants.ACTIVE_FLAG.equals(rsReport.getString("STATUS")) ? true : false);
								String orgLevels = rsReport.getString("ORG_NODE_LEVEL");
								orgLevels = orgLevels.replace("Corporation,Diocese", "Corporation/Diocese");
								to.setAllOrgNode(orgLevels);
								
								String strRoles = rsReport.getString("ROLES");
								if (strRoles != null && strRoles.length() > 0) {
									String[] roles = strRoles.split(",");
									for (String role : roles) {
										ROLE_TYPE role_type = Utils.getRoles(role);
										if (role_type != null) {
											to.addRole(role_type);
										}
									}
								}
								String strOrgLevl = rsReport.getString("ORG_NODE_LEVEL");
								strOrgLevl = strOrgLevl.replace("Corporation,Diocese", "Corporation/Diocese");
								if (strOrgLevl != null && strOrgLevl.length() > 0) {
									String[] orgLevel = strOrgLevl.split(",");
									to.setOrgNodeLevelArr(orgLevel);
								}
								String custProdLinks = rsReport.getString("CUST_PROD_ID");
								if (custProdLinks != null && custProdLinks.length() > 0) {
									String[] customerProductArr = custProdLinks.split(",");
									to.setCustomerProductArr(customerProductArr);
								}
								to.setMenuId(rsReport.getString("MENUID"));
								to.setMenuName(rsReport.getString("MENUNAME"));
								reportTOResult.add(to);
							}
							
	        			} catch (SQLException e) {
	        				e.printStackTrace();
	        			}
	        			return reportTOResult;
					}	
			});
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ReportDAOImpl - getAllReportList() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return reports;
	}

	@SuppressWarnings("unchecked")
	/*@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #p4, 'getAssessmentList' )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #p4, 'getAssessmentList' )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #p4, 'getAssessmentList' )")
	} )*/
	private List<AssessmentTO> getAssessmentList(final String query, final String reportTypeLike, final String roles, final Long orgNodeLevel, final long custProdId, Map<String, Object> paramMap) {
		String contractName = (String) paramMap.get("contractName");
		if(contractName == null) {
			contractName = Utils.getContractName();
		}
		return (List<AssessmentTO>) getJdbcTemplatePrism(contractName).execute(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement cs = con.prepareCall(query);
				cs.setString(1, reportTypeLike);
				cs.setString(2, roles);
				cs.setLong(3, orgNodeLevel);
				cs.setLong(4, custProdId);
				cs.registerOutParameter(5, oracle.jdbc.OracleTypes.CURSOR);
				cs.registerOutParameter(6, oracle.jdbc.OracleTypes.VARCHAR);
				return cs;
			}
		}, new CallableStatementCallback<Object>() {
			public Object doInCallableStatement(CallableStatement cs) {
				ResultSet data = null;
				List<AssessmentTO> assessmentList = new ArrayList<AssessmentTO>();
				try {
					cs.execute();
					data = (ResultSet) cs.getObject(5);
					Utils.logError(cs.getString(6));
					long oldAssessmentId = -1;
					AssessmentTO assessmentTO = null;
					while (data.next()) {
						long assessmentId = Long.valueOf(data.getString("MENU_ID"));
						if (oldAssessmentId != assessmentId) {
							oldAssessmentId = assessmentId;
							assessmentTO = new AssessmentTO();
							assessmentTO.setAssessmentId(assessmentId);
							assessmentTO.setAssessmentName(data.getString("MENU_NAME"));
							assessmentList.add(assessmentTO);
						}
						ReportTO reportTO = new ReportTO();
						reportTO.setReportId(Long.valueOf(data.getString("REPORT_ID")));
						reportTO.setReportName(data.getString("REPORT_NAME"));
						reportTO.setReportUrl(data.getString("REPORT_FOLDER_URI"));
						reportTO.setEnabled(data.getString("STATUS").equals(IApplicationConstants.ACTIVE_FLAG) ? true : false);
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
						reportTO.setReportType(data.getString("TYPE"));
						reportTO.setOrgLevel(data.getString("ORGLEVEL") != null ? data.getString("ORGLEVEL") : "");
						assessmentTO.addReport(reportTO);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return assessmentList;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#getAssessments(java.util.Map)
	 */
	@Caching( cacheable = {
			@Cacheable(value = "inorsConfigCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'", key = "T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getAssessments'.concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)) )"),
			@Cacheable(value = "tascConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'",  key = "T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getAssessments'.concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)) )"),
			@Cacheable(value = "usmoConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'",  key = "T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getAssessments'.concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)) )")
	} )
	public List<AssessmentTO> getAssessments(Map<String, Object> paramMap) {
		logger.log(IAppLogger.INFO, "Enter: getAssessments()");
		
		/*boolean isSuperUser = ((Boolean) paramMap.get("isSuperUser")).booleanValue();
		boolean isGrowthUser = ((Boolean) paramMap.get("isGrowthUser")).booleanValue();
		boolean isEduUser = ((Boolean) paramMap.get("isEduUser")).booleanValue();
		boolean parentReports = ((Boolean) paramMap.get("parentReports")).booleanValue();*/
		String roles = (String) paramMap.get("roles");
		Long orgNodeLevel = (Long) paramMap.get("orgNodeLevel");
		long custProdId = ((Long) paramMap.get("custProdId")).longValue();
		/*logger.log(IAppLogger.INFO, "isSuperUser = " + isSuperUser);
		logger.log(IAppLogger.INFO, "isGrowthUser = " + isGrowthUser);
		logger.log(IAppLogger.INFO, "isEduUser = " + isEduUser);*/
		logger.log(IAppLogger.INFO, "orgNodeLevel = " + orgNodeLevel);
		logger.log(IAppLogger.INFO, "roles = " + roles);
		logger.log(IAppLogger.INFO, "custProdId = " + custProdId);
		List<AssessmentTO> assessments = null;
		if (roles.indexOf("ROLE_PARENT") != -1) {
			assessments = getAssessmentList(IQueryConstants.GET_ALL_ASSESSMENT_LIST, "PN%", roles, orgNodeLevel, custProdId, paramMap);
		} else if (roles.indexOf("ROLE_SUPER") != -1) { /* For super user */
			assessments = getAssessmentList(IQueryConstants.GET_ALL_ASSESSMENT_LIST, "API%", roles, orgNodeLevel, custProdId, paramMap);
		} else if (roles.indexOf("ROLE_GRW") != -1) {/* For growth user */
			assessments = getAssessmentList(IQueryConstants.GET_GROWTH_ASSESSMENT_LIST, "API%", IApplicationConstants.ROLE_GROWTH_ID, orgNodeLevel, custProdId, paramMap);
		} else if (roles.indexOf("ROLE_RESCORE") != -1) {/* For rescore user */
			assessments = getAssessmentList(IQueryConstants.GET_GROWTH_ASSESSMENT_LIST, "API%", IApplicationConstants.ROLE_RESCORE_ID, orgNodeLevel, custProdId, paramMap);
		} else if (orgNodeLevel== IApplicationConstants.DEFAULT_LEVELID_VALUE) {/* For education center user */
			assessments = getAssessmentList(IQueryConstants.GET_EDU_ASSESSMENT_LIST, "API%", roles, orgNodeLevel, custProdId, paramMap);
		} else { /* For All users other than growth user */
			assessments = getAssessmentList(IQueryConstants.GET_ALL_BUT_GROWTH_ASSESSMENT_LIST, "API%", roles, orgNodeLevel, custProdId, paramMap);
		}
		logger.log(IAppLogger.INFO, "Exit: getAssessments()");
		return assessments;
	}

	/**
	 * @author Joy
	 * Delete the report data
	 * Moved to package and minimize multiple DB Calls - By Joy
	 * @param reportId
	 * @return boolean
	 */
	@Caching( evict = { 
			@CacheEvict(value = "inorsConfigCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", allEntries = true),
			@CacheEvict(value = "tascConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  allEntries = true),
			@CacheEvict(value = "usmoConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  allEntries = true)
	} )
	public boolean deleteReport(final Map<String, Object> paramMap) throws SystemException {
		logger.log(IAppLogger.INFO, "Enter: ReportDAOImpl - deleteReport");
		long t1 = System.currentTimeMillis();
		//final UserTO loggedinUserTO = (UserTO) paramMap.get("loggedinUserTO");
		final long loggedInCustomer = Long.valueOf( paramMap.get("loggedInCustomer").toString());
		boolean status = false;
		
		try {
			status =  (Boolean) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						CallableStatement cs = null;
						cs = con.prepareCall("{call " + IQueryConstants.DELETE_REPORT + "}");
						cs.setLong(1, Long.parseLong((String)paramMap.get("reportId")));
						cs.setLong(2, loggedInCustomer);
						cs.registerOutParameter(3, oracle.jdbc.OracleTypes.NUMBER);
						cs.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR);
						return cs;
					}
				}, new CallableStatementCallback<Object>() {
					public Object doInCallableStatement(CallableStatement cs) {
						long executionStatus = 0;
						com.ctb.prism.core.transferobject.ObjectValueTO statusTO = new com.ctb.prism.core.transferobject.ObjectValueTO();
	        			try {
							cs.execute();
							executionStatus = cs.getLong(3);
							statusTO.setValue(Long.toString(executionStatus));
							statusTO.setName("");
							if(cs.getString(4)!= null && cs.getString(4).length() > 0) {
								logger.log(IAppLogger.ERROR, "Error while deleting report "+ cs.getString(4));
							}
						} catch (SQLException e) {
	        				e.printStackTrace();
	        			}
	        			return (Long.parseLong(statusTO.getValue()) == 1 ? true : false);
					}	
			});
		}catch(Exception e){
			e.printStackTrace();
			return false;
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ReportDAOImpl - deleteReport() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return status;
	}

	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getAllRoles'.concat(#root.method.name) )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getAllRoles'.concat(#root.method.name) )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getAllRoles'.concat(#root.method.name) )")
	} )
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

	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getAllRoles'.concat(#root.method.name) )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getAllRoles'.concat(#root.method.name) )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getAllRoles'.concat(#root.method.name) )")
	} )
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
	
	
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getAllRoles'.concat(#root.method.name) )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getAllRoles'.concat(#root.method.name) )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getAllRoles'.concat(#root.method.name) )")
	} )
	public List<ObjectValueTO>  getAllMenus() throws SystemException{
		List<ObjectValueTO> menuList = null;
		List<Map<String, Object>> dataList = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ALL_MENUS);
		if (dataList != null && dataList.size() > 0) {
			menuList = new ArrayList<ObjectValueTO>();
			for (Map<String, Object> data : dataList) {
				ObjectValueTO to = new ObjectValueTO();
				to.setValue(((BigDecimal) data.get("DB_MENUID")).toString());
				to.setName((String) data.get("MENU_NAME"));
				menuList.add(to);
			}
		}
		return menuList;
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

	@SuppressWarnings("unchecked")
	@Caching( cacheable = {
			@Cacheable(value = "inorsConfigCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getOrgNodeLevel') )"),
			@Cacheable(value = "tascConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getOrgNodeLevel') )"),
			@Cacheable(value = "usmoConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getOrgNodeLevel') )")
	} )
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getOrgNodeLevel(final Map<String, Object> paramMap) throws SystemException {

		logger.log(IAppLogger.INFO, "Enter: getOrgNodeLevel()");
		List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOList = null;
		long t1 = System.currentTimeMillis();
		
		try{
			objectValueTOList = (List<com.ctb.prism.core.transferobject.ObjectValueTO>) getJdbcTemplatePrism().execute(
				    new CallableStatementCreator() {
				        public CallableStatement createCallableStatement(Connection con) throws SQLException {
				        	CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_ORG_NODE_LEVEL + "}");
				            cs.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR); 
				            cs.registerOutParameter(2, oracle.jdbc.OracleTypes.VARCHAR);
				            return cs;				      			            
				        }
				    } ,   new CallableStatementCallback<Object>()  {
			        		public Object doInCallableStatement(CallableStatement cs) {
			        			ResultSet rsOrgNodeLevel = null;
			        			List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOResult 
			        							= new ArrayList<com.ctb.prism.core.transferobject.ObjectValueTO>();
			        			try {
									cs.execute();
									rsOrgNodeLevel = (ResultSet) cs.getObject(1);

									com.ctb.prism.core.transferobject.ObjectValueTO objectValueTO = null;
									while(rsOrgNodeLevel.next()){
										objectValueTO = new com.ctb.prism.core.transferobject.ObjectValueTO();
										objectValueTO.setValue(rsOrgNodeLevel.getString("VALUE"));
										objectValueTO.setName(rsOrgNodeLevel.getString("NAME"));
										objectValueTOResult.add(objectValueTO);
									}
									
			        			} catch (SQLException e) {
			        				e.printStackTrace();
			        			}
			        			return objectValueTOResult;
				        }
				    });
			
		}catch(Exception e){
			e.printStackTrace();
			throw new SystemException(e.getMessage());
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: getOrgNodeLevel() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return objectValueTOList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#loadManageMessage(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public List<ManageMessageTO> loadManageMessage(final Map<String, Object> paramMap) throws SystemException {
		logger.log(IAppLogger.INFO, "Enter: ReportDAOImpl - loadManageMessage()");
		long t1 = System.currentTimeMillis();
		
		final long reportId = ((Long) paramMap.get("reportId")).longValue();
		final long custProdId = ((Long) paramMap.get("custProdId")).longValue();
		final String reportName = ((String) paramMap.get("reportName"));
		List<ManageMessageTO> manageMessageTOList = null;
		try{
			manageMessageTOList = (List<ManageMessageTO>) getJdbcTemplatePrism().execute(
				    new CallableStatementCreator() {
				        public CallableStatement createCallableStatement(Connection con) throws SQLException {
				        	CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_MANAGE_MESSAGE_LIST + "}");
				            cs.setLong(1, reportId);	
				            if (IApplicationConstants.GENERIC_REPORT_NAME.equalsIgnoreCase(reportName)) {
				            	cs.setString(2, IApplicationConstants.GENERIC_MESSAGE_TYPE);
				            }else if(IApplicationConstants.PRODUCT_SPECIFIC_REPORT_NAME.equalsIgnoreCase(reportName)) {
				            	cs.setString(2, IApplicationConstants.PRODUCT_SPECIFIC_MESSAGE_TYPE);
				            }else{
				            	cs.setString(2, String.valueOf(IApplicationConstants.DEFAULT_PRISM_VALUE));
				            }
				            cs.setLong(3, custProdId);	
				            cs.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR); 
				            cs.registerOutParameter(5, oracle.jdbc.OracleTypes.VARCHAR);
				            return cs;				      			            
				        }
				    } ,   new CallableStatementCallback<Object>()  {
			        		public Object doInCallableStatement(CallableStatement cs) {
			        			ResultSet rsMessage = null;
			        			List<ManageMessageTO> manageMessageTOResult = new ArrayList<ManageMessageTO>();
			        			try {
									cs.execute();
									rsMessage = (ResultSet) cs.getObject(4);
									ManageMessageTO manageMessageTO = null;
									while(rsMessage.next()){
										manageMessageTO = new ManageMessageTO();
										String message = Utils.convertClobToString((Clob) rsMessage.getClob("MESSAGE")).trim();
										manageMessageTO.setMessage(("".equals(message)) ? IApplicationConstants.EMPTY_MESSAGE : message);
										manageMessageTO.setMessageTypeDesc(rsMessage.getString("MESSAGE_DESC").trim());
										manageMessageTO.setMessageTypeName(rsMessage.getString("MESSAGE_NAME").trim());
										manageMessageTO.setMessageTypeId(rsMessage.getLong("MESSAGE_TYPEID"));
										manageMessageTO.setMessageType(rsMessage.getString("MESSAGE_TYPE").trim());
										manageMessageTO.setReportId(rsMessage.getLong("REPORTID"));
										manageMessageTO.setActivationStatus(null != rsMessage.getString("ACTIVATION_STATUS") 
												? rsMessage.getString("ACTIVATION_STATUS").trim() 
														: IApplicationConstants.DEFAULT_VALUE_DRM_CHECKBOX);
										manageMessageTO.setCustProdIdHidden(rsMessage.getLong("CUST_PROD_ID"));
										if(rsMessage.getString("MESSAGE_TYPE").trim().equals("GSCM")){
											manageMessageTO.setReportName(IApplicationConstants.GENERIC_REPORT_NAME);
										}else if(rsMessage.getString("MESSAGE_TYPE").trim().equals("PSCM")){
											manageMessageTO.setReportName(IApplicationConstants.PRODUCT_SPECIFIC_REPORT_NAME);
										}
										manageMessageTOResult.add(manageMessageTO);
									}
								} catch (SQLException e) {
			        				e.printStackTrace();
			        			} catch (IOException e) {
									e.printStackTrace();
								}
			        			return manageMessageTOResult;
				        }
				    });
		}catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred in loadManageMessage():", e);
			return null;
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ReportDAOImpl - loadManageMessage() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return manageMessageTOList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	@Caching( evict = { 
			@CacheEvict(value = "inorsConfigCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", allEntries = true),
			@CacheEvict(value = "tascConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  allEntries = true),
			@CacheEvict(value = "usmoConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  allEntries = true)
	} )
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
					//ps.setLong(2, getCurrentMsgType (manageMessageTO.getCustProdIdHidden() , manageMessageTO.getMessageTypeId()));
					ps.setLong(2,  manageMessageTO.getMessageTypeId());
					ps.setString(3, manageMessageTO.getMessage());
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

	/**
	 * @author Joy
	 * Update the report data
	 * Moved to package and minimize multiple DB Calls - By Joy
	 * @param ReportTO
	 * @return boolean
	 */
	@Caching( evict = { 
			@CacheEvict(value = "inorsConfigCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", allEntries = true),
			@CacheEvict(value = "tascConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  allEntries = true),
			@CacheEvict(value = "usmoConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  allEntries = true)
	} )
	public boolean updateReportNew(final ReportTO reportTO) {
		logger.log(IAppLogger.INFO, "Enter: ReportDAOImpl - updateReportNew");
		long t1 = System.currentTimeMillis();
		
		final String userRoles = Utils.arrayToSeparatedString(reportTO.getUserRoles(),',');
		final String orgNodeLevels = Utils.arrayToSeparatedString(reportTO.getOrgNodeLevelArr(),',');
		final String customerProducts = Utils.arrayToSeparatedString(reportTO.getCustomerProductArr(),',');
		boolean status = false;
		
		try {
			status =  (Boolean) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						CallableStatement cs = null;
						cs = con.prepareCall("{call " + IQueryConstants.EDIT_REPORT + "}");
						cs.setLong(1, reportTO.getReportId());
						cs.setString(2, reportTO.getReportName());
						cs.setString(3, reportTO.getReportDescription());
						cs.setString(4, reportTO.getReportType());
						cs.setString(5, reportTO.getReportOriginalUrl());
						cs.setString(6, reportTO.getReportStatus());
						cs.setString(7, userRoles);
						cs.setString(8, orgNodeLevels);
						cs.setString(9, reportTO.getMenuId());
						cs.setString(10, customerProducts);
						cs.setLong(11,reportTO.getReportSequence());
						cs.registerOutParameter(12, oracle.jdbc.OracleTypes.NUMBER);
						cs.registerOutParameter(13, oracle.jdbc.OracleTypes.VARCHAR);
						return cs;
					}
				}, new CallableStatementCallback<Object>() {
					public Object doInCallableStatement(CallableStatement cs) {
						long executionStatus = 0;
						com.ctb.prism.core.transferobject.ObjectValueTO statusTO = new com.ctb.prism.core.transferobject.ObjectValueTO();
	        			try {
							cs.execute();
							executionStatus = cs.getLong(12);
							statusTO.setValue(Long.toString(executionStatus));
							statusTO.setName("");
							if(cs.getString(13)!= null && cs.getString(13).length() > 0) {
								logger.log(IAppLogger.ERROR, "Error while editing report "+ cs.getString(13));
							}
						} catch (SQLException e) {
	        				e.printStackTrace();
	        			}
	        			return (Long.parseLong(statusTO.getValue()) == 1 ? true : false);
					}	
			});
		}catch(Exception e){
			e.printStackTrace();
			return false;
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ReportDAOImpl - updateReportNew() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return status;
	}

	/**
	 * @author Joy
	 * add new dashboard
	 * Moved to package and minimize multiple DB Calls - By Joy
	 * @param reportParameterTO
	 * @return ReportTO
	 */
	@SuppressWarnings("unchecked")
	@Caching( evict = { 
			@CacheEvict(value = "inorsConfigCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", allEntries = true),
			@CacheEvict(value = "tascConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  allEntries = true),
			@CacheEvict(value = "usmoConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  allEntries = true)
	} )
	public ReportTO addNewDashboard(final ReportParameterTO reportParameterTO) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: ReportDAOImpl - addNewDashboard()");
		long t1 = System.currentTimeMillis();
		
		final String reportName = reportParameterTO.getReportName();
		final String reportDescription = reportParameterTO.getReportDescription();
		final String reportType = reportParameterTO.getReportType();
		final String reportUri = reportParameterTO.getReportUrl();
		final String assessmentType = reportParameterTO.getAssessmentName();
		final String reportStatus = reportParameterTO.getReportStatus();
		final String customerProducts = Utils.arrayToSeparatedString(reportParameterTO.getCustomerProductArr(),',');
		final String userRoles = Utils.arrayToSeparatedString(reportParameterTO.getUserRoles(),',');
		final String orgNodeLevels = Utils.arrayToSeparatedString(reportParameterTO.getOrgNodeLevel(),',');
		final String dbMenuId = reportParameterTO.getMenuId();
		final long customerId = Long.parseLong(reportParameterTO.getCustomerId());
		ReportTO report = null;
		
		try {
			report = (ReportTO) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						CallableStatement cs = null;
						cs = con.prepareCall("{call " + IQueryConstants.ADD_REPORT + "}");
						cs.setString(1, reportName);
						cs.setString(2, reportDescription);
						cs.setString(3, reportType);
						cs.setString(4, reportUri);
						cs.setString(5, reportStatus);
						cs.setString(6, userRoles);
						cs.setString(7, orgNodeLevels);
						cs.setString(8, dbMenuId);
						cs.setString(9, customerProducts);
						cs.setLong(10, customerId);
						cs.registerOutParameter(11, oracle.jdbc.OracleTypes.NUMBER);
						cs.registerOutParameter(12, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(13, oracle.jdbc.OracleTypes.VARCHAR);
						return cs;
					}
				}, new CallableStatementCallback<Object>() {
					public Object doInCallableStatement(CallableStatement cs) {
	        			ResultSet rsReport = null;
	        			ReportTO to = null;
	        			try {
							cs.execute();
							if(cs.getString(13)!= null && cs.getString(13).length() > 0) {
								logger.log(IAppLogger.ERROR, "Error while adding a report "+ cs.getString(13));
							}
							rsReport = (ResultSet) cs.getObject(12);
							if(rsReport.next()){
								to = new ReportTO();
								to.setReportId(rsReport.getLong("ID"));
								to.setReportName(rsReport.getString("REPORT_NAME"));
								to.setReportDescription(rsReport.getString("REPORT_DESC"));
								to.setReportUrl(rsReport.getString("REPORT_FOLDER_URI"));
								to.setReportOriginalUrl(rsReport.getString("REPORT_FOLDER_URI"));
								to.setReportType(rsReport.getString("REPORT_TYPE"));
								to.setReportSequence(rsReport.getString("REPORT_SEQ") == null ? to.getReportId() : rsReport.getLong("REPORT_SEQ"));
								to.setEnabled(IApplicationConstants.ACTIVE_FLAG.equals(rsReport.getString("STATUS")) ? true : false);
								String orgLevels = rsReport.getString("ORG_NODE_LEVEL");
								orgLevels = orgLevels.replace("Corporation,Diocese", "Corporation/Diocese");
								to.setAllOrgNode(orgLevels);
									
								String strRoles = rsReport.getString("ROLES");
								if (strRoles != null && strRoles.length() > 0) {
									String[] roles = strRoles.split(",");
									for (String role : roles) {
										ROLE_TYPE role_type = Utils.getRoles(role);
										if (role_type != null) {
											to.addRole(role_type);
										}
									}
								}
								String strOrgLevl = rsReport.getString("ORG_NODE_LEVEL");
								strOrgLevl = strOrgLevl.replace("Corporation,Diocese", "Corporation/Diocese");
								if (strOrgLevl != null && strOrgLevl.length() > 0) {
									String[] orgLevel = strOrgLevl.split(",");
									to.setOrgNodeLevelArr(orgLevel);
								}
								String custProdLinks = rsReport.getString("CUST_PROD_ID");
								if (custProdLinks != null && custProdLinks.length() > 0) {
									String[] customerProductArr = custProdLinks.split(",");
									to.setCustomerProductArr(customerProductArr);
								}
								to.setMenuId(rsReport.getString("MENUID"));
								to.setMenuName(rsReport.getString("MENUNAME"));
							}
						} catch (SQLException e) {
	        				e.printStackTrace();
	        			}
	        			return to;
					}	
			});
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ReportDAOImpl - addNewDashboard() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return report;
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
				String createdDate = getFormattedDate((Timestamp) data.get("CREATED_DATE_TIME"), "MM/dd/yyyy HH:mm:ss");
				to.setJobId(((BigDecimal) data.get("JOB_ID")).longValue());
				Timestamp ts = (Timestamp) data.get("UPDATED_DATE_TIME");
				if (ts == null) {
					ts = (Timestamp) data.get("CREATED_DATE_TIME");
				}
				Calendar cal = Calendar.getInstance();
				cal.setTime(ts);
				cal.add(Calendar.DAY_OF_WEEK, (Integer.parseInt((String) paramMap.get("gdfExpiryTime"))));
				ts.setTime(cal.getTime().getTime());
				ts = new Timestamp(cal.getTime().getTime());
				String updatedDate = getFormattedDate(ts, "MM/dd/yyyy HH:mm:ss");
				if (null != ((String) data.get("FILE_SIZE")) || ((String) data.get("FILE_SIZE")) != "") {
					to.setFileSize((String) data.get("FILE_SIZE"));
				} else {
					to.setFileSize(" ");
				}
				String filePath = (String) data.get("REQUEST_FILENAME");
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
				to.setQuerySheetTO(getQuerySheetTO((String) data.get("REQUEST_TYPE"), (String) data.get("EXTRACT_CATEGORY"), (String) data.get("REQUEST_DETAILS")));
				to.setS3Key((String) data.get("S3_KEY"));
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

	private QuerySheetTO getQuerySheetTO(String requestType, String extractCategory, String requestDetails) {
		logger.log(IAppLogger.INFO, "requestType = " + requestType);
		logger.log(IAppLogger.INFO, "requestDetails = " + requestDetails);
		QuerySheetTO querySheetTO = new QuerySheetTO();
		if (IApplicationConstants.REQUEST_TYPE.GDF.toString().equals(requestType) && IApplicationConstants.EXTRACT_CATEGORY.AE.toString().equals(extractCategory) && requestDetails != null) {
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
			int studentCount = (students != null) ? students.split(",").length : 0;
			int classCount = (classNames != null) ? classNames.split(",").length : 0;
			int schoolCount = (gradeNames != null) ? gradeNames.split(",").length : 0;
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
	public String getRequestSummary(String requestDetails, String contractName) {
		QuerySheetTO querySheetTO = getQuerySheetTO(IApplicationConstants.REQUEST_TYPE.GDF.toString(), IApplicationConstants.EXTRACT_CATEGORY.AE.toString(), requestDetails);
		String jobId = querySheetTO.getJobId();
		String productId = querySheetTO.getTestAdministration();
		String gradeId = querySheetTO.getGradeNames();
		String corpDiocese = querySheetTO.getCorpDiocese();
		String schools = querySheetTO.getSchoolNames();
		String klass = querySheetTO.getClassNames();
		String orgNodeIds = getOrgNodeIds(corpDiocese, schools, klass);
		String selectedStudents = querySheetTO.getSelectedStudents();
		String customerId = querySheetTO.getCustomerId();
		List<Map<String, Object>> dataList = getJdbcTemplatePrism(contractName).queryForList(CustomStringUtil.replaceCharacterInString('~', orgNodeIds, IReportQuery.GET_REQUEST_SUMMARY), jobId, productId, gradeId);
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
		
		List<Map<String, Object>> selectionDataList = getJdbcTemplatePrism(contractName).queryForList(CustomStringUtil.replaceCharacterInString('~', selectedStudents, IReportQuery.GET_STUDENT_SELECTION_STATISTICS));
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
			stateOrgNodeId = getAncestorOrgNodeId(gdTo.getSchool(), 1, contractName);
		}

		List<String> classNameList = new ArrayList<String>();
		if ("-1".equals(klass)) {
			classNameList = getSelectedClassNames(stateOrgNodeId, selectedStudents, customerId, contractName);
		} else {
			classNameList.add(valueMap.get(querySheetTO.getClassNames()));
		}

		List<String> gradeNameList = new ArrayList<String>();
		if ("-1".equals(gradeId)) {
			gradeNameList = getAllGradeNames(stateOrgNodeId, gdTo.getTestAdministrationVal(), corpDiocese, schools, gdTo.getGroupFile(), gdTo.getTestProgram(),customerId, contractName);
		} else {
			gradeNameList.add(getResult(CustomStringUtil.replaceCharacterInString('?', gradeId, IQueryConstants.GET_GRADE_NAME_BY_ID), contractName));
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
				String filePath = (String) data.get("REQUEST_FILENAME");
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
				to.setS3Key((String) data.get("S3_KEY"));
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
	 * @param gdfExpiryTime
	 * @return
	 * @throws Exception
	 */
	public Map<Long, String> getScheduledGroupFiles(Map<String, Object> paramMap) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: in getScheduledGroupFiles("+paramMap+")");
		String gdfExpiryTime = (String) paramMap.get("gdfExpiryTime");
		String contractName = (String) paramMap.get("contractName");
		Map<Long, String> jobMap = new HashMap<Long, String>();
		try {
			int gdfExpiryTimeRange = Integer.parseInt(gdfExpiryTime);
			List<Map<String, Object>> dataList = getJdbcTemplatePrism(contractName).queryForList(IReportQuery.GET_DELETE_SCHEDULED_GROUP_DOWNLOAD_LIST, gdfExpiryTimeRange);
			if (dataList != null && dataList.size() > 0) {
				for (Map<String, Object> data : dataList) {
					String filePath = (String) data.get("request_filename");
					Long jobId = ((BigDecimal) data.get("job_id")).longValue();
					if (filePath != null && filePath.length() > 0) {
						jobMap.put(jobId, filePath);
					}else{
						jobMap.put(jobId, "");
					}
				}
			}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Exception in getScheduledGroupFiles("+paramMap+")");
			e.printStackTrace();
		}
		logger.log(IAppLogger.INFO, "jobMap = " + jobMap);
		logger.log(IAppLogger.INFO, "Exit: in getScheduledGroupFiles("+paramMap+")");
		return jobMap;
	}

	/**
	 * @param appendLog
	 * @param jobId
	 * @throws Exception
	 */
	public void updateScheduledGroupFiles(Map<String, Object> paramMap) throws Exception {
		logger.log(IAppLogger.ERROR, "Enter: updateScheduledGroupFiles("+paramMap+")");
		String contractName = (String) paramMap.get("contractName");
		String appendLog = null;
		
		if(paramMap.get("appendLog") != null && ((String)paramMap.get("appendLog")).length() > 4000) 
			appendLog = ((String)paramMap.get("appendLog")).substring(0, 3999);
		else {
			appendLog = (String) paramMap.get("appendLog");
		}
		
		Long jobId = (Long) paramMap.get("jobId");
		try {
			getJdbcTemplatePrism(contractName).update(IReportQuery.DELETE_SCHEDULED_GROUP_FILES, appendLog, jobId);
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Exception in updateScheduledGroupFiles("+paramMap+")");
		}
		logger.log(IAppLogger.ERROR, "Exit: updateScheduledGroupFiles("+paramMap+")");
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
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( 'getTestAdministrations' )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( 'getTestAdministrations' )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( 'getTestAdministrations' )")
	} )
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getTestAdministrations() {
		return getJdbcTemplatePrism().query(IQueryConstants.GET_TEST_ADMINISTRATIONS_GD, new ObjectValueTOMapper());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#populateStudentTableGD(com.ctb.prism.report.transferobject.GroupDownloadTO)
	 */
	//@Cacheable(value = {"inorsDefaultCache", "tascDefaultCache"}, key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#to.string).concat('populateStudentTableGD') )")
	public List<GroupDownloadStudentTO> populateStudentTableGD(GroupDownloadTO to) {
		logger.log(IAppLogger.INFO, "Exit: populateStudentTableGD()");
		List<GroupDownloadStudentTO> studentList = new ArrayList<GroupDownloadStudentTO>();

		final String schoolId = to.getSchool();
		final String classId = to.getKlass();
		final String gradeId = to.getGrade();
		final String gradeIdCommaSep = Utils.arrayToSeparatedString(to.getGrades(), ',') ;
		final String testProgram = to.getTestProgram();
		final String collationHierarchy = to.getCollationHierarchy();
		final String groupFile = to.getGroupFile();
		final String customerId = to.getCustomerId();
		final String loggedInOrgNodeId = to.getLoggedInOrgNodeId();
		final String subtestCommaSep = Utils.arrayToSeparatedString(to.getSubtest(), ',') ;
		final String testAdministrationVal = to.getTestAdministrationVal();
		final String districtId = to.getDistrict();
		final String studentGroupsCommaSep = to.getStudentGroups();
		String userName = to.getUserName();
		String contractName = to.getContractName();
		
		if (testAdministrationVal == null || testAdministrationVal.isEmpty()) {
			logger.log(IAppLogger.ERROR, "testAdministrationVal cannot be null or empty: " + testAdministrationVal);
			logger.log(IAppLogger.INFO, "Exit: populateStudentTableGD()");
			return studentList;
		}

		logger.log(IAppLogger.INFO, "contractName = " + contractName);
		logger.log(IAppLogger.INFO, "schoolId = " + schoolId);
		logger.log(IAppLogger.INFO, "classId = " + classId);
		logger.log(IAppLogger.INFO, "gradeId = " + gradeId);
		logger.log(IAppLogger.INFO, "testProgram = " + testProgram);
		logger.log(IAppLogger.INFO, "collationHierarchy = " + collationHierarchy);
		logger.log(IAppLogger.INFO, "customerId = " + customerId);
		logger.log(IAppLogger.INFO, "loggedInOrgNodeId = " + loggedInOrgNodeId);
		logger.log(IAppLogger.INFO, "userName = " + userName);
		logger.log(IAppLogger.INFO, "subtestCommaSep = " + subtestCommaSep);
		logger.log(IAppLogger.INFO, "gradeIdCommaSep = " + gradeIdCommaSep);
		logger.log(IAppLogger.INFO, "testAdministrationVal = " + testAdministrationVal);
		logger.log(IAppLogger.INFO, "districtId = " + districtId);
		logger.log(IAppLogger.INFO, "studentGroupsCommaSep = " + studentGroupsCommaSep);
		
		if(IApplicationConstants.CONTRACT_NAME.usmo.toString().equals(contractName)){
			studentList = (List<GroupDownloadStudentTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_STUDENTS_G_MO);
					cs.setLong(1, Long.parseLong(testAdministrationVal));
					cs.setLong(2, Long.parseLong(schoolId));
					//cs.setLong(3, Long.parseLong(gradeId));
					cs.setString(3, gradeIdCommaSep);
					cs.setString(4, subtestCommaSep);
					cs.setString(5, studentGroupsCommaSep);
					cs.registerOutParameter(6, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(7, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					List<GroupDownloadStudentTO> studentList = new ArrayList<GroupDownloadStudentTO>();
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(6);
						GroupDownloadStudentTO student = null;
						while (rs.next()) {
							student = new GroupDownloadStudentTO();
							student.setId(rs.getString("STUDENT_BIO_ID"));
							student.setName(rs.getString("STUDENT_NAME"));
							student.setGrade(rs.getString("GRADE_NAME"));
							student.setSchool(rs.getString("SCHOOL_NAME"));
							student.setGradeId(rs.getString("GRADEID"));
							student.setGradeCode(rs.getString("GRADE_CODE"));
							student.setExtStudentId(rs.getString("EXT_STUDENT_ID"));
							student.setLastNameCap(rs.getString("LAST_NAME_CAP"));
							student.setCurYear(rs.getString("CURYEAR"));
							studentList.add(student);
						}
						return studentList;
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return studentList;
				}
			});
		}else{
			if ("-1".equals(classId)) {
				if ((schoolId != null) && (!"undefined".equalsIgnoreCase(schoolId))) {
					logger.log(IAppLogger.INFO, "ALL Classes");
					if ("-1".equals(gradeId)) {
						logger.log(IAppLogger.INFO, "ALL Grades");
						logger.log(IAppLogger.INFO, "{CALL PKG_GROUP_DOWNLOADS.SP_GET_STUDENTS_ALL_C_ALL_G("+loggedInOrgNodeId+", "+testAdministrationVal+", "+districtId+", "+schoolId+", "+testProgram+", "+customerId+", "+groupFile+", "+collationHierarchy+", REF_CURSOR, EXCEPTION)}");
						studentList = (List<GroupDownloadStudentTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
							public CallableStatement createCallableStatement(Connection con) throws SQLException {
								CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_STUDENTS_ALL_C_ALL_G);
								cs.setLong(1, Long.parseLong(loggedInOrgNodeId));
								cs.setLong(2, Long.parseLong(testAdministrationVal));
								cs.setLong(3, Long.parseLong(districtId));
								cs.setLong(4, Long.parseLong(schoolId));
								cs.setLong(5, Long.parseLong(testProgram));
								cs.setLong(6, Long.parseLong(customerId));
								cs.setString(7, groupFile);
								cs.setString(8, collationHierarchy);
								cs.registerOutParameter(9, oracle.jdbc.OracleTypes.CURSOR);
								cs.registerOutParameter(10, oracle.jdbc.OracleTypes.VARCHAR);
								return cs;
							}
						}, new CallableStatementCallback<Object>() {
							public Object doInCallableStatement(CallableStatement cs) {
								ResultSet rs = null;
								List<GroupDownloadStudentTO> studentList = new ArrayList<GroupDownloadStudentTO>();
								try {
									cs.execute();
									rs = (ResultSet) cs.getObject(9);
									studentList = getStudentListFromResultSet(rs);
								} catch (SQLException e) {
									e.printStackTrace();
								}
								return studentList;
							}
						});
					} else {
						logger.log(IAppLogger.INFO, "One Grade");
						logger.log(IAppLogger.INFO, "{CALL PKG_GROUP_DOWNLOADS.SP_GET_STUDENTS_ALL_C_ONE_G("+loggedInOrgNodeId+", "+testAdministrationVal+", "+districtId+", "+schoolId+", "+testProgram+", "+customerId+", "+gradeId+", "+collationHierarchy+", REF_CURSOR, EXCEPTION)}");
						studentList = (List<GroupDownloadStudentTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
							public CallableStatement createCallableStatement(Connection con) throws SQLException {
								CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_STUDENTS_ALL_C_ONE_G);
								cs.setLong(1, Long.parseLong(loggedInOrgNodeId));
								cs.setLong(2, Long.parseLong(testAdministrationVal));
								cs.setLong(3, Long.parseLong(districtId));
								cs.setLong(4, Long.parseLong(schoolId));
								cs.setLong(5, Long.parseLong(testProgram));
								cs.setLong(6, Long.parseLong(customerId));
								cs.setLong(7, Long.parseLong(gradeId));
								cs.setString(8, collationHierarchy);
								cs.registerOutParameter(9, oracle.jdbc.OracleTypes.CURSOR);
								cs.registerOutParameter(10, oracle.jdbc.OracleTypes.VARCHAR);
								return cs;
							}
						}, new CallableStatementCallback<Object>() {
							public Object doInCallableStatement(CallableStatement cs) {
								ResultSet rs = null;
								List<GroupDownloadStudentTO> studentList = new ArrayList<GroupDownloadStudentTO>();
								try {
									cs.execute();
									rs = (ResultSet) cs.getObject(9);
									studentList = getStudentListFromResultSet(rs);
								} catch (SQLException e) {
									e.printStackTrace();
								}
								return studentList;
							}
						});
					}
				}
			} else {
				logger.log(IAppLogger.INFO, "One Class");
				if ((classId != null) && (!"undefined".equalsIgnoreCase(classId))) {
					if ("-1".equals(gradeId)) {
						logger.log(IAppLogger.INFO, "ALL Grades");
						logger.log(IAppLogger.INFO, "{CALL PKG_GROUP_DOWNLOADS.SP_GET_STUDENTS_ONE_C_ALL_G("+testAdministrationVal+", "+customerId+", "+loggedInOrgNodeId+", "+districtId+", "+schoolId+", "+classId+", "+testProgram+", "+groupFile+", "+collationHierarchy+", REF_CURSOR, EXCEPTION)}");
						studentList = (List<GroupDownloadStudentTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
							public CallableStatement createCallableStatement(Connection con) throws SQLException {
								CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_STUDENTS_ONE_C_ALL_G);
								cs.setLong(1, Long.parseLong(testAdministrationVal));
								cs.setLong(2, Long.parseLong(customerId));
								cs.setLong(3, Long.parseLong(loggedInOrgNodeId));
								cs.setLong(4, Long.parseLong(districtId));
								cs.setLong(5, Long.parseLong(schoolId));
								cs.setLong(6, Long.parseLong(classId));
								cs.setLong(7, Long.parseLong(testProgram));
								cs.setString(8, groupFile);
								cs.setString(9, collationHierarchy);
								cs.registerOutParameter(10, oracle.jdbc.OracleTypes.CURSOR);
								cs.registerOutParameter(11, oracle.jdbc.OracleTypes.VARCHAR);
								return cs;
							}
						}, new CallableStatementCallback<Object>() {
							public Object doInCallableStatement(CallableStatement cs) {
								ResultSet rs = null;
								List<GroupDownloadStudentTO> studentList = new ArrayList<GroupDownloadStudentTO>();
								try {
									cs.execute();
									rs = (ResultSet) cs.getObject(10);
									studentList = getStudentListFromResultSet(rs);
								} catch (SQLException e) {
									e.printStackTrace();
								}
								return studentList;
							}
						});
					} else {
						logger.log(IAppLogger.INFO, "One Grade");
						logger.log(IAppLogger.INFO, "{CALL PKG_GROUP_DOWNLOADS.SP_GET_STUDENTS_ONE_C_ONE_G("+testAdministrationVal+", "+customerId+""+classId+", "+testProgram+", "+gradeId+", "+collationHierarchy+", REF_CURSOR, EXCEPTION)}");
						studentList = (List<GroupDownloadStudentTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
							public CallableStatement createCallableStatement(Connection con) throws SQLException {
								CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_STUDENTS_ONE_C_ONE_G);
								cs.setLong(1, Long.parseLong(testAdministrationVal));
								cs.setLong(2, Long.parseLong(customerId));
								cs.setLong(3, Long.parseLong(classId));
								cs.setLong(4, Long.parseLong(testProgram));
								cs.setLong(5, Long.parseLong(gradeId));
								cs.setString(6, collationHierarchy);
								cs.registerOutParameter(7, oracle.jdbc.OracleTypes.CURSOR);
								cs.registerOutParameter(8, oracle.jdbc.OracleTypes.VARCHAR);
								return cs;
							}
						}, new CallableStatementCallback<Object>() {
							public Object doInCallableStatement(CallableStatement cs) {
								ResultSet rs = null;
								List<GroupDownloadStudentTO> studentList = new ArrayList<GroupDownloadStudentTO>();
								try {
									cs.execute();
									rs = (ResultSet) cs.getObject(7);
									studentList = getStudentListFromResultSet(rs);
								} catch (SQLException e) {
									e.printStackTrace();
								}
								return studentList;
							}
						});
					}
				}
			}
		}

		logger.log(IAppLogger.INFO, "studentList.size() = " + studentList.size());
		logger.log(IAppLogger.INFO, "Exit: populateStudentTableGD()");
		return studentList;
	}
	
	private List<GroupDownloadStudentTO> getStudentListFromResultSet(ResultSet rs) throws SQLException {
		List<GroupDownloadStudentTO> studentList = new ArrayList<GroupDownloadStudentTO>();
		while (rs.next()) {
			String id = rs.getString("ID").toString();
			String name = (String) rs.getString("NAME");
			String klass = (String) rs.getString("CLASS");
			String grade = (String) rs.getString("GRADE");
			String ic = (String) rs.getString("IC");
			String isr = (String) rs.getString("ISR");
			String ip = (String) rs.getString("IP");
			String icFlag = (String) rs.getString("IC_FLAG");
			String isrFlag = (String) rs.getString("ISR_FLAG");
			String ipFlag = (String) rs.getString("IP_FLAG");
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
		return studentList;
	}

	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #p4, #p5, #p6, 'getAllGrades' )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #p4, #p5, #p6, 'getAllGrades' )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #p4, #p5, #p6, 'getAllGrades' )")
	} )
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
	
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #p4, 'getAllClassesForAllGrades' )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #p4, 'getAllClassesForAllGrades' )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #p4, 'getAllClassesForAllGrades' )")
	} )
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

	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #p4, #p5, #p6, #p7, 'getAllGradeNames' )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #p4, #p5, #p6, #p7, 'getAllGradeNames' )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #p4, #p5, #p6, #p7, 'getAllGradeNames' )")
	} )
	private List<String> getAllGradeNames(String stateOrgNodeId, String testAdministrationVal, String corpDiocese, String school, String groupFile, String testProgram, String customerId, String contractName) {
		List<String> grades = new ArrayList<String>();
		List<Map<String, Object>> lstData = getJdbcTemplatePrism(contractName)
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

	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, 'getSelectedClassNames' )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, 'getSelectedClassNames' )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, 'getSelectedClassNames' )")
	} )
	private List<String> getSelectedClassNames(String stateOrgNodeId, String studentIds, String customerId, String contractName) {
		List<String> classes = new ArrayList<String>();
		// List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(CustomStringUtil.replaceCharacterInString('~', studentIds, IQueryConstants.GET_SELECTED_CLASS_NAMES), stateOrgNodeId, customerId);
		List<Map<String, Object>> lstData = getJdbcTemplatePrism(contractName).queryForList(CustomStringUtil.replaceCharacterInString('~', studentIds, IQueryConstants.GET_SELECTED_CLASS_NAMES));
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
		String currUserName = to.getUserName();
		String currAdminId = to.getAdminId();
		String currCustomerId = to.getCustomerId();
		String studentSelection = IApplicationConstants.FLAG_N.equals(to.getStudentSelection()) ? "NO" : "YES";
		

		logger.log(IAppLogger.INFO, "button = " + button);
		logger.log(IAppLogger.INFO, "testAdministrationVal = " + testAdministrationVal);
		logger.log(IAppLogger.INFO, "fileName = " + fileName);
		logger.log(IAppLogger.INFO, "groupFile = " + groupFile);
		logger.log(IAppLogger.INFO, "students = " + students);
		logger.log(IAppLogger.INFO, "orgNodeId = " + orgNodeId);
		logger.log(IAppLogger.INFO, "currUserName = " + currUserName);
		logger.log(IAppLogger.INFO, "currAdminId = " + currAdminId);
		logger.log(IAppLogger.INFO, "currCustomerId = " + currCustomerId);
		logger.log(IAppLogger.INFO, "studentSelection = " + studentSelection);

		Long job_id = getJdbcTemplatePrism().queryForLong(IQueryConstants.GET_PROCESS_SEQ);
		String job_name = groupFile;
		String extract_category = IApplicationConstants.EXTRACT_CATEGORY.AE.toString(); // As per requirement email
		String extract_filetype = groupFile;
		
		String request_type = "";
		String request_summary = "";
		String request_filename = null;
		String otherRequestParams = null;
		String job_status = "";
		if(IApplicationConstants.DOWNLOAD_GRF_BUTTON.equals(button)){
			otherRequestParams = CustomStringUtil.appendString("cust_prod_id='",testAdministrationVal,"'|","appeal_ind='",studentSelection,"'");
			request_filename = fileName;
			request_type = IApplicationConstants.REQUEST_TYPE.GRF.toString();
			request_summary = "GRF Download - " + groupFile + ": Submitted";
			job_status = IApplicationConstants.JOB_STATUS.SU.toString();
		}else{
			request_type = IApplicationConstants.REQUEST_TYPE.GDF.toString(); // As per requirement email
			request_summary = "Group Download - " + groupFile + ": In Progress";
			job_status = IApplicationConstants.JOB_STATUS.IP.toString();
		}
		
		String request_details_str = Utils.objectToJson(to);
		InputStream is = null;
		is = new ByteArrayInputStream(request_details_str.getBytes());
		LobHandler lobHandler = new DefaultLobHandler();
		String request_email = to.getEmail();
		String job_log = null;
		
		Long customerid = (currCustomerId != null) ? Long.valueOf(currCustomerId) : 0;
		Long productId = (testAdministrationVal != null) ? Long.valueOf(testAdministrationVal) : 0;

		logger.log(IAppLogger.INFO, "job_id = " + job_id);
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
					new Object[] { job_id, currUserName, job_name, extract_category, extract_filetype, request_type, request_summary, new SqlLobValue(is, request_details_str.length(), lobHandler),
							request_filename, request_email, job_log, job_status, customerid, productId, customerid, otherRequestParams },
					new int[] { Types.NUMERIC, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.CLOB, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
							Types.VARCHAR, Types.NUMERIC, Types.NUMERIC, Types.NUMERIC, Types.VARCHAR });
		
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
		logger.log(IAppLogger.INFO, "Enter: updateJobTracking()");
		int updateCount = 0;
		try {
			String request_filename = to.getFileName();
			String gdfExpiryTime = to.getGdfExpiryTime();
			String job_log = to.getJobLog();
			String job_status = to.getJobStatus();
			String file_size = to.getFileSize();
			String job_id = to.getJobId();
			String request_details = to.getRequestDetails();
			String contractName = to.getContractName();

			logger.log(IAppLogger.INFO, "request_filename: " + request_filename);
			logger.log(IAppLogger.INFO, "gdfExpiryTime: " + gdfExpiryTime);
			logger.log(IAppLogger.INFO, "job_log: " + job_log);
			logger.log(IAppLogger.INFO, "job_status: " + job_status);
			logger.log(IAppLogger.INFO, "file_size: " + file_size);
			logger.log(IAppLogger.INFO, "job_id: " + job_id);
			logger.log(IAppLogger.INFO, "request_details: " + request_details);
			logger.log(IAppLogger.INFO, "contractName: " + contractName);

			String request_summary = getRequestSummary(Utils.objectToJson(to), contractName);
			logger.log(IAppLogger.INFO, "request_summary: " + request_summary);

			updateCount = getJdbcTemplatePrism(contractName).update(CustomStringUtil.replaceCharacterInString('#', gdfExpiryTime, IQueryConstants.UPDATE_JOB_TRACKING), request_filename, request_summary, job_log, job_status, file_size, job_id);
			logger.log(IAppLogger.INFO, "updateCount: " + updateCount);

			logger.log(IAppLogger.INFO, "Exit: updateJobTracking()");
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return updateCount;
	}
	
	public int updateJobTrackingStatus(String contractName, String jobId, String jobStatus, String jobLog) {
		logger.log(IAppLogger.INFO, "Enter: updateJobTrackingStatus()");
		int updateCount = 0;
		try {
			if (jobLog == null || "null".equalsIgnoreCase(jobLog)) {
				jobLog = "Unknown Exception";
			}
			if(contractName == null || "null".equalsIgnoreCase(contractName)){
				contractName = "inors";
			}
			logger.log(IAppLogger.INFO, "jobId: " + jobId);
			logger.log(IAppLogger.INFO, "jobStatus: " + jobStatus);
			logger.log(IAppLogger.INFO, "jobLog: " + jobLog);
			logger.log(IAppLogger.INFO, "contractName: " + contractName);
			updateCount = getJdbcTemplatePrism(contractName).update(IQueryConstants.UPDATE_JOB_TRACKING_STATUS, jobStatus, jobLog, jobId);
			logger.log(IAppLogger.INFO, "updateCount: " + updateCount);

			logger.log(IAppLogger.INFO, "Exit: updateJobTrackingStatus()");
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return updateCount;
	}
	
	@Caching( cacheable = {
			@Cacheable(value = "inorsConfigCache", condition="#contractName == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, 'getProductNameFromId' )"),
			@Cacheable(value = "tascConfigCache",  condition="#contractName == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, 'getProductNameFromId' )"),
			@Cacheable(value = "usmoConfigCache",  condition="#contractName == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, 'getProductNameFromId' )")
	} )
	private String getProductNameFromId(Long productId, String contractName) {
		String productName = "";
		List<Map<String, Object>> lstData = getJdbcTemplatePrism(contractName).queryForList(IQueryConstants.GET_PRODUCT_NAME_BY_ID, productId);
		if (lstData.size() > 0) {
			for (Map<String, Object> fieldDetails : lstData) {
				productName = fieldDetails.get("PRODUCT_NAME").toString();
			}
		}
		return productName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#getGDFilePaths(com.ctb.prism.report.transferobject.GroupDownloadTO)
	 */
	public Map<String, String> getGDFilePaths(GroupDownloadTO to) {
		logger.log(IAppLogger.INFO, "Enter: getGDFilePaths()");
		String contractName = to.getContractName();
		logger.log(IAppLogger.INFO, "contractName: " + contractName);
		Map<String, String> filePaths = new LinkedHashMap<String, String>();
		String students = to.getStudents();
		String groupFile = to.getGroupFile();
		logger.log(IAppLogger.INFO, "students: " + students);
		logger.log(IAppLogger.INFO, "groupFile: " + groupFile);

		String productStr = "";
		try {
			Long productId = Long.parseLong(to.getTestAdministrationVal());
			logger.log(IAppLogger.INFO, "productId: " + productId);
			String productName = getProductNameFromId(productId, contractName);
			if (productName.length() > 4) {
				productStr = productName.substring(0, 5);
			}
		} catch (NumberFormatException e) {
			logger.log(IAppLogger.WARN, "Invalid productId");
		}
		logger.log(IAppLogger.INFO, "productStr: " + productStr);

		if (IApplicationConstants.EXTRACT_FILETYPE.ICL.toString().equals(groupFile)) {
			// Invitation Code Letter
			filePaths = getICLetterPaths(students, productStr, contractName);
		} else if (IApplicationConstants.EXTRACT_FILETYPE.BOTH.toString().equals(groupFile)) {
			// Both (IP and ISR)
			filePaths = getBothPaths(students, productStr, contractName);
		} else if (IApplicationConstants.EXTRACT_FILETYPE.IPR.toString().equals(groupFile)) {
			// Image Prints
			filePaths = getIPPaths(students, productStr, contractName);
		} else if (IApplicationConstants.EXTRACT_FILETYPE.ISR.toString().equals(groupFile)) {
			// Individual Student Report
			filePaths = getISRPaths(students, productStr, contractName);
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
	@Caching( cacheable = {
			@Cacheable(value = "inorsConfigCache", condition="#contractName == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, 'getICLetterPaths' )"),
			@Cacheable(value = "tascConfigCache",  condition="#contractName == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, 'getICLetterPaths' )"),
			@Cacheable(value = "usmoConfigCache",  condition="#contractName == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, 'getICLetterPaths' )")
	} )
	private Map<String, String> getICLetterPaths(String students, String productStr, String contractName) {
		Map<String, String> icPaths = new LinkedHashMap<String, String>();
		String[] studentIds = students.split(",");
		// Loop used to ensure collation hierarchy
		int icPrefixSequence = 0;
		for (String studentId : studentIds) { // TODO : Don't use loop
			String query = IQueryConstants.GET_IC_FILE_PATH;
			query = CustomStringUtil.replaceAll(query, "*", productStr);
			query = CustomStringUtil.replaceAll(query, "?", studentId);
			logger.log(IAppLogger.INFO, "query: " + query);
			List<Map<String, Object>> lstData = getJdbcTemplatePrism(contractName).queryForList(query);
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
	@Caching( cacheable = {
			@Cacheable(value = "inorsConfigCache", condition="#contractName == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, 'getISRPaths' )"),
			@Cacheable(value = "tascConfigCache",  condition="#contractName == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, 'getISRPaths' )"),
			@Cacheable(value = "usmoConfigCache",  condition="#contractName == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, 'getISRPaths' )")
	} )
	private Map<String, String> getISRPaths(String students, String productStr, String contractName) {
		Map<String, String> isrPaths = new LinkedHashMap<String, String>();
		String[] studentIds = students.split(",");
		// Loop used to ensure collation hierarchy
		int isrPrefixSequence = 0;
		for (String studentId : studentIds) { // TODO : Don't use loop
			String query = IQueryConstants.GET_STUDENTS_PDF_FILE_PATH_ISR_ONLY;
			query = CustomStringUtil.replaceAll(query, "*", productStr);
			query = CustomStringUtil.replaceAll(query, "?", studentId);
			logger.log(IAppLogger.INFO, "query: " + query);
			List<Map<String, Object>> lstData = getJdbcTemplatePrism(contractName).queryForList(query);
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
	@Caching( cacheable = {
			@Cacheable(value = "inorsConfigCache", condition="#contractName == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, 'getIPPaths' )"),
			@Cacheable(value = "tascConfigCache",  condition="#contractName == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, 'getIPPaths' )"),
			@Cacheable(value = "usmoConfigCache",  condition="#contractName == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, 'getIPPaths' )")
	} )
	private Map<String, String> getIPPaths(String students, String productStr, String contractName) {
		Map<String, String> iprPaths = new LinkedHashMap<String, String>();
		String[] studentIds = students.split(",");
		// Loop used to ensure collation hierarchy
		int iprPrefixSequence = 0;
		for (String studentId : studentIds) { // TODO : Don't use loop
			String query = IQueryConstants.GET_STUDENTS_PDF_FILE_PATH_IPR_ONLY;
			query = CustomStringUtil.replaceAll(query, "*", productStr);
			query = CustomStringUtil.replaceAll(query, "?", studentId);
			logger.log(IAppLogger.INFO, "query: " + query);
			List<Map<String, Object>> lstData = getJdbcTemplatePrism(contractName).queryForList(query);
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
	@Caching( cacheable = {
			@Cacheable(value = "inorsConfigCache", condition="#contractName == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, 'getBothPaths' )"),
			@Cacheable(value = "tascConfigCache",  condition="#contractName == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, 'getBothPaths' )"),
			@Cacheable(value = "usmoConfigCache",  condition="#contractName == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, 'getBothPaths' )")
	} )
	private Map<String, String> getBothPaths(String students, String productStr, String contractName) {
		Map<String, String> bothPaths = new LinkedHashMap<String, String>();
		String[] studentIds = students.split(",");
		// Loop used to ensure collation hierarchy
		int isrPrefixSequence = 0;
		int iprPrefixSequence = 0;
		for (String studentId : studentIds) { // TODO : Don't use loop
			String query = IQueryConstants.GET_STUDENTS_PDF_FILE_PATH_BOTH;
			query = CustomStringUtil.replaceAll(query, "*", productStr);
			query = CustomStringUtil.replaceAll(query, "?", studentId);
			logger.log(IAppLogger.INFO, "query: " + query);
			List<Map<String, Object>> lstData = getJdbcTemplatePrism(contractName).queryForList(query);
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
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="#contractName == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, 'getResult' )"),
			@Cacheable(value = "tascDefaultCache",  condition="#contractName == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, 'getResult' )"),
			@Cacheable(value = "usmoDefaultCache",  condition="#contractName == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, 'getResult' )")
	} )
	private String getResult(String query, String contractName) {
		logger.log(IAppLogger.DEBUG, "Enter: getResult()");
		logger.log(IAppLogger.DEBUG, query);
		String result = null;
		List<Map<String, Object>> lstData = getJdbcTemplatePrism(contractName).queryForList(query);
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
	 * @param msgtype,reportname and infoname
	 * @return message
	 */
	@Caching( cacheable = {
			@Cacheable(value = "inorsConfigCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getSystemConfigurationMessage') )"),
			@Cacheable(value = "tascConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getSystemConfigurationMessage') )"),
			@Cacheable(value = "usmoConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getSystemConfigurationMessage') )")
	} )
	public String getSystemConfigurationMessage(Map<String, Object> paramMap) {
		logger.log(IAppLogger.INFO, "Enter: getSystemConfigurationMessage()");
		long t1 = System.currentTimeMillis();
		String MESSAGE_NAME = (String) paramMap.get("MESSAGE_NAME");
		String REPORT_ID = (String) paramMap.get("REPORT_ID");
		String MESSAGE_TYPE = (String) paramMap.get("MESSAGE_TYPE");
		logger.log(IAppLogger.INFO, "paramMap="+paramMap);

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
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getReportMessage') )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getReportMessage') )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getReportMessage') )")
	} )
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
	
	@Caching( cacheable = {
			@Cacheable(value = "inorsConfigCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getAllReportMessages') )"),
			@Cacheable(value = "tascConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getAllReportMessages') )"),
			@Cacheable(value = "usmoConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getAllReportMessages') )")
	} )
	public List<ReportMessageTO> getAllReportMessages(Map<String, Object> paramMap) {
		logger.log(IAppLogger.INFO, "Enter: getAllReportMessages()");
		List<ReportMessageTO> reportMessageList = new ArrayList<ReportMessageTO>();
		String reportId = (String) paramMap.get("REPORT_ID");
		if(reportId == null) {
			try {
				reportId = getReportId((String) paramMap.get("REPORT_URL"));
			} catch (Exception ex) {}
		}
		String productId = (String) paramMap.get("PRODUCT_ID");
		String customerId = (String) paramMap.get("CUSTOMER_ID");
		String orgNodeLevel = (String) paramMap.get("ORG_NODE_LEVEL");
		// String userId = (String) paramMap.get("USER_ID");
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
		logger.log(IAppLogger.INFO, "Exit: getAllReportMessages()");
		return reportMessageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#getProcessDataGD(java.lang.String)
	 */
	//@Cacheable(value = {"inorsDefaultCache", "tascDefaultCache"}, key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, 'getProcessDataGD' )")
	public JobTrackingTO getProcessDataGD(String processId, String contractName) {
		logger.log(IAppLogger.INFO, "Enter: getProcessDataGD()");
		JobTrackingTO to = new JobTrackingTO();
		List<Map<String, Object>> lstData = getJdbcTemplatePrism(contractName).queryForList("SELECT * FROM JOB_TRACKING WHERE JOB_ID = ?", processId);
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
				to.setQuerySheetTO(getQuerySheetTO((String) data.get("REQUEST_TYPE"), (String) data.get("EXTRACT_CATEGORY"), (String) data.get("REQUEST_DETAILS")));
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
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, 'getConventionalFileNameGD' )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, 'getConventionalFileNameGD' )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, 'getConventionalFileNameGD' )")
	} )
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
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="#contractName == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, 'getAncestorOrgNodeId' )"),
			@Cacheable(value = "tascDefaultCache",  condition="#contractName == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, 'getAncestorOrgNodeId' )"),
			@Cacheable(value = "usmoDefaultCache",  condition="#contractName == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, 'getAncestorOrgNodeId' )")
	} )
	public String getAncestorOrgNodeId(String orgNodeId, int ancestorLevel, String contractName) {
		String ancestorOrgNodeId = "-1";
		if (orgNodeId == null) {
			return ancestorOrgNodeId;
		}
		String orgNodeIdPath = getOrgNodeIdPath(orgNodeId, contractName);
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
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="#contractName == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, 'getOrgNodeIdPath' )"),
			@Cacheable(value = "tascDefaultCache",  condition="#contractName == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, 'getOrgNodeIdPath' )"),
			@Cacheable(value = "usmoDefaultCache",  condition="#contractName == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, 'getOrgNodeIdPath' )")
	} )
	public String getOrgNodeIdPath(String orgNodeId, String contractName) {
		if (orgNodeId == null) {
			return "0";
		}
		StringBuilder orgNodeIdPath = new StringBuilder();
		orgNodeIdPath.insert(0, orgNodeId);
		String parentOrgNodeId = getParentOrgNodeId(orgNodeId, contractName);
		while ((parentOrgNodeId != null) && (!"0".equals(parentOrgNodeId))) {
			orgNodeIdPath.insert(0, "~");
			orgNodeIdPath.insert(0, parentOrgNodeId);
			parentOrgNodeId = getParentOrgNodeId(parentOrgNodeId, contractName);
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
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="#contractName == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #01, 'getParentOrgNodeId' )"),
			@Cacheable(value = "tascDefaultCache",  condition="#contractName == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #01, 'getParentOrgNodeId' )"),
			@Cacheable(value = "usmoDefaultCache",  condition="#contractName == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #01, 'getParentOrgNodeId' )")
	} )
	public String getParentOrgNodeId(String orgNodeId, String contractName) {
		String parentOrgNodeId = null;
		List<Map<String, Object>> lstData = getJdbcTemplatePrism(contractName).queryForList("SELECT PARENT_ORG_NODEID FROM ORG_NODE_DIM WHERE ORG_NODEID = ?", orgNodeId);
		if (!lstData.isEmpty()) {
			for (Map<String, Object> fieldDetails : lstData) {
				parentOrgNodeId = fieldDetails.get("PARENT_ORG_NODEID").toString();
			}
		}
		return parentOrgNodeId;
	}

	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, 'getCustProdId' )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, 'getCustProdId' )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, 'getCustProdId' )")
	} )
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
	
	//@Cacheable(value = {"inorsDefaultCache", "tascDefaultCache"}, key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, 'getListOfRoles' )")
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key = "T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).Long(#reportId)).concat(#root.method.name) )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key = "T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).Long(#reportId)).concat(#root.method.name) )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key = "T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).Long(#reportId)).concat(#root.method.name) )")
	} )
	private String getListOfRoles(Long reportId) {
		logger.log(IAppLogger.DEBUG, "Enter: getListOfRoles()"); 
		String roles = null;
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_LIST_OF_ROLES,reportId);
		if (!lstData.isEmpty()) {
			for (Map<String, Object> fieldDetails : lstData) {
				roles = fieldDetails.get("ROLES").toString();
			}
		}
		return roles;
	}
	
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0,#p1, 'getCurrentMsgType' )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0,#p1, 'getCurrentMsgType' )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0,#p1, 'getCurrentMsgType' )")
	} )
	private Long getCurrentMsgType (Long currentCustProdId, Long oldMsgTypeId) {
		logger.log(IAppLogger.INFO, "Enter: getCurrentAdminMsgType()"); 
		long msgTypeId = 0;
		
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_CURRENT_MSGTYPE,currentCustProdId,oldMsgTypeId);
		if (!lstData.isEmpty()) {
			for (Map<String, Object> fieldDetails : lstData) {
				msgTypeId =  ((BigDecimal) fieldDetails.get("MSGTYPEID")).longValue() ;
			}
		}
		return msgTypeId;
	}

	/**
	 * @param paramMap
	 * @return
	 */
	public List<ReportActionTO> getProductsForEditActions(Map<String, Object> paramMap) {
		logger.log(IAppLogger.INFO, "Enter: getProductsForEditActions()");
		final String reportId = (String) paramMap.get("reportId");
		final String customerId = (String) paramMap.get("customerId");
		logger.log(IAppLogger.INFO, "reportId = " + reportId);
		logger.log(IAppLogger.INFO, "customerId = " + customerId);

		List<ReportActionTO> reportData = (List<ReportActionTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement cs = null;
				cs = con.prepareCall(IQueryConstants.GET_PRODUCTS_EDIT_ACTIONS);
				cs.setLong(1, Long.parseLong(reportId));
				cs.setLong(2, Long.parseLong(customerId));
				cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
				cs.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR);
				return cs;
			}
		}, new CallableStatementCallback<Object>() {
			public Object doInCallableStatement(CallableStatement cs) {
				List<ReportActionTO> reportResult = new ArrayList<ReportActionTO>();
				ResultSet reportResultSet = null;
				try {
					cs.execute();
					reportResultSet = (ResultSet) cs.getObject(3);
					Utils.logError(cs.getString(4));
					reportResult = parseProductsForEditAction(reportResultSet);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				logger.log(IAppLogger.INFO, "reportResult.size() = " + reportResult.size());
				return reportResult;
			}

			/**
			 * Report Data. It is a Single TO.
			 * 
			 * @param reportResultSet
			 * @return
			 * @throws SQLException 
			 */
			private List<ReportActionTO> parseProductsForEditAction(ResultSet reportResultSet) throws SQLException {
				List<ReportActionTO> productList = new ArrayList<ReportActionTO>();
				while(reportResultSet.next()){
					ReportActionTO to = new ReportActionTO();
					to.setReportId(reportResultSet.getString("DB_REPORTID"));
					to.setReportName(reportResultSet.getString("REPORT_NAME"));
					to.setCustProdId(reportResultSet.getString("CUST_PROD_ID"));
					to.setProductName(reportResultSet.getString("PRODUCT_NAME"));
					productList.add(to);
				}
				logger.log(IAppLogger.INFO, "productList.size() = " + productList.size());
				return productList;
			}
		});
		logger.log(IAppLogger.INFO, "Exit: getProductsForEditActions()");
		return reportData;
	}
	
	/* (non-Javadoc)
	 * @see com.ctb.prism.report.dao.IReportDAO#getActionsForEditActions(java.util.Map)
	 */
	public List<ReportActionTO> getActionsForEditActions(Map<String, Object> paramMap) {
		logger.log(IAppLogger.INFO, "Enter: getActionsForEditActions()");
		final String reportId = (String) paramMap.get("reportId");
		final String custProdId = (String) paramMap.get("custProdId");
		logger.log(IAppLogger.INFO, "reportId = " + reportId);
		logger.log(IAppLogger.INFO, "custProdId = " + custProdId);

		@SuppressWarnings("unchecked")
		List<ReportActionTO> editDataList = (List<ReportActionTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement cs = null;
				cs = con.prepareCall(IQueryConstants.GET_ACTIONS_EDIT_ACTIONS);
				cs.setLong(1, Long.parseLong(reportId));
				cs.setLong(2, Long.parseLong(custProdId));
				cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
				cs.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR);
				return cs;
			}
		}, new CallableStatementCallback<Object>() {
			public Object doInCallableStatement(CallableStatement cs) {
				ResultSet actionResultSet = null;
				List<ReportActionTO> actionResult = null;
				try {
					cs.execute();
					actionResultSet = (ResultSet) cs.getObject(3);
					Utils.logError(cs.getString(4));
					actionResult = parseActionDataForEditAction(actionResultSet);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				logger.log(IAppLogger.INFO, "actionResult.size() = " + actionResult.size());
				return actionResult;
			}

			/**
			 * Action Data. It is a Collection of TO.
			 * 
			 * @param reportResultSet
			 * @return
			 * @throws SQLException 
			 */
			private List<ReportActionTO> parseActionDataForEditAction(ResultSet actionResultSet) throws SQLException {
				List<ReportActionTO> actionList = new ArrayList<ReportActionTO>();
				while(actionResultSet.next()){
					ReportActionTO to = new ReportActionTO();
					to.setActionId(actionResultSet.getString("DB_ACTIONID"));
					to.setActionName(actionResultSet.getString("ACTION_NAME"));
					actionList.add(to);
				}
				logger.log(IAppLogger.INFO, "actionList.size() = " + actionList.size());
				return actionList;
			}
		});
		logger.log(IAppLogger.INFO, "editDataList.size() = " + editDataList.size());
		logger.log(IAppLogger.INFO, "Exit: getActionsForEditActions()");
		return editDataList;
	}
	
	/* (non-Javadoc)
	 * @see com.ctb.prism.report.dao.IReportDAO#getActionAccess(java.util.Map)
	 */
	public List<ReportActionTO> getActionAccess(Map<String, Object> paramMap) {
		logger.log(IAppLogger.INFO, "Enter: getActionAccess()");
		final String reportId = (String) paramMap.get("reportId");
		final String custProdId = (String) paramMap.get("custProdId");
		final String actionId = (String) paramMap.get("actionId");
		logger.log(IAppLogger.INFO, "reportId = " + reportId);
		logger.log(IAppLogger.INFO, "custProdId = " + custProdId);
		logger.log(IAppLogger.INFO, "actionId = " + actionId);

		@SuppressWarnings("unchecked")
		List<ReportActionTO> editDataList = (List<ReportActionTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement cs = null;
				cs = con.prepareCall(IQueryConstants.GET_ACTION_ACCESS);
				cs.setLong(1, Long.parseLong(reportId));
				cs.setLong(2, Long.parseLong(custProdId));
				cs.setLong(3, Long.parseLong(actionId));
				cs.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);
				cs.registerOutParameter(5, oracle.jdbc.OracleTypes.VARCHAR);
				return cs;
			}
		}, new CallableStatementCallback<Object>() {
			public Object doInCallableStatement(CallableStatement cs) {
				ResultSet actionResultSet = null;
				List<ReportActionTO> actionAccessResult = null;
				try {
					cs.execute();
					actionResultSet = (ResultSet) cs.getObject(4);
					Utils.logError(cs.getString(5));
					actionAccessResult = parseActionAccessDataForEditAction(actionResultSet);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				logger.log(IAppLogger.INFO, "actionAccessResult.size() = " + actionAccessResult.size());
				return actionAccessResult;
			}

			/**
			 * Action Data. It is a Collection of TO.
			 * 
			 * @param reportResultSet
			 * @return
			 * @throws SQLException 
			 */
			private List<ReportActionTO> parseActionAccessDataForEditAction(ResultSet actionResultSet) throws SQLException {
				List<ReportActionTO> actionAccessList = new ArrayList<ReportActionTO>();
				while(actionResultSet.next()){
					ReportActionTO to = new ReportActionTO();
					to.setRoleId(actionResultSet.getString("ROLEID"));
					to.setRoleName(actionResultSet.getString("ROLE_NAME"));
					to.setLevelId(actionResultSet.getString("ORG_LEVEL"));
					to.setLevelName(actionResultSet.getString("ORG_LABEL"));
					to.setActivationStatus(actionResultSet.getString("ACTIVATION_STATUS"));
					actionAccessList.add(to);
				}
				logger.log(IAppLogger.INFO, "actionAccessList.size() = " + actionAccessList.size());
				return actionAccessList;
			}
		});
		logger.log(IAppLogger.INFO, "editDataList.size() = " + editDataList.size());
		logger.log(IAppLogger.INFO, "Exit: getActionAccess()");
		return editDataList;
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.report.dao.IReportDAO#updateDataForActions(java.util.Map)
	 */
	@Caching( evict = { 
			@CacheEvict(value = "inorsConfigCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", allEntries = true),
			@CacheEvict(value = "tascConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  allEntries = true),
			@CacheEvict(value = "usmoConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  allEntries = true)
	} )
	public String updateDataForActions(Map<String, Object> paramMap) {
		logger.log(IAppLogger.INFO, "Enter: updateDataForActions()");
		final String reportId = (String) paramMap.get("reportId");
		final String custProdId = (String) paramMap.get("custProdId");
		final String actionId = (String) paramMap.get("actionId");
		final String roleIdLevelId = (String) paramMap.get("roleIdLevelId");
		
		logger.log(IAppLogger.INFO, "reportId = " + reportId);
		logger.log(IAppLogger.INFO, "custProdId = " + custProdId);
		logger.log(IAppLogger.INFO, "actionId = " + actionId);
		logger.log(IAppLogger.INFO, "roleIdLevelId = " + roleIdLevelId);

		String dbException = (String) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement cs = null;
				cs = con.prepareCall(IQueryConstants.SP_UPDATE_ACTION_DATA);
				cs.setLong(1, Long.parseLong(reportId));
				cs.setLong(2, Long.parseLong(custProdId));
				cs.setLong(3, Long.parseLong(actionId));
				cs.setString(4, roleIdLevelId);
				cs.registerOutParameter(5, oracle.jdbc.OracleTypes.VARCHAR);
				return cs;
			}
		}, new CallableStatementCallback<Object>() {
			public Object doInCallableStatement(CallableStatement cs) {
				String query = null;
				try {
					cs.execute();
					query = cs.getString(5);
					Utils.logError(cs.getString(5));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return query;
			}
		});
		if (dbException != null) {
			logger.log(IAppLogger.INFO, "dbException = " + dbException);
		}
		logger.log(IAppLogger.INFO, "Exit: updateDataForActions()");
		return dbException;
	}

	public void updateJobTrackingTable(String jobId, String filePath) {
		getJdbcTemplatePrism().update(IReportQuery.UPDATE_FILENAME_JOB_TRACKING, filePath, jobId);
	}
	
	/**
	 * Get list of students and formid
	 * This method is for bulk candidate report download
	 */
	public List<ObjectValueTO> getStudentList(String query) {
		if(query == null) return null;
		logger.log(IAppLogger.INFO, query);
		List<ObjectValueTO> list = getJdbcTemplatePrism().query(query, new RowMapper<ObjectValueTO>() {
			public ObjectValueTO mapRow(ResultSet rs, int col) throws SQLException {
				ObjectValueTO to = new ObjectValueTO();
				to.setName(rs.getString(2));
				to.setValue(rs.getString(3));
				to.setClikedOrgId(rs.getLong(10)); // form id
				return to;
			}
		});
		return list;
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.report.dao.IReportDAO#getGenericSystemConfigurationMessages(java.util.Map)
	 */
	// @Cacheable(value = "configCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getGenericSystemConfigurationMessages') )")
	public Map<String, String> getGenericSystemConfigurationMessages(Map<String, Object> paramMap) {
		logger.log(IAppLogger.INFO, "Enter: getGenericSystemConfigurationMessages()");
		final String messageNames = (String) paramMap.get("messageNames");
		logger.log(IAppLogger.INFO, "messageNames = " + messageNames);
		@SuppressWarnings("unchecked")
		Map<String, String> genericSystemConfigurationMessages = (Map<String, String>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement cs = null;
				cs = con.prepareCall(IQueryConstants.GET_GENERIC_SYSTEM_CONFIGURATION_MESSAGES);
				cs.setString(1, messageNames);
				cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
				cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
				return cs;
			}
		}, new CallableStatementCallback<Object>() {
			public Object doInCallableStatement(CallableStatement cs) {
				Map<String, String> messageResult = new HashMap<String, String>();
				ResultSet messageResultSet = null;
				try {
					cs.execute();
					messageResultSet = (ResultSet) cs.getObject(2);
					Utils.logError(cs.getString(3));
					while (messageResultSet.next()) {
						messageResult.put(messageResultSet.getString("MESSAGE_NAME"), messageResultSet.getString("REPORT_MSG"));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				logger.log(IAppLogger.INFO, "messageResult.size() = " + messageResult.size());
				return messageResult;
			}
		});
		logger.log(IAppLogger.INFO, "Exit: getGenericSystemConfigurationMessages()");
		return genericSystemConfigurationMessages;
	}

}
