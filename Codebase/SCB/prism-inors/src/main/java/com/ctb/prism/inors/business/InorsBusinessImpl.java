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
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ctb.prism.admin.dao.IAdminDAO;
import com.ctb.prism.admin.transferobject.ObjectValueTO;
import com.ctb.prism.core.Service.IRepositoryService;
import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.constant.IApplicationConstants.CANDIDATE_RPT_USER_TYPE;
import com.ctb.prism.core.constant.IApplicationConstants.JOB_STATUS;
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

	/* (non-Javadoc)
	 * @see com.ctb.prism.inors.business.IInorsBusiness#getJob(java.lang.String)
	 */
	public com.ctb.prism.core.transferobject.JobTrackingTO getJob(String jobId, String contractName) {
		return inorsDAO.getJob(jobId, contractName);
	}
	
	/* (non-Javadoc)
	 * @see com.ctb.prism.inors.business.IInorsBusiness#updateJob(com.ctb.prism.core.transferobject.JobTrackingTO)
	 */
	public com.ctb.prism.core.transferobject.JobTrackingTO updateJob(com.ctb.prism.core.transferobject.JobTrackingTO bulkDownloadTO) {
		return inorsDAO.updateJob(bulkDownloadTO);
	}
	
	public com.ctb.prism.core.transferobject.JobTrackingTO updateJobStatusAndLog(com.ctb.prism.core.transferobject.JobTrackingTO bulkDownloadTO) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.business.IInorsBusiness#batchPDFDownload(java.lang.String)
	 */
	public void asyncPDFDownload(String jobId, String contractName) {
		try {
			logger.log(IAppLogger.INFO, "START ================== f r o m  async method --------------- ");
			if (IApplicationConstants.CONTRACT_NAME_TASC.equalsIgnoreCase(contractName)) {
				logger.log(IAppLogger.INFO, "=START================ DOWNLOADING CANDIDATE REPORTS ================== JOB ID : " + jobId);
				batchCRPDFDownload(jobId, contractName);
				logger.log(IAppLogger.INFO, "=END================== DOWNLOADING CANDIDATE REPORTS ================== JOB ID : " + jobId);
			} else if (IApplicationConstants.CONTRACT_NAME_INORS.equalsIgnoreCase(contractName)) {
				batchPDFDownload(jobId, contractName);
			} else {
				logger.log(IAppLogger.ERROR, "Invalid Contract Name: " + contractName);
			}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "asyncPDFDownload() Failed for Job Id: " + jobId);
			e.printStackTrace();
		}
		logger.log(IAppLogger.INFO, "END   ================== f r o m  async method --------------- ");
	}

	/**
	 * Process candidate report download
	 * 
	 * @param jobId
	 * @param jobTO
	 */
	private void batchCRPDFDownload(String jobId, String contractName) {
		com.ctb.prism.core.transferobject.JobTrackingTO jobTO = getJob(jobId, contractName); // TODO : Initialize from database
		OutputStream fos = null;
		InputStream is = null;
		StringBuffer log = new StringBuffer();

		// set status to inprogress
		try {
			jobTO.setJobStatus(JOB_STATUS.IP.toString());
			jobTO.setContractName(contractName);
			updateJob(jobTO);

			String folderLoc = CustomStringUtil.appendString(propertyLookup.get("pdfGenPathIC"), File.separator, jobId, File.separator);
			folderLoc = folderLoc.replace("//", "/");
			String[] otherParams = jobTO.getOtherRequestparams().split(",");
			//username is required in candidate report
			jobTO.setUserName(otherParams[3]);
			String userType = (otherParams != null && otherParams.length > 1) ? otherParams[1] : CANDIDATE_RPT_USER_TYPE.REGULAR.toString();
			// if (jobTO.getRequestDetails() == null) jobTO.setRequestDetails("157,158,159,160");
			String[] studentBioIds = (jobTO.getRequestDetails() != null) ? jobTO.getRequestDetails().split(",") : null;

			// split into pieces with a max. size of as defined -
			// jasperreports.properties : CRConcurrentStudSize
			List<String[]> list = splitArray(studentBioIds, (propertyLookup.get("CRConcurrentStudSize") != null) ? Integer.parseInt(propertyLookup.get("CRConcurrentStudSize")) : 50);

			List<String> studentfiles = new LinkedList<String>();
			List<String> archieveFileNames = new LinkedList<String>();
			int count = 0;
			for (String[] arrStudentIds : list) {
				// String[] StudAndFormId = studentBioId.split("\\|");
				logger.log(IAppLogger.INFO, "\nDownloading Candidate Report for " + StringUtils.join(arrStudentIds, ','));
				String tempFileName = (CustomStringUtil.appendString(jobTO.getRequestFilename(), "_", "" + count++, "_", Utils.getDateTime(), ".pdf"));
				logger.log(IAppLogger.INFO, "\n\n-----------------------------"+count+" of "+list.size()+"------------------------------------\n\n");
				String fileName = CustomStringUtil.appendString(folderLoc, tempFileName);
				fileName = fileName.replaceAll("\\\\", "/");
				String folder = FileUtil.getDirFromFilePath(fileName);
				File dir = new File(folder);
				if (!dir.isDirectory()) {
					dir.mkdirs();
					logger.log(IAppLogger.INFO, "Directory created = " + folder);
				} else {
					logger.log(IAppLogger.INFO, "Directory exists = " + folder);
				}
				StringBuffer URLStringBuf = new StringBuffer();
				URLStringBuf.append(propertyLookup.get("bulkDownloadUrl"));
				URLStringBuf.append("icDownload.do?reportUrl=").append(otherParams[0]);
				URLStringBuf.append("&assessmentId=0&type=pdf&token=0&filter=true");
				URLStringBuf.append("&LoggedInUserName=").append(jobTO.getUserName());
				URLStringBuf.append("&LoggedInUserId=").append(jobTO.getUserId());
				URLStringBuf.append("&p_Student_Bio_Id=").append(StringUtils.join(arrStudentIds, ','));
				URLStringBuf.append("&p_Form_Id=").append("-1");
				URLStringBuf.append("&p_Is_Bulk=1&p_Admin_Name=").append(jobTO.getCustomerId()).append("&p_User_Type=").append(userType);
				URLStringBuf.append("&contractName=").append(contractName);

				URL url1 = new URL(URLStringBuf.toString());
				fos = new FileOutputStream(fileName);

				// Contacting the URL
				logger.log(IAppLogger.INFO, "\nConnecting to: " + url1.toString());
				URLConnection urlConn = url1.openConnection();

				// Checking whether the URL contains a PDF
				if (!urlConn.getContentType().equalsIgnoreCase("application/pdf")) {
					logger.log(IAppLogger.ERROR, " : FAILED.\n[Sorry. This is not a PDF.]");
					log.append(CustomStringUtil.appendString("Error getting candidate report for Student Bio Id # ", arrStudentIds.toString()));
				} else {
					// Read the PDF from the URL and save to a local file
					is = url1.openStream();
					IOUtils.copy(is, fos);
					logger.log(IAppLogger.INFO, "\n------------CR PDF Created: " + fileName);
				}
				studentfiles.add(fileName);
				archieveFileNames.add(tempFileName);

				// release resources
				IOUtils.closeQuietly(is);
				IOUtils.closeQuietly(fos);
			}

			// create archive
			String archiveFileName = CustomStringUtil.appendString(folderLoc, jobTO.getRequestFilename(), ".zip");
			PdfGenerator.zipit(studentfiles, archieveFileNames, archiveFileName);

			// calculating file size
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
			
			// Upload File to S3
			String jobStatus_jobLog = moveFileToS3AndCleanDirectory(archiveFileName, otherParams[2]);
			String jobStatus = jobStatus_jobLog.substring(0, jobStatus_jobLog.indexOf('|'));
			String jobLog = jobStatus_jobLog.substring(jobStatus_jobLog.indexOf('|') + 1);

			// set status to completed
			jobTO.setRequestFilename(CustomStringUtil.appendString(otherParams[2], FileUtil.getFileNameFromFilePath(archiveFileName)));
			jobTO.setJobStatus(jobStatus);
			log.append(jobLog);
			jobTO.setJobLog(log.toString());
			updateJob(jobTO);
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(IAppLogger.ERROR, " : FAILED.\n[" + e.getMessage() + "]\n");
			log.append("Error Creating CR files : " + e.getMessage());
			// set status to error
			jobTO.setJobStatus(JOB_STATUS.ER.toString());
			jobTO.setJobLog(log.toString());
			updateJobStatusAndLog(jobTO);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(fos);
		}
	}

	/**
	 * Split array into multiple schemas
	 * 
	 * @param <T>
	 * @param array
	 * @param max
	 * @return
	 */
	public static <T extends Object> List<T[]> splitArray(T[] array, int max) {
		int x = array.length / max;
		int lower = 0;
		int upper = 0;
		List<T[]> list = new ArrayList<T[]>();
		if (array.length == 1) {
			list.add(Arrays.copyOfRange(array, 0, 1));
		} else {
			for (int i = 0; i < x; i++) {
				upper += max;
				list.add(Arrays.copyOfRange(array, lower, upper));
				lower = upper;
			}
			if (upper < array.length - 1) {
				lower = upper;
				upper = array.length;
				list.add(Arrays.copyOfRange(array, lower, upper));
			}
		}
		return list;
	}

	/**
	 * Uploads the file to S3 and then deletes all files from the directory
	 * where the file is.
	 * 
	 * @param zipFileName
	 *            It is the fully qualified file name in disk.
	 * @param s3Location
	 *            It does not contain filename but it starts with environment
	 *            postfix.
	 * @return
	 */
	private String moveFileToS3AndCleanDirectory(String zipFileName, String s3Location) {
		String jobStatus = IApplicationConstants.JOB_STATUS.IP.toString();
		String jobLog = "S3 Upload in progress";
		try {
			zipFileName = zipFileName.replace("//", "/");
			logger.log(IAppLogger.INFO, "zipFileName = " + zipFileName);
			s3Location = s3Location.replace("//", "/");
			logger.log(IAppLogger.INFO, "s3Location = " + s3Location);
			File file = new File(zipFileName);

			// Upload File to S3
			repositoryService.uploadAsset(s3Location, file);
			logger.log(IAppLogger.INFO, "File(" + zipFileName + ") uploaded to S3");

			// Delete File from Mount Location
			String dir = FileUtil.getDirFromFilePath(zipFileName);
			File dirLocation = new File(dir);
			FileUtils.deleteDirectory(dirLocation);
			logger.log(IAppLogger.INFO, "Temp Directory Deleted Successfully: " + dir);

			// Set Job Status and Log
			jobStatus = IApplicationConstants.JOB_STATUS.CO.toString();
			jobLog = "S3 Upload completed successfully";
		} catch (Exception e) {
			jobStatus = IApplicationConstants.JOB_STATUS.ER.toString();
			jobLog = "S3 Upload Issue: " + e.getMessage();
			logger.log(IAppLogger.WARN, jobLog);
			e.printStackTrace();
		}
		return CustomStringUtil.appendString(jobStatus, "|", jobLog);
	}

	/**
	 * @param fileMap
	 *            The Key of this Map is the Actual File Location and the Value
	 *            is the system generated Pdf name to be used for that file.
	 */
	private void saveFilesFromS3ToMountLocation(String tempDirectory, Map<String, String> fileMap, String rootPath) {
		for (String key : fileMap.keySet()) {
			String s3Key = rootPath + key;
			try {
				byte[] assetBytes = repositoryService.getAssetBytes(s3Key);
				logger.log(IAppLogger.INFO, "File read successfully from S3: " + s3Key);
				String fileName = FileUtil.getFileNameFromFilePath(s3Key);
				fileName = tempDirectory + File.separator + fileName;
				fileName = fileName.replace("//", "/");
				FileUtil.createFile(fileName, assetBytes);
				logger.log(IAppLogger.INFO, "File saved successfully from S3: " + fileName);
			} catch (Exception e) {
				logger.log(IAppLogger.WARN, e.getMessage());
				logger.log(IAppLogger.WARN, "Not able to save file from S3: " + s3Key);
			}
		}
	}
	
	/**
	 * Process inors file download
	 * 
	 * @param jobId
	 * @param jobTO
	 */
	private void batchPDFDownload(String jobId, String contractName) {
		logger.log(IAppLogger.INFO, "Enter: batchPDFDownload()");
		logger.log(IAppLogger.INFO, "contractName: " + contractName);
		try{
		String tempDirectory = CustomStringUtil.appendString(propertyLookup.get("pdfGenPathIC"), File.separator, jobId);
		File tempJobDirectory = new File(tempDirectory);
		if (!tempJobDirectory.exists()) {
			logger.log(IAppLogger.INFO,  "Creating directory ... " + tempJobDirectory);
			boolean status = tempJobDirectory.mkdir();
			logger.log(IAppLogger.INFO, tempJobDirectory + " : " + status);
		}
		String jobLog = null;
		String jobStatus = IApplicationConstants.JOB_STATUS.IP.toString();
		String fileSize = null;
		JobTrackingTO jobTrackingTO = reportBusiness.getProcessDataGD(jobId, contractName);
		String clobStr = jobTrackingTO != null ? jobTrackingTO.getRequestDetails(): "";
		logger.log(IAppLogger.INFO, "Clob Data is : " + clobStr);
		GroupDownloadTO to = Utils.jsonToObject(clobStr, GroupDownloadTO.class);
		String rootPath = loginDAO.getRootPath(to.getCustomerId(), to.getTestAdministrationVal(), contractName);
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
			zipFileName = CustomStringUtil.appendString(tempDirectory, File.separator, zipFileName);
			zipFileName = zipFileName.replace("//", "/");
			String querySheetFileName = CustomStringUtil.appendString("0-", fileName, "_Querysheet.pdf");
			if ((groupFile != null) && (groupFile.equals(IApplicationConstants.EXTRACT_FILETYPE.ICL.toString()))) {
				querySheetFileName = "000" + querySheetFileName;
			}
			String gdfExpiryTime = propertyLookup.get("gdfExpiryTime");

			logger.log(IAppLogger.INFO, "zipFileName(CP): " + zipFileName);
			logger.log(IAppLogger.INFO, "gdfExpiryTime: " + gdfExpiryTime);

			String envString = to.getEnvString().toUpperCase();
			if(envString.indexOf("/") > -1) {
				envString = envString.substring(envString.indexOf("/") + 1);
			}
			// logger.log(IAppLogger.INFO, "envString = " + envString);
			String s3Key = envString + File.separator + IApplicationConstants.GDF_S3_LOCATION + FileUtil.getFileNameFromFilePath(zipFileName); // JOB_TRACKING.REQUEST_FILENAME
			s3Key = s3Key.replace("//", "/");
			to.setFileName(s3Key);
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
					String querySheetAsString = reportBusiness.getRequestSummary(Utils.objectToJson(to), contractName);
					String querySheetFile = CustomStringUtil.appendString(tempDirectory, File.separator, querySheetFileName);
					querySheetFile = querySheetFile.replace("//", "/");
					FileUtil.createDuplexPdf(querySheetFile, querySheetAsString);
					filePaths.put(querySheetFileName, querySheetFileName);
					filePaths.putAll(filePathsGD);
					// Save files from s3 to mount path
					saveFilesFromS3ToMountLocation(tempDirectory, filePathsGD, rootPath);
					if ("CP".equals(button)) {
						/**
						 * For Combined Pdf the Pdf file name is the generated Default Zip File Name
						 */
						String pdfFileName = FileUtil.generateDefaultZipFileName(currentUser, groupFile) + ".pdf";
						pdfFileName = CustomStringUtil.appendString(tempDirectory, File.separator, pdfFileName);
						pdfFileName = pdfFileName.replace("//", "/");

						// Merge Pdf files
						byte[] input = FileUtil.getMergedPdfBytesFromTempDir(new ArrayList<String>(filePaths.keySet()), tempDirectory);

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
							FileUtil.createDuplexZipFile(zipFileName, filePaths, tempDirectory);
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
					// logger.log(IAppLogger.INFO, "Temp QuerySheet file deleted = " + new File(querySheetFileName).delete());
					// Upload File to S3
					String s3Location = to.getEnvString().toUpperCase() + File.separator + IApplicationConstants.GDF_S3_LOCATION;
					String jobStatus_jobLog = moveFileToS3AndCleanDirectory(zipFileName, s3Location);
					jobStatus = jobStatus_jobLog.substring(0, jobStatus_jobLog.indexOf('|'));
					jobLog = jobStatus_jobLog.substring(jobStatus_jobLog.indexOf('|') + 1);
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
