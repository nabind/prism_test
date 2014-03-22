package com.ctb.prism.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.dao.BaseDAO;
import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.login.Service.ILoginService;
import com.ctb.prism.login.security.provider.AuthenticatedUser;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.report.service.IReportService;
import com.ctb.prism.report.transferobject.AssessmentTO;
import com.ctb.prism.report.transferobject.ManageMessageTO;
import com.ctb.prism.report.transferobject.ObjectValueTO;
import com.ctb.prism.report.transferobject.ReportTO;

@Controller
public class CommonController extends BaseDAO {
	
	private static final IAppLogger logger = LogFactory.getLoggerInstance(CommonController.class.getName());

	@Autowired
	private Utils utils;
	
	@Autowired
	private IReportService reportService;
	
	@Autowired
	private IPropertyLookup propertyLookup;
	
	@Autowired	private ILoginService loginService;
	
	/**
	 * Load error page
	 * @param jspPath
	 * @return
	 */
	@RequestMapping(value = "/showError", method = RequestMethod.GET)
	public ModelAndView loadErrorPage(HttpServletRequest req, HttpServletResponse res) {
		logger.log(IAppLogger.INFO, "Enter: loadErrorPage()");
		ModelAndView mv = new ModelAndView("error/error");
		logger.log(IAppLogger.INFO, "Exit: loadErrorPage()");
		return mv;
	}
	
	/**
	 * Load JSP view
	 * @param jspPath
	 * @return
	 */
	@RequestMapping(value = "/loadJSPView", method = RequestMethod.GET)
	public ModelAndView loadJSPView(@RequestParam("path") String jspPath) {
		logger.log(IAppLogger.INFO, "Enter: loadJSPView() = " + jspPath);
		ModelAndView mv = new ModelAndView(jspPath);
		logger.log(IAppLogger.INFO, "Exit: loadJSPView()");
		return mv;
	}
	
	/**
	 * Load Report jsp
	 * @param jspPath
	 * @return
	 */
	@RequestMapping(value = "/loadReportView", method = RequestMethod.GET)
	public ModelAndView loadJSPView(HttpServletRequest req, HttpServletResponse res) {
		String assessmentId = req.getParameter("assessmentId");
		String studentBioId = req.getParameter("studentId");
		if(studentBioId != null && studentBioId.length() == 0) {
			studentBioId = null;
		}
		ReportTO report = new ReportTO();
		report.setReportUrl(req.getParameter("reportUrl"));
		report.setReportName(req.getParameter("reportName"));
		String reportType  = req.getParameter("reportType");
		if(reportType != null && reportType.endsWith(IApplicationConstants.REPORT_TYPE_TABLE)) {
			report.setReportApiUrl(IApplicationConstants.REPORT_TABLE_URL);
		} else if(reportType != null && reportType.endsWith(IApplicationConstants.REPORT_TYPE_CUSTOM)) {  
			report.setCustomUrl(req.getParameter("customUrl"));
			report.setReportApiUrl(req.getParameter("customUrl"));
			report.setRefreshButtonClass("customRefresh");
			report.setScrolling("auto");
			if(reportType != null && reportType.endsWith(IApplicationConstants.REPORT_TYPE_CUSTOM_NO_FILTER)) {
				report.setHideFilter("hide");
			}
			req.getSession().setAttribute(IApplicationConstants.REPORT_TYPE_CUSTOM+req.getParameter("reportUrl"), IApplicationConstants.TRUE);
		} else {
			report.setReportApiUrl(IApplicationConstants.REPORT_API_URL);
		}
		report.setReportType(reportType);
		String reportId  = req.getParameter("reportId");
		report.setReportId( (reportId == null)? 0 : Long.parseLong(reportId));
		report.setTabCount(req.getParameter("tabCount"));
		report.setCurrentTabNumber(req.getParameter("currentTabNumber"));
		report.setAssessmentName(assessmentId);
		report.setStudentBioId(studentBioId);
		
		String productName = ("101".equals(assessmentId))? "TerraNova 3 : " : ("102".equals(assessmentId))? "TerraNova with PTCS : " : ("103".equals(assessmentId))? "TerraNova with InView : " : ("104".equals(assessmentId))? "Bible : " : ("105".equals(assessmentId))? "TerraNova Longitudinal : " : "";
		report.setProductName(productName);
		
		String jspPath = req.getParameter("path");
		ModelAndView mv = new ModelAndView(jspPath);
		mv.addObject("homeReport", report);
		return mv;
	}
	
	/**
	 * This method is for showing static PDF files from mount location
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping(value="/acsipdf", method=RequestMethod.GET)
	public ModelAndView acsiPdf(HttpServletRequest req, HttpServletResponse res)
			throws ServletException {
		OutputStream os = null;
		try {
			String fileName = utils.getAcsiPdfLocation() + req.getParameter("pdfFileName");
			String userType = req.getParameter("userType");
			logger.log(IAppLogger.INFO, CustomStringUtil.appendString("Downloading pdf, user type: "
					, userType, "file name : ", fileName));
			
			//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			//LdapUserDetailsImpl userDetails = (LdapUserDetailsImpl) auth.getPrincipal();
			String currentOrg = (String) req.getSession().getAttribute(IApplicationConstants.CURRORG);
			
			if(userType != null && userType.equals(currentOrg)) {
				File file = null;
				file = new File(fileName);
		
				byte[] pdf = null;
				if (file.isFile()) pdf = IOUtils.toByteArray(new FileInputStream(fileName)); //getFileData(file);
				
				if(pdf != null) {
					res.setContentType("application/pdf");
					res.setHeader("Content-Disposition",
							CustomStringUtil.appendString("attachment; filename=\"", file.getName(), "\""));
					res.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
					res.setHeader("Pragma", "public");
			
					os = res.getOutputStream();
					os.write(pdf);
					/*for (int i = 0; i < pdf.length; i++) {
						os.write(pdf[i]);
					}*/
					os.flush();
				} else {
					logger.log(IAppLogger.DEBUG, "PDF file not present is the specified location");
					return new ModelAndView("error/error");
				}
			} else {
				logger.log(IAppLogger.DEBUG, "User does not have acces to view this PDF");
				return new ModelAndView("error/error");
			}
		} catch (IOException e) {
			logger.log(IAppLogger.ERROR, "", e);
			return new ModelAndView("error/error");
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "", e);
			return new ModelAndView("error/error");
		} finally {
			IOUtils.closeQuietly(os);
		}
		return null;
	}
	
	@RequestMapping(value="/inorspdf", method=RequestMethod.GET)
	public ModelAndView inorsPdf(HttpServletRequest req, HttpServletResponse res)
			throws ServletException {
		OutputStream os = null;
		try {
			String rootPath = loginService.getRootPath(req.getParameter("customerId"), req.getParameter("testAdmin"));
			String fileName = rootPath + req.getParameter("pdfFileName");
			String userType = req.getParameter("userType");
			logger.log(IAppLogger.INFO, CustomStringUtil.appendString("Downloading pdf, user type: "
					, userType, "file name : ", fileName));
			
			//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			//LdapUserDetailsImpl userDetails = (LdapUserDetailsImpl) auth.getPrincipal();
			String currentOrg = (String) req.getSession().getAttribute(IApplicationConstants.CURRORG);
			
			if(userType != null && userType.equals(currentOrg)) {
				File file = null;
				file = new File(fileName);
		
				byte[] pdf = null;
				if (file.isFile()) pdf = IOUtils.toByteArray(new FileInputStream(fileName)); //getFileData(file);
				
				if(pdf != null) {
					res.setContentType("application/pdf");
					res.setHeader("Content-Disposition",
							CustomStringUtil.appendString("attachment; filename=\"", file.getName(), "\""));
					res.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
					res.setHeader("Pragma", "public");
			
					os = res.getOutputStream();
					os.write(pdf);
					/*for (int i = 0; i < pdf.length; i++) {
						os.write(pdf[i]);
					}*/
					os.flush();
				} else {
					logger.log(IAppLogger.DEBUG, "PDF file not present is the specified location");
					return new ModelAndView("error/error");
				}
			} else {
				logger.log(IAppLogger.DEBUG, "User does not have acces to view this PDF");
				return new ModelAndView("error/error");
			}
		} catch (IOException e) {
			logger.log(IAppLogger.ERROR, "", e);
			return new ModelAndView("error/error");
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "", e);
			return new ModelAndView("error/error");
		} finally {
			IOUtils.closeQuietly(os);
		}
		return null;
	}
	
	/**
	 * Utility method to convert file to byte[]
	 * @param file
	 * @return
	 */
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
	
	/**
	 * Method to keep alive users
	 * @param req
	 * @param res
	 * @return
	 * @throws ServletException
	 */

	
	@RequestMapping(value="/acsiAlert", method=RequestMethod.GET)
	public ModelAndView acsiAlert(HttpServletRequest req, HttpServletResponse res) throws ServletException {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			//LdapUserDetailsImpl userDetails = (LdapUserDetailsImpl) auth.getPrincipal();
			
			AuthenticatedUser authenticatedUser = (AuthenticatedUser) auth.getPrincipal();			
			UserTO loggedInUser = authenticatedUser.getUserTO();
			
			logger.log(IAppLogger.INFO, CustomStringUtil.appendString("-----KEEP ALIVE USER ------- "
					, loggedInUser.getUserName()));
			
			res.setContentType("plain/text");
			//res.setCharacterEncoding("UTF-8");
			res.getWriter().write("Success");
			
		} catch (IOException e) {
			logger.log(IAppLogger.ERROR, "", e);
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "", e);
		} 
		return null;
	}
	
	/**
	 * Method to return images to report
	 * @param req
	 * @param res
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping(value="/image", method=RequestMethod.GET)
	public ModelAndView image(HttpServletRequest req, HttpServletResponse res) throws ServletException {
		OutputStream os = null;
		try {
			logger.log(IAppLogger.INFO, "fetching image for report");
			String image = req.getParameter("image");
			String assessmentId = req.getParameter("assessmentId");
			if(assessmentId == null) assessmentId = (String) req.getSession().getAttribute(IApplicationConstants.CURR_ASSESSMENT);
			InputStream inputStream = null;/*(InputStream) req.getSession().getAttribute(
					CustomStringUtil.appendString(IApplicationConstants.REPORT_IMAGE, assessmentId));*/
			if(inputStream == null) {
				logger.log(IAppLogger.INFO, "Image stream not found in session");
				String logoFile = "";
				/*if("101".equals(assessmentId)) logoFile = "TerranovaLogo.jpg";
				if("102".equals(assessmentId)) logoFile = "PTCSLogo.jpg";
				if("103".equals(assessmentId)) logoFile = "InViewLogo.jpg";
				if("104".equals(assessmentId)) logoFile = "BibleLogo.jpg";
				if("105".equals(assessmentId)) logoFile = "TerranovaLogo.jpg";*/
				logoFile = propertyLookup.get(CustomStringUtil.appendString("report.",assessmentId, ".logo"));
				URL settingsUrl = Thread.currentThread().getContextClassLoader().getResource(logoFile);
				if(settingsUrl != null) inputStream = settingsUrl.openStream();
			}
			if(inputStream != null) {
				byte[] img = Utils.getFileDataFromInputStream(inputStream);
				res.setContentType("image/jpeg");
				os = res.getOutputStream();
				for (int i = 0; i < img.length; i++) {
					os.write(img[i]);
				}
			}
			
		} catch (IOException e) {
			logger.log(IAppLogger.ERROR, "", e);
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "", e);
		} 
		return null;
	}
	
	/**
	 * Method to clear all EH-Cache
	 * @param req
	 * @param res
	 * @return
	 * @throws ServletException
	 * @throws IOException 
	 */
	@RequestMapping(value="/clearConfigCache", method=RequestMethod.GET)
	public ModelAndView clearConfigCache(HttpServletRequest req, HttpServletResponse res) 
		throws ServletException, IOException {
		ModelAndView mv = new ModelAndView("common/success");
		try {
			reportService.removeConfigurationCache();
			logger.log(IAppLogger.INFO, "Config cache cleared ....");
			
			mv.addObject("message", "Config cache cleared !!!");
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "", e);
			mv.addObject("message", e.getMessage());
		} 
		return mv;
	}
	
	@Secured({"ROLE_CTB"})
	@RequestMapping(value="/clearReportCache", method=RequestMethod.GET)
	public ModelAndView clearReportCache(HttpServletRequest req, HttpServletResponse res) 
		throws ServletException, IOException {
		ModelAndView mv = new ModelAndView("common/success");
		try {
			reportService.removeReportCache();
			logger.log(IAppLogger.INFO, "Cache cleared ....");
			
			mv.addObject("message", "Cache cleared !!!");
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "", e);
			mv.addObject("message", e.getMessage());
		} 
		return mv;
	}
	
	@RequestMapping(value="/clearAllCache", method=RequestMethod.GET)
	public ModelAndView clearAllCache(HttpServletRequest req, HttpServletResponse res) 
		throws ServletException, IOException {
		ModelAndView mv = new ModelAndView("common/success");
		System.out.println("Inside clearAllCache");
		
		try {
			reportService.removeCache();
			logger.log(IAppLogger.INFO, "All Cache cleared ....");
			
			mv.addObject("message", "All Cache cleared !!!");
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "", e);
			mv.addObject("message", e.getMessage());
		} 
		return mv;
	}

}
