package com.ctb.prism.report.business;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.login.dao.ILoginDAO;
import com.ctb.prism.report.dao.IReportDAO;
import com.ctb.prism.report.transferobject.AssessmentTO;
import com.ctb.prism.report.transferobject.GroupDownloadTO;
import com.ctb.prism.report.transferobject.IReportFilterTOFactory;
import com.ctb.prism.report.transferobject.InputControlTO;
import com.ctb.prism.report.transferobject.JobTrackingTO;
import com.ctb.prism.report.transferobject.ManageMessageTO;
import com.ctb.prism.report.transferobject.ObjectValueTO;
import com.ctb.prism.report.transferobject.ReportParameterTO;
import com.ctb.prism.report.transferobject.ReportTO;

@Component("reportBusiness")
public class ReportBusinessImpl implements IReportBusiness {

	private static final IAppLogger logger = LogFactory.getLoggerInstance(ReportBusinessImpl.class.getName());

	@Autowired
	private IReportDAO reportDAO;

	@Autowired
	private ILoginDAO loginDAO;

	@Autowired
	private IPropertyLookup propertyLookup;

	@Autowired
	private IReportFilterTOFactory reportFilterFactory;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#getFilledReport(net.sf.jasperreports.engine.JasperReport, java.util.Map)
	 */
	public JasperPrint getFilledReport(JasperReport jasperReport, Map<String, Object> parameters) throws Exception {
		if ((jasperReport != null && "Invitation_Pdf".equals(jasperReport.getName().trim())) || "false".equals(propertyLookup.get("jasper.filled.report.cache"))) {
			return reportDAO.getFilledReportNoCache(jasperReport, parameters);
		} else {
			return reportDAO.getFilledReport(jasperReport, parameters);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#removeReportCache()
	 */
	public void removeReportCache() {
		reportDAO.removeReportCache();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#removeCache()
	 */
	public void removeCache() {
		reportDAO.removeCache();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#getReportJasperObject(java.lang.String)
	 */
	public JasperReport getReportJasperObject(String reportPath) {
		return reportDAO.getReportJasperObject(reportPath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#getReportJasperObjectForName(java.lang.String)
	 */
	public JasperReport getReportJasperObjectForName(String reportname) {
		return reportDAO.getReportJasperObjectForName(reportname);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#getReportJasperObjectList(java.lang.String)
	 */
	public List<ReportTO> getReportJasperObjectList(final String reportPath) throws JRException {
		return reportDAO.getReportJasperObjectList(reportPath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#getInputControlDetails(java.lang.String)
	 */
	public List<InputControlTO> getInputControlDetails(String reportPath) {
		return reportDAO.getInputControlDetails(reportPath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#getAllInputControls()
	 */
	public List<InputControlTO> getAllInputControls() {
		return reportDAO.getAllInputControls();
	}

	/**
	 * Returns the default filter values for a report
	 * 
	 * @param tos
	 *            List of input control details
	 * @param userName
	 *            logged in user name
	 * @param assessmentId
	 * @param combAssessmentId
	 * @param reportUrl
	 */
	// @Cacheable(cacheName = "defaultInputControls")
	public Object getDefaultFilter(List<InputControlTO> tos, String userName, String assessmentId, String combAssessmentId, String reportUrl) {
		logger.log(IAppLogger.INFO, "Enter: ReportBusinessImpl - getDefaultFilter");
		Class<?> clazz = null;
		Object obj = null;
		String tenantId = null;
		// ReportFilterTO to = new ReportFilterTO();
		tenantId = reportDAO.getTenantId(userName);
		try {
			clazz = reportFilterFactory.getReportFilterTO();
			obj = clazz.newInstance();
			clazz.getMethod("setLoggedInUserJasperOrgId", String.class).invoke(obj, tenantId);
			clazz.getMethod("setLoggedInUserName", String.class).invoke(obj, userName);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		for (InputControlTO ito : tos) {
			String labelId = ito.getLabelId();
			String query = ito.getQuery();
			if (query != null) {
				query = query.replaceAll(IApplicationConstants.LOGGED_IN_USER_JASPER_ORG_ID, tenantId);
				query = query.replaceAll(IApplicationConstants.LOGGED_IN_USERNAME, CustomStringUtil.appendString("'", userName, "'"));
				query = query.replaceAll("\\$[P][{]\\w+[}]", "-99");
				// handle special i/p controls
				query = replaceSpecial(query, clazz, obj);
				logger.log(IAppLogger.DEBUG, query);
				List<ObjectValueTO> list = reportDAO.getValuesOfSingleInput(query);
				/*
				 * if(list != null && list.size() == 0) { // patch for form level if(IApplicationConstants.IC_FORM_LEVEL.equals(ito.getLabel())) { ObjectValueTO formObj = new ObjectValueTO();
				 * formObj.setName(""); formObj.setValue("0_0-0"); list.add(formObj); } }
				 */
				String methodName = null;
				try {
					methodName = CustomStringUtil.appendString("set", labelId.substring(0, 1).toUpperCase(), labelId.substring(1));
					Method setterMethod = clazz.getMethod(methodName, new Class[] { List.class });
					setterMethod.invoke(obj, list);
				} catch (Exception e) {
					logger.log(IAppLogger.WARN, CustomStringUtil.appendString("Could not invoke method ", methodName, "on ReportFilterTO"), e);
				}
			}
		}

		logger.log(IAppLogger.INFO, "Exit: ReportBusinessImpl - getDefaultFilter");
		return obj;
	}

	/**
	 * update in-statement of queries
	 * 
	 * @param inQuery
	 * @param to
	 * @return
	 */
	private String replaceSpecial(String inQuery, Class<?> clazz, Object obj) {
		String tempQuery = inQuery;
		String trimPart = "";
		String replacedQuery = "";
		try {
			if (tempQuery.indexOf("$X{IN") != -1) {
				trimPart = tempQuery.substring(tempQuery.indexOf("$X{IN"), tempQuery.indexOf("}") + 1);
				String part = tempQuery.substring(tempQuery.indexOf("$X{IN") + 3, tempQuery.indexOf("}"));
				String[] parts = part.split(",");
				if (parts.length == 3) {
					String coll = CustomStringUtil.capitalizeFirstCharacter(parts[2].trim());
					Method m = clazz.getMethod(CustomStringUtil.appendString("get", coll));
					ArrayList<ObjectValueTO> listOfValues = (ArrayList<ObjectValueTO>) m.invoke(obj);
					StringBuilder builder = new StringBuilder();
					builder.append(parts[1]).append(" ").append(parts[0]).append(" ");
					boolean isFirst = true;
					builder.append(" (");
					if (listOfValues != null && listOfValues.size() == 0) {
						builder.append("-99");
					} else {
						for (ObjectValueTO objectValue : listOfValues) {
							if (!isFirst)
								builder.append(",");
							isFirst = false;
							builder.append("'").append(objectValue.getValue()).append("'");
						}
					}
					builder.append(") ");

					replacedQuery = tempQuery.replace(trimPart, builder.toString());
					if (replacedQuery.indexOf("$X{IN") != -1) {
						replacedQuery = replaceSpecial(replacedQuery, clazz, obj);
					}
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			return inQuery;
		}
		return replacedQuery.length() == 0 ? inQuery : replacedQuery;
	}

	/**
	 * Fetch all values of a input after replacing all required parameters
	 * 
	 * @param query
	 * @param userName
	 * @return
	 * @throws SystemException
	 * @throws IllegalArgumentException
	 */
	public List<ObjectValueTO> getValuesOfSingleInput(String query, String userName, String changedObject, String changedValue, Map<String, String> replacableParams, Object obj)
			throws SystemException {
		if (query == null)
			return null;

		try {
			Class<?> c = reportFilterFactory.getReportFilterTO();
			// replace all params
			String tenantId = reportDAO.getTenantId(userName);
			query = query.replaceAll(IApplicationConstants.LOGGED_IN_USER_JASPER_ORG_ID, tenantId);
			query = query.replaceAll(IApplicationConstants.LOGGED_IN_USERNAME, CustomStringUtil.appendString("'", userName, "'"));
			query = query.replace(CustomStringUtil.getJasperParameterString(changedObject), CustomStringUtil.appendString("'", changedValue, "'"));

			// replace all required params
			if (query != null && query.indexOf(IApplicationConstants.JASPER_PARAM_INITIAL) != -1) {
				@SuppressWarnings("rawtypes")
				Iterator it = replacableParams.entrySet().iterator();
				while (it.hasNext()) {
					try {
						@SuppressWarnings("rawtypes")
						Map.Entry pairs = (Map.Entry) it.next();
						if (pairs.getValue() != null && pairs.getValue() instanceof String) {
							query = query.replace((String) pairs.getKey(), CustomStringUtil.appendString("'", (String) pairs.getValue(), "'"));
							if (query.indexOf(IApplicationConstants.JASPER_PARAM_INITIAL) == -1) {
								break;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			// handle special i/p controls
			if (changedValue != null) {
				String[] chengedValueArr = changedValue.split(",");
				if (chengedValueArr != null && chengedValueArr.length > 0) {
					ArrayList<ObjectValueTO> listOfValues = new ArrayList<ObjectValueTO>();
					for (String val : chengedValueArr) {
						ObjectValueTO objectValueTo = new ObjectValueTO();
						objectValueTo.setValue(val);
						listOfValues.add(objectValueTo);
					}
					String coll = CustomStringUtil.capitalizeFirstCharacter(changedObject);
					String methodName = CustomStringUtil.appendString("set", coll);
					Method m = c.getMethod(methodName, new Class[] { List.class });
					m.invoke(obj, listOfValues);
					query = replaceSpecial(query, c, obj);
				}
			}

			// replace others with null - not required --
			if (query != null && query.indexOf(IApplicationConstants.JASPER_PARAM_INITIAL) != -1) {
				query = query.replaceAll("\\$[P][{]\\w+[}]", "null");
			}

			logger.log(IAppLogger.DEBUG, query);

			// fetch data
			return reportDAO.getValuesOfSingleInput(query);
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#getValuesOfSingleInput(java.lang.String)
	 */
	public List<ObjectValueTO> getValuesOfSingleInput(String query) {
		return reportDAO.getValuesOfSingleInput(query);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#getAllReportList(java.util.Map)
	 */
	public List<ReportTO> getAllReportList(Map<String, Object> paramMap) {
		List<ReportTO> allReports = reportDAO.getAllReportList(paramMap);
		if (allReports != null && !allReports.isEmpty()) {
			List<ObjectValueTO> roles = reportDAO.getAllRoles();
			allReports.get(0).setObjectList(roles);

			List<ObjectValueTO> assessments = reportDAO.getAllAssessments();
			allReports.get(0).setObjectList2(assessments);

			try {
				List<com.ctb.prism.core.transferobject.ObjectValueTO> adminyear = reportDAO.getAdminYear(null);
				allReports.get(0).setAdminYear(adminyear);
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				List<com.ctb.prism.core.transferobject.ObjectValueTO> customerProduct = loginDAO.getCustomerProduct(paramMap);
				allReports.get(0).setCustomerProductList(customerProduct);
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				List<com.ctb.prism.core.transferobject.ObjectValueTO> orgNodeLevel = reportDAO.getOrgNodeLevel(null);
				allReports.get(0).setOrgNodeLevelList(orgNodeLevel);
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return allReports;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#updateReport(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
	 */
	public boolean updateReport(String reportId, String reportName, String reportUrl, String isEnabled, String[] roles) {
		return reportDAO.updateReport(reportId, reportName, reportUrl, isEnabled, roles);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#updateReportNew(com.ctb.prism.report.transferobject.ReportTO)
	 */
	public boolean updateReportNew(ReportTO reportTO) {
		return reportDAO.updateReportNew(reportTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#getAssessments(boolean)
	 */
	public List<AssessmentTO> getAssessments(boolean parentReports) {
		return reportDAO.getAssessments(parentReports);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#deleteReport(java.lang.String)
	 */
	public boolean deleteReport(String reportId) throws SystemException {
		return reportDAO.deleteReport(reportId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#addNewDashboard(com.ctb.prism.report.transferobject.ReportParameterTO)
	 */
	public ReportTO addNewDashboard(ReportParameterTO reportParameterTO) throws Exception {
		return reportDAO.addNewDashboard(reportParameterTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#getReport(java.lang.String)
	 */
	public ReportTO getReport(String reportIdentifier) throws SystemException {
		return reportDAO.getReport(reportIdentifier);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#getReportMessageFilter(java.util.Map)
	 */
	public Map<String, Object> getReportMessageFilter(final Map<String, Object> paramMap) throws SystemException {
		List<com.ctb.prism.core.transferobject.ObjectValueTO> customerProductList = loginDAO.getCustomerProduct(paramMap);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("customerProductList", customerProductList);
		return returnMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#loadManageMessage(java.util.Map)
	 */
	public Map<String, Object> loadManageMessage(final Map<String, Object> paramMap) throws SystemException {
		List<ManageMessageTO> manageMessageTOList = reportDAO.loadManageMessage(paramMap);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("manageMessageTOList", manageMessageTOList);
		return returnMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#saveManageMessage(java.util.List)
	 */
	public int saveManageMessage(final List<ManageMessageTO> manageMessageTOList) throws SystemException {
		int saveFlag = reportDAO.saveManageMessage(manageMessageTOList);
		return saveFlag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#getAllGroupDownloadFiles(java.util.Map)
	 */
	public List<JobTrackingTO> getAllGroupDownloadFiles(Map<String, Object> paramMap) throws SystemException {
		return reportDAO.getAllGroupDownloadFiles(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#getRequestDetail(java.util.Map)
	 */
	public List<JobTrackingTO> getRequestDetail(Map<String, Object> paramMap) throws SystemException {
		return reportDAO.getRequestDetail(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#deleteGroupFiles(java.lang.String)
	 */
	public boolean deleteGroupFiles(String Id) throws Exception {
		return reportDAO.deleteGroupFiles(Id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#deleteScheduledGroupFiles(java.lang.String)
	 */
	public void deleteScheduledGroupFiles(String gdfExpiryTime) throws Exception {
		reportDAO.deleteScheduledGroupFiles(gdfExpiryTime);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#getTestAdministrations()
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getTestAdministrations() {
		return reportDAO.getTestAdministrations();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#getDistricts()
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateDistrictGD(GroupDownloadTO to) {
		return reportDAO.populateDistrictGD(to);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#getGrades()
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateGradeGD(GroupDownloadTO to) {
		return reportDAO.populateGradeGD(to);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#populateSchoolGD(com.ctb.prism.report.transferobject.GroupDownloadTO)
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateSchoolGD(GroupDownloadTO to) {
		return reportDAO.populateSchoolGD(to);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#populateClassGD(com.ctb.prism.report.transferobject.GroupDownloadTO)
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateClassGD(GroupDownloadTO to) {
		return reportDAO.populateClassGD(to);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#populateStudentTableGD(com.ctb.prism.report.transferobject.GroupDownloadTO)
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateStudentTableGD(GroupDownloadTO to) {
		return reportDAO.populateStudentTableGD(to);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#getGDFilePaths(com.ctb.prism.report.transferobject.GroupDownloadTO)
	 */
	public List<String> getGDFilePaths(GroupDownloadTO to) {
		return reportDAO.getGDFilePaths(to);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#createJobTracking(com.ctb.prism.report.transferobject.GroupDownloadTO)
	 */
	public String createJobTracking(GroupDownloadTO to) {
		return reportDAO.createJobTracking(to);
	}
	
	public String getSystemConfigurationMessage(Map<String,Object> paramMap) {
		return reportDAO.getSystemConfigurationMessage(paramMap);
	}
}
