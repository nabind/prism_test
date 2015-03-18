package com.vaannila.web;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.vaannila.DAO.MapDAOImpl;
import com.vaannila.DAO.TascDAOImpl;
import com.vaannila.TO.SearchProcess;
import com.vaannila.TO.StudentDetailsTO;
import com.vaannila.TO.TASCProcessTO;

@Controller
public class MapController {
	
	String jsonStr = "";
	/**
	 * This method is to get all processes in datatable
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/process/mapProcess.htm")
	public ModelAndView tascProcess(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("Enter: mapProcess()");
		try {
			String adminid = request.getParameter("adminid");
			System.out.println("adminid = " + adminid);
			if(adminid == null) {
				adminid = (String) request.getSession().getAttribute("adminid");
			} else {
				request.getSession().setAttribute("adminid", adminid);
			}
			if(!UserController.checkLogin(request)) return new ModelAndView("welcome", "message", "Please login.");
			MapDAOImpl stageDao = new MapDAOImpl();
			System.out.println("getting processes...");
			List<TASCProcessTO> processes = stageDao.getProcess(null);
			System.out.println("got processes successfully");
			request.getSession().setAttribute("mapProcess", processes);
			convertProcessToJson(processes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Exit: mapProcess()");
		return new ModelAndView("mapProcess", "message", jsonStr);
	}
	
	@RequestMapping("/process/getErrorStudents.htm")
	public @ResponseBody String getErrorStudents(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("Enter: getErrorStudents()");
		try {
			String adminid = request.getParameter("adminid");
			System.out.println("adminid = " + adminid);
			if(adminid == null) {
				adminid = (String) request.getSession().getAttribute("adminid");
			} else {
				request.getSession().setAttribute("adminid", adminid);
			}
			MapDAOImpl stageDao = new MapDAOImpl();
			System.out.println("getting processes...");
			String taskId = request.getParameter("taskId");
			List<StudentDetailsTO> students = stageDao.getStudentDetails(taskId);
			System.out.println("got processes successfully");
			request.getSession().setAttribute("errorStudents", students);
			convertProcessToJson(students);
			
			response.setContentType("text/plain");
			response.getWriter().write(convertProcessToJson(students));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Exit: getErrorStudents()");
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
	
	
	
	@RequestMapping("/process/getMapProcessLog.htm")
	public @ResponseBody String getMapProcessLog(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String taskId = request.getParameter("taskId");
			MapDAOImpl stageDao = new MapDAOImpl();
			String processLog = stageDao.getProcessLog(taskId);
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
	
	@RequestMapping("/process/mapSearch.htm")
	public ModelAndView tascSearch(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("view method called");
		String adminid = request.getParameter("adminid");
		if(adminid != null) {
			request.getSession().setAttribute("adminid", adminid);
		}
		if(!UserController.checkLogin(request)) return new ModelAndView("welcome", "message", "Please login.");
		
		return new ModelAndView("mapSearch", "message", "");
	}
	
	/**
	 * This method is to search processes
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/process/searchMap.htm")
	public ModelAndView searchTasc(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("process method called");
		try {
			if(!UserController.checkLogin(request)) return new ModelAndView("welcome", "message", "Please login.");
			SearchProcess process = new SearchProcess();
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
		
		return new ModelAndView("mapProcess", "message", jsonStr);
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
	
	private <T> String convertProcessToJson(List<T> processes) {
		String jsonStr = null;
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        //xstream.alias("product", T.class);
		for(Iterator<T> itr = processes.iterator(); itr.hasNext();) {
			jsonStr = xstream.toXML(itr.next());
		}
		return jsonStr;
	}
	
	public static String getDateTime(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateformatter = new SimpleDateFormat(dateFormat);
		return dateformatter.format(cal.getTime());
	}
	
}
