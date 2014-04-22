package com.prism.itext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.lowagie.text.pdf.PdfEncryptor;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.prism.cipher.PasswordGenerator;
import com.prism.constant.Constants;
import com.prism.dao.CommonDAO;
import com.prism.dao.CommonDAOImpl;
import com.prism.mail.EmailSender;
import com.prism.to.OrgTO;
import com.prism.to.UserTO;
import com.prism.util.CustomStringUtil;
import com.prism.util.FileUtil;
import com.prism.util.PropertyFile;
import com.prism.util.ReportPDF;

/**
 * @author Amitabha Roy
 * 
 */
public class UserAccountPdf {
	private static final Logger logger = Logger.getLogger(UserAccountPdf.class);
	private static boolean ENCRYPTION_NEEDED = false;
	private static boolean ARCHIVE_NEEDED = false;
	private static String DDMMYY = "";
	private static Map<Integer, String> orgMap = null;
	private static StringBuffer processLog = null;
	private static String CUSOMERID = null;
	
	private static CommonDAO dao = null;

	public static void main(String[] args) {
		//args = new String[] { "L", "604893"};
		logger.info("Program Starts...");
		boolean validArgs = validateCommandLineArgs(args);
		if (validArgs) {
			String flag = args[0];
			Properties prop = PropertyFile.loadProperties(Constants.PROPERTIES_FILE);
			Properties jdbcProp = PropertyFile.loadProperties(Constants.JDBC_PROPERTIES_FILE);
			if (prop == null) {
				logger.error("Not able to read property file.");
				System.exit(1);
			} else {
				int length = (args.length > 0) ? args.length - 1 : 0;
				String[] ids = new String[length];
				// Creates a new array with args[1] to args[last element]
				System.arraycopy(args, 1, ids, 0, length);
				//ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
				try {
					dao = new CommonDAOImpl(jdbcProp);
				} catch (Exception e) {
					e.printStackTrace();
				};
				if ("true".equals(prop.getProperty("pdfEncryptionRequired"))) {
					ENCRYPTION_NEEDED = true;
				}
				if ("true".equals(prop.getProperty("archiveNeeded"))) {
					ARCHIVE_NEEDED = true;
				}
				String encDocLocation = "";
				String identifier = "";
				for (String id : ids) {
					if (flag.equalsIgnoreCase(Constants.ARGS_OPTIONS.L.toString())) {
						//// processLoginPdf(prop, dao, id);
						encDocLocation = manupulateTenants(id, prop, null, false, false);
					} else if (flag.equalsIgnoreCase(Constants.ARGS_OPTIONS.I.toString())) {
						String letterLocation = processIcLetterPdf(prop, dao, id);
						ARCHIVE_NEEDED = false;
						logger.info("IC Letter Location: " + letterLocation);
						archiveICLetter(prop);
						
						letterLocation = processIndividualIcLetterPdf(prop, dao, id);
						identifier = "IC_";
						logger.info("IC Letter Location: " + letterLocation);
						//archiveICLetter(prop);
					} else if (flag.equalsIgnoreCase(Constants.ARGS_OPTIONS.A.toString())) {
						logger.info("All/Both Login Pdf and IC Letter...");
						////processLoginPdf(prop, dao, id);
						String letterLocation = processIcLetterPdf(prop, dao, id);
						encDocLocation = manupulateTenants(id, prop, letterLocation, false, false);
						logger.info("IC Letter Location: " + letterLocation);
						logger.info("All/Both Login Pdf and IC Letter Completed.");
						archiveICLetter(prop);
					} else if (flag.equalsIgnoreCase(Constants.ARGS_OPTIONS.S.toString())) {
						String letterLocation = processIndividualIcLetterPdf(prop, dao, id);
						identifier = "IC_";
						logger.info("IC Letter Location: " + letterLocation);
						archiveICLetter(prop);
					} else if (flag.equalsIgnoreCase(Constants.ARGS_OPTIONS.X.toString())) {
						String letterLocation = processIndividualIcLetterPdfFromExtractTable(prop, dao, id);
						logger.info("IC Letter Location: " + letterLocation);
						archiveICLetter(prop);
					}
				}
				
				if(ARCHIVE_NEEDED) {
					File arc = new File(CustomStringUtil.appendString(
							prop.getProperty("pdfGenPath"),
							File.separator,"archive",File.separator));
					if(!arc.exists()) arc.mkdir();
					String arcFilePath = CustomStringUtil.appendString(
							arc.getAbsolutePath(),File.separator,
							prop.getProperty("tempPdfLocation"),
							prop.getProperty("schoolaArc"),
							identifier,
							getDateTime("ddMMyyyyHHmmss"),".ZIP");
							
					archiveFiles(prop.getProperty("pdfGenPath"),arcFilePath);
				}
				/*try {
					applicationContext.close();
				} catch (Exception e) {
					logger.warn("Error in closing Application Context");
				}*/
			}
		}
		logger.info("The End!");
	}
	
	private static void archiveICLetter(Properties prop) {
		File arc = new File(CustomStringUtil.appendString(
				prop.getProperty("pdfGenPathIC"), File.separator, CUSOMERID, 
				File.separator,"archive",File.separator));
		if(!arc.exists()) arc.mkdir();
		String arcFilePath = CustomStringUtil.appendString(
				arc.getAbsolutePath(),File.separator,
				"CTB_", getDateTime("yyMMdd"),"_", PasswordGenerator.generateRandomAlpha(3),".ZIP");
				
		archiveFiles(CustomStringUtil.appendString(
				prop.getProperty("pdfGenPathIC"), File.separator, CUSOMERID),arcFilePath);
	}

	private static String processIcLetterPdf(Properties prop, CommonDAO dao, String schoolId) {
		logger.info("Processing for IC Letter...");
		String letterLoc = "";
		try {
			OrgTO school = dao.getSchoolDetails(schoolId, false); // Users not required
			if(school != null) {
				String adminId = dao.getCurrentAdminYear();
				// List<String> studentIdList = dao.getStudentIdList(schoolId);
				List<String> tempFileList = new ArrayList<String>();
				List<String> pdfPathList = new ArrayList<String>();
				List<String> pdfDistPathList = new ArrayList<String>();
				
				File folder = new File(CustomStringUtil.appendString(prop.getProperty("pdfGenPathIC"), File.separator, school.getCustomerCode()));
				if(!folder.exists()) {
					folder.mkdir();
				}
				CUSOMERID = school.getCustomerCode();
				String schoolCoverUrl = getSchoolCoverURLString(prop, schoolId);
				String schoolCoverPath = ReportPDF.savePdfFromPrismWeb(getOrgPdfPath(prop, school.getSchoolCode(), school.getCustomerCode(), "SCHOOL"), new URL(schoolCoverUrl));
				String districtCoverUrl = getDistrictCoverURLString(prop, schoolId);
				String districtCoverPath = ReportPDF.savePdfFromPrismWeb(getOrgPdfPath(prop, school.getDistrictCode(), school.getCustomerCode(), "DISTRICT"), new URL(districtCoverUrl));
				pdfDistPathList.add(districtCoverPath);
				pdfDistPathList.add(prop.getProperty("WHATS_IN_THE_BOX"));
				pdfDistPathList.add(prop.getProperty("GENERIC_LETTER"));
				pdfDistPathList.add(prop.getProperty("SAMPLE_IC"));
				
				pdfPathList.add(schoolCoverPath);
				pdfPathList.add(prop.getProperty("WHATS_IN_THE_BOX"));
				pdfPathList.add(prop.getProperty("GENERIC_LETTER"));
				
				String docName = getPdfPath(prop, school.getDistrictCode(), school.getSchoolCode(), school.getCustomerCode(), false);//getDocName(school, prop);
				// for (String studentBioId : studentIdList) { // TODO : File is same for all students so skipping the loop
				// letterLoc = ReportPDF.saveLetterFromPrismWeb(prop, schoolId, school.getElementName(), school.getCustomerCode(), adminId, /* studentBioId */"-1", false, false); // -1 for all students combined pdf
				String pdfPath = getPdfPath(prop, school.getDistrictCode(), school.getSchoolCode(), school.getCustomerCode(), true);
				String urlString = getURLString(prop, schoolId, adminId, "-1", false);
				URL url = new URL(urlString);
				letterLoc = ReportPDF.savePdfFromPrismWeb(pdfPath, url);
				if ((letterLoc != null) && (!letterLoc.isEmpty())) {
					pdfPathList.add(letterLoc);
					tempFileList.add(letterLoc);
					tempFileList.add(schoolCoverPath);
					tempFileList.add(districtCoverPath);
				}
				// }
				
				if (!pdfDistPathList.isEmpty()) {
					String distPdf = CustomStringUtil.appendString(prop.getProperty("pdfGenPathIC"), File.separator, school.getCustomerCode(), 
							File.separator, prop.getProperty("ICLetterFile"), 
							school.getDistrictCode(), ".pdf");
					File file = new File(distPdf);
					if(!file.exists() && "PUBLIC".equals(school.getOrgMode())) {
						byte[] mergedPdfBytes = FileUtil.getMergedPdfBytes(pdfDistPathList, "");
						FileUtil.createFile(distPdf, mergedPdfBytes);
						logger.info("District letter created @ " + distPdf);
					}
				} 
				if (!pdfPathList.isEmpty()) {
					byte[] mergedPdfBytes = FileUtil.getMergedPdfBytes(pdfPathList, "");
					FileUtil.createFile(docName, mergedPdfBytes);
					letterLoc = docName;
					logger.info("IC letter created @ " + letterLoc);
				} else {
					logger.warn("No pdf found");
				}
	
				FileUtil.removeFile(tempFileList);
			} else {
				logger.info("No school found with org-nodeid " + schoolId);
			}
		} catch (Exception e) {
			logger.error("Error processing : Java exception : " + e.getMessage());
			e.printStackTrace();
			return "";
		}
		logger.info("Processing for IC Letter Completed.");
		return letterLoc;
	}

	/*
	 * Individual IC letter in a school
	 */
	private static String processIndividualIcLetterPdf(Properties prop, CommonDAO dao, String schoolId) {
		logger.info("Processing for IC Letter...");
		String letterLoc = "";
		String rootPath = null;
		try {
			OrgTO school = dao.getSchoolDetails(schoolId, false); // Users not required
			CUSOMERID = school.getCustomerCode();
			if(school != null) {
				String adminId = "-1";
				List<String> studentIdList = dao.getStudentIdList(schoolId); // Actually test element id list
				logger.info(studentIdList.size() + " students found for school id " + schoolId);
				Map<String, String> pdfPathList = new HashMap<String, String>();
				List<String> tempFileList = new ArrayList<String>();
	
				int count = 0;
				for (String studentBioId : studentIdList) {
					logger.info("Processing " + ++count + " of " + studentIdList.size() + " students");
					String pdfPath = getIndividualIcPdfPath(prop, school.getDistrictCode(), school.getSchoolCode(), school.getCustomerCode(), studentBioId);
					String urlString = getIndividualIcURLString(prop, schoolId, adminId, studentBioId, false);
					URL url = new URL(urlString);
					letterLoc = ReportPDF.savePdfFromPrismWeb(pdfPath, url);
					if ((letterLoc != null) && (!letterLoc.isEmpty())) {
						pdfPathList.put(studentBioId, letterLoc);
						tempFileList.add(letterLoc);
					}
				}
				rootPath = dao.getRootPath(schoolId);
				/*if("Y".equals(prop.getProperty("LOCAL_TEST_MODE"))){
					rootPath = "D:\\Test\\IC" + CustomStringUtil.replaceAll(rootPath, "/", "\\\\");
				}*/
				FileUtil.copyFiles(rootPath, new HashSet<String>(pdfPathList.values()));
				if (!pdfPathList.isEmpty()) {
					dao.updateStudentsPDFloc(pdfPathList);
					logger.debug("IC letter created @ " + rootPath);
				} else {
					logger.warn("No pdf found");
				}
			}
		} catch (Exception e) {
			logger.error("Error processing : Java exception : " + e.getMessage());
			e.printStackTrace();
			return "";
		}
		logger.info("Processing for IC Letter Completed.");
		return rootPath;
	}

	/*
	 * Individual IC letter in a school
	 */
	private static String processIndividualIcLetterPdfFromExtractTable(Properties prop, CommonDAO dao, String schoolId) {
		logger.info("Processing for IC Letter From Extract Table...");
		String letterLoc = "";
		try {
			OrgTO school = dao.getSchoolDetails(schoolId, false); // Users not required
			if(school != null) {
				String adminId = dao.getCurrentAdminYear();
				List<String> studentIdList = dao.getStudentIdListFromExtractTable(schoolId);
				// List<String> pdfPathList = new ArrayList<String>();
				Map<String, String> pdfPathList = new HashMap<String, String>();
	
				String docName = prop.getProperty("pdfGenPath") + File.separator + prop.getProperty("tempPdfLocation") + prop.getProperty("districtText") + prop.getProperty("schoolText")
						+ school.getDateStrWtYear() + "_IC.zip";
				for (String studentBioId : studentIdList) { // TODO : File is same for all students so skipping the loop
					letterLoc = ReportPDF.saveLetterFromPrismWeb(prop, "-1", school.getElementName(), school.getCustomerCode(), adminId, studentBioId, true, false);// for all students in school separate pdf
					if ((letterLoc != null) && (!letterLoc.isEmpty())) {
						// pdfPathList.add(letterLoc);
						pdfPathList.put(studentBioId, letterLoc);
					}
				}
	
				dao.updateStudentsPDFloc(pdfPathList);
	
				if (!pdfPathList.isEmpty()) {
					letterLoc = FileUtil.createPDFFile(docName, new ArrayList<String>(pdfPathList.values()));
					logger.debug("IC letter created @ " + letterLoc);
				} else {
					logger.warn("No pdf found");
				}
	
				if (letterLoc != null && !letterLoc.isEmpty()) {
					// send pdf to school
					/* Fetch support email from customer table */
					String supportEmail = dao.getSupportEmailForCustomer(school.getCustomerCode());
					if (school.getEmail() != null && school.getEmail().trim().length() > 0) {
						String mailSubject = dao.getSubjectPrefix(school.getOrgNodeId()) + "" + school.getElementName() + "" + school.getOrgNodeId();
						if (sendMail(schoolId, false, false, prop, school.getEmail(), letterLoc, null, null, false, true, supportEmail, mailSubject)) {
							// logger.debug("	IC mail sent successfully ... for process id : " + processId);
							logger.info("Mail sent successfully to " + school.getEmail());
						} else {
							logger.warn("FAILED: sending mail. Updating status.");
						}
					} else {
						logger.warn("FAILED: sending mail .. no school mail id is defined.");
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error processing : Java exception : " + e.getMessage());
			e.printStackTrace();
			return "";
		}
		logger.info("Processing for IC Letter Completed.");
		return letterLoc;
	}

	private static void processLoginPdf(Properties prop, CommonDAO dao, String schoolId) {
		logger.info("Processing for Login Pdf...");
		long processId = 0;
		boolean schoolUserPresent = false;
		String encDocLocation = "";
		try {
			OrgTO school = dao.getSchoolDetails(schoolId, true); // Users required for all orgs under it
			if(school != null) {
				if (school != null && school.getUsers() != null && school.getUsers().size() > 0) {
					schoolUserPresent = true;
					DDMMYY = school.getDateStrWtYear();
					logger.info(school.getUsers().size() + " new school user found.");
				} else {
					logger.warn("No new school user found. ");
				}
				// fetching all repo users for school
				if (schoolUserPresent) {
					// generate auto password
					List<OrgTO> schools = new ArrayList<OrgTO>();
					schools.add(school);
					schools = PasswordGenerator.populateWithPassword(schools);
					orgMap = dao.getOrgMap(school.getUsers());
					// create teacher user list from school users
					List<OrgTO> teachers = new ArrayList<OrgTO>();
					for (UserTO user : school.getUsers()) {
						OrgTO orgTo = new OrgTO();
						orgTo.setPassword(user.getPassword());
						orgTo.setUserName(user.getUserName());
						orgTo.setElementName(user.getOrgNodeName());
						orgTo.setOrgNodeId(user.getOrgNodeId());
						// TODO : Check OrgNodeLevel in user
						orgTo.setOrgNodeLevel(user.getOrgNodeLevel());
						orgTo.setRoleList(user.getRoles());
						setHierarchy(dao, orgTo, user);
						teachers.add(orgTo);
					}
					boolean migration = false;
					boolean state = false;
					encDocLocation = createPdf(dao, school, teachers, prop, ENCRYPTION_NEEDED, schoolUserPresent, false, migration, state);
					logger.debug("Created PDF with name : " + encDocLocation);
					if (PdfGenerator.isIssueFound() && !migration) {
						logger.warn("Some issues identified during generation of pdf.");
					} else {
						// update org_user table set newuser = N
						logger.debug("Updating USERS table with password and reseting new user flag ");
						dao.updateNewUserFlag(school.getUsers());
						if (encDocLocation != null && encDocLocation.trim().length() > 0) {
							logger.info("Updated Process Status to success");
							/* Fetch support email from customer table */
							String supportEmail = dao.getSupportEmailForCustomer(school.getCustomerCode());
							String mailSubject = dao.getSubjectPrefix(school.getOrgNodeId()) + "" + school.getElementName() + "" + school.getOrgNodeId();
							// send mail to school
							if (school.getEmail() != null && school.getEmail().trim().length() > 0) {
								if (sendMail(schoolId, false, migration, prop, school.getEmail(), encDocLocation, null, null, schoolUserPresent, false, supportEmail, mailSubject)) {
									logger.debug("	mail sent successfully ... for process id : " + processId);
									logger.info("Mail sent successfully to " + school.getEmail());
									// update staging status
									logger.info("Updated Process Status to complete");
									logger.info("Updated Mail Status to success");
								} else {
									logger.warn("FAILED: sending mail. Updating status.");
								}
							} else {
								logger.warn("Sending mail to Support group only .. no school mail id is defined.");
								if (sendMail(schoolId, false, migration, prop, supportEmail, encDocLocation, null, null, schoolUserPresent, false, null, mailSubject)) {
									logger.info("Mail sent successfully to " + supportEmail);
								} else {
									logger.warn("FAILED: sending mail. Updating status.");
								}
							}
							// Sending password email for PDF opening
							logger.info("Sending password email for PDF opening");
							logger.debug("Sending password email for PDF opening");
							sendPasswordToMailId(dao, schoolId, prop, null, false, false, supportEmail);
						} else {
							logger.info("Error generating PDF");
						}
					}
				} else {
					logger.warn("No new school user found. Exiting.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Processing for Login Pdf Completed.");
	}

	private static void printHelp(String message) {
		if (message != null) {
			logger.error(message);
		}
		logger.info("**************************************************");
		logger.info("args[0] - Param 1 is required.");
		logger.info("              L = Login Pdf");
		logger.info("              I = IC Letter combined PDF");
		logger.info("              A = All/Both Login Pdf and IC Letter");
		logger.info("              S = IC Letter Separte PDF- No zip");
		logger.info("              X = IC Letter Separte PDF- No zip From BIO_STUDENT_EXTRACT");
		logger.info("args[1] - Param 2 is required.");
		logger.info("              Provide space separated Ids");
		logger.info("**************************************************");
	}

	/**
	 * Two command line args are required.
	 * 
	 * @param args
	 * @return
	 */
	private static boolean validateCommandLineArgs(String[] args) {
		if ((args == null) || (args.length < 2)) {
			printHelp("Both Param 1 and Param 2 are required.");
			return false;
		} else {
			if (args[0].equalsIgnoreCase(Constants.ARGS_OPTIONS.L.toString()) || args[0].equalsIgnoreCase(Constants.ARGS_OPTIONS.I.toString())
					|| args[0].equalsIgnoreCase(Constants.ARGS_OPTIONS.A.toString()) || args[0].equalsIgnoreCase(Constants.ARGS_OPTIONS.S.toString())
					|| args[0].equalsIgnoreCase(Constants.ARGS_OPTIONS.X.toString())) {
				return true;
			} else {
				printHelp("Param 1 is not valid. Please provide L/I/A/S");
				return false;
			}
		}
	}

	/**
	 * Sets the organization hierarchy
	 * 
	 * @param orgTo
	 * @param userTo
	 */
	private static void setHierarchy(CommonDAO dao, OrgTO orgTo, UserTO userTo) {
		String orgHierarchy[] = null;
		int depth = 0;
		try {
			if (userTo.getOrgNodeCodePath() == null) {
				userTo.setOrgNodeCodePath(dao.getOrgNodeCodePath(userTo.getOrgNodeId()));
			}
			if (null != userTo.getOrgNodeCodePath()) {
				orgHierarchy = userTo.getOrgNodeCodePath().split("~");
				depth = orgHierarchy.length;
				switch (depth) {
				case 4: {
					Integer code = Integer.parseInt(orgHierarchy[3]);
					String name = orgMap.get(code);
					orgTo.setTestingSiteCode(code);
					orgTo.setTestingSiteName(name);
					userTo.setTestingSiteCode(code);
					userTo.setTestingSiteName(name);
				}
				case 3: {
					Integer code = Integer.parseInt(orgHierarchy[2]);
					String name = orgMap.get(code);
					orgTo.setCountyCode(code);
					orgTo.setCountyName(name);
					userTo.setCountyCode(code);
					userTo.setCountyName(name);
				}
				case 2: {
					Integer code = Integer.parseInt(orgHierarchy[1]);
					String name = orgMap.get(code);
					orgTo.setStateCode(code);
					orgTo.setStateName(name);
					userTo.setStateCode(code);
					userTo.setStateName(name);
				}
				}
			} else {
				orgTo.setStateCode(Integer.parseInt(orgTo.getOrgNodeId()));
				orgTo.setStateName(orgTo.getElementName());
				userTo.setStateCode(Integer.parseInt(orgTo.getOrgNodeId()));
				userTo.setStateName(orgTo.getElementName());
			}
		} catch (Exception e) {
			logger.error("Error processing : Java exception : " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * This calls create PDF method to create pdf with specified template
	 * 
	 * @param school
	 * @param teachers
	 * @param prop
	 * @param encrypt
	 * @param schoolUserPresent
	 * @param isInitialLoad
	 * @param migration
	 * @param state
	 * @return
	 */
	private static String createPdf(CommonDAO dao, OrgTO school, List<OrgTO> teachers, Properties prop, boolean encrypt, boolean schoolUserPresent, boolean isInitialLoad, boolean migration,
			boolean state) {
		logger.debug("generating pdf... ");
		if (prop.getProperty("pdfGenPath") == null) {
			logger.info("PDF generation path (pdfGenPath) is not defined");
		}
		Map<String, String> orgLabelMap = dao.getOrgLabelMap();
		String docLocation = PdfGenerator.generatePdf(prop, school, teachers, schoolUserPresent, isInitialLoad, migration, state, orgLabelMap);

		if (encrypt) {
			String encLocation = encryptPdf(prop, docLocation, school.getOrgNodeId(), school.getCustomerCode(), school.getElementName(), state, school.getOrgNodeLevel());
			FileUtil.removeFile(docLocation);
			return encLocation;
		} else {
			return docLocation;
		}
	}

	/**
	 * This calls mail sending menthod to send send PDF to school users
	 * 
	 * @param level3OrgId
	 * @param isInitialLoad
	 * @param migration
	 * @param prop
	 * @param toMailAddr
	 * @param attachment
	 * @param attachmentTwo
	 * @param processLog
	 * @param schoolUserPresent
	 * @param letterMail
	 * @return
	 */
	private static boolean sendMail(String level3OrgId, boolean isInitialLoad, boolean migration, Properties prop, String toMailAddr, String attachment, String attachmentTwo, StringBuffer processLog,
			boolean schoolUserPresent, boolean letterMail, String supportEmail, String mailSubject) {
		logger.debug("sending mail... ");
		boolean mailSent = false;
		// String mailSubject = "";
		String mailBody = "";
		try {
			mailSubject = prop.getProperty("mailSubject");
			logger.info("Email Subject: " + mailSubject);
			mailBody = prop.getProperty("messageBody") + prop.getProperty("messageFooter");
			EmailSender.sendMail(prop, toMailAddr, attachment, attachmentTwo, mailSubject, mailBody, supportEmail);
			mailSent = true;
		} catch (Exception e) {
			logger.warn("Mail sending failed " + e.getMessage());
		}
		return mailSent;
	}

	/**
	 * Main method responsible for fetching data, storing password and sending mail
	 * 
	 * @param level3OrgId
	 * @param prop
	 * @param acLetterLocation
	 * @param migration
	 * @param state
	 * @param isEducationCenter
	 * @return
	 * @throws Exception
	 */
	private static boolean sendPasswordToMailId(CommonDAO dao, String level3OrgId, Properties prop, String toMail, boolean state, boolean isEducationCenter, String supportEmail) {
		try {
			OrgTO school = dao.getSchoolDetails(level3OrgId, false); // Users not required
			if (school != null) {
				/* Fetch support email from customer table */
				// String supportEmail = dao.getSupportEmailForCustomer(school.getCustomerCode());
				if (school.getEmail() != null && school.getEmail().trim().length() > 0) {
					if (sendPasswordMail(level3OrgId, prop, school.getEmail(), school.getOrgNodeLevel(), supportEmail)) {
						logger.info("Mail sent successfully to " + school.getEmail());
					} else {
						logger.warn("FAILED: sending mail. Updating status.");
					}
				} else {
					/* Changed for the new requirement where mail will be send to support group if no school email is present */
					logger.warn("Sending mail to Support group only .. no school mail id is defined.");
					// prop.getProperty("supportEmail");
					if (sendPasswordMail(level3OrgId, prop, supportEmail, school.getOrgNodeLevel(), null)) {
						logger.info("Mail sent successfully to " + supportEmail);
					} else {
						logger.warn("FAILED: sending mail. Updating status.");
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error processing : Java exception : " + e.getMessage());
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * @author Arunava Datta
	 * @param level3OrgId
	 * @param prop
	 * @param toMailAddr
	 * @param OrgLevel
	 * @return mail sent (true)successfully/(false)failed
	 */
	private static boolean sendPasswordMail(String level3OrgId, Properties prop, String toMailAddr, String OrgLevel, String supportEmail) {
		logger.debug("sending password mail... ");
		boolean mailSent = false;
		String mailSubject = "";
		String mailBody = "";
		String messagePasswordBody = "";
		String password = "";
		try {
			mailSubject = prop.getProperty("mailPasswordSubject");
			logger.info("Email Subject: " + mailSubject);
			messagePasswordBody = prop.getProperty("messagePasswordBody") + prop.getProperty("messageFooter");
			String tascPropertyPasswordString = "pdfPasswordLevel";

			if (null != level3OrgId || null != OrgLevel) {
				password = prop.getProperty(tascPropertyPasswordString + OrgLevel);
			}
			mailBody = CustomStringUtil.replaceCharacterInString('~', password, messagePasswordBody);
			EmailSender.sendMail(prop, toMailAddr, mailSubject, mailBody, supportEmail);
			mailSent = true;
		} catch (Exception e) {
			logger.error("Mail sending failed " + e.getMessage());
		}
		return mailSent;
	}

	/**
	 * This method encrypts pdf with strength: 128bits
	 * 
	 * @param prop
	 * @param docLocation
	 * @param orgid
	 * @param customerCode
	 * @param elementName
	 * @param state
	 * @param orgNodeLevel
	 * @return
	 */
	private static String encryptPdf(Properties prop, String docLocation, String orgid, String customerCode, String elementName, boolean state, final String orgNodeLevel) {
		logger.debug("Encrypting pdf... ");
		String docName = null;
		StringBuffer docBuff = new StringBuffer();
		try {
			String pdfPrefix = (state) ? prop.getProperty("pdfFileNamePrefixLoginState") : prop.getProperty("pdfFileNamePrefixLogin");
			docBuff.append(prop.getProperty("pdfGenPath")).append(File.separator).append(pdfPrefix);
			docBuff.append(elementName).append("_").append(customerCode).append("_").append(orgid).append(".pdf");
			docName = docBuff.toString();
			// Add the security object to the document
			PdfEncryptor.encrypt(new PdfReader(docLocation), new FileOutputStream(docName), prop.getProperty("pdfPasswordLevel" + orgNodeLevel).getBytes(), prop.getProperty("pdfOwnerPassword")
					.getBytes(), PdfWriter.AllowDegradedPrinting, PdfWriter.STRENGTH128BITS);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return docName;
	}

	/**
	 * Archive files
	 * 
	 * @param docLocation
	 * @param arcFilePath
	 */
	private static void archiveFiles(String docLocation, String arcFilePath) {
		ZipOutputStream zos = null;
		FileOutputStream fos = null;
		FileInputStream in = null;
		try {
			fos = new FileOutputStream(arcFilePath);
			zos = new ZipOutputStream(fos);

			File folder = new File(docLocation);
			File[] listOfFiles = folder.listFiles();
			for (File f : listOfFiles) {
				if (f.isFile())
					addToZipFile(f, zos);
			}

			for (File f : listOfFiles) {
				if (f.isFile()) {
					logger.debug(CustomStringUtil.appendString("Deleting file : ", f.getAbsolutePath()));
					FileUtil.removeFile(f.getAbsolutePath());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(zos);
			IOUtils.closeQuietly(fos);
			IOUtils.closeQuietly(in);
		}
	}

	/**
	 * Add entries into zip
	 * 
	 * @param f
	 * @param zos
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void addToZipFile(File f, ZipOutputStream zos) throws FileNotFoundException, IOException {
		logger.debug("Writing '" + f.getName() + "' to zip file");
		FileInputStream fis = null;
		try {
			File file = new File(f.getAbsolutePath());
			fis = new FileInputStream(file);
			ZipEntry zipEntry = new ZipEntry(f.getName());
			zos.putNextEntry(zipEntry);

			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zos.write(bytes, 0, length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			zos.closeEntry();
			IOUtils.closeQuietly(fis);
		}
	}

	/**
	 * Main method responsible for fetching data, storing password and sending mail.
	 * 
	 * @param level3JasperOrgId
	 * @param prop
	 * @param acLetterLocation
	 * @param migration
	 * @param state
	 * @return
	 */
	private static String manupulateTenants(String level3JasperOrgId, Properties prop, String acLetterLocation, boolean migration, boolean state) {
		long processId = 0;
		boolean schoolUserPresent = false;
		boolean teacherUserPresent = false;
		String encDocLocation = "";
		try {
			logger.debug("Using the following schema ... ");
			logger.debug("        Load schema       : " + prop.getProperty("dbUserName"));
			logger.debug("        Staging schema    : " + prop.getProperty("dbStageUserName"));
			logger.debug("        Repository schema : " + prop.getProperty("dbRepoUserName"));

			if (migration)
				logger.debug("***** Migrating user for : " + level3JasperOrgId);

			logger.info("Processing school (jasperorgId) : " + level3JasperOrgId);

			if (dao == null)
				dao = new CommonDAOImpl(prop);
			/*if (stageDao == null) stageDao = new StageDAOImpl(prop);*/

			logger.info("Getting Schools ... ");
			//lStartTime = new Date().getTime();
			/** Log time difference */
			// start time
			OrgTO school = dao.getSchoolDetailsAcsi(level3JasperOrgId, state);
			//lEndTime = new Date().getTime();
			/** Log time difference */
			// end time
			//logElapsedTime("Get school information : ");
			/** Log time difference */
			// difference

			if (school != null && school.getUsers() != null && school.getUsers().size() > 0) {
				//schoolUserPresent = true; 
				/** we don't need to generate school users even if that present **/
				schoolUserPresent = false; 
				logger.info("New school user found. count .. " + school.getUsers().size());
			} else {
				logger.warn("No new school user found. Incremental PDF will be generated.");
			}
			if (school != null) {
				//lStartTime = new Date().getTime();
				/** Log time difference */
				// start time
				processId = 1; // stageDao.getProcessId(school.getStructureElement());
				//lEndTime = new Date().getTime();
				/** Log time difference */
				// end time
				//logElapsedTime("Get process id : ");
				/** Log time difference */
				// difference
			}
			if (processId > 0 || migration) {
				logger.debug("Processing PDF for processId : " + processId);
				logger.info("Processing PDF for processId : " + processId);

				//lStartTime = new Date().getTime();
				/** Log time difference */
				// start time
				if (true/* stageDao.updateMailStatus(processId, INPROGRESS_STATUS, UTILITY_STATUS) > 0 || migration */) {
					//lEndTime = new Date().getTime();
					/** Log time difference */
					// end time
					//logElapsedTime("update email status to 'IP' : ");
					/** Log time difference */
					// difference

					/** Commenting the following for PHASE II */
					// if(repoDao == null) repoDao = new RepoDAOImpl(prop);

					String adminid = dao.getCurrentAdminYearAcsi();
					boolean isInitialLoad = true;// (migration)? false : stageDao.isInitialLoad(school.getStructureElement(), adminid);

					// fetching all repo users for school
					if (schoolUserPresent) {
						logger.debug("	fetching repo school users ... ");
						logger.info("fetching repo school users ...");

						/** Commenting the following for PHASE II */
						// lStartTime = new Date().getTime(); /** Log time difference*/ // start time
						// school.setUsers(repoDao.getSchoolUsers(school.getUsers(), school.getJasperOrgId()));
						// lEndTime = new Date().getTime(); /** Log time difference*/ // end time
						// logElapsedTime("Fetching school users from Jasper repo : "); /** Log time difference*/ // difference

						/*
						 * if(school != null && school.getUsers() != null && school.getUsers().size() > 0 ) { if(school.getUsers().get(0) != null) {
						 * school.setTenantId(school.getUsers().get(0).getTenantId()); } }
						 */
						/** adding this line for PHASE II */
						// NA //school.setTenantId(school.getJasperOrgId());

						//lStartTime = new Date().getTime();
						/** Log time difference */
						// start time
						// generate auto password
						List<OrgTO> schools = new ArrayList<OrgTO>();
						schools.add(school);
						schools = PasswordGenerator.populateWithPassword(schools);
						//lEndTime = new Date().getTime();
						/** Log time difference */
						// end time
						//logElapsedTime("Generate password for each school user : ");
						/** Log time difference */
						// difference

						// updating password for each users
						/** Commenting the following for PHASE II */
						// logger.debug("	updating repo school users ... ");
						// logger.info("updating repo school users ... ");

						// lStartTime = new Date().getTime(); /** Log time difference*/ // start time
						// repoDao.updateSchoolUsers(school);
						// lEndTime = new Date().getTime(); /** Log time difference*/ // end time
						// logElapsedTime("Update school user in jasper repo - with new password : "); /** Log time difference*/ // difference

						/** adding for PHASE II */
						// call LDAP code to save user password
						logger.debug("Connecting to LDAP ...");
						//LdapManager ldap = LdapManager.getInstance(prop.getProperty("ldap.host"), Integer.parseInt(prop.getProperty("ldap.port")), prop.getProperty("app.ldap.username"),
						//		prop.getProperty("app.ldap.password"));
						//ldap.setUSERS_OU(prop.getProperty("ldap.users.ou"));
						logger.debug("Connection established with LDAP ...");
						for (UserTO user : school.getUsers()) {
							try {
								//ldap.deleteUser(user.getUserName());
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							logger.debug("---------------------------------------------------");
							logger.debug("Username: " + user.getUserName() + " Password: " + user.getPassword());
							logger.debug("---------------------------------------------------");
							try {
								//ldap.addUser(user.getUserName(), user.getUserName(), user.getUserName(), user.getPassword());
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
					logger.info("Getting Teachers ... ");

					//lStartTime = new Date().getTime();
					/** Log time difference */
					// start time
					List<OrgTO> teachers = dao.getAllTeachersAcsi(level3JasperOrgId);
					//lEndTime = new Date().getTime();
					/** Log time difference */
					// end time
					//logElapsedTime("Get all teacher users : ");
					/** Log time difference */
					// difference

					if (teachers != null && teachers.size() > 0) {
						teacherUserPresent = true;
					}

					if (!schoolUserPresent && !teacherUserPresent) {
						// no new school user found also no new teacher found
						logger.warn("No New School and Teacher user found");

						if (acLetterLocation != null && acLetterLocation.trim().length() > 0) {
							logger.info("Sending AC email for new students");
							if (school.getEmail() != null && school.getEmail().trim().length() > 0) {
								if (sendMailAcsi(level3JasperOrgId, isInitialLoad, migration, prop, school.getEmail(), acLetterLocation, null, processLog, schoolUserPresent, true)) {
									// updating new activation flag
									logger.info("AC letter created. Updating new_code flag to N");
									//TODO//dao.updateNewActivationFlag(level3JasperOrgId);

									// stageDao.updateProcessStatus(processId, COMPLETE_STATUS);
									logger.info("Updated Process Status to complete");

									// stageDao.updateMailStatus(processId, SUCCESS_STATUS, INPROGRESS_STATUS);
									logger.info("Updated Mail Status to success");
								} else {
									logger.info("Failed sending AC mail.");
									// stageDao.updateProcessStatus(processId, ERROR_STATUS);
									// stageDao.updateMailStatus(processId, ERROR_STATUS, INPROGRESS_STATUS);
								}
							} else {
								logger.warn("Failed sending mail .. no school mail id is defined.");
								// stageDao.updateProcessStatus(processId, ERROR_STATUS);
								// stageDao.updateMailStatus(processId, ERROR_STATUS, INPROGRESS_STATUS);
							}
						} else {
							logger.warn("No New Students found");
							// stageDao.updateProcessStatus(processId, COMPLETE_STATUS);
							logger.info("Updated Process Status to complete");

							// stageDao.updateMailStatus(processId, SUCCESS_STATUS, INPROGRESS_STATUS);
							logger.info("Updated Mail Status to success");
						}
						ARCHIVE_NEEDED = false;
						return encDocLocation;
					}

					// fetch all teacher user from repo
					/** Commenting the following for PHASE II */
					// logger.debug("	fetching repo teacher users ... ");
					// logger.info("fetching repo teacher users ... ");

					// lStartTime = new Date().getTime(); /** Log time difference*/ // start time
					// teachers = RepoDAOImpl.getTeacherUsers(teachers);
					// lEndTime = new Date().getTime(); /** Log time difference*/ // end time
					// logElapsedTime("Get all teacher users from jasper repository : "); /** Log time difference*/ // difference

					// generate auto password
					//lStartTime = new Date().getTime();
					/** Log time difference */
					// start time
					teachers = PasswordGenerator.populateTeachersWithPassword(teachers);
					//lEndTime = new Date().getTime();
					/** Log time difference */
					// end time
					//logElapsedTime("Generate password for all teachers : ");
					/** Log time difference */
					// difference

					// updating password for each users
					/** Commenting the following for PHASE II */
					// logger.debug("	updating repo teacher users ... ");
					// logger.info("updating repo teacher users ... ");
					// lStartTime = new Date().getTime(); /** Log time difference*/ // start time
					// repoDao.updateTeacherUsers(teachers);
					// lEndTime = new Date().getTime(); /** Log time difference*/ // end time
					// logElapsedTime("Update teacher user in jasper repo - with new password : "); /** Log time difference*/ // difference

					/** adding for PHASE II */
					// call LDAP code to save user password
					logger.debug("Connecting to LDAP ...");
					//LdapManager ldap = LdapManager.getInstance(prop.getProperty("ldap.host"), Integer.parseInt(prop.getProperty("ldap.port")), prop.getProperty("app.ldap.username"),
					//		prop.getProperty("app.ldap.password"));
					//ldap.setUSERS_OU(prop.getProperty("ldap.users.ou"));
					logger.debug("Connection established with LDAP ...");
					for (OrgTO user : teachers) {
						try {
							//ldap.deleteUser(user.getUserName());
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						logger.debug("---------------------------------------------------");
						logger.debug("Username: " + user.getUserName() + " Password: " + user.getPassword());
						logger.debug("---------------------------------------------------");
						try {
							//ldap.addUser(user.getUserName(), user.getUserName(), user.getUserName(), user.getPassword());
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					// ordering teachers by Grade
					Collections.sort(teachers);

					// create pdf
					logger.info("Creating PDF ... ");
					encDocLocation = createPdfAcsi(school, teachers, prop, true, schoolUserPresent, isInitialLoad, migration, state);
					logger.info("Created PDF with name : " + encDocLocation);
					if (PdfGenerator.isIssueFound() && !migration) {
						logger.info("Some issues identified during generation of pdf.");
						// stageDao.updateMailStatus(processId, ERROR_STATUS, INPROGRESS_STATUS);
						// stageDao.updateProcessStatus(processId, ERROR_STATUS);
					} else {
						// update org_user table set newuser = N

						//lStartTime = new Date().getTime();
						/** Log time difference */
						// start time
						List<UserTO> teacherUsers = new ArrayList<UserTO>();
						int i = 0;
						for (OrgTO user : teachers) {
							UserTO teacherUser = new UserTO();
							teacherUser.setUserName(user.getUserName());
							teacherUser.setSalt(user.getSalt());
							teacherUser.setEncPassword(user.getEncPassword());
							teacherUsers.add(teacherUser);
							logger.info("(" + ++i + " of " + teachers.size() + ") Updating ORG_USER.newuser = 'N' for " + user.getUsers().size() + " Students");
							dao.updateNewUserFlag(user.getUsers());
						}
						logger.info("Updating ORG_USER.newuser = 'N' for " + teacherUsers.size() + " Teachers");
						dao.updateNewUserFlag(teacherUsers);
						//lEndTime = new Date().getTime();
						/** Log time difference */
						// end time
						//logElapsedTime("Update newuser flag for school : ");
						/** Log time difference */
						// difference

						//lStartTime = new Date().getTime();
						/** Log time difference */
						// start time
						//TODO//dao.updateTeacherNewuserFlag(teachers);
						//lEndTime = new Date().getTime();
						/** Log time difference */
						// end time
						//logElapsedTime("Update newuser flag for teacher : ");
						/** Log time difference */
						// difference

						if (encDocLocation != null && encDocLocation.trim().length() > 0) {
							// update staging status
							// stageDao.updateProcessStatus(processId, INPROGRESS_STATUS);
							logger.info("Updated Process Status to success");

							// send mail to school
							if (school.getEmail() != null && school.getEmail().trim().length() > 0) {
								// String attachmentName = school.getElementName() + "_" + school.getStructureElement() + ".pdf";
								if ((acLetterLocation != null && acLetterLocation.trim().length() == 0)) {
									logger.warn("No new students loaded .. sending email as single attachment");
									acLetterLocation = null; // as we need to send only one attachment
								}
								if (sendMailAcsi(level3JasperOrgId, isInitialLoad, migration, prop, school.getEmail(), encDocLocation, acLetterLocation, processLog, schoolUserPresent, false)) {
									logger.debug("	mail sent successfully ... for process id : " + processId);
									// removeFile(encDocLocation);
									logger.info("Mail sent successfully to " + school.getEmail());

									if (acLetterLocation != null && acLetterLocation.trim().length() > 0) {
										// updating new activation flag
										logger.info("AC letter created. Updating new_code flag to N");
										//TODO//dao.updateNewActivationFlag(level3JasperOrgId);
									}

									// update staging status
									//lStartTime = new Date().getTime();
									/** Log time difference */
									// start time
									// stageDao.updateProcessStatus(processId, COMPLETE_STATUS);
									logger.info("Updated Process Status to complete");

									// stageDao.updateMailStatus(processId, SUCCESS_STATUS, INPROGRESS_STATUS);
									logger.info("Updated Mail Status to success");
									//lEndTime = new Date().getTime();
									/** Log time difference */
									// end time
									//logElapsedTime("Update process status : ");
									/** Log time difference */
									// difference

								} else {
									logger.info("Failed sending mail. Updating status.");
									// stageDao.updateProcessStatus(processId, ERROR_STATUS);
									// stageDao.updateMailStatus(processId, ERROR_STATUS, INPROGRESS_STATUS);
								}
							} else {
								logger.warn("Failed sending mail .. no school mail id is defined.");
								// stageDao.updateProcessStatus(processId, ERROR_STATUS);
								// stageDao.updateMailStatus(processId, ERROR_STATUS, INPROGRESS_STATUS);
							}
						} else {
							logger.error("Error generating PDF");
							// stageDao.updateProcessStatus(processId, ERROR_STATUS);
							// stageDao.updateMailStatus(processId, ERROR_STATUS, INPROGRESS_STATUS);
						}
					}
				} else {
					logger.debug("Failed to update email status : (jasperorgId) : " + level3JasperOrgId);
					logger.info("Failed to update email status : (jasperorgId) : " + level3JasperOrgId);
					// stageDao.updateProcessStatus(processId, ERROR_STATUS);
					// stageDao.updateMailStatus(processId, ERROR_STATUS, INPROGRESS_STATUS);
				}
			} else {
				logger.debug("ProcessId not found");
				logger.info("ProcessId not found " + level3JasperOrgId);
				if (school != null)
					logger.info("  ... for ... structure element " + school.getStructureElement());
				// stageDao.updateProcessStatus(processId, ERROR_STATUS);
				// stageDao.updateMailStatus(processId, ERROR_STATUS, INPROGRESS_STATUS);
			}
		} catch (Exception e) {
			logger.error("Error processing : Java exception : " + e.getMessage());
			try { /* stageDao.updateProcessStatus(processId, ERROR_STATUS); */
			} catch (Exception ex) {
			}
			try { /* stageDao.updateMailStatus(processId, ERROR_STATUS, INPROGRESS_STATUS); */
			} catch (Exception ex) {
			}
			e.printStackTrace();
			return "";
		} finally {
			if (processId > 0) {
				//lStartTime = new Date().getTime();
				/** Log time difference */
				// start time
				logger.info("Processing complete for (jasperorgId) " + level3JasperOrgId);
				try {
					// stageDao.updateProcessLog(processId, processLog.toString(), encDocLocation, acLetterLocation);
				} catch (Exception ex) {
				}
				//lEndTime = new Date().getTime();
				/** Log time difference */
				// end time
				//logElapsedTime("Update process Log : ");
				/** Log time difference */
				// difference
			}
			logger.debug("Process Log ----------------------------------------- ");
			//logger.debug((processLog != null) ? processLog.toString() : "error");
		}
		return encDocLocation;
	}
	
	private static String getDocName(OrgTO school, Properties prop) {
		StringBuffer docBuff = new StringBuffer();
		String pdfPrefix = school.getTestAdministration();//prop.getProperty("testAdministrator");
		docBuff.append(prop.getProperty("pdfGenPath"));
		docBuff.append(File.separator);
		docBuff.append(pdfPrefix).append("_");
		docBuff.append(school.getDistrictName()).append("_");
		docBuff.append(school.getDistrictCode()).append("_");
		docBuff.append(school.getElementName()).append("_");
		docBuff.append(school.getSchoolCode()).append("_");
		docBuff.append(getDateTime("ddMMyyyyHHmmss"));
		// docBuff.append(".pdf");
		return docBuff.toString();
	}
	
	/**
	 * This calls create PDF method to create pdf with specified template
	 * 
	 * @param school
	 * @param teachers
	 * @param prop
	 * @return
	 * @throws Exception 
	 */
	private static String createPdfAcsi(OrgTO school, List<OrgTO> teachers, Properties prop, boolean encrypt, boolean schoolUserPresent, boolean isInitialLoad, boolean migration, boolean state) throws Exception {
		logger.debug("generating pdf... ");
		if (prop.getProperty("pdfGenPath") == null) {
			logger.info("PDF generation path (pdfGenPath) is not defined");
		}
		
		for(OrgTO aU : teachers) {
			school.setTestAdministration(aU.getTestAdministration());
			if(aU.getTestAdministration() != school.getTestAdministration()) {
				logger.info("One School is asssociated witth multiple adminstration Teacher user with IS_NEW_USER Y!!!!");
				throw new Exception("One School is asssociated witth multiple adminstration Teacher user with IS_NEW_USER Y!!!!");
			}
		}
		String docName = getDocName(school, prop) + ".pdf";
		/*StringBuffer docBuff = new StringBuffer();
		String pdfPrefix = prop.getProperty("testAdministrator");
		docBuff.append(prop.getProperty("pdfGenPath")).append(File.separator).append(pdfPrefix);
		docBuff.append(prop.getProperty("districtText")).append(school.getDistrictCode());
		docBuff.append(prop.getProperty("schoolText")).append(school.getSchoolCode()).append("_");
		docBuff.append(getDateTime("ddMMyyyyHHmmss")).append(".pdf");
		
		docName = docBuff.toString();*/
		// lStartTime = new Date().getTime(); /** Log time difference*/ // start time
		String docLocation = PdfGenerator.generatePdfAcsi(prop, school, teachers, schoolUserPresent, isInitialLoad, migration, state, docName);
		// lEndTime = new Date().getTime(); /** Log time difference*/ // end time
		// logElapsedTime("Create PDF : "); /** Log time difference*/ // difference

		if (ENCRYPTION_NEEDED) {
			// lStartTime = new Date().getTime(); /** Log time difference*/ // start time
			String encLocation = encryptPdfAcsi(prop, docLocation, school.getSchoolCode(), school.getCustomerCode(), school.getDistrictCode(), state, school.getTestAdministration());
			removeFileAcsi(docLocation);
			// lEndTime = new Date().getTime(); /** Log time difference*/ // end time
			// logElapsedTime("Encrypt PDF : "); /** Log time difference*/ // difference
			
			/*if (ARCHIVE_NEEDED) {
				if(encLocation != null && encLocation.length() > 0) {
					String pdfPrefix = prop.getProperty("testAdministrator");
					String docName = CustomStringUtil.appendString(prop.getProperty("pdfGenPath"),
							File.separator, pdfPrefix, "_", school.getSchoolCode(), "_", getDateTime("ddMMyyyyHHmmss"), ".ZIP");
					
					File file = new File(encLocation);
					if(file.exists()) {
						String arcFilePath = file.getAbsolutePath();
						List<String> filePaths = new ArrayList<String>();
						filePaths.add(arcFilePath);
						FileUtil.createZipFile(docName, filePaths);
					}
				}
			}*/
			
			return encLocation;
		} else {
			return docLocation;
		}
	}
	
	/**
	 * This calls mail sending menthod to send send PDF to school users
	 * @param prop
	 * @param toMailAddr
	 * @param attachment
	 * @return 
	 */
	private static boolean sendMailAcsi(String level3JasperOrgId, boolean isInitialLoad, boolean migration, Properties prop, 
			String toMailAddr, String attachment, String attachmentTwo, StringBuffer processLog, 
			boolean schoolUserPresent, boolean letterMail) {
		logger.debug("sending mail... ");
		
		//lStartTime = new Date().getTime(); /** Log time difference*/ // start time 
		
		boolean mailSent = false;
		int trySending = 0;
		String mailSubject = "";
		String mailBody = "";
		try {
			trySending++;
			logger.debug("Checking .. if school is new ... --> ");
			// boolean newSchool = dao.newSchool(level3JasperOrgId);
			// logger.debug(newSchool);
			//if(newSchool) updateLog("NEW SCHOOL"); else updateLog("OLD SCHOOL");
			if(migration) {
				mailSubject = prop.getProperty("mailSubjectMigration");
				mailBody = prop.getProperty("messageBodyMigration")+prop.getProperty("messageFooter");
				logger.debug("Mail sending for Migrated users");
			} else if(letterMail) {
				mailSubject = prop.getProperty("mailSubjectAnnualAddlGroup");
				mailBody = prop.getProperty("messageBodyAnnualAddlGroup")+prop.getProperty("messageFooter");
				//updateLog("Mail sending for IC");
			} else if(schoolUserPresent) {
				mailSubject = prop.getProperty("mailSubjectAnnualNewSch");
				mailBody = prop.getProperty("messageBodyAnnualNewSch")+prop.getProperty("messageFooter");
				//updateLog("Sending mail for new school");
			} else { // mail for additional group
				if(isInitialLoad) { 
					// returning school is loading data first time
					mailSubject = prop.getProperty("mailSubjectAnnualRetSch");
					mailBody = prop.getProperty("messageBodyAnnualRetSch")+prop.getProperty("messageFooter");
					//updateLog("Sending mail for addl. users. Ret. School");
				} else {
					mailSubject = prop.getProperty("mailSubjectAnnualAddlGroup");
					mailBody = prop.getProperty("messageBodyAnnualAddlGroup")+prop.getProperty("messageFooter");
					//updateLog("Sending mail for addl. users.");
				}
			}
			EmailSender.sendMailAcsi(prop, toMailAddr, attachment, attachmentTwo, mailSubject, mailBody);
			mailSent = true;
		} catch (Exception e) {
			logger.debug("Mail sending failed ..."+e.getMessage());
			//updateLog("Mail sending failed", e.getMessage());
		}
		//lEndTime = new Date().getTime();   /** Log time difference*/ // end time 
		//logElapsedTime("Mail sending : "); /** Log time difference*/ // difference 
		
		return mailSent;
		
	}
	
	/**
	 * This method encrypt pdf with strength: 128bits 
	 * @param prop
	 * @param docLocation
	 * @return
	 */
	private static String encryptPdfAcsi(Properties prop, String docLocation, String schoolNum, 
			String customerCode, String districtNumber, boolean state, String testAdministration) {
		logger.debug("encrypting pdf... ");
		String docName = null;
		StringBuffer docBuff = new StringBuffer();
		try {
			String pdfPrefix = testAdministration;
			docBuff.append(prop.getProperty("pdfGenPath")).append(File.separator).append(pdfPrefix);
			docBuff.append(prop.getProperty("districtText")).append(districtNumber);
			docBuff.append(prop.getProperty("schoolText")).append(schoolNum).append("_");
			docBuff.append(getDateTime("ddMMyyyyHHmmss")).append(".pdf");
			
			docName = docBuff.toString();
			
			// Add the security object to the document
			PdfEncryptor.encrypt(new PdfReader(docLocation), 
					new FileOutputStream(docName), 
					prop.getProperty("pdfPassword").getBytes(), 
					prop.getProperty("pdfOwnerPassword").getBytes(),
					PdfWriter.ALLOW_COPY,
					PdfWriter.STRENGTH128BITS);
		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return docName;
	}
	
	public static String getDateTime(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateformatter = new SimpleDateFormat(dateFormat);
		return dateformatter.format(cal.getTime());
	}
	
	private static void removeFileAcsi(String file) {
		try {
			File pdf = new File(file);
			if(pdf.exists()) pdf.delete();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param prop
	 * @param schoolId
	 * @param adminid
	 * @param studentBioId
	 * @param isExtTable
	 * @return
	 */
	private static String getURLString(Properties prop, String schoolId, String adminid, String studentBioId, boolean isExtTable) {
		StringBuffer URLStringBuf = new StringBuffer();
		URLStringBuf.append(prop.getProperty("jasperURL"));
		URLStringBuf.append(prop.getProperty("jasperURLParams"));
		URLStringBuf.append("&type=pdf&token=0&filter=true&p_L3_Jasper_Org_Id=").append(schoolId);
		URLStringBuf.append("&p_AdminYear=").append(adminid).append("&assessmentId=105_InvLetter").append("&p_Student_Bio_Id=").append(studentBioId);
		if (isExtTable) {
			URLStringBuf.append("&p_ExtTable=Y");
		} else {
			URLStringBuf.append("&p_ExtTable=N");
		}
		return URLStringBuf.toString();
	}
	
	/**
	 * get school cover url
	 * @param prop
	 * @param schoolId
	 * @return
	 */
	private static String getSchoolCoverURLString(Properties prop, String schoolId) {
		StringBuffer URLStringBuf = new StringBuffer();
		URLStringBuf.append(prop.getProperty("jasperURL"));
		URLStringBuf.append(prop.getProperty("schoolCoverURLParams"));
		URLStringBuf.append("&type=pdf&token=0&filter=true&p_L3_Jasper_Org_Id=").append(schoolId);
		URLStringBuf.append("&assessmentId=105_InvLetter");
		System.out.println("School cover >>> "+URLStringBuf.toString());
		return URLStringBuf.toString();
	}
	
	/**
	 * get district cover url
	 * @param prop
	 * @param schoolId
	 * @return
	 */
	private static String getDistrictCoverURLString(Properties prop, String schoolId) {
		StringBuffer URLStringBuf = new StringBuffer();
		URLStringBuf.append(prop.getProperty("jasperURL"));
		URLStringBuf.append(prop.getProperty("districtCoverURLParams"));
		URLStringBuf.append("&type=pdf&token=0&filter=true&p_L3_Jasper_Org_Id=").append(schoolId);
		URLStringBuf.append("&assessmentId=105_InvLetter");
		return URLStringBuf.toString();
	}

	/**
	 * @param prop
	 * @param schoolId
	 * @param adminId
	 * @param studentBioId
	 * @param isExtTable
	 * @return
	 */
	private static String getIndividualIcURLString(Properties prop,
			String schoolId, String adminId, String studentBioId,
			boolean isExtTable) {
		StringBuffer buff = new StringBuffer();
		buff.append(prop.getProperty("jasperURL"));
		buff.append(prop.getProperty("jasperURLParams"));
		buff.append("&type=pdf&token=0&drillDown=true&assessmentId=105_InvLetter");
		buff.append("&p_Student_Bio_Id=");
		buff.append(studentBioId);
		buff.append("&p_L3_Jasper_Org_Id=-1&p_AdminYear=-1");
		return buff.toString();
	}

	/**
	 * Get PDF location
	 * @param prop
	 * @param elementName
	 * @param customerCode
	 * @return
	 */
	private static String getPdfPath(Properties prop, String districtCode, String schoolCode, String customerCode, boolean tempLoc) {
		StringBuffer docBuff = new StringBuffer();
		docBuff.append(prop.getProperty("pdfGenPathIC")).append(File.separator).append(customerCode).append(File.separator);
		docBuff.append(prop.getProperty("ICLetterFile"));
		docBuff.append(districtCode).append(prop.getProperty("schoolText")).append(schoolCode);
		//docBuff.append("_").append(getDateTime("ddMMyyyyHHmmss"));
		if(tempLoc) docBuff.append(System.currentTimeMillis());
		docBuff.append(".pdf");
		return docBuff.toString();
	}
	
	/**
	 * PDF loc for school and district
	 * @param prop
	 * @param orgCode
	 * @param orgType
	 * @return
	 */
	private static String getOrgPdfPath(Properties prop, String orgCode, String customerCode, String orgType) {
		StringBuffer docBuff = new StringBuffer();
		docBuff.append(prop.getProperty("pdfGenPathIC")).append(File.separator).append(customerCode).append(File.separator);
		docBuff.append("Cover_").append(orgType).append("_").append(orgCode);
		docBuff.append(".pdf");
		return docBuff.toString();
	}

	private static String getIndividualIcPdfPath(Properties prop,
			String districtCode, String schoolCode, String customerCode,
			String studentBioId) {
		StringBuffer docBuff = new StringBuffer();
		docBuff.append(prop.getProperty("pdfGenPathIC"));
		docBuff.append(File.separator);
		docBuff.append(prop.getProperty("ICLetterFile"));
		docBuff.append(districtCode);
		docBuff.append(prop.getProperty("schoolText"));
		docBuff.append(schoolCode);
		docBuff.append("_");
		docBuff.append(studentBioId);
		docBuff.append(".pdf");
		return docBuff.toString();
	}
}
