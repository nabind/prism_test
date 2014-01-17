package com.prism.itext;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lowagie.text.pdf.PdfEncryptor;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.prism.cipher.PasswordGenerator;
import com.prism.constant.Constants;
import com.prism.dao.CommonDAO;
import com.prism.mail.EmailSender;
import com.prism.to.OrgTO;
import com.prism.to.UserTO;
import com.prism.util.CustomStringUtil;
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
	private static String SUCCESS_STATUS = "SU";
	private static String ERROR_STATUS = "ER";
	private static String COMPLETE_STATUS = "CP";
	private static String INPROGRESS_STATUS = "IP";
	private static String UTILITY_STATUS = "IP";

	public static void main(String[] args) {
		logger.info("Program Starts...");
		// args = new String[2];
		// args[0] = "i";
		// args[1] = "362010";
		boolean validArgs = validateCommandLineArgs(args);
		if (validArgs) {
			String flag = args[0];
			Properties prop = PropertyFile.loadProperties(Constants.PROPERTIES_FILE);
			if (prop == null) {
				logger.error("Not able to read property file.");
				System.exit(1);
			} else {
				int length = (args.length > 0) ? args.length - 1 : 0;
				String[] ids = new String[length];
				// Creates a new array with args[1] to args[last element]
				System.arraycopy(args, 1, ids, 0, length);
				ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
				CommonDAO dao = applicationContext.getBean("commonDAO", CommonDAO.class);
				if ("true".equals(prop.getProperty("pdfEncryptionRequired"))) {
					ENCRYPTION_NEEDED = true;
				}
				if ("true".equals(prop.getProperty("archiveNeeded"))) {
					ARCHIVE_NEEDED = true;
				}
				for (String id : ids) {
					if (flag.equalsIgnoreCase("L")) {
						processLoginPdf(prop, dao, id);
					} else if (flag.equalsIgnoreCase("I")) {
						String letterLocation = processIcLetterPdf(prop, dao, id);
						logger.info("IC Letter Location: " + letterLocation);
					} else if (flag.equalsIgnoreCase("A")) {
						logger.info("All/Both Login Pdf and IC Letter...");
						processLoginPdf(prop, dao, id);
						String letterLocation = processIcLetterPdf(prop, dao, id);
						logger.info("IC Letter Location: " + letterLocation);
						logger.info("All/Both Login Pdf and IC Letter Completed.");
					}
				}
			}
		}
		logger.info("The End!");
	}

	private static String processIcLetterPdf(Properties prop, CommonDAO dao, String schoolId) {
		logger.info("Processing for IC Letter...");
		long processId = 0;
		String letterLoc = "";
		try {
			OrgTO school = dao.getSchoolDetails(schoolId);
			if (school != null) {
				processId = dao.getProcessIdNoCondition(school.getStructureElement());
			}
			if (processId > 0 || true) {
				String adminid = dao.getCurrentAdminYear();
				if (dao.newStudentsLoaded(schoolId)) {
					logger.info("New students found");
					letterLoc = ReportPDF.saveLetterFromPrismWeb(prop, schoolId, school.getElementName(), school.getCustomerCode(), adminid);
					logger.debug("AC letter created @ " + letterLoc);
				} else {
					logger.info("No new students found");
				}
				if (letterLoc != null && letterLoc.trim().length() > 0) {
					// send pdf to school
					/* Fetch support email from customer table */
					String supportEmail = dao.getSupportEmailForCustomer(school.getCustomerCode());
					if (school.getEmail() != null && school.getEmail().trim().length() > 0) {
						if (sendMail(schoolId, false, false, prop, school.getEmail(), letterLoc, null, null, false, true, supportEmail)) {
							logger.debug("	IC mail sent successfully ... for process id : " + processId);
							// removeFile(encDocLocation);
							logger.info("Mail sent successfully to " + school.getEmail());
							// update staging status
							dao.updateProcessStatus(processId, COMPLETE_STATUS);
							logger.info("Updated Process Status to complete");

							dao.updateMailStatus(processId, SUCCESS_STATUS, INPROGRESS_STATUS);
							logger.info("Updated Mail Status to success");
						} else {
							logger.info("Failed sending mail. Updating status.");
							dao.updateProcessStatus(processId, ERROR_STATUS);
							dao.updateMailStatus(processId, ERROR_STATUS, INPROGRESS_STATUS);
						}
					} else {
						logger.debug("failed sending mail .. no school mail id is defined.");
						logger.info("Failed sending mail .. no school mail id is defined.");
						dao.updateProcessStatus(processId, ERROR_STATUS);
						dao.updateMailStatus(processId, ERROR_STATUS, INPROGRESS_STATUS);
					}
				}
			}
		} catch (Exception e) {
			logger.info("Error processing : Java exception : " + e.getMessage());
			try {
				dao.updateProcessStatus(processId, ERROR_STATUS);
			} catch (Exception ex) {
			}
			try {
				dao.updateMailStatus(processId, ERROR_STATUS, INPROGRESS_STATUS);
			} catch (Exception ex) {
			}
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
			OrgTO school = dao.getSchoolDetails(schoolId);
			if (school != null && school.getUsers() != null && school.getUsers().size() > 0) {
				schoolUserPresent = true;
				DDMMYY = school.getDateStrWtYear();
				logger.info("New school user found. count .. " + school.getUsers().size());
			} else {
				logger.info("No new school user found. ");
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
					dao.updateNewuserFlag(school.getUsers());
					if (encDocLocation != null && encDocLocation.trim().length() > 0) {
						logger.info("Updated Process Status to success");
						/* Fetch support email from customer table */
						String supportEmail = dao.getSupportEmailForCustomer(school.getCustomerCode());
						// send mail to school
						if (school.getEmail() != null && school.getEmail().trim().length() > 0) {
							if (sendMail(schoolId, false, migration, prop, school.getEmail(), encDocLocation, null, null, schoolUserPresent, false, supportEmail)) {
								logger.debug("	mail sent successfully ... for process id : " + processId);
								logger.info("Mail sent successfully to " + school.getEmail());
								// update staging status
								logger.info("Updated Process Status to complete");
								logger.info("Updated Mail Status to success");
							} else {
								logger.info("Failed sending mail. Updating status.");
							}
						} else {
							logger.debug("Sending mail to Support group only .. no school mail id is defined.");
							if (sendMail(schoolId, false, migration, prop, supportEmail, encDocLocation, null, null, schoolUserPresent, false, null)) {
								logger.info("Mail sent successfully to " + supportEmail);
							} else {
								logger.info("Failed sending mail. Updating status.");
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
				logger.debug("No new school user found. Exiting.");
				logger.info("No new school user found. Exiting.");
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
		logger.info("              I = IC Letter");
		logger.info("              A = All/Both Login Pdf and IC Letter");
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
			if (args[0].equalsIgnoreCase("L") || args[0].equalsIgnoreCase("I") || args[0].equalsIgnoreCase("A")) {
				return true;
			} else {
				printHelp("Param 1 is not valid. Please provide L/I/A");
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
			removeFile(docLocation);
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
			boolean schoolUserPresent, boolean letterMail, String supportEmail) {
		logger.debug("sending mail... ");
		boolean mailSent = false;
		String mailSubject = "";
		String mailBody = "";
		try {
			mailSubject = prop.getProperty("mailSubject");
			mailBody = prop.getProperty("messageBody") + prop.getProperty("messageFooter");
			EmailSender.sendMail(prop, toMailAddr, attachment, attachmentTwo, mailSubject, mailBody, supportEmail);
			mailSent = true;
		} catch (Exception e) {
			logger.debug("Mail sending failed ..." + e.getMessage());
			logger.info("Mail sending failed " + e.getMessage());
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
			OrgTO school = dao.getSchoolDetails(level3OrgId);
			if (school != null) {
				/* Fetch support email from customer table */
				// String supportEmail = dao.getSupportEmailForCustomer(school.getCustomerCode());
				if (school.getEmail() != null && school.getEmail().trim().length() > 0) {
					if (sendPasswordMail(level3OrgId, prop, school.getEmail(), school.getOrgNodeLevel(), supportEmail)) {
						logger.info("Mail sent successfully to " + school.getEmail());
					} else {
						logger.info("Failed sending mail. Updating status.");
					}
				} else {
					/* Changed for the new requirement where mail will be send to support group if no school email is present */
					logger.debug("Sending mail to Support group only .. no school mail id is defined.");
					// prop.getProperty("supportEmail");
					if (sendPasswordMail(level3OrgId, prop, supportEmail, school.getOrgNodeLevel(), null)) {
						logger.info("Mail sent successfully to " + supportEmail);
					} else {
						logger.info("Failed sending mail. Updating status.");
					}
				}
			}
		} catch (Exception e) {
			logger.info("Error processing : Java exception : " + e.getMessage());
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
	@SuppressWarnings("unused")
	private static boolean sendPasswordMail(String level3OrgId, Properties prop, String toMailAddr, String OrgLevel, String supportEmail) {
		logger.debug("sending password mail... ");
		boolean mailSent = false;
		String mailSubject = "";
		String mailBody = "";
		String messagePasswordBody = "";
		String password = "";
		try {
			mailSubject = prop.getProperty("mailPasswordSubject");
			messagePasswordBody = prop.getProperty("messagePasswordBody") + prop.getProperty("messageFooter");
			String tascPropertyPasswordString = "pdfPasswordLevel";

			if (null != level3OrgId || null != OrgLevel) {
				password = prop.getProperty(tascPropertyPasswordString + OrgLevel);
			}
			mailBody = CustomStringUtil.replaceCharacterInString('~', password, messagePasswordBody);
			EmailSender.sendMail(prop, toMailAddr, mailSubject, mailBody, supportEmail);
			mailSent = true;
		} catch (Exception e) {
			logger.debug("Mail sending failed ..." + e.getMessage());
			logger.info("Mail sending failed " + e.getMessage());
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
		logger.debug("encrypting pdf... ");
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
	 * Cleans up temp files.
	 * 
	 * @param file
	 */
	private static void removeFile(String file) {
		try {
			File pdf = new File(file);
			if (pdf.exists())
				pdf.delete();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
