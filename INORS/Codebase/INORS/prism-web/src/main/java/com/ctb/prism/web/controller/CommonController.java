package com.ctb.prism.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.theme.CookieThemeResolver;

import com.ctb.prism.core.Service.IRepositoryService;
import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.dao.BaseDAO;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.core.util.FileUtil;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.login.Service.ILoginService;
import com.ctb.prism.login.security.encoder.DigitalMeasuresHMACQueryStringBuilder;
import com.ctb.prism.login.security.provider.AuthenticatedUser;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.report.service.IReportService;
import com.ctb.prism.report.transferobject.ReportTO;

@Controller
public class CommonController extends BaseDAO {
	
	private static final IAppLogger logger = LogFactory.getLoggerInstance(CommonController.class.getName());

	// @Autowired
	// private Utils utils;
	
	@Autowired
	private IReportService reportService;
	
	@Autowired
	private IPropertyLookup propertyLookup;
	
	@Autowired	private ILoginService loginService;
	
	@Autowired
	private IRepositoryService repositoryService;
	
	
	@Autowired private CookieThemeResolver themeResolver;
	
	@Autowired
    DigitalMeasuresHMACQueryStringBuilder hmac;
	
	private String ASSET_PATH = "/ACSIREPORTS/Static_Files";

	
	
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
	 * Load empty report
	 * @param jspPath
	 * @param reportName
	 * @param reportMsg
	 * @return
	 */
	@RequestMapping(value = "/loadEmptyReport", method = RequestMethod.GET)
	public ModelAndView loadEmptyReport(@RequestParam("reportName") String reportName, 
			@RequestParam("reportMsg") String reportMsg) {
		ModelAndView modelAndView = new ModelAndView("report/emptyReport");
		modelAndView.addObject("reportName", reportName != null ? 
				reportName.substring(reportName.lastIndexOf(",")+1) : reportName);
		modelAndView.addObject("reportMsg", IApplicationConstants.EMPTY_REPORT);
		
		return modelAndView;
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
	 * Load report jsp - parameter will come in post
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "/loadReport", method = RequestMethod.POST)
	public ModelAndView loadReport(HttpServletRequest req, HttpServletResponse res) {
		return loadJSPView(req, res);
	}
	
	/**
	 * This method is for showing static PDF files from mount location
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws ServletException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/acsipdf", method=RequestMethod.GET)
	public ModelAndView acsiPdf(HttpServletRequest req, HttpServletResponse res)
			throws ServletException {
		OutputStream os = null;
		Map<String, Object> propertyMap = new HashMap<String, Object>();
		try {
			propertyMap=(Map<String,Object>)req.getSession().getAttribute("propertyMap");
			//String fileName = utils.getAcsiPdfLocation() + req.getParameter("pdfFileName");
			String fileName = propertyMap.get(IApplicationConstants.STATIC_PDF_LOCATION) + req.getParameter("pdfFileName");
			String userType = req.getParameter("userType");
			logger.log(IAppLogger.INFO, CustomStringUtil.appendString("Downloading pdf, user type: "
					, userType, "file name : ", fileName));
			
			//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			//LdapUserDetailsImpl userDetails = (LdapUserDetailsImpl) auth.getPrincipal();
			String currentOrg = (String) req.getSession().getAttribute(IApplicationConstants.CURRORG);
			
			if(userType != null && userType.equals(currentOrg)) {
				/*File file = null;
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
					for (int i = 0; i < pdf.length; i++) {
						os.write(pdf[i]);
					}
					os.flush();
				} else {
					logger.log(IAppLogger.DEBUG, "PDF file not present is the specified location");
					return new ModelAndView("error/error");
				}*/
				logger.log(IAppLogger.DEBUG, "PDF file "+ fileName + " is getting downloaded from S3");
				byte[] data =repositoryService.getAssetBytes(fileName);	
				if(data != null) {
					// Download the file
					FileUtil.browserDownload(res, data, FileUtil.getFileNameFromFilePath(fileName));
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
			logger.log(IAppLogger.DEBUG, "PDF file not present is the specified location");
			return new ModelAndView("error/error");
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "", e);
			logger.log(IAppLogger.DEBUG, "PDF file not present is the specified location");
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
			String rootPath = loginService.getRootPath(req.getParameter("customerId"), req.getParameter("testAdmin"), Utils.getContractName());
			String fileName = rootPath + req.getParameter("pdfFileName");
			String userType = req.getParameter("userType");
			logger.log(IAppLogger.INFO, CustomStringUtil.appendString("Downloading pdf, user type: "
					, userType, "file name : ", fileName));
			
			//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			//LdapUserDetailsImpl userDetails = (LdapUserDetailsImpl) auth.getPrincipal();
			String currentOrg = (String) req.getSession().getAttribute(IApplicationConstants.CURRORG);
			
			if(userType != null && userType.equals(currentOrg)) {/*
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
					for (int i = 0; i < pdf.length; i++) {
						os.write(pdf[i]);
					}
					os.flush();
				} else {
					logger.log(IAppLogger.DEBUG, "PDF file not present is the specified location");
					return new ModelAndView("error/error");
				}
			*/
				logger.log(IAppLogger.DEBUG, "PDF file "+ fileName + " is getting downloaded from S3");
				byte[] data =repositoryService.getAssetBytes(fileName);
				if(data != null) {
					// Download the file
					FileUtil.browserDownload(res, data, FileUtil.getFileNameFromFilePath(fileName));
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
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/resourcepdf", method=RequestMethod.GET)
	public ModelAndView resourcepdf(HttpServletRequest req, HttpServletResponse res)
			throws ServletException {
		OutputStream os = null;
		Map<String, Object> propertyMap = new HashMap<String, Object>();
		try {
			//String fileName = utils.getAcsiPdfLocation() + req.getParameter("pdfFileName");
			
			propertyMap=(Map<String,Object>)req.getSession().getAttribute("propertyMap");
			//String fileName = utils.getAcsiPdfLocation() + req.getParameter("pdfFileName");
			String fileName = propertyMap.get(IApplicationConstants.STATIC_PDF_LOCATION) + req.getParameter("pdfFileName"); 
			
			logger.log(IAppLogger.INFO, CustomStringUtil.appendString("Downloading pdf - file name : ", fileName));
			
			//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			//LdapUserDetailsImpl userDetails = (LdapUserDetailsImpl) auth.getPrincipal();
			//String currentOrg = (String) req.getSession().getAttribute(IApplicationConstants.CURRORG);
			
		/*	File file = null;
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
				for (int i = 0; i < pdf.length; i++) {
					os.write(pdf[i]);
				}
				os.flush();
			} else {
				logger.log(IAppLogger.DEBUG, "PDF file not present is the specified location");
				return new ModelAndView("error/error");
			}*/
			
			logger.log(IAppLogger.DEBUG, "PDF file "+ fileName + " is getting downloaded from S3");
			byte[] data =repositoryService.getAssetBytes(fileName);
			if(data != null) {
				// Download the file
				FileUtil.browserDownload(res, data, FileUtil.getFileNameFromFilePath(fileName));
			} else {
				logger.log(IAppLogger.DEBUG, "PDF file not present is the specified location");
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
		logger.log(IAppLogger.INFO, "Inside clearAllCache");
		
		String theme = themeResolver.resolveThemeName(req);
		
		try {
			reportService.removeCache();
			//reportService.removeCache(Utils.getContractNameNoLogin(theme));
			logger.log(IAppLogger.INFO, "All Cache cleared ....");
			
			mv.addObject("message", "All Cache cleared !!!");
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "", e);
			mv.addObject("message", e.getMessage());
		} 
		return mv;
	}
	
	@RequestMapping(value="/clearContractCache", method=RequestMethod.GET)
	public ModelAndView clearContractCache(HttpServletRequest req, HttpServletResponse res) 
		throws ServletException, IOException {
		
		ModelAndView mv = new ModelAndView("common/success");
		logger.log(IAppLogger.INFO, "Inside clearContractCache");
		
		String expiryTime = req.getParameter("time_stamp");
		String signature = req.getParameter("signature");
		String theme = req.getParameter("theme");
		
		if(theme== null) {
			theme = themeResolver.resolveThemeName(req);
		}

		logger.log(IAppLogger.INFO,"expiryTime : {}" + expiryTime);
		logger.log(IAppLogger.INFO,"signature : {}" + signature);
		logger.log(IAppLogger.INFO,"theme : {}" + theme);
		
		if(signature != null && !signature.isEmpty()) {
			 try{
				 if(hmac.isValidRequest(expiryTime, signature, theme)) {
					 logger.log(IAppLogger.INFO,"Authentication Filter : User request is valid, clearing cache for: " + theme);
					//reportService.removeCache(Utils.getContractName(theme));
					 reportService.removeCacheSimpleDB(Utils.getContractName(theme));
					 logger.log(IAppLogger.INFO, "All Cache cleared ....");
						
					 mv.addObject("message", "All Cache cleared !!! for " +Utils.getContractName(theme));
				 } else{
					 logger.log(IAppLogger.ERROR,"Authentication Filter : User request is not valid. Either token is expired or request parameters are tempared.");
					 throw new AuthenticationServiceException("User request is not valid. Either token is expired or request parameters are tempared.");
				 }
			 } catch(AuthenticationServiceException ae) {
				 logger.log(IAppLogger.ERROR, "", ae);
				 mv.addObject("message", ae.getMessage());
		     } catch (Exception e) {
				 logger.log(IAppLogger.ERROR, "", e);
				 mv.addObject("message", e.getMessage());
			 }
		}
		
		return mv;
	}
	
	
	/**
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value="/getAssestList" , method=RequestMethod.GET)
	public ModelAndView getAssestList (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ModelAndView mv = new ModelAndView("common/assetList");
		String assetPath = request.getParameter("assetPath");
		List<String> assets = repositoryService.getAssetList(assetPath);
		logger.log(IAppLogger.INFO, "assets count: " + assets.size());
		mv.addObject("assets", assets);
		return mv;
	}
	
	/**
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value="/downloadAssest" , method=RequestMethod.GET)
	public void downloadAssest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fileName = request.getParameter("assetPath");
		//@SuppressWarnings("unchecked")
		//Map<String, Object> propertyMap = (Map<String,Object>)request.getSession().getAttribute("propertyMap");
		//String fileName = propertyMap.get(IApplicationConstants.STATIC_PDF_LOCATION) + request.getParameter("assetPath"); 
		try{
			byte[] data = repositoryService.getAssetBytes(fileName);
			logger.log(IAppLogger.INFO, "data.length = " + data.length);
			FileUtil.browserDownload(response, data, FileUtil.getFileNameFromFilePath(fileName));
		} catch(Exception e){
			logger.log(IAppLogger.ERROR, "assets not found: " + fileName);
		}
				
	}
	
	@RequestMapping(value="/downloadAssestByS3Key" , method=RequestMethod.GET)
	public void downloadAssestByS3Key(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String s3Key = request.getParameter("s3Key");
		byte[] data = repositoryService.getAssetBytesByS3Key(s3Key);
		logger.log(IAppLogger.INFO, "data.length = " + data.length);
		FileUtil.browserDownload(response, data, FileUtil.getFileNameFromFilePath(s3Key));
	}
	
	/**
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/displayAssest", method = RequestMethod.GET)
	public void displayAssest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String assetPath = request.getParameter("assetPath");
		byte[] data = repositoryService.getAssetBytes(assetPath);
		logger.log(IAppLogger.INFO, "data.length = " + data.length);
		if (assetPath.endsWith(".pdf") || assetPath.endsWith(".PDF")) {
			response.setContentType("application/pdf");
		}
		FileUtil.browserDisplay(response, data);
	}
	
	/**
	 * @param fullyQualifiedS3Key
	 * @param file
	 * @throws Exception
	 */
	private void uploadAssestToS3(String fullyQualifiedS3Key, File file) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: uploadAssestToS3()");
		repositoryService.uploadAssetByS3Key(fullyQualifiedS3Key, file);
		logger.log(IAppLogger.INFO, "Exit: uploadAssestToS3()");
	}

	/**
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/uploadAllAssestsByS3Keys", method = RequestMethod.GET)
	public void uploadAllAssestsByS3Keys(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: uploadAllAssestsByS3Keys()");
		try {
			String s3Key = request.getParameter("s3Key");
			String dir = request.getParameter("dir");
			logger.log(IAppLogger.INFO, "s3Key = " + s3Key);
			logger.log(IAppLogger.INFO, "dir = " + dir);
			File folder = new File(dir);
			File[] listOfFiles = folder.listFiles();
			for (File f : listOfFiles) {
				if (f.isFile()) {
					try {
						String fullyQualifiedS3Key = s3Key + f.getName();
						uploadAssestToS3(fullyQualifiedS3Key, f);
						logger.log(IAppLogger.WARN, "S3 Upload OK for: " + fullyQualifiedS3Key);
					} catch (Exception e) {
						logger.log(IAppLogger.WARN, "S3 Upload Failed for: " + f.getAbsolutePath());
						logger.log(IAppLogger.WARN, "S3 Upload Issue: " + e.getMessage());
					}
				}
			}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "S3 Upload Error: " + e.getMessage());
		}
		logger.log(IAppLogger.INFO, "Exit: uploadAllAssestsByS3Keys()");
	}

}
