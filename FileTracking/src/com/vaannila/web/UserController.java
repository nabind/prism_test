package com.vaannila.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.vaannila.DAO.CommonDAOImpl;
import com.vaannila.DAO.StageDAOImpl;
import com.vaannila.TO.AdminTO;
import com.vaannila.TO.OrgProcess;
import com.vaannila.TO.OrgTO;
import com.vaannila.TO.SearchProcess;
import com.vaannila.cipher.PasswordCipherer;
import com.vaannila.util.EmailSender;
import com.vaannila.util.PropertyFile;

@Controller
public class UserController extends MultiActionController {
	
	String jsonStr = "";
	
	@RequestMapping("/process/welcome.htm")
	public ModelAndView welcome(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("welcome method called");
		
		return new ModelAndView("welcome", "message", "");
	}
	@RequestMapping("/process/login.htm")
	public ModelAndView login(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("login method called");
		
		Properties prop = PropertyFile.loadProperties("acsi.properties");
		String username = prop.getProperty("j_username");
		String password = prop.getProperty("j_password");
		
		String j_username_0 = prop.getProperty("j_username_0");
		
		String j_username_2 = prop.getProperty("j_username_2");
		
		String j_username_1 = prop.getProperty("j_username_1");
		String j_password_1 = prop.getProperty("j_password_1");
		
		String j_username_3 = prop.getProperty("j_username_3");
		
		
		String givenUsername = request.getParameter("j_username");
		String givenPassword = request.getParameter("j_password");
		
		String encPwd = PasswordCipherer.getEncryptedString(givenPassword);
    	boolean tascuser = false, mapuser = false, supportuser = false;
		if(givenUsername != null && givenUsername.equals(j_username_1)) {
			password = j_password_1;
			username = j_username_1;
		}
		if(givenUsername != null && givenUsername.equals(j_username_0)) {
			//password = password;
			username = j_username_0;
			tascuser = true;
		}
		if(givenUsername != null && givenUsername.equals(j_username_2)) {
			//password = password;
			username = j_username_2;
			mapuser = true;
		}
		if(givenUsername != null && givenUsername.equals(j_username_3)) {
			//password = password;
			username = j_username_3;
			supportuser = true;
		}
		
    	if(encPwd != null && encPwd.equals(password)) {
    		if(username != null && username.equals(givenUsername)) {
    			request.getSession().setAttribute("validUser", "true");
    			request.getSession().setAttribute("userName", givenUsername);
    			
    			if(!tascuser && !mapuser && !supportuser) {
	    			CommonDAOImpl commonDao = new CommonDAOImpl();
	    			List<AdminTO> adminList = commonDao.getAllAdminYear();
	    			request.getSession().setAttribute("adminList", adminList);
	    			for(AdminTO admin : adminList) {
	    				if("Y".equals(admin.getCurrentAdmin())) {
	    					request.getSession().setAttribute("adminid", admin.getAdminId());
	    					break;
	    				}
	    			}
    			} else {
    				if(mapuser) return new ModelAndView("mapSearch", "message", "");
    				else if(tascuser) return new ModelAndView("tascSearch", "message", "");
    				else return new ModelAndView("supportPage", "message", "");
    			}
    			
//    			StageDAOImpl stageDao = new StageDAOImpl();
//    			List<OrgProcess> processes = stageDao.getProcessDetails(
//    					(String) request.getSession().getAttribute("adminid"));
//    			request.getSession().setAttribute("allProcess", processes);
    			/*if(prop.getProperty("j_username_0").equals(username)) {
    				return new ModelAndView("tascSearch", "message", "");
    			}*/
    			return new ModelAndView("process", "message", "");
    		}
    	}
		
		return new ModelAndView("welcome", "message", "Invalid login. Please try again.");
	}
	
	public static boolean checkLogin(HttpServletRequest request) {
		String user = (String) request.getSession().getAttribute("validUser");
		if(user != null && user.equals("true")) {
			return true;
		}
		return false;
	}
	
	public static boolean checkAdmin(HttpServletRequest request) {
		Properties prop = PropertyFile.loadProperties("acsi.properties");
		String username = prop.getProperty("j_username");
		String user = (String) request.getSession().getAttribute("userName");
		if(user != null && user.equals(username)) {
			return true;
		}
		return false;
	}
	@RequestMapping("/process/logout.htm")
	public ModelAndView logout(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("logout method called");
		
		request.getSession().invalidate();
		
		return new ModelAndView("welcome", "message", "");
	}
	@RequestMapping("/process/view.htm")
	public ModelAndView view(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("view method called");
		String adminid = request.getParameter("adminid");
		if(adminid != null) {
			request.getSession().setAttribute("adminid", adminid);
		}
		if(!checkLogin(request)) return new ModelAndView("welcome", "message", "Please login.");
		
		/*try {
			StageDAOImpl stageDao = new StageDAOImpl();
			List<OrgProcess> processes = stageDao.getProcessDetails();
			
			request.getSession().setAttribute("allProcess", processes);
			convertToJson(processes);
			
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		return new ModelAndView("process", "message", "");
	}
	
	/**
	 * This method is to search processes
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/process/search.htm")
	public ModelAndView search(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("process method called");
		try {
			if(!checkLogin(request)) return new ModelAndView("welcome", "message", "Please login.");
			SearchProcess process = new SearchProcess();
			String processId = request.getParameter("processId");
			long lProcessId = 0;
			if(processId != null && processId.length() > 0) {
				try {
					lProcessId = Long.valueOf(processId.trim());
				} catch (Exception e) {}
			}
			process.setProcessid(lProcessId);
			process.setStructElement(request.getParameter("structElem"));
			process.setUpdatedDate(request.getParameter("updatedDate"));
			process.setDataStatus(request.getParameter("dataStatus"));
			process.setPdfStatus(request.getParameter("pdfStatus"));
			process.setTargetStatus(request.getParameter("targetStatus"));
			process.setTargerEmailStatus(request.getParameter("emailStatus"));
			process.setProcessStatus(request.getParameter("processStatus"));
			
			StageDAOImpl stageDao = new StageDAOImpl();
			List<OrgProcess> processes = stageDao.searchProcess(process,
					(String) request.getSession().getAttribute("adminid"));
			
			request.getSession().setAttribute("allProcess", processes);
			convertToJson(processes);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("office", "message", jsonStr);
	}
	
	/**
	 * This method is to get all processes in datatable
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/process/process.htm")
	public ModelAndView process(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("process method called");
		try {
			String adminid = request.getParameter("adminid");
			if(adminid == null) {
				adminid = (String) request.getSession().getAttribute("adminid");
			} else {
				request.getSession().setAttribute("adminid", adminid);
			}
			if(!checkLogin(request)) return new ModelAndView("welcome", "message", "Please login.");
			StageDAOImpl stageDao = new StageDAOImpl();
			List<OrgProcess> processes = stageDao.getProcessDetails(adminid);
			
			request.getSession().setAttribute("allProcess", processes);
			convertToJson(processes);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("office", "message", jsonStr);
	}
	
	/**
	 * This method is to get all processes in ajax
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/process/processDetails.htm")
	public @ResponseBody String processDetails(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("processDetails method called");
		try {
			CommonDAOImpl commonDao = new CommonDAOImpl();
			OrgTO processes = commonDao.getSchoolDetails(request.getParameter("structElem"),
					(String) request.getSession().getAttribute("adminid"));
			if(processes == null) {
				processes = new OrgTO();
				processes.setProcessNotExist("true");
			} else {
				processes.setProcessNotExist("false");
			}
			response.setContentType("application/json");
			response.getWriter().write(convertToJson(processes));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * This method is to update school email id
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/process/updateEmail.htm")
	public @ResponseBody String updateEmail(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("processDetails method called");
		try {
			CommonDAOImpl commonDao = new CommonDAOImpl();
			commonDao.updateEmail(request.getParameter("newMail")
					,request.getParameter("structureElement")
					,(String) request.getSession().getAttribute("adminid") );
			
			StageDAOImpl stageDao = new StageDAOImpl();
			stageDao.updateEmail(request.getParameter("newMail")
					,request.getParameter("structureElement")
					,(String) request.getSession().getAttribute("adminid") );
			
			// update process log
			updateLog("School email address is changed from " + 
						request.getParameter("oldEmail") + " to " + 
						request.getParameter("newMail")
					, request.getParameter("processId") );
			
			response.setContentType("plain/text");
			response.getWriter().write("Updated");
		} catch (Exception e) {
			e.printStackTrace();
			response.setContentType("plain/text");
			response.getWriter().write("Error");
		}
		
		return null;
	}
	
	/**
	 * This method is to resend email to school with login pdf
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/process/sendMail.htm")
	public @ResponseBody String sendMail(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("send mail method called");
		try {
			String processId = request.getParameter("processId");
			String schoolEmail = request.getParameter("schoolEmail");
			String type = request.getParameter("type");
			
			String pdfFileLoc = "";
			String letterFileLoc = "";
			String message = "";
			List<OrgProcess> processes = (List<OrgProcess>) request.getSession().getAttribute("allProcess");
			for(OrgProcess orgProcess : processes) {
				if( processId != null && processId.trim().equals(""+orgProcess.getProcessid()) ) {
					/*pdfFileLoc = orgProcess.getProcessLog();
					if(pdfFileLoc != null && pdfFileLoc.indexOf("Created PDF with name : ") != -1) {
						pdfFileLoc = pdfFileLoc.substring( pdfFileLoc.indexOf("Created PDF with name : ")+24,
								pdfFileLoc.indexOf(".pdf")+4 );
					} else {
						pdfFileLoc = "";
						response.setContentType("text/plain");
						response.getWriter().write("There is no Login PDF for this process [Process Id: "+ processId + 
								"]. Please check the \"Process Log\" for more details.");
						return null;
					}*/
					
					if("L".equals(type)) {
						pdfFileLoc = orgProcess.getLoginPDFLoc();
						message = "Login";
					} else if("I".equals(type)) {
						pdfFileLoc = orgProcess.getLetterPDFLoc();
						message = "Invitation Letter";
					} else if("A".equals(type)) {
						pdfFileLoc = orgProcess.getLoginPDFLoc();
						letterFileLoc = orgProcess.getLetterPDFLoc();
						message = "Invitation Letter and Login";
					} 
					if(pdfFileLoc != null && pdfFileLoc.trim().length() == 0) {
						pdfFileLoc = "";
						response.setContentType("text/html");
						response.getWriter().write("<span style='color:red;font-size:20px'> There is no "+message+" PDF exists for this process [Process Id: "+ processId + 
								"]. Please check the \"Process Log\" for more details.</span>");
						
						return null;
					}
					break;
				}
			}
			
			if(pdfFileLoc != null && pdfFileLoc.length() > 0) {
				String pdfFileName = pdfFileLoc.substring(pdfFileLoc.lastIndexOf("/"), pdfFileLoc.length());
				//schoolEmail = "amit_dhara@ctb.com"; // TODO remove
				
				Properties prop = PropertyFile.loadProperties("acsi.properties");
				pdfFileLoc = pdfFileLoc.replaceAll(prop.getProperty("pdfGenPath"), prop.getProperty("pdfGenPathNew"));
				letterFileLoc = letterFileLoc.replaceAll(prop.getProperty("pdfGenPath"), prop.getProperty("pdfGenPathNew"));
				System.out.println("email pdfFileLoc >>>"+pdfFileLoc+"<<<");
				System.out.println("email letterFileLoc >>>"+letterFileLoc+"<<<");
				String mailSubject = prop.getProperty("mailSubject");
				String mailBody = prop.getProperty("messageBody")+prop.getProperty("messageFooter");
				//_SendMailSSL.sendMail(prop, schoolEmail, pdfFileLoc, pdfFileName, mailSubject, mailBody);
				EmailSender.sendMail(prop, schoolEmail, pdfFileLoc, letterFileLoc, pdfFileName, mailSubject, mailBody);
				
				// update process log
				updateLog(message+" PDF resend done.", processId);
				
				response.setContentType("text/plain");
				response.getWriter().write("Success");
			} else {
				response.setContentType("text/plain");
				response.getWriter().write("Error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setContentType("text/plain");
			response.getWriter().write("Error");
		}
		
		return null;
	}
	
	/**
	 * This method is to download login pdf
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/process/viewPdf.htm")
	public ModelAndView viewPdf(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("viewPdf method called");
		OutputStream os = null;
		try {
			String pdfFileLoc = "";
			String processId = request.getParameter("processId");
			String type = request.getParameter("type");
			List<OrgProcess> processes = (List<OrgProcess>) request.getSession().getAttribute("allProcess");
			for(OrgProcess orgProcess : processes) {
				if( processId != null && processId.trim().equals(""+orgProcess.getProcessid()) ) {
					/*pdfFileLoc = orgProcess.getProcessLog();
					if(pdfFileLoc != null && pdfFileLoc.indexOf("Created PDF with name : ") != -1) {
						pdfFileLoc = pdfFileLoc.substring( pdfFileLoc.indexOf("Created PDF with name : ")+24,
								pdfFileLoc.indexOf(".pdf")+4 );
					} else {
						pdfFileLoc = "";
						response.setContentType("text/html");
						response.getWriter().write("<span style='color:red;font-size:20px'> There is no Login PDF exists for this process [Process Id: "+ processId + 
								"]. Please check the \"Process Log\" for more details.</span>");
						
						return null;
					}*/
					String message = "";
					if("L".equals(type)) {
						pdfFileLoc = orgProcess.getLoginPDFLoc();
						message = "Login";
					} else if("I".equals(type)) {
						pdfFileLoc = orgProcess.getLetterPDFLoc();
						message = "Invitation Letter";
					} 
					if(pdfFileLoc != null && pdfFileLoc.trim().length() > 0) {
						Properties prop = PropertyFile.loadProperties("acsi.properties");
						pdfFileLoc = pdfFileLoc.replaceAll(prop.getProperty("pdfGenPath"), prop.getProperty("pdfGenPathNew"));
					}
					if(pdfFileLoc == null ||(pdfFileLoc != null && pdfFileLoc.trim().length() == 0)) {
						pdfFileLoc = orgProcess.getProcessLog();
						if(pdfFileLoc != null && pdfFileLoc.indexOf("Created PDF with name : ") != -1) {
							pdfFileLoc = pdfFileLoc.substring( pdfFileLoc.indexOf("Created PDF with name : ")+24,
									pdfFileLoc.indexOf(".pdf")+4 );
						} else {
							pdfFileLoc = "";
							response.setContentType("text/html");
							response.getWriter().write("<span style='color:red;font-size:20px'> There is no "+message+" PDF exists for this process [Process Id: "+ processId + 
							"]. Please check the \"Process Log\" for more details.</span>");
							
							return null;
						}
						/*
						pdfFileLoc = "";
						response.setContentType("text/html");
						response.getWriter().write("<span style='color:red;font-size:20px'> There is no "+message+" PDF exists for this process [Process Id: "+ processId + 
								"]. Please check the \"Process Log\" for more details.</span>");
						
						return null;*/
					} 
					break;
				}
			}
			
			//pdfFileLoc = "C:\\Documents and Settings\\amit_dhara\\My Documents\\Quick_Start_Guide.pdf"; // TODO remove
			System.out.println("pdfFileLoc >>>"+pdfFileLoc+"<<<");
			
			File file = null;
			file = new File(pdfFileLoc);
	
			byte[] pdf = IOUtils.toByteArray(new FileInputStream(file)); //getFileData(file);
			
			if(pdf != null) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition","attachment; filename=\"" + file.getName() + "\"");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
		
				os = response.getOutputStream();
				for (int i = 0; i < pdf.length; i++) {
					os.write(pdf[i]);
				}
			} else {
				System.out.println("PDF file not present is the specified location");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(os != null) try {os.close();}catch (Exception e) {}
		}
		return null;
	}
	
	/**
	 * Utility method to convert file to byte[]
	 * @param file
	 * @return
	 */
	@Deprecated
	private byte[] getFileData(final File file) {
		byte[] fileData = null;
		java.io.InputStream inputStrem = null;
		int i = 0;
		if (file.isFile()) {
			java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
			try {
				inputStrem = file.toURL().openStream();
				while ((i = inputStrem.read()) != -1) {
					baos.write(i);
				}
				fileData = baos.toByteArray();
			} catch (java.net.MalformedURLException exMalformedURLException) {
				exMalformedURLException.printStackTrace();
			} catch (java.io.IOException exIOException) {
				exIOException.printStackTrace();
			} finally {
				try {
					if (inputStrem != null)
						inputStrem.close();
					baos.close();
				} catch (IOException exIOException) {
					exIOException.printStackTrace();
				}
			}
		}
		return fileData;
	}
	@RequestMapping("/process/getProcessLog.htm")
	public @ResponseBody String getProcessLog(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String processId = request.getParameter("processId");
			StageDAOImpl stageDao = new StageDAOImpl();
			String processLog = stageDao.getProcessLog(Long.valueOf(processId));
			if(processLog != null) processLog = processLog.replaceAll("\n", "<br>");
			
			response.setContentType("text/plain");
			response.getWriter().write(processLog);
		} catch (Exception e) {
			System.out.println("Failed to get log");
			e.printStackTrace();
		}
		return null;
	}
	
	private static void updateLog(String message, String processId) {
		try {
			StringBuffer processLog = new StringBuffer();
			processLog.append("\n");
			processLog.append(getTime()).append(message);
			
			StageDAOImpl stageDao = new StageDAOImpl();
			stageDao.updateProcessLog(Long.valueOf(processId), processLog.toString());
		} catch (Exception e) {
			System.out.println("Failed to update log");
			e.printStackTrace();
		}
		
	}
	
	private static String getTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateformatter = new SimpleDateFormat("hh:mm:ss a");
		StringBuffer time = new StringBuffer();
		time.append("[");
		time.append(dateformatter.format(cal.getTime()));
		time.append("] [PDF Util] ");
		return time.toString();
	}
	
	public void convertToJson(List<OrgProcess> processes) {
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("product", OrgProcess.class);
		for(Iterator<OrgProcess> itr = processes.iterator(); itr.hasNext();) {
			jsonStr = xstream.toXML(itr.next());
		}
	}
	
	public String convertToJson(OrgTO processes) {
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("process", OrgTO.class);
		return xstream.toXML(processes);
	}
	
}
