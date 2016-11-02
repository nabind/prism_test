package com.vaannila.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.digester.Substitutor;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.vaannila.DAO.TascDAOImpl;
import com.vaannila.TO.StudentDetailsGhiTO;
import com.vaannila.TO.StudentGhiJsonObject;
import com.vaannila.TO.SearchProcess;
import com.vaannila.TO.StudentDetailsTO;
import com.vaannila.TO.StudentDetailsWinTO;
import com.vaannila.TO.StudentJsonObject;
import com.vaannila.TO.StudentWinJsonObject;
import com.vaannila.TO.TASCProcessTO;
import com.vaannila.util.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vaannila.DAO.SupportDAOImpl;

@Controller
public class TascController {
	
	private static final Logger logger = Logger.getLogger(TascController.class);
	
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
		logger.info("Enter: tascProcess()");
		try {
			String adminid = request.getParameter("adminid");
			logger.info("adminid = " + adminid);
			if(adminid == null) {
				adminid = (String) request.getSession().getAttribute("adminid");
			} else {
				request.getSession().setAttribute("adminid", adminid);
			}
			if(!UserController.checkLogin(request)) return new ModelAndView("welcome", "message", "Please login.");
			TascDAOImpl stageDao = new TascDAOImpl();
			logger.info("getting processes...");
			List<TASCProcessTO> processes = stageDao.getProcess(null);
			logger.info("got processes successfully");
			request.getSession().setAttribute("tascProcess", processes);
			convertProcessToJson(processes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Exit: tascProcess()");
		return new ModelAndView("tascProcess", "message", jsonStr);
	}
	
	@RequestMapping("/process/testElementIdList.htm")
	public @ResponseBody String testElementIdList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Enter: testElementIdList()");
		String processId = request.getParameter("processId");
		logger.info("processId=" + processId);
		try {
			TascDAOImpl stageDao = new TascDAOImpl();
			List<TASCProcessTO> testElementIdList = stageDao.getTestElementIdList(processId);
			logger.info("testElementIdList=" + testElementIdList);
			response.setContentType("application/json");
			response.getWriter().write(convertToJson(testElementIdList));
		} catch (Exception e) {
			logger.error("Failed to get testElementIdList, processId=" + processId);
			response.setContentType("text/plain");
			response.getWriter().write("Error");
			e.printStackTrace();
		}
		logger.info("Exit: testElementIdList()");
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
		logger.info("Enter: getStudentDetails()");
		String processId = request.getParameter("processId");
		String testElementId = request.getParameter("testElementId");
		logger.info("processId=" + processId);
		logger.info("testElementId=" + testElementId);
		try {
			TascDAOImpl stageDao = new TascDAOImpl();
			Map<String, String> studentDetailsMap = stageDao.getStudentDetails(processId, testElementId);
			logger.info("Map = " + studentDetailsMap);
			String studentDetailsJson = Utils.mapToJson(studentDetailsMap);
			logger.info("Json = " + studentDetailsJson);
			response.setContentType("text/plain");
			response.getWriter().write(studentDetailsJson);
		} catch (Exception e) {
			logger.error("Failed to get StudentDetails, processId=" + processId + ", testElementId=" + testElementId);
			response.setContentType("text/plain");
			response.getWriter().write("Error");
			e.printStackTrace();
		}
		logger.info("Exit: getStudentDetails()");
		return null;
	}
	
	@RequestMapping("/process/getTascProcessLog.htm")
	public @ResponseBody String getTascProcessLog(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String processId = request.getParameter("processId");
			String source = request.getParameter("source");
			TascDAOImpl stageDao = new TascDAOImpl();
			String processLog = stageDao.getProcessLog(processId,source);
			if(processLog != null) processLog = processLog.replaceAll("\n", "<br>");
			else processLog = "";
			response.setContentType("text/plain");
			response.getWriter().write(processLog);
		} catch (Exception e) {
			logger.error("Failed to get log");
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/process/tascSearch.htm")
	public ModelAndView tascSearch(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("view method called");
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
		logger.info("process method called");
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
		logger.info("process method called");
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
		logger.info("Enter: tascSearchEr()");
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
		logger.info("Enter: tascSearchErNew()");
		if(!UserController.checkLogin(request)) return new ModelAndView("welcome", "message", "Please login.");
		return new ModelAndView("tascSearchErNew", "message", "");
	}
	
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
		logger.info("Enter: searchTascErNew()");
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
		logger.info("Enter: searchTascErPaging()");
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
		logger.info("Enter: getStudentHist()");
		String erSsHistId = request.getParameter("erSsHistId");
		logger.info("erSsHistId=" + erSsHistId);
		try {
			TascDAOImpl stageDao = new TascDAOImpl();
			Map<String, String> studentDetailsMap = stageDao.getStudentHist(erSsHistId);
			logger.info("Map = " + studentDetailsMap);
			String studentDetailsJson = Utils.mapToJson(studentDetailsMap);
			logger.info("Json = " + studentDetailsJson);
			response.setContentType("text/plain");
			response.getWriter().write(studentDetailsJson);
		} catch (Exception e) {
			logger.error("Failed to get StudentDetails, erSsHistId=" + erSsHistId);
			response.setContentType("text/plain");
			response.getWriter().write("Error");
			e.printStackTrace();
		}
		logger.info("Exit: getStudentHist()");
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
		logger.info("Enter: getMoreInfo()");
		String erExcdId = request.getParameter("erExcdId");
		logger.info("erExcdId=" + erExcdId);
		try {
			TascDAOImpl stageDao = new TascDAOImpl();
			Map<String, String> moreInfoMap = stageDao.getMoreInfo(erExcdId);
			logger.info("Map = " + moreInfoMap);
			String moreInfoJson = Utils.mapToJson(moreInfoMap);
			logger.info("Json = " + moreInfoJson);
			response.setContentType("text/plain");
			response.getWriter().write(moreInfoJson);
			
		} catch (Exception e) {
			logger.error("Failed to get More Info, erExcdId=" + erExcdId);
			response.setContentType("text/plain");
			response.getWriter().write("Error");
			e.printStackTrace();
		}
		logger.info("Exit: getMoreInfo()");
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
			logger.error("Failed to get log");
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
	@RequestMapping("/process/downloadCsv.htm")
	public void downloadCsv(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Enter: downloadCsv()");
		long t1 = System.currentTimeMillis();
		String fileName = "data.csv";
		String contentType = "application/octet-stream";
		SearchProcess process = new SearchProcess();
		List<StudentDetailsTO> studentDetailsTOList = null;
		try {
			process = (SearchProcess)request.getSession().getAttribute("tascRequestTO");
			process.setMode("CSV");
			process.setSortCol("12");
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
			.append(",").append("State Prefix")
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
			logger.info("buffer: "+buffer);
			Utils.browserDownload(response, buffer.toString().getBytes(), fileName, contentType);
		} catch (Exception e) {
			logger.error("Failed to download the file");
			e.printStackTrace();
		}finally{
			long t2 = System.currentTimeMillis();
			logger.info("Exit: downloadCsv() took time: " + String.valueOf(t2 - t1) + "ms");
		}
	}
	
	@RequestMapping("/process/combined.htm")
    public ModelAndView searchCombined(HttpServletRequest request,
                  HttpServletResponse response) throws Exception {
		   logger.info("Enter: searchCombined()");
           String showCommentFlag = "false";
           String savedComments = "";
           String uuid = "";
           String stateCode = "";
           String userName = (String)request.getSession().getAttribute("userName");
           try {
                  if(!UserController.checkLogin(request)) return new ModelAndView("welcome", "message", "Please login.");
                  SearchProcess process = new SearchProcess();
                  process.setUuid(request.getParameter("uuid"));
                  process.setTestElementId(request.getParameter("testElementId"));
                  process.setStateCode(request.getParameter("stateCode"));
                  process.setDRCStudentId(request.getParameter("drcStudentId"));
                  process.setLevel1OrgCode(request.getParameter("stateCode"));
                  request.getSession().setAttribute("combinedRequestTO", process);
                 
                  if((process.getUuid() != null && process.getUuid().length() > 0) || (process.getTestElementId() != null && process.getTestElementId().length() > 0) ) {
                        TascDAOImpl stageDao = new TascDAOImpl();
                       
                        String action = request.getParameter("action");
                       
                        SupportDAOImpl supportDao = new SupportDAOImpl();
                       
                        if(action!=null && action.equals("delete")){
                               String studentBiodId = request.getParameter("bioId");
                               StudentDetailsTO studentTO = new StudentDetailsTO();
                               studentTO.setStudentBioId(studentBiodId);
                               //Delete student code goes here with bio id
                               jsonStr = supportDao.deleteStudent(studentTO);
                               logger.info("Student "+studentBiodId+ " has been deleted");
                        }
                        if(action!=null && action.equals("invalidate")){
                               String inUuid = request.getParameter("inUuid");
                               String inStateCode = request.getParameter("inStateCode");
                               String erTestSchId = request.getParameter("erTestSchId");
                               StudentDetailsTO studentTO = new StudentDetailsTO();
                               studentTO.setUuid(inUuid);
                               studentTO.setStateCode(inStateCode);
                               studentTO.setErTestSchId(erTestSchId);
                               //Invalidate student code goes here with uuid
                               jsonStr = supportDao.invalidate(studentTO,false);
                               logger.info("Student "+process.getUuid()+ " has been invalidate");
                        }
                        if(action!=null && action.equals("undoInvalidate")){
                               String inUuid = request.getParameter("inUuid");
                               String inStateCode = request.getParameter("inStateCode");
                               String erTestSchId = request.getParameter("erTestSchId");
                               StudentDetailsTO studentTO = new StudentDetailsTO();
                               studentTO.setUuid(inUuid);
                               studentTO.setStateCode(inStateCode);
                               studentTO.setErTestSchId(erTestSchId);
                               //Invalidate student code goes here with uuid
                               jsonStr = supportDao.invalidate(studentTO,true);
                               logger.info("Student "+process.getUuid()+ " has been invalidate");
                        }
                        if(action!=null && action.equals("invalidateSch")){
                               String insUuid = request.getParameter("insUuid");
                               String insStateCode = request.getParameter("insStateCode");
                               String erTestSchId = request.getParameter("erTestSchId");
                               StudentDetailsTO studentTO = new StudentDetailsTO();
                               studentTO.setUuid(insUuid);
                               studentTO.setStateCode(insStateCode);
                               studentTO.setErTestSchId(erTestSchId);
                               //Invalidate schedule code goes here with uuid
                               jsonStr = supportDao.invalidateSch(studentTO,false);
                               logger.info("Student "+insUuid+ " with erTestSchId " +erTestSchId+" has been invalidate");
                        }
                        if(action!=null && action.equals("undoInvalidateSch")){
                               String insUuid = request.getParameter("insUuid");
                               String insStateCode = request.getParameter("insStateCode");
                               String erTestSchId = request.getParameter("erTestSchId");
                               StudentDetailsTO studentTO = new StudentDetailsTO();
                               studentTO.setUuid(insUuid);
                               studentTO.setStateCode(insStateCode);
                               studentTO.setErTestSchId(erTestSchId);
                               //Invalidate schedule code goes here with uuid
                               jsonStr = supportDao.invalidateSch(studentTO,true);
                               logger.info("Student "+insUuid+ " with schedule " +erTestSchId+" has been invalidate");
                        }
                        if(action!=null && action.equals("unlock")){
                               String unUuid = request.getParameter("unUuid");
                               String unStateCode = request.getParameter("unStateCode");
                               String scheduleId = request.getParameter("schId");    
                               StudentDetailsTO studentTO = new StudentDetailsTO();
                               studentTO.setUuid(unUuid);
                               studentTO.setStateCode(unStateCode);
                               studentTO.setScheduleId(scheduleId);
                               //Unlock student code goes here with uuid
                               jsonStr = supportDao.unlockStudnet(studentTO,false);
                               logger.info("Student "+unUuid+ " with schedule " +scheduleId+" has been unlocked");
                        }
                        if(action!=null && action.equals("undoUnlock")){
                               String unUuid = request.getParameter("unUuid");
                               String unStateCode = request.getParameter("unStateCode");
                               String scheduleId = request.getParameter("schId");    
                               StudentDetailsTO studentTO = new StudentDetailsTO();
                               studentTO.setUuid(unUuid);
                               studentTO.setStateCode(unStateCode);
                               studentTO.setScheduleId(scheduleId);
                               //Unlock student code goes here with uuid
                               jsonStr = supportDao.unlockStudnet(studentTO,true);
                               logger.info("Student "+unUuid+ " with schedule " +scheduleId+" has been locked");
                        }
                       
                        List<StudentDetailsTO> studentDetailsTOList = stageDao.getCombinedProcess(process);
                        request.setAttribute("combinedList", studentDetailsTOList);
                       
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
                       
                       
                        /** this is to fetch GHI record **/
                        Map<String,List> mapGhi = stageDao.getCombinedGhi(process);
                        request.setAttribute("combinedGhiList", (List<StudentDetailsTO>)mapGhi.get("op"));
                        request.setAttribute("docStatusGhi", (List<StudentDetailsGhiTO>)mapGhi.get("docStatusGhi"));
                        request.setAttribute("errorGhi", (List<StudentDetailsGhiTO>)mapGhi.get("er"));
                       
                        convertProcessToJson(studentDetailsTOList);
                        if (userName.equals("Support"))
                               return new ModelAndView("supportPageTASC", "message", "");
                        else
                               return new ModelAndView("combined", "message", "");
                  } else {
                        if (userName.equals("Support"))
                               return new ModelAndView("supportPageTASC", "message", "Please provide UUID");
                        else  
                               return new ModelAndView("combined", "message", "Please provide UUID");
                  }
                 
           } catch (Exception e) {
                  e.printStackTrace();
                  jsonStr = "";
           } finally{
                  request.setAttribute("showCommentFlag", showCommentFlag);
                  request.setAttribute("uuid", uuid);
                  request.setAttribute("stateCode", stateCode);
                  request.setAttribute("savedComments", savedComments);
           }
           if (userName.equals("Support"))
                  return new ModelAndView("supportPageTASC", "message", jsonStr);
           else
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
		logger.info("Enter: saveComments()");
		String comments = request.getParameter("comments");
		String uuid = request.getParameter("uuId");
		String stateCode = request.getParameter("stateCode");
		logger.info("comments=" + comments);
		logger.info("uuId=" + uuid);
		logger.info("stateCode=" + stateCode);
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
			logger.info("Json = " + status);
			response.setContentType("text/plain");
			response.getWriter().write(status);
		} catch (Exception e) {
			logger.error("Failed to save comments=" + comments);
			response.setContentType("text/plain");
			response.getWriter().write("Error");
			e.printStackTrace();
		}
		logger.info("Exit: saveComments()");
		return null;
	}
	
	/**
	 * This method is to collect search criteria
	 * @author Joy
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/process/completenessCheckSearch.htm")
	public ModelAndView completenessCheckSearch(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Enter: completenessCheckSearch()");
		if(!UserController.checkLogin(request)) return new ModelAndView("welcome", "message", "Please login.");
		return new ModelAndView("completenessCheckSearch", "message", "");
	}
	
	/**
	 * This method is to show searched records page wise
	 * @author Joy
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/process/searchCompletenessCheck.htm")
	public ModelAndView searchCompletenessCheck(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Enter: searchCompletenessCheck()");
		ModelAndView modelAndView = new ModelAndView("welcome", "message", "Please login.");
		try {
			if(!UserController.checkLogin(request)) return modelAndView;
			SearchProcess process = new SearchProcess();
			process.setProcessStatus(request.getParameter("coCheckStatus"));
			process.setProcessedDateFrom(request.getParameter("coCheckDateFrom"));
			process.setProcessedDateTo(request.getParameter("coCheckDateTo"));
			process.setImagingId(request.getParameter("imagingId"));
			process.setBarcode(request.getParameter("barcode"));
			
			if("-1".equals(process.getProcessStatus())){
				process.setProcessStatusDesc("All");
			}else if("ER".equals(process.getProcessStatus())){
				process.setProcessStatusDesc("Error (record received from MF, but there is an error)");
			}else if("NR".equals(process.getProcessStatus())){
				process.setProcessStatusDesc("Not Received (record not received from MF)");
			}else if("CO".equals(process.getProcessStatus())){
				process.setProcessStatusDesc("Completed (processed successfully to Prism)");
			}else if("IN".equals(process.getProcessStatus())){
				process.setProcessStatusDesc("Invalidated (IN)");
			}
			
			request.getSession().setAttribute("coCheckTO", process);
			modelAndView = new ModelAndView("completenessCheckResult", "message", jsonStr);
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
	@RequestMapping(value = "/process/coCheckResult.htm", method = RequestMethod.GET)
	public @ResponseBody String coCheckResult(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Enter: coCheckResult()");
		long t1 = System.currentTimeMillis();
		TascDAOImpl stageDao = new TascDAOImpl();
    	SearchProcess process = (SearchProcess)request.getSession().getAttribute("coCheckTO");
    	String searchParameter = request.getParameter("sSearch");
    	if(searchParameter != null){
    		process.setSearchParam(searchParameter);
    	}
    	long totalRecordCount = stageDao.getTotalRecordCountWin(process);
    	
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
		
		List<StudentDetailsWinTO> studentDetailsTOList = stageDao.getResultWin(process);
    	
		StudentWinJsonObject studentJsonObject = new StudentWinJsonObject();
		studentJsonObject.setiTotalDisplayRecords(totalRecordCount);
		studentJsonObject.setiTotalRecords(totalRecordCount);
		studentJsonObject.setAaData(studentDetailsTOList);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonStr = gson.toJson(studentJsonObject);
		response.setContentType("application/json");
		response.getWriter().write(jsonStr);
		long t2 = System.currentTimeMillis();
		logger.info("Exit: coCheckResult() took time: " + String.valueOf(t2 - t1) + "ms");
		return null;			
	}
	
	/**
	 * @author Joy
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/process/downloadCsvWin.htm")
	public void downloadCsvWin(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Enter: downloadCsvWin()");
		long t1 = System.currentTimeMillis();
		String fileName = "dataWin.csv";
		String contentType = "application/octet-stream";
		SearchProcess process = new SearchProcess();
		List<StudentDetailsWinTO> studentDetailsTOList = null;
		try {
			process = (SearchProcess)request.getSession().getAttribute("coCheckTO");
			process.setMode("CSV");
			process.setSortCol("12");
			process.setSortDir("desc");
			TascDAOImpl stageDao = new TascDAOImpl();
			studentDetailsTOList = stageDao.getResultWin(process);
			process.setMode("");
			process.setSortCol("");
			process.setSortDir("");
			
			StringBuffer buffer = new StringBuffer();
			buffer.append("Scan Batch")
			.append(",").append("District Number")
			.append(",").append("School Number")
			.append(",").append("UUID")
			.append(",").append("Bar Code")
			.append(",").append("Form")
			.append(",").append("Braille")
			.append(",").append("Large Print")
			.append(",").append("Date Test Taken")
			.append(",").append("LogIn Date")
			.append(",").append("Scan Date")
			.append(",").append("Winscore Export Date")
			.append(",").append("Imaging ID")
			.append(",").append("OrgTPName")
			.append(",").append("LastName")
			.append(",").append("FirstName")
			.append(",").append("MiddleInitial")
			.append(",").append("LithoCode")
			.append(",").append("StudentScanStk")
			.append(",").append("StudentScanSeq")
			.append(",").append("WinscoreDocId")
			.append(",").append("Commodity Code")
			.append(",").append("WinscoreStatus")
			.append(",").append("Prism Status")
			.append(",").append("Image File Path(s)")
			.append(",").append("Image File Name(s)")
			.append("\n");
			
			String data = StringUtils.collectionToDelimitedString(studentDetailsTOList, "\n");
			buffer.append(data);
			logger.info("buffer: "+buffer);
			Utils.browserDownload(response, buffer.toString().getBytes(), fileName, contentType);
		} catch (Exception e) {
			logger.error("Failed to download the file");
			e.printStackTrace();
		}finally{
			long t2 = System.currentTimeMillis();
			logger.info("Exit: downloadCsvWin() took time: " + String.valueOf(t2 - t1) + "ms");
		}
	}
	
	/**
	 * Search review screen
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/process/scoreReview.htm")
	public ModelAndView scoreReview(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if(!UserController.checkLogin(request)) return new ModelAndView("welcome", "message", "Please login.");
		return new ModelAndView("scoreReviewSearch", "message", "");
	}
	
	/**
	 * fetch review result
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/process/scoreReviewResult.htm")
	public ModelAndView scoreReviewResult(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView("welcome", "message", "Please login.");
		try {
			if(!UserController.checkLogin(request)) return modelAndView;
			SearchProcess process = new SearchProcess();
			process.setSourceSystem(request.getParameter("sourceSystem"));
			process.setStatus(request.getParameter("scrStatus"));
			process.setProcessedDateFrom(request.getParameter("dateFrom"));
			process.setProcessedDateTo(request.getParameter("dateTo"));
			process.setUuid(request.getParameter("uuid"));
			process.setStateCode(request.getParameter("stateCode"));
			
			if("-1".equals(process.getSourceSystem())){
				process.setSourceSystemDesc("All");
			}else if("OL".equals(process.getSourceSystem())){
				process.setSourceSystemDesc("Online");
			}else if("PP".equals(process.getSourceSystem())){
				process.setSourceSystemDesc("Peper Pencil");
			}
			
			if("-1".equals(process.getStatus())){
				process.setProcessStatusDesc("All");
			}else if("RV".equals(process.getStatus())){
				process.setProcessStatusDesc("In Review");
			}else if("AP".equals(process.getStatus())){
				process.setProcessStatusDesc("Approved");
			}else if("RJ".equals(process.getStatus())){
				process.setProcessStatusDesc("Rejected");
			}else if("AE".equals(process.getStatus())){
				process.setProcessStatusDesc("Approved with Error");
			}else if("PR".equals(process.getStatus())){
				process.setProcessStatusDesc("Processed");
			}else if("IN".equals(process.getStatus())){
				process.setProcessStatusDesc("Invalidated by System");
			}
			
			TascDAOImpl stageDao = new TascDAOImpl();
			List<TASCProcessTO> reviewProcess = stageDao.getScoreReview(process);
			request.getSession().setAttribute("reviewProcess", reviewProcess);
			request.getSession().setAttribute("reviewTO", process);
			modelAndView = new ModelAndView("scoreReviewResult", "message", jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelAndView;
	}
	
	@RequestMapping("/process/getReviewResult.htm")
	public @ResponseBody String getReviewResult(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			SearchProcess process = new SearchProcess();
			process.setStudentBioId(request.getParameter("studentBioId"));
			process.setSubtestId(request.getParameter("subtestId"));
			
			TascDAOImpl stageDao = new TascDAOImpl();
			List<Map<String, String>> reviewProcess = stageDao.getReviewResult(process);
			logger.info("result size: " + reviewProcess!=null? reviewProcess.size() : "null");
			logger.info(listmapToJsonString(reviewProcess, process));
			response.setContentType("application/json");
			response.getWriter().write(listmapToJsonString(reviewProcess, process));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String listmapToJsonString(List<Map<String, String>> list, SearchProcess process) {
		JSONArray json_arr = new JSONArray();
		for (Map<String, String> map : list) {
			JSONObject json_obj = new JSONObject();
			for (Map.Entry<String, String> entry : map.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				try {
					json_obj.put(key, value);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			json_arr.put(json_obj);
		}
		
		JSONObject json_obj = new JSONObject();
		try {
			json_obj.put("student_bio_id", process.getStudentBioId());
			json_obj.put("subtestid", process.getSubtestId());
			json_obj.put("drcStudentID", process.getDRCStudentId());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return "{\"draw\":" + json_obj.toString() + ",\"data\":" + json_arr.toString() + "}";
	}
	
	/**
	 * @author Joy
	 * save review score
	 * @throws Exception
	 */
	@RequestMapping("/process/saveReviewScore.htm")
	public @ResponseBody String saveReviewScore(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String studentBioId = request.getParameter("studentBioId");
			String subtestId = request.getParameter("subtestId");
			String statusStr = request.getParameter("statusStr");
			String commentStr = request.getParameter("commentStr");
			
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("studentBioId", studentBioId);
			paramMap.put("subtestId", subtestId);
			paramMap.put("statusStr", statusStr);
			paramMap.put("commentStr", commentStr);
			
			TascDAOImpl stageDao = new TascDAOImpl();
			String message = stageDao.saveReviewScore(paramMap);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String jsonStr = gson.toJson(message);
			logger.info("jsonStr:"+jsonStr);
			response.setContentType("application/json");
			response.getWriter().write(jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * This method is to collect search criteria
	 * @author Joy
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/process/trackErrorSearch.htm")
	public ModelAndView trackErrorSearch(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Enter: trackErrorSearch()");
		if(!UserController.checkLogin(request)) return new ModelAndView("welcome", "message", "Please login.");
		return new ModelAndView("trackErrorSearch", "message", "");
	}
	
	/**
	 * This method is to show searched records page wise
	 * @author Abir
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/process/searchError.htm")
	public ModelAndView searchError(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Enter: searchError()");
		ModelAndView modelAndView = new ModelAndView("welcome", "message", "Please login.");
		try {
			if(!UserController.checkLogin(request)) return modelAndView;
			SearchProcess process = new SearchProcess();
			process.setProcessStatus(request.getParameter("trackErrorStatus"));
			process.setErrorDateFrom(request.getParameter("errorDateFrom"));
			process.setErrorDateTo(request.getParameter("errorDateTo"));
			process.setDRCStudentId(request.getParameter("DRCStudentID"));
			process.setDRCDocumentId(request.getParameter("DRCDocumentID"));
			process.setUuid(request.getParameter("uuid"));
			process.setLastName(request.getParameter("lastName"));
			process.setStateCode(request.getParameter("stateCode"));
			process.setForm(request.getParameter("form"));
			process.setTestElementId(request.getParameter("testElementId"));
			process.setBarcode(request.getParameter("barcode"));
			
			if("-1".equals(process.getProcessStatus())){
				process.setProcessStatusDesc("All");
			}else if("ER".equals(process.getProcessStatus())){
				process.setProcessStatusDesc("Error");
			}else if("CO".equals(process.getProcessStatus())){
				process.setProcessStatusDesc("Completed");
			}
		
			request.getSession().setAttribute("errorTrackingTO", process);
			modelAndView = new ModelAndView("trackErrorResult", "message", jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelAndView;
	}
	
	
	/**
	 * This method is for paging
	 * @author Abir
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/process/errorResult.htm", method = RequestMethod.GET)
	public @ResponseBody String errorResult(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Enter: errorResult()");
		long t1 = System.currentTimeMillis();
		TascDAOImpl stageDao = new TascDAOImpl();
    	SearchProcess process = (SearchProcess)request.getSession().getAttribute("errorTrackingTO");
    	String searchParameter = request.getParameter("sSearch");
    	if(searchParameter != null){
    		process.setSearchParam(searchParameter);
    	}
    	long totalRecordCount = stageDao.getTotalRecordCountGhi(process);
    	
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
		
        List<StudentDetailsGhiTO> studentDetailsTOList = stageDao.getResultGhi(process);
    	
		StudentGhiJsonObject studentGhiJsonObject = new StudentGhiJsonObject();
		studentGhiJsonObject.setiTotalDisplayRecords(totalRecordCount);
		studentGhiJsonObject.setiTotalRecords(totalRecordCount);
		studentGhiJsonObject.setAaData(studentDetailsTOList);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonStr = gson.toJson(studentGhiJsonObject);
		response.setContentType("application/json");
		response.getWriter().write(jsonStr);
		long t2 = System.currentTimeMillis();
		logger.info("Exit: errorResult() took time: " + String.valueOf(t2 - t1) + "ms");
		return null;			
	}
	
	/**
	 * @author Joy
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/process/downloadCsvGhi.htm")
	public void downloadCsvGhi(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Enter: downloadCsvGhi()");
		long t1 = System.currentTimeMillis();
		String fileName = "dataGhi.csv";
		String contentType = "application/octet-stream";
		SearchProcess process = new SearchProcess();
		List<StudentDetailsGhiTO> studentDetailsTOList = null;
		try {
			process = (SearchProcess)request.getSession().getAttribute("errorTrackingTO");
			process.setMode("CSV");
			process.setSortCol("26");
			process.setSortDir("desc");
			TascDAOImpl stageDao = new TascDAOImpl();
			studentDetailsTOList = stageDao.getResultGhi(process);
			process.setMode("");
			process.setSortCol("");
			process.setSortDir("");
			
			StringBuffer buffer = new StringBuffer();
			buffer.append("Record Id")
			.append(",").append("State Prefix")
			.append(",").append("Test Mode")
			.append(",").append("Student Name")
			.append(",").append("Examinee ID (UUID)")
			.append(",").append("DRC Student ID")
			.append(",").append("Error Status")
			.append(",").append("BarcodeID")
			.append(",").append("Schedule ID")
			.append(",").append("TCA Schedule Date")
			.append(",").append("Date Test Taken")
			.append(",").append("Form")
			.append(",").append("Content Name")
			.append(",").append("Content Test Code")
			.append(",").append("Test Language")
			.append(",").append("Litho Code")
			.append(",").append("Scale Score")
			.append(",").append("Content Score (NC)")
			.append(",").append("Status Code for Content Area")
			.append(",").append("Test Center Code")
			.append(",").append("Test Center Name")
			.append(",").append("Error Description")
			.append(",").append("Last Updated Doc Date")
			.append(",").append("Scanned/Process Date")
			.append(",").append("Org Code Path")
			.append(",").append("Prism Process Date")
			.append(",").append("Doc ID")
			.append(",").append("File Name")
			.append(",").append("File Generation Date-Time")
			.append("\n");
			
			String data = StringUtils.collectionToDelimitedString(studentDetailsTOList, "\n");
			buffer.append(data);
			logger.info("buffer: "+buffer);
			Utils.browserDownload(response, buffer.toString().getBytes(), fileName, contentType);
		} catch (Exception e) {
			logger.error("Failed to download the file");
			e.printStackTrace();
		}finally{
			long t2 = System.currentTimeMillis();
			logger.info("Exit: downloadCsvGhi() took time: " + String.valueOf(t2 - t1) + "ms");
		}
	}
	
	/**
	 * @author Joy
	 * Get History data depending upon given drcStudentID
	 * @throws Exception
	 */
	@RequestMapping("/process/getHistoryResult.htm")
	public @ResponseBody String getHistoryResult(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			SearchProcess process = new SearchProcess();
			process.setDRCStudentId(request.getParameter("drcStudentID"));
			
			TascDAOImpl stageDao = new TascDAOImpl();
			List<Map<String, String>> historyData = stageDao.getHistoryResult(process);
			logger.info("result size: " + historyData!=null? historyData.size() : "null");
			logger.info(listmapToJsonString(historyData, process));
			response.setContentType("application/json");
			response.getWriter().write(listmapToJsonString(historyData, process));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @author Joy
	 * Get data for single student
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/process/combinedGhi.htm")
	public ModelAndView combinedGhi(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Enter: combinedGhi()");
		SearchProcess process = new SearchProcess();
		try {
			if(!UserController.checkLogin(request)) return new ModelAndView("welcome", "message", "Please login.");
			
			process.setUuid(request.getParameter("uuid"));
			process.setStateCode(request.getParameter("stateCode"));
			process.setDRCStudentId(request.getParameter("drcStudentId"));
			process.setLevel1OrgCode(request.getParameter("level1OrgCode"));
			request.getSession().setAttribute("combinedGhiRequestTO", process);
			
			if((process.getUuid() != null && process.getUuid().length() > 0) || (process.getDRCStudentId() != null && process.getDRCStudentId().length() > 0) ) {
				TascDAOImpl stageDao = new TascDAOImpl();
				Map<String,List> mapGhi = stageDao.getCombinedGhi(process);
				request.setAttribute("combinedGhiList", (List<StudentDetailsTO>)mapGhi.get("op"));
				request.setAttribute("errorGhi", (List<StudentDetailsGhiTO>)mapGhi.get("er"));
				
				return new ModelAndView("combinedGhi", "message", "");
			} else {
				return new ModelAndView("combinedGhi", "message", "Please provide UUID or DRC Student ID");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("combinedGhi", "message","");
	}
	
}
