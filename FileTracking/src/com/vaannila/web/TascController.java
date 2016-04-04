package com.vaannila.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.vaannila.DAO.TascDAOImpl;
import com.vaannila.TO.SearchProcess;
import com.vaannila.TO.StudentDetailsTO;
import com.vaannila.TO.StudentJsonObject;
import com.vaannila.TO.TASCProcessTO;
import com.vaannila.util.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
public class TascController {
	
	String jsonStr = "";
	/**
	 * This method is to get all processes in datatable
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/process/tascProcess.htm")
	public ModelAndView tascProcess(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("Enter: tascProcess()");
		try {
			String adminid = request.getParameter("adminid");
			System.out.println("adminid = " + adminid);
			if(adminid == null) {
				adminid = (String) request.getSession().getAttribute("adminid");
			} else {
				request.getSession().setAttribute("adminid", adminid);
			}
			if(!UserController.checkLogin(request)) return new ModelAndView("welcome", "message", "Please login.");
			TascDAOImpl stageDao = new TascDAOImpl();
			System.out.println("getting processes...");
			List<TASCProcessTO> processes = stageDao.getProcess(null);
			System.out.println("got processes successfully");
			request.getSession().setAttribute("tascProcess", processes);
			convertProcessToJson(processes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Exit: tascProcess()");
		return new ModelAndView("tascProcess", "message", jsonStr);
	}
	
	@RequestMapping("/process/testElementIdList.htm")
	public @ResponseBody String testElementIdList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("Enter: testElementIdList()");
		String processId = request.getParameter("processId");
		System.out.println("processId=" + processId);
		try {
			TascDAOImpl stageDao = new TascDAOImpl();
			List<TASCProcessTO> testElementIdList = stageDao.getTestElementIdList(processId);
			System.out.println("testElementIdList=" + testElementIdList);
			response.setContentType("application/json");
			response.getWriter().write(convertToJson(testElementIdList));
		} catch (Exception e) {
			System.out.println("Failed to get testElementIdList, processId=" + processId);
			response.setContentType("text/plain");
			response.getWriter().write("Error");
			e.printStackTrace();
		}
		System.out.println("Exit: testElementIdList()");
		return null;
	}
	
	public String convertToJson(List<TASCProcessTO> processes) {
		String jsonStr = "[";
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		xstream.alias("product", TASCProcessTO.class);
        xstream.setMode(XStream.NO_REFERENCES);
        int count = 0;
		for(Iterator<TASCProcessTO> itr = processes.iterator(); itr.hasNext();) {
			count++;
			jsonStr += xstream.toXML(itr.next());
			if(count < processes.size()) jsonStr += ",";
		}
		jsonStr += "]";
		return jsonStr;
	}
	
	@RequestMapping("/process/getStudentDetails.htm")
	public @ResponseBody String getStudentDetails(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("Enter: getStudentDetails()");
		String processId = request.getParameter("processId");
		String testElementId = request.getParameter("testElementId");
		System.out.println("processId=" + processId);
		System.out.println("testElementId=" + testElementId);
		try {
			TascDAOImpl stageDao = new TascDAOImpl();
			Map<String, String> studentDetailsMap = stageDao.getStudentDetails(processId, testElementId);
			System.out.println("Map = " + studentDetailsMap);
			String studentDetailsJson = Utils.mapToJson(studentDetailsMap);
			System.out.println("Json = " + studentDetailsJson);
			response.setContentType("text/plain");
			response.getWriter().write(studentDetailsJson);
		} catch (Exception e) {
			System.out.println("Failed to get StudentDetails, processId=" + processId + ", testElementId=" + testElementId);
			response.setContentType("text/plain");
			response.getWriter().write("Error");
			e.printStackTrace();
		}
		System.out.println("Exit: getStudentDetails()");
		return null;
	}
	
	@RequestMapping("/process/getTascProcessLog.htm")
	public @ResponseBody String getTascProcessLog(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String processId = request.getParameter("processId");
			TascDAOImpl stageDao = new TascDAOImpl();
			String processLog = stageDao.getProcessLog(processId);
			if(processLog != null) processLog = processLog.replaceAll("\n", "<br>");
			else processLog = "";
			response.setContentType("text/plain");
			response.getWriter().write(processLog);
		} catch (Exception e) {
			System.out.println("Failed to get log");
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/process/tascSearch.htm")
	public ModelAndView tascSearch(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("view method called");
		String adminid = request.getParameter("adminid");
		if(adminid != null) {
			request.getSession().setAttribute("adminid", adminid);
		}
		if(!UserController.checkLogin(request)) return new ModelAndView("welcome", "message", "Please login.");
		
		return new ModelAndView("tascSearch", "message", "");
	}
	
	/**
	 * This method is to search processes
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/process/searchTasc.htm")
	public ModelAndView searchTasc(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("process method called");
		try {
			if(!UserController.checkLogin(request)) return new ModelAndView("welcome", "message", "Please login.");
			SearchProcess process = new SearchProcess();
			process.setStructElement(request.getParameter("sourceSystem"));
			process.setCreatedDate(request.getParameter("updatedDateFrom"));
			process.setUpdatedDate(request.getParameter("updatedDateTo"));
			
			request.getSession().setAttribute("tascRequestTO", process);
			TascDAOImpl stageDao = new TascDAOImpl();
			List<TASCProcessTO> processes = stageDao.getProcess(process);
			
			request.getSession().setAttribute("tascProcess", processes);
			convertProcessToJson(processes);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("tascProcess", "message", jsonStr);
	}
	
	@RequestMapping("/process/searchTascGraph.htm")
	public ModelAndView searchTascGraph(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("process method called");
		try {
			if(!UserController.checkLogin(request)) return new ModelAndView("welcome", "message", "Please login.");
			TascDAOImpl stageDao = new TascDAOImpl();
			List<TASCProcessTO> processes = stageDao.getProcessCountList();
			
			StringBuilder pp = new StringBuilder();
			StringBuilder ol = new StringBuilder();
			StringBuilder co = new StringBuilder();
			StringBuilder er = new StringBuilder();
			long error = 0, success = 0;
			int count = 0;
			for(TASCProcessTO processTO : processes) {
				if(count > 0) {
					pp.append(",");
					ol.append(",");
					co.append(",");
					er.append(",");
				} else {
					String[] date = (processTO.getDateTimestamp() != null)?processTO.getDateTimestamp().split(","):"2014,02,09".split(",");
					int month = Integer.parseInt(date[1]);
					month =  month - 1;
					request.getSession().setAttribute("tascStartDate", date[0]+","+month+","+date[2]);
				}
				count++;
				pp.append(processTO.getPpCount());
				ol.append(processTO.getOlCount());
				co.append(processTO.getCoCount());
				er.append(processTO.getErCount());
				error = error + Integer.parseInt(processTO.getErCount());
				success = success + Integer.parseInt(processTO.getCoCount());
			}
			request.getSession().setAttribute("tascPPCountList", pp.toString());
			request.getSession().setAttribute("tascOLCountList", ol.toString());
			request.getSession().setAttribute("tascCOCountList", co.toString());
			request.getSession().setAttribute("tascERCountList", er.toString());
			request.getSession().setAttribute("successCount", success+"");
			request.getSession().setAttribute("errorCount", error+"");
			convertProcessToJson(processes);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("tascProcessGraph", "message", jsonStr);
	}
	
	public ModelAndView showDataload(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if(!UserController.checkLogin(request)) return new ModelAndView("welcome", "message", "Please login.");
		List<TASCProcessTO> processes = (List<TASCProcessTO>) request.getSession().getAttribute("tascProcess");
		if(processes == null) {
			TascDAOImpl stageDao = new TascDAOImpl();
			processes = stageDao.getProcess(null);
		}
		if(processes != null) {
			int ppCount = 0, olCount = 0;
			for(TASCProcessTO process : processes) {
				if("PP".equals(process.getSourceSystem())) {
					ppCount++;
				} else if("OL".equals(process.getSourceSystem())) {
					olCount++;
				}
			}
			
			StringBuilder sb = new StringBuilder();
			for(TASCProcessTO process : processes) {
				if("PP".equals(process.getSourceSystem())) {
					sb.append("[Date.UTC(").append(getDateTime("yyyy, MM, dd")).append(")");
					process.getDateTimestamp();
				} else if("OL".equals(process.getSourceSystem())) {
					
				}
			}
		}
		return null;
	}
	
	private <T> void convertProcessToJson(List<T> processes) {
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        //xstream.alias("product", T.class);
		for(Iterator<T> itr = processes.iterator(); itr.hasNext();) {
			jsonStr = xstream.toXML(itr.next());
		}
	}
	
	public static String getDateTime(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateformatter = new SimpleDateFormat(dateFormat);
		return dateformatter.format(cal.getTime());
	}
	
	/**
	 * This method is to collect search criteria
	 * @author Joy
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/process/tascSearchEr.htm")
	public ModelAndView tascSearchEr(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("Enter: tascSearchEr()");
		if(!UserController.checkLogin(request)) return new ModelAndView("welcome", "message", "Please login.");
		return new ModelAndView("tascSearchEr", "message", "");
	}
	
	/**
	 * This method is to collect search criteria
	 * @author Joy
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/process/tascSearchErNew.htm")
	public ModelAndView tascSearchErNew(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("Enter: tascSearchErNew()");
		if(!UserController.checkLogin(request)) return new ModelAndView("welcome", "message", "Please login.");
		return new ModelAndView("tascSearchErNew", "message", "");
	}
	
	/**
	 * This method is to show searched records
	 * @author Joy
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping("/process/searchTascEr.htm")
	public ModelAndView searchTascEr(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("Enter: searchTascEr()");
		try {
			if(!UserController.checkLogin(request)) return new ModelAndView("welcome", "message", "Please login.");
			SearchProcess process = new SearchProcess();
			process.setProcessedDateFrom(request.getParameter("processedDateFrom"));
			process.setProcessedDateTo(request.getParameter("processedDateTo"));
			process.setUuid(request.getParameter("uuid"));
			process.setRecordId(request.getParameter("recordId"));
			process.setLastName(request.getParameter("lastName"));
			process.setExceptionCode(request.getParameter("exceptionCode"));
			process.setSubjectCa(request.getParameter("subjectCa"));
			process.setSourceSystem(request.getParameter("sourceSystem"));
			process.setStateCode(request.getParameter("stateCode"));
			process.setForm(request.getParameter("form"));
			process.setTestElementId(request.getParameter("testElementId"));
			process.setBarcode(request.getParameter("barcode"));
			process.setProcessId(request.getParameter("processId"));
			if("OAS".equals(process.getSourceSystem())){
				process.setSourceSystemDesc("Online");
			}else if("PP".equals(process.getSourceSystem())){
				process.setSourceSystemDesc("Paper Pencil");
			}else if("ERESOURCE".equals(process.getSourceSystem())){
				process.setSourceSystemDesc("eResources");
			}
			
			request.getSession().setAttribute("tascRequestTO", process);
			TascDAOImpl stageDao = new TascDAOImpl();
			List<StudentDetailsTO> studentDetailsTOList = stageDao.getProcessEr(process);
			request.getSession().setAttribute("studentDetailsTOList", studentDetailsTOList);
			convertProcessToJson(studentDetailsTOList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("tascProcessEr", "message", jsonStr);
	}*/
	
	/**
	 * This method is to show searched records page wise for eResource and normal for other
	 * @author Joy
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/process/searchTascErNew.htm")
	public ModelAndView searchTascErNew(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("Enter: searchTascErNew()");
		ModelAndView modelAndView = new ModelAndView("welcome", "message", "Please login.");
		try {
			if(!UserController.checkLogin(request)) return modelAndView;
			SearchProcess process = new SearchProcess();
			process.setProcessStatus(request.getParameter("processStatus"));
			process.setProcessedDateFrom(request.getParameter("processedDateFrom"));
			process.setProcessedDateTo(request.getParameter("processedDateTo"));
			process.setUuid(request.getParameter("uuid"));
			process.setRecordId(request.getParameter("recordId"));
			process.setLastName(request.getParameter("lastName"));
			process.setExceptionCode(request.getParameter("exceptionCode"));
			process.setSubjectCa(request.getParameter("subjectCa"));
			process.setSourceSystem(request.getParameter("sourceSystem"));
			process.setStateCode(request.getParameter("stateCode"));
			process.setForm(request.getParameter("form"));
			process.setTestElementId(request.getParameter("testElementId"));
			process.setBarcode(request.getParameter("barcode"));
			process.setProcessId(request.getParameter("processId"));
			if("OAS".equals(process.getSourceSystem())){
				process.setSourceSystemDesc("Online");
			}else if("PP".equals(process.getSourceSystem())){
				process.setSourceSystemDesc("Paper Pencil");
			}else if("ERESOURCE".equals(process.getSourceSystem())){
				process.setSourceSystemDesc("eResources");
			}
			
			request.getSession().setAttribute("tascRequestTO", process);
			modelAndView = new ModelAndView("tascProcessErPaging", "message", jsonStr);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelAndView;
	}
	
	/**
	 * This method is for paging
	 * @author Joy
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/process/searchTascErPaging.htm", method = RequestMethod.GET)
	public @ResponseBody String searchTascErPaging(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("Enter: searchTascErPaging()");
		TascDAOImpl stageDao = new TascDAOImpl();
    	SearchProcess process = (SearchProcess)request.getSession().getAttribute("tascRequestTO");
    	String searchParameter = request.getParameter("sSearch");
    	if(searchParameter != null){
    		process.setSearchParam(searchParameter);
    	}
    	long totalRecordCount = stageDao.getTotalRecordCount(process);
    	
    	long pageDisplayLength = Long.parseLong(request.getParameter("iDisplayLength"));
    	long pageNumber = 0;
    	if (null != request.getParameter("iDisplayStart")){
    		pageNumber = (Long.parseLong(request.getParameter("iDisplayStart"))/pageDisplayLength)+1;
    	}
    	
    	process.setPageNumber(pageNumber);
    	process.setPageDisplayLength(pageDisplayLength);
    	
    	String sortEcho = request.getParameter("sEcho");
        String sortCol = request.getParameter("iSortCol_0");
        String sortDir = request.getParameter("sSortDir_0");
        process.setSortCol(sortCol);
        process.setSortDir(sortDir);
		
		List<StudentDetailsTO> studentDetailsTOList = stageDao.getProcessErPaging(process);
    	
		StudentJsonObject studentJsonObject = new StudentJsonObject();
		studentJsonObject.setiTotalDisplayRecords(totalRecordCount);
		studentJsonObject.setiTotalRecords(totalRecordCount);
		studentJsonObject.setAaData(studentDetailsTOList);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonStr = gson.toJson(studentJsonObject);
		response.setContentType("application/json");
		response.getWriter().write(jsonStr);
		return null;			
	}
	
	/**
	 * @author Joy
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/process/getStudentHist.htm")
	public @ResponseBody String getStudentHist(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("Enter: getStudentHist()");
		String erSsHistId = request.getParameter("erSsHistId");
		System.out.println("erSsHistId=" + erSsHistId);
		try {
			TascDAOImpl stageDao = new TascDAOImpl();
			Map<String, String> studentDetailsMap = stageDao.getStudentHist(erSsHistId);
			System.out.println("Map = " + studentDetailsMap);
			String studentDetailsJson = Utils.mapToJson(studentDetailsMap);
			System.out.println("Json = " + studentDetailsJson);
			response.setContentType("text/plain");
			response.getWriter().write(studentDetailsJson);
		} catch (Exception e) {
			System.out.println("Failed to get StudentDetails, erSsHistId=" + erSsHistId);
			response.setContentType("text/plain");
			response.getWriter().write("Error");
			e.printStackTrace();
		}
		System.out.println("Exit: getStudentHist()");
		return null;
	}
	
	/**
	 * @author Joy
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/process/getMoreInfo.htm")
	public @ResponseBody String getMoreInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("Enter: getMoreInfo()");
		String erExcdId = request.getParameter("erExcdId");
		System.out.println("erExcdId=" + erExcdId);
		try {
			TascDAOImpl stageDao = new TascDAOImpl();
			Map<String, String> moreInfoMap = stageDao.getMoreInfo(erExcdId);
			System.out.println("Map = " + moreInfoMap);
			String moreInfoJson = Utils.mapToJson(moreInfoMap);
			System.out.println("Json = " + moreInfoJson);
			response.setContentType("text/plain");
			response.getWriter().write(moreInfoJson);
		} catch (Exception e) {
			System.out.println("Failed to get More Info, erExcdId=" + erExcdId);
			response.setContentType("text/plain");
			response.getWriter().write("Error");
			e.printStackTrace();
		}
		System.out.println("Exit: getMoreInfo()");
		return null;
	}
	
	/**
	 * @author Joy
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/process/getErrorLog.htm")
	public @ResponseBody String getErrorLog(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String erExcdId = request.getParameter("erExcdId");
			TascDAOImpl stageDao = new TascDAOImpl();
			String errorLog = stageDao.getErrorLog(erExcdId);
			if(errorLog != null) errorLog = errorLog.replaceAll("\n", "<br>");
			else errorLog = "";
			response.setContentType("text/plain");
			response.getWriter().write(errorLog);
		} catch (Exception e) {
			System.out.println("Failed to get log");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @author Joy
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/process/downloadCsv.htm")
	public void downloadCsv(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("Enter: downloadCsv()");
		long t1 = System.currentTimeMillis();
		String fileName = "data.csv";
		String contentType = "application/octet-stream";
		SearchProcess process = new SearchProcess();
		List<StudentDetailsTO> studentDetailsTOList = null;
		try {
			process = (SearchProcess)request.getSession().getAttribute("tascRequestTO");
			process.setMode("CSV");
			process.setSortCol("13");
			process.setSortDir("desc");
			TascDAOImpl stageDao = new TascDAOImpl();
			studentDetailsTOList = stageDao.getProcessErPaging(process);
			process.setMode("");
			process.setSortCol("");
			process.setSortDir("");
			
			StringBuffer buffer = new StringBuffer();
			buffer.append("Record Id")
			.append(",").append("Student Name")
			.append(",").append("UUID")
			.append(",").append("Test Element Id")
			.append(",").append("Ex. Code")
			.append(",").append("Status")
			.append(",").append("Bar Code")
			.append(",").append("Test/Schedule Date")
			.append(",").append("State Code")
			.append(",").append("Form")
			.append(",").append("Subtest")
			.append(",").append("Test Center Code")
			.append(",").append("Test Center Name");
			
			if("ERESOURCE".equals(process.getSourceSystem())){
				buffer.append(",").append("CTB Customer ID")
				.append(",").append("State Name")
				.append(",").append("DOB")
				.append(",").append("Gender")
				.append(",").append("Government ID")
				.append(",").append("Government ID Type")
				.append(",").append("Address")
				.append(",").append("City")
				.append(",").append("County")
				.append(",").append("State")
				.append(",").append("Zip")
				.append(",").append("Email")
				.append(",").append("Alternate Email")
				.append(",").append("Primary Phone Number")
				.append(",").append("Cell Phone Number")
				.append(",").append("Alternate Phone Number")
				.append(",").append("Resolved Ethnicity Race")
				.append(",").append("Home Language")
				.append(",").append("Education Level")
				.append(",").append("Attend College")
				.append(",").append("Contact")
				.append(",").append("Examinee County Parish Code")
				.append(",").append("Registered On")
				.append(",").append("Registered Test Center")
				.append(",").append("Registered Test Center Code")
				.append(",").append("Schedule ID")
				.append(",").append("Time of Day")
				.append(",").append("Checked in Date")
				.append(",").append("Content Test Type")
				.append(",").append("Content Test Code")
				.append(",").append("TASC Radiness")
				.append(",").append("ECC")
				.append(",").append("Regst TC County Parish Code")
				.append(",").append("Sched TC County Parish Code");
			}else{
				buffer.append(",").append("Test Language")
				.append(",").append("Litho Code")
				.append(",").append("Scoring Date")
				.append(",").append("Scanned/Processed Date")
				.append(",").append("Number Correct")
				.append(",").append("Status Code")
				.append(",").append("Scan Batch=OrgTP_Struc-Lvl-Elm_Opunit")
				.append(",").append("Scan Stack")
				.append(",").append("Scan Sequence")
				.append(",").append("Bio Image(s)");
			}
			buffer.append(",").append("Source System")
			.append(",").append("Prism Process Date")
			.append(",").append("Error Log")
			.append("\n");
			
			String data = StringUtils.collectionToDelimitedString(studentDetailsTOList, "\n");
			buffer.append(data);
			System.out.println("buffer: "+buffer);
			Utils.browserDownload(response, buffer.toString().getBytes(), fileName, contentType);
		} catch (Exception e) {
			System.out.println("Failed to download the file");
			e.printStackTrace();
		}finally{
			long t2 = System.currentTimeMillis();
			System.out.println("Exit: downloadCsv() took time: " + String.valueOf(t2 - t1) + "ms");
		}
	}
	
	@RequestMapping("/process/combined.htm")
	public ModelAndView searchCombined(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("Enter: searchCombined()");
		String showCommentFlag = "false"; 
		String savedComments = ""; 
		String uuid = "";
		String stateCode = "";
		try {
			if(!UserController.checkLogin(request)) return new ModelAndView("welcome", "message", "Please login.");
			SearchProcess process = new SearchProcess();
			process.setUuid(request.getParameter("uuid"));
			process.setTestElementId(request.getParameter("testElementId"));
			process.setStateCode(request.getParameter("stateCode"));
			request.getSession().setAttribute("combinedRequestTO", process);
			
			if((process.getUuid() != null && process.getUuid().length() > 0) || (process.getTestElementId() != null && process.getTestElementId().length() > 0) ) {
				TascDAOImpl stageDao = new TascDAOImpl();
				List<StudentDetailsTO> studentDetailsTOList = stageDao.getCombinedProcess(process);
				request.setAttribute("combinedList", studentDetailsTOList);
				
				/*if(studentDetailsTOList != null && studentDetailsTOList.size() > 0
						&& (process.getUuid() != null && process.getUuid().equals(studentDetailsTOList.get(0).getUuid()))
						&& (process.getStateCode() != null && process.getStateCode().length() > 0)){
					savedComments = (studentDetailsTOList.get(0).getComments()==null?"":studentDetailsTOList.get(0).getComments());
					showCommentFlag = "true";
					uuid = process.getUuid();
					stateCode = process.getStateCode();
				} else{
					savedComments = "";
				}*/
				
				List<StudentDetailsTO> erBucket = stageDao.getERBucket(process);
				request.setAttribute("erBucket", erBucket);
				
				if(erBucket != null && erBucket.size() > 0
						&& (process.getUuid() != null && process.getUuid().equals(erBucket.get(0).getUuid()))
						&& (process.getStateCode() != null && process.getStateCode().length() == 2)){
					savedComments = (erBucket.get(0).getComments()==null?"":erBucket.get(0).getComments());
					showCommentFlag = "true";
					uuid = process.getUuid();
					stateCode = process.getStateCode();
				} else{
					savedComments = "";
				}
				
				List<StudentDetailsTO> erError = stageDao.getERError(process);
				request.setAttribute("erError", erError);
				
				List<StudentDetailsTO> oasPpError = stageDao.getOasPPError(process);
				request.setAttribute("oasPpError", oasPpError);
				
				convertProcessToJson(studentDetailsTOList);
				return new ModelAndView("combined", "message", "");
			} else {
				return new ModelAndView("combined", "message", "Please provide UUID or TEST-ELEMENT-ID");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			request.setAttribute("showCommentFlag", showCommentFlag);
			request.setAttribute("uuid", uuid);
			request.setAttribute("stateCode", stateCode);
			request.setAttribute("savedComments", savedComments);
		}
		return new ModelAndView("combined", "message", jsonStr);
	}
	
	/**
	 * @author Abir
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/process/saveComments.htm")
	public @ResponseBody String saveComments (HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("Enter: saveComments()");
		String comments = request.getParameter("comments");
		String uuid = request.getParameter("uuId");
		String stateCode = request.getParameter("stateCode");
		System.out.println("comments=" + comments);
		System.out.println("uuId=" + uuid);
		System.out.println("stateCode=" + stateCode);
		try {
			TascDAOImpl stageDao = new TascDAOImpl();
			StudentDetailsTO studentDetailsTO = new StudentDetailsTO();
			studentDetailsTO.setComments(comments);
			studentDetailsTO.setUuid(uuid);
			studentDetailsTO.setStateCode(stateCode);
			
			int updatedCount = stageDao.saveComments(studentDetailsTO);
			
			String status = null;
			if(updatedCount > 0) {
				if (comments.length() > 0)
					status = "Comments added sucessfully";
				else
					status = "Comments removed sucessfully";
			} else {
				status = "Comments save failed as no match found";
			}
			System.out.println("Json = " + status);
			response.setContentType("text/plain");
			response.getWriter().write(status);
		} catch (Exception e) {
			System.out.println("Failed to save comments=" + comments);
			response.setContentType("text/plain");
			response.getWriter().write("Error");
			e.printStackTrace();
		}
		System.out.println("Exit: saveComments()");
		return null;
	}
	
		
	
}
