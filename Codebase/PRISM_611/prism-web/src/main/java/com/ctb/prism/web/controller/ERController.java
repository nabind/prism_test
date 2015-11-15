package com.ctb.prism.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ctb.prism.core.Service.IERService;
import com.ctb.prism.core.constant.IApplicationConstants.CANDIDATE_RPT_USER_TYPE;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.transferobject.JobTrackingTO;
import com.ctb.prism.core.util.AESEncryption;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.login.security.provider.AuthenticatedUser;
import com.ctb.prism.login.transferobject.UserTO;
import com.mysql.jdbc.Util;

/**
 * @author TCS
 * 
 */
//@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Controller
public class ERController {
	private static final IAppLogger logger = LogFactory
			.getLoggerInstance(ERController.class.getName());

	@Autowired private IPropertyLookup propertyLookup;
	@Autowired private IERService erService;
	
	/**
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws SystemException
	 */
	@RequestMapping(value = "/candidateReport", method = RequestMethod.GET)
	public void candidateReport(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		OutputStream fos = null;
		InputStream is = null;
		StringBuffer log = new StringBuffer();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			AuthenticatedUser authenticatedUser = (AuthenticatedUser) auth.getPrincipal();			
			UserTO loggedInUser = authenticatedUser.getUserTO();
			
			String studentBioId = req.getParameter("ctbstudentid");
			JobTrackingTO jobTrackingTO = new JobTrackingTO();
			jobTrackingTO.setStudentBioId(studentBioId);
			jobTrackingTO.setOrgCode(req.getParameter("org_node_code"));
			jobTrackingTO.setUuid(req.getParameter("uuid"));
			jobTrackingTO = erService.getStudent(jobTrackingTO);
			
			if(jobTrackingTO.getClikedOrgId() != 0) {
				logger.log(IAppLogger.INFO, "\nSending Candidate Report to ER for " + studentBioId);
				StringBuffer URLStringBuf = new StringBuffer();
				URLStringBuf.append(propertyLookup.get("bulkDownloadUrl"));
				URLStringBuf.append("icDownload.do?reportUrl=").append(propertyLookup.get("CandidateReportUrl"));
				URLStringBuf.append("&assessmentId=0&type=pdf&token=0&filter=true");
				URLStringBuf.append("&LoggedInUserName=").append(loggedInUser.getUserName());
				URLStringBuf.append("&LoggedInUserId=").append(loggedInUser.getUserId());
				URLStringBuf.append("&p_Org_Id=").append(jobTrackingTO.getClikedOrgId());
				URLStringBuf.append("&p_Student_Bio_Id=").append(studentBioId);
				URLStringBuf.append("&p_Form_Id=").append("-1");
				URLStringBuf.append("&p_Is_Bulk=2&p_Admin_Name=").append(jobTrackingTO.getCustomerId());
				URLStringBuf.append("&p_User_Type=").append(CANDIDATE_RPT_USER_TYPE.ERESOURCE.toString());
				URLStringBuf.append("&theme=").append("tasc");
				
				URL url1 = new URL(URLStringBuf.toString());
				
				// Contacting the URL
				logger.log(IAppLogger.INFO, "\nConnecting to: " + url1.toString());
				URLConnection urlConn = url1.openConnection();
	
				// Checking whether the URL contains a PDF
				if (!urlConn.getContentType().equalsIgnoreCase("application/pdf")) {
					logger.log(IAppLogger.ERROR, " : FAILED.\n[Sorry. This is not a PDF.]");
					log.append(CustomStringUtil.appendString(
							"Error getting candidate report for Student Bio Id # ", studentBioId));
				} else {
					res.setContentType("application/pdf");
					res.setHeader("Content-Disposition",
							CustomStringUtil.appendString("attachment; filename=\"", studentBioId, ".pdf", "\""));
					res.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
					res.setHeader("Pragma", "public");
			
					fos = res.getOutputStream();
					is = url1.openStream();
					// TODO - encrypt the input stream with AES
					//fos.write(IOUtils.toByteArray(is));
					fos.write(AESEncryption.encrypt(is));
					/*for (int i = 0; i < pdf.length; i++) {
						os.write(pdf[i]);
					}*/
					fos.flush();
				}
			} else {
				logger.log(IAppLogger.ERROR, "Given CTBSTUDENTID, UUID and STATE CODE is not matching with PRISM");
				showError(res, "Given CTBSTUDENTID, UUID and STATE CODE is not matching with PRISM");
			}
			
			// release resources
		} catch (IOException e) {
			logger.log(IAppLogger.ERROR, "", e);
			showError(res, "Error Downloading report. Please try after some time or contact system administrator.");
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "", e);
			showError(res, "Error Downloading report. Please try after some time or contact system administrator.");
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(fos);
		}
	}
	
	private void showError(HttpServletResponse response, String message) {
		response.setContentType("text/plain");
		try {
			response.getWriter().write( message );
		} catch (IOException e) {
			logger.log(IAppLogger.ERROR, message, e);
		}
	}
	
	
}
