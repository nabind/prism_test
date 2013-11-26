package com.ctb.prism.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
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
import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.core.util.FileUtil;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.inors.constant.InorsDownloadConstants;
import com.ctb.prism.inors.service.IInorsService;
import com.ctb.prism.inors.transferobject.BulkDownloadTO;
import com.ctb.prism.inors.transferobject.GrtTO;
import com.ctb.prism.inors.transferobject.InvitationCodeTO;
import com.ctb.prism.inors.util.InorsDownloadUtil;
import com.ctb.prism.inors.util.PdfGenerator;
import com.ctb.prism.report.service.IReportService;
import com.ctb.prism.report.transferobject.GroupDownload;
import com.ctb.prism.report.transferobject.IReportFilterTOFactory;
import com.ctb.prism.report.transferobject.InputControlTO;
import com.ctb.prism.report.transferobject.ReportTO;
import com.ctb.prism.report.transferobject.JobTrackingTO;
import com.ctb.prism.web.jms.JmsMessageProducer;
import com.ctb.prism.web.util.JsonUtil;
import com.ctb.prism.login.transferobject.UserTO;
/**
 * @author TCS
 * 
 */
//@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Controller
public class InorsController {
	private static final IAppLogger logger = LogFactory
			.getLoggerInstance(InorsController.class.getName());

	@Autowired private IInorsService inorsService;
	
	@Autowired private IAdminService adminService;
	
	@Autowired private IReportService reportService;
		
	@Autowired private IReportFilterTOFactory reportFilterFactory;
	
	@Autowired private ReportController reportController;
	
	@Autowired private IPropertyLookup propertyLookup;
	
	@Autowired private JmsMessageProducer messageProducer;
	
	//Arunava Datta for Group Download Listing
	@RequestMapping(value = "/groupDownloadFiles", method = RequestMethod.GET)
	public ModelAndView groupDownloadFiles(HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		ModelAndView modelAndView = new ModelAndView("inors/groupDownloadFiles");
		UserTO loggedinUserTO = (UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("loggedinUserTO", loggedinUserTO);
		List<JobTrackingTO> groupList = reportService.getAllGroupDownloadFiles(paramMap);
		if(groupList != null && !groupList.isEmpty()) {
			modelAndView.addObject("groupList", groupList);
		}
		String grpList = JsonUtil.convertToJsonAdmin(groupList);
		logger.log(IAppLogger.DEBUG, grpList);
		return modelAndView;
	}
	@RequestMapping(value="/deleteGroupDownloadFiles", method=RequestMethod.GET )
	public ModelAndView deleteGroupDownloadFiles(HttpServletRequest req, HttpServletResponse res) {
		try {
			logger.log(IAppLogger.INFO, "Enter: InorsController - deleteGroupDownloadFiles");
			String Id = (String)req.getParameter("jobId");
			logger.log(IAppLogger.INFO, "Id of the file to delete is ........................."+Id);
			boolean isDeleted = reportService.deleteGroupFiles(Id);
			res.setContentType("text/plain");
			String status = "Fail";
			if(isDeleted) {
				status = "Success";
			}
			res.getWriter().write( "{\"status\":\""+status+"\"}" );

			logger.log(IAppLogger.INFO, "Enter: InorsController - deleteGroupDownloadFiles");

		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error deleting Group File", e);
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
	public ModelAndView bulkCandidateReportDownload(HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		ModelAndView modelAndView = new ModelAndView("inors/bulkCandidateReport");
		modelAndView.addObject("reportUrl", "/public/TASC/Reports/TASC_Org_Hier/Candidate_Report_files");
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
	public @ResponseBody String downloadCandicateReport(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String status = "Error";
		try {
			String startDate = request.getParameter("p_Start_Date");
			String endDate = request.getParameter("p_End_Date");
			String reportUrl = request.getParameter("reportUrl");
			String currentUser = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSER);
			String currentOrg = (String) request.getSession().getAttribute(IApplicationConstants.CURRORG);
			String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
			String currentUserId = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSERID);
			//String currentOrgLevel = (String) request.getSession().getAttribute(IApplicationConstants.CURRORGLVL);
			String docName = CustomStringUtil.appendString(currentUser, " " ,Utils.getDateTime(), "_Querysheet.pdf");
			
			
			/**/
			List<InputControlTO> allInputControls = reportController.getInputControlList(reportUrl);
			
			// get compiled jasper report
			JasperReport jasperReport = null;
			boolean mainReportPresent = false;
			
			//fetch report list 
			List<ReportTO> reportList = reportController.getCompliledJrxmlList(reportUrl);
			
			if(reportList != null && !reportList.isEmpty()) {
				for(ReportTO reportTo : reportList) {
					if(reportTo.isMainReport()) {
						jasperReport = reportTo.getCompiledReport();
						mainReportPresent = true;
						break;
					}
				}
				
				if(!mainReportPresent) {
					jasperReport = reportList.get(0).getCompiledReport();
				}
			} 
			String assessmentId = request.getParameter("assessmentId");
			Object reportFilterTO = reportService.getDefaultFilter(allInputControls, currentUser, assessmentId, "", reportUrl);
			Map<String, Object> parameters = reportController.getReportParametersFromRequest(
					request, allInputControls, reportFilterFactory.getReportFilterTO(), currentOrg, null);
				//reportController.getReportParameter(allInputControls, reportFilterTO, false, request);
			
			
			String mainQuery = jasperReport.getDatasets()[0].getQuery().getText(); //jasperReport.getQuery().getText();
			
			// replace all parameters with jasper parameter string
			Map<String, String> replacableParams = new HashMap<String, String>();
			Iterator it = parameters.entrySet().iterator();
			try {
				while (it.hasNext()) {
				    Map.Entry pairs = (Map.Entry)it.next();
				    if(pairs.getValue() != null && pairs.getValue() instanceof String) {
				    	replacableParams.put(CustomStringUtil.getJasperParameterString((String) pairs.getKey()), 
				    			(String) pairs.getValue());
				    } else if(pairs.getValue() != null && pairs.getValue() instanceof List) {
				    	String val = pairs.getValue().toString();
				    	val = val.substring(1, val.length()-1);
				    	replacableParams.put(CustomStringUtil.getJasperParameterString((String) pairs.getKey()),
				    			val);
				    }
				}
			} catch (Exception e) {
				logger.log(IAppLogger.WARN, "Some error occuered getting cascading values.",e);
			}
			
			it = request.getParameterMap().entrySet().iterator();
			while (it.hasNext()) {
			    try {
					@SuppressWarnings("rawtypes")
					Map.Entry pairs = (Map.Entry)it.next();
					if(pairs.getValue() != null && pairs.getValue() instanceof String) {
						if(!replacableParams.containsKey(pairs.getKey())) {
							replacableParams.put(CustomStringUtil.getJasperParameterString((String) pairs.getKey()), 
					    			(String) pairs.getValue());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			replacableParams.put("$P!{p_Start_Test_Date}", request.getParameter("p_Start_Test_Date"));
			replacableParams.put("$P!{p_End_Test_Date}", request.getParameter("p_End_Test_Date"));
			replacableParams.put(CustomStringUtil.getJasperParameterString("p_Grade_Id"), "112");
			replacableParams.put(CustomStringUtil.getJasperParameterString("p_Product_Id"), "1001");
			
			String changedObject = "p_Ethnicities,p_Roster_Subtest_MultiSelect";
			/*List<ObjectValueTO> allStudents = reportService.getValuesOfSingleInput(
					mainQuery, currentUser, changedObject, "", replacableParams, reportFilterTO, true);
			*/
			
			
			BulkDownloadTO bulkDownloadTO = new BulkDownloadTO();
			bulkDownloadTO.setQuerysheetFile(docName);
			bulkDownloadTO.setUdatedBy((currentUserId == null)? 0 : Long.valueOf(currentUserId));
			bulkDownloadTO.setUsername(currentUser);
			
			bulkDownloadTO.setCustomerId(customer);
			bulkDownloadTO.setReportUrl(reportUrl);
			bulkDownloadTO.setRequestType(request.getParameter("fileType"));
			bulkDownloadTO.setDownloadMode(request.getParameter("mode"));
			bulkDownloadTO.setStartDate(startDate);
			bulkDownloadTO.setEndDate(endDate);
			bulkDownloadTO.setSelectedNodes(CustomStringUtil.appendString(
					"&LoggedInUserName=", currentUser,
					"&p_Student_Bio_Id=", "-1",
					"&p_Form_Id=", "-1",
					"&p_Product_Id=", "1001",
					"&p_Org_Id=", currentOrg, 
					"&p_Grade_Id=", "112",
					"&p_Admin_Name=", "1001",
					"&p_User_Type=", "REGULAR",
					"&p_Is_Bulk=", "1",
					"&p_Start_Test_Date=" + bulkDownloadTO.getStartDate(), 
					"&p_End_Test_Date=", bulkDownloadTO.getEndDate()));
			
			bulkDownloadTO = inorsService.createJob(bulkDownloadTO);
			
			String querysheetFile = PdfGenerator.generateQuerysheetForCR(bulkDownloadTO, propertyLookup);
			
			logger.log(IAppLogger.INFO, "sending messsage --------------- ");
			messageProducer.sendJobForProcessing(bulkDownloadTO.getJobId());
			
			if(bulkDownloadTO.getJobId() != 0) status = "Success";
			
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
	public ModelAndView icLetterDownloads(@ModelAttribute("groupDownload") BulkDownloadTO bulkDownloadTO, 
			HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute("icDownload", "true");
		return getOrganizations(bulkDownloadTO, request, response);
	}
	
	/**
	 * Entry method for group download screen
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/groupDownloads", method = RequestMethod.GET)
	public ModelAndView getOrganizations(@ModelAttribute("groupDownload") BulkDownloadTO bulkDownloadTO, 
			HttpServletRequest request,
			HttpServletResponse response) {
		
		
		ModelAndView modelAndView = null;
		boolean icDownload = false;
		if("true".equals((String) request.getAttribute("icDownload"))) {
			modelAndView = new ModelAndView("inors/icLetterDownloads");
			icDownload = true;
		} else {
			modelAndView = new ModelAndView("inors/groupDownloads");
		}
		String reportUrl = (String) request.getParameter("reportUrl");
		
		String currentUser = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSER);
		String currentOrg =(String) request.getSession().getAttribute(IApplicationConstants.CURRORG);
		String email = (String) request.getSession().getAttribute(IApplicationConstants.EMAIL);
		String orgJsonString;
		String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
		long currCustomer = (customer == null)? 0 : Long.valueOf(customer);
		List<OrgTreeTO> orgTreeTOs = new ArrayList<OrgTreeTO>();
		try {
			String adminYear = (String) request.getParameter("AdminYear");
			Map<String, Object> parameters = null;
			if(IApplicationConstants.TRUE.equals(request.getParameter("filter"))) {
				@SuppressWarnings("unchecked")
				List<InputControlTO> allInputControls = (List<InputControlTO>) request.getSession().getAttribute(
						IApplicationConstants.REPORT_TYPE_CUSTOM + "InputControls" + reportUrl);
				parameters = reportController.getReportParametersFromRequest(request, allInputControls, reportFilterFactory.getReportFilterTO(), currentOrg, "false");
			} else {
				// get all input controls for report
				List<InputControlTO> allInputControls = reportController.getInputControlList(reportUrl);
				
				// get default parameters for logged-in user
				Object reportFilterTO = reportService.getDefaultFilter(allInputControls, currentUser, request.getParameter("assessmentId"), "", reportUrl);

				// get parameter values for report
				parameters = reportController.getReportParameter(allInputControls, reportFilterTO, false, request);
			}
			adminYear =  (String) parameters.get("p_adminYear");
			String school = (String) parameters.get("p_school");
			String corp = (String) parameters.get("p_corp");
			String orgClass = (String) parameters.get("p_class");
			modelAndView.addObject("rootOrgId", corp);
			
			request.getSession().setAttribute(IApplicationConstants.REPORT_TYPE_CUSTOM + "parameters" + reportUrl, parameters);
					
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
		} 
		
		if(request.getSession().getAttribute("retainBulkDownloadTO") != null) {
			BulkDownloadTO tempTo = (BulkDownloadTO) request.getSession().getAttribute("retainBulkDownloadTO");
			bulkDownloadTO.setFileName(tempTo.getFileName());
			bulkDownloadTO.setEmail(tempTo.getEmail());
			bulkDownloadTO.setGroupFile(tempTo.getGroupFile());
			bulkDownloadTO.setCollationHierarchy(tempTo.getCollationHierarchy());
		} else {
			if(icDownload) {
				bulkDownloadTO.setFileName(CustomStringUtil.appendString(currentUser, " ", Utils.getDateTime(), " ", "IC"));
			} else {
				bulkDownloadTO.setFileName(CustomStringUtil.appendString(currentUser, " ", Utils.getDateTime()));
			}
			bulkDownloadTO.setEmail(email);
		}
		bulkDownloadTO.setIstepPlus(true); // TODO set this value based on administration selected
		
		
		modelAndView.addObject("reportUrl", reportUrl);
		return modelAndView;
	}
	
	/**
	 * Get school -class - student hierarchy for tree
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/groupDownloadHierarchy", method = RequestMethod.GET)
	public @ResponseBody
	String getTenantHierarchy(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		List<OrgTreeTO> orgTreeTOs = new ArrayList<OrgTreeTO>();
		String orgJsonString;
		String currentUser = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSER);
		String currentOrg = (String) request.getSession().getAttribute( IApplicationConstants.CURRORG);
		String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
		long currCustomer = (customer == null)? 0 : Long.valueOf(customer);
		try {
			String nodeid = (String) request.getParameter("tenantId");
			String nodeLevel = (String) request.getParameter("nodeLevel");
			String adminYear = (String) request.getParameter("AdminYear");
			String reportUrl = (String) request.getParameter("reportUrl");
			
			Map<String, Object> parameters = null;
			parameters = (Map<String, Object>) request.getSession().getAttribute(IApplicationConstants.REPORT_TYPE_CUSTOM + "parameters" + reportUrl);
			while(parameters == null) {
				logger.log(IAppLogger.WARN, "Waiting for custom report parameter .... ");
				Thread.sleep(2000);
				parameters = (Map<String, Object>) request.getSession().getAttribute(IApplicationConstants.REPORT_TYPE_CUSTOM + "parameters" + reportUrl);
			}
			adminYear =  (String) parameters.get("p_adminYear");
			String school = (String) parameters.get("p_school");
			String corp = (String) parameters.get("p_corp");
			String orgClass = (String) parameters.get("p_class");
			String grade = (String) parameters.get("p_gradeid");
			
			if (nodeid != null) {
				orgTreeTOs = adminService.getHierarchy(nodeid, adminYear, currCustomer, ("3".equals(nodeLevel) ) ? orgClass : "-1");
				
				if(orgTreeTOs != null && orgTreeTOs.isEmpty() && nodeid.indexOf("_") == -1) {
					List<com.ctb.prism.admin.transferobject.ObjectValueTO> studentList = adminService.getAllStudents(adminYear, nodeid, customer, grade);
					
					for(com.ctb.prism.admin.transferobject.ObjectValueTO obj : studentList) {
						OrgTO to = new OrgTO();
						OrgTreeTO treeTo = new OrgTreeTO();
									
						to.setId((obj.getValue() == null)? 0 : Long.valueOf(obj.getValue()));
						to.setParentTenantId(Long.valueOf(nodeid));
						to.setOrgLevel(-1);
						to.setClassName("jstree-leaf");
						
						treeTo.setState("leaf");
						treeTo.setOrgTreeId(Long.valueOf(nodeid));
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
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} 

		return null;
	}
	
	/**
	 * Download job initiation for requested students 
	 * 
	 * @param bulkDownloadTO
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/downloadBulkPdf", method = RequestMethod.GET)
	public @ResponseBody
	String downloadBulkPdf(@ModelAttribute("groupDownload") BulkDownloadTO bulkDownloadTO, 
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String currentUser = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSER);
		String currentUserId = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSERID);
		String currentOrg = (String) request.getSession().getAttribute( IApplicationConstants.CURRORG);
		String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
		long currCustomer = (customer == null)? 0 : Long.valueOf(customer);
		String status = "Error";
		try {
			//remove retain bulk value
			request.getSession().removeAttribute("retainBulkDownloadTO");
			String fileType = request.getParameter("fileType");
			String nodeid = (String) request.getParameter("tenantId");
			String adminYear = (String) request.getParameter("AdminYear");
			String reportUrl = (String) request.getParameter("reportUrl");
			String docName = CustomStringUtil.appendString(currentUser, " " ,Utils.getDateTime(), "_Querysheet.pdf");
			
			String school = null, corp = null, orgClass = null, testProgram = null, grade = null;
			if(!"CR".equals(fileType)) {
				Map<String, Object> parameters = null;
				parameters = (Map<String, Object>) request.getSession().getAttribute(IApplicationConstants.REPORT_TYPE_CUSTOM + "parameters" + reportUrl);
				while(parameters == null) {
					logger.log(IAppLogger.WARN, "Waiting for custom report parameter .... ");
					Thread.sleep(2000);
					parameters = (Map<String, Object>) request.getSession().getAttribute(IApplicationConstants.REPORT_TYPE_CUSTOM + "parameters" + reportUrl);
				}
				
				adminYear =  (String) parameters.get("p_adminYear");
				school = (String) parameters.get("p_school");
				corp = (String) parameters.get("p_corp");
				orgClass = (String) parameters.get("p_class");
				grade = (String) parameters.get("p_gradeid");
				testProgram = (String) parameters.get("p_testProgram");
			}
			bulkDownloadTO.setQuerysheetFile(docName);
			bulkDownloadTO.setUdatedBy((currentUserId == null)? 0 : Long.valueOf(currentUserId));
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
			messageProducer.sendJobForProcessing(bulkDownloadTO.getJobId());
			
			if(bulkDownloadTO.getJobId() != 0) status = "Success";
			
			response.setContentType("application/json");
			response.getWriter().write("");
			response.getWriter().write( "{\"status\":\""+status+"\"}" );
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
			response.getWriter().write( "{\"status\":\""+status+"\"}" );
		} 

		return null;
	}
	
	/**
	 * This method is to retain form values
	 * @param bulkDownloadTO
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/retainDownloadValues", method = RequestMethod.GET)
	public @ResponseBody
	String retainDownloadValues(@ModelAttribute("groupDownload") BulkDownloadTO bulkDownloadTO, 
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("retainBulkDownloadTO", bulkDownloadTO);
		logger.log(IAppLogger.INFO, "Retaining from values");
		return null;
	}
	
	@RequestMapping(value = "/gRTInvitationCodeFiles", method = RequestMethod.GET)
	public ModelAndView gRTInvitationCodeFiles(HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		ModelAndView modelAndView = new ModelAndView("inors/gRTInvitationCodeFiles");
		List<com.ctb.prism.inors.transferobject.ObjectValueTO> corporationList = inorsService.getDistricts();
		modelAndView.addObject("corporationList", corporationList);
		
		return modelAndView;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/downloadGRTInvitationCodeFiles", method = RequestMethod.GET)
	public void downloadGRTInvitationCodeFiles(HttpServletRequest request, HttpServletResponse response) {
		logger.log(IAppLogger.INFO, "Enter: InorsController.downloadGRTInvitationCodeFiles");
		Map<String, String> paramMap = new HashMap<String, String>();
		
		String type = (String) request.getParameter("type");
		String testAdministration = (String) request.getParameter("testAdministration");
		String[] tokens = testAdministration.split("~");
		String product = tokens[0];
		String term = tokens[1];
		String year = tokens[2];
		String testProgram = (String) request.getParameter("testProgram");
		String corpDiocese = (String) request.getParameter("corpDiocese");
		String school = (String) request.getParameter("school");

		String userId = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSERID);

		paramMap.put("type", type);
		paramMap.put("product", product);
		paramMap.put("term", term);
		paramMap.put("year", year);
		paramMap.put("testProgram", testProgram);
		paramMap.put("parentOrgNodeId", corpDiocese);
		paramMap.put("orgNodeId", school);
		paramMap.put("userId", userId);

		logger.log(IAppLogger.INFO, "type=" + type + ", product=" + product
				+ ", term=" + term + ", year=" + year + ", testProgram="
				+ testProgram + ", parentOrgNodeId=" + corpDiocese
				+ ", orgNodeId=" + school + ", userId=" + userId);

		byte[] data = null;
		String fileName = "";
		String zipFileName = "";
		if ("IC".equals(type)) {
			List<InvitationCodeTO> icList = (List<InvitationCodeTO>) inorsService.getDownloadData(paramMap);
			data = InorsDownloadUtil.createICByteArray(icList, ',');
			fileName = InorsDownloadConstants.IC_FILE_PATH;
			zipFileName = InorsDownloadConstants.IC_ZIP_FILE_PATH;
		} else if ("GRT".equals(type)) {
			List<GrtTO> grtList = (List<GrtTO>) inorsService.getDownloadData(paramMap);
			data = InorsDownloadUtil.createGRTByteArray(grtList, ',');
			fileName = InorsDownloadConstants.GRT_FILE_PATH;
			zipFileName = InorsDownloadConstants.GRT_ZIP_FILE_PATH;
		}
		try {
			data = FileUtil.zipBytes(fileName, data);
		} catch (IOException e) {
			logger.log(IAppLogger.ERROR, "zipBytes - ", e);
			e.printStackTrace();
		}
		response.setContentType("application/force-download");
		response.setContentLength(data.length);
		response.setHeader("Content-Disposition", "attachment; filename=" + zipFileName);
		try {
			FileCopyUtils.copy(data, response.getOutputStream());
		} catch (IOException e) {
			logger.log(IAppLogger.ERROR, "downloadGRTInvitationCodeFiles - ", e);
			e.printStackTrace();
		}
		logger.log(IAppLogger.INFO, "Exit: InorsController.downloadGRTInvitationCodeFiles");
	}
	
	@RequestMapping(value = "/populateSchool", method = RequestMethod.GET)
	public void populateSchool(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BusinessException {
		logger.log(IAppLogger.INFO, "Enter: InorsController - populateSchool()");
		long t1 = System.currentTimeMillis();
		response.setContentType("text/plain");
		Long parentOrgNodeId = Long.parseLong(request.getParameter("parentOrgNodeId"));
		List<com.ctb.prism.inors.transferobject.ObjectValueTO> schoolList = null;
		String jsonString = "";
		
		try{
			schoolList =  inorsService.populateSchool(parentOrgNodeId);
			jsonString = JsonUtil.convertToJsonAdmin(schoolList);
			logger.log(IAppLogger.INFO, "jsonString for parentOrgNodeId: " + parentOrgNodeId);
			logger.log(IAppLogger.INFO, jsonString);
			response.getWriter().write(jsonString);
	    }catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: InorsController - populateSchool() took time: "+String.valueOf(t2 - t1)+"ms");
		}
	}
	
	// every night @ 1 AM
	@Scheduled(cron= "* * 1 * * ?")
	public void doSomething() {
	    // something that should execute periodically
		logger.log(IAppLogger.INFO, "CRON JOB @ 1 AM ----- f r o m  Scheduled method --------------- ");
	}
	
}
