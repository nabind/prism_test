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
			List<StudentDetailsTO> studentDetailsTOList = stageDao.getProcessErPaging(process);
			
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
	@RequestMapping("/process/searchTascEr.htm")
	public ModelAndView searchTascEr(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("Enter: searchTascEr()");
		ModelAndView modelAndView = new ModelAndView("welcome", "message", "Please login.");
		try {
			if(!UserController.checkLogin(request)) return modelAndView;
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
			
			if(!"ERESOURCE".equals(process.getSourceSystem())){
				TascDAOImpl stageDao = new TascDAOImpl();
				List<StudentDetailsTO> studentDetailsTOList = stageDao.getProcessErPaging(process);
				convertProcessToJson(studentDetailsTOList);
				modelAndView = new ModelAndView("tascProcessEr", "message", "jsonStr");
			}else{
				modelAndView = new ModelAndView("tascProcessErPaging", "message", jsonStr);
			}
			
			
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
    	
    	long pageNumber = 0;
    	if (null != request.getParameter("iDisplayStart")){
    		pageNumber = (Long.parseLong(request.getParameter("iDisplayStart"))/10)+1;
    	}
    	long pageDisplayLength = Long.parseLong(request.getParameter("iDisplayLength"));
    	process.setPageNumber(pageNumber);
    	process.setPageDisplayLength(pageDisplayLength);
    	
    	
    	//TODO FOR SORTING
    	String sEcho = request.getParameter("sEcho");
        String sCol = request.getParameter("iSortCol_0");
        String sdir = request.getParameter("sSortDir_0");
    	
		
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
	
	private List<StudentDetailsTO> getListBasedOnSearchParameter(String searchParameter,List<StudentDetailsTO> studentDetailsTOList) {
		List<StudentDetailsTO> studentDetailsTOListForSearch = new ArrayList<StudentDetailsTO>();
		searchParameter = searchParameter.toUpperCase();
		for (StudentDetailsTO studentDetailsTO : studentDetailsTOList) {
			if(studentDetailsTO.getStudentName().toUpperCase().indexOf(searchParameter)!=-1
					|| studentDetailsTO.getUuid().toUpperCase().indexOf(searchParameter)!=-1
					|| studentDetailsTO.getTestElementId().toUpperCase().indexOf(searchParameter)!=-1
					|| studentDetailsTO.getProcessId().toUpperCase().indexOf(searchParameter)!=-1
					|| studentDetailsTO.getExceptionCode().toUpperCase().indexOf(searchParameter)!=-1
					|| studentDetailsTO.getSourceSystem().toUpperCase().indexOf(searchParameter)!=-1
					|| studentDetailsTO.getOverallStatus().toUpperCase().indexOf(searchParameter)!=-1
					|| studentDetailsTO.getErSsHistId().toUpperCase().indexOf(searchParameter)!=-1
					|| studentDetailsTO.getBarcode().toUpperCase().indexOf(searchParameter)!=-1
					|| studentDetailsTO.getDateScheduled().toUpperCase().indexOf(searchParameter)!=-1
					|| studentDetailsTO.getStateCode().toUpperCase().indexOf(searchParameter)!=-1
					|| studentDetailsTO.getForm().toUpperCase().indexOf(searchParameter)!=-1
					|| studentDetailsTO.getErExcdId().toUpperCase().indexOf(searchParameter)!=-1
					|| studentDetailsTO.getSubtestName().toUpperCase().indexOf(searchParameter)!=-1
					|| studentDetailsTO.getProcessedDate().toUpperCase().indexOf(searchParameter)!=-1){
				
				studentDetailsTOListForSearch.add(studentDetailsTO);
				
			}
		}
		return studentDetailsTOListForSearch;
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
		String fileName = "data.csv";
		String contentType = "application/octet-stream";
		SearchProcess process = new SearchProcess();
		List<StudentDetailsTO> studentDetailsTOList = null;
		try {
			process = (SearchProcess)request.getSession().getAttribute("tascRequestTO");
			//studentDetailsTOList = (List<StudentDetailsTO>)request.getSession().getAttribute("studentDetailsTOList");
			TascDAOImpl stageDao = new TascDAOImpl();
			studentDetailsTOList = stageDao.getProcessEr(process);
			
			StringBuffer buffer = new StringBuffer();
			buffer.append("Record Id")
			.append(",").append("Student Name")
			.append(",").append("UUID")
			.append(",").append("Test Element Id")
			.append(",").append("Process Id")
			.append(",").append("Ex. Code")
			.append(",").append("Status")
			.append(",").append("Bar Code")
			.append(",").append("Scheduled Date")
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
			}
			buffer.append(",").append("Source System")
			/*.append(",").append("Processed Date From")
			.append(",").append("Processed Date To")*/
			.append(",").append("Processed Date")
			.append(",").append("Error Log")
			.append("\n");
			
			String data = StringUtils.collectionToDelimitedString(studentDetailsTOList, "\n");
			buffer.append(data);
			System.out.println("buffer: "+buffer);
			Utils.browserDownload(response, buffer.toString().getBytes(), fileName, contentType);
		} catch (Exception e) {
			System.out.println("Failed to download the file");
			e.printStackTrace();
		}
	}
}
