package com.ctb.prism.report.business;

import java.lang.reflect.Method;
import java.util.ArrayList;
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
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.report.dao.IReportDAO;
import com.ctb.prism.report.transferobject.AssessmentTO;
import com.ctb.prism.report.transferobject.InputControlTO;
import com.ctb.prism.report.transferobject.ObjectValueTO;
import com.ctb.prism.report.transferobject.ReportFilterTO;
import com.ctb.prism.report.transferobject.ReportTO;

@Component("reportBusiness")
public class ReportBusinessImpl implements IReportBusiness {
	
	private static final IAppLogger logger = 
		LogFactory.getLoggerInstance(ReportBusinessImpl.class.getName());

	@Autowired
	private IReportDAO reportDAO;
	
	public JasperPrint getFilledReport(JasperReport jasperReport, Map<String, Object> parameters) throws Exception {
		if(jasperReport != null && "Invitation_Pdf".equals(jasperReport.getName().trim()) )  {
			return reportDAO.getFilledReportNoCache(jasperReport, parameters);
		} else {
			return reportDAO.getFilledReport(jasperReport, parameters);
		}
	}
	
	public void removeReportCache() {
		reportDAO.removeReportCache();
	}
	public void removeCache() {
		reportDAO.removeCache();
	}
	public JasperReport getReportJasperObject(String reportPath) {
		return reportDAO.getReportJasperObject(reportPath);
	}
	public JasperReport getReportJasperObjectForName(String reportname) {
		return reportDAO.getReportJasperObjectForName(reportname);
	}
	public List<ReportTO> getReportJasperObjectList(final String reportPath) throws JRException {
		return reportDAO.getReportJasperObjectList(reportPath);
	}

	public List<InputControlTO> getInputControlDetails(String reportPath){
		return reportDAO.getInputControlDetails(reportPath);
	}
	
	/**
	 * Returns the default filter values for a report
	 * @param tos List of input control details
	 * @param userName logged in user name
	 * @param assessmentId 
	 * @param combAssessmentId
	 * @param reportUrl
	 */
	//@Cacheable(cacheName = "defaultInputControls")
	public ReportFilterTO getDefaultFilter(List<InputControlTO> tos, String userName, String assessmentId, String combAssessmentId, String reportUrl )
	{
		logger.log(IAppLogger.INFO, "Enter: ReportBusinessImpl - getDefaultFilter");
		
		ReportFilterTO to = new ReportFilterTO();
		String tenantId = reportDAO.getTenantId(userName);
		to.setLoggedInUserJasperOrgId(tenantId);
		
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
				query = replaceSpecial(query, to);
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
					methodName = "set"+labelId.substring(0, 1).toUpperCase()+labelId.substring(1);
					Method setterMethod = ReportFilterTO.class.getMethod(methodName, new Class[]{List.class});
					setterMethod.invoke(to, list);
				} catch (Exception e) {
					logger.log(IAppLogger.WARN, 
							CustomStringUtil.appendString("Could not invoke method ", methodName, "on ReportFilterTO"), e);
				}
			}
		}
		
		logger.log(IAppLogger.INFO, "Exit: ReportBusinessImpl - getDefaultFilter");
		return to;
	}
	
	/**
	 * update in-statement of queries
	 * @param inQuery
	 * @param to
	 * @return
	 */
	private String replaceSpecial(String inQuery, ReportFilterTO to) {
		String tempQuery = inQuery;
		String trimPart = "";
		String replacedQuery = "";
		try {
			if(tempQuery.indexOf("$X{IN") != -1) {
				trimPart = tempQuery.substring(tempQuery.indexOf("$X{IN"), tempQuery.indexOf("}")+1);
				String part = tempQuery.substring(tempQuery.indexOf("$X{IN")+3, tempQuery.indexOf("}"));
				String[] parts = part.split(",");
				if(parts.length == 3) {
					String coll = CustomStringUtil.capitalizeFirstCharacter(parts[2].trim());
					Method m = ReportFilterTO.class.getMethod( CustomStringUtil.appendString("get", coll) );
					ArrayList<ObjectValueTO> listOfValues = (ArrayList<ObjectValueTO>) m.invoke(to);
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
					if(replacedQuery.indexOf("$X{IN") != -1) {
						replacedQuery = replaceSpecial(replacedQuery, to);
					}
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			return inQuery;
		} 
		return replacedQuery.length() == 0? inQuery : replacedQuery;
	}
	
	/**
	 * Fetch all values of a input after replacing all required parameters
	 * @param query
	 * @param userName
	 * @return
	 * @throws SystemException 
	 * @throws IllegalArgumentException 
	 */
	public List<ObjectValueTO> getValuesOfSingleInput(String query, String userName, String changedObject, 
			String changedValue, Map<String, String> replacableParams, ReportFilterTO reportFilterTO) throws SystemException {
		if(query == null) return null;
		
		try {
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
							query = query.replace((String) pairs.getKey(), 
									CustomStringUtil.appendString("'", (String) pairs.getValue(), "'"));
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
					Method m = ReportFilterTO.class.getMethod(methodName, new Class[]{List.class});
					m.invoke(reportFilterTO, listOfValues);
					query = replaceSpecial(query, reportFilterTO);
				}
			}
			
			// replace others with null - not required --
			if(query != null && query.indexOf(IApplicationConstants.JASPER_PARAM_INITIAL) != -1) {
				query = query.replaceAll("\\$[P][{]\\w+[}]", "null");
			}
			
			logger.log(IAppLogger.DEBUG, query);
			
			// fetch data
			return reportDAO.getValuesOfSingleInput(query);
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}
	
	public List<ObjectValueTO> getValuesOfSingleInput(String query) {
		return reportDAO.getValuesOfSingleInput(query);
	}
	
	public List<ReportTO> getAllReportList() {
		return reportDAO.getAllReportList();
	}

	public boolean updateReport(String reportId, String reportName,
			String reportUrl, String isEnabled, String[] roles) {
		return reportDAO.updateReport(reportId, reportName, reportUrl, isEnabled, roles);
	}
	
	public List<AssessmentTO> getAssessments(boolean parentReports) {
		return reportDAO.getAssessments(parentReports);
	}
	
	public boolean deleteReport(String reportId) throws SystemException {
		return reportDAO.deleteReport(reportId);
	}
	public ReportTO addNewDashboard(String reportName, String reportDescription, String reportType,
			String reportUri, String assessmentType, String reportStatus, String[] userRoles)throws Exception{
		return reportDAO.addNewDashboard(reportName,reportDescription,reportType,reportUri,assessmentType,reportStatus,userRoles);
	}
}
