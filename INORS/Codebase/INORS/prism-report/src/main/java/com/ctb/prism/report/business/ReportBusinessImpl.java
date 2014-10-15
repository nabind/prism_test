package com.ctb.prism.report.business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.login.dao.ILoginDAO;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.report.dao.IReportDAO;
import com.ctb.prism.report.transferobject.AssessmentTO;
import com.ctb.prism.report.transferobject.GroupDownloadStudentTO;
import com.ctb.prism.report.transferobject.GroupDownloadTO;
import com.ctb.prism.report.transferobject.IReportFilterTOFactory;
import com.ctb.prism.report.transferobject.InputControlTO;
import com.ctb.prism.report.transferobject.JobTrackingTO;
import com.ctb.prism.report.transferobject.ManageMessageTO;
import com.ctb.prism.report.transferobject.ObjectValueTO;
import com.ctb.prism.report.transferobject.ReportMessageTO;
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
		if (jasperReport != null 
				&& ("Rescore Request Form".equals(jasperReport.getName().trim()) 
						|| "Invitation Pdf".equals(jasperReport.getName().trim()))
						|| "Rescore Request Log".equals(jasperReport.getName().trim())
						|| "Rescore Request Summary".equals(jasperReport.getName().trim())) {
			return reportDAO.getFilledReportIC(jasperReport, parameters);
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
	
	public boolean removeCache(InputStream input){
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		try {
			while (true) {
	            String cacheKey;
				
					cacheKey = reader.readLine();
				
	            if (cacheKey == null) break;
	            reportDAO.removeCache(cacheKey);
	        }  
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void removeConfigurationCache() {
		reportDAO.removeConfigurationCache();
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
	
	public String getTenantId(String userName) {
		return reportDAO.getTenantId(userName);
	}

	/**
	 * Returns the default filter values for a report
	 * 
	 * @param tos
	 *            List of input control details
	 * @param userName
	 *            logged in user name
	 * @param customerId
	 * @param assessmentId
	 * @param combAssessmentId
	 * @param reportUrl
	 * @param sessionParams
	 * @param userId
	 */
	@Cacheable(value = "defaultCache", 
			key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( ('getDefaultFilter').concat(#tenantId).concat(#reportUrl).concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#sessionParams)) )")
	public Object getDefaultFilter(List<InputControlTO> tos, String userName, String customerId, String assessmentId, String combAssessmentId, 
			String reportUrl, Map<String, Object> sessionParams, String userId, String tenantId) {
		logger.log(IAppLogger.INFO, "Enter: ReportBusinessImpl - getDefaultFilter");
		Class<?> clazz = null;
		Object obj = null;
		//String tenantId = null;
		// ReportFilterTO to = new ReportFilterTO();
		//tenantId = reportDAO.getTenantId(userName);
		try {
			clazz = reportFilterFactory.getReportFilterTO();
			obj = clazz.newInstance();
			clazz.getMethod("setLoggedInUserJasperOrgId", String.class).invoke(obj, tenantId);
			clazz.getMethod("setLoggedInUserName", String.class).invoke(obj, userName);
			clazz.getMethod("setLoggedInUserId", String.class).invoke(obj, userId);
			clazz.getMethod("setP_customerid", String.class).invoke(obj, customerId);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		for (InputControlTO ito : tos) {
			String labelId = ito.getLabelId();
			String query = ito.getQuery();
			if (query != null) {
				query = query.replaceAll(IApplicationConstants.LOGGED_IN_USER_ID, userId);
				query = query.replaceAll(IApplicationConstants.LOGGED_IN_USER_JASPER_ORG_ID, tenantId);
				query = query.replaceAll(IApplicationConstants.LOGGED_IN_USERNAME, CustomStringUtil.appendString("'", userName, "'"));				
				query = query.replaceAll(IApplicationConstants.LOGGED_IN_CUSTOMER, customerId);

				/*** NEW ***/
				if (sessionParams != null) {
					Iterator it = sessionParams.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry pairs = (Map.Entry) it.next();
						try {
							String m = CustomStringUtil.appendString("get", ((String) pairs.getKey()).substring(0, 1).toUpperCase(), ((String) pairs.getKey()).substring(1));
							List<ObjectValueTO> tempObj = (List<ObjectValueTO>) clazz.getMethod(m).invoke(obj);
							boolean matched = false;
							if (tempObj != null && tempObj.size() > 0) {
								for (ObjectValueTO val : tempObj) {
									if (val.getValue().equals(((String[]) pairs.getValue())[0])) {
										matched = true;
										break;
									}
								}
								if (matched) {
									if("p_generate_file".equals((String) pairs.getKey())) {
										query = query.replaceAll(CustomStringUtil.getJasperParameterStringRegx((String) pairs.getKey()), 
												CustomStringUtil.appendString("'",((String[]) pairs.getValue())[0], "'"));
									} else {
										query = query.replaceAll(CustomStringUtil.getJasperParameterStringRegx((String) pairs.getKey()), ((String[]) pairs.getValue())[0]);
									}
									//query = query.replaceAll(CustomStringUtil.getJasperParameterStringRegx((String) pairs.getKey()), ((String[]) pairs.getValue())[0]);
								} else {
									if("p_generate_file".equals((String) pairs.getKey())) {
										query = query.replaceAll(CustomStringUtil.getJasperParameterStringRegx((String) pairs.getKey()), 
												CustomStringUtil.appendString("'",tempObj.get(0).getValue(), "'"));
									} else {
										query = query.replaceAll(CustomStringUtil.getJasperParameterStringRegx((String) pairs.getKey()), tempObj.get(0).getValue());
									}
									//query = query.replaceAll(CustomStringUtil.getJasperParameterStringRegx((String) pairs.getKey()), tempObj.get(0).getValue());
								}
							}
						} catch (Exception e) {
							/** Nothing to do here 
							 * - this block will get lots of java.lang.NoSuchMethodException
							 * - which is expected and known
							 * - as the ReportFilterTO (dynamic) class contains only report filter setters/getters **/
							//e.printStackTrace();
						}
					}
				}
				/*** NEW ***/

				query = query.replaceAll("\\$[P][{]\\w+[}]", "-99");
				// handle special i/p controls
				query = replaceSpecial(query, clazz, obj);
				logger.log(IAppLogger.DEBUG, query);
				
				//System.out.println("Query for default filter:" +query);
				
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
	 * Replace special for session input controls
	 * 
	 * @param inQuery
	 * @param inputCollection
	 * @return
	 */
	private String replaceSpecial(String inQuery, List<String> inputCollection) {
		String tempQuery = inQuery;
		String trimPart = "";
		String replacedQuery = "";
		try {
			if (tempQuery.indexOf("$X{IN") != -1) {
				trimPart = tempQuery.substring(tempQuery.indexOf("$X{IN"), tempQuery.indexOf("}") + 1);
				String part = tempQuery.substring(tempQuery.indexOf("$X{IN") + 3, tempQuery.indexOf("}"));
				String[] parts = part.split(",");
				if (parts.length == 3) {

					StringBuilder builder = new StringBuilder();
					builder.append(parts[1]).append(" ").append(parts[0]).append(" ");
					boolean isFirst = true;
					builder.append(" (");
					if (inputCollection != null && inputCollection.size() == 0) {
						builder.append("-99");
					} else {
						for (String objectValue : inputCollection) {
							if (!isFirst)
								builder.append(",");
							isFirst = false;
							builder.append("'").append(objectValue).append("'");
						}
					}
					builder.append(") ");

					replacedQuery = tempQuery.replace(trimPart, builder.toString());
					if (replacedQuery.indexOf("$X{IN") != -1) {
						replacedQuery = replaceSpecial(replacedQuery, inputCollection);
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
	 * @param customerId
	 * @param changedObject
	 * @param changedValue
	 * @param replacableParams
	 * @param obj 
	 * @param userId 
	 * @return
	 * @throws SystemException
	 * @throws IllegalArgumentException
	 */
	public List<ObjectValueTO> getValuesOfSingleInput(String query, String userName, String customerId, String changedObject, String changedValue, Map<String, String> replacableParams, Object obj, String userId)
			throws SystemException {
		if (query == null)
			return null;

		try {
			Class<?> c = reportFilterFactory.getReportFilterTO();
			// replace all params
			//String tenantId = reportDAO.getTenantId(userName);
			String tenantId = replacableParams.get(IApplicationConstants.CURRORG);
			query = query.replaceAll(IApplicationConstants.LOGGED_IN_USER_ID, userId);
			query = query.replaceAll(IApplicationConstants.LOGGED_IN_USER_JASPER_ORG_ID, tenantId);
			query = query.replaceAll(IApplicationConstants.LOGGED_IN_USERNAME, CustomStringUtil.appendString("'", userName, "'"));
			query = query.replaceAll(IApplicationConstants.LOGGED_IN_CUSTOMER, customerId);			
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
			//System.out.println("Query for Cascading filter:" +query);

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
	 * @see com.ctb.prism.report.business.IReportBusiness#updateReportNew(com.ctb.prism.report.transferobject.ReportTO)
	 */
	public boolean updateReportNew(ReportTO reportTO) {
		return reportDAO.updateReportNew(reportTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ctb.prism.report.business.IReportBusiness#getAssessments(paramMap)
	 */
	public List<AssessmentTO> getAssessments(Map<String, Object> paramMap) {
		boolean isSuperUser = false;
		boolean isGrowthUser = false;
		boolean isEduUser = false;
		UserTO loggedinUserTO = (UserTO) paramMap.get("loggedinUserTO");
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		authList = loggedinUserTO.getRoles();
		for (int i = 0; i < authList.size(); i++) {
			if (authList.get(i).getAuthority().equals("ROLE_GRW")) {
				isGrowthUser = true;
				break;
			}
			if (authList.get(i).getAuthority().equals("ROLE_SUPER")) {// This is needed if a super user is having growth role as well
				isSuperUser = true;
				break;
			}
		}
		if ("E".equals(loggedinUserTO.getUserType())) {
			isEduUser = true;
		}
		Boolean parentReports = ((Boolean) paramMap.get("parentReports")).booleanValue();
		Long orgNodeLevel = (Long) paramMap.get("orgNodeLevel");
		paramMap.clear();
		paramMap.put("parentReports", parentReports);
		paramMap.put("isEduUser", isEduUser);
		paramMap.put("isGrowthUser", isGrowthUser);
		paramMap.put("isSuperUser", isSuperUser);
		paramMap.put("orgNodeLevel", orgNodeLevel);
		String userId = loggedinUserTO.getUserId();
		logger.log(IAppLogger.INFO, "userId = " + userId);
		paramMap.put("userId", userId);
		return reportDAO.getAssessments(paramMap);
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
	 * @see com.ctb.prism.report.business.IReportBusiness#populateStudentTableGD(com.ctb.prism.report.transferobject.GroupDownloadTO)
	 */
	public List<GroupDownloadStudentTO> populateStudentTableGD(GroupDownloadTO to) {
		return reportDAO.populateStudentTableGD(to);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#getGDFilePaths(com.ctb.prism.report.transferobject.GroupDownloadTO)
	 */
	public Map<String, String> getGDFilePaths(GroupDownloadTO to) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#getSystemConfigurationMessage(java.util.Map)
	 */
	public String getSystemConfigurationMessage(Map<String, Object> paramMap) {
		return reportDAO.getSystemConfigurationMessage(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#getProcessDataGD(java.lang.String)
	 */
	public JobTrackingTO getProcessDataGD(String processId) {
		return reportDAO.getProcessDataGD(processId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#getConventionalFileNameGD(java.lang.Long)
	 */
	public String getConventionalFileNameGD(Long orgNodeId) {
		return reportDAO.getConventionalFileNameGD(orgNodeId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#updateJobTracking(com.ctb.prism.report.transferobject.GroupDownloadTO)
	 */
	public int updateJobTracking(GroupDownloadTO to) {
		return reportDAO.updateJobTracking(to);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#getReportMessage(java.util.Map)
	 */
	public String getReportMessage(Map<String, Object> paramMap) {
		return reportDAO.getReportMessage(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#getRequestSummary(java.lang.String)
	 */
	public String getRequestSummary(String requestDetails) {
		return reportDAO.getRequestSummary(requestDetails);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#getAllReportMessages(java.util.Map)
	 */
	public List<ReportMessageTO> getAllReportMessages(Map<String, Object> paramMap) {
		return reportDAO.getAllReportMessages(paramMap);
	}

	public String getStudentFileName(String type, String studentBioId, String custProdId) {
		return reportDAO.getStudentFileName(type, studentBioId, custProdId);
	}

	public int updateJobTrackingStatus(String jobId, String jobStatus, String jobLog) {
		return reportDAO.updateJobTrackingStatus(jobId, jobStatus, jobLog);
	}
}
