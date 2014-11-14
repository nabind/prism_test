package com.ctb.prism.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ctb.prism.admin.service.IAdminService;
import com.ctb.prism.admin.transferobject.OrgTO;
import com.ctb.prism.admin.transferobject.OrgTreeTO;
import com.ctb.prism.core.Service.IRepositoryService;
import com.ctb.prism.core.Service.IUsabilityService;
import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.constant.IApplicationConstants.EXTRACT_CATEGORY;
import com.ctb.prism.core.constant.IApplicationConstants.EXTRACT_FILETYPE;
import com.ctb.prism.core.constant.IApplicationConstants.JOB_STATUS;
import com.ctb.prism.core.constant.IApplicationConstants.REQUEST_TYPE;
import com.ctb.prism.core.constant.IEmailConstants;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.core.jms.JmsMessageProducer;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.core.util.EmailSender;
import com.ctb.prism.core.util.FileUtil;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.inors.service.IInorsService;
import com.ctb.prism.inors.transferobject.BulkDownloadTO;
import com.ctb.prism.inors.util.InorsDownloadUtil;
import com.ctb.prism.inors.util.PdfGenerator;
import com.ctb.prism.login.Service.ILoginService;
import com.ctb.prism.login.security.filter.RESTAuthenticationFilter;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.report.service.IReportService;
import com.ctb.prism.report.transferobject.GroupDownloadStudentTO;
import com.ctb.prism.report.transferobject.GroupDownloadTO;
import com.ctb.prism.report.transferobject.IReportFilterTOFactory;
import com.ctb.prism.report.transferobject.InputControlTO;
import com.ctb.prism.report.transferobject.JobTrackingTO;
import com.ctb.prism.report.transferobject.ObjectValueTO;
import com.ctb.prism.report.transferobject.ReportMessageTO;
import com.ctb.prism.report.transferobject.ReportTO;
import com.ctb.prism.web.util.JsonUtil;
/**
 * @author TCS
 * 
 */
// @PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Controller
public class InorsController {
	private static final IAppLogger logger = LogFactory.getLoggerInstance(InorsController.class.getName());

	@Autowired private IInorsService inorsService;

	@Autowired	private IAdminService adminService;

	@Autowired	private IReportService reportService;
	
	@Autowired private IUsabilityService usabilityService;

	@Autowired	private IReportFilterTOFactory reportFilterFactory;

	@Autowired	private ReportController reportController;

	@Autowired	private IPropertyLookup propertyLookup;

	@Autowired	private JmsMessageProducer messageProducer;
	
	@Autowired	private ILoginService loginService;
	
	@Autowired private IRepositoryService repositoryService;

	/**
	 * For Group Download Listing.
	 * 
	 * @author Arunava Datta
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = "/groupDownloadFiles", method = RequestMethod.GET)
	public ModelAndView groupDownloadFiles(HttpServletRequest request, HttpServletResponse response) throws SystemException {
		logger.log(IAppLogger.INFO, "Enter: groupDownloadFiles()");
		ModelAndView modelAndView = new ModelAndView("inors/groupDownloadFiles");
		String grpList = "";
		UserTO loggedinUserTO = (UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loggedinUserTO", loggedinUserTO);
		paramMap.put("gdfExpiryTime", propertyLookup.get("gdfExpiryTime"));
		List<JobTrackingTO> groupList = reportService.getAllGroupDownloadFiles(paramMap);
		if (groupList != null && !groupList.isEmpty()) {
			for (JobTrackingTO to : groupList) {
				String displayFilename = to.getRequestFilename();
				if (IApplicationConstants.REQUEST_TYPE.GDF.toString().equals(to.getRequestType())) {
					if (displayFilename != null && !displayFilename.isEmpty()) {
						int length = displayFilename.length();
						int u = displayFilename.lastIndexOf("_");
						int e = displayFilename.lastIndexOf(".");
						logger.log(IAppLogger.INFO, "length=" + length + ", u=" + u + ", e=" + e);
						if (u > -1 && e > -1 && length > u && length > e && e > u) {
							String prefix = displayFilename.substring(0, u);
							String suffix = displayFilename.substring(e);
							displayFilename = prefix + suffix;
							logger.log(IAppLogger.INFO, "displayFilename=" + displayFilename);
						}
					}
				}
				to.setDisplayFilename(displayFilename);
			}
			modelAndView.addObject("groupList", groupList);
			grpList = JsonUtil.convertToJsonAdmin(groupList);
		}

		logger.log(IAppLogger.INFO, grpList);
		logger.log(IAppLogger.INFO, "Exit: groupDownloadFiles()");
		return modelAndView;
	}

	/**
	 * For Group Download deleting.
	 * 
	 * @author Arunava Datta
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "/deleteGroupDownloadFiles", method = RequestMethod.GET)
	public ModelAndView deleteGroupDownloadFiles(HttpServletRequest req, HttpServletResponse res) {
		logger.log(IAppLogger.INFO, "Enter: deleteGroupDownloadFiles()");
		try {
			String Id = (String) req.getParameter("jobId");
			logger.log(IAppLogger.INFO, "Id of the file to delete is ........................." + Id);
			boolean isDeleted = reportService.deleteGroupFiles(Id);
			res.setContentType("text/plain");
			String status = "Fail";
			if (isDeleted) {
				status = "Success";
			}
			res.getWriter().write("{\"status\":\"" + status + "\"}");
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error deleting Group File", e);
			e.printStackTrace();
		}
		logger.log(IAppLogger.INFO, "Exit: deleteGroupDownloadFiles()");
		return null;
	}

	/**
	 * For Group Download file downloading.
	 * 
	 * @author Arunava Datta
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = "/downloadGroupDownloadFiles", method = RequestMethod.GET)
	public void downloadGroupDownloadFiles(HttpServletRequest request, HttpServletResponse response) {
		logger.log(IAppLogger.INFO, "Enter: Controller - downloadGroupDownloadFiles");
		String Id = (String) request.getParameter("jobId");
		String filePath = (String) request.getParameter("filePath");
		String fileName = (String) request.getParameter("fileName");
		String orgLevel = (String) request.getParameter("orgLevel");
		String requestType = (String) request.getParameter("requestType");
		
		try {
			if(Utils.getContractName().equals(IApplicationConstants.CONTRACT_NAME.inors)) {
				FileUtil.browserDownload(response, filePath);
			} else if(Utils.getContractName().equals(IApplicationConstants.CONTRACT_NAME.tasc)){
				updateFileExt(Id,filePath,fileName,orgLevel,requestType,response);
			} else {
				//For new project
			}
			
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "downloadGroupDownloadFiles - ", e);
			e.printStackTrace();
		}
		logger.log(IAppLogger.INFO, "Exit: Controller - downloadGroupDownloadFiles");
	}
	

	/**
	 * For Group Download file validation.
	 * 
	 * @author Arunava Datta
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/checkFileAvailability", method = RequestMethod.GET)
	public ModelAndView checkFileAvailability(HttpServletRequest req, HttpServletResponse res) throws IOException {
		logger.log(IAppLogger.INFO, "Enter: checkFileAvailability()");
		String filePath = (String) req.getParameter("filePath");
		File file = new File(filePath);
		String status = "Fail";
		try {
			byte[] data = FileCopyUtils.copyToByteArray(file);
			status = "Success";
			res.setContentType("text/plain");
			res.getWriter().write("{\"status\":\"" + status + "\"}");
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error downloading Group File", e);
			e.printStackTrace();
			res.setContentType("text/plain");
			res.getWriter().write("{\"status\":\"" + status + "\"}");
		}
		logger.log(IAppLogger.INFO, "Exit: checkFileAvailability()");
		return null;
	}
	
	@RequestMapping(value = "/getStudentFileName", method = RequestMethod.GET)
	public ModelAndView getStudentFileName(HttpServletRequest req, HttpServletResponse res) throws IOException {
		logger.log(IAppLogger.INFO, "Enter: getStudentFileName()");
		String studentBioId = (String) req.getParameter("studentBioId");
		String custProdId = (String) req.getParameter("custProdId");
		String type = (String) req.getParameter("type");
		String fileName = "";
		try {
			fileName = reportService.getStudentFileName(type, studentBioId, custProdId);
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error downloading Group File", e);
			e.printStackTrace();
		}
		res.setContentType("text/plain");
		res.getWriter().write("{\"fileName\":\"" + fileName + "\"}");
		logger.log(IAppLogger.INFO, "Exit: getStudentFileName()");
		return null;
	}

	/**
	 * For getting the required data for a particular request while viewing. Arunava Datta
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "/getRequestDetailViewData", method = RequestMethod.GET)
	public ModelAndView getRequestDetailViewData(HttpServletRequest req, HttpServletResponse res) {
		logger.log(IAppLogger.INFO, "Enter: getRequestDetailViewData()");
		try {
			String jobId = (String) req.getParameter("jobId");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("jobId", jobId);
			ModelAndView modelAndView = new ModelAndView("report/groupDownloadFiles");
			List<JobTrackingTO> requestList = reportService.getRequestDetail(paramMap);
			String replist = JsonUtil.convertToJsonAdmin(requestList);
			modelAndView.addObject("requestView", requestList);
			String requestViewJsonString = JsonUtil.convertToJsonAdmin(requestList);
			logger.log(IAppLogger.INFO, requestViewJsonString);
			res.setContentType("application/json");
			res.getWriter().write(requestViewJsonString);
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
			e.printStackTrace();
		} finally {
			logger.log(IAppLogger.INFO, "Exit: getRequestDetailViewData()");
		}
		return null;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = "/downloadCandicateReportPage", method = RequestMethod.GET)
	public ModelAndView bulkCandidateReportDownload(HttpServletRequest request, HttpServletResponse response) throws SystemException {
		logger.log(IAppLogger.INFO, "Enter: bulkCandidateReportDownload()");
		ModelAndView modelAndView = new ModelAndView("inors/bulkCandidateReport");
		modelAndView.addObject("reportUrl", "/public/TASC/Reports/TASC_Org_Hier/Candidate_Report_files");
		logger.log(IAppLogger.INFO, "Exit: bulkCandidateReportDownload()");
		return modelAndView;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/downloadCandicateReport", method = RequestMethod.GET)
	public @ResponseBody
	String downloadCandicateReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String status = "Error";
		try {
			String startDate = request.getParameter("p_Start_Date");
			String endDate = request.getParameter("p_End_Date");
			String reportUrl = request.getParameter("reportUrl");
			String currentUser = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSER);
			String currentOrg = (String) request.getSession().getAttribute(IApplicationConstants.CURRORG);
			String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
			String currentUserId = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSERID);
			// String currentOrgLevel = (String) request.getSession().getAttribute(IApplicationConstants.CURRORGLVL);
			String docName = CustomStringUtil.appendString(currentUser, "_" ,Utils.getDateTime(), "_Querysheet.pdf");

			List<InputControlTO> allInputControls = reportController.getInputControlList(reportUrl);

			// get compiled jasper report
			JasperReport jasperReport = null;
			boolean mainReportPresent = false;

			// fetch report list 
			List<ReportTO> reportList = reportController.getCompliledJrxmlList(reportUrl);
			
			if (reportList != null && !reportList.isEmpty()) {
				for (ReportTO reportTo : reportList) {
					if (reportTo.isMainReport()) {
						jasperReport = reportTo.getCompiledReport();
						mainReportPresent = true;
						break;
					}
				}
				if (!mainReportPresent) {
					jasperReport = reportList.get(0).getCompiledReport();
				}
			}
			String assessmentId = request.getParameter("assessmentId");
			Object reportFilterTO = reportService.getDefaultFilterTasc(allInputControls, currentUser, assessmentId, "", reportUrl);
			Map<String, Object> parameters = reportController.getReportParametersFromRequest(
					request, allInputControls, reportFilterFactory.getReportFilterTO(), currentOrg, null);
			// reportController.getReportParameter(allInputControls, reportFilterTO, false, request);

			String mainQuery = jasperReport.getDatasets()[0].getQuery().getText(); //jasperReport.getQuery().getText();

			// replace all parameters with jasper parameter string
			Map<String, String> replacableParams = new HashMap<String, String>();
			Iterator it = parameters.entrySet().iterator();
			try {
				while (it.hasNext()) {
					Map.Entry pairs = (Map.Entry) it.next();
					if (pairs.getValue() != null && pairs.getValue() instanceof String) {
						replacableParams.put(CustomStringUtil.getJasperParameterString((String) pairs.getKey()), (String) pairs.getValue());
					} else if (pairs.getValue() != null && pairs.getValue() instanceof List) {
						String val = pairs.getValue().toString();
						val = val.substring(1, val.length() - 1);
						replacableParams.put(CustomStringUtil.getJasperParameterString((String) pairs.getKey()), val);
					}
				}
			} catch (Exception e) {
				logger.log(IAppLogger.ERROR, "Some error occuered getting cascading values.", e);
				e.printStackTrace();
			}

			it = request.getParameterMap().entrySet().iterator();
			while (it.hasNext()) {
				try {
					@SuppressWarnings("rawtypes")
					Map.Entry pairs = (Map.Entry) it.next();
					if (pairs.getValue() != null && pairs.getValue() instanceof String) {
						if (!replacableParams.containsKey(pairs.getKey())) {
							replacableParams.put(CustomStringUtil.getJasperParameterString((String) pairs.getKey()), (String) pairs.getValue());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			String p_Start_Test_Date = request.getParameter("p_Start_Test_Date");
			String p_End_Test_Date = request.getParameter("p_End_Test_Date");
			replacableParams.put("$P!{p_Start_Test_Date}", ((p_Start_Test_Date != null) && (!p_Start_Test_Date.trim().isEmpty())) ? p_Start_Test_Date : "");
			replacableParams.put("$P!{p_End_Test_Date}", ((p_End_Test_Date != null) && (!p_End_Test_Date.trim().isEmpty())) ? p_End_Test_Date : "");
			replacableParams.put("$P{p_Start_Test_Date_1}", ((p_Start_Test_Date != null) && (!p_Start_Test_Date.trim().isEmpty())) ? p_Start_Test_Date : "-1");
			replacableParams.put("$P{p_End_Test_Date_1}", ((p_End_Test_Date != null) && (!p_End_Test_Date.trim().isEmpty())) ? p_End_Test_Date : "-1");
			
			replacableParams.put(CustomStringUtil.getJasperParameterString("p_Grade_Id"), "112"); // TODO : Remove Hardcoding
			replacableParams.put(CustomStringUtil.getJasperParameterString("p_Product_Id"), "1001"); // TODO : Remove Hardcoding
			
			String changedObject = "p_Ethnicities,p_Roster_Subtest_MultiSelect";
			List<ObjectValueTO> allStudents = reportService.getValuesOfSingleInputTasc(mainQuery, currentUser, changedObject, "", replacableParams, reportFilterTO, true);
			
			StringBuilder builder = new StringBuilder();
			int count = 0;
			if(allStudents != null) {
				for(ObjectValueTO obj : allStudents) {
					if(count > 0) builder.append(",");
					// studentId | formId
					//builder.append(obj.getValue()).append("|").append(obj.getClikedOrgId());
					builder.append(obj.getValue());
					count++;
				}
			}
			com.ctb.prism.core.transferobject.JobTrackingTO jobTrackingTO = new com.ctb.prism.core.transferobject.JobTrackingTO(); 
            //Casting Required 
             
            jobTrackingTO.setJobName(docName);
            jobTrackingTO.setUserId((String) request.getSession().getAttribute(
            		IApplicationConstants.CURRUSERID));
            String username = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSER);
            if(username != null && username.indexOf(RESTAuthenticationFilter.RANDOM_STRING) != -1) {
            	// remove random character for SSO users
            	username = username.substring(0, username.indexOf(RESTAuthenticationFilter.RANDOM_STRING));
            }
            jobTrackingTO.setRequestFilename(CustomStringUtil.appendString(
            		username, "_", "Candidate_PDF", "_", Utils.getDateTime())
                    );
            jobTrackingTO.setAdminId(request.getParameter("p_Admin_Name"));
            jobTrackingTO.setExtractCategory(EXTRACT_CATEGORY.PD.toString());     
            jobTrackingTO.setRequestType(REQUEST_TYPE.GDF.toString());  
            jobTrackingTO.setExtractFiletype(EXTRACT_FILETYPE.CR.toString());
            jobTrackingTO.setRequestSummary(CustomStringUtil.appendString(
            		"Download candidate report for ", allStudents.size() +"", " student(s)." //,
            		//"\n\n Request parameters: \n\n", 
            		//replacableParams.toString().replace("$P{", "").replace("}","").replace("{", "").replace("$P!", "").replace("p_", "").replace(",", "\n")
            		));   
            jobTrackingTO.setRequestDetails(builder.toString());
            jobTrackingTO.setJobStatus(JOB_STATUS.SU.toString()); 
            jobTrackingTO.setNumber(allStudents.size()); 
            jobTrackingTO.setAdminId(replacableParams.get("p_Admin_Name")); 
            jobTrackingTO.setCustomerId(customer);
            jobTrackingTO.setOtherRequestparams(CustomStringUtil.appendString(
            		propertyLookup.get("CandidateReportUrl"), ",", request.getParameter("userType"))
            		);
             
			jobTrackingTO = usabilityService.insertIntoJobTracking(jobTrackingTO); 

			// String querysheetFile = PdfGenerator.generateQuerysheetCR(jobTrackingTO, propertyLookup);
			
			logger.log(IAppLogger.INFO, "sending messsage --------------- ");
			messageProducer.sendJobForProcessing(String.valueOf(jobTrackingTO.getJobId()), Utils.getContractName());
			
			if(jobTrackingTO.getJobId() != 0) status = "Success";
			
			response.setContentType("application/json");
			response.getWriter().write("");
			response.getWriter().write( "{\"status\":\""+status+"\"}" );
			
		} catch (Exception ex) {
			logger.log(IAppLogger.ERROR, ex.getMessage(), ex);
			response.getWriter().write( "{\"status\":\""+status+"\"}" );
		}
		
		return null;
	}

	@RequestMapping(value = "/icLetterDownloads", method = RequestMethod.GET)
	public ModelAndView icLetterDownloads(HttpServletRequest request, HttpServletResponse response) {
		logger.log(IAppLogger.INFO, "Enter: icLetterDownloads()");
		request.setAttribute("icDownload", "true");
		logger.log(IAppLogger.INFO, "Exit: icLetterDownloads()");
		return groupDownloadForm(request, response);
	}

	private String getReportMessage(List<ReportMessageTO> reportMessages, String messageType, String messageName) {
		String message = null;
		if ((reportMessages != null) && (!reportMessages.isEmpty())) {
			for (ReportMessageTO reportMessage : reportMessages) {
				if ((messageType.equals(reportMessage.getMessageType())) && (messageName.equals(reportMessage.getMessageName()))) {
					message = reportMessage.getMessage();
					break;
				}
			}
		}
		return message;
	}

	public Map<String, Object> getReportParameters(HttpServletRequest request, String reportUrl) {
		String currentUser = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSER);
		String currentUserId = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSERID);
		String assessmentId = request.getParameter("assessmentId");
		Map<String, Object> inputControls = (Map<String, Object>) request.getSession().getAttribute("_REMEMBER_ME_ALL_");
		List<InputControlTO> allInputControls = reportController.getInputControlList(reportUrl);
		String customerId=(String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);

		// get default parameters for logged-in user
		Object reportFilterTO = reportService.getDefaultFilter(allInputControls, currentUser, customerId, assessmentId, "", 
				reportUrl, inputControls, currentUserId, (String) request.getSession().getAttribute(IApplicationConstants.CURRORG));
		return reportController.getReportParameter(allInputControls, reportFilterTO, false, request, reportUrl);
	}

	/**
	 * Entry method for group download screen. This method takes care of the text box and drop down values.
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/groupDownloadForm", method = RequestMethod.GET)
	public ModelAndView groupDownloadForm(HttpServletRequest request, HttpServletResponse response) {
		logger.log(IAppLogger.INFO, "Enter: groupDownloadForm()");
		ModelAndView modelAndView = null;

		Map<String, Object> paramMap = new HashMap<String, Object>();
		String reportId = (String) request.getParameter("reportId");
		logger.log(IAppLogger.INFO, "reportId=" + reportId);
		paramMap.put("REPORT_ID", reportId);
		paramMap.put("MESSAGE_TYPE", IApplicationConstants.DASH_MESSAGE_TYPE.RSCM.toString());
		paramMap.put("MESSAGE_NAME", IApplicationConstants.GROUP_DOWNLOAD_INSTRUCTION);
		String groupDownloadInstructionMessage = reportService.getSystemConfigurationMessage(paramMap);
		String customerId = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
		String orgNodeLevel = ((Long) request.getSession().getAttribute(IApplicationConstants.CURRORGLVL)).toString();
		String currentUserId = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSERID);
		String currentUserName = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSER);
		if ("true".equals((String) request.getAttribute("icDownload"))) {
			modelAndView = new ModelAndView("inors/icLetterDownloads");
		} else {
			modelAndView = new ModelAndView("inors/groupDownloads");
		}
		String testAdministrationVal = (String) request.getParameter("p_test_administration");
		//request.getSession().setAttribute("GDF_testadmin", testAdministrationVal);
		String testProgram = (String) request.getParameter("p_test_program");
		String corpDiocese = (String) request.getParameter("p_corpdiocese");
		String school = (String) request.getParameter("p_school");
		String klass = (String) request.getParameter("p_class");
		String grade = (String) request.getParameter("p_grade");
		String groupFile = (String) request.getParameter("p_generate_file");
		String collationHierarchy = (String) request.getParameter("p_collation");

		logger.log(IAppLogger.INFO, "testAdministrationVal=" + testAdministrationVal);
		logger.log(IAppLogger.INFO, "testProgram=" + testProgram);
		logger.log(IAppLogger.INFO, "corpDiocese=" + corpDiocese);
		logger.log(IAppLogger.INFO, "school=" + school);
		logger.log(IAppLogger.INFO, "klass=" + klass);
		logger.log(IAppLogger.INFO, "grade=" + grade);
		logger.log(IAppLogger.INFO, "groupFile=" + groupFile);
		logger.log(IAppLogger.INFO, "collationHierarchy=" + collationHierarchy);

		if ((testAdministrationVal == null) || ("null".equalsIgnoreCase(testAdministrationVal))) {
			String reportUrl = (String) request.getParameter("reportUrl");
			logger.log(IAppLogger.INFO, "reportUrl=" + reportUrl);
			modelAndView.addObject("reportUrl", reportUrl);
			// get parameter values for report
			Map<String, Object> parameters = getReportParameters(request, reportUrl);
			request.getSession().setAttribute(IApplicationConstants.REPORT_TYPE_CUSTOM + "parameters" + reportUrl, parameters);
			if ((parameters != null) && (!parameters.isEmpty())) {
				testAdministrationVal = CustomStringUtil.getNotNullString(parameters.get("p_test_administration"));
				testProgram = CustomStringUtil.getNotNullString(parameters.get("p_test_program"));
				corpDiocese = CustomStringUtil.getNotNullString(parameters.get("p_corpdiocese"));
				school = CustomStringUtil.getNotNullString(parameters.get("p_school"));
				klass = CustomStringUtil.getNotNullString(parameters.get("p_class"));
				grade = CustomStringUtil.getNotNullString(parameters.get("p_grade"));
				groupFile = CustomStringUtil.getNotNullString(parameters.get("p_generate_file"));
				collationHierarchy = CustomStringUtil.getNotNullString(parameters.get("p_collation"));
			}
		} else {
			/****NEW***/
			Map<String, String[]> sessionParams = (Map<String, String[]>) request.getSession().getAttribute("_REMEMBER_ME_ALL_");
			if(sessionParams == null) sessionParams = new HashMap<String, String[]>();
			Iterator itr = request.getParameterMap().entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry mapEntry = (Map.Entry) itr.next();
				request.getSession().setAttribute("_REMEMBER_ME_" + mapEntry.getKey(), mapEntry.getValue());
				sessionParams.put((String) mapEntry.getKey(), (String[]) mapEntry.getValue());
			}
			request.getSession().setAttribute("_REMEMBER_ME_ALL_", sessionParams);
			/****NEW***/
		}
		logger.log(IAppLogger.INFO, "testAdministrationVal=" + testAdministrationVal);
		logger.log(IAppLogger.INFO, "testProgram=" + testProgram);
		logger.log(IAppLogger.INFO, "corpDiocese=" + corpDiocese);
		logger.log(IAppLogger.INFO, "school=" + school);
		logger.log(IAppLogger.INFO, "klass=" + klass);
		logger.log(IAppLogger.INFO, "grade=" + grade);
		logger.log(IAppLogger.INFO, "groupFile=" + groupFile);
		logger.log(IAppLogger.INFO, "collationHierarchy=" + collationHierarchy);

		modelAndView.addObject("testAdministrationVal", testAdministrationVal);
		modelAndView.addObject("testProgram", testProgram);
		modelAndView.addObject("corpDiocese", corpDiocese);
		modelAndView.addObject("school", school);
		modelAndView.addObject("klass", klass);
		modelAndView.addObject("grade", grade);
		modelAndView.addObject("groupFile", groupFile);
		modelAndView.addObject("collationHierarchy", collationHierarchy);
		request.getSession().setAttribute("GDF_testadmin", testAdministrationVal);
		List<ReportMessageTO> reportMessages = null;
		if (testAdministrationVal != null) {
			String productId = testAdministrationVal;
			logger.log(IAppLogger.INFO, "reportId=" + reportId);
			logger.log(IAppLogger.INFO, "productId=" + productId);
			logger.log(IAppLogger.INFO, "customerId=" + customerId);
			logger.log(IAppLogger.INFO, "orgNodeLevel=" + orgNodeLevel);
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("REPORT_ID", reportId);
			parameterMap.put("PRODUCT_ID", productId);
			parameterMap.put("CUSTOMER_ID", customerId);
			parameterMap.put("ORG_NODE_LEVEL", orgNodeLevel);
			parameterMap.put("USER_ID", currentUserId);
			reportMessages = reportService.getAllReportMessages(parameterMap);
			Map<String, String> hiddenReportTypes = new HashMap<String, String>();
			hiddenReportTypes.put(IApplicationConstants.DASH_MESSAGE_TYPE.RSCM.toString(), IApplicationConstants.GROUP_DOWNLOAD_INSTRUCTION);
			reportMessages = setDisplayFlags(reportMessages, hiddenReportTypes);
			modelAndView.addObject("reportMessages", reportMessages);
			String productName = getProductNameById(testAdministrationVal);
			String hideContentFlag = getHideContentFlagGroupDownloadForm(groupFile, productName, reportMessages);
			String dataloadMessage = getReportMessage(reportMessages, IApplicationConstants.DASH_MESSAGE_TYPE.DM.toString(), IApplicationConstants.DATALOAD_MESSAGE);
			if (hideContentFlag.equals(IApplicationConstants.FLAG_N)) {
				try {
					String currentUser = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSER);
					String fileName = (String) request.getParameter("fileName");
					if ((fileName == null) || (fileName.equalsIgnoreCase("null"))) {
						fileName = (String) request.getSession().getAttribute("FILE_NAME_GD");
						if ((fileName == null) || (fileName.equalsIgnoreCase("null"))) {
							fileName = FileUtil.generateDefaultZipFileName(currentUser, groupFile);
							request.getSession().setAttribute("FILE_NAME_GD", fileName);
						}
					}
					String email = (String) request.getParameter("email");
					if ((email == null) || (email.equalsIgnoreCase("null"))) {
						email = (String) request.getSession().getAttribute("EMAIL_GD");
						if ((email == null) || (email.equalsIgnoreCase("null"))) {
							email = (String) request.getSession().getAttribute(IApplicationConstants.EMAIL);
						}
					}
					logger.log(IAppLogger.INFO, "fileName=" + fileName);
					logger.log(IAppLogger.INFO, "email=" + email);
					modelAndView.addObject("fileName", fileName);
					modelAndView.addObject("email", email);

					List<GroupDownloadStudentTO> studentList = new ArrayList<GroupDownloadStudentTO>();
					GroupDownloadTO to = new GroupDownloadTO();
					to.setSchool(school);
					to.setKlass(klass);
					to.setGrade(grade);
					to.setTestProgram(testProgram);
					to.setTestAdministrationVal(testAdministrationVal);
					to.setDistrict(corpDiocese);
					to.setCollationHierarchy(collationHierarchy);
					to.setCustomerId(customerId);
					to.setOrgNodeLevel(orgNodeLevel);
					to.setGroupFile(groupFile);
					to.setUserId(currentUserId);
					to.setUserName(currentUserName);
					if ((testProgram != null) && (!"null".equalsIgnoreCase(testProgram))) {
						LinkedHashSet<GroupDownloadStudentTO> tempList = new LinkedHashSet<GroupDownloadStudentTO>(populateStudentTableGD(to));
						studentList = new ArrayList<GroupDownloadStudentTO>(tempList);
					}
					Integer rowNum = 0;
					for(GroupDownloadStudentTO student : studentList) {
						rowNum = rowNum + 1;
						student.setRowNum(rowNum);
					}
					logger.log(IAppLogger.INFO, "Students after removing duplicates: " + studentList.size() + "\n" + JsonUtil.convertToJsonAdmin(studentList));
					modelAndView.addObject("studentList", studentList);
					modelAndView.addObject("studentCount", studentList.size());
				} catch (Exception e) {
					logger.log(IAppLogger.ERROR, e.getMessage(), e);
					e.printStackTrace();
				}
			} else {
				logger.log(IAppLogger.INFO, "------------------------Hiding the input form---------------------");
			}
			logger.log(IAppLogger.INFO, "hideContentFlag=" + hideContentFlag);
			logger.log(IAppLogger.INFO, "dataloadMessage=" + dataloadMessage);
			modelAndView.addObject("hideContentFlag", hideContentFlag);
			modelAndView.addObject("dataloadMessage", dataloadMessage);

		} else {
			modelAndView.addObject("reportMessages", null);
		}
		modelAndView.addObject("groupDownloadInstructionMessage", groupDownloadInstructionMessage);
		logger.log(IAppLogger.INFO, "Exit: groupDownloadForm()");
		return modelAndView;
	}

	/**
	 * Rule 1: Invitation Code Letters (IC) are available for the current ISTEP+ administration only. Rule 2: Image of student responses to Applied Skills test. For the two most recent ISTEP+
	 * administrations. (Not available for IMAST or IREAD-3) Rule 3: ISTEP+ and IMAST Student Report (ISR) for the two most recent administrations. Rule 4: IREAD-3 Student Report (ISR) for the 2013
	 * and 2014 administrations (Spring and Summer).
	 * 
	 * @param reportMessages
	 * @return
	 */
	private String getHideContentFlagGroupDownloadForm(String groupFile, String productName, List<ReportMessageTO> reportMessages) {
		String hideContentFlag = IApplicationConstants.FLAG_N;
		String currentAdminYear = inorsService.getCurrentAdminYear();
		int lastAdmYr = Integer.parseInt(currentAdminYear) - 1;
		String lastAdminYear = String.valueOf(lastAdmYr);
		logger.log(IAppLogger.INFO, "currentAdminYear=" + currentAdminYear);
		logger.log(IAppLogger.INFO, "lastAdminYear=" + lastAdminYear);
		if (groupFile.equals(IApplicationConstants.EXTRACT_FILETYPE.ICL.toString())) {
			// Rule 1: Invitation Code Letters (IC) are available for the current ISTEP+ administration only.
			// Report Notification: Invitation Code Letters (IC) are available for the current ISTEP+ administration only.
			if (productName != null) {
				if (productName.startsWith("ISTEP+")) {
					if ((productName.endsWith(currentAdminYear))) {
						// OK
					} else {
						hideContentFlag = IApplicationConstants.FLAG_Y;
					}
				} else {
					hideContentFlag = IApplicationConstants.FLAG_Y;
				}
			}
		} else if (groupFile.equals(IApplicationConstants.EXTRACT_FILETYPE.IPR.toString())) {
			// Rule 2: Image of student responses to Applied Skills test. For the two most recent ISTEP+ administrations. (Not available for IMAST or IREAD-3)
			if (productName != null) {
				if (productName.startsWith("ISTEP+")) {
					if ((productName.endsWith(currentAdminYear)) || (productName.endsWith(lastAdminYear))) {
						// OK
					} else {
						hideContentFlag = IApplicationConstants.FLAG_Y;
					}
				} else {
					hideContentFlag = IApplicationConstants.FLAG_Y;
				}
			}
		} else if (groupFile.equals(IApplicationConstants.EXTRACT_FILETYPE.ISR.toString())) {
			// Rule 3: ISTEP+ and IMAST Student Report (ISR) for the two most recent administrations.
			// Rule 4: IREAD-3 Student Report (ISR) for the 2013 and 2014 administrations (Spring and Summer).
			if (productName != null) {
				if (productName.startsWith("ISTEP+")) {
					if ((productName.endsWith(currentAdminYear)) || (productName.endsWith(lastAdminYear))) {
						// OK
					} else {
						hideContentFlag = IApplicationConstants.FLAG_Y;
					}
				} else if (productName.startsWith("IMAST")) {
					if ((productName.endsWith(currentAdminYear)) || (productName.endsWith(lastAdminYear))) {
						// OK
					} else {
						hideContentFlag = IApplicationConstants.FLAG_Y;
					}
				} else if (productName.startsWith("IREAD-3")) {
					if ((productName.endsWith(currentAdminYear)) || (productName.endsWith(lastAdminYear))) {
						// OK
					} else {
						hideContentFlag = IApplicationConstants.FLAG_Y;
					}
				} else {
					hideContentFlag = IApplicationConstants.FLAG_Y;
				}
			}
		} else if (groupFile.equals(IApplicationConstants.EXTRACT_FILETYPE.BOTH.toString())) {
			// Rule 2, 3 and 4
			if (productName.startsWith("ISTEP+")) {
				if ((productName.endsWith(currentAdminYear)) || (productName.endsWith(lastAdminYear))) {
					// OK
				} else {
					hideContentFlag = IApplicationConstants.FLAG_Y;
				}
			} else if (productName.startsWith("IMAST")) {
				if ((productName.endsWith(currentAdminYear)) || (productName.endsWith(lastAdminYear))) {
					// OK
				} else {
					hideContentFlag = IApplicationConstants.FLAG_Y;
				}
			} else if (productName.startsWith("IREAD-3")) {
				if ((productName.endsWith(currentAdminYear)) || (productName.endsWith(lastAdminYear))) {
					// OK
				} else {
					hideContentFlag = IApplicationConstants.FLAG_Y;
				}
			} else {
				hideContentFlag = IApplicationConstants.FLAG_Y;
			}
		}
		return hideContentFlag;
	}

	/**
	 * This method is used to hide a particular message from jsp.
	 * 
	 * @param allReportMessages
	 * @param hiddenReportTypes
	 * @return
	 */
	private List<ReportMessageTO> setDisplayFlags(List<ReportMessageTO> allReportMessages, Map<String, String> hiddenReportTypes) {
		for (Entry<String, String> hiddenReportType : hiddenReportTypes.entrySet()) {
			for (ReportMessageTO reportMessageTO : allReportMessages) {
				String messageType = reportMessageTO.getMessageType();
				String messageName = reportMessageTO.getMessageName();
				if ((messageType != null) && (messageType.equals(hiddenReportType.getKey()))) {
					if ((messageName != null) && (messageName.equals(hiddenReportType.getValue()))) {
						reportMessageTO.setDisplayFlag(IApplicationConstants.FLAG_N);
						logger.log(IAppLogger.INFO, "Hiding MessageName: " + messageName);
					}
				}
			}
		}
		return allReportMessages;
	}

	/**
	 * 
	 * @param to
	 * @return
	 */
	private List<GroupDownloadStudentTO> populateStudentTableGD(GroupDownloadTO to) {
		logger.log(IAppLogger.INFO, "Enter: populateStudentTableGD()");
		long t1 = System.currentTimeMillis();
		List<GroupDownloadStudentTO> studentList = new ArrayList<GroupDownloadStudentTO>();
		try {
			studentList = reportService.populateStudentTableGD(to);
			logger.log(IAppLogger.INFO, "Students: " + studentList.size());
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "populateStudentTableGD() :" + e.getMessage());
			e.printStackTrace();
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: populateStudentTableGD(): " + String.valueOf(t2 - t1) + "ms");
		}
		return studentList;
	}

	/**
	 * This method will create a Job Tracking Id and call processGroupDownload(processId)
	 * 
	 * @param to
	 * @param response
	 * @return {"handler" : "success/failure", "type" ; "sync/async"}
	 * @throws SystemException
	 */
	@RequestMapping(value = "/groupDownloadFunction", method = RequestMethod.POST)
	public @ResponseBody
	String groupDownloadFunction(HttpServletRequest request, HttpServletResponse response) throws SystemException {
		logger.log(IAppLogger.INFO, "Enter: groupDownloadFunction()");
		String json = (String) request.getParameter("json");
		GroupDownloadTO to = Utils.jsonToObject(json, GroupDownloadTO.class);
		Map<String, Object> propertyMap = (Map<String, Object>) request.getSession().getAttribute("propertyMap");
		to.setEnvString((String) propertyMap.get(IApplicationConstants.STATIC_PDF_LOCATION));

		String handler = "";
		String type = "";
		String downloadFileName = "";
		String jobTrackingId = "";
		logger.log(IAppLogger.INFO, to.toString());
		try {
			// Asynchronous : Create Process Id
			type = "async";
			if ((to.getStudents() != null) && (!to.getStudents().isEmpty())) {
				String currentUserId = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSERID);
				String currentUser = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSER);
				String adminId = (String) request.getSession().getAttribute(IApplicationConstants.ADMIN_YEAR);
				String customerId = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
				logger.log(IAppLogger.INFO, "currentUserId = " + currentUserId);
				logger.log(IAppLogger.INFO, "currentUser = " + currentUser);
				logger.log(IAppLogger.INFO, "adminId = " + adminId);
				logger.log(IAppLogger.INFO, "customerId = " + customerId);
				to.setUserId(currentUserId);
				to.setUserName(currentUser);
				to.setAdminId(adminId);
				to.setCustomerId(customerId);
				jobTrackingId = reportService.createJobTracking(to);
			}
			logger.log(IAppLogger.INFO, "jobTrackingId = " + jobTrackingId);
			handler = "success";
			
			// TODO : JMS Integration for processGroupDownload() method
			logger.log(IAppLogger.INFO, "sending messsage --------------- ");
			messageProducer.sendJobForProcessing(jobTrackingId, Utils.getContractName());
			// inorsService.batchPDFDownload(jobTrackingId);
			
			String jsonString = CustomStringUtil.appendString("{\"handler\": \"", handler, "\", \"type\": \"", type, "\", \"downloadFileName\": \"", downloadFileName, "\", \"jobTrackingId\": \"", jobTrackingId, "\"}");
			logger.log(IAppLogger.INFO, "groupDownloadFunction(): " + jsonString);
			logger.log(IAppLogger.INFO, "Exit: groupDownloadFunction()");
			return jsonString;
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage());
			handler = "error";
			String jsonString = CustomStringUtil.appendString("{\"handler\": \"", handler, "\", \"type\": \"", type, "\", \"downloadFileName\": \"", downloadFileName, "\", \"jobTrackingId\": \"", jobTrackingId, "\"}");
			return jsonString;
		}
	}

	/**
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/downloadZippedPdf", method = RequestMethod.GET)
	public void downloadZippedPdf(HttpServletRequest request, HttpServletResponse response) {
		long t1 = System.currentTimeMillis();
		logger.log(IAppLogger.INFO, "Enter: downloadZippedPdf()");

		String fileName = request.getParameter("fileName");
		// IMPORTANT : fileName should be with forward slash ('/') format in database : C:/Temp/GroupDownload/1111.pdf
		String fileType = request.getParameter("fileType");
		String email = request.getParameter("email");
		logger.log(IAppLogger.INFO, "fileName=" + fileName);
		logger.log(IAppLogger.INFO, "email=" + email);
		String zipFileName = fileType + ".zip";
		try {
			// fileName = CustomStringUtil.replaceAll(fileName, "/", "\\\\");
			String customerId = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
			String testAdmin = (String) request.getSession().getAttribute("GDF_testadmin");
			logger.log(IAppLogger.INFO, "testAdmin=" + testAdmin);
			logger.log(IAppLogger.INFO, "customerId=" + customerId);
			String rootPath = loginService.getRootPath(customerId, testAdmin, Utils.getContractName());
			fileName = CustomStringUtil.appendString(rootPath, fileName);
			File pdfFile = new File(fileName);
			if (!pdfFile.canRead()) {
				logger.log(IAppLogger.WARN, "No Read Permission");
			} else {
				logger.log(IAppLogger.INFO, "Can Read File");
			}
			// Now read the pdf file from disk
			//byte[] data = FileCopyUtils.copyToByteArray(new FileInputStream(fileName));
			byte[] data =repositoryService.getAssetBytes(fileName);
			// Zip the pdf file
			byte[] zipData = FileUtil.zipBytes(fileName, data);

			// Download the file
			FileUtil.browserDownload(response, zipData, zipFileName);

			// Send email - [Amit] Conmmenting as this is not needed for sync download
			/*if ((email != null) && (!email.isEmpty())) {
				notificationMailGD(email);
			}*/

			// Delete the zip file from disk
			logger.log(IAppLogger.INFO, "temp zip file deleted = " + new File(zipFileName).delete());
		} catch (Exception e) {
			ReportController.showDownloadError(response);
			logger.log(IAppLogger.ERROR, e.getMessage());
			e.printStackTrace();
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: downloadZippedPdf(): " + String.valueOf(t2 - t1) + "ms");
		}
	}
	
	@RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
		long t1 = System.currentTimeMillis();
		logger.log(IAppLogger.INFO, "Enter: downloadFile()");
		String fileName = request.getParameter("fileName");
		try {
			File studentFile = new File(fileName);
			if (!studentFile.canRead()) {
				logger.log(IAppLogger.WARN, "No Read Permission");
			} else {
				logger.log(IAppLogger.INFO, "Can Read File");
			}
			// Now read the pdf file from disk
		//	byte[] data = FileCopyUtils.copyToByteArray(new FileInputStream(fileName));
			byte[] data =repositoryService.getAssetBytes(fileName);	
			// Download the file
			FileUtil.browserDownload(response, data, FileUtil.getFileNameFromFilePath(fileName));
		} catch (Exception e) {
			ReportController.showDownloadError(response);
			logger.log(IAppLogger.ERROR, e.getMessage());
			e.printStackTrace();
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: downloadFile(): " + String.valueOf(t2 - t1) + "ms");
		}
	}

	@Autowired
	private EmailSender emailSender;
	/**
	 * This method is used to send a notification mail after Group Download
	 * 
	 * @param email
	 */
	private void notificationMailGD(String email) {
		logger.log(IAppLogger.INFO, "Enter: notificationMailGD()");
		try {
			Properties prop = new Properties();
			prop.setProperty(IEmailConstants.SMTP_HOST, propertyLookup.get(IEmailConstants.SMTP_HOST));
			prop.setProperty(IEmailConstants.SMTP_PORT, propertyLookup.get(IEmailConstants.SMTP_PORT));
			prop.setProperty("senderMail", propertyLookup.get("senderMail"));
			prop.setProperty("supportEmail", propertyLookup.get("supportEmail"));
			String subject = propertyLookup.get("mail.gd.subject");
			String mailBody = propertyLookup.get("mail.gd.body");
			logger.log(IAppLogger.INFO, "Email triggered...");
			emailSender.sendMail(prop, email, null, null, subject, mailBody);
			logger.log(IAppLogger.INFO, "Email sent to : " + email);
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Unable to send Email: " + e.getMessage());
			e.printStackTrace();
		}
		logger.log(IAppLogger.INFO, "Exit: notificationMailGD()");
	}

	/**
	 * Get school -class - student hierarchy for tree.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/groupDownloadHierarchy", method = RequestMethod.GET)
	public @ResponseBody
	String getTenantHierarchy(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: getTenantHierarchy()");
		List<OrgTreeTO> orgTreeTOs = new ArrayList<OrgTreeTO>();
		String orgJsonString;
		String currentUser = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSER);
		String currentOrg = (String) request.getSession().getAttribute(IApplicationConstants.CURRORG);
		String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
		long currCustomer = (customer == null) ? 0 : Long.parseLong(customer);
		try {
			String nodeid = (String) request.getParameter("tenantId");
			String nodeLevel = (String) request.getParameter("nodeLevel");
			String adminYear = (String) request.getParameter("AdminYear");
			String reportUrl = (String) request.getParameter("reportUrl");

			Map<String, Object> parameters = null;
			parameters = (Map<String, Object>) request.getSession().getAttribute(IApplicationConstants.REPORT_TYPE_CUSTOM + "parameters" + reportUrl);
			while (parameters == null) {
				logger.log(IAppLogger.WARN, "Waiting for custom report parameter .... ");
				Thread.sleep(2000);
				parameters = (Map<String, Object>) request.getSession().getAttribute(IApplicationConstants.REPORT_TYPE_CUSTOM + "parameters" + reportUrl);
			}
			adminYear = (String) parameters.get("p_adminYear");
			String school = (String) parameters.get("p_school");
			String corp = (String) parameters.get("p_corp");
			String orgClass = (String) parameters.get("p_class");
			String grade = (String) parameters.get("p_gradeid");

			if (nodeid != null) {
				orgTreeTOs = adminService.getHierarchy(parameters);

				if (orgTreeTOs != null && orgTreeTOs.isEmpty() && nodeid.indexOf("_") == -1) {
					List<com.ctb.prism.admin.transferobject.ObjectValueTO> studentList = adminService.getAllStudents(adminYear, nodeid, customer, grade);

					for (com.ctb.prism.admin.transferobject.ObjectValueTO obj : studentList) {
						OrgTO to = new OrgTO();
						OrgTreeTO treeTo = new OrgTreeTO();

						to.setId((obj.getValue() == null) ? 0 : Long.parseLong(obj.getValue()));
						to.setParentTenantId(Long.parseLong(nodeid));
						to.setOrgLevel(-1);
						to.setClassName("jstree-leaf");

						treeTo.setState("leaf");
						treeTo.setOrgTreeId(Long.parseLong(nodeid));
						treeTo.setData(obj.getName());
						treeTo.setMetadata(to);
						treeTo.setAttr(to);
						orgTreeTOs.add(treeTo);
					}
				}
			}
			orgJsonString = JsonUtil.convertToJsonAdmin(orgTreeTOs);

			logger.log(IAppLogger.DEBUG, orgJsonString);
			response.setContentType("application/json");
			response.getWriter().write(orgJsonString);
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
			e.printStackTrace();
		}
		logger.log(IAppLogger.INFO, "Exit: getTenantHierarchy()");
		return null;
	}

	/**
	 * Download job initiation for requested students.
	 * 
	 * @param bulkDownloadTO
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/downloadBulkPdf", method = RequestMethod.GET)
	public @ResponseBody
	String downloadBulkPdf(@ModelAttribute("groupDownload") BulkDownloadTO bulkDownloadTO, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: downloadBulkPdf()");
		String currentUser = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSER);
		String currentUserId = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSERID);
		String currentOrg = (String) request.getSession().getAttribute(IApplicationConstants.CURRORG);
		String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
		long currCustomer = (customer == null) ? 0 : Long.parseLong(customer);
		String status = "Error";
		try {
			// remove retain bulk value
			request.getSession().removeAttribute("retainBulkDownloadTO");
			String fileType = request.getParameter("fileType");
			String nodeid = (String) request.getParameter("tenantId");
			String adminYear = (String) request.getParameter("AdminYear");
			String reportUrl = (String) request.getParameter("reportUrl");
			String docName = CustomStringUtil.appendString(currentUser, " ", Utils.getDateTime(), "_Querysheet.pdf");

			String school = null, corp = null, orgClass = null, testProgram = null, grade = null;
			if (!"CR".equals(fileType)) {
				Map<String, Object> parameters = null;
				parameters = (Map<String, Object>) request.getSession().getAttribute(IApplicationConstants.REPORT_TYPE_CUSTOM + "parameters" + reportUrl);
				while (parameters == null) {
					logger.log(IAppLogger.WARN, "Waiting for custom report parameter .... ");
					Thread.sleep(2000);
					parameters = (Map<String, Object>) request.getSession().getAttribute(IApplicationConstants.REPORT_TYPE_CUSTOM + "parameters" + reportUrl);
				}

				adminYear = (String) parameters.get("p_adminYear");
				school = (String) parameters.get("p_school");
				corp = (String) parameters.get("p_corp");
				orgClass = (String) parameters.get("p_class");
				grade = (String) parameters.get("p_gradeid");
				testProgram = (String) parameters.get("p_testProgram");
			}
			bulkDownloadTO.setQuerysheetFile(docName);
			bulkDownloadTO.setUdatedBy((currentUserId == null) ? 0 : Long.parseLong(currentUserId));
			bulkDownloadTO.setUsername(currentUser);
			bulkDownloadTO.setTestAdministration(adminYear);
			bulkDownloadTO.setSchool(school);
			bulkDownloadTO.setCorp(corp);
			bulkDownloadTO.setOrgClass(orgClass);
			bulkDownloadTO.setTestProgram(testProgram);
			bulkDownloadTO.setGrade(grade);
			bulkDownloadTO.setCustomerId(customer);
			bulkDownloadTO.setReportUrl(reportUrl);
			bulkDownloadTO.setRequestType(fileType);
			bulkDownloadTO.setDownloadMode(request.getParameter("mode"));

			bulkDownloadTO = inorsService.createJob(bulkDownloadTO);

			String querysheetFile = PdfGenerator.generateQuerysheet(bulkDownloadTO, propertyLookup);

			logger.log(IAppLogger.INFO, "sending messsage --------------- ");
			messageProducer.sendJobForProcessing(""+bulkDownloadTO.getJobId(), Utils.getContractName());

			if (bulkDownloadTO.getJobId() != 0)
				status = "Success";

			response.setContentType("application/json");
			response.getWriter().write("");
			response.getWriter().write("{\"status\":\"" + status + "\"}");
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
			e.printStackTrace();
			response.getWriter().write("{\"status\":\"" + status + "\"}");
		}
		logger.log(IAppLogger.INFO, "Exit: downloadBulkPdf()");
		return null;
	}

	/**
	 * This method is to retain form values.
	 * 
	 * @param bulkDownloadTO
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/retainDownloadValues", method = RequestMethod.GET)
	public @ResponseBody
	String retainDownloadValues(@ModelAttribute("groupDownload") BulkDownloadTO bulkDownloadTO, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: retainDownloadValues()");
		request.getSession().setAttribute("retainBulkDownloadTO", bulkDownloadTO);

		String fileName = request.getParameter("fileName");
		String email = request.getParameter("email");
		logger.log(IAppLogger.INFO, "fileName=" + fileName);
		logger.log(IAppLogger.INFO, "email=" + email);
		request.getSession().setAttribute("FILE_NAME_GD", fileName);
		request.getSession().setAttribute("EMAIL_GD", email);

		logger.log(IAppLogger.INFO, "Exit: retainDownloadValues()");
		return null;
	}

	/**
	 * Clears the Group Download related Session data.
	 * 
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/clearGDCache", method = RequestMethod.GET)
	public void clearGDCache(HttpServletRequest request) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: clearGDCache()");
		request.getSession().removeAttribute("retainBulkDownloadTO");
		request.getSession().removeAttribute("p_test_administration");
		request.getSession().removeAttribute("p_test_program");
		request.getSession().removeAttribute("p_corpdiocese");
		request.getSession().removeAttribute("p_school");
		request.getSession().removeAttribute("p_class");
		request.getSession().removeAttribute("p_grade");
		request.getSession().removeAttribute("p_generate_file");
		request.getSession().removeAttribute("p_collation");
		logger.log(IAppLogger.INFO, "Exit: clearGDCache()");
	}

	/**
	 * Returns the product name from the product id.
	 * 
	 * @param testAdministrationVal
	 * @return
	 */
	private String getProductNameById(String testAdministrationVal) {
		String productName = null;
		try {
			if ((testAdministrationVal != null) && (!"null".equalsIgnoreCase(testAdministrationVal))) {
				productName = inorsService.getProductNameById(Long.valueOf(testAdministrationVal));
			}
		} catch (Exception e) {
			logger.log(IAppLogger.WARN, e.getMessage());
			e.printStackTrace();
		}
		return (productName == null) ? "" : productName;
	}

	/**
	 * Displays the GRT/IC File Download page with pre-populated corporation list.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/grtICFileForm", method = RequestMethod.GET)
	public ModelAndView grtICFileForm(HttpServletRequest request) {
		logger.log(IAppLogger.INFO, "Enter: grtICFileForm()");
		ModelAndView modelAndView = new ModelAndView("inors/grtICFileForm");

		String testAdministrationVal = (String) request.getParameter("p_test_administration");
		String testProgram = (String) request.getParameter("p_test_program");
		String corpDiocese = (String) request.getParameter("p_corpdiocese");
		String school = (String) request.getParameter("p_school");
		logger.log(IAppLogger.INFO, "testAdministrationVal=" + testAdministrationVal);
		logger.log(IAppLogger.INFO, "testProgram=" + testProgram);
		logger.log(IAppLogger.INFO, "corpDiocese=" + corpDiocese);
		logger.log(IAppLogger.INFO, "school=" + school);

		if ((testAdministrationVal == null) || ("null".equalsIgnoreCase(testAdministrationVal))) {
			String reportUrl = (String) request.getParameter("reportUrl");
			logger.log(IAppLogger.INFO, "reportUrl=" + reportUrl);
			modelAndView.addObject("reportUrl", reportUrl);
			// get parameter values for report
			Map<String, Object> parameters = getReportParameters(request, reportUrl);
			logger.log(IAppLogger.INFO, "parameters = " + parameters);
			request.getSession().setAttribute(IApplicationConstants.REPORT_TYPE_CUSTOM + "parameters" + reportUrl, parameters);
			if ((parameters != null) && (!parameters.isEmpty())) {
				testAdministrationVal = CustomStringUtil.getNotNullString(parameters.get("p_test_administration"));
				testProgram = CustomStringUtil.getNotNullString(parameters.get("p_test_program"));
				corpDiocese = CustomStringUtil.getNotNullString(parameters.get("p_corpdiocese"));
				school = CustomStringUtil.getNotNullString(parameters.get("p_school"));
			}
		}
		logger.log(IAppLogger.INFO, "testAdministrationVal=" + testAdministrationVal);
		logger.log(IAppLogger.INFO, "testProgram=" + testProgram);
		logger.log(IAppLogger.INFO, "corpDiocese=" + corpDiocese);
		logger.log(IAppLogger.INFO, "school=" + school);

		String productName = getProductNameById(testAdministrationVal);
		logger.log(IAppLogger.INFO, "productName=" + productName);

		modelAndView.addObject("testAdministrationVal", testAdministrationVal);
		modelAndView.addObject("testAdministrationText", productName);
		modelAndView.addObject("testProgram", testProgram);
		modelAndView.addObject("corpDiocese", corpDiocese);
		modelAndView.addObject("school", school);

		String reportId = (String) request.getParameter("reportId");
		String productId = (testAdministrationVal == null) ? IApplicationConstants.DEFAULT_PRODUCT_ID : testAdministrationVal;
		String customerId = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
		String orgNodeLevel = ((Long) request.getSession().getAttribute(IApplicationConstants.CURRORGLVL)).toString();
		String currentUserId = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSERID);
		logger.log(IAppLogger.INFO, "reportId=" + reportId);
		logger.log(IAppLogger.INFO, "productId=" + productId);
		logger.log(IAppLogger.INFO, "customerId=" + customerId);
		logger.log(IAppLogger.INFO, "orgNodeLevel=" + orgNodeLevel);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("REPORT_ID", reportId);
		paramMap.put("PRODUCT_ID", productId);
		paramMap.put("CUSTOMER_ID", customerId);
		paramMap.put("ORG_NODE_LEVEL", orgNodeLevel);
		paramMap.put("USER_ID", currentUserId);
		List<ReportMessageTO> reportMessages = reportService.getAllReportMessages(paramMap);
		modelAndView.addObject("reportMessages", reportMessages);
		String dataloadMessage = getReportMessage(reportMessages, IApplicationConstants.DASH_MESSAGE_TYPE.DM.toString(), IApplicationConstants.DATALOAD_MESSAGE);
		logger.log(IAppLogger.INFO, "dataloadMessage=" + dataloadMessage);
		modelAndView.addObject("dataloadMessage", dataloadMessage);

		String currentAdminYear = inorsService.getCurrentAdminYear();
		int lastAdmYr = Integer.parseInt(currentAdminYear) - 1;
		String lastAdminYear = String.valueOf(lastAdmYr);
		logger.log(IAppLogger.INFO, "currentAdminYear=" + currentAdminYear);
		logger.log(IAppLogger.INFO, "lastAdminYear=" + lastAdminYear);

		modelAndView.addObject("currentAdminYear", currentAdminYear);
		modelAndView.addObject("lastAdminYear", lastAdminYear);

		String currentIcFile = "IC_ISTEP_SPRING_" + currentAdminYear + "_FILENAME";
		String lastIcFile = "IC_ISTEP_SPRING_" + lastAdminYear + "_FILENAME";

		String currentIcFileName = propertyLookup.get(currentIcFile);
		String lastIcFileName = propertyLookup.get(lastIcFile);

		logger.log(IAppLogger.INFO, "currentIcFileName=" + currentIcFileName);
		logger.log(IAppLogger.INFO, "lastIcFileName=" + lastIcFileName);

		modelAndView.addObject("currentIcFileName", currentIcFileName);
		modelAndView.addObject("lastIcFileName", lastIcFileName);

		String showGrtDiv = "Y";
		String showIcDiv = "N";
		if (productName != null && productName.length() >= 4) {
			String selectedYear = productName.substring(productName.length() - 4);
			logger.log(IAppLogger.INFO, "selectedYear=" + selectedYear);
			modelAndView.addObject("selectedYear", selectedYear);

			String yearPrefix = getYearPrefix(selectedYear);
			logger.log(IAppLogger.INFO, "yearPrefix=" + yearPrefix);
			String productPrefix = "";
			String productSuffix = "";
			if (productName.toUpperCase().contains("ISTEP")) {
				productPrefix = "ISTEP";
			} else if (productName.toUpperCase().contains("IMAST")) {
				productPrefix = "IMAST";
			} else if (productName.toUpperCase().contains("IREAD")) {
				productPrefix = "IREAD3";
			} else {
				logger.log(IAppLogger.ERROR, "Invalid Product Prefix: Expected - ISTEP/IMAST/IREAD");
			}
			if (productName.toUpperCase().contains("SPRING")) {
				productSuffix = "SPRING";
			} else if (productName.toUpperCase().contains("SUMMER")) {
				productSuffix = "SUMMER";
			} else {
				logger.log(IAppLogger.ERROR, "Invalid Product Suffix: Expected - SPRING/SUMMER");
			}

			// IC is available for latest two years
			if ((productName.contains(currentAdminYear)) || (productName.contains(lastAdminYear))) {
				// IC is available for ISTEP
				if (productName.contains("ISTEP")) {
					showIcDiv = "Y";
				}
			}
			String icFileLayoutDisplayName = yearPrefix + " Invitation Code File Record Layout";
			// String icFileLayoutHref = productSuffix.trim() + yearPrefix + " Invitation Code Layout.xls";
			String icFileLayoutHref = propertyLookup.get("IC_" + productPrefix + "_" + productSuffix + "_" + selectedYear + "_FILENAME");
			if (icFileLayoutHref == null || icFileLayoutHref.isEmpty()) {
				if (showIcDiv == "Y") {
					logger.log(IAppLogger.ERROR, "IC Layout Filename is not configured in Properties file");
				}
			} else {
				logger.log(IAppLogger.INFO, "icFileLayoutHref=" + icFileLayoutHref);
			}
			logger.log(IAppLogger.INFO, "icFileLayoutDisplayName=" + icFileLayoutDisplayName);
			modelAndView.addObject("icFileLayoutDisplayName", icFileLayoutDisplayName);
			modelAndView.addObject("icFileLayoutHref", icFileLayoutHref);

			// GRT is available for all years
			String grtFileLayoutDisplayName = yearPrefix + " GRT File Record Layout";
			// String grtFileLayoutHref = productPrefix + productSuffix + yearPrefix + " GR 3-8 GRT Corp Version.xls";
			String grtFileLayoutHref = propertyLookup.get("GRT_" + productPrefix + "_" + productSuffix + "_" + selectedYear + "_FILENAME");
			if (grtFileLayoutHref == null || grtFileLayoutHref.isEmpty()) {
				if (showGrtDiv == "Y") {
					logger.log(IAppLogger.ERROR, "GRT Layout Filename is not configured in Properties file");
				}
			} else {
				logger.log(IAppLogger.INFO, "grtFileLayoutHref=" + grtFileLayoutHref);
			}
			logger.log(IAppLogger.INFO, "grtFileLayoutDisplayName=" + grtFileLayoutDisplayName);
			modelAndView.addObject("grtFileLayoutDisplayName", grtFileLayoutDisplayName);
			modelAndView.addObject("grtFileLayoutHref", grtFileLayoutHref);
		} else {
			logger.log(IAppLogger.ERROR, "Invalid Product Name");
		}
		logger.log(IAppLogger.INFO, "showGrtDiv=" + showGrtDiv);
		logger.log(IAppLogger.INFO, "showIcDiv=" + showIcDiv);
		modelAndView.addObject("showGrtDiv", showGrtDiv);
		modelAndView.addObject("showIcDiv", showIcDiv);

		logger.log(IAppLogger.INFO, "Exit: grtICFileForm()");
		return modelAndView;
	}
	
	/**
	 * For input value 2014 the output value is 2013-14.
	 * 
	 * @param adminYear
	 * @return
	 */
	private String getYearPrefix(String adminYear) {
		Integer currentYear = Integer.parseInt(adminYear);
		Integer lastYear = currentYear - 1;
		String yearPrefix = lastYear + "-" + currentYear.toString().substring(2);
		return yearPrefix;
	}

	/**
	 * Downloads GRT or Invitation Code files from the browser based on user inputs.
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/downloadGRTInvitationCodeFiles", method = RequestMethod.GET)
	public void downloadGRTInvitationCodeFiles(HttpServletRequest request, HttpServletResponse response) {
		logger.log(IAppLogger.INFO, "Enter: downloadGRTInvitationCodeFiles()");
		Map<String, String> paramMap = new HashMap<String, String>();

		String type = (String) request.getParameter("type");
		String testAdministrationVal = (String) request.getParameter("testAdministrationVal");
		String testProgram = (String) request.getParameter("testProgram");
		String corpDiocese = (String) request.getParameter("corpDiocese");
		String school = (String) request.getParameter("school");

		logger.log(IAppLogger.INFO, "type=" + type);
		logger.log(IAppLogger.INFO, "testAdministrationVal=" + testAdministrationVal);
		logger.log(IAppLogger.INFO, "testProgram=" + testProgram);
		logger.log(IAppLogger.INFO, "corpDiocese=" + corpDiocese);
		logger.log(IAppLogger.INFO, "school=" + school);

		String productName = getProductNameById(testAdministrationVal);
		logger.log(IAppLogger.INFO, "productName=" + productName);
		String[] tokens = productName.split(" ");
		String product = tokens[0];
		String term = tokens[1];
		String year = tokens[2];

		logger.log(IAppLogger.INFO, "product=" + product);
		logger.log(IAppLogger.INFO, "term=" + term);
		logger.log(IAppLogger.INFO, "year=" + year);

		String userId = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSERID);
		String userName = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSER);

		String productStr = product;
		if (productStr.indexOf('+') > 0) {
			productStr = productStr.replace("+", "").trim();
		}
		if (productStr.indexOf('-') > 0) {
			productStr = productStr.replace("-", "").trim();
		}
		String layoutName = type + "_" + productStr + "_" + term + "_" + year;
		layoutName = layoutName.toUpperCase();
		if (layoutName.indexOf(' ') > 0) {
			layoutName = layoutName.replaceAll("\\s+", "").trim();
		}
		logger.log(IAppLogger.INFO, "layoutName=" + layoutName);

		String headers = propertyLookup.get(layoutName + "_HEADER");
		String aliases = propertyLookup.get(layoutName + "_ALIAS");
		ArrayList<String> headerList = InorsDownloadUtil.getRowDataLayout(headers);
		ArrayList<String> aliasList = InorsDownloadUtil.getRowDataLayout(aliases);
		if (headerList.size() != aliasList.size()) {
			logger.log(IAppLogger.ERROR, "Header count(" + headerList.size() + ") does not match with Alias count(" + aliasList.size() + ")");
		} else {
			logger.log(IAppLogger.INFO, "Header count(" + headerList.size() + ") matches with Alias count(" + aliasList.size() + ")");
		}

		paramMap.put("type", type);
		paramMap.put("product", product);
		paramMap.put("term", term);
		paramMap.put("year", year);
		paramMap.put("productId", testAdministrationVal);
		paramMap.put("testProgram", testProgram);
		paramMap.put("parentOrgNodeId", corpDiocese);
		paramMap.put("orgNodeId", school);
		paramMap.put("userId", userId);
		paramMap.put("userName", userName);

		byte[] data = null;
		String fileName = layoutName.toLowerCase() + ".dat";
		String zipFileName = layoutName.toLowerCase() + ".zip";
		ArrayList<ArrayList<String>> table = inorsService.getTabulerData(paramMap, aliasList, headerList);
		data = InorsDownloadUtil.getTableDataBytes(table, IApplicationConstants.COMMA);

		try {
			data = FileUtil.zipBytes(fileName, data);
		} catch (IOException e) {
			logger.log(IAppLogger.ERROR, "zipBytes - ", e);
			e.printStackTrace();
		}
		FileUtil.browserDownload(response, data, zipFileName);
		logger.log(IAppLogger.INFO, "Exit: downloadGRTInvitationCodeFiles()");
	}

	/**
	 * @author Amit Dhara,Arunava Datta Scheduler every night @ 1 AM Group Download files deletion and job tracking update if exp date >= SYSDATE Scheduler
	 * @RequestMapping(value = "/doSomething", method = RequestMethod.GET) -- > For testing enable this and hit the URL as doSomething.do
	 * @throws Exception
	 */

	@Scheduled(cron="${cron.expression}")
	public void doSomething() throws Exception {
		logger.log(IAppLogger.INFO, " START CRON JOB @ 1 AM ----- f r o m  Scheduled method for GROUP DOWNLOAD FILES --------------- ");
		String gdfExpiryTime = propertyLookup.get("gdfExpiryTime");
		reportService.deleteScheduledGroupFiles(gdfExpiryTime);
		logger.log(IAppLogger.INFO, "END CRON JOB @ 1 AM ----- f r o m  Scheduled method for GROUP DOWNLOAD FILES--------------- ");
	}
	
	private void updateFileExt(String Id,String filePath,String fileName,String orgLevel,String requestType,HttpServletResponse response) throws Exception {
		String extensionType = "";
		String password = propertyLookup.get("gdfpassword");

		try {			
			if(orgLevel.equals("1") && requestType.equals("SDF")&& !fileName.endsWith(".zip"))
			{
				File oldFile = new File(filePath);
				String newFilePath=filePath.replaceAll(fileName,"");
				String newFileName=fileName.replace(fileName.substring(fileName.lastIndexOf(".") + 1),"zip");
				newFilePath=newFilePath.concat(newFileName);

				FileUtil.createPasswordProtectedZipFile(filePath,newFilePath,password);

				File Newfile = new File(newFilePath);
				byte[] data = FileCopyUtils.copyToByteArray(Newfile);
				//response.setContentType("application/" + "zip");
				response.setContentType("binary/data");
				response.setContentLength(data.length);
				response.setHeader("Content-Disposition", "attachment; filename=" + newFileName);
				FileCopyUtils.copy(data, response.getOutputStream());
				oldFile.delete();
				reportService.updateJobTrackingTable(Id,newFilePath);
			} else	{
				File oldFile = new File(filePath);
				if (-1 == fileName.lastIndexOf(".")) {
					extensionType = "force-download";
				} else {
					extensionType = fileName.substring(fileName.lastIndexOf(".") + 1);
				}
				if(extensionType != null && extensionType.equalsIgnoreCase("zip")) {
					response.setContentType("binary/data");
				} else {
					response.setContentType("application/" + extensionType);
				}
				byte[] data = FileCopyUtils.copyToByteArray(oldFile);
				//response.setContentType("application/" + extensionType);
				response.setContentLength(data.length);
				response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
				FileCopyUtils.copy(data, response.getOutputStream());
			}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "updateFileExt - ", e);
			e.printStackTrace();
		}
		logger.log(IAppLogger.INFO, "Exit: Controller - updateFileExt");
	}
	

}
