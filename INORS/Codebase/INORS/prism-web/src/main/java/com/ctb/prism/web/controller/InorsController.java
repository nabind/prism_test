package com.ctb.prism.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.constant.IEmailConstants;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.core.util.EmailSender;
import com.ctb.prism.core.util.FileUtil;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.inors.constant.InorsDownloadConstants;
import com.ctb.prism.inors.service.IInorsService;
import com.ctb.prism.inors.transferobject.BulkDownloadTO;
import com.ctb.prism.inors.transferobject.GrtTO;
import com.ctb.prism.inors.transferobject.InvitationCodeTO;
import com.ctb.prism.inors.util.InorsDownloadUtil;
import com.ctb.prism.inors.util.PdfGenerator;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.report.service.IReportService;
import com.ctb.prism.report.transferobject.GroupDownloadStudentTO;
import com.ctb.prism.report.transferobject.GroupDownloadTO;
import com.ctb.prism.report.transferobject.IReportFilterTOFactory;
import com.ctb.prism.report.transferobject.InputControlTO;
import com.ctb.prism.report.transferobject.JobTrackingTO;
import com.ctb.prism.report.transferobject.ReportTO;
import com.ctb.prism.web.jms.JmsMessageProducer;
import com.ctb.prism.web.util.JsonUtil;

/**
 * @author TCS
 * 
 */
// @PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Controller
public class InorsController {
	private static final IAppLogger logger = LogFactory.getLoggerInstance(InorsController.class.getName());

	@Autowired
	private IInorsService inorsService;

	@Autowired
	private IAdminService adminService;

	@Autowired
	private IReportService reportService;

	@Autowired
	private IReportFilterTOFactory reportFilterFactory;

	@Autowired
	private ReportController reportController;

	@Autowired
	private IPropertyLookup propertyLookup;

	@Autowired
	private JmsMessageProducer messageProducer;

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
			modelAndView.addObject("groupList", groupList);
			grpList = JsonUtil.convertToJsonAdmin(groupList);
		}

		logger.log(IAppLogger.DEBUG, grpList);
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
		String extensionType = "";
		if (-1 == fileName.lastIndexOf(".")) {
			extensionType = ".pdf";
		} else {
			extensionType = fileName.substring(fileName.lastIndexOf(".") + 1);
		}
		File file = new File(filePath);
		try {
			byte[] data = FileCopyUtils.copyToByteArray(file);
			response.setContentType("application/" + extensionType);
			response.setContentLength(data.length);
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			FileCopyUtils.copy(data, response.getOutputStream());
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
		logger.log(IAppLogger.INFO, "Enter: downloadCandicateReport()");
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
			String docName = CustomStringUtil.appendString(currentUser, " ", Utils.getDateTime(), "_Querysheet.pdf");

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
			Object reportFilterTO = reportService
					.getDefaultFilter(allInputControls, currentUser, assessmentId, "", reportUrl, (Map<String, Object>) request.getSession().getAttribute("inputControls"));
			Map<String, Object> parameters = reportController.getReportParametersFromRequest(request, allInputControls, reportFilterFactory.getReportFilterTO(), currentOrg, null);
			// reportController.getReportParameter(allInputControls, reportFilterTO, false, request);

			String mainQuery = jasperReport.getDatasets()[0].getQuery().getText(); // jasperReport.getQuery().getText();

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
				logger.log(IAppLogger.WARN, "Some error occuered getting cascading values.", e);
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
			replacableParams.put("$P!{p_Start_Test_Date}", request.getParameter("p_Start_Test_Date"));
			replacableParams.put("$P!{p_End_Test_Date}", request.getParameter("p_End_Test_Date"));
			replacableParams.put(CustomStringUtil.getJasperParameterString("p_Grade_Id"), "112");
			replacableParams.put(CustomStringUtil.getJasperParameterString("p_Product_Id"), "1001");

			String changedObject = "p_Ethnicities,p_Roster_Subtest_MultiSelect";
			/*
			 * List<ObjectValueTO> allStudents = reportService.getValuesOfSingleInput( mainQuery, currentUser, changedObject, "", replacableParams, reportFilterTO, true);
			 */

			BulkDownloadTO bulkDownloadTO = new BulkDownloadTO();
			bulkDownloadTO.setQuerysheetFile(docName);
			bulkDownloadTO.setUdatedBy((currentUserId == null) ? 0 : Long.valueOf(currentUserId));
			bulkDownloadTO.setUsername(currentUser);

			bulkDownloadTO.setCustomerId(customer);
			bulkDownloadTO.setReportUrl(reportUrl);
			bulkDownloadTO.setRequestType(request.getParameter("fileType"));
			bulkDownloadTO.setDownloadMode(request.getParameter("mode"));
			bulkDownloadTO.setStartDate(startDate);
			bulkDownloadTO.setEndDate(endDate);
			bulkDownloadTO.setSelectedNodes(CustomStringUtil.appendString("&LoggedInUserName=", currentUser, "&p_Student_Bio_Id=", "-1", "&p_Form_Id=", "-1", "&p_Product_Id=", "1001", "&p_Org_Id=",
					currentOrg, "&p_Grade_Id=", "112", "&p_Admin_Name=", "1001", "&p_User_Type=", "REGULAR", "&p_Is_Bulk=", "1", "&p_Start_Test_Date=" + bulkDownloadTO.getStartDate(),
					"&p_End_Test_Date=", bulkDownloadTO.getEndDate()));

			bulkDownloadTO = inorsService.createJob(bulkDownloadTO);

			String querysheetFile = PdfGenerator.generateQuerysheetForCR(bulkDownloadTO, propertyLookup);

			logger.log(IAppLogger.INFO, "sending messsage --------------- ");
			messageProducer.sendJobForProcessing(bulkDownloadTO.getJobId());

			if (bulkDownloadTO.getJobId() != 0)
				status = "Success";

			response.setContentType("application/json");
			response.getWriter().write("");
			response.getWriter().write("{\"status\":\"" + status + "\"}");
		} catch (Exception ex) {
			logger.log(IAppLogger.ERROR, ex.getMessage(), ex);
			ex.printStackTrace();
			response.getWriter().write("{\"status\":\"" + status + "\"}");
		}
		logger.log(IAppLogger.INFO, "Exit: downloadCandicateReport()");
		return null;
	}

	@RequestMapping(value = "/icLetterDownloads", method = RequestMethod.GET)
	public ModelAndView icLetterDownloads(HttpServletRequest request, HttpServletResponse response) {
		logger.log(IAppLogger.INFO, "Enter: icLetterDownloads()");
		request.setAttribute("icDownload", "true");
		logger.log(IAppLogger.INFO, "Exit: icLetterDownloads()");
		return groupDownloadForm(request, response);
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
		paramMap.put("MESSAGE_TYPE", IApplicationConstants.REPORT_SPECIFIC_MESSAGE_TYPE);
		paramMap.put("MESSAGE_NAME", IApplicationConstants.GROUP_DOWNLOAD_INSTRUCTION);
		String groupDownloadInstructionMessage = reportService.getSystemConfigurationMessage(paramMap);
		if ("true".equals((String) request.getAttribute("icDownload"))) {
			modelAndView = new ModelAndView("inors/icLetterDownloads");
		} else {
			modelAndView = new ModelAndView("inors/groupDownloads");
		}
		String reportUrl = (String) request.getParameter("reportUrl");
		logger.log(IAppLogger.INFO, "reportUrl=" + reportUrl);

		String currentUser = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSER);
		try {
			Map<String, Object> parameters = null;
			// get all input controls for report
			List<InputControlTO> allInputControls = reportController.getInputControlList(reportUrl);

			// get default parameters for logged-in user
			Object reportFilterTO = reportService.getDefaultFilter(allInputControls, currentUser, request.getParameter("assessmentId"), "", reportUrl, (Map<String, Object>) request.getSession()
					.getAttribute("inputControls"));

			// get parameter values for report
			parameters = reportController.getReportParameter(allInputControls, reportFilterTO, false, request);

			String testAdministrationVal = (String) request.getParameter("p_test_administration");
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

			String fileName = (String) request.getParameter("fileName");
			if ((fileName == null) || (fileName.equalsIgnoreCase("null"))) {
				fileName = (String) request.getSession().getAttribute("FILE_NAME_GD");
				if ((fileName == null) || (fileName.equalsIgnoreCase("null"))) {
					fileName = fileNameConventionGD("CP", currentUser, fileName, groupFile)[0];
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

			modelAndView.addObject("testAdministrationVal", testAdministrationVal);
			modelAndView.addObject("testProgram", testProgram);
			modelAndView.addObject("corpDiocese", corpDiocese);
			modelAndView.addObject("school", school);
			modelAndView.addObject("klass", klass);
			modelAndView.addObject("grade", grade);
			modelAndView.addObject("groupFile", groupFile);
			modelAndView.addObject("collationHierarchy", collationHierarchy);
			modelAndView.addObject("fileName", fileName);
			modelAndView.addObject("email", email);
			request.getSession().setAttribute(IApplicationConstants.REPORT_TYPE_CUSTOM + "parameters" + reportUrl, parameters);

			List<GroupDownloadStudentTO> studentList = new ArrayList<GroupDownloadStudentTO>();
			GroupDownloadTO to = new GroupDownloadTO();
			to.setSchool(school);
			to.setKlass(klass);
			to.setGrade(grade);
			to.setTestProgram(testProgram);
			to.setTestAdministrationVal(testAdministrationVal);
			to.setDistrict(corpDiocese);
			studentList = populateStudentTableGD(to);
			logger.log(IAppLogger.INFO, "Students: " + studentList.size() + "\n" + JsonUtil.convertToJsonAdmin(studentList));
			modelAndView.addObject("studentList", studentList);
			modelAndView.addObject("studentCount", studentList.size());
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
			e.printStackTrace();
		}
		modelAndView.addObject("groupDownloadInstructionMessage", groupDownloadInstructionMessage);
		modelAndView.addObject("reportUrl", reportUrl);
		logger.log(IAppLogger.INFO, "Exit: groupDownloadForm()");
		return modelAndView;
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
	@RequestMapping(value = "/groupDownloadFunction", method = RequestMethod.GET)
	public @ResponseBody
	String groupDownloadFunction(@ModelAttribute GroupDownloadTO to, HttpServletRequest request, HttpServletResponse response) throws SystemException {
		logger.log(IAppLogger.INFO, "Enter: groupDownloadFunction()");
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
				String adminId = (String) request.getSession().getAttribute(IApplicationConstants.ADMIN_YEAR);
				String customerId = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
				logger.log(IAppLogger.INFO, "currentUserId = " + currentUserId);
				logger.log(IAppLogger.INFO, "adminId = " + adminId);
				logger.log(IAppLogger.INFO, "customerId = " + customerId);
				to.setUserId(currentUserId);
				to.setAdminId(adminId);
				to.setCustomerId(customerId);
				jobTrackingId = reportService.createJobTracking(to);
			}
			logger.log(IAppLogger.INFO, "jobTrackingId = " + jobTrackingId);
			handler = "success";
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage());
			e.printStackTrace();
		}
		String jsonString = CustomStringUtil.appendString("{\"handler\": \"", handler, "\", \"type\": \"", type, "\", \"downloadFileName\": \"", downloadFileName, "\", \"jobTrackingId\": \"",
				jobTrackingId, "\"}");
		logger.log(IAppLogger.INFO, "groupDownloadFunction(): " + jsonString);

		// TODO : JMS Integration for processGroupDownload() method
		processGroupDownload(jobTrackingId);
		logger.log(IAppLogger.INFO, "Exit: groupDownloadFunction()");
		return jsonString;
	}

	/**
	 * This method will be called using JMS.
	 * 
	 * @param processId
	 * @param button
	 * @param fileName
	 * @param filePaths
	 */
	private void processGroupDownload(String processId) {
		logger.log(IAppLogger.INFO, "Enter: processGroupDownload()");
		Map<String, String> paramMap = new HashMap<String, String>();
		String requestFileName = null;
		String jobLog = null;
		String jobStatus = IApplicationConstants.JOB_STATUS.IP.toString();
		Long fileSize = null;
		String clobStr = reportService.getProcessDataGD(processId);
		logger.log(IAppLogger.INFO, "Clob Data is : " + clobStr);
		String[] tokens = clobStr.split("\\|");
		List<String> filePaths = new ArrayList<String>();
		// See : ReportDaoImpl.createJobTracking()
		if (tokens.length == 5) { // IMPORTANT : Number of parameters needed in this method.
			String button = tokens[0];
			String fileName = tokens[1];
			String groupFile = tokens[2];
			String school = tokens[3];
			String students = tokens[4];
			logger.log(IAppLogger.INFO, "button: " + button);
			logger.log(IAppLogger.INFO, "fileName: " + fileName);
			logger.log(IAppLogger.INFO, "groupFile: " + groupFile);
			logger.log(IAppLogger.INFO, "school: " + school);
			logger.log(IAppLogger.INFO, "students: " + students);

			GroupDownloadTO to = new GroupDownloadTO();
			to.setGroupFile(groupFile);
			to.setStudents(students);
			filePaths = reportService.getGDFilePaths(to);
			logger.log(IAppLogger.INFO, "filePaths: " + filePaths.size());

			if (!filePaths.isEmpty()) {
				try {
					if ("CP".equals(button)) {
						// The default naming convention is: Username + Date Time Stamp
						String pdfFileName = fileName + ".pdf";
						String zipFileName = fileName + ".zip";
						logger.log(IAppLogger.INFO, "zipFileName(CP): " + zipFileName);

						// Merge Pdf files
						byte[] input = FileUtil.getMergedPdfBytes(filePaths);

						// Create Pdf file in disk
						FileUtil.createFile(pdfFileName, input);

						// Now read the pdf file from disk
						byte[] data = FileCopyUtils.copyToByteArray(new File(pdfFileName));

						// Zip the pdf file
						byte[] zipData = FileUtil.zipBytes(zipFileName, data);

						// Create Zip file in disk
						FileUtil.createFile(zipFileName, zipData);

						// Delete the Pdf file from disk
						logger.log(IAppLogger.INFO, "temp pdf file deleted = " + new File(pdfFileName).delete());

						requestFileName = zipFileName;
						fileSize = new Long(zipData.length);
						jobStatus = IApplicationConstants.JOB_STATUS.CO.toString();
						jobLog = "Asynchoronous Combined Pdf";
					} else if ("SP".equals(button)) {
						// TODO : convention implementation
						Long orgNodeId = Long.parseLong(school);
						logger.log(IAppLogger.INFO, "orgNodeId: " + orgNodeId);
						fileName = reportService.getConventionalFileNameGD(orgNodeId);
						String[] fileNames = fileNameConventionGD(button, "", fileName, groupFile);
						String zipFileName = fileNames[0] + ".zip";
						logger.log(IAppLogger.INFO, "zipFileName(SP): " + zipFileName);

						// Create Zip file in disk from all the pdf files
						FileUtil.createZipFile(zipFileName, filePaths);

						requestFileName = zipFileName;
						fileSize = FileUtil.fileSize(zipFileName);
						jobStatus = IApplicationConstants.JOB_STATUS.CO.toString();
						jobLog = "Asynchoronous Separate Pdfs";
					}
				} catch (FileNotFoundException e) {
					jobLog = e.getMessage();
					e.printStackTrace();
				} catch (IOException e) {
					jobLog = e.getMessage();
					e.printStackTrace();
				}
			} else {
				jobLog = "No File to download";
				logger.log(IAppLogger.INFO, jobLog);
			}
		} else {
			jobLog = "Invalid Clob (Student) Data to Process";
			logger.log(IAppLogger.WARN, jobLog);
		}

		paramMap.put("requestFileName", requestFileName);
		paramMap.put("jobLog", jobLog);
		paramMap.put("jobStatus", jobStatus);
		if (fileSize == null) {
			fileSize = 0L;
		}
		paramMap.put("fileSize", fileSize.toString());
		paramMap.put("jobId", processId);
		int updateCount = reportService.updateJobTracking(paramMap);
		logger.log(IAppLogger.INFO, "updateCount: " + updateCount);
		logger.log(IAppLogger.INFO, "Exit: processGroupDownload()");
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
			File pdfFile = new File(fileName);
			if (!pdfFile.canRead()) {
				logger.log(IAppLogger.WARN, "No Read Permission");
			} else {
				logger.log(IAppLogger.INFO, "Can Read File");
			}
			// Now read the pdf file from disk
			byte[] data = FileCopyUtils.copyToByteArray(new FileInputStream(fileName));

			// Zip the pdf file
			byte[] zipData = FileUtil.zipBytes(fileName, data);

			// Download the file
			FileUtil.browserDownload(response, zipData, zipFileName);

			// Send email
			if ((email != null) && (!email.isEmpty())) {
				notificationMailGD(email);
			}

			// Delete the zip file from disk
			logger.log(IAppLogger.INFO, "temp zip file deleted = " + new File(zipFileName).delete());
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage());
			e.printStackTrace();
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: downloadZippedPdf(): " + String.valueOf(t2 - t1) + "ms");
		}
	}

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
			EmailSender.sendMail(prop, email, null, null, subject, mailBody);
			logger.log(IAppLogger.INFO, "Email sent to : " + email);
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Unable to send Email: " + e.getMessage());
			e.printStackTrace();
		}
		logger.log(IAppLogger.INFO, "Exit: notificationMailGD()");
	}

	/**
	 * @param button
	 * @param currentUser
	 * @param fileName
	 * @param groupFile
	 * @return
	 */
	private String[] fileNameConventionGD(String button, String currentUser, String fileName, String groupFile) {
		logger.log(IAppLogger.INFO, "Enter: fileNameConventionGD()");
		if ((groupFile == null) || (groupFile.equalsIgnoreCase("null"))) {
			groupFile = "";
		}
		String[] generatedFileNames = new String[2];
		if ("CP".equals(button)) {
			if ((fileName != null) && (!fileName.equalsIgnoreCase("null"))) {
				generatedFileNames[0] = fileName;
			} else {
				if (!groupFile.isEmpty()) {
					generatedFileNames[0] = CustomStringUtil.appendString(currentUser, " ", Utils.getDateTime(), " ", groupFile);
				} else {
					generatedFileNames[0] = CustomStringUtil.appendString(currentUser, " ", Utils.getDateTime());
				}
			}
		} else if ("SP".equals(button)) {
			if ("ISR".equals(groupFile)) {
				generatedFileNames[0] = CustomStringUtil.appendString("1-", fileName, groupFile);
			} else if ("IPR".equals(groupFile)) {
				generatedFileNames[0] = CustomStringUtil.appendString("1-", fileName, groupFile);
			} else if ("BOTH".equals(groupFile)) {
				generatedFileNames[0] = CustomStringUtil.appendString("1a-", fileName, ".ISR");
				generatedFileNames[1] = CustomStringUtil.appendString("1b-", fileName, ".IPR");
			}
		}
		logger.log(IAppLogger.INFO, "generatedFileNames=" + generatedFileNames);
		logger.log(IAppLogger.INFO, "Exit: fileNameConventionGD()");
		return generatedFileNames;
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
		long currCustomer = (customer == null) ? 0 : Long.valueOf(customer);
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

						to.setId((obj.getValue() == null) ? 0 : Long.valueOf(obj.getValue()));
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
		long currCustomer = (customer == null) ? 0 : Long.valueOf(customer);
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
			bulkDownloadTO.setUdatedBy((currentUserId == null) ? 0 : Long.valueOf(currentUserId));
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
		String productName = "";
		try {
			if ((testAdministrationVal != null) && ("null".equalsIgnoreCase(testAdministrationVal))) {
				productName = inorsService.getProductNameById(Long.parseLong(testAdministrationVal));
			}
		} catch (Exception e) {
			logger.log(IAppLogger.WARN, e.getMessage());
			e.printStackTrace();
		}
		logger.log(IAppLogger.INFO, "productName=" + productName);

		modelAndView.addObject("testAdministrationVal", testAdministrationVal);
		modelAndView.addObject("testAdministrationText", productName);
		modelAndView.addObject("testProgram", testProgram);
		modelAndView.addObject("corpDiocese", corpDiocese);
		modelAndView.addObject("school", school);

		String reportUrl = (String) request.getParameter("reportUrl");
		String currentUser = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSER);
		Map<String, Object> parameters = null;
		List<InputControlTO> allInputControls = reportController.getInputControlList(reportUrl);

		// get default parameters for logged-in user
		Object reportFilterTO = reportService.getDefaultFilter(allInputControls, currentUser, request.getParameter("assessmentId"), "", reportUrl, (Map<String, Object>) request.getSession()
				.getAttribute("inputControls"));

		// get parameter values for report
		parameters = reportController.getReportParameter(allInputControls, reportFilterTO, false, request);
		logger.log(IAppLogger.INFO, "parameters = " + parameters);
		request.getSession().setAttribute(IApplicationConstants.REPORT_TYPE_CUSTOM + "parameters" + reportUrl, parameters);
		logger.log(IAppLogger.INFO, "Exit: grtICFileForm()");
		return modelAndView;
	}

	/**
	 * Downloads GRT or Invitation Code files from the browser based on user inputs.
	 * 
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
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

		String productName = "";
		try {
			productName = inorsService.getProductNameById(Long.parseLong(testAdministrationVal));
		} catch (Exception e) {
			logger.log(IAppLogger.WARN, e.getMessage());
			e.printStackTrace();
		}
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
		String fileName = "";
		String zipFileName = "";
		if ("IC".equals(type)) {
			List<InvitationCodeTO> icList = (List<InvitationCodeTO>) inorsService.getDownloadData(paramMap);
			data = InorsDownloadUtil.getICBytes(year, icList, ",");
			fileName = InorsDownloadConstants.IC_FILE_PATH;
			zipFileName = InorsDownloadConstants.IC_ZIP_FILE_PATH;
		} else if ("GRT".equals(type)) {
			List<GrtTO> grtList = (List<GrtTO>) inorsService.getDownloadData(paramMap);
			data = InorsDownloadUtil.getGRTBytes(year, grtList, ",");
			fileName = InorsDownloadConstants.GRT_FILE_PATH;
			zipFileName = InorsDownloadConstants.GRT_ZIP_FILE_PATH;
		}
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
	 * @param request
	 * @return
	 * @deprecated
	 */
	@RequestMapping(value = "/populateDistrictGrt", method = RequestMethod.GET)
	public @ResponseBody
	String populateDistrictGrt(HttpServletRequest request) {
		logger.log(IAppLogger.INFO, "Enter: populateDistrictGrt()");
		long t1 = System.currentTimeMillis();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("testProgram", request.getParameter("testProgram"));
		List<com.ctb.prism.core.transferobject.ObjectValueTO> districtList = null;
		String jsonString = "";
		try {
			districtList = inorsService.populateDistrictGrt(paramMap);
			jsonString = JsonUtil.convertToJsonAdmin(districtList);
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage());
			e.printStackTrace();
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: populateDistrictGrt(): " + String.valueOf(t2 - t1) + "ms");
		}
		return jsonString;
	}

	/**
	 * Creates a json for all the school under a corporation.
	 * 
	 * @param request
	 * @return
	 * @deprecated
	 */
	@RequestMapping(value = "/populateSchoolGrt", method = RequestMethod.GET)
	public @ResponseBody
	String populateSchoolGrt(HttpServletRequest request) {
		logger.log(IAppLogger.INFO, "Enter: populateSchoolGrt()");
		long t1 = System.currentTimeMillis();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("districtId", request.getParameter("districtId"));
		paramMap.put("testProgram", request.getParameter("testProgram"));
		List<com.ctb.prism.core.transferobject.ObjectValueTO> schoolList = null;
		String jsonString = "";
		try {
			schoolList = inorsService.populateSchoolGrt(paramMap);
			jsonString = JsonUtil.convertToJsonAdmin(schoolList);
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage());
			e.printStackTrace();
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, CustomStringUtil.appendString("Exit: populateSchoolGrt(): ", String.valueOf(t2 - t1), "ms"));
		}
		return jsonString;
	}

	/**
	 * @author Amit Dhara,Arunava Datta Scheduler every night @ 1 AM Group Download files deletion and job tracking update if exp date >= SYSDATE Scheduler
	 * @RequestMapping(value = "/doSomething", method = RequestMethod.GET) -- > For testing enable this and hit the URL as doSomething.do
	 * @throws Exception
	 */

	@Scheduled(cron = "* * 1 * * ?")
	public void doSomething() throws Exception {
		logger.log(IAppLogger.INFO, " START CRON JOB @ 1 AM ----- f r o m  Scheduled method for GROUP DOWNLOAD FILES --------------- ");
		String gdfExpiryTime = propertyLookup.get("gdfExpiryTime");
		reportService.deleteScheduledGroupFiles(gdfExpiryTime);
		logger.log(IAppLogger.INFO, "END CRON JOB @ 1 AM ----- f r o m  Scheduled method for GROUP DOWNLOAD FILES--------------- ");
	}

}
