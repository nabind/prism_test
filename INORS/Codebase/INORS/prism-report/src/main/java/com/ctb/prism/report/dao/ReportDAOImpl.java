package com.ctb.prism.report.dao;

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
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.OracleLobHandler;
import org.springframework.stereotype.Repository;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.constant.IApplicationConstants.ROLE_TYPE;
import com.ctb.prism.core.constant.IQueryConstants;
import com.ctb.prism.core.constant.IReportQuery;
import com.ctb.prism.core.dao.BaseDAO;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.core.transferobject.ObjectValueTOMapper;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.report.transferobject.AssessmentTO;
import com.ctb.prism.report.transferobject.InputControlTO;
import com.ctb.prism.report.transferobject.JobTrackingTO;
import com.ctb.prism.report.transferobject.ManageMessageTO;
import com.ctb.prism.report.transferobject.ManageMessageTOMapper;
import com.ctb.prism.report.transferobject.ObjectValueTO;
import com.ctb.prism.report.transferobject.ReportParameterTO;
import com.ctb.prism.report.transferobject.ReportTO;
import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;

/**
 * This class is responsible for reading and writing to database.
 * The transactions through this class should be related to report only.
 */
@Repository("reportDAO")
public class ReportDAOImpl extends BaseDAO implements IReportDAO {
	
	/**
	 * This method returns report print object filled with data
	 * @param jasperReport
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	@Cacheable(cacheName = "filledJasperPrint")
	public JasperPrint getFilledReport(JasperReport jasperReport, Map<String, Object> parameters) throws Exception {
		Connection conn = null;
		logger.info( CustomStringUtil.appendString("####----------------------------JASPER--PRINT-----------------------------", 
				jasperReport.getName()));
		try {
			conn = getPrismConnection();
			return JasperFillManager.fillReport(jasperReport, parameters, conn);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			if(conn != null) try {conn.close();} catch (SQLException e) {}
		}
	}
	/**
	 *	Same report - without putting the same in cache 
	 */
	public JasperPrint getFilledReportNoCache(JasperReport jasperReport, Map<String, Object> parameters) throws Exception {
		Connection conn = null;
		logger.info( CustomStringUtil.appendString("####----------------------------JASPER--PRINT-----------------------------", 
				jasperReport.getName()));
		try {
			conn = getPrismConnection();
			return JasperFillManager.fillReport(jasperReport, parameters, conn);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			if(conn != null) try {conn.close();} catch (SQLException e) {}
		}
	}
	
	/**
	 * Returns {@link JasperReport} object of a particular report by compiling the JRXML file retrieved from database
	 */
	
	public JasperReport getReportJasperObject(final String reportPath) {
		logger.info( "Enter: ReportDAOImpl - getReportJasperObject");
		
		Object[] param = new String[]{ reportPath };
		return getJdbcTemplate().queryForObject(IQueryConstants.GET_JASPER_REPORT_OBJECT, param, new RowMapper<JasperReport>() {
			public JasperReport mapRow(ResultSet rs, int col) throws SQLException {
				JasperReport jasperReport = null;
				try {
					jasperReport = JasperCompileManager.compileReport(rs.getBinaryStream(1));
				} catch (JRException e) {
					logger.warn("Could not compile report jrxml retrieved from database for report "+ reportPath, e);
				}
				logger.info("Exit: ReportDAOImpl - getReportJasperObject");
				return jasperReport;
			}
		});
	}
	
	public JasperReport getReportJasperObjectForName(final String reportname) {
		logger.info( "Enter: ReportDAOImpl - getReportJasperObject");
		
		Object[] param = new String[]{ reportname };
		return getJdbcTemplate().queryForObject(IQueryConstants.GET_JASPER_REPORT_OBJECT_FOR_NAME, param, new RowMapper<JasperReport>() {
			public JasperReport mapRow(ResultSet rs, int col) throws SQLException {
				JasperReport jasperReport = null;
				try {
					jasperReport = JasperCompileManager.compileReport(rs.getBinaryStream(1));
				} catch (JRException e) {
					logger.warn("Could not compile report jrxml retrieved from database for report "+ reportname, e);
				}
				logger.info("Exit: ReportDAOImpl - getReportJasperObject");
				return jasperReport;
			}
		});
	}
	
	@TriggersRemove(cacheName="compiledJrxml", removeAll=true)
	public void removeReportCache() {
		logger.info( "Removed report cache");
	}
	
	@TriggersRemove(cacheName={"msgCache", "compiledJrxml", "reportInputControls", "defaultInputControls",
			"tenantId", "orgChildren", "securityQuestions", "orgTreeChildren", "orgUsers", "allAdminYear", "filledJasperPrint"}, removeAll=true)
	public void removeCache() {
		logger.info( "Removed all cache");
	}
	/**
	 * This method is to fetch all reports (including subreports)
	 * @param reportPath - report URL
	 * @return List of ReportTO
	 */
	@Cacheable(cacheName = "compiledJrxml")
	public  List<ReportTO> getReportJasperObjectList(final String reportPath) throws JRException {
		logger.info( "Enter: ReportDAOImpl - getReportJasperObject");
		
		Object[] param = new String[]{ reportPath };
		return getJdbcTemplate().query(IQueryConstants.GET_JASPER_REPORT_OBJECT, param, 
				new RowMapper<ReportTO>() {
					public ReportTO mapRow(ResultSet rs, int col) throws SQLException {
						ReportTO reportTo = null;
						JasperReport jasperReport = null;
						reportTo = new ReportTO();
						try {
							String objectType = rs.getString(1);
							if("jrxml".equals(objectType)) {
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
							
							if("Main jrxml".equalsIgnoreCase(reportLabel)) {
								reportTo.setMainReport(true);
							} else {
								reportTo.setMainReport(false);
							}
						} catch (JRException e) {
							try {
								throw new JRException(e.getMessage());
							} catch (JRException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							logger.warn("Could not compile report jrxml retrieved from database for report "+ reportPath, e);
						}
						logger.info("Exit: ReportDAOImpl - getReportJasperObject");
						return reportTo;
					}
				});
	}
	
	/*public List<ReportTO> getReportJasperObjectList(final String reportPath) throws JRException {
		logger.info( "Enter: ReportDAOImpl - getReportJasperObject");
		List<ReportTO> reportList = null;
		Object[] param = new String[]{ reportPath };
		List<Map<String, Object>> lstData = getJdbcTemplate().queryForList(IQueryConstants.GET_JASPER_REPORT_OBJECT, param);
		if ( lstData.size() > 0 ) {
			reportList = new ArrayList<ReportTO>();
			JasperReport jasperReport = null;
			for (Map<String, Object> fieldDetails : lstData) {
				Blob data = (Blob) fieldDetails.get("DATA");
				java.io.InputStream in = null;
				try {
					in = data.getBinaryStream();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if(in != null) jasperReport = JasperCompileManager.compileReport(in);
				String reportLabel = (String) fieldDetails.get("LABEL");
				ReportTO reportTo = new ReportTO();
				reportTo.setCompiledReport(jasperReport);
				if("Main jrxml".equalsIgnoreCase(reportLabel)) {
					reportTo.setMainReport(true);
				} else {
					reportTo.setMainReport(false);
				}
				reportList.add(reportTo);
			}
		}
		return reportList;
	}*/

	/**
	 * Returns the input control details of a particular report
	 */
	@Cacheable(cacheName = "reportInputControls")
	public List<InputControlTO> getInputControlDetails(String reportPath){
		logger.info( "Enter: ReportDAOImpl - getInputControlDetails");
		
		List<InputControlTO> inputControlTOs = null;
		Object[] param = new String[]{ reportPath };
		int[] types = new int[]{Types.VARCHAR};	
		List<Map<String, Object>> lstData = getJdbcTemplate().queryForList(IQueryConstants.GET_INPUT_CONTROL_DETAILS, param, types);
		if ( lstData.size() > 0 )
		{
			inputControlTOs = new ArrayList<InputControlTO>();
			for (Map<String, Object> fieldDetails : lstData) {
				InputControlTO to = new InputControlTO();
				to.setControlId( ((BigDecimal) fieldDetails.get("CONTROLID")).longValue());
				to.setDatasource((BigDecimal) fieldDetails.get("DATASOURCE") != null ? 
						((BigDecimal) fieldDetails.get("DATASOURCE")).toString() : null);
				to.setDataType((BigDecimal) fieldDetails.get("DATATYPE") != null ? 
						((BigDecimal) fieldDetails.get("DATATYPE")).toString() : null);
				to.setLabel((String) fieldDetails.get("LBL"));
				to.setLabelId((String) fieldDetails.get("LABELID"));
				to.setMandatory(((BigDecimal)fieldDetails.get("MANDATORY")).intValue()==1 ? true : false);
				to.setQueryValueColumn((String) fieldDetails.get("QUERY_VALUE_COLUMN"));
				to.setReadonly(((BigDecimal)fieldDetails.get("READ_ONLY")).intValue()==1 ? true : false);
				to.setSequence(((BigDecimal) fieldDetails.get("SEQ")).intValue());
				to.setType(((BigDecimal) fieldDetails.get("TYPE")).toString());
				to.setVisible(((BigDecimal)fieldDetails.get("VISIBLE")).intValue()==1 ? true : false);
				
				String strListOfValues = (String) fieldDetails.get("LIST_OF_VALUES");
				if ( strListOfValues != null)
				{
					String[] values = strListOfValues.split(",");
					to.setListOfValues(Arrays.asList(values));
				}
				else
				{
					to.setQuery((String) fieldDetails.get("SQL_QUERY"));
				}
				to.setFieldType((fieldDetails.get("FIELD_TYPE") != null)?((BigDecimal) fieldDetails.get("FIELD_TYPE")).toString():null);
				to.setMinLength((fieldDetails.get("MIN_LENGTH") != null)?fieldDetails.get("MIN_LENGTH").toString():null);
				to.setMaxLength((fieldDetails.get("MAX_LENGTH") != null)?fieldDetails.get("MAX_LENGTH").toString():null);
				inputControlTOs.add(to);
			}
		}
		
		logger.info( "Exit: ReportDAOImpl - getInputControlDetails");
		return inputControlTOs;
	}
	
	public List<InputControlTO> getAllInputControls() {
		logger.info( "Enter: ReportDAOImpl - getAllInputControls");
		
		List<InputControlTO> inputControlTOs = null;
		//Object[] param = new String[]{ reportPath };
		//int[] types = new int[]{Types.VARCHAR};	
		List<Map<String, Object>> lstData = getJdbcTemplate().queryForList(IQueryConstants.GET_ALL_INPUT_CONTROLS);
		if ( lstData.size() > 0 )
		{
			inputControlTOs = new ArrayList<InputControlTO>();
			for (Map<String, Object> fieldDetails : lstData) {
				InputControlTO to = new InputControlTO();
				to.setLabelId((String) fieldDetails.get("LABELID"));
				inputControlTOs.add(to);
			}
		}
		logger.info( "Exit: ReportDAOImpl - getAllInputControls");
		return inputControlTOs;
	}
	
	/**
	 * Fetch all values for a single input control
	 * @param ito
	 * @return
	 */
	@Cacheable(cacheName = "defaultInputControls")
	public List<ObjectValueTO> getValuesOfSingleInput(String query) {
		if(query == null) return null;
		logger.info(query);
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
	 * Returns E if the username is an Education Center User.
	 * Returns O if the username is an Org User
	 * Else returns null
	 * 
	 * @param username
	 * @return
	 */
	private String getUserType(String username) {
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_USER_TYPE, username, username);
		if (!lstData.isEmpty()) {
			for (Map<String, Object> fieldDetails : lstData) {
				return fieldDetails.get("USER_TYPE").toString();
			}
		}
		return null;
	}
	
	/**
	 * Retrieves and returns tenantId corresponding to the userName
	 * @param userName
	 * @return tenantId
	 */
	@Cacheable(cacheName = "tenantId")
	public String getTenantId( String userName )
	{	
		String userType = getUserType(userName);
		logger.info( "Enter: ReportDAOImpl - userType = " + userType);
		
		if (IApplicationConstants.EDU_USER_FLAG.equals(userType)) {
			return getJdbcTemplatePrism().queryForObject(IQueryConstants.GET_EDU_TENANT_ID, new Object[]{ /*userParameter[0]*/userName }, new RowMapper<String>() {
				public String mapRow(ResultSet rs, int col) throws SQLException {
					return ((BigDecimal) rs.getObject(1)).toString();
				}
			});
		} else {
			return getJdbcTemplatePrism().queryForObject(IQueryConstants.GET_TENANT_ID, new Object[]{ userName }, new RowMapper<String>() {
				public String mapRow(ResultSet rs, int col) throws SQLException {
					return ((BigDecimal) rs.getObject(1)).toString();
				}
			});
		}
	}
	
	/**
	 * Returns the information( report id, report name, report url, the user roles who can access the report etc) of all reports
	 * @Cacheable(cacheName = "allReports") 
	 */
	//@Cacheable(cacheName = "allReports")
	public List<ReportTO> getAllReportList(Map<String,Object> paramMap)
	{
		logger.info( "Enter: ReportDAOImpl - getAllReportList");
		
		List<ReportTO> reports = null;
		List<Map<String,Object>> dataList=null;
		if(paramMap.get("editReport")!=null && paramMap.get("editReport").equals("editReport"))
				{
					 dataList = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_DASHBOARD_DETAILS,paramMap.get("reportId"),paramMap.get("reportId"),IApplicationConstants.DEFAULT_LEVELID_VALUE);
				}
		else    {
			         dataList = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ALL_REPORT_LIST,IApplicationConstants.DEFAULT_LEVELID_VALUE);
		        }
		if ( dataList != null && dataList.size() > 0 )
		{
			reports = new ArrayList<ReportTO>();
			for (Map<String, Object> data : dataList)
			{
				ReportTO to = new ReportTO();
				to.setReportId(((BigDecimal) data.get("ID")).longValue());
				to.setReportName((String) data.get("REPORT_NAME"));
				to.setReportDescription((String) data.get("REPORT_DESC"));
				to.setReportUrl((String) data.get("REPORT_FOLDER_URI"));
				to.setReportOriginalUrl((String) data.get("REPORT_FOLDER_URI"));
				to.setReportType((String) data.get("REPORT_TYPE"));
				to.setEnabled(((String)data.get("STATUS")).equals(IApplicationConstants.ACTIVE_FLAG) ? true : false);
				to.setAllOrgNode((String) data.get("ORG_NODE_LEVEL"));
				to.setLinkName(((BigDecimal) data.get("CUST_PROD_ID")).longValue());
				to.setProducttName((String) data.get("product_name"));
				to.setProductId(((BigDecimal) data.get("productid")).longValue());
				
				String strRoles = (String) data.get("ROLES");
				if ( strRoles != null && strRoles.length() > 0)
				{
					String[] roles = strRoles.split(",");
					for (String role : roles)
					{
						ROLE_TYPE role_type = Utils.getRoles(role);
						if ( role_type != null)
						{
							to.addRole(role_type);
						}
					}
				}
				String strOrgLevl = (String) data.get("ORG_NODE_LEVEL");
				if ( strOrgLevl != null && strOrgLevl.length() > 0)
				{
					String[] orgLevel = strOrgLevl.split(",");
					to.setOrgNodeLevelArr(orgLevel);
				}
				to.setAssessmentName((String) data.get("ASSESSMENT_NAME"));
				to.setMenuId(((BigDecimal) data.get("MENUID")).toString());
				to.setMenuName((String) data.get("MENUNAME"));
				reports.add(to);
			}
		}
		logger.info( "Exit: ReportDAOImpl - getAllReportList");
		return reports;
	}
	
	/**
	 * Updates the particular report information in database
	 * @param reportId
	 * @param reportName
	 * @param reportUrl
	 * @param isEnabled 1 if enabled, 0 otherwise
	 * @param roles The user roles who can access this particular report.
	 * 
	 * @CacheEvict(value = "allReports", allEntries=true)
	 */
	@TriggersRemove(cacheName="allReports", removeAll=true)
	public boolean updateReport(String reportId, String reportName, String reportUrl, String isEnabled, String[] roles) {
		logger.info("Enter: ReportDAOImpl - updateReport");
		try {
			// update report table
			getJdbcTemplatePrism().update(IQueryConstants.UPDATE_REPORT, reportName, reportUrl, isEnabled, reportId);
			// delete from report_role table
			getJdbcTemplatePrism().update(IQueryConstants.DELETE_REPORT_ROLE, reportId);
			
			if(roles != null) {
				for (String role : roles) {
					if (Utils.isValidRoles(role)) {
						// insert into report_role table						
						getJdbcTemplatePrism().update(IQueryConstants.INSERT_REPORT_ROLE, reportId, role, IApplicationConstants.ACTIVE_FLAG);
					}
				}
				//if(strRole.length() >= 3) strRole = strRole.substring(0, strRole.length() - 3);
			}
		} catch (Exception e) {
			logger.error("Error occurred while updating report details.", e);
			return false;
		}
		logger.info("Exit: ReportDAOImpl - updateReport");
		return true;
	}
	
	/**
	 * Retrieves assessments details from database.
	 * @return List of all available assessments {@link AssessmentTO} along with corresponding report details {@link ReportTO}
	 */
	public List<AssessmentTO> getAssessments(boolean parentReports)
	{
		logger.info( "Enter: ReportDAOImpl - getAssessments");
		
		List<AssessmentTO> assessments = null;
		
		List<Map<String,Object>> dataList = null;
		if(parentReports) {
			dataList= getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ALL_ASSESSMENT_LIST, "PN%");
		} else {
			dataList= getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ALL_ASSESSMENT_LIST, "API%");
		}
		if ( dataList != null && dataList.size() > 0 ) {
			assessments = new ArrayList<AssessmentTO>();
			long oldAssessmentId = -1;
			AssessmentTO assessmentTO = null;
			
			for (Map<String, Object> data : dataList) {
				long assessmentId = ((BigDecimal)data.get("MENU_ID")).longValue();
				if ( oldAssessmentId != assessmentId ) {
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
				String strRoles = (String) data.get("ROLES");
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
				reportTO.setOrgLevel((((BigDecimal) data.get("ORGLEVEL")) != null)? 
						((BigDecimal) data.get("ORGLEVEL")).toString() : "");
				assessmentTO.addReport(reportTO);
			}
		}
		
		logger.info( "Exit: ReportDAOImpl - getAssessments");
		return assessments;
	}
	
	
	public boolean deleteReport(String reportId) throws SystemException {
		logger.info("Enter: ReportDAOImpl - deleteReport");
		try {
			// delete from report_role table
			getJdbcTemplatePrism().update(IQueryConstants.DELETE_REPORT_ROLE, reportId);
			
			// delete from report table
			getJdbcTemplatePrism().update(IQueryConstants.DELETE_REPORT, reportId);
		}catch (Exception e) {
			logger.error("Error occurred while updating report details.", e);
			return false;
		}
		logger.info("Exit: ReportDAOImpl - deleteReport");
		return true;
	}
		
	/**
	 * Returns the reportTO on add.
	 * 
	 * @param nodeid
	 * @return
	 */
	private ReportTO getDashboardData(String reportid) {
				
		ReportTO to = null;
		List<Map<String,Object>> dataList = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_DASHBOARD_DETAILS,reportid,reportid,IApplicationConstants.DEFAULT_LEVELID_VALUE);
		if ( dataList != null && dataList.size() > 0 )
		{
			to = new ReportTO();
			for (Map<String, Object> data : dataList)
			{
				
				to.setReportId(((BigDecimal) data.get("ID")).longValue());
				to.setReportName((String) data.get("REPORT_NAME"));
				to.setReportDescription((String) data.get("REPORT_DESC"));
				to.setReportUrl((String) data.get("REPORT_FOLDER_URI"));
				to.setReportType((String) data.get("REPORT_TYPE"));
				to.setEnabled(((String)data.get("STATUS")).equals(IApplicationConstants.ACTIVE_FLAG) ? true : false);
				to.setAllOrgNode((String) data.get("ORG_NODE_LEVEL"));
				to.setLinkName(((BigDecimal) data.get("CUST_PROD_ID")).longValue());
				to.setProducttName((String) data.get("product_name"));
				to.setProductId(((BigDecimal) data.get("productid")).longValue());
				to.setMenuId(String.valueOf(data.get("MENUID")));
				to.setMenuName((String) data.get("MENUNAME"));
				String strRoles = (String) data.get("ROLES");
				if ( strRoles != null && strRoles.length() > 0)
				{
					String[] roles = strRoles.split(",");
					for (String role : roles)
					{
						ROLE_TYPE role_type = Utils.getRoles(role);
						if ( role_type != null)
						{
							to.addRole(role_type);
						}
					}
				}
			}
		}
	
		return to;
		
	}
	
	@Cacheable(cacheName = "roleCache")
	public List<ObjectValueTO> getAllRoles() {
		List<ObjectValueTO> roles = null;
		List<Map<String,Object>> dataList = getJdbcTemplatePrism().queryForList(IQueryConstants.ALL_ROLE);
		if ( dataList != null && dataList.size() > 0 ) {
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
		List<Map<String,Object>> dataList = getJdbcTemplatePrism().queryForList(IQueryConstants.ALL_ASSESSMENTS);
		if ( dataList != null && dataList.size() > 0 ) {
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
	 * @param reportIdentifier - report id OR report URL
	 * @return
	 */
	public ReportTO getReport(String reportIdentifier) {
		ReportTO to = new ReportTO();
		List<Map<String,Object>> dataList = getJdbcTemplatePrism().queryForList(
				IQueryConstants.REPORT_TYPE, reportIdentifier, reportIdentifier);
		if ( dataList != null && dataList.size() > 0 ) {
			for (Map<String, Object> data : dataList) {
				to.setReportType((String) data.get("TYPE"));
				to.setReportId(((BigDecimal) data.get("ID")).longValue());
				to.setReportUrl((String) data.get("URI"));
				break;
			}
		}
		return to;
	}
	
	
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getAdminYear(final Map<String,Object> paramMap)
			throws SystemException {
		logger.info("Enter: ReportDAOImpl - getAdminYear()");
		List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOList = null;
		try{
			objectValueTOList = getJdbcTemplatePrism().query(IQueryConstants.ALL_ADMIN_YEAR,new ObjectValueTOMapper());
		}catch(Exception e){
			logger.error("Error occurred in getAdminYear():", e);
			throw new SystemException(e);
		}
		logger.info("Exit: ReportDAOImpl - getAdminYear()");
		return objectValueTOList;
	}
	
	@Cacheable(cacheName = "customerProductCache")
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getCustomerProduct(final Map<String,Object> paramMap)
			throws SystemException {
		logger.info("Enter: ReportDAOImpl - getCustomerProduct()");
		List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOList = null;
		UserTO loggedinUserTO = (UserTO) paramMap.get("loggedinUserTO");
		List placeHolderValueList = new ArrayList();
		placeHolderValueList.add(loggedinUserTO.getCustomerId());
		try{
			
			objectValueTOList = getJdbcTemplatePrism().query(IQueryConstants.CUST_PROD,placeHolderValueList.toArray(),
					new ObjectValueTOMapper());
		}catch(Exception e){
			logger.error("Error occurred in getCustomerProduct():", e);
			throw new SystemException(e);
		}
		logger.info("Exit: ReportDAOImpl - getCustomerProduct()");
		return objectValueTOList;
	}
	
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getOrgNodeLevel(final Map<String,Object> paramMap)
			throws SystemException {
		logger.info("Enter: ReportDAOImpl - getOrgNodeLevel()");
		List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOList = null;
		try{
			objectValueTOList = getJdbcTemplatePrism().query(IQueryConstants.ALL_ORG_NODE_LEVEL,new ObjectValueTOMapper());
		}catch(Exception e){
			logger.error("Error occurred in getOrgNodeLevel():", e);
			throw new SystemException(e);
		}
		logger.info("Exit: ReportDAOImpl - getOrgNodeLevel()");
		return objectValueTOList;
	}

	@SuppressWarnings("unchecked")
	public List<ManageMessageTO> loadManageMessage(final Map<String,Object> paramMap)
			throws SystemException {
		
		long reportId = ((Long) paramMap.get("reportId")).longValue();
		long custProdId = ((Long) paramMap.get("custProdId")).longValue();
		String reportName=((String) paramMap.get("reportName"));

		@SuppressWarnings("rawtypes")
		List placeHolderValueList = new ArrayList();
		placeHolderValueList.add(reportId);
		placeHolderValueList.add(custProdId);
		placeHolderValueList.add(reportId);
		placeHolderValueList.add(custProdId);
		
		List<ManageMessageTO> manageMessageTOList = null;
		try{
			if(null!=reportName && reportName.startsWith("System Configuration"))
			{
				manageMessageTOList = getJdbcTemplatePrism().query(IQueryConstants.GET_MANAGE_MESSAGE_LIST_SCM, 
						placeHolderValueList.toArray(),new ManageMessageTOMapper());
			}
			else
			{
			manageMessageTOList = getJdbcTemplatePrism().query(IQueryConstants.GET_MANAGE_MESSAGE_LIST, 
					placeHolderValueList.toArray(),new ManageMessageTOMapper());
			}

		}catch(Exception e){
			logger.error("Error occurred in loadManageMessage():", e);
			return null;
		}
		return manageMessageTOList;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public int saveManageMessage(final List<ManageMessageTO> manageMessageTOList)
			throws SystemException {
		logger.info("Enter: ReportDAOImpl - saveManageMessage()");
		int insertDMFlag = 1;
		int successFlag = 0;
		
		try{
			//Delete DASH_MESSAGES - Start
			//Delete all records from DASH_MESSAGES depending upon Search Criteria - No dependency for deleted record count
			List placeHolderValueList = new ArrayList();
			placeHolderValueList.add(manageMessageTOList.get(0).getReportId());
			placeHolderValueList.add(manageMessageTOList.get(0).getCustProdIdHidden());
			int deleteCountDbMessage = getJdbcTemplatePrism().update(IQueryConstants.DELETE_DASH_MESSAGE, placeHolderValueList.toArray());
			//Delete DASH_MESSAGES - End
			
			//Insert DASH_MESSAGES - Start
			int[] insertDMCountArr = getJdbcTemplatePrism().batchUpdate(IQueryConstants.INSERT_DASH_MESSAGES,new BatchPreparedStatementSetter() {
				
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ManageMessageTO manageMessageTO = manageMessageTOList.get(i);
					ps.setLong(1, manageMessageTO.getReportId());
					ps.setLong(2, manageMessageTO.getMessageTypeId());
					ps.setString(3, manageMessageTO.getMessage());
					ps.setLong(4, manageMessageTO.getCustProdIdHidden());
					ps.setString(5, IApplicationConstants.CHECKED_CHECKBOX_VALUE.equalsIgnoreCase(manageMessageTO.getActivationStatus()) 
							? IApplicationConstants.CHECKED_VALUE_DRM_CHECKBOX : IApplicationConstants.DEFAULT_VALUE_DRM_CHECKBOX);
				}
				
				public int getBatchSize() {
					return manageMessageTOList.size();
				}
				
			});
			//Insert DASH_MESSAGES - End
			insertDMFlag = Utils.batchUpdateCheck(insertDMCountArr);
			if(insertDMFlag == 1){
				successFlag = 1;
			}
			
		}catch(Exception e){
			logger.error("Error occurred in saveManageMessage():", e);
			e.printStackTrace();
			successFlag = 0;
			throw new SystemException(e);
		}finally{
			if(successFlag == 0){
				throw new SystemException("Exception: ReportDAOImpl - saveManageMessage()");
			}
		}
		logger.info("Exit: ReportDAOImpl - saveManageMessage()");
		return successFlag;
	}
	
	//365348
	public boolean updateReportNew(ReportTO reportTO) 
	{
		logger.info("Enter: ReportDAOImpl - updateReport");
		try {
			    int userRoleLoop=0,orgLevelLoop=0;
			    getJdbcTemplatePrism().update(IQueryConstants.UPDATE_REPORT_NEW,reportTO.getReportName(),reportTO.getReportDescription(),reportTO.getReportUrl(),reportTO.getReportStatus(),reportTO.getReportType(), reportTO.getReportId());
			    getJdbcTemplatePrism().update(IQueryConstants.DELETE_REPORT_ROLE, reportTO.getReportId());
							for(userRoleLoop=0;userRoleLoop<reportTO.getUserRoles().length;userRoleLoop++)
							{
								
									for(orgLevelLoop=0 ; orgLevelLoop < reportTO.getOrgNodeLevelArr().length -1 ; orgLevelLoop++)
									 {
										getJdbcTemplatePrism().update(IQueryConstants.INSERT_REPORT_ROLE,reportTO.getMenuId(),reportTO.getReportId(),reportTO.getUserRoles()[userRoleLoop],reportTO.getOrgNodeLevelArr()[orgLevelLoop],reportTO.getCustomerLinks(),reportTO.getReportId(),IApplicationConstants.ACTIVE_FLAG);
									 }
						    }
		} catch (Exception e) {
			logger.error("Error occurred while updating report details.", e);
			return false;
		}
		logger.info("Exit: ReportDAOImpl - updateReport");
		return true;
	}
	

	/**
	 * add new dashboard
	 * @param String reportName, String reportDescription, String userName,
	 *	String reportType, String password, String assessmentType,String reportStatus, String[] userRoles
	 *@return ReportTO
	 */
	
	//Arunava Datta
	public ReportTO addNewDashboard(ReportParameterTO reportParameterTO)throws Exception{
		logger.info("Enter: ReportDAOImpl - addNewDashboard");
		
		ReportTO reportTo=null;
		String reportName = reportParameterTO.getReportName();
		String reportDescription = reportParameterTO.getReportDescription();
		String reportType = reportParameterTO.getReportType();
		String reportUri = reportParameterTO.getReportUrl();
		String assessmentType = reportParameterTO.getAssessmentName();
		String reportStatus = reportParameterTO.getReportStatus();
		Long customerLinks = reportParameterTO.getLinkName();
		String[] userRoles = reportParameterTO.getUserRoles();
		String[] orgNodeLevel = reportParameterTO.getOrgNodeLevel();
		try {
			
			List<Map<String, Object>> reportMap=null;
			reportMap= getJdbcTemplatePrism().queryForList(IQueryConstants.CHECK_EXISTING_REPORT,reportName);
			if (reportMap == null || reportMap.isEmpty())
					{
				    int userRoleLoop = 0,orgNodeLevelLoop=0;
				    long report_seq_id = getJdbcTemplatePrism().queryForLong(
							IQueryConstants.DB_REPORT_SEQ_ID);
					
					getJdbcTemplatePrism()
							.update(IQueryConstants.INSERT_REPORT,report_seq_id,
									reportName, reportDescription, reportType,
									reportUri,reportStatus);
						
							for(userRoleLoop=0;userRoleLoop<userRoles.length;userRoleLoop++)
							{
								for(orgNodeLevelLoop=0;orgNodeLevelLoop<orgNodeLevel.length;orgNodeLevelLoop++)
								{
									getJdbcTemplatePrism().update(
													IQueryConstants.INSERT_REPORT_ROLE,
													reportParameterTO.getMenuId(),
													report_seq_id,
													userRoles[userRoleLoop],
													orgNodeLevel[orgNodeLevelLoop],
													customerLinks,
													report_seq_id,IApplicationConstants.ACTIVE_FLAG);
								 }
							  }
					
								reportTo = getDashboardData(String.valueOf(report_seq_id ));
						}
		}
		catch(Exception e){
		logger.error("Error occurred while adding dashboard details.", e);
		return null;
		}
	logger.info("Exit: ReportDAOImpl - addNewDashboard");
	return reportTo;	
	}
	
	/**
	 * @author Arunava
	 * @param paramMap
	 * @return
	 */
	//Arunava Datta for Group Download listing

	public List<JobTrackingTO> getAllGroupDownloadFiles(Map<String,Object> paramMap)
	{
		logger.info( "Enter: ReportDAOImpl - getAllGroupDownloadFiles");
		UserTO loggedinUserTO = (UserTO) paramMap.get("loggedinUserTO");
		List<JobTrackingTO> allGroupFiles = null;
		List<Map<String,Object>> dataList = getJdbcTemplatePrism().queryForList(IReportQuery.GET_GROUP_DOWNLOAD_LIST,loggedinUserTO.getUserId());
		if ( dataList != null && dataList.size() > 0 )
		{
			allGroupFiles = new ArrayList<JobTrackingTO>();
			for (Map<String, Object> data : dataList)
			{
				    JobTrackingTO to = new JobTrackingTO();
				    String createdDate = getFormattedDate((Timestamp) data.get("created_date_time"),"MM/dd/yyyy HH:mm:ss");
			        to.setJobId( ((BigDecimal) data.get("job_id")).longValue());
				    Timestamp ts =(Timestamp)data.get("updated_date_time");
				    Calendar cal = Calendar.getInstance();
				    cal.setTime(ts);
				    cal.add(Calendar.DAY_OF_WEEK,(Integer.parseInt((String) paramMap.get("gdfExpiryTime"))));
				    ts.setTime(cal.getTime().getTime());
				    ts = new Timestamp(cal.getTime().getTime());
				    String updatedDate =getFormattedDate(ts,"MM/dd/yyyy HH:mm:ss");
				    if(null!=((String) data.get("file_size")) || ((String) data.get("file_size")) !=""){
				    to.setFileSize((String) data.get("file_size"));
				    }else{
				    to.setFileSize(" ");	
				    }
				    String filePath=(String) data.get("request_filename");
				    String fileName="";
				    if (filePath.lastIndexOf("\\") >=0)
				    {
				    	fileName = filePath.substring(filePath.lastIndexOf("\\")+1);
				    }
				    else
				    {
				    	fileName = filePath.substring(filePath.lastIndexOf("/")+1);
				    }
				    //fileName = filePath.substring(filePath.lastIndexOf(File.separator)+1);
				    to.setRequestFilename(fileName);
				    to.setFilePath(filePath);
				    to.setCreatedDateTime(createdDate);
				    to.setUpdatedDateTime(updatedDate);
				    to.setRequestType((String) data.get("request_type"));
				    to.setJobStatus((String) data.get("job_status"));
					allGroupFiles.add(to);
			}
		}
		logger.info( "Exit: ReportDAOImpl - getAllGroupDownloadFiles");
		return allGroupFiles;
	}

	/**
	 * @author Arunava
	 * @param paramMap
	 * @return
	 */
	//Arunava Datta for Group Download listing

	public List<JobTrackingTO> getRequestDetail(Map<String,Object> paramMap)
	{
		logger.info( "Enter: ReportDAOImpl - getRequestDetail");
		List<JobTrackingTO> allGroupFiles = null;
		String jobId = (String) paramMap.get("jobId");
		List<Map<String,Object>> dataList = getJdbcTemplatePrism().queryForList(IReportQuery.GET_GROUP_DOWNLOAD_REQUEST_VIEW,jobId);
		if ( dataList != null && dataList.size() > 0 )
		{
			allGroupFiles = new ArrayList<JobTrackingTO>();
			for (Map<String, Object> data : dataList)
			{
				    JobTrackingTO to = new JobTrackingTO();
				    String filePath=(String) data.get("request_filename");
				    //String extractStartdate = getFormattedDate((Timestamp) data.get("extract_startdate"),"MM/dd/yyyy HH:mm:ss");
				    //String extractEnddate = getFormattedDate((Timestamp) data.get("extract_enddate"),"MM/dd/yyyy HH:mm:ss");
				    String fileName = "";
				    if (filePath.lastIndexOf("\\") >=0)
				    {
				    	fileName = filePath.substring(filePath.lastIndexOf("\\")+1);
				    }
				    else
				    {
				    	fileName = filePath.substring(filePath.lastIndexOf("/")+1);
				    }
				    to.setRequestFilename(fileName);
				    to.setRequestType((String) data.get("request_type"));
				    to.setRequestDetails((String) data.get("request_details"));
				    to.setRequestSummary((String) data.get("request_summary"));
				    to.setExtractCategory((String) data.get("extract_category"));
				    //to.setExtractStartdate(extractStartdate);
				    //to.setExtractEnddate(extractEnddate);
				    to.setJobName((String) data.get("job_name"));
					allGroupFiles.add(to);
			}
		}
		logger.info( "Exit: ReportDAOImpl - getAllGroupDownloadFiles");
		return allGroupFiles;
	}

	/**
	 * @author Arunava
	 * @param id
	 * @return
	 * @throws SystemException
	 */
	//Arunava Datta for Group Download deleting

	@TriggersRemove(cacheName="orgUsers", removeAll=true)
	public boolean deleteGroupFiles(String Id)
			throws Exception {
		try {
				getJdbcTemplatePrism().update(IReportQuery.DELETE_GROUP_FILES, Id);
		} 
		catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Returns the date in a defined format
	 * @author Arunava Datta
	 * @param pTimestamp
	 * @param pFormat  examples: dd-MM-yyyy
	 * @return
	 * @see http://java.sun.com/j2se/1.4.2/docs/api/java/text/SimpleDateFormat.html*/
		public static String getFormattedDate(Timestamp pTimestamp, String pFormat) {
			DateFormat dF = new java.text.SimpleDateFormat(pFormat);
			return dF.format(pTimestamp.getTime());
		}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.dao.IReportDAO#getTestAdministrations()
	 */
	public List<com.ctb.prism.report.transferobject.ObjectValueTO> getTestAdministrations() {
		return getObjectValueTOList(IQueryConstants.GET_TEST_ADMINISTRATIONS_GD);
	}
	
	/* (non-Javadoc)
	 * @see com.ctb.prism.report.dao.IReportDAO#getDistricts()
	 */
	public List<com.ctb.prism.report.transferobject.ObjectValueTO> getDistricts() {
		return getObjectValueTOList(IQueryConstants.GET_DISTRICTS_GD, new Object[]{"PUBLIC"});
	}
	
	/* (non-Javadoc)
	 * @see com.ctb.prism.report.dao.IReportDAO#getGrades()
	 */
	public List<com.ctb.prism.report.transferobject.ObjectValueTO> getGrades() {
		return getObjectValueTOList(IQueryConstants.GET_GRADES_GD);
	}
	
	/* (non-Javadoc)
	 * @see com.ctb.prism.report.dao.IReportDAO#populateSchoolGD(java.lang.Long)
	 */
	public List<com.ctb.prism.report.transferobject.ObjectValueTO> populateSchoolGD(Long parentOrgNodeId) {
		return getObjectValueTOList(IQueryConstants.GET_SCHOOLS_GD, new Object[]{"PUBLIC", parentOrgNodeId});
	}
	
	public List<com.ctb.prism.report.transferobject.ObjectValueTO> populateClassGD(Long parentOrgNodeId) {
		return getObjectValueTOList(IQueryConstants.GET_CLASSES_GD, new Object[]{"PUBLIC", parentOrgNodeId});
	}
	
	public List<com.ctb.prism.report.transferobject.ObjectValueTO> populateStudentTableGD(Long orgNodeId) {
		return getObjectValueTOList(IQueryConstants.GET_STUDENT_TABLE_GD, new Object[]{orgNodeId});
	}

	/**
	 * Utility method to run database query and create list of ObjectValueTO
	 * 
	 * @param query
	 * @return
	 */
	private List<com.ctb.prism.report.transferobject.ObjectValueTO> getObjectValueTOList(
			String query) {
		List<com.ctb.prism.report.transferobject.ObjectValueTO> list = getJdbcTemplatePrism()
				.query(query,
						new com.ctb.prism.report.transferobject.ObjectValueTOMapper() {
							public ObjectValueTO mapRow(ResultSet rs, int col)
									throws SQLException {
								ObjectValueTO to = new ObjectValueTO();
								to.setValue(rs.getString(1));
								to.setName(rs.getString(2));
								return to;
							}
						});
		return list;
	}

	/**
	 * Overloaded utility method to run database query and create list of
	 * ObjectValueTO. Query params can be passed.
	 * 
	 * @param query
	 * @param params
	 * @return
	 */
	private List<com.ctb.prism.report.transferobject.ObjectValueTO> getObjectValueTOList(
			String query, Object[] params) {
		List<com.ctb.prism.report.transferobject.ObjectValueTO> list = getJdbcTemplatePrism()
				.query(query,
						params,
						new com.ctb.prism.report.transferobject.ObjectValueTOMapper() {
							public ObjectValueTO mapRow(ResultSet rs, int col)
									throws SQLException {
								ObjectValueTO to = new ObjectValueTO();
								to.setValue(rs.getString(1));
								to.setName(rs.getString(2));
								return to;
							}
						});
		return list;
	}
}
