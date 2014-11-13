package com.prism.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.prism.cipher.PasswordGenerator;
import com.prism.dao.TascDao;
import com.prism.itext.PdfGenerator;
import com.prism.mail.EmailSender;
import com.prism.to.OrgTO;
import com.prism.to.UserTO;
import com.prism.util.Constants;
import com.prism.util.CustomStringUtil;
import com.prism.util.FileUtil;
import com.prism.util.PdfUtil;
import com.prism.util.PropertyFile;

public class TascService implements PrismPdfService {

	private final Logger logger = Logger.getLogger(TascService.class);
	private TascDao dao = null;
	private Calendar cal = null;
	private StringBuffer processLog = null;
	private long lStartTime; // start time
	private long lEndTime; // end time
	private boolean encryptionNeeded = false;
	private boolean archiveNeeded = false;
	private String DDMMYY = "";
	private Map<Integer, String> orgMap = null;

	private PdfGenerator pdfGenerator;

	public TascService() {
		pdfGenerator = new PdfGenerator(Constants.TASC_PROPERTIES_FILE);
	}

	/**
	 * Main method that creates the PDF.
	 * 
	 */
	// args[0] - required: O/E
	// ....................O = Org Node Id
	// ....................E = Education Center Id
	// args[1] - required: space separated Ids
	public void mainMethod(String[] args) {
		// args = new String[2];
		// args[0] = "o";
		// args[1] = "500167102";
		logger.info("Starting..");
		boolean isEducationCenter;
		int length = (args.length > 0) ? args.length - 1 : 0;
		String Ids[] = new String[length];
		Properties prop = PropertyFile.loadProperties("tasc.properties");
		if ((args == null) || (args.length == 0)) {
			logger.error("Please provide O/E (O = Org Node Id, E = Education Center Id) ");
		} else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("E")) {
				logger.error("Please provide Education Center Id");
			} else {
				logger.error("Please provide Org Node Id");
			}
		} else {
			if (args[0].equalsIgnoreCase("E")) {
				isEducationCenter = true;
				logger.debug("Processing for Education Center...");
			} else {
				isEducationCenter = false;
				logger.debug("Processing for Organization...");
			}
			// Creates a new array with args[1] to args[last element]
			System.arraycopy(args, 1, Ids, 0, length);
			if (prop == null) {
				logger.error("Error getting property file.");
				System.exit(1);
			} else {
				if ("true".equals(prop.getProperty("pdfEncryptionRequired"))) {
					encryptionNeeded = true;
				}
				if ("true".equals(prop.getProperty("archiveNeeded"))) {
					archiveNeeded = true;
				}
			}
			for (String orgId : Ids) {
				processLog = new StringBuffer();
				// generate only login PDF
				manupulateTenants(orgId, prop, null, false, false, isEducationCenter);
			}
			if (archiveNeeded) {
				File arc = new File(CustomStringUtil.appendString(prop.getProperty("pdfGenPath"), File.separator, "archive", File.separator));
				if (!arc.exists()) {
					arc.mkdir();
				}
				String arcFilePath = CustomStringUtil.appendString(arc.getAbsolutePath(), File.separator, prop.getProperty("tempPdfLocation"),
						prop.getProperty("schoolaArc"), DDMMYY, ".ZIP");
				archiveFiles(prop.getProperty("pdfGenPath"), arcFilePath);
			}
		}
		logger.debug("Completed!! ");
	}

	/**
	 * Archive files
	 * 
	 * @param docLocation
	 * @param arcFilePath
	 */
	private void archiveFiles(String docLocation, String arcFilePath) {
		ZipOutputStream zos = null;
		FileOutputStream fos = null;
		FileInputStream in = null;
		try {
			fos = new FileOutputStream(arcFilePath);
			zos = new ZipOutputStream(fos);
			File folder = new File(docLocation);
			File[] listOfFiles = folder.listFiles();
			for (File f : listOfFiles) {
				if (f.isFile()) {
					FileUtil.addToZipFile(f, zos);
				}
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
	 * Sets the organization hierarchy.
	 * 
	 * @param orgTo
	 * @param userTo
	 */
	private void setHierarchy(OrgTO orgTo, UserTO userTo) {
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
			updateLog("Error processing : Java exception : " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Main method responsible for fetching data, storing password and sending
	 * mail.
	 * 
	 * @param level3OrgId
	 * @param prop
	 * @param mailSubject
	 * @param toMail
	 * @param state
	 * @param isEducationCenter
	 * @param supportEmail
	 * @return
	 */
	private boolean sendPasswordToMailId(String level3OrgId, Properties prop, String mailSubject, String toMail, boolean state, boolean isEducationCenter,
			String supportEmail) {
		OrgTO school = null;
		try {
			if (dao == null) {
				dao = new TascDao(prop);
			}
			if (isEducationCenter == false) {
				school = dao.getSchoolDetails(level3OrgId, state, false);
			} else {
				school = dao.getEducationCenterDetails(level3OrgId);
			}
			if (school != null) {
				/* Fetch support email from customer table */
				// String supportEmail =
				// dao.getSupportEmailForCustomer(school.getCustomerCode());
				if (school.getEmail() != null && school.getEmail().trim().length() > 0) {
					if (sendPasswordMail(level3OrgId, prop, mailSubject, school.getEmail(), school.getOrgNodeLevel(), supportEmail)) {
						updateLog("Mail sent successfully to ", school.getEmail());
					} else {
						updateLog("Failed sending mail. Updating status.");
					}
				} else {
					/*
					 * Changed for the new requirement where mail will be send
					 * to support group if no school email is present
					 */
					logger.debug("Sending mail to Support group only .. no school mail id is defined.");
					// prop.getProperty("supportEmail");
					if (sendPasswordMail(level3OrgId, prop, mailSubject, supportEmail, school.getOrgNodeLevel(), null)) {
						updateLog("Mail sent successfully to ", supportEmail);
					} else {
						updateLog("Failed sending mail. Updating status.");
					}
				}
			}
		} catch (Exception e) {
			updateLog("Error processing : Java exception : " + e.getMessage());
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * @param level3OrgId
	 * @param prop
	 * @param acLetterLocation
	 * @param migration
	 * @param state
	 * @param isEducationCenter
	 * @return
	 */
	private boolean manupulateTenants(String level3OrgId, Properties prop, String acLetterLocation, boolean migration, boolean state, boolean isEducationCenter) {
		long processId = 0;
		boolean schoolUserPresent = false;
		String encDocLocation = "";
		try {
			logger.debug("Using the following schema ... ");
			logger.debug("        Load schema       : " + prop.getProperty("dbUserName"));
			updateLog("Processing school (jasperorgId) : ", level3OrgId);
			if (dao == null) {
				dao = new TascDao(prop);
			}
			logger.debug("getting schools ... ");
			updateLog("getting schools ... ");
			lStartTime = new Date().getTime();
			OrgTO school = null;
			if (!isEducationCenter) {
				school = dao.getSchoolDetails(level3OrgId, state, false);
			} else {
				school = dao.getEducationCenterDetails(level3OrgId);
			}
			lEndTime = new Date().getTime();
			logElapsedTime("Get school information : ");

			if (school == null) {
				logger.error("ORG_NODEID NOT FOUND: " + level3OrgId);
				return false;
			}
			/* Fetch support email from customer table */
			OrgTO orgTO = dao.getSupportEmailForCustomer(school.getCustomerCode());
			String supportEmail = orgTO.getEmail();
			if (orgTO.isSendLoginPdf()) {
				if (school != null && school.getUsers() != null && school.getUsers().size() > 0) {
					schoolUserPresent = true;
					DDMMYY = school.getDateStrWtYear();
					updateLog("New school user found. count .. ", "" + school.getUsers().size());
				} else {
					updateLog("No new school user found. ");
				}

				// fetching all repo users for school
				if (schoolUserPresent) {
					lStartTime = new Date().getTime();
					// generate auto password
					List<OrgTO> schools = new ArrayList<OrgTO>();
					schools.add(school);
					schools = PasswordGenerator.populateWithPassword(schools);
					lEndTime = new Date().getTime();
					logElapsedTime("Generate password for each school user : ");
					lStartTime = new Date().getTime();
					if ((isEducationCenter == false)/* && (orgMap == null) */)
						orgMap = dao.getOrgMap(school.getUsers());
					lEndTime = new Date().getTime();
					logElapsedTime("getOrgMap : ");

					// create teacher user list from school users
					lStartTime = new Date().getTime();
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
						if (isEducationCenter == false) {
							setHierarchy(orgTo, user);
						}
						teachers.add(orgTo);
					}
					lEndTime = new Date().getTime();
					logElapsedTime("setHierarchy : ");
					encDocLocation = createPdf(school, teachers, prop, encryptionNeeded, schoolUserPresent, false, migration, state);
					logger.debug("Created PDF with name : " + encDocLocation);
					if (pdfGenerator.isIssueFound() && !migration) {
						logger.warn("Some issues identified during generation of pdf.");
					} else {
						// update org_user table set newuser = N
						logger.debug("Updating USERS table with password and reseting new user flag ");
						lStartTime = new Date().getTime();
						// for(UserTO user : school.getUsers()) {
						dao.updateNewuserFlag(school.getUsers());
						// }
						lEndTime = new Date().getTime();
						logElapsedTime("Update newuser flag for school : ");
						if (encDocLocation != null && encDocLocation.trim().length() > 0) {
							updateLog("Updated Process Status to success");

							// send mail to school
							String customerCode = dao.getCustomerCode(school.getCustomerCode());
							String mailSubject = CustomStringUtil.appendString(customerCode, " ", school.getOrgNodeCode(), " ", school.getElementName(),
									" TASC Online Reporting System - User Accounts");
							logger.info(mailSubject);
							if (school.getEmail() != null && school.getEmail().trim().length() > 0) {
								if (sendMail(level3OrgId, false, migration, prop, mailSubject, school.getEmail(), encDocLocation, acLetterLocation, processLog,
										schoolUserPresent, false, supportEmail)) {
									logger.debug("	mail sent successfully ... for process id : " + processId);
									updateLog("Mail sent successfully to ", school.getEmail());

									// update staging status
									lStartTime = new Date().getTime();
									updateLog("Updated Process Status to complete");
									updateLog("Updated Mail Status to success");
									lEndTime = new Date().getTime();
									logElapsedTime("Update process status : ");
								} else {
									updateLog("Failed sending mail. Updating status.");
								}
							} else {
								logger.debug("Sending mail to Support group only .. no school mail id is defined.");
								if (sendMail(level3OrgId, false, migration, prop, mailSubject, supportEmail, encDocLocation, acLetterLocation, processLog,
										schoolUserPresent, false, null)) {
									updateLog("Mail sent successfully to ", supportEmail);
								} else {
									updateLog("Failed sending mail. Updating status.");
								}
							}

							// Sending password email for PDF opening
							updateLog("Sending password email for PDF opening");
							logger.debug("Sending password email for PDF opening");
							sendPasswordToMailId(level3OrgId, prop, mailSubject, null, false, isEducationCenter, supportEmail);
						} else {
							updateLog("Error generating PDF");
						}
					}
				} else {
					logger.debug("No new school user found. Exiting.");
					updateLog("No new school user found. Exiting.");
				}
			} else {
				// configured not to send login pdf
				logger.debug("CUSTOMER_INFO.SEND_LOGIN_PDF is set to N, not creating password/pdf");
				updateLog("CUSTOMER_INFO.SEND_LOGIN_PDF is set to N, not creating password/pdf");
			}
		} catch (Exception e) {
			updateLog("Error processing : Java exception : " + e.getMessage());
			e.printStackTrace();
			return false;
		} finally {
			if (processId > 0) {
				lStartTime = new Date().getTime();
				updateLog("Processing complete for (jasperorgId) ", level3OrgId);
				try {
				} catch (Exception ex) {
				}
				lEndTime = new Date().getTime();
				logElapsedTime("Update process Log : ");
			}
			logger.debug("Process Log ----------------------------------------- ");
			logger.debug((processLog != null) ? processLog.toString() : "error");
		}
		return true;
	}

	/**
	 * 
	 * @param message
	 * @param id
	 */
	private void updateLog(String message, String id) {
		processLog.append("\n");
		processLog.append(getTime()).append(message);
		if (id != null) {
			processLog.append(id);
		}
	}

	/**
	 * Overloaded updateLog() method where id is null by default.
	 * 
	 * @param message
	 */
	private void updateLog(String message) {
		updateLog(message, null);
	}

	/**
	 * Returns time string
	 * 
	 * @return
	 */
	private String getTime() {
		cal = Calendar.getInstance();
		SimpleDateFormat dateformatter = new SimpleDateFormat("hh:mm:ss a");
		StringBuffer time = new StringBuffer();
		time.append("[");
		time.append(dateformatter.format(cal.getTime()));
		time.append("] [UserAccountPdf] ");
		return time.toString();
	}

	/**
	 * Logs Elapsed Time
	 * 
	 * @param message
	 */
	private void logElapsedTime(String message) {
		long difference = lEndTime - lStartTime; // check different
		StringBuffer timeDiff = new StringBuffer();
		if (difference > 1000) {
			timeDiff.append(message).append(" Elapsed : ");
			timeDiff.append(difference / 1000).append(" second(s) ");
			timeDiff.append(difference % 1000).append(" milliseconds");
		} else {
			timeDiff.append(message).append(" Elapsed : ");
			timeDiff.append(difference).append(" milliseconds");
		}
		logger.debug(timeDiff.toString());
	}

	/**
	 * This calls create PDF method to create pdf with specified template.
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
	private String createPdf(OrgTO school, List<OrgTO> teachers, Properties prop, boolean encrypt, boolean schoolUserPresent, boolean isInitialLoad,
			boolean migration, boolean state) {
		logger.debug("generating pdf... ");
		if (prop.getProperty("pdfGenPath") == null) {
			updateLog("PDF generation path (pdfGenPath) is not defined");
		}
		lStartTime = new Date().getTime();
		Map<String, String> orgLabelMap = dao.getOrgLabelMap();
		String docLocation = pdfGenerator.generatePdfTasc(prop, school, teachers, schoolUserPresent, isInitialLoad, migration, state, orgLabelMap);
		lEndTime = new Date().getTime();
		logElapsedTime("Create PDF : ");
		if (encrypt) {
			lStartTime = new Date().getTime();
			String encLocation = PdfUtil.encryptPdf(prop, docLocation, school.getOrgNodeId(), school.getCustomerCode(), school.getElementName(), state,
					school.getOrgNodeLevel());
			FileUtil.removeFile(docLocation);
			lEndTime = new Date().getTime();
			logElapsedTime("Encrypt PDF : ");
			return encLocation;
		} else {
			return docLocation;
		}
	}

	/**
	 * This calls mail sending method to send send PDF to school users
	 * 
	 * @param level3OrgId
	 * @param isInitialLoad
	 * @param migration
	 * @param prop
	 * @param mailSubject
	 * @param toMailAddr
	 * @param attachment
	 * @param attachmentTwo
	 * @param processLog
	 * @param schoolUserPresent
	 * @param letterMail
	 * @param supportEmail
	 * @return
	 */
	private boolean sendMail(String level3OrgId, boolean isInitialLoad, boolean migration, Properties prop, String mailSubject, String toMailAddr,
			String attachment, String attachmentTwo, StringBuffer processLog, boolean schoolUserPresent, boolean letterMail, String supportEmail) {
		logger.debug("sending mail... ");
		lStartTime = new Date().getTime();
		boolean mailSent = false;
		// String mailSubject = "";
		String mailBody = "";
		try {
			// mailSubject = prop.getProperty("mailSubject");
			mailBody = prop.getProperty("messageBody") + prop.getProperty("messageFooter");
			EmailSender.sendMailTasc(prop, toMailAddr, attachment, attachmentTwo, mailSubject, mailBody, supportEmail);
			mailSent = true;
		} catch (Exception e) {
			logger.debug("Mail sending failed ..." + e.getMessage());
			updateLog("Mail sending failed", e.getMessage());
		}
		lEndTime = new Date().getTime();
		logElapsedTime("Mail sending : ");
		return mailSent;
	}

	/**
	 * @param level3OrgId
	 * @param prop
	 * @param mailSubject
	 * @param toMailAddr
	 * @param OrgLevel
	 * @param supportEmail
	 * @return mail sent (true)successfully/(false)failed
	 */
	private boolean sendPasswordMail(String level3OrgId, Properties prop, String mailSubject, String toMailAddr, String OrgLevel, String supportEmail) {
		logger.debug("sending password mail... ");
		lStartTime = new Date().getTime();
		boolean mailSent = false;
		// String mailSubject = "";
		String mailBody = "";
		String messagePasswordBody = "";
		String password = "";
		try {
			// mailSubject = prop.getProperty("mailPasswordSubject");
			messagePasswordBody = prop.getProperty("messagePasswordBody") + prop.getProperty("messageFooter");
			String tascPropertyPasswordString = "pdfPasswordLevel";

			if (null != level3OrgId || null != OrgLevel) {
				password = prop.getProperty(tascPropertyPasswordString + OrgLevel);
			}
			mailBody = CustomStringUtil.replaceCharacterInString('~', password, messagePasswordBody);
			EmailSender.sendMailTasc(prop, toMailAddr, mailSubject, mailBody, supportEmail);
			mailSent = true;
		} catch (Exception e) {
			logger.debug("Mail sending failed ..." + e.getMessage());
			updateLog("Mail sending failed", e.getMessage());
		}
		lEndTime = new Date().getTime();
		logElapsedTime("Mail sending : ");
		return mailSent;
	}

}
