package com.ctb.prism.inors.business;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ctb.prism.admin.dao.IAdminDAO;
import com.ctb.prism.admin.transferobject.ObjectValueTO;
import com.ctb.prism.core.Service.IRepositoryService;
import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.constant.IEmailConstants;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.transferobject.BaseTO;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.core.util.EmailSender;
import com.ctb.prism.core.util.FileUtil;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.inors.dao.IInorsDAO;
import com.ctb.prism.inors.transferobject.BulkDownloadTO;
import com.ctb.prism.inors.util.ConcatPdf;
import com.ctb.prism.inors.util.PdfGenerator;
import com.ctb.prism.login.dao.ILoginDAO;
import com.ctb.prism.report.business.IReportBusiness;
import com.ctb.prism.report.transferobject.GroupDownloadTO;
import com.ctb.prism.report.transferobject.JobTrackingTO;
import com.lowagie.text.DocumentException;

/**
 * @author TCS
 * 
 */
@Component("inorBusiness")
public class InorsBusinessImpl implements IInorsBusiness {

	private static final IAppLogger logger = LogFactory.getLoggerInstance(InorsBusinessImpl.class.getName());

	@Autowired
	private IInorsDAO inorsDAO;

	@Autowired
	private IAdminDAO adminDAO;

	@Autowired
	private IReportBusiness reportBusiness;
	
	@Autowired
	private IRepositoryService repositoryService;

	@Autowired
	private IPropertyLookup propertyLookup;

	@Autowired
	private ILoginDAO loginDAO;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.business.IInorsBusiness#createJob(com.ctb.prism.inors.transferobject.BulkDownloadTO)
	 */
	public BulkDownloadTO createJob(BulkDownloadTO bulkDownloadTO) {
		String[] allNodes = (bulkDownloadTO.getSelectedNodes() != null) ? bulkDownloadTO.getSelectedNodes().split(",") : null;
		;
		// if(allNodes == null) return bulkDownloadTO;
		List<ObjectValueTO> students = null;
		long studentCount = 0;
		StringBuilder studentIds = new StringBuilder();
		Set<String> school = new HashSet<String>();
		Set<String> orgClass = new HashSet<String>();
		Set<String> schoolName = new HashSet<String>();
		Set<String> orgClassName = new HashSet<String>();
		BulkDownloadTO schoolClassNode = null;
		if (allNodes != null) {
			for (String node : allNodes) {

				if (node.indexOf("|") != -1) {
					String nodeId = node.substring(0, node.indexOf("|"));
					if ("-1".equals(node.substring(node.indexOf("|") + 1, node.length()))) {
						studentCount++;
						studentIds.append(nodeId).append(",");
						schoolClassNode = inorsDAO.getSchoolClass(nodeId);
						school.add(schoolClassNode.getSchoolNodes());
						schoolName.add(schoolClassNode.getSchool());
						orgClass.add(schoolClassNode.getClassNodes());
						orgClassName.add(schoolClassNode.getOrgClass());
					} else {

						if ("3".equals(node.substring(node.indexOf("|") + 1, node.length()))) {
							// schoolCount++;
							// bulkDownloadTO.setSchoolNodes(bulkDownloadTO.getSchoolNodes() + "," + nodeId);
							school.add(nodeId);
							schoolClassNode = inorsDAO.getNodeName(nodeId);
							schoolName.add(schoolClassNode.getOrgClass());
						}
						if ("4".equals(node.substring(node.indexOf("|") + 1, node.length()))) {
							// classCount++;
							// bulkDownloadTO.setClassNodes(bulkDownloadTO.getClassNodes() + "," + nodeId);
							orgClass.add(nodeId);
							schoolClassNode = inorsDAO.getNodeName(nodeId);
							schoolName.add(schoolClassNode.getOrgClass());
						}
						// get all students
						students = adminDAO.getAllStudents(bulkDownloadTO.getTestAdministration(), nodeId, bulkDownloadTO.getCustomerId(), bulkDownloadTO.getGrade());
						studentCount += (students == null) ? 0 : students.size();
						if (students != null) {
							for (ObjectValueTO obj : students) {
								studentIds.append(obj.getValue()).append(",");
							}
						}
					}
				}
			}
			if (studentIds.length() > 1) {
				// remove last comma
				studentIds.deleteCharAt(studentIds.length() - 1);
			}
			// all school names
			bulkDownloadTO.setSchool(setToString(schoolName));
			// all school node ids
			bulkDownloadTO.setSchoolNodes(setToString(school));
			// all class names
			bulkDownloadTO.setOrgClass(setToString(orgClassName));
			// all class node ids
			bulkDownloadTO.setSchoolNodes(setToString(orgClass));

			bulkDownloadTO.setSchoolCount(school.size());
			bulkDownloadTO.setClassCount(orgClass.size());
			bulkDownloadTO.setStudentCount(studentCount);
			bulkDownloadTO.setStudentBioIds(studentIds.toString());
		}
		return inorsDAO.createJob(bulkDownloadTO);
	}

	/**
	 * Converts Set<String> to comma separated String.
	 * 
	 * @param node
	 * @return
	 */
	private String setToString(Set<String> node) {
		String[] nodes = node.toArray(new String[0]);
		StringBuilder str = new StringBuilder();
		for (String sch : nodes) {
			str.append(sch).append(",");
		}
		if (str.length() > 1) {
			// remove last comma
			str.deleteCharAt(str.length() - 1);
		}
		return str.toString();
	}

	/**
	 * Get all students.
	 * 
	 * @param jobTO
	 * @return
	 */
	public String getAllStudents(BulkDownloadTO jobTO) {
		String[] allNodes = (jobTO != null && jobTO.getSelectedNodes() != null) ? jobTO.getSelectedNodes().split(",") : null;
		;
		if (allNodes == null)
			return "0";
		List<ObjectValueTO> students = null;
		long studentCount = 0, schoolCount = 0, classCount = 0;
		StringBuilder studentIds = new StringBuilder();
		for (String node : allNodes) {

			if (node.indexOf("|") != -1) {
				String nodeId = node.substring(0, node.indexOf("|"));
				if ("-1".equals(node.substring(node.indexOf("|") + 1, node.length()))) {
					studentCount++;
					studentIds.append(nodeId).append(",");
				} else {

					if ("3".equals(node.substring(node.indexOf("|") + 1, node.length()))) {
						schoolCount++;
					}
					if ("4".equals(node.substring(node.indexOf("|") + 1, node.length()))) {
						classCount++;
					}
					// get all students
					students = adminDAO.getAllStudents(jobTO.getTestAdministration(), nodeId, jobTO.getCustomerId(), jobTO.getGrade());
					studentCount += (students == null) ? 0 : students.size();
					if (students != null) {
						for (ObjectValueTO obj : students) {
							studentIds.append(obj.getValue()).append(",");
						}
					}
				}
			}
		}
		if (studentIds.length() > 1) {
			// remove last comma
			studentIds.deleteCharAt(studentIds.length() - 1);
		}
		return studentIds.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.business.IInorsBusiness#getJob(java.lang.String)
	 */
	public BulkDownloadTO getJob(String jobId) {
		return inorsDAO.getJob(jobId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.business.IInorsBusiness#batchPDFDownload(java.lang.String)
	 */
	public void batchPDFDownload(String jobId, String contractName) {
		logger.log(IAppLogger.INFO, "START ================== f r o m  async method --------------- ");
		try {
			batchPDFDownload(jobId, null, contractName);
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Bulk Download Failed for Job Id: " + jobId);
			e.printStackTrace();
		}
		logger.log(IAppLogger.INFO, "END   ================== f r o m  async method --------------- ");
	}

	/**
	 * Process inors file download
	 * 
	 * @param jobId
	 * @param jobTO
	 */
	private void batchPDFDownload(String jobId, BulkDownloadTO jobTO, String contractName) {
		logger.log(IAppLogger.INFO, "Enter: batchPDFDownload()");
		logger.log(IAppLogger.INFO, "contractName: " + contractName);
		try{
		String jobLog = null;
		String jobStatus = IApplicationConstants.JOB_STATUS.IP.toString();
		String fileSize = null;
		JobTrackingTO jobTrackingTO = reportBusiness.getProcessDataGD(jobId, contractName);
		String clobStr = jobTrackingTO != null ? jobTrackingTO.getRequestDetails(): "";
		logger.log(IAppLogger.INFO, "Clob Data is : " + clobStr);
		GroupDownloadTO to = Utils.jsonToObject(clobStr, GroupDownloadTO.class);
		String rootPath = loginDAO.getRootPath(to.getCustomerId(), to.getTestAdministrationVal(), Utils.getContractName());
		Map<String, String> filePaths = new LinkedHashMap<String, String>();
		if (to != null) {
			to.setContractName(contractName);
			String button = to.getButton();
			String fileName = to.getFileName();
			String groupFile = to.getGroupFile();
			String school = to.getSchool();
			String students = to.getStudents();
			String currentUser = to.getUserName();
			logger.log(IAppLogger.INFO, "button: " + button);
			logger.log(IAppLogger.INFO, "fileName: " + fileName);
			logger.log(IAppLogger.INFO, "groupFile: " + groupFile);
			logger.log(IAppLogger.INFO, "school: " + school);
			logger.log(IAppLogger.INFO, "students: " + students);
			logger.log(IAppLogger.INFO, "currentUser: " + currentUser);

			String zipFileName = fileName + "_" + System.currentTimeMillis() +".zip";
			zipFileName = CustomStringUtil.appendString(rootPath, "/GDF/", zipFileName);
			String querySheetFileName = CustomStringUtil.appendString("0-", fileName, "_Querysheet.pdf");
			if ((groupFile != null) && (groupFile.equals(IApplicationConstants.EXTRACT_FILETYPE.ICL.toString()))) {
				querySheetFileName = "000" + querySheetFileName;
			}
			String gdfExpiryTime = propertyLookup.get("gdfExpiryTime");

			logger.log(IAppLogger.INFO, "zipFileName(CP): " + zipFileName);
			logger.log(IAppLogger.INFO, "gdfExpiryTime: " + gdfExpiryTime);

			to.setFileName(zipFileName);
			to.setExtractStartDate(jobTrackingTO.getExtractStartdate());
			to.setGdfExpiryTime(gdfExpiryTime);
			to.setRequestDetails(clobStr);
			/**
			 * The Key of this Map is the Actual File Location and the Value is the system generated Pdf name to be used for that file.
			 */
			Map<String, String> filePathsGD = reportBusiness.getGDFilePaths(to);
			logger.log(IAppLogger.INFO, "filePaths: " + filePaths.size());

			if (!filePathsGD.isEmpty()) {
				try {
					String querySheetAsString = reportBusiness.getRequestSummary(Utils.objectToJson(to), Utils.getContractName());
					FileUtil.createDuplexPdf(CustomStringUtil.appendString(rootPath, "/GDF/", querySheetFileName), querySheetAsString);
					filePaths.put(CustomStringUtil.appendString("/GDF/", querySheetFileName), querySheetFileName);
					filePaths.putAll(filePathsGD);
					if ("CP".equals(button)) {
						/**
						 * For Combined Pdf the Pdf file name is the generated Default Zip File Name
						 */
						String pdfFileName = FileUtil.generateDefaultZipFileName(currentUser, groupFile) + ".pdf";
						pdfFileName = CustomStringUtil.appendString(rootPath, "/GDF/", pdfFileName);
						// Merge Pdf files
						byte[] input = FileUtil.getMergedPdfBytes(new ArrayList<String>(filePaths.keySet()), rootPath);

						// Create Pdf file in disk
						FileUtil.createFile(pdfFileName, input);

						// Create Zip file in disk
						List<String> list = new ArrayList<String>();
						list.add(pdfFileName);
						FileUtil.createZipFile(zipFileName, list);

						// Delete the Pdf file from disk
						logger.log(IAppLogger.INFO, "temp pdf file deleted = " + new File(pdfFileName).delete());

						// fileSize = FileUtil.fileSize(input);
						fileSize = FileUtil.getFileSize(zipFileName);
						logger.log(IAppLogger.INFO, "fileSize=" + fileSize);
						//jobStatus = IApplicationConstants.JOB_STATUS.CO.toString();
						jobLog = "Asynchoronous Combined Pdf";
					} else if ("SP".equals(button)) {
						// TODO : convention implementation
						Long orgNodeId = Long.parseLong(school);
						logger.log(IAppLogger.INFO, "orgNodeId: " + orgNodeId);
						logger.log(IAppLogger.INFO, "zipFileName(SP): " + zipFileName);

						// Create Zip file in disk from all the pdf files
						try {
							FileUtil.createDuplexZipFile(zipFileName, filePaths, rootPath);
							// fileSize = FileUtil.fileSize(zipFileName);
							fileSize = FileUtil.getFileSize(zipFileName);
							logger.log(IAppLogger.INFO, "fileSize=" + fileSize);
							//jobStatus = IApplicationConstants.JOB_STATUS.CO.toString();
							jobLog = "Asynchoronous Separate Pdfs";
						} catch (Exception e) {
							jobStatus = IApplicationConstants.JOB_STATUS.ER.toString();
							jobLog = e.getMessage();
							e.printStackTrace();
						}
					}
					logger.log(IAppLogger.INFO, "Temp QuerySheet file deleted = " + new File(querySheetFileName).delete());
					// Upload File to S3
					try {
						String envString = to.getEnvString().toUpperCase();
						logger.log(IAppLogger.INFO, "envString = " + envString);
						String keyWithFileName = "/" + envString + "/" + zipFileName;
						logger.log(IAppLogger.INFO, "keyWithFileName = " + keyWithFileName);
						String keyWithoutFileName = FileUtil.getDirFromFilePath(keyWithFileName);
						logger.log(IAppLogger.INFO, "keyWithoutFileName = " + keyWithoutFileName);
						File file = new File(zipFileName);
						repositoryService.uploadAsset(keyWithoutFileName, file);
						logger.log(IAppLogger.INFO, "Asset(" + keyWithFileName + ") uploaded successfully");
						// TODO : Delete File from Mount Location
						jobStatus = IApplicationConstants.JOB_STATUS.CO.toString();
					} catch (Exception e) {
						jobStatus = IApplicationConstants.JOB_STATUS.ER.toString();
						jobLog = "Invalid Contract Name or S3 Upload issue";
						logger.log(IAppLogger.WARN, jobLog);
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					jobStatus = IApplicationConstants.JOB_STATUS.ER.toString();
					jobLog = e.getMessage();
					e.printStackTrace();
				} catch (IOException e) {
					jobStatus = IApplicationConstants.JOB_STATUS.ER.toString();
					jobLog = e.getMessage();
					e.printStackTrace();
				} catch (DocumentException e) {
					jobStatus = IApplicationConstants.JOB_STATUS.ER.toString();
					jobLog = e.getMessage();
					e.printStackTrace();
				}
			} else {
				jobStatus = IApplicationConstants.JOB_STATUS.NA.toString();
				jobLog = "No File to download";
				logger.log(IAppLogger.INFO, jobLog);
			}
		} else {
			jobStatus = IApplicationConstants.JOB_STATUS.ER.toString();
			jobLog = "Invalid REQUEST_DETAILS Field";
			logger.log(IAppLogger.WARN, jobLog);
		}
		
		to.setJobLog(jobLog);
		to.setJobStatus(jobStatus);
		if (fileSize == null) {
			fileSize = "0 M";
		}
		to.setFileSize(fileSize.toString());
		to.setJobId(jobId);
		int updateCount = reportBusiness.updateJobTracking(to);
		logger.log(IAppLogger.INFO, "updateCount: " + updateCount);

		// email notification
		if(IApplicationConstants.JOB_STATUS.CO.toString().equals(jobStatus)) {
			notificationMailGD(to.getEmail(), to.getFileName());
		} else {
			logger.log(IAppLogger.INFO, "Notification Mail was Not Sent. jobStatus = " + jobStatus);
		}
		}catch(Exception e){
			int updateCount = reportBusiness.updateJobTrackingStatus(contractName, jobId, IApplicationConstants.JOB_STATUS.ER.toString(), ("Exception in batchPDFDownload: " + e.getMessage() == null ? "" : e.getMessage()));
			logger.log(IAppLogger.INFO, "updateCount: " + updateCount);
			e.printStackTrace();
		}
		logger.log(IAppLogger.INFO, "Exit: batchPDFDownload()");
	}

	@Autowired
	private EmailSender emailSender;
	/**
	 * This method is used to send a notification mail after Group Download
	 * 
	 * @param email
	 */
	private void notificationMailGD(String email, String fileName) {
		logger.log(IAppLogger.INFO, "Enter: notificationMailGD()");
		try {
			String file = fileName;
			if(file != null && file.lastIndexOf(File.separator) != -1) {
				file = file.substring(file.lastIndexOf(File.separator)+1);
				if(file.lastIndexOf("_") != -1) {
					file = file.substring(0, file.lastIndexOf("_"));
					file = CustomStringUtil.appendString(file, ".zip");
				}
				
			}
			Properties prop = new Properties();
			prop.setProperty(IEmailConstants.SMTP_HOST, propertyLookup.get(IEmailConstants.SMTP_HOST));
			prop.setProperty(IEmailConstants.SMTP_PORT, propertyLookup.get(IEmailConstants.SMTP_PORT));
			prop.setProperty("senderMail", propertyLookup.get("senderMail"));
			prop.setProperty("supportEmail", propertyLookup.get("supportEmail"));
			String subject = propertyLookup.get("mail.gd.subject");
			String mailBody = CustomStringUtil.appendString(file, propertyLookup.get("mail.gd.body"));
			logger.log(IAppLogger.INFO, "Email triggered...");
			emailSender.sendMail(prop, email, null, null, subject, mailBody);
			logger.log(IAppLogger.INFO, "Email sent to : " + email);
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Unable to send Email: " + e.getMessage());
			e.printStackTrace();
		}
		logger.log(IAppLogger.INFO, "Exit: notificationMailGD()");
	}

	/**
	 * Process ic files
	 * 
	 * @param jobTO
	 * @param folderLoc
	 * @param students
	 * @param isrFileMap
	 * @param log
	 */
	private void procesICFiles(BulkDownloadTO jobTO, String folderLoc, String[] students, Map<String, String> isrFileMap, StringBuffer log) {
		OutputStream fos = null;
		InputStream is = null;
		try {
			List<String> isrFiles = new ArrayList<String>();
			List<String> studentfiles = new LinkedList<String>();
			List<String> archieveFileNames = new LinkedList<String>();
			if (IApplicationConstants.DOWNLOAD_TYPE_MERGED.equals(jobTO.getRequestType())) {
				// add querysheet file
				isrFiles.add(CustomStringUtil.appendString(folderLoc, jobTO.getQuerysheetFile()));
				// add student IC files
				for (String studentId : students) {
					isrFiles.add(isrFileMap.get(studentId));
				}
				String fileName = CustomStringUtil.appendString(folderLoc, jobTO.getFileName(), "merged.pdf");
				fos = new FileOutputStream(fileName);
				ConcatPdf.concatPDFs(isrFiles, fos, false, folderLoc);
				studentfiles.add(fileName);
				archieveFileNames.add(jobTO.getFileName() + "merged.pdf");
			} else {
				// add querysheet file
				studentfiles.add(CustomStringUtil.appendString(folderLoc, jobTO.getQuerysheetFile()));
				archieveFileNames.add(CustomStringUtil.appendString("0-", jobTO.getQuerysheetFile()));
				for (String studentId : students) {
					// add student IC files
					String isrFile = isrFileMap.get(studentId);
					studentfiles.add(isrFile);
					archieveFileNames.add(CustomStringUtil.appendString("1-", isrFile.substring(isrFile.lastIndexOf(File.separator) + 1)));
				}
			}

			// create archive
			String archiveFileName = CustomStringUtil.appendString(folderLoc, jobTO.getFileName(), Utils.getDateTime(), "IC.zip");
			PdfGenerator.zipit(studentfiles, archieveFileNames, archiveFileName);

			File file = new File(archiveFileName);
			double size = 0;
			DecimalFormat f = new DecimalFormat("##.00");
			if (file.exists()) {
				size = file.length() / 1024 / 1024;
				if (size == 0) {
					size = file.length() / 1024;
					jobTO.setFileSize(f.format(size) + "K");
				} else {
					jobTO.setFileSize(f.format(size) + "M");
				}
			}

			// set status to completed
			jobTO.setStatus(IApplicationConstants.COMPLETED_FLAG);
			jobTO.setLog(log.toString());
			updateJob(jobTO);

		} catch (Exception npe) {
			logger.log(IAppLogger.ERROR, " : FAILED.\n[" + npe.getMessage() + "]\n");
			log.append("Error merging/archiving files : " + npe.getMessage());
			// set status to error
			jobTO.setStatus(IApplicationConstants.ERROR_FLAG);
			jobTO.setLog(log.toString());
			updateStatus(jobTO);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(fos);
		}
	}

	/**
	 * Process Group File Download
	 * 
	 * @param jobTO
	 * @param folderLoc
	 * @param students
	 * @param isrFileMap
	 * @param log
	 */
	private void procesFiles(BulkDownloadTO jobTO, String folderLoc, String[] students, Map<String, String> isrFileMap, StringBuffer log) {
		OutputStream fos = null;
		InputStream is = null;
		try {
			List<String> isrFiles = new ArrayList<String>();
			List<String> studentfiles = new LinkedList<String>();
			List<String> archieveFileNames = new LinkedList<String>();
			if (IApplicationConstants.DOWNLOAD_TYPE_MERGED.equals(jobTO.getRequestType())) {
				// add querysheet file
				isrFiles.add(CustomStringUtil.appendString(folderLoc, jobTO.getQuerysheetFile()));
				// add student ISR files
				for (String studentId : students) {
					isrFiles.add(isrFileMap.get(studentId));
					// add image print files
					if ("3".equals(jobTO.getGroupFile())) {
						// TODO
						/** GET CODE for IMAGE PRINT */
					}
				}
				// merge querysheet file + isr + image print
				String fileName = CustomStringUtil.appendString(folderLoc, jobTO.getFileName(), "merged.pdf");
				fos = new FileOutputStream(fileName);
				ConcatPdf.concatPDFs(isrFiles, fos, false, folderLoc);
				studentfiles.add(fileName);
				archieveFileNames.add(jobTO.getFileName() + "merged.pdf");
			} else {
				// add querysheet file
				studentfiles.add(CustomStringUtil.appendString(folderLoc, jobTO.getQuerysheetFile()));
				archieveFileNames.add(CustomStringUtil.appendString("0-", jobTO.getQuerysheetFile()));
				for (String studentId : students) {
					// add student ISR files
					String isrFile = isrFileMap.get(studentId);
					studentfiles.add(isrFile);
					// add image print files
					if ("3".equals(jobTO.getGroupFile())) {
						archieveFileNames.add(CustomStringUtil.appendString("1a-", isrFile.substring(isrFile.lastIndexOf(File.separator) + 1)));
						// TODO
						/** GET CODE for IMAGE PRINT */
						// studentfiles.add( "imageprint file name with loc" );
						// archieveFileNames.add(CustomStringUtil.appendString("1b-", "image print file name"));
					} else {
						archieveFileNames.add(CustomStringUtil.appendString("1-", isrFile.substring(isrFile.lastIndexOf(File.separator) + 1)));
					}

				}
			}

			// create archive
			String archiveFileName = CustomStringUtil.appendString(folderLoc, jobTO.getFileName(), Utils.getDateTime(), ".zip");
			PdfGenerator.zipit(studentfiles, archieveFileNames, archiveFileName);

			File file = new File(archiveFileName);
			double size = 0;
			DecimalFormat f = new DecimalFormat("##.00");
			if (file.exists()) {
				size = file.length() / 1024 / 1024;
				if (size == 0) {
					size = file.length() / 1024;
					jobTO.setFileSize(f.format(size) + "K");
				} else {
					jobTO.setFileSize(f.format(size) + "M");
				}
			}

			// set status to completed
			jobTO.setStatus(IApplicationConstants.COMPLETED_FLAG);
			jobTO.setLog(log.toString());
			updateJob(jobTO);

		} catch (Exception npe) {
			logger.log(IAppLogger.ERROR, " : FAILED.\n[" + npe.getMessage() + "]\n");
			log.append("Error merging/archiving files : " + npe.getMessage());
			// set status to error
			jobTO.setStatus(IApplicationConstants.ERROR_FLAG);
			jobTO.setLog(log.toString());
			updateStatus(jobTO);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(fos);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.business.IInorsBusiness#updateStatus(com.ctb.prism.inors.transferobject.BulkDownloadTO)
	 */
	public BulkDownloadTO updateStatus(BulkDownloadTO bulkDownloadTO) {
		return inorsDAO.updateStatus(bulkDownloadTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.business.IInorsBusiness#updateJobLog(com.ctb.prism.inors.transferobject.BulkDownloadTO)
	 */
	public BulkDownloadTO updateJobLog(BulkDownloadTO bulkDownloadTO) {
		return inorsDAO.updateJobLog(bulkDownloadTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.business.IInorsBusiness#updateJob(com.ctb.prism.inors.transferobject.BulkDownloadTO)
	 */
	public BulkDownloadTO updateJob(BulkDownloadTO bulkDownloadTO) {
		return inorsDAO.updateJob(bulkDownloadTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.business.IInorsBusiness#updateJobStatusAnsLog(com.ctb.prism.inors.transferobject.BulkDownloadTO)
	 */
	public BulkDownloadTO updateJobStatusAnsLog(BulkDownloadTO bulkDownloadTO) {
		return inorsDAO.updateJobStatusAnsLog(bulkDownloadTO);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileOutputStream fos = null;
		InputStream is = null;
		try {
			URL url1 = new URL("http://localhost:8080/tasc/icDownload.do");

			fos = new FileOutputStream("C:\\temp\\GroupDownload\\testFile.pdf");

			// Contacting the URL
			logger.log(IAppLogger.INFO, "\nConnecting to " + url1.toString() + " ... ");
			HttpURLConnection urlConn = (HttpURLConnection) url1.openConnection();
			urlConn.setRequestMethod("POST");
			urlConn.setRequestProperty("User-Agent", "Mozilla/5.0");
			urlConn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			// Send post request
			urlConn.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(urlConn.getOutputStream());
			wr.writeBytes("reportUrl=/public/ISTEP/Reports/Dummy_Student_Report_files&assessmentId=0&type=pdf&token=0&filter=true&p_studentIds=23740625");
			wr.flush();
			wr.close();

			int responseCode = urlConn.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url1.toString());
			System.out.println("Post parameters : " + "reportUrl=/public/ISTEP/Reports/Dummy_Student_Report_files&assessmentId=0&type=pdf&token=0&filter=true&p_studentIds=23740625");
			System.out.println("Response Code : " + responseCode);

			if (!urlConn.getContentType().equalsIgnoreCase("application/pdf")) {
				logger.log(IAppLogger.ERROR, " : FAILED.\n[Sorry. This is not a PDF.]");
				throw new Exception();
			} else {
				// BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
				IOUtils.copy(is, fos);
			}

		} catch (Exception npe) {
			logger.log(IAppLogger.ERROR, " : FAILED.\n[" + npe.getMessage() + "]\n");
			npe.printStackTrace();
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(fos);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.business.IInorsBusiness#getDownloadData(java.util.Map)
	 */
	public List<? extends BaseTO> getDownloadData(Map<String, String> paramMap) {
		return inorsDAO.getDownloadData(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.business.IInorsBusiness#getDistricts()
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateDistrictGrt(Map<String, String> paramMap) {
		return inorsDAO.populateDistrictGrt(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.business.IInorsBusiness#populateSchool(java.lang.Long)
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateSchoolGrt(Map<String, String> paramMap) {
		return inorsDAO.populateSchoolGrt(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.business.IInorsBusiness#getProductNameById(java.lang.Long)
	 */
	public String getProductNameById(Long productId) {
		return inorsDAO.getProductNameById(productId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.business.IInorsBusiness#getTableData(java.util.Map, java.util.ArrayList)
	 */
	public ArrayList<ArrayList<String>> getTabulerData(Map<String, String> paramMap, ArrayList<String> aliasList, ArrayList<String> headerList) {
		return inorsDAO.getTabulerData(paramMap, aliasList, headerList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.business.IInorsBusiness#getCurrentAdminYear()
	 */
	public String getCurrentAdminYear() {
		return inorsDAO.getCurrentAdminYear();
	}
}
