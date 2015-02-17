package com.prism.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.lowagie.text.pdf.PdfEncryptor;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.prism.cipher.PasswordGenerator;
import com.prism.dao.InorsDao;
import com.prism.itext.PdfGenerator;
import com.prism.mail.EmailSender;
import com.prism.to.OrgTO;
import com.prism.to.UserTO;
import com.prism.util.AWSStorageUtil;
import com.prism.util.Constants;
import com.prism.util.CustomStringUtil;
import com.prism.util.FileUtil;
import com.prism.util.PdfUtil;
import com.prism.util.PropertyFile;
import com.prism.util.ReportPDF;

public class InorsService implements PrismPdfService {
	private final Logger logger = Logger.getLogger(InorsService.class);
	private boolean ENCRYPTION_NEEDED = false;
	private boolean ARCHIVE_NEEDED = false;
	private int icLetterCount = 0;
	private String DDMMYY = "";
	private Map<Integer, String> orgMap = null;
	private StringBuffer processLog = null;
	private String CUSOMERID = null;

	private InorsDao dao = null;
	private PdfGenerator pdfGenerator;

	public InorsService() {
		pdfGenerator = new PdfGenerator(Constants.INORS_PROPERTIES_FILE);
	}

	public void mainMethod(String[] args) throws Exception {
		logger.info("InorsService Starts...");
		boolean validArgs = validateCommandLineArgsInors(args);
		String flag = args[0];
		if (validArgs) {			
			Properties prop = PropertyFile.loadProperties(Constants.INORS_PROPERTIES_FILE);
			Properties jdbcProp = PropertyFile.loadProperties(Constants.INORS_JDBC_PROPERTIES_FILE);
			if (prop == null) {
				logger.error("Not able to read property file.");
				System.exit(1);
			} else {
				String[] ids = CustomStringUtil.getAllButFirstArg(args);
				try {
					dao = new InorsDao(jdbcProp);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if ("true".equals(prop.getProperty("pdfEncryptionRequired"))) {
					ENCRYPTION_NEEDED = true;
				}
				if ("true".equals(prop.getProperty("archiveNeeded"))) {
					ARCHIVE_NEEDED = true;
				}
				String encDocLocation = "";
				String identifier = "";
				long count = 1;
				for (String id : ids) {
					CUSOMERID = dao.getCustomerId(id);
					if (flag.equalsIgnoreCase(Constants.ARGS_OPTIONS.L.toString())) {
						logger.info("Creating Login PDF");
						encDocLocation = manupulateTenantsInors(id, prop, null, false, false);
					} else if (flag.equalsIgnoreCase(Constants.ARGS_OPTIONS.I.toString())) {
						logger.info("Creating IC Letter PDF");
						logger.info("Checking if new student present for school # " + id);
						boolean newStudentPresent = dao.getNewStudents(id);
						if (newStudentPresent) {
							String letterLocation = processIcLetterPdfInors(prop, dao, id);
							icLetterCount = icLetterCount + 1;
							logger.info("IC Letter Location: " + letterLocation);
						} else {
							logger.info("No new student found for school # " + id);
						}
						ARCHIVE_NEEDED = false;
						logger.info("SCHOOL " + count++ + "/" + (ids.length) + " IS DONE ----------------------------------------");
					} else if (flag.equalsIgnoreCase(Constants.ARGS_OPTIONS.A.toString())) {
						logger.info("Creating Both Login Pdf and IC Letter Pdf...");
						String letterLocation = "";
						boolean newStudentPresent = dao.getNewStudents(id);
						if (newStudentPresent) {
							letterLocation = processIcLetterPdfInors(prop, dao, id);
						} else {
							logger.info("No new IC letter found for school # " + id);
						}
						encDocLocation = manupulateTenantsInors(id, prop, letterLocation, false, false);
						logger.info("IC Letter Location: " + letterLocation);
						logger.info("All/Both Login Pdf and IC Letter Completed.");

					} else if (flag.equalsIgnoreCase(Constants.ARGS_OPTIONS.S.toString())) { // S option for both S3 upload and group IC
						
						logger.info("Creating Group IC Letter PDF");
						logger.info("Checking if new student present for school # " + id);
						boolean newStudentPresent = dao.getNewStudents(id);
						if (newStudentPresent) {
							String letterLocation = processIcLetterPdfInors(prop, dao, id);
							icLetterCount = icLetterCount + 1;
							logger.info("IC Letter Location: " + letterLocation);
						} else {
							logger.info("No new student found for school # " + id);
						}
						ARCHIVE_NEEDED = false;
						logger.info("SCHOOL " + count++ + "/" + (ids.length) + " IS DONE for Group IC----------------------------------------");
						
						
						logger.info("Creating Separate IC Letter PDFs to upload in S3");
						processIndividualIcLetterPdfInors(false, prop, dao, id);
						identifier = "IC_";

						// S3 code
						if ("true".equals(prop.getProperty("moveFilesToS3"))) {
							String rootPath = dao.getRootPathForCurAdmin(CUSOMERID);
							rootPath = prop.getProperty("environment.postfix").toUpperCase() + rootPath;
							moveFilesToS3(rootPath, prop.getProperty("pdfGenPathInvIC"));
							logger.info("All Files Successfully Moved to S3");
						} else {
							logger.info("Files NOT moved to S3");
						}
						 /* Deleting files from mount location after uploading to S3 */
						if ("true".equals(prop.getProperty("cleanDirectory.pdfGenPathInvIC"))) {
							FileUtils.cleanDirectory(new File(prop.getProperty("pdfGenPathInvIC")));
							logger.info("All TEMP files deleted from: " + prop.getProperty("pdfGenPathInvIC"));
						} else {
							logger.info("TEMP files NOT deleted from: " + prop.getProperty("pdfGenPathInvIC"));
						}
						ARCHIVE_NEEDED = false;
					}
				}

				if (flag.equalsIgnoreCase(Constants.ARGS_OPTIONS.I.toString())
						|| flag.equalsIgnoreCase(Constants.ARGS_OPTIONS.S.toString())) {
					
					if (icLetterCount > 0) {
						archiveICLetterInors(prop);
					}
					// send mail
					String subject = "Merged IC Letter Generatation completed ";
					String body = CustomStringUtil.appendString(subject, "for all the schools. Individiual IC leter has been started.\n",
							"Different notification will go once Individiual IC leter is completed");
					try {
						EmailSender.sendMailInors(prop, prop.getProperty("supportEmail"), subject, body, null);
					} catch (Exception e) {
						logger.error("Failed to send email" + e.getMessage());
					}
					// individual student
					count = 1;
					for (String id : ids) {
						processIndividualIcLetterPdfInors(true, prop, dao, id);
						identifier = "IC_";
						logger.info("SCHOOL " + count++ + "/" + (ids.length) + " IS DONE FOR INDV -------------------------------------------");
					}
					// send mail
					subject = "Individiual IC Letter Generatation completed ";
					body = CustomStringUtil.appendString(subject, "for all the schools. Individiual IC leter has been completed.\n");
					try {
						EmailSender.sendMailInors(prop, prop.getProperty("supportEmail"), subject, body, null);
					} catch (Exception e) {
						logger.error("Failed to send email" + e.getMessage());
					}
				}

				if (ARCHIVE_NEEDED) {
					File arc = new File(CustomStringUtil.appendString(prop.getProperty("pdfGenPath"), File.separator, "archive", File.separator));
					if (!arc.exists())
						arc.mkdir();
					String arcFilePath = CustomStringUtil.appendString(arc.getAbsolutePath(), File.separator, prop.getProperty("tempPdfLocation"),
							prop.getProperty("schoolaArc"), identifier, CustomStringUtil.getDateTimeInors("ddMMyyyyHHmmss"), ".ZIP");

					FileUtil.archiveFiles(prop.getProperty("pdfGenPath"), arcFilePath);
				}
			}
			
		}
		
		
		
		logger.info("Program Ends!");
	}

	private void moveFilesToS3(String s3Path, String dir) {
		dir = dir.replace("//", "/");
		File locDirectory = new File(dir);
		if (locDirectory.isDirectory()) {
			File[] localFiles = locDirectory.listFiles();
			AWSStorageUtil aWSStorageUtil = AWSStorageUtil.getInstance();
			for (File file : localFiles) {
				s3Path = s3Path.replace("//", "/");
				logger.debug("Calling aWSStorageUtil.uploadObject(" + s3Path + ", " + dir + "/" + file.getName() + ")");
				if (file != null && file.isFile()) {
					try {
						aWSStorageUtil.uploadObject(s3Path, dir + "/" + file.getName());
						logger.info("File Successfully Uploaded to S3");
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				} else if (file.isDirectory()) {
					// TODO : Do we need recursive upload?
				}
			}
		}
	}

	private void archiveICLetterInors(Properties prop) {
		File arc = new File(CustomStringUtil.appendString(prop.getProperty("pdfGenPathIC"), File.separator, CUSOMERID, File.separator, "archive",
				File.separator));
		if (!arc.exists())
			arc.mkdir();
		String arcFilePath = CustomStringUtil.appendString(arc.getAbsolutePath(), File.separator, "CTB_", CustomStringUtil.getDateTimeInors("yyMMdd"), "_",
				PasswordGenerator.generateRandomAlpha(3), ".ZIP");
		FileUtil.archiveFiles(CustomStringUtil.appendString(prop.getProperty("pdfGenPathIC"), File.separator, CUSOMERID), arcFilePath);
	}

	private String processIcLetterPdfInors(Properties prop, InorsDao dao, String schoolId) {
		logger.info("Processing for IC Letter...");
		String letterLoc = "";
		try {
			// Users not required
			OrgTO school = dao.getSchoolDetails(schoolId, false);
			if (school != null) {
				String adminId = dao.getCurrentAdminYear();
				List<String> tempFileList = new ArrayList<String>();
				List<String> pdfPathList = new ArrayList<String>();
				List<String> pdfDistPathList = new ArrayList<String>();
				
				/* directory for Group IC*/
				File groupICFolder = new File(CustomStringUtil.appendString(prop.getProperty("pdfGenPathIC"), File.separator, school.getCustomerCode()));
				if (!groupICFolder.exists()) {
					groupICFolder.mkdir();
				}
				
				CUSOMERID = school.getCustomerCode();
				String schoolCoverUrl = getSchoolCoverURLStringInors(prop, schoolId);
				String schoolCoverPath = ReportPDF.savePdfFromPrismWeb(getOrgPdfPathInors(prop, school.getSchoolCode(), school.getCustomerCode(), "SCHOOL"),
						new URL(schoolCoverUrl));
				String districtCoverUrl = getDistrictCoverURLStringInors(prop, schoolId);
				String districtCoverPath = ReportPDF.savePdfFromPrismWeb(
						getOrgPdfPathInors(prop, school.getDistrictCode(), school.getCustomerCode(), "DISTRICT"), new URL(districtCoverUrl));
				pdfDistPathList.add(districtCoverPath);
				pdfDistPathList.add(prop.getProperty("WHATS_IN_THE_BOX"));
				pdfDistPathList.add(prop.getProperty("GENERIC_LETTER"));
				pdfDistPathList.add(prop.getProperty("SAMPLE_IC"));

				pdfPathList.add(schoolCoverPath);
				pdfPathList.add(prop.getProperty("WHATS_IN_THE_BOX"));
				pdfPathList.add(prop.getProperty("GENERIC_LETTER"));

				String docName = getPdfPathInors(prop, school.getDistrictCode(), school.getSchoolCode(), school.getCustomerCode(), false);// getDocName(school,
																																			// prop);
				// combined pdf
				String pdfPath = getPdfPathInors(prop, school.getDistrictCode(), school.getSchoolCode(), school.getCustomerCode(), true);
				String urlString = getURLStringInors(prop, schoolId, adminId, "-1", false);
				URL url = new URL(urlString);
				letterLoc = ReportPDF.savePdfFromPrismWeb(pdfPath, url);
				if ((letterLoc != null) && (!letterLoc.isEmpty())) {
					pdfPathList.add(letterLoc);
					tempFileList.add(letterLoc);
					tempFileList.add(schoolCoverPath);
					tempFileList.add(districtCoverPath);
				}

				if (!pdfDistPathList.isEmpty()) {
					String distPdf = CustomStringUtil.appendString(prop.getProperty("pdfGenPathIC"), File.separator, school.getCustomerCode(), File.separator,
							prop.getProperty("ICLetterFile"), school.getDistrictCode(), ".pdf");
					File file = new File(distPdf);
					if (!file.exists() && "PUBLIC".equals(school.getOrgMode())) {
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
	private void processIndividualIcLetterPdfInors(boolean isGroup, Properties prop, InorsDao dao, String schoolId) {
		logger.info("Processing for IC Letter...");
		String letterLoc = "";
		try {
			
			// Users not required
			OrgTO school = dao.getSchoolDetails(schoolId, false);
			if (school != null) {
				CUSOMERID = school.getCustomerCode();
				
				
				/* directory for individual IC*/
				File invICFolder = new File(CustomStringUtil.appendString(prop.getProperty("pdfGenPathInvIC"), File.separator, CUSOMERID));
				if (!invICFolder.exists()) {
					invICFolder.mkdir();
				}
				
				if (school != null) {
					String adminId = "-1";
					// Actually test element id list
					List<String> studentIdList = dao.getStudentIdList(schoolId);
					logger.info(studentIdList.size() + " students found for school id " + schoolId);
					Map<String, String> pdfPathList = new HashMap<String, String>();
					List<String> tempFileList = new ArrayList<String>();

					int count = 0;
					for (String studentBioId : studentIdList) {
						logger.info("Processing " + ++count + " of " + studentIdList.size() + " students");
						String pdfPath = getIndividualIcPdfPathInors(isGroup, prop, school.getDistrictCode(), school.getSchoolCode(), school.getCustomerCode(), studentBioId);
						String urlString = getIndividualIcURLStringInors(prop, schoolId, adminId, studentBioId, false);
						URL url = new URL(urlString);
						letterLoc = ReportPDF.savePdfFromPrismWeb(pdfPath, url);
						if ((letterLoc != null) && (!letterLoc.isEmpty())) {
							pdfPathList.put(studentBioId, letterLoc);
							tempFileList.add(letterLoc);
						}
					}

					if (!pdfPathList.isEmpty()) {
						dao.updateStudentsPDFloc(schoolId, pdfPathList);
						logger.debug("IC letter created");
					} else {
						logger.warn("No pdf found");
					}
				}
			} else {
				logger.warn("School NOT found: " + schoolId);
			}
		} catch (Exception e) {
			logger.error("Error processing : Java exception : " + e.getMessage());
			e.printStackTrace();
		}
		logger.info("Processing for IC Letter Completed.");
	}

	/**
	 * Individual IC letter in a school.
	 * 
	 * @param configProperties
	 * @param prismProperties
	 * @param dao
	 * @param schoolId
	 * @return
	 */
	@SuppressWarnings("unused")
	private String processIndividualIcLetterPdfFromExtractTableInors(Properties configProperties, Properties prismProperties, InorsDao dao, String schoolId) {
		logger.info("Processing for IC Letter From Extract Table...");
		String letterLoc = "";
		try {
			// Users not required
			OrgTO school = dao.getSchoolDetails(schoolId, false);
			if (school != null) {
				String adminId = dao.getCurrentAdminYear();
				List<String> studentIdList = dao.getStudentIdListFromExtractTable(schoolId);
				Map<String, String> pdfPathList = new HashMap<String, String>();

				String docName = configProperties.getProperty("pdfGenPath") + File.separator + prismProperties.getProperty("tempPdfLocation")
						+ prismProperties.getProperty("districtText") + prismProperties.getProperty("schoolText") + school.getDateStrWtYear() + "_IC.zip";
				// TODO : File is same for all students so skipping the loop
				for (String studentBioId : studentIdList) {
					// for all students in school separate pdf
					letterLoc = ReportPDF.saveLetterFromPrismWeb(configProperties, prismProperties, "-1", school.getElementName(), school.getCustomerCode(),
							adminId, studentBioId, true, false);
					if ((letterLoc != null) && (!letterLoc.isEmpty())) {
						pdfPathList.put(studentBioId, letterLoc);
					}
				}
				dao.updateStudentsPDFloc(schoolId, pdfPathList);

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
						if (sendMailInors(schoolId, false, false, prismProperties, school.getEmail(), letterLoc, null, null, false, true, supportEmail,
								mailSubject)) {
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

	private void processLoginPdfInors(Properties prop, InorsDao dao, String schoolId) {
		logger.info("Processing for Login Pdf...");
		long processId = 0;
		boolean schoolUserPresent = false;
		String encDocLocation = "";
		try {
			// Users required for all orgs under it
			OrgTO school = dao.getSchoolDetails(schoolId, true);
			if (school != null) {
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
						setHierarchyInors(dao, orgTo, user);
						teachers.add(orgTo);
					}
					boolean migration = false;
					boolean state = false;
					encDocLocation = createPdfInors(dao, school, teachers, prop, ENCRYPTION_NEEDED, schoolUserPresent, false, migration, state);
					logger.debug("Created PDF with name : " + encDocLocation);
					if (pdfGenerator.isIssueFound() && !migration) {
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
								if (sendMailInors(schoolId, false, migration, prop, school.getEmail(), encDocLocation, null, null, schoolUserPresent, false,
										supportEmail, mailSubject)) {
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
								if (sendMailInors(schoolId, false, migration, prop, supportEmail, encDocLocation, null, null, schoolUserPresent, false, null,
										mailSubject)) {
									logger.info("Mail sent successfully to " + supportEmail);
								} else {
									logger.warn("FAILED: sending mail. Updating status.");
								}
							}
							// Sending password email for PDF opening
							logger.info("Sending password email for PDF opening");
							logger.debug("Sending password email for PDF opening");
							sendPasswordToMailIdInors(dao, schoolId, prop, null, false, false, supportEmail);
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

	private void printHelpInors(String message) {
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
	private boolean validateCommandLineArgsInors(String[] args) {
		if ((args == null) || (args.length < 2)) {
			printHelpInors("Both Param 1 and Param 2 are required.");
			return false;
		} else {
			if (args[0].equalsIgnoreCase(Constants.ARGS_OPTIONS.L.toString()) || args[0].equalsIgnoreCase(Constants.ARGS_OPTIONS.I.toString())
					|| args[0].equalsIgnoreCase(Constants.ARGS_OPTIONS.A.toString()) || args[0].equalsIgnoreCase(Constants.ARGS_OPTIONS.S.toString())
					|| args[0].equalsIgnoreCase(Constants.ARGS_OPTIONS.X.toString())) {
				return true;
			} else {
				printHelpInors("Param 1 is not valid. Please provide L/I/A/S");
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
	private void setHierarchyInors(InorsDao dao, OrgTO orgTo, UserTO userTo) {
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
	private String createPdfInors(InorsDao dao, OrgTO school, List<OrgTO> teachers, Properties prop, boolean encrypt, boolean schoolUserPresent,
			boolean isInitialLoad, boolean migration, boolean state) {
		logger.debug("generating pdf... ");
		if (prop.getProperty("pdfGenPath") == null) {
			logger.info("PDF generation path (pdfGenPath) is not defined");
		}
		Map<String, String> orgLabelMap = dao.getOrgLabelMap();
		String docLocation = pdfGenerator.generatePdfInors(prop, school, teachers, schoolUserPresent, isInitialLoad, migration, state, orgLabelMap);

		if (encrypt) {
			String encLocation = PdfUtil.encryptPdf(prop, docLocation, school.getOrgNodeId(), school.getCustomerCode(), school.getElementName(), state,
					school.getOrgNodeLevel());
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
	private boolean sendMailInors(String level3OrgId, boolean isInitialLoad, boolean migration, Properties prop, String toMailAddr, String attachment,
			String attachmentTwo, StringBuffer processLog, boolean schoolUserPresent, boolean letterMail, String supportEmail, String mailSubject) {
		logger.debug("sending mail... ");
		boolean mailSent = false;
		String mailBody = "";
		try {
			mailSubject = prop.getProperty("mailSubject");
			logger.info("Email Subject: " + mailSubject);
			mailBody = prop.getProperty("messageBody") + prop.getProperty("messageFooter");
			mailSent = true;
		} catch (Exception e) {
			logger.warn("Mail sending failed " + e.getMessage());
		}
		return mailSent;
	}

	/**
	 * Main method responsible for fetching data, storing password and sending
	 * mail
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
	private boolean sendPasswordToMailIdInors(InorsDao dao, String level3OrgId, Properties prop, String toMail, boolean state, boolean isEducationCenter,
			String supportEmail) {
		try {
			// Users not required
			OrgTO school = dao.getSchoolDetails(level3OrgId, false);
			if (school != null) {
				/* Fetch support email from customer table */
				if (school.getEmail() != null && school.getEmail().trim().length() > 0) {
					if (sendPasswordMailInors(level3OrgId, prop, school.getEmail(), school.getOrgNodeLevel(), supportEmail)) {
						logger.info("Mail sent successfully to " + school.getEmail());
					} else {
						logger.warn("FAILED: sending mail. Updating status.");
					}
				} else {
					// mail will be send to support group if no school email is
					// present
					logger.warn("Sending mail to Support group only .. no school mail id is defined.");

					if (sendPasswordMailInors(level3OrgId, prop, supportEmail, school.getOrgNodeLevel(), null)) {
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
	 * @param level3OrgId
	 * @param prop
	 * @param toMailAddr
	 * @param OrgLevel
	 * @return mail sent (true)successfully/(false)failed
	 */
	private boolean sendPasswordMailInors(String level3OrgId, Properties prop, String toMailAddr, String OrgLevel, String supportEmail) {
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
			mailSent = true;
		} catch (Exception e) {
			logger.error("Mail sending failed " + e.getMessage());
		}
		return mailSent;
	}

	/**
	 * Main method responsible for fetching data, storing password and sending
	 * mail.
	 * 
	 * @param level3JasperOrgId
	 * @param prop
	 * @param acLetterLocation
	 * @param migration
	 * @param state
	 * @return
	 */
	private String manupulateTenantsInors(String level3JasperOrgId, Properties prop, String acLetterLocation, boolean migration, boolean state) {
		long processId = 0;
		boolean schoolUserPresent = false;
		boolean teacherUserPresent = false;
		String encDocLocation = "";
		try {
			logger.debug("Using the following schema ... ");
			logger.debug("        Load schema       : " + prop.getProperty("dbUserName"));
			logger.debug("        Staging schema    : " + prop.getProperty("dbStageUserName"));
			logger.debug("        Repository schema : " + prop.getProperty("dbRepoUserName"));

			if (migration) {
				logger.debug("***** Migrating user for : " + level3JasperOrgId);
			}
			logger.info("Processing school (jasperorgId) : " + level3JasperOrgId);

			if (dao == null) {
				dao = new InorsDao(prop);
			}
			logger.info("Getting Schools ... ");
			OrgTO school = dao.getSchoolDetailsAcsi(level3JasperOrgId, state);
			if (school != null && school.getUsers() != null && school.getUsers().size() > 0) {
				/** we don't need to generate school users even if that present **/
				schoolUserPresent = false;
				logger.info("New school user found. count = " + school.getUsers().size());
			} else {
				logger.warn("No new school user found for school = " + level3JasperOrgId);
			}
			if (school != null) {
				processId = 1; // stageDao.getProcessId(school.getStructureElement());
			}
			if (processId > 0 || migration) {
				if (true) {
					String adminid = dao.getCurrentAdminYearAcsi();
					boolean isInitialLoad = true;

					// fetching all repo users for school
					if (schoolUserPresent) {
						logger.info("fetching repo school users ...");
						List<OrgTO> schools = new ArrayList<OrgTO>();
						schools.add(school);
						schools = PasswordGenerator.populateWithPassword(schools);
					}
					logger.info("Getting Teachers ... ");
					List<OrgTO> teachers = dao.getAllTeachersAcsi(level3JasperOrgId);
					if (teachers != null && teachers.size() > 0) {
						teacherUserPresent = true;
					}
					if (!schoolUserPresent && !teacherUserPresent) {
						// no new school user found also no new teacher found
						logger.warn("No New School and Teacher user found");
						if (acLetterLocation != null && acLetterLocation.trim().length() > 0) {
							logger.info("Sending AC email for new students");
							if (school.getEmail() != null && school.getEmail().trim().length() > 0) {
								if (sendMailAcsiInors(level3JasperOrgId, isInitialLoad, migration, prop, school.getEmail(), acLetterLocation, null, processLog,
										schoolUserPresent, true)) {
									// updating new activation flag
									logger.info("AC letter created. Updating new_code flag to N");
									logger.info("Updated Process Status to complete");
									logger.info("Updated Mail Status to success");
								} else {
									logger.info("Failed sending AC mail.");
								}
							} else {
								logger.warn("Failed sending mail .. no school mail id is defined.");
							}
						} else {
							logger.warn("No New Students found");
							logger.info("Updated Process Status to complete");
							logger.info("Updated Mail Status to success");
						}
						ARCHIVE_NEEDED = false;
						return encDocLocation;
					}

					teachers = PasswordGenerator.populateTeachersWithPassword(teachers);
					logger.debug("Connecting to LDAP ...");
					logger.debug("Connection established with LDAP ...");

					// ordering teachers by Grade
					Collections.sort(teachers);
					// create pdf
					logger.info("Creating PDF ... ");
					encDocLocation = createPdfAcsiInors(school, teachers, prop, true, schoolUserPresent, isInitialLoad, migration, state);
					logger.info("Created PDF with name : " + encDocLocation);
					if (pdfGenerator.isIssueFound() && !migration) {
						logger.info("Some issues identified during generation of pdf.");
					} else {
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
						if (encDocLocation != null && encDocLocation.trim().length() > 0) {
							logger.info("Updated Process Status to success");

							// send mail to school
							if (school.getEmail() != null && school.getEmail().trim().length() > 0) {
								if ((acLetterLocation != null && acLetterLocation.trim().length() == 0)) {
									logger.warn("No new students loaded .. sending email as single attachment");
									// as we need to send only one attachment
									acLetterLocation = null;
								}
								if (sendMailAcsiInors(level3JasperOrgId, isInitialLoad, migration, prop, school.getEmail(), encDocLocation, acLetterLocation,
										processLog, schoolUserPresent, false)) {
									logger.info("Mail sent successfully to " + school.getEmail());
									if (acLetterLocation != null && acLetterLocation.trim().length() > 0) {
										// updating new activation flag
										logger.info("AC letter created. Updating new_code flag to N");
									}
									logger.info("Updated Process Status to complete");
									logger.info("Updated Mail Status to success");
								} else {
									logger.info("Failed sending mail. Updating status.");
								}
							} else {
								logger.warn("Failed sending mail .. no school mail id is defined.");
							}
						} else {
							logger.error("Error generating PDF");
						}
					}
				} else {
					logger.debug("Failed to update email status : (jasperorgId) : " + level3JasperOrgId);
					logger.info("Failed to update email status : (jasperorgId) : " + level3JasperOrgId);
				}
			} else {
				logger.info("ProcessId not found " + level3JasperOrgId);
				if (school != null) {
					logger.info("  ... for ... structure element " + school.getStructureElement());
				}
			}
		} catch (Exception e) {
			logger.error("Error processing : Java exception : " + e.getMessage());
			e.printStackTrace();
			return "";
		}
		return encDocLocation;
	}

	private String getDocNameInors(OrgTO school, Properties prop) {
		StringBuffer docBuff = new StringBuffer();
		String pdfPrefix = school.getTestAdministration().trim();

		if (pdfPrefix.startsWith("IREAD"))
			pdfPrefix = "IREAD-3";
		if (pdfPrefix.startsWith("ISTEP"))
			pdfPrefix = "ISTEP+";
		if (pdfPrefix.startsWith("IMAST"))
			pdfPrefix = "IMAST";
		pdfPrefix = pdfPrefix + prop.getProperty("TestSpringSummer");

		docBuff.append(prop.getProperty("pdfGenPath"));
		docBuff.append(File.separator);
		docBuff.append(pdfPrefix);
		docBuff.append(prop.getProperty("districtText"));
		docBuff.append(school.getDistrictCode().trim());
		docBuff.append(prop.getProperty("schoolText"));
		docBuff.append(school.getSchoolCode().trim()).append("_");

		docBuff.append(CustomStringUtil.getDateTimeInors("ddMMyyyyHHmmss"));
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
	private String createPdfAcsiInors(OrgTO school, List<OrgTO> teachers, Properties prop, boolean encrypt, boolean schoolUserPresent, boolean isInitialLoad,
			boolean migration, boolean state) throws Exception {
		logger.debug("generating pdf... ");
		if (prop.getProperty("pdfGenPath") == null) {
			logger.info("PDF generation path (pdfGenPath) is not defined");
		}

		for (OrgTO aU : teachers) {
			school.setTestAdministration(aU.getTestAdministration());
			prop.setProperty("testAdministrator", aU.getTestAdministration());
			if (aU.getTestAdministration() != school.getTestAdministration()) {
				logger.info("One School is asssociated witth multiple adminstration Teacher user with IS_NEW_USER Y!!!!");
				throw new Exception("One School is asssociated witth multiple adminstration Teacher user with IS_NEW_USER Y!!!!");
			}
		}
		String docName = getDocNameInors(school, prop) + ".pdf";
		String docLocation = pdfGenerator.generatePdfAcsiInors(prop, school, teachers, schoolUserPresent, isInitialLoad, migration, state, docName);
		if (ENCRYPTION_NEEDED) {
			String encLocation = encryptPdfAcsiInors(prop, docLocation, school.getSchoolCode(), school.getCustomerCode(), school.getDistrictCode(), state,
					school.getTestAdministration());
			FileUtil.removeFile(docLocation);
			return encLocation;
		} else {
			return docLocation;
		}
	}

	/**
	 * This calls mail sending menthod to send send PDF to school users
	 * 
	 * @param prop
	 * @param toMailAddr
	 * @param attachment
	 * @return
	 */
	private boolean sendMailAcsiInors(String level3JasperOrgId, boolean isInitialLoad, boolean migration, Properties prop, String toMailAddr,
			String attachment, String attachmentTwo, StringBuffer processLog, boolean schoolUserPresent, boolean letterMail) {
		logger.debug("sending mail... ");
		boolean mailSent = false;
		int trySending = 0;
		String mailSubject = "";
		String mailBody = "";
		try {
			trySending++;
			logger.debug("Checking .. if school is new ... --> ");
			if (migration) {
				mailSubject = prop.getProperty("mailSubjectMigration");
				mailBody = prop.getProperty("messageBodyMigration") + prop.getProperty("messageFooter");
				logger.debug("Mail sending for Migrated users");
			} else if (letterMail) {
				mailSubject = prop.getProperty("mailSubjectAnnualAddlGroup");
				mailBody = prop.getProperty("messageBodyAnnualAddlGroup") + prop.getProperty("messageFooter");
			} else if (schoolUserPresent) {
				mailSubject = prop.getProperty("mailSubjectAnnualNewSch");
				mailBody = prop.getProperty("messageBodyAnnualNewSch") + prop.getProperty("messageFooter");
			} else { // mail for additional group
				if (isInitialLoad) {
					// returning school is loading data first time
					mailSubject = prop.getProperty("mailSubjectAnnualRetSch");
					mailBody = prop.getProperty("messageBodyAnnualRetSch") + prop.getProperty("messageFooter");
				} else {
					mailSubject = prop.getProperty("mailSubjectAnnualAddlGroup");
					mailBody = prop.getProperty("messageBodyAnnualAddlGroup") + prop.getProperty("messageFooter");
				}
			}
			mailSent = true;
		} catch (Exception e) {
			logger.debug("Mail sending failed ..." + e.getMessage());
		}
		return mailSent;

	}

	/**
	 * This method encrypt pdf with strength: 128bits
	 * 
	 * @param prop
	 * @param docLocation
	 * @return
	 */
	private String encryptPdfAcsiInors(Properties prop, String docLocation, String schoolNum, String customerCode, String districtNumber, boolean state,
			String testAdministration) {
		logger.debug("encrypting pdf... ");
		String docName = null;
		StringBuffer docBuff = new StringBuffer();
		try {
			String pdfPrefix = testAdministration;
			docBuff.append(prop.getProperty("pdfGenPath")).append(File.separator).append(pdfPrefix);
			docBuff.append(prop.getProperty("districtText")).append(districtNumber);
			docBuff.append(prop.getProperty("schoolText")).append(schoolNum).append("_");
			docBuff.append(CustomStringUtil.getDateTimeInors("ddMMyyyyHHmmss")).append(".pdf");
			docName = docBuff.toString();
			// Add the security object to the document
			PdfEncryptor.encrypt(new PdfReader(docLocation), new FileOutputStream(docName), prop.getProperty("pdfPassword").getBytes(),
					prop.getProperty("pdfOwnerPassword").getBytes(), PdfWriter.ALLOW_COPY, PdfWriter.STRENGTH128BITS);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return docName;
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
	private String getURLStringInors(Properties prop, String schoolId, String adminid, String studentBioId, boolean isExtTable) {
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
	 * 
	 * @param prop
	 * @param schoolId
	 * @return
	 */
	private String getSchoolCoverURLStringInors(Properties prop, String schoolId) {
		StringBuffer URLStringBuf = new StringBuffer();
		URLStringBuf.append(prop.getProperty("jasperURL"));
		URLStringBuf.append(prop.getProperty("schoolCoverURLParams"));
		URLStringBuf.append("&type=pdf&token=0&filter=true&p_L3_Jasper_Org_Id=").append(schoolId);
		URLStringBuf.append("&assessmentId=105_InvLetter");
		logger.debug("School cover >>> " + URLStringBuf.toString());
		return URLStringBuf.toString();
	}

	/**
	 * get district cover url
	 * 
	 * @param prop
	 * @param schoolId
	 * @return
	 */
	private String getDistrictCoverURLStringInors(Properties prop, String schoolId) {
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
	private String getIndividualIcURLStringInors(Properties prop, String schoolId, String adminId, String studentBioId, boolean isExtTable) {
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
	 * 
	 * @param prop
	 * @param elementName
	 * @param customerCode
	 * @return
	 */
	private String getPdfPathInors(Properties prop, String districtCode, String schoolCode, String customerCode, boolean tempLoc) {
		StringBuffer docBuff = new StringBuffer();
		docBuff.append(prop.getProperty("pdfGenPathIC")).append(File.separator).append(customerCode).append(File.separator);
		docBuff.append(prop.getProperty("ICLetterFile"));
		docBuff.append(districtCode).append(prop.getProperty("schoolText")).append(schoolCode);
		if (tempLoc) {
			docBuff.append(System.currentTimeMillis());
		}
		docBuff.append(".pdf");
		return docBuff.toString();
	}

	/**
	 * PDF loc for school and district
	 * 
	 * @param prop
	 * @param orgCode
	 * @param orgType
	 * @return
	 */
	private String getOrgPdfPathInors(Properties prop, String orgCode, String customerCode, String orgType) {
		StringBuffer docBuff = new StringBuffer();
		docBuff.append(prop.getProperty("pdfGenPathIC")).append(File.separator).append(customerCode).append(File.separator);
		docBuff.append("Cover_").append(orgType).append("_").append(orgCode);
		docBuff.append(".pdf");
		return docBuff.toString();
	}

	private String getIndividualIcPdfPathInors(boolean isGroup, Properties prop, String districtCode, String schoolCode, String customerCode, String studentBioId) {
		StringBuffer docBuff = new StringBuffer();
		if(isGroup){
			docBuff.append(prop.getProperty("pdfGenPathIC"));
		} else {
			docBuff.append(prop.getProperty("pdfGenPathInvIC"));
		}
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
