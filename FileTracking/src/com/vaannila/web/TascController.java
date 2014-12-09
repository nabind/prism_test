package com.vaannila.web;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.vaannila.DAO.TascDAOImpl;
import com.vaannila.TO.SearchProcess;
import com.vaannila.TO.TASCProcessTO;
import com.vaannila.util.Utils;

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
	
	public void convertProcessToJson(List<TASCProcessTO> processes) {
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("product", TASCProcessTO.class);
		for(Iterator<TASCProcessTO> itr = processes.iterator(); itr.hasNext();) {
			jsonStr = xstream.toXML(itr.next());
		}
	}
	
	public static String getDateTime(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateformatter = new SimpleDateFormat(dateFormat);
		return dateformatter.format(cal.getTime());
	}
}
