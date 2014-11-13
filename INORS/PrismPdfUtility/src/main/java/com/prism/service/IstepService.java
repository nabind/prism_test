package com.prism.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.lowagie.text.pdf.PdfEncryptor;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.prism.cipher.PasswordGenerator;
import com.prism.dao.IstepDao;
import com.prism.itext.PdfGenerator;
import com.prism.mail.EmailSender;
import com.prism.to.OrgTO;
import com.prism.to.UserTO;
import com.prism.util.Constants;
import com.prism.util.CustomStringUtil;
import com.prism.util.FileUtil;
import com.prism.util.PropertyFile;

public class IstepService implements PrismPdfService {
	private final Logger logger = Logger.getLogger(IstepService.class);
	private IstepDao dao = null;
	private boolean encryptionNeeded = false;
	private boolean archiveNeeded = false;
	private boolean userExists = false;
	private boolean districtUserExists = false;
	private String DDMMYY = "";

	private PdfGenerator pdfGenerator;

	public IstepService() {
		pdfGenerator = new PdfGenerator(Constants.ISTEP_PROPERTIES_FILE);
	}

	/**
	 * The main method that runs the utility.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public void mainMethod(String[] args) throws Exception {
		long t1 = System.currentTimeMillis();
		args = new String[] { "603833", "603834", "605138" };
		logger.info("Starting File Generation. Please wait...");
		Properties configProperties = PropertyFile.loadProperties(Constants.ISTEP_JDBC_PROPERTIES_FILE);
		Properties istepProperties = PropertyFile.loadProperties(Constants.ISTEP_PROPERTIES_FILE);
		if (args == null || args.length == 0) {
			logger.error("Please provide school org id ");
		} else {
			if (configProperties == null || istepProperties == null) {
				logger.error("Error getting property file.");
				System.exit(1);
			} else {
				if ("true".equals(istepProperties.getProperty("pdfEncryptionRequired"))) {
					encryptionNeeded = true;
				}
				if ("true".equals(istepProperties.getProperty("archiveNeeded"))) {
					archiveNeeded = true;
				}
			}
			Set<String> districtSet = new HashSet<String>();
			for (String orgId : args) {
				logger.info("Processing School Id: " + orgId);
				if (dao == null) {
					dao = new IstepDao(configProperties);
				}
				logger.info("Fetching school details from database. Please wait... ");
				OrgTO school = dao.getSchoolDetails(orgId, false);
				if (manupulateTenants(school, configProperties, istepProperties, null, false, false)) {
					userExists = true;
				}
				if (school.getParentJasperOrgId() != null) {
					districtSet.add(school.getParentJasperOrgId());
				}
			}
			for (String districtId : districtSet) {
				logger.info("districtId = " + districtId);
				if (manupulateDistricts(districtId, configProperties, istepProperties, null, false, false)) {
					districtUserExists = true;
				}
			}

			if (archiveNeeded) {
				File arc = new File(CustomStringUtil.appendString(configProperties.getProperty("pdfGenPath"), File.separator, "archive", File.separator));
				if (!arc.exists()) {
					arc.mkdir();
				}
				if (userExists) {
					String arcFilePath = CustomStringUtil.appendString(arc.getAbsolutePath(), File.separator, istepProperties.getProperty("tempPdfLocation"),
							istepProperties.getProperty("schoolaArc"), DDMMYY, ".ZIP");
					FileUtil.archiveFiles(configProperties.getProperty("pdfGenPath"), arcFilePath);
				}
				if (districtUserExists) {
					String arcFilePath = CustomStringUtil.appendString(arc.getAbsolutePath(), File.separator, istepProperties.getProperty("tempPdfLocation"),
							istepProperties.getProperty("districtArc"), DDMMYY, ".ZIP");
					FileUtil.archiveFiles(configProperties.getProperty("pdfGenPathDist"), arcFilePath);
				}
			}
		}
		long t2 = System.currentTimeMillis();
		logger.info("Completed!! Time taken: " + String.valueOf(t2 - t1) + "ms");
	}

	/**
	 * Processes the districts.
	 * 
	 * @param level2OrgId
	 * @param configProperties
	 * @param istepProperties
	 * @param acLetterLocation
	 * @param migration
	 * @param state
	 * @return
	 */
	private boolean manupulateDistricts(String level2OrgId, Properties configProperties, Properties istepProperties, String acLetterLocation,
			boolean migration, boolean state) {
		long processId = 0;
		boolean districtUserPresent = false;
		String encDocLocation = "";
		try {
			logger.debug("DB Schema: " + configProperties.getProperty("dbUserName"));
			logger.info("Processing District Id: " + level2OrgId);
			if (dao == null) {
				dao = new IstepDao(configProperties);
			}
			logger.info("Fetching district details from database. Please wait... ");
			OrgTO district = dao.getDistrictDetails(level2OrgId, state);
			if (district != null && district.getUsers() != null && district.getUsers().size() > 0) {
				districtUserPresent = true;
				DDMMYY = district.getDateStrWtYear();
				logger.info("New district user found. Count = " + district.getUsers().size());
			} else {
				logger.info("No new district user found. ");
			}
			// fetching all repo users for district
			if (districtUserPresent) {
				district.setTenantId(district.getJasperOrgId());
				// generate auto password
				List<OrgTO> districts = new ArrayList<OrgTO>();
				districts.add(district);
				districts = PasswordGenerator.populateWithPassword(districts);
				logger.info("Generating password for each district user. Please wait...");
				// create teacher user list from district users
				List<OrgTO> teachers = new ArrayList<OrgTO>();
				for (UserTO user : district.getUsers()) {
					OrgTO orgTo = new OrgTO();
					orgTo.setTenantId(user.getTenantId());
					orgTo.setFullName(user.getFullName());
					orgTo.setPassword(user.getPassword());
					orgTo.setUserName(user.getUserName());
					orgTo.setUserType(user.getUserType());
					teachers.add(orgTo);
				}
				encDocLocation = createPdf(district, teachers, configProperties, istepProperties, encryptionNeeded, districtUserPresent, false, migration,
						state);
				logger.info("Pdf created: " + encDocLocation);
				if (pdfGenerator.isIssueFound() && !migration) {
					logger.info("Some issues identified during generation of pdf.");
				} else {
					// update org_user table set newuser = N
					logger.debug("Updating USERS table with password and reseting new user flag. Please wait...");
					dao.updateNewuserFlag(district.getUsers());
					if (encDocLocation != null && encDocLocation.trim().length() > 0) {
						// send mail to district
						if (district.getEmail() != null && district.getEmail().trim().length() > 0) {
							if (sendMail(level2OrgId, false, migration, istepProperties, district.getEmail(), encDocLocation, acLetterLocation,
									districtUserPresent, false)) {
								logger.info("Email sent successfully to " + district.getEmail() + " for process id: " + processId);
							} else {
								logger.error("Failed to send Email.");
							}
						} else {
							logger.error("Failed to send Email. No district Email id is defined.");
						}
					} else {
						logger.error("Error in generating Pdf.");
					}
				}
			} else {
				logger.error("No new district user found. Exiting.");
				return false;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return false;
		} finally {
			if (processId > 0) {
				logger.info("Processing complete for District Id: " + level2OrgId);
			}
		}
		return true;
	}

	/**
	 * Processes the schools.
	 * 
	 * @param school
	 * @param configProperties
	 * @param istepProperties
	 * @param acLetterLocation
	 * @param migration
	 * @param state
	 * @return
	 */
	private boolean manupulateTenants(OrgTO school, Properties configProperties, Properties istepProperties, String acLetterLocation, boolean migration,
			boolean state) {
		long processId = 0;
		boolean schoolUserPresent = false;
		String encDocLocation = "";
		try {
			logger.debug("DB Schema: " + configProperties.getProperty("dbUserName"));
			if (school != null && school.getUsers() != null && school.getUsers().size() > 0) {
				schoolUserPresent = true;
				DDMMYY = school.getDateStrWtYear();
				logger.info("New school user found. Count = " + school.getUsers().size());
			} else {
				logger.info("No new school user found. ");
			}
			// fetching all repo users for school
			if (schoolUserPresent) {
				school.setTenantId(school.getJasperOrgId());
				// generate auto password
				List<OrgTO> schools = new ArrayList<OrgTO>();
				schools.add(school);
				schools = PasswordGenerator.populateWithPassword(schools);
				logger.info("Generating password for each school user. Please wait...");
				// create teacher user list from school users
				List<OrgTO> teachers = new ArrayList<OrgTO>();
				for (UserTO user : school.getUsers()) {
					OrgTO orgTo = new OrgTO();
					orgTo.setTenantId(user.getTenantId());
					orgTo.setFullName(user.getFullName());
					orgTo.setPassword(user.getPassword());
					orgTo.setUserName(user.getUserName());
					orgTo.setUserType(user.getUserType());
					teachers.add(orgTo);
				}
				encDocLocation = createPdf(school, teachers, configProperties, istepProperties, encryptionNeeded, schoolUserPresent, false, migration, state);
				logger.info("Pdf created: " + encDocLocation);
				if (pdfGenerator.isIssueFound() && !migration) {
					logger.info("Some issues identified during generation of pdf.");
				} else {
					// update org_user table set newuser = N
					logger.debug("Updating USERS table with password and reseting new user flag. Please wait...");
					dao.updateNewuserFlag(school.getUsers());
					if (encDocLocation != null && encDocLocation.trim().length() > 0) {
						// send mail to school
						if (school.getEmail() != null && school.getEmail().trim().length() > 0) {
							if (sendMail(school.getJasperOrgId(), false, migration, istepProperties, school.getEmail(), encDocLocation, acLetterLocation,
									schoolUserPresent, false)) {
								logger.info("Email sent successfully to " + school.getEmail() + " for process id: " + processId);
							} else {
								logger.error("Failed to send Email.");
							}
						} else {
							logger.error("Failed to send Email. No school Email id is defined.");
						}
					} else {
						logger.error("Error in generating Pdf.");
					}
				}
			} else {
				logger.error("No new school user found. Exiting.");
				return false;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return false;
		} finally {
			if (processId > 0) {
				logger.info("Processing complete for School Id: " + school.getJasperOrgId());
			}
		}
		return true;
	}

	private String createPdf(OrgTO organization, List<OrgTO> teachers, Properties configProperties, Properties istepProperties, boolean encrypt,
			boolean userPresent, boolean isInitialLoad, boolean migration, boolean state) {
		String orgNodeLevel = organization.getOrgNodeLevel();
		logger.info("orgNodeLevel=" + orgNodeLevel);
		logger.info("Generating pdf. Please wait... ");
		if ("3".equals(orgNodeLevel)) {
			if (configProperties.getProperty("pdfGenPath") == null) {
				logger.info("PDF generation path (pdfGenPath) is not defined");
			}
			String docLocation = pdfGenerator.generatePdfIstep(configProperties, istepProperties, organization, teachers, userPresent, isInitialLoad,
					migration, state);
			if (encrypt) {
				String encLocation = encryptPdf(configProperties, istepProperties, docLocation, organization.getOrgNode(), organization.getCustomerCode(),
						organization.getElementName(), state);
				FileUtil.removeFile(docLocation);
				return encLocation;
			} else {
				return docLocation;
			}
		} else if ("2".equals(orgNodeLevel)) {
			if (configProperties.getProperty("pdfGenPathDist") == null) {
				logger.error("PDF generation path (pdfGenPathDist) is not defined");
			}
			String docLocation = pdfGenerator.generateDistrictPdfIstep(configProperties, istepProperties, organization, teachers, userPresent, isInitialLoad,
					migration, state);
			if (encrypt) {
				String encLocation = encryptPdf(configProperties, istepProperties, docLocation, organization.getOrgNode(), organization.getCustomerCode(),
						organization.getElementName(), state);
				FileUtil.removeFile(docLocation);
				return encLocation;
			} else {
				return docLocation;
			}
		} else {
			return null;
		}
	}

	/**
	 * This method encrypt pdf with strength: 128bits
	 * 
	 * @param prop
	 * @param docLocation
	 * @return
	 */
	private String encryptPdf(Properties configProperties, Properties istepProperties, String docLocation, String orgid, String customerCode,
			String elementName, boolean state) {
		logger.info("Encrypting pdf. Please wait... ");
		String docName = null;
		StringBuffer docBuff = new StringBuffer();
		try {
			String pdfPrefix = (state) ? istepProperties.getProperty("pdfFileNamePrefixLoginState") : istepProperties.getProperty("pdfFileNamePrefixLogin");
			docBuff.append(configProperties.getProperty("pdfGenPath")).append(File.separator).append(pdfPrefix);
			docBuff.append(elementName).append("_").append(customerCode).append("_").append(orgid).append(".pdf");
			docName = docBuff.toString();
			// Add the security object to the document
			PdfEncryptor.encrypt(new PdfReader(docLocation), new FileOutputStream(docName), istepProperties.getProperty("pdfPassword").getBytes(),
					istepProperties.getProperty("pdfOwnerPassword").getBytes(), PdfWriter.AllowDegradedPrinting, PdfWriter.STRENGTH128BITS);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return docName;
	}

	/**
	 * This calls mail sending menthod to send send PDF to school users
	 * 
	 * @param prop
	 * @param toMailAddr
	 * @param attachment
	 * @return
	 */
	private boolean sendMail(String level3OrgId, boolean isInitialLoad, boolean migration, Properties prop, String toMailAddr, String attachment,
			String attachmentTwo, boolean schoolUserPresent, boolean letterMail) {
		logger.info("Sending Email. Please wait... ");
		boolean mailSent = false;
		String mailSubject = "";
		String mailBody = "";
		try {
			mailSubject = prop.getProperty("mailSubject");
			mailBody = prop.getProperty("messageBody") + prop.getProperty("messageFooter");
			EmailSender.sendMailIstep(prop, toMailAddr, attachment, attachmentTwo, mailSubject, mailBody);
			mailSent = true;
			logger.info("Email sent to " + toMailAddr);
		} catch (Exception e) {
			logger.error("Mail sending failed: " + e.getMessage());
		}
		return mailSent;
	}
}