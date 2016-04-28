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

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.Item;
import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.core.util.Utils;
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
import com.ctb.prism.webservice.transferobject.ReportActionTO;
import com.ctb.prism.core.Service.ISimpleDBService;

import java.beans.Introspector;
@Component("reportBusiness")
public class MReportBusinessImpl implements IReportBusiness {

	private static final IAppLogger logger = LogFactory.getLoggerInstance(ReportBusinessImpl.class.getName());

	@Autowired
	private IReportDAO reportDAO;

	@Autowired
	private ILoginDAO loginDAO;

	@Autowired
	private IPropertyLookup propertyLookup;

	@Autowired
	private IReportFilterTOFactory reportFilterFactory;
	
	@Autowired 
	private ISimpleDBService simpleDBService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#getFilledReport(net.sf.jasperreports.engine.JasperReport, java.util.Map)
	 */
	public JasperPrint getFilledReport(JasperReport jasperReport, Map<String, Object> parameters) throws Exception {
		if (jasperReport != null 
				&& ( jasperReport.getName() != null && 
						(jasperReport.getName().trim().startsWith("Rescore_review") 
						|| "Invitation Pdf".equals(jasperReport.getName().trim())
						|| "Rescore Request Log".equals(jasperReport.getName().trim())
						|| "Rescore Request Summary".equals(jasperReport.getName().trim())
						|| "Rescore_Request_Confirmation".equals(jasperReport.getName().trim())))) {
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
	@Deprecated
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
	
	/**
	 * Retrieves all keys from AWS Simple DB for given contract
	 * @param contractName
	 * @return
	 */
	public boolean removeCache(String contractName) {
		if(propertyLookup.get("store.cache.key.simpledb").equals("true")) {
			List<Item> cacheKeyList = simpleDBService.getAllItems(Utils.getContractName(contractName));
			return removeCache(cacheKeyList, contractName);
		} else {
			//reportDAO.removeCache(contractName);
			if("inors".equals(contractName)) return reportDAO.removeInorsCache(contractName);
			if("tasc".equals(contractName)) return reportDAO.removeTascCache(contractName);
			if("usmo".equals(contractName)) return reportDAO.removeUsmoCache(contractName);
			return false;
		}
		
	}
	
	/**
	 * Remove cache from ElastiCache and then delete keys from AWS Simple DB
	 * @param contractName
	 * @return
	 */
	public boolean removeCache(List<Item> cacheKeyList, String contractName) {
		/** clear from ElastiCache **/
		for(Item item : cacheKeyList){
			//reportDAO.removeCache(item.getName());
			for(Attribute attr : item.getAttributes()) {
				System.out.println("Removing cache for key : "+ attr.getValue());
				logger.log(IAppLogger.INFO, "removeCache : Removing cache for key - "+ attr.getValue());
				reportDAO.removeCache(attr.getValue());
			}
		}
		logger.log(IAppLogger.INFO, "All Cache Cleared");
		
		/** delete keys from simple-db **/
		for(Item item : cacheKeyList){
			for(Attribute attr : item.getAttributes()) {
				System.out.println("Removing simple-db key : ");
				simpleDBService.deleteItem(contractName, attr.getValue());
			}
		}
		logger.log(IAppLogger.INFO, "All simpledb deleted");
		return true;
	}
	
	
	public void removeConfigurationCache(String contract) {
		//reportDAO.removeConfigurationCache(contract);
		if("inors".equals(contract)) reportDAO.removeInorsConfig(contract);
		if("tasc".equals(contract)) reportDAO.removeTascConfig(contract);
		if("usmo".equals(contract)) reportDAO.removeUsmoConfig(contract);
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
	
  @Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getDefaultFilter') )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getDefaultFilter') )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getDefaultFilter') )")
	} )
	
  public Object getDefaultFilter(Map<String, Object> paramMap) {
		
	logger.log(IAppLogger.INFO, "Enter: ReportBusinessImpl - getDefaultFilter");
		
		@SuppressWarnings("unchecked")
		List<InputControlTO> tos = (List<InputControlTO>)paramMap.get("tos");
		if (tos == null) {
			tos = new ArrayList<InputControlTO>();
		}
		String userName = (String) paramMap.get("userName");
		String customerId = (String)paramMap.get("customerId");	
		String reportUrl = (String) paramMap.get("reportUrl");
		@SuppressWarnings("unchecked")
		Map<String, Object> sessionParams = (Map<String, Object>) paramMap.get("sessionParams");
		String userId = (String) paramMap.get("userId");
		String tenantId = (String)paramMap.get("currentOrg");
		String orgLevel = (String) paramMap.get("orgLevel");
		String orgName = (String)paramMap.get("orgName");
			
		Class<?> clazz = null;
		Object obj = null;
		try {
			clazz = reportFilterFactory.getReportFilterTO();
			obj = clazz.newInstance();
			if(Utils.usernameNeeded(reportUrl)) {
				clazz.getMethod("setLoggedInUserName", String.class).invoke(obj, userName);
				clazz.getMethod("setLoggedInUserId", String.class).invoke(obj, userId);
			}
			clazz.getMethod("setLoggedInUserJasperOrgId", String.class).invoke(obj, tenantId);
			clazz.getMethod("setP_customerid", String.class).invoke(obj, customerId);
			
			clazz.getMethod("setLoggedInUserOrgLevel", String.class).invoke(obj, orgLevel);
			clazz.getMethod("setLoggedInUserOrgName", String.class).invoke(obj, orgName);
			clazz.getMethod("setLoggedInUserCustomerCode", String.class).invoke(obj, customerId);
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Map<String, Object> cascadingValues = new HashMap<String, Object>();
		for (InputControlTO ito : tos) {
			String labelId = ito.getLabelId();
			String query = ito.getQuery();
			//System.out.println("Query for default filter: " +query);
			if (query != null) {
				if(Utils.usernameNeeded(reportUrl)) {
					query = query.replaceAll(IApplicationConstants.LOGGED_IN_USER_ID, CustomStringUtil.appendString("\"", userId, "\""));
					query = query.replaceAll(IApplicationConstants.LOGGED_IN_USERNAME, CustomStringUtil.appendString("\"", userName, "\""));	
				}
				query = query.replaceAll(IApplicationConstants.LOGGED_IN_USER_JASPER_ORG_ID, CustomStringUtil.appendString("\"", tenantId, "\""));
				//query = query.replaceAll(IApplicationConstants.LOGGED_IN_CUSTOMER, customerId);
				query = query.replaceAll(IApplicationConstants.LOGGED_IN_USER_ORG_LEVEL, CustomStringUtil.appendString("\"", orgLevel, "\""));
				query = query.replaceAll(IApplicationConstants.LOGGED_IN_USER_ORG_NAME, CustomStringUtil.appendString("\"", orgName, "\""));
				query = query.replaceAll(IApplicationConstants.LOGGED_IN_USER_CUSTOMER_CODE, CustomStringUtil.appendString("\"", customerId, "\""));

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
								/*Fix for TD 83237 - By Joy
								 * Problem: Next I/P control is not populated properly if 
								 * current I/P control is multi-select*/
								 
								if (matched) {
									if(((String[]) pairs.getValue()).length > 1) {
										query = query.replaceAll(CustomStringUtil.getJasperParameterStringRegx((String) pairs.getKey()), 
												CustomStringUtil.appendString("\"",Utils.arrayToSeparatedString(((String[]) pairs.getValue()),','), "\""));
									} else {
										query = query.replaceAll(CustomStringUtil.getJasperParameterStringRegx((String) pairs.getKey()), 
												CustomStringUtil.appendString("\"",((String[]) pairs.getValue())[0], "\""));
									}
								} else {
									if(((String[]) tempObj.toArray()).length > 1) {
										query = query.replaceAll(CustomStringUtil.getJasperParameterStringRegx((String) pairs.getKey()), 
												CustomStringUtil.appendString("\"",Utils.arrayToSeparatedString(((String[]) tempObj.toArray()),','), "\""));
									} else {
										query = query.replaceAll(CustomStringUtil.getJasperParameterStringRegx((String) pairs.getKey()), 
												CustomStringUtil.appendString("\"",tempObj.get(0).getValue(), "\""));
									}
								}
							}
						} catch (Exception e) {
							
						}
					}
				}
				/*** NEW ***/
				
				// Replace cascading values -- that already fetched from old input controls
				Iterator it = cascadingValues.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pairs = (Map.Entry) it.next();
					query = query.replaceAll(CustomStringUtil.getJasperParameterStringRegx((String) pairs.getKey())	, CustomStringUtil.appendString("\"",(String) pairs.getValue(), "\""));
				}

				//query = query.replaceAll("\\$[P][{]\\w+[}]", "-99");
				// handle special i/p controls
				//query = replaceSpecial(query, clazz, obj);
				//logger.log(IAppLogger.DEBUG, query);
				logger.log(IAppLogger.INFO, query);
				
				//System.out.println("Query for default filter: " +query);
				//System.out.println("--------------------------------------------------------");
				
				List<ObjectValueTO> list = reportDAO.getValuesOfSingleInput(query);
				if(list != null && list.size() > 0) {
					cascadingValues.put(list.get(0).getRowIndentifier(), list.get(0).getValue());
				}
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

	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat(#root.method.name) )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat(#root.method.name) )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat(#root.method.name) )")
	} )
	public List<ObjectValueTO> getValuesOfSingleInput(Map<String, Object> paramMap) throws SystemException {
		String reportUrl = (String) paramMap.get("reportUrl");
		String query = (String) paramMap.get("query");
		String userName = (String) paramMap.get("userName"); 
		String customerId = (String) paramMap.get("customerId");
		String changedObject = (String) paramMap.get("changedObject");
		String changedValue = (String) paramMap.get("changedValue");
		@SuppressWarnings("unchecked")
		Map<String, String> replacableParams = (Map<String, String>) paramMap.get("replacableParams");
		Object obj = paramMap.get("obj");
		String userId = (String) paramMap.get("userId");
		return getValuesOfSingleInput(query, userName, customerId, changedObject, changedValue, replacableParams, obj, userId, reportUrl);
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
	public List<ObjectValueTO> getValuesOfSingleInput(String query, String userName, String customerId, String changedObject, String changedValue, 
			Map<String, String> replacableParams, Object obj, String userId, String reportUrl)
			throws SystemException {
		if (query == null)
			return null;

		try {
			Class<?> c = reportFilterFactory.getReportFilterTO();
			// replace all params
			//String tenantId = reportDAO.getTenantId(userName);
			if(Utils.usernameNeeded(reportUrl)) {
				query = query.replaceAll(IApplicationConstants.LOGGED_IN_USER_ID, userId);
				query = query.replaceAll(IApplicationConstants.LOGGED_IN_USERNAME, CustomStringUtil.appendString("'", userName, "'"));
			}
			String tenantId = replacableParams.get(IApplicationConstants.CURRORG);
			//query = query.replaceAll(IApplicationConstants.LOGGED_IN_USER_ID, userId);
			query = query.replaceAll(IApplicationConstants.LOGGED_IN_USER_JASPER_ORG_ID, tenantId);
			//query = query.replaceAll(IApplicationConstants.LOGGED_IN_USERNAME, CustomStringUtil.appendString("'", userName, "'"));
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
							if("$P!{p_Start_Test_Date}".equals(pairs.getKey()) || "$P!{p_End_Test_Date}".equals(pairs.getKey())) {
								query = query.replace((String) pairs.getKey(), (String) pairs.getValue());
							} else {
								query = query.replace((String) pairs.getKey(), 
										CustomStringUtil.appendString("'", (String) pairs.getValue(), "'"));
							}
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
			
			try {
				List<ObjectValueTO> menuList = reportDAO.getAllMenus();
				allReports.get(0).setMenuList(menuList);
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
		/*boolean isSuperUser = false;
		boolean isGrowthUser = false;
		boolean isEduUser = false;*/
		StringBuilder roles =new StringBuilder();
		//UserTO loggedinUserTO = (UserTO) paramMap.get("loggedinUserTO");
		//List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		//authList = loggedinUserTO.getRoles();
		@SuppressWarnings("unchecked")
		List<GrantedAuthority> authList= (List<GrantedAuthority>)paramMap.get("roles");
		Long defaultCustProdId = (Long) paramMap.get("defaultCustProdId");
/*		for (int i = 0; i < authList.size(); i++) {
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
		Boolean parentReports = ((Boolean) paramMap.get("parentReports")).booleanValue();*/
		Long orgNodeLevel = (Long) paramMap.get("orgNodeLevel");
		//paramMap.clear();
		Map<String, Object> menuMap = new HashMap<String, Object>();
		
		menuMap.put("orgNodeLevel", orgNodeLevel);
	/*	paramMap.put("parentReports", parentReports);
		paramMap.put("isEduUser", isEduUser);
		paramMap.put("isGrowthUser", isGrowthUser);
		paramMap.put("isSuperUser", isSuperUser);
		String userId = loggedinUserTO.getUserId();
		logger.log(IAppLogger.INFO, "userId = " + userId);*/
		for (int i = 0; i < authList.size(); i++) {
			roles.append(authList.get(i).getAuthority()).append(",");
		}
		roles.replace(roles.lastIndexOf(","), roles.lastIndexOf(",")+1, "");
		logger.log(IAppLogger.INFO, "Roles = " + roles.toString());
		menuMap.put("roles", roles.toString());
		//paramMap.put("custProdId", loggedinUserTO.getDefultCustProdId());
		menuMap.put("custProdId", defaultCustProdId);
		menuMap.put("database",paramMap.get("database"));
		menuMap.put("contractName",paramMap.get("contractName"));
		menuMap.put("customerCode",paramMap.get("customerCode"));
		return reportDAO.getAssessments(menuMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.business.IReportBusiness#deleteReport(java.lang.String)
	 */
	public boolean deleteReport(Map<String, Object> paramMap) throws SystemException {
		return reportDAO.deleteReport(paramMap);
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

	/* (non-Javadoc)
	 * @see com.ctb.prism.report.business.IReportBusiness#getScheduledGroupFiles(java.util.Map)
	 */
	public Map<Long, String> getScheduledGroupFiles(Map<String, Object> paramMap) throws Exception {
		return reportDAO.getScheduledGroupFiles(paramMap);
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.report.business.IReportBusiness#updateScheduledGroupFiles(java.util.Map)
	 */
	public void updateScheduledGroupFiles(Map<String, Object> paramMap) throws Exception{
		reportDAO.updateScheduledGroupFiles(paramMap);
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
	public JobTrackingTO getProcessDataGD(String processId, String contractName) {
		return reportDAO.getProcessDataGD(processId, contractName);
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
	public String getRequestSummary(String requestDetails, String contractName) {
		return reportDAO.getRequestSummary(requestDetails, contractName);
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

	public int updateJobTrackingStatus(String contractName, String jobId, String jobStatus, String jobLog) {
		return reportDAO.updateJobTrackingStatus(contractName, jobId, jobStatus, jobLog);
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.report.business.IReportBusiness#getProductsForEditActions(java.util.Map)
	 */
	public List<ReportActionTO> getProductsForEditActions(Map<String, Object> paramMap) {
		return reportDAO.getProductsForEditActions(paramMap);
	}
	
	/* (non-Javadoc)
	 * @see com.ctb.prism.report.business.IReportBusiness#getActionsForEditActions(java.util.Map)
	 */
	public List<ReportActionTO> getActionsForEditActions(Map<String, Object> paramMap) {
		return reportDAO.getActionsForEditActions(paramMap);
	}
	
	/* (non-Javadoc)
	 * @see com.ctb.prism.report.business.IReportBusiness#getActionAccess(java.util.Map)
	 */
	public List<ReportActionTO> getActionAccess(Map<String, Object> paramMap) {
		return reportDAO.getActionAccess(paramMap);
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.report.business.IReportBusiness#updateDataForActions(java.util.Map)
	 */
	public String updateDataForActions(Map<String, Object> paramMap) {
		return reportDAO.updateDataForActions(paramMap);
	}
	
	public void updateJobTrackingTable(String jobId,String filePath)
	{
		reportDAO.updateJobTrackingTable(jobId,filePath);	
	}

	/**
	 * Returns the default filter values for a report
	 * @param tos List of input control details
	 * @param userName logged in user name
	 * @param assessmentId 
	 * @param combAssessmentId
	 * @param reportUrl
	 */
	//@Cacheable(cacheName = "tascdefaultInputControls")
	public Object getDefaultFilterTasc(List<InputControlTO> tos, String userName, String assessmentId, String combAssessmentId, String reportUrl )
	{
		logger.log(IAppLogger.INFO, "Enter: ReportBusinessImpl - getDefaultFilter");
		Class<?> clazz = null;
		Object obj = null;
		String tenantId = null;
		//ReportFilterTO to = new ReportFilterTO();
		tenantId = reportDAO.getTenantId(userName);
		try {
			clazz = reportFilterFactory.getReportFilterTO();
			obj = clazz.newInstance();
			clazz.getMethod("setLoggedInUserJasperOrgId", String.class).invoke(obj, tenantId);
			clazz.getMethod("setLoggedInUserName", String.class).invoke(obj, userName);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		for (InputControlTO ito : tos)
		{
			String labelId = ito.getLabelId();
			String query = ito.getQuery();
			if ( query != null )
			{
				query = query.replaceAll(IApplicationConstants.LOGGED_IN_USER_JASPER_ORG_ID, tenantId);
				query = query.replaceAll(IApplicationConstants.LOGGED_IN_USERNAME, CustomStringUtil.appendString("'",userName,"'"));
				query = query.replaceAll("\\$[P][{]\\w+[}]", "-99");
				// handle special i/p controls
				query = replaceSpecial(query, clazz, obj);
				logger.log(IAppLogger.DEBUG, query);
				List<ObjectValueTO> list = reportDAO.getValuesOfSingleInput(query);
				/*if(list != null && list.size() == 0) {
					// patch for form level
					if(IApplicationConstants.IC_FORM_LEVEL.equals(ito.getLabel())) {
						ObjectValueTO formObj = new ObjectValueTO();
						formObj.setName("");
						formObj.setValue("0_0-0");
						list.add(formObj);
					}
				}*/
				String methodName = null;
				try {
					methodName = CustomStringUtil.appendString("set",labelId.substring(0, 1).toUpperCase(),labelId.substring(1));
					Method setterMethod = clazz.getMethod(methodName, new Class[]{List.class});
					setterMethod.invoke(obj, list);
				} catch (Exception e) {
					logger.log(IAppLogger.WARN, 
							CustomStringUtil.appendString("Could not invoke method ", methodName, "on ReportFilterTO"), e);
				}
			}
		}
		
		logger.log(IAppLogger.INFO, "Exit: ReportBusinessImpl - getDefaultFilter");
		return obj;
	}

	/**
	 * Fetch all values of a input after replacing all required parameters
	 * @param query
	 * @param userName
	 * @return
	 * @throws SystemException 
	 * @throws IllegalArgumentException 
	 */
	public List<ObjectValueTO> getValuesOfSingleInputTasc(String query, String userName, String changedObject, 
			String changedValue, Map<String, String> replacableParams, Object obj, boolean bulkDownload) throws SystemException {
		if(query == null) return null;
		
		try {
			Class<?> c = reportFilterFactory.getReportFilterTO();
			// replace all params
			String tenantId = reportDAO.getTenantId(userName);
			query = query.replaceAll(IApplicationConstants.LOGGED_IN_USER_JASPER_ORG_ID, tenantId);
			query = query.replaceAll(IApplicationConstants.LOGGED_IN_USERNAME, CustomStringUtil.appendString("'",userName,"'"));
			query = query.replace(CustomStringUtil.getJasperParameterString(changedObject), 
					CustomStringUtil.appendString("'", changedValue, "'"));
			
			// replace all required params
			if(query != null && query.indexOf(IApplicationConstants.JASPER_PARAM_INITIAL) != -1) {
				@SuppressWarnings("rawtypes")
				Iterator it = replacableParams.entrySet().iterator();
				while (it.hasNext()) {
				    try {
						@SuppressWarnings("rawtypes")
						Map.Entry pairs = (Map.Entry)it.next();
						if(pairs.getValue() != null && pairs.getValue() instanceof String) {
							if("$P!{p_Start_Test_Date}".equals(pairs.getKey()) || "$P!{p_End_Test_Date}".equals(pairs.getKey())) {
								query = query.replace((String) pairs.getKey(), (String) pairs.getValue());
							} else {
								query = query.replace((String) pairs.getKey(), 
										CustomStringUtil.appendString("'", (String) pairs.getValue(), "'"));
							}
							if(query.indexOf(IApplicationConstants.JASPER_PARAM_INITIAL) == -1) {
								break;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			// handle special i/p controls
			if(changedValue != null) {
				if(bulkDownload) {
					String[] changedObjectArr = changedObject.split(",");
					for(int i=0; i<changedObjectArr.length; i++) {
						String[] chengedValueArr = (replacableParams.get(CustomStringUtil.getJasperParameterString(changedObjectArr[i])) != null)? 
								replacableParams.get(CustomStringUtil.getJasperParameterString(changedObjectArr[i])).split(",") : null;
						if(chengedValueArr != null && chengedValueArr.length > 0) {
							ArrayList<ObjectValueTO> listOfValues = new ArrayList<ObjectValueTO>();
							for(String val : chengedValueArr) {
								ObjectValueTO objectValueTo = new ObjectValueTO();
								objectValueTo.setValue(val.trim());
								listOfValues.add(objectValueTo);
							}
							String coll = CustomStringUtil.capitalizeFirstCharacter(changedObjectArr[i]);
							String methodName = CustomStringUtil.appendString("set", coll);
							Method m = c.getMethod(methodName, new Class[]{List.class});
							m.invoke(obj, listOfValues);
							query = replaceSpecial(query, c, obj, coll);
						}
					}
				} else {
					String[] chengedValueArr = changedValue.split(",");
					if(chengedValueArr != null && chengedValueArr.length > 0) {
						ArrayList<ObjectValueTO> listOfValues = new ArrayList<ObjectValueTO>();
						for(String val : chengedValueArr) {
							ObjectValueTO objectValueTo = new ObjectValueTO();
							objectValueTo.setValue(val);
							listOfValues.add(objectValueTo);
						}
						String coll = CustomStringUtil.capitalizeFirstCharacter(changedObject);
						String methodName = CustomStringUtil.appendString("set", coll);
						Method m = c.getMethod(methodName, new Class[]{List.class});
						m.invoke(obj, listOfValues);
						query = replaceSpecial(query, c, obj);
					}
				}
			}
			
			// replace others with null - not required --
			if(query != null && query.indexOf(IApplicationConstants.JASPER_PARAM_INITIAL) != -1) {
				query = query.replaceAll("\\$[P][{]\\w+[}]", "null");
			}
			
			logger.log(IAppLogger.INFO, "query: " + query);
			
			// fetch data
			if(bulkDownload) return reportDAO.getStudentList(query);
			else return reportDAO.getValuesOfSingleInput(query);
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}
	
	private static String replaceSpecial(String inQuery, Class<?> clazz, Object obj, String colm) {
		String tempQuery = inQuery;
		String trimPart = "";
		String replacedQuery = "";
		try {
			if(tempQuery.indexOf("$X{IN") != -1) {
				String partQ = "";
				if(tempQuery.indexOf(Introspector.decapitalize(colm)) != -1) {
					partQ = tempQuery.substring(tempQuery.indexOf(Introspector.decapitalize(colm)) - 33);
				} else {
					//partQ = tempQuery.substring(tempQuery.indexOf(Introspector.decapitalize(colm)));
					partQ = tempQuery;
				}
				trimPart = partQ.substring(partQ.indexOf("$X{IN"), partQ.indexOf("}")+1);
				String part = partQ.substring(partQ.indexOf("$X{IN")+3, partQ.indexOf("}"));
				String[] parts = part.split(",");
				if(parts.length == 3) {
					String coll = CustomStringUtil.capitalizeFirstCharacter(parts[2].trim());
					if(coll.equals(colm) ) {
						Method m = clazz.getMethod( CustomStringUtil.appendString("get", coll) );
						ArrayList<ObjectValueTO> listOfValues = (ArrayList<ObjectValueTO>) m.invoke(obj);
						StringBuilder builder = new StringBuilder();
						builder.append(parts[1]).append(" ").append(parts[0]).append(" ");
						boolean isFirst = true;
						builder.append(" (");
						if(listOfValues != null && listOfValues.size() == 0) {
							builder.append("-99");
						} else {
							for(ObjectValueTO objectValue : listOfValues) {
								if(!isFirst) builder.append(",");
								isFirst = false;
								builder.append("'").append(objectValue.getValue()).append("'");
							}
						}
						builder.append(") ");
						
						replacedQuery = tempQuery.replace(trimPart, builder.toString());
						
						if(tempQuery.indexOf(Introspector.decapitalize(colm)) != -1) {
							replacedQuery = replaceSpecial(replacedQuery, clazz, obj, colm);
						}
					}
						
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			return inQuery;
		} 
		return replacedQuery.length() == 0? inQuery : replacedQuery;
	}

	public Map<String, String> getGenericSystemConfigurationMessages(Map<String, Object> paramMap) {
		return reportDAO.getGenericSystemConfigurationMessages(paramMap);
	}
}
